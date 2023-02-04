package content.region.kandarin.ardougne.quest.fightarena.cutscenes

import content.region.kandarin.ardougne.quest.fightarena.npcs.enemies.BouncerNPC.Companion.spawnBouncer
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
    }

    override fun runStage(stage: Int) {
        when (stage) {

            // ------------------  Settings  ------------------

            0 -> {
                PacketRepository.send(MinimapState::class.java, MinimapStateContext(player, 1))
                loadRegion(10289)
                addNPC(BOUNCER, 47, 26, Direction.WEST)
                teleport(player, 43, 19)
                timedUpdate(1)
            }

            // ----------------  Bouncer zoom  ----------------

            1 -> {
                moveCamera(43, 27)
                moveCamera(42, 27)
                rotateCamera(46, 27)
                replaceScenery(Scenery(77, location(174, 26, 0)), PRISON_DOOR_79, 4)
                replaceScenery(Scenery(78, location(174, 27, 0)), PRISON_DOOR_80, 4)
                timedUpdate(1)
            }

            2 -> {
                DoorActionHandler.handleAutowalkDoor(getNPC(BOUNCER)!!, getScenery(174, 27, 0))
                DoorActionHandler.handleAutowalkDoor(getNPC(BOUNCER)!!, getScenery(174, 26, 0))
                timedUpdate(1)
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

            // ---------------  End & Spawn bouncer  ---------------

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
