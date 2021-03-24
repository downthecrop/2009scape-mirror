package rs09.game.node.entity.skill.magic

import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Items
import rs09.game.interaction.InteractionListener
import rs09.game.node.entity.skill.magic.spellconsts.Modern

class SpellTablets : InteractionListener() {
    val B2P_TABLET = Items.BONES_TO_PEACHES_8015
    val B2B_TABLET = Items.BONES_TO_BANANAS_8014
    override fun defineListeners() {

        on(B2B_TABLET,ITEM,"break"){player, node ->
            breakTablet(player)
            SpellListeners.run(Modern.BONES_TO_BANANAS,SpellListener.NONE,"modern",player)
            player.inventory.remove(Item(node.id))
            return@on true
        }

        on(B2P_TABLET,ITEM,"break"){player, node ->
            breakTablet(player)
            SpellListeners.run(Modern.BONES_TO_PEACHES,SpellListener.NONE,"modern",player)
            player.inventory.remove(Item(node.id))
            return@on true
        }

    }

    fun breakTablet(player: Player){
        player.audioManager.send(979)
        player.animator.forceAnimation(Animation(4069))
        player.lock(5)
        player.setAttribute("tablet-spell",true)
    }
}