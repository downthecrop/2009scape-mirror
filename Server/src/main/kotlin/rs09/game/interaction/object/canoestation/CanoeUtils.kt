package rs09.game.interaction.`object`.canoestation

import core.cache.def.impl.VarbitDefinition
import core.game.content.global.travel.canoe.Canoe
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import org.rs09.consts.Components

object CanoeUtils {
    val SHAPE_INTERFACE = Components.CANOE_52

    fun checkCanoe(player: Player, canoe: Canoe){
        if(player.skills.getLevel(Skills.WOODCUTTING) < canoe.requiredLevel) return
        player.packetDispatch.sendInterfaceConfig(SHAPE_INTERFACE,canoe.silhouetteChild,true)
        player.packetDispatch.sendInterfaceConfig(SHAPE_INTERFACE,canoe.textChild,false)
    }

    fun getCanoeFromVarbit(player: Player, varbit: VarbitDefinition): Canoe{
        var bit = varbit.getValue(player)
        if(bit > 10) bit -= 10
        return Canoe.values()[bit - 1]
    }

    fun getCraftValue(canoe: Canoe, floating: Boolean): Int{
        return 1 + (canoe.ordinal + if (floating) 10 else 0)
    }

    fun getStationIndex(location: Location): Int{
        return when(location.regionId){
            12850 -> 0
            12852,12596 -> 1
            12341 -> 2
            12342 -> 3
            12603 -> 4
            else -> 0
        }
    }

    fun getDestinationFromButtonID(buttonID: Int): Location {
        return when(buttonID){
            47 -> Location.create(3240, 3242, 0)
            48 -> Location.create(3199, 3344, 0)
            3 -> Location.create(3109, 3415, 0)
            6 -> Location.create(3132, 3510, 0)
            49 -> Location.create(3139, 3796, 0)
            else -> Location.create(3240, 3242, 0)
        }
    }

    fun getNameByIndex(index: Int): String{
        return when(index){
            0 -> "Lumbridge"
            1 -> "the Champion's Guild."
            2 -> "Barbarian Village"
            3 -> "Edgeville"
            4 -> "the Wilderness Pond"
            else -> "Uhhh report this."
        }
    }

}