package rs09.game.interaction.item.withnpc

import api.sendNPCDialogue
import api.sendPlayerDialogue
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.Item
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import rs09.game.content.dialogue.DialogueFile
import rs09.game.interaction.InteractionListener

class PoisonChaliceOnKingArthur : InteractionListener() {
    override fun defineListeners() {
        onUseWith(NPC, Items.POISON_CHALICE_197, NPCs.KING_ARTHUR_251){player, used, with ->
            player.dialogueInterpreter.open(PoisonChaliceOnKingArthurDialogue(),with)
            return@onUseWith true
        }
    }
}

class PoisonChaliceOnKingArthurDialogue : DialogueFile(){
    var init = true
    override fun handle(componentID: Int, buttonID: Int) {
        if (init) stage = 0 ; init = false

        when(stage){
            0 -> sendNPCDialogue(player!!,this.npc!!.id,"You have chosen poorly",FacialExpression.SAD).also { stage++ }
            1 -> sendPlayerDialogue(player!!,"Excuse me?",FacialExpression.ANNOYED).also { stage++ }
            2 -> sendNPCDialogue(player!!,this.npc!!.id,"Sorry, I meant to say 'thank you'. Most refreshing.").also { stage++ }
            3 -> sendPlayerDialogue(player!!,"Are you sure that stuff is safe to drink?",FacialExpression.DISGUSTED_HEAD_SHAKE).also { stage++ }
            4 -> sendNPCDialogue(player!!,this.npc!!.id,"Oh yes, Stankers' creations may be dangerous for those with weak constitutions, but, personally. I find them rather invigorating.").also { stage++ }
            5 ->{ if (!player!!.achievementDiaryManager.hasCompletedTask(DiaryType.SEERS_VILLAGE,0,3)){
                    player!!.achievementDiaryManager.finishTask(player!!,DiaryType.SEERS_VILLAGE,0,3)
                    player!!.inventory.remove(Item(197,1))
                    end()
                }else {
                player!!.inventory.remove(Item(197,1))
                    end()
                }
            }
        }
    }
}