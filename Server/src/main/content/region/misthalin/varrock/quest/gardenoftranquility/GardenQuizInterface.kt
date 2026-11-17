package content.region.misthalin.varrock.quest.gardenoftranquility

import core.api.openDialogue
import core.api.openInterface
import core.api.setInterfaceText
import core.game.interaction.InterfaceListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import org.rs09.consts.Components
import org.rs09.consts.NPCs

class GardenQuizInterface : InterfaceListener {
    companion object {
        const val WISE_OLD_MAN_QUIZ_INTERFACE = Components.GARDEN_QUIZ_133
        const val OPTION_A_CHILD = 0
        const val OPTION_B_CHILD = 1
        const val OPTION_C_CHILD = 2
        const val QUESTION = 6
        const val QUESTIONNUMBER = 7
        var CORRECT_ANSWER_COUNT = 0

        val QUESTION_VALUES = arrayOf(
            "Two goblins are fighting over the colour of their armour. What do you do?",
            "A Drunken dwarf offers you a kebab. What do you do?",
            "Are you enjoying your experience of Farming?",
            "A dwarf asks you to put on a silly golden helmet so that you can be fired from a cannon into a wall. What do you do?",
            "You meet Pkmaster0036 in the Wilderness. He asks you who is the greatest player killer in the world. How do you reply?",
            "Queen Ellamaria orders you to go on a foolish quest without any offer of reward. What do you say?",
            "A Draynor bank guard asks you if you've seen any suspicious characters lately, because the bank has been robbed. What do you say?"
        )
        val OPTION_A_VALUES = arrayOf(
            "Leave them to it. Armour's not going to help - any noob can take them.",
            "Take his generous gift even though you have no need for it.",
            "Yes",
            "Put the helmet on the dwarf and fire him into the wall.",
            "You of course Pkmaster0036, no one could ever challenge your greatness!",
            "Stop ordering me around and get back behind the bar where you belong!",
            "No, especially not that wise old man, who doesn't look at all suspicious."
        )
        val OPTION_B_VALUES = arrayOf(
            "Show them a range of colours so that they can come to a compromise.",
            "Stand there until he gets annoyed and attacks you.",
            "It's absolutely, unquestionably the most interesting thing I've ever done!",
            "Refuse this crazy request blankly.",
            "Cower in fear and quickly cast teleport to Varrock",
            "I'll do whatever you ask - I just love the monarchy!",
            "Yes, that wise old man over there looks incredibly dodgy."
        )
        val OPTION_C_VALUES = arrayOf(
            "Kill them both and steal their bronze spears.",
            "Run away leaving the lonely dwarf on his own.",
            "Definitely.",
            "Put on the silly helmet and jump into the cannon.",
            "I am, prepare to meet your doom Pkmaster0036!",
            "Ask me nicely and I might consider it.",
            "If I do I'll let you know."
        )

        val CORRECT_ANSWERS = arrayOf(
            1,
            0,
            1,
            2,
            2,
            2,
            0
        )
        var QUESTION_NUMBER_VALUE = 1

        fun updateQuiz(player: Player) {
            setInterfaceText(player, OPTION_A_VALUES[QUESTION_NUMBER_VALUE - 1], WISE_OLD_MAN_QUIZ_INTERFACE, OPTION_A_CHILD)
            setInterfaceText(player, OPTION_B_VALUES[QUESTION_NUMBER_VALUE - 1], WISE_OLD_MAN_QUIZ_INTERFACE, OPTION_B_CHILD)
            setInterfaceText(player, OPTION_C_VALUES[QUESTION_NUMBER_VALUE - 1], WISE_OLD_MAN_QUIZ_INTERFACE, OPTION_C_CHILD)
            setInterfaceText(player, QUESTION_VALUES[QUESTION_NUMBER_VALUE - 1], WISE_OLD_MAN_QUIZ_INTERFACE, QUESTION)
            setInterfaceText(player, "${QUESTION_NUMBER_VALUE}" + "/7:", WISE_OLD_MAN_QUIZ_INTERFACE, QUESTIONNUMBER)
        }
    }

    override fun defineInterfaceListeners() {
        onOpen(WISE_OLD_MAN_QUIZ_INTERFACE) { player, _ ->
            updateQuiz(player)
            return@onOpen true
        }

        on(WISE_OLD_MAN_QUIZ_INTERFACE) { player, _, _, buttonID, _, _ ->
            if (buttonID == CORRECT_ANSWERS[QUESTION_NUMBER_VALUE - 1]) {
                CORRECT_ANSWER_COUNT++
            }
            if (QUESTION_NUMBER_VALUE == 7) {
                openDialogue(player, GardenQuizDialogueFile(CORRECT_ANSWER_COUNT >= 4), NPC(NPCs.WISE_OLD_MAN_2253))
            } else {
                QUESTION_NUMBER_VALUE++
                openInterface(player, WISE_OLD_MAN_QUIZ_INTERFACE)
            }
            return@on true
        }
    }
}

