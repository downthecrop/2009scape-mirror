package content.region.kandarin.ardougne.quest.arena

import core.api.animate
import core.api.getScenery
import core.api.location
import core.api.openDialogue
import core.game.activity.Cutscene
import core.game.component.Component
import core.game.dialogue.FacialExpression
import core.game.global.action.DoorActionHandler
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import core.net.packet.PacketRepository
import core.net.packet.context.MinimapStateContext
import core.net.packet.out.MinimapState
import org.rs09.consts.Components

class HengradCutscene(player: Player) : Cutscene(player) {
    override fun setup() {
        setExit(location(2600, 3142, 0))
        if (player.settings.isRunToggled) {
            player.settings.toggleRun()
        }
    }

    override fun runStage(stage: Int) {
        when (stage) {

            0 -> {
                PacketRepository.send(MinimapState::class.java, MinimapStateContext(player, 1))
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
                timedUpdate(3)
            }

            3 -> {
                move(player, 40, 5)
                move(getNPC(KHAZARD_GUARD)!!, 41, 5)
                timedUpdate(8)
            }

            4 -> {
                animate(getNPC(KHAZARD_GUARD)!!, 1060, true)
                timedUpdate(2)
            }

            5 -> {
                DoorActionHandler.handleAutowalkDoor(player, getScenery(2600, 3141, 0))
                timedUpdate(2)
            }

            6 -> {
                move(player, 40, 6)
                move(getNPC(KHAZARD_GUARD)!!, 40, 5)
                timedUpdate(3)
            }

            7 -> {
                dialogueUpdate(257, FacialExpression.FRIENDLY, "The General seems to have taken a liking to you. He'd normally kill imposters like you without a second thought.").also { getNPC(KHAZARD_GUARD)!!.faceLocation(player.location) }
                player.faceLocation(getNPC(KHAZARD_GUARD)!!.location)
                timedUpdate(3)
            }

            8 -> {
                loadRegion(10289)
                teleport(player, 40, 6)
                timedUpdate(6)
            }

            9 -> {
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

