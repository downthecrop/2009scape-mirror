package rs09.game.content.zone.keldagrim

import core.game.component.Component
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs09.consts.Components
import rs09.game.world.GameWorld

@Initializable
class DwarvenBoatmanDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage++){
            0 -> player("I'd like to go to Keldagrim please.")
            1 -> npc("How do you know about that?")
            2 -> player("Look, I just do, okay?")
            3 -> npc("uhhhh")
            4 -> player("I'm, errr, from the future!")
            5 -> npc("hmmm")
            6 -> player("Can I please just go?")
            7 -> npc("Hmmmmmmmmmmm")
            8 -> player("Ok, fine! I'll prove it! errrr....")
            9 -> npc("Hm?")
            10 -> player("In the future your big statue gets knocked over!")
            11 -> npc("How do you even know about-")
            12 -> npc("Gah, nevermind! Just climb","on board!")
            13 -> player("Thank you!")
            14 -> end().also { GameWorld.Pulser.submit(travelPulse(player)) }
        }
        return true
    }

    override fun open(vararg args: Any?): Boolean {
        npc("Aye, what do you want?")
        stage = 0
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return DwarvenBoatmanDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(2205,1844,1843)
    }

    override fun npc(vararg messages: String?): Component {
        return super.npc(FacialExpression.OLD_NORMAL, *messages)
    }
}

class travelPulse(val player: Player): Pulse(1){
    var counter = 0
    override fun pulse(): Boolean {
        when(counter++){
            0 -> player.lock().also { player.interfaceManager.open(Component(Components.FADE_TO_BLACK_120)) }
            3 -> player.properties.teleportLocation = Location.create(2888, 10225, 0)
            4 -> {
                player.interfaceManager.close(Component(Components.FADE_TO_BLACK_120))
                player.interfaceManager.open(Component(Components.FADE_FROM_BLACK_170))
            }
            6 -> player.unlock().also { player.interfaceManager.close(Component(Components.FADE_FROM_BLACK_170)); player.setAttribute("/save:keldagrim-visited",true);  return true }
        }
        return false
    }
}