package core

import TestUtils
import content.global.skill.gather.GatheringSkillOptionListeners
import content.global.skill.gather.woodcutting.WoodcuttingListener
import core.api.log
import core.cache.def.impl.NPCDefinition
import core.game.interaction.*
import core.game.node.scenery.Scenery
import core.game.world.map.Location
import core.game.world.map.RegionManager
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import core.game.node.Node
import core.game.node.entity.impl.PulseType
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.GameWorld
import core.game.world.map.Region
import core.net.packet.PacketProcessor
import core.plugin.ClassScanner
import core.plugin.Plugin
import core.tools.Log
import org.rs09.consts.NPCs

class PathfinderTests {
    companion object {init {TestUtils.preTestSetup(); GatheringSkillOptionListeners().defineListeners(); WoodcuttingListener().defineListeners() }; val NPC_TEST_LOC = ServerConstants.HOME_LOCATION!!.transform(2, 10, 0)}

    @Test fun getOccupiedTilesShouldReturnCorrectSetOfTilesThatAnObjectOccupiesAtAllRotations() {
        //clay fireplace - 13609 - sizex: 1, sizey: 2
        val scenery = Scenery(13609, Location.create(50, 50, 0))

        scenery.rotation = 0
        val occupiedAt0 = scenery.occupiedTiles.toTypedArray()
        Assertions.assertArrayEquals(arrayOf(Location.create(50, 50), Location.create(50,51)), occupiedAt0)

        scenery.rotation = 1
        val occupiedAt1 = scenery.occupiedTiles.toTypedArray()
        Assertions.assertArrayEquals(arrayOf(Location.create(50,50), Location.create(51,50)), occupiedAt1)

        scenery.rotation = 2
        val occupiedAt2 = scenery.occupiedTiles.toTypedArray()
        Assertions.assertArrayEquals(arrayOf(Location.create(50,50), Location.create(50,49)), occupiedAt2)

        scenery.rotation = 3
        val occupiedAt3 = scenery.occupiedTiles.toTypedArray()
        Assertions.assertArrayEquals(arrayOf(Location.create(50,50), Location.create(49,50)), occupiedAt3)
    }

    @Test fun movementPulseShouldStopEarlyIfNextToATileOccupiedByTargetObject() {
        val start = Location.create(2731, 3481)
        val dest = RegionManager.getObject(0, 2720, 3475, 1307)
        val p = TestUtils.getMockPlayer("treefindtest")
        p.location = start
        p.init()

        Assertions.assertEquals(true, InteractionListeners.run(1307, IntType.SCENERY, "chop-down", p, dest!!))
        TestUtils.advanceTicks(20, false)
        Assertions.assertEquals(Location.create(2722, 3475, 0), p.location)
    }

    @Test fun movementInteractionShouldTrigger() {
        val npc = NPC.create(0, NPC_TEST_LOC)
        npc.init()

        var intListenerRan = false
        InteractionListeners.add(0, IntType.NPC.ordinal, arrayOf("testoptlistener"), method = {player: Player, node: Node ->
            intListenerRan = true
            return@add true
        })

        var pluginRan = false
        val option = Option("testoption", 4)
        val option2 = Option("testoptlistener", 0)
        val testHandler = object : OptionHandler() {
            override fun newInstance(arg: Any?): Plugin<Any> {
                NPCDefinition.forId(0).handlers["option:testoption"] = this
                return this
            }
            override fun handle(player: Player?, node: Node?, option: String?): Boolean {
                pluginRan = true
                return true
            }
        }
        testHandler.newInstance(null)
        npc.interaction.set(option)
        npc.interaction.set(option2)
        option.handler = testHandler

        TestUtils.getMockPlayer("interactionTest").use {p ->
            p.location = ServerConstants.HOME_LOCATION
            TestUtils.simulateInteraction(p, npc, 0)
            TestUtils.advanceTicks(10, false)
            Assertions.assertEquals(true, intListenerRan)
            p.location = ServerConstants.HOME_LOCATION
            TestUtils.simulateInteraction(p, npc, 4)
            TestUtils.advanceTicks(10, false)
            Assertions.assertEquals(true, pluginRan)
        }
    }

    @Test fun entityMovingToStationaryNPCShouldNotIdleIndefinitely() {
        TestUtils.getMockPlayer("idlenpcdest").use {p ->
            val startLoc = ServerConstants.HOME_LOCATION
            p.location = startLoc
            val npc = NPC.create(0, NPC_TEST_LOC)
            npc.isNeverWalks = true
            npc.init()
            GameWorld.Pulser.submit(object : MovementPulse(p, npc) {
                override fun pulse(): Boolean {
                    return true
                }
            })
            TestUtils.advanceTicks(10, false)
            Assertions.assertNotEquals(startLoc, p.location)
        }
    }

    @Test fun entityTargetMovementPulseShouldNotStopOnSameTileAsEntity() {
        TestUtils.getMockPlayer("entitystoptest").use {p ->
            p.location = ServerConstants.HOME_LOCATION
            val npc = NPC.create(0, NPC_TEST_LOC)
            npc.isNeverWalks = true
            npc.init()
            GameWorld.Pulser.submit(object : MovementPulse(p, npc) {
                override fun pulse(): Boolean {
                    return true
                }
            })
            TestUtils.advanceTicks(10, false)
            Assertions.assertNotEquals(p.location, npc.location)
            Assertions.assertEquals(1.0, p.location.getDistance(npc.location))
        }
    }

    @Test fun entityTargetMovementPulseWithExplicitParamsShouldNotStopOnSameTile() {
        TestUtils.getMockPlayer("entitystoptest2").use { p ->
            p.location = ServerConstants.HOME_LOCATION
            val npc = NPC.create(0, NPC_TEST_LOC)
            npc.isNeverWalks = true
            npc.init()
            GameWorld.Pulser.submit(object : MovementPulse(p, npc, DestinationFlag.ENTITY) {
                override fun pulse(): Boolean {
                    return true
                }
            })
            TestUtils.advanceTicks(10, false)
            Assertions.assertNotEquals(p.location, npc.location)
            Assertions.assertEquals(1.0, p.location.getDistance(npc.location))
        }
    }

    @Test fun doubleMovementPulseToEntityShouldNotStopOnSameTile() {
        TestUtils.getMockPlayer("entitystoptest3").use { p ->
            p.location = ServerConstants.HOME_LOCATION
            val npc = NPC.create(0, NPC_TEST_LOC)
            npc.isNeverWalks = true
            npc.init()
            p.pulseManager.run(object : MovementPulse(p, npc) {
                override fun pulse(): Boolean {
                    return true
                }
            })
            p.pulseManager.run(object : MovementPulse(p, npc) {
                override fun pulse(): Boolean {
                    return true
                }
            }, PulseType.STANDARD)
            TestUtils.advanceTicks(10, false)
            Assertions.assertNotEquals(ServerConstants.HOME_LOCATION, p.location)
            Assertions.assertNotEquals(p.location, npc.location)
            Assertions.assertEquals(1.0, p.location.getDistance(npc.location))
        }
    }

    @Test fun simulatedInteractionPacketWithMovementFromPluginShouldNotEndOnSameTile() {
        val testHandler = object : OptionHandler() {
            override fun newInstance(arg: Any?): Plugin<Any> {
                NPCDefinition.forId(0).handlers["option:testoption"] = this
                return this
            }
            override fun handle(player: Player?, node: Node?, option: String?): Boolean {
                log(this::class.java, Log.ERR, "Interaction triggered")
                return true
            }
        }
        testHandler.newInstance(null)
        val npc = NPC.create(0, NPC_TEST_LOC)
        val option = Option("testoption", 4)
        npc.interaction.set(option)
        option.handler = testHandler

        TestUtils.getMockPlayer("entitystoptest4").use { p ->
            p.location = ServerConstants.HOME_LOCATION
            npc.isNeverWalks = true
            npc.init()
            TestUtils.simulateInteraction(p, npc, 4)
            TestUtils.advanceTicks(20, false)
            Assertions.assertNotEquals(ServerConstants.HOME_LOCATION, p.location)
            Assertions.assertNotEquals(p.location, npc.location)
            Assertions.assertEquals(1.0, p.location.getDistance(npc.location))
        }
    }

    @Test fun simulatedInteractionPacketWithMovementFromListenerShouldNotEndOnSameTile() {
        val npc = NPC.create(0, NPC_TEST_LOC)
        npc.isNeverWalks = true
        npc.init()

        InteractionListeners.add(0, IntType.NPC.ordinal, arrayOf("testoptlistener2"), method = {player: Player, node: Node ->
            return@add true
        })
        val opt = Option("testoptlistener2", 1)
        npc.interaction.set(opt)

        TestUtils.getMockPlayer("entitystoptest5").use { p ->
            p.location = ServerConstants.HOME_LOCATION
            TestUtils.simulateInteraction(p, npc, 1)
            TestUtils.advanceTicks(20, false)
            Assertions.assertNotEquals(ServerConstants.HOME_LOCATION, p.location)
            Assertions.assertNotEquals(p.location, npc.location)
            Assertions.assertEquals(1.0, p.location.getDistance(npc.location))
        }
    }

    @Test fun npcShouldReliablyReturnToSpawnLocationIfTooFar() {
        //spawn a player into the area just to make sure it ticks...
        TestUtils.getMockPlayer("areatest").use { p ->
            val npc = NPC(1, Location.create(3240, 3226, 0))
            npc.isWalks = true
            npc.isNeverWalks = false
            npc.walkRadius = 5
            npc.init()
            npc.properties.spawnLocation = ServerConstants.HOME_LOCATION
            TestUtils.advanceTicks(5, false)
            Assertions.assertEquals(true, npc.getAttribute("return-to-spawn", false))
            TestUtils.advanceTicks(50, false)
            Assertions.assertEquals(true, npc.location.getDistance(ServerConstants.HOME_LOCATION) <= 9)
        }
    }

    @Test fun npcShouldReliablyReturnToSpawnEvenIfRegionUnloaded() {
        //spawn a player into the area just to make sure it ticks...
        TestUtils.getMockPlayer("areaunloadtest").use { p ->
            val npc = NPC(1, Location.create(3240, 3226, 0))
            npc.isWalks = true
            npc.isNeverWalks = false
            npc.walkRadius = 5
            npc.init()
            npc.properties.spawnLocation = ServerConstants.HOME_LOCATION
            TestUtils.advanceTicks(3, false)
            Assertions.assertEquals(true, npc.getAttribute("return-to-spawn", false))
            p.clear()
            RegionManager.forId(npc.location.regionId).flagInactive(true)
            TestUtils.advanceTicks(50, false)
            Assertions.assertEquals(false, npc.getAttribute("return-to-spawn", false))
            Assertions.assertEquals(true, npc.location.getDistance(ServerConstants.HOME_LOCATION) <= 5)
        }
    }
}