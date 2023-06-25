package content.global.activity.shootingstar

import core.api.*
import core.game.system.timer.*
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import org.json.simple.*

class StarBonus : PersistTimer (1, "shootingstar:bonus") {
    var ticksLeft = 1500

    override fun save (root: JSONObject, entity: Entity) {
        root["ticksLeft"] = ticksLeft.toString()
    }

    override fun parse (root: JSONObject, entity: Entity) {
        ticksLeft = root["ticksLeft"].toString().toInt()
    }

    override fun run (entity: Entity) : Boolean {
        if (entity is Player && ticksLeft == 500) {
            entity.sendMessage("<col=f0f095>You have 5 minutes of your mining bonus left</col>")
        } else if (entity is Player && ticksLeft == 0) {
            entity.sendMessage("<col=FF0000>Your mining bonus has run out!</col>")
        }
        return ticksLeft-- > 0
    }
}
