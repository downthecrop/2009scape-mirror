package content.region.asgarnia.taverley.quest

import content.region.asgarnia.taverley.dialogue.KaqemeexDialogue
import core.api.*
import core.game.component.Component
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs

/**
 * Druidic Ritual Quest
 */
@Initializable
class DruidicRitual : Quest("Druidic Ritual", 48, 47, 4, 80, 0, 3, 4) {

    companion object {
        const val questName = "Druidic Ritual"
    }
    override fun drawJournal(player: Player, stage: Int) {
        super.drawJournal(player, stage)
        var line = 12
        var stage = getStage(player)

        var started = getQuestStage(player, questName) > 0

        if (!started) {
            line(player, "I can start this quest by speaking to !!Kaqemeex?? who is at", line++)
            line(player, "the !!Druids Circle?? just !!north?? of !!Taverly??.", line++)
        } else {
            line(player, "Kaqemeex and the druids are preparing a ceremony to", line++, true)
            line(player, "purify the Varrock stone circle. I told Kaqemeex I would", line++, true)
            line(player, "help them.", line++, true)

            if (stage >= 20) {
                // Line disappears.
            } else if (stage >= 10) {
                line++
                line(player, "I should speak to !!Sanfew?? in the village to the !!south??.", line++)
            }

            if (stage >= 99) {
                line(player, "The ceremony required various meats being placed in the", line++, true)
                line(player, "Cauldron of Thunder. I did this and gave them to Sanfew.", line++, true)
            } else if (stage >= 20) {
                line++
                line(player, "!!Sanfew?? told me for the ritual they would need me to place", line++)
                line(player, "!!raw bear meat??, !!raw chicken??, !!raw rat meat??, and !!raw beef?? in", line++)
                line(player, "the !!Cauldron of Thunder?? in the !!dungeon south?? of !!Taverley??.", line++)
            }

            if (stage >= 100) {
                line(player, "Kaqemeex then taught me the basics of the Heblore skill.", line++, true)
            } else if (stage >= 99) {
                line++
                line(player, "I should speak to !!Kaqemeex?? again and claim my !!reward??.", line++)
            }

            if (stage >= 100) {
                line++
                line(player,"<col=FF0000>QUEST COMPLETE!</col>", line++)
                line(player, "I gained !!4 quest points??, !!250 Heblore XP?? and access to", line++)
                line(player, "the !!Heblore skill??.", line)
            }
        }
    }

    override fun finish(player: Player) {
        var ln = 10
        super.finish(player)
        player.packetDispatch.sendString("You have completed the Druidic Ritual Quest!", 277, 4)
        player.packetDispatch.sendItemZoomOnInterface(Items.CLEAN_GUAM_249, 230, 277, 5)

        drawReward(player,"4 Quest Points", ln++)
        drawReward(player,"250 Herblore XP", ln++)
        drawReward(player,"Access to Herblore skill", ln++)

        rewardXP(player, Skills.HERBLORE, 250.0)

        player.interfaceManager.closeChatbox()
    }

    override fun questCloseEvent(player: Player?, component: Component?) {
        queueScript(player!!, 1, QueueStrength.SOFT) {
            openDialogue(player,455, NPC.create(455, player.location), true)
            return@queueScript stopExecuting(player)
        }
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }
}
