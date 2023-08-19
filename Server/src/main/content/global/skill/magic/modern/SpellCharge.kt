package content.global.skill.magic.modern

import core.api.getAudio
import core.api.playAudio
import core.api.sendMessage
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.system.timer.PersistTimer

class SpellCharge : PersistTimer (700, "magic:spellcharge") {
    override fun run (entity: Entity) : Boolean {
        if (entity !is Player) return false
        sendMessage(entity, "Your magical charge fades away.")
        playAudio(entity, getAudio(1650))
        return false
    }
}
