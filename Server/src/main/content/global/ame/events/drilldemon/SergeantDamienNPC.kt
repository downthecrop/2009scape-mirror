package content.global.ame.events.drilldemon

import core.game.node.entity.npc.NPC
import org.rs09.consts.NPCs
import content.global.ame.RandomEventNPC
import content.global.ame.kidnapPlayer
import core.api.*
import core.api.utils.WeightBasedTable

import core.game.world.map.Location

class SergeantDamienNPC(override var loot: WeightBasedTable? = null) : RandomEventNPC(NPCs.SERGEANT_DAMIEN_2790) {
    override fun init() {
        super.init()
        sendChat("${player.username}! Drop and give me 20!")
        face(player)
        kidnapPlayer(this, player, Location(3163, 4819, 0)) { player, _ ->
            player.interfaceManager.closeDefaultTabs()
            setComponentVisibility(player, 548, 69, true)
            setComponentVisibility(player, 746, 12, true)
            openDialogue(player, SeargentDamienDialogue(isCorrect = true, eventStart = true), NPCs.SERGEANT_DAMIEN_2790)
        }
    }

    override fun talkTo(npc: NPC) {
        openDialogue(player, SeargentDamienDialogue(), npc)
    }
}