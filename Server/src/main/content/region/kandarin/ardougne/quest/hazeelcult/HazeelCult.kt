package content.region.kandarin.ardougne.quest.hazeelcult

import content.data.Quests
import content.region.kandarin.ardougne.quest.hazeelcult.HazeelCultListeners.Companion.HAZEEL_CULT_VARP
import content.region.kandarin.ardougne.quest.hazeelcult.HazeelCultListeners.Companion.attrCarnillean
import content.region.kandarin.ardougne.quest.hazeelcult.HazeelCultListeners.Companion.attrHazeel
import content.region.kandarin.ardougne.quest.hazeelcult.HazeelCultListeners.Companion.attrSewer1R
import content.region.kandarin.ardougne.quest.hazeelcult.HazeelCultListeners.Companion.attrSewer2R
import content.region.kandarin.ardougne.quest.hazeelcult.HazeelCultListeners.Companion.attrSewer3L
import content.region.kandarin.ardougne.quest.hazeelcult.HazeelCultListeners.Companion.attrSewer4R
import content.region.kandarin.ardougne.quest.hazeelcult.HazeelCultListeners.Companion.attrSewer5R
import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs09.consts.Items

/* HAZEEL CULT
 *
 * Primary video source: https://www.youtube.com/watch?v=hFBJx_xERQ4 (carnillean playthrough)
 * Primary video source: https://www.youtube.com/watch?v=ZYPB823IyRk (mahjarrat playthrough)
 * https://web.archive.org/web/20081217064620/http://runescape.salmoneus.net/quests/HazeelCult.html
 * https://web.archive.org/web/20090121182721/http://runehq.com/guide.php?type=quest&id=00297
 * https://runescape.wiki/w/Hazeel_Cult?oldid=815270
 *
 * Varp 223 - Hazeel Cult progress
 * https://chisel.weirdgloop.org/varbs/display?varplayer=223
 * Values  Stage  Description
 * 0       0      unstarted
 * 2       1      after talking to ceril
 * 3       2      talk to clivet (set attr for carnillean)
 * 4       2      talk to clivet (set attr for mahjarrat)
 * 5       3      poison poured in food (mahjarrat-only stage)
 * 6       4      alomone either fought or he tells you he needs scroll
 * 7       5      either returning the armour, or finding the scroll
 * 9       100    quest complete - either presenting proof that jones is a bad guy (duh) or resurrecting hazeel
 *
 * The varp values just appear to track quest stages, so I don't update the varp at intermediate quest stages.
 *
 * Good source of half the quest log: https://youtu.be/Kf95y-YdviA?si=kWeA-8oa-22TttE4&t=140
 *
 * Original framework of the quest and about half the dialogue is credit to szumaster.
 *
 */

@Initializable
class HazeelCult : Quest(Quests.HAZEEL_CULT, 74, 73, 1, HAZEEL_CULT_VARP, 0, 1, 9) {

    // These functions are used at various points to check whether the player is following the Carnillean's side, or Hazeel's.
    companion object {
        fun carnilleanArc(player: Player) : Boolean {
            return (!getAttribute(player, attrHazeel, false) && getAttribute(player, attrCarnillean, false))
        }

        fun mahjarratArc(player: Player) : Boolean {
            return (getAttribute(player, attrHazeel, false) && !getAttribute(player, attrCarnillean, false))
        }

        // the fake ending where you don't prove Jones guilty yet. lol.
        fun fakeFinish(player: Player) {
            openInterface(player, 277)

            player.packetDispatch.sendString("" + player.getQuestRepository().getPoints() + "", 277, 7)
            player.packetDispatch.sendString("You have... kind of... completed the Hazeel Cult Quest!", 277, 4)

            player.packetDispatch.sendItemZoomOnInterface(Items.COINS_995, 230, 277, 5)

            player.packetDispatch.sendString("5 coins", 277, 10)
            player.packetDispatch.sendString("", 277, 11)
            player.packetDispatch.sendString("", 277, 12)
            player.packetDispatch.sendString("", 277, 13)
            player.packetDispatch.sendString("", 277, 14)
            player.packetDispatch.sendString("", 277, 15)
            player.packetDispatch.sendString("", 277, 16)
            player.packetDispatch.sendString("", 277, 17)
        }
    }

    override fun drawJournal(player: Player, stage: Int) {
        super.drawJournal(player, stage)
        var line = 11

        val attributeMahjarrat = mahjarratArc(player)
        val attributeCarnillean = carnilleanArc(player)

        if (stage >= 0) {
            line(player, "I can start the quest by talking to !!Sir Ceril Carnillean?? at", line++, stage >= 1)
            line(player, "the house due !!West?? of !!Ardougne Zoo??.", line++, stage >= 1)
            line++
        }
        if (stage >= 1) {
            line(player, "I spoke to !!Sir Ceril Carnillean?? at his house and agreed to", line++, stage >= 2)
            line(player, "help him !!investigate?? the theft of a !!family heirloom??.", line++, stage >= 2)
            line++
        }
        if (stage >= 2) {
            line(player, "I found a !!member of the cult?? called !!Clivet?? at the entrance", line++, stage >= 3)
            line(player, "to the !!cult's hideout??, south of !!Ardougne??.", line++, stage >= 3)
            line++
        }

        /* --------------- *
         * HAZEEL SIDE
         * --------------- */
        if (stage >= 3 && attributeMahjarrat && !attributeCarnillean) {
            line(player, "Having decided to assist the cult in their mission to revive !!Hazeel??,", line++, stage >= 4)
            line(player, "I tried to !!poison?? the !!Carnilleans?? but instead", line++, stage >= 4)
            line(player, "only managed to kill their pet dog.", line++, stage >= 4)
            line++
        }
        if (stage >= 4 && attributeMahjarrat && !attributeCarnillean) {
            line(player, "I spoke to !!Clivet?? and he told me that I had failed,", line++, stage >= 5)
            line(player, "but he gave me an !!amulet?? anyway.", line++, stage >= 5)
            line(player, "Then I managed to take the sewer rafts to the cult hideout.", line++, stage >= 5)
            line++
        }
        if (stage >= 5 && attributeMahjarrat && !attributeCarnillean) {
            line(player, "I found the scroll needed to revive !!Hazeel?? in a", line++, stage >= 100)
            line(player, "secret compartment upstairs at the !!Carnillean Mansion??.", line++, stage >= 100)
            line++
        }
        if (stage == 100 && attributeMahjarrat && !attributeCarnillean) {
            line(player, "I gave !!Alomone?? the scroll from the !!Carnillean Mansion?? and", line++, true)
            line(player, "helped perform the revivification of !!Hazeel??.", line++, true)
            line(player, "I was rewarded for all of my help", line++, true)
            line(player, "in returning !!Hazeel?? to his followers.", line++, true)
            line++
        }

        /* --------------- *
         * CARNILLEAN SIDE
         * --------------- */
        if (stage >= 2 && attributeCarnillean && !attributeMahjarrat) {
            line(player, "He told me a pack of lies about the !!Carnilleans??,", line++, stage >= 4)
            line(player, "then asked me to join the !!cult??. Obviously, I refused.", line++, stage >= 4)
            line(player, "I was still no closer to recovering the !!missing armour??.", line++, stage >= 4)
            line(player, "After speaking to him, he jumped onto a !!raft?? and headed", line++, stage >= 4)
            line(player, "into the !!sewer system??. I need to find a way to follow", line++, stage >= 4)
            line++
        }
        if (stage >= 4 && attributeCarnillean && !attributeMahjarrat) {
            line(player, "I managed to enter !!the hideout??, kill the !!cult leader?? and", line++, stage >= 5)
            line(player, "retrieve !!the armour??. I discovered that !!Jones the Butler??", line++, stage >= 5)
            line(player, "was secretly a !!member of the cult?? and a !!traitor??.", line++, stage >= 5)
            line++
        }
        // this stage displays a 'fake' quest ending!
        // https://youtu.be/hFBJx_xERQ4?si=ft_1rZGKAHpDjind&t=648
        if (stage >= 5 && attributeCarnillean && !attributeMahjarrat) {
            line(player, "I returned !!the armour??, but !!Ceril?? didn't believe !!Jones was involved??", line++, stage >= 100)
            line(player, "with the cult and was responsible for the theft.", line++, stage >= 100)
            line++
        }
        if (stage == 100 && attributeCarnillean && !attributeMahjarrat) {
            line(player, "I found undeniable evidence that the !!Butler?? was", line++, true)
            line(player, "involved with the cult and gave it to !!Ceril??.", line++, true)
            line(player, "My name was cleared and", line++, true)
            line(player, "I graciously accepted the reward for all of my help.", line++, true)
            line++
        }

        /* --------------- *
         * QUEST COMPLETE
         * --------------- */
        if (stage == 100) {
            line(player, "<col=FF0000>QUEST COMPLETE!</col>", line, false)
        }
    }

    override fun finish(player: Player) {
        var ln = 10
        super.finish(player)
        player.packetDispatch.sendItemZoomOnInterface(Items.COINS_995, 230, 277, 5)

        drawReward(player, "1 Quest Point", ln++)
        drawReward(player, "2,000 Coins", ln++)
        drawReward(player, "1,500 Thieving XP", ln)

        rewardXP(player, Skills.THIEVING, 1500.0)
        addItemOrDrop(player, Items.COINS_995, 2000)

        // no attributes need removing on quest completion. the sewer valves are not saved, and which quest arc you chose affects the quest journal and post-quest dialogue.
    }

    // use ::setqueststage 74 0 to reset quest.
    override fun reset(player: Player) {
        removeAttribute(player, attrSewer1R)
        removeAttribute(player, attrSewer2R)
        removeAttribute(player, attrSewer3L)
        removeAttribute(player, attrSewer4R)
        removeAttribute(player, attrSewer5R)
        removeAttribute(player, attrHazeel)
        removeAttribute(player, attrCarnillean)
    }

    override fun newInstance(`object`: Any?): Quest = this
}
