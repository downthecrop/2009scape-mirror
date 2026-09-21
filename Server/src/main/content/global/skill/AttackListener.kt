package content.global.skill

import content.global.skill.slayer.monsters.KillerwattBehavior
import core.api.sendMessage
import core.game.node.entity.combat.CombatStyle
import core.game.interaction.InteractionListener
import core.game.interaction.IntType
import core.game.node.entity.npc.NPC
import org.rs09.consts.NPCs

class AttackListener : InteractionListener {
    override fun defineListeners() {
        flagInstant()
        on(IntType.NPC, "attack") { player, npc ->
            // Makes sure player uses correct attack styles for Lumbridge dummies
            if (npc.id == NPCs.MAGIC_DUMMY_4474 && player.getSwingHandler(false).type != CombatStyle.MAGIC) {
                sendMessage(player, "You can only attack this with magic.")
                return@on true
            }
            if (npc.id == NPCs.MELEE_DUMMY_7891 && player.getSwingHandler(false).type != CombatStyle.MELEE) {
                sendMessage(player, "You must use the training sword to attack this.")
                return@on true
            }
            if (npc.id == NPCs.KILLERWATT_3201 || npc.id == NPCs.KILLERWATT_3202) {
                KillerwattBehavior.aggressUponKillerwatt(player, npc as NPC)
            }
            player.attack(npc)
            return@on true
        }
    }
}