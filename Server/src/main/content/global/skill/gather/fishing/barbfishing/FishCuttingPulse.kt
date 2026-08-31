package content.global.skill.gather.fishing.barbfishing

import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import org.rs09.consts.Items

/**
 * Pulse used for cutting fish into fish offcuts
 * @param player the player running the pulse
 * @param fish the fish being cut
 * @author Ceikry
 * @author Bishop
 */

class FishCuttingPulse(val player: Player, val fish: Int) : Pulse(0){
    fun checkRequirements(): Boolean {
        if(!(freeSlots(player) >= 2 ||
            (freeSlots(player) >= 1 && inInventory(player, Items.FISH_OFFCUTS_11334)))){
            sendMessage(player, "You don't have enough space in your pack to attempt cutting open the fish.")
            return false
        }
        return true
    }

    override fun pulse(): Boolean {

        val odds = when (fish) {
            Items.LEAPING_TROUT_11328 -> 100
            Items.LEAPING_SALMON_11330 -> 85
            Items.LEAPING_STURGEON_11332 -> 70
            else -> return false
        }

        val offcutSuccess = RandomFunction.random(odds) < 50
        val roeSuccess    = RandomFunction.random(100)  < 50

        if (removeItem(player, fish.asItem())) {
            animate(player, Animation(5244))
            when {
                (offcutSuccess && roeSuccess) -> {
                    addItemOrDrop(player, Items.FISH_OFFCUTS_11334)
                    if (fish == Items.LEAPING_STURGEON_11332) {
                        addItemOrDrop(player, Items.CAVIAR_11326)
                        rewardXP(player, Skills.COOKING, 15.0)
                        sendMessage(player, "You cut open the fish and extract some fish cuts and caviar.")
                    } else {
                        addItemOrDrop(player, Items.ROE_11324)
                        rewardXP(player, Skills.COOKING, 10.0)
                        sendMessage(player, "You cut open the fish and extract some fish cuts and roe.")
                    }
                }
                (roeSuccess) -> {
                    if (fish == Items.LEAPING_STURGEON_11332) {
                        addItemOrDrop(player, Items.CAVIAR_11326)
                        rewardXP(player, Skills.COOKING, 15.0)
                        sendMessage(player, "You cut open the fish and extract caviar, but the rest of the fish is reduced to")
                        sendMessage(player, "useless fragments, which you discard.")
                    } else  {
                        addItemOrDrop(player, Items.ROE_11324)
                        rewardXP(player, Skills.COOKING, 10.0)
                        sendMessage(player, "You cut open the fish and extract roe, but the rest of the fish is reduced to")
                        sendMessage(player, "useless fragments, which you discard.")
                    }
                }
                // It appears that you can't get cutoffs without also getting roe or caviar
                // Thus, passing only the offcuts roll is a total failure
                else -> {
                    sendMessage(player, "You fail to gain anything useful and reduce the fish to fragments, not even")
                    sendMessage(player, "usable as bait.")
                }
            }
        return true
        }
        else return false
    }
}
