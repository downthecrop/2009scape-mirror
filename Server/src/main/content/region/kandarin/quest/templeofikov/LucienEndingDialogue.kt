package content.region.kandarin.quest.templeofikov

import core.api.*
import core.game.dialogue.*
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs

@Initializable
class LucienEndingDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player): DialoguePlugin {
        return LucienEndingDialogue(player)
    }
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, LucienEndingDialogueFile(), npc)
        return false
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.LUCIEN_272)
    }
}

class LucienEndingDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        b.onQuestStages(TempleOfIkov.questName, 100)
                .endWith { _, player ->
                    sendMessage(player, "You have completed the Temple of Ikov quest.")
                }
        b.onQuestStages(TempleOfIkov.questName, 1,2,3,4,5,6,7)
                .npcl(FacialExpression.FRIENDLY, "Have you got the Staff of Armadyl yet?")
                .branch { player ->
                    return@branch if (inInventory(player, Items.STAFF_OF_ARMADYL_84)) { 1 } else { 0 }
                }.let { branch ->
                    branch.onValue(1)
                            .options()
                            .let { optionBuilder ->
                                optionBuilder.option_playerl("Yes! Here it is.")
                                        .betweenStage { _, player, _, _ ->
                                            removeItem(player, Items.STAFF_OF_ARMADYL_84)
                                        }
                                        .iteml(Items.STAFF_OF_ARMADYL_84, "You give Lucien the Staff of Armadyl.")
                                        .npcl(FacialExpression.FRIENDLY, "Muhahahhahahaha!")
                                        .npcl(FacialExpression.FRIENDLY, "I can feel the power of the staff running through me! I will be more powerful and they shall bow down to me!")
                                        .npcl(FacialExpression.FRIENDLY, "I suppose you want your reward? I shall grant you much power!")
                                        .endWith { _, player ->
                                            if(getQuestStage(player, TempleOfIkov.questName) == 6) {
                                                finishQuest(player, TempleOfIkov.questName)
                                            }
                                        }
                                optionBuilder.option_playerl("No, not yet.")
                                        .end()
                            }
                    branch.onValue(0)
                            .playerl(FacialExpression.FRIENDLY, "No, not yet.")
                            .end()
                }
        b.onPredicate { _ -> true }
                .npcl("Not here. Meet me at the Flying Horse Inn in East Ardougne.")
                .end()
    }
}