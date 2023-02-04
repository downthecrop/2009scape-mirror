package content.region.kandarin.ardougne.quest.fightarena.cutscenes

import content.region.kandarin.ardougne.quest.fightarena.FightArenaListeners.Companion.Jeremy
import content.region.kandarin.ardougne.quest.fightarena.npcs.enemies.KhazardOgreNPC.Companion.spawnOgre
import core.api.*
import core.game.activity.Cutscene
import core.game.dialogue.FacialExpression
import core.game.global.action.DoorActionHandler
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import core.game.world.map.Location
import core.net.packet.PacketRepository
import core.net.packet.context.MinimapStateContext
import core.net.packet.out.MinimapState

class RescueCutscene(player: Player) : Cutscene(player) {

    // Jeremy rescue & first fight with Ogre.
    // Source: https://youtu.be/-wV5dIyM0YM?t=182
    override fun setup() {
        setExit(location(2603, 3155, 0))
        if (player.settings.isRunToggled) {
            player.settings.toggleRun()
        }
    }

    override fun runStage(stage: Int) {
        when (stage) {

            // ----------------   Prologue   ----------------

            0 -> {
                PacketRepository.send(MinimapState::class.java, MinimapStateContext(player, 1))
                move(Jeremy, 56, 31)
                timedUpdate(2)
            }

            1 -> {
                player.faceLocation(location(2616, 3167, 0))
                animate(player, 2098)
                timedUpdate(1)
            }

            2 -> {
                move(player, 57, 32)
                timedUpdate(-1)
            }

            3 -> {
                DoorActionHandler.handleAutowalkDoor(Jeremy, getScenery(2617, 3167, 0))
                player.faceLocation(location(2617, 3164, 0))
                timedUpdate(2)
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

            // ----------------   Cutscene   ----------------

            6 -> {
                loadRegion(10289)
                addNPC(JEREMYRESCUE, 41, 17, Direction.NORTH)
                addNPC(GENERAL, 45, 19, Direction.NORTH)
                addNPC(OGRE, 48, 30, Direction.NORTH)
                addNPC(JUSTIN, 41, 32, Direction.EAST)
                timedUpdate(1)
            }

            7 -> {
                moveCamera(47, 20)
                rotateCamera(45, 15)
                teleport(player, 47, 15)
                Jeremy.teleport(Location.create(2616, 3167, 0))
                timedUpdate(2)
            }

            8 -> {
                DoorActionHandler.handleAutowalkDoor(player, getScenery(174, 16, 0))
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
                timedUpdate(2)
            }

            11 -> {
                move(player, 43, 19)
                timedUpdate(3)
            }

            // ---------------- Baldur's Gate ----------------

            12 -> {
                player.faceLocation(getNPC(JEREMYRESCUE)!!.location)
                sendPlayerDialogue(player, "Jeremy, where's your father?", FacialExpression.NEUTRAL)
                timedUpdate(2)
            }

            13 -> {
                getNPC(JEREMYRESCUE)!!.faceLocation(player.location)
                sendNPCDialogue(player, JEREMYRESCUE, "Quick help him! That beast will kill him. He's too old to fight.")
                teleport(getNPC(OGRE)!!, 45, 30)
                move(getNPC(JUSTIN)!!, 42, 32)
                timedUpdate(1)
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

            // ----------------  End & Spawn Ogre  ----------------

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

