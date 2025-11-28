package content.region.kandarin.handlers

import core.api.*
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
import core.game.node.entity.npc.agg.AggressiveBehavior
import core.game.node.entity.npc.agg.AggressiveHandler
import core.game.node.entity.player.Player
import core.game.world.GameWorld
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import org.rs09.consts.NPCs

/*
 * Behavior is based on the following sources:
 * https://www.youtube.com/watch?v=u2A-_ihV_2w (november 2008)
 * https://www.youtube.com/watch?v=O0EIZu7-iys (august 2009)
 * https://runescape.wiki/w/Tortoise?oldid=815524         https://web.archive.org/web/20090308094532/http://runescape.wikia.com/wiki/Tortoise
 *
 * todo fix att, str, def? I'm not sure what is the typical way to calculate these when they are unknown.
 * level 79: HP 101, max hit 12, "low attack but good defence"
 * level 92: HP 121, max hit 12, "low attack but good defence"
 *
 * https://runescape.wiki/w/Gnome_Archer?oldid=949978     https://web.archive.org/web/20090929124104/http://runescape.wikia.com/wiki/Gnome_archer
 * https://runescape.wiki/w/Gnome_Driver?oldid=1235409    https://web.archive.org/web/20090929124109/http://runescape.wikia.com:80/wiki/Gnome_driver
 * https://runescape.wiki/w/Gnome_Mage?oldid=1425756      https://web.archive.org/web/20090928131013/http://runescape.wikia.com:80/wiki/Gnome_mage
 */

@Initializable
class GnomeTortoiseNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return GnomeTortoiseNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.TORTOISE_3808)
    }

    fun spawnGnomes(location: Location, direction: Direction) {

        //todo: sometimes the child spawns or direction is wrong on death. does a move trigger right before it dies?

        var archerLoc = location
        var driverLoc = location
        var mageLoc = location

        // X D X  3x3 turtle, C center
        // M C A  D driver, M mage, A archer
        // X X X

        // If I was smart I could probably do this with vectors, but I'm dumb so just doing the possibilities by hand.
        if(direction == Direction.NORTH) {
            archerLoc = location.transform(1,0,0)
            driverLoc = location.transform(0,1,0)
            mageLoc   = location.transform(-1,0,0)
        }else if(direction == Direction.NORTH_EAST) {
            archerLoc = location.transform(1,-1,0)
            driverLoc = location.transform(1,1,0)
            mageLoc   = location.transform(-1,1,0)
        }else if(direction == Direction.EAST) {
            archerLoc = location.transform(0,-1,0)
            driverLoc = location.transform(1,0,0)
            mageLoc   = location.transform(0,1,0)
        }else if(direction == Direction.SOUTH_EAST) {
            archerLoc = location.transform(-1,-1,0)
            driverLoc = location.transform(1,-1,0)
            mageLoc   = location.transform(1,1,0)
        }else if(direction == Direction.SOUTH) {
            archerLoc = location.transform(-1,0,0)
            driverLoc = location.transform(0,-1,0)
            mageLoc   = location.transform(1,0,0)
        }else if(direction == Direction.SOUTH_WEST) {
            archerLoc = location.transform(-1,1,0)
            driverLoc = location.transform(-1,-1,0)
            mageLoc   = location.transform(1,-1,0)
        }else if(direction == Direction.WEST) {
            archerLoc = location.transform(0,1,0)
            driverLoc = location.transform(-1,0,0)
            mageLoc   = location.transform(0,-1,0)
        }else if(direction == Direction.NORTH_WEST) {
            archerLoc = location.transform(1,1,0)
            driverLoc = location.transform(-1,1,0)
            mageLoc   = location.transform(-1,-1,0)
        }

        val npcArcher = GnomeArcherNPC(NPCs.GNOME_ARCHER_3814, archerLoc)
        npcArcher.sendChat("Argh!")
        npcArcher.init()

        val npcDriver = GnomeDriverNPC(NPCs.GNOME_DRIVER_3815, driverLoc)
        npcDriver.sendChat("Nooooo! Dobbie's dead!")
        npcDriver.init()

        val npcMage = GnomeMageNPC(NPCs.GNOME_MAGE_3816, mageLoc)
        npcMage.sendChat("Kill the infidel!")
        npcMage.init()
    }

    override fun finalizeDeath(killer: Entity?) {
        val turtleLoc = this.centerLocation
        val turtleDir = this.direction
        spawnGnomes(turtleLoc, turtleDir)
        super.finalizeDeath(killer)
        // todo remove this debug if not needed. It's just telling me the "direction" the tortoise dies in so I can verify the direction that the child NPCs should spawn.
        if (killer is Player) {
            killer.debug(direction.toString())
        }
    }
}

//handles the attack switching
class GnomeTortoiseBehavior : NPCBehavior(NPCs.TORTOISE_3808) {

    private val combatHandler = MultiSwingHandler(
        true,
        // per wiki source, melee has a max hit of 12
        SwitchAttack(
            CombatStyle.MELEE.swingHandler,
            Animation(3953, Priority.HIGH)
        ),
        // todo correct the projectile locations (they should originate from the range or mage gnome, not the center). not sure how to do this.
        SwitchAttack(
            CombatStyle.RANGE.swingHandler,
            Animation(3954, Priority.HIGH),
            null,
            null,
            Projectile.create(
                null as Entity?,
                null,
                10, //bronze arrow
                35,
                30,
                10,
                50,
                14,
                255
            )
        ),
        // per wiki source above, spell should be water strike with the sounds of ice barrage
        SwitchAttack(
            CombatStyle.MAGIC.swingHandler,
            Animation(3955, Priority.HIGH),
            null,
            null,
            Projectile.create(
                null as Entity?,
                null,
                94, //water strike
                35,
                30,
                10,
                50,
                14,
                255
            )
        )
    )

    override fun getSwingHandlerOverride(self: NPC, original: CombatSwingHandler): CombatSwingHandler {
        return combatHandler
    }
}

// Handles Gnome Archer behavior
class GnomeArcherNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return GnomeArcherNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GNOME_ARCHER_3814)
    }

    override fun init() {
        super.init()
        this.isRespawn = false
        this.isAggressive = true
        this.aggressiveHandler = AggressiveHandler(this, object : AggressiveBehavior() {
            override fun ignoreCombatLevelDifference(): Boolean {
                return true
            }
        })
    }
}

class GnomeArcherBehavior : NPCBehavior(NPCs.GNOME_ARCHER_3814) {
    override fun onCreation(self: NPC) {
        // stops the entity from instantly moving.
        delayEntity(self, 1)
        setAttribute(self, "despawn-time", GameWorld.ticks + 25)
    }

    override fun tick(self: NPC): Boolean {
        if (!self.inCombat() && (getAttribute(self, "despawn-time", 0) <= GameWorld.ticks))
            self.clear()
        return true
    }
}

// Handles Gnome Mage behavior
class GnomeMageNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return GnomeMageNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GNOME_MAGE_3816)
    }

    override fun init() {
        super.init()
        this.isRespawn = false
        this.isAggressive = true
        this.aggressiveHandler = AggressiveHandler(this, object : AggressiveBehavior() {
            override fun ignoreCombatLevelDifference(): Boolean {
                return true
            }
        })
    }
}

class GnomeMageBehavior : NPCBehavior(NPCs.GNOME_MAGE_3816) {
    override fun onCreation(self: NPC) {
        // stops the entity from instantly moving.
        delayEntity(self, 1)
        setAttribute(self, "despawn-time", GameWorld.ticks + 25)
    }

    override fun tick(self: NPC): Boolean {
        if (!self.inCombat() && (getAttribute(self, "despawn-time", 0) <= GameWorld.ticks))
            self.clear()
        return true
    }
}

// Handles Gnome Driver behavior
class GnomeDriverNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return GnomeDriverNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GNOME_DRIVER_3815)
    }

    override fun init() {
        super.init()
        this.isRespawn = false
        this.isAggressive = true
        this.aggressiveHandler = AggressiveHandler(this, object : AggressiveBehavior() {
            override fun ignoreCombatLevelDifference(): Boolean {
                return true
            }
        })
    }
}

class GnomeDriverBehavior : NPCBehavior(NPCs.GNOME_DRIVER_3815) {
    override fun onCreation(self: NPC) {
        // stops the entity from instantly moving.
        delayEntity(self, 1)
        setAttribute(self, "despawn-time", GameWorld.ticks + 25)
    }

    override fun tick(self: NPC): Boolean {
        if (!self.inCombat() && (getAttribute(self, "despawn-time", 0) <= GameWorld.ticks))
            self.clear()
        return true
    }
}