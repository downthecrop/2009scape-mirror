package rs09.game.content.quest.members.naturespirit

import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.plugin.Initializable

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
                line(player, "I've agreed to help Filliman become a Nature Spirit.",line++)
            }

            if(stage == 35){
                line(player, "The first thing Filliman needs me to do is go and get",line++)
                line(player, "blessed by !!Drezel?? in the temple of Saradomin.",line++)
            }

            if(stage >= 40){
                line(player, "Drezel mentioned I seem to have !!something of the faith??.", line++, false)
            }

            if (stage == 40){
                line(player, "I should return to !!Filliman?? to see what I need to do.", line++, false)
            }



        }
    }

    override fun drawReward(player: Player?, string: String?, line: Int) {
        super.drawReward(player, string, line)
    }

    override fun finish(player: Player?) {
        super.finish(player)
    }
}
