package content.region.asgarnia.burthorpe.quest.deathplateau

import core.api.*
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.GroundItemManager
import org.rs09.consts.Items
import org.rs09.consts.Scenery

class DeathPlateauInteractionListener : InteractionListener {

    companion object {
        val stoneBalls = intArrayOf(
            Items.STONE_BALL_3109, // Red
            Items.STONE_BALL_3110, // Blue
            Items.STONE_BALL_3111, // Yellow
            Items.STONE_BALL_3112, // Purple
            Items.STONE_BALL_3113  // Green
        )
        val stoneMechanisms = intArrayOf(
            Scenery.STONE_MECHANISM_3676, // 4 Outer stone plates
            Scenery.STONE_MECHANISM_3677 // 2 Inner stone plates
        )
    }
    override fun defineListeners() {
        on(Scenery.DOOR_3747, SCENERY, "open") { player, _ ->
            // Harold's Door
            if (player.location == location(2906, 3543, 1)) {
                openDialogue(player, DeathPlateauDoorDialogueFile(1))
            } else {
                DoorActionHandler.handleAutowalkDoor(player, getScenery(2906, 3543, 1))
            }
            return@on true
        }
        on(Scenery.DOOR_3745, SCENERY, "open") { player, node ->
            if (node.location == location(2823, 3555, 0)) {
                // 1st Door to Tenzing
                openDialogue(player, DeathPlateauDoorDialogueFile(2))
            } else if (node.location == location(2820, 3558, 0)) {
                // 2nd Door to chicken pen
                openDialogue(player, DeathPlateauDoorDialogueFile(3))
            }
            return@on true
        }

        on(Items.IOU_3103, ITEM, "read") { player, _ ->
            openDialogue(player, IOUNoteDialogueFile())
            return@on true
        }

        on(Items.COMBINATION_3102, ITEM, "read") { player, _ ->
            openInterface(player, 220)
            setInterfaceText(player, "<col=3D1E00>Red is North of Blue. Yellow is South of Purple.", 220, 7)
            setInterfaceText(player, "<col=3D1E00>Green is North of Purple. Blue is West of", 220, 8)
            setInterfaceText(player, "<col=3D1E00>Yellow. Purple is East of Red.", 220, 9)
            return@on true
        }

        onUseWith(IntType.SCENERY, stoneBalls, *stoneMechanisms) { player, used, with ->
            val stoneBall = used.asItem()
            val stoneMechanism = with.asScenery()

            // Place item on table
            if (removeItem(player, stoneBall)) {
                produceGroundItem(player, stoneBall.id, 1, stoneMechanism.location)
            }
            // Check if order was correct
            /**
             * Facing north
             * NONE [2894, 3564, 0] [2895, 3564, 0] GREEN
             * RED  [2894, 3563, 0] [2895, 3563, 0] PURPLE
             * BLUE [2894, 3562, 0] [2895, 3562, 0] YELLOW
             */
            if (GroundItemManager.get(Items.STONE_BALL_3109, location(2894, 3563, 0), player) != null &&
                GroundItemManager.get(Items.STONE_BALL_3110, location(2894, 3562, 0), player) != null &&
                GroundItemManager.get(Items.STONE_BALL_3111, location(2895, 3562, 0), player) != null &&
                GroundItemManager.get(Items.STONE_BALL_3112, location(2895, 3563, 0), player) != null &&
                GroundItemManager.get(Items.STONE_BALL_3113, location(2895, 3564, 0), player) != null) {
                if (getQuestStage(player, DeathPlateau.questName) == 16) {
                    sendMessage(player, "The equipment room door has unlocked.")
                    setQuestStage(player, DeathPlateau.questName, 19)
                }
            }
            return@onUseWith true
        }

        on(Scenery.LARGE_DOOR_3743, SCENERY, "open") { player, node ->
            if (getQuestStage(player, DeathPlateau.questName) > 16) {
                DoorActionHandler.handleAutowalkDoor(player, node as core.game.node.scenery.Scenery)
            } else {
                sendMessage(player, "The door is locked.")
            }
            return@on true
        }
    }
}