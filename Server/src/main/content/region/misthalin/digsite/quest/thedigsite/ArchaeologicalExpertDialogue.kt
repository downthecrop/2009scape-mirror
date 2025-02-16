package content.region.misthalin.digsite.quest.thedigsite

import content.region.desert.quest.deserttreasure.DesertTreasure
import core.api.*
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import org.rs09.consts.NPCs
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.Items


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

        b.onQuestStages(DesertTreasure.questName, 4,5,6,7,8,9,10,11,12,13,14,15,100)
                .npc("Hello again.", "Was that translation any use to Asgarnia?")
                .playerl("I think it was, thanks!")
                .end()

        b.onQuestStages(DesertTreasure.questName, 3)
                .branch { player ->
                    return@branch if (inInventory(player, Items.TRANSLATION_4655)) { 1 } else { 0 }
                }.let { branch ->
                    branch.onValue(1)
                            .npc("Hello again.", "Was that translation any use to Asgarnia?")
                            .playerl(FacialExpression.SUSPICIOUS, "I, uh, kind of didn't take it to him yet...")
                            .npc(FacialExpression.THINKING, "Whyever not?", "You're the strangest delivery @g[boy,girl] I've ever met!")
                            .end()
                    branch.onValue(0)
                            .playerl("I lost that translation you gave me...")
                            .npcl("Oh, no matter, I mostly remember what it said anyway! Here you go!")
                            .endWith { _, player ->
                                addItemOrDrop(player, Items.TRANSLATION_4655)
                            }
                }
        b.onQuestStages(DesertTreasure.questName, 2)
                .betweenStage { df, player, _, _ ->
                    addItemOrDrop(player, Items.TRANSLATION_4655)
                }
                .npcl("There you go, that book contains the sum of my translating ability. If you would be so kind as to take that back to Asgarnia, I think it will reassure him that he is on the")
                .npcl("right track for a find of great archaeological importance!")
                .playerl("Wow! You write really quickly don't you?")
                .npcl("What can I say? It's a skill I picked up through my many years of taking field notes!")
                .endWith { _, player ->
                    if(getQuestStage(player, DesertTreasure.questName) == 2) {
                        setQuestStage(player, DesertTreasure.questName, 3)
                    }
                }

        b.onQuestStages(DesertTreasure.questName, 1)
                .playerl("Hello, are you Terry Balando?")
                .npcl("That's right, who wants to know...?")
                .npcl("Ah yes, I recognise you! You're the fellow who found that strange artefact about Zaros for the museum, aren't you? What can I do for you now?")
                .playerl("That's right. I was in the desert down by the Bedabin Camp, and I found an archaeologist who asked me to deliver this to you.")
                .npcl("You spoke to the legendary Asgarnia Smith??? Quickly, let me see what he had to give you! He is always at the forefront of archaeological breakthroughs!")
                .betweenStage { df, player, _, _ ->
                    removeItem(player, Items.ETCHINGS_4654) // remove if exists
                }
                .playerl("So what does the inscription say? Anything interesting?")
                .npcl("This... this is fascinating! These cuneiforms seem to predate even the settlement we are excavating here... Yes, yes, this is most interesting indeed!")
                .playerl("Can you translate it for me?")
                .npc("Well, I am not familiar with this particular language, but", "the similarities inherent in the pictographs seem to show", "a prevalent trend towards a syllabary consistent with", "the phonemes we have discovered in this excavation!")
                .playerl("Um... So, can you translate it for me or not?")
                .npcl("Well, unfortunately this is the only example of this particular language I have ever seen, but I might be able to make a rough translation, of sorts...")
                .npcl("It might be slightly obscure on the finer details, but it should be good enough to understand the rough meaning of what was originally written. Please, just wait a moment, I will write up what I can")
                .npcl("translate into a journal for you. Then you can take it back to Asgarnia, I think he will be extremely interested in the translation!")
                .endWith { _, player ->
                    if(getQuestStage(player, DesertTreasure.questName) == 1) {
                        setQuestStage(player, DesertTreasure.questName, 2)
                    }
                }

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