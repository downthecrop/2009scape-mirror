package content.global.skill.construction.decoration.workshop



import core.api.*
import core.cache.def.impl.SceneryDefinition
import core.game.dialogue.DialogueInterpreter
import core.game.dialogue.DialoguePlugin
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.plugin.ClassScanner.definePlugin
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs09.consts.Items

/**
 * Handles the 5 types of tool stores
 * ToolsPlugin.java
 * @author Clayton Williams
 * @date Oct 29, 2015
 */


@Initializable
class ToolsPlugin : OptionHandler() {
    val id =0
    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any?> {
        definePlugin(ToolDialogue())
        SceneryDefinition.forId(13699).getHandlers().put("option:search", this)
        SceneryDefinition.forId(13700).getHandlers().put("option:search", this)
        SceneryDefinition.forId(13701).getHandlers().put("option:search", this)
        SceneryDefinition.forId(13702).getHandlers().put("option:search", this)
        SceneryDefinition.forId(13703).getHandlers().put("option:search", this)
        return this
    }
    override fun handle(player: Player, node: Node, option: String?): Boolean {
        openDialogue(player,DialogueInterpreter.getDialogueKey("con:tools"), node.getId())
        return true
    }

    /**
     * Handles the tool dialogue
     * @author Clayton Williams
     * @date Oct 29, 2015
     */
    private inner class ToolDialogue : DialoguePlugin {
        private var id: Int? = null
        /**
         * Constructs a new `ToolDialogue` `Object`.
         */
        constructor()

        /**
         * Constructs a new `ToolDialogue` `Object`.
         *
         * @param player
         * the player.
         */
        constructor(player: Player?) : super(player)

        fun addItemMsg(player: Player, id: Int, amount: Int = 1) {

            if (!addItem(player, id,amount)){
                sendDialogue(player, "You have no space in your inventory.")
                stage = 3  //Stage 3, player just needs to confirm they received the no inventory space dialogue
            } else {end()} //Player received their Item, close dialogue
        }

        public override fun newInstance(player: Player?): DialoguePlugin {
            return ToolDialogue(player)
        }

        override fun open(vararg args: Any?): Boolean {
            id = args[0] as Int
            when (id) {
                13699 -> {
                    sendDialogueOptions(player,"Select a Tool", "Saw", "Chisel", "Hammer", "Shears")
                }

                13700 -> {
                    sendDialogueOptions(player,"Select a Tool", "Bucket", "Knife", "Spade", "Tinderbox")
                }

                13701 -> {
                    sendDialogueOptions(player,"Select a Tool", "Brown apron", "Glassblowing pipe", "Needle")
                }

                13702 -> {
                    sendDialogueOptions(player,"Select a Tool","Amulet mould","Necklace mould","Ring mould","Holy mould","More Options"
                    )
                    stage = 1
                }

                13703 -> {
                    sendDialogueOptions(player,"Select a Tool", "Rake", "Spade", "Trowel", "Seed dibber", "Watering can")
                }
            }
            return true
        }
        override fun handle(interfaceId: Int, buttonId: Int): Boolean {
            if (stage == 3) {//If reply was on full inventory confirmation, close dialogue
                end()
                return true
            }
            when (id) {
                13699 -> {
                    when (buttonId) {
                        1 -> {
                            addItemMsg(player, Items.SAW_8794)
                        }

                        2 -> {
                            addItemMsg(player, Items.CHISEL_1755)
                        }

                        3 -> {
                            addItemMsg(player, Items.HAMMER_2347)
                        }

                        4 -> {
                            addItemMsg(player, Items.SHEARS_1735)
                        }
                    }
                }

                13700 -> {
                    when (buttonId) {
                        1 -> {
                            addItemMsg(player, Items.BUCKET_1925)
                        }

                        2 -> {
                            addItemMsg(player, Items.KNIFE_946)
                        }

                        3 -> {
                            addItemMsg(player, Items.SPADE_952)
                        }

                        4 -> {
                            addItemMsg(player, Items.TINDERBOX_590)
                        }
                    }
                }

                13701 -> {
                    when (buttonId) {
                        1 -> {
                            addItemMsg(player, Items.BROWN_APRON_1757)
                        }

                        2 -> {
                            addItemMsg(player, Items.GLASSBLOWING_PIPE_1785)
                        }

                        3 -> {
                            addItemMsg(player, Items.NEEDLE_1733)
                        }
                    }
                }

                13702 -> {
                    when (stage) {
                        1 -> when (buttonId) {
                            1 -> {
                                addItemMsg(player, Items.AMULET_MOULD_1595)
                            }

                            2 -> {
                                addItemMsg(player, Items.NECKLACE_MOULD_1597)
                            }

                            3 -> {
                                addItemMsg(player, Items.RING_MOULD_1592)
                            }

                            4 -> {
                                addItemMsg(player, Items.HOLY_MOULD_1599)
                            }

                            5 -> {                                
                                sendDialogueOptions(player,
                                    "Select a Tool", "Bracelet mould", "Tiara mould"
                                )
                                stage = 2
                            }
                        }
                        2 -> {
                            when (buttonId) {
                                1 -> {
                                    addItemMsg(player, Items.BRACELET_MOULD_11065)
                                }

                                2 -> {
                                    addItemMsg(player, Items.TIARA_MOULD_5523)
                                }
                            }
                        }
                    }
                }
                13703 -> {
                    when (buttonId) {
                        1 -> {
                            addItemMsg(player, Items.RAKE_5341)
                        }

                        2 -> {
                            addItemMsg(player, Items.SPADE_952)
                        }

                        3 -> {
                            addItemMsg(player, Items.TROWEL_676)
                        }

                        4 -> {
                            addItemMsg(player, Items.SEED_DIBBER_5343)
                        }

                        5 -> {
                            addItemMsg(player, Items.WATERING_CAN_5331)
                        }
                    }
                }
            }
            return true
        }
        override fun getIds(): IntArray {
            return intArrayOf(DialogueInterpreter.getDialogueKey("con:tools"))
        }
    }
}
