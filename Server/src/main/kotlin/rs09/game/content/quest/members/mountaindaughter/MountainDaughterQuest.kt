package rs09.game.content.quest.members.mountaindaughter

import core.game.content.quest.fremtrials.FremennikTrials
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import org.rs09.consts.Items

class MountainDaughterQuest : Quest("Mountain Daughter",74,89,2,423,0,1,70){

    class SkillRequirement(val skill: Int?, val level: Int?)

    val requirements = arrayListOf<SkillRequirement>()

    override fun drawJournal(player: Player?, stage: Int) {
        super.drawJournal(player, stage)
        var line = 11
        val started = player?.questRepository?.getStage("Mountain Daughter")!! > 0

        if(!started){
            line(player,"Requirements to complete quest:",line++)
            line += 1
            line(player,"Level 20 Agility",line++, player.skills.getStaticLevel(Skills.WOODCUTTING) >= 40)
            line(player,"The ability to defeat a !!level 70 monster??",line++)
        }
    }
    override fun finish(player: Player?) {
        super.finish(player)
        player ?: return
        var ln = 10
        player.packetDispatch.sendItemZoomOnInterface(Items.BEARHEAD_4502, 235, 277, 5)
        drawReward(player, "2 Quest Points", ln++)
        drawReward(player, "2,000 Prayer XP",ln++)
        drawReward(player, "1,000 Attack XP", ln++)
        drawReward(player, "A Bearhead", ln++)
        player.skills.addExperience(Skills.PRAYER, 2000.0)
        player.skills.addExperience(Skills.ATTACK, 1000.0)
        player.questRepository.syncronizeTab(player)
    }
    override fun newInstance(`object`: Any?): Quest {
        requirements.add(MountainDaughterQuest.SkillRequirement(Skills.AGILITY, 20))
        return this
    }
}