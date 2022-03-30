package rs09.game.content.tutorial

import api.*
import core.game.component.Component
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs

/**
 * Handles the cooking tutor's dialogue
 * @author Ceikry
 */
@Initializable
class TutorialMasterChefDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player?): DialoguePlugin {
        return TutorialMasterChefDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        when(getAttribute(player, "tutorial:stage", 0))
        {
            18 -> Component.setUnclosable(
                player,
                interpreter.sendDialogues(
                    npc,
                    FacialExpression.FRIENDLY,
                    "Ahh! Welcome, newcomer. I am the Master Chef, Lev. It",
                    "is here I will teach you how to cook food truly fit for a",
                    "king."
                )
            )

            19,20 -> {
                if(!inInventory(player, Items.BREAD_DOUGH_2307))
                {
                    if(!inInventory(player, Items.BUCKET_OF_WATER_1929))
                    {
                        sendItemDialogue(player, Items.BUCKET_OF_WATER_1929, "The Master Chef gives you another bucket of water.")
                        addItem(player, Items.BUCKET_OF_WATER_1929)
                        TutorialStage.load(player, 19)
                        return false
                    }
                    if(!inInventory(player, Items.POT_OF_FLOUR_1933))
                    {
                        sendItemDialogue(player, Items.POT_OF_FLOUR_1933, "The Master Chef gives you another pot of flour.")
                        addItem(player, Items.POT_OF_FLOUR_1933)
                        TutorialStage.load(player, 19)
                        return false
                    }
                }
                return false
            }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(getAttribute(player, "tutorial:stage", 0))
        {
            18 -> when(stage)
            {
                0 -> Component.setUnclosable(
                    player,
                    interpreter.sendDialogues(
                        player,
                        FacialExpression.HALF_GUILTY,
                        "I already know how to cook. Brynna taught me just",
                        "now."
                    )
                ).also { stage++ }
                1 -> Component.setUnclosable(
                    player,
                    interpreter.sendDialogues(
                        npc,
                        FacialExpression.LAUGH,
                        "Hahahahahaha! You call THAT cooking? Some shrimp",
                        "on an open log fire? Oh, no, no no. I am going to",
                        "teach you the fine art of cooking bread."
                    )
                ).also { stage++ }
                2 -> Component.setUnclosable(
                    player,
                    interpreter.sendDialogues(
                        npc,
                        FacialExpression.FRIENDLY,
                        "And no fine meal is complete without good music, so",
                        "we'll cover that while you're here too."
                    )
                ).also { stage++ }
                3 -> {
                    Component.setUnclosable(
                        player,
                        interpreter.sendDoubleItemMessage(
                            Items.BUCKET_OF_WATER_1929,
                            Items.POT_OF_FLOUR_1933,
                            "The Cooking Guide gives you a <col=08088A>bucket of water<col> and a <col=08088A>pot of flour</col>."
                        )
                    )
                    addItem(player, Items.BUCKET_OF_WATER_1929)
                    addItem(player, Items.POT_OF_FLOUR_1933)
                    stage++
                }
                4 -> {
                    end()
                    setAttribute(player, "tutorial:stage", 19)
                    TutorialStage.load(player, 19)
                }
            }
        }

        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MASTER_CHEF_942)
    }
}