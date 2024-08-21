package content.region.asgarnia.falador.quest.recruitmentdrive

import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs09.consts.Items

/**
 * Recruitment Drive Quest
 *
 * https://www.youtube.com/watch?v=0yvFREeXNn0 - Quest start log only.
 * https://www.youtube.com/watch?v=lNlSiUvPL1o - Very good
 * https://www.youtube.com/watch?v=OGWpX1WqpKM 10:12 - Final congrats page.
 * https://www.youtube.com/watch?v=nu4OAswRcGg - Speaking to Tiffy after the quest (IMPORTANT!)
 * https://www.youtube.com/watch?v=srFMJa4nuX0 1:47 blur ass quest log again
 * https://www.youtube.com/watch?v=L7NdDTWa-1Q HAZEEL's CULT
 * 1 - Speak to Sir Amik Varze.
 * 2 - Sent to secret training ground.
 * 3 - Finish all stages.
 * 100 - Finish by talking to Tiffy.
 */
@Initializable
class RecruitmentDrive : Quest("Recruitment Drive", 103, 102, 1, 496, 0, 1, 2) {
    companion object {
        const val questName = "Recruitment Drive"
        const val attributeOriginalGender = "/save:quest:recruitmentdrive-originalgender"

        // Stage state: (0: reset), (1: passed), (-1: failed)
        const val attributeStagePassFailState = "/save:quest:recruitmentdrive-stagestate"
        const val attributeCurrentStage = "/save:quest:recruitmentdrive-currentstage"
        const val attributeStage1 = "/save:quest:recruitmentdrive-stage1"
        const val attributeStage2 = "/save:quest:recruitmentdrive-stage2"
        const val attributeStage3 = "/save:quest:recruitmentdrive-stage3"
        const val attributeStage4 = "/save:quest:recruitmentdrive-stage4"
        const val attributeStage5 = "/save:quest:recruitmentdrive-stage5"
        val attributeStageArray = arrayOf(attributeStage1, attributeStage2, attributeStage3, attributeStage4, attributeStage5)
    }

    override fun drawJournal(player: Player, stage: Int) {
        super.drawJournal(player, stage)
        var line = 12
        var stage = getStage(player)

        var started = getQuestStage(player, questName) > 0

        if(!started){
            line(player, "I can start this quest by speaking to !!Sir Amik Varze??,", line++)
            line(player, "upstairs in !!Falador Castle,??", line++)
            if (isQuestComplete(player, "Druidic Ritual")) {
                line(player, "with the Druidic Ritual Quest completed,", line++, true)
            } else {
                line(player, "with the !!Druidic Ritual Quest?? completed,", line++)
            }
            if (isQuestComplete(player, "Black Knights' Fortress")) {
                line(player, "and since I have completed the Black Knights' Fortress", line++, true)
                line(player, "Quest.", line++, true)
            } else {
                line(player, "and after I have completed the !!Black Knights' Fortress??", line++)
                line(player, "Quest.", line++)
            }
        } else {
            line(player, "Sir Amik Varze told me that he had put my name forward as", line++, true)
            line(player, "a potential member of some mysterious organisation.", line++, true)

            if (stage >= 2) {
            } else if (stage >= 1) {
                line(player, "I should head to !!Falador Park?? to meet my !!Contact?? so that I", line++, false)
                line(player, "can begin my !!testing for the job??", line++, false)
            }

            if (stage >= 3) {
                line(player, "I went to Falador Park, and met a strange old man named", line++, true)
                line(player, "Tiffy.", line++, true)
                line(player, "He sent me to a secret training ground, where my wits", line++, true)
                line(player, "were thoroughly tested.", line++, true)
                line(player, "Luckily, I was too smart to fall for any of their little tricks,", line++, true)
                line(player, "and passed the test with flying colours.", line++, true)
            } else if (stage >= 2) {
                line(player, "I went to !!Falador Park??, and met a strange old man named", line++, false)
                line(player, "!!Tiffy??.", line++, false)
                line(player, "He sent me to a !!secret training ground??, where my wits", line++, false)
                line(player, "were thoroughly tested.", line++, false)
            }

            if (stage >= 4) {
                line(player, "I am now an official member of the Temple Knights,", line++, true)
                line(player, "although I have to wait for the paperwork to go through", line++, true)
                line(player, "before I can commence working for them.", line++, true)
            } else if (stage >= 3) {
                line(player, "I should talk to !!Tiffy?? to become a Temple Knight.", line++, false)
            }
            if (stage >= 100) {
                line++
                line(player,"<col=FF0000>QUEST COMPLETE!</col>", line)
            }
        }
    }

    override fun reset(player: Player) {
        removeAttribute(player, attributeOriginalGender)
        removeAttribute(player, attributeStagePassFailState)
        removeAttribute(player, attributeCurrentStage)
        removeAttribute(player, attributeStage1)
        removeAttribute(player, attributeStage2)
        removeAttribute(player, attributeStage3)
        removeAttribute(player, attributeStage4)
        removeAttribute(player, attributeStage5)
    }

    override fun finish(player: Player) {
        var ln = 10
        super.finish(player)
        player.packetDispatch.sendString("You have passed the Recruitment Drive!", 277, 4)
        player.packetDispatch.sendItemZoomOnInterface(Items.INITIATE_SALLET_5574, 230, 277, 5)

        drawReward(player, "1 Quest Point", ln++)
        drawReward(player, "1000 Prayer, Herblore and", ln++)
        drawReward(player, "Agility XP", ln++)
        drawReward(player, "Gaze of Saradomin", ln++)
        drawReward(player, "Temple Knight's Initiate Helm", ln)

        rewardXP(player, Skills.PRAYER, 1000.0)
        rewardXP(player, Skills.HERBLORE, 1000.0)
        rewardXP(player, Skills.AGILITY, 1000.0)
        addItem(player, Items.INITIATE_SALLET_5574)
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }
}
