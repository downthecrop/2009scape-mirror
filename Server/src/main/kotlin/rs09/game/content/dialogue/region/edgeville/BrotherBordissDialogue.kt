package rs09.game.content.dialogue.region.edgeville

import core.game.component.Component
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs09.consts.Items

@Initializable
/**
 * Handles Boriss' dialogue
 * @author Ceikry
 */
class BrotherBordissDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player?): DialoguePlugin {
        return BrotherBordissDialogue(player)
    }

    override fun npc(vararg messages: String?): Component {
        return npc(FacialExpression.OLD_NORMAL,*messages)
    }

    override fun open(vararg args: Any?): Boolean {
        player ?: return false
        if(getSigil(player) != null && player.inventory.contains(Items.BLESSED_SPIRIT_SHIELD_13736,1)){
            player("Can you combine my shield and sigil for me?")
            stage = 10
            return true
        }

        npc("Lovely day, adventurer.")
        stage = 1000
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            10 -> npc("I certainly can, for the price of","1,500,000 coins.").also { stage++ }
            11 -> options("Okay, sounds great!","No, thanks.").also { stage++ }
            12 -> when(buttonId){
                1 -> if(player.inventory.contains(995,1500000)){
                    player("Okay, sounds great!")
                    stage = 15
                } else {
                    player("I'd like to, but I don't have the coin.")
                    stage++
                }
                2 -> player("No, thanks").also { stage = 1000 }
            }
            13 -> npc("That's a shame, then.").also { stage = 1000 }
            15 -> {
                npc("Lovely, here you are.")
                val sigil = getSigil(player)
                if(sigil == null){
                    end()
                    return true
                }
                if(player.inventory.remove(sigil,Item(995,1500000),Item(Items.BLESSED_SPIRIT_SHIELD_13736))){
                    player.inventory.add(getShield(player,sigil))
                }
                stage++
            }
            16 -> player("Thank you!").also { stage = 1000 }



            1000 -> end()
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(7724)
    }


    fun getSigil(player: Player): Item?{
        for(sigil in arrayOf(Items.ARCANE_SIGIL_13746,Items.DIVINE_SIGIL_13748,Items.SPECTRAL_SIGIL_13752,Items.ELYSIAN_SIGIL_13750)){
            if(player.inventory.contains(sigil,1)) return Item(sigil)
        }
        return null
    }

    fun getShield(player: Player, sigil: Item): Item?{
        return when(sigil.id){
            Items.ARCANE_SIGIL_13746 -> Item(Items.ARCANE_SPIRIT_SHIELD_13738)
            Items.ELYSIAN_SIGIL_13750 -> Item(Items.ELYSIAN_SPIRIT_SHIELD_13742)
            Items.DIVINE_SIGIL_13748 -> Item(Items.DIVINE_SPIRIT_SHIELD_13740)
            Items.SPECTRAL_SIGIL_13752 -> Item(Items.SPECTRAL_SPIRIT_SHIELD_13744)
            else -> null
        }
    }
}