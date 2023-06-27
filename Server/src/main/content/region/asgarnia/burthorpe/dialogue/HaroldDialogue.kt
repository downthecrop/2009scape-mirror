package content.region.asgarnia.burthorpe.dialogue

import content.region.asgarnia.burthorpe.quest.deathplateau.DeathPlateau
import content.region.asgarnia.burthorpe.quest.deathplateau.HaroldDialogueFile
import core.api.*
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.Animations
import org.rs09.consts.Items
import org.rs09.consts.NPCs

/**
 * Harold main dialogue.
 * @author ovenbread
 */
@Initializable
class HaroldDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        if (isQuestInProgress(player!!, DeathPlateau.questName, 10, 29)) {
            // Call the dialogue file for Harold from the deathplateau quest folder.
            openDialogue(player!!, HaroldDialogueFile(), npc)
        }
        // Fallback to default.
        when (stage) {
            START_DIALOGUE -> player(FacialExpression.FRIENDLY, "Hello there.").also { stage++ }
            1 -> npc(FacialExpression.FRIENDLY, "Hi.").also { stage++ }
            2 -> player(FacialExpression.FRIENDLY, "Can I buy you a drink?").also { stage++ }
            3 -> npc(FacialExpression.HAPPY, "Now you're talking! An Asgarnian Ale, please!").also { stage++ }
            4 -> {
                if (removeItem(player!!, Items.ASGARNIAN_ALE_1905)) {
                    sendMessage(player!!, "You give Harold an Asgarnian Ale.")
                    sendItemDialogue(player!!, Items.ASGARNIAN_ALE_1905, "You give Harold an Asgarnian Ale.").also { stage++ }
                } else {
                    player(FacialExpression.FRIENDLY, "I'll go and get you one.").also { stage = END_DIALOGUE }
                }
            }
            5 -> {
                end()
                animate(npc!!, Animation(Animations.HUMAN_EATTING_829), true)
                runTask(npc!!, 3) {
                    npc(FacialExpression.FRIENDLY, "*burp*").also { stage = END_DIALOGUE }
                }
            }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return HaroldDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.HAROLD_1078)
    }
}