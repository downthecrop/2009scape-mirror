package content.global.travel.canoe

import content.data.skill.SkillingTool
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.scenery.SceneryBuilder
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.net.packet.PacketRepository
import core.net.packet.context.MinimapStateContext
import core.net.packet.out.MinimapState
import core.tools.RandomFunction
import org.rs09.consts.Components
import org.rs09.consts.Scenery
import org.rs09.consts.Sounds
import kotlin.math.abs

/**
 * Interaction and interface listener for canoe along river Lum.
 * This handles Components.CANOE_52, Components.CANOE_STATIONS_MAP_53 and Components.CANOE_TRAVEL_758 globally.
 */
class CanoeListener : InteractionListener, InterfaceListener {
    companion object {
        const val CANOE_STATION_VARBIT_ATTRIBUTE = "canoeStationVarbit" // Index of Canoe Stations (0-4)
        const val CANOE_SELECTED_ATTRIBUTE = "canoeSelected" // Index of Canoes (0-3)

        const val CANOE_SHAPING_INTERFACE = Components.CANOE_52
        const val CANOE_DESTINATION_INTERFACE = Components.CANOE_STATIONS_MAP_53
        const val CANOE_TRAVEL_INTERFACE = Components.CANOE_TRAVEL_758

        val CANOE_SHAPING_SILHOUETTE = arrayOf(0, 9, 10, 8)
        val CANOE_SHAPING_TEXT = arrayOf(0, 3, 2, 5)
        val CANOE_SHAPING_BUTTONS = arrayOf(30, 31, 32, 33)

        val CANOE_DESTINATION_BUTTONS = arrayOf(47, 48, 3, 6, 49)
        val CANOE_DESTINATION_HIDE_ROW = arrayOf(10, 11, 12, 20, 18)
        val CANOE_DESTINATION_YOU_ARE_HERE = arrayOf(25, 24, 23, 19, 0)

        val CANOE_TREE_FALLING_ANIMATION = Animation(3304)
        val CANOE_PLAYER_PUSHING_ANIMATION = Animation(3301)
        val CANOE_PUSHING_ANIMATION = Animation(3304)
        val CANOE_SINKING_ANIMATION = Animation(3305)
        /** Canoe travel interface animations. A 2D array mapping [ origin ][ destination ]. Index 0-4 for Lumbridge to Wilderness. */
        val CANOE_TRAVEL_ANIMATIONS = arrayOf(
                arrayOf(0, 9890, 9889, 9888, 9887),
                arrayOf(9906, 0, 9893, 9892, 9891),
                arrayOf(9904, 9905, 0, 9895, 9894),
                arrayOf(9901, 9902, 9903, 0, 9896),
                arrayOf(9897, 9898, 9899, 9900, 0),
        )

        @JvmStatic
        fun getAxeAnimation(axe: SkillingTool): Animation {
            return when (axe) {
                SkillingTool.BRONZE_AXE -> Animation(6744)
                SkillingTool.IRON_AXE -> Animation(6743)
                SkillingTool.STEEL_AXE -> Animation(6742)
                SkillingTool.BLACK_AXE -> Animation(6741)
                SkillingTool.MITHRIL_AXE -> Animation(6740)
                SkillingTool.ADAMANT_AXE -> Animation(6739)
                SkillingTool.RUNE_AXE -> Animation(6738)
                SkillingTool.DRAGON_AXE -> Animation(6745)
                else -> axe.animation
            }
        }

        /** To scale woodcutting success. Follows WoodcuttingNode values at 15,30,45,60 (3 levels 'harder'). */
        private fun checkSuccess(player: Player, resource: Canoes, tool: SkillingTool): Boolean {
            val skill = Skills.WOODCUTTING
            val level: Int = getDynLevel(player, skill) + getFamiliarBoost(player, skill)
            val hostRatio = RandomFunction.randomDouble(100.0)
            val lowMod: Double = if (tool == SkillingTool.BLACK_AXE) resource.tierModLow / 2 else resource.tierModLow
            val low: Double = resource.baseLow + tool.ordinal * lowMod
            val highMod: Double = if (tool == SkillingTool.BLACK_AXE) resource.tierModHigh / 2 else resource.tierModHigh
            val high: Double = resource.baseHigh + tool.ordinal * highMod
            val clientRatio = RandomFunction.getSkillSuccessChance(low, high, level)
            return hostRatio < clientRatio
        }

        /** Maps canoe stations stages to related properties. */
        enum class CanoeStationSceneries(val sceneryId: Int, val varbitValue: Int) {
            // Stage 1 - Tree is standing, option to chop-down.
            TREE_STANDING(Scenery.CANOE_STATION_12144, 0),

            // Stage 2 - The tree is falling via an animation, denies player of options.
            TREE_FALLING(Scenery.CANOE_STATION_12145, 9),

            // Stage 3 - Tree is on the cradle after the chop, option to shape the boat via an interface.
            TREE_FALLEN(Scenery.CANOE_STATION_12146, 10),

            // Stage 4 - Tree gets shaped into 1 of 4 canoes, option to push it into the water.
            TREE_SHAPED_LOG(Scenery.CANOE_STATION_12147, 1),
            TREE_SHAPED_DUGOUT(Scenery.CANOE_STATION_12148, 2),
            TREE_SHAPED_STABLE_DUGOUT(Scenery.CANOE_STATION_12149, 3),
            TREE_SHAPED_WAKA(Scenery.CANOE_STATION_12150, 4),

            // Stage 5 - Canoe is being pushed into the water via an animation, option to travel is available.
            CANOE_PUSHING_LOG(Scenery.CANOE_STATION_12151, 5),
            CANOE_PUSHING_DUGOUT(Scenery.CANOE_STATION_12152, 6),
            CANOE_PUSHING_STABLE_DUGOUT(Scenery.CANOE_STATION_12153, 7),
            CANOE_PUSHING_WAKA(Scenery.CANOE_STATION_12154, 8),

            // Stage 6 - Canoe is in the water, option to travel via an interface.
            CANOE_FLOATING_LOG(Scenery.CANOE_STATION_12155, 11),
            CANOE_FLOATING_DUGOUT(Scenery.CANOE_STATION_12156, 12),
            CANOE_FLOATING_STABLE_DUGOUT(Scenery.CANOE_STATION_12157, 13),
            CANOE_FLOATING_WAKA(Scenery.CANOE_STATION_12158, 14),

            // Stage 7 - Canoe is animated to be sunk at the end of the ride. NO VARBITS, NOT PART OF THE CANOE_STATION SCENERY.
            CANOE_SINKING_LOG(Scenery.A_SINKING_CANOE_12159, 0),
            CANOE_SINKING_DUGOUT(Scenery.A_SINKING_CANOE_12160, 0),
            CANOE_SINKING_STABLE_DUGOUT(Scenery.A_SINKING_CANOE_12161, 0),
            CANOE_SINKING_WAKA(Scenery.A_SINKING_CANOE_12162, 0);

            companion object {
                @JvmField
                val stationIdMap = values().associateBy { it.sceneryId }
                val stationIdArray = stationIdMap.values.map { it.sceneryId }.toIntArray()
            }
        }

        /** Enums to map canoes to related properties. */
        enum class Canoes(val level: Int, val experience: Double, val maxDistance: Int, val baseLow: Double, val baseHigh: Double, val tierModLow: Double, val tierModHigh: Double, val treeShaped: CanoeStationSceneries, val canoePushing: CanoeStationSceneries, val canoeFloating: CanoeStationSceneries, val canoeSinking: CanoeStationSceneries) {
            LOG             (12, 30.0, 1, 32.0, 100.0, 16.0, 50.0, CanoeStationSceneries.TREE_SHAPED_LOG, CanoeStationSceneries.CANOE_PUSHING_LOG, CanoeStationSceneries.CANOE_FLOATING_LOG, CanoeStationSceneries.CANOE_SINKING_LOG),
            DUGOUT          (27, 60.0, 2, 16.0, 50.0, 8.0, 25.0, CanoeStationSceneries.TREE_SHAPED_DUGOUT, CanoeStationSceneries.CANOE_PUSHING_DUGOUT, CanoeStationSceneries.CANOE_FLOATING_DUGOUT, CanoeStationSceneries.CANOE_SINKING_DUGOUT),
            STABLE_DUGOUT   (42, 90.0, 3, 8.0, 25.0, 4.0, 12.5, CanoeStationSceneries.TREE_SHAPED_STABLE_DUGOUT, CanoeStationSceneries.CANOE_PUSHING_STABLE_DUGOUT, CanoeStationSceneries.CANOE_FLOATING_STABLE_DUGOUT, CanoeStationSceneries.CANOE_SINKING_STABLE_DUGOUT),
            WAKA            (57, 150.0, 4, 4.0, 12.5, 2.0, 6.25, CanoeStationSceneries.TREE_SHAPED_WAKA, CanoeStationSceneries.CANOE_PUSHING_WAKA, CanoeStationSceneries.CANOE_FLOATING_WAKA, CanoeStationSceneries.CANOE_SINKING_WAKA);

            companion object {
                @JvmField
                val indexMap = Canoes.values().associateBy { it.ordinal }
            }
        }

        /** Enums to map canoe stations locations to related properties. */
        enum class CanoeStationLocations(val stationRegion: Int, val stationVarbit: Int, val playerChopLocation: Location, val playerFloatLocation: Location, val playerFacingLocation: Location, val canoeSinkLocation: Location, val playerDestination: Location, val locationName: String) {
            LUMBRIDGE(12850, 1839, Location(3243, 3235), Location(3243, 3237), Location(-1, 0), Location(3239, 3242), Location(3240, 3242), "Lumbridge"),
            CHAMPIONS(12852, 1840, Location(3204, 3343), Location(3202, 3343), Location(0, -1), Location(3199, 3344), Location(3199, 3344), "the Champion's Guild"),
            BARBARIAN(12341, 1841, Location(3112, 3409), Location(3112, 3411), Location(-1, 0), Location(3109, 3411), Location(3109, 3415), "Barbarian Village"),
            EDGEVILLE(12342, 1842, Location(3132, 3508), Location(3132, 3510), Location(-1, 0), Location(3132, 3510), Location(3132, 3510), "Edgeville"),
            WILDERNESS(12603, 0, Location(0, 0), Location(0, 0), Location(0, 0), Location(3142, 3795), Location(3139, 3796), "the Wilderness Pond");

            companion object {
                private val stationRegionMap = CanoeStationLocations.values().associateBy { it.stationRegion }

                @JvmStatic
                fun getCanoeStationbyLocation(location: Location): CanoeStationLocations {
                    return stationRegionMap[location.regionId]!!
                }
            }
        }
    }

    override fun defineDestinationOverrides() {
        // Set player's standing location when chopping down the tree.
        setDest(IntType.SCENERY, CanoeStationSceneries.stationIdArray, "chop-down") { _, node ->
            return@setDest CanoeStationLocations.getCanoeStationbyLocation(node.location).playerChopLocation
        }
        // Set player's standing location when shaping and floating the canoe.
        setDest(IntType.SCENERY, CanoeStationSceneries.stationIdArray, "shape-canoe", "float canoe", "float log", "float waka") { _, node ->
            return@setDest CanoeStationLocations.getCanoeStationbyLocation(node.location).playerFloatLocation
        }
    }

    override fun defineListeners() {
        // Stage 1 - 2: Tree is standing, option to chop-down.
        on(CanoeStationSceneries.stationIdArray, IntType.SCENERY, "chop-down") { player, node ->
            val canoeStation = CanoeStationLocations.getCanoeStationbyLocation(node.location)
            val axe: SkillingTool? = SkillingTool.getHatchet(player)
            val stationVarbit = node.asScenery().definition.configFile
            if (axe == null) {
                sendMessage(player, "You do not have an axe which you have the woodcutting level to use.")
                return@on true
            }
            if (getStatLevel(player, Skills.WOODCUTTING) < 12) {
                sendMessage(player, "You need a woodcutting level of at least 12 to chop down this tree.")
                return@on true
            }
            lock(player, axe.animation.duration + CANOE_TREE_FALLING_ANIMATION.duration)
            face(player, canoeStation.playerChopLocation.transform(canoeStation.playerFacingLocation))
            animate(player, axe.animation)
            queueScript(player, axe.animation.duration, QueueStrength.SOFT) { stage: Int ->
                when (stage) {
                    0 -> {
                        player.animator.stop()
                        setVarbit(player, stationVarbit, CanoeStationSceneries.TREE_FALLING.varbitValue)
                        animateScenery(player, node.asScenery(), CANOE_TREE_FALLING_ANIMATION.id)
                        return@queueScript delayScript(player, CANOE_TREE_FALLING_ANIMATION.duration)
                    }
                    1 -> {
                        setVarbit(player, stationVarbit, CanoeStationSceneries.TREE_FALLEN.varbitValue)
                        unlock(player)
                        return@queueScript stopExecuting(player)
                    }
                    else -> return@queueScript stopExecuting(player)
                }
            }
            return@on true
        }

        // Stage 3: Tree is on the cradle after the chop, option to shape-canoe via interface CANOE_52.
        on(CanoeStationSceneries.stationIdArray, IntType.SCENERY, "shape-canoe") { player, node ->
            val canoeStation = CanoeStationLocations.getCanoeStationbyLocation(node.location)
            setAttribute(player, CANOE_STATION_VARBIT_ATTRIBUTE, canoeStation.stationVarbit)
            face(player, canoeStation.playerFloatLocation.transform(canoeStation.playerFacingLocation))
            openInterface(player, CANOE_SHAPING_INTERFACE)
            openOverlay(player, 333) // Black overlay
            return@on true
        }

        // Stage 4 - 5: Tree gets shaped into 1 of 4 canoes, option to float-canoe.
        on(CanoeStationSceneries.stationIdArray, IntType.SCENERY, "float canoe", "float log", "float waka") { player, node ->
            val canoeStation = CanoeStationLocations.getCanoeStationbyLocation(node.location)
            val canoe = Canoes.indexMap[getAttribute(player, CANOE_SELECTED_ATTRIBUTE, 0)]!!
            setVarbit(player, canoeStation.stationVarbit, canoe.canoePushing.varbitValue)
            lock(player, CANOE_PLAYER_PUSHING_ANIMATION.duration)
            playAudio(player, Sounds.CANOE_ROLL_2731)
            face(player, canoeStation.playerFloatLocation.transform(canoeStation.playerFacingLocation))
            queueScript(player, 0, QueueStrength.SOFT) { stage: Int ->
                when (stage) {
                    0 -> {
                        animate(player, CANOE_PLAYER_PUSHING_ANIMATION)
                        animateScenery(player, node.asScenery(), CANOE_PUSHING_ANIMATION.id)
                        return@queueScript delayScript(player, CANOE_PLAYER_PUSHING_ANIMATION.duration)
                    }
                    1 -> {
                        setVarbit(player, canoeStation.stationVarbit, canoe.canoeFloating.varbitValue)
                        unlock(player)
                        player.animator.stop()
                        return@queueScript stopExecuting(player)
                    }
                    else -> return@queueScript stopExecuting(player)
                }
            }
            return@on true
        }

        // Stage 6: Canoe is in the water, option to paddle via interface CANOE_STATIONS_MAP_53.
        on(CanoeStationSceneries.stationIdArray, IntType.SCENERY, "paddle log", "paddle canoe") { player, _ ->
            closeInterface(player)
            openInterface(player, Components.CANOE_STATIONS_MAP_53)
            openOverlay(player, 333) // Black overlay
            return@on true
        }
    }

    var temp = 14
    override fun defineInterfaceListeners() {

        // Stage 3: Opening interface 52
        onOpen(CANOE_SHAPING_INTERFACE) { player, _ ->
            Canoes.values().forEach {
                if (getStatLevel(player, Skills.WOODCUTTING) >= it.level && it != Canoes.LOG) {
                    setComponentVisibility(player, CANOE_SHAPING_INTERFACE, CANOE_SHAPING_SILHOUETTE[it.ordinal], true)
                    setComponentVisibility(player, CANOE_SHAPING_INTERFACE, CANOE_SHAPING_TEXT[it.ordinal], false)
                }
            }
            return@onOpen true
        }

        // Stage 3: On button click interface 52
        on(CANOE_SHAPING_INTERFACE) { player, _, _, buttonID, _, _ ->
            closeInterface(player)
            val canoe = Canoes.indexMap[CANOE_SHAPING_BUTTONS.indexOf(buttonID)]!!
            val stationVarbit = getAttribute(player, CANOE_STATION_VARBIT_ATTRIBUTE, 1839)
            val axe: SkillingTool? = SkillingTool.getHatchet(player)
            if (axe == null) {
                sendMessage(player, "You do not have an axe which you have the woodcutting level to use.")
                return@on true
            }

            lock(player, 4)
            animate(player, getAxeAnimation(axe))
            submitIndividualPulse(player, object : Pulse(3) {
                override fun pulse(): Boolean {
                    if (checkSuccess(player, canoe, axe)) {
                        setAttribute(player, CANOE_SELECTED_ATTRIBUTE, CANOE_SHAPING_BUTTONS.indexOf(buttonID))
                        setVarbit(player, stationVarbit, canoe.treeShaped.varbitValue)
                        rewardXP(player, Skills.WOODCUTTING, canoe.experience)
                        unlock(player)
                        return true
                    }
                    animate(player, getAxeAnimation(axe))
                    return false
                }
            })
            return@on true
        }

        onOpen(CANOE_DESTINATION_INTERFACE) { player, component ->
            val canoe = Canoes.indexMap[getAttribute(player, CANOE_SELECTED_ATTRIBUTE, 0)]!!
            val origin = CanoeStationLocations.getCanoeStationbyLocation(player.location)
            for (i in CanoeStationLocations.values()) {
                setComponentVisibility(player, component.id, CANOE_DESTINATION_HIDE_ROW[i.ordinal], true)
                if (i == CanoeStationLocations.WILDERNESS) {
                    if (canoe == Canoes.WAKA) {
                        // setComponentVisibility(player, component.id, 22, false) // Some red text on Wildy text.
                        setComponentVisibility(player, component.id, CANOE_DESTINATION_HIDE_ROW[i.ordinal], false)
                    }
                } else if (i.ordinal == origin.ordinal) {
                    setComponentVisibility(player, component.id, CANOE_DESTINATION_YOU_ARE_HERE[i.ordinal], false)
                }else if (abs(i.ordinal - origin.ordinal) <= canoe.maxDistance) {
                    setComponentVisibility(player, component.id, CANOE_DESTINATION_HIDE_ROW[i.ordinal], false)
                }
            }
            return@onOpen true
        }

        on(CANOE_DESTINATION_INTERFACE) { player, _, _, buttonID, _, _ ->
            val origin = CanoeStationLocations.getCanoeStationbyLocation(player.location)
            var destination: CanoeStationLocations = CanoeStationLocations.LUMBRIDGE
            when (buttonID) {
                CANOE_DESTINATION_BUTTONS[0] -> destination = CanoeStationLocations.LUMBRIDGE
                CANOE_DESTINATION_BUTTONS[1] -> destination = CanoeStationLocations.CHAMPIONS
                CANOE_DESTINATION_BUTTONS[2] -> destination = CanoeStationLocations.BARBARIAN
                CANOE_DESTINATION_BUTTONS[3] -> destination = CanoeStationLocations.EDGEVILLE
                CANOE_DESTINATION_BUTTONS[4] -> destination = CanoeStationLocations.WILDERNESS
            }
            val arrivalMessage = destination.locationName
            val interfaceAnimationId = CANOE_TRAVEL_ANIMATIONS[origin.ordinal][destination.ordinal]

            if (player.familiarManager.hasFamiliar()) {
                sendMessage(player, "You can't take a follower on a canoe.")
                return@on true
            }
            lock(player, Animation(interfaceAnimationId).duration + 1 + Animation(Components.FADE_FROM_BLACK_170).duration)
            // Tried using queueScript here, but it doesn't work as interfaces and overlays can't open. Maybe its because this is within an interface listener.
            submitIndividualPulse(player, object : Pulse() {
                var counter = 0
                override fun pulse(): Boolean {
                    when (counter++) {
                        0 -> {
                            openInterface(player, CANOE_TRAVEL_INTERFACE)
                            openOverlay(player, 333) // Only call openOverlay(player, 333) after openInterface
                            animateInterface(player, CANOE_TRAVEL_INTERFACE, 3, interfaceAnimationId)
                            PacketRepository.send(MinimapState::class.java, MinimapStateContext(player, 2))
                            player.interfaceManager.removeTabs(0, 1, 2, 3, 4, 5, 6, 11, 12)
                        }
                        Animation(interfaceAnimationId).duration + 1 -> {
                            teleport(player, destination.playerDestination)
                            closeInterface(player)
                            closeOverlay(player)
                            openOverlay(player, Components.FADE_FROM_BLACK_170)
                        }
                        Animation(interfaceAnimationId).duration + 1 + Animation(Components.FADE_FROM_BLACK_170).duration -> {
                            unlock(player)
                            player.interfaceManager.restoreTabs()
                            PacketRepository.send(MinimapState::class.java, MinimapStateContext(player, 0))
                            val sinkingScenery = SceneryBuilder.add(core.game.node.scenery.Scenery(Scenery.A_SINKING_CANOE_12159, destination.canoeSinkLocation, 1), 3) as core.game.node.scenery.Scenery
                            animateScenery(sinkingScenery, CANOE_SINKING_ANIMATION.id)
                            sendMessage(player, "You arrive at $arrivalMessage.")
                            sendMessage(player, "Your canoe sinks from the long journey.")
                            if (destination == CanoeStationLocations.WILDERNESS) {
                                sendMessage(player, "There are no trees nearby to make a new canoe. Guess you're walking.")
                            }
                            setVarbit(player, origin.stationVarbit, 0)
                            return true
                        }
                    }
                    return false
                }
            })
            return@on true
        }
    }
}