package rs09.game.content.activity.guild.crafting

import api.*
import core.game.content.global.action.DoorActionHandler
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import org.rs09.consts.Items
import org.rs09.consts.Scenery
import rs09.game.content.dialogue.region.craftingguild.TheDoorDialogues
import rs09.game.interaction.InteractionListener

/**
 * @author bushtail
 */

class CraftingGuildListeners : InteractionListener() {
    private val GUILD_DOOR = Scenery.GUILD_DOOR_2647
    private val APRON = Items.BROWN_APRON_1757
    private val CAPE = Items.CRAFTING_CAPE_9780

    override fun defineListeners() {
        on(GUILD_DOOR, SCENERY, "open") { player, door ->
            if (player.location == Location.create(2933, 3289, 0)) {
                if (hasLevelStat(player, Skills.CRAFTING, 40)) {
                    if (inEquipment(player, APRON)) {
                        openDialogue(player, TheDoorDialogues(0))
                        DoorActionHandler.handleAutowalkDoor(player, door.asScenery())
                        return@on true
                    } else if (inEquipment(player, CAPE)) {
                        openDialogue(player, TheDoorDialogues(0))
                        DoorActionHandler.handleAutowalkDoor(player, door.asScenery())
                        return@on true
                    } else {
                        openDialogue(player, TheDoorDialogues(1))
                        return@on false
                    }
                } else {
                    openDialogue(player, TheDoorDialogues(2))
                    return@on false
                }
            } else {
                DoorActionHandler.handleAutowalkDoor(player, door.asScenery())
                return@on true
            }
        }
    }
}
