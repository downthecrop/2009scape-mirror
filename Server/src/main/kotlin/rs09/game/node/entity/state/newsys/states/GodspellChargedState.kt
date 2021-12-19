package rs09.game.node.entity.state.newsys.states

import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import org.json.simple.JSONObject
import rs09.game.node.entity.state.newsys.PlayerState
import rs09.game.node.entity.state.newsys.State
import rs09.game.world.World;

@PlayerState("godcharge")
class GodspellChargedState(player: Player? = null) : State(player) {
    val DURATION = 700
    var startTick: Int = 0

    override fun save(root: JSONObject) {
        root.put("ticks_elapsed", World.ticks - startTick)
    }

    override fun parse(_data: JSONObject) {
        if(_data.containsKey("ticks_elapsed")){
            startTick = World.ticks - _data["ticks_elapsed"].toString().toInt()
        }
    }

    override fun newInstance(player: Player?): State {
        var ret = GodspellChargedState(player)
        ret.startTick = World.ticks
        return ret
    }

    override fun createPulse() {
        player ?: return
        if(World.ticks - startTick >= DURATION) return
        pulse = object : Pulse(DURATION) {
            override fun pulse(): Boolean {
                player.sendMessage("Your magical charge fades away.")
                player.clearState("godcharge")
                pulse = null
                return true
            }
        }
    }

}
