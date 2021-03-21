package rs09.game.content.ame.events.certer

import core.game.node.entity.player.Player
import org.rs09.consts.Items
import rs09.game.interaction.InterfaceListener

class CerterEventInterface : InterfaceListener() {
    val CERTER_INTERFACE = 184
    val OPTION_A_CHILD = 1
    val OPTION_B_CHILD = 2
    val OPTION_C_CHILD = 3
    val ITEM_CHILD = 7
    val items = mapOf(
        Items.BLACK_SWORD_1283 to "a sword",
        Items.BRONZE_SWORD_1277 to "a sword",
        Items.BRONZE_CHAINBODY_1103 to "a platebody",
        Items.BLACK_CHAINBODY_1107 to "a platebody",
        Items.WOODEN_SHIELD_1171 to "a shield",
        Items.BRONZE_KITESHIELD_1189 to "a shield",
    )
    val falseOptions = arrayOf("a ring","a dragon","a cat","a helmet","a pair of boots","a fish","a gun","a staff","a cannon","a dwarf","a bow","an arrow","a chinchompa","a chicken","a feather","a ninja","a bot")
    override fun defineListeners() {
        on(CERTER_INTERFACE){player, _, _, buttonID, _, _ ->
            val answer = buttonID - 7
            val correctAnswer = player.getAttribute("certer:correctIndex",0)
            player.setAttribute("certer:correct",correctAnswer == answer)
            player.interfaceManager.close()
            player.dialogueInterpreter.open(CerterDialogue(false),player.antiMacroHandler.event?.asNpc())
            return@on true
        }

        onOpen(CERTER_INTERFACE){player, _ ->
            generateOptions(player)
            return@onOpen true
        }
    }

    fun generateOptions(player: Player) {
        val correct = items.keys.random()
        val indexes = arrayListOf(1,2,3)
        val correctIndex = indexes.random()
        indexes.remove(correctIndex)
        player.setAttribute("certer:correctIndex",correctIndex)

        player.packetDispatch.sendString(items[correct],CERTER_INTERFACE,optionFromIndex(correctIndex))

        val tempOptions = falseOptions
        val false1 = tempOptions.random()
        var false2 = tempOptions.random()
        while(false1 == false2) false2 = tempOptions.random()

        player.packetDispatch.sendString(false1,CERTER_INTERFACE,optionFromIndex(indexes[0]))
        player.packetDispatch.sendString(false2,CERTER_INTERFACE,optionFromIndex(indexes[1]))
        player.packetDispatch.sendItemOnInterface(correct,1,CERTER_INTERFACE,ITEM_CHILD)
    }

    fun optionFromIndex(index: Int): Int{
        return when(index){
            1 -> OPTION_A_CHILD
            2 -> OPTION_B_CHILD
            3 -> OPTION_C_CHILD
            else -> OPTION_A_CHILD
        }
    }
}