package content.region.asgarnia.handlers

import content.global.skill.crafting.TanningProduct
import core.api.*
import core.game.interaction.IntType
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery
import content.region.asgarnia.dialogue.TheDoorDialogues
import core.game.global.action.DoorHelper
import core.game.interaction.InteractionListener

/**
 * @author bushtail, Player Name
 */

class CraftingGuildListeners : InteractionListener {
    override fun defineListeners() {
        on(Scenery.GUILD_DOOR_2647, IntType.SCENERY, "open") { player, door ->
            if (player.location == Location.create(2933, 3289, 0)) {
                if (hasLevelStat(player, Skills.CRAFTING, 40)) {
                    if (anyInEquipment(player, Items.BROWN_APRON_1757, Items.CRAFTING_CAPE_9780, Items.CRAFTING_CAPET_9781)) {
                        openDialogue(player, TheDoorDialogues(0))
                        handleDoor(player, door.asScenery())
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
                handleDoor(player, door.asScenery())
                return@on true
            }
        }
        setDest(IntType.SCENERY, intArrayOf(Scenery.GUILD_DOOR_2647), "open") { entity, node ->
            DoorHelper.getDestination(entity, node.asScenery())
        }
        on(NPCs.TANNER_804, IntType.NPC, "trade") { player, node ->
            TanningProduct.open(player, node.id)
            return@on true
        }
    }
}
