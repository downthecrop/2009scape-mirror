package content.region.asgarnia.burthorpe.dialogue

import core.api.openDialogue
import core.api.openNpcShop
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

@Initializable
class LillyDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, LillyDialogueFile(), npc)
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return LillyDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.LILLY_4294)
    }
}

class LillyDialogueFile : DialogueBuilderFile() {

    override fun create(b: DialogueBuilder) {
        b.defaultDialogue().npcl(
            FacialExpression.HALF_GUILTY, "Uh..... hi... didn't see you there. Can.... I help?"
        ).playerl(
            FacialExpression.HALF_ASKING, " Umm... do you sell potions?"
        ).npcl(
            FacialExpression.HALF_GUILTY, " Erm... yes. When I'm not drinking them."
        ).options().let { optionsBuilder ->
            optionsBuilder.option("I'd like to see what you have for sale.")
                .playerl(FacialExpression.FRIENDLY, "I'd like to see what you have for sale. ")
                .endWith { _, player -> openNpcShop(player, NPCs.LILLY_4294) }
            optionsBuilder.option("That's a pretty wall hanging.").playerl(
                FacialExpression.FRIENDLY, "That's a pretty wall hanging."
            ).npcl(
                FacialExpression.HAPPY, " Do you think so? I made it myself."
            ).playerl(
                FacialExpression.ASKING, " Really? Is that why there's all this cloth and dye around?"
            ).npcl(
                FacialExpression.HAPPY, " Yes, it's a hobby of mine when I'm.... relaxing."
            ).end()
            optionsBuilder.option("Bye. ").playerl(FacialExpression.FRIENDLY, "Bye.")
                .npcl(FacialExpression.FRIENDLY, " Have fun and come back soon!").end()
        }
    }
}