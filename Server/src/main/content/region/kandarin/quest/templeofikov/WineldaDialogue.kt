package content.region.kandarin.quest.templeofikov

import core.api.*
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs

@Initializable
class WineldaDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player): DialoguePlugin {
        return WineldaDialogue(player)
    }
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, WineldaDialogueFile(), npc)
        return false
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.WINELDA_276)
    }
}

class WineldaDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        b.onQuestStages(TempleOfIkov.questName, 5,6,7,100)
                .playerl(FacialExpression.FRIENDLY, "Hi again. Could you do the honours again please?")
                .npcl(FacialExpression.FRIENDLY, "Certainly! We helps those that helps poor Winelda!")
                .endWith { _, player ->
                    // There's a cutscene, but I'm lazy man.
                    teleport(player, Location(2664, 9876, 0))
                }

        b.onPredicate { player -> getAttribute(player, TempleOfIkov.attributeTalkedToWinelda, false) }
                .npcl("Have you got roots for poor Winelda?")
                .branch { player ->
                    if (inInventory(player, Items.LIMPWURT_ROOT_225)) {
                        if (amountInInventory(player, Items.LIMPWURT_ROOT_225) >= 20) {
                            return@branch 2
                        }
                        return@branch 1
                    }
                    return@branch 0
                }.let{ branch ->
                    branch.onValue(2)
                            .playerl(FacialExpression.FRIENDLY, "Yes, I've got them.")
                            .betweenStage { _, player, _, _ ->
                                removeItem(player, Item(Items.LIMPWURT_ROOT_225, 20))
                            }
                            .iteml(Items.LIMPWURT_ROOT_225, "You give Winelda the limpwurt roots.")
                            .npcl(FacialExpression.FRIENDLY, "Good! Good! My potion is nearly ready! Bubble, bubble, toil and trouble!")
                            .npcl(FacialExpression.FRIENDLY, "Now we shows them ours magic! Hold on tight!")
                            .endWith { _, player ->
                                if(getQuestStage(player, TempleOfIkov.questName) == 4) {
                                    setQuestStage(player, TempleOfIkov.questName, 5)
                                }
                                // There's a cutscene, but I'm lazy man.
                                teleport(player, Location(2664, 9876, 0))
                            }
                    branch.onValue(1)
                            .playerl(FacialExpression.FRIENDLY, "I've got some limpwurt roots!")
                            .npcl(FacialExpression.FRIENDLY, "We needs 20 rooteses!")
                            .end()
                    branch.onValue(0)
                            .playerl(FacialExpression.FRIENDLY, "How many did you need again?")
                            .npcl(FacialExpression.FRIENDLY, "We needs 20 Limpwurt roots for pot.")
                            .end()
                }

        b.onPredicate { _ -> true }
                .npcl(FacialExpression.FRIENDLY, "Hehe! We see you're in a pickle!")
                .npcl("Wants to be getting over the nasty lava do we?")
                .options()
                .let { optionBuilder ->
                    optionBuilder.option_playerl("Nah, not bothered!")
                            .npcl(FacialExpression.FRIENDLY, "Hehe! Ye'll come back! They always come back!")
                            .end()
                    optionBuilder.option_playerl("Yes we do!")
                            .npcl(FacialExpression.FRIENDLY, "Mocking us are we? Clever one aren't we?")
                            .npcl(FacialExpression.FRIENDLY, "I'm knowing some magic trickesses! I could get over easy as that!")
                            .npcl(FacialExpression.FRIENDLY, "Don't tell them! They always come! They pester poor Winelda!")
                            .playerl(FacialExpression.FRIENDLY, "If you're such a great witch, get me over!")
                            .npcl(FacialExpression.FRIENDLY, "See! They pester Winelda!")
                            .playerl(FacialExpression.FRIENDLY, "I can do something for you!")
                            .npcl(FacialExpression.FRIENDLY, "Good! Don't pester! Help!")
                            .npcl(FacialExpression.FRIENDLY, "Get Winelda 20 limpwurt roots for my pot.")
                            .npcl(FacialExpression.FRIENDLY, "Then we shows them some magic!")
                            .endWith { _, player ->
                                setAttribute(player, TempleOfIkov.attributeTalkedToWinelda, true)
                            }
                    optionBuilder.option_playerl("Yes I do!")
                            .npcl(FacialExpression.FRIENDLY, "I'm knowing some magic trickesses! I could get over easy as that!")
                            .npcl(FacialExpression.FRIENDLY, "Don't tell them! They always come! They pester poor Winelda!")
                            .playerl(FacialExpression.FRIENDLY, "If you're such a great witch, get me over!")
                            .npcl(FacialExpression.FRIENDLY, "See! They pester Winelda!")
                            .playerl(FacialExpression.FRIENDLY, "I can do something for you!")
                            .npcl(FacialExpression.FRIENDLY, "Good! Don't pester! Help!")
                            .npcl(FacialExpression.FRIENDLY, "Get Winelda 20 limpwurt roots for my pot.")
                            .npcl(FacialExpression.FRIENDLY, "Then we shows them some magic!")
                            .endWith { _, player ->
                                setAttribute(player, TempleOfIkov.attributeTalkedToWinelda, true)
                            }
                }
    }
}