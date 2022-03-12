package rs09.game.content.zone.keldagrim

import api.*
import core.cache.def.impl.SceneryDefinition
import core.game.content.dialogue.DialoguePlugin
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.world.map.Location
import core.plugin.Initializable
import core.plugin.Plugin
import rs09.game.content.dialogue.DialogueFile

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
            "climb-up" -> {
                when(node.id){
                    9138 -> player.properties.teleportLocation = Location.create(2930, 10197, 0)
                }
            }
            "climb-down" -> {
                when(node.id){
                    9084 -> player.dialogueInterpreter.open(BlastFurnaceDoorDialogue(),Scenery(9084, location(2929,10196,0)))
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


class BlastFurnaceDoorDialogue : DialogueFile(){
    var init = true
    override fun handle(componentID: Int, buttonID: Int) {
        if (init) {
            //stage = if (player!!.getSkills().getLevel(Skills.SMITHING) >= 60) {
            //    100
            //}else{
            //    5
            //}
            //init = false
            //uncomment the above out when BF gets fixed.
            stage = 69
        }
        when(stage){
            5 -> sendDialogue(player!!,"You must be Smithing Level 60 or higher in order to enter the Blast Furnace").also { stage = 10 }
            10 -> sendDialogue(player!!,"However, you may enter if you pay the entrance fee").also { stage = 20 }
            20 -> options("Yes","No").also { stage = 30 }
            30 -> when(buttonID){
                1 -> {
                    if(player!!.equipment.contains(6465,1) && player!!.inventory.contains(995, 1250)){
                        removeItem(player!!, Item(995, 1250), Container.INVENTORY)
                        player!!.setAttribute("BlastTimer",1000)
                        player?.properties?.teleportLocation = Location.create(1940, 4958, 0).also { end() }
                    }
                    else if (player!!.inventory.contains(995, 2500)) {
                        removeItem(player!!, Item(995, 2500), Container.INVENTORY)
                        player!!.setAttribute("BlastTimer",1000)
                        player?.properties?.teleportLocation = Location.create(1940, 4958, 0).also { end() }
                    } else sendDialogue(player!!, "You don't have enough gold to pay the fee!").also { stage = 40 }
                }
                20 -> sendDialogue(player!!,"Then get out of here!").also { stage = 40 }
            }
            40 -> end()
            69 -> sendDialogue(player!!,"The Blast Furnace is temporarily closed down for maintenance.").also { end() } //Remove this line when BF gets fixed
            100 -> player?.properties?.teleportLocation = Location.create(1940, 4958, 0).also { stage = 40 }
        }
    }
}
