package content.region.kandarin.quest.templeofikov

import core.api.*
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs09.consts.Items

/**
 * Temple of Ikov Quest
 *
 * This is the quest where the journal after completion is a barren wasteland of a useless log.
 * One of the worst logs I've ever had the pleasure/pain of doing.
 *
 * 1 - Talked to Lucien (Lucien pendant)
 * 2 - Entered chamber of fear (North Fence)
 * 3 - Toggled lever(with disabling trap)
 * 4 - Killed fire warrior (comes with trying to open the back door)
 * 5 - Winelda given 20 fukkin Limpwurt
 * 6 - Good (Armadyl pendant), Bad (Took the Staff of Armadyl)
 * 7,100 - Good (Killed Lucien), Bad (Gave Lucien staff)
 *
 * In parallel (all before 4, log disappears after 4)
 * A - Took boots
 * B - Cross bridge and found lever
 * C - Attached lever to switch near entrance lever
 * D - Found ice arrows
 */
@Initializable
class TempleOfIkov : Quest("Temple of Ikov", 121, 120, 1,26, 0, 1, 80 /* 80 or 90 since there's 2 endings */) {

    companion object {
        const val questName = "Temple of Ikov"
        const val attributeChosenEnding = "/save:quest:templeofikov-chosenending"

        const val attributeDisabledTrap = "/save:quest:templeofikov-disabledtrap"
        const val attributeTalkedToWinelda = "/save:quest:templeofikov-talkedtowinelda"

        const val attributeCrossedBridge = "/save:quest:templeofikov-crossedbridge"
        const val attributeIceChamberAccess = "/save:quest:templeofikov-icechamberaccess"
        const val attributeIceArrows = "/save:quest:templeofikov-icearrows"

        const val attributeRandomChest = "quest:templeofikov-randomChest"
        const val attributeWarriorInstance = "quest:templeofikov-warriorInstance"
    }
    override fun drawJournal(player: Player, stage: Int) {
        super.drawJournal(player, stage)
        var line = 12
        var stage = getStage(player)

        var started = getQuestStage(player, questName) > 0

        if (!started) {
            line(player, "I can start this quest at the !!Flying Horse Inn?? in !!Ardougne??", line++, false)
            line(player, "by speaking to !!Lucien??", line++, false)
            line++
            line(player, "To start this quest I will need:", line++, false)
            line(player, "Level 42 !!Thieving??", line++, hasLevelStat(player, Skills.THIEVING, 42))
            line(player, "Level 40 !!Ranged??", line++, hasLevelStat(player, Skills.RANGE, 40))
            line(player, "Ability to defeat a level 84 enemy with Ranged.", line++, false)
        } else {
            if (stage >= 2) {
                line(player, "Lucien has asked me to retrieve the !!Staff of Armadyl?? from", line++, true)
                line(player, "from the !!Temple of Ikov??. The entrance is near !!Hemenster??. He has", line++, true)
                line(player, "given me a !!pendant?? so I can enter the !!chamber of fear??.", line++, true)
            } else if (stage >= 1) {
                line(player, "Lucien has asked me to retrieve the !!Staff of Armadyl?? from", line++, false)
                line(player, "from the !!Temple of Ikov??. The entrance is near !!Hemenster??. He has", line++, false)
                line(player, "given me a !!pendant?? so I can enter the !!chamber of fear??.", line++, false)
            }

            if (stage == 2) {
                line(player, "I have entered the chamber of fear.", line++, true)

            }
            if (stage == 3) {
                line(player, "I have entered the chamber of fear. I found a trap on a", line++, true)
                line(player, "lever and have disabled it. I pulled the lever.", line++, true)
            }

            // This is a whole side part of retrieving ice arrows that would disappear once you reach stage 4.
            if (stage < 4) {
                // This is questionable. It's seen in 2014, but seems to be a "side thing". It tracks that you've obtained ice arrows. Derived from RS3.
                if (getAttribute(player, attributeIceArrows, false)) {
                    line(player, "I have found some boots that make me lighter. I made it", line++, true)
                    line(player, "across the lava bridge and found a lever. I fit the lever", line++, true)
                    line(player, "into the bracket and pulled the lever. I found arrows", line++, true)
                    line(player, "made of ice in a chest.", line++, true)
                } else if (getAttribute(player, attributeIceChamberAccess, false)) {
                    line(player, "I have found some boots that make me lighter. I made it", line++, true)
                    line(player, "across the lava bridge and found a lever. I fit the lever", line++, true)
                    line(player, "into the bracket and pulled the lever.", line++, true)
                } else if (getAttribute(player, attributeCrossedBridge, false)) {
                    line(player, "I have found some boots that make me lighter. I made it", line++, true)
                    line(player, "across the lava bridge and found a lever.", line++, true)
                }
            }

            // Derived. Need sources.
            if (stage in 2..3) {
                line++
                line(player, "I need to find the entrance to the !!Temple of Ikov??", line++, false)
            }

            if (stage == 4) {
                line(player, "I have entered the chamber of fear. I found a trap on a", line++, true)
                line(player, "lever and have disabled it. I pulled the lever. I went into", line++, true)
                line(player, "another chamber and was attacked by a Fire Warrior! I", line++, true)
                line(player, "killed it using arrows made of ice and my trusty bow.", line++, true)
            }
            if (stage == 4 && getAttribute(player, attributeTalkedToWinelda, false) ) {
                line++
                // This will never show up crossed. https://www.youtube.com/watch?v=OKYM2oFOUtk 5:18
                line(player, "My path is blocked by lava. !!Winelda?? will teleport me across", line++, false)
                line(player, "if I get her !!twenty limpwurt roots??.", line++, false)
            }
            if (stage == 5) {
                line(player, "I have entered the chamber of fear. I found a trap on a", line++, true)
                line(player, "lever and have disabled it. I pulled the lever. I went into", line++, true)
                line(player, "another chamber and was attacked by a Fire Warrior! I", line++, true)
                line(player, "killed it using arrows made of ice and my trusty bow.", line++, true)
            }

            if (stage == 6 && getAttribute(player, attributeChosenEnding, 0) == 1 ) {
                line(player, "I have entered the chamber of fear. I found a trap on a", line++, true)
                line(player, "lever and have disabled it. I pulled the lever. I went into", line++, true)
                line(player, "another chamber and was attacked by a Fire Warrior! I", line++, true)
                line(player, "killed it using arrows made of ice and my trusty bow.", line++, true)
                line++
                line(player, "I agreed to help the !!Guardians of Armadyl??, I will kill !!Lucien??.", line++, false)
                line(player, "The guardians gave me a !!pendant?? that I will need to enable", line++, false)
                line(player, "me to attack him.", line++, false)
            }

            if (stage == 6 && getAttribute(player, attributeChosenEnding, 0) == 2 ) {
                line(player, "I have entered the chamber of fear. I found a trap on a", line++, true)
                line(player, "lever and have disabled it. I pulled the lever. I went into", line++, true)
                line(player, "another chamber and was attacked by a Fire Warrior! I", line++, true)
                line(player, "killed it using arrows made of ice and my trusty bow.", line++, true)
                line++
                // Derived. Need sources.
                line(player, "I recovered the !!Staff of Armadyl?? from the !!Temple of Ikov??.", line++, false)
                line(player, "!!Lucien?? is staying at his house west of the !!Grand Exchange??", line++, false)
                line(player, "in !!Varrock??.", line++, false)
            }

            if (stage >= 100) {
                if (getAttribute(player, attributeChosenEnding, 0) == 1) {
                    //end quest kill lucien https://www.youtube.com/watch?v=cePHhIOqsqg 19:40
                    line++
                    line(player, "I agreed to help the Guardians of Armadyl, I killed Lucien", line++, true)
                    line(player, "and banished him from this plane!", line++, true)
                }

                if (getAttribute(player, attributeChosenEnding, 0) == 2) {
                    //end quest helped lucien
                    line++
                    line(player, "I recovered the Staff of Armadyl from the Temple of Ikov.", line++, true)
                    line(player, "Lucien was staying at his house west of the Grand Exchange", line++, true)
                    line(player, "in Varrock. He said that the staff had made him more", line++, true)
                    line(player, "powerful!", line++, true)
                }

                line++
                line(player,"<col=FF0000>QUEST COMPLETE!</col>", line)
            }
        }

    }

    override fun reset(player: Player) {
        removeAttribute(player, attributeChosenEnding)
        removeAttribute(player, attributeDisabledTrap)
        removeAttribute(player, attributeTalkedToWinelda)
        removeAttribute(player, attributeCrossedBridge)
        removeAttribute(player, attributeIceArrows)
        removeAttribute(player, attributeIceChamberAccess)
    }

    override fun finish(player: Player) {
        var ln = 10
        super.finish(player)
        when (getAttribute(player, attributeChosenEnding, 0)){
            1 -> player.packetDispatch.sendString("Temple of Ikov Quest completed for Armadyl!", 277, 4)
            2 -> player.packetDispatch.sendString("Temple of Ikov Quest completed for Lucien!", 277, 4)
            else -> player.packetDispatch.sendString("Temple of Ikov Quest completed!!", 277, 4)
        }
        player.packetDispatch.sendItemZoomOnInterface(Items.YEW_LONGBOW_855,230,277,5)

        drawReward(player, "1 Quest Point", ln++)
        drawReward(player, "10,500 Ranged XP", ln++)
        drawReward(player, "8,000 Fletching XP", ln++)

        rewardXP(player, Skills.RANGE, 10500.0)
        rewardXP(player, Skills.FLETCHING, 8000.0)
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }
}