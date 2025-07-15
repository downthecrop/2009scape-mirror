package content.global.handlers.iface.tabs

import content.global.skill.summoning.familiar.BurdenBeast
import core.api.*

import core.game.container.access.InterfaceContainer
import core.game.container.impl.EquipmentContainer
import core.game.global.action.EquipHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListeners
import core.game.interaction.InterfaceListener
import core.game.node.entity.combat.DeathTask
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.prayer.PrayerType
import core.tools.Log
import org.rs09.consts.Components
import org.rs09.consts.Items

class EquipmentTabInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        onOpen(ITEMS_KEPT_ON_DEATH_102) { player, component ->
            /**
             * (Highlight white items are auto destroyed on death Enum#616 (Items kept on death interface) TODO: Parse server sided
             * CS2 Script 118 - Items kept on death interface
             * Credit Woahscam for figuring this all out.
             * @arg_0 (Int): Zone type - 0 Default/1 Safe/2 POH/3 Castle Wars/4 Trouble Brewing/5 Barbarian Assault
             * @arg_1 (Int): Amount of items kept on death - 0/1/3/4
             * @arg_2 (Object): Item kept on death - slot 0 item id
             * @arg_3 (Object): Item kept on death - slot 1 item id
             * @arg_4 (Object): Item kept on death - slot 2 item id
             * @arg_5 (Object): Item kept on death - slot 3 item id
             * @arg_6 (Int): Player is skulled - 0 Not Skulled/1 Skulled
             * @arg_7 (Int): Player has BoB summoned with items - 0 BoB not summoned or has no items /1 BoB summoned with items
             * @arg_8 (String): String to append based on amount of items kept on death.
             */
            val zoneType = player.zoneMonitor.type
            val itemsKeptOnDeath = DeathTask.getContainers(player)[0]
            val amountKeptOnDeath = if (!player.skullManager.isSkulled && itemsKeptOnDeath.itemCount() < 3) {
                if (player.prayer[PrayerType.PROTECT_ITEMS]) 4 else 3
            } else {
                itemsKeptOnDeath.itemCount()
            }
            val slot0 = itemsKeptOnDeath.getId(0)
            val slot1 = itemsKeptOnDeath.getId(1)
            val slot2 = itemsKeptOnDeath.getId(2)
            val slot3 = itemsKeptOnDeath.getId(3)
            val hasSkull = if (player.skullManager.isSkulled) 1 else 0
            val beast: BurdenBeast? = if (player.familiarManager.hasFamiliar() && player.familiarManager.familiar.isBurdenBeast) player.familiarManager.familiar as BurdenBeast else null
            val hasBob = if (beast != null && !beast.container.isEmpty) 1 else 0
            val message = "You are skulled."
            val cs2Args = arrayOf<Any>(hasBob, hasSkull, slot3, slot2, slot1, slot0, amountKeptOnDeath, zoneType, message)

            if (amountKeptOnDeath > 4 && zoneType == 0) {
                log(this::class.java, Log.ERR, "Items kept on death interface should not contain more than 4 items when not in a safe zone!")
            }

            player.packetDispatch.sendRunScript(118, "siiooooii", *cs2Args)

            val settings = IfaceSettingsBuilder().enableAllOptions().build()
            player.packetDispatch.sendIfaceSettings(settings, 18, component.id, 0, itemsKeptOnDeath.itemCount())
            player.packetDispatch.sendIfaceSettings(settings, 21, component.id, 0, DeathTask.getContainers(player)[1].itemCount())
            return@onOpen true
        }

        on(Components.WORNITEMS_387) { player, component, opcode, buttonID, slot, itemID ->
            when (buttonID) {
                28 -> {
                    if (opcode == 81) EquipHandler.unequip(player, slot, itemID)
                    if (opcode == 206) operateItem(player, slot)
                }
                52 -> openInterface(player, ITEMS_KEPT_ON_DEATH_102)
                55 -> openInterface(player, Components.EQUIP_SCREEN2_667)
            }
            return@on true
        }

        onOpen(Components.EQUIP_SCREEN2_667) { player, component ->
            val settings = IfaceSettingsBuilder().enableAllOptions().build()
            player.packetDispatch.sendIfaceSettings(settings, 14, component.id, 0, 13)
            EquipmentContainer.update(player)
            openSingleTab(player, Components.INVENTORY_WEAR2_670)
            return@onOpen true
        }

        on(Components.EQUIP_SCREEN2_667) { player, _, opcode, buttonID, slot, itemID ->
            if (buttonID == 14) {
                when (opcode) {
                    9 -> sendMessage(player, player.equipment.get(slot).definition.examine)
                    155 -> EquipHandler.unequip(player, slot, itemID)
                    196 -> operateItem(player, slot)
                }
            }
            return@on true
        }

        onClose(Components.EQUIP_SCREEN2_667) { player, _ ->
            closeTabInterface(player)
            return@onClose true
        }

        onOpen(Components.INVENTORY_WEAR2_670) { player, component ->
            InterfaceContainer.generateItems(player, player.inventory.toArray(), arrayOf("Equip"), component.id, 0, 7, 4, 93)
            return@onOpen true
        }

        on(Components.INVENTORY_WEAR2_670) { player, _, opcode, _, slot, _ ->
            if (opcode == 9) sendMessage(player, player.inventory.get(slot).definition.examine)
            if (opcode == 155) equipItem(player, slot)
            return@on true
        }
    }

    private fun equipItem(player: Player, slot: Int) {
        val item = player.inventory.get(slot) ?: return

        if (item.definition.options.any { it in arrayOf("Equip", "Wield", "Wear") } || item.id == Items.BEER_1917) {
            InteractionListeners.run(item.id, IntType.ITEM, "equip", player, item)
        } else {
            sendMessage(player, "You can't wear that.")
        }
    }

    private fun operateItem(player: Player, slot: Int) {
        val item = player.equipment.get(slot) ?: return

        when {
            InteractionListeners.run(item.id, IntType.ITEM, "operate", player, item) -> return
            item.operateHandler?.handle(player, item, "operate") == true -> return
            else -> sendMessage(player, "You can't operate that.")
        }
    }

    companion object {
        private const val ITEMS_KEPT_ON_DEATH_102 = 102
    }
}