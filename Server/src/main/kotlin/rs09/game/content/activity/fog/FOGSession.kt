package rs09.game.content.activity.fog

import api.ContentAPI
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.tools.RandomFunction
import org.rs09.consts.Items

class FOGSession(val playerA: Player, val playerB: Player) {

    var hunter = playerA
    var hunted = playerB
    var isActive = true

    var winner: Player? = null
    var loser: Player? = null

    var playerAScore = 0
    var playerBScore = 0

    var round = 1

    var roundTimer = 0

    fun start(){
        playerA.setAttribute("fog-session",this)
        playerA.logoutListeners["fog-logout"] = { player -> ContentAPI.teleport(player, Location.create(1714, 5599, 0)); isActive = false }
        playerB.setAttribute("fog-session",this)
        playerB.logoutListeners["fog-logout"] = { player -> ContentAPI.teleport(player, Location.create(1714, 5599, 0)); isActive = false }

    }

    fun tick() {
        roundTimer++
        playerA.varpManager.get(FOGUtils.TIMER_VARP).setVarbit(4, roundTimer).send(playerA)
        playerB.varpManager.get(FOGUtils.TIMER_VARP).setVarbit(4, roundTimer).send(playerB)

        if(FOGUtils.carryingStone(hunted) && roundTimer % 5 == 0){
            FOGUtils.incrementHuntedScore(this, FOGUtils.getTickScore(hunted))
        } else if (!ContentAPI.inInventory(hunted, Items.STONE_OF_POWER_12845)){
            FOGUtils.showStoneNotice(hunted)
        }

        if((roundTimer == 1000 || getCurrentHuntedScore() >= 5000) && isActive){
            changeRounds()
        }
    }

    fun getCurrentHuntedScore(): Int {
        return when(round){
            1 -> playerBScore
            2 -> playerAScore
            else -> -1
        }
    }

    fun changeRounds(){
        if(round == 2){
            finishGame()
        } else {
            round = 2
            hunted = playerA
            hunter = playerB
            FOGUtils.updateHuntStatus(playerA, playerA == hunter)
            FOGUtils.updateHuntStatus(playerB, playerB == hunter)
            hunted.properties.teleportLocation = FOGUtils.getRandomStartLocation()
            hunter.properties.teleportLocation = FOGUtils.getRandomStartLocation()
        }
    }

    fun finishGame(){
        if(playerAScore > playerBScore){
            winner = playerA
            loser = playerB
        } else if (playerBScore > playerAScore){
            winner = playerB
            loser = playerA
        }

        if(winner == null) {
        //draw
            rewardLoss(playerA)
            rewardLoss(playerB)
        } else {
            rewardLoss(loser!!)
            rewardVictory(winner!!)
        }
    }

    fun getScore(player: Player): Int{
        return when(player){
            playerA -> playerAScore
            playerB -> playerBScore
            else -> 0
        }
    }

    fun rewardLoss(player: Player){
        ContentAPI.addItemOrDrop(player, Items.FIST_OF_GUTHIX_TOKEN_12852, RandomFunction.random(1,3))
        ContentAPI.sendMessage(player, "You lost and were given some tokens for your effort.")
    }

    fun rewardVictory(player: Player){
        var tokens = getScore(player) / 200.0
        tokens += player.skills.totalLevel / 100.0

        ContentAPI.addItemOrDrop(player, Items.FIST_OF_GUTHIX_TOKEN_12852, tokens.toInt())
        ContentAPI.sendMessage(player, "Guthix smiles upon your victory and rewards you.")
    }
}