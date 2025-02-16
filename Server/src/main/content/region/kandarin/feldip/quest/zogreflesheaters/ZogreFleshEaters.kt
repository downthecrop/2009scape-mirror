package content.region.kandarin.feldip.quest.zogreflesheaters

import content.data.Quests
import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs09.consts.Items

/**
 * Zogre Flesh Eaters Quest
 *
 * 1 - Talked to Grish
 * 2 - Smashed Barricade
 * 2 Cont' - Collected Stuff Underground
 *   A - Black Prism from Coffin
 *   B - Half Torn Page from Broken Lecturn
 *   C - Tankard from Backpack (Kill the Zombie which turns out to be Brentle Vahn)
 * 3 - Talked to Zavistic Rarve with A & B only, points to where Sithik Ints is
 * 4 - Talked to Sithik and challenged to incriminate him
 * 4 Cont' - Collected Evidence
 *   A - Tankard from 2C after talking to Innkeeper
 *   B - Portrait of Sithik using papyrus and charcoal with questionable drawing skills
 *   C - Book of HAM philosophy from drawer
 *   D - Necromantic book
 * 5 - Incriminate Sithik to Zavistic Rarve and given potion
 * 6 - Turned Sithik into an Ogre with potion
 * 7 - Talked to Grish
 * 8 - Get Key from Grish
 * 9 - Killed Slash Bash
 * 100 - Returned to Grish with Ogre Artifact
 *
 * This quest journal is the worst; there's typos, and it is out of order.
 *
 * if (VARPBIT[487] > 12) return 2; if (VARPBIT[487 == 0) return 0; return 1; };
 * define_varbit 487 455 0 4
 *
 */
@Initializable
class ZogreFleshEaters : Quest(Quests.ZOGRE_FLESH_EATERS, 40, 39, 1, 455, 0, 1, 13) {
    companion object {
        val questName = Quests.ZOGRE_FLESH_EATERS
        const val varbitGateBashed = 496
        const val varbitOgreCoffin = 488
        const val varbitSithikOgre = 495

        const val attributeAskedAboutSickies = "quest:zogreflesheaters-askedaboutsickies"

        // 2A
        const val attributeSearchedCoffin = "/save:quest:zogreflesheaters-searchedcoffin"
        const val attributeBrokeLockCoffin = "/save:quest:zogreflesheaters-brokelockcoffin"
        const val attributeOpenedCoffin = "/save:quest:zogreflesheaters-openedcoffin" // You can fail apparently...
        const val attributeFoundBlackPrism = "/save:quest:zogreflesheaters-foundblackprism"
        // 2B
        const val attributeFoundHalfTornPage = "/save:quest:zogreflesheaters-foundhalftornpage"
        // 2C
        const val attributeFoughtZombie = "/save:quest:zogreflesheaters-foughtzombie"
        const val attributeFoundTankard = "/save:quest:zogreflesheaters-foundtankard"
        // 4A
        const val attributeAskedAboutTankard = "quest:zogreflesheaters-askedabouttankard"
        // 4B
        const val attributeMadePortrait = "/save:quest:zogreflesheaters-madeportrait"
        // 4C
        const val attributeFoundHamBook = "/save:quest:zogreflesheaters-foundhambook"
        // 4D
        const val attributeFoundNecromanticBook = "/save:quest:zogreflesheaters-foundnecromanticbook"

        const val attributeSlashBashInstance = "/save:quest:zogreflesheaters-slashbashinstance"

        // Open Uglug Nar Shop with Relicym balm
        const val attributeOpenUglugNarShop = "/save:quest:zogreflesheaters-openuglugnarshop"

        fun requirements(player: Player): Boolean {
            return arrayOf(
                    hasLevelStat(player, Skills.RANGE, 30),
                    hasLevelStat(player, Skills.SMITHING, 4),
                    hasLevelStat(player, Skills.HERBLORE, 8),
                    isQuestComplete(player, Quests.JUNGLE_POTION),
                    isQuestComplete(player, Quests.BIG_CHOMPY_BIRD_HUNTING),
            ).all { it }
        }
    }

    override fun drawJournal(player: Player, stage: Int) {
        super.drawJournal(player, stage)
        var line = 11
        var stage = getStage(player)

        var started = getQuestStage(player, questName) > 0

        if (!started) {
            line(player, "I can !!start?? this quest by talking to !!Grish?? at the Ogrish", line++, false)
            line(player, "ceremonial dance place called !!Jiggig??.", line++, false)
            line(player, "To start this !!quest?? I should complete these quests:-", line++, false)
            line(player, "!!Jungle Potion.??", line++, isQuestComplete(player, Quests.JUNGLE_POTION))
            line(player, "!!Big Chompy Bird Hunting.??", line++, isQuestComplete(player, Quests.BIG_CHOMPY_BIRD_HUNTING))
            line(player, "It would help if I had the following skills levels:-", line++, false)
            line(player, "!!Ranged level : 30??", line++, hasLevelStat(player, Skills.RANGE, 30))
            line(player, "!!Fletching level : 30??", line++, hasLevelStat(player, Skills.FLETCHING, 30))
            line(player, "!!Smithing level : 4??", line++, hasLevelStat(player, Skills.SMITHING, 4))
            line(player, "!!Herblore level : 8??", line++, hasLevelStat(player, Skills.HERBLORE, 8))
            line(player, "Must be able to defeat a !!level 111?? foe.", line++, false)
        } else if (stage < 100) {

            line(player, "I started this quest by talking to Grish, he asked me to", line++, true)
            line(player, "check out the underground area where some Zombie ogres", line++, true)
            line(player, "(Zogres) were coming from.", line++, true)

            if (stage >= 2) {
                line(player, "I have to find a way into the ceremonial dance area and", line++, true)
                line(player, "then underground.", line++, true)
                line(player, "I persuaded a guard to let me past, I only had to mention", line++, true)
                line(player, "Grish's name and the guard smashed the barricade down. I", line++, true)
                line(player, "can enter now.", line++, true)
            } else if (stage >= 1) {
                line(player, "I have to find a way into the ceremonial dance area and", line++, false)
                line(player, "then underground.", line++, false)
            }

            if (stage >= 2) {
                // Set 2A (Stays)
                if (getAttribute(player, attributeFoundBlackPrism, false) || stage >= 3) {
                    line(player, "I have searched a coffin, it had a funny looking hole at the", line++, true)
                    line(player, "side.", line++, true)
                    line(player, "I have forced the lock on a coffin, maybe I can open it", line++, true)
                    line(player, "now?", line++, true)
                    line(player, "I've !!opened?? the !!coffin?? and retrieved a !!black prism??, this", line++, stage >= 3)
                    line(player, "may be useful.", line++, stage >= 3)
                } else if (getAttribute(player, attributeOpenedCoffin, false)) {
                    line(player, "I have searched a coffin, it had a funny looking hole at the", line++, true)
                    line(player, "side.", line++, true)
                    line(player, "I have forced the lock on a coffin, maybe I can open it", line++, true)
                    line(player, "now?", line++, true)
                    line(player, "I've !!opened?? the !!coffin??, maybe there's something in it.", line++, false)
                } else if (getAttribute(player, attributeBrokeLockCoffin, false)) {
                    line(player, "I have searched a coffin, it had a funny looking hole at the", line++, true)
                    line(player, "side.", line++, true)
                    line(player, "I have forced the lock on a !!coffin??, maybe I can open it", line++, false)
                    line(player, "now?", line++, false)
                } else if (getAttribute(player, attributeSearchedCoffin, false)) {
                    line(player, "I have searched a coffin, it had a funny looking hole at the", line++, true)
                    line(player, "side.", line++, true)
                }
            }

            if (stage >= 2) {
                // Set 2B (Stays)
                if (getAttribute(player, attributeFoundHalfTornPage, false) || stage >= 3) {
                    line(player, "I found a !!half torn page?? from a !!necromatic spellbook??,", line++, stage >= 3)
                    line(player, "maybe this is a !!clue???", line++, stage >= 3)
                }
            }

            // 4 - This is the weirdest shit that is out of place. (Stays) This appears after talking to Sithik Int.
            if (stage >= 4) {
                line(player, "I have shown the prism and torn page to the grand", line++, true)
                line(player, "secretary of the wizards guild.", line++, true)
            }

            // Set 2C (Does not stay)
            if (stage in 2..4 && getAttribute(player, attributeFoundTankard, false)) {
                line(player, "I killed a !!human zombie?? which dropped a !!backpack??. The", line++, stage >= 3)
                line(player, "!!backpack?? had the name !!'B. Vahn'?? on it, inside the !!backpack??", line++, stage >= 3)
                line(player, "I found a !!tankard??.", line++, stage >= 3)
            } else if (stage in 2..4 && getAttribute(player, attributeFoughtZombie, false)) {
                line(player, "I killed a !!human zombie?? which dropped a !!backpack??.", line++, stage >= 3)
            }

            // Stays until you find all the 3X stuff above.
            if (stage in 2..4 && !(getAttribute(player, attributeFoundBlackPrism, false) &&
                            getAttribute(player, attributeFoundHalfTornPage, false) &&
                            getAttribute(player, attributeFoundTankard, false))) {
                line(player, "I need to find out what happened here.", line++, false) // Cleared when the 3 sets above are done.
            }

            // Set 4A (Does not stay)
            if (stage in 2..4 && getAttribute(player, attributeAskedAboutTankard, false)) {
                line(player, "The 'Dragon Inn' !!Innkeeper?? says the tankard belongs to", line++, false)
                line(player, "one of his locals called !!Brentle Vahn??. He was seen talking", line++, false)
                line(player, "to a !!wizard?? the other day.", line++, false) // Cleared after handed in with the rest
            }

            // 3 Zavistic Rarve seen prism and page - lines disappears right after...
            if (stage == 3) {
                line(player, "I have shown the !!prism?? and the !!necromantic page?? to", line++, false)
                line(player, "Zavistic Rarve. He's told me about a !!wizard?? named", line++, false)
                line(player, "!!Sithik Ints?? who might have some information.", line++, false)
            }

            // 4 Spoken with Sithik
            if (stage >= 4) {
                line(player, "I've spoken to !!Sithik??, I need to see if he was !!involved??", line++, stage >= 5)
                line(player, "with the !!Undead Ogres at 'Jiggig'?? in some way.", line++, stage >= 5)
            }

            // Only stays for 4
            if (stage == 4) {
                if (getAttribute(player, attributeMadePortrait, false)) {
                    line(player, "I've made a !!portrait?? of !!Sithik??...not sure what this will do?", line++, false)
                }
                if (getAttribute(player, attributeFoundHamBook, false)) {
                    line(player, "I've found a !!book?? on !!HAM philosophy??...what does this prove?", line++, false)
                }
                if (getAttribute(player, attributeFoundNecromanticBook, false)) {
                    line(player, "I've found a !!necromantic book??...what does this prove?", line++, false)
                }
            }

            // 4 - Who the hell knows why this is here. (Stays) This appears after getting the potion.
            if (stage >= 5) {
                line(player, "I've talked to Zavistic Rarve regarding the prism and the", line++, true)
                line(player, "torn page, he gave some information on a student called", line++, true)
                line(player, "Sithik Ints, he may know more about what's happening", line++, true)
                line(player, "here.", line++, true)
            }

            // Beyond this is legit
            if (stage >= 5) {
                line(player, "Zavistic has given me some sort of !!potion??, apparently I", line++, stage >= 6)
                line(player, "need to give it to !!Sithik??.", line++, stage >= 6)
            }

            if (stage >= 7) {
                line(player, "I came back into Sithik's room to find that he had been", line++, true)
                line(player, "turned into an Ogre!", line++, true)
            } else if (stage >= 6) {
                line(player, "I have put some of the !!potion?? into !!Sithik's tea??, the !!potion??", line++, false)
                line(player, "will take some time to act. Perhaps I should !!get out of??", line++, false)
                line(player, "!!here?? in case there are any !!side effects???", line++, false)
            }

            if (stage >= 8) {
                line(player, "Sithik has told me how to make 'brutal arrows', which", line++, true)
                line(player, "should be more effective against Zogres.", line++, true)

                line(player, "Sithik has given me some pointers on how I can make a", line++, true)
                line(player, "cure disease potion, though I'm still not sure exactly which", line++, true)
                line(player, "herbs I should use.", line++, true)
            } else if (stage >= 7) {
                line(player, "!!Sithik?? has told me that there is no way I can remove the", line++, false)
                line(player, "effects of the !!necromantic curse spell?? from the !!Jiggig??", line++, false)
                line(player, "area. I'll have to go back and let !!Grish?? know.", line++, false)

                line(player, "!!Sithik?? has told me how to make !!'brutal arrows'??, which", line++, false)
                line(player, "should be more !!effective?? against !!Zogres??.", line++, false)

                line(player, "!!Sithik?? has given me some pointers on how I can make a", line++, false)
                line(player, "!!cure disease potion??, though I'm still not sure exactly which", line++, false)
                line(player, "!!herbs?? I should use.", line++, false)
            }

            if (stage >= 9) {
                line(player, "I've told Grish to relocated the dance area, but he needs", line++, true)
                line(player, "me to get something from the tomb to so that he can do", line++, true) // "tomb to so" is authentic [sic]
                line(player, "this.", line++, true)
                line(player, "I need to go back into the tomb and look for some 'old'", line++, true)
                line(player, "items that Grish has asked for.", line++, true)
                line(player, "I should return the !!artifact?? to !!Grish??.", line++, false)
            } else if (stage >= 8) {
                line(player, "I've told Grish to relocated the dance area, but he needs", line++, true)
                line(player, "me to get something from the tomb to so that he can do", line++, true) // "tomb to so" is authentic [sic]
                line(player, "this.", line++, true)
                line(player, "I need to go back into the !!tomb?? and look for some !!'old'??", line++, false)
                line(player, "!!items?? that !!Grish?? has asked for.", line++, false)
            }

        } else {
            // The ending is COMPLETELY replaced from this entire shitshow of a quest log.
            line(player, "I talked to Grish in the Jiggig area which is swarming with", line++, true)
            line(player, "Zombie Ogres (Zogres) These disgusting creatures carry", line++, true)
            line(player, "disease and are quite dangerous so the Ogres weren't", line++, true)
            line(player, "too keen to try and sort them out.", line++, true)

            line(player, "I talked to an ogre called Grish who asked me to look into", line++, true)
            line(player, "the problem. After some searching around in a tomb, I", line++, true)
            line(player, "found some clues which pointed me to the human", line++, true)
            line(player, "habitation of Yannile.", line++, true)

            line(player, "With the help of Zavistic Rarve, the grand secretary of", line++, true)
            line(player, "the Wizards guild I was able to piece the clues together", line++, true)
            line(player, "and discover that a Wizard named 'Sithik Ints' was", line++, true)
            line(player, "responsible.", line++, true)

            line(player, "Unfortunately I couldn't remove the curse from the area,", line++, true)
            line(player, "however, I was able to return some important artefacts to", line++, true)
            line(player, "Grish, who can now set up a new ceremonial dance area for", line++, true)
            line(player, "the ogres of Gu' Tanoth.", line++, true)

            line(player, "Sithik Ints also told me how to make Brutal arrows which are", line++, true)
            line(player, "more effective against Zogres, and he also told me how to", line++, true)
            line(player, "make a disease balm.", line++, true)
            line++
            line(player,"<col=FF0000>QUEST COMPLETE!</col>", line)
        }
    }

    override fun reset(player: Player) {
        setVarp(player, varbitGateBashed, 0, true)
        removeAttribute(player, attributeAskedAboutSickies)
        removeAttribute(player, attributeSearchedCoffin)
        removeAttribute(player, attributeBrokeLockCoffin)
        removeAttribute(player, attributeOpenedCoffin)
        removeAttribute(player, attributeFoundBlackPrism)
        removeAttribute(player, attributeFoundHalfTornPage)
        removeAttribute(player, attributeFoughtZombie)
        removeAttribute(player, attributeFoundTankard)
        removeAttribute(player, attributeAskedAboutTankard)
        removeAttribute(player, attributeMadePortrait)
        removeAttribute(player, attributeFoundHamBook)
        removeAttribute(player, attributeFoundNecromanticBook)
        removeAttribute(player, attributeOpenUglugNarShop)

    }
    override fun finish(player: Player) {
        var ln = 10
        super.finish(player)
        player.packetDispatch.sendString("You have completed Zogre Flesh Eaters!", 277, 4)
        player.packetDispatch.sendItemZoomOnInterface(Items.OGRE_ARTEFACT_4818, 240, 277, 5)

        drawReward(player,"1 Quest Point.", ln++)
        drawReward(player,"Can now make Brutal Arrows", ln++)
        drawReward(player,"and cure disease potions.", ln++)
        drawReward(player,"2000 Ranged, Fletching and", ln++)
        drawReward(player,"Herblore XP", ln++)

        player.skills.addExperience(Skills.RANGE, 2000.0)
        player.skills.addExperience(Skills.FLETCHING, 2000.0)
        player.skills.addExperience(Skills.HERBLORE, 2000.0)
    }

    override fun setStage(player: Player, stage: Int) {
        super.setStage(player, stage)
        this.updateVarps(player)
    }

    override fun updateVarps(player: Player) {
        if(getQuestStage(player, questName) >= 2) {
            setVarbit(player, varbitGateBashed, 1, true)
        } else {
            setVarbit(player, varbitGateBashed, 0, true)
        }
        if(getQuestStage(player, questName) >= 7) {
            setVarbit(player, varbitSithikOgre, 1, true)
        } else {
            setVarbit(player, varbitSithikOgre, 0, true)
        }
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }
}