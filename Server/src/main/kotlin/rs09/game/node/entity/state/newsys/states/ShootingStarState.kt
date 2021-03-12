package rs09.game.node.entity.state.newsys.states

import core.game.node.entity.player.Player
import rs09.game.node.entity.state.newsys.PlayerState
import rs09.game.node.entity.state.newsys.State
import core.game.system.task.Pulse
import rs09.tools.ticksToSeconds
import org.json.simple.JSONObject

@PlayerState("shooting-star")
class ShootingStarState(player: Player? = null) : State(player) {
    var ticksLeft = 1500

    override fun save(root: JSONObject) {
        root["ticksLeft"] = ticksLeft
    }

    override fun parse(_data: JSONObject) {
        if(_data.containsKey("ticksLeft")){
            ticksLeft = _data["ticksLeft"].toString().toInt()
        }
    }

    override fun newInstance(player: Player?): State {
        return ShootingStarState(player)
    }

    override fun createPulse() {
        player ?: return
        if(ticksLeft <= 0) return
        pulse = object : Pulse(){
            override fun pulse(): Boolean {
                val minutes = ticksToSeconds(ticksLeft) / 60.0
                if(minutes % 5.0 == 0.0){
                    player.sendMessage("<col=f0f095>You have $minutes minutes of your mining bonus left</col>")
                }
                if(ticksLeft-- <= 0){
                    player.sendMessage("<col=FF0000>Your mining bonus has run out!</col>")
                    pulse = null
                    return true
                }
                return false
            }
        }
    }

}