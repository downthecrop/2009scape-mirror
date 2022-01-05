package rs09.game.content.activity.fog

import core.game.component.Component
import core.game.node.entity.player.Player
import core.game.node.item.Item
import rs09.game.world.GameWorld
import core.plugin.Initializable
import core.game.content.dialogue.DialoguePlugin
import org.rs09.consts.Components
import rs09.game.interaction.FOGRewardsInterface

@Initializable
class ReggieDialogue(player: Player? = null) : DialoguePlugin(player){
    override fun open(vararg args: Any?): Boolean {
        if(GameWorld.settings?.allow_token_purchase == true){
            options("Can I see your shop?","Nevermind.","Can I buy some tokens?")
        } else {
            options("Can I see your shop?","Nevermind.")
        }
        stage = 0
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        FOGRewardsInterface().newInstance(Unit)
        return ReggieDialogue(player)
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        var buyAmount = 0
        when(stage){
            0 -> when(buttonId){
                1 -> npc("Certainly!").also { stage++ }
                2 -> end()
                3 -> npc("Sure thing. My tokens are 1000 coins","each.").also { stage = 10 }
            }
            1 -> end().also { player.interfaceManager.open(Component(732)) }

            //Buying tokens with config option set to true
            10 -> player?.dialogueInterpreter?.sendOptions("How many?","50","100","250","500").also { stage++ }
            11 -> when(buttonId){
                1 -> buyAmount = 50
                2 -> buyAmount = 100
                3 -> buyAmount = 250
                4 -> buyAmount = 500
                else -> buyAmount = 0
            }.also {
                if(buyAmount > 0){
                    if(player?.inventory?.containsItem(Item(995, 1000 * buyAmount))!!) {
                        player?.inventory?.add(Item(12852, buyAmount))
                        player?.inventory?.remove(Item(995, 1000 * buyAmount))
                    } else {
                        player?.sendMessage("You dont have enough coins for that.")
                    }
                }
                end()
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(7601)
    }

}