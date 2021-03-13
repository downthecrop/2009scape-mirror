package rs09.game.content.dialogue

import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.net.packet.PacketRepository
import core.net.packet.context.ChildPositionContext
import core.net.packet.out.RepositionChild
import core.tools.StringUtils

/**
 * Represents a skill dialogue handler class.
 * @author Vexia
 */
open class SkillDialogueHandler(
        /**
         * Represents the player.
         */
        val player: Player,
        /**
         * Represents the skill dialogue type.
         */
        val type: SkillDialogue?, vararg data: Any) {
    /**
     * Gets the player.
     * @return The player.
     */

    /**
     * Gets the type.
     * @return The type.
     */

    /**
     * Gets the passed data.
     * @return the data.
     */
    /**
     * Represents the object data passed through.
     */
    val data: Array<Any>

    /**
     * Method used to open a skill dialogue.
     */
    fun open() {
        player.dialogueInterpreter.open(SKILL_DIALOGUE, this)
    }

    /**
     * Method used to display the content on the dialogue.
     */
    fun display() {
        if (type == null) {
            player.debug("Error! Type is null.")
            return
        }
        type.display(player, this)
    }

    /**
     * Method used to create a product.
     * @param amount the amount.
     * @param index the index.
     */
    open fun create(amount: Int, index: Int) {}

    /**
     * Gets the total amount of items to be made.
     * @param index the index.
     * @return the amount.
     */
    open fun getAll(index: Int): Int {
        return player.inventory.getAmount(data[0] as Item)
    }

    /**
     * Gets the name.
     * @param item the item.
     * @return the name.
     */
    protected open fun getName(item: Item): String {
        return StringUtils.formatDisplayName(item.name.replace("Unfired", ""))
    }

    /**
     * Represents a skill dialogue type.
     * @author 'Vexia
     */
    enum class SkillDialogue
    /**
     * Constructs a new `SkillDialogue` `Object`.
     * @param interfaceId the interface id.
     * @param base the base button.
     * @param length the length.
     */(
            /**
             * Represents the interface id.
             */
            val interfaceId: Int,
            /**
             * Represents the base button.
             */
            private val baseButton: Int,
            /**
             * Represents the length.
             */
            private val length: Int) {
        ONE_OPTION(309, 5, 1) {
            override fun display(player: Player, handler: SkillDialogueHandler) {
                val item = handler.data[0] as Item
                player.packetDispatch.sendString("<br><br><br><br>" + item.name, 309, 6)
                player.packetDispatch.sendItemZoomOnInterface(item.id, 160, 309, 2)
                PacketRepository.send(RepositionChild::class.java, ChildPositionContext(player, 309, 6, 60, 20))
                PacketRepository.send(RepositionChild::class.java, ChildPositionContext(player, 309, 2, 210, 30))
            }

            override fun getAmount(handler: SkillDialogueHandler, buttonId: Int): Int {
                return when(buttonId){
                    5 -> 1
                    4 -> 5
                    3 -> -1 //-1 is used to prompt an "enter an amount"
                    else -> handler.getAll(getIndex(handler, buttonId))
                }
            }
        },
        TWO_OPTION(303, 7, 2) {
            override fun display(player: Player, handler: SkillDialogueHandler) {
                var item: Item
                player.interfaceManager.openChatbox(306)
                for (i in handler.data.indices) {
                    item = handler.data[i] as Item
                    player.packetDispatch.sendString("<br><br><br><br>" + handler.getName(item), 303, 7 + i)
                    player.packetDispatch.sendItemZoomOnInterface(item.id, 160, 303, 2 + i)
                }
            }

            override fun getIndex(handler: SkillDialogueHandler?, buttonId: Int): Int {
                when (buttonId) {
                    6, 5, 4, 3 -> return 0
                    10, 9, 8, 7 -> return 1
                }
                return 1
            }

            override fun getAmount(handler: SkillDialogueHandler, buttonId: Int): Int {
                when (buttonId) {
                    6, 10 -> return 1
                    5, 9 -> return 5
                    4, 8 -> return 10
                    3, 7 -> return -1
                }
                return 1
            }
        },
        THREE_OPTION(304, 8, 3) {
            override fun display(player: Player, handler: SkillDialogueHandler) {
                var item: Item? = null
                for (i in 0..2) {
                    item = handler.data[i] as Item
                    player.packetDispatch.sendItemZoomOnInterface(item.id, 135, 304, 2 + i)
                    player.packetDispatch.sendString("<br><br><br><br>" + item!!.name, 304, 304 - 296 + i * 4)
                }
            }

            override fun getIndex(handler: SkillDialogueHandler?, buttonId: Int): Int {
                when (buttonId) {
                    7, 6, 5, 4 -> return 0
                    11, 10, 9, 8 -> return 1
                    15, 14, 13, 12 -> return 2
                }
                return 1
            }

            override fun getAmount(handler: SkillDialogueHandler, buttonId: Int): Int {
                when (buttonId) {
                    7, 11, 15 -> return 1
                    6, 10, 14 -> return 5
                    5, 9, 13 -> return 10
                    4, 8, 12 -> return -1
                }
                return 1
            }
        },
        FOUR_OPTION(305, 9, 4) {
            override fun display(player: Player, handler: SkillDialogueHandler) {
                var item: Item? = null
                for (i in 0..3) {
                    item = handler.data[i] as Item
                    player.packetDispatch.sendItemZoomOnInterface(item!!.id, 135, 305, 2 + i)
                    player.packetDispatch.sendString("<br><br><br><br>" + item.name, 305, 305 - 296 + i * 4)
                }
            }

            override fun getIndex(handler: SkillDialogueHandler?, buttonId: Int): Int {
                when (buttonId) {
                    5, 8, 6, 7 -> return 0
                    9, 10, 11, 12 -> return 1
                    13, 14, 15, 16 -> return 2
                    17, 18, 19, 20 -> return 3
                }
                return 0
            }

            override fun getAmount(handler: SkillDialogueHandler, buttonId: Int): Int {
                when (buttonId) {
                    8, 12, 16, 20 -> return 1
                    7, 11, 15, 19 -> return 5
                    6, 10, 14, 18 -> return 10
                    5, 9, 13, 17 -> return -1
                }
                return 1
            }
        },
        FIVE_OPTION(306, 7, 5) {
            /**
             * Represents the position data.
             */
            private val positions = arrayOf(intArrayOf(10, 30), intArrayOf(117, 10), intArrayOf(217, 20), intArrayOf(317, 15), intArrayOf(408, 15))
            override fun display(player: Player, handler: SkillDialogueHandler) {
                var item: Item
                player.interfaceManager.openChatbox(306)
                for (i in handler.data.indices) {
                    item = handler.data[i] as Item
                    player.packetDispatch.sendString("<br><br><br><br>" + handler.getName(item), 306, 10 + 4 * i)
                    player.packetDispatch.sendItemZoomOnInterface(item.id, 160, 306, 2 + i)
                    PacketRepository.send(RepositionChild::class.java, ChildPositionContext(player, 306, 2 + i, positions[i][0], positions[i][1]))
                }
            }

            override fun getIndex(handler: SkillDialogueHandler?, buttonId: Int): Int {
                when (buttonId) {
                    9, 8, 7, 6 -> return 0
                    13, 12, 11, 10 -> return 1
                    17, 16, 15, 14 -> return 2
                    21, 20, 19, 18 -> return 3
                    25, 24, 23, 22 -> return 4
                }
                return 0
            }

            override fun getAmount(handler: SkillDialogueHandler, buttonId: Int): Int {
                when (buttonId) {
                    9, 13, 17, 21, 25 -> return 1
                    8, 12, 16, 20, 24 -> return 5
                    7, 11, 15, 19, 23 -> return 10
                    6, 10, 14, 18, 22 -> return -1
                }
                return 1
            }
        };

        /**
         * Gets the interfaceId.
         * @return The interfaceId.
         */

        /**
         * Method used to display the content for this type.
         * @param player the player.
         * @param handler the handler.
         */
        open fun display(player: Player, handler: SkillDialogueHandler) {}

        /**
         * Gets the amount.
         * @param handler the handler.
         * @param buttonId the buttonId.
         * @return the amount.
         */
        open fun getAmount(handler: SkillDialogueHandler, buttonId: Int): Int {
            for (k in 0..3) {
                for (i in 0 until length) {
                    val `val` = baseButton - k + 4 * i
                    if (`val` == buttonId) {
                        return if (k == 13) 1 else if (k == 8) 5 else if (k == 7) 10 else 6
                    }
                }
            }
            return -1
        }

        /**
         * Gets the index selected.
         * @param handler the handler.
         * @param buttonId the buttonId.
         * @return the index selected.
         */
        open fun getIndex(handler: SkillDialogueHandler?, buttonId: Int): Int {
            var index = 0
            for (k in 0..3) {
                for (i in 1 until length) {
                    val `val` = baseButton + k + 4 * i
                    if (`val` == buttonId) {
                        return index + 1
                    } else if (`val` <= buttonId) {
                        index++
                    }
                }
                index = 0
            }
            return index
        }

        companion object {
            /**
             * Gets the type for the length.
             * @param length2 the length to compare.
             * @return the type.
             */
            fun forLength(length2: Int): SkillDialogue? {
                for (dial in values()) {
                    if (dial.length == length2) {
                        return dial
                    }
                }
                return null
            }
        }

    }

    companion object {
        /**
         * Represents the skill dialogue id.
         */
        const val SKILL_DIALOGUE = 3 shl 16
    }

    /**
     * Constructs a new `SkillDialogueHandler` `Object`.
     * @param player the player.
     * @param type the type.
     * @param data the data.
     */
    init {
        this.data = data as Array<Any>
    }
}