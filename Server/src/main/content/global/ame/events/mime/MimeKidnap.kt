package content.global.ame.events.mime

import content.global.ame.RandomEventNPC
import content.global.ame.kidnapPlayer
import core.api.sendMessage
import core.api.utils.WeightBasedTable
import core.game.node.entity.npc.NPC
import core.game.world.map.Location
import org.rs09.consts.NPCs
/**
 * https://www.youtube.com/watch?v=lFKRYDij-hg (Aha, you are required)
 *
 * (proof) - https://www.youtube.com/watch?v=KgbYtytpX5M
 *
 * https://www.youtube.com/watch?v=QHYZeyx5p3I
 */
class MimeKidnap( type: String = "", override var loot: WeightBasedTable? = null) : RandomEventNPC(NPCs.MYSTERIOUS_OLD_MAN_410) {
    val spawnInLocation =  Location(2008, 4764, 0)
    override fun init() {
        super.init()
        face(player)
        sendChat("Aha, you are required ${player.username}!")
        kidnapPlayer(this, player,  spawnInLocation) { _, _ ->  }
    }
    override fun talkTo(npc: NPC) {
        sendMessage(player, "He isn't interested in talking to you.")
    }
}
