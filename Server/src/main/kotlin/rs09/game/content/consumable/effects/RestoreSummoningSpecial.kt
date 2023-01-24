package rs09.game.content.consumable.effects

import core.game.content.consumable.ConsumableEffect
import core.game.node.entity.player.Player

class RestoreSummoningSpecial : ConsumableEffect(){
    override fun activate(p: Player) {
        val f = p.familiarManager.familiar
        f?.updateSpecialPoints(-15)
    }
}