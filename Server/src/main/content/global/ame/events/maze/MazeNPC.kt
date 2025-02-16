package content.global.ame.events.maze

import content.global.ame.RandomEventNPC
import content.global.ame.kidnapPlayer
import core.api.*
import core.api.utils.WeightBasedTable
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.link.TeleportManager
import core.game.system.timer.impl.AntiMacro
import core.game.world.map.Location
import core.game.world.map.build.DynamicRegion
import core.game.world.update.flag.context.Graphics
import org.rs09.consts.NPCs
import org.rs09.consts.Sounds

class MazeNPC(var type: String = "", override var loot: WeightBasedTable? = null) : RandomEventNPC(NPCs.MYSTERIOUS_OLD_MAN_410) {

    override fun init() {
        super.init()
        sendChat("Aha, you'll do ${player.username}!")
        face(player)
        queueScript(player, 4, QueueStrength.SOFT) { stage: Int ->
            when (stage) {
                0 -> {
                    lock(player, 6)
                    sendGraphics(Graphics(1576, 0, 0), player.location)
                    animate(player,8939)
                    playAudio(player, Sounds.TELEPORT_ALL_200)
                    return@queueScript delayScript(player, 3)
                }
                1 -> {
                    MazeInterface.initMaze(player)
                    // Note: This event is NOT instanced:
                    // Sources:
                    // https://youtu.be/2gpzn9oNdy0 (2007)
                    // https://youtu.be/Tni1HURgnxg (2008)
                    // https://youtu.be/igdwDZOv9LU (2008)
                    // https://youtu.be/0oBCkLArUmc (2011 - even with personal Mysterious Old Man) - "Sorry, this is not the old man you are looking for."
                    // https://youtu.be/FMuKZm-Ikgs (2011)
                    // val region = DynamicRegion.create(11591)
                    kidnapPlayer(player, MazeInterface.STARTING_POINTS.random(), TeleportManager.TeleportType.INSTANT) // 10 random spots
                    AntiMacro.terminateEventNpc(player)
                    sendGraphics(Graphics(1577, 0, 0), player.location)
                    animate(player,8941)
                    removeAttribute(player, MazeInterface.MAZE_ATTRIBUTE_CHESTS_OPEN)
                    return@queueScript stopExecuting(player)
                }
                else -> return@queueScript stopExecuting(player)
            }
        }
    }

    override fun talkTo(npc: NPC) {
        // Do nothing.
    }
}