package rs09.game.content.zone

import api.*
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.system.task.Pulse
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneBuilder
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs09.consts.NPCs
import rs09.game.content.dialogue.DialogueFile
import rs09.tools.secondsToTicks
import java.util.concurrent.TimeUnit

@Initializable
class FarmingPatchZone : MapZone("farming patch", true), Plugin<Any> {

    val playersInZone = hashMapOf<Player,Int>()

    override fun configure() {
        registerRegion(12083)
        registerRegion(10548)
        register(ZoneBorders(3594,3521,3608,3532))
        submitWorldPulse(zonePulse)
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        ZoneBuilder.configure(this)
        return this
    }

    override fun fireEvent(identifier: String?, vararg args: Any?): Any {
        return Unit
    }

    override fun enter(e: Entity?): Boolean {
        if(e is Player && playersInZone[e] == null && getStatLevel(e, Skills.FARMING) <= 15) {
            playersInZone[e] = 0
        }
        return super.enter(e)
    }

    override fun leave(e: Entity?, logout: Boolean): Boolean {
        if(e is Player){
            playersInZone.remove(e)
        }
        return super.enter(e)
    }

    val zonePulse = object : Pulse(){
        override fun pulse(): Boolean {
            playersInZone.toList().forEach { (player, ticks) ->
                if(ticks == secondsToTicks(TimeUnit.MINUTES.toSeconds(5).toInt())){
                    val npc = NPC(NPCs.GITHAN_7122)
                    npc.location = player.location
                    npc.init()
                    npc.moveStep()
                    npc.face(player)
                    openDialogue(player, SpiritDialogue(true), npc)
                } else if (ticks == secondsToTicks(TimeUnit.MINUTES.toSeconds(10).toInt())){
                    val npc = NPC(NPCs.GITHAN_7122)
                    npc.location = player.location
                    npc.init()
                    npc.moveStep()
                    npc.face(player)
                    openDialogue(player, SpiritDialogue(false), npc)
                    playersInZone.remove(player)
                }

                playersInZone[player] = ticks + 1
            }
            return false
        }
    }

    internal class SpiritDialogue(val is5: Boolean) : DialogueFile(){
        override fun handle(componentID: Int, buttonID: Int) {
            when(stage){
                0 -> {
                    if(is5) npcl(FacialExpression.NEUTRAL, "In case you didn't know, you don't have to stand by your Farming patch. Your crops will grow even if you're not around.").also { stage++ }
                    else npcl(FacialExpression.NEUTRAL, " Did you know that if your Farming patch has fully grown, it will never catch disease or die? It will be perfectly safe until you choose to harvest it.").also { stage++ }
                }
                1 -> end()
            }
        }

        override fun end(){
            super.end()
            poofClear(npc ?: return)
        }
    }

}