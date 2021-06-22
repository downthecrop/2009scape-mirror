package rs09.game.interaction.`object`

import api.ContentAPI
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.player.link.emote.Emotes
import core.game.world.map.RegionManager
import org.rs09.consts.NPCs
import rs09.game.interaction.InteractionListener

/**
 * Handles taunting of the demon in the wizard's tower
 * @author afaroutdude / Ceikry
 */
private const val BARS = 37668
class DemonTauntHandler : InteractionListener(){

    override fun defineListeners() {
        on(BARS,OBJECT,"taunt-through"){player,_ ->
            ContentAPI.sendMessage(player, "You taunt the demon, making it growl.")
            val demon = ContentAPI.findLocalNPC(player, NPCs.LESSER_DEMON_82) ?: return@on true
            ContentAPI.sendChat(demon, "Graaagh!")
            ContentAPI.face(demon, player, 3)
            ContentAPI.emote(player, Emotes.RASPBERRY)
            player.achievementDiaryManager.finishTask(player, DiaryType.LUMBRIDGE, 1, 13)
            return@on true
        }

    }
}