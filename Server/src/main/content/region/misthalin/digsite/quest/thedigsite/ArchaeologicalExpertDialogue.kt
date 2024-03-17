package content.region.misthalin.digsite.quest.thedigsite

import core.api.*
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import org.rs09.consts.NPCs
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable


@Initializable
class ArchaeologicalExpertDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, ArchaeologicalExpertDialogueFile(), npc)
        return true
    }
    override fun newInstance(player: Player): DialoguePlugin {
        return ArchaeologicalExpertDialogue(player)
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ARCHAEOLOGICAL_EXPERT_619)
    }
}
class ArchaeologicalExpertDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {

        // Fallback dialogue.
        b.onPredicate { player -> true }
                .playerl(FacialExpression.FRIENDLY, "Hello. Who are you?")
                .npcl(FacialExpression.FRIENDLY, "Good day to you. My name is Terry Balando, I am an expert archaeologist. I am employed by Varrock Museum to oversee all finds at this site. Anything you find must be reported to me.")
                .playerl(FacialExpression.FRIENDLY, "Oh, okay. If I find anything of interest I will bring it here.")
                .npcl(FacialExpression.FRIENDLY, "Can I help you at all?")
                .options().let { optionBuilder ->
                    optionBuilder.option_playerl("I have something I need checking out.")
                            .npcl(FacialExpression.FRIENDLY, "Okay, give it to me and I'll have a look for you.")
                            .end()
                    optionBuilder.option_playerl("No thanks.")
                            .npcl("Good, let me know if you find anything unusual.")
                            .end()
                    optionBuilder.option_playerl("Can you tell me anything about the digsite?")
                            .npcl("Yes, indeed! I am studying the lives of the settlers. During the end of the Third Age, there used to be a great city at the site. Its inhabitants were humans, supporters of the god Saradomin. It's not recorded what happened to the community here. I suspect nobody has lived here for over a millennium!")
                            .end()
                    // I lost the letter you gave me.

                    optionBuilder.option_playerl("Can you tell me more about the tools an archaeologist uses?")
                            .npcl(FacialExpression.FRIENDLY, "Of course! Let's see now... Trowels are vital for fine digging work, so you can be careful to not damage or disturb any artefacts. Rock picks are for splitting rocks or scraping away soil.")
                            .playerl(FacialExpression.FRIENDLY, "What about specimen jars and brushes?")
                            .npcl(FacialExpression.FRIENDLY, "Those are essential for carefully cleaning and storing smaller samples.")
                            .playerl(FacialExpression.FRIENDLY, "Where can I get any of these things?")
                            .npcl(FacialExpression.FRIENDLY, "Well, we've come into a bit more funding of late, so there should be a stock of each of them in the Exam Centre's tools cupboard. We also hand out relevant tools as students complete each level of their Earth Sciences exams.")
                            .playerl(FacialExpression.FRIENDLY, "Ah, okay, thanks.")
                            .end()
                }
    }
}