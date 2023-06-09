package content.region.kandarin.ardougne.quest.arena.cutscenes

import content.region.kandarin.ardougne.quest.arena.npc.ScorpionNPC.Companion.spawnScorpion
import core.api.animate
import core.api.location
import core.api.sendChat
import core.game.activity.Cutscene
import core.game.global.action.DoorActionHandler
import core.game.node.entity.player.Player
import core.game.world.map.Direction

class SecondFightCutscene(player: Player) : Cutscene(player) {
    override fun setup() {
        setExit(location(2603, 3155, 0))
        if (player.settings.isRunToggled) {
            player.settings.toggleRun()
        }
        addNPC(GUARD, 36, 5, Direction.EAST)
        addNPC(SCORPION, 47, 23, Direction.WEST)

    }

    override fun runStage(stage: Int) {
        when (stage) {

            0 -> {
                move(getNPC(GUARD)!!, 39, 5)
                sendChat(getNPC(GUARD)!!, "Right you, move it.")
                timedUpdate(4)
            }

            1 -> {
                move(player, 40, 6)
                timedUpdate(1)
            }

            2 -> {
                animate(getNPC(GUARD)!!, 805)
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
                DoorActionHandler.handleAutowalkDoor(player, getObject(46, 16))
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
                dialogueUpdate("From above you hear a voice..... 'Ladies and gentlemen! Let today's first fight between the outsider and everyone's favourite scorpion commence.'")
                timedUpdate(4)
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
                timedUpdate(1)
            }

            16 -> {
                DoorActionHandler.handleAutowalkDoor(getNPC(SCORPION)!!, getObject(46, 24))
                DoorActionHandler.handleAutowalkDoor(getNPC(SCORPION)!!, getObject(46, 23))
                timedUpdate(1)
            }

            17 -> {
                move(getNPC(SCORPION)!!, 45, 20)
                moveCamera(37, 23, 300, 5)
                rotateCamera(43, 23, 300, 5)
                timedUpdate(2)
            }

            18 -> {
                move(getNPC(SCORPION)!!, 44, 20)
                timedUpdate(1)
            }

            19 -> {
                move(getNPC(SCORPION)!!, 42, 20)
                timedUpdate(3)
            }

            20 -> {
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
