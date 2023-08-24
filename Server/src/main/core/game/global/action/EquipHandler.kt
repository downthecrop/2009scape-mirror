package core.game.global.action

import core.game.event.ItemEquipEvent
import core.game.event.ItemUnequipEvent
import core.game.container.impl.EquipmentContainer
import content.global.handlers.item.equipment.brawling_gloves.BrawlingGlovesManager
import content.global.skill.slayer.SlayerEquipmentFlags
import core.api.playAudio
import core.game.node.Node
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.audio.Audio
import core.plugin.Plugin
import core.game.interaction.InteractionListener
import core.game.interaction.IntType
import core.game.interaction.InteractionListeners
import core.game.system.config.ItemConfigParser
import org.rs09.consts.Sounds

/**
 * Represents the equipment equipping handler plugin.
 * @author Ceikry
 * @author Woah
 */
class EquipHandler : InteractionListener {

    override fun defineListeners() {
        on(IntType.ITEM, "equip", "wield", "wear") { player, node ->
            handleEquip(player, node)
            return@on true
        }
    }

    fun handleEquip(player: Player, node: Node) {
        val item = node.asItem()

        if (item == null || player.inventory[item.slot] != item || item.name.toLowerCase().contains("goblin mail")) {
            return
        }

        val equipStateListener = item.definition.getConfiguration<Plugin<Any>>("equipment", null)
        if (equipStateListener != null) {
            val bool = equipStateListener.fireEvent("equip", player, item)
            if (bool != true) {
                return
            }
        }
        if (!InteractionListeners.run(node.id, player, node, true)) {
            return
        }

        val lock = player.locks.equipmentLock
        if (lock != null && lock.isLocked) {
            if (lock.message != null) {
                player.packetDispatch.sendMessage(lock.message)
            }
            return
        }

        if (player.equipment.add(item, item.slot, true, true)) {
            //check if a brawling glove is being equipped and register it
            if (item.id in 13845..13857) {
                player.debug("Registering gloves... ID: " + item.id)
                BrawlingGlovesManager.getInstance(player).registerGlove(item.id)
            }

            player.dialogueInterpreter.close()

            /* TODO: Send different equip sound based on what is being equip.*/
            playAudio(player, item.definition.getConfiguration(ItemConfigParser.EQUIP_AUDIO, 2244))

            if (player.properties.autocastSpell != null) {
                val itemEquipmentSlot = item.definition.getConfiguration<Int>(ItemConfigParser.EQUIP_SLOT, -1)

                if (itemEquipmentSlot == EquipmentContainer.SLOT_WEAPON) {
                    player.properties.autocastSpell = null
                    val wif = player.getExtension<WeaponInterface>(WeaponInterface::class.java)
                    wif.selectAutoSpell(-1, true)
                    wif.openAutocastSelect()
                }
            }

            if (SlayerEquipmentFlags.isSlayerEq(item.id)) {
                SlayerEquipmentFlags.updateFlags(player)
            }

            player.dispatch(ItemEquipEvent(item.id, item.slot))
        }
    }

    companion object {
        /**
         * Unequips an item.
         * @param player the player.
         * @param slot the slot.
         * @param itemId the item id.
         */
        @JvmStatic
        fun unequip(player: Player, slot: Int, itemId: Int) {
            if (slot < 0 || slot > 13) {
                return
            }
            val item = player.equipment[slot] ?: return
            val lock = player.locks.equipmentLock
            if (lock != null && lock.isLocked) {
                if (lock.message != null) {
                    player.packetDispatch.sendMessage(lock.message)
                }
                return
            }
            if (slot == EquipmentContainer.SLOT_WEAPON) {
                player.packetDispatch.sendString("", 92, 0)
            }
            val maximumAdd = player.inventory.getMaximumAdd(item)
            if (maximumAdd < item.amount) {
                player.packetDispatch.sendMessage("Not enough free space in your inventory.")
                return
            }
            val plugin = item.definition.getConfiguration<Plugin<Any>>("equipment", null)
            if (plugin != null) {
                if (!(plugin.fireEvent("unequip", player, item) as Boolean)) {
                    return
                }
            }
            if (!InteractionListeners.run(itemId, player, item, false)) {
                return
            }
            if (player.equipment.remove(item)) {
                /* TODO: Send different unequip sound based on what is being unequipped.*/
                playAudio(player, Sounds.EQUIP_FUN_2238)
                player.dialogueInterpreter.close()
                player.inventory.add(item)

                player.dispatch(ItemUnequipEvent(itemId, slot))
            }

            if (SlayerEquipmentFlags.isSlayerEq(item.id)) {
                SlayerEquipmentFlags.updateFlags(player)
            }
        }
    }
}
