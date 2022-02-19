package rs09.game.content.quest.members.thefremenniktrials

import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs09.consts.Items

@Initializable
class ThoraDialogue(player: Player? = null) : DialoguePlugin(player){
    val fName = player?.getAttribute("fremennikname","name jeff")
    var curNPC: NPC? = NPC(0, Location(0,0,0))
    override fun open(vararg args: Any?): Boolean {
        curNPC = args[0] as? NPC
        npc = args[0] as NPC
        if(player.inventory.contains(3707,1)){
            playerl(FacialExpression.ASKING,"Thanks for making me this cocktail. Why don't you make them anymore normally?")
            stage = 35
        }
        else if(player.inventory.contains(3709,1)){
            playerl(FacialExpression.HAPPY,"Hi! Can I please have one of your legendary cocktails now?")
            stage = 25
        }
        else if(player?.getAttribute("sigmundreturning",false) == true){
            playerl(FacialExpression.ASKING,"I'm trying to remember who I was meant to give this trade item to.")
            stage = 30
        }
        else if(player?.getAttribute("sigmund-steps", 0) == 13){
            playerl(FacialExpression.ASKING,"I don't suppose you have any idea where I could find a written promise from Askeladden to stay out of the Longhall?")
            stage = 20
        }
        else if(player?.getAttribute("sigmund-steps", 0) == 12){
            playerl(FacialExpression.ASKING,"I don't suppose you have any idea where I could find the longhall barkeeps' legendary cocktail, do you?")
            stage = 1
        }
        else if(player.questRepository.isComplete("Fremennik Trials")){
            npcl(FacialExpression.HAPPY,"Hello again, $fName. I suppose you want a drink? Or are you going to try another scam with that terrible Askeladden again?")
            stage = 50
        }
        else{
            playerl(FacialExpression.HAPPY,"Hello there.")
            stage = 60
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            1 -> npcl(FacialExpression.AMAZED,"How did you hear about that? I didn't think anybody knew about that.").also { stage++ }
            2 -> npcl(FacialExpression.HAPPY,"Well, it is true that in my younger years as a barkeep, I wandered the lands trying various alcoholic delicacies.").also { stage++ }
            3 -> npcl(FacialExpression.HAPPY,"Do know how many different types of alcohol there are here in Gielinor? Lots!").also { stage++ }
            4 -> npcl(FacialExpression.HAPPY,"Well, anyway, I used a fusion of various drinks from all around the world to create the greatest cocktail ever made!").also { stage++ }
            5 -> npcl(FacialExpression.HAPPY,"Of course, when my wander lust was gone, and I returned to Rellekka to serve as barkeep here, I gave all that up.").also { stage++ }
            6 -> playerl(FacialExpression.ASKING,"But you still remember how to make it, right?").also { stage++ }
            7 -> npcl(FacialExpression.HAPPY,"Of course.").also { stage++ }
            8 -> playerl(FacialExpression.ASKING,"And you have all the ingredients here? I don't need to go chasing round the world for obscure ingredients to make it?").also { stage++ }
            9 -> npcl(FacialExpression.ASKING,"No, I have them all here. Why?").also { stage++ }
            10 -> playerl(FacialExpression.ASKING,"Can you make me your legendary cocktail then?").also { stage++ }
            11 -> npcl(FacialExpression.SAD,"I would rather not; it is a reminder of a life I left behind when I came back.").also { stage++ }
            12 -> playerl(FacialExpression.ASKING,"Any way I could change your mind?").also { stage++ }
            13 -> npcl(FacialExpression.THINKING,"You need this to become a Fremennik, right? Well, you seem okay for an outerlander, it would be a shame to see you fail. You know Askeladden?").also { stage++ }
            14 -> playerl(FacialExpression.THINKING,"That kid outside? Sure.").also { stage++ }
            15 -> npcl(FacialExpression.ANGRY,"He is nothing but a pest. He keeps sneaking in and stealing beer.").also { stage++ }
            16 -> npcl(FacialExpression.ANGRY,"I shudder to think what he will be like when he has passed his trial of manhood, and is allowed in here legitimately.").also { stage++ }
            17 -> npcl(FacialExpression.ANGRY,"If you can get him to sign a contract promising that he will NEVER EVER EVER darken my doorway here again, you get the drink.").also { stage++ }
            18 -> playerl(FacialExpression.ASKING,"Any idea how I can get him to do that?").also { stage++ }
            19 -> npcl(FacialExpression.HAPPY,"Knowing that little horror, he'll probably be willing to in exchange for some cash. You should go ask him yourself though.").also {
                player?.incrementAttribute("sigmund-steps",1)
                stage = 1000
            }
            20 -> npcl(FacialExpression.ANNOYED,"Well, as I say, you should talk to him about that. Knowing the little runt as I do though He'll probably do it for the cash.").also {stage++}

            25 -> npcl(FacialExpression.AMAZED,"What?!?! I can't believe you... Let me look at that... Askeladden would NEVER... Gosh. It looks legitimate.").also {
                player?.inventory?.remove(Item(Items.PROMISSORY_NOTE_3709))
                player?.inventory?.add(Item(Items.LEGENDARY_COCKTAIL_3707))
                player?.setAttribute("/save:sigmundreturning",true)
                stage++
            }
            26 -> npcl(FacialExpression.HAPPY,"Here you go, on the house! You have made my life SO much easier! Knowing that little monster won't be bugging me in here all the time anymore!").also { stage++ }
            27 -> npcl(FacialExpression.HAPPY,"That little weasel will have to abide by this written promise that Askeladden can never ever enter the Longhall again! He can't get round this one!").also { stage++ }
            28 -> playerl(FacialExpression.SUSPICIOUS,"Uh... yeah... yeah, you probably won't see someone called Askeladden coming in here...").also { stage = 1000 }

            30 -> npcl(FacialExpression.NEUTRAL,"If it's not the note from Askeladden it isn't me, I'm afraid.").also { stage = 1000 }

            35 -> npcl(FacialExpression.SAD,"Ah... when I gave up my travels across the world many years back, to return to my expected role as longhall barkeep,").also { stage++ }
            36 -> npcl(FacialExpression.SAD,"as my mother, and her mother, were before me, I gave up a lot of the freedom I had found in the outside world.").also { stage++ }
            37 -> npcl(FacialExpression.SAD,"I know it is our custom to shun outerlanders and their ways, but I didn't find them as bad as the stories say.").also { stage++ }
            38 -> npcl(FacialExpression.SAD,"Sometimes I feel as though we Fremennik live in a prison that we have constructed for ourselves,").also { stage++ }
            39 -> npcl(FacialExpression.SAD,"and that WE are the outerlanders, out here on the edge of the world...").also { stage++ }
            40 -> npcl(FacialExpression.SAD,"I'm sorry, I think it is part of the job of Longhall barkeep to get philosophical about things ocassionally.").also { stage++ }
            41 -> npcl(FacialExpression.HAPPY,"I wish you all the best of luck with your trials, outerlander.").also { stage++ }
            42 -> npcl(FacialExpression.HAPPY,"When you have finished, perhaps you will come back here, and we can share a drink over tales of the outside world?").also { stage++ }
            43 -> playerl(FacialExpression.HAPPY,"Thanks, I'd like that.").also { stage = 1000 }

            50 -> playerl(FacialExpression.THINKING,"Scam? Oh... You mean his promise to not come in here anymore?").also { stage++ }
            51 -> npcl(FacialExpression.ANNOYED,"Yes. Yes I do. I should have never trusted him not to come in here, even with that written promise. Anyway, do you want a drink or not?").also { stage++ }
            52 -> options("Yes please","No thanks").also { stage++ }
            53 -> when(buttonId){
                1 -> playerl(FacialExpression.HAPPY,"Yes please").also {
                    npc.openShop(player)
                    stage = 1000
                }
                2 -> playerl(FacialExpression.HAPPY,"No thanks.").also { stage++ }
            }
            54 -> npcl(FacialExpression.HAPPY,"Okay then.").also { stage = 1000 }

            60 -> npcl(FacialExpression.HAPPY,"Hello yourself, outerlander. A little out of the way up here, aren't you?").also { stage++ }
            61 -> npcl(FacialExpression.HAPPY,"I would love to stop and chat with you, but unfortunately we have a custom that no Fremennik may speak with the outerlanders without the permission of our chieftain. Don't take it personally.").also { stage++ }
            62 -> npcl(FacialExpression.HAPPY,"The Chieftain is stood just over there, his name is Brundt. I suggest you go and introduce yourself.").also { stage++ }
            63 -> npcl(FacialExpression.ASKING,"You wouldn't want to go making any enemies because you weren't aware of our customs now, would you?").also { stage++ }
            64 -> playerl(FacialExpression.THINKING,"No, I guess not. Thanks anyway.").also { stage++ }
            65 -> npcl(FacialExpression.HAPPY,"Not a problem, outerlander. It's always nice to see a new face in the long hall; it happens so rarely.").also { stage = 1000 }

            1000 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return ThoraDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(1300)
    }
}