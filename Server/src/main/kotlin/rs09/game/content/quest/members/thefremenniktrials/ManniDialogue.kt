package rs09.game.content.quest.members.thefremenniktrials

import api.addItem
import api.removeItem
import core.game.node.entity.impl.Animator
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.task.Pulse
import rs09.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import org.rs09.consts.Items

@Initializable
class ManniDialogue(player: Player? = null) : DialoguePlugin(player){
    var curNPC: NPC? = NPC(0,Location(0,0,0))
    override fun open(vararg args: Any?): Boolean {
        curNPC = args[0] as? NPC
        if(player?.questRepository?.getStage("Fremennik Trials")!! > 0){
            if(player?.inventory?.contains(3707, 1) == true){
                playerl(FacialExpression.HAPPY,"Hey. I got your cocktail for you.")
                stage = 170
                return true
            }
            else if(player?.inventory?.contains(3706,1) == true){
                playerl(FacialExpression.ASKING,"So it doesn't bother you at all that you just gave up your place here for one drink?")
                stage = 180
                return true
            }
            else if(player?.getAttribute("sigmundreturning",false) == true){
                playerl(FacialExpression.ASKING,"Is this trade item for you?")
                stage = 165
                return true
            }
            else if(player?.getAttribute("sigmund-steps", 0) == 12){
                playerl(FacialExpression.ASKING,"I don't suppose you have any idea where I could find the longhall barkeeps' legendary cocktail, do you?")
                stage = 162
                return true
            }
            else if(player?.getAttribute("sigmund-steps", 0) == 11){
                playerl(FacialExpression.ASKING,"I don't suppose you have any idea where I could find a token to allow a seat at the champions table, do you?")
                stage = 150
                return true
            }
            else if(player?.getAttribute("fremtrials:manni-accepted",false) == true) {
                if (player?.inventory?.containsItem(Item(3712)) == true || player?.inventory?.containsItem(Item(3711)) == true) {
                    npc("Ah, I see you have your keg of beer. Are ye ready to", "drink against each other?")
                    stage = 101
                    return true
                } else {
                    npc("Come back when you're ready to begin", "the contest.")
                    stage = 1000;
                    return true
                }
            }
            else if(player?.getAttribute("fremtrials:manni-vote",false) == true){
                npc("e have my vote!")
                stage = 1000
                return true
            }
            else if(player.questRepository.isComplete("Fremennik Trials")){
                playerl(FacialExpression.HAPPY,"Howdy!")
                stage = 190
                return true
            }
            player("Hello there!")
            stage = 0
            return true
        }
        else {
            playerl(FacialExpression.HAPPY,"Hello there!")
            stage = 200
            return true
        }
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> {npc("Hello outerlander. I overheard your conversation with","Brundt just now. You wish to become a member of the","Fremennik?");stage++}
            1 -> {player("That's right! Why, are you on the counsel?");stage++}
            2 -> {npc("Do not let my drink-ssussed appearance fool you, I","earnt my place on the council many years past.I am","always glad to see new blood enter our tribe, and will","happily vote for you.");stage++}
            3 -> {player("Great!");stage++}
            4 -> {npc("Providing you can pass a little test for me. As a","Fremennik, you will need to show cunning, stamina,","fastitude, and an iron constitution. I know of only one","way to test all of these.");stage++}
            5 -> {player("And what's that?");stage++}
            6 -> {npc("Why, a drinking contest!");stage++}
            7 -> {npc("The task is simple enough! You versus me, a stiff drink","each, last man standing wins the trial. So what say you?");stage++}
            8 -> {options("Yes","No");stage++}
            9 -> when(buttonId){
                    1 -> {player("A drinking contest? Easy. Set them up, and I'll knock","them back.");stage++}
                    2 -> {player("I don't like the sound of that.");stage = 12}
                }

                //Yes
                10 -> {npc("When you are ready to begin, go and pick up a keg","from that table over there, and come back here.");stage++}
                11 -> {npc("We start when you have your keg of beer with you","and finish when one of us can drink no more and","yields.");stage = 1000;player?.setAttribute("/save:fremtrials:manni-accepted",true)}

                //No
                12 -> {npc("That's a shame."); stage = 1000}


            //Begin drinking competition
            101 -> {options("Yes","No");stage++}
            102 -> when(buttonId){
                    1 -> {player("Yes, let's start this drinking contest!");stage++}
                    2 -> {player("No, I don't think I am.");stage = 1000}
                  }
            103 -> {npc("As you wish outerlander; I will drink first, then you will","drink.");stage++}
            104 -> {GameWorld.submit(DrinkingPulse(player,curNPC,player?.getAttribute("fremtrials:keg-mixed",false))); end()}

            //Sigmund stuff
            150 -> npcl(FacialExpression.HAPPY,"As a matter of fact, I do. I have one right here. I earnt my place here at the longhall for surviving over 5000 battles and raiding parties.").also { stage++ }
            151 -> npcl(FacialExpression.HAPPY,"Due to my contribution to the tribe, I am now permitted to spend my days here in the longhall listening to the epic tales of the bard, and drinking beer.").also { stage++ }
            152 -> playerl(FacialExpression.HAPPY,"Cool. That sounds pretty sweet! So I guess you don't want to give it away?").also { stage++ }
            153 -> npcl(FacialExpression.SAD,"I think it sounds better than it actually is outerlander. I miss my glory days of combat on the battlefield.").also { stage++ }
            154 -> npcl(FacialExpression.SAD,"And to tell you the truth, the beer here isn't great, and the bards' music is lousy. I would happily give up my token if it were not for the one thing that keeps me here.").also { stage++ }
            155 -> npcl(FacialExpression.HAPPY,"Our barkeep is one of the best in the world, and has worked in taverns across the land. When she was younger, she experimented a lot with her drinks").also { stage++ }
            156 -> npcl(FacialExpression.HAPPY,"and invented a cocktail so alcoholic and tasty that it has become something of a legend to all who enjoy a drink.").also { stage++ }
            157 -> npcl(FacialExpression.SAD,"Unfortunately, she decided that cocktails were not a suitable drink for Fremennik warriors, and vowed to never again make it.").also { stage++ }
            158 -> npcl(FacialExpression.SAD,"I have been here every day since she returned, hoping that someday she might change her mind and I might try this legendary cocktail for myself. Alas, it has never come to pass...").also { stage++ }
            159 -> npcl(FacialExpression.SAD,"If you can persuade her to make me her legendary cocktail, I will be happy to never let another drop of alcohol pass my lips, and will give you my champions token.").also { stage++ }
            160 -> playerl(FacialExpression.THINKING,"That's all?").also { stage++ }
            161 -> npcl(FacialExpression.NEUTRAL,"That is all.").also {
                player?.incrementAttribute("sigmund-steps",1)
                stage = 1000
            }
            162 -> npcl(FacialExpression.ANNOYED,"Uh... yes, the longhall barkeep has it. So could you get me my drink now please?").also { stage = 1000 }

            165 -> npcl(FacialExpression.ANNOYED,"Not me, no.").also { stage = 1000 }
            170 -> npcl(FacialExpression.AMAZED,"...It is true! The legendary cocktail! I have waited for this day ever since I first started drinking!").also {
                removeItem(player,3707)
                addItem(player,3706)
                stage++
            }
            171 -> npcl(FacialExpression.HAPPY,"Here outerlander, you may take my token. I will happily give up my place at the longhalls table of champions just for a taste of this exquisite beverage!").also { stage++ }
            172 -> playerl(FacialExpression.ASKING,"It's just a drink...").also { stage++ }
            173 -> npcl(FacialExpression.HAPPY,"No, it is an artform. A drink such as this should be appreciated, and admired.").also { stage++ }
            174 -> npcl(FacialExpression.HAPPY,"It is like a fine painting, or a tasteful sculpture. If what I hear is true, then all other drinks become like unpalatable water in comparison to this!").also { stage++ }
            175 -> playerl(FacialExpression.HAPPY,"I guess you're happy with the trade then!").also { stage = 1000 }

            180 -> npcl(FacialExpression.HAPPY,"Ah, but it was not just any drink... It was the finest cocktail ever created! Now that I have tasted it, I need never drink again, for my tastebuds will never be so excited!").also { stage++ }
            181 -> playerl(FacialExpression.ASKING,"So it was nice?").also { stage++ }
            182 -> npcl(FacialExpression.HAPPY,"It was... exquisite!").also { stage++ }
            183 -> playerl(FacialExpression.ASKING,"What did it taste of, then?").also { stage++ }
            184 -> npcl(FacialExpression.HAPPY,"Mostly tomato juice.").also { stage = 1000 }

            190 -> npcl(FacialExpression.HAPPY,"Hey! It's ${player.getAttribute("fremennikname","clyde smilers")}! Let me buy you a drink!").also {
                addItem(player, Items.BEER_1917)
                stage++ }
            191 -> npcl(FacialExpression.HAPPY,"There ya go! Anyone who can drink like you earns my respect!").also { stage = 1000 }

            200 -> npcl(FacialExpression.HAPPY,"Do not think me rude outerlander, but our customs forbid me talking to you. All contact with outerlanders must be vetted by our chieftain, Brundt.").also { stage++ }
            201 -> playerl(FacialExpression.ASKING,"Where is this Brundt?").also { stage++ }
            202 -> npcl(FacialExpression.HAPPY,"He is standing just over there. He will speak for the tribe.").also { stage = 1000 }


            1000 -> end()
        }
        return true
    }

    class DrinkingPulse(val player: Player?,val npc: NPC?, val lowAlcohol: Boolean? = false) : Pulse(){
        var counter = 0
        override fun pulse(): Boolean {
            if(!lowAlcohol!!){
                when(counter++){
                    0 -> {player?.lock();npc?.lock(); npc?.isNeverWalks = true}
                    1 -> {player?.face(npc); npc?.face(player)}
                    3 -> npc?.animator?.animate(Animation(1330,Animator.Priority.HIGH))
                    5 -> {player?.animator?.animate(Animation(1330,Animator.Priority.HIGH)); player?.inventory?.remove(Item(3711))}
                    7 -> player?.dialogueInterpreter?.sendDialogues(player,FacialExpression.DRUNK,"Ish no fair!! I canna drink another drop! I alsho","feel veddy, veddy ill...")
                    15 -> player?.dialogueInterpreter?.sendDialogues(npc,FacialExpression.DRUNK,"I guessh I win then ouddaladder! (hic) Niche try,","anyway!")
                    16 -> {player?.unlock(); npc?.unlock(); player?.face(player);npc?.face(npc); npc?.isNeverWalks = false; return true}
                }
            } else {
                when(counter++){
                    0 -> {player?.lock();npc?.lock(); npc?.isNeverWalks = true}
                    1 -> {player?.face(npc); npc?.face(player)}
                    3 -> npc?.animator?.animate(Animation(1330,Animator.Priority.HIGH))
                    5 -> {player?.animator?.animate(Animation(1330,Animator.Priority.HIGH)); player?.inventory?.remove(Item(3711))}
                    7 -> player?.dialogueInterpreter?.sendDialogues(player,FacialExpression.HAPPY,"Aaaah, lovely stuff. So you want to get the next round","in, or shall I? You don't look so good there!")
                    15 -> player?.dialogueInterpreter?.sendDialogues(npc,FacialExpression.DRUNK,"Wassha? Guh? You drank that whole keg! But it dinnna","affect you at all! I conshede! You can probably","outdrink me!")
                    21 -> player?.dialogueInterpreter?.sendDialogues(npc,FacialExpression.DRUNK,"I jusht can't (hic) believe it! Thatsh shome might fine","drinking legs you got! Anyone who can drink like","THAT getsh my vote atta somsh.... coumah... gets my","vote!")
                    22 -> {player?.unlock(); npc?.unlock(); player?.face(player);npc?.face(npc); npc?.isNeverWalks = false;player?.removeAttribute("fremtrials:cherrybomb");player?.removeAttribute("fremtrials:manni-accepted");player?.removeAttribute("fremtrials:keg-mixed"); player?.setAttribute("/save:fremtrials:manni-vote",true); player?.setAttribute("/save:fremtrials:votes",player.getAttribute("fremtrials:votes",0) + 1); return true}
                }
            }
            return false
        }
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return ManniDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(1286)
    }

}