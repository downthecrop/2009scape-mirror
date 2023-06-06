package content.global.skill.farming

import core.cache.def.impl.SceneryDefinition
import core.cache.def.impl.VarbitDefinition
import core.game.node.scenery.Scenery
import core.game.node.entity.player.Player

enum class CompostBins(val varbit: Int) {
    FALADOR_COMPOST(740),
    CATHERBY_COMPOST(741),
    PORT_PHAS_COMPOST(742),
    ARDOUGNE_COMPOST(743);

    companion object {
        @JvmField
        val bins = values().map { it.varbit to it }.toMap()

        @JvmStatic
        fun forObject(obj: Scenery): CompostBins?{
            return forObjectID(obj.id)
        }

        @JvmStatic
        fun forObjectID(id: Int): CompostBins?{
            val objDef = SceneryDefinition.forId(id)
            return bins[objDef.varbitID]
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
