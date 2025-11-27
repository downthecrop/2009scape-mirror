package content.global.ame.events.candlelight

import content.global.ame.RandomEventNPC
import content.global.ame.kidnapPlayer
import core.api.*
import core.api.utils.WeightBasedTable
import core.game.node.entity.npc.NPC
import core.game.world.map.Location
import org.rs09.consts.NPCs

class PiousPeteNPC(override var loot: WeightBasedTable? = null) : RandomEventNPC(NPCs.PRIEST_3206) {
    override fun init() {
        super.init()
        // Supposed to be "I'm sorry to drag you away from your tasks, but I need a little help with something." but it's too goddamn long.
        sendChat("${player.username}! I need a little help with something.")
        face(player)
        kidnapPlayer(this, player, Location(1972, 5002, 0)) { player, _ ->
            CandlelightInterface.initCandlelight(player)
            openDialogue(player, PiousPeteStartingDialogueFile(), NPC(NPCs.PIOUS_PETE_3207))
        }
    }

    override fun talkTo(npc: NPC) {
        player.dialogueInterpreter.open(PiousPeteDialogueFile(),npc)
    }
}