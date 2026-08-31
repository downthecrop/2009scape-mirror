package content.region.kandarin.yanille.quest.thehandinthesand.quest

import content.data.Quests
import core.api.getAttribute
import core.api.getQuestStage
import core.api.hasLevelStat
import core.api.playJingle
import core.api.removeAttributes
import core.api.rewardXP
import core.api.sendItemZoomOnInterface
import core.api.setInterfaceText
import core.api.setVarbit
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.Jingles

/**
 * The Hand in the Sand quest.
 * @author Edith
 */

@Initializable
class TheHandintheSand : Quest(Quests.THE_HAND_IN_THE_SAND, 72, 71, 1, 0, VARBIT_QUEST_PROGRESS_1527, 0, 1, 160) {

    companion object {

        /** Quest progress varbit used for the quest tab: 0 = not started, 1 = in progress, 160 = completed. */
        const val VARBIT_QUEST_PROGRESS_1527 = 1527

        /** Betty's counter state. */
        const val VARBIT_BETTYS_COUNTER_1537 = 1537
        const val BETTYS_COUNTER_NO_VIAL_0 = 0
        const val BETTYS_COUNTER_SHOW_VIAL_1 = 1

        /** Sandy's NPC wrapper state. */
        const val VARBIT_SANDY_STATE_1535 = 1535
        const val SANDY_NORMAL_0 = 0
        const val SANDY_DISTRACTED_1 = 1
        const val SANDY_ARRESTED_2 = 2

        /** Sandy's coffee mug scenery state. */
        const val VARBIT_SANDYS_COFFEE_MUG_1536 = 1536
        const val SANDYS_COFFEE_VISIBLE_0 = 0
        const val SANDYS_COFFEE_REMOVED_1 = 1

        /** Player has used Zavistic Rarve's one time teleport. */
        const val ATTRIBUTE_ZAVISTIC_TELEPORT = "/save:thehandinthesand_zavistic_teleport"

        /** Player has made the truth serum. */
        const val ATTRIBUTE_TRUTH_SERUM_MADE = "/save:thehandinthesand_truth_serum_made"

        /** Sandy has been distracted and the player can use the truth serum on his coffee. */
        const val ATTRIBUTE_SANDY_DISTRACTED = "/save:thehandinthesand_sandy_distracted"

        /** Randomly selected Sandy distraction option that succeeds for that player. */
        const val ATTRIBUTE_SANDY_DISTRACTION_OPTION = "/save:thehandinthesand_sandy_distraction_option"

        /** Sandy interrogation question progress. */
        const val ATTRIBUTE_ASKED_ROTA_CHANGE = "/save:thehandinthesand_asked_rota_change"
        const val ATTRIBUTE_ASKED_BERT_MEMORY = "/save:thehandinthesand_asked_bert_memory"
        const val ATTRIBUTE_ASKED_WIZARD_DEATH = "/save:thehandinthesand_asked_wizard_death"

        /** Player has seen Bert's post-quest explanation about daily sand. */
        const val ATTRIBUTE_BERT_POST_QUEST = "/save:thehandinthesand_bert_post_quest"

        /** Player has accepted the quest and needs to show the sandy hand to the Guard Captain. */
        const val STAGE_QUEST_STARTED_5 = 5

        /** Player has shown the sandy hand to the Guard Captain and needs to speak to Zavistic Rarve. */
        const val STAGE_SEE_WIZARD_10 = 10

        /** Player has spoken to Zavistic Rarve and needs to ask Bert about Sandy's rota. */
        const val STAGE_ASK_BERT_15 = 15

        /** Player has received Bert's rota and needs to find Sandy's original rota. */
        const val STAGE_SANDYS_ROTA_20 = 20

        /** Player has both rotas and needs to show them to Bert. */
        const val STAGE_BOTH_ROTAS_25 = 25

        /** Player has received Bert's magic scroll and needs to take it to Zavistic Rarve. */
        const val STAGE_MAGIC_SCROLL_30 = 30

        /** Player has received the magical orb and needs Betty's help making truth serum. */
        const val STAGE_MAGICAL_ORB_35 = 35

        /** Player has spoken to Betty and needs to make truth serum. */
        const val STAGE_TRUTH_SERUM_40 = 40

        /** Player has made truth serum and needs to drug Sandy's coffee. */
        const val STAGE_DRUG_SANDY_45 = 45

        /** Player has drugged Sandy's coffee and needs to activate the magical orb to interrogate Sandy. */
        const val STAGE_INTERROGATE_SANDY_50 = 50

        /** Player has recorded Sandy's confession and needs to return to Zavistic Rarve. */
        const val STAGE_SANDY_CONFESSION_55 = 55

        /** Player needs to bring Zavistic Rarve a bucket of sand and five earth runes. (Cutscene) */
        const val STAGE_RETRIEVE_ITEMS_60 = 60

        /** Zavistic Rarve has enchanted the Yanille sandpit and the player needs to continue speaking with him. */
        const val STAGE_SANDPIT_FILLED_65 = 65

        /** Player needs to search the Entrana sandpit for Clarence's remains. */
        const val STAGE_FIND_CLARENCE_70 = 70

        /** Player has received Clarence's head and needs to return it to Zavistic Rarve. */
        const val STAGE_RETURN_HEAD_75 = 75

        /** Player has completed The Hand in the Sand. */
        const val STAGE_QUEST_COMPLETED_100 = 100

    }

    override fun drawJournal(player: Player, stage: Int) {
        super.drawJournal(player, stage)
        var line = 12
        val sandyDistracted = getAttribute(player, ATTRIBUTE_SANDY_DISTRACTED, false)

        if (stage == 0) {
            line(player, "I can start this quest by speaking to !!Bert?? in !!Yanille?? in", line++)
            line(player, "the house near the !!Sandpit??.", line++)
            line(player, "Before I begin I will need to:", line++)
            line(player, "<col=000000>Have level 17 !!Thieving??.", line++, hasLevelStat(player, Skills.THIEVING, 17))
            line(player, "<col=000000>Have level 49 !!Crafting??.", line++, hasLevelStat(player, Skills.CRAFTING, 49))
            return
        }

        if (stage >= STAGE_QUEST_STARTED_5) {
            line(player, "!!Bert?? the sandpit worker in Yanille has asked me to", line++, stage >= STAGE_QUEST_COMPLETED_100)
            line(player, "investigate the !!hand?? that he found in the sand.", line++, stage >= STAGE_QUEST_COMPLETED_100)

            if (stage >= STAGE_SEE_WIZARD_10) {
                line(player, "I have spoken to the Guard Captain.", line++, true)
            } else {
                line(player, "I need to speak to the !!Guard Captain?? who is in the !!Dragon Inn??", line++)
                line(player, "south of the !!sandpit??", line++) // Authentically missing a period.
            }
        }

        if (stage >= STAGE_ASK_BERT_15) {
            line(player, "I have shown the hand to the Wizards in Yanille.", line++, true)

            if (stage >= STAGE_SANDYS_ROTA_20) {
                line(player, "I have Bert's copy of the Rota.", line++, true)
            } else {
                line(player, "Find out why !!Bert's?? hours have changed.", line++)
            }
        }

        if (stage >= STAGE_SANDYS_ROTA_20) {
            if (stage >= STAGE_BOTH_ROTAS_25) {
                line(player, "I have Sandy's copy of the Rota.", line++, true)
            } else {
                line(player, "I should ask !!Sandy?? in the !!Sand Corp?? Offices in", line++)
                line(player, "!!Brimhaven?? about Bert's rota.", line++)
            }
        }

        if (stage == STAGE_MAGIC_SCROLL_30) {
            line(player, "Ring the bell at the Wizard Guild in !!Yanille?? and give", line++)
            line(player, "!!scroll?? to !!Zavistic Rarve??.", line++) // No, these are not my typos.
        }

        if (stage >= STAGE_MAGICAL_ORB_35) {
            line(player, "I have taken the scroll to !!Zavistic Rarve??.", line++, true)

            if (sandyDistracted || stage >= STAGE_INTERROGATE_SANDY_50) {
                line(player, "I have distracted Sandy successfully.", line++, true)
            } else if (stage >= STAGE_DRUG_SANDY_45) {
                line(player, "Find a way to get !!Sandy?? to drink the !!Truth Serum.??", line++) // Translated from Portuguese
            } else if (stage >= STAGE_TRUTH_SERUM_40) {
                line(player, "!!Betty?? has told me what I need to do to make the !!Truth Serum??:", line++)
                line(player, "I need to !!craft?? a bullseye lens", line++)
                line(player, "I need to make some !!redberry juice in the bottle Betty?? gave me", line++)
                line(player, "I need to make some !!pink dye??", line++)
                line(player, "I need to make a !!rose tinted lens??", line++)
            } else {
                line(player, "!!Betty?? in !!Port Sarim?? will guide you on how to make some !!Truth Serum??.", line++)
            }
        }

        if (stage >= STAGE_INTERROGATE_SANDY_50) {
            line(player, "I have drugged !!Sandy's?? coffee.", line++, true)
        }

        if (stage >= STAGE_SANDY_CONFESSION_55) {
            line(player, "I have interrogated Sandy.", line++, true)
        }

        if (stage >= STAGE_RETRIEVE_ITEMS_60) {
            line(player, "I have returned the information from the orb.", line++, true)
        }

        if (stage >= STAGE_SANDPIT_FILLED_65) {
            line(player, "The Sandpit has been enchanted.", line++, true)
        }

        if (stage >= STAGE_FIND_CLARENCE_70) {
            if (stage >= STAGE_RETURN_HEAD_75) {
                line(player, "I have retrieved the head of a wizard.", line++, true)

                if (stage >= STAGE_QUEST_COMPLETED_100) {
                    line(player, "The dead wizard has been buried and Sandy arrested for murder.", line++)
                } else {
                    line(player, "Return the !!wizard head?? to !!Zavistic Rarve?? in !!Yanille??.", line++)
                }
            } else {
                line(player, "Visit the !!sandpit?? on the island of !!Entrana?? and return", line++)
                line(player, "any other wizard parts.", line++) // Inauthentic line break to fit it into our journal
            }
        }

        if (stage >= STAGE_QUEST_COMPLETED_100) {
            line++
            line(player, "%%QUEST COMPLETE!&&", line++)
            line(player, "<red>Every day I may ask Bert to transport some sand to my bank.",line++)
        }
    }

    override fun hasRequirements(player: Player): Boolean {
        return hasLevelStat(player, Skills.CRAFTING, 49) &&
                hasLevelStat(player, Skills.THIEVING, 17)
    }

    override fun finish(player: Player) {
        var line = 10
        super.finish(player)

        setInterfaceText(player, "You have completed The Hand in the Sand!", 277, 4)
        sendItemZoomOnInterface(player, 277, 5, Items.SANDY_HAND_6945, 240)

        drawReward(player, "1 Quest Point", line++)
        drawReward(player, "1,000 Thieving XP", line++)
        drawReward(player, "9,000 Crafting XP", line++)
        drawReward(player, "Secret Reward from Bert", line)

        rewardXP(player, Skills.THIEVING, 1000.0)
        rewardXP(player, Skills.CRAFTING, 9000.0)
        playJingle(player, Jingles.QUEST_COMPLETE1_152)
    }

    override fun reset(player: Player) {
        removeAttributes(
            player,
            ATTRIBUTE_ZAVISTIC_TELEPORT,
            ATTRIBUTE_TRUTH_SERUM_MADE,
            ATTRIBUTE_SANDY_DISTRACTED,
            ATTRIBUTE_SANDY_DISTRACTION_OPTION,
            ATTRIBUTE_ASKED_ROTA_CHANGE,
            ATTRIBUTE_ASKED_BERT_MEMORY,
            ATTRIBUTE_ASKED_WIZARD_DEATH,
            ATTRIBUTE_BERT_POST_QUEST
        )

        setVarbit(player, VARBIT_BETTYS_COUNTER_1537, BETTYS_COUNTER_NO_VIAL_0, true)
        setVarbit(player, VARBIT_SANDY_STATE_1535, SANDY_NORMAL_0, true)
        setVarbit(player, VARBIT_SANDYS_COFFEE_MUG_1536, SANDYS_COFFEE_VISIBLE_0, true)
    }

    override fun updateVarps(player: Player) {
        val stage = getQuestStage(player, Quests.THE_HAND_IN_THE_SAND)

        if (stage == 0 || stage >= STAGE_QUEST_COMPLETED_100) {
            setVarbit(player, VARBIT_BETTYS_COUNTER_1537, BETTYS_COUNTER_NO_VIAL_0, true)
        }

        if (stage == 0) {
            setVarbit(player, VARBIT_SANDY_STATE_1535, SANDY_NORMAL_0, true)
        }

        if (stage >= STAGE_QUEST_COMPLETED_100) {
            setVarbit(player, VARBIT_SANDY_STATE_1535, SANDY_ARRESTED_2, true)
            setVarbit(player, VARBIT_SANDYS_COFFEE_MUG_1536, SANDYS_COFFEE_REMOVED_1, true)
        }
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }

}