package content.global.skill

import core.game.node.entity.combat.CombatStyle
import core.game.interaction.InteractionListener
import core.game.interaction.IntType

class AttackListener : InteractionListener {
    override fun defineListeners() {
        flagInstant()
        on(IntType.NPC, "attack"){ player, npc ->
            //Makes sure player uses correct attack styles for lumbridge dummies
            if (npc.id == 4474 && player.getSwingHandler(false).type != CombatStyle.MAGIC) {
                player.sendMessage("You can only attack this with magic.")
                return@on true
            }
            if (npc.id == 7891 && player.getSwingHandler(false).type != CombatStyle.MELEE) {
                player.sendMessage("You must use the training sword to attack this.")
                return@on true
            }
            player.attack(npc)
            return@on true
        }
    }
}