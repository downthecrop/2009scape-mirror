package rs09.game.content.ame.events.certer

import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs09.consts.Items
import rs09.game.interaction.InterfaceListener

class CerterEventInterface : InterfaceListener() {
    val CERTER_INTERFACE = 184
    val OPTION_A_CHILD = 1
    val OPTION_B_CHILD = 2
    val OPTION_C_CHILD = 3
    val ITEM_CHILD = 7
    val items = mapOf(
        /*
         * NOTE: We use these "NULL" items to get their model id + zoom values.
         * These were the actual items used by this random event before they
         * decided to swap over to using model ids instead.
         */
        Items.BOWL_1923 to "A bowl.",
        Items.NULL_6189 to "A fish.",
        Items.NULL_6190 to "A bass.",
        Items.NULL_6191 to "A sword.",
        Items.NULL_6192 to "A battleaxe.",
        Items.NULL_6193 to "A helmet.",
        Items.NULL_6194 to "A kiteshield.",
        Items.NULL_6195 to "A pair of shears.",
        Items.NULL_6196 to "A shovel.",
        Items.NULL_6197 to "A ring.",
        Items.NULL_6198 to "A necklace."
    )
    val falseOptions = arrayOf("An axe.", "An arrow.", "A pair of boots.", "A pair of gloves.", "A staff.", "A bow.", "A feather.", "The disenfrachaised youth of 1940's Columbia.")
    override fun defineListeners() {
        on(CERTER_INTERFACE) { player, _, _, buttonID, _, _ ->
            val answer = buttonID - 7
            val correctAnswer = player.getAttribute("certer:correctIndex", 0)
            player.setAttribute("certer:correct", correctAnswer == answer)
            player.interfaceManager.close()
            player.dialogueInterpreter.open(CerterDialogue(false), player.antiMacroHandler.event?.asNpc())
            return@on true
        }

        onOpen(CERTER_INTERFACE) { player, _ ->
            generateOptions(player)
            return@onOpen true
        }

        onClose(CERTER_INTERFACE) { player, _ ->
            player.setAttribute("random:pause", false)
            return@onClose true
        }
    }

    fun generateOptions(player: Player) {
        val correct = items.keys.random()
        val indexes = arrayListOf(1, 2, 3)
        val correctIndex = indexes.random()
        val correctItem = Item(correct).definition
        val iFaceModelId = correctItem.interfaceModelId
        val iFaceZoom = correctItem.modelZoom
        indexes.remove(correctIndex)
        player.setAttribute("certer:correctIndex", correctIndex)

        player.packetDispatch.sendString(items[correct], CERTER_INTERFACE, optionFromIndex(correctIndex))

        val tempOptions = falseOptions.toMutableList()
        val false1 = tempOptions.random()
        tempOptions.remove(false1)
        val false2 = tempOptions.random()

        player.packetDispatch.sendString(false1, CERTER_INTERFACE, optionFromIndex(indexes[0]))
        player.packetDispatch.sendString(false2, CERTER_INTERFACE, optionFromIndex(indexes[1]))
        player.packetDispatch.sendModelOnInterface(iFaceModelId, CERTER_INTERFACE, ITEM_CHILD, iFaceZoom)
    }

    fun optionFromIndex(index: Int): Int {
        return when (index) {
            1 -> OPTION_A_CHILD
            2 -> OPTION_B_CHILD
            3 -> OPTION_C_CHILD
            else -> OPTION_A_CHILD
        }
    }
}