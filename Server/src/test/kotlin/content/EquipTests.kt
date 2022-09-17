package content

import TestUtils
import api.EquipmentSlot
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.rs09.consts.Items
import rs09.game.content.global.action.EquipHandler
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.IntType
import rs09.game.interaction.InteractionListeners

class EquipTests {
    companion object {
        init {TestUtils.preTestSetup(); EquipHandler().defineListeners()}
    }

    @Test fun equipShouldFireEquipListeners() {
        var didRun = false
        val listener = object : InteractionListener {
            override fun defineListeners() {
                onEquip(4151) {_,_ -> didRun = true; return@onEquip true}
            }
        }
        listener.defineListeners()

        val p = TestUtils.getMockPlayer("bill")
        p.inventory.add(Item(4151))
        InteractionListeners.run(4151, IntType.ITEM, "equip", p, p.inventory[0])

        Assertions.assertEquals(true, didRun)
    }

    @Test fun unequipShouldFireUnequipListeners() {
        var didRun = false
        val listener = object : InteractionListener {
            override fun defineListeners() {
                onUnequip(4151) {_,_ -> didRun = true; return@onUnequip true}
            }
        }
        listener.defineListeners()

        val p = TestUtils.getMockPlayer("bill")
        p.equipment.replace(Item(4151), EquipmentSlot.WEAPON.ordinal)
        EquipHandler.unequip(p, EquipmentSlot.WEAPON.ordinal, 4151)

        Assertions.assertEquals(true, didRun)
    }

    @Test fun equippingItemThatReplacesAnotherItemShouldCallUnequipListenersForTheReplacedItem() {
        var didRun = false
        val listener = object : InteractionListener {
            override fun defineListeners() {
                onUnequip(4151) {_,_ -> didRun = true; return@onUnequip true}
            }
        }
        listener.defineListeners()

        val p = TestUtils.getMockPlayer("bill")
        p.equipment.replace(Item(4151), EquipmentSlot.WEAPON.ordinal)
        p.inventory.add(Item(1333))
        p.skills.staticLevels[Skills.ATTACK] = 40
        InteractionListeners.run(1333, IntType.ITEM, "equip", p, p.inventory[0])

        Assertions.assertEquals(true, didRun, p.equipment.toString())
    }

    @Test fun equippingItemShouldAddUnequippedItemToExistingStackInInventory() {
        val p = TestUtils.getMockPlayer("bill")
        p.skills.staticLevels[Skills.ATTACK] = 99
        p.equipment.replace(Item(Items.BRONZE_DART_806, 1000), EquipmentSlot.WEAPON.ordinal)
        p.inventory.add(Item(Items.BRONZE_DART_806, 1000))
        p.inventory.add(Item(Items.RUNE_SCIMITAR_1333))

        InteractionListeners.run(Items.RUNE_SCIMITAR_1333, IntType.ITEM, "equip", p, p.inventory[1])
        Assertions.assertEquals(2000, p.inventory.getAmount(Items.BRONZE_DART_806), "\n" + p.inventory.toString() + "\n" + p.equipment.toString())
    }

    @Test fun equippingItemThatUnequipsTwoItemsShouldBeAllowedWithOnlyOneInitiallyFreeSlot() {
        val p = TestUtils.getMockPlayer("bill")
        p.equipment.replace(Item(Items.BRONZE_SWORD_1277), EquipmentSlot.WEAPON.ordinal)
        p.equipment.replace(Item(Items.WOODEN_SHIELD_1171), EquipmentSlot.SHIELD.ordinal)

        p.inventory.add(Item(Items.CUP_OF_TEA_1978, 26))
        p.inventory.add(Item(Items.BRONZE_2H_SWORD_1307))

        Assertions.assertEquals(1, p.inventory.freeSlots())

        InteractionListeners.run(Items.BRONZE_2H_SWORD_1307, IntType.ITEM, "equip", p, p.inventory[26])
        Assertions.assertEquals(0, p.inventory.freeSlots())
        Assertions.assertEquals(Items.BRONZE_SWORD_1277, p.inventory[26].id)
        Assertions.assertEquals(Items.WOODEN_SHIELD_1171, p.inventory[27].id)
    }

    @Test fun shouldNotBeAbleToEquipA2HWeaponAndAShieldAtTheSameTime() {
        val p = TestUtils.getMockPlayer("bill")
        p.inventory.add(Item(Items.BRONZE_2H_SWORD_1307))
        p.inventory.add(Item(Items.WOODEN_SHIELD_1171))

        InteractionListeners.run(Items.BRONZE_2H_SWORD_1307, IntType.ITEM, "equip", p, p.inventory[0])
        Assertions.assertEquals(Items.BRONZE_2H_SWORD_1307, p.equipment.get(EquipmentSlot.WEAPON.ordinal).id)

        InteractionListeners.run(Items.WOODEN_SHIELD_1171, IntType.ITEM, "equip", p, p.inventory[1])
        Assertions.assertEquals(Items.BRONZE_2H_SWORD_1307, p.inventory[1]?.id ?: -1)
    }

    @Test fun equippingShieldShouldNotUnequipOneHandedWeapon() {
        val p = TestUtils.getMockPlayer("bill")
        p.inventory.add(Item(Items.BRONZE_SWORD_1277))
        p.inventory.add(Item(Items.WOODEN_SHIELD_1171))

        InteractionListeners.run(Items.BRONZE_SWORD_1277, IntType.ITEM, "equip", p, p.inventory[0])
        Assertions.assertEquals(Items.BRONZE_SWORD_1277, p.equipment[EquipmentSlot.WEAPON.ordinal].id)

        InteractionListeners.run(Items.WOODEN_SHIELD_1171, IntType.ITEM, "equip", p, p.inventory[1])
        Assertions.assertEquals(Items.BRONZE_SWORD_1277, p.equipment[EquipmentSlot.WEAPON.ordinal]?.id ?: -1)
    }

    @Test fun equippingStackableItemShouldAddToExistingStackInEquipmentIfApplicable() {
        val p = TestUtils.getMockPlayer("bill")
        p.equipment.replace(Item(Items.BRONZE_ARROW_882, 100), EquipmentSlot.AMMO.ordinal)
        p.inventory.add(Item(Items.BRONZE_ARROW_882, 200))

        InteractionListeners.run(Items.BRONZE_ARROW_882, IntType.ITEM, "equip", p, p.inventory[0])
        Assertions.assertEquals(300, p.equipment[EquipmentSlot.AMMO.ordinal].amount)
    }

    @Test fun swappingEquipmentShouldPreserveInventorySlots() {
        val p = TestUtils.getMockPlayer("bill")
        p.skills.staticLevels[Skills.ATTACK] = 70
        p.skills.staticLevels[Skills.DEFENCE] = 40
        p.equipment.replace(Item(Items.ABYSSAL_WHIP_4151), EquipmentSlot.WEAPON.ordinal)
        p.equipment.replace(Item(Items.RUNE_DEFENDER_8850), EquipmentSlot.SHIELD.ordinal)
        p.inventory.add(Item(Items.DRAGON_CLAWS_14484), true, 25)
        Assertions.assertEquals(p.inventory.getSlot(Item(Items.DRAGON_CLAWS_14484)), 25)

        InteractionListeners.run(Items.DRAGON_CLAWS_14484, IntType.ITEM, "equip", p, p.inventory[25])
        Assertions.assertEquals(Items.DRAGON_CLAWS_14484, p.equipment.get(EquipmentSlot.WEAPON.ordinal).id)
        Assertions.assertEquals(p.inventory.getSlot(Item(Items.ABYSSAL_WHIP_4151)), 25)
        Assertions.assertEquals(p.inventory.getSlot(Item(Items.RUNE_DEFENDER_8850)), 0)
    }
}
