package rs09.game.node.entity.state.newsys.states

import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.tools.RandomFunction
import org.json.simple.JSONObject
import rs09.game.node.entity.state.newsys.PlayerState
import rs09.game.node.entity.state.newsys.State
import rs09.game.world.GameWorld

@PlayerState("disease")
class DiseasedState(player: Player? = null) : State(player){
    var hitsLeft = 25

    override fun save(root: JSONObject) {
        if(hitsLeft > 0){
            root.put("hitsLeft",hitsLeft)
        }
    }

    override fun parse(_data: JSONObject) {
        if(_data.containsKey("hitsLeft")){
            hitsLeft = _data["hitsLeft"].toString().toInt()
        }
    }

    override fun newInstance(player: Player?): State {
        return DiseasedState(player)
    }

    override fun createPulse() {
        player ?: return
        if(player.getAttribute("immunity:disease",0) > GameWorld.ticks){
            return
        }
        if(hitsLeft <= 0) return
        player.sendMessage("You have been diseased!")
        pulse = object : Pulse(30){
            override fun pulse(): Boolean {
                val damage = RandomFunction.random(1,5)
                player.impactHandler.manualHit(player,damage,ImpactHandler.HitsplatType.DISEASE)
                var skillId = RandomFunction.random(24)
                if(skillId == 3) skillId--
                player.skills.updateLevel(skillId,-damage,0)
                hitsLeft--
                if(hitsLeft <= 0) player.sendMessage("The disease has wore off.")
                return hitsLeft <= 0
            }
        }
    }

}