package rs09.game.content.global.action

import core.game.container.impl.EquipmentContainer
import core.game.node.Node
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.audio.Audio
import core.game.node.entity.player.link.diary.DiaryType
import core.game.system.task.Pulse
import core.game.world.map.zone.ZoneBorders
import core.plugin.Plugin
import rs09.game.interaction.OptionListener
import rs09.game.system.config.ItemConfigParser
import rs09.game.world.GameWorld
import rs09.tools.Items

/**
 * Represents the equipment equipping handler plugin.
 * @author Ceikry
 * @author Woah
 */
class EquipHandler : OptionListener() {

    override fun defineListeners() {

        on(ITEM,"equip","wield","wear"){player,node ->
            GameWorld.Pulser.submit(object : Pulse(){
                override fun pulse(): Boolean {
                    handleEquip(player,node)
                    return true
                }
            })
            return@on true
        }



    }

    fun handleEquip(player: Player,node: Node){
        val item = node.asItem()

        if(item == null || player.inventory[item.slot] != item){
            return
        }

        val equipStateListener = item.definition.getConfiguration<Plugin<Any>>("equipment", null)
        if(equipStateListener != null){
            val bool = equipStateListener.fireEvent("equip",player,item)
            if(bool != true){
                return
            }
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
                player.brawlingGlovesManager.registerGlove(item.id)
            }
            if (item.id == Items.BLACK_CHAINBODY_1107 && player.getAttribute("diary:falador:black-chain-bought", false)
                && ZoneBorders(2969, 3310, 2975, 3314, 0).insideBorder(player)
            ) {
                player.achievementDiaryManager.finishTask(player, DiaryType.FALADOR, 0, 2)
            }
            player.dialogueInterpreter.close()
            player.audioManager.send(item.definition.getConfiguration(ItemConfigParser.EQUIP_AUDIO, 2244))
            if (player.properties.autocastSpell != null) {
                player.properties.autocastSpell = null
                val wif = player.getExtension<WeaponInterface>(WeaponInterface::class.java)
                wif.selectAutoSpell(-1, true)
                wif.openAutocastSelect()
            }
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
            if (player.equipment.remove(item)) {
                player.audioManager.send(Audio(2238, 10, 1))
                player.dialogueInterpreter.close()
                player.inventory.add(item)
            }
        }
    }
}