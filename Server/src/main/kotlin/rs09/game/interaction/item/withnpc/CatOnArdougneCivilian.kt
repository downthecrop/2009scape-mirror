package rs09.game.interaction.item.withnpc

import api.Container
import api.*
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import rs09.game.interaction.InteractionListener

class CatOnArdougneCivilian: InteractionListener() {

    private val civilians = intArrayOf(
        NPCs.CIVILIAN_785,
        NPCs.CIVILIAN_786,
        NPCs.CIVILIAN_787
    )

    private val cats = intArrayOf(
        Items.PET_CAT_1561,
        Items.PET_CAT_1562,
        Items.PET_CAT_1563,
        Items.PET_CAT_1564,
        Items.PET_CAT_1565,
        Items.PET_CAT_1566,
        Items.OVERGROWN_CAT_1567,
        Items.OVERGROWN_CAT_1568,
        Items.OVERGROWN_CAT_1569,
        Items.OVERGROWN_CAT_1570,
        Items.OVERGROWN_CAT_1571,
        Items.OVERGROWN_CAT_1572,
        Items.LAZY_CAT_6551,
        Items.LAZY_CAT_6552,
        Items.LAZY_CAT_6553,
        Items.LAZY_CAT_6554,
        Items.WILY_CAT_6555,
        Items.WILY_CAT_6556,
        Items.WILY_CAT_6557,
        Items.WILY_CAT_6558,
        Items.WILY_CAT_6559,
        Items.WILY_CAT_6560,
        Items.HELL_CAT_7582,
        Items.OVERGROWN_HELLCAT_7581,
        Items.LAZY_HELL_CAT_7584,
        Items.WILY_HELLCAT_7585,
    )

    override fun defineListeners() {
        onUseWith(NPC,cats,*civilians){player, used, _ ->
            sendItemDialogue(player,Items.DEATH_RUNE_560,"You hand over the cat.<br>You are given 100 Death Runes.")
            player.familiarManager.removeDetails(used.id)
            removeItem(player,used,Container.INVENTORY)
            addItem(player,Items.DEATH_RUNE_560,100)
            return@onUseWith true;
        }
    }
}