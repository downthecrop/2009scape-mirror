package rs09.game.content.zone

import api.*
import org.rs09.consts.Items
import rs09.game.interaction.InteractionListener
import core.game.content.global.action.ClimbActionHandler
import core.game.node.entity.skill.Skills
import core.game.node.entity.skill.agility.AgilityHandler
import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatSpell
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.node.entity.state.EntityState
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.system.task.Pulse
import rs09.game.world.GameWorld
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneBuilder
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.tools.RandomFunction

/**
 * The herb items.
 */
val SINISTER_CHEST_HERBS = arrayOf(Item(205, 2), Item(207, 3), Item(209), Item(211), Item(213), Item(219))

public class YanilleAgilityDungeonListeners : InteractionListener() {
    override fun defineListeners() {
        ZoneBuilder.configure(YanilleAgilityDungeon());

        on(1728, SCENERY, "climb-down") { player, _ -> 
			player.teleport(Location(2620, 9565, 0))
            return@on true
        }
        on(1729, SCENERY, "climb-up") { player, _ ->
            player.teleport(Location(2620, 9496, 0))
            return@on true
        }
        on(2316, SCENERY, "climb-up") { player, _ ->
            player.teleport(Location.create(2569, 9525, 0))
            return@on true
        }
        on(intArrayOf(2318, 2317), SCENERY, "climb-up", "climb-down") { player, target ->
            if (player.skills.getLevel(Skills.AGILITY) < 67) {
                player.dialogueInterpreter.sendDialogue("You need an Agility level of at least 67 in order to do this.")
                return@on true
            }
            val loc = if (target.id == 2317) { Location(2615, 9503, 0) } else { Location(2617, 9572, 0) }
            ClimbActionHandler.climb(player, if(target.id == 2317) { ClimbActionHandler.CLIMB_UP } else { ClimbActionHandler.CLIMB_DOWN }, loc)
            player.sendMessage("You climb " + if(target.id == 2317) { "up" } else { "down" } + " the pile of rubble...")
            player.skills.addExperience(Skills.AGILITY, 5.5, true)
            return@on true
        }
        on(intArrayOf(35969, 2303), SCENERY, "walk-across") { player, target ->
            handleBalancingLedge(player, target.asScenery())
            return@on true
        }
        on(377, SCENERY, "open") { player, target ->
            if (!player.inventory.contains(Items.SINISTER_KEY_993, 1)) {
                player.sendMessage("The chest is locked.")
            } else {
                if (player.getInventory().freeSlots() == 0) {
                    player.sendMessage("You don't have enough inventory space.");
                    return@on true
                }
                player.lock(1)
                if(player.inventory.remove(Item(Items.SINISTER_KEY_993, 1))) {
                    player.sendMessages("You unlock the chest with your key...", "A foul gas seeps from the chest");
                    player.getStateManager().register(EntityState.POISONED, true, 28, player);
                    for(item in SINISTER_CHEST_HERBS) {
                        addItemOrDrop(player, item.id, item.amount)
                    }
                    SceneryBuilder.replace(target.asScenery(), target.asScenery().transform(target.id + 1), 5);
                }
            }
            return@on true
        }
        on(378, SCENERY, "search") { player, _ ->
            player.sendMessage("The chest is empty.")
            return@on true
        }
        on(378, SCENERY, "shut") { player, _ ->
            return@on true
        }

    }

	/**
	 * Handles the balancing ledge.
	 * @param player the player.
	 * @param object the object.
	 */
	fun handleBalancingLedge(player: Player, scenery: Scenery) {
		if (player.skills.getLevel(Skills.AGILITY) < 40) {
			player.getDialogueInterpreter().sendDialogue("You need an Agility level of at least 40 in order to do this.");
			return;
		}
		val dir = Direction.getLogicalDirection(player.location, scenery.location)
		val diff = if(player.location.y == 9512) { 0 } else { 1 }
		var end = scenery.location;
		var xp = 0.0;
		if (AgilityHandler.hasFailed(player, 40, 0.01)) {
			player.lock(3)
			GameWorld.Pulser.submit(object : Pulse(2, player) {
				override public fun pulse(): Boolean {
					AgilityHandler.fail(player, 1, Location(2572, 9570, 0), Animation.create(761 - diff), RandomFunction.random(1, 3), "You lost your balance!")
					return true
				}
			})
		} else {
			xp = 22.5;
			end = scenery.location.transform(dir.getStepX() * 7, dir.getStepY() * 7, 0);
		}
		AgilityHandler.walk(player, -1, player.location, end, Animation.create(157 - diff), xp, null);
	}

}

/**
 * Handles the salarin twisted npc.
 * @author Vexia
 */
@Initializable
public class SalarinTwistedNPC : AbstractNPC {

    /**
     * The spell ids.
     */
    val SPELL_IDS = intArrayOf( 1, 4, 6, 8 )

    /**
     * Constructs a new {@Code SalarinTwistedNPC} {@Code
     * Object}
     */
    public constructor(): super(-1, null) {}

    /**
     * Constructs a new {@Code SalarinTwistedNPC} {@Code
     * Object}
     * @param id the id.
     * @param location the location.
     */
    public constructor(id: Int, location: Location) : super(id, location) {
        super.setAggressive(true)
    }

    override public fun construct(id: Int, location: Location, vararg objects: Any?): AbstractNPC {
        return SalarinTwistedNPC(id, location)
    }

    override fun checkImpact(state: BattleState) {
        if (state.style != CombatStyle.MAGIC) {
            state.neutralizeHits()
            return
        }
        if (state.spell  == null) {
            state.neutralizeHits()
            return
        }
        val spell = state.spell
        for (id in SPELL_IDS) {
            if (id == spell.spellId) {
                state.estimatedHit = state.maximumHit
                return
            }
        }
        state.neutralizeHits()
    }

    override public fun getIds(): IntArray {
        return intArrayOf(205);
    }

}

/**
 * Handles the yanille agility dungeon.
 * @author Vexia
 */
public class YanilleAgilityDungeon : MapZone("Yanille agility", true) {
	override public fun configure() {
		register(ZoneBorders(2544, 9481, 2631, 9587))
	}
}
