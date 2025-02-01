package content.region.kandarin.witchhaven.dialogue

import content.region.kandarin.witchhaven.quest.seaslug.KennithDialogueFile
import core.api.*
import core.game.dialogue.DialoguePlugin
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs09.consts.NPCs

class KennithDialogue : InteractionListener {

    override fun defineListeners() {
        on(NPCs.KENNITH_4864, IntType.NPC, "talk-to"){ player, npc ->
            openDialogue(player, KennithDialogueFile(), npc)
            return@on true
        }
    }

    // Because Kennith is behind the counter
    override fun defineDestinationOverrides() {
        setDest(IntType.NPC, intArrayOf(NPCs.KENNITH_4864),"talk-to"){ _, _ ->
            return@setDest Location.create(2765, 3286, 1)
        }
    }
}

// INSTEAD OF THIS as Kennith is unreachable.
//@Initializable
//class KennithDialogue(player: Player? = null) : DialoguePlugin(player){
//    override fun newInstance(player: Player): DialoguePlugin {
//        return KennithDialogue(player)
//    }
//    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
//        // Fallback to default. Always the start of Sea Slug
//        openDialogue(player!!, KennithDialogueFile(), npc)
//        return true
//    }
//    override fun getIds(): IntArray {
//        // Base is CAROLINE_697 (Should be named KENNITH_697)
//        return intArrayOf(NPCs.CAROLINE_697, NPCs.KENNITH_4864)
//    }
//}
