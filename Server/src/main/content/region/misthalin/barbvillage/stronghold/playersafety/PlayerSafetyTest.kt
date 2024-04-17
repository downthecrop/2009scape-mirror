package content.region.misthalin.barbvillage.stronghold.playersafety

import core.api.*
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import core.plugin.Initializable


// /**
//  * Handles the player safety test.
//  * Java version
//  * @author Tyler Telis
//  * @author Vexia
//  * Kotlin
//  * @author gregf36665
//  */
@Initializable
class PlayerSafetyTest : InterfaceListener {

    companion object {
        private val testQuestions = listOf(
            TestQuestion(697, 26, mapOf(4 to 37, 3 to 40, 5 to 43),  4),
            TestQuestion(699, 20, mapOf(4 to 31, 3 to 34), 4),
            TestQuestion(707, 20, mapOf(3 to 31, 4 to 35, 5 to 35), 3),
            TestQuestion(710, 20, mapOf(9 to 31, 10 to 34), 9),
            TestQuestion(704, 26, mapOf(10 to 37, 12 to 43, 11 to 40), 10),
            // todo figure out why these 2 are not working
            // TestQuestion(694, 20, mapOf(12 to 31, 13 to 34), 13),
            // TestQuestion(708, 26, mapOf(12 to 31, 13 to 34), 12),
            TestQuestion(696, 20, mapOf(4 to 31, 3 to 34, 5 to 34), 4)


            )
        private var testQuestionNumber = 0
    }

    /**
     * Define a new Test Question
     * interfaceId: the base ID for the test question
     * baseChild: the ID of the Component child
     * answers: A map of button press: child to display
     * correctOption: The button number for the correct option
     */
    class TestQuestion(val interfaceId: Int, val baseChild : Int, val answers : Map<Int, Int>, val correctOption: Int){

        fun showAnswer(player: Player, button : Int){
            setComponentVisibility(player, interfaceId, baseChild, false)
            answers[button]?.let { setComponentVisibility(player, interfaceId, it, false) }
            return
        }
    }

    private fun checkAnswer(player: Player, button: Int){
        val question = testQuestions[testQuestionNumber]
        question.showAnswer(player, button)
        if (button == question.correctOption){
            testQuestionNumber += 1
        }
    }

    private fun completedTest(player: Player) {
        closeInterface(player)
        player.savedData.globalData.testStage = 2
        sendMessage(player,"Well done! You completed the exams.")
        sendDialogue(player, "Congratulations! The test has been completed. " +
                "Hand the paper in to Professor Henry for marking.")
    }

    fun update(player: Player){
        // Close all open interfaces
        closeInterface(player)
        // Open the correct one now
        testQuestions.forEachIndexed(){ index, testQuestion ->
            if (index == testQuestionNumber){
                openInterface(player, testQuestion.interfaceId)
            }
        }
    }

    override fun defineInterfaceListeners() {
        onOpen(697) { _, _ ->
            testQuestionNumber = 0 // Set the testQuestionNumber back to 0 on first launch
            return@onOpen true
        }
        for (question in testQuestions){
            on(question.interfaceId) { player, _, _, buttonID, _, _ ->
                if (buttonID in 0..35) {
                    checkAnswer(player, buttonID)
                }
                else{
                    update(player)
                    if (testQuestionNumber == testQuestions.size) {
                        completedTest(player)
                        return@on true
                    }
                }
                return@on true
            }
        }
    }
}

