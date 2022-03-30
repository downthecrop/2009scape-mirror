package core.game.interaction.item

import api.EquipmentSlot
import api.openDialogue
import api.sendMessage
import rs09.game.world.GameWorld.Pulser
import rs09.game.content.activity.pyramidplunder.PyramidPlunderMinigame.Companion.GUARDIAN_ROOM
import core.plugin.Initializable
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import org.rs09.consts.Items
import rs09.game.content.dialogue.DialogueFile
import rs09.game.interaction.InteractionListener
import rs09.tools.END_DIALOGUE

/**
 * Adds functionality to the pharoah's scepter
 * @author ceik
 */
class PharoahSceptre : InteractionListener() {
    override fun defineListeners() {
        val SCEPTRES = intArrayOf(Items.PHARAOHS_SCEPTRE_9044, Items.PHARAOHS_SCEPTRE_9046, Items.PHARAOHS_SCEPTRE_9048, Items.PHARAOHS_SCEPTRE_9050)

        on(SCEPTRES, ITEM, "teleport", "operate"){player, node ->
            val sceptre = node.asItem()

            if(sceptre.id == SCEPTRES.last())
            {
                sendMessage(player, "You have used up all the charges on this sceptre.")
                return@on true
            }

            openDialogue(player, SceptreDialog())
            return@on true
        }
    }

    class SceptreDialog : DialogueFile() {
        fun teleport(location: Location?, player: Player) {
            player.lock()
            player.visualize(ANIMATION, GRAPHICS)
            player.impactHandler.disabledTicks = 4
            Pulser.submit(object : Pulse(4, player) {
                override fun pulse(): Boolean {
                    player.unlock()
                    player.properties.teleportLocation = location
                    player.animator.reset()
                    return true
                }
            })
        }

        override fun handle(componentID: Int, buttonID: Int) {
            if (player!!.isTeleBlocked) {
                player!!.sendMessage("A magical force has stopped you from teleporting.")
                return
            }
            when (stage) {
                0 -> options("Jalsavrah", "Jaleustrophos", "Jaldraocht", "Nowhere").also { stage++ }
                1 -> {
                    end()
                    when (buttonID) {
                        1 -> {
                            teleport(GUARDIAN_ROOM, player!!)
                        }
                        2 -> {
                            teleport(Location.create(3342, 2827, 0), player!!)
                        }
                        3 -> {
                            teleport(Location.create(3233, 2902, 0), player!!)
                        }
                        4 -> return
                    }
                    //This sucks but I'm too lazy to fix it.
                    if (player!!.equipment.containsItem(Item(9044))) {
                        player!!.equipment.replace(Item(9046), EquipmentSlot.WEAPON.ordinal)
                        player!!.packetDispatch.sendMessage("<col=7f03ff>Your Pharoah's Sceptre has 2 charges remaining.")
                    } else if (player!!.equipment.containsItem(Item(9046))) {
                        player!!.equipment.replace(Item(9048), EquipmentSlot.WEAPON.ordinal)
                        player!!.packetDispatch.sendMessage("<col=7f03ff>Your Pharoah's Sceptre has 1 charge remaining.")
                    } else if (player!!.equipment.containsItem(Item(9048))) {
                        player!!.equipment.replace(Item(9050), EquipmentSlot.WEAPON.ordinal)
                        player!!.sendMessage("<col=7f03ff>Your Pharoah's Sceptre has used its last charge.")
                    } else if (player!!.inventory.containsItem(Item(9050))) {
                        player!!.inventory.remove(Item(9050))
                        player!!.inventory.add(Item(9048))
                        player!!.packetDispatch.sendMessage("<col=7f03ff>Your Pharoah's Sceptre has 2 charges remaining.")
                    } else if (player!!.inventory.containsItem(Item(9048))) {
                        player!!.inventory.remove(Item(9048))
                        player!!.inventory.add(Item(9046))
                        player!!.packetDispatch.sendMessage("<col=7f03ff>Your Pharoah's Sceptre has 1 charge remaining.")
                    } else if (player!!.inventory.containsItem(Item(9046))) {
                        player!!.inventory.remove(Item(9046))
                        player!!.inventory.add(Item(9044))
                        player!!.sendMessage("<col=7f03ff>Your Pharoah's Sceptre has used its last charge.")
                    }
                }
            }
        }
    }

    companion object {
        private val GRAPHICS = Graphics(715)
        private val ANIMATION = Animation(714)
    }
}