package content.region.kandarin.ardougne.quest.clocktower

import core.api.addItemOrDrop
import core.api.getAttribute
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.plugin.Initializable
import org.rs09.consts.Items

/**
 * https://www.youtube.com/watch?v=Cl68Z0bsRq4
 */
@Initializable
class ClockTower : Quest("Clock Tower",38, 37, 1, 10, 0, 1, 8) {
    companion object {
        const val questName = "Clock Tower"
        const val attributeBlueCog = "/save:quest:clocktower-bluecogplaced"
        const val attributeBlackCog = "/save:quest:clocktower-blackcogplaced"
        const val attributeWhiteCog = "/save:quest:clocktower-whitecogplaced"
        const val attributeRedCog = "/save:quest:clocktower-redcogplaced"
        const val attributeBlackCogCooled = "/save:quest:clocktower-blackcogcooled"
        const val attributeRatsPoisoned = "/save:quest:clocktower-poisonplaced"
        const val attributeAskKojoAboutRats = "quest:clocktower-askkojoaboutrats"
    }

    override fun drawJournal(player: Player?, stage: Int) {
        super.drawJournal(player, stage)

        var line = 12
        var stage = getStage(player)

        when (stage) {
            0 -> {
                line(player, "I can start this quest by speaking to !!Brother Kojo?? at the", line++)
                line(player, "!!Clock Tower?? which is located !!South?? of !!Ardougne??", line++)
            }

            in 1 .. 10 -> {
                line(player, "I spoke to Brother Kojo at the Clock Tower South of", line++, true)
                line(player, "Ardougne and agreed to help him repair the clock.", line++, true)
                line(player,"To repair the clock I need to find the four coloured cogs", line++, false)
                line(player,"and place them on the four correctly coloured spindles.", line++, false)
                line(player, if (getAttribute(player!!, attributeBlueCog, false)) "I placed the !!Blue Cog?? on it's !!spindle??." else "I haven't placed the !!Blue Cog?? on it's !!spindle?? yet.", line++, getAttribute(player!!, attributeBlueCog, false))
                line(player, if (getAttribute(player!!, attributeBlackCog, false)) "I placed the !!Black Cog?? on it's !!spindle??." else "I haven't placed the !!Black Cog?? on it's !!spindle?? yet.", line++, getAttribute(player!!, attributeBlackCog, false))
                line(player, if (getAttribute(player!!, attributeWhiteCog, false)) "I placed the !!White Cog?? on it's !!spindle??." else "I haven't placed the !!White Cog?? on it's !!spindle?? yet.", line++, getAttribute(player!!, attributeWhiteCog, false))
                line(player, if (getAttribute(player!!, attributeRedCog, false)) "I placed the !!Red Cog?? on it's !!spindle??." else "I haven't placed the !!Red Cog?? on it's !!spindle?? yet.", line++, getAttribute(player!!, attributeRedCog, false))
            }

            100 -> {
                line(player, "I spoke to Brother Kojo at the Clock Tower South of", line++, true)
                line(player, "I have placed all four cogs successfully on the spindles.", line++, true)
                line(player,"Brother Kojo was grateful for all my help and rewarded me.", line++, true)
                line++
                line(player,"<col=FF0000>QUEST COMPLETE!</col>", line)
            }
        }
    }

    override fun finish(player: Player) {
        var ln = 10
        super.finish(player)
        player.packetDispatch.sendString("You have completed the Clock Tower Quest!", 277, 4)
        player.packetDispatch.sendItemZoomOnInterface(Items.COINS_995, 240, 277, 5)

        drawReward(player,"1 Quest Point", ln++)
        drawReward(player,"500 coins", ln++)

        addItemOrDrop(player, Items.COINS_995, 500)
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }
}
