package content.region.misthalin.digsite.quest.thedigsite

import core.api.*
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs

@Initializable
class PanningGuideDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, PanningGuideDialogueFile(), npc)
        return true
    }
    override fun newInstance(player: Player): DialoguePlugin {
        return PanningGuideDialogue(player)
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.PANNING_GUIDE_620)
    }
}
class PanningGuideDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        b.onPredicate { getAttribute(player!!, TheDigSite.attributePanningGuideTea, false) }
                .playerl(FacialExpression.FRIENDLY, "Hello, who are you?")
                .npcl(FacialExpression.FRIENDLY, "Hello, I am the panning guide. I'm here to teach you how to pan for gold.")
                .playerl(FacialExpression.FRIENDLY, "Excellent!")
                .npcl(FacialExpression.FRIENDLY, "Let me explain how panning works. First, you need a panning tray. Use the tray in the panning points in the water and then search your tray.")
                .npcl("If you find any gold, take it to the archaeological expert up in the museum storage facility. He will calculate its value for you.")
                .options()
                .let { optionBuilder ->
                    optionBuilder.option_playerl("Can you tell me more about the tools an archaeologist uses?")
                            .npcl(FacialExpression.FRIENDLY, "Of course! Let's see now... Rock picks are for splitting rocks or scraping away soil; you can get one from a cupboard in the Exam Centre.")
                            .playerl(FacialExpression.FRIENDLY, "What about sample jars?")
                            .npcl(FacialExpression.FRIENDLY, "I think you'll find them scattered about pretty much everywhere, but I know you can get one from a cupboard somewhere in the Exam Centre, just like the rock pick!")
                            .playerl(FacialExpression.FRIENDLY, "Okay, what about a specimen brush?")
                            .npcl(FacialExpression.FRIENDLY, "We have a bit of a shortage of those at the moment. You could try borrowing one from a workman on the site... but I don't think they'd give it willingly.")
                            .playerl(FacialExpression.FRIENDLY, "Sounds like I'll need to be sneaky to get one of those, then... Okay - trowel?")
                            .npcl(FacialExpression.FRIENDLY, "Ahh... that you must earn by passing your exams! The examiner holds those.")
                            .playerl(FacialExpression.FRIENDLY, "Anything else?")
                            .npcl(FacialExpression.FRIENDLY, "If you need something identified or are not sure about something, give it to the archaeological expert in the Exam Centre.")
                            .playerl(FacialExpression.FRIENDLY, "Ahh, ok thanks.")
                            .end()
                    optionBuilder.option_playerl("Thank you!")
                            .end()
                }

        b.onPredicate { _ -> true }
                .playerl(FacialExpression.FRIENDLY, "Hello, who are you?")
                .npcl(FacialExpression.FRIENDLY, "Hello, I am the panning guide. I teach students how to pan in these waters. They're not permitted to do so until after they've had training and, of course, they must be invited to pan here too.")
                .playerl(FacialExpression.FRIENDLY, "So, how do I become invited?")
                .npcl(FacialExpression.FRIENDLY, "I'm not supposed to let people pan here unless they have permission. Mind you, I could let you have a go if you're willing to do me a favour...")
                .playerl(FacialExpression.FRIENDLY, "What's that?")
                .npcl(FacialExpression.FRIENDLY, "Well, to be honest, what I would really like is a nice cup of tea!")
                .branch { player -> if (inInventory(player, Items.CUP_OF_TEA_712)) { 1 } else { 0 } }
                .let { branch ->
                    branch.onValue(0)
                            .playerl(FacialExpression.FRIENDLY, "Tea?")
                            .npcl(FacialExpression.FRIENDLY, "Absolutely, I'm parched!")
                            .npcl(FacialExpression.FRIENDLY, "If you could bring me one of those, I would be more than willing to let you pan here. I usually get some from the main campus building, but I'm busy at the moment.")
                            .end()
                    branch.onValue(1)
                            .playerl(FacialExpression.FRIENDLY, "I've some here that you can have.")
                            .npcl(FacialExpression.FRIENDLY, "Ah! Lovely! You can't beat a good cuppa! You're free to pan all you want.")
                            .endWith { _, player ->
                                if(removeItem(player, Items.CUP_OF_TEA_712)) {
                                    setAttribute(player, TheDigSite.attributePanningGuideTea, true)
                                }
                            }
                }
    }
}
class PanningGuideCannotPanDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        b.onPredicate { _ -> true }
                .npcl(FacialExpression.ANNOYED, "Hey! You can't pan yet!")
                .playerl(FacialExpression.THINKING, "Why not?")
                .npcl("We do not allow the uninvited to pan here.")
                .options()
                .let { optionBuilder ->
                    optionBuilder.option_playerl("OK, forget it.")
                            .end()
                    return@let optionBuilder
                }
                .option_playerl("So how do I become invited then?")
                .npcl(FacialExpression.FRIENDLY, "I'm not supposed to let people pan here unless they have permission. Mind you, I could let you have a go if you're willing to do me a favour.")
                .playerl(FacialExpression.FRIENDLY, "What's that?")
                .npcl(FacialExpression.FRIENDLY, "Well, to be honest, what I would really like is a nice cup of tea!")
                .branch { player -> if (inInventory(player, Items.CUP_OF_TEA_712)) { 1 } else { 0 } }
                .let { branch ->
                    branch.onValue(0)
                            .playerl(FacialExpression.FRIENDLY, "Tea?")
                            .npcl(FacialExpression.FRIENDLY, "Absolutely, I'm parched!")
                            .npcl(FacialExpression.FRIENDLY, "If you could bring me one of those, I would be more than willing to let you pan here. I usually get some from the main campus building, but I'm busy at the moment.")
                            .end()
                    branch.onValue(1)
                            .playerl(FacialExpression.FRIENDLY, "I've some here that you can have.")
                            .betweenStage { _, player, _, _ ->
                                if(removeItem(player, Items.CUP_OF_TEA_712)) {
                                    setAttribute(player, TheDigSite.attributePanningGuideTea, true)
                                }
                            }
                            .npcl(FacialExpression.FRIENDLY, "Ah! Lovely! You can't beat a good cuppa! You're free to pan all you want.")
                            .end()
                }

    }
}