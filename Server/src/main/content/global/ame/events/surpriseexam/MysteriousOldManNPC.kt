package content.global.ame.events.surpriseexam

import content.global.ame.RandomEventNPC
import content.global.ame.kidnapPlayer
import core.api.*
import org.rs09.consts.NPCs
import core.api.utils.WeightBasedTable
import core.game.node.entity.npc.NPC
import core.game.world.map.Location

class MysteriousOldManNPC(var type: String = "", override var loot: WeightBasedTable? = null) : RandomEventNPC(NPCs.MYSTERIOUS_OLD_MAN_410) {
    override fun init() {
        super.init()
        sendChat("Surprise exam, ${player.username}!")
        face(player)
        kidnapPlayer(this, player, Location(1886, 5025, 0)) { _, _ ->
            /* nothing needed */
        }
    }

    override fun talkTo(npc: NPC) {
        sendMessage(player, "He isn't interested in talking to you.")
    }
}
