package core.game.content.quest.members.familycrest

import core.game.node.entity.impl.ForceMovement
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.update.flag.context.Animation
import core.net.packet.PacketRepository
import core.net.packet.context.BuildSceneryContext
import core.net.packet.out.ClearScenery
import core.net.packet.out.ConstructScenery
import core.net.packet.out.UpdateAreaPosition;
import rs09.game.interaction.InteractionListener

fun doDoor(player: Player, scenery: Scenery) {
    val d = if(scenery.rotation == 0 || scenery.rotation == 3) { -1 } else { 0 }
    var direction = Direction.NORTH
    if(scenery.rotation % 2 == 0) {
        direction = if(player.location.x <= scenery.location.x + d) { Direction.EAST } else { Direction.WEST }
    } else {
        direction = if(player.location.y <= scenery.location.y + d) { Direction.NORTH } else { Direction.SOUTH }
    }
    ForceMovement.run(player,
        player.location,
        player.location.transform(direction.stepX, direction.stepY, 0),
        ForceMovement.WALK_ANIMATION,
        ForceMovement.WALK_ANIMATION,
        direction,
        8);

}

class WitchavenLeverInteraction : InteractionListener() {
	val DOWN_ANIMATION = Animation(2140)
	val UP_ANIMATION = Animation(2139)

    // TODO: It seems like the even numbers are used for the "up" positions, but there's no parent object with varps for them.
    // Currently, we just send a `ConstructScenery` packet to the specific player. Is there a cleaner way to do this?
    val NORTH_LEVER_A = 2421
    val NORTH_LEVER_B = 2425
    val SOUTH_LEVER = 2423
    val LEVERS = intArrayOf(NORTH_LEVER_A, NORTH_LEVER_A+1, NORTH_LEVER_B, NORTH_LEVER_B+1, SOUTH_LEVER, SOUTH_LEVER+1)

    val NORTH_DOOR = 2431 // 92 in RSC
    val HELLHOUND_DOOR = 2430 // 91 in RSC
    val SOUTHWEST_DOOR = 2427 // 90 in RSC
    val SOUTHEAST_DOOR = 2429 // 88 in RSC
    val DOORS = intArrayOf(NORTH_DOOR, HELLHOUND_DOOR, SOUTHWEST_DOOR, SOUTHEAST_DOOR)

    override fun defineListeners() {
        on(LEVERS, SCENERY, "pull") { player, node ->
            val baseId = if(node.id % 2 == 0) { node.id - 1 } else { node.id }
            if(player.questRepository.getQuest("Family Crest").getStage(player) == 0) {
                player.sendMessage("Nothing interesting happens.")
            }
            val old = player.getAttribute("family-crest:witchaven-lever:${baseId}", false)
            player.setAttribute("family-crest:witchaven-lever:${baseId}", !old)
            val direction = if(old) { "down" } else { "up" }
            player.sendMessage("You pull the lever ${direction}.")
            player.sendMessage("You hear a clunk.")
            player.animate(if(old) { DOWN_ANIMATION } else { UP_ANIMATION })
            val downLever = (node as Scenery).transform(baseId)
            val upLever = (node as Scenery).transform(baseId + 1)
            player.debug("lever: ${downLever.id} ${upLever.id}")
            val buffer = UpdateAreaPosition.getChunkUpdateBuffer(player, RegionManager.getRegionChunk(player.location).currentBase)
            if(old) {
                // TODO: This doesn't seem to properly make the lever return to the "down" position visually.
                ClearScenery.write(buffer, upLever)
                ConstructScenery.write(buffer, downLever)
                //PacketRepository.send(ClearScenery::class.java, BuildSceneryContext(player, downLever))
            } else {
                ClearScenery.write(buffer, downLever)
                ConstructScenery.write(buffer, upLever)
                //PacketRepository.send(ConstructScenery::class.java, BuildSceneryContext(player, upLever))
            }
            player.session.write(buffer)
            return@on true
        }
        on(DOORS, SCENERY, "open") { player, node ->
            val northA = player.getAttribute("family-crest:witchaven-lever:${NORTH_LEVER_A}", false)
            val northB = player.getAttribute("family-crest:witchaven-lever:${NORTH_LEVER_B}", false)
            val south = player.getAttribute("family-crest:witchaven-lever:${SOUTH_LEVER}", false)
            val questComplete = player.questRepository.getQuest("Family Crest").getStage(player) >= 100
            // Authentic door formulae from https://gitlab.com/open-runescape-classic/core/-/blob/develop/server/plugins/com/openrsc/server/plugins/authentic/quests/members/FamilyCrest.java#L575-657
            val canPass = when(node.id) {
                NORTH_DOOR -> !northA && (south || northB)
                HELLHOUND_DOOR -> questComplete || (northA && northB && !south)
                SOUTHWEST_DOOR -> (northA && !south) || (northA && northB && !south)
                SOUTHEAST_DOOR -> (northA && south) || (northA && northB && south)
                else -> false
            } 
            if(canPass) {
                player.sendMessage("The door swings open. You go through the door.")
                doDoor(player, node as Scenery)
            } else {
                player.sendMessage("The door is locked.")
            }
            return@on true
        }
    }
}
