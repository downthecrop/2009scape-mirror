package rs09.game.interaction.`object`.canoestation

import core.game.component.Component
import core.game.node.entity.skill.Skills
import core.game.node.entity.skill.gather.SkillingTool
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Components
import rs09.game.interaction.InteractionListener

class CanoeStationListener : InteractionListener() {

    private val STATION_IDs = intArrayOf(12140, 12141, 12142, 12143, 12144, 12145, 12146, 12147, 12148, 12151, 12152, 12153, 12154, 12155, 12156, 12157, 12158, 12144, 12146, 12149, 12150, 12157)
    private val STAGE_TREE_NONINTERACTABLE = 9
    private val STAGE_LOG_CHOPPED = 10
    private val SHAPING_INTERFACE = Components.CANOE_52
    private val PUSH = Animation(3301)
    private val FALL = Animation(3303)
    private val FLOAT = Animation(3304)

    override fun defineDestinationOverrides() {
        setDest(SCENERY,STATION_IDs,"chop-down"){ _,node ->
            return@setDest CanoeUtils.getChopLocation(node.location)
        }
        setDest(SCENERY,STATION_IDs,"shape-canoe","float canoe","float log","float waka"){ _,node ->
            return@setDest CanoeUtils.getCraftFloatLocation(node.location)
        }
    }

    override fun defineListeners() {
        on(STATION_IDs,SCENERY,"chop-down"){ player, node ->
            val axe: SkillingTool? = SkillingTool.getHatchet(player)
            val varbit = node.asScenery().definition.configFile
            if(varbit.getValue(player) != 0){
                player.varpManager.setVarbit(varbit,0)
            }

            if (axe == null) {
                player.packetDispatch.sendMessage("You do not have an axe which you have the woodcutting level to use.")
                return@on true
            }
            if (player.skills.getLevel(Skills.WOODCUTTING) < 12) {
                player.packetDispatch.sendMessage("You need a woodcutting level of at least 12 to chop down this tree.")
                return@on true
            }
            player.lock()
            player.varpManager.get(varbit.configId).clearBitRange(0,31)
            player.faceLocation(CanoeUtils.getFaceLocation(player.location))
            player.animate(axe.animation)
            player.varpManager.setVarbit(varbit,STAGE_TREE_NONINTERACTABLE)
            player.pulseManager.run(object : Pulse(4){
                override fun pulse(): Boolean {
                    player.animator.stop()
                    player.varpManager.setVarbit(varbit,STAGE_LOG_CHOPPED)
                    player.packetDispatch.sendSceneryAnimation(node.asScenery().getChild(player), FALL, false)
                    player.unlock()
                    return true
                }
            })
            return@on true
        }

        on(STATION_IDs,SCENERY,"shape-canoe"){ player, node ->
            val varbit = node.asScenery().definition.configFile
            if(varbit.getValue(player) != STAGE_LOG_CHOPPED){
                player.varpManager.setVarbit(varbit,0)
                return@on true
            }
            player.faceLocation(CanoeUtils.getFaceLocation(player.location))
            player.interfaceManager.open(Component(SHAPING_INTERFACE))
            player.setAttribute("canoe-varbit",varbit)
            return@on true
        }

        on(STATION_IDs,SCENERY,"float canoe","float log","float waka"){ player, node ->
            val varbit = node.asScenery().definition.configFile
            val canoe = CanoeUtils.getCanoeFromVarbit(player,varbit)
            player.animator.animate(PUSH)
            player.lock()
            player.faceLocation(CanoeUtils.getFaceLocation(player.location))
            player.pulseManager.run(object : Pulse(){
                override fun pulse(): Boolean {
                    player.varpManager.setVarbit(varbit,CanoeUtils.getCraftValue(canoe,true))
                    player.packetDispatch.sendSceneryAnimation(node.asScenery(), FLOAT, false)
                    player.unlock()
                    return true
                }
            })
            return@on true
        }

        on(STATION_IDs,SCENERY,"paddle log","paddle canoe"){ player, node ->
            player.interfaceManager.open(Component(Components.CANOE_STATIONS_MAP_53))
            return@on true
        }
    }
}