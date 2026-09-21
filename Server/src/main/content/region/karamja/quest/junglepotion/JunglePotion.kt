package content.region.karamja.quest.junglepotion

import content.data.Quests
import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs09.consts.Items

// Ovenbread's quest log sources:
// http://youtu.be/uMsIV1ON6bU
// http://youtu.be/5mx33KwEwAc
// http://youtu.be/ufRMHEAlQsI // BEST LOG

// Main quest source: https://www.youtube.com/watch?v=1LFaFPiWurM

/**
 * Jungle Potion Quest
 * @author Ovenbread (quest log), oftheshire (quest logic)
 */

@Initializable
class JunglePotion : Quest(Quests.JUNGLE_POTION, 81, 80, 1, JPOT_STAGE_VARP, 0, 1, 12) {

    companion object {
        // quest state varp
        const val JPOT_STAGE_VARP = 175

        // these villagers appear after Jungle Potion is completed. They are some of the varbits inside varp 534.
        const val SHARIMIKA_VB = 892     // varbit controls wrapper NPC 2504
        const val MAMMA_BUFETTA_VB = 893 // varbit controls wrapper NPC 2507
        const val LAYLEEN_VB = 894       // varbit controls wrapper NPC 2510
        const val KARADAY_VB = 895       // varbit controls wrapper NPC 2513
        const val SAFTA_DOC_VB = 896     // varbit controls wrapper NPC 2516
        const val GABOOTY_VB = 897       // varbit controls wrapper NPC 2519
        const val FANELLAMAN_VB = 898    // varbit controls wrapper NPC 2522
        const val JAGBAKOBA_VB = 899     // varbit controls wrapper NPC 2525
        const val MURCAILY_VB = 900      // varbit controls wrapper NPC 2528
        const val RIONASTA_VB = 901      // varbit controls wrapper NPC 2531
        const val TBW_VILLAGERS_VB = 902 // overlaps varbits 892-901
    }

    /**
     * Quest stages:
     * 0  - not started
     * 1  - need to pick snakeweed
     * 2  - picked snakeweed
     * 3  - given snakeweed, need to pick ardrigal
     * 4  - picked ardrigal
     * 5  - given ardrigal, need to pick sito foil
     * 6  - picked sito foil
     * 7  - given sito foil, need to pick volencia moss
     * 8  - picked volencia moss
     * 9  - given volencia moss, need to pick rogue's purse
     * 10 - picked rogue's purse
     * 11 - given rogue's purse - QUEST COMPLETE!
     */

    override fun drawJournal(player: Player, stage: Int) {
        super.drawJournal(player, stage)
        var line = 12

        val stage = getStage(player)
        val started = getQuestStage(player, Quests.JUNGLE_POTION) > 0

        if (!started) {
            line(player, "I can start this quest by speaking to !!Trufitus Shakaya??", line++, false)
            line(player, "who lives in the main hut in !!Tai Bwo Wannai??.", line++, false)
            line(player, "village on the island of !!Karamja??.", line++, false)
        } else if (stage <= 11) {
            line(player, "I spoke to Trufitus, he needs to commune with the", line++, true)
            line(player, "gods, he's asked me to help him by collecting herbs.", line++, true)
            if (stage >= 11) {
                line++
                line(player, "I've given Snakeweed, Ardrigal, Sito Foil,", line++, true)
                line(player, "Valencia Moss and Rogues Purse to Trufitus.", line++, true)
                line++
                line(player, "Trufitus needs to commune with the gods.", line++, true)
                line++
                line(player, "I should speak to !!Trufitus??", line++, false)
            } else if (stage >= 10) {
                line++
                line(player, "I picked some fresh Rogues Purse for Trufitus.", line++, true)
                line++
                line(player, "I need to give the !!Rogues Purse?? to !!Trufitus??.", line++, false)
            } else if (stage >= 9) {
                line++
                line(player, "I've given Snakeweed, Ardrigal, Sito Foil and", line++, true)
                line(player, "Valencia Moss to Trufitus.", line++, true)
                line++
                line(player, "I need to pick some fresh !!Rogues Purse?? for !!Trufitus??.", line++, false)
            } else if (stage >= 8) {
                line++
                line(player, "I picked some fresh Volencia Moss for Trufitus.", line++, true)
                line++
                line(player, "I need to give the !!Volencia Moss?? to !!Trufitus??.", line++, false)
            } else if (stage >= 7) {
                line++
                line(player, "I've given Snakeweed, Ardrigal and Sito Foil", line++, true)
                line(player, "to Trufitus.", line++, true)
                line++
                line(player, "I need to pick some fresh !!Volencia Moss?? for !!Trufitus??.", line++, false)
            } else if (stage >= 6) {
                line++
                line(player, "I picked some fresh Sito Foil for Trufitus.", line++, true)
                line++
                line(player, "I need to give the !!Sito Foil?? to !!Trufitus??.", line++, false)
            } else if (stage >= 5) {
                line++
                line(player, "I've given Snakeweed and Ardrigal to Trufitus.", line++, true)
                line++
                line(player, "I need to pick some fresh !!Sito Foil?? for !!Trufitus??.", line++, false)
            } else if (stage >= 4) {
                line++
                line(player, "I picked some fresh Ardrigal for Trufitus.", line++, true)
                line++
                line(player, "I need to give the !!Ardrigal?? to !!Trufitus??.", line++, false)
            } else if (stage >= 3) {
                line++
                line(player, "I've given Snakeweed to Trufitus.", line++, true)
                line++
                line(player, "I need to pick some fresh !!Ardrigal?? for !!Trufitus??.", line++, false)
            } else if (stage >= 2) {
                line++
                line(player, "I picked some fresh Snakeweed for Trufitus.", line++, true)
                line++
                line(player, "I need to give the !!Snake Weed?? to !!Trufitus??.", line++, false)
            } else if (stage >= 1) {
                line++
                line(player, "I need to pick some fresh !!Snakeweed?? for !!Trufitus??.", line++, false)
            }
        } else {
            line(player, "Trufitus Shakaya of the Tai Bwo Wannai village needed", line++, false)
            line(player, "some jungle herbs in order to make a potion which would", line++, false)
            line(player, "help him commune with the gods. I collected five lots", line++, false)
            line(player, "of jungle herbs for him and he was able to", line++, false)
            line(player, "commune with the gods.", line++, false)
            line++
            line(player, "As a reward he showed me some herblore techniques.", line++, false)
            line++
            line(player, "<col=FF0000>QUEST COMPLETE!</col>", line++, false)
        }
    }

    // this runs whenever setqueststage runs, and also runs on login when the quest tab gets synced
    override fun updateVarps(player: Player) {

        // quest stages line up with the varp
        if (getQuestStage(player, Quests.JUNGLE_POTION) >= 12) {
            setVarp(player, JPOT_STAGE_VARP, 12, true) // except for stage 100, which is varp set to 12
        } else {
            setVarp(player, JPOT_STAGE_VARP, getQuestStage(player, Quests.JUNGLE_POTION), true)
        }

        // this is 010 010 010 010 010 010 010 010 010 010 in binary. each of the 10 villager varbits is set to 2 for them to appear.
        if (isCompleted(player)) setVarbit(player, TBW_VILLAGERS_VB, 306783378, true)
    }

    // use ::setqueststage 81 0 to reset quest
    override fun reset(player: Player) {
        setVarbit(player, TBW_VILLAGERS_VB, 0, true)
        player.questRepository.syncronizeTab(player)
    }

    override fun finish(player: Player) {
        var ln = 10
        super.finish(player)
        player.packetDispatch.sendString("You have completed the Jungle Potion Quest!", 277, 4)
        player.packetDispatch.sendItemZoomOnInterface(Items.CLEAN_VOLENCIA_MOSS_1532,230,277,5)

        drawReward(player, "1 Quest Point", ln++)
        drawReward(player, "775 Herblore XP", ln++)

        rewardXP(player, Skills.HERBLORE, 775.0)
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }
}