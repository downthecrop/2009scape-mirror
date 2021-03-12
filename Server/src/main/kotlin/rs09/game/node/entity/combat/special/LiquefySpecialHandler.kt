package rs09.game.node.entity.combat.special

import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import rs09.game.node.entity.combat.handlers.MeleeSwingHandler
import core.plugin.Initializable
import core.plugin.Plugin

@Initializable
class LiquefySpecialHandler : MeleeSwingHandler(), Plugin<Any> {
    override fun newInstance(arg: Any?): Plugin<Any> {
        CombatStyle.MELEE.swingHandler.register(11037,this)
        return this
    }

    override fun fireEvent(identifier: String?, vararg args: Any?): Any {
        return Unit
    }

    override fun swing(entity: Entity?, victim: Entity?, state: BattleState?): Int {
        entity?.asPlayer()?.sendMessage("You need to be underwater to use this special attack.")
        entity?.asPlayer()?.settings?.isSpecialToggled = false
        return super.swing(entity, victim, state)
    }

}