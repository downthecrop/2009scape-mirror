package content.global.skill.summoning.pet

import core.game.node.entity.player.Player
import core.game.node.entity.state.PlayerState
import core.game.node.entity.state.State
import core.game.system.task.Pulse
import org.json.simple.JSONObject
import core.api.*

@PlayerState("incubator")
class IncubatorState(player: Player? = null) : State(player) {
    var egg: IncubatorEgg? = null
    var ticksLeft = 0

    override fun newInstance(player: Player?): State {
        return IncubatorState(player)
    }

    override fun save(root: JSONObject) {
        if(ticksLeft <= 0) return

        val data = JSONObject()
        data.put("eggOrdinal",(egg?.ordinal ?: 0).toString())
        data.put("ticksLeft",ticksLeft.toString())
        root.put("eggdata",data)
    }

    override fun parse(_data: JSONObject) {
        if(_data.containsKey("eggdata")){
            val data = _data["eggdata"] as JSONObject
            egg = IncubatorEgg.values()[data["eggOrdinal"].toString().toInt()]
            ticksLeft = data["ticksLeft"].toString().toInt()
        }
    }

    override fun createPulse() {
        player ?: return
        egg ?: return
        setVarbit(player, 4277, 1)
        pulse = object : Pulse(){
            override fun pulse(): Boolean {
                if(ticksLeft-- <= 0){
                    player.setAttribute("/save:inc", egg!!.ordinal)
                    player.sendMessage("Your " + egg!!.product.name.toLowerCase() + " has finished hatching.")
                    pulse = null
                    return true
                }
                return false
            }
        }
    }

}
