package content.region.misc.keldagrim.handlers

import content.minigame.blastfurnace.BlastFurnace
import core.api.*
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.Location
import core.plugin.Initializable
import core.plugin.Plugin
import core.game.dialogue.DialogueFile
import core.game.dialogue.DialoguePlugin
import core.tools.END_DIALOGUE
import org.rs09.consts.Items

/**
 * File that contains several plugins relating to Keldagrim,
 * most notably the KeldagrimOptionHandlers and GETrapdoorDialogue
 * Anything that wasn't significant enough for its own individual file, tbh.
 * @author Ceikry
 */

const val GETrapdoorDialogueID = 22381232

/**
 * Handles various options around keldagrim that weren't significant enough for a separate file
 */
@Initializable
class KeldagrimOptionHandlers : OptionHandler() {

    override fun handle(player: Player?, node: Node?, option: String?): Boolean {
        player ?: return false
        node ?: return false
        option ?: return false
        when(option){
            "go-through" -> {
                when (node.id) {
                    5973 -> player.properties.teleportLocation = Location.create(2838, 10125)
                    5998 -> player.properties.teleportLocation = Location.create(2780, 10161)
                }
            }
            "open" -> {
                when(node.id){
                    28094 -> player.dialogueInterpreter.open(GETrapdoorDialogueID)
                }
            }
            "enter" -> {
                when(node.id){
                    5014 -> player.properties.teleportLocation = Location.create(2730, 3713, 0)
                }
            }
        }
        return true
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.forId(5973).handlers["option:go-through"] = this
        SceneryDefinition.forId(5998).handlers["option:go-through"] = this
        SceneryDefinition.forId(9084).handlers["option:climb-down"] = this
        SceneryDefinition.forId(9138).handlers["option:climb-up"] = this
        SceneryDefinition.forId(28094).handlers["option:open"] = this
        SceneryDefinition.forId(5014).handlers["option:enter"] = this
        return this
    }
}

/**
 * Dialogue used for the trapdoor in the grand exchange.
 */
@Initializable
class GETrapdoorDialogue(player: Player? = null) : DialoguePlugin(player){
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> end()
            10 -> when(buttonId){
                1 -> KeldagrimCartMethods.goToKeldagrim(player).also { end() }
                2 -> end()
            }
        }
        return true
    }

    override fun open(vararg args: Any?): Boolean {
        val keldagrimVisited = player.getAttribute("keldagrim-visited",false)
        if(keldagrimVisited){
           options("Travel to Keldagrim","Nevermind.")
            stage = 10
        } else {
            player.dialogueInterpreter.sendDialogue("Perhaps I should visit Keldagrim first.")
            stage = 0
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return GETrapdoorDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(GETrapdoorDialogueID)
    }
}


class BlastFurnaceDoorDialogue(val fee: Int) : DialogueFile(){
    var init = true
    override fun handle(componentID: Int, buttonID: Int) {
        when(stage){
            0 -> sendDialogue(player!!,"You must be Smithing Level 60 or higher in order to enter the Blast Furnace").also { stage++ }
            1 -> sendDialogue(player!!,"However, you may enter for 10 minutes if you pay the entrance fee.<br>($fee gp)").also { stage++ }
            2 -> options("Yes","No").also { stage++ }
            3 -> when(buttonID){
                1 -> {
                    if (removeItem(player!!, Item(Items.COINS_995, fee)))
                        BlastFurnace.enter(player!!, true)
                    else
                        sendDialogue(player!!, "You don't have enough gold to cover the entrance fee!")
                    stage = END_DIALOGUE
                }
                20 -> sendDialogue(player!!,"Then get out of here!").also { stage = END_DIALOGUE }
            }
        }
    }
}
