package rs09.game.content.activity.guild.crafting

import api.*
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.skill.Skills
import rs09.game.content.dialogue.region.craftingguild.MasterCrafterDialogue
import rs09.game.interaction.InteractionListener

/**
 * @author bushtail
 */

class CraftingGuildListeners : InteractionListener() {
    private val GUILD_DOOR = 2647
    private val APRON = 1757
    private val CAPE = 9780
    private val MASTER = 805

    override fun defineListeners() {
        on(GUILD_DOOR, SCENERY, "open" ) { player, node ->
            if(hasLevelStat(player, Skills.CRAFTING, 40)) {
                if(inEquipment(player, APRON)) {
                    sendNPCDialogue(player, MASTER, "Welcome to the Guild of Master Craftsmen.", FacialExpression.FRIENDLY)
                    return@on true
                } else if(inEquipment(player, CAPE)) {
                    sendNPCDialogue(player, MASTER, "Welcome to the Guild of Master Craftsmen.", FacialExpression.FRIENDLY)
                    return@on true
                } else {
                    sendNPCDialogue(player, MASTER, "Where's your brown apron? You can't come in here unless you're wearing one.", FacialExpression.DISAGREE)
                    sendPlayerDialogue(player, "Err... I haven't got one.")
                    return@on false
                }
            } else {
                sendNPCDialogue(player, MASTER, "Sorry, only experienced crafters are allowed in here. You must be level 40 or above to enter.")
                return@on false
            }
        }
    }
}