package content.region.desert.alkharid.dialogue

import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs
import core.game.dialogue.Topic
import core.game.interaction.InteractionListener
import core.game.interaction.IntType
import core.tools.END_DIALOGUE

/**
 * @author bushtail
 */

@Initializable
class FadliDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player) {
    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return FadliDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player(core.game.dialogue.FacialExpression.FRIENDLY, "Hi!").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage) {
            0 -> npcl(core.game.dialogue.FacialExpression.HALF_ASKING, "What?").also { stage++ }
            1 -> showTopics(
                Topic("What do you do?", 101),
                Topic("What is this place?", 201),
                Topic(core.game.dialogue.FacialExpression.FRIENDLY,"I'd like to store some items, please.", 301),
                Topic("Do you watch any matches?", 401)
            )

            101 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY, "You can store your stuff here if you want. You can dump anything you don't want to carry whilst your fighting duels and then pick it up again on the way out.").also { stage++ }
            102 -> npcl(core.game.dialogue.FacialExpression.SAD, "To be honest I'm wasted here.").also { stage++ }
            103 -> npcl(core.game.dialogue.FacialExpression.EVIL_LAUGH, "I should be winning duels in an arena! I'm the best warrior in Al Kharid!").also { stage++ }
            104 -> player(core.game.dialogue.FacialExpression.HALF_WORRIED, "Easy, tiger!").also { stage = END_DIALOGUE }

            201 -> npcl(core.game.dialogue.FacialExpression.LAUGH, "Isn't it obvious?").also { stage++ }
            202 -> npcl(core.game.dialogue.FacialExpression.ANNOYED, "This is the Duel Arena...duh!").also { stage = END_DIALOGUE }

            301 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY, "Sure.").also {
                end()
                player.bank.open()
            }

            401 -> npcl(core.game.dialogue.FacialExpression.LAUGH, "Most aren't any good so I throw rotten fruit at them!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FADLI_958)
    }
}

class FadliListener : InteractionListener {
    override fun defineListeners() {
        on(NPCs.FADLI_958, IntType.NPC, "buy") { player, node ->
            if(node.asNpc().openShop(player)) {
                return@on true
            }
            return@on false
        }
    }
}