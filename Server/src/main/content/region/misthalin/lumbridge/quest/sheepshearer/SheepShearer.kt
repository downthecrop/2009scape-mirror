package content.region.misthalin.lumbridge.quest.sheepshearer

import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs09.consts.Items
import kotlin.math.min

@Initializable
class SheepShearer : Quest("Sheep Shearer", 28, 27, 1, 179, 0, 20, 21) {
    companion object {
        val ATTR_NUM_BALLS_OF_WOOL_DELIVERED = "/save:sheep-shearer:num-balls-of-wool-delivered"
        val ATTR_IS_PENGUIN_SHEEP_SHEARED = "/save:sheep-shearer:is-penguin-sheep-sheared"

        /**
         * Gets the number of balls of wool that have been delivered to Fred the Farmer.
         * @param player the player to check
         * @return the number of balls of wool that have been delivered
         */
        fun getBallsOfWoolDelivered(player: Player): Int {
            return getAttribute(player, ATTR_NUM_BALLS_OF_WOOL_DELIVERED, 0)
        }

        /**
         * Gets the number of balls of wool remaining to be delivered to Fred the Farmer.
         * @param player the player to check
         * @return the number of balls of wool remaining to be delivered
         */
        fun getBallsOfWoolRequired(player: Player): Int {
            return 20 - getBallsOfWoolDelivered(player)
        }

        /**
         * Gets the number of balls of wool that can be removed from the player's inventory,
         * up to the amount still required by Fred the Farmer.
         * @param player the player whose inventory should be checked
         * @return the number of balls of wool that can be removed from the player's inventory
         * and delivered to Fred the Farmer
         */
        fun getBallsOfWoolToRemove(player: Player): Int {
            return min(getBallsOfWoolRequired(player), amountInInventory(player, Items.BALL_OF_WOOL_1759))
        }

        /**
         * Gets the number of balls of wool that still need to be collected by the player.
         *
         * This takes into account the number of balls of wool already delivered to Fred the
         * Farmer, and the number of balls of wool in the player's inventory.
         * @param player the player to check
         * @return the number of balls of wool that still needs to be collected by the player
         */
        fun getBallsOfWoolToCollect(player: Player): Int {
            return getBallsOfWoolRequired(player) - getBallsOfWoolToRemove(player)
        }

        /**
         * Delivers all balls of wool in the player's inventory, up to the amount still required
         * by Fred the Farmer.
         * @param player the player delivering balls of wool to Fred the Farmer
         * @return the number of balls of wool removed from the player's inventory and delivered to Fred the Farmer
         */
        fun deliverBallsOfWool(player: Player): Int {
            val removeAmount = getBallsOfWoolToRemove(player)
            if (removeItem(player, Item(Items.BALL_OF_WOOL_1759, removeAmount))) {
                setAttribute(player, ATTR_NUM_BALLS_OF_WOOL_DELIVERED, getBallsOfWoolDelivered(player) + removeAmount)
                return removeAmount
            }
            return 0
        }
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }

    override fun drawJournal(player: Player, stage: Int) {
        super.drawJournal(player, stage)

        var ln = 11

        if (stage == 0) {
            line(player, "I can start this quest by speaking to !!Farmer Fred?? at his", ln++)
            line(player, "!!farm?? just a little way !!North West of Lumbridge??", ln++)
        } else if (stage == 10 || stage == 90) {
            line(player, "<col=000000>I asked Farmer Fred, near Lumbridge, for a quest. Fred</col>", ln++, true)
            line(player, "<col=000000>said he'd pay me for shearing his sheep for him!</col>", ln++, true)
            ln++

            val ballsOfWoolToCollect = getBallsOfWoolToCollect(player)
            if (ballsOfWoolToCollect == 0) {
                line(player, "I have enough !!balls of wool?? to give !!Fred?? and get my !!reward??", ln++)
                line(player, "!!money!??", ln++)
            } else {
                line(player, "I need to collect $ballsOfWoolToCollect more !!balls of wool.??", ln++)
            }
        } else if (stage == 100) {
            line(player, "<col=000000>I brought Farmer Fred 20 balls of wool, and he paid me for</col>", ln++, true)
            line(player, "<col=000000>it!</col>", ln++, true)
            ln++
            line(player, "<col=FF0000>QUEST COMPLETE!</col>", ln++)
        }
    }

    override fun finish(player: Player?) {
        super.finish(player)
        player ?: return

        var ln = 10
        player.packetDispatch.sendItemZoomOnInterface(Items.SHEARS_1735, 240, 277, 5)
        drawReward(player, "1 Quest Point", ln++)
        drawReward(player, "150 Crafting XP", ln++)
        drawReward(player, "60 coins", ln++)
        rewardXP(player, Skills.CRAFTING, 150.0)
        addItemOrDrop(player, Items.COINS_995, 60)
        removeAttribute(player, ATTR_NUM_BALLS_OF_WOOL_DELIVERED)
        removeAttribute(player, ATTR_IS_PENGUIN_SHEEP_SHEARED)
    }
}