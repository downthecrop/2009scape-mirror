package rs09.game.node.entity.skill.farming

import core.cache.def.impl.SceneryDefinition
import core.cache.def.impl.VarbitDefinition
import core.game.node.scenery.Scenery
import core.game.node.entity.player.Player
import rs09.game.node.entity.state.newsys.states.FarmingState

enum class CompostBins(val varpIndex: Int, val varpOffest: Int) {
    FALADOR_COMPOST(511,0),
    CATHERBY_COMPOST(511,8),
    PORT_PHAS_COMPOST(511,16),
    ARDOUGNE_COMPOST(511,24);

    companion object {
        @JvmField
        val bins = values().map { (it.varpIndex shl it.varpOffest) to it }.toMap()

        @JvmStatic
        fun forObject(obj: Scenery): CompostBins?{
            return forObjectID(obj.id)
        }

        @JvmStatic
        fun forObjectID(id: Int): CompostBins?{
            val objDef = SceneryDefinition.forId(id)
            val def = VarbitDefinition.forObjectID(objDef.varbitID)
            return bins[def.configId shl def.bitShift]
        }
    }

    fun getBinForPlayer(player: Player) : CompostBin {
        var state: FarmingState? = player.states.get("farming") as FarmingState?
        return if(state == null){
            state = player.registerState("farming") as FarmingState
            state.getBin(this).also { state.init() }
        } else
            state.getBin(this)
    }
}