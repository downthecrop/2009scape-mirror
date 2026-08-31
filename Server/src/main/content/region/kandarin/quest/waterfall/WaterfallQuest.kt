package content.region.kandarin.quest.waterfall

import content.data.Quests
import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs09.consts.Components
import org.rs09.consts.Items

/**
 * Waterfall Quest
 * @author Splinter - original
 * @author Bishop - refactor
 */

@Initializable
class WaterfallQuest : Quest(Quests.WATERFALL_QUEST, 65, 64, 1, 65, 0, 1, 10) {

    private val questVarp = 65

    override fun drawJournal(player: Player, stage: Int) {
        super.drawJournal(player, stage)

        var ln = 11
        var stage = getStage(player)
        var started = getQuestStage(player, Quests.WATERFALL_QUEST) > 0

        val hasPebble = (inInventory(player, Items.GLARIALS_PEBBLE_294))
        val hasAmulet = (inInventory(player, Items.GLARIALS_AMULET_295) || inEquipment(player, Items.GLARIALS_AMULET_295))
        val inChamber = ((player.location.x in 2561..2570 && player.location.y in 9902..9917) || (player.location.x in 2599..2608 && player.location.y in 9901..9916))

        if (!started) {
            line(player, "I can start this quest by speaking to !!Almera?? in her house", ln++)
            line(player, "next to the !!Baxtorian Falls.", ln++)
            line(player, "", ln++)
            line(player, "I need to be able to fight !!Level 84 Giants.", ln++)
            limitScrolling(player, ln + 6, true)
        } else {
            if (stage >= 10) {
                line(player, "I spoke to !!Almera?? in a house close to the Baxtorian", ln++, stage >= 20)
                line(player, "waterfall. Her son was missing so I offered to help find", ln++, stage >= 20)
                line(player, "him. The boy, !!Hudon's?? looking for treasure in the waterfall.", ln++, stage >= 20)
                ln++
            }
            if (stage >= 20) {
                line(player, "I found Hudon a short raft ride down the river. He is", ln++, stage >= 30)
                line(player, "convinced there is treasure here somewhere. Maybe I", ln++, stage >= 30)
                line(player, "need to do a little research.", ln++, stage >= 30)
                ln++
            }
            if (stage >= 30) {
                line(player, "I found a book in the tourist office about Baxtorian. The", ln++, true)
                line(player, "book tells of a sad love story about 2 elf lovers. It ends", ln++, true)
                line(player, "with Baxtorian withdrawing to his home under the waterfall", ln++, true)
                line(player, "after his wife dies. It is told that only Glarial could enter", ln++, true)
                line(player, "their home.", ln++, true)
                ln++
                line(player, "The book also mentions !!Glarial's tomb?? and a pebble, it", ln++, stage == 100 || hasAmulet || inChamber)
                line(player, "appears that the pebble is used to enter the tomb.", ln++, stage == 100 || hasAmulet || inChamber)
                line(player, "From what I understand Glarial's pebble was hidden in a", ln++, stage == 100 || hasPebble || hasAmulet || inChamber)
                line(player, "cave under the Tree Gnome Village by Golrie's ancestors.", ln++, stage == 100 || hasPebble || hasAmulet || inChamber)
                ln++
            }
            if (stage >= 30 && (stage == 100 || hasAmulet || inChamber)) {
                line(player, "Inside the tomb I found Glarial's amulet and ashes.", ln++, stage == 100 || inChamber)
                ln++
            }
            if (stage >= 30 && (stage == 100 || inChamber)) {
                line(player, "I finally got access to the derelict home of Baxtorian. The", ln++, true)
                line(player, "door must have been keyed to Glarial's amulet.", ln++, true)
                line(player, "", ln++)
                line(player, "I have found a room containing the !!Chalice of Eternity.?? The", ln++, stage == 100)
                line(player, "chalice is suspended in midair just out of reach.", ln++, stage == 100)
                ln++
            }
            if (stage == 100) {
                line(player, "Using Glarial's ashes as a counterweight, I was able to", ln++, true)
                line(player, "remove the treasure that had been left in the chalice.", ln++, true)
                ln++
                line(player, "!!QUEST COMPLETE!", ln++)
            }
            limitScrolling(player, ln, false)
        }
    }

    override fun finish(player: Player) {
        var ln = 10
        super.finish(player)

        setInterfaceText(player, "You have completed the Waterfall Quest!", Components.QUEST_COMPLETE_SCROLL_277, 4)
        sendItemZoomOnInterface(player,Components.QUEST_COMPLETE_SCROLL_277, 5, Items.DIAMOND_1601, 230)

        drawReward(player, "1 Quest Point", ln++)
        drawReward(player, "13,750 Strength XP", ln++)
        drawReward(player, "13,750 Attack XP", ln++)
        drawReward(player, "2 diamonds", ln++)
        drawReward(player, "2 gold bars", ln++)
        drawReward(player, "40 Mithril seeds", ln++)

        rewardXP(player, Skills.STRENGTH, 13750.0)
        rewardXP(player, Skills.ATTACK, 13750.0)

        addItemOrDrop(player, Items.DIAMOND_1601, 2)
        addItemOrDrop(player, Items.GOLD_BAR_2357, 2)
        addItemOrDrop(player, Items.MITHRIL_SEEDS_299, 40)
    }

    override fun setStage(player: Player, stage: Int) {
        super.setStage(player, stage)
        this.updateVarps(player)
    }

    override fun updateVarps(player: Player) {
        setVarp(player, questVarp, (getQuestStage(player, Quests.WATERFALL_QUEST) / 10), true)
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }

}