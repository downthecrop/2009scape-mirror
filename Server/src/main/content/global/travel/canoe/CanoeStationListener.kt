package content.global.travel.canoe

import core.game.component.Component
import core.game.node.entity.skill.Skills
import content.data.skill.SkillingTool
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Components
import core.game.interaction.InteractionListener
import core.game.interaction.IntType

import core.api.*
import org.rs09.consts.Sounds

class CanoeStationListener : InteractionListener {

    private val STATION_IDs = intArrayOf(12140, 12141, 12142, 12143, 12144, 12145, 12146, 12147, 12148, 12151, 12152, 12153, 12154, 12155, 12156, 12157, 12158, 12149, 12150)
    private val STAGE_TREE_NONINTERACTABLE = 9
    private val STAGE_LOG_CHOPPED = 10
    private val SHAPING_INTERFACE = Components.CANOE_52
    private val PUSH = Animation(3301)
    private val FALL = Animation(3303)
    private val FLOAT = Animation(3304)

    override fun defineDestinationOverrides() {
        setDest(IntType.SCENERY,STATION_IDs,"chop-down"){ _, node ->
            return@setDest CanoeUtils.getChopLocation(node.location)
        }
        setDest(IntType.SCENERY,STATION_IDs,"shape-canoe","float canoe","float log","float waka"){ _, node ->
            return@setDest CanoeUtils.getCraftFloatLocation(node.location)
        }
    }

    override fun defineListeners() {
        on(STATION_IDs, IntType.SCENERY, "chop-down"){ player, node ->
            val axe: SkillingTool? = SkillingTool.getHatchet(player)
            val varbit = node.asScenery().definition.configFile
            if(varbit.getValue(player) != 0){
                setVarbit(player,varbit,0)
            }

            if (axe == null) {
                player.packetDispatch.sendMessage("You do not have an axe which you have the woodcutting level to use.")
                return@on true
            }
            if (player.skills.getLevel(Skills.WOODCUTTING) < 12) {
                player.packetDispatch.sendMessage("You need a woodcutting level of at least 12 to chop down this tree.")
                return@on true
            }
            lock(player, 5)
            setVarp(player, varbit.varpId, 0)
            player.faceLocation(CanoeUtils.getFaceLocation(player.location))
            player.animate(axe.animation)
            setVarbit(player,varbit,STAGE_TREE_NONINTERACTABLE)
            player.pulseManager.run(object : Pulse(4){
                override fun pulse(): Boolean {
                    player.animator.stop()
                    setVarbit(player,varbit,STAGE_LOG_CHOPPED)
                    player.packetDispatch.sendSceneryAnimation(node.asScenery().getChild(player), FALL, false)
                    player.unlock()
                    return true
                }
            })
            return@on true
        }

        on(STATION_IDs, IntType.SCENERY, "shape-canoe"){ player, node ->
            val varbit = node.asScenery().definition.configFile
            if(varbit.getValue(player) != STAGE_LOG_CHOPPED){
                setVarbit(player,varbit,0)
                return@on true
            }
            player.faceLocation(CanoeUtils.getFaceLocation(player.location))
            player.interfaceManager.open(Component(SHAPING_INTERFACE))
            player.setAttribute("canoe-varbit",varbit)
            return@on true
        }

        on(STATION_IDs, IntType.SCENERY, "float canoe","float log","float waka"){ player, node ->
            val varbit = node.asScenery().definition.configFile
            val canoe = CanoeUtils.getCanoeFromVarbit(player, varbit)
            player.animator.animate(PUSH)
            lock(player, 2)
            playAudio(player, Sounds.CANOE_ROLL_2731)
            player.faceLocation(CanoeUtils.getFaceLocation(player.location))
            player.pulseManager.run(object : Pulse(){
                override fun pulse(): Boolean {
                    setVarbit(player,varbit, CanoeUtils.getCraftValue(canoe, true))
                    player.packetDispatch.sendSceneryAnimation(node.asScenery(), FLOAT, false)
                    player.unlock()
                    return true
                }
            })
            return@on true
        }

        on(STATION_IDs, IntType.SCENERY, "paddle log","paddle canoe"){ player, node ->
            player.interfaceManager.open(Component(Components.CANOE_STATIONS_MAP_53))
            return@on true
        }
    }
}
