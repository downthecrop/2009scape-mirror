package content.region.kandarin.quest.whileguthixsleeps

import core.api.getQuestStage
import core.api.hasLevelStat
import core.api.isQuestComplete
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import org.rs09.consts.Items
import content.data.Quests

/**
 * While Guthix Sleeps Quest
 *
 * Not completed. Here is the quest journal in color. https://www.youtube.com/watch?v=lOwoy45ywwA
 * Uncomment the @Initializable when it's done.
 *
 */
//@Initializable
class WhileGuthixSleeps : Quest(Quests.WHILE_GUTHIX_SLEEPS, 161, 160, 5, 5491, 0, 1, 900) {

    override fun drawJournal(player: Player, stage: Int) {
        super.drawJournal(player, stage)
        var line = 12
        var stage = getStage(player)

        var started = getQuestStage(player, Quests.WHILE_GUTHIX_SLEEPS) > 0

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
            line(
                player,
                "!!Recipe for Disaster??",
                line++,
                isQuestComplete(player, Quests.RECIPE_FOR_DISASTER)
            )
            line(
                player,
                "!!Mourning's Ends Part II - The Temple of Light??",
                line++,
                isQuestComplete(player, Quests.MOURNINGS_END_PART_II)
            )
            line(player, "!!Swan Song??", line++, isQuestComplete(player, Quests.SWAN_SONG))
            line(
                player,
                "!!Zogre Flesh Eaters??",
                line++,
                isQuestComplete(player, Quests.ZOGRE_FLESH_EATERS)
            )
            line(player, "!!Path of Glouphrie??", line++, isQuestComplete(player, Quests.THE_PATH_OF_GLOUPHRIE))
            line(player, "!!Summer's End??", line++, isQuestComplete(player, Quests.SUMMERS_END))
            line(player, "!!Legends' Quest??", line++, isQuestComplete(player, Quests.LEGENDS_QUEST))
            line(player, "!!Dream Mentor??", line++, isQuestComplete(player, Quests.DREAM_MENTOR))
            line(
                player,
                "!!Hand in the Sand??",
                line++,
                isQuestComplete(player, Quests.THE_HAND_IN_THE_SAND)
            )
            line(player, "!!Tears of Guthix??", line++, isQuestComplete(player, Quests.TEARS_OF_GUTHIX))
            line(player, "!!King's Ransom??", line++, isQuestComplete(player, Quests.KINGS_RANSOM))
            line(
                player,
                "!!Defender of Varrock??",
                line++,
                isQuestComplete(player, Quests.DEFENDER_OF_VARROCK)
            )
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
        player.packetDispatch.sendItemZoomOnInterface(Items.LONGBOW_839, 230, 277, 5)

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