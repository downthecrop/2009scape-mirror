package content.region.kandarin.quest.observatoryquest

import content.data.Quests
import core.api.*
import core.api.setVarp
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs09.consts.Items

// http://youtu.be/TWIkCRRea8A The initial quest log.
// http://youtu.be/onHNm9Z5L-o The best full quest log.
// http://youtu.be/cq-jGTXXHuE
// http://youtu.be/ekYr30h43ag Scorpius.
/**
 * Observatory Quest
 */
@Initializable
class ObservatoryQuest : Quest(Quests.OBSERVATORY_QUEST, 96, 95, 2,112, 0, 1, 7) {

    companion object {
        val questName = Quests.OBSERVATORY_QUEST
        const val observatoryVarp = 112
        const val goblinStoveVarbit = 3837
        const val telescopeVarbit = 3838

        const val starChartsInterface = 104
        const val telescopeInterface = 552
        const val dungeonWarning = 560

        const val attributeKilledGuard = "/save:quest:observatoryquest-killedguard"
        const val attributeUnlockedGate = "/save:quest:observatoryquest-unlockedgate"
        const val attributeTelescopeStar = "/save:quest:observatoryquest-telescopestar" // NULL - not seen telescope, 19 - 30 one of the random star patterns
        const val attributeReceivedWine = "/save:quest:observatoryquest-receivedwine"
        const val attributeReceivedMould = "/save:quest:observatoryquest-receivedmould"
        const val attributeRandomChest = "/save:quest:observatoryquest-randomchest"
        const val attributeFinishedCutscene = "/save:quest:observatoryquest-finishedcutscene"

    }
    override fun drawJournal(player: Player, stage: Int) {
        super.drawJournal(player, stage)
        var line = 12
        var stage = getStage(player)

        var started = getQuestStage(player, questName) > 0

        if (!started) {
            line(player, "I can start this quest by speaking to the !!professor?? in the", line++, false)
            line(player, "!!Observatory reception, south-west of Ardougne.??", line++, false)
        } else {
            line(player, "I can start this quest by speaking to the professor in the", line++, true)
            line(player, "Observatory reception, south-west of Ardougne.", line++, true)

            if (stage >= 5) {
                line(player, "I should take the letter the Examiner has given me to the", line++, true)
                line(player, "Curator of Varrock Museum, for his approval.", line++, true)
                line++
            } else if (stage >= 1) {
                line(player, "Seems the observatory telescope needs repairing, due to", line++)
                line(player, "the nearby goblins. The !!professor?? wants me to help by", line++)
                line(player, "getting the following, with the help of his !!assistant??:", line++)
                line++
            }
            if (stage >= 2) {
                line(player, "!!3 plain wooden planks??", line++, true)
            } else if (stage >= 1) {
                line(player, "!!3 plain wooden planks??", line++, inInventory(player, Items.PLANK_960, 3))
            }
            if (stage >= 3) {
                line(player, "!!1 bronze bar??", line++, true)
            } else if (stage >= 2) {
                line(player, "!!1 bronze bar??", line++, inInventory(player, Items.BRONZE_BAR_2349))
            }
            if (stage >= 4) {
                line(player, "!!1 molten glass??", line++, true)
            } else if (stage >= 3) {
                line(player, "!!1 molten glass??", line++, inInventory(player, Items.MOLTEN_GLASS_1775))
            }
            if (stage >= 5) {
                line(player, "!!1 lens mould??", line++, true)
            } else if (stage >= 4) {
                line(player, "!!1 lens mould??", line++)
            }

            if (stage >= 6) {
                line(player, "The professor was pleased to have all the pieces needed", line++, true)
                line(player, "to fix the telescope. Apparently, the professor's last", line++, true)
                line(player, "attempt at Crafting ended in disaster. So, he wants me to", line++, true)
                line(player, "create the lens by using the molten glass with the mould.", line++, true)
                line(player, "Fine by me!", line++, true)
            } else if (stage >= 5) {
                line(player, "The !!professor?? was pleased to have all the pieces needed", line++)
                line(player, "to fix the telescope. Apparently, the professor's last", line++)
                line(player, "attempt at Crafting ended in disaster. So, he wants me to", line++)
                line(player, "create the !!lens?? by using the !!molten glass?? with the !!mould??.", line++)
                line(player, "Fine by me!", line++)
            }

            if (stage >= 100) {
                line(player, "The professor has gone ahead to the Observatory. He", line++, true)
                line(player, "wants me to meet him there by travelling through the", line++, true)
                line(player, "dungeon below it.", line++, true)
            } else if (stage >= 6) {
                line(player, "The !!professor?? has gone ahead to the !!Observatory??. He", line++)
                line(player, "wants me to meet him there by travelling through the", line++)
                line(player, "!!dungeon?? below it.", line++)
            }
            if (stage >= 100) {
                line++
                line(player,"<col=FF0000>QUEST COMPLETE!</col>", line)
            }
        }
    }

    override fun finish(player: Player) {
        var ln = 10
        super.finish(player)
        player.packetDispatch.sendString("You have completed the Observatory Quest!", 277, 4)
        // This image is special since it isn't an item, but a standalone model.
        player.packetDispatch.sendModelOnInterface(1174, 277, 5, 0) // Scenery 2210, Model 1174
        player.packetDispatch.sendAngleOnInterface(277, 5, 2040, 0, 1836)

        drawReward(player, "2 Quest Points", ln++)
        drawReward(player, "2,250 Crafting XP", ln++)
        drawReward(player, "A payment depending on", ln++)
        drawReward(player, "which constellation you", ln++)
        drawReward(player, "observed", ln++)

        rewardXP(player, Skills.CRAFTING, 2250.0)
    }

    override fun updateVarps(player: Player) {
        setVarp(player, observatoryVarp, getQuestStage(player, questName), true)
        if(getQuestStage(player, questName) >= 6) {
            setVarbit(player, telescopeVarbit, 1, true)
        } else {
            setVarbit(player, telescopeVarbit, 0, true)
        }
        if(getQuestStage(player, questName) >= 7) {
            setVarp(player, observatoryVarp, 7, true)
        }
    }

    override fun reset(player : Player) {
        removeAttribute(player, attributeKilledGuard)
        removeAttribute(player, attributeUnlockedGate)
        removeAttribute(player, attributeTelescopeStar)
        removeAttribute(player, attributeReceivedWine)
        removeAttribute(player, attributeReceivedMould)
        removeAttribute(player, attributeRandomChest)
        removeAttribute(player, attributeFinishedCutscene)
        setVarbit(player, 3837, 0, true)
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }
}