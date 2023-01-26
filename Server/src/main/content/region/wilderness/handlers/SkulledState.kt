package content.region.wilderness.handlers

import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import org.json.simple.JSONObject
import core.game.node.entity.state.PlayerState
import core.game.node.entity.state.State

@PlayerState("skull")
class SkulledState(player: Player? = null) : State(player) {
    var ticksLeft = 2000

    override fun save(root: JSONObject) {
        root.put("ticksLeft",ticksLeft)
    }

    override fun parse(_data: JSONObject) {
        if(_data.containsKey("ticksLeft")){
            ticksLeft = _data["ticksLeft"].toString().toInt()
        }
    }

    override fun newInstance(player: Player?): State {
        return SkulledState(player)
    }

    override fun createPulse() {
        player ?: return
        if(ticksLeft <= 0) return
        player.skullManager.setSkullIcon(0)
        player.skullManager.isSkulled = true
        pulse = object : Pulse(){
            override fun pulse(): Boolean {
                ticksLeft--
                if(ticksLeft <= 0) {
                    player.skullManager.reset()
                    pulse = null
                    return true
                }
                return false
            }
        }
    }

}