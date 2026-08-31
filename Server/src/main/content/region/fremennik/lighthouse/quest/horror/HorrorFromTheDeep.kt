package content.region.fremennik.lighthouse.quest.horror

import content.data.Quests
import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs09.consts.Components
import org.rs09.consts.Items

/**
 * Horror From The Deep Quest
 */

@Initializable
class HorrorFromTheDeep : Quest(Quests.HORROR_FROM_THE_DEEP, 77, 76, 2, HFTD_QUEST_VARBIT, 0, 1, 10) {

    companion object {
        // overall varp containing all the varbits below
        const val HORROR_VARP = 351

        // quest progress varbit
        const val HFTD_QUEST_VARBIT = 34

        /* Quest stages:
         * 0 - unstarted
         * 1 - talked to Larrissa
         * 2 - entered lighthouse after getting key and fixing bridge
         * 3 - talked to Larrissa inside the lighthouse
         * 4 - fixed light
         * 5 - dag fight
         * 10 - quest complete
         */

        // other varbits
        const val HFTD_DOOR = 35            // 1 if you've gotten to the strange door
        const val HFTD_BRIDGE_W = 36        // 1 if you've fixed the west side of bridge
        const val HFTD_BRIDGE_E = 37        // 1 if you've fixed the east side of bridge
        const val HFTD_LIGHTHOUSE_KEY = 38  // 1 if you obtained key from gunnjorn
        const val HFTD_LIGHTHOUSE_DOOR = 39 // 1 if you entered lighthouse
        const val HFTD_WALL_FIRE = 40       // 1 if you put a fire rune in the door
        const val HFTD_WALL_WATER = 41      // 1 if you put a water rune in the door
        const val HFTD_WALL_EARTH = 42      // 1 if you put an earth rune in the door
        const val HFTD_WALL_AIR = 43        // 1 if you put an air rune in the door
        const val HFTD_WALL_SWORD = 44      // 1 if you put a sword in the door
        const val HFTD_WALL_ARROW = 45      // 1 if you put an arrow in the door
        const val HFTD_LIGHT_TAR = 46       // 1 if you use swamp tar to fill the light
        const val HFTD_LIGHT_LENS = 47      // 1 if you use a tinderbox to light the light
        const val HFTD_LIGHT_LIT = 48       // 1 if you use molten glass to fix the lens

        // attributes
        const val GOD_BOOKS_ACCESS = "/save:god_books:access" // do not reset on quest complete
        const val HFTD_NEEDS_CASKET = "/save:needs-casket"    // if the player didn't have enough room for a casket after the fight
        const val HFTD_COMBAT = "dagFight"                    // if the player is in the boss fight
        const val HFTD_TARGET = "target"                      // the dag's target player during boss fight
        const val HFTD_BABY_DEAD = "/save:hftd:babyDag"       // if you killed the first dag in the boss fight
    }

    override fun drawJournal(player: Player, stage: Int, ) {
        super.drawJournal(player, stage)
        var line = 11

        // stage 1 checks
        val key = getVarbit(player, HFTD_LIGHTHOUSE_KEY) == 1
        val bridgeWest = getVarbit(player, HFTD_BRIDGE_W) == 1
        val bridgeEast = getVarbit(player, HFTD_BRIDGE_E) == 1

        // stage 2 checks
        val enteredLighthouse = getVarbit(player, HFTD_LIGHTHOUSE_DOOR) == 1

        // stage 3 checks
        val tar = getVarbit(player, HFTD_LIGHT_TAR) == 1
        val lit = getVarbit(player, HFTD_LIGHT_LIT) == 1
        val lens = getVarbit(player, HFTD_LIGHT_LENS) == 1

        // stage 4 checks
        val door = getVarbit(player, HFTD_DOOR) == 1

        when(stage) {
            // stage 0: not started
            0 -> {
                line(player, "I can start this quest by speaking to !!Larrissa?? at the", line++)
                line(player, "!!Lighthouse?? to the !!North?? of the !!Barbarian Outpost??.", line++)
                line(player, "To complete this quest I need:", line++)
                line(player, "!!Level 35 agility??", line++, hasLevelStat(player, Skills.AGILITY, 35)) // needed to access barbarian agility course
                line(player, "!!Level 13 or higher magic will be an advantage??", line++, hasLevelStat(player, Skills.MAGIC, 13))
                line(player, "!!I must also be able to defeat strong level 100 enemies??", line++)
                limitScrolling(player, line, true)
            }

            // stages 1-3
            in 1..3 -> {
                // stage 1: accepted quest from Larrissa
                line(player, "I travelled to an isolated !!Lighthouse?? north of the !!Barbarian outpost??, ", line++, stage >= 2)
                line(player, "to find a !!Fremennik?? girl called !!Larrissa?? locked outside, ", line++, stage >= 2)
                line(player, "and worried about her boyfriend !!Jossik??.", line++, stage >= 2)
                line++

                // bridge repair
                if (bridgeWest
                    && bridgeEast) {
                    line(player, "I repaired the !!bridge?? to Rellekka with some planks.", line++, true)
                } else {
                    line(player, "I need to !!repair the bridge?? leading to Rellekka.", line++, false)
                }
                // getting key
                if (key) {
                    line(player, "I recovered a !!spare key?? from Larrissa's cousin !!Gunnjorn??", line++, true)
                } else {
                    line(player, "I also need to get the lighthouse key from her cousin Gunnjorn.", line++, false)
                }
                line++
                // talk to Larrissa after you've done all this and quest stage advances to 2
                if (stage == 1
                    && bridgeWest
                    && bridgeEast
                    && key) {
                    line(player, "I should talk to !!Larrissa??.", line++, false)
                    line++
                }

                // stage 2: talked to larrissa, after got key and fixed bridge
                if (stage >= 2) {
                    line(player, "I also need to use the key I got from Gunnjorn", line++, enteredLighthouse)
                    line(player, "to enter the lighthouse.", line++, enteredLighthouse)
                    line++

                    // entered the lighthouse. talk to larrissa inside the lighthouse
                    if (enteredLighthouse
                        && stage == 2) {
                        line(player, "I should talk to !!Larrissa??.", line++, false)
                        line++
                    }
                }

                // stage 3: talked to Larrissa inside the lighthouse
                if (stage >= 3) {
                    line(player, "Now I need to find some way of fixing the lighthouse lamp.", line++, false)
                    line(player, "I need to re-tar the lighthouse torch.", line++, tar)
                    line(player, "I need to light the lighthouse torch.", line++, lit)
                    line(player, "I need to fix the lighthouse lens.", line++, lens)
                }
            }

            // stages 4-5
            in 4..5 -> {
                // stage 4: fixed the lighthouse light
                line(player, "I repaired the bridge leading to Rellekka and got a key from Gunnjorn", line++, true)
                line(player, "so I could enter the lighthouse.", line++, true)
                line(player, "I have re-tarred the lighthouse torch.", line++, true)
                line(player, "I have fixed the lighthouse lens.", line++, true)
                line(player, "I have relit the lighthouse torch.", line++, true)
                line++

                // after the light is fixed, you can go down to the basement
                if (stage == 4
                    && door
                ) {
                    line(player, "After I entered the !!lighthouse??, and repaired the !!lighting mechanism??,", line++, false)
                    line(player, "I discovered a !!strange wall?? that blocked the entrance to an", line++, false)
                    line(player, "underground cavern.", line++, false)
                    line++
                }

                // stage 5: dag fight
                if (stage == 5) {
                    line(player, "I found Jossik in an underground cavern, behind a strange wall", line++, false)
                    line(player, "where he had been attacked by some sea creatures.", line++, false)
                    line(player, "I must defeat these sea monsters to save him!", line++, false)
                }
            }

            // quest complete!
            100 -> {
                line(player, "I travelled to an isolated Lighthouse north of the", line++, true)
                line(player, "Barbarian outpost, to find a Fremennik girl called Larrissa", line++, true)
                line(player, "locked outside, and worried about her boyfriend Jossik.", line++, true)
                line++
                line(player, "I recovered a spare key from Larrissa's cousin Gunnjorn", line++, true)
                line(player, "and repaired the bridge to Rellekka with some planks.", line++, true)
                line++
                line(player, "After I entered the lighthouse, and repaired the lighting", line++, true)
                line(player, "mechanism, I discovered a strange wall that blocked the", line++, true)
                line(player, "entrance to an underground cavern, where Jossik was.", line++, true)
                line(player, "", line++, true)
                line++
                line(player, "After I killed some strange sea monsters, I managed", line++, true)
                line(player, "to get !!Jossik?? out of the cavern and back to the", line++, true)
                line(player, "lighthouse.", line++, true)
                line++
                line(player, "I found a !!strange casket?? on the dead body of the !!sea monster??, ", line++, getAttribute(player, GOD_BOOKS_ACCESS, false))
                line(player, "which !!Jossik?? said he could tell me about.", line++, getAttribute(player, GOD_BOOKS_ACCESS, false))
                line++
                line(player, "<col=FF0000>QUEST COMPLETE!</col>", line, false)
                limitScrolling(player, line, false)
            }
        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        var ln = 10

        // trivia: the only quest that has different text in journal than the usual "You have completed the ... Quest!"
        sendString(player,
            "You have survived the ${Quests.HORROR_FROM_THE_DEEP}!",
            Components.QUEST_COMPLETE_SCROLL_277,
            4
        )
        sendItemZoomOnInterface(player, Components.QUEST_COMPLETE_SCROLL_277, 5, Items.RUSTY_CASKET_3849)
        drawReward(player, "2 Quest Points", ln++)
        drawReward(player, "4662 XP in each of: Ranged,", ln++)
        drawReward(player, "Magic, Strength", ln)

        rewardXP(player, Skills.RANGE, 4662.0)
        rewardXP(player, Skills.MAGIC, 4662.0)
        rewardXP(player, Skills.STRENGTH, 4662.0)

        // the other attributes should stay
        removeAttribute(player, HFTD_BABY_DEAD)
    }

    // use ::setqueststage 77 0 to reset the quest
    override fun reset(player: Player) {

        // remove attributes
        removeAttributes(
            player,
            HFTD_NEEDS_CASKET,
            GOD_BOOKS_ACCESS,
            HFTD_BABY_DEAD
        )

        // reset the varp, which wipes all the varbits
        setVarp(player, HORROR_VARP, 0, true)
    }

    // this is called whenever setQuestStage is called
    override fun updateVarps(player: Player) {
        val stage = getQuestStage(player, Quests.HORROR_FROM_THE_DEEP)

        // the quest stages are perfectly lined up with the varbit
        if (stage >= 10) {
            // except for stage 100, which is varbit set to 10
            setVarbit(player, HFTD_QUEST_VARBIT, 10, true)
        } else {
            setVarbit(player, HFTD_QUEST_VARBIT, stage, true)
        }
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }
}
