package content.region.kandarin.quest.whileguthixsleeps

import core.api.getQuestStage
import core.api.hasLevelStat
import core.api.isQuestComplete
import core.api.rewardXP
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs09.consts.Items

/**
 * While Guthix Sleeps Quest
 *
 * Not completed. Here is the quest journal in color. https://www.youtube.com/watch?v=lOwoy45ywwA
 * Uncomment the @Initializable when it's done.
 *
 */
//@Initializable
class WhileGuthixSleeps : Quest("While Guthix Sleeps", 161, 160, 5,5491, 0, 1, 900) {

    companion object {
        const val questName = "While Guthix Sleeps"
    }
    override fun drawJournal(player: Player, stage: Int) {
        super.drawJournal(player, stage)
        var line = 12
        var stage = getStage(player)

        var started = getQuestStage(player, questName) > 0

        if (!started) {
            line(player, "I can start this quest by speaking to !!Radimus Erkle?? in the", line++, false)
            line(player, "!!Legends' Guild??, which is located in the !!north-east?? of", line++, false)
            line(player, "!!Ardougne??", line++, false)
            line++
            line(player, "To complete this quest, I need:", line++, false)
            line(player, "!!Level 65 Defence??", line++, hasLevelStat(player, Skills.DEFENCE, 65))
            line(player, "!!Level 75 Magic??", line++, hasLevelStat(player, Skills.MAGIC, 75))
            line(player, "!!Level 65 Herblore??", line++, hasLevelStat(player, Skills.HERBLORE, 65))
            line(player, "!!Level 60 Thieving??", line++, hasLevelStat(player, Skills.THIEVING, 60))
            line(player, "!!Level 55 Hunter??", line++, hasLevelStat(player, Skills.HUNTER, 55))
            line(player, "!!Level 65 Farming??", line++, hasLevelStat(player, Skills.FARMING, 65))
            line(player, "!!Level 23 Summoning??", line++, hasLevelStat(player, Skills.SUMMONING, 23))
            line(player, "I also need to have completed the following quests:", line++, false)
            line(player, "!!Recipe for Disaster??", line++, isQuestComplete(player, "Recipe for Disaster"))
            line(player, "!!Mourning's Ends Part II - The Temple of Light??", line++, isQuestComplete(player, "Mourning's End Part II"))
            line(player, "!!Swan Song??", line++, isQuestComplete(player, "Swan Song"))
            line(player, "!!Zogre Flesh Eaters??", line++, isQuestComplete(player, "Zogre Flesh Eaters"))
            line(player, "!!Path of Glouphrie??", line++, isQuestComplete(player, "Path of Glouphrie"))
            line(player, "!!Summer's End??", line++, isQuestComplete(player, "Summer's End"))
            line(player, "!!Legends' Quest??", line++, isQuestComplete(player, "Legends' Quest"))
            line(player, "!!Dream Mentor??", line++, isQuestComplete(player, "Dream Mentor"))
            line(player, "!!Hand in the Sand??", line++, isQuestComplete(player, "The Hand in the Sand"))
            line(player, "!!Tears of Guthix??", line++, isQuestComplete(player, "Tears of Guthix"))
            line(player, "!!King's Ransom??", line++, isQuestComplete(player, "King's Ransom"))
            line(player, "!!Defender of Varrock??", line++, isQuestComplete(player, "Defender of Varrock"))
            line(player, "!!Be eligible for entry to the Warriors' Guild??", line++)
            line(player, "!!Defeated Bork in the Chaos Tunnels??", line++)
            line(player, "!!And gain a total of 270 quest points.??", line++)
            line(player, "I have all the !!stats?? and !!requirements?? needed to start and", line++)
            line(player, "complete this quest.", line++)
        }
    }

    override fun finish(player: Player) {
        var ln = 10
        super.finish(player)
        player.packetDispatch.sendString("You have completed While Guthix Sleeps!", 277, 4)
        player.packetDispatch.sendItemZoomOnInterface(Items.LONGBOW_839,230,277,5)

        drawReward(player, "5 Quest Points", ln++)
        drawReward(player, "Lump of dragon metal.", ln++)
        drawReward(player, "5000 coins.", ln++)
        drawReward(player, "Talk to Idria to receive 4 x", ln++)
        drawReward(player, "100,000 XP.", ln++)
        drawReward(player, "Opportunity to loot Movario's", ln++)
        drawReward(player, "base", ln++)
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }
}