package content.region.kandarin.ardougne.quest.monksfriend


import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs09.consts.Items

/**
* Represents the "Monk's Friend" quest.
* @author Kya
*/

@Initializable
class MonksFriend: Quest("Monk's Friend", 89, 88, 1, 30, 0, 1, 80) {

    override fun newInstance(`object`: Any?): Quest {
        return this
    }

    override fun drawJournal(player: Player?, stage: Int) {
        super.drawJournal(player, stage)
        var line = 11
        player?: return
        if(stage == 0){
            line(player, "I can start this quest by speaking to !!Brother Omad?? in the", line++)
            line(player, "!!Monastery?? south of !!Ardougne??.", line++)
            line++
        }
        if(stage == 10){
            line(player, "Brother Omad asked me to recover a child's blanket.", line++)
        }
        if(stage >= 20){
            line(player, "Brother Omad asked me to recover a child's blanket, I ", line++, true)
            line(player, "found the secret cave and gave back the blanket.", line++, true)

        }
        if(stage ==30){
            line(player, "I agreed to find !!Brother Cedric.?? he is somewhere in the", line++)
            line(player, "!!forest?? south of !!Ardougne??", line++)
        }
        if(stage >= 50){
            line(player, "I found Brother Cedric in the forest south of Ardougne.", line++, true)
            line(player, "I sobered him up and I helped him fix his cart.", line++, true)
        }
        if(stage == 100){
            line(player, "I had a party with the Monks. There were party balloons and we danced the night away!", line++, true)
            line++
            line(player, "%%QUEST COMPLETE!&&", line++)
        }
    }

    override fun finish(player: Player?) {
        super.finish(player)
        player ?: return
        var ln = 10
        player.packetDispatch.sendItemZoomOnInterface(Items.LAW_RUNE_563, 230, 277, 5)
        drawReward(player,"1 Quest Point", ln++)
        drawReward(player,"8 Law Runes", ln++)
        drawReward(player,"2000 Woodcutting XP", ln++)

        rewardXP(player, Skills.WOODCUTTING, 2000.0)
        player.inventory.add(Item(563, 8))
    }
}
