package content.region.asgarnia.burthorpe.dialogue

import core.api.openNpcShop
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.NPCs

@Initializable
class SamDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int) : Boolean {
        when (stage) {
            START_DIALOGUE -> npcl(FacialExpression.FRIENDLY, "Welcome to the Burthorpe Games Rooms!").also { stage++ }
            1 -> showTopics(
                Topic(FacialExpression.FRIENDLY, "How do I play board games?", 10),
                Topic(FacialExpression.FRIENDLY, "What games can I play?", 30),
                Topic(FacialExpression.FRIENDLY, "Can I buy a drink please?", 20),
                Topic(FacialExpression.FRIENDLY, "Thanks!", END_DIALOGUE),
            )
            10 -> npcl(FacialExpression.FRIENDLY, "You can challenge someone to a game anywhere in the games rooms by using the right click option. Choose the type of game and then choose the options you want such as time per move. If you want to play a particular").also { stage++ }
            11 -> npcl(FacialExpression.FRIENDLY, "game there are challenge rooms dedicated to that game. In the challenge rooms you can see other players ranks by right clicking them, their skill will be displayed instead of their combat level. Once you have enough").also { stage++ }
            12 -> npcl(FacialExpression.FRIENDLY, "experience you will be able to use the challenge rooms on the first floor! If your opponent accepts the challenge you will be taken to one of the tables in the main room where you will play your game of choice.").also { stage++ }
            13 -> npcl(FacialExpression.FRIENDLY, "Once you have finished your game you will go back to the challenge room where you can challenge again!").also { stage = 1 }

            20 -> npcl(FacialExpression.FRIENDLY, "Certainly ${if (player.isMale) "sir" else "miss"}, our speciality is Asgarnian Ale, we also serve Wizard's Mind Bomb and Dwarven Stout.").also { stage++ }
            21 -> openNpcShop(player, npc.id).also {
                end()
                stage = END_DIALOGUE
            }

            30 -> npcl(FacialExpression.FRIENDLY, "Currently we offer Draughts, Runelink, Runesquares, and Runeversi.").also { stage++ }
            31 -> showTopics(
                Topic(FacialExpression.FRIENDLY, "Draughts?", 35),
                Topic(FacialExpression.FRIENDLY, "Runelink?", 36),
                Topic(FacialExpression.FRIENDLY, "Runesquares?", 37),
                Topic(FacialExpression.FRIENDLY, "Runeversi?", 38),
            )
            35 -> npcl(FacialExpression.FRIENDLY, "Draughts uses standard rules, apart from: a draw is declared if no piece has been taken or promoted for forty moves. To play Draughts go to the challenge room in the SW corner.").also { stage = 1 }
            36 -> npcl(FacialExpression.FRIENDLY, "Yup, you have to get four runes in row. The challenge room for Runelink is in the SE corner.").also { stage = 1 }
            37 -> npcl(FacialExpression.FRIENDLY, "Yes, you take it in turns to add a line with the goal of making squares. Everytime you make a square you take another turn. The challenge room for Runesquares is in the SW corner.").also { stage = 1 }
            38 -> npcl(FacialExpression.FRIENDLY, "Yep, the aim is to have more of your runes on the board than your opponent. You can take your opponents pieces by trapping them between your own. The challenge room for Runeversi is in the SE corner.").also { stage = 1 }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return SamDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SAM_1357)
    }
}