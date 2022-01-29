package rs09.game.content.quest.members.thefremenniktrials

import api.addItem
import api.removeItem
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import kotlin.random.Random

@Initializable
class ChieftanBrundt(player: Player? = null) : DialoguePlugin(player){
    override fun open(vararg args: Any?): Boolean {
        if(player?.inventory?.contains(3701,1) == true){
            playerl(FacialExpression.HAPPY,"I got Sigli's hunting map for you.")
            stage = 515
            return true
        }
        else if(player?.inventory?.contains(3708,1) == true){
            playerl(FacialExpression.ASKING,"So cutting sales tax isn't going to ruin your economy here or anything?")
            stage = 520
            return true
        }
        else if(player?.getAttribute("sigmundreturning",false) == true){
            playerl(FacialExpression.ASKING,"I've got this trade item. Is it for you?")
            stage = 525
            return true
        }
        if(player?.getAttribute("sigmund-steps",0) == 5){
            playerl(FacialExpression.ASKING,"I don't suppose you have any idea where I could find a map to unspoiled hunting grounds, do you?")
            stage = 510
            return true
        }
        else if(player?.getAttribute("sigmund-steps",0) == 4){
            playerl(FacialExpression.ASKING,"I don't suppose you have any idea where I could find a guarantee of a reduction on sales taxes, do you?")
            stage = 500
            return true
        }
        else if(player.getAttribute("fremtrials:votes",0) == 7){
            npcl(FacialExpression.HAPPY," Greetings again outerlander! How goes your attempts to gain votes with the council of elders?")
            stage = 545
            return true
        }
        else if(player.getAttribute("fremtrials:votes",0) in 3..6){
            npcl(FacialExpression.HAPPY," Greetings again outerlander! How goes your attempts to gain votes with the council of elders?")
            stage = 540
            return true
        }
        else if(player.getAttribute("fremtrials:votes",0) == 1){
            npcl(FacialExpression.HAPPY," Greetings again outerlander! How goes your attempts to gain votes with the council of elders?")
            stage = 535
            return true
        }
        else if(player.getAttribute("fremtrials:votes",-1) == 0){
            npcl(FacialExpression.HAPPY," Greetings again outerlander! How goes your attempts to gain votes with the council of elders?")
            stage = 530
            return true
        }
        else if(player?.questRepository?.getStage("Fremennik Trials")!! == 0) {
            npc("Greetings outlander!")
            stage = 0
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            //Pre-Quest
            0 -> { options("What is this place?", "Why will no-one talk to me?", "Do you have any quests?", "Nice hat!");stage++}
            1 -> when(buttonId){
                    1 -> TODO("Not yet implemented")
                    2 -> TODO("Not yet implemented")
                    3 -> {player("Do you have any quests?");stage = 300}
                    4 -> TODO("Not yet implemented")
                 }

            //Do you have any quests?
            300 -> {npc("Quests, you say outlander? Well, I would not call it a","quest as such, but if you are brave of heart and strong","of body, perhaps..."); stage++}
            301 -> {npc("No, you would not be interested. Forget I said","anything, outerlander.");stage++}
            302 -> {options("Yes, I am interested.","No, I'm not interested.");stage++}
            303 -> when(buttonId){
                    1 -> {player("Actually, I would be very interested to hear what you","have to offer.");stage = 310}
                    2 -> {player("No, I'm not interested.");stage = 340}
                  }

                //Yes, I am interested in what you have to say.
                310 -> {npc("You would? These are unusual sentiments to hear from","an outerlander! My suggestion was going to be that if","you crave adventure and battle,"); stage++}
                311 -> {npc("and your heart sings for glory, then perhaps you would","be interested in joining our clan, and becoming a","Fremennik yourself?");stage++}
                312 -> {player("What would that involve exactly?");stage++}
                313 -> {npc("Well, there are two ways to become a member of our","clan and call yourself a Fremennik: be born a","Fremennik, or be voted in by our council of elders.");stage++}
                314 -> {player("Well, I think I've missed the first way, but how can I","get the council of elders to vote to let me join your","clan?");stage++}
                315 -> {npc("Well, that I cannot answer myself. You will need to","speak to each of them and see what they require of you","as proof of your dedication.");stage++}
                316 -> {npc("There are twelve council members around this village;","you will need to gain a majority vote of at least seven","councillors in your favor.");stage++}
                317 -> {npc("So, what say you? Give me the word and I will tell all","of my tribe of your intentions, be they yes or nay.");stage++}
                318 -> {options("I want to become a Fremennik!","I don't want to become a Fremennik.");stage++}
                319 -> when(buttonId){
                        1 -> {player("I think I would enjoy the challenge of becoming an","honorary Fremennik. Where and how do I start?");stage = 320}
                        2 -> {player("That sounds too complicated to me.");stage = 322}
                      }

                    //I think I would enjoy the challenge of becoming an honorary fremennik
                    320 -> {npc("As I say outerlander, you must find and speak to the","twelve members of the council of elders, and see what","tasks they might set you.");stage++}
                    321 -> {npc("If you can gain the support of seven of the twelve, then","you will be accepted as one of us without question.");stage = 1000;player?.questRepository?.getQuest("Fremennik Trials")?.start(player)}

                    //That sounds too complicated for me.
                    322 -> {npc("Well, that's what I expect from an outerlander.");stage = 1000}

                //No, I'm not interested
                340 -> TODO("Not implemented yet")

            500 -> npcl(FacialExpression.THINKING,"A reduction on sales taxes? Why, I am the only one in the Fremennik who may authorise such a thing. What does an outerlander want with that?").also { stage++ }
            501 -> playerl(FacialExpression.HAPPY,"Actually, it's not for me. I need to get it as part of my trials").also { stage++ }
            502 -> npcl(FacialExpression.THINKING,"Hmmm. Interesting. Your trials seem to be very different to those I took as a young lad.").also { stage++ }
            503 -> npcl(FacialExpression.NEUTRAL,"Well, I am not adverse in principle to giving a slight tax break to our shops.").also { stage++ }
            504 -> npcl(FacialExpression.THINKING,"There will of course be a shortfall in the tribe's income, that will need to be made up for elsewhere, however.").also { stage++ }
            505 -> npcl(FacialExpression.ASKING,"How about this. For many years Sigli has been the only one in the tribe who knows the locations of the best hunting grounds where game is easiest to catch.").also { stage++ }
            506 -> npcl(FacialExpression.ASKING,"If you can persuade him to let the entire tribe know these hunting grounds, then we can increase productivity within the tribe, and any shortfall caused by lowering sales taxes will be covered.").also { stage++ }
            507 -> npcl(FacialExpression.HAPPY,"I think this is a more than fair arrangement to make, dont you?").also { stage++ }
            508 -> playerl(FacialExpression.HAPPY,"Yeah, that sounds very fair.").also { stage++ }
            509 -> npcl(FacialExpression.HAPPY,"Speak to Sigli then, and you may have my promise to reduce our sales taxes. And best of luck with the rest of your trials.").also {
                player?.incrementAttribute("sigmund-steps",1)
                stage = 1000
            }

            510 -> npcl(FacialExpression.ANNOYED,"Ah, outerlander... if you wish to become a Fremennik you should try and pay more attention to what people tell you... ").also { stage++ }
            511 -> npcl(FacialExpression.ANNOYED,"Sigli the hunter is the only one who knows of such hunting grounds. Go and request his knowledge.").also { stage = 1000 }

            515 -> npcl(FacialExpression.HAPPY,"Excellent work outerlander! And so quickly, too! Here, you may take my financial report promising reduced sales taxes on all goods.").also {
                removeItem(player,3701)
                addItem(player,3708)
                stage = 1000
            }

            520 -> npcl(FacialExpression.HAPPY,"Not at all outerlander; now that we have Sigli's map we can increase the amount of hunts we run, and make up any shortfall that way.").also { stage = 1000 }

            525 -> npcl(FacialExpression.ANNOYED,"Not unless it's a map of the hunting grounds.").also { stage = 1000 }

            530 -> playerl(FacialExpression.HAPPY,"I don't have any votes yet.").also { stage++ }
            531 -> npcl(FacialExpression.HAPPY,"Go and speak to the twelve members of the council of elders who live in this village. I am sure at least a few will be prepared to vote for you.").also { stage = 550 }

            535 -> playerl(FacialExpression.HAPPY,"I only have one vote so far.").also { stage++ }
            536 -> npcl(FacialExpression.HAPPY,"Hmmm... well that is certainly a good start I would say. Keep up the good work!").also { stage++ }
            537 -> npcl(FacialExpression.HAPPY,"Remember: You need to get at least seven council votes to be accepted as a member of the Fremennik.").also { stage = 550 }

            540 -> playerl(FacialExpression.HAPPY,"I only have ${player.getAttribute("fremtrials:votes",0)} votes so far.").also { stage++ }
            541 -> npcl(FacialExpression.HAPPY,"Hmmm... you are doing very well so far, outerlander. Keep up the good work!").also { stage = 537 }

            545 -> playerl(FacialExpression.HAPPY,"I have seven members of the council prepared to vote in my favour now!").also { stage++ }
            546 -> npcl(FacialExpression.HAPPY,"I know outerlander, for I have been closely monitoring your progress so far!").also {stage++}
            547 -> npcl(FacialExpression.HAPPY,"Then let us put the formality aside, and let me personally welcome you into the Fremennik! May you bring us honour!").also {
                if(player.inventory.freeSlots() >= 10){
                    println(GenerateFremennikName())
                    player.setAttribute("/save:fremennikname", GenerateFremennikName())
                    stage = 560
                }
                else stage = 548

            }
            548 -> sendDialogue("You require 10 free spaces in your backpack to claim your reward.").also { stage = 1000 }

            550 -> npcl(FacialExpression.HAPPY,"If you need any help with your trials, I suggest you speak to Askeladden. He is currently doing his own trials of manhood to become a true Fremennik.")

            560 -> npcl(FacialExpression.HAPPY,"From this day onward, you are outerlander no more! In honour of your acceptance into the Fremennik, you gain a new name: ${player.getAttribute("fremennikname","how did u break this")}.").also {
                cleanupAttributes(player)
                player.questRepository.getQuest("Fremennik Trials").finish(player)
                stage = 1000
            }

            1000 -> end()
        }
        return true
    }


    override fun newInstance(player: Player?): DialoguePlugin {
        return ChieftanBrundt(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(1294)
    }

}

fun GenerateFremennikName(): String {
    val namePrefixes = arrayOf(
        "Bal",
        "Bar",
        "Dal",
        "Dar",
        "Den",
        "Dok",
        "Jar",
        "Jik",
        "Lar",
        "Rak",
        "Ral",
        "Ril",
        "Sig",
        "Tal",
        "Thor",
        "Ton"
    )
    val nameSuffixes = arrayOf(
        "dar",
        "dor",
        "dur",
        "kal",
        "kar",
        "kir",
        "kur",
        "lah",
        "lak",
        "lim",
        "lor",
        "rak",
        "rar",
        "tin",
        "ton",
        "tor",
        "vald"
    )
    val randomPrefix = Random.nextInt(0, 15)
    val randomSuffix = Random.nextInt(0, 16)

    return "${namePrefixes[randomPrefix]}${nameSuffixes[randomSuffix]}"
}

fun cleanupAttributes(player: Player){
    player.removeAttribute("PeerStarted")
    player.removeAttribute("housepuzzlesolved")
    player.removeAttribute("fremtrials:peer-vote")
    player.removeAttribute("fremtrials:votes")
    player.removeAttribute("fremtrials:sigmund-vote")
    player.removeAttribute("fremtrials:manni-vote")
    player.removeAttribute("fremtrials:swensen-vote")
    player.removeAttribute("fremtrials:olaf-accepted")
    player.removeAttribute("fremtrials:lalli-talkedto")
    player.removeAttribute("fremtrials:askeladden-talkedto")
    player.removeAttribute("hasWool")
    player.removeAttribute("lyreConcertPlayed")
    player.removeAttribute("fremtrials:olaf-vote")
    player.removeAttribute("fremtrials:sigli-accepted")
    player.removeAttribute("fremtrials:thorvald-vote")

}