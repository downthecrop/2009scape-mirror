package rs09.game.content.quest.members.thefremenniktrials

import api.addItem
import api.isQuestComplete
import api.removeItem
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import rs09.tools.END_DIALOGUE

@Initializable
class FishermanDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun open(vararg args: Any?): Boolean {
        if(player?.inventory?.contains(3704,1) == true){
            playerl(FacialExpression.HAPPY,"Here. I got you your map.")
            stage = 15
            return true
        }
        else if(player?.inventory?.contains(3703,1) == true){
            playerl(FacialExpression.ASKING,"I don't see what's so special about this so called exotic fish.")
            stage = 16
            return true
        }
        else if(player?.getAttribute("sigmundreturning",false) == true){
            playerl(FacialExpression.ASKING,"Is this trade item for you?")
            stage = 17
            return true
        }
        if(player?.getAttribute("sigmund-steps", 0) == 8){
            playerl(FacialExpression.ASKING,"I don't suppose you have any idea where I could find a map of deep sea fishing spots do you?")
            stage = 10
            return true
        }
        else if(player?.getAttribute("sigmund-steps", 0) == 7){
            playerl(FacialExpression.ASKING,"I don't suppose you have any idea where I could find an exotic and extremely odd fish, do you?")
            stage = 1
            return true
        } else if (isQuestComplete(player, "Fremennik Trials")){
            player(FacialExpression.FRIENDLY, "Hello there.").also { stage = 100 }
        } else if (!isQuestComplete(player, "Fremennik Trials")) {
            player(FacialExpression.FRIENDLY, "Hello there.").also { stage = 200 }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            1 -> npcl(FacialExpression.AMAZED,"Ah, so even outerlanders have heard of my amazing catch the other day!").also { stage++ }
            2 -> playerl(FacialExpression.THINKING,"You have it? Can I trade you something for it?").also { stage++ }
            3 -> npcl(FacialExpression.HAPPY,"As exotic looking as it is, it is bad eating. I will happily trade it if you can find me the secret map of the best fishing spots that the navigator has hidden away.").also { stage++ }
            4 -> playerl(FacialExpression.AMAZED,"Is that all?").also { stage++ }
            5 -> npcl(FacialExpression.HAPPY,"Indeed it is, outerlander. The only reason I sit out here in the cold all day long is so I don't have to pay his outrageous prices.").also { stage++ }
            6 -> npcl(FacialExpression.HAPPY,"By getting me his copy of that map, I will finally be self sufficient. I might even make a profit!").also { stage++ }
            7 -> playerl(FacialExpression.THINKING,"I'll see what I can do.").also {
                player?.incrementAttribute("sigmund-steps",1)
                stage = 1000
            }
            10 -> npcl(FacialExpression.ANNOYED,"You should pay attention when I speak! I already told you, that rip off navigator has it, and I want it!").also { stage = 1000 }

            15 -> npcl(FacialExpression.HAPPY,"Great work outerlander! With this, I can finally catch enough fish to make an honest living from it! Here, have the stupid fish.").also {
                removeItem(player,3704)
                addItem(player,3703)
                stage = 1000
            }

            16 -> npcl(FacialExpression.ANNOYED,"Me neither, outerlander. That is why I gave it to you.").also { stage = 1000 }

            17 -> npcl(FacialExpression.ANNOYED,"I don't think so.").also { stage = 1000 }

            100 -> npcl(FacialExpression.FRIENDLY, "Welcome back, ${if (player.isMale) "brother " else "sister "}${player.getAttribute("fremennikname","fremmyname")}. It has been too long since last we spoke! The fish are really biting today!").also { stage = END_DIALOGUE }
            200 -> npcl(FacialExpression.ANNOYED, "Don't talk to me outerlander. Anything you have to say to me, you can say to the Chieftain. Goodbye.").also { stage = END_DIALOGUE }

            1000 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return FishermanDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(1302)
    }
}