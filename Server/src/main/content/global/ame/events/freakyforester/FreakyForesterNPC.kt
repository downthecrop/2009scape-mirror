package content.global.ame.events.freakyforester

import content.global.ame.RandomEventNPC
import content.global.ame.kidnapPlayer
import core.api.*
import org.rs09.consts.NPCs
import core.api.utils.WeightBasedTable
import core.game.node.entity.npc.NPC
import core.game.world.map.Location

class FreakyForesterNPC(override var loot: WeightBasedTable? = null) : RandomEventNPC(NPCs.FREAKY_FORESTER_2458) {
    override fun init() {
        super.init()
        sendChat("Ah, ${player.username}, just the person I need!")
        face(player)
        kidnapPlayer(this, player, Location(2599, 4777, 0)) { player, _ ->
            FreakUtils.giveFreakTask(player)
            openDialogue(player, FreakyForesterDialogue(), FreakUtils.freakNpc)
        }
    }

    override fun talkTo(npc: NPC) {
        player.dialogueInterpreter.open(FreakyForesterDialogue(),npc)
    }
}