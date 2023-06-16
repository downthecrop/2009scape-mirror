package content.global.skill.summoning.pet

import core.game.node.entity.player.Player
import core.game.node.entity.state.PlayerState
import core.game.node.entity.state.State
import core.game.system.task.Pulse
import org.json.simple.JSONObject
import core.api.*
import core.tools.*

@PlayerState("incubator")
class IncubatorState(player: Player? = null) : State(player) {
    var egg: IncubatorEgg? = null
    var completionTimeMs = 0L

    fun setTicksLeft (ticks: Int) {
        completionTimeMs = System.currentTimeMillis() + (ticksToSeconds(ticks) * 1000)
    }

    override fun newInstance(player: Player?): State {
        return IncubatorState(player)
    }

    override fun save(root: JSONObject) {
        if(pulse == null) return

        val data = JSONObject()
        data.put("eggOrdinal",(egg?.ordinal ?: 0).toString())
        data.put("endTime", completionTimeMs.toString())
        root.put("eggdata",data)
    }

    override fun parse(_data: JSONObject) {
        if(_data.containsKey("eggdata")){
            val data = _data["eggdata"] as JSONObject
            egg = IncubatorEgg.values()[data["eggOrdinal"].toString().toInt()]
            
            if (data.containsKey("ticksLeft"))
                completionTimeMs = System.currentTimeMillis() + ticksToSeconds(data["ticksLeft"].toString().toInt() * 1000)
            else
                completionTimeMs = data["endTime"].toString().toLong()
        }
    }

    override fun createPulse() {
        player ?: return
        egg ?: return
        setVarbit(player, 4277, 1)
        pulse = object : Pulse(){
            override fun pulse(): Boolean {
                if(System.currentTimeMillis() >= completionTimeMs){
                    player.setAttribute(ATTR_INCUBATOR_PRODUCT, egg!!.ordinal)
                    player.sendMessage("Your " + egg!!.product.name.toLowerCase() + " has finished hatching.")
                    pulse = null
                    return true
                }
                return false
            }
        }
    }
    
    companion object {
        const val ATTR_INCUBATOR_PRODUCT = "/save:incubator:egg-product"
    }
}
