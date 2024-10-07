package content.region.morytania.quest.naturespirit

import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs09.consts.Items

@Initializable
class NatureSpiritQuest : Quest("Nature Spirit", 95, 94, 2, 307, 0, 1, 110 ) {
    override fun newInstance(`object`: Any?): Quest {
        return this
    }

    override fun drawJournal(player: Player?, stage: Int) {
        super.drawJournal(player, stage)
        player ?: return
        var line = 12
        if(stage == 0){
            line(player, "I can start this quest by speaking to !!Drezel?? in the temple.", line++)
            line(player, /* The "to" is [sic] */"to !!Saradomin?? at the mouth of the river !!Salve??.", line++)
            line(player, "I first need to complete :", line++)
            line(player, "!!The Restless Ghost.??", line++, isQuestComplete(player, "The Restless Ghost"))
            line(player, "!!Priest in Peril.??", line++, isQuestComplete(player, "Priest in Peril"))
            if (isQuestComplete(player, "The Restless Ghost") && isQuestComplete(player, "Priest in Peril")) {
                line(player, "I've completed all the quest requirements.", line++)
            }
            line(player, "In order to complete this quest !!level 18 crafting?? would be", line++, getStatLevel(player, Skills.CRAFTING) >= 18)
            line(player, "an advantage.", line++, getStatLevel(player, Skills.CRAFTING) >= 18)
            if (getStatLevel(player, Skills.CRAFTING) >= 18) {
                line(player, "I have a suitable crafting level for this quest.", line++)
            }
            if (isQuestComplete(player, "The Restless Ghost") && isQuestComplete(player, "Priest in Peril") && getStatLevel(player, Skills.CRAFTING) >= 18) {
                line(player, "I have all the requirements for this quest.", line++)
            }
        } else if (stage < 100) {
            line(player, "After talking to Drezel in the temple of Saradomin I've", line++, true)
            line(player, "agreed to look for a Druid called Filliman Tarlock.", line++, true)

            if (stage >= 15) {
                line(player, "I've found a spirit in the swamp which I think might be", line++, true)
                line(player, "Filliman Tarlock.", line++, true)
            } else if (stage >= 10) {
                line(player, "I need to look for !!Filliman Tarlock?? in the !!Swamps?? of Mort", line++)
                line(player, "Myre. I should be wary of !!Ghasts??.", line++)
            }

            if (stage >= 20) {
                line(player, "I've communicated with Fillman using the amulet of", line++, true)
                line(player, "ghostspeak.", line++, true)
            } else if (stage >= 15) {
                // Questionable
                line(player, "I located a !!spirit?? in the swamp. I believe he's", line++)
                line(player, "!!Filliman Tarlock?? but I can't understand him.", line++)
            }

            if (stage >= 25) {
                line(player, "I managed to convince Fillman that he's a ghost.", line++, true)
            } else if (stage >= 20) {
                line(player, "I think I need to convince this poor fellow !!Tarlock?? that he's", line++)
                line(player, "actually !!dead??!", line++)
            }

            if (stage >= 30) {
                line(player, "Fillman is looking for his journal to help him plan what his", line++, true)
                line(player, "next step is.", line++, true)
                line(player, "I've given Filliman his journal. I wonder what he plans to do", line++, true)
                line(player, "now?", line++, true)
            } else if (stage >= 25){
                line(player, "Fillman is looking for his !!journal?? to help him plan what his", line++, true)
                line(player, "next step is.", line++, true)
                // Questionable
//                line(player, "Filliman needs his !!journal?? to figure out what to do",line++)
//                line(player, "next. He mentioned something about a !!knot??.", line++)
            }

            if (stage >= 35) {
                line(player, "I've agreed to help Fillman become a nature spirit.", line++, true)
                line(player, "I need to find 'something from nature', 'something of", line++, true)
                line(player, "faith' and 'something of the spirit-to-become freely", line++, true)
                line(player, "given'.", line++, true)
            } else if (stage >= 30) {
                // Derived by squinting hard
                line(player, "!!Filliman?? might need !!my help?? with his !!plan??.", line++)
                // Questionable
//                line(player, "I should speak to !!Filliman Tarlock?? to see what I can", line++)
//                line(player, "do to help.", line++)
            }

            if (stage >= 40) {
                line(player, "Filliman gave me a 'bloom' spell to cast in the swamp.", line++, true)
                line(player, "With the bloom spell I can collect 'Something of nature.'", line++, true)
                line(player, "I've been blessed at the temple by Drezel.", line++, true)
            } else if (stage >= 35) {
                line(player, "!!Filliman?? gave me a '!!bloom??' spell but I need to be !!blessed?? at", line++)
                line(player, "the !!temple?? before I can cast it. I am supposed to collect", line++)
                line(player, "'!!something from nature??'.", line++)
            }

            if (stage >= 45) {
                // Disappears.
            } else if (stage >= 40) {
                line(player, "I should return to !!Filliman?? to see what I need to do.", line++)
            }

            if (stage in 45 until 55){
                if (NSUtils.hasPlacedFungus(player)) {
                    line(player, "I've cast the bloom spell in the swamp.", line++, true)
                    line(player, "I collected a Mort Myre Fungi.", line++, true)
                    line(player, "I think I have collected 'something of nature'.", line++, true)
                } else if (inInventory(player, Items.MORT_MYRE_FUNGUS_2970)) {
                    line(player, "I've cast the bloom spell in the swamp.", line++, true)
                    line(player, "I collected a Mort Myre Fungi.", line++, true)
                    line(player, "I have a !!Mort Myre Fungi??, I hope this is what !!Fillman??",line++)
                    line(player, "wanted.",line++)
                } else {
                    // Questionable
//                    if(stage == 50){
//                        line(player, "I know for a fact the fungus is !!something of Nature??.", line++, false)
//                    }
                    line(player, "I need to collect '!!something of nature??'.", line++)
                }

                // Just stand on the damn thing.
                line(player, "I need to find '!!something with faith??'.",line++, false)

                if (NSUtils.hasPlacedCard(player)) {
                    line(player, "The spell scroll was absorbed into the spirit stone I think I", line++, true)
                    line(player, "have collected 'something of spirit-to-become freely", line++, true)
                    line(player, "given.'", line++, true)
                } else {
                    line(player, "I need to find :",line++)
                    line(player, "'!!something of the spirit-to-be freely given??.'", line++)
                }
            }

            if (stage >= 55) {
                line(player, "I managed to get all the required items that Fillman asked.", line++, true)
                line(player, "for. He says that he can cast the spell now which will", line++, true)
                line(player, "transform him into a Nature Spirit.", line++, true)
            }

            if (stage >= 60) {
                line(player, "I entered Fillimans grotto as he asked me to.", line++, true) // no apostrophe is sic
                line(player, "Filliman has turned into a nature spirit, it was an", line++, true)
                line(player, "impressive transformation!", line++, true)
                line(player, "Filliman says he can help me to defeat the ghasts.", line++, true)
            } else if (stage >= 55) {
                // Questionable
                line(player, "!!Filliman?? has asked me to enter his !!grotto??.", line++)
            }

            if (stage >= 70) {
                line(player, "Filliman has blessed the silver sickle for me.", line++, true)
                // --- Should be separate stage, but we don't have it.
                // line(player, "I need to use the !!sickle?? to make the swamp bloom.", line++)
                line(player, "I cast the bloom spell in the swamp.", line++, true)
                // --- Should be separate stage, but we don't have it.
                // line(player, "I need to collect some !!bloomed items?? from the swamp", line++)
                // line(player, "and put them into a druid pouch.", line++)
                line(player, "I collected some bloomed items from the swamp an put", line++, true)
                line(player, "them into a druid pouch.", line++, true)
            } else if (stage >= 60) {
                // Questionable
                line(player, "I need to bring a silver sickle for !!Filliman?? to bless.", line++)
            }
            // We don't have this stage.
//            if (stage >= 80) {
//                line(player, "The druid pouch made a ghast appear which I attacked and", line++, true)
//                line(player, "killed.", line++, true)
//                line(player, "I've killed two ghasts now.", line++, true)
//                line(player, "I've killed three ghasts now.", line++, true)
//                line(player, "I should tell !!Filliman?? that I've killed the !!three ghasts??.", line++, true)
//            } else
            if (stage >= 75){
                line(player, "!!Filliman?? asked me to kill !!three Ghasts??.", line++, false)
            }
        } else {
            // The final text is a summary of the quest.
            line(player, "Drezel, a priest of Saradomin, asked me to look for the", line++, true)
            line(player, "druid Filliman Tarlock in the swamps of Mort Myre. However", line++, true)
            line(player, "Filliman had been slain and appeared as a ghost. After", line++, true)
            line(player, "persuading Filliman that he was in fact dead I helped him to", line++, true)
            line(player, "make a transformation into a Nature Spirit.", line++, true)
            line++
            line(player, "In return for this help Filliman blessed a silver sickle and", line++, true)
            line(player, "showed me how to defeat the ghasts of Mort Myre.", line++, true)
            line(player, "He also gave me some kill experience in crafting,", line++, true)
            line(player, "hitpoints and defence.", line++, true)
            line(player, "%%QUEST COMPLETE!&&",line++)
        }
    }

    override fun finish(player: Player?) {
        super.finish(player)
        player ?: return
        var ln = 10
        player.packetDispatch.sendItemZoomOnInterface(Items.SILVER_SICKLEB_2963,230,277,5)
        drawReward(player, "2 Quest Points", ln++)
        drawReward(player, "3,000 Crafting XP",ln++)
        drawReward(player, "2,000 Hitpoints XP", ln++)
        drawReward(player, "2,000 Defence XP", ln++)
        rewardXP(player, Skills.CRAFTING, 3000.0)
        rewardXP(player, Skills.HITPOINTS, 2000.0)
        rewardXP(player, Skills.DEFENCE, 2000.0)
        NSUtils.cleanupAttributes(player)
    }
}
