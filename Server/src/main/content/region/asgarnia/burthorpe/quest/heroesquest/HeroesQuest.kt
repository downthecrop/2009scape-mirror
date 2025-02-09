package content.region.asgarnia.burthorpe.quest.heroesquest

import content.data.Quests
import content.region.misthalin.varrock.quest.shieldofarrav.ShieldofArrav
import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs09.consts.Items

/**
 * Heroes' Quest
 */
@Initializable
class HeroesQuest : Quest(Quests.HEROES_QUEST,75, 74, 1, 188, 0, 1, 15) {
    /**
     *  Do note: "other players can help you even if they have already finished Heroes' Quest"
     *  1 - Talked to Achietties to start the quest
     *
     *  PHOENIX
     *  2 - Talked to Katrine
     *  3 - Talked to Alfonse
     *  4 - Talked to Charlie
     *  5 - HIDDEN Killed Grip (You need key from Black Arm Friend)
     *  6 - Talked to Katrine with Candlestick
     *  BLACK ARM
     *  2 - Talked to Straven
     *  3 - Talked to Trobert
     *  4 - Talked to Garv
     *  5 - HIDDEN Unlocked Chest (You need Grip killed from Phoenix Friend)
     *  6 - Talked to Katrine with Candlestick
     *
     *  100 - Achiettes with all the items
     */

    companion object {
        const val attributeGruborLetsYouIn = "/save:quest:heroesquest-gruborletsyouin"
        const val attributeGripTookPapers = "/save:quest:heroesquest-griptookpapers"
        const val attributeGripSaidDuties = "/save:quest:heroesquest-gripsaidduties"
        const val attributeHasOpenedBackdoor = "/save:quest:heroesquest-hasopenedbackdoor"
        const val attributeHasOpenedChestDoor = "/save:quest:heroesquest-hasopenedchestdoor"

        fun checkQuestsAreComplete(player: Player): Boolean {
            return isQuestComplete(player, Quests.SHIELD_OF_ARRAV) &&
                    isQuestComplete(player, Quests.LOST_CITY) &&
                    isQuestComplete(player, Quests.MERLINS_CRYSTAL) &&
                    isQuestComplete(player, Quests.DRAGON_SLAYER) &&
                    getQuestPoints(player) >= 55
        }

        /** Abstraction of Shield of Arrav isPhoenix function */
        fun isPhoenix(player: Player): Boolean {
            return ShieldofArrav.isPhoenix(player)
        }

        /** Abstraction of Shield of Arrav isBlackArm function */
        fun isBlackArm(player: Player): Boolean {
            return ShieldofArrav.isBlackArm(player)
        }

        fun hasRequirements(player: Player): Boolean {
            return arrayOf(
                    hasLevelStat(player, Skills.HERBLORE, 25),
                    hasLevelStat(player, Skills.MINING, 50),
                    hasLevelStat(player, Skills.FISHING, 53),
                    hasLevelStat(player, Skills.COOKING, 53),
                    isQuestComplete(player, Quests.SHIELD_OF_ARRAV),
                    isQuestComplete(player, Quests.LOST_CITY),
                    isQuestComplete(player, Quests.MERLINS_CRYSTAL),
                    isQuestComplete(player, Quests.DRAGON_SLAYER),
                    getQuestPoints(player) >= 55,
            ).all { it }
        }

        fun allItemsInInventory(player: Player): Boolean {
            return inInventory(player, Items.FIRE_FEATHER_1583) &&
                    inInventory(player, Items.LAVA_EEL_2149) &&
                    inInventory(player, Items.THIEVES_ARMBAND_1579)
        }
    }

    override fun drawJournal(player: Player?, stage: Int) {
        super.drawJournal(player, stage)
        var line = 12
        var stage = getStage(player)

        var started = getQuestStage(player!!, Quests.HEROES_QUEST) > 0

        if(!started){
            if (checkQuestsAreComplete(player)) {
                line(player, "I can start this quest by speaking to !!Achietties?? at the", line++)
                line(player, "!!Heroes' Guild?? located !!North?? of !!Taverly??", line++)
                line(player, "as all required quests are complete, and I have enough QP.", line++)
            } else {
                line(player, "I can start this quest by speaking to !!Achietties?? at the", line++)
                line(player, "!!Heroes' Guild?? located !!North?? of !!Taverly?? after completing", line++)
                line(player, "!!The Shield of Arrav??", line++, isQuestComplete(player, Quests.SHIELD_OF_ARRAV))
                line(player, "!!The Lost City??", line++, isQuestComplete(player, Quests.LOST_CITY))
                line(player, "!!Merlin's Crystal??", line++, isQuestComplete(player, Quests.MERLINS_CRYSTAL))
                line(player, "!!The Dragon Slayer??", line++, isQuestComplete(player, Quests.DRAGON_SLAYER))
                line(player, "!!and gaining 55 Quest Points??", line++, getQuestPoints(player) >= 55)
            }
            line(player, "To complete this quest I need:", line++, false)
            line(player, "!!Level 25 Herblore??", line++, hasLevelStat(player, Skills.HERBLORE, 25))
            line(player, "!!Level 50 Mining??", line++, hasLevelStat(player, Skills.MINING, 50))
            line(player, "!!Level 53 Fishing??", line++, hasLevelStat(player, Skills.FISHING, 53))
            line(player, "!!Level 53 Cooking??", line++, hasLevelStat(player, Skills.COOKING, 53))
        } else if (stage < 100) {
            line(player, "!!Achietties?? will let me into the !!Heroes' Guild?? if I can get:", line++)

            // This is completely dependent on what you have in your inventory.
            if (inInventory(player, Items.FIRE_FEATHER_1583)) {
                line(player, "An Entranan Firebird Feather - I now have one on me!", line++, true)
            } else {
                line(player, "An !!Entranan Firebird Feather?? - I should check on !!Entrana??", line++)
            }

            // This is completely dependent on what you have in your inventory.
            if (inInventory(player, Items.LAVA_EEL_2149)) {
                line(player, "A cooked lava eel - I now have one on me!", line++, true)
            } else {
                line(player, "A !!cooked lava eel?? - I should speak to a !!Fishing Expert??", line++)
            }

            if (isPhoenix(player)) {
                if (inInventory(player, Items.THIEVES_ARMBAND_1579)) {
                    line(player, "A Master Thieves Armband - I now have one on me!", line++, true)
                } else {
                    line(player, "A !!Master Thieves Armband?? - the !!Phoenix Gang can help me??", line++)
                }

                if (!inInventory(player, Items.THIEVES_ARMBAND_1579)) {
                    if (stage >= 2) {
                        line(player, "I spoke to Straven about the Master Thieves Armband.", line++, true)
                    }

                    if (stage >= 3) {
                        line(player, "Then I told Alfonse the password 'Gherkin'.", line++, true)
                    } else if (stage >= 2) {
                        line(player, "He told me I can get one by stealing !!Pete's Candlestick??", line++)
                        line(player, "I should use the password he gave me at !!Brimhaven??", line++)
                    }

                    if (stage >= 4) {
                        line(player, "Charlie told me about a secret door into Scarface Pete's", line++, true)
                        line(player, "hideout, but he couldn't find a way of getting through it.", line++, true)
                    } else if (stage >= 3) {
                        line(player, "He said, secretly speak to !!Charlie?? round the back.", line++)
                    }

                    if (stage >= 6) {
                        line(player, "A rival gang member collected a candlestick for me after I", line++, true)
                        line(player, "killed Grip and got the Treasure Room key for them.", line++, true)
                        line(player, "I gave Straven Scarface Pete's candlestick, and in reward", line++, true)
                        line(player, "he gave me a Master Thieves Armband to prove my skills.", line++, true)
                    } else if (stage >= 4) {
                        line(player, "Maybe !!another player?? can help to get through this !!door???.", line++)
                    }
                }
            }
            if (isBlackArm(player)) {
                if (inInventory(player, Items.THIEVES_ARMBAND_1579)) {
                    line(player, "A Master Thieves Armband - I now have one on me!", line++, true)
                } else {
                    line(player, "A !!Master Thieves Armband?? - the !!Black Arms can help me??", line++)
                }

                if (!inInventory(player, Items.THIEVES_ARMBAND_1579)) {
                    if (stage >= 2) {
                        line(player, "I spoke to Katrine about the Master Thieves Armband.", line++, true)
                    }

                    if (stage >= 3) {
                        line(player, "I used the Black Arm password to enter the Brimhaven HQ.", line++, true)
                    } else if (stage >= 2) {
                        line(player, "She told me I can get one by stealing !!Pete's Candlestick??", line++)
                        line(player, "I should use the password she gave me at !!Brimhaven??", line++)
                    }

                    if (stage >= 4) {
                        line(player, "I managed to pass myself off as Hartigen and enter the", line++, true)
                        line(player, "HQ.", line++, true)
                    } else if (stage >= 3) {
                        line(player, "I need to disguise myself as !!Hartigen the Black Knight?? in", line++)
                        line(player, "order to get inside !!Scarface Pete's hideout??", line++)
                    }

                    if (stage >= 6) {
                        line(player, "I collected the candlesticks with the Treasure Room key", line++, true)
                        line(player, "after a rival gang member killed Grip.", line++, true)
                        line(player, "I gave Katrine Scarface Pete's candlestick, and in reward", line++, true)
                        line(player, "she gave me a Master Thieves Armband to prove my skills.", line++, true)
                    } else if (stage >= 4) {
                        line(player, "I can move around the hideout, but now I need Grips keys", line++)
                        line(player, "to get into the treasure room and get the candlesticks.", line++)
                        line(player, "I need !!another player's help?? with this, as it's so risky.", line++)
                    }
                }
            }

            if (allItemsInInventory(player)) {
                line(player, "Now that I have !!all the required items??, I should go and speak to", line++)
                line(player, "!!Achietties?? and give them to her", line++)
            }
        } else {
            // Everything above is replaced by this.
            line(player, "I gave Achietties an Entranan Firebird Feather, A cooked", line++, true)
            line(player, "lava eel from a dangerous fishing spot and after some", line++, true)
            line(player, "difficulty, a Master Thief Armband.", line++, true)
            line(player, "Once I had handed these over to Achietties I had proved", line++, true)
            line(player, "myself worthy of entrance to the Heroes' Guild.", line++, true)
            line++
            line++
            line(player,"<col=FF0000>QUEST COMPLETE!</col>", line)
        }
    }

    override fun reset(player: Player) {
        if (getQuestStage(player, Quests.HEROES_QUEST) == 0) {
            removeAttribute(player, attributeGruborLetsYouIn)
            removeAttribute(player, attributeGripTookPapers)
            removeAttribute(player, attributeGripSaidDuties)
            removeAttribute(player, attributeHasOpenedBackdoor)
            removeAttribute(player, attributeHasOpenedChestDoor)

            // For testing: if you set quest stage to 0, it will switch your gang (blackarm <-> phoenix)
            // Remember to set stage to 0 twice to keep your gang.
            println("Swapping gang for Heroes Quest.")
            ShieldofArrav.swapGang(player)
        }
    }

    override fun finish(player: Player) {
        var ln = 10
        super.finish(player)
        player.packetDispatch.sendString("You have completed the Heroes Quest!", 277, 4)
        player.packetDispatch.sendItemZoomOnInterface(Items.DRAGON_BATTLEAXE_1377, 240, 277, 5)

        drawReward(player,"1 Quest Point", ln++)
        drawReward(player,"Access to the Heroes' Guild", ln++)
        drawReward(player,"A total of 29,232 XP spread", ln++)
        drawReward(player,"over twelve skills", ln++)

        rewardXP(player, Skills.ATTACK, 3075.0)
        rewardXP(player, Skills.DEFENCE, 3075.0)
        rewardXP(player, Skills.STRENGTH, 3075.0)
        rewardXP(player, Skills.HITPOINTS, 3075.0)
        rewardXP(player, Skills.RANGE, 2075.0)
        rewardXP(player, Skills.FISHING, 2725.0)
        rewardXP(player, Skills.COOKING, 2825.0)
        rewardXP(player, Skills.WOODCUTTING, 1575.0)
        rewardXP(player, Skills.FIREMAKING, 1575.0)
        rewardXP(player, Skills.SMITHING, 2725.0)
        rewardXP(player, Skills.MINING, 2575.0)
        rewardXP(player, Skills.HERBLORE, 1325.0)
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }
}