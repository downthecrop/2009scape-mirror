package content.region.kandarin.seers.quest.elementalworkshop2

import content.region.kandarin.seers.quest.elementalworkshop2.ElementalWorkshop2.Companion.EW2_JCTBOX1
import content.region.kandarin.seers.quest.elementalworkshop2.ElementalWorkshop2.Companion.EW2_JCTBOX2
import content.region.kandarin.seers.quest.elementalworkshop2.ElementalWorkshop2.Companion.EW2_JCTBOX3
import core.api.*
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import org.rs09.consts.Components
import org.rs09.consts.Sounds

/**
 * Junction box in Elemental Workshop II
 */

class JunctionBoxInterface : InterfaceListener {

    companion object {
        // 1317.cs2 controls the visibility of the pipes based on varbit 4835
        private const val PIPES_LEFT_VARBIT = 4835 // 0 = no pipes, 1 = one pipe, 2 = two pipes, 3 = three pipes

        // 1319.cs2 controls the highlighting of the pipe based on varbit 4834
        private const val PIPE_SELECT_VARBIT = 4834 // 0 = no select, 1 = top left, 2 = top middle, 3 = top right, 4 = bottom left, 5 = bottom middle, 6 = bottom right

        // checks if the three specific pipes required for the puzzle solution are placed
        // the solution checker is called in the lever listeners
        fun isSolutionCorrect(player: Player): Boolean {
            val s1 = getVarbit(player, EW2_JCTBOX1)
            val s2 = getVarbit(player, EW2_JCTBOX2)
            val s3 = getVarbit(player, EW2_JCTBOX3)

            val requiredIndices = intArrayOf(3, 7, 12)
            val currentValues = intArrayOf(s1, s2, s3)

            return requiredIndices.all { required ->
                currentValues.contains(required + 1)
            }
        }

        // the interface children for displaying each placed pipe
        private val pipes = intArrayOf(
            5, // from button1 to button4
            6, // from button2 to button5
            7, // from button3 to button6

            8, // from button4 to button5 NEEDED TO SOLVE
            9, // from button4 to button6
            10,// from button5 to button6
            11,// from button1 to button5
            12,// from button1 to button6 NEEDED TO SOLVE

            13,// from button1 to button2
            14,// from button1 to button3

            15,// from button2 to button4
            16,// from button2 to button6

            17,// from button2 to button3 NEEDED TO SOLVE
            18,// from button3 to button4

            19,// from button3 to button5
        )
    }

    override fun defineInterfaceListeners() {
        onOpen(Components.JUNCTION_BOX_262) { player, _ ->
            val s1 = getVarbit(player, EW2_JCTBOX1)
            val s2 = getVarbit(player, EW2_JCTBOX2)
            val s3 = getVarbit(player, EW2_JCTBOX3)

            // Hide all pipes initially
            pipes.forEach { setComponentVisibility(player, Components.JUNCTION_BOX_262, it, true) }

            // Count how many pipes are already placed to set PIPES_LEFT_VARBIT
            var placedCount = 0
            if (s1 > 0) {
                setComponentVisibility(player, Components.JUNCTION_BOX_262, pipes[s1 - 1], false); placedCount++
            }
            if (s2 > 0) {
                setComponentVisibility(player, Components.JUNCTION_BOX_262, pipes[s2 - 1], false); placedCount++
            }
            if (s3 > 0) {
                setComponentVisibility(player, Components.JUNCTION_BOX_262, pipes[s3 - 1], false); placedCount++
            }

            setVarbit(player, PIPES_LEFT_VARBIT, 3 - placedCount, true)

            // reset any selected
            setVarbit(player, PIPE_SELECT_VARBIT, 0, true)

            return@onOpen true
        }

        /**
         * When a button is pressed, we first check to see if the pipeSelect varbit has been set.
         * If it's zero, this is our first selection. Record it.
         * If it has a value, select the child to display based on the pipeSelect varbit and current button selection.
         * After displaying a child, clear pipeSelect and decrement the pipesLeft varbit.
         * If this is the first selection, record the child in the firstSelect varbit.
         * If this is the second selection, record the child in the secondSelect varbit.
         * If this is the third selection, record the child in the thirdSelect varbit.
         */
        on(Components.JUNCTION_BOX_262) { player, _, _, buttonID, _, _ ->

            // interface buttons
            // I think some of this is also controlled by 1318.cs2 fwiw
            val b1 = 24
            val b2 = 25
            val b3 = 20
            val b4 = 21
            val b5 = 22
            val b6 = 23

            // check if the player is clicking a button that already has a pipe attached. if yes, remove the pipe.
            if (getVarbit(player, PIPE_SELECT_VARBIT) == 0) {
                if (tryDetachPipe(player, buttonID, b1, b2, b3, b4, b5, b6)) {
                    return@on true
                }
            }

            val currentSelection = getVarbit(player, PIPE_SELECT_VARBIT)

            // connecting a new pipe
            if (currentSelection == 0) {
                val selection = when (buttonID) {
                    b1 -> 1; b2 -> 2; b3 -> 3
                    b4 -> 4; b5 -> 5; b6 -> 6
                    else -> 0
                }
                setVarbit(player, PIPE_SELECT_VARBIT, selection, true)
            } else {
                val pipeIndex = getPipeIndex(currentSelection, buttonID, b1, b2, b3, b4, b5, b6)

                if (pipeIndex != -1) {
                    // check if this specific pipe is already placed
                    if (isPipeAlreadyPlaced(player, pipeIndex + 1)) {
                        setVarbit(player, PIPE_SELECT_VARBIT, 0, true)
                        return@on true
                    }

                    // place the pipe
                    val pipeChild = pipes[pipeIndex]
                    setComponentVisibility(player, Components.JUNCTION_BOX_262, pipeChild, false)
                    playAudio(player, Sounds.EW2_PLACE_PIPE_3179)

                    // pipe status is saved in these varbits and recalled on open. they are linked to main quest progression status
                    val saved = when {
                        getVarbit(player, EW2_JCTBOX1) == 0 -> {
                            setVarbit(player, EW2_JCTBOX1, pipeIndex + 1, true); true
                        }

                        getVarbit(player, EW2_JCTBOX2) == 0 -> {
                            setVarbit(player, EW2_JCTBOX2, pipeIndex + 1, true); true
                        }

                        getVarbit(player, EW2_JCTBOX3) == 0 -> {
                            setVarbit(player, EW2_JCTBOX3, pipeIndex + 1, true); true
                        }

                        else -> false
                    }

                    if (saved) {
                        val left = getVarbit(player, PIPES_LEFT_VARBIT)
                        setVarbit(player, PIPES_LEFT_VARBIT, left - 1, true)
                    }
                }
                setVarbit(player, PIPE_SELECT_VARBIT, 0, true)
            }

            // if there are no pipes left, we have placed them all. hammer time!
            if (getVarbit(player, PIPES_LEFT_VARBIT) == 0) {
                lock(player, 3)
                runTask(player, 2) {
                    closeInterface(player)
                    sendPlayerDialogue(player, "I hope I got that right.")
                }
            }
            return@on true
        }
    }

    /**
     * Checks if any active pipe uses the clicked button as an endpoint.
     * If so, it removes that pipe and increments the PIPES_LEFT_VARBIT.
     */
    private fun tryDetachPipe(
        player: Player,
        buttonID: Int,
        b1: Int,
        b2: Int,
        b3: Int,
        b4: Int,
        b5: Int,
        b6: Int
    ): Boolean {
        val saveVarbits = intArrayOf(EW2_JCTBOX1, EW2_JCTBOX2, EW2_JCTBOX3)

        for (v in saveVarbits) {
            val pipeIndexPlusOne = getVarbit(player, v)
            if (pipeIndexPlusOne == 0) continue

            val pipeIndex = pipeIndexPlusOne - 1
            // Check if the clicked button is one of the two ends for this pipe index
            if (isButtonPartOfPipe(pipeIndex, buttonID, b1, b2, b3, b4, b5, b6)) {
                // Hide the pipe on interface
                setComponentVisibility(player, Components.JUNCTION_BOX_262, pipes[pipeIndex], true)
                // Reset the varbit
                setVarbit(player, v, 0, true)
                // Increment pipes left
                val left = getVarbit(player, PIPES_LEFT_VARBIT)
                setVarbit(player, PIPES_LEFT_VARBIT, left + 1, true)
                playAudio(player, Sounds.EW2_REMOVE_PIPE_3181)
                return true
            }
        }
        return false
    }

    // just read the function title
    private fun isPipeAlreadyPlaced(player: Player, pipeID: Int): Boolean {
        return getVarbit(player, EW2_JCTBOX1) == pipeID
                || getVarbit(player, EW2_JCTBOX2) == pipeID
                || getVarbit(player, EW2_JCTBOX3) == pipeID
    }

    /**
     * Maps the start selection (1-6) and the end button ID to the index in the 'pipes' array.
     */
    private fun getPipeIndex(start: Int, endBtn: Int, b1: Int, b2: Int, b3: Int, b4: Int, b5: Int, b6: Int): Int {
        return when (start) {
            1 -> when (endBtn) { // Top Left
                b4 -> 0; b5 -> 6; b6 -> 7; b2 -> 8; b3 -> 9; else -> -1
            }

            2 -> when (endBtn) { // Top Mid
                b5 -> 1; b4 -> 10; b6 -> 11; b1 -> 8; b3 -> 12; else -> -1
            }

            3 -> when (endBtn) { // Top Right
                b6 -> 2; b4 -> 13; b5 -> 14; b1 -> 9; b2 -> 12; else -> -1
            }

            4 -> when (endBtn) { // Bottom Left
                b1 -> 0; b5 -> 3; b6 -> 4; b2 -> 10; b3 -> 13; else -> -1
            }

            5 -> when (endBtn) { // Bottom Mid
                b2 -> 1; b4 -> 3; b6 -> 5; b1 -> 6; b3 -> 14; else -> -1
            }

            6 -> when (endBtn) { // Bottom Right
                b3 -> 2; b4 -> 4; b5 -> 5; b1 -> 7; b2 -> 11; else -> -1
            }

            else -> -1
        }
    }

    private fun isButtonPartOfPipe(
        pipeIndex: Int,
        buttonID: Int,
        b1: Int,
        b2: Int,
        b3: Int,
        b4: Int,
        b5: Int,
        b6: Int
    ): Boolean {
        return when (pipeIndex) {
            0 -> buttonID == b1 || buttonID == b4
            1 -> buttonID == b2 || buttonID == b5
            2 -> buttonID == b3 || buttonID == b6
            3 -> buttonID == b4 || buttonID == b5
            4 -> buttonID == b4 || buttonID == b6
            5 -> buttonID == b5 || buttonID == b6
            6 -> buttonID == b1 || buttonID == b5
            7 -> buttonID == b1 || buttonID == b6
            8 -> buttonID == b1 || buttonID == b2
            9 -> buttonID == b1 || buttonID == b3
            10 -> buttonID == b2 || buttonID == b4
            11 -> buttonID == b2 || buttonID == b6
            12 -> buttonID == b2 || buttonID == b3
            13 -> buttonID == b3 || buttonID == b4
            14 -> buttonID == b3 || buttonID == b5
            else -> false
        }
    }
}
