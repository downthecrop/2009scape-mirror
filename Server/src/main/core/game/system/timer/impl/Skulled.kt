package core.game.system.timer.impl

import core.api.*
import core.game.system.timer.*
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import org.json.simple.*

class Skulled : PersistTimer (1, "skulled") {
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
