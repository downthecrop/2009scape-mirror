package rs09.game.interaction.`object`.canoestation

import api.*
import core.cache.def.impl.VarbitDefinition
import core.game.component.Component
import core.game.content.global.travel.canoe.Canoe
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.node.entity.skill.gather.SkillingTool
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation
import core.net.packet.PacketRepository
import core.net.packet.context.MinimapStateContext
import core.net.packet.out.MinimapState
import core.tools.RandomFunction
import org.rs09.consts.Components
import rs09.game.interaction.InterfaceListener
import kotlin.math.abs

class CanoeInterfaceListeners : InterfaceListener() {

    val SHAPE_INTERFACE = Components.CANOE_52
    val DESTINATION_INTERFACE = Components.CANOE_STATIONS_MAP_53
    val EDGEVILLE_REGION = 12342
    private val boatChilds = intArrayOf(47, 48, 3, 6, 49)
    private val locationChilds = intArrayOf(50, 47, 44, 36)


    override fun defineListeners() {

        onOpen(SHAPE_INTERFACE){player, _ ->
            CanoeUtils.checkCanoe(player,Canoe.DUGOUT)
            CanoeUtils.checkCanoe(player,Canoe.STABLE_DUGOUT)
            CanoeUtils.checkCanoe(player,Canoe.WAKA)
            return@onOpen true
        }

        on(SHAPE_INTERFACE) { player, _, _, buttonID, _, _ ->
            player.interfaceManager.close()
            val canoe = Canoe.getCanoeFromChild(buttonID)
            val varbit = player.getAttribute("canoe-varbit", VarbitDefinition.forObjectID(0))

            val axe: SkillingTool? = SkillingTool.getHatchet(player)
            if (axe == null) {
                player.packetDispatch.sendMessage("You do not have an axe which you have the woodcutting level to use.")
                return@on true
            }

            player.lock()
            animate(player,CanoeUtils.getShapeAnimation(axe))
            player.pulseManager.run(object : Pulse(3) {
                override fun pulse(): Boolean {
                    if (RandomFunction.random(if (canoe == Canoe.WAKA) 8 else 6) == 1) {
                        if (player.location.regionId == EDGEVILLE_REGION && canoe == Canoe.WAKA) {
                            player.achievementDiaryManager.finishTask(player, DiaryType.VARROCK, 2, 10)
                        }
                        player.varpManager.setVarbit(varbit,CanoeUtils.getCraftValue(canoe,false))
                        player.skills.addExperience(Skills.WOODCUTTING, canoe.experience)
                        player.unlock()
                        return true
                    }
                    animate(player,CanoeUtils.getShapeAnimation(axe))
                    return false
                }
            })
            return@on true
        }

        onOpen(DESTINATION_INTERFACE){player, component ->
            val varbit = player.getAttribute("canoe-varbit",VarbitDefinition.forObjectID(0))
            val canoe = CanoeUtils.getCanoeFromVarbit(player,varbit)
            val stationIndex = CanoeUtils.getStationIndex(player.location)
            val maxDistance = canoe.maxDist
            player.packetDispatch.sendInterfaceConfig(DESTINATION_INTERFACE,boatChilds[stationIndex],true)
            player.packetDispatch.sendInterfaceConfig(DESTINATION_INTERFACE,locationChilds[stationIndex],false)
            if(canoe != Canoe.WAKA){
                player.packetDispatch.sendInterfaceConfig(DESTINATION_INTERFACE,49,true)
                for(i in 0..3){
                    if(i == stationIndex) continue
                    if(abs(i - stationIndex) > maxDistance){
                        player.packetDispatch.sendInterfaceConfig(DESTINATION_INTERFACE,boatChilds[i],true)
                        player.packetDispatch.sendInterfaceConfig(DESTINATION_INTERFACE,locationChilds[i],true)
                    }
                }
            }
            return@onOpen true
        }

        on(DESTINATION_INTERFACE){player, _, _, buttonID, _, _ ->
            val dest = CanoeUtils.getDestinationFromButtonID(buttonID)
            val destIndex = CanoeUtils.getStationIndex(dest)
            val arrivalMessage = CanoeUtils.getNameByIndex(destIndex)
            val stationIndex = CanoeUtils.getStationIndex(player.location)
            val interfaceAnimationId = CanoeUtils.getTravelAnimation(stationIndex,destIndex)
            var travelAnimDur = 15
            val varbit = player.getAttribute("canoe-varbit",VarbitDefinition.forObjectID(0))

            if (player.familiarManager.hasFamiliar()) {
                player.sendMessage("You can't take a follower on a canoe.")
                return@on true
            }
            if (interfaceAnimationId != 0) {
                travelAnimDur = Animation(interfaceAnimationId).duration
            }

            player.lock()
            player.interfaceManager.close()
            player.pulseManager.run(object : Pulse(){
                var counter = 0
                override fun pulse(): Boolean {
                    when(counter++){
                        0 -> {
                            player.interfaceManager.openOverlay(Component(Components.FADE_TO_BLACK_120))
                            player.interfaceManager.open(Component(Components.CANOE_TRAVEL_758))
                            animateInterface(player, Components.CANOE_TRAVEL_758, 3, interfaceAnimationId)
                        }
                        2 -> {
                            PacketRepository.send(MinimapState::class.java, MinimapStateContext(player, 2))
                            player.interfaceManager.removeTabs(0, 1, 2, 3, 4, 5, 6, 11, 12)
                        }
                        travelAnimDur+1 -> {
                            player.properties.teleportLocation = dest
                            player.interfaceManager.close(Component(Components.CANOE_TRAVEL_758))
                            player.interfaceManager.closeOverlay()
                            player.interfaceManager.openOverlay(Component(Components.FADE_FROM_BLACK_170))
                        }
                        travelAnimDur+3 -> {
                            player.unlock()
                            player.interfaceManager.restoreTabs()
                            PacketRepository.send(MinimapState::class.java, MinimapStateContext(player, 0))
                            player.sendMessage("You arrive at $arrivalMessage.")
                            player.sendMessage("Your canoe sinks from the long journey.")
                            if(destIndex == 4){
                                player.sendMessage("There are no trees nearby to make a new canoe. Guess you're walking.")
                            }
                            player.varpManager.setVarbit(varbit,0)
                            return true
                        }
                    }
                    return false
                }
            })
            return@on true
        }
    }
}