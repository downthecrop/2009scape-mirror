package rs09.game.interaction.`object`.canoestation

import core.cache.def.impl.VarbitDefinition
import core.game.content.global.travel.canoe.Canoe
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.entity.skill.gather.SkillingTool
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
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

    fun getTravelAnimation(stationId: Int, destId: Int): Int? {
        val fromLumbridge = mapOf(
                4 to 9887,
                3 to 9888,
                2 to 9889,
                1 to 9890
        )
        val fromChampions = mapOf(
                4 to 9891,
                3 to 9892,
                2 to 9893,
                0 to 9906
        )
        val fromBarbarian = mapOf(
                4 to 9894,
                3 to 9895,
                1 to 9905,
                0 to 9906
        )
        val fromEdge = mapOf(
                4 to 9896,
                2 to 9903,
                1 to 9902,
                0 to 9901
        )

        val fromWilderness = mapOf(
                3 to 9900,
                2 to 9899,
                1 to 9898,
                0 to 9897
        )
        when(stationId){
            0 -> {
                return fromLumbridge[destId]
            }
            1 -> {
                return fromChampions[destId]
            }
            2 -> {
                return fromBarbarian[destId]
            }
            3 -> {
                return fromEdge[destId]
            }
            4 -> {
                return fromWilderness[destId]
            }
        }
        return 0
    }

    fun getShapeAnimation(axe: SkillingTool): Animation{
        when(axe){
            SkillingTool.BRONZE_AXE -> return Animation(6744);
            SkillingTool.IRON_AXE -> return Animation(6743);
            SkillingTool.STEEL_AXE -> return Animation(6742);
            SkillingTool.BLACK_AXE -> return Animation(6741);
            SkillingTool.MITHRIL_AXE -> return Animation(6740);
            SkillingTool.ADAMANT_AXE -> return Animation(6739);
            SkillingTool.RUNE_AXE -> return Animation(6738);
            SkillingTool.DRAGON_AXE -> return Animation(6745);
        }
        return axe.animation;
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

    fun getFaceLocation(location: Location): Location{
        return when(getStationIndex(location)){
            1 -> location.transform(0,-1,0)
            0,2,3 -> location.transform(-1,0,0)
            else -> location
        }
    }

    fun getChopLocation(location: Location): Location{
        return when(getStationIndex(location)){
            0 -> Location.create(3243, 3235, 0)
            1 -> Location.create(3204, 3343, 0)
            2 -> Location.create(3112, 3409, 0)
            3 -> Location.create(3132, 3508, 0)
            else -> Location.create(0,0)
        }
    }

    fun getCraftFloatLocation(location: Location): Location{
        return when(getStationIndex(location)){
            0 -> Location.create(3243, 3237, 0)
            1 -> Location.create(3202, 3343, 0)
            2 -> Location.create(3112, 3411, 0)
            3 -> Location.create(3132, 3510, 0)
            else -> Location.create(0,0)
        }
    }

}