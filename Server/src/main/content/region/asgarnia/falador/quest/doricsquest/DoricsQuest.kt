package content.region.asgarnia.falador.quest.doricsquest

import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs09.consts.Components
import org.rs09.consts.Items

@Initializable
class DoricsQuest : Quest("Doric's Quest", 17, 16, 1, 31, 0, 1, 100) {
    override fun newInstance(`object`: Any?): Quest { return this }

    override fun drawJournal(player: Player?, stage: Int) {
        super.drawJournal(player, stage)
        player ?: return
        var line = 11
        if(stage == 0) {
            line(player, "I can start this quest by speaking to !!Doric?? who is !!North of??", line++)
            line(player, "!!Falador??.", line++)
            line(player, "There aren't any requirements but !!Level 15 Mining?? will help.", line++)
        } else {
            if(stage in 1..99) {
                line(player, "I have spoken to !!Doric??.", line++)
                line(player, "I need to collect some items and bring them to !!Doric??:", line++)
                line(player, "6 Clay", line++, inInventory(player, Items.CLAY_434, 6))
                line(player, "4 Copper Ore", line++, inInventory(player, Items.COPPER_ORE_436, 4))
                line(player, "2 Iron Ore", line++, inInventory(player, Items.IRON_ORE_440, 2))
            }

            if(stage == 100) {
                line(player, "I have spoken to !!Doric??.", line++, true)
                line(player, "I have collected some Clay, Copper Ore, and Iron Ore.", line++, true)
                line(player, "Doric rewarded me for all my hard work.", line++, true)
                line(player, "I can now use Doric's Anvils whenever I want.", line++, true)
                line++
                line(player, "%%QUEST COMPLETE!&&", line++)
            }
        }
    }

    override fun finish(player: Player?) {
        super.finish(player)
        player ?: return
        var line = 10

        sendItemZoomOnInterface(player, Components.QUEST_COMPLETE_SCROLL_277, 5, Items.STEEL_PICKAXE_1269)
        drawReward(player, "1 Quest Point", line++)
        drawReward(player, "1300 Mining XP", line++)
        drawReward(player, "180 Coins", line++)
        drawReward(player, "Use of Doric's Anvils", line++)

        rewardXP(player, Skills.MINING, 1300.0)
        addItemOrDrop(player, Items.COINS_995, 180)
        removeAttribute(player, "doric-angy-count")
    }
}