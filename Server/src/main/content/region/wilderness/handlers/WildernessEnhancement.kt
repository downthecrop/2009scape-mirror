package content.region.wilderness.handlers

import core.api.*
import core.tools.*
import core.ServerConstants
import core.game.interaction.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.IronmanMode
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.node.item.Item
import core.game.world.GameWorld
import core.game.world.map.Location
import content.global.handlers.scenery.BankBoothListener
import org.rs09.consts.*

class WildernessEnhancement : StartupListener, InteractionListener {
    var enabled = false

    override fun startup() {
        enabled = ServerConstants.ENHANCED_WILDERNESS && GameWorld.settings!!.wild_pvp_enabled
        if (enabled) {
            for (location in abyssalDemonSpawns) {
                val npc = core.game.node.entity.npc.NPC.create(NPCs.ABYSSAL_DEMON_1615, location, Unit)
                npc.isWalks = true
                npc.isNeverWalks = false
                npc.isRespawn = true
                npc.init()
            }
            for (location in darkBeastSpawns) {
                val npc = core.game.node.entity.npc.NPC.create(NPCs.DARK_BEAST_2783, location, Unit)
                npc.isWalks = true
                npc.isNeverWalks = false
                npc.isRespawn = true
                npc.init()
            }
            for (location in pyrefiendSpawns) {
                val npc = core.game.node.entity.npc.NPC.create(NPCs.PYREFIEND_1636, location, Unit)
                npc.isWalks = true
                npc.isNeverWalks = false
                npc.isRespawn = true
                npc.init()
            }
            for (location in monkfishSpawns) {
                val npc = core.game.node.entity.npc.NPC.create(NPCs.FISHING_SPOT_3848, location, Unit)
                npc.isWalks = false
                npc.isNeverWalks = true
                npc.isRespawn = true
                npc.init()
            }
            val edward = core.game.node.entity.npc.NPC.create(NPCs.EDWARD_1782, edwardSpawn, Unit)
            edward.isWalks = false
            edward.isRespawn = true
            edward.init()
            SceneryBuilder.add(Scenery(14859, runiteSpawn))
            registerEdwardNoteListener()
        }
    }

    override fun defineListeners() {}

    fun registerEdwardNoteListener() {
        onUseAnyWith(IntType.NPC, NPCs.EDWARD_1782) { player, used, with -> 
            if (!ServerConstants.BANK_BOOTH_NOTE_UIM && player.ironmanManager.checkRestriction(IronmanMode.ULTIMATE)) {
                return@onUseAnyWith true
            }
            BankBoothListener.convertToNotes(used, player)
            return@onUseAnyWith true
        }
    }

    companion object {
        val abyssalDemonSpawns = arrayOf (
            Location.create(3368, 3894, 0),
            Location.create(3373, 3891, 0),
            Location.create(3368, 3898, 0),
            Location.create(3367, 3889, 0),
            Location.create(3365, 3897, 0),
            Location.create(3364, 3886, 0),
            Location.create(3383, 3893, 0) 
        )
        val darkBeastSpawns = arrayOf (
            Location.create(3214, 3933, 0),
            Location.create(3224, 3927, 0),
            Location.create(3213, 3915, 0),
            Location.create(3235, 3919, 0),
            Location.create(3237, 3931, 0) 
        )
        val pyrefiendSpawns = arrayOf (
            Location.create(3350, 3926, 0),
            Location.create(3357, 3927, 0),
            Location.create(3372, 3923, 0),
            Location.create(3378, 3939, 0),
            Location.create(3368, 3943, 0),
            Location.create(3367, 3951, 0) 
        )
        val edwardSpawn = Location.create(3023, 3952, 0) 
        val runiteSpawn = Location.create(3059, 3940, 0) 
        val monkfishSpawns = arrayOf (
            Location.create(3026, 3957, 0),
            Location.create(3022, 3960, 0) 
        )
    }
}
