package content.region.kandarin.ardougne.quest.arena

import content.region.kandarin.ardougne.quest.arena.BouncerNPC.Companion.spawnBouncer
import core.api.getScenery
import core.api.location
import core.api.replaceScenery
import core.game.activity.Cutscene
import core.game.global.action.DoorActionHandler
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.world.map.Direction
import core.net.packet.PacketRepository
import core.net.packet.context.MinimapStateContext
import core.net.packet.out.MinimapState
import org.rs09.consts.Scenery.PRISON_DOOR_79
import org.rs09.consts.Scenery.PRISON_DOOR_80

class BouncerCutscene(player: Player) : Cutscene(player) {

    override fun setup() {
        setExit(player.location.transform(0, 0, 0))
        if (player.settings.isRunToggled) {
            player.settings.toggleRun()
        }
        loadRegion(10289)
        addNPC(BOUNCER, 47, 26, Direction.WEST)
    }

    override fun runStage(stage: Int) {
        when (stage) {

            0 -> {
                teleport(player, 43, 19)
                timedUpdate(1)
            }

            1 -> {
                moveCamera(43, 27)
                moveCamera(42, 27)
                rotateCamera(46, 27)
                timedUpdate(2)
            }

            2 -> {
                DoorActionHandler.handleAutowalkDoor(getNPC(BOUNCER)!!, getObject(46, 26))
                timedUpdate(2)
            }

            3 -> {
                move(getNPC(BOUNCER)!!, 45, 26)
                moveCamera(39, 27, 300, 1)
                rotateCamera(44, 27, 300, 1)
                timedUpdate(1)
            }

            4 -> {
                move(getNPC(BOUNCER)!!, 44, 26)
                timedUpdate(1)
            }

            5 -> {
                move(getNPC(BOUNCER)!!, 42, 26)
                timedUpdate(-1)
            }

            6 -> {
                end {
                    spawnBouncer(player)
                }
            }
        }
    }

    companion object {
        const val BOUNCER = 269
    }
}
