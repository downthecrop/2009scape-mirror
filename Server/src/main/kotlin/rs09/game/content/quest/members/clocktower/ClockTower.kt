package rs09.game.content.quest.members.clocktower

import api.addItemOrDrop
import api.getAttribute
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.plugin.Initializable
import org.rs09.consts.Items

/**
 * @author qmqz
 */

@Initializable
class ClockTower : Quest("Clock Tower",36, 37, 1, 10, 0, 1, 8) {

    /**
     *todo:
     * be able to walk through gate Id: 39 after poisoning rattos and they die
     * figure out levers
     * stop people from just walking through the gate left of levers
     **/

    override fun drawJournal(player: Player?, stage: Int) {
        super.drawJournal(player, stage)

        var line = 12
        var stage = getStage(player)

        when (stage) {
            0 -> {
                line(player, "I can start this quest by speaking to !!Brother Kojo?? at the", line++)
                line(player, "!!Clock Tower?? which is located !!South?? of !!Ardougne??", line++)
            }

            1, 2 -> {
                line(player, "I spoke to Brother Kojo at the Clock Tower South of", line++, true)
                line(player, "Ardougne and agreed to help him repair the clock.", line++, true)
                line(player,"To repair the clock I need to find the four coloured cogs", line++, getAttribute(player!!, "quest:clocktower-cogsplaced", 0) >= 4)
                line(player,"and place them on the four correctly coloured spindles.", line++, getAttribute(player!!, "quest:clocktower-cogsplaced", 0) >= 4)
                line(player, if (getAttribute(player!!, "quest:clocktower-bluecogplaced", false)) "I placed the !!Blue Cog?? on it's !!spindle??." else "I haven't placed the !!Blue Cog?? on it's !!spindle?? yet.", line++, getAttribute(player!!, "quest:clocktower-bluecogplaced", false))
                line(player, if (getAttribute(player!!, "quest:clocktower-blackcogplaced", false)) "I placed the !!Black Cog?? on it's !!spindle??." else "I haven't placed the !!Black Cog?? on it's !!spindle?? yet.", line++, getAttribute(player!!, "quest:clocktower-blackcogplaced", false))
                line(player, if (getAttribute(player!!, "quest:clocktower-blackwhiteplaced", false)) "I placed the !!White Cog?? on it's !!spindle??." else "I haven't placed the !!White Cog?? on it's !!spindle?? yet.", line++, getAttribute(player!!, "quest:clocktower-whitecogplaced", false))
                line(player, if (getAttribute(player!!, "quest:clocktower-blackredplaced", false)) "I placed the !!Red Cog?? on it's !!spindle??." else "I haven't placed the !!Red Cog?? on it's !!spindle?? yet.", line++, getAttribute(player!!, "quest:clocktower-redcogplaced", false))
            }

            100 -> {
                line(player, "I spoke to Brother Kojo at the Clock Tower South of", line++, true)
                line(player, "Ardougne and agreed to help him repair the clock.", line++, true)
                line(player,"To repair the clock I need to find the four coloured cogs", line++, true)
                line(player,"and place them on the four correctly coloured spindles.", line++, true)
                line(player,"I placed the !!Blue Cog?? on it's !!spindle??.", line++, true)
                line(player,"I placed the !!Black Cog?? on it's !!spindle??.", line++, true)
                line(player,"I placed the !!White Cog?? on it's !!spindle??.", line++, true)
                line(player,"I placed the !!Red Cog?? on it's !!spindle??.", line++, true)
                line(player, "<col=FF0000>QUEST COMPLETE!", line++ +1)
            }

                //line(player,"", line++)

        }
    }

    override fun finish(player: Player) {
        var ln = 10
        super.finish(player)
        player.packetDispatch.sendString("You have completed the Clock Tower Quest!", 277, 4)
        player.packetDispatch.sendItemZoomOnInterface(1891, 240, 277, 5)

        drawReward(player,"1 Quest Point", ln++)
        drawReward(player,"500 coins", ln++)

        addItemOrDrop(player, Items.COINS_995, 500)

    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }
}