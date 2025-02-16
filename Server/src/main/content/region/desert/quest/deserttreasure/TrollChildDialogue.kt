package content.region.desert.quest.deserttreasure

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
class TrollChildDialogue(player: Player? = null) : DialoguePlugin(player){
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player!!, TrollChildDialogueFile(), npc)
        return false
    }
    override fun newInstance(player: Player?): DialoguePlugin {
        return TrollChildDialogue(player)
    }
    override fun getIds(): IntArray {
        // NPCs.BANDIT_1932 is wrong, should be NPCs.TROLL_CHILD_1932
        // NPCs.TROLL_CHILD_1933, NPCs.TROLL_CHILD_1934 are varbit controlled 1932 instances.
        return intArrayOf(NPCs.BANDIT_1932)
    }
}
class TrollChildDialogueFile : DialogueBuilderFile() {

    companion object {
        fun dialogueBeforeQuestCrying(builder: DialogueBuilder): DialogueBuilder {
            // From https://youtu.be/AJaHuCuxfFg 15:19
            return builder
                    .playerl("Hello there.")
                    .line("The troll child is crying to itself.", "It is ignoring you completely.")
        }
        fun dialogueStillCrying(builder: DialogueBuilder): DialogueBuilder {
            return builder
                    .playerl("Hello there.")
                    .npcl(FacialExpression.OLD_NEARLY_CRYING,"Waaaaaaa!")
                    .line("This troll seems very upset about something.", "Maybe some sweet food would take his mind off things?")
        }
        fun dialogueStoppedCrying(builder: DialogueBuilder): DialogueBuilder {
            return builder
                    .playerl("Hello there.")
                    .npc(FacialExpression.OLD_SAD,"-sniff-","H-hello there.")
                    .playerl("Why so sad, little troll?")
                    .npc(FacialExpression.OLD_NEARLY_CRYING,"It was the bad man!", "He hurt my mommy and daddy!", "He made them all freezey!")
                    .playerl("Bad man...?")
                    .npcl(FacialExpression.OLD_NEARLY_CRYING,"He said it was because they stole his diamond! But they never did! They found it, and didn't know who it belonged to!")
                    .npcl(FacialExpression.OLD_NEARLY_CRYING,"My mommy always told me stealing is wrong, they would never steal from someone!")
                    .npcl(FacialExpression.OLD_NEARLY_CRYING,"Then he did some wavey hand thing and my mommy and daddy got frozified!")
                    .playerl("A diamond you say? Listen, I think I might be able to help your parents, but I need that Diamond in return.")
                    .npcl(FacialExpression.OLD_NEARLY_CRYING,"-sniff- I don't think they really wanted it anyway, they would have given it back to the bad man if he'd asked before freezifying them...")
                    .npcl(FacialExpression.OLD_NEARLY_CRYING,"I give you my promise mister that if you unfreeze my mommy and daddy, you can have the stupid diamond.")
                    .npcl(FacialExpression.OLD_NEARLY_CRYING,"Do we have a deal?")
        }
        fun dialogueYesToHelp(builder: DialogueBuilder): DialogueBuilder {
            return builder
                    .playerl("Absolutely. Don't worry kid, I'll get your parents back to you safe and sound.")
        }
        fun dialogueNoToHelp(builder: DialogueBuilder): DialogueBuilder {
            return builder
                    .playerl("Sorry, I can't make any promises about that, and I don't think I have the time to waste trying to defrost some stupid ice trolls.")
                    .npcl(FacialExpression.OLD_NEARLY_CRYING,"Waaaaaaa!")
        }
        fun dialogueHaveYouFreedThem(builder: DialogueBuilder): DialogueBuilder {
            return builder
                    .npcl(FacialExpression.OLD_SAD,"You didn't free my mommy and daddy yet?")
                    .player("Not yet...")
                    .npc(FacialExpression.OLD_SAD,"Please try harder!", "I love my mommy and daddy!")
        }
        fun dialogueThankYou(builder: DialogueBuilder): DialogueBuilder {
            return builder
                    .npc(FacialExpression.OLD_CALM_TALK1, "Thanks for all of your help!", "I'm surprised you managed to survive the blizzard,", "being a thin skinned fleshy and all!")
                    .player("What can I say?", "I'm a lot tougher than I look.")
        }
        fun dialogueLostDiamond(builder: DialogueBuilder): DialogueBuilder {
            return builder
                    .playerl("I lost that diamond of Ice you gave me...")
                    .npcl(FacialExpression.OLD_CALM_TALK1, "That's okay, it blew back on an icy wind... It's almost like it wants to stay here! Here, take it back, you've earned it.")
        }
    }

    override fun create(b: DialogueBuilder) {
        // Dialogue Logic
        b.onQuestStages(DesertTreasure.questName, 0,1,2,3,4,5,6,7,8)
                .let{dialogueBeforeQuestCrying(it)}
                .end()

        b.onQuestStages(DesertTreasure.questName, 9)
                .branch { player ->
                    return@branch DesertTreasure.getSubStage(player, DesertTreasure.attributeIceStage)
                }.let { branch ->
                    // Branch on sub-stages.
                    branch.onValue(0)
                            .let{dialogueStillCrying(it)}
                            .end()

                    branch.onValue(1)
                            .let{dialogueStoppedCrying(it)}
                            .options().let { optionBuilder ->
                                optionBuilder.option("Yes")
                                        .let{dialogueYesToHelp(it)}
                                        .endWith { _, player ->
                                            if (DesertTreasure.getSubStage(player, DesertTreasure.attributeIceStage) == 1) {
                                                DesertTreasure.setSubStage(player, DesertTreasure.attributeIceStage, 2)
                                            }
                                        }
                                optionBuilder.option("No")
                                        .let{dialogueNoToHelp(it)}
                                        .end()
                            }

                    branch.onValue(2)
                            .let{dialogueHaveYouFreedThem(it)}
                            .end()

                    branch.onValue(3)
                            .let{dialogueHaveYouFreedThem(it)}
                            .end()

                    branch.onValue(4)
                            .let{dialogueHaveYouFreedThem(it)}
                            .end()

                    branch.onValue(100)
                            .branch { player ->
                                return@branch if (!inInventory(player, Items.ICE_DIAMOND_4671)) { 1 } else { 0 }
                            }.let { branch ->
                                branch.onValue(1)
                                    .let{dialogueLostDiamond(it)}
                                    .endWith { _, player ->
                                        addItemOrDrop(player, Items.ICE_DIAMOND_4671)
                                    }
                                branch.onValue(0)
                                        .let{dialogueThankYou(it)}
                                        .end()
                            }
                }
    }
}