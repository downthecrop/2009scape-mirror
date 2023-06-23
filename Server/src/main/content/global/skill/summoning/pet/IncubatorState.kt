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
    }

    override fun parse(_data: JSONObject) {
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
