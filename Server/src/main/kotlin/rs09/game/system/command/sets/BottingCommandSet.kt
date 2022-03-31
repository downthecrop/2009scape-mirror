package rs09.game.system.command.sets

import core.game.component.Component
import core.plugin.Initializable
import org.rs09.consts.Components
import rs09.game.ai.general.GeneralBotCreator
import rs09.game.ai.general.scriptrepository.PlayerScripts
import rs09.game.ai.general.scriptrepository.Script
import rs09.game.system.command.Command
import rs09.game.system.command.Privilege
import rs09.game.world.GameWorld
import rs09.tools.stringtools.colorize

@Initializable
class BottingCommandSet : CommandSet(Privilege.STANDARD) {
    override fun defineCommands() {
        define("scripts"){player, _ ->
            if(GameWorld.settings?.enabled_botting != true){
                player.sendChat("I just tried to do something silly!")
                return@define
            }
            if(!player.getAttribute("botting:warning_shown",false)){
                player.dialogueInterpreter.sendDialogue(colorize("%RWARNING: Running a bot script will permanently remove you"),colorize("%Rfrom the highscores."))
                player.dialogueInterpreter.addAction { player, buttonId -> player.setAttribute("/save:botting:warning_shown",true) }
                return@define
            }
            for (i in 0..310) {
                player.packetDispatch.sendString("", 275, i)
            }
            var lineid = 11
            player.packetDispatch.sendString("Bot Scripts",275,2)
            for(script in PlayerScripts.identifierMap.values) {
                player.packetDispatch.sendString("<bold>${script.name}</bold>", 275, lineid++)
                script.description.forEach { line ->
                    player.packetDispatch.sendString(line,275,lineid++)
                }
                player.packetDispatch.sendString("<img=3> ::script ${script.identifier}",275,lineid++)
                player.packetDispatch.sendString("<str>                                 </str>",275,lineid++)
            }
            player.interfaceManager.open(Component(Components.QUESTJOURNAL_SCROLL_275))
        }
        define("script"){player,args ->
            if(GameWorld.settings?.enabled_botting != true){
                player.sendChat("I just tried to do something very silly!")
                return@define
            }
            if(!player.getAttribute("botting:warning_shown",false)){
                player.dialogueInterpreter.sendDialogue(colorize("%RWARNING: Running a bot script will permanently remove you from the highscores."))
                player.dialogueInterpreter.addAction { player, buttonId -> player.setAttribute("/save:botting:warning_shown",true) }
                return@define
            }
            if(args.size < 2){
                reject(player,"Usage: ::script identifier")
            }
            val identifier = args[1]
            val script = PlayerScripts.identifierMap[identifier]
            if(script == null){
                reject(player,"Invalid script identifier")
            }
            player.interfaceManager.close()
            GeneralBotCreator(script!!.clazz.newInstance() as Script,player,true)
            player.sendMessage(colorize("%RStarting script..."))
            player.sendMessage(colorize("%RTo stop the script, do ::stopscript or log out."))

        }
        define("stopscript"){player,args ->
            val pulse: GeneralBotCreator.BotScriptPulse? = player.getAttribute("botting:script",null)
            pulse?.stop()
            player.interfaceManager.closeOverlay()
        }
    }
}