package rs09.game.interaction.item

import api.*
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.update.flag.context.Graphics
import rs09.game.content.dialogue.DialogueFile
import rs09.game.interaction.InteractionListener
import rs09.tools.END_DIALOGUE

/**
 * Handles the Staff of the Raven's (2021 Hween Reward) Recolor Transformation
 */
class StaffOfTheRaven : InteractionListener() {
    val ids = intArrayOf(14654, 14655, 14656)
    override fun defineListeners() {
        on(ids, ITEM, "recolor", "operate"){player, node ->
            val hasUnlocked = player.getAttribute("sotr:purchased",false)
            val hasRecolorA = player.getAttribute("sotr:recolor1", false)
            val hasRecolorB = player.getAttribute("sotr:recolor2", false)
            val isBase = node.id == 14654
            val isOperate = getUsedOption(player) == "operate"

            if(!hasUnlocked){
                //Remove the item if the player has not purchased it. Just in case.
                switchStaff(player, null, isOperate, node.asItem())
                return@on true
            }

            if(!isBase){
                switchStaff(player, 14654, isOperate, node.asItem())
                return@on true
            }

            player.dialogueInterpreter.open(object : DialogueFile(){
                override fun handle(componentID: Int, buttonID: Int) {
                    if(!hasRecolorA && !hasRecolorB){
                        dialogue("You do not have any recolors unlocked.")
                        stage = END_DIALOGUE
                    } else if(hasRecolorA && !hasRecolorB){
                        switchStaff(player, 14655, isOperate, node.asItem())
                    } else if(hasRecolorB && !hasRecolorA){
                        switchStaff(player, 14656, isOperate, node.asItem())
                    } else {
                        when (stage) {
                            0 -> options("Purple", "Orange").also { stage++ }
                            1 -> when (buttonID) {
                                1 -> switchStaff(player, 14655, isOperate, node.asItem())
                                2 -> switchStaff(player, 14656, isOperate, node.asItem())
                                else -> { }
                            }.also { end() }
                        }
                    }
                }
            })
            return@on true
        }
    }

    fun switchStaff(player: Player, to: Int?, equipped: Boolean, original: Item){
        player.graphics(Graphics(1119))
        val item: Item? = if(to != null) Item(to) else to

        if(equipped){
            player.equipment.replace(item, original.slot)
        } else {
            player.inventory.replace(item, original.slot)
        }
    }
}