package rs09.game.content.quest.members.thefremenniktrials

import api.addItem
import api.inInventory
import api.removeItem
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.item.Item
import org.rs09.consts.Items

@Initializable
class LalliDialogue(player: Player? = null) : DialoguePlugin(player){
    override fun open(vararg args: Any?): Boolean {
        player?.let {
            println(it.getAttribute("lalliEatStew", false))
            if (it.questRepository.isComplete("Fremennik Trials")){
                playerl(FacialExpression.NEUTRAL,"Hello there.")
                stage = 100
                return true
            }
            if (it.getAttribute("lalliStewCabbageAdded", false)!! && it.getAttribute("lalliStewOnionAdded", false)!! && it.getAttribute("lalliStewPotatoAdded", false)!! && it.getAttribute("lalliStewRockAdded", false)!!){
                npcl(FacialExpression.OLD_NORMAL,"It am ready now?")
                stage = 60
                return true
            }
            if(it.getAttribute("lalliStewCabbageAdded", false)!! || it.getAttribute("lalliStewOnionAdded", false)!! || it.getAttribute("lalliStewPotatoAdded", false)!! || it.getAttribute("lalliStewRockAdded", false)!!){
                npcl(FacialExpression.OLD_NORMAL,"It am ready now?")
                stage = 58
                return true
            }
            if (it.getAttribute("lalliEatStew", false)!!){
                playerl(FacialExpression.NEUTRAL,"Hello there.")
                stage = 65
                return true
            }
            if (it.getAttribute("hasWool", false)!!){
                playerl(FacialExpression.NEUTRAL,"Hello there.")
                stage = 75
                return true
            }
            if (it.getAttribute("fremtrials:askeladden-talkedto", false)!!) {
                player("Hello there.")
                stage = 50
                return true
            }
            if(player.questRepository.isComplete("Fremennik Trials")){
                playerl(FacialExpression.HAPPY,"Hello there.")
                stage = 100
                return true
            }
            if (it.questRepository.getStage("Fremennik Trials") > 0) {
                player("Hello there.").also { stage = 0; return true }
            }
        }
        return true
    }



    override fun newInstance(player: Player?): DialoguePlugin {
        return LalliDialogue(player)
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> npc(FacialExpression.OLD_SNEAKY,"Bah! Puny humans always try steal Lalli's golden","apples! You go away now!").also { stage++ }
            1 -> player(FacialExpression.ANNOYED,"Actually, I'm not after your golden apples. I was","wondering if I could have some golden wool, I need it","to string a lyre.").also { stage++ }
            2 -> npc(FacialExpression.OLD_SNEAKY,"Ha! You not fool me human! Me am smart! Other","trolls so jealous of how brainy I are, they kick me out","of camp and make me live down here in cave! But me","have last funny!").also { stage++ }
            3 -> npc(FacialExpression.OLD_SNEAKY,"Me find golden apples on tree and me build wall to stop","anyone who not Lalli eating lovely golden apples! Did","me not tell you I are smart?").also { stage++ }
            4 -> player("Yes, yes, you are incredibly clever. Now please can I","have some golden wool?").also { stage++ }
            5 -> npc(FacialExpression.OLD_SNEAKY,"Hmm, me think you not really think I are clever. Me","think you is trying to trick Lalli. Me not like you as much","as other human. He give me present. I give him wool.").also { stage++ }
            6 -> options("Other human?","No, honest, you're REALLY clever.","Can I give you a present?").also { stage++ }
            7 -> when(buttonId){
                1 -> player("Other human? You mean someone else has been here","and you gave them wool?").also { stage = 10 }
                2 -> player("No, honest, you're REALLY clever!").also { stage = 20 }
                3 -> player("Can I give you a present.").also { stage = 30 }
            }

                //Other human?
                10 -> npc(FacialExpression.OLD_SNEAKY,"Human call itself Askeladden! It not trick Lalli. Lalli do","good deal with human! Stupid human get some dumb","wool, but did not get golden apples!").also { stage++ }
                11 -> player("I see... okay, well, bye!").also { player.setAttribute("/save:fremtrials:lalli-talkedto",true); stage = 1000 }

                //Honest, you're clever
                20 -> npc(FacialExpression.OLD_SNEAKY,"Me no believe you tell truth. Go away.").also { stage = 1000 }

                //Can I give you a present
                30 -> npc(FacialExpression.OLD_SNEAKY,"Me think about that.").also { stage = 1000 }

            50 -> npc(FacialExpression.OLD_SNEAKY,"Bah! Puny humans always try steal Lalli's golden","apples! You go away now!").also { stage++ }
            51 -> player("Wait! I have something that might interest you... a pet", "rock!").also { stage++ }
            52 -> npc(FacialExpression.OLD_LAUGH2, "Hah! You think me stupid or something human? Me", "already got one! Me don't want lots of baby rocks either,", "so me don't want another one around the place!").also { stage++ }
            53 -> player(FacialExpression.HALF_CRYING,"Please... all I want is some of your golden wool...").also { stage++ }
            54 -> npc(FacialExpression.OLD_SNEAKY, "Stupid human think he can trick me into giving away", "some golden apples! Hah! Golden apples is all me got to", "eat. you not get them. no way!").also { stage++ }
            55 -> player(FacialExpression.HALF_THINKING, "Hmm... So you're hungry? I think I will have the", "perfect thing for you to eat... I just need to get myself", "an onion, a potato, and a cabbage...").also { stage++ }
            56 -> interpreter.sendDialogue("You have a cunning plan to trick this troll. You need your pet rock,", "a cabbage, a potato and an onion.").also { player.removeAttribute("fremtrials:fremtrials:lalli-talkedto");stage = 1000 }

            58 -> playerl(FacialExpression.NEUTRAL,"Not just yet...").also { stage = 1000 }

            60 -> playerl(FacialExpression.HAPPY,"Indeed it is. Try it and see.").also { stage++ }
            61 -> npcl(FacialExpression.OLD_HAPPY,"Hmm... YUM! That are delicious! Me never know human know to make soup out of stone? It some special stone?").also { stage++ }
            62 -> playerl(FacialExpression.NEUTRAL,"Indeed it is. But I'm willing to trade it.").also { stage++ }
            63 -> npcl(FacialExpression.OLD_SNEAKY,"Let me think about that, me like to think.").also {
                player.setAttribute("/save:lalliEatStew",true)
                player.removeAttribute("lalliStewOnionAdded")
                player.removeAttribute("lalliStewPotatoAdded")
                player.removeAttribute("lalliStewCabbageAdded")
                player.removeAttribute("lalliStewRockAdded")
                stage = 1000
            }

            65 -> npcl(FacialExpression.OLD_HAPPY,"Your soup very tasty, human! But me still not want trade golden apples for your stone. Me think pet rock get jealous.").also { stage++ }
            66 -> playerl(FacialExpression.ANGRY,"I. DON'T. WANT. ANY. GOLDEN. APPLES. ALL. I. WANT. IS. A. GOLDEN. FLEECE.").also { stage++ }
            67 -> npcl(FacialExpression.OLD_ALMOST_CRYING,"Gee, sorry human, all you have do is ask, me not need you to shout. You act like you think Lalli am stupid or something...").also {
                if(player.inventory.isFull){
                    stage = 68
                }else{
                    stage = 70
                }
            }
            68 -> npcl(FacialExpression.OLD_NORMAL,"Me would give you golden fleece if me think you have enough room in your inventory.").also { stage = 1000 }
            70 -> npcl(FacialExpression.OLD_HAPPY,"Here you go. Hah! Me trick you Human! All you got is worthless golden fleece! Me got very rare soup-making stone!").also { stage = 71 }
            71 -> playerl(FacialExpression.HAPPY,"Glad you're happy Lalli!").also {
                player.inventory.add(Item(Items.GOLDEN_FLEECE_3693))
                player.setAttribute("/save:hasWool",true)
                player.removeAttribute("lalliEatStew")
                stage = 1000
            }
            75 -> npcl(FacialExpression.OLD_HAPPY,"Hah! How you like worthless golden fleece! Me very like rare soup-making stone!").also { stage++ }
            76 -> playerl(FacialExpression.HAPPY,"Oh it's great, thank you again for the fleece Lalli, I hope you're enjoying your soup").also { stage = 1000 }

            100 -> npcl(FacialExpression.OLD_HAPPY,"Ha! Stupid human! You no get any golden apples! Even though me much like stone soup!").also { stage++ }
            101 -> playerl(FacialExpression.ASKING,"Can I please have some more golden wool?").also { stage++ }
            102 -> npcl(FacialExpression.OLD_HAPPY,"Me will sell you some golden wool, but me no sell you any golden apples!").also { stage++ }
            103 -> playerl(FacialExpression.HAPPY,"How much for the golden wool?").also { stage++ }
            104 -> npcl(FacialExpression.OLD_HAPPY,"Me want 1000 coins! You pay or go!").also {
                stage = if(inInventory(player,Items.COINS_995,1000)){
                    105
                }
                else 115
            }
            105 -> options("Pay","Don't pay").also { stage++ }
            106 -> when(buttonId){
                1 -> playerl(FacialExpression.HAPPY,"Is that all? Here you go!").also {
                    removeItem(player,Item(Items.COINS_995,1000))
                    addItem(player,Items.GOLDEN_FLEECE_3693)
                    stage++
                }
                2 -> npcl(FacialExpression.OLD_HAPPY,"You no pay, then you go! You no get any golden apples!").also { stage = 1000 }
            }
            107 -> npcl(FacialExpression.OLD_HAPPY,"Ha! Stupid human think he buy golden apples, but he actually get stupid wool! Hahaha!").also { stage = 1000 }

            115 -> playerl(FacialExpression.THINKING,"I don't even have that much cash on me...").also { stage++ }
            116 -> npcl(FacialExpression.OLD_NORMAL,"Well stupid bad trade human don't get no wool then! Or any golden apples!").also { stage = 1000 }

            1000 -> end()
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(1270)
    }

}