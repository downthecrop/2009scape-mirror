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

        var ln = 11

        /**
         * Just draw this if the quest is done
         */
        if (stage == QUEST_STATE_DONE) {
            ln++
            line(player, "I helped !!Thormac?? get his scorpions back.", ln++)
            line(player, "Now he can upgrade my battlestaffs into mystic staffs.", ln)
            ln++
            ln++
            line(player, "<col=FF0000>QUEST COMPLETE!</col>", ln)
            return
        }

        val caughtTaverly = player!!.getAttribute(ATTRIBUTE_TAVERLY, false)
        val caughtBarb = player.getAttribute(ATTRIBUTE_BARB, false)
        val caughtMonk = player.getAttribute(ATTRIBUTE_MONK, false)

        if (stage == QUEST_STATE_NOT_STARTED){
            line(player, "I can start this quest by speaking to !!Thormac?? who is in the", ln++)
            line(player, "!!Sorcerer's Tower?? south-west of !!Catherby??", ln++)
            ln++ //blank line
            line(player, "Requirements:", ln++)
            line(player, "Level 31 Prayer", ln, player.skills.staticLevels[Skills.PRAYER] >= 31)
        }
        else {
            line(player, "Speak to Thormac.", ln++, true)
            ln++

            if (stage == QUEST_STATE_TALK_SEERS) {
                line(player, "I've spoken to !!Thormac?? in the !!Sorcerer's Tower?? south-west of !!Catherby.??", ln++)
                line(player, "He's lost his pet !!Kharid Scorpions?? and needs my help to find them.", ln++)
                // Todo check this line
                line(player, "He's told me to ask a !!Seer?? for help.", ln)
            }
            else {
                // todo check this line
                line(player, "I talked to a Seer. He told me where I should look.", ln++, caughtTaverly)
                ln++
                line(player, "The first !!Kharid Scorpion?? is in a secret room near some", ln++, caughtTaverly)
                line(player, "nasty spiders with two coffins nearby.", ln++, caughtTaverly)
                ln++
                if (stage == QUEST_STATE_DARK_PLACE && caughtTaverly){
                    // Todo check this line
                    line(player, "I should go back to the Seer and ask about the other scorpions.", ln++)
                }

                if (stage >= QUEST_STATE_OTHER_SCORPIONS){
                    val barb_strike = caughtBarb || (stage == QUEST_STATE_PEKSA_HELP)
                    line(player, "The second !!Kharid Scorpion?? has been in a !!village of??", ln++, barb_strike)
                    line(player, "!!uncivilised-looking warriors in the east.?? It's been picked up", ln++, barb_strike)
                    line(player, "by some sort of !!merchant??", ln++, barb_strike)
                    ln++
                    if (stage == QUEST_STATE_PEKSA_HELP){
                        // todo check this block
                        line(player, "I spoke with !!Peksa?? who said he sent it to his brother", ln++, caughtBarb)
                        line(player, "at the !!Barbarian outpost.??", ln++, caughtBarb)
                        ln++
                    }

                    line(player, "The third !!Kharid Scorpion?? is in some sort of !!upstairs room??", ln++, caughtMonk)
                    line(player, "with !!brown clothing on a table??", ln++, caughtMonk)
                }

                if (caughtBarb && caughtTaverly && caughtMonk && stage >= QUEST_STATE_OTHER_SCORPIONS){
                    ln++
                    line(player, "I should tell !!Thormac?? I have all of his scorpions.", ln)
                }

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