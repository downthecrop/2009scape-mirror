package content.region.kandarin.quest.scorpioncatcher

import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs09.consts.Items

@Initializable
class ScorpionCatcher : Quest("Scorpion Catcher", 108, 107,  1, 76, 0, 1, 6) {
    companion object {
        const val QUEST_STATE_NOT_STARTED = 0
        const val QUEST_STATE_TALK_SEERS = 10
        const val QUEST_STATE_DARK_PLACE = 20
        const val QUEST_STATE_OTHER_SCORPIONS = 30
        const val QUEST_STATE_PEKSA_HELP = 40
        const val QUEST_STATE_DONE = 100

        const val ATTRIBUTE_TAVERLY = "scorpion_catcher:caught_taverly"
        const val ATTRIBUTE_BARB = "scorpion_catcher:caught_barb"
        const val ATTRIBUTE_MONK = "scorpion_catcher:caught_monk"

    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }

    override fun drawJournal(player: Player?, stage: Int) {
        super.drawJournal(player, stage)

        var ln = 12

        val caughtTaverly = player!!.getAttribute(ATTRIBUTE_TAVERLY, false)
        val caughtBarb = player.getAttribute(ATTRIBUTE_BARB, false)
        val caughtMonk = player.getAttribute(ATTRIBUTE_MONK, false)

        if (stage == QUEST_STATE_NOT_STARTED){
            line(player, "I can start this quest by speaking to !!Thormac?? who is in the", ln++)
            line(player, "!!Sorcerer's Tower?? south-west of !!Catherby??", ln++)
            ln++ //blank line
            line(player, "Requirements:", ln++)
            line(player, "Level 31 Prayer", ln, player.skills.staticLevels[Skills.PRAYER] >= 31)
        } else {
            line(player, "I've spoken to Thormac in the Sorcerer's Tower south-west", ln++, true)
            line(player, "of Catherby. He's lost his pet Kharid Scorpions and needs", ln++, true)
            line(player, "my help to find them.", ln++, true)

            // 10 -> 20
            if (stage >= QUEST_STATE_DARK_PLACE) {
                ln++
                line(player, "I've spoken to a Seer and been given the location of one", ln++, true)
                line(player, "of the Kharid Scorpions.", ln++, true)
            } else if (stage >= QUEST_STATE_TALK_SEERS) {
                ln++
                line(player, "I need to go to the !!Seers' Village?? and talk to the !!Seer??", ln++)
                line(player, "about the lost !!Kharid Scorpions??.", ln++)
            }

            // 20 -> 20 + 1st Scorpion
            if (stage >= QUEST_STATE_DARK_PLACE && caughtTaverly || stage >= QUEST_STATE_OTHER_SCORPIONS) {
                ln++
                line(player, "The first Kharid Scorpion is in a secret room near some", ln++, true)
                line(player, "nasty spiders with two coffins nearby.", ln++, true)
            } else if (stage >= QUEST_STATE_DARK_PLACE) {
                ln++
                line(player, "The first !!Kharid Scorpion?? is in a secret room near some", ln++)
                line(player, "!!nasty spiders?? with two !!coffins?? nearby.", ln++)
                // Jan 21, 2010 version has a slightly updated but similar location.
//                line(player, "The first !!Kharid Scorpion?? is in a !!dark place between a lake??", ln++)
//                line(player, "and a !!holy island??. It will be close when you enter.", ln++)
                ln++
                line(player, "I'll need to talk to a !!Seer?? again one I've caught the first", ln++)
                line(player, "!!Kharid Scorpion??.", ln++)
            }

            // 20 + 1st Scorpion -> 30
            if (stage >= QUEST_STATE_OTHER_SCORPIONS) {
                // This line disappears when the you talk to the Seer again.
            } else if (stage >= QUEST_STATE_DARK_PLACE && caughtTaverly){
                // Todo check this line
                ln++
                line(player, "I should go back to the Seer and ask about the other", ln++)
                line(player, "scorpions.", ln++)
            }

            // 30 -> 40
            if (stage >= QUEST_STATE_OTHER_SCORPIONS){
                ln++
                val barb_strike = caughtBarb || (stage == QUEST_STATE_PEKSA_HELP)
                line(player, "The second !!Kharid Scorpion?? has been in a !!village of??", ln++, barb_strike || stage == QUEST_STATE_DONE)
                line(player, "!!uncivilised-looking warriors in the east??. It's been picked up", ln++, barb_strike || stage == QUEST_STATE_DONE)
                line(player, "by some sort of !!merchant??.", ln++, barb_strike || stage == QUEST_STATE_DONE)
                // Jan 21, 2010 version has a slightly updated but similar location.
//                line(player, "The second !!Kharid Scorpion?? was once in a !!village two??", ln++, barb_strike || stage == QUEST_STATE_DONE)
//                line(player, "!!canoe trips from lumbridge??. A !!shopkeeper?? there picked it up.", ln++, barb_strike || stage == QUEST_STATE_DONE)
                if (stage == QUEST_STATE_PEKSA_HELP){
                    // todo check this block
                    ln++
                    line(player, "I spoke with !!Peksa?? who said he sent it to his brother", ln++, caughtBarb)
                    line(player, "at the !!Barbarian outpost.??", ln++, caughtBarb)
                }
                ln++
                line(player, "The third !!Kharid Scorpion?? is in some sort of !!upstairs room??", ln++, caughtMonk || stage == QUEST_STATE_DONE)
                line(player, "with !!brown clothing on a table??.", ln++, caughtMonk || stage == QUEST_STATE_DONE)
                // Jan 21, 2010 version has a slightly updated but similar location.
//                line(player, "The third !!Kharid Scorpion?? is in an !!upstairs room?? with !!brown??", ln++, caughtMonk || stage == QUEST_STATE_DONE)
//                line(player, "!!clothing on a table?? The clothing is adorned with a golden", ln++, caughtMonk || stage == QUEST_STATE_DONE)
//                line(player, "four-pointed star. You should start looking where monks", ln++, caughtMonk || stage == QUEST_STATE_DONE)
//                line(player, "reside.", ln++, caughtMonk || stage == QUEST_STATE_DONE)
            }

            // 40 -> 100
            if (stage == QUEST_STATE_DONE) {
                // This line disappears when you complete the quest.
            } else if (caughtBarb && caughtTaverly && caughtMonk && stage >= QUEST_STATE_OTHER_SCORPIONS){
                ln++
                line(player, "I need to take the !!Kharid Scorpions?? to !!Thormac??.", ln)
            }

            // 100
            if (stage == QUEST_STATE_DONE) {
                ln++
                line(player, "I've spoken to Thormac and he thanked me for finding his", ln++, true)
                line(player, "pet Kharid Scorpions.", ln++, true)
                ln++
                ln++
                line(player, "<col=FF0000>QUEST COMPLETE!</col>", ln)
                return
            }

        }
    }

    override fun finish(player: Player?) {
        var ln = 10
        super.finish(player)
        player!!.packetDispatch.sendString("You have completed the Scorpion Catcher Quest!", 277, 4)
        player.packetDispatch.sendItemZoomOnInterface(Items.SCORPION_CAGE_463, 240, 277, 5)

        drawReward(player, "1 Quest Point", ln++)
        drawReward(player, "6625 Strength XP", ln)

        player.skills.addExperience(Skills.STRENGTH, 6625.0)

    }
}