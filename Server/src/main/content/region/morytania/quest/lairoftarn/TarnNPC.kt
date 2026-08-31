package content.region.morytania.quest.lairoftarn

import content.region.morytania.quest.lairoftarn.LairOfTarn.Companion.ATTR_KILLED_TARN
import core.api.*
import core.game.interaction.QueueStrength
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.CombatSwingHandler
import core.game.node.entity.combat.MultiSwingHandler
import core.game.node.entity.combat.equipment.SwitchAttack
import core.game.node.entity.impl.Animator.Priority
import core.game.node.entity.impl.Projectile
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import core.game.world.map.RegionManager.getLocalPlayers
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery

/**
 * Tarn Razorlor, the boss of The Lair of Tarn Razorlor miniquest.
 */

// tarn attacks with melee and magic
// amusingly, the tarn ghost seems to have an incorrect hitbox: https://youtu.be/4QtJRexA3Xo?si=_Ps3FBb6iCAzuhmc&t=148

@Initializable
class TarnNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {

    // anims and gfxs for Tarn
    companion object {
        const val MUTANT_DEATH_ANIM = 5619
        const val MUTANT_MELEE_ATT = 5617
        const val MUTANT_MAGE_ATT = 5613
        const val MUTANT_SPELL_PROJ = 1013

        const val GHOST_DEATH = 9055
        const val GHOST_DEF = 404
        const val GHOST_MELEE = 422
    }

    var isTransformed = false

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return TarnNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MUTANT_TARN_5421)
    }

    // TODO: this should be removed if/when NPC stacking is properly fixed (ref gitlab issue #2268)
    override fun shouldPreventStacking(mover: Entity?): Boolean {
        return mover !is Player
    }

    // transform to ghost tarn
    override fun startDeath(killer: Entity?) {
        if (!isTransformed) {
            isTransformed = true
            val player = killer as? Player ?: return super.startDeath(killer)
            val duration = animationDuration(Animation(MUTANT_DEATH_ANIM))

            properties.combatPulse.stop()
            player.properties.combatPulse.stop()
            impactHandler.disabledTicks = duration

            lock(this, duration)
            pulseManager.clear()
            walkingQueue.reset()

            sendChat(this, "Ha! I can't be slain that easily, fool!")
            animate(this, MUTANT_DEATH_ANIM, true)
            fullRestore()

            queueScript(this, duration, QueueStrength.SOFT) { _ ->
                transform(NPCs.TARN_5420)
                animator.reset()
                properties.combatPulse.restart()
                properties.defenceAnimation.id = GHOST_DEF
                properties.deathAnimation.id = GHOST_DEATH
                attack(player)
                return@queueScript stopExecuting(this)
            }
            return
        }
        super.startDeath(killer)
    }
}

class TarnNPCBehavior : NPCBehavior(NPCs.MUTANT_TARN_5421) {

    // chance to spawn the animated steel armours
    override fun tick(self: NPC): Boolean {

        val player = getLocalPlayers(self.location).firstOrNull()
        val base = getAttribute(self, LairOfTarn.ATTR_REGION_BASE, Location.create(3136, 4608, 0))

        // instanced coords for each armour
        val offsets = listOf(
            49 to 13, // set 1
            49 to 10, // set 2
            53 to 11  // set 3
        )

        for ((x, y) in offsets) {
            // get the armour
            val armour = getScenery(base.transform(x, y, 0)) ?: continue

            // if the player is close to the armour
            if (player != null) {
                if (armour.location.withinDistance(player.location, 2) && RandomFunction.roll(5)) {
                    // remove the scenery
                    removeScenery(armour)

                    // create the NPC
                    val steel = NPC.create(NPCs.ANIMATED_STEEL_ARMOUR_5382, armour.location)
                    steel.init()
                    steel.isRespawn = false
                    steel.isAggressive = true
                    steel.attack(player)
                }
            }
        }

        return super.tick(self)
    }

    override fun onDeathFinished(self: NPC, killer: Entity) {

        // finish lair of tarn miniquest
        if (killer is Player) {
            setAttribute(killer, ATTR_KILLED_TARN, true)
            sendMessage(killer, "Killing Tarn Razorlor has greatly improved your Slayer knowledge.")
            rewardXP(killer, Skills.SLAYER, 5000.0)
            sendMessage(killer, "You gain 5000 Slayer XP.")

            // spawn the diary in the instanced region
            val base = getAttribute(self, LairOfTarn.ATTR_REGION_BASE, Location.create(3136, 4608, 0))
            produceGroundItem(killer, Items.TARNS_DIARY_10587, 1, base.transform(48, 26, 0))
        }

        // open the door
        val base = getAttribute(self, LairOfTarn.ATTR_REGION_BASE, Location.create(3136, 4608, 0))
        val door = getScenery(base.transform(50, 19, 0))
        if (door != null) replaceScenery(door, Scenery.PASSAGEWAY_20572, -1)

        super.onDeathFinished(self, killer)
    }

    // tarn uses melee and magic. I hate our combat code.
    private val mutantCombatHandler = MultiSwingHandler(
        true,
        SwitchAttack(
            CombatStyle.MELEE.swingHandler,
            Animation(TarnNPC.MUTANT_MELEE_ATT, Priority.HIGH)
        ),
        SwitchAttack(
            CombatStyle.MAGIC.swingHandler,
            Animation(TarnNPC.MUTANT_MAGE_ATT, Priority.HIGH),
            null,
            null,
            Projectile.create(
                null as Entity?,
                null,
                TarnNPC.MUTANT_SPELL_PROJ,
                15,
                15,
                10,
                50,
                14,
                255
            )
        )
    )

    // switch combat for ghost
    private val ghostCombatHandler = MultiSwingHandler(
        true,
        SwitchAttack(
            CombatStyle.MELEE.swingHandler,
            Animation(TarnNPC.GHOST_MELEE, Priority.HIGH)
        )
    )

    override fun getSwingHandlerOverride(self: NPC, original: CombatSwingHandler): CombatSwingHandler {
        val tarn = self as TarnNPC
        return if (tarn.isTransformed) {
            ghostCombatHandler
        } else {
            mutantCombatHandler
        }
    }
}