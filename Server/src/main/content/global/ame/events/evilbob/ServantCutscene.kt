package content.global.ame.events.evilbob

import core.api.*
import core.game.activity.Cutscene
import core.game.node.entity.player.Player
import core.game.world.map.Direction

class ServantCutsceneN(player: Player) : Cutscene(player) {

    override fun setup() {
        setExit(player.location.transform(0, 0, 0))
        loadRegion(13642)
        addNPC(2479, 28, 41, Direction.SOUTH)      // Evil Bob
        addNPC(2481, 31, 41, Direction.SOUTH_WEST) // Servant
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                teleport(player, 30, 41)
                moveCamera(30, 37, 400, 255)
                rotateCamera(30, 50, 400, 255)
                openInterface(player, 186)
                timedUpdate(0)
            }
            1 -> {
                timedUpdate(0)
            }
            2 -> {  // Slow start
                moveCamera(30, 49, 400, 2)
                timedUpdate(2)
            }
            3 -> {  // Fast middle
                moveCamera(30, 44, 380, 4)
                timedUpdate(6)
            }
            4 -> {  // Slow end
                moveCamera(30, 45, 340, 2)
                timedUpdate(2)
            }
            5 -> {
                timedUpdate(2)
            }
            6 -> {
                closeInterface(player)
                end(fade = false) { player.locks.lockTeleport(1000000) }
            }
        }
    }
}

class ServantCutsceneS(player: Player) : Cutscene(player) {

    override fun setup() {
        setExit(player.location.transform(0, 0, 0))
        loadRegion(13642)
        addNPC(2479, 28, 41, Direction.SOUTH)      // Evil Bob
        addNPC(2481, 31, 41, Direction.SOUTH_WEST) // Servant
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                teleport(player, 30, 41)
                moveCamera(31, 46, 365, 255)
                rotateCamera(29, 30, 365, 255)
                openInterface(player, 186)
                timedUpdate(0)
            }
            1 -> {
                timedUpdate(0)
            }
            2 -> {
                moveCamera(31, 43, 480, 2)
                timedUpdate(2)
            }
            3 -> {
                moveCamera(31, 37, 455, 4)
                timedUpdate(8)
            }
            4 -> {
                moveCamera(31, 36, 395, 2)
                timedUpdate(2)
            }
            5 -> {
                timedUpdate(3)
            }
            6 -> {
                closeInterface(player)
                end(fade = false) { player.locks.lockTeleport(1000000) }
            }
        }
    }
}

class ServantCutsceneE(player: Player) : Cutscene(player) {

    override fun setup() {
        setExit(player.location.transform(0, 0, 0))
        loadRegion(13642)
        addNPC(2479, 28, 41, Direction.SOUTH)      // Evil Bob
        addNPC(2481, 31, 41, Direction.SOUTH_WEST) // Servant
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                teleport(player, 30, 41)
                moveCamera(25, 41, 440, 255)
                rotateCamera(42, 41, 440, 255)
                openInterface(player, 186)
                timedUpdate(0)
            }
            1 -> {
                timedUpdate(0)
            }
            2 -> {  // Slow start
                moveCamera(28, 41, 500, 3)
                timedUpdate(2)
            }
            3 -> {  // Fast middle
                moveCamera(34, 41, 390, 5)
                timedUpdate(6)
            }
            4 -> {  // Slow end
                moveCamera(36, 41, 340, 2)
                timedUpdate(2)
            }
            5 -> {
                timedUpdate(4)
            }
            6 -> {
                closeInterface(player)
                end(fade = false) { player.locks.lockTeleport(1000000) }
            }
        }
    }
}

class ServantCutsceneW(player: Player) : Cutscene(player) {

    override fun setup() {
        setExit(player.location.transform(0, 0, 0))
        loadRegion(13642)
        addNPC(2479, 28, 41, Direction.SOUTH)      // Evil Bob
        addNPC(2481, 31, 41, Direction.SOUTH_WEST) // Servant
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                teleport(player, 30, 41)
                moveCamera(34, 41, 325, 255)
                rotateCamera(16, 40, 300, 255)
                openInterface(player, 186)
                timedUpdate(0)
            }
            1 -> {
                timedUpdate(0)
            }
            2 -> {  // Slow start
                moveCamera(31, 41, 440, 3)
                timedUpdate(2)
            }
            3 -> {  // Fast middle
                moveCamera(24, 41, 330, 5)
                timedUpdate(7)
            }
            4 -> {  // Slow end
                moveCamera(23, 41, 300, 2)
                timedUpdate(2)
            }
            5 -> {
                timedUpdate(3)
            }
            6 -> {
                closeInterface(player)
                end(fade = false) { player.locks.lockTeleport(1000000) }
            }
        }
    }
}