package core.game.node.entity.state.newsys.states

import core.game.node.entity.player.Player
import core.game.node.entity.state.newsys.PlayerState
import core.game.node.entity.state.newsys.State
import core.game.system.task.Pulse
import org.json.simple.JSONObject

@PlayerState("godcharge")
class GodspellChargedState(player: Player? = null) : State(player) {
    var TICKS = 700

    override fun save(root: JSONObject) {
        if(TICKS > 0){
            root.put("ticksleft",TICKS)
        }
    }

    override fun parse(_data: JSONObject) {
        if(_data.containsKey("ticksleft")){
            TICKS = _data["ticksleft"].toString().toInt()
        }
    }

    override fun newInstance(player: Player?): State {
        return GodspellChargedState(player)
    }

    override fun createPulse() {
        player ?: return
        if(TICKS <= 0) return
        pulse = object : Pulse(TICKS){
            override fun pulse(): Boolean {
                player.sendMessage("Your magical charge fades away.")
                pulse = null
                return true
            }
        }
    }

}