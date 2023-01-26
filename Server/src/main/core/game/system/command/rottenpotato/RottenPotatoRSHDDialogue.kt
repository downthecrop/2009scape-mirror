package core.game.system.command.rottenpotato

import core.api.*
import core.api.InputType
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.login.PlayerParser
import core.game.world.ImmerseWorld
import core.game.world.repository.Repository
import core.plugin.Initializable
import core.tools.colorize
import core.game.bots.AIRepository

@Initializable
/**
 * Rotten Potato "RS HD" option menu
 * @author Ceikry
 */
class RottenPotatoRSHDDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player) {
    val ID = 38575793
    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return RottenPotatoRSHDDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        options("Wipe Bots", "Spawn Bots", "Force Log Players", "View Bank", "Copy Inventory")
        stage = 0
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> {
                when (buttonId) {
                    //Wipe Bots
                    1 -> AIRepository.clearAllBots().also { player.sendMessage(colorize("%RBots wiped.")); end() }
                    //Spawn Bots
                    2 -> ImmerseWorld.spawnBots().also { player.sendMessage(colorize("%RBots Respawning...")); end() }
                    //Force Log All Online Players
                    3 -> {
                        Repository.disconnectionQueue.clear().also { end() }
                        Repository.players.toArray().forEach {
                            val p = it.asPlayer()
                            if (p != null && !p.isArtificial()) { // Should never be null.
                                p.removeAttribute("combat-time")
                                p.clear()
                                PlayerParser.save(p)
                                p.getDetails().save()
                            }
                        }
                    }
                    //View Bank
                    4 -> {
                        end()
                        sendInputDialogue(player, InputType.STRING_SHORT, "Enter player name:"){ value ->
                            val other = Repository.getPlayerByName(value.toString().toLowerCase().replace(" ","_"))
                            if(other == null){
                                player.sendMessage(colorize("%RInvalid player name."))
                                return@sendInputDialogue
                            }

                            other.bank.open(player)
                        }
                    }
                    //View Inventory
                    5 -> {
                        end()
                        sendInputDialogue(player, InputType.STRING_SHORT, "Enter player name:"){ value ->
                            val other = Repository.getPlayerByName(value.toString().toLowerCase().replace(" ","_"))
                            if(other == null){
                                player.sendMessage(colorize("%RInvalid player name."))
                                return@sendInputDialogue
                            }

                            player.inventory.clear()
                            player.inventory.addAll(other.inventory)
                        }
                    }
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(ID)
    }
}