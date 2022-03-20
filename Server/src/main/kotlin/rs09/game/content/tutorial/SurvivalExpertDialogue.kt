package rs09.game.content.tutorial

import api.addItem
import api.inInventory
import api.setAttribute
import core.game.component.Component
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs

/**
 * Handles the survival expert's dialogue
 * @author Ceikry
 */
@Initializable
class SurvivalExpertDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player?): DialoguePlugin {
        return SurvivalExpertDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        val tutStage = player?.getAttribute("tutorial:stage", 0) ?: 0
        when(tutStage)
        {
            4 -> Component.setUnclosable(
                player,
                interpreter.sendDialogues(
                    npc,
                    FacialExpression.FRIENDLY,
                    "Hello there, newcomer. My name is Brynna. My job is",
                    "to teach you a few suvival tips and tricks. First off",
                    "we're going to start with the most basic skill of",
                    "all: making a fire."
                )
            )

            11 -> Component.setUnclosable(
                player,
                interpreter.sendDialogues(
                    npc,
                    FacialExpression.FRIENDLY,
                    "Well done! Next we need to get some food in our",
                    "bellies. We'd need something to cook. There are shrimp",
                    "in the pond there. So let's catch and cook some."
                )
            )

            5, 14, 15 -> {
                if(!inInventory(player, Items.BRONZE_AXE_1351))
                {
                    player.dialogueInterpreter.sendItemMessage(Items.BRONZE_AXE_1351, "The Survival Expert gives you a spare bronze axe.")
                    addItem(player, Items.BRONZE_AXE_1351)
                }
                if(!inInventory(player, Items.TINDERBOX_590))
                {
                    player.dialogueInterpreter.sendItemMessage(Items.TINDERBOX_590, "The Survival Expert gives you a spare tinderbox.")
                    addItem(player, Items.TINDERBOX_590)
                }
                return false
            }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(player?.getAttribute("tutorial:stage", 0))
        {
            4 -> when(stage)
            {
                0 -> {
                    Component.setUnclosable(
                        player,
                        interpreter.sendDoubleItemMessage(
                            Items.TINDERBOX_590,
                            Items.BRONZE_AXE_1351,
                            "The Surivival Guide gives you a <col=08088A>tinderbox</col> and a <col=08088A>bronze axe</col>!"
                        )
                    )
                    addItem(player, Items.TINDERBOX_590)
                    addItem(player, Items.BRONZE_AXE_1351)
                    stage++
                }
                1 -> {
                    end()
                    setAttribute(player, "tutorial:stage", 5)
                    TutorialStage.load(player, 5)
                }
            }

            11 -> when(stage){
                0 -> {
                    Component.setUnclosable(
                        player,
                        interpreter.sendItemMessage(303, "The Survival Guide gives you a <col=08088A>net</col>!")
                    )
                    addItem(player, Items.SMALL_FISHING_NET_303)
                    stage++
                }
                1 -> {
                    end()
                    setAttribute(player, "tutorial:stage", 12)
                    TutorialStage.load(player, 12)
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SURVIVAL_EXPERT_943)
    }

}