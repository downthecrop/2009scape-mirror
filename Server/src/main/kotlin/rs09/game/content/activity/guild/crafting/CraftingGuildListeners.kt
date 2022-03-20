package rs09.game.content.activity.guild.crafting

import api.*
import core.game.content.dialogue.FacialExpression
import core.game.content.global.action.DoorActionHandler
import core.game.node.entity.skill.Skills
import org.rs09.consts.NPCs
import rs09.game.content.dialogue.region.craftingguild.MasterCrafterDialogue
import rs09.game.content.dialogue.region.craftingguild.TheDoorDialogues
import rs09.game.interaction.InteractionListener

/**
 * @author bushtail
 */

class CraftingGuildListeners : InteractionListener() {
    private val GUILD_DOOR = 2647
    private val APRON = 1757
    private val CAPE = 9780
    val master = NPCs.MASTER_CRAFTER_805

    override fun defineListeners() {
        on(GUILD_DOOR, SCENERY, "open" ) { player, door ->
            if(hasLevelStat(player, Skills.CRAFTING, 40)) {
                if(inEquipment(player, APRON)) {
                    openDialogue(player, TheDoorDialogues(2))
                    DoorActionHandler.handleAutowalkDoor(player, door.asScenery())
                    return@on true
                } else if(inEquipment(player, CAPE)) {
                    openDialogue(player, TheDoorDialogues(2))
                    DoorActionHandler.handleAutowalkDoor(player, door.asScenery())
                    return@on true
                } else {
                    openDialogue(player, TheDoorDialogues(1))
                    return@on false
                }
            } else {
                openDialogue(player, TheDoorDialogues(0))
                return@on false
            }
        }
    }
}