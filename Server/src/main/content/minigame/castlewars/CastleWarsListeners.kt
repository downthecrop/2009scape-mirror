package rs09.game.content.activity.castlewars

import core.api.*
import content.global.skill.agility.AgilityHandler
import content.global.skill.summoning.familiar.BurdenBeast
import core.cache.def.impl.ItemDefinition
import core.game.container.Container
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Items
import org.rs09.consts.Sounds
import rs09.game.content.activity.castlewars.areas.CastleWarsWaitingArea

@Suppress("unused")
class CastleWarsListeners : InteractionListener {

    override fun defineListeners() {

        // Scenery Interactions - PORTALS
        on(CastleWars.joinSaradominTeamPortal, IntType.SCENERY, "Enter") { player, _ ->
            if (GameWorld.settings?.enable_castle_wars != true) return@on false
            if (joinError(player)) return@on true
            // MapArea handles joining properties
            player.properties.teleportLocation = CastleWarsWaitingArea.saradominWaitingRoom.randomWalkableLoc
            player.setAttribute(CastleWars.portalAttribute, CastleWars.saradominName)
            return@on true
        }
        on(CastleWars.joinZamorakTeamPortal, IntType.SCENERY, "Enter") { player, _ ->
            if (GameWorld.settings?.enable_castle_wars != true) return@on false
            if (joinError(player)) return@on true
            // MapArea handles joining properties
            player.properties.teleportLocation = CastleWarsWaitingArea.zamorakWaitingRoom.randomWalkableLoc
            player.setAttribute(CastleWars.portalAttribute, CastleWars.zamorakName)
            return@on true
        }
        on(CastleWars.saradominLeaveLobbyPortal, IntType.SCENERY, "Exit") { player, _ ->
            player.properties.teleportLocation = CastleWars.lobbyBankArea.randomWalkableLoc
            // MapArea handles leaving properties
            return@on true
        }
        on(CastleWars.zamorakLeaveLobbyPortal, IntType.SCENERY, "Exit") { player, _ ->
            player.properties.teleportLocation = CastleWars.lobbyBankArea.randomWalkableLoc
            // MapArea handles leaving properties
            return@on true
        }
        on(CastleWars.joinGuthixTeamPortal, IntType.SCENERY, "Enter") { player, _ ->
            if (GameWorld.settings?.enable_castle_wars != true) return@on false
            if (joinError(player)) return@on true

            // Join the team with fewer players, if they're equal, join randomly
            if (CastleWarsWaitingArea.waitingSaradominPlayers.size < CastleWarsWaitingArea.waitingZamorakPlayers.size) {
                player.properties.teleportLocation = CastleWarsWaitingArea.saradominWaitingRoom.randomWalkableLoc
            } else if (CastleWarsWaitingArea.waitingSaradominPlayers.size > CastleWarsWaitingArea.waitingZamorakPlayers.size) {
                player.properties.teleportLocation = CastleWarsWaitingArea.zamorakWaitingRoom.randomWalkableLoc
            } else {
                if (Math.random() < 0.5) {
                    player.properties.teleportLocation = CastleWarsWaitingArea.saradominWaitingRoom.randomWalkableLoc
                } else {
                    player.properties.teleportLocation = CastleWarsWaitingArea.zamorakWaitingRoom.randomWalkableLoc
                }
            }
            player.setAttribute(CastleWars.portalAttribute, CastleWars.guthixName)
            return@on true
        }

        // Scenery Interactions - Item Tables
        on(CastleWars.cwTableItemRewardMap.keys.toIntArray(), SCENERY, "take-from") { player, node ->
            // Retrieve the item id from the map (null safe)
            val rewardItem: Int = CastleWars.cwTableItemRewardMap.getValue(node.id)

            // If item is added to inventory, play pickup sound
            if (addItem(player, rewardItem)) {
                playAudio(player, Sounds.PICK2_2582)

            // Warn player inventory full using custom dialogue box
            } else {
                val formattedItemName = (ItemDefinition.forId(rewardItem).name.lowercase() + "s.") // Get the formatted item name string
                    .replace("pes.", "pe.")                                     // Replacement for ropes. -> rope.
                    .replace("bronze ", "")                                     // Replacement for bronze pickaxes. -> pickaxes.

                // Toolkit gets a more grammatically correct sentence
                if (rewardItem == Items.TOOLKIT_4051) {
                    sendDialogue(player, "Your inventory is too full to hold a toolkit.")
                } else {
                    sendDialogue(player, (CastleWars.invFullMessage + formattedItemName))
                }
            }
            return@on true
        }

        // Item/Scenery Interaction - Outside Battlement (Wall) Rope Climb Setup
        onUseWith(SCENERY, CastleWars.cwClimbingRope, *CastleWars.cwCastleBattlementsMap.keys.toIntArray()) { player, rope, wall ->
            // Remove rope item from inventory
            removeItem(player, rope)
            // First, replace the scenery with the rope on top of the wall
            replaceScenery(wall.asScenery(), CastleWars.cwCastleBattlementsMap.getValue(wall.id), CastleWars.ropeAliveTicks)
            // Second, create a new scenery with the rope falling down
            val toAdd = Scenery(CastleWars.cwCastleClimbingRope, wall.location, 4, wall.direction.toInteger())
            SceneryBuilder.add(toAdd, CastleWars.ropeAliveTicks)
            toAdd.isActive = true
            return@onUseWith true
        }

        // Scenery Interactions - Outside Battlement (Wall) Player Climbs Rope
        on(CastleWars.cwCastleClimbingRope, SCENERY, "climb") { player, rope ->
            // Get the direction of the scenery
            val dir = rope.asScenery().direction.opposite
            // Move player on top of the wall from that specific rope location
            teleport(player, Location(dir.stepY + player.location.x, -dir.stepX + player.location.y), TeleportManager.TeleportType.INSTANT)
            return@on true
        }

        // Scenery Interactions - Water Tap - No pulse
        onUseWith(SCENERY, Items.BUCKET_1925, CastleWars.castleWaterTap) { player, used, _ ->
            // Lock player
            lock(player, 1)
            // Bucket fill animation
            animate(player, 832)
            // Bucket fill sound
            playAudio(player, Sounds.TAP_FILL_2609)
            // Replace empty -> full water bucket
            replaceSlot(player, used.asItem().slot, Item(Items.BUCKET_OF_WATER_1929))
            return@onUseWith true
        }

        // Scenery Interactions - Stepping stones
        on(CastleWars.cwSteppingStones, SCENERY, "jump-to") { player, stone ->
            // Lock the player
            lock(player, 3)
            // Make the player "Jump" to the next stepping stone
            AgilityHandler.forceWalk(player, -1, player.location, stone.location, Animation(741), 10, 0.0, null, 1)
            // Delay sound to line up with "jump" movement
            runTask(player, 1) {
                // Play jumping sound
                playAudio(player, Sounds.JUMP_2461)
            }
            return@on true
        }
    }

    private fun hasNonCombatItems(container: Container): Boolean {
        for (item in container.toArray()) {
            if (item?.id != null && (item.id == Items.COINS_995 || item.definition?.noteId == item.id))
                return true
        }
        return false
    }

    private fun capeOrHelmetError(player: Player): String? {
        val wornCape = getItemFromEquipment(player, EquipmentSlot.CAPE)?.id ?: -1
        val wornHelmet = getItemFromEquipment(player, EquipmentSlot.HEAD)?.id ?: -1

        if (wornCape != -1 || wornHelmet != -1) return "You can't wear hats, capes, or helms in the arena."
        return null
    }

    private fun nonCombatItemsCheck(player: Player): String? {
        // Coins & Noted items, possibly others?
        if (hasNonCombatItems(player.inventory)) return "You can't take non-combat items into the arena."

        return null
    }

    private fun familiarCheck(player: Player): String? {
        // https://forum.tip.it/topic/250066-castle-wars-familiar-msg/
        // Your familiar can't hold any non-combat items

        // Check the player's familiar container for Coins or Noted items
        val familiar: BurdenBeast = player.familiarManager.familiar as? BurdenBeast ?: return null

        if (hasNonCombatItems(familiar.container)) return "Your familiar can't take non-combat items into the arena."

        return null
    }

    private fun joinError(player: Player): Boolean {
        val errorMessage = capeOrHelmetError(player) ?: nonCombatItemsCheck(player) ?: familiarCheck(player) ?: return false
        player.sendMessage(errorMessage).also { return true }
    }
}