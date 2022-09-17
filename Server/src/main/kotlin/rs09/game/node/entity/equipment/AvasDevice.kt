package rs09.game.node.entity.equipment

import api.*
import api.events.EventHook
import api.events.TickEvent
import core.game.node.entity.Entity
import core.game.node.entity.combat.equipment.Ammunition
import core.game.node.entity.player.Player
import org.rs09.consts.Items
import rs09.game.Event
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.IntType
import rs09.game.system.config.ItemConfigParser
import rs09.tools.secondsToTicks
import rs09.tools.stringtools.colorize

/**
 * Handles Ava's device
 * @source https://runescape.wiki/w/Ava%27s_accumulator?oldid=2097350
 */
class AvasDevice : InteractionListener, EventHook<TickEvent> {
    override fun defineListeners() {
        onEquip(devices) { player, _ ->
            if (!isQuestComplete(player, "Animal Magnetism")) {
                sendMessage(player, "You need to complete Animal Magnetism to equip this.")
                return@onEquip false
            }

            if (attractEnabled(player))
                player.hook(Event.Tick, this)

            setAttribute(player, LAST_TICK, getWorldTicks()) //set this on equip so can't be spam-re-equipped to spawn infinite items.
            return@onEquip true
        }
        onUnequip(devices) { player, _ ->
            if (attractEnabled(player))
                player.unhook(this)
            return@onUnequip true
        }
        on(devices, IntType.ITEM, "operate") { player, _ ->
            val attract = !attractEnabled(player)
            setAttribute(player, ATTRACT_ENABLED, attract)
            sendMessage(
                player,
                colorize(
                    "Ava's device will ${if (attract) "now" else "no longer"} randomly collect loot for you.",
                    "990000"
                )
            )
            if (attract) {
                player.hook(Event.Tick, this)
            } else {
                player.unhook(this)
            }
            return@on true
        }
    }

    override fun process(entity: Entity, event: TickEvent) {
        if (entity !is Player) {
            entity.unhook(this)
            return
        }

        if (getWorldTicks() - getLastTick(entity) < attractDelay)
            return
        else
            setAttribute(entity, LAST_TICK, getWorldTicks())

        if (isInterfered(entity)) {
            sendMessage(entity, "Your armour interferes with Ava's device.")
            return
        }

        val wornId = getItemFromEquipment(entity, EquipmentSlot.CAPE)?.id ?: -1

        val reward = when (wornId) {
            Items.AVAS_ACCUMULATOR_10499 -> ACCUMULATOR_REWARDS
            Items.AVAS_ATTRACTOR_10498 -> ATTRACTOR_REWARDS
            else -> {
                entity.unhook(this)
                return
            }
        }.random()

        if (equipSlot(reward) == EquipmentSlot.AMMO) {
            val equippedId = getItemFromEquipment(entity, EquipmentSlot.AMMO)?.id ?: -1
            if (reward == equippedId || equippedId == -1) {
                entity.equipment.add(reward.asItem(), true, false)
                return
            }
        }

        addItemOrDrop(entity, reward)
    }

    private fun attractEnabled(entity: Entity) : Boolean {
        return getAttribute(entity, ATTRACT_ENABLED, true) //defaults to enabled
    }

    private fun getLastTick(entity: Entity) : Int {
        return getAttribute(entity, LAST_TICK, 0)
    }

    private fun isInterfered(player: Player) : Boolean {
        val chestPiece = getItemFromEquipment(player, EquipmentSlot.CHEST)
        val modelId = chestPiece?.definition?.maleWornModelId1 ?: -1
        return modelId != -1 && modelId in metalBodies
    }

    companion object {
        const val ATTRACT_ENABLED = "/save:avadevice:attract"
        const val LAST_TICK = "avadevice:tick"
        val devices = intArrayOf(Items.AVAS_ACCUMULATOR_10499, Items.AVAS_ATTRACTOR_10498)
        val metalBodies = intArrayOf(301, 306, 3379)
        val attractDelay = secondsToTicks(180)
        val ATTRACTOR_REWARDS = arrayOf(Items.IRON_BAR_2351, Items.IRON_KNIFE_863,Items.IRON_DART_807,Items.IRON_DAGGER_1203,Items.IRON_BOLTS_9140,Items.IRON_ARROW_884,Items.IRON_ORE_440,Items.COPPER_ORE_436,Items.IRON_FULL_HELM_1153,Items.IRON_2H_SWORD_1309,Items.STEEL_BAR_2353)
        val ACCUMULATOR_REWARDS = arrayOf(Items.STEEL_BAR_2353,Items.STEEL_2H_SWORD_1311,Items.STEEL_KNIFE_865,Items.STEEL_DAGGER_1207,Items.STEEL_MED_HELM_1141,Items.STEEL_DART_808,Items.STEEL_BOLTS_9141,Items.STEEL_ARROW_886,Items.IRON_BAR_2351)
    }
}