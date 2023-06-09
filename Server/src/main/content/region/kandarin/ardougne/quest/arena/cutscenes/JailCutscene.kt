package content.region.kandarin.ardougne.quest.arena.cutscenes

import content.region.kandarin.ardougne.quest.arena.dialogue.HengradDialogue
import core.api.*
import core.game.activity.Cutscene
import core.game.global.action.DoorActionHandler
import core.game.node.entity.player.Player
import core.game.world.map.Direction

class JailCutscene(player: Player) : Cutscene(player) {
    override fun setup() {
        setExit(location(2600, 3142, 0))
        if (player.settings.isRunToggled) {
            player.settings.toggleRun()
        }
        loadRegion(10289)
    }

    override fun runStage(stage: Int) {
        when (stage) {

            0 -> {
                addNPC(KHAZARD_GUARD, 48, 5, Direction.WEST)
                teleport(player, 46, 5)
                timedUpdate(1)
            }

            1 -> {
                moveCamera(43, 4)
                rotateCamera(41, 5)
                timedUpdate(1)
            }

            2 -> {
                move(player, 45, 5)
                timedUpdate(1)
            }

            3 -> {
                move(player, 40, 5)
                move(getNPC(KHAZARD_GUARD)!!, 41, 5)
                timedUpdate(6)
            }

            4 -> {
                animate(getNPC(KHAZARD_GUARD)!!, 2098)
                DoorActionHandler.handleAutowalkDoor(player, getObject(40, 5))
                timedUpdate(5)
            }

            5 -> {
                move(player, 40, 6)
                face(player, getNPC(KHAZARD_GUARD)!!)
                move(getNPC(KHAZARD_GUARD)!!, 40, 5)
                timedUpdate(2)
            }

            6 -> {
                face(getNPC(KHAZARD_GUARD)!!, player)
                sendNPCDialogue(player, KHAZARD_GUARD, "The General seems to have taken a liking to you. He'd normally kill imposters like you without a second thought.")
                timedUpdate(3)
            }

            7 -> {
                end {
                    openDialogue(player, HengradDialogue())
                }
            }
        }
    }

    companion object {
        private const val KHAZARD_GUARD = 257
    }
}

