package content.region.kandarin.ardougne.plaguecity.quest.elena

import core.api.addItem
import core.api.addItemOrDrop
import core.api.removeAttributes
import core.api.rewardXP
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs09.consts.Items

@Initializable
class PlagueCity : Quest("Plague City", 98, 97, 1, 165, 0, 1, 29) {
    override fun newInstance(`object`: Any?): Quest { return this }
    companion object { const val PlagueCityQuest = "Plague City" }
    override fun drawJournal(player: Player?, stage: Int) {
        super.drawJournal(player, stage)
        var line = 11
        player ?: return

        if (stage == 0) {
            line++
            line(player, "I can start this quest by speaking to !!Edmond?? who is in !!East??", line++, false)
            line(player, "!!Ardougne??",line++, false)
            line++
            line(player, "There aren't any requirements for this quest.", line++, false)
        }

        if (stage >= 1) {
            line++
            line(player, "I've spoken to Edmond, he's asked me to", line++, stage >= 2)
            line(player, "help find his daughter Elena.", line++, stage >= 2)
            line++
        }

        if (stage >= 2) {
            line++
            line(player, "Alrena has given me a Gasmask to protect me", line++, stage >= 3)
            line(player, "from the plague while in West Ardougne.", line++, stage >= 3)
            line++
        }

        if (stage >= 3) {
            line++
            line(player, "She's making a spare which will be in the wardrobe.", line++, stage >= 4)
            line(player, "I've spoken to Edmond about getting into West Ardougne.", line++, stage >= 4)
        }

        if (stage >= 4) {
            line++
            line(player, "I've softened the ground in Edmond's garden enough to dig.", line++, stage >= 5)
        }

        if (stage >= 5) {
            line++
            line(player, "I've dug a tunnel into the sewers.", line++, stage >= 6)
        }

        if (stage >= 6) {
            line++
            line(player, "I've managed to clear the way into West Ardougne.", line++, stage >= 7)
        }

        if (stage >= 9) {
            line++
            line(player, "I've spoken to Jethick, he thinks Elena was staying with the", line++, stage >= 11)
            line(player, "Rehnison Family, in a timber house to the north of the city.", line++, stage >= 11)
        }

        if (stage >= 11) {
            line++
            line(player, "I've spoken to Milli about Elena,", line++, stage >= 12)
            line(player, "she says Elena was taken into one of the Plague Houses.", line++, stage >= 12)
        }

        if (stage >= 15) {
            line++
            line(player, "Bravek might give me clearance if I make his Hangover Cure.", line++, stage >= 16)
        }

        if (stage >= 16) {
            line++
            line(player, "I've given Bravek the Hangover Cure and he has", line++, stage >= 17)
            line(player, "given me a warrant to enter the plague House.", line++, stage >= 17)
        }

        if (stage >= 99) {
            line++
            line(player, "I've freed Elena from the Plague House.", line++, stage == 100)
        }

        if (stage == 100) {
            line++
            line(player, "I've spoken to Edmond and he thanked me", line++, true)
            line(player, "for rescuing his daughter Elena.", line++, true)
            line++
            line(player, "<col=FF0000>QUEST COMPLETE!</col>", line, false)
        }
    }

    override fun finish(player: Player?) {
        super.finish(player)
        player ?: return
        var ln = 10

        player.packetDispatch.sendItemZoomOnInterface(Items.GAS_MASK_1506, 230, 277, 5)
        drawReward(player, "1 Quest Point", ln++)
        drawReward(player, "2,425 Mining XP", ln++)
        drawReward(player, "An Ardougne Teleport Scroll", ln)
        rewardXP(player, Skills.MINING, 2425.0)
        addItemOrDrop(player, Items.A_MAGIC_SCROLL_1505)
        removeAttributes(player, "/save:elena:dig", "/save:elena:bucket")
    }
}