package content.region.misc.entrana.handlers

import content.global.travel.ship.Ships
import core.api.*
import core.cache.def.impl.ItemDefinition
import core.game.dialogue.DialogueFile
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.tools.END_DIALOGUE
import org.rs09.consts.NPCs

/**
 * Listeners for the Entrana monks' fast-travel option
 * @author Player Name
 */

fun sail(player: Player?, ship: Ships) {
    ship.sail(player)
    playJingle(player!!, 172)
}

class EntranaListeners : InteractionListener {
    override fun defineListeners() {
        for (npc in arrayOf(NPCs.MONK_OF_ENTRANA_2730, NPCs.MONK_OF_ENTRANA_658, NPCs.MONK_OF_ENTRANA_2731)) {
            on(npc, IntType.NPC, "take-boat") { player, _ ->
                sail(player, Ships.ENTRANA_TO_PORT_SARIM)
                return@on true
            }
        }

        for (npc in arrayOf(NPCs.MONK_OF_ENTRANA_2728, NPCs.MONK_OF_ENTRANA_657, NPCs.MONK_OF_ENTRANA_2729)) {
            on(npc, IntType.NPC, "take-boat") { player, _ ->
                if (!ItemDefinition.canEnterEntrana(player)) {
                    openDialogue(player, object : DialogueFile() {
                        override fun handle(componentID: Int, buttonID: Int) {
                            this.npc = NPC(npc)
                            when (stage) {
                                0 -> npc("NO WEAPONS OR ARMOUR are permitted on holy", "Entrana AT ALL. We will not allow you to travel there", "in breach of mighty Saradomin's edict.").also { stage++ }
                                1 -> npc("Do not try to deceive us again. Come back when you", "have laid down your Zamorakian instruments of death.").also { stage = END_DIALOGUE }
                            }
                        }
                    })
                    return@on true
                }
                sail(player, Ships.PORT_SARIM_TO_ENTRANA)
                if (!player.achievementDiaryManager.getDiary(DiaryType.FALADOR).isComplete(0, 14)) {
                    player.achievementDiaryManager.getDiary(DiaryType.FALADOR).updateTask(player, 0, 14, true)
                }
                return@on true
            }
        }
    }
}
