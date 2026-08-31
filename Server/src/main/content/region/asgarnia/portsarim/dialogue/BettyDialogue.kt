package content.region.asgarnia.portsarim.dialogue

import content.data.Quests
import content.region.kandarin.yanille.quest.thehandinthesand.dialogue.HITSBettyDialogueFile
import content.region.kandarin.yanille.quest.thehandinthesand.quest.TheHandintheSand
import core.api.getQuestStage
import core.api.openDialogue
import core.game.dialogue.ChatAnim
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialogueOption
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

/**
 * Dialogue for Betty's Magic Emporium.
 * @author Edith
 */

@Initializable
class BettyDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player): DialoguePlugin {
        return BettyDialogue(player)
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, BettyDialogueFile(), npc)
        return false
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BETTY_583)
    }

}

class BettyDialogueFile : DialogueLabeller() {
    override fun addConversation() {

        exec { player, _ ->
            val stage = getQuestStage(player, Quests.THE_HAND_IN_THE_SAND)

            if ((stage >= TheHandintheSand.STAGE_MAGICAL_ORB_35 &&
                        stage < TheHandintheSand.STAGE_INTERROGATE_SANDY_50) ||
                (stage >= TheHandintheSand.STAGE_SANDY_CONFESSION_55 &&
                        stage < TheHandintheSand.STAGE_QUEST_COMPLETED_100) ||
                stage >= TheHandintheSand.STAGE_QUEST_COMPLETED_100
            ) {
                loadLabel(player, "quest_options")
            } else {
                loadLabel(player, "default_options")
            }
        }

        label("default_options")
            npc(ChatAnim.HAPPY, "Welcome to the magic emporium.")
            options(
                DialogueOption("open_shop", "Can I see your wares?"),
                DialogueOption("not_into_magic", "Sorry, I'm not into Magic.", expression = FacialExpression.HALF_GUILTY)
            )

        label("open_shop")
            exec { player, npc ->
                npc.openShop(player)
            }

        label("not_into_magic")
            npc(ChatAnim.HAPPY, "Well, if you see anyone who is into Magic, please send", "them my way.")

        /** If the player is doing The Hand in the Sand quest. */
        label("quest_options")
            options(
                DialogueOption("hand_in_sand", "Talk to Betty about the Hand in the Sand quest.", skipPlayer = true),
                DialogueOption("default_options", "Talk to Betty about her shop.", skipPlayer = true)
            )

        label("hand_in_sand")
            open(player!!, HITSBettyDialogueFile(), npc!!)
    }

}