package rs09.game.content.dialogue.region.piscatoris

import core.plugin.Initializable
import org.rs09.consts.NPCs
import rs09.game.interaction.InteractionListener
import rs09.game.node.entity.npc.BankerNPC

/**
 * Provides banker interactions for Arnold Lydspor
 *
 * @author vddCore
 */
@Initializable
class ArnoldLydsporListener : InteractionListener {
    companion object {
        val NPC_IDS = intArrayOf(NPCs.ARNOLD_LYDSPOR_3824)
    }

    override fun defineListeners() {
        on(NPC_IDS, NPC, "bank", handler = BankerNPC::attemptBank)
        on(NPC_IDS, NPC, "collect", handler = BankerNPC::attemptCollect)
        /* Talk-to handled by NPCTalkListener */
    }
}