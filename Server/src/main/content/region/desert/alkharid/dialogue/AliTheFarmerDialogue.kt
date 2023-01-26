package content.region.desert.alkharid.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable

/**
 * @author qmqz
 */

@Initializable
class AliTheFarmerDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player(core.game.dialogue.FacialExpression.FRIENDLY,"Hello there!")
        stage = 0
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> npc(core.game.dialogue.FacialExpression.FRIENDLY,"Oh, er, hello. What do you want?").also { stage++ }
            1 -> options ("What can you tell me about Al Kharid?", "So, what do you do here?",
                "I hear you work for Ali Morrisane...", "I hear you've been threatening the other shopkeepers.").also {  stage++ }
            2 -> when(buttonId){
                1 -> player(core.game.dialogue.FacialExpression.ASKING, "What can you tell me about Al Kharid?").also { stage = 10 }
                2 -> player(core.game.dialogue.FacialExpression.ASKING, "So, what do you do here?").also { stage = 20 }
                3 -> player(core.game.dialogue.FacialExpression.ASKING, "I hear you work for Ali Morrisane...").also { stage = 30 }
                4 -> player(core.game.dialogue.FacialExpression.ASKING, "I hear you've been threatening the other shopkeepers.").also { stage = 40 }

            }

            10 -> npc(core.game.dialogue.FacialExpression.FRIENDLY,"There's not much farming land around here.", "Only that little patch outside.").also { stage++ }
            11 -> player(core.game.dialogue.FacialExpression.ASKING,"Can you give me any advice on farming here in","the desert?").also { stage++ }
            12 -> npc(core.game.dialogue.FacialExpression.FRIENDLY,"Like I said, I only know about cactuses...").also { stage++ }
            13 -> player(core.game.dialogue.FacialExpression.FRIENDLY,"Just tell me about cactuses, then.").also { stage++ }
            14 -> npc(core.game.dialogue.FacialExpression.FRIENDLY,"First you have to weed the patch using a rake.").also { stage++ }
            15 -> player(core.game.dialogue.FacialExpression.ASKING,"Can you give me any other advice?").also { stage++ }
            16 -> npc(core.game.dialogue.FacialExpression.FRIENDLY,"Not really. I've not done much farming recently.").also { stage++ }
            17 -> player(core.game.dialogue.FacialExpression.ASKING,"Well, can you at least sell me any gardening",  "tools or seeds?").also { stage++ }
            18 -> npc(core.game.dialogue.FacialExpression.GUILTY,"Sorry. They haven't been delivered yet.").also { stage = 1 }

            20 -> npc(
                core.game.dialogue.FacialExpression.FRIENDLY,"I'm going to set up a shop selling farming implements.",
                "That patch out there may be small, ", "but it's all we've got.").also { stage = 11 }

            30 -> npc(
                core.game.dialogue.FacialExpression.FRIENDLY,"Yes, he bought these tents and had them put up for us.",
                "He says he'll also get our goods in,", "so we can start selling them soon.").also { stage++ }
            31 -> npc(core.game.dialogue.FacialExpression.FRIENDLY,"I only know how to farm cactuses, so this spot",  "is perfect.").also { stage++ }
            32 -> player(core.game.dialogue.FacialExpression.THINKING,"Maybe I should talk to him...").also { stage++ }
            33 -> npc(core.game.dialogue.FacialExpression.FRIENDLY,"Of course.", "He's always happy to talk to possible business", "partners.").also { stage = 1 }

            40 -> npc(core.game.dialogue.FacialExpression.FRIENDLY,"Now why would you think that?").also { stage++ }
            41 -> player(core.game.dialogue.FacialExpression.FRIENDLY,"One of the shopkeepers told they were threatened", "by a man with a rake...").also { stage++ }
            42 -> npc(core.game.dialogue.FacialExpression.FRIENDLY,"Those people just don't want us to succeed.", "Don't listen to them!").also { stage = 99 }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return AliTheFarmerDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(2821)
    }
}