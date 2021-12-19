package rs09.game.interaction.item.withnpc

import api.removeItem
import api.sendChat
import api.sendMessage
import core.game.content.global.Bones
import core.game.node.entity.player.link.diary.DiaryType
import org.rs09.consts.NPCs
import rs09.game.interaction.InteractionListener

class BonesOnStrayDog : InteractionListener() {
    override fun defineListeners() {
        val bones = Bones.array
        val dogs = intArrayOf(NPCs.STRAY_DOG_4766, NPCs.STRAY_DOG_4767, NPCs.STRAY_DOG_5917, NPCs.STRAY_DOG_5918)

        onUseWith(NPC, bones, *dogs){player, used, with ->
            if(removeItem(player, used.asItem())){
                sendMessage(player, "You feed your dog bones.")
                sendChat(with.asNpc(), "Woof")
                player.achievementDiaryManager.finishTask(player, DiaryType.VARROCK, 0, 8)
            }
            return@onUseWith true
        }
    }
}