package content.global.ame.events.maze

import content.global.ame.RandomEventNPC
import content.global.ame.kidnapPlayer
import core.api.*
import core.api.utils.WeightBasedTable
import core.game.node.entity.npc.NPC
import org.rs09.consts.NPCs

class MazeNPC(var type: String = "", override var loot: WeightBasedTable? = null) : RandomEventNPC(NPCs.MYSTERIOUS_OLD_MAN_410) {
    override fun init() {
        super.init()
        sendChat("Aha, you'll do ${player.username}!")
        face(player)
        // Note: This event is NOT instanced:
        // Sources:
        // https://youtu.be/2gpzn9oNdy0 (2007)
        // https://youtu.be/Tni1HURgnxg (2008)
        // https://youtu.be/igdwDZOv9LU (2008)
        // https://youtu.be/0oBCkLArUmc (2011 - even with personal Mysterious Old Man) - "Sorry, this is not the old man you are looking for."
        // https://youtu.be/FMuKZm-Ikgs (2011)
        // val region = DynamicRegion.create(11591)
        kidnapPlayer(this, player, MazeInterface.STARTING_POINTS.random()) { player, _ ->
            MazeInterface.initMaze(player)
            removeAttribute(player, MazeInterface.MAZE_ATTRIBUTE_CHESTS_OPEN)
        }
    }

    override fun talkTo(npc: NPC) {
        sendMessage(player, "He isn't interested in talking to you.")
    }
}