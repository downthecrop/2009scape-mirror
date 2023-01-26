package content.data.consumables.effects

import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player

class RestoreSummoningSpecial : ConsumableEffect(){
    override fun activate(p: Player) {
        val f = p.familiarManager.familiar
        f?.updateSpecialPoints(-15)
    }
}