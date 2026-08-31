package content.region.morytania.quest.lairoftarn

import content.region.morytania.quest.lairoftarn.LairOfTarn.Companion.ATTR_KILLED_TARN
import content.region.morytania.quest.lairoftarn.LairOfTarn.Companion.ATTR_LOG_20904_DISABLED
import content.region.morytania.quest.lairoftarn.LairOfTarn.Companion.ATTR_LOG_20905_DISABLED
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.impl.ForceMovement.direction
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.system.command.sets.MusicCommandSet
import core.game.world.map.Location
import core.game.world.map.build.DynamicRegion
import org.rs09.consts.Animations
import org.rs09.consts.Items
import org.rs09.consts.Music
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery
import kotlin.math.abs

/**
 * Listeners for the Lair of Tarn Razorlor miniquest
 */

class LairOfTarnListeners : InteractionListener {

    // This companion object is mostly for the enormous amount of scenery in this area.
    companion object {

        // this variable records whatever the last trap was that you disabled, and doesn't hurt you if you step on it.
        var lastTrap = location(0, 0, 0)

        val pillars =
            intArrayOf(
                Scenery.PILLAR_20872, Scenery.PILLAR_20873, Scenery.PILLAR_20874, Scenery.PILLAR_20875,
                Scenery.PILLAR_20876, Scenery.PILLAR_20877, Scenery.PILLAR_20878, Scenery.PILLAR_20879,
                Scenery.PILLAR_20880, Scenery.PILLAR_20881, Scenery.PILLAR_20882, Scenery.PILLAR_20883,
                Scenery.PILLAR_20884, Scenery.PILLAR_20885, Scenery.PILLAR_20886, Scenery.PILLAR_20887,
                Scenery.PILLAR_20888, Scenery.PILLAR_20889,

                Scenery.LEDGE_20890, Scenery.LEDGE_20891, Scenery.LEDGE_20892, Scenery.LEDGE_20893,
                Scenery.LEDGE_20894, Scenery.LEDGE_20895, Scenery.LEDGE_20896, Scenery.LEDGE_20897,
                Scenery.LEDGE_20898, Scenery.LEDGE_20899, Scenery.LEDGE_20900, Scenery.LEDGE_20901
            )

        val passageways =
            intArrayOf(
                Scenery.PASSAGEWAY_20575, Scenery.PASSAGEWAY_20654, Scenery.PASSAGEWAY_20849, Scenery.PASSAGEWAY_20850,
                Scenery.PASSAGEWAY_20851, Scenery.PASSAGEWAY_20853, Scenery.PASSAGEWAY_20854, Scenery.PASSAGEWAY_20721,
                Scenery.PASSAGEWAY_20845, Scenery.PASSAGEWAY_20846, Scenery.PASSAGEWAY_20855, Scenery.PASSAGEWAY_20838,
                Scenery.PASSAGEWAY_20856, Scenery.PASSAGEWAY_20837, Scenery.PASSAGEWAY_20836, Scenery.PASSAGEWAY_20857,
                Scenery.PASSAGEWAY_20864, Scenery.PASSAGEWAY_20863, Scenery.PASSAGEWAY_20858, Scenery.PASSAGEWAY_20859,
                Scenery.PASSAGEWAY_20865, Scenery.PASSAGEWAY_20866, Scenery.PASSAGEWAY_20860, Scenery.PASSAGEWAY_20861,
                Scenery.PASSAGEWAY_20862, Scenery.PASSAGEWAY_20867, Scenery.PASSAGEWAY_20868, Scenery.PASSAGEWAY_20871,
                Scenery.PASSAGEWAY_20465, Scenery.PASSAGEWAY_20847, Scenery.PASSAGEWAY_20848, Scenery.PASSAGEWAY_20821,
                Scenery.PASSAGEWAY_20822, Scenery.PASSAGEWAY_20823, Scenery.PASSAGEWAY_20824, Scenery.PASSAGEWAY_20829,
                Scenery.PASSAGEWAY_20830, Scenery.PASSAGEWAY_20831, Scenery.PASSAGEWAY_20832, Scenery.PASSAGEWAY_20833,
                Scenery.PASSAGEWAY_20834, Scenery.PASSAGEWAY_20835, Scenery.PASSAGEWAY_20466, Scenery.PASSAGEWAY_20572,
                Scenery.PASSAGEWAY_20573, Scenery.PASSAGEWAY_20467, Scenery.PASSAGEWAY_20844, Scenery.PASSAGEWAY_20842,
                Scenery.PASSAGEWAY_20841, Scenery.PASSAGEWAY_20843
            )

        val walls =
            intArrayOf(
                Scenery.WALL_20920, Scenery.WALL_20921, Scenery.WALL_20922, Scenery.WALL_20923,
                Scenery.WALL_20924, Scenery.WALL_20925, Scenery.WALL_20926
            )

        val stairs = intArrayOf(Scenery.STAIRS_20619, Scenery.STAIRS_20684)

        val floors = intArrayOf(Scenery.FLOOR_20915, Scenery.FLOOR_20916)

        const val HUMAN_JUMP = 5355
        const val HUMAN_DISARM_TRAP = 2286
        const val ENCHANT_SALVE_AMULET = 5637
        const val ENCHANT_SALVE_AMULET_GFX = 1012
    }

    // This allows interaction with pillars and ledges from a distance.
    override fun defineDestinationOverrides() {
        setDest(IntType.SCENERY, pillars, "Jump-to") { player, node ->
            val locX = player.location.x
            val locY = player.location.y

            if (abs(locX - node.location.x) == 2
                || abs(locY - node.location.y) == 2
            ) {
                return@setDest player.location
            }
            return@setDest node.location
        }
    }

    override fun defineListeners() {

        /* * * * * * * * * * * * * * * * * * * * * *
         *
         *   LAIR OF TARN (AREA) RELATED LISTENERS
         *
         * * * * * * * * * * * * * * * * * * * * * */

        on(passageways, IntType.SCENERY, "Enter") { player, node ->
            val locX = player.location.x
            val locY = player.location.y
            val locZ = player.location.z

            when (node.id) {
                // door out of the instanced fight in tarn's lair (20465) and the terror dogs (20466)
                Scenery.PASSAGEWAY_20465 -> teleport(player, location(3185, 4601, 0))
                Scenery.PASSAGEWAY_20466 -> teleport(player, location(3185, 4601, 0))

                // the passageway in the Tarn fight room
                Scenery.PASSAGEWAY_20467 -> {
                    if (getAttribute(player, ATTR_KILLED_TARN, false)) {
                        teleport(player, location(locX, locY + 6, locZ))
                    } else {
                        sendMessage(player, "You need to have killed Tarn Razorlor to enter this passageway.")
                    }
                }

                Scenery.PASSAGEWAY_20572 -> teleport(player, location(locX, locY + 6, locZ))
                Scenery.PASSAGEWAY_20573 -> teleport(player, location(locX, locY - 6, locZ))
                Scenery.PASSAGEWAY_20575 -> when (locX) {
                    3167, 3189 -> teleport(player, location(locX + 5, locY, locZ + 1))
                    3148 -> teleport(player, location(locX - 5, locY, locZ + 1))
                    else -> teleport(player, location(locX, locY + 5, locZ + 1))
                }

                Scenery.PASSAGEWAY_20654 -> when (locX) {
                    3158 -> teleport(player, location(locX, locY + 5, locZ + 1))
                    3141 -> teleport(player, location(locX, locY + 5, locZ + 1))
                    else -> teleport(player, location(locX + 5, locY, locZ + 1))
                }

                Scenery.PASSAGEWAY_20721 -> teleport(player, location(locX - 5, locY, locZ - 1))

                Scenery.PASSAGEWAY_20821 -> teleport(player, location(locX, locY - 5, locZ))
                Scenery.PASSAGEWAY_20822 -> teleport(player, location(locX, locY + 5, locZ))
                Scenery.PASSAGEWAY_20823 -> teleport(player, location(locX - 6, locY, locZ))
                Scenery.PASSAGEWAY_20824 -> teleport(player, location(locX + 6, locY, locZ))
                Scenery.PASSAGEWAY_20829 -> teleport(player, location(locX, locY + 5, locZ - 1))

                Scenery.PASSAGEWAY_20830 -> teleport(player, location(locX, locY - 5, locZ + 1))
                Scenery.PASSAGEWAY_20831 -> teleport(player, location(locX, locY + 5, locZ + 1))
                Scenery.PASSAGEWAY_20832 -> teleport(player, location(locX, locY - 5, locZ - 1))
                Scenery.PASSAGEWAY_20833 -> teleport(player, location(locX, locY + 5, locZ - 1))
                Scenery.PASSAGEWAY_20834 -> teleport(player, location(locX, locY - 5, locZ + 1))
                Scenery.PASSAGEWAY_20835 -> teleport(player, location(locX + 5, locY, locZ - 1))
                Scenery.PASSAGEWAY_20836 -> teleport(player, location(locX - 5, locY, locZ + 1))
                Scenery.PASSAGEWAY_20837 -> teleport(player, location(locX + 5, locY, locZ))
                Scenery.PASSAGEWAY_20838 -> teleport(player, location(locX - 5, locY, locZ))

                Scenery.PASSAGEWAY_20841 -> teleport(player, location(locX, locY + 5, locZ - 1))
                Scenery.PASSAGEWAY_20842 -> teleport(player, location(locX, locY - 5, locZ + 1))
                Scenery.PASSAGEWAY_20843 -> teleport(player, location(locX, locY + 6, locZ + 1))
                Scenery.PASSAGEWAY_20844 -> teleport(player, location(locX, locY - 6, locZ - 1))
                Scenery.PASSAGEWAY_20845 -> teleport(player, location(locX - 5, locY, locZ - 1))
                Scenery.PASSAGEWAY_20846 -> teleport(player, location(locX + 7, locY, locZ + 1))
                Scenery.PASSAGEWAY_20847 -> teleport(player, location(locX - 5, locY, locZ + 1))
                Scenery.PASSAGEWAY_20848 -> teleport(player, location(locX + 5, locY, locZ - 1))
                Scenery.PASSAGEWAY_20849 -> teleport(player, location(locX, locY + 5, locZ + 1))

                Scenery.PASSAGEWAY_20850 -> teleport(player, location(locX, locY - 5, locZ - 1))
                Scenery.PASSAGEWAY_20851 -> teleport(player, location(locX - 5, locY, locZ + 1))
                Scenery.PASSAGEWAY_20853 -> teleport(player, location(locX, locY + 7, locZ - 1))
                Scenery.PASSAGEWAY_20854 -> teleport(player, location(locX, locY - 7, locZ + 1))
                Scenery.PASSAGEWAY_20855 -> teleport(player, location(locX, locY + 7, locZ))
                Scenery.PASSAGEWAY_20856 -> teleport(player, location(locX, locY - 7, locZ))
                Scenery.PASSAGEWAY_20857 -> teleport(player, location(locX - 5, locY, locZ + 1))
                Scenery.PASSAGEWAY_20858 -> teleport(player, location(locX + 5, locY, locZ - 1))
                Scenery.PASSAGEWAY_20859 -> teleport(player, location(locX, locY + 5, locZ - 1))

                Scenery.PASSAGEWAY_20860 -> teleport(player, location(locX, locY - 5, locZ + 1))
                Scenery.PASSAGEWAY_20861 -> teleport(player, location(locX + 11, locY, locZ))
                Scenery.PASSAGEWAY_20862 -> teleport(player, location(locX - 11, locY, locZ))
                Scenery.PASSAGEWAY_20863 -> teleport(player, location(locX, locY - 5, locZ))
                Scenery.PASSAGEWAY_20864 -> teleport(player, location(locX, locY + 5, locZ))
                Scenery.PASSAGEWAY_20865 -> teleport(player, location(locX - 4, locY, locZ))
                Scenery.PASSAGEWAY_20866 -> teleport(player, location(locX + 4, locY, locZ))
                Scenery.PASSAGEWAY_20867 -> teleport(player, location(locX + 4, locY, locZ))
                Scenery.PASSAGEWAY_20868 -> teleport(player, location(locX - 4, locY, locZ))

                // Door to Tarn's lair. Needs 40 slayer to fight Tarn.
                Scenery.PASSAGEWAY_20871 -> if (hasLevelStat(player, Skills.SLAYER, 40)) {
                    // choose to send player to boss fight or regular terror dogs.
                    if (getAttribute(player, ATTR_KILLED_TARN, false)) {
                        // terror dogs
                        teleport(player, location(3149, 4644, 0))
                    } else {
                        // boss fight
                        teleport(player, location(3186, 4612, 0))
                        LairOfTarnCutscene(player).start()
                    }
                } else sendMessage(player, "You need at least 40 Slayer to attempt this.")
            }
            return@on true
        }

        on(stairs, IntType.SCENERY, "Climb", "Climb-down") { player, node ->
            val locX = player.location.x
            val locY = player.location.y
            val locZ = player.location.z

            when (node.id) {
                Scenery.STAIRS_20619 -> when (locX) {
                    3158, 3184 -> teleport(player, location(locX, locY - 5, locZ - 1))
                    3143 -> teleport(player, location(locX + 5, locY, locZ - 1))
                    else -> teleport(player, location(locX - 5, locY, locZ - 1))
                }

                Scenery.STAIRS_20684 -> teleport(player, location(locX, locY - 5, locZ - 1))
            }
            return@on true
        }

        // TODO: a lot of the scenery should be animated: log traps, wall traps, floor traps.

        // Jumpable pillars and ledges. Note that I don't have a source for the log trap behavior.
        on(pillars, IntType.SCENERY, "Jump-to") { player, node ->
            forceMove(
                player,
                player.location,
                node.location,
                0,
                75,
                anim = HUMAN_JUMP
            ) {
                if (node.id == Scenery.PILLAR_20879
                    && !getAttribute(player, ATTR_LOG_20904_DISABLED, false)
                ) {
                    // if log is not disabled, punch you off the ledge.
                    forceMove(
                        player,
                        player.location,
                        Location.create(3145, 4597, 0),
                        0,
                        5
                    ) {
                        impact(player, 5)
                        sendMessage(player, "The hanging log knocks you down to the level below.")
                    }
                }
                if (node.id == Scenery.LEDGE_20895) {
                    // if you reach the other side, reset the log.
                    removeAttribute(player, ATTR_LOG_20904_DISABLED)
                }
                if (node.id == Scenery.PILLAR_20889
                    && !getAttribute(player, ATTR_LOG_20905_DISABLED, false)
                ) {
                    // if log is not disabled, punch you off the ledge.
                    forceMove(
                        player,
                        player.location,
                        Location.create(3145, 4574, 1),
                        0,
                        5
                    ) {
                        impact(player, 5)
                        sendMessage(player, "The hanging log knocks you down to the level below.")
                    }
                }
                if (node.id == Scenery.LEDGE_20901 || node.id == Scenery.LEDGE_20900) {
                    // if you reach the other side, reset the log.
                    removeAttribute(player, ATTR_LOG_20905_DISABLED)
                }
                if (node.id == Scenery.PILLAR_20872
                    && !getAttribute(player, ATTR_LOG_20904_DISABLED, false)
                ) {
                    // if log is not disabled, punch you off the ledge.
                    forceMove(
                        player,
                        player.location,
                        Location.create(3185, 4560, 0),
                        0,
                        5
                    ) {
                        impact(player, 5)
                        sendMessage(player, "The hanging log knocks you down to the level below.")
                    }
                }
                if (node.id == Scenery.LEDGE_20890 || node.id == Scenery.LEDGE_20891) {
                    // if you reach the other side, reset the log.
                    removeAttribute(player, ATTR_LOG_20904_DISABLED)
                }
            }
            return@on true
        }

        // Floor search to disable the log trap
        on(intArrayOf(Scenery.FLOOR_20966, Scenery.FLOOR_20968), IntType.SCENERY, "Search") { player, node ->
            // this might be a chance to disable, but I'm just making it always true.
            when (node.id) {
                Scenery.FLOOR_20966 -> {
                    setAttribute(player, ATTR_LOG_20904_DISABLED, true)
                    sendMessage(player, "You try to disarm the trap...")
                    animate(player, HUMAN_DISARM_TRAP)
                    sendMessage(player, "...You hear a click.")
                }

                Scenery.FLOOR_20968 -> {
                    setAttribute(player, ATTR_LOG_20905_DISABLED, true)
                    sendMessage(player, "You try to disarm the trap...")
                    animate(player, HUMAN_DISARM_TRAP)
                    sendMessage(player, "...You hear a click.")
                }
            }
            return@on true
        }

        // Searchable floor traps. They should otherwise damage you if you step on them.
        on(floors, IntType.SCENERY, "Search") { player, node ->
            sendMessage(player, "You try to disarm the trap...")
            // this might be a chance to disable, but I'm just making it always true.
            animate(player, HUMAN_DISARM_TRAP)
            sendMessage(player, "...You hear a click.")

            lastTrap = node.location

            return@on true
        }

        // Searchable wall traps.
        on(walls, IntType.SCENERY, "Search") { player, node ->
            sendMessage(player, "You try to disarm the trap...")
            animate(player, Animations.HUMAN_PICKPOCKETING_881)
            forceMove(
                player,
                player.location,
                player.location.transform(direction(player.location, node.centerLocation), 3),
                0,
                5
            )
            sendMessage(player, "...and succeed! You quickly walk past.")

            return@on true
        }

        /*
            TODO: add pushy walls
            pushy walls should push you off
            Scenery.WALL_20945

            Location.create(3162, 4600, 1)
            Location.create(3164, 4600, 1)
            Location.create(3171, 4600, 1)
            Location.create(3173, 4600, 1)
         */

        /*
            TODO: add more floor traps
            maybe they drop you through the floor. I can't find anyone triggering them.
            Scenery.FLOOR_20960

            Location.create(3139, 4556, 2)
            Location.create(3143, 4556, 2)
            Location.create(3139, 4558, 2)
            Location.create(3141, 4558, 2)
            Location.create(3143, 4558, 2)
         */

        /* * * * * * * * * * * * * * * * * * * * * *
         *
         *   LAIR OF TARN (mQuest) RELATED LISTENERS
         *
         * * * * * * * * * * * * * * * * * * * * * */

        onUseWith(ITEM, Items.SALVE_AMULET_4081, Items.TARNS_DIARY_10587) { player, used, _ ->
            if (removeItem(player, used)) {
                addItem(player, Items.SALVE_AMULETE_10588)
                visualize(player, ENCHANT_SALVE_AMULET, ENCHANT_SALVE_AMULET_GFX)
                sendMessage(player, "You enchant the Salve amulet...")
            }
            return@onUseWith true
        }
    }
}

// spawns tarn and the terror dogs (fight is instanced)
fun startBossFight(player: Player) {
    // create the dynamic region and set multicombat
    val region = DynamicRegion.create(12616)
    region.toggleMulticombat()
    region.setMusicId(Music.UNDEAD_DUNGEON_214)
    val base = region.baseLocation

    // register the dynamic region's borders inside the regular maparea, this will restrict cannons and drain prayer
    LairOfTarnZone().zone.register(getRegionBorders(region.id))
    LairOfTarnPrayerDrainZone().zone.register(getRegionBorders(region.id))

    // move player into the instanced copy
    player.properties.teleportLocation = base.transform(50, 4, 0)

    // start the NPCs
    val tarn = TarnNPC(NPCs.MUTANT_TARN_5421, base.transform(50, 15, 0))
    val dog1 = TerrorDogNPC(NPCs.TERROR_DOG_5417, base.transform(52, 13, 0))
    val dog2 = TerrorDogNPC(NPCs.TERROR_DOG_5417, base.transform(47, 13, 0))

    tarn.init()
    tarn.isRespawn = false
    tarn.isAggressive = true
    tarn.aggressiveHandler.radius = 16
    tarn.aggressiveHandler.chanceRatio = 10
    tarn.aggressiveHandler.isAllowTolerance = false
    tarn.attack(player)
    setAttribute(tarn, LairOfTarn.ATTR_REGION_BASE, base)

    dog1.init()
    dog1.isWalks = true
    dog1.isRespawn = false
    dog1.isAggressive = true
    dog1.aggressiveHandler.radius = 16
    dog1.aggressiveHandler.chanceRatio = 10
    dog1.aggressiveHandler.isAllowTolerance = false
    dog1.attack(player)

    dog2.init()
    dog2.isWalks = true
    dog2.isRespawn = false
    dog2.isAggressive = true
    dog2.aggressiveHandler.radius = 16
    dog2.aggressiveHandler.chanceRatio = 10
    dog2.aggressiveHandler.isAllowTolerance = false
    dog2.attack(player)
}