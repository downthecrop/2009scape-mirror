package content.region.morytania.quest.creatureoffenkenstrain

import core.api.*
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs09.consts.Items

val CREATURE_OF_FENKENSTRAIN = "Creature of Fenkenstrain"

/**
 * Creature of Fenkenstrain Quest
 *
 * https://www.youtube.com/watch?v=zmvgzuA31S0
 * https://www.youtube.com/watch?v=RoR6zLGrRoY
 * https://www.youtube.com/watch?v=I6X0kER1wDA - You can telekinetic grab the pickled brain jar lol
 * https://www.youtube.com/watch?v=53_2dKEu0xo - has the letter and the bottom part of the quest dialogue
 * https://www.youtube.com/watch?v=s57ElnRNhmU - in 2014 but the most thorough

 * 1 - Read Signpost to start the quest
 * 2 - Talked to Fenkenstrain
 * 3 - Collected body parts
 * 4 - Gave needle and thread
 * 5 - Repaired the lightning conductor and bring the creature to life
 * 6 - Talked to the creature
 * 100 - Stoped Fenkenstrain by stealing ring of charos
 */
@Initializable
class CreatureOfFenkenstrain : Quest("Creature of Fenkenstrain", 41, 40, 2, 399, 0, 1, 9) {

    companion object {
        const val questName = "Creature of Fenkenstrain"
        const val attributeArms = "/save:quest:creatureoffenkenstrain-arms"
        const val attributeLegs = "/save:quest:creatureoffenkenstrain-legs"
        const val attributeTorso = "/save:quest:creatureoffenkenstrain-torso"
        const val attributeHead = "/save:quest:creatureoffenkenstrain-decaphead"
        const val attributeUnlockedMemorial = "/save:quest:creatureoffenkenstrain-amuletonmemorial"
        const val attributeUnlockedShed = "/save:quest:creatureoffenkenstrain-keyonsheddoor"
        const val attributeNeedle = "/save:quest:creatureoffenkenstrain-needle"
        const val attributeThread = "/save:quest:creatureoffenkenstrain-thread"
        const val fenkenstrainVarp = 399
    }
    override fun drawJournal(player: Player, stage: Int) {
        super.drawJournal(player, stage)
        var line = 12
        var stage = getStage(player)

        var started = getQuestStage(player, questName) > 0

        if(!started){
            line(player, "I can start this quest by reading the signpost in the", line++, false)
            line(player, "centre of !!Canifis??.", line++, false)
            line(player, "I must be able to defeat a !!level 51 monster??, and need the", line++, false)
            line(player, "following skill levels:", line++, false)
            line(player, "Level 20 Crafting", line++, hasLevelStat(player, Skills.CRAFTING, 20))
            line(player, "Level 25 Theiving", line++, hasLevelStat(player, Skills.THIEVING, 25))
            line(player, "I also need to have completed the following quests:", line++, false)
            line(player, "Priest in Peril", line++, isQuestComplete(player, "Priest in Peril"))
            line(player, "Restless Ghost", line++, isQuestComplete(player, "The Restless Ghost"))
        } else {
            line(player, "I read the signpost in Canifis, which tells of a butler", line++, true)
            line(player, "position that is available at the castle to the northeast.", line++, true)
            if (stage >= 2) {
                line(player, "I spoke to Fenkenstrain, who wanted me to find him some", line++, true)
                line(player, "body parts so that he could build a creature.", line++, true)
            } else if (stage >= 1) {
                line(player, "I should go up to the castle and speak to !!Dr Fenkenstrain??", line++, false)
            }
            if (stage >= 3) {
                line(player, "I gave a torso, some arms and legs, and a head to", line++, true)
                line(player, "Fenkenstrain, who then wanted a needle and 5 lots of", line++, true)
                line(player, "thread, so that he could sew the bodyparts together and", line++, true)
                line(player, "create his creature.", line++, true)
            } else if (stage >= 2) {
                line++
                line(player, "I need to find these body parts for !!Fenkenstrain??:", line++, false)
                line(player, "a pair of !!arms??", line++, false)
                line(player, "a pair legs !!legs??", line++, false)
                line(player, "a !!torso??", line++, false)
                line(player, "a !!head??", line++, false)
                line++
                line(player, "Apparently the soil of !!Morytania?? has a unique quality", line++, false)
                line(player, "which preserves the bodies of the dead better than", line++, false)
                line(player, "elsewhere, so perhaps I should look at the graves in the", line++, false)
                line(player, "local area", line++, false)
            }
            if (stage >= 4) {
                line(player, "I brought Fenkenstrain a needle and 5 quantities of", line++, true)
                line(player, "thread.", line++, true)
            } else if (stage >= 3) {
                line(player, "I need to bring !!Fenkenstrain?? a !!needle?? and !!5 quantities??", line++, false)
                line(player, "!!of thread??.", line++, false)
            }
            if (stage >= 5) {
                line(player, "I repaired the lightning conductor, and Fenkenstrain", line++, true)
                line(player, "brought the Creature to life.", line++, true)
            } else if (stage >= 4) {
                line(player, "!!Fenkenstrain?? has ordered me to repair the lightning", line++, false)
                line(player, "conductor.", line++, false)
            }
            if (stage == 5) {
                line(player, "!!Fenkenstrain?? wants to talk to me.", line++, false)
                line++
            }
            if (stage >= 7) {
                line(player, "The Creature went on a rampage, and Fenkenstrain sent", line++, true)
                line(player, "me up to the Tower to destroy it.", line++, true)
                line(player, "The Creature convinced me to stop Fenkenstrain's", line++, true)
                line(player, "experiments once and for all, and has told me the true", line++, true)
                line(player, "history of Fenkenstrain's treachery.", line++, true)
            } else if (stage >= 6) {
                line(player, "The !!Creature?? went on a rampage, and !!Fenkenstrain?? wants", line++, false)
                line(player, "me to go up the !!Tower?? to destroy it.", line++, false)
            }
            if (stage >= 8) {
                line(player, "I stole Fenkenstrain's Ring of Charos, and he released", line++, true)
                line(player, "me from his service.", line++, true)
            } else if (stage >= 7) {
                line(player, "I must find a way to stop Fenkenstrain's experiments.", line++, false)
            }
            if (stage >= 100) {
                line++
                line(player,"<col=FF0000>QUEST COMPLETE!</col>", line)
            }
        }
    }

    override fun hasRequirements(player: Player): Boolean {
        return arrayOf(
                hasLevelStat(player, Skills.CRAFTING, 20),
                hasLevelStat(player, Skills.THIEVING, 25),
                isQuestComplete(player, "Priest in Peril"),
                isQuestComplete(player, "The Restless Ghost"),
        ).all { it }
    }

    override fun reset(player: Player) {
        setVarp(player, fenkenstrainVarp, 0, true)
        removeAttribute(player, attributeArms)
        removeAttribute(player, attributeLegs)
        removeAttribute(player, attributeTorso)
        removeAttribute(player, attributeHead)
        removeAttribute(player, attributeUnlockedMemorial)
        removeAttribute(player, attributeUnlockedShed)
        removeAttribute(player, attributeNeedle)
        removeAttribute(player, attributeThread)
    }

    override fun finish(player: Player) {
        var ln = 10
        super.finish(player)
        player.packetDispatch.sendString("You have completed Creature of Fenkenstrain!", 277, 4)
        player.packetDispatch.sendItemZoomOnInterface(Items.RING_OF_CHAROS_4202,230,277,5)

        drawReward(player, "2 quest points", ln++)
        drawReward(player, "Ring of Charos", ln++)
        drawReward(player, "1,000 Theiving XP", ln++)

        addItemOrDrop(player, Items.RING_OF_CHAROS_4202, 1)
        rewardXP(player, Skills.THIEVING, 1000.0)
    }

    override fun setStage(player: Player, stage: Int) {
        super.setStage(player, stage)
        this.updateVarps(player)
    }

    override fun updateVarps(player: Player) {
        // This is a bit of a hack. I didn't manage to align the quest with the varp,
        // so I had to include both stage 3 and 4 to varp value 3 to show the creature.
        if(getQuestStage(player, questName) == 4) {
            setVarp(player, fenkenstrainVarp, 3, true)
        }
        if(getQuestStage(player, questName) >= 8) {
            setVarp(player, fenkenstrainVarp, 8, true)
        }
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }
}