package content.region.asgarnia.burthorpe.quest.deathplateau

import core.api.*
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import org.rs09.consts.Components

/**
 * @author bushtail
 * Thanks for your work on this quest Phil, it's appreciated.
 * @author ovenbread
 */

class DiceGameInterfaceListener : InterfaceListener {
    companion object {
        val GOLDSTACKS = intArrayOf(995, 996, 997, 998, 999, 1000, 1001, 1002, 1003, 1004)
        val GOLDTHRESHOLDS = intArrayOf(1, 2, 3, 4, 5, 25, 100, 250, 1000, 10000)

        const val ATTRIBUTE_HAROLD_DICE_1 = "deathplateau:harold1"
        const val ATTRIBUTE_HAROLD_DICE_2 = "deathplateau:harold2"
        const val ATTRIBUTE_PLAYER_DICE_1 = "deathplateau:player1"
        const val ATTRIBUTE_PLAYER_DICE_2 = "deathplateau:player2"
        const val ATTRIBUTE_WIN_STATE = "deathplateau:winstate"
    }

    override fun defineInterfaceListeners() {
        onOpen(Components.DEATH_DICE_99) { player, _ ->
            var goldID = 0
            var wager = player.getAttribute<Int>("deathplateau:wager", 50)
            player.setAttribute(ATTRIBUTE_HAROLD_DICE_1, 0)
            player.setAttribute(ATTRIBUTE_HAROLD_DICE_2, 0)
            player.setAttribute(ATTRIBUTE_PLAYER_DICE_1, 0)
            player.setAttribute(ATTRIBUTE_PLAYER_DICE_2, 0)
            player.setAttribute(ATTRIBUTE_WIN_STATE, false)
            goldID = when(wager){
                1 -> GOLDSTACKS[0]
                2 -> GOLDSTACKS[1]
                3 -> GOLDSTACKS[2]
                4 -> GOLDSTACKS[3]
                5 -> GOLDSTACKS[4]
                in 25..99 -> GOLDSTACKS[5]
                in 100..249 -> GOLDSTACKS[6]
                in 250..999 -> GOLDSTACKS[7]
                in 1000..9999 -> GOLDSTACKS[8]
                else -> GOLDSTACKS[9]
            }
            sendItemOnInterface(player, Components.DEATH_DICE_99, 7, goldID, wager)
            sendItemOnInterface(player, Components.DEATH_DICE_99, 8, goldID, wager)
            setComponentVisibility(player, Components.DEATH_DICE_99, 9, true)
            setInterfaceText(player, "Harold rolls...", Components.DEATH_DICE_99, 13)
            preRoll(player)
            setInterfaceText(player, player.username, Components.DEATH_DICE_99,6)
            (1..4).forEach { sendAnimationOnInterface(player, 1149, Components.DEATH_DICE_99, it) }
            submitRollPulse(player, true)
            return@onOpen true
        }

        on(Components.DEATH_DICE_99){ player, _, _, buttonID, _, _ ->
            when(buttonID) {
                10 -> submitRollPulse(player, false)
                12 -> {
                    closeInterface(player)
                    val callback: (() -> Unit)? =
                        getAttribute(player, "deathplateau:dicegameclose", null)
                    callback?.invoke()
                }
            }
            return@on true
        }

        onClose(Components.DEATH_DICE_99){ player, _ ->
            setComponentVisibility(player, Components.DEATH_DICE_99, 9, true)
            setComponentVisibility(player, Components.DEATH_DICE_99, 11, true)
            return@onClose true
        }
    }

    private fun onClose(deathDice99: Int, any: Any) {

    }

    private fun preRoll(player: Player, triesRemaining: Int = 5) {
        val h1 = (1..5).random()
        val h2 = (1..5).random()
        val p1 = (1..5).random()
        val p2 = (1..5).random()
        if(h1 + h2 == p1 + p2 && triesRemaining > 0) {
            preRoll(player, triesRemaining - 1)
        } else {
            player.setAttribute(ATTRIBUTE_HAROLD_DICE_1, h1)
            player.setAttribute(ATTRIBUTE_HAROLD_DICE_2, h2)
            player.setAttribute(ATTRIBUTE_PLAYER_DICE_1, p1)
            player.setAttribute(ATTRIBUTE_PLAYER_DICE_2, p2)
            player.setAttribute(ATTRIBUTE_WIN_STATE, h1 + h2 < p1 + p2)
        }
    }

    private fun submitRollPulse(player: Player, isNPCTurn: Boolean) {
        val roll1: Int
        val roll2: Int

        if (isNPCTurn) {
            roll1 = getAttribute<Int?>(player, ATTRIBUTE_HAROLD_DICE_1, null) ?: return
            roll2 = getAttribute<Int?>(player, ATTRIBUTE_HAROLD_DICE_2, null) ?: return
        } else {
            roll1 = getAttribute<Int?>(player, ATTRIBUTE_PLAYER_DICE_1, null) ?: return
            roll2 = getAttribute<Int?>(player, ATTRIBUTE_PLAYER_DICE_2, null) ?: return
        }

        val component1 = if (isNPCTurn) 2 else 3
        val component2 = if (isNPCTurn) 1 else 4

        submitWorldPulse(
            InterfaceUpdatePulse(
                player, isNPCTurn,
                component1, component2,
                roll1, roll2
            )
        )
    }

    private class InterfaceUpdatePulse(val player: Player, val isNPCTurn: Boolean, val component1: Int, val component2: Int, val roll1: Int, val roll2: Int) : Pulse() {
        val DICEANIM = intArrayOf(42069, 1150, 1151, 1152, 1153, 1154, 1155)
        var counter = 0
        override fun pulse(): Boolean {
            when (counter++) {
                1 -> {
                    setComponentVisibility(player, Components.DEATH_DICE_99, 9, true)
                    if (isNPCTurn) {
                        setInterfaceText(player, "Harold rolls...", Components.DEATH_DICE_99, 13)
                    } else {
                        setInterfaceText(player, "Your roll...", Components.DEATH_DICE_99, 13)
                    }
                    sendAnimationOnInterface(player, DICEANIM[roll1], Components.DEATH_DICE_99, component1)
                }
                4 -> sendAnimationOnInterface(player, DICEANIM[roll2], Components.DEATH_DICE_99, component2)
                7 -> {
                    if (isNPCTurn) {
                        setComponentVisibility(player, Components.DEATH_DICE_99, 9, false)
                    } else {
                        if (player.getAttribute<Boolean>("deathplateau:winstate")) {
                            setInterfaceText(player,"You win!", Components.DEATH_DICE_99, 13)
                        } else {
                            setInterfaceText(player,"You lose!", Components.DEATH_DICE_99, 13)
                        }
                        setComponentVisibility(player, Components.DEATH_DICE_99, 11, false)
                        setComponentVisibility(player, Components.DEATH_DICE_99, 9, true)
                    }
                    return true
                }
            }
            return false
        }
    }
}
