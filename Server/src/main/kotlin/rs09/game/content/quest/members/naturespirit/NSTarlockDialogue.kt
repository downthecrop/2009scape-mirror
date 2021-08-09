package rs09.game.content.quest.members.naturespirit

import api.ContentAPI
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import org.rs09.consts.Items
import rs09.tools.END_DIALOGUE

class NSTarlockDialogue(player: Player? = null) : DialoguePlugin(player) {
    var questStage = 0

    override fun newInstance(player: Player?): DialoguePlugin {
        return NSTarlockDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        val quest = player.questRepository.getQuest("Nature Spirit")
        questStage = quest.getStage(player)

        when(questStage){
            10 -> sendDialogue("A shifting apparition appears in front of you.")
            else -> return false
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        if(questStage == 10){
            when(stage){
                0 -> playerl(FacialExpression.HALF_ASKING, "Hello?").also { stage++ }
                1 -> if(ContentAPI.inEquipment(player, Items.GHOSTSPEAK_AMULET_552)){
                    npcl(FacialExpression.EXTREMELY_SHOCKED, "Oh, I understand you! At last, someone who doesn't just mumble. I understand what you're saying!").also { stage++ }
                } else npcl(FacialExpression.HALF_GUILTY, "OooOOoOOoOOOOo.")

                2 -> options("I'm wearing an amulet of ghost speak!","How long have you been a ghost?", "What's it like being a ghost?", "Ok, thanks.").also { stage++ }
                3 -> when(buttonId){
                    1 -> playerl(FacialExpression.NEUTRAL, "I'm wearing an amulet of ghost speak!").also { stage = 10 }
                    2 -> playerl(FacialExpression.NEUTRAL, "How long have you been a ghost?").also { stage = 20 }
                    3 -> playerl(FacialExpression.NEUTRAL, "What's it like being a ghost?").also { stage = 30 }
                    4 -> playerl(FacialExpression.NEUTRAL, "Ok, thanks.").also { stage = END_DIALOGUE }
                }

                10 -> npcl(FacialExpression.HALF_GUILTY, "Why you poor fellow, have you passed away and you want to send a message back to a loved one?").also { stage++ }
                11 -> playerl(FacialExpression.HALF_THINKING, "Err.. Not exactly...").also { stage++ }
                12 -> npcl(FacialExpression.HALF_GUILTY, "You have come to haunt my dreams until I pass on your message to a dearly loved one. I understand. Pray, tell me who would you like me to pass a message on to?").also { stage++ }
                13 -> playerl(FacialExpression.NEUTRAL, "Ermm, you don't understand... It's just that...").also { stage++ }
                14 -> npcl(FacialExpression.HALF_GUILTY, "Yes!").also { stage++ }
                15 -> playerl(FacialExpression.NEUTRAL, "Well please don't be upset or anything... But you're the ghost!").also { stage++ }
                16 -> npcl(FacialExpression.HALF_GUILTY, "Don't be silly now! That in no way reflects the truth!").also { stage = 2 }

            }
        }
    }

    override fun getIds(): IntArray {
        TODO("Not yet implemented")
    }

}