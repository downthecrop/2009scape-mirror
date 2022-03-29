package rs09.game.node.entity.skill.cooking

import api.*
import core.cache.def.impl.ItemDefinition
import core.game.node.scenery.Scenery
import core.game.node.entity.skill.cooking.CookableItems
import core.game.node.entity.skill.cooking.CookingRewrite.Companion.cook
import core.game.node.item.Item
import core.net.packet.PacketRepository
import core.net.packet.context.ChildPositionContext
import core.net.packet.out.RepositionChild
import org.rs09.consts.Items
import rs09.game.content.dialogue.DialogueFile
import rs09.game.content.dialogue.SkillDialogueHandler
import rs09.tools.START_DIALOGUE

/**
 * @author Ceikry
 * @author bushtail - fixing it up
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
                            stage = 100
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
                    -1 -> sendInputDialogue(player!!, true, "Enter the amount:") { value -> cook(player!!, `object`, initial, product, value as Int) }

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
                        display(Items.COOKED_MEAT_2142)
                    }
                    2 -> {
                        product = CookableItems.forId(initial).cooked
                        display()
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

    fun display(vararg args : Int) {
        player!!.interfaceManager.openChatbox(307)
        PacketRepository.send(RepositionChild::class.java, ChildPositionContext(player, 307, 3, 60, 90))
        PacketRepository.send(RepositionChild::class.java, ChildPositionContext(player, 307, 2, 208, 20))
        player!!.packetDispatch.sendItemZoomOnInterface(if(args.size == 1) args[0] else product, 160, 307, 2)
        player!!.packetDispatch.sendString(ItemDefinition.forId(product).name, 307, 3)
        stage = 1
    }
}