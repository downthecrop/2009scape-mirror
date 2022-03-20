package rs09.game.content.quest.members.deathplateau

import api.addItemOrDrop
import api.setAttribute
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs09.consts.Items

/**
 * @author qmqz
 */

@Initializable
class DeathPlateau : Quest("Death Plateau",314, 43, 1, 314, 0, 1, 80) {

    override fun drawJournal(player: Player?, stage: Int) {
        super.drawJournal(player, stage)

        var line = 12
        var stage = getStage(player)

        when (stage) {
            0 -> {
                line(player, "I can start this quest by speaking to !!Denulth?? who is in his", line++)
                line(player, "tent at the !!Imperial Guard camp?? in !!Burthorpe", line++)
            }

            //line(player,"", line++)

        }
    }

    override fun finish(player: Player) {
        var ln = 10
        super.finish(player)
        player.packetDispatch.sendString("You have completed the Death Plateau Quest!", 277, 4)
        player.packetDispatch.sendItemZoomOnInterface(1891, 240, 277, 5)

        drawReward(player,"1 Quest Point", ln++)
        drawReward(player,"3,000 Attack XP", ln++)
        drawReward(player,"Some Steel Claws", ln++)
        drawReward(player,"Ability to make Claws", ln++)

        addItemOrDrop(player, Items.STEEL_CLAWS_3097, 1)
        player.skills.addExperience(Skills.ATTACK, 3000.0)

    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }
}