package rs09.game.system.command.rottenpotato

import api.removeItem
import core.cache.def.impl.ItemDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.Rights
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs09.consts.Items
import rs09.game.system.SystemLogger
import rs09.game.system.command.CommandSystem
import rs09.tools.stringtools.colorize

/**
 * Handles the Rotten Potato options
 * @author Ceikry
 */
private val ROTTEN_POTATO_OPTIONS = arrayOf("RS HD","Heal","Extra","Commands")
@Initializable
class RottenPotatoOptionHandler : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        for(option in ROTTEN_POTATO_OPTIONS) {
            ItemDefinition.forId(Items.ROTTEN_POTATO_5733).handlers["option:${option.toLowerCase()}"] = this
        }
        return this;
    }

    override fun handle(player: Player?, node: Node?, option: String?): Boolean {
        player ?: return false
        node ?: return false
        option ?: return false

        // re-add the fucking check because some fucking moron removed it at some point
        if(player.rights != Rights.ADMINISTRATOR)
        {
            removeItem(player, Items.ROTTEN_POTATO_5733)
            SystemLogger.logAlert("Player ${player.username} had a rotten potato. It has been removed.")
            return false
        }

        when(option){
            "rs hd" -> player.dialogueInterpreter.open(RottenPotatoRSHDDialogue().ID)
            "heal" -> player.fullRestore().also { player.sendMessage(colorize("%RAll healed!")) }
            "extra" -> player.dialogueInterpreter.open(RottenPotatoExtraDialogue().ID)
            "commands" -> {
                CommandSystem.commandSystem.parse(player,"commands")
            }
        }
        return true
    }

}