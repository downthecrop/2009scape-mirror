package content.global.skill.farming

import core.api.*
import core.Util.clamp
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import core.game.node.entity.state.PlayerState
import core.game.node.entity.state.State
import core.tools.SystemLogger
import java.util.concurrent.TimeUnit
import content.global.skill.farming.timers.*

@PlayerState("farming")
/**
 * Kept around solely for the purpose of porting save data from this old system to the new one.
 * //TODO REMOVE BY END OF 2023
**/
class FarmingState(player: Player? = null) : State(player) {
    override fun save(root: JSONObject) {}
    override fun parse(_data: JSONObject) {
        player ?: return
        if(_data.containsKey("farming-bins")){
            _data["bins"] = _data["farming-bins"]
            val timer = getOrStartTimer <Compost> (player)
            timer.parse (_data, player)
        }
        if(_data.containsKey("farming-patches")){
            _data["patches"] = _data["farming-patches"]
            val timer = getOrStartTimer <CropGrowth> (player)
            timer.parse(_data, player) 
        }
    }

    override fun newInstance(player: Player?): State {
        return FarmingState(player)
    }

    override fun createPulse() {}
}
