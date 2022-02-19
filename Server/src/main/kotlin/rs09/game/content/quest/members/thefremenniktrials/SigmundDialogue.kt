package rs09.game.content.quest.members.thefremenniktrials

import api.addItem
import api.removeItem
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.Items

@Initializable
class SigmundDialogue (player: Player? = null) : DialoguePlugin(player) {

    private val QUEST_ITEMS = intArrayOf(3709,3707,3706,3710,3705,3704,3703,3702,3701,3708,3700,3699,3698)

    val gender = if(player?.isMale == true){
        "brother"
    }else "sister"

    val fName = player?.getAttribute("fremennikname","uhhh fucking uhhh geoff lol")

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if(player.questRepository.isComplete("Fremennik Trials")){
            playerl(FacialExpression.HAPPY,"Hello there!")
            stage = 50
            return true
        }
        else if(!player.questRepository.hasStarted("Fremennik Trials")){
            playerl(FacialExpression.HAPPY,"Hello there!")
            stage = 60
            return true
        }
        if(player?.inventory?.contains(3698,1) == true){
            playerl(FacialExpression.HAPPY,"Here's that flower you wanted.")
            stage = 30
            return true
        }
        else if(player?.getAttribute("sigmundreturning",false) == true && !player.inventory.containsAtLeastOneItem(QUEST_ITEMS)){
            npcl(FacialExpression.ASKING,"So... how goes it outerlander? Did you manage to obtain my flower for me yet? Or do you lack the necessary merchanting skills?")
            stage = 35
            return true
        }
        if(player?.getAttribute("sigmund-started",false)!!){
            playerl(FacialExpression.HAPPY,"Hello there!")
            stage = 25
            return true
        }
        else if(!player?.getAttribute("fremtrials:sigmund-vote",false)!!){
            playerl(FacialExpression.HAPPY,"Hello there!")
            stage = 1
            return true
        }
        else if(player?.getAttribute("fremtrials:sigmund-vote",false) == true){
            playerl(FacialExpression.HAPPY,"Hello there!")
            stage = 40
            return true
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            1 -> npcl(FacialExpression.NEUTRAL,"Hello outlander.").also { stage++ }
            2 -> playerl(FacialExpression.NEUTRAL,"Are you a member of the council?").also { stage++ }
            3 -> npcl(FacialExpression.NEUTRAL,"That I am outlander; it is a position that brings my family and I pride.").also { stage++ }
            4 -> playerl(FacialExpression.NEUTRAL,"I was wondering if I can count on your vote at the council of elders?").also { stage++ }
            5 -> npcl(FacialExpression.THINKING,"You wish to become a Fremennik? I may be persuaded to swing my vote to your favor, but you will first need to do a little task for me.").also { stage++ }
            6 -> playerl(FacialExpression.ANNOYED,"How did I know it wouldn't be that simple for your vote?").also { stage++ }
            7 -> npcl(FacialExpression.NEUTRAL,"Calm yourself outerlander. It is but a small task really... I simply require a flower.").also { stage++ }
            8 -> playerl(FacialExpression.ASKING,"A flower? What's the catch?").also { stage++ }
            9 -> npcl(FacialExpression.NEUTRAL,"The catch? Well, it is not just any flower. Someone in this town has an extremely unique flower from a far off land that they picked up on their travels.").also { stage++ }
            10 -> npcl(FacialExpression.NEUTRAL,"I would like you to demonstrate your merchanting skills to me by persuading them to part with it, and then give it to me for my vote.").also { stage++ }
            11 -> playerl(FacialExpression.THINKING,"Well... I guess that doesn't sound too hard.").also { stage++ }
            12 -> npcl(FacialExpression.HAPPY,"Excellent! You will obtain this flower for me, then?").also { stage++ }
            13 -> options("Yes","No").also { stage++ }
            14 -> when(buttonId){
                1 -> playerl(FacialExpression.ASKING,"Okay. I don't think this will be too difficult. Any suggestions on where to start looking for this flower?").also {
                    player?.setAttribute("/save:sigmund-started",true)
                    player?.setAttribute("/save:sigmund-steps",1)
                    stage++ }
                2 -> playerl(FacialExpression.ANNOYED,"You know what? No. This all sounds like a lot of hassle to me, and frankly I just can't be bothered with it right now. I'll go get someone else to vote for me.").also { stage = 20 }
            }
            15 -> npcl(FacialExpression.THINKING,"Ah, well outerlander, if I knew where to start looking I would simply do it myself!").also { stage++ }
            16 -> playerl(FacialExpression.ANNOYED,"No help at ALL?").also { stage++ }
            17 -> npcl(FacialExpression.NEUTRAL,"We are a very insular clan, so I would not expect you to have to leave this town to find whatever you need.").also { stage = 1000 }

            //No
            20 -> npcl(FacialExpression.NEUTRAL,"As you wish outlander. If you change your mind, come and see me again; I am very interested in getting my hands on that flower").also { stage = 1000 }

            25 -> npcl(FacialExpression.ASKING,"So... how goes it outerlander? Did you manage to obtain my flower for me yet? Or do you lack the necessary merchanting skills?").also { stage++ }
            26 -> playerl(FacialExpression.ASKING,"I'm still working on it... Do you have any suggestion where to start looking for it?").also { stage++ }
            27 -> npcl(FacialExpression.THINKING," I suggest you ask around the other Fremennik in the town. A good merchant will find exactly what their customer needs somewhere.").also { stage = 1000 }

            30 -> npcl(FacialExpression.AMAZED,"Incredible! Your merchanting skills might even match my own! I have no choice but to recommend you to the council of elders!").also {
                removeItem(player,3698)
                player?.removeAttribute("sigmund-steps")
                player?.removeAttribute("sigmund-started")
                player?.removeAttribute("sigmundreturning")
                player?.setAttribute("/save:fremtrials:votes",player.getAttribute("fremtrials:votes",0) + 1)
                player?.setAttribute("/save:fremtrials:sigmund-vote",true)
                stage = 1000
            }

            35 -> playerl(FacialExpression.ASKING,"I'm still working on it... Do you have any suggestion where to start looking for it?").also { stage++ }
            36 -> npcl(FacialExpression.ASKING,"I suggest you ask around the other Fremennik in the town. A good merchant will find exactly what their customer needs somewhere.").also { stage++ }
            37 -> playerl(FacialExpression.ASKING,"I was making some trades, but then I lost the goods...").also { stage++ }
            38 -> npcl(FacialExpression.THINKING,"Hmmm... well try and start again at the beginning. And try to be more careful of your wares in future.").also {
                addItem(player, Items.PROMISSORY_NOTE_3709)
                stage = 1000 }

            40 -> npcl(FacialExpression.HAPPY,"Hello again outerlander! I am amazed once more at your apparent skill at merchanting!").also { stage++ }
            41 -> playerl(FacialExpression.HAPPY,"So I can count on your vote at the council of elders?").also { stage++ }
            42 -> npcl(FacialExpression.HAPPY,"Absolutely, outerlander. Your merchanting skills will be a real boon to the Fremennik.").also { stage = 1000 }

            50 -> npcl(FacialExpression.HAPPY,"Greetings again $gender $fName! What can I do for you this day?").also { stage++ }
            51 -> options("Can I see your wares?","Nothing thanks").also { stage++ }
            52 -> when(buttonId){
                1 -> playerl(FacialExpression.HAPPY,"Can I see your wares?").also { stage++ }
                2 -> playerl(FacialExpression.HAPPY,"Nothing thanks").also { stage = 55 }
            }
            53 -> npcl(FacialExpression.HAPPY,"Certainly, $fName.").also {
                npc.openShop(player)
                stage = 1000
            }

            55 -> npcl(FacialExpression.HAPPY,"Well, feel free to stop by anytime you wish $fName. You are always welcome here!").also { stage = 1000 }

            60 -> npcl(FacialExpression.HAPPY,"Hello outerlander. By the laws of our tribe, I am afraid I may not speak to you without the express permission of the chieftain.").also { stage++ }
            61 -> playerl(FacialExpression.ASKING,"Where would I find him?").also { stage++ }
            62 -> npcl(FacialExpression.HAPPY,"In the longhall, outerlander.").also { stage = 1000 }

            1000 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return SigmundDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(1282)
    }
}