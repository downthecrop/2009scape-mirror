package rs09.game.content.quest.members.thefremenniktrials

import api.addItem
import api.removeItem
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable

@Initializable
class SailorDialogue (player: Player? = null) : DialoguePlugin(player)  {

    override fun open(vararg args: Any?): Boolean {
        if(player?.inventory?.contains(3699,1) == true){
            playerl(FacialExpression.HAPPY,"You'll be glad to know I have had a love song written just for you by Olaf. So can I have that flower of yours now?")
            stage = 15
        }
        else if(player?.inventory?.contains(3698,1) == true){
            playerl(FacialExpression.ASKING,"So tell me... who is this woman that you are trying to impress anyway?")
            stage = 20
            return true
        }
        else if(player?.getAttribute("sigmundreturning",false) == true){
            playerl(FacialExpression.ASKING,"I've got an item to trade.")
            stage = 25
            return true
        }
        else if(player?.getAttribute("sigmund-steps",0) == 2){
            playerl(FacialExpression.ASKING,"I don't suppose you have any idea where I could find a love ballad, do you?")
            stage = 10
            return true
        }
        else if(player?.getAttribute("sigmund-started",false)!!){
            playerl(FacialExpression.ASKING,"I don't suppose you have any idea where I could find a unique flower from across the sea, do you?")
            stage = 1
            return true
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            1 -> npcl(FacialExpression.HAPPY,"Ah! Even the outerlanders have heard of my mysterious flower! I found it in a country far far away from here!").also { stage++ }
            2 -> playerl(FacialExpression.ASKING,"Can I buy it from you?").also { stage++ }
            3 -> npcl(FacialExpression.NEUTRAL,"I'm afraid not, outerlander. There is a woman in this village whose heart I seek to capture, and I think giving her this strange flower might be my best bet with her.").also { stage++ }
            4 -> playerl(FacialExpression.THINKING,"Maybe you could let me have the flower and do something else to impress her?").also { stage++ }
            5 -> npcl(FacialExpression.HAPPY,"Hmm... that is not a totally stupid idea outerlander. I know she is a lover of music, and a romantic ballad might be just the thing with which to woo her.").also { stage++ }
            6 -> npcl(FacialExpression.THINKING,"Unfortunately I don't have a musical bone in my entire body, so someone else will have to write it for me.").also { stage++ }
            7 -> playerl(FacialExpression.ASKING,"So if I can find someone to write you a romantic ballad, you will give me your flower?").also { stage++ }
            8 -> npcl(FacialExpression.HAPPY,"That sounds like a fair deal to me, outerlander.").also {
                player.incrementAttribute("sigmund-steps",1)
                stage = 1000
            }
            10 -> npcl(FacialExpression.NEUTRAL,"Well, the only musician I know of in these parts would be that terrible bard Olaf... Ask him.").also { stage = 1000 }

            15 -> npcl(FacialExpression.HAPPY,"Oh. It's by Olaf? Hmm. Well, a deal's a deal. I just hope it's better than the usual rubbish he comes up with, or my chances are worse than ever.").also {
                removeItem(player,3699)
                addItem(player,3698)
                stage = 1000
            }

            20 -> npcl(FacialExpression.HAPPY,"It's Thora, the longhall barkeep. Please don't tell her though. She's not like the rest of the Fremennik girls, she has a secret desire to see the world.").also { stage++ }
            21 -> npcl(FacialExpression.HAPPY,"Being a sailor, I can really relate to that.").also { stage = 1000 }

            25 -> npcl(FacialExpression.HAPPY,"Remember, it's not for me unless it's a love ballad.")

            1000 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin{
        return SailorDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(1385)
    }

}