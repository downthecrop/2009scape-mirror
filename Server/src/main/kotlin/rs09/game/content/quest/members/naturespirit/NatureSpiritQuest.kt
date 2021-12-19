package rs09.game.content.quest.members.naturespirit

import api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs09.consts.Items

@Initializable
class NatureSpiritQuest : Quest("Nature Spirit", 95, 94, 2, 307, 0, 1, 110 ) {
    override fun newInstance(`object`: Any?): Quest {
        return this
    }

    override fun drawJournal(player: Player?, stage: Int) {
        super.drawJournal(player, stage)
        player ?: return
        var line = 11
        if(stage == 0){
            line(player, "I can start this quest by speaking to !!Drezel?? in the !!temple of Saradomin??.", line++)
        } else {
            if(stage >= 10){
                line(player, "After talking to Drezel in the temple of Saradomin I've",line++, true)
                line(player,"agreed to look for a Druid called Filliman Tarlock.", line++, true)
            }

            if(stage == 10){
                line(player, "I need to look for !!Filliman Tarlock?? in the !!Swamps?? of Mort",line++)
                line(player, "Myre. I should be wary of !!Ghasts??.", line++)
            }

            if(stage == 15){
                line(player, "I located a !!spirit?? in the swamp. I believe he's", line++, false)
                line(player, "!!Filliman Tarlock?? but I can't understand him.",line++, false)
            }

            if(stage == 20){
                line(player, "I located !!Filliman Tarlock?? in the swamp. I believe he's",line++)
                line(player, "dead but he doesn't believe me. I need to convince him.", line++)
            }

            if(stage >= 25){
                line(player, "I located Filliman Tarlock in the swamp and managed to",line++,true)
                line(player, "convince him that he is in fact a ghost. ", line++, true)
            }

            if(stage == 25){
                line(player, "Filliman needs his !!journal?? to figure out what to do",line++)
                line(player, "next. He mentioned something about a !!knot??.", line++)
            }

            if(stage >= 30){
                line(player, "I recovered Filliman's journal for him.", line++, true)
            }

            if(stage == 30) {
                line(player, "I should speak to !!Filliman Tarlock?? to see what I can",line++)
                line(player, "do to help.", line++)
            }

            if(stage >= 40){
                line(player, "I've gone and gotten blessed by Drezel.", line++, true)
            }

            if(stage >= 35) {
                line(player, "I've agreed to help Filliman become a Nature Spirit.",line++, true)
            }

            if(stage == 35){
                line(player, "The first thing Filliman needs me to do is go and get",line++)
                line(player, "blessed by !!Drezel?? in the temple of Saradomin.",line++)
            }

            if (stage == 40){
                line(player, "I should return to !!Filliman?? to see what I need to do.", line++, false)
            }

            if(stage in 45 until 55){
                line(player, "In order to help Filliman I need to find 3 things:", line++, false)
                line(player, "Something of !!Faith??.",line++, false)
                line(player, "Something of !!Nature??.", line++, stage >= 50)
                line(player, "Something of the !!spirit-to-be freely given??.", line++, false)
            }

            if(stage == 50){
                line(player, "I know for a fact the fungus is !!something of Nature??.", line++, false)
            }

            if(stage >= 55){
                line(player, "I've helped Filliman complete the spell.", line++, true)
            }

            if(stage == 55){
                line(player, "Filliman has asked me to meet him back inside the !!grotto??.", line++, false)
            }

            if(stage == 75){
                line(player, "I need to go and kill !!3 Ghasts?? for Filliman.", line++, false)
            }

            if(stage >= 100){
                line(player,"!!QUEST COMPLETE!??",line++)
            }
        }
    }

    override fun finish(player: Player?) {
        super.finish(player)
        player ?: return
        var ln = 10
        player.packetDispatch.sendItemZoomOnInterface(Items.SILVER_SICKLEB_2963,230,277,5)
        drawReward(player, "2 Quest Points", ln++)
        drawReward(player, "3,000 Crafting XP",ln++)
        drawReward(player, "2,000 Hitpoints XP", ln++)
        drawReward(player, "2,000 Defence XP", ln++)
        rewardXP(player, Skills.CRAFTING, 3000.0)
        rewardXP(player, Skills.HITPOINTS, 2000.0)
        rewardXP(player, Skills.DEFENCE, 2000.0)
        NSUtils.cleanupAttributes(player)
    }
}
