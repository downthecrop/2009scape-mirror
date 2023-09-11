package content.region.misc.keldagrim.dialogue

import core.api.openNpcShop
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.NPCs

@Initializable
class AgmundiDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> npc(FacialExpression.OLD_NOT_INTERESTED,"Oh no, not another human... what do you want then?").also { stage++ }
            1 -> player(FacialExpression.ASKING, "Oh, do you get humans here often?").also { stage++ }
            2 -> npc(FacialExpression.OLD_NORMAL, "Not that often, no, but sometimes.").also { stage++ }
            3 -> npc(FacialExpression.OLD_NORMAL, "Of course, since you people are too big for dwarven", "clothes, they typically don't stay very long.").also { stage++ }
            4 -> player(FacialExpression.SUSPICIOUS, "Why don't you make bigger clothes then?").also { stage++ }
            5 -> npc(FacialExpression.OLD_NOT_INTERESTED, "What'd be the point? Besides, I don't make", "these clothes myself.").also { stage++ }
            6 -> showTopics(
                    Topic(FacialExpression.FRIENDLY, "Who makes these clothes then?", 10),
                    Topic(FacialExpression.FRIENDLY, "I still want to buy your clothes.", 8, true),
                    Topic(FacialExpression.FRIENDLY, "So do you have any quests for me?", 20),
            )
            8 -> openNpcShop(player, NPCs.AGMUNDI_2161).also { stage = END_DIALOGUE }
            10 -> npc(FacialExpression.OLD_DEFAULT,"Oh, my sister, she lives in Keldagrim-East.", "Has a little stall on the other side of", "the Kelda.").also { stage++ }
            11 -> npc(FacialExpression.OLD_DEFAULT,"If she only worked a little harder, like me,", "she wouldn't have to live in the sewers of the city.", "Shame really.").also { stage++ }
            12 -> player(FacialExpression.FRIENDLY, "The sewers? Your sister lives in the sewers?").also { stage++ }
            13 -> npc(FacialExpression.OLD_SAD,"Keldagrim-East, such a ghastly place.", "Not civil, polite and clean like we are in the West.").also { stage++ }
            14 -> player(FacialExpression.SUSPICIOUS, "Uh-huh.").also { stage = END_DIALOGUE }
            20 -> npc(FacialExpression.OLD_NOT_INTERESTED,"Quests? Why would I have any quests?").also { stage++ }
            21 -> player(FacialExpression.FRIENDLY, "Oh, just anything to do would be fine.").also { stage++ }
            22 -> npc(FacialExpression.OLD_NOT_INTERESTED,"No, not right now... maybe I'll have something", "for you to do later, but nothing at the moment.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return AgmundiDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.AGMUNDI_2161)
    }
}