package content.region.misthalin.digsite.quest.thedigsite

import core.api.*
import core.game.dialogue.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.Items
import org.rs09.consts.NPCs

@Initializable
class DigsiteWorkmanDialogue (player: Player? = null) : DialoguePlugin(player), InteractionListener {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        npc!!
        when(stage) {
            START_DIALOGUE -> playerl(FacialExpression.FRIENDLY, "Hello!").also { stage++ }
            1 -> npcl(FacialExpression.FRIENDLY, "Why hello there! What can I do you for?").also { stage++ }
            2 -> showTopics(
                    Topic(FacialExpression.FRIENDLY, "What do you do here?", 3),
                    Topic(FacialExpression.FRIENDLY, "I'm not sure.", 6),
                    Topic(FacialExpression.FRIENDLY, "Can I dig around here?", 7),
            )
            3 -> npcl(FacialExpression.FRIENDLY, "Well, my job involved digging for finds, cleaning them and transporting them for identification.").also { stage++ }
            4 -> playerl(FacialExpression.FRIENDLY, "Sounds interesting.").also { stage++ }
            5 -> npcl(FacialExpression.FRIENDLY, "I find it very interesting and very rewarding. So glad to see you're taking an interest in the digsite. Hope to see you out here digging sometime!").also {
                stage = END_DIALOGUE
            }
            6 -> npcl(FacialExpression.FRIENDLY, "Well, let me know when you are and I'll do my very best to help you!").also {
                stage = END_DIALOGUE
            }
            7 -> npcl(FacialExpression.FRIENDLY, "You can only use a site you have the appropriate exam level for.").also { stage++ }
            8 -> playerl(FacialExpression.FRIENDLY, "Appropriate exam level?").also { stage++ }
            9 -> npcl(FacialExpression.FRIENDLY, "Oh yes, you need to have been trained in the various techniques before you can be allowed to dig for artefacts.").also { stage++ }
            10 -> playerl(FacialExpression.FRIENDLY, "Ah yes, I understand.").also {
                stage = END_DIALOGUE
            }
        }
        return true
    }
    override fun newInstance(player: Player): DialoguePlugin {
        return DigsiteWorkmanDialogue(player)
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DIGSITE_WORKMAN_613, NPCs.DIGSITE_WORKMAN_4564, NPCs.DIGSITE_WORKMAN_4565/*, NPCs.DIGSITE_WORKMAN_5958*/)
    }

    override fun defineListeners() {
        onUseWith(IntType.NPC, Items.INVITATION_LETTER_696, NPCs.DIGSITE_WORKMAN_613, NPCs.DIGSITE_WORKMAN_4564, NPCs.DIGSITE_WORKMAN_4565) { player, used, with ->
            openDialogue(player, DigsiteWorkmanDialogueFile(), with as NPC)
            return@onUseWith false
        }
    }
}
class DigsiteWorkmanDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {

        // Fallback dialogue.
        b.onPredicate { _ -> true }
                .playerl(FacialExpression.FRIENDLY, "Here, have a look at this...")
                .npc(FacialExpression.FRIENDLY, "I give permission... blah de blah... err. Okay, that's all in", "order, you may use the mineshaft now. I'll hang onto", "this scroll, shall I?")
                .endWith { _, player ->
                    removeItem(player, Items.INVITATION_LETTER_696)
                    if(getQuestStage(player, TheDigSite.questName) == 7) {
                        setQuestStage(player, TheDigSite.questName, 8)
                    }
                }
    }
}