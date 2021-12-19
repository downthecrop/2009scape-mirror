package rs09.game.interaction.`object`

import api.*
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.player.link.emote.Emotes
import org.rs09.consts.NPCs
import rs09.game.interaction.InteractionListener

/**
 * Handles taunting of the demon in the wizard's tower
 * @author afaroutdude / Ceikry
 */
private const val BARS = 37668
class DemonTauntHandler : InteractionListener(){

    override fun defineListeners() {
        on(BARS,SCENERY,"taunt-through"){ player, _ ->
            sendMessage(player, "You taunt the demon, making it growl.")
            val demon = findLocalNPC(player, NPCs.LESSER_DEMON_82) ?: return@on true
            sendChat(demon, "Graaagh!")
            face(demon, player, 3)
            emote(player, Emotes.RASPBERRY)
            player.achievementDiaryManager.finishTask(player, DiaryType.LUMBRIDGE, 1, 13)
            return@on true
        }

    }
}