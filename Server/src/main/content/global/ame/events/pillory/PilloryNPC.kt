package content.global.ame.events.pillory

import content.global.ame.RandomEventNPC
import content.global.ame.kidnapPlayer
import core.api.*
import core.api.utils.WeightBasedTable
import core.game.component.Component.setUnclosable
import core.game.node.entity.npc.NPC
import org.rs09.consts.NPCs

class PilloryNPC(override var loot: WeightBasedTable? = null) : RandomEventNPC(NPCs.PILLORY_GUARD_2791) {
    override fun init() {
        super.init()
        sendChat("${player.username}, you're under arrest!")
        face(player)
        kidnapPlayer(this, player, PilloryInterface.LOCATIONS.random()) { player, _ ->
            PilloryInterface.initPillory(player)
            setUnclosable(player, player.dialogueInterpreter.sendPlainMessage(true, "", "Solve the pillory puzzle to be returned to where you came from."))
        }
    }

    override fun talkTo(npc: NPC) {
        sendMessage(player, "He isn't interested in talking to you.")
    }
}