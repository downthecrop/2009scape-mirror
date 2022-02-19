package rs09.game.content.quest.members.thefremenniktrials

import api.addItem
import api.removeItem
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.PlayerDetails
import core.plugin.Initializable
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression

@Initializable
class SwensenTheNavigator(player: Player? = null) : DialoguePlugin(player){
    val gender = if (player?.isMale == true){"brother"} else "sister"
    val fName = player?.getAttribute("fremennikname","doug hug'em")
    override fun open(vararg args: Any?): Boolean {
        if(player?.inventory?.contains(3705,1) == true){
            playerl(FacialExpression.HAPPY,"I would like your map of fishing spots.")
            stage = 120
            return true
        }
        else if(player?.inventory?.contains(3704,1) == true){
            playerl(FacialExpression.ASKING,"If this map of fishing spots is so valuable, why did you give it away to me so easily?")
            stage = 125
            return true
        }
        else if(player?.getAttribute("sigmundreturning",false) == true){
            playerl(FacialExpression.ASKING,"I have a trade item here.")
            stage = 130
            return true
        }
        if(player?.getAttribute("sigmund-steps", 0) == 9){
            npcl(FacialExpression.HAPPY,"Greetings outerlander.")
            stage = 115
            return true
        }
        else if(player?.getAttribute("sigmund-steps", 0) == 8){
            npcl(FacialExpression.HAPPY,"Greetings outerlander.")
            stage = 105
            return true
        }
        if(player?.getAttribute("fremtrials:maze-complete",false) == true){
            npc("Outerlander! You have finished my maze!","I am genuinely impressed!")
            stage = 100
            return true
        } else if(player?.getAttribute("fremtrials:swensen-vote",false) == true){
            npc("You have my vote!")
            stage = 1000
            return true
        }
        else if(player.questRepository.isComplete("Fremennik Trials")){
            playerl(FacialExpression.HAPPY,"Hello!")
            stage = 140
            return true
        }
        else if(player.questRepository.hasStarted("Fremennik Trials")){
            player("Hello!")
            stage = 0
            return true
        }
        else{
            playerl(FacialExpression.HAPPY,"Hello!")
            stage = 145
            return true
        }
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> {player("I am trying to become a member of the Fremennik","clan! The Chieftan told me that I may be able to gain","your vote at the council of elders?");stage++}
            1 -> {npc("You wish to stop being an outerlander? I can","understand that! I have no reason why I would prevent","you becoming a Fremennik...");stage++}
            2 -> {npc("...but you must first pass a little test for me to prove","you are worthy.");stage++}
            3 -> {player("What kind of test?");stage++}
            4 -> {npc("Well, I serve our clan as a navigator. The seas can be","a fearful place when you know not where you are","heading.");stage++}
            5 -> {npc("Should something happen to me, all members of our","tribe have some basic sense of direction so that they","may always return safely home.");stage++}
            6 -> {npc("If you are able to demonstrate to me that you too have","a good sense of direction then I will recommend you to","the rest of the council of elders immediately.");stage++}
            7 -> {player("Well, how would I go about showing that?");stage++}
            8 -> {npc("Ah, a simple task! Below this building I have constructed","a maze; should you be able to walk from one side to the","other that will be proof to me.");stage++}
            9 -> {npc("You wish to try my challenge?");stage++}
            10 -> {options("Yes","No");stage++}
            11 -> when(buttonId){
                    1 -> {player("A maze? Is that all? Sure, it sounds simple enough.");stage++}
                    2 -> {player("No, that sounds too hard.");stage = 1000}
                 }

                //Yes
                12 -> {npc("I will warn you outerlander, this maze was designed by","myself, and is of the most fiendish complexity!");stage++}
                13 -> {player("Oh really? Watch and learn...");stage = 1000;player?.setAttribute("/save:fremtrials:swensen-accepted",true)}

            //After maze
            100 -> {player("So does that mean I can rely on your vote at the","council of elders to allow me into your village?");stage++}
            101 -> {npc("Of course outerlander! I am nothing if not a man of","my word!");stage++}
            102 -> {
                    player("Thanks!");
                    player?.removeAttribute("fremtrials:maze-complete")
                    player?.removeAttribute("fremtrials:swensen-accepted")
                    player?.setAttribute("/save:fremtrials:swensen-vote",true)
                    player?.setAttribute("/save:fremtrials:votes",player.getAttribute("fremtrials:votes",0) + 1)
                    stage = 1000
                  }

            //Sigmund subquest
            105 -> playerl(FacialExpression.ASKING,"I don't suppose you have any idea where I could find a map of deep sea fishing spots do you?").also { stage++ }
            106 -> npcl(FacialExpression.HAPPY,"Hmmm? Why of course! As the navigator for the Fremennik I keep all of our maps secure right here.").also { stage++ }
            107 -> playerl(FacialExpression.HAPPY,"Great! Can I have it?").also { stage++ }
            108 -> npcl(FacialExpression.ANNOYED,"Have it? Just like that? I think not outerlander. This map shows all of the prime fishing locations nearby. It is very valuable to our clan. I am afraid I can not just give it away.").also { stage++ }
            109 -> playerl(FacialExpression.THINKING,"Perhaps I can trade you something for it?").also { stage++ }
            110 -> npcl(FacialExpression.ANNOYED,"A trade? For a map of the best fishing spots in a hundred leagues? I will trade it for no less than a weather forecast from our Seer.").also { stage++ }
            111 -> npcl(FacialExpression.THINKING,"As a navigator, the weather is extremely important for plotting the best course. Unfortunately the Seer is always too busy to help me with a forecast.").also { stage++ }
            112 -> playerl(FacialExpression.ASKING,"Where could I get a weather forecast from then?").also { stage++ }
            113 -> npcl(FacialExpression.ANNOYED,"I just told you: from the Seer. You will need to persuade him to take the time to make a forecast somehow.").also {
                player?.incrementAttribute("sigmund-steps",1)
                stage = 1000
            }
            115 -> playerl(FacialExpression.THINKING,"I don't suppose you have any idea where I could find a weather forecast from the Fremennik Seer do you?").also { stage++ }
            116 -> npcl(FacialExpression.ANNOYED,"Uh... from the Seer perhaps?").also { stage = 1000 }

            120 -> npcl(FacialExpression.ANNOYED,"I have already told you outerlander; I will not exchange it for anything other than a divination on the weather from our seer himself!").also { stage++ }
            121 -> playerl(FacialExpression.HAPPY,"What, like this one I have here?").also { stage++ }
            122 -> npcl(FacialExpression.AMAZED,"W-what...? I don't believe it! How did you...?").also { stage++ }
            123 -> npcl(FacialExpression.HAPPY,"I suppose it doesn't matter, you have my gratitude outerlander! With this forecast I will be able to plan a safe course for our next raiding expedition!").also {
                removeItem(player,3705)
                addItem(player,3704)
                stage++
            }
            124 -> npcl(FacialExpression.HAPPY,"Here, outerlander; you may take my map of local fishing patterns with my gratitude!").also { stage = 1000 }

            125 -> npcl(FacialExpression.HAPPY,"Hmmm? Well, firstly it will be of value to our entire clan, so I have lost nothing from giving it to you.").also { stage++ }
            126 -> npcl(FacialExpression.HAPPY,"The other reason is of course that I have already memorised it, so I can make myself another copy whenever I want!").also { stage = 1000 }

            130 -> npcl(FacialExpression.ANNOYED,"It isn't for me, I'm afraid.").also { stage = 1000 }

            140 -> npcl(FacialExpression.HAPPY,"Greetings to you $gender $fName. How fare you today?").also { stage++ }
            141 -> playerl(FacialExpression.HAPPY,"I am fine thanks Swensen. How are you doing?").also { stage++ }
            142 -> npcl(FacialExpression.HAPPY,"I am fine too!").also { stage = 1000 }

            145 -> npcl(FacialExpression.HAPPY,"Hello outerlander. This is my home, I would be grateful if you would leave.").also { stage++ }
            146 -> playerl(FacialExpression.THINKING,"Oh. Okay.").also { stage++ }
            147 -> npcl(FacialExpression.HAPPY,"I am sorry outerlander, I will not offer you hospitality until my Chieftain has vouched for your honesty. This is our way.").also { stage = 1000 }

            1000 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return SwensenTheNavigator(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(1283)
    }

}