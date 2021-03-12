package rs09.game.node.entity.skill.construction.decoration.portalchamber

import core.game.content.dialogue.DialoguePlugin
import core.game.node.entity.player.Player
import core.game.node.entity.skill.construction.decoration.portalchamber.PortalChamberPlugin
import core.plugin.Initializable

/**
 * Portal Chamber Dialogue go brrrrrr
 * @author Ceikry
 */
@Initializable
class PortalChamberDialogue(player: Player? = null) : DialoguePlugin(player) {
    var portal = "none"
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> when(buttonId){
                1 -> directPortal("varrock")
                2 -> directPortal("lumbridge")
                3 -> directPortal("falador")
                4 -> options("Camelot Portal","Ardougne Portal","Yanille Portal","Kharyll Portal").also { stage++ }
            }
            1 -> when(buttonId){
                1 -> directPortal("camelot")
                2 -> directPortal("ardougne")
                3 -> directPortal("yanille")
                4 -> directPortal("kharyrll")
            }
        }
        return true
    }

    fun directPortal(portal: String){
        PortalChamberPlugin.direct(player,portal.toUpperCase())
        end()
    }

    override fun open(vararg args: Any?): Boolean {
        player.dialogueInterpreter.sendOptions("Select one.","Varrock Portal","Lumbridge Portal","Falador Portal","More...")
        stage = 0
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return PortalChamberDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(394857)
    }
}