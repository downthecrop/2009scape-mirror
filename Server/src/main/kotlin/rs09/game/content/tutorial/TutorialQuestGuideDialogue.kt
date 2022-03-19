package rs09.game.content.tutorial

import api.getAttribute
import api.setAttribute
import api.setVarbit
import core.game.component.Component
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.Components
import org.rs09.consts.NPCs

/**
 * Handles the quest guide's dialogue
 * @author Ceikry
 */
@Initializable
class TutorialQuestGuideDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player?): DialoguePlugin {
        return TutorialQuestGuideDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        when(getAttribute(player, "tutorial:stage", 0))
        {
            27 -> Component.setUnclosable(
                player,
                interpreter.sendDialogues(
                    npc,
                    FacialExpression.FRIENDLY,
                    "Ah. Welcome adventurer. I'm here to tell you all about",
                    "quests. Lets start by opening the Quest list."
                )
            )

            28 -> Component.setUnclosable(
                player,
                interpreter.sendDialogues(
                    npc,
                    FacialExpression.FRIENDLY,
                    "Now you have the journal open. I'll tell you a bit about",
                    "it At the moment all the quests are shown in red. Which",
                    "means you have not started them yet."
                )
            )

            else -> return false
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(getAttribute(player, "tutorial:stage", 0))
        {
            27 -> {
                Component.setUnclosable(
                    player,
                    interpreter.sendPlaneMessageWithBlueTitle(
                        "Open the Quest Journal.",
                        "",
                        "Click on the flashing icon next to your inventory.",
                        "",
                        ""
                    )
                )
                setVarbit(player, 1021, 0, 3)
                player.interfaceManager.openTab(Component(Components.QUESTJOURNAL_V2_274))
            }
            28 -> when(stage)
            {
                0 -> Component.setUnclosable(
                    player,
                    interpreter.sendDialogues(
                        npc,
                        FacialExpression.FRIENDLY,
                        "When you start a quest it will change colour to yellow,",
                        "and to green when you've finished. This is so you can",
                        "easily see what's complete, what's started and what's left",
                        "to begin."
                    )
                ).also { stage++ }
                1 -> Component.setUnclosable(
                    player,
                    interpreter.sendDialogues(
                        npc,
                        FacialExpression.FRIENDLY,
                        "The start of quests are easy to find. Look out for the",
                        "star icons on the minimap, just like the one you should",
                        "see marking my house."
                    )
                ).also { stage++ }
                2 -> Component.setUnclosable(
                    player,
                    interpreter.sendDialogues(
                        npc,
                        FacialExpression.FRIENDLY,
                        "There's not a lot more I can tell you about questing.",
                        "You have to experience the thrill of it yourself to fully",
                        "understand. You may find some adevnture in the caves",
                        "under my house."
                    )
                ).also { stage++ }
                3 -> {
                    end()
                    setAttribute(player, "tutorial:stage", 29)
                    TutorialStage.load(player, 29)
                }
            }
        }

        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.QUEST_GUIDE_949)
    }

}