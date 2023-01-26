package content.region.fremennik.rellekka.quest.thefremenniktrials

import core.api.addItem
import core.api.removeItem
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable

@Initializable
class SkulgrimenDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if(player?.inventory?.contains(3703,1) == true){
            playerl(core.game.dialogue.FacialExpression.HAPPY,"Hi there. I got your fish, so can I have that bowstring for Sigli now?")
            stage = 20
            return true
        }
        else if(player?.inventory?.contains(3702,1) == true){
            playerl(core.game.dialogue.FacialExpression.ASKING,"So about this bowstring... was it hard to make or something?")
            stage = 25
            return true
        }
        else if(player?.getAttribute("sigmundreturning",false) == true){
            playerl(core.game.dialogue.FacialExpression.ASKING,"Is this trade item for you?")
            stage = 26
            return true
        }
        if(player?.getAttribute("sigmund-steps", 0) == 7){
            playerl(core.game.dialogue.FacialExpression.ASKING,"I don't suppose you have any idea where I could find an exotic and extremely odd fish, do you?")
            stage = 15
            return true
        }
        else if(player?.getAttribute("sigmund-steps", 0) == 6){
            playerl(core.game.dialogue.FacialExpression.ASKING,"I don't suppose you have any idea where I could find a finely balanced custom bowstring, do you?")
            stage = 1
            return true
        }
        else if(player.questRepository.isComplete("Fremennik Trials")){
            npcl(core.game.dialogue.FacialExpression.HAPPY,"Hello again, ${player.getAttribute("fremennikname","ringo")}. Come to see what's for sale?")
            stage = 1001
            return true
        }
        else{
            playerl(core.game.dialogue.FacialExpression.HAPPY,"Hello!")
            stage = 100
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            1 -> npcl(core.game.dialogue.FacialExpression.THINKING,"Aye, I have a few in stock. What would an outerlander be wanting with equipment like that?").also { stage++ }
            2 -> playerl(core.game.dialogue.FacialExpression.HAPPY,"It's for Sigli. It needs to be weighted precisely to suit his hunting bow.").also { stage++ }
            3 -> npcl(core.game.dialogue.FacialExpression.HAPPY,"For Sigli eh? Well, I made his bow in the first place, so I'll be able to select the right string for you... just one small problem.").also { stage++ }
            4 -> playerl(core.game.dialogue.FacialExpression.THINKING,"What's that?").also { stage++ }
            5 -> npcl(core.game.dialogue.FacialExpression.THINKING,"This string you'll be wanting... Very special it is. Take a lot of time to recreate. Not sure you have the cash for it.").also { stage++ }
            6 -> playerl(core.game.dialogue.FacialExpression.THINKING,"Then maybe you'll accept something else...?").also { stage++ }
            7 -> npcl(core.game.dialogue.FacialExpression.HAPPY,"Heh. Good thinking outerlander. Well, it's true, there is more to life than just making money. Making weapons is good money, but it's not why I do it. I'll tell you what.").also { stage++ }
            8 -> npcl(core.game.dialogue.FacialExpression.HAPPY,"I heard a rumour that one of the fishermen down by the docks caught some weird looking fish as they were fishing the other day. From what I hear this fish is unique.").also { stage++ }
            9 -> npcl(core.game.dialogue.FacialExpression.HAPPY,"From what I hear this fish is unique. Nobody's ever seen its like before. This intrigues me. I'd like to have it for myself.").also { stage++ }
            10 -> npcl(core.game.dialogue.FacialExpression.HAPPY,"Make a good trophy. You get me that fish, I give you the bowstring. What do you say? We got a deal?").also { stage++ }
            11 -> playerl(core.game.dialogue.FacialExpression.HAPPY,"Sounds good to me.").also {
                player?.incrementAttribute("sigmund-steps",1)
                stage = 1000
            }
            15 -> npcl(core.game.dialogue.FacialExpression.EXTREMELY_SHOCKED,"What? There's another one?").also { stage++ }
            16 -> playerl(core.game.dialogue.FacialExpression.ANNOYED,"Er... no, it's the one for you that I'm looking for...").also { stage++ }
            17 -> npcl(core.game.dialogue.FacialExpression.ANNOYED,"Ah. I see. I already told you. Some guy down by the docks was bragging. Best ask there, I reckon.").also { stage = 1000 }

            20 -> npcl(core.game.dialogue.FacialExpression.HAPPY,"Ohh... That's a nice fish. Very pleased. Here. Take the bowstring. You fulfilled agreement. Only fair I do same. Good work outerlander.").also {
                removeItem(player,3703)
                addItem(player,3702)
                stage++
            }
            21 -> playerl(core.game.dialogue.FacialExpression.HAPPY,"Thanks!").also { stage = 1000 }

            25 -> npcl(core.game.dialogue.FacialExpression.HAPPY,"Not hard. Just a trick to it. Takes skill to learn, but when learnt, easy. Sigli will be happy. Finest bowstring on continent. Will suit his needs perfectly.").also { stage = 1000 }

            26 -> npcl(core.game.dialogue.FacialExpression.ANNOYED,"Not for me, I'm afraid.").also { stage = 1000 }

            100 -> npcl(core.game.dialogue.FacialExpression.HAPPY,"Sorry. I can't sell weapons to outerlanders. Wouldn't be right. Against our beliefs.").also { stage = 1000 }

            1000 -> end()
            1001 ->{
                end()
                npc.openShop(player)
            }
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return SkulgrimenDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(1303)
    }

}