package rs09.game.content.quest.members.thefremenniktrials

import api.addItem
import api.removeItem
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable

@Initializable
class YrsaDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if(player?.inventory?.contains(3708,1) == true){
            playerl(FacialExpression.HAPPY,"Hello. Can I have those boots now? Here is a written statement from Brundt outlining future tax burdens upon Fremennik merchants and shopkeepers for the year.")
            stage = 15
            return true
        }
        else if(player?.inventory?.contains(3700,1) == true){
            playerl(FacialExpression.ASKING,"Hey, these shoes look pretty comfy. Think you could make me a pair like them?")
            stage = 20
            return true
        }
        else if(player?.getAttribute("sigmundreturning",false) == true){
            playerl(FacialExpression.ASKING,"I have this trade item but I can't remember who it's for")
            stage = 25
            return true
        }
        if(player?.getAttribute("sigmund-steps",0) == 4){
            playerl(FacialExpression.ASKING,"I don't suppose you have any idea where I could find a guarantee of a reduction on sales taxes, do you?")
            stage = 10
            return true
        }
        else if(player?.getAttribute("sigmund-steps",0) == 3){
            playerl(FacialExpression.ASKING,"I don't suppose you have any idea where I could find some custom sturdy boots, do you?")
            stage = 1
            return true
        }
        else if(player.questRepository.isComplete("Fremennik Trials")){
            npcl(FacialExpression.HAPPY,"Welcome to my clothes shop. I can change your shoes, or I've got a fine selection of clothes for sale.")
            stage = 30
            //Uncomment this out when we got the shoe shop up and running
            //npcl(FacialExpression.HAPPY,"Welcome to my clothes shop. I can change your shows, or I've got a fine selection of clothes for sale")
            return true
        }
        else {
            playerl(FacialExpression.HAPPY,"Hello!")
            stage = 35
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            1 -> npcl(FacialExpression.THINKING,"Well, I don't usually have many shoes in stock here in my little clothes shop... I will be able to make you up a pair if you are really desperate though?").also { stage++ }
            2 -> playerl(FacialExpression.NEUTRAL,"They're not for me... I need them for Olaf.").also { stage++ }
            3 -> npcl(FacialExpression.ANGRY,"Oh, that foolish bard... Why didn't he just ask me to make him some? It is his stupid pride, I believe! I will tell you what I will do outlander.").also { stage++ }
            4 -> npcl(FacialExpression.ASKING,"I know that you must have the ear of the chieftain for him to consider you as worthy of becoming a Fremennik by trial.").also { stage++ }
            5 -> npcl(FacialExpression.HAPPY,"I will make you a pair of sturdy boots for Olaf if you will persuade him to reduce the sales tax placed upon all Fremennik shopkeepers. It does nothing but hurt my business now.").also { stage++ }
            6 -> playerl(FacialExpression.NEUTRAL,"Okay, I will see what I can do").also {
                player?.incrementAttribute("sigmund-steps",1)
                stage = 1000
            }
            10 -> npcl(FacialExpression.NEUTRAL,"Yes I do outerlander. Only the Chieftain may permit such a thing. Talk to him.").also { stage = 1000 }

            15 -> npcl(FacialExpression.HAPPY,"Certainly! Let me have a look at what he has written here, just give me a moment...").also {
                removeItem(player,3708)
                addItem(player,3700)
                stage++
            }
            16 -> npcl(FacialExpression.HAPPY,"Yes, that all appears in order. Tell Olaf to come to me next time for shoes!").also { stage = 1000 }

            20 -> npcl(FacialExpression.HAPPY,"Maybe if you pass your trial and become a full fledged member of the Fremennik...").also { stage = 1000 }

            25 -> npcl(FacialExpression.ANNOYED,"Not me, I'm afraid.").also { stage = 1000 }

            30 -> options("I'd like to buy some clothes","Nothing, thanks.").also { stage++ }
            31 -> when(buttonId){
                1 -> playerl(FacialExpression.HAPPY,"I'd like to buy some clothes").also {
                    npc.openShop(player)
                    stage = 1000
                }
                2 -> playerl(FacialExpression.HAPPY,"Nothing, thanks.").also { stage++ }
            }
            32 -> npcl(FacialExpression.HAPPY,"As you wish.").also { stage = 1000 }

            35 -> npcl(FacialExpression.HAPPY,"I'm sorry outerlander, I cannot sell you any clothes. Our customs forbid it.").also { stage = 1000 }

            1000 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return YrsaDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(1301)
    }


}