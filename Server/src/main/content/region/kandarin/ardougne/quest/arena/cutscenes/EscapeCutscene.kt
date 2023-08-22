package content.region.kandarin.ardougne.quest.arena.cutscenes

import content.region.kandarin.ardougne.quest.arena.FightArenaListeners.Companion.Jeremy
import content.region.kandarin.ardougne.quest.arena.npc.OgreNPC.Companion.spawnOgre
import core.api.*
import core.game.activity.Cutscene
import core.game.dialogue.FacialExpression
import core.game.global.action.DoorActionHandler
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import core.game.world.map.Location

class EscapeCutscene(player: Player) : Cutscene(player) {
    override fun setup() {
        setExit(location(2603, 3155, 0))
        if (player.settings.isRunToggled) {
            player.settings.toggleRun()
        }
    }

    override fun runStage(stage: Int) {
        when (stage) {

            0 -> {
                move(Jeremy, 56, 31)
                timedUpdate(3)
            }

            1 -> {
                player.faceLocation(location(56, 31, 0))
                animate(player, 2098)
                timedUpdate(4)
            }

            2 -> {
                move(player, 57, 32)
                timedUpdate(-1)
            }

            3 -> {
                DoorActionHandler.handleAutowalkDoor(Jeremy, getObject(57, 31, 0))
                player.faceLocation(location(57, 28, 0))
                timedUpdate(3)
            }

            4 -> {
                move(Jeremy, 57, 31)
                timedUpdate(1)
            }

            5 -> {
                sendChat(Jeremy, "I'll run ahead.")
                move(Jeremy, 57, 20)
                timedUpdate(5)
            }

            6 -> {
                teleport(Jeremy, 56, 31)
                loadRegion(10289)
                addNPC(JEREMYRESCUE, 41, 17, Direction.NORTH)
                addNPC(GENERAL, 45, 19, Direction.NORTH)
                addNPC(OGRE, 48, 30, Direction.NORTH)
                addNPC(JUSTIN, 41, 32, Direction.EAST)
                timedUpdate(-1)
            }

            7 -> {
                moveCamera(47, 20)
                rotateCamera(45, 15)
                teleport(player, 47, 15)
                timedUpdate(2)
            }

            8 -> {
                DoorActionHandler.handleAutowalkDoor(player, getObject(46, 16))
                moveCamera(41, 26, 300, 4)
                rotateCamera(45, 15, 300, 4)
                timedUpdate(-1)
            }

            9 -> {
                move(player, 44, 17)
                timedUpdate(2)
            }

            10 -> {
                move(player, 43, 18)
                timedUpdate(1)
            }

            11 -> {
                move(player, 43, 19)
                timedUpdate(3)
            }

            12 -> {
                player.faceLocation(getNPC(JEREMYRESCUE)!!.location)
                sendPlayerDialogue(player, "Jeremy, where's your father?",FacialExpression.HALF_ASKING)
                timedUpdate(2)
            }

            13 -> {
                getNPC(JEREMYRESCUE)!!.faceLocation(player.location)
                move(getNPC(JUSTIN)!!, 42, 32)
                sendNPCDialogue(player, JEREMYRESCUE, "Quick help him! That beast will kill him. He's too old to fight.")
                teleport(getNPC(OGRE)!!, 45, 30)
                timedUpdate(2)
            }

            14 -> {
                moveCamera(42, 26)
                rotateCamera(42, 26)
                timedUpdate(-1)
            }

            15 -> {
                move(getNPC(OGRE)!!, 43, 31)
                timedUpdate(4)
            }

            16 -> {
                getNPC(OGRE)!!.faceLocation(getNPC(JUSTIN)!!.location)
                animate(getNPC(OGRE)!!, 359, true)
                animate(getNPC(JUSTIN)!!, 404, true)
                timedUpdate(2)
            }

            17 -> {
                end {
                    spawnOgre(player)
                }
            }
        }
    }

    companion object {
        private const val GENERAL = 258
        private const val JEREMYRESCUE = 266
        private const val JUSTIN = 267
        private const val OGRE = 270
    }
}

