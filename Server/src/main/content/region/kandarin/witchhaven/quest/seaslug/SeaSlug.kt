package content.region.kandarin.witchhaven.quest.seaslug

import content.data.Quests
import core.api.*
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs09.consts.Items

/**
 * Sea Slug Quest
 *
 * Note that the varp 159 controls the quest AND some environments:
 * The BADLY_REPAIRED_WALL_18381 disappears after varp 159 is set to 9
 * KENNITH_4864 disappears after varp 159 is set to 11
 * KENNITH_4865 (but ID 4864) appears after varp 159 is set to 11
 *
 * https://www.youtube.com/watch?v=lf83SACuIDw (This is amazing)
 * https://www.youtube.com/watch?v=VnghpKbUqKw
 * https://www.youtube.com/watch?v=thHuATlGYag
 * https://www.youtube.com/watch?v=VR91Rbyuou4 (This has many other unvisited paths)
 */
@Initializable
class SeaSlug : Quest(Quests.SEA_SLUG, 109, 108, 1,159, 0, 1, 12) {

    companion object {
        const val questVarp = 159
    }
    override fun drawJournal(player: Player, stage: Int) {
        super.drawJournal(player, stage)
        var line = 12
        var stage = getStage(player)

        var started = getQuestStage(player, Quests.SEA_SLUG) > 0

        if (!started) {
            line(player, "I can start this quest by speaking to !!Caroline?? who is !!East??", line++, false)
            line(player, "!!of Ardougne??.", line++, false)
            line++
            line(player, "Requirements:", line++, false)
            // I think this is an old line. I saw it being the other line for 30 Firemaking reqs.
            line(player, "You'll need level 30 !!Firemaking??", line++, hasLevelStat(player, Skills.FIREMAKING, 30))
            // line(player, "!!Level 30 Firemaking??", line++, hasLevelStat(player, Skills.FIREMAKING, 30))
        } else {
            line(player, "I have spoken to Caroline and agreed to help", line++, true)
            line++

            if (stage >= 3) {
                line(player, "I gave Holgart the Swamp Paste and his boat is now ready", line++, true)
                line(player, "to take me to the Fishing Platform", line++, true)
            } else if (stage >= 2) {
                // authentic from https://www.youtube.com/watch?v=VnghpKbUqKw
                line(player, "I've spoken to !!Holgart?? but his boat is broken", line++, false)
                line(player, "He needs me to bring him some !!Swamp Paste??", line++, false)
                line++
                line(player, "I can make !!Swamp Paste?? by mixing !!Swamp Tar?? with !!Flour?? and", line++, false)
                line(player, "then heating the mixture on a !!Fire??", line++, false)
                line(player, "I can find !!Swamp Tar?? in the !!Swamp South of Lumbridge??", line++, false)
                line++
                line(player, "I need to get to the !!Fishing Platform?? and find out what's", line++, false)
                line(player, "happened to Kent and Kennith", line++, false)
            } else if (stage >= 1) {
                // derived
                line(player, "I need to speak to !!Holgart??.", line++, false)
            }

            if (stage >= 5) {
                line++
                line(player, "I've found Kennith, he's hiding behind some boxes.", line++, true)
                line++
                line(player, "I've found Kent on a small island", line++, true)
            } else if (stage >= 4) {
                line++
                line(player, "I've found Kennith, he's hiding behind some boxes.", line++, true)
                line++
                line(player, "I need to find !!Kent??", line++, false)
            } else if (stage >= 3) {
                line++
                line(player, "I need to find !!Kent?? and !!Kennith??", line++, false)
            }

            if (stage >= 9) {
                line++
                line(player, "!!Kent?? has asked me to help !!Kennith?? escape", line++, true)
            } else if (stage >= 5) {
                line++
                line(player, "!!Kent?? has asked me to help !!Kennith?? escape", line++, false)
            }

            if (stage >= 9) {
                line(player, "After speaking to Bailey, I found that Sea Slugs are", line++, true)
                line(player, "afraid of heat.", line++, true)
                line(player, "I should find a way of lighting this damp torch.", line++, true)
            } else if (stage >= 6) {
                line(player, "After speaking to !!Bailey??, I found that Sea Slugs are", line++, false)
                line(player, "afraid of heat.", line++, false)
                line(player, "I should find a way of lighting this damp torch.", line++, false)
            }

            if (stage >= 8) {
                // Disappears
            } else if (stage >= 7) {
                // Derived
                line(player, "I should talk to !!Kennith??", line++, false)
            }

            if (stage >= 9) {
                line(player, "I've created an opening to let Kennith escape", line++, true)
            } else if (stage >= 8) {
                // Derived
                line(player, "I need to find a way to get !!Kennith?? out", line++, false)
            }

            if (stage >= 10) {
                line++
                line(player, "Kennith can't get downstairs without some help", line++, true)
            } else if (stage >= 9) {
                // Derived
                line++
                line(player, "I should talk to !!Kennith?? again", line++, false)
            }


            if (stage >= 11) {
                line++
                line(player, "I've used the Crane to lower Kennith into the boat", line++, true)
            } else if (stage >= 10) {
                line++
                line(player, "!!Kennith?? won't go near the !!Sea Slugs??", line++, false)
                line(player, "I need to find another way to get him out", line++, false)
            }

            if (stage >= 100) {
                line++
                line(player, "I've spoken to Caroline and she thanked me for", line++, true)
                line(player, "rescuing her family from the Sea Slugs", line++, true)
            } else if (stage >= 11) {
                line++
                line(player, "I need to take the boat back to shore and talk to !!Caroline??", line++, false)
            }

            if (stage >= 100) {
                line++
                line(player,"<col=FF0000>QUEST COMPLETE!</col>", line)
            }
        }

    }

    override fun reset(player: Player) {
        // removeAttribute(player, attributeTalkedToHolgart)
    }

    override fun finish(player: Player) {
        var ln = 10
        super.finish(player)
        player.packetDispatch.sendString("You have completed Sea Slug!", 277, 4)
        player.packetDispatch.sendItemZoomOnInterface(Items.SEA_SLUG_1466,230,277,5)

        drawReward(player, "1 Quest Point", ln++)
        drawReward(player, "7175 Fishing XP", ln++)
        drawReward(player, "Oyster pearls", ln++)

        rewardXP(player, Skills.FISHING, 7175.0)
        addItemOrDrop(player, Items.OYSTER_PEARLS_413)
    }

    override fun setStage(player: Player, stage: Int) {
        super.setStage(player, stage)
        this.updateVarps(player)
    }

    override fun updateVarps(player: Player) {
        // The quest stages are perfectly aligned with the varp since the varp controls npcs and sceneries
        if (getQuestStage(player, Quests.SEA_SLUG) >= 12) {
            setVarp(player, questVarp, 12, true) // Except for stage 100 which is varp set to 12 obviously.
        } else {
            setVarp(player, questVarp, getQuestStage(player, Quests.SEA_SLUG), true)
        }
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }
}