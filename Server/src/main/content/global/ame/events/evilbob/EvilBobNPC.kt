package content.global.ame.events.evilbob

import content.global.ame.RandomEventNPC
import content.global.ame.kidnapPlayer
import core.api.*
import core.api.utils.WeightBasedTable
import core.game.node.entity.npc.NPC
import core.game.world.map.Location
import org.rs09.consts.NPCs

class EvilBobNPC(override var loot: WeightBasedTable? = null) : RandomEventNPC(NPCs.EVIL_BOB_2478) {
    override fun init() {
        super.init()
        sendChat("meow")
        face(player)
        kidnapPlayer(this, player, Location(3419, 4776, 0), "No... what? Nooooooooooooo!") { player, _ ->
            EvilBobUtils.giveEventFishingSpot(player)
            sendMessage(player, "Welcome to Scape2009.")
            openDialogue(player, EvilBobDialogue(), NPCs.EVIL_BOB_2479)
        }
    }

    override fun talkTo(npc: NPC) {
        openDialogue(player, EvilBobDialogue(), this.asNpc())
    }
}