package content.global.ame.events

import core.game.node.entity.player.Player
import content.global.ame.events.supriseexam.SurpriseExamUtils
import core.game.dialogue.DialogueFile

class MysteriousOldManDialogue(val type: String) : DialogueFile() {

    val CHOICE_STAGE = 50000

    override fun handle(componentID: Int, buttonID: Int) {

        if(type == "sexam" && stage < CHOICE_STAGE){
            npc("Would you like to come do a surprise exam?")
            stage = CHOICE_STAGE
        }

        else if(stage >= CHOICE_STAGE){
            when(stage) {
                CHOICE_STAGE -> options("Yeah, sure!", "No, thanks.").also { stage++ }
                CHOICE_STAGE.substage(1) -> when(buttonID){
                    1 -> {
                        end()
                        teleport(player!!,type)
                        content.global.ame.RandomEventManager.getInstance(player!!)?.event?.terminate()
                    }
                    2 -> {
                        end()
                        content.global.ame.RandomEventManager.getInstance(player!!)?.event?.terminate()
                    }
                }
            }
        }
    }
    fun teleport(player: Player,type: String){
        when(type){
            "sexam" -> SurpriseExamUtils.teleport(player)
        }
    }
}