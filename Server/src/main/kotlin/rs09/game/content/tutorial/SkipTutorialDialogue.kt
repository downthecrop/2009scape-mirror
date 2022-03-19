package rs09.game.content.tutorial

import api.setAttribute
import api.teleport
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs09.consts.NPCs

/**
 * Handles Skippy's skip tutorial dialogue
 * @author Ceikry
 */
@Initializable
class SkipTutorialDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player?): DialoguePlugin {
        return SkipTutorialDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npcl(FacialExpression.FRIENDLY, "Hey, would you like to skip to the end? Choose wisely! This is the only time you get this choice.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> options("Yes, I'd like to skip the tutorial.", "No thanks.").also { stage++ }
            1 -> when(buttonId)
            {
                1 -> {
                    end()
                    setAttribute(player, "/save:tutorial:stage", 71)
                    TutorialStage.load(player, 71)
                    teleport(player, Location.create(3141, 3089, 0))
                }
                2 -> end()
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SKIPPY_2796)
    }
}