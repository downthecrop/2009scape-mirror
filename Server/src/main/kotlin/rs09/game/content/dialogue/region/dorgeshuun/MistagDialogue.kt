package rs09.game.content.dialogue.region.dorgeshuun

import core.game.component.Component
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import rs09.game.content.quest.members.thelosttribe.GoblinFollower
import rs09.game.content.quest.members.thelosttribe.MistagLTDialogue
import rs09.tools.END_DIALOGUE
import rs09.tools.START_DIALOGUE

@Initializable
class MistagDialogue (player: Player? = null) : DialoguePlugin(player){
    override fun newInstance(player: Player?): DialoguePlugin {
        return MistagDialogue(player)
    }

    override fun npc(vararg messages: String?): Component {
        return npc(FacialExpression.OLD_NORMAL,*messages)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        val ltStage = player.questRepository.getStage("Lost Tribe")

        if(args.size > 1 && args[1] == "greeting"){
            npc("A human knows ancient greeting?")
            loadFile(MistagLTDialogue(true,ltStage))
            return true
        }
        if(!player.getAttribute("mistag-greeted",false)){
            npc("Who...who are you? How did you get in here?")
            stage = -100
            return true
        }

        if(ltStage == 45){
            npc("Greetings, friend. I am sorry I panicked when I saw you.")
            loadFile(MistagLTDialogue(false,ltStage))
            return true
        } else if(ltStage == 50){
            npc("Hello, friend?")
            loadFile(MistagLTDialogue(false,ltStage))
            return true
        }

        npc("Hello friend!").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            //Pre-greeting
            -100 -> npc("Help! A surface dweller this deep in our mines? We will","all be destroyed!").also { stage++ }
            -99 -> end()

            //Normal Dialogue
            START_DIALOGUE -> options("May I mine the rocks here?","Can you show me the way out?").also { stage++ }
            1 -> when(buttonId){
                1 -> player("May I mine the rocks here?").also { stage = 10 }
                2 -> player("Can you show me the way out of the mine?").also { stage = 20 }
            }
            10 -> npc("Certainly, friend!").also { stage = END_DIALOGUE }
            20 -> npc("Certainly!").also { GoblinFollower.sendToLumbridge(player); stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(2084)
    }

}