package core.game.system.timer.impl

import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.system.timer.PersistTimer
import core.game.system.timer.RSTimer
import core.game.system.timer.TimerFlag

class Skulled : PersistTimer (1, "skulled", flags = arrayOf(TimerFlag.ClearOnDeath)) {
    override fun onRegister (entity: Entity) {
        if (entity !is Player) return
        entity.skullManager.setSkullIcon(0)
        entity.skullManager.setSkulled(true)
    }

    override fun run (entity: Entity) : Boolean {
        if (entity !is Player) return false
        entity.skullManager.reset()
        return false
    }

    override fun getTimer (vararg args: Any) : RSTimer {
        val t = Skulled()
        t.runInterval = args.getOrNull(0) as? Int ?: 500
        return t
    }
}
