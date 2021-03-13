package rs09.game.interaction.`object`

import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.player.link.emote.Emotes
import core.game.world.map.RegionManager
import rs09.game.interaction.InteractionListener

/**
 * Handles taunting of the demon in the wizard's tower
 * @author afaroutdude / Ceikry
 */
private const val BARS = 37668
class DemonTauntHandler : InteractionListener(){

    override fun defineListeners() {
        on(BARS,OBJECT,"taunt-through"){player,_ ->
            player.packetDispatch.sendMessage("You taunt the demon, making it growl.")
            val localNpcs = RegionManager.getLocalNpcs(player)
            player.animator.animate(Emotes.RASPBERRY.animation)
            for (npc in localNpcs) {
                if (npc.id == 82) {
                    npc.sendChat("Graaaagh!", 1)
                    player.faceLocation(npc.location)
                }
            }
            player.achievementDiaryManager.finishTask(player, DiaryType.LUMBRIDGE, 1, 13)
            return@on true
        }

    }
}