package content.global.skill.herblore

import content.region.kandarin.quest.barbariantraining.BarbarianTraining
import core.game.node.entity.skill.Skills
import core.game.node.entity.player.Player
import core.game.node.Node
import core.game.interaction.InteractionListener
import core.game.interaction.IntType
import core.api.hasLevelStat
import core.api.sendMessage
import core.api.removeItem
import core.api.addItem
import core.api.getAttribute
import core.api.rewardXP
import core.api.sendDialogue
import core.api.setAttribute
import org.rs09.consts.Items

/**
 * Represents the barbarian mixing listener.
 * @author 'Vexia
 * @author treevar
 * @version 2.0
 */

class BarbarianMixListener : InteractionListener {
    override fun defineListeners(){
        for (potion in BarbarianPotion.values()) {
            if (potion.canUseRoe) {
                onUseWith(IntType.ITEM, potion.item, Items.ROE_11324) { player, used, with -> // Roe
                    handle(player, used, with);
                }
            }
            onUseWith(IntType.ITEM, potion.item, Items.CAVIAR_11326) { player, used, with -> // Caviar
                handle(player, used, with);
            }
        }
    }

    fun handle(player: Player, inputPotion: Node, egg: Node): Boolean {
        // Check if player has started barbarian herblore training
        val herbloreStage = getAttribute(player, BarbarianTraining.attributeHerblore, 0)
        if (herbloreStage == 0) {
            return false
        }
        val potion: BarbarianPotion = BarbarianPotion.forId(inputPotion.id) ?: return false

        if (!hasLevelStat(player, Skills.HERBLORE, potion.level)) {
            sendMessage(player, "You need a Herblore level of ${potion.level} to make this mix.")
            return true
        }

        if (!removeItem(player, potion.item)) { // Remove input potion
            return false
        }
        if (!removeItem(player, egg.id)) { // Remove egg used
            addItem(player, potion.item) // Add potion back to inventory if we can't remove the egg
            return false
        }
        addItem(player, potion.product) // Add output potion
        rewardXP(player, Skills.HERBLORE, potion.xp) // Add exp
        sendMessage(player, "You combine your potion with the fish eggs.")
        // Progress Barbarian training if this is your first attack mix
        if (herbloreStage == 1 && potion.product == Items.ATTACK_MIX2_11429) {
            setAttribute(player, BarbarianTraining.attributeHerblore, 2)
            sendDialogue(player, "You feel you have learned more of barbarian ways. Otto might wish to talk to you more.")
        }
        return true
    }
}
