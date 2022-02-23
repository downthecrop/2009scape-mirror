import java.util.concurrent.TimeUnit;

import api.*
import core.cache.def.impl.NPCDefinition
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.impl.ForceMovement
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import rs09.game.interaction.InteractionListener
import rs09.game.world.GameWorld

/*@Initializable
class HunterPitfall : OptionHandler() {
    //19227, 
    val graahkPitIds = intArrayOf(19227)//, 19268,19267,19266,19264,19265)
    val GRAAHK_ID = 5105
    val hunterReq = 41
    override fun handle(player: Player?, node: Node?, option: String?): Boolean {
        node ?: return true
        player ?: return true
        player.sendMessage("Hello from HunterPitfall: ${option}")
        when(option) {
            "tease" -> {
                (node as Entity).attack(player)
            }
        }
        return true
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        for(graahkPit in graahkPitIds) {
            SceneryDefinition.forId(graahkPit).handlers["option:trap"] = this
        }
        //NPCDefinition.forId(GRAAHK_ID).handlers["option:tease"] = this
        return this
    }
}*/

val LARUPIA_IDS: IntArray = intArrayOf(NPCs.SPINED_LARUPIA_5104)
val GRAAHK_IDS: IntArray = intArrayOf(NPCs.HORNED_GRAAHK_5105, NPCs.HORNED_GRAAHK_5106, NPCs.HORNED_GRAAHK_5107, NPCs.HORNED_GRAAHK_5108)
val KYATT_IDS: IntArray = intArrayOf(NPCs.SABRE_TOOTHED_KYATT_5103, NPCs.SABRE_TOOTHED_KYATT_7497)
val BEAST_IDS: IntArray = intArrayOf(*LARUPIA_IDS, *GRAAHK_IDS, *KYATT_IDS)
val HUNTER_REQS = hashMapOf(
    "Spined larupia" to 31,
    "Horned graahk" to 41,
    "Sabre-toothed kyatt" to 55,
    )
//val pitVarpOffsets = hashMapOf( 19264 to 3, 19265 to 6, 19266 to 9, 19267 to 12, 19268 to 15,)
data class Pit(val varpId: Int, val varpOffset: Int, val horizontal: Boolean)
val pitVarps = hashMapOf(
    // Larupia pits (the duplicate 24 is likely authentic)
    Location.create(2565,2888) to Pit(917, 27, true),
    Location.create(2556,2893) to Pit(917, 18, false),
    Location.create(2552,2904) to Pit(917, 24, true),
    Location.create(2543,2908) to Pit(917, 21, false),
    Location.create(2538,2899) to Pit(917, 24, true),
    // Kyatt pits
    Location.create(2700,3795) to Pit(917, 0, true),
    Location.create(2700,3785) to Pit(917, 3, false),
    Location.create(2706,3789) to Pit(917, 6, false),
    Location.create(2730,3791) to Pit(917, 9, true),
    Location.create(2737,3784) to Pit(917, 12, true),
    Location.create(2730,3780) to Pit(917, 15, false),
    // Graahk pits
    Location.create(2766,3010) to Pit(918, 3, false),
    Location.create(2762,3005) to Pit(918, 6, false),
    Location.create(2771,3004) to Pit(918, 9, true),
    Location.create(2777,3001) to Pit(918, 12, false),
    Location.create(2784,3001) to Pit(918, 15, true),
    )
/*val pitJumpSpots = hashMapOf(
    Location.create(2766,3010) to hashMapOf(
        Location.create(2766,3009) to Direction.NORTH,
        Location.create(2767,3009) to Direction.NORTH,
        Location.create(2766,3012) to Direction.SOUTH,
        Location.create(2767,3012) to Direction.SOUTH,
    ),
    Location.create(2762,3005) to hashMapOf(
        Location.create(2762,3004) to Direction.NORTH,
        Location.create(2763,3004) to Direction.NORTH,
        Location.create(2762,3007) to Direction.SOUTH,
        Location.create(2763,3007) to Direction.SOUTH,
    ),
    Location.create(2771,3004) to hashMapOf(
        Location.create(2770,3004) to Direction.EAST,
        Location.create(2770,3005) to Direction.EAST,
        Location.create(2773,3004) to Direction.WEST,
        Location.create(2773,3005) to Direction.WEST,
    ),
    Location.create(2777,3001) to hashMapOf(
        Location.create(2777,3000) to Direction.NORTH,
        Location.create(2778,3000) to Direction.NORTH,
        Location.create(2777,3003) to Direction.SOUTH,
        Location.create(2778,3003) to Direction.SOUTH,
    ),
    Location.create(2784,3001) to hashMapOf(
        Location.create(2783,3002) to Direction.EAST,
        Location.create(2783,3001) to Direction.EAST,
        Location.create(2786,3002) to Direction.WEST,
        Location.create(2786,3001) to Direction.WEST,
    ),
    )*/
fun pitJumpSpots(loc: Location): HashMap<Location, Direction>? {
    val pit = pitVarps[loc] ?: return null
    if(pit.horizontal) {
        return hashMapOf(
            loc.transform(-1, 0, 0) to Direction.EAST,
            loc.transform(-1, 1, 0) to Direction.EAST,
            loc.transform(2, 0, 0) to Direction.WEST,
            loc.transform(2, 1, 0) to Direction.WEST,
        )
    } else {
        return hashMapOf(
            loc.transform(0, -1, 0) to Direction.NORTH,
            loc.transform(1, -1, 0) to Direction.NORTH,
            loc.transform(0, 2, 0) to Direction.SOUTH,
            loc.transform(1, 2, 0) to Direction.SOUTH,
        )
    }
}

val KNIFE = Item(Items.KNIFE_946)
val TEASING_STICK = Item(Items.TEASING_STICK_10029)
val LOGS = Item(Items.LOGS_1511)
    
val PIT = 19227
val SPIKED_PIT = 19228
val GRAAHK_PIT = 19231
val LARUPIA_PIT = 19232
val KYATT_PIT = 19233

class PitfallListeners : InteractionListener() {

    override fun defineListeners() {
        setDest(SCENERY, intArrayOf(PIT, SPIKED_PIT, LARUPIA_PIT, GRAAHK_PIT, KYATT_PIT), "trap", "jump", "dismantle") { player, node ->
            val pit = node as Scenery
            val src = player.getLocation()
            var dst = pit.getLocation()
            val locs = pitJumpSpots(dst)
            if(locs != null) {
                for(loc in locs.keys) {
                    if(src.getDistance(loc) <= src.getDistance(dst)) {
                        dst = loc
                    }
                }
            } else {
                if(player is Player) {
                    player.sendMessage("Error: Unimplemented pit at ${pit.location}")
                }
            }
            return@setDest dst
        }
        on(PIT, SCENERY, "trap") { player, node ->
            val pit = node as Scenery;
            // TODO: check hunter level, remove logs
            if(player.skills.getLevel(Skills.HUNTER) < 31) {
                player.sendMessage("You need a hunter level of 31 to set a pitfall trap.")
                return@on true
            }

            val maxTraps = player.hunterManager.maximumTraps
            if(player.getAttribute("pitfall:count", 0) >= maxTraps) {
                player.sendMessage("You can't set up more than ${maxTraps} pitfall traps at your hunter level.")
                return@on true
            }
            player.incrementAttribute("pitfall:count", 1)

            if(!player.inventory.containsItem(KNIFE) || !player.inventory.remove(LOGS)) {
                player.sendMessage("You need some logs and a knife to set a pitfall trap.")
                return@on true
            }
            
            player.setAttribute("pitfall:timestamp:${pit.location.x}:${pit.location.y}", System.currentTimeMillis())
            setPitState(player, pit.location, 1)
            val collapsePulse = object : Pulse(201, player) {
                override fun pulse(): Boolean {
                    val oldTime = player.getAttribute("pitfall:timestamp:${pit.location.x}:${pit.location.y}", System.currentTimeMillis())
                    if(System.currentTimeMillis() - oldTime >= TimeUnit.MINUTES.toMillis(2)) {
                        player.sendMessage("Your pitfall trap has collapsed.")
                        setPitState(player, pit.location, 0)
                        player.incrementAttribute("pitfall:count", -1)
                    }
                    return true
                }
            }
            GameWorld.Pulser.submit(collapsePulse)
            return@on true
        }
        on(SPIKED_PIT, SCENERY, "jump") { player, node ->
            val pit = node as Scenery;
            val src = player.getLocation()
            val dir = pitJumpSpots(pit.getLocation())!![src]
            if(dir != null) {
                val dst = src.transform(dir, 3)
                ForceMovement.run(player, src, dst, ForceMovement.WALK_ANIMATION, Animation(1603), dir, 16);
                val pitfall_npc: Entity? = player.getAttribute("pitfall_npc", null)
                if(pitfall_npc != null && pitfall_npc.getLocation().getDistance(src) < 3.0) {
                    val last_pit_loc: Location? = pitfall_npc.getAttribute("last_pit_loc", null)
                    if(last_pit_loc == pit.location) {
                        player.sendMessage("The ${pitfall_npc.name.toLowerCase()} won't jump the same pit twice in a row.")
                        return@on true
                    }
                    // TODO: what are the actual probabilities of a graahk jumping over a pit?
                    val chance = RandomFunction.getSkillSuccessChance(50.0, 100.0, player.skills.getLevel(Skills.HUNTER))
                    if(RandomFunction.random(0.0, 100.0) < chance) {
                        //ForceMovement.run(pitfall_npc, pitfall_npc.getLocation(), pit.getLocation(), ForceMovement.WALK_ANIMATION, Animation(ANIM), dir, 8);
                        //pitfall_npc.setLocation(pit.getLocation());
                        //pitfall_npc.walkingQueue.addPath(pit.location.x, pit.location.y);
                        teleport(pitfall_npc, pit.location)
                        pitfall_npc.startDeath(null)
                        player.removeAttribute("pitfall:timestamp:${pit.location.x}:${pit.location.y}")
                        player.incrementAttribute("pitfall:count", -1)
                        setPitState(player, pit.location, 3)
                        //pitfall_npc.animate(Animation(5234))
                    } else {
                        //ForceMovement.run(pitfall_npc, pitfall_npc.getLocation(), dst, ForceMovement.WALK_ANIMATION, Animation(ANIM), dir, 8);
                        //pitfall_npc.walkingQueue.addPath(npcdst.x, npcdst.y)
                        val npcdst = dst.transform(dir, if(dir == Direction.SOUTH || dir == Direction.WEST) 1 else 0)
                        teleport(pitfall_npc, npcdst)
                        pitfall_npc.animate(Animation(5232, Priority.HIGH))
                        pitfall_npc.attack(player)
                        pitfall_npc.setAttribute("last_pit_loc", pit.location)
                    }
                }
            }
            return@on true
        }
        on(SPIKED_PIT, SCENERY, "dismantle") { player, node ->
            val pit = node as Scenery;
            player.removeAttribute("pitfall:timestamp:${pit.location.x}:${pit.location.y}")
            player.incrementAttribute("pitfall:count", -1)
            setPitState(player, pit.location, 0)
            return@on true
        }
        on(LARUPIA_PIT, SCENERY, "dismantle") { player, node ->
            lootCorpse(player, node as Scenery, 180.0, Items.LARUPIA_FUR_10095, Items.TATTY_LARUPIA_FUR_10093)
            return@on true
        }
        on(GRAAHK_PIT, SCENERY, "dismantle") { player, node ->
            lootCorpse(player, node as Scenery, 240.0, Items.GRAAHK_FUR_10099, Items.TATTY_GRAAHK_FUR_10097)
            return@on true
        }
        on(KYATT_PIT, SCENERY, "dismantle") { player, node ->
            lootCorpse(player, node as Scenery, 300.0, Items.KYATT_FUR_10103, Items.TATTY_KYATT_FUR_10101)
            return@on true
        }
        on(BEAST_IDS, NPC, "tease") { player, node ->
            val entity = node as Entity
            val hunterReq = HUNTER_REQS[entity.name]!!
            if(player.skills.getLevel(Skills.HUNTER) < hunterReq) {
                player.sendMessage("You need a hunter level of ${hunterReq} to hunt ${entity.name.toLowerCase()}s.")
                return@on true
            }
            if(!player.inventory.containsItem(TEASING_STICK)) {
                player.sendMessage("You need a teasing stick to hunt ${entity.name.toLowerCase()}s.")
                return@on true
            }
            entity.attack(player)
            player.setAttribute("pitfall_npc", entity)
            return@on true
        }
    }

    fun lootCorpse(player: Player, pit: Scenery, xp: Double, goodFur: Int, badFur: Int) {
        if(player.inventory.freeSlots() < 2) {
            player.sendMessage("You don't have enough inventory space. You need 2 more free slots.");   
            return
        }
        setPitState(player, pit.location, 0)
        player.getSkills().addExperience(Skills.HUNTER, xp, true);
        player.inventory.add(Item(Items.BIG_BONES_532))
        // TODO: what's the actual probability of tatty vs perfect fur?
        val chance = RandomFunction.getSkillSuccessChance(50.0, 100.0, player.skills.getLevel(Skills.HUNTER))
        if(RandomFunction.random(0.0, 100.0) < chance) {
            player.inventory.add(Item(goodFur))
        } else {
            player.inventory.add(Item(badFur))
        }
    }

    fun setPitState(player: Player, loc: Location, state: Int) {
        val pit = pitVarps[loc]!!
        player.varpManager.get(pit.varpId).setVarbit(pit.varpOffset, state).send(player)
    }
}

@Initializable
class PitfallNPC : AbstractNPC {
    constructor() : super(NPCs.HORNED_GRAAHK_5105, null, true) {}
    private constructor(id: Int, location: Location) : super(id, location) {}
    override fun construct(id: Int, location: Location, vararg objects: Any?): AbstractNPC {
        return PitfallNPC(id, location)
    }

    init {
        walkRadius = 22
    }

    override fun getIds(): IntArray {
        return BEAST_IDS
    }

    override fun isAttackable(entity: Entity, style: CombatStyle, message: Boolean): Boolean {
        return false
    }

    override fun isIgnoreAttackRestrictions(victim: Entity): Boolean {
        return victim is Player
    }
}
