package content.global.skill.cooking

import core.api.*
import core.cache.def.impl.ItemDefinition
import core.game.node.scenery.Scenery
import content.global.skill.cooking.CookingRewrite.Companion.cook
import core.net.packet.PacketRepository
import core.net.packet.context.ChildPositionContext
import core.net.packet.out.RepositionChild
import org.rs09.consts.Items
import core.game.dialogue.DialogueFile
import core.tools.START_DIALOGUE

/**
 * @author Ceikry
 * @author bushtail - fixing it up
 * @auther Woah     - for more fixing up
 */

class CookingDialogue(vararg val args: Any) : DialogueFile(){
    var initial = 0
    var product = 0
    var `object`: Scenery? = null
    var sinew = false
    var itemid = 0
    override fun handle(componentID: Int, buttonID: Int) {
        when(stage){
            START_DIALOGUE -> {
                when (args.size) {
                    2 -> {
                        initial = args.get(0) as Int
                        if (CookableItems.intentionalBurn(initial)) { // checks intentional burning
                            product = CookableItems.getIntentionalBurn(initial).id
                        } else {
                            product = CookableItems.forId(initial).cooked
                        }
                        `object` = args.get(1) as Scenery
                    }
                    5 -> {
                        initial = args.get(0) as Int
                        product = args.get(1) as Int
                        sinew = args.get(2) as Boolean
                        `object` = args.get(3) as Scenery
                        itemid = args.get(4) as Int
                        if (sinew) {
                            options(
                                "Dry the meat into sinew",
                                "Cook the meat"
                            )
                            stage = if (amountInInventory(player!!, initial) > 1) 100 else 101
                            return
                        }
                    }
                }
                display()
            }

            1 -> {
                end()
                val amount = getAmount(buttonID)
                when (amount) {
                    -1 -> sendInputDialogue(player!!, true, "Enter the amount:") { value ->
                        if (value is String) {
                            cook(player!!, `object`, initial, product, value.toInt())
                        } else {
                            cook(player!!, `object`, initial, product, value as Int)
                        }
                    }

                    else -> {
                        end()
                        cook(player!!, `object`, initial, product, amount)
                    }
                }
            }

            100 -> {
                when (buttonID) {
                    1 -> {
                        product = Items.SINEW_9436
                        display()
                    }
                    2 -> {
                        product = CookableItems.forId(initial).cooked
                        display()
                    }
                }
            }

            101 -> {
                when (buttonID) {
                    1 -> {
                        end()
                        cook(player!!, `object`, initial, Items.SINEW_9436, 1)
                    }
                    2 -> {
                        end()
                        cook(player!!, `object`, initial, CookableItems.forId(initial).cooked, 1)
                    }
                }
            }
        }
    }

    private fun getAmount(buttonId: Int): Int {
        when (buttonId) {
            5 -> return 1
            4 -> return 5
            3 -> return -1
            2 -> return player!!.inventory.getAmount(initial)
        }
        return -1
    }

    fun display() {
        player!!.packetDispatch.sendItemZoomOnInterface(initial, 160, 307, 2)
        player!!.packetDispatch.sendString("<br><br><br><br>${ItemDefinition.forId(initial).name}", 307, 6)

        // Re-format this interface because it is not formatted properly for the chat-box
        // Swords
        PacketRepository.send(RepositionChild::class.java, ChildPositionContext(player, 307, 0, 12, 15))
        PacketRepository.send(RepositionChild::class.java, ChildPositionContext(player, 307, 1, 431, 15))
        // "How many would you like to cook?"
        PacketRepository.send(RepositionChild::class.java, ChildPositionContext(player, 307, 7, 0, 12))
        // Right click context menu boxes
        PacketRepository.send(RepositionChild::class.java, ChildPositionContext(player, 307, 3, 58, 27))
        PacketRepository.send(RepositionChild::class.java, ChildPositionContext(player, 307, 4, 58, 27))
        PacketRepository.send(RepositionChild::class.java, ChildPositionContext(player, 307, 5, 58, 27))
        PacketRepository.send(RepositionChild::class.java, ChildPositionContext(player, 307, 6, 58, 27))

        player!!.interfaceManager.openChatbox(307)
        stage = 1
    }
}