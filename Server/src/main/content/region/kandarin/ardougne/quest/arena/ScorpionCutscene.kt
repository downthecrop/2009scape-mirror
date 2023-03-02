package content.region.kandarin.ardougne.quest.arena

import content.region.kandarin.ardougne.quest.arena.KhazardScorpionNPC.Companion.spawnScorpion
import core.api.*
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

class ScorpionCutscene(player: Player) : Cutscene(player) {
    override fun setup() {
        setExit(location(2603, 3155, 0))
        if (player.settings.isRunToggled) {
            player.settings.toggleRun()
        }
    }

    override fun runStage(stage: Int) {
        when (stage) {

            0 -> {
                PacketRepository.send(MinimapState::class.java, MinimapStateContext(player, 1))
                addNPC(GUARD, 36, 5, Direction.EAST)
                timedUpdate(3)
            }

            1 -> {
                move(getNPC(GUARD)!!, 39, 5)
                sendChat(getNPC(GUARD)!!, "Right you, move it.")
                move(player, 40, 6)
                timedUpdate(5)
            }

            2 -> {
                animate(getNPC(GUARD)!!, 805, true)
                timedUpdate(2)
            }

            3 -> {
                DoorActionHandler.handleAutowalkDoor(player, getObject(40, 5))
                timedUpdate(2)
            }

            4 -> {
                move(player, 42, 5)
                timedUpdate(2)
            }

            5 -> {
                move(player, 43, 6)
                move(getNPC(GUARD)!!, 43, 6)
                timedUpdate(-1)
            }

            6 -> {
                move(player, 47, 15)
                timedUpdate(1)
            }

            7 -> {
                move(getNPC(GUARD)!!, 43, 6)
                timedUpdate(2)
            }

            8 -> {
                move(getNPC(GUARD)!!, 48, 7)
                timedUpdate(6)
            }

            9 -> {
                move(getNPC(GUARD)!!, 48, 14)
                timedUpdate(8)
            }

            10 -> {
                sendChat(getNPC(GUARD)!!, "Get out! there.")
                DoorActionHandler.handleAutowalkDoor(player, getScenery(2606, 3152, 0))
                move(getNPC(GUARD)!!, 47, 15)
                timedUpdate(1)
            }

            11 -> {
                move(player, 43, 19)
                timedUpdate(2)
            }

            12 -> {
                move(getNPC(GUARD)!!, 48, 14)
                timedUpdate(1)
            }

            13 -> {
                sendDialogue(player, "From above you hear a voice..... 'Ladies and gentlemen! Let today's first fight between the outsider and everyone's favourite scorpion commence.'")
                timedUpdate(3)
            }

            14 -> {
                loadRegion(10289)
                addNPC(SCORPION, 47, 23, Direction.WEST)
                teleport(player, 43, 19)
                timedUpdate(1)
            }

            15 -> {
                moveCamera(42, 23)
                rotateCamera(46, 23)
                replaceScenery(Scenery(77, location(174, 23, 0)), PRISON_DOOR_79, 4)
                replaceScenery(Scenery(78, location(174, 24, 0)), PRISON_DOOR_80, 4)
                timedUpdate(1)
            }

            16 -> {
                DoorActionHandler.handleAutowalkDoor(getNPC(SCORPION)!!, getScenery(174, 24, 0))
                DoorActionHandler.handleAutowalkDoor(getNPC(SCORPION)!!, getScenery(174, 23, 0))
                timedUpdate(1)
            }

            17 -> {
                move(getNPC(SCORPION)!!, 45, 20)
                moveCamera(40, 23, 300, 1)
                rotateCamera(43, 23, 300, 1)
                timedUpdate(1)
            }

            18 -> {
                end {
                    spawnScorpion(player)
                }
            }
        }
    }

    companion object {
        const val GUARD = 257
        const val SCORPION = 271
    }
}
