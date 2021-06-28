package rs09.game.node.entity.skill.agility.shortcuts

import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.impl.ForceMovement
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin

/**
 * Handles the stepping stone shortcut.
 * @author Ceikry
 */
@Initializable
class SteppingStoneShortcut : OptionHandler() {
    private val stones = HashMap<Location,SteppingStoneInstance>()
    internal class SteppingStoneInstance(val pointA: Location, val pointB: Location, val option: String, val levelReq: Int)

    override fun handle(player: Player?, node: Node?, option: String?): Boolean {
        player ?: return false
        val stone = stones[player.location]
        stone ?: return false

        if(player.skills.getLevel(Skills.AGILITY) < stone.levelReq){
            player.sendMessage("You need an agility level of ${stone.levelReq} for this shortcut.")
            return true
        }

        val finalDest = when(player.location){
            stone.pointA -> stone.pointB
            stone.pointB -> stone.pointA
            else -> player.location
        }
        val offset = getOffset(player,finalDest)
        player.debug("Offset: ${offset.first},${offset.second}")
        player.pulseManager.run(object : Pulse(2){
            override fun pulse(): Boolean {
                val there = player.location == finalDest
                if(!there){
                    ForceMovement.run(player,player.location,player.location.transform(offset.first,offset.second,0), ANIMATION,10)
                }
                return there
            }
        })
        return true
    }

    fun getOffset(player: Player, location: Location): Pair<Int,Int>{
        var diffX = location.x - player.location.x
        var diffY = location.y - player.location.y
        if(diffX > 1) diffX = 1
        if(diffX < -1) diffX = -1
        if(diffY > 1) diffY = 1
        if(diffY < -1) diffY = -1
        return Pair(diffX,diffY)
    }

    fun configure(objects: IntArray, pointA: Location, pointB: Location, option: String, levelReq: Int){
        val instance = SteppingStoneInstance(pointA,pointB, option, levelReq)
        objects.forEach {
            SceneryDefinition.forId(it).handlers["option:$option"] = this
        }
        stones.put(pointA,instance)
        stones.put(pointB, instance)
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        configure(intArrayOf(2335,2333),Location.create(2925,2947,0),Location.create(2925,2951,0),"cross",30)
        configure(intArrayOf(9315),Location.create(3149, 3363, 0),Location.create(3154, 3363, 0),"jump-onto",31)

        return this
    }

    companion object {
        /**
         * Represents the animation to use.
         */
        private val ANIMATION = Animation(741)
    }
}