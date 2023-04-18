package content.region.karamja.quest.tribaltotem

import core.api.closeInterface
import core.api.sendMessage
import core.api.setInterfaceText
import core.game.world.map.Location
import core.game.interaction.InterfaceListener

class TTDoorCodeInterfaceListener : InterfaceListener {

    override fun defineInterfaceListeners() {
        val LETTERONEBACK = 17
        val LETTERONEFORWARD = 18
        val LETTERTWOBACK = 19
        val LETTERTWOFORWARD = 20
        val LETTERTHREEBACK = 21
        val LETTERTHREEFORWARD = 22
        val LETTERFOURBACK = 23
        val LETTERFOURFORWARD = 24
        val ENTER = 27
        val EXIT = 28
        val DOORLOCKINTERFACE = 369
        val LETTERS = arrayOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z")

        onOpen(DOORLOCKINTERFACE) { player, component ->
            player.setAttribute("tt-letter-one", 0)
            player.setAttribute("tt-letter-two", 0)
            player.setAttribute("tt-letter-three", 0)
            player.setAttribute("tt-letter-four", 0)
            return@onOpen true
        }

        onClose(DOORLOCKINTERFACE) { player, component ->
            player.removeAttribute("tt-letter-one")
            player.removeAttribute("tt-letter-two")
            player.removeAttribute("tt-letter-three")
            player.removeAttribute("tt-letter-four")
            return@onClose true
        }

        on(DOORLOCKINTERFACE) { player, component, opcode, buttonID, slot, itemID ->
            when (buttonID) {
                LETTERONEBACK -> {
                    if (player.getAttribute("tt-letter-one", 0) == 0) {
                        player.setAttribute("tt-letter-one", 25)
                        setInterfaceText(player, LETTERS[player.getAttribute("tt-letter-one", 0)], DOORLOCKINTERFACE, 13)
                    } else {
                        (player.incrementAttribute("tt-letter-one", -1))
                        setInterfaceText(player, LETTERS[player.getAttribute("tt-letter-one", 0)], DOORLOCKINTERFACE, 13)
                    }
                }

                LETTERONEFORWARD -> {
                    if (player.getAttribute("tt-letter-one", 0) == 25) {
                        player.setAttribute("tt-letter-one", 0)
                        setInterfaceText(player, LETTERS[player.getAttribute("tt-letter-one", 0)], DOORLOCKINTERFACE, 13)
                    } else {
                        (player.incrementAttribute("tt-letter-one", 1))
                        setInterfaceText(player, LETTERS[player.getAttribute("tt-letter-one", 0)], DOORLOCKINTERFACE, 13)
                    }
                }

                LETTERTWOBACK -> {
                    if (player.getAttribute("tt-letter-two", 0) == 0) {
                        player.setAttribute("tt-letter-two", 25)
                        setInterfaceText(player, LETTERS[player.getAttribute("tt-letter-two", 0)], DOORLOCKINTERFACE, 14)
                    } else {
                        (player.incrementAttribute("tt-letter-two", -1))
                        setInterfaceText(player, LETTERS[player.getAttribute("tt-letter-two", 0)], DOORLOCKINTERFACE, 14)
                    }
                }

                LETTERTWOFORWARD -> {
                    if (player.getAttribute("tt-letter-two", 0) == 25) {
                        player.setAttribute("tt-letter-two", 0)
                        setInterfaceText(player, LETTERS[player.getAttribute("tt-letter-two", 0)], DOORLOCKINTERFACE, 14)
                    } else {
                        (player.incrementAttribute("tt-letter-two", 1))
                        setInterfaceText(player, LETTERS[player.getAttribute("tt-letter-two", 0)], DOORLOCKINTERFACE, 14)
                    }
                }

                LETTERTHREEBACK -> {
                    if (player.getAttribute("tt-letter-three", 0) == 0) {
                        player.setAttribute("tt-letter-three", 25)
                        setInterfaceText(player, LETTERS[player.getAttribute("tt-letter-three", 0)], DOORLOCKINTERFACE, 15)
                    } else {
                        (player.incrementAttribute("tt-letter-three", -1))
                        setInterfaceText(player, LETTERS[player.getAttribute("tt-letter-three", 0)], DOORLOCKINTERFACE, 15)
                    }
                }

                LETTERTHREEFORWARD -> {
                    if (player.getAttribute("tt-letter-three", 0) == 25) {
                        player.setAttribute("tt-letter-three", 0)
                        setInterfaceText(player, LETTERS[player.getAttribute("tt-letter-three", 0)], DOORLOCKINTERFACE, 15)
                    } else {
                        (player.incrementAttribute("tt-letter-three", 1))
                        setInterfaceText(player, LETTERS[player.getAttribute("tt-letter-three", 0)], DOORLOCKINTERFACE, 15)
                    }
                }

                LETTERFOURBACK -> {
                    if (player.getAttribute("tt-letter-four", 0) == 0) {
                        player.setAttribute("tt-letter-four", 25)
                        setInterfaceText(player, LETTERS[player.getAttribute("tt-letter-four", 0)], DOORLOCKINTERFACE, 16)
                    } else {
                        (player.incrementAttribute("tt-letter-four", -1))
                        setInterfaceText(player, LETTERS[player.getAttribute("tt-letter-four", 0)], DOORLOCKINTERFACE, 16)
                    }
                }

                LETTERFOURFORWARD -> {
                    if (player.getAttribute("tt-letter-four", 0) == 25) {
                        player.setAttribute("tt-letter-four", 0)
                        setInterfaceText(player, LETTERS[player.getAttribute("tt-letter-four", 0)], DOORLOCKINTERFACE, 16)
                    } else {
                        (player.incrementAttribute("tt-letter-four", 1))
                        setInterfaceText(player, LETTERS[player.getAttribute("tt-letter-four", 0)], DOORLOCKINTERFACE, 16)
                    }
                }

                ENTER -> {
                    val letterOne = LETTERS[player.getAttribute("tt-letter-one", 0)]
                    val letterTwo = LETTERS[player.getAttribute("tt-letter-two", 0)]
                    val letterThree = LETTERS[player.getAttribute("tt-letter-three", 0)]
                    val letterFour = LETTERS[player.getAttribute("tt-letter-four", 0)]

                    if (letterOne == "K" && letterTwo == "U" && letterThree == "R" && letterFour == "T") {
                        player.setAttribute("/save:TT:DoorUnlocked",true)
                        sendMessage(player, "You hear a satisfying click, signifying that the door has been unlocked")
                        closeInterface(player)
                    }
                    else {
                        sendMessage(player,"You hear a satisfying click, and then a worrying thunk.")
                        sendMessage(player,"The floor opens up beneath you sending you plummeting down")
                        sendMessage(player,"to the sewers.")
                        player.teleport(Location.create(2641, 9721, 0))
                        closeInterface(player)
                    }
                }
            }
            return@on true
        }
    }
}