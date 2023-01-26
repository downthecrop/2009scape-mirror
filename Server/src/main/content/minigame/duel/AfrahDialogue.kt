package content.minigame.duel

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable

/**
 * @author qmqz
 */

@Initializable
class AfrahDialogue : core.game.dialogue.DialoguePlugin {

    private val conversations = arrayOf (0, 4, 10, 11, 15, 17, 20, 22, 23, 24, 29, 32)

    override fun open(vararg args: Any): Boolean {
        player(core.game.dialogue.FacialExpression.ASKING, "Hi!")
        stage = conversations.random()
        npc = args[0] as NPC
        return true
    }

    constructor() {}
    constructor(player: Player?) : super(player) {}

    override fun getIds(): IntArray {
        return intArrayOf(968)
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                npc(core.game.dialogue.FacialExpression.ASKING, "Ooh. This is exciting!")
                stage++
            }
            1 -> {
                player(core.game.dialogue.FacialExpression.ASKING, "Yup!")
                stage = 99
            }
            2 -> {
                npc(core.game.dialogue.FacialExpression.ASKING, "I wouldn't want to be the poor guy that has to", "clean up after the duels.")
                stage++
            }
            3 -> {
                player(core.game.dialogue.FacialExpression.ASKING, "Me neither.")
                stage = 99
            }
            4 -> {
                npc(core.game.dialogue.FacialExpression.ASKING, "My son just won his first duel!")
                stage++
            }
            5 -> {
                player(core.game.dialogue.FacialExpression.ASKING, "Congratulations!")
                stage++
            }
            6 -> {
                npc(core.game.dialogue.FacialExpression.ASKING, "He ripped his opponent in half!")
                stage++
            }
            7 -> {
                player(core.game.dialogue.FacialExpression.ASKING, "That's gotta hurt!")
                stage++
            }
            8 -> {
                npc(core.game.dialogue.FacialExpression.ASKING, "He's only 10 as well!")
                stage++
            }
            9 -> {
                player(core.game.dialogue.FacialExpression.ASKING, "You gotta start 'em young!")
                stage = 99
            }
            10 -> {
                npc(core.game.dialogue.FacialExpression.ASKING, "Hmph.")
                stage = 99
            }
            11 -> {
                npc(core.game.dialogue.FacialExpression.ASKING, "My favourite fighter is Mubariz!")
                stage++
            }
            12 -> {
                player(core.game.dialogue.FacialExpression.ASKING, "The guy at the information kiosk?")
                stage++
            }
            13 -> {
                npc(core.game.dialogue.FacialExpression.ASKING, "Yeah! He rocks!")
                stage++
            }
            14 -> {
                player(core.game.dialogue.FacialExpression.ASKING, "Takes all sorts, I guess.")
                stage = 99
            }
            15 -> {
                npc(core.game.dialogue.FacialExpression.ASKING, "Hi! I'm here to watch the duels!")
                stage++
            }
            16 -> {
                player(core.game.dialogue.FacialExpression.ASKING, "Me too!")
                stage = 99
            }
            17 -> {
                npc(core.game.dialogue.FacialExpression.ASKING, "Did you know they think this place dates","back to the second age?!")
                stage++
            }
            18 -> {
                player(core.game.dialogue.FacialExpression.ASKING, "Really?")
                stage++
            }
            19 -> {
                npc(core.game.dialogue.FacialExpression.ASKING, "Yeah. The guy at the information kiosk was telling me.")
                stage = 99
            }
            20 -> {
                npc(core.game.dialogue.FacialExpression.ANGRY, "Can't you see I'm watching the duels?")
                stage++
            }
            21 -> {
                player(core.game.dialogue.FacialExpression.SAD, "I'm sorry!")
                stage = 99
            }
            22 -> {
                npc(core.game.dialogue.FacialExpression.ASKING, "Well. This beats doing the shopping!")
                stage = 99
            }
            23 -> {
                npc(core.game.dialogue.FacialExpression.ASKING, "Hi!")
                stage = 99
            }
            24 -> {
                npc(core.game.dialogue.FacialExpression.ASKING, "Knock knock!")
                stage++
            }
            25 -> {
                player(core.game.dialogue.FacialExpression.ASKING, "Who's there?")
                stage++
            }
            26 -> {
                npc(core.game.dialogue.FacialExpression.ASKING, "Boo!")
                stage++
            }
            27 -> {
                player(core.game.dialogue.FacialExpression.ASKING, "Boo who?")
                stage++
            }
            28 -> {
                npc(core.game.dialogue.FacialExpression.LAUGH, "Don't cry, it's just me!")
                stage = 99
            }
            29 -> {
                npc(core.game.dialogue.FacialExpression.ASKING, "Why did the skeleton burp?")
                stage++
            }
            30 -> {
                player(core.game.dialogue.FacialExpression.ASKING, "I don't know?")
                stage++
            }
            31 -> {
                npc(core.game.dialogue.FacialExpression.ASKING, "'Cause it didn't have the guts to fart!")
                stage = 99
            }
            32 -> {
                npc(core.game.dialogue.FacialExpression.ASKING, "Waaaaassssssuuuuupp?!.")
                stage = 99
            }
            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player): core.game.dialogue.DialoguePlugin {
        return AfrahDialogue(player)
    }
}