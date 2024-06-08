package content.region.asgarnia.rimmington.quest.witchpotion

import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs09.consts.Components
import org.rs09.consts.Items

/**
 * Represents the Witch's Potion Quest
 */
@Initializable
class WitchsPotion : Quest(QUEST_NAME, 31, 30, 1, 67, 0, 1, 3) {
    companion object {
        const val QUEST_NAME = "Witch's Potion"
    }

    override fun drawJournal(player: Player?, stage: Int) {
        super.drawJournal(player, stage)
        player ?: return
        var line = 11
        if (stage == 0) {
            line(player, "I can start this quest by speaking to !!Hetty?? who in her house in", line++)
            line(player, "!!Rimmington??, West of !!Port Sarim??.", line++)
        } else if (stage in 1..39) {
            line(player, "I spoke to !!Hetty?? in her house at Rimmington. Hetty told me", line++, true)
            line(player, "she could increase my magic power if I can bring her", line++, true)
            line(player, "certain ingredients for a potion.", line++, true)
            line++
            line(player, "Hetty needs me to bring her the following:", line++)

            if(inInventory(player, Items.ONION_1957, 1))
                line(player, "I have an onion with me", line++, true)
            else
                line(player, "!!An onion??", line++)

            if(inInventory(player, Items.RATS_TAIL_300, 1))
                line(player, "I have a rat's tail with me", line++, true)
            else
                line(player, "!!A rat's tail??", line++)

            if(inInventory(player, Items.BURNT_MEAT_2146, 1))
                line(player, "I have a piece of burnt meat with me", line++, true)
            else
                line(player, "!!A piece of burnt meat??", line++)

            if(inInventory(player, Items.EYE_OF_NEWT_221, 1))
                line(player, "I have an eye of newt with me", line++, true)
            else
                line(player, "!!An eye of newt??", line++)
        } else if (stage in 40..99) {
            line(player, "I spoke to !!Hetty?? in her house at Rimmington. Hetty told me.", line++, true)
            line(player, "she could increase my magic power if I can bring her", line++, true)
            line(player, "certain ingredients for a potion.", line++, true)
            line(player, "I brought her an onion, a rat's tail, a piece of burnt meat", line++, true)
            line(player, "and an eye of newt which she used to make a potion.", line++, true)
            line(player, "I should drink from the !!cauldron?? and improve my magic!", line++)
        } else if (stage == 100) {
            line(player, "I have spoken to !!Hetty??.", line++, true)
            line(player, "I spoke to !!Hetty?? in her house at Rimmington. Hetty told me.", line++, true)
            line(player, "she could increase my magic power if I can bring her", line++, true)
            line(player, "certain ingredients for a potion.", line++, true)
            line(player, "I brought her an onion, a rat's tail, a piece of burnt meat", line++, true)
            line(player, "and an eye of newt which she used to make a potion.", line++, true)
            line(player, "I drank from the cauldron and my magic power increased!", line++, true)
            line++
            line(player, "%%QUEST COMPLETE!&&", line++)
        }
    }

    override fun finish(player: Player?) {
        super.finish(player)
        player ?: return
        var line = 10

        sendItemZoomOnInterface(player, Components.QUEST_COMPLETE_SCROLL_277, 5, Items.EYE_OF_NEWT_221)
        drawReward(player, "1 Quest Point", line++)
        drawReward(player, "325 Magic XP", line++)

        rewardXP(player, Skills.MAGIC, 325.0)
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }
}