package rs09.game.content.quest.members.monksfriend


import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable


@Initializable
/**
* Handles BrotherCedricDialogue Dialogue
* @author Kya
*/
class BrotherCedricDialogue(player: Player? = null): DialoguePlugin(player) {
    override fun newInstance(player: Player?): DialoguePlugin {
        return BrotherCedricDialogue(player)
    }

    //Item declaration
    private val JUG = 1937
    private val LOGS = 1511

    override fun open(vararg args: Any?): Boolean {
        npc = (args[0] as NPC).getShownNPC(player)
        val qstage = player?.questRepository?.getStage("Monk's Friend") ?: -1

        when(qstage) {
            0  -> playerl(FacialExpression.HAPPY,"Hello.").also { stage = 1 }                                                                               // First dialogue
            10 -> playerl(FacialExpression.HAPPY,"Hello.").also { stage = 1 }                                                                               // Finding blanket
            20 -> playerl(FacialExpression.HAPPY,"Hello.").also { stage = 1 }                                                                               // Talk to him again
            30 -> playerl(FacialExpression.HAPPY,"Brother Cedric are you okay?").also{stage = 10}                                                           // Haven't found Cedric
            40 -> playerl(FacialExpression.HAPPY,"Are you okay?").also{stage = 20}                                                                          // Haven't given Cedric water
            41 -> npcl(FacialExpression.HALF_ASKING,"Now I just need to fix this cart and we can go party.?").also{stage = 26}                                    // Haven't completed dialogue
            42 -> npcl(FacialExpression.HALF_ASKING,"Did you manage to get some wood?").also{stage = 40}                                                          // Haven't given Cedric logs
            50 -> playerl(FacialExpression.HAPPY,"Hello Cedric.").also{stage = 50}                                                                          // Helped Cedric
            100 -> npcl(FacialExpression.NEUTRAL,"Brother Omad sends you his thanks! He won't be in a fit state to thank you in person.").also{stage = 1000}  // After quest complete
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            1 -> npcl(FacialExpression.DRUNK,"Honey, money, woman and wine!").also {stage++}
            2 -> playerl(FacialExpression.HALF_ASKING,"Are you ok?").also { stage++}
            3 -> npcl(FacialExpression.DRUNK,"Yesshh...hic up...beautiful!").also {stage++}
            4 -> playerl(FacialExpression.NEUTRAL,"Take care old monk.").also { stage++}
            5 -> npcl(FacialExpression.DRUNK,"La..di..da..hic..up!").also {stage = 1000}

            10 -> npcl(FacialExpression.DRUNK,"Yeesshhh, I'm very, very drunk..hic..up..").also { stage++}
            11 -> playerl(FacialExpression.NEUTRAL,"Brother Omad needs the wine for the party.").also {stage++}
            12 -> npcl(FacialExpression.SAD,"Oh dear, oh dear, I knew I had to do something!").also { stage = 1000}.also{player.questRepository.getQuest("Monk's Friend").setStage(player, 40)}

            20 -> npcl(FacialExpression.DRUNK,"Hic up! Oh my head! I need a jug of water.").also { stage++}
            21 -> if(player.inventory.containItems(JUG)){
                playerl(FacialExpression.FRIENDLY,"Cedric! Here, drink! I have some water.").also{stage++}
            } else {
                playerl(FacialExpression.NEUTRAL,"I'll see if I can get some.").also{stage = 999}
            }
            22 -> npcl(FacialExpression.DRUNK,"Good stuff, my head's spinning!").also { stage++}
            23 -> sendDialogue("You hand the monk a jug of water.").also{stage++}.also{player.inventory.remove(Item(JUG))}.also{player.questRepository.getQuest("Monk's Friend").setStage(player, 41)}
            24 -> npcl(FacialExpression.HAPPY,"Aah! That's better!").also { stage++}
            25 -> npcl(FacialExpression.HAPPY,"Now I just need to fix this cart and we can go party.").also { stage++}
            26 -> npcl(FacialExpression.NEUTRAL,"Could you help?").also { stage++}
            27 -> options(
                    "No, I've helped enough monks today!",
                    "Yes, I'd be happy to!").also { stage++}
            28 -> when(buttonId){
                1 -> playerl(FacialExpression.ANGRY,"No, I've helped enough monks today!").also { stage=998}
                2 -> playerl(FacialExpression.FRIENDLY,"Yes, I'd be happy to!").also { stage++}
            }
            29 -> npcl(FacialExpression.HAPPY,"Excellent, I just need some wood.").also{stage++}
            30 -> playerl(FacialExpression.NEUTRAL,"Ok, I'll see what I can find.").also {stage = 1000}.also{player.questRepository.getQuest("Monk's Friend").setStage(player, 42)}

            40 -> if(player.inventory.containItems(LOGS)){
                sendDialogue("You hand Cedric some logs.").also{stage++}.also{player.inventory.remove(Item(LOGS))}.also{player.questRepository.getQuest("Monk's Friend").setStage(player, 50)}
            } else {
                playerl(FacialExpression.SAD,"Not yet, I'm afraid.").also{stage = 1000}
            }
            41 -> playerl(FacialExpression.HAPPY,"Here you go!").also { stage++ }
            42 -> npcl(FacialExpression.HAPPY,"Well done! Now I'll fix this cart. You head back to Brother Omad and tell him I'll be there soon.").also{ stage++}
            43 -> playerl(FacialExpression.HAPPY,"Ok! I'll see you later!").also{stage=1000}

            50 -> npcl(FacialExpression.NEUTRAL,"Hi, I'm almost done here. Could you tell Omad that I'll be back soon?").also { stage = 1000 }

            60 -> npcl(FacialExpression.HAPPY,"Brother Omad sends you his thanks! He won't be in a fit state to thank you in person.").also{stage = 1000}

            998 -> npcl(FacialExpression.HAPPY,"In that case I'd better drink more wine! It helps me think.").also { stage=1000}
            999 -> npcl(FacialExpression.DRUNK,"Thanks! *hic*").also{stage++}
            1000 -> end()
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(280)
    }

}
