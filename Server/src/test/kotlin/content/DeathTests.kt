package content

import TestUtils
import api.asItem
import core.game.content.global.action.DropItemHandler
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.player.info.Rights
import core.game.node.entity.player.link.IronmanMode
import core.game.world.map.Location
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.rs09.consts.Items
import rs09.game.node.entity.player.graves.GraveType
import rs09.game.node.entity.player.graves.GraveController
import rs09.game.world.GameWorld
import rs09.tools.secondsToTicks
class DeathTests {
    init {
        //explicitly register the GraveController as a tick listener because tests don't run reflection
        TestUtils.preTestSetup() // need cache parsed to properly evaluate item values
        GameWorld.tickListeners.add(GraveController())
    }
    //Grave requirements source: https://runescape.wiki/w/Gravestone?oldid=854455

    @Test fun graveUtilsProduceGraveShouldProduceCorrectGrave() {
        val type = GraveType.MEM_PLAQUE
        val grave = GraveController.produceGrave(type)

        Assertions.assertEquals(type, grave.type)
    }

    @Test fun graveInitializedWithItemsShouldInitializeCorrectly() {
        val inventory = arrayOf(
                Items.RUNE_SCIMITAR_1333.asItem(),
                Items.RUNE_SCIMITAR_1333.asItem(),
                Items.RUNE_SCIMITAR_1333.asItem(),
                Items.YELLOW_BEAD_1472.asItem()
        )
        val player = TestUtils.getMockPlayer("gravetest", IronmanMode.NONE, Rights.REGULAR_PLAYER)

        val grave = GraveController.produceGrave(GraveType.MEM_PLAQUE)
        grave.initialize(player, Location.create(0,0,0), inventory)

        for (item in grave.getItems()) {
            Assertions.assertEquals(player, item.dropper)
            Assertions.assertEquals(player.details.uid, item.dropperUid)
            Assertions.assertEquals(
                    GameWorld.ticks + secondsToTicks(GraveType.MEM_PLAQUE.durationMinutes * 60),
                    item.decayTime
            )
            Assertions.assertEquals(true, item.isRemainPrivate)
            Assertions.assertEquals(true, item.id in inventory.map { it.id }.toIntArray())
            Assertions.assertEquals(true, grave.isActive)
        }
    }

    @Test fun graveInitializedWithNoItemsShouldNotSpawn() {
        val grave = GraveController.produceGrave(GraveType.MEM_PLAQUE)
        val p = TestUtils.getMockPlayer("gravetest")

        grave.initialize(p, Location.create(0,0,0), arrayOf())
        Assertions.assertEquals(false, grave.isActive)
    }

    @Test fun graveInitializedWithEctophialAndPouchesShouldNotKeepThem() {
        val inventory = arrayOf(
                Items.ECTOPHIAL_4251.asItem(),
                Items.SMALL_POUCH_5509.asItem(),
                Items.MEDIUM_POUCH_5510.asItem(),
                Items.MEDIUM_POUCH_5511.asItem(),
                Items.LARGE_POUCH_5512.asItem(),
                Items.LARGE_POUCH_5513.asItem(),
                Items.GIANT_POUCH_5514.asItem(),
                Items.GIANT_POUCH_5515.asItem()
        )
        val p = TestUtils.getMockPlayer("gravetest")
        val grave = GraveController.produceGrave(GraveType.MEM_PLAQUE)
        grave.initialize(p, Location.create(0,0,0), inventory)

        Assertions.assertEquals(true, grave.getItems().isEmpty())
        Assertions.assertEquals(false, grave.isActive)
    }

    @Test fun graveInitializedWithDroppableUntradablesShouldKeepThem() {
        val inventory = arrayOf(
                Items.FIRE_CAPE_6570.asItem(),
                Items.RUNE_DEFENDER_8850.asItem()
        )
        val p = TestUtils.getMockPlayer("gravetest")
        val grave = GraveController.produceGrave(GraveType.MEM_PLAQUE)
        grave.initialize(p, Location.create(0,0,0), inventory)

        Assertions.assertEquals(2, grave.getItems().size)
        Assertions.assertEquals(true, grave.isActive)
    }

    @Test fun graveInitializedWithDestroyableItemsShouldNotKeepThem() {
        val inventory = arrayOf(
                Items.HOLY_GRAIL_19.asItem()
        )
        val p = TestUtils.getMockPlayer("gravetest")
        val grave = GraveController.produceGrave(GraveType.MEM_PLAQUE)
        grave.initialize(p, Location.create(0,0,0), inventory)

        Assertions.assertEquals(0, grave.getItems().size)
        Assertions.assertEquals(false, grave.isActive)
    }

    @Test fun graveInitializedWithReleasableItemsShouldNotKeepThem() {
        val inventory = arrayOf(
                Items.CHINCHOMPA_10033.asItem(),
                Items.CHINCHOMPA_9976.asItem(),
                Items.BABY_IMPLING_JAR_11238.asItem()
        )
        val p = TestUtils.getMockPlayer("gravetest")
        val grave = GraveController.produceGrave(GraveType.MEM_PLAQUE)
        grave.initialize(p, Location.create(0,0,0), inventory)

        Assertions.assertEquals(0, grave.getItems().size)
        Assertions.assertEquals(false, grave.isActive)
    }

    @Test fun graveInitializedWithItemThatHasDropTransformShouldContainTransformedItem() {
        //We actually don't have any items that have this implemented yet, but we should test it once we do.
    }

    @Test fun graveShouldSerializeAndDeserializeFromJsonCorrectly() {
        val inventory = arrayOf(
                Items.BRONZE_2H_SWORD_1307.asItem(),
                Items.BRONZE_AXE_1351.asItem()
        )

        val startTime = GameWorld.ticks
        val p = TestUtils.getMockPlayer("gravetest")
        val grave = GraveController.produceGrave(GraveType.MEM_PLAQUE)
        grave.initialize(p, Location.create(0,0,0), inventory)

        TestUtils.advanceTicks(30, false)
        val expectedTicksRemaining = secondsToTicks(GraveType.MEM_PLAQUE.durationMinutes * 60) - (GameWorld.ticks - startTime)
        Assertions.assertEquals(expectedTicksRemaining, grave.ticksRemaining)

        GraveController.serializeToServerStore()
        GraveController.activeGraves.remove(p.details.uid)
        GraveController.deserializeFromServerStore()

        val newGrave = GraveController.activeGraves[p.details.uid]
        Assertions.assertNotNull(newGrave)
        Assertions.assertEquals(expectedTicksRemaining, newGrave?.ticksRemaining ?: -1)
        Assertions.assertEquals(2, newGrave?.getItems()?.size ?: -1)

        val expectedItemDecayTick = GameWorld.ticks + expectedTicksRemaining
        Assertions.assertEquals(expectedItemDecayTick, newGrave?.getItems()?.get(0)?.decayTime ?: -1)
        Assertions.assertEquals(true, newGrave?.isActive)
    }

    @Test fun regularDeathShouldSpawnGraveWithItems() {
        val inventory = arrayOf(
                Items.RUNE_SCIMITAR_1333.asItem(),
                Items.RUNE_SCIMITAR_1333.asItem(),
                Items.RUNE_SCIMITAR_1333.asItem(), //none of these should be in the grave due to having higher value
                Items.YELLOW_BEAD_1472.asItem(),
                Items.RED_BEAD_1470.asItem()
        )
        val p = TestUtils.getMockPlayer("gravetester", IronmanMode.NONE, Rights.REGULAR_PLAYER)
        p.location = Location.create(0,0,0)
        p.setAttribute("tutorial:complete", true)

        for (item in inventory)
            p.inventory.add(item)

        p.finalizeDeath(null)

        val grave = GraveController.activeGraves[p.details.uid]
        Assertions.assertNotNull(grave)
        Assertions.assertEquals(2, grave?.getItems()?.size ?: -1)
        Assertions.assertEquals(false, grave?.getItems()?.map { it.id }?.contains(Items.RUNE_SCIMITAR_1333))
    }

    @Test fun skulledDeathShouldNotSpawnGrave() {
        val inventory = arrayOf(
                Items.ABYSSAL_WHIP_4151.asItem(),
                Items.ABYSSAL_WHIP_4151.asItem(),
                Items.ABYSSAL_WHIP_4151.asItem(),
                Items.ABYSSAL_WHIP_4151.asItem(),
                Items.ABYSSAL_WHIP_4151.asItem()
        )
        val p = TestUtils.getMockPlayer("gravetester", IronmanMode.NONE, Rights.REGULAR_PLAYER)
        p.location = Location.create(0,0,0)
        p.setAttribute("tutorial:complete", true)

        for (item in inventory)
            p.inventory.add(item)

        p.skullManager.isSkulled = true
        p.finalizeDeath(null)

        val grave = GraveController.activeGraves[p.details.uid]
        Assertions.assertNull(grave)
    }

    @Test fun creatingNewGraveWithGraveAlreadyActiveShouldDestroyOldGrave() {
        val inventory1 = arrayOf(
                Items.RUNE_SCIMITAR_1333.asItem()
        )
        val grave1 = GraveController.produceGrave(GraveType.MEM_PLAQUE)
        val p = TestUtils.getMockPlayer("gravetester")
        grave1.initialize(p, Location.create(0,0,0), inventory1)

        Assertions.assertEquals(true, grave1.isActive)
        Assertions.assertEquals(Items.RUNE_SCIMITAR_1333, GraveController.activeGraves[p.details.uid]?.getItems()?.getOrNull(0)?.id ?: -1)

        val inventory2 = arrayOf(
                Items.ABYSSAL_WHIP_4151.asItem()
        )
        val grave2 = GraveController.produceGrave(GraveType.MEM_PLAQUE)
        grave2.initialize(p, Location.create(0,0,0), inventory2)

        Assertions.assertEquals(false, grave1.isActive)
        Assertions.assertEquals(true, grave2.isActive)
        Assertions.assertEquals(Items.ABYSSAL_WHIP_4151, GraveController.activeGraves[p.details.uid]?.getItems()?.getOrNull(0)?.id ?: -1)
    }

    @Test fun deathWithOnly3ItemsShouldNotProduceAGrave() {
        val inventory = arrayOf(
                Items.RUNE_SCIMITAR_1333.asItem(),
                Items.RUNE_SCIMITAR_1333.asItem(),
                Items.RUNE_SCIMITAR_1333.asItem()
        )
        val p = TestUtils.getMockPlayer("gtester", IronmanMode.NONE, Rights.REGULAR_PLAYER)
        p.location = Location.create(0,0,0)
        p.setAttribute("tutorial:complete", true)

        for (item in inventory)
            p.inventory.add(item)

        p.finalizeDeath(null)

        Assertions.assertNull(GraveController.activeGraves[p.details.uid])
    }

    @Test fun deathInsideWildernessShouldNotProduceAGrave() {
        val inventory = arrayOf(
                Items.RUNE_SCIMITAR_1333.asItem(),
                Items.RUNE_SCIMITAR_1333.asItem(),
                Items.RUNE_SCIMITAR_1333.asItem(),
                Items.RUNE_SCIMITAR_1333.asItem(),
                Items.RUNE_SCIMITAR_1333.asItem()
        )
        val p = TestUtils.getMockPlayer("tester3333", IronmanMode.NONE, Rights.REGULAR_PLAYER)
        p.skullManager.isWilderness = true

        for (item in inventory) {
            p.inventory.add(item)
        }

        p.finalizeDeath(null)

        Assertions.assertNull(GraveController.activeGraves[p.details.uid])
    }

    @Test fun deathWithRFDGlovesShouldKeepRFDGloves() {
        val inventory = arrayOf(
            Items.GLOVES_7453.asItem(),
            Items.GLOVES_7454.asItem(),
            Items.GLOVES_7455.asItem(),
            Items.GLOVES_7456.asItem(),
            Items.GLOVES_7457.asItem(),
            Items.GLOVES_7458.asItem(),
            Items.GLOVES_7459.asItem(),
            Items.GLOVES_7460.asItem(),
            Items.GLOVES_7461.asItem(),
            Items.GLOVES_7462.asItem()
        )
        val p = TestUtils.getMockPlayer("glovetest", IronmanMode.NONE, Rights.REGULAR_PLAYER)

        for(item in inventory)
            p.inventory.add(item)

        p.finalizeDeath(null)

        val g = GraveController.activeGraves[p.details.uid]
        Assertions.assertNotNull(g)
        Assertions.assertEquals(7, g?.getItems()?.size ?: -1)
    }

    @Test fun shouldNotBeAbleToDropItemOnGrave() {
        val inventory = arrayOf(
            Items.RUNE_SCIMITAR_1333.asItem(),
            Items.RUNE_SCIMITAR_1333.asItem(),
            Items.RUNE_SCIMITAR_1333.asItem(),
            Items.RUNE_SCIMITAR_1333.asItem()
        )
        val p = TestUtils.getMockPlayer("droptest", IronmanMode.NONE, Rights.REGULAR_PLAYER)

        for (item in inventory)
            p.inventory.add(item)

        p.finalizeDeath(null)
        p.inventory.add(Items.RUNE_SCIMITAR_1333.asItem())

        val g = GraveController.activeGraves[p.details.uid]

        Assertions.assertNotNull(g)
        Assertions.assertEquals(p.location, g?.location)

        val canDrop = DropItemHandler.drop(p, p.inventory[0])
        Assertions.assertEquals(false, canDrop)
    }
}