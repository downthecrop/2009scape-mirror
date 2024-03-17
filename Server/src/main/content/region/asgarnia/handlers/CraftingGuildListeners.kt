package content.region.asgarnia.handlers

import content.global.skill.crafting.TanningProduct
import core.api.*
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import org.rs09.consts.Items
import org.rs09.consts.Scenery
import content.region.asgarnia.dialogue.TheDoorDialogues
import core.game.interaction.InteractionListener
import core.game.interaction.IntType
import org.rs09.consts.NPCs

/**
 * @author bushtail
 */

class CraftingGuildListeners : InteractionListener {
    private val GUILD_DOOR = Scenery.GUILD_DOOR_2647
    private val REQUIRED_ITEMS = intArrayOf(Items.BROWN_APRON_1757, Items.CRAFTING_CAPE_9780, Items.CRAFTING_CAPET_9781)

    override fun defineListeners() {
        on(GUILD_DOOR, IntType.SCENERY, "open") { player, door ->
            if (player.location == Location.create(2933, 3289, 0)) {
                if (hasLevelStat(player, Skills.CRAFTING, 40)) {
                    if (anyInEquipment(player, *REQUIRED_ITEMS)) {
                        openDialogue(player, TheDoorDialogues(0))
                        core.game.global.action.DoorActionHandler.handleAutowalkDoor(player, door.asScenery())
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
                core.game.global.action.DoorActionHandler.handleAutowalkDoor(player, door.asScenery())
                return@on true
            }
        }

        on(NPCs.TANNER_804, IntType.NPC, "trade") { player, node ->
            TanningProduct.open(player, node.id)
            return@on true
        }
    }
}