package content.global.skill.herblore

import content.global.skill.herblore.BarbarianPotion
import core.game.node.entity.skill.Skills
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.node.Node
import core.game.interaction.InteractionListener
import core.game.interaction.IntType
import core.api.hasLevelStat
import core.api.sendMessage
import core.api.removeItem
import core.api.addItem
import core.api.rewardXP

/**
 * Represents the barbarian mixing listener.
 * @author 'Vexia
 * @author treevar
 * @version 2.0
 */

class BarbarianMixListener : InteractionListener {
    override fun defineListeners(){
        for (potion in BarbarianPotion.values()) {
            if (potion.isBoth()) {
                onUseWith(IntType.ITEM, potion.getItem(), 11324) { player, used, with -> //Roe
                    handle(player, used, with);
                }
            }
            onUseWith(IntType.ITEM, potion.getItem(), 11326) { player, used, with -> //Caviar
                handle(player, used, with);
            }
        }
    }

    fun handle(player: Player, inputPotion: Node, egg: Node): Boolean {
        val potion: BarbarianPotion? = BarbarianPotion.forId(inputPotion.getId())
        if(potion == null){
            return false
        }

        if (!hasLevelStat(player, Skills.HERBLORE, potion.getLevel())) {
            sendMessage(player, "You need a herblore level of " + potion.getLevel().toString() + " to make this mix.")
            return true
        }

        if(!removeItem(player, potion.getItem())) { //Remove input potion
            return false
        }
        if(!removeItem(player, egg.getId())) { //Remove egg used
            addItem(player, potion.getItem()) //Add potion back to inventory if we can't remove the egg
            return false
        }
        addItem(player, potion.getProduct()) //Add output potion
        rewardXP(player, Skills.HERBLORE, potion.getExp()) //Add exp
        return true
    }
}
