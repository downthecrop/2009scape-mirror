package rs09.game.interaction.item

import api.animate
import api.sendChat
import api.stopWalk
import org.rs09.consts.Animations
import org.rs09.consts.Items
import rs09.game.interaction.InteractionListener

/**
 * Interaction listener for the Toy Horsey item
 * @author Woah
 */
class ToyHorseListener : InteractionListener() {

    // Map of horse item ids to their correct emote
    val HORSEY_MAP = mapOf(
        Items.TOY_HORSEY_2520 to Animations.HUMAN_PLAY_WITH_BROWN_HORSE_918,
        Items.TOY_HORSEY_2522 to Animations.HUMAN_PLAY_WITH_WHITE_HORSE_919,
        Items.TOY_HORSEY_2524 to Animations.HUMAN_PLAY_WITH_BLACK_HORSE_920,
        Items.TOY_HORSEY_2526 to Animations.HUMAN_PLAY_WITH_GRAY_HORSE_921
    )

    // Array of phrases used during the interaction
    val PHRASES = arrayOf(
        "Come-on Dobbin, we can win the race!",
        "Hi-ho Silver, and away",
        "Neaahhhyyy! Giddy-up horsey!"
    )

    override fun defineListeners() {
        on(HORSEY_MAP.keys.toIntArray(), ITEM, "play-with") { player, node ->
            // "high-priority" interaction, so movement is stopped
            stopWalk(player)
            animate(player, HORSEY_MAP.get(node.id))
            sendChat(player, PHRASES.random())
            return@on true
        }
    }
}