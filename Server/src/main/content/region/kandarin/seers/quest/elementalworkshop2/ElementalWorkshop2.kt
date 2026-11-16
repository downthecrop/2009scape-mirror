package content.region.kandarin.seers.quest.elementalworkshop2

import content.data.Quests
import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs09.consts.Items

/**
 * ELEMENTAL WORKSHOP II QUEST
 *
 * Requirements: 20 Magic, 30 Smithing, completion of Elemental Workshop I
 * Primary Sources: https://www.youtube.com/watch?v=YKSWisRgGZk, https://www.youtube.com/watch?v=hv2rCofydXo
 * Wiki: https://runescape.wiki/w/Elemental_Workshop_II?oldid=849620
 */

@Initializable
class ElementalWorkshop2 : Quest(Quests.ELEMENTAL_WORKSHOP_II, 53, 52, 1, EW2_STAGE, 0, 1, 11) {

    // a truckload of varbits - https://chisel.weirdgloop.org/varbs/display?varplayer=896
    companion object {
        const val EW2_VARP1 = 896           // first EW2 varp that packages varbits 2639-2652
        const val EW2_STAGE = 2639          // Main quest progress. Incremented with quest stages.
        const val EW2_MACHINERY = 2640      // The machinery you have to search for the key. Set to 1 after you read the scroll.
        const val EW2_HATCH = 2641          // The hatch down to EW2. Set to 1 after you unlock it with the key.
        const val EW2_CART_LEVER = 2642     // cart moving lever. 0 = start (south), 1 = west, 2 = north, 3 = east
        const val EW2_JIG_CART = 2643       // Jig Cart state. 0 = Empty, 1 = ele bar, 2 = hot bar, 3 = smashed hot, 4 = smashed cooled, 5 = primed
        const val EW2_CRANE_STATE = 2644    // Crane State. 0 = broken, 1 = fixed/empty, 2 = ele bar, 3 = hot bar.
        const val EW2_CRANE_POSN = 2645     // Crane Position. 0 = South Down, 1 = South Up, 2 = North Down, 3 = North Up.
        const val EW2_JCTBOX1 = 2646        // junction box status. see junctionboxinterface for details.
        const val EW2_JCTBOX2 = 2647        // junction box status. see junctionboxinterface for details.
        const val EW2_JCTBOX3 = 2648        // junction box status. see junctionboxinterface for details.
        //const val EW2_JCTBOX_ALL = 2649   // junction box - this overlaps 2646, 2647, 2648. could be used to reset whole jct box at once.
        const val EW2_PIPE = 2650           // pipe varbit. 0 = broken, 1 = fixed
        const val EW2_WEST_VALVE = 2651     // water valve west. 0 = closed, 1 = open
        const val EW2_EAST_VALVE = 2652     // water valve east. 0 = closed, 1 = open

        const val EW2_VARP2 = 897           // second EW2 varp that packages varbits 2653-2664
        const val EW2_TANK_RACK = 2653      // tank door holding state, controlled by corkscrew lever. 0 = closed, 1 = door opened grabber inside, 2 = door opened grabber out, 3 = door closed hot bar inside, 4 = door opened hot bar inside, 5 = door opened hot bar out, 6 = door closed cooled bar inside, 7 = door opened cooled bar inside, 8 = door closed cooled bar out
        const val EW2_TANK_DOOR = 2654      // door lever for tank. 0 = closed, 1 = open
        const val EW2_COG_PIN1 = 2655       // cog pin. 0 = no cog, 1 = small, 2 = med, 3 = large
        const val EW2_COG_PIN2 = 2656       // cog pin. 0 = no cog, 1 = small, 2 = med, 3 = large
        const val EW2_COG_PIN3 = 2657       // cog pin. 0 = no cog, 1 = small, 2 = med, 3 = large
        const val EW2_ALL_COGS = 2658       // overlaps the cog varbits.
        const val EW2_FAN_LEVER = 2659      // fan switch. 0 = fan off, 1 = fan on
        const val EW2_FAN_STATE = 2660      // fan blades. 0 = off, 1 = forward, 2 = reverse
        //const val EW2_FAN_STATION = 2661  // overlaps cogs, fan switch, and fan blades varbits
        const val EW2_EXTRACTOR_GUN = 2662  // extractor gun. 0 = empty, 1 = primed bar, 2 = mind bar
        //const val EW2_2663 = 2663         // no idea what this one does. it's in the place of the hammer lever?
        //const val EW2_2664 = 2664         // no idea what this one does. values from 1 to 7 back at the dig site library where you find the book.

        const val ATTR_JIGGED = "/save:quest:ew2-jigged"       // true after you put a bar on the jig cart
        const val ATTR_DIPPED = "/save:quest:ew2-dipped"       // true after dip the bar in the lava
        const val ATTR_FLATTENED = "/save:quest:ew2-flattened" // true after you flatten the hot bar
        const val ATTR_WATERED = "/save:quest:ew2-watered"     // true after you cool the bar in the tank
        const val ATTR_WINDED = "/save:quest:ew2-winded"       // true after you cool the bar in the wind tunnel
    }

    /**
     * Quest Stages and Varbit values:
     * https://chisel.weirdgloop.org/varbs/display?varbit=2639
     *
     * val  stage   desc.
     * 0    0       unstarted
     * 1    1       found the beaten book
     * 2    2       read the beaten book
     * 3    3       read the beaten book's scroll
     * 4    4       found key
     * 5    5       went down center hatch. need to fix equipment
     * 6    6       inside the workshop. processing bar
     * 7    7       take primed bar
     * 8    8       went through the mind door
     * 9    9       placed a primed bar
     * 10   10      sat in the extractor
     * 11   100     smithed a mind helmet, quest complete
     */
    override fun drawJournal(player: Player, stage: Int) {

        // Quest journal source: https://www.youtube.com/watch?v=hv2rCofydXo

        super.drawJournal(player, stage)
        var line = 12
        val stage = getStage(player)

        // unstarted
        if (stage == 0) {
            line(player, "I can start this quest by reading a", line++)
            line(player, "!!book?? found at the !!Dig Site??.", line++)
            line++
            line(player,"To start this quest I will need:", line++)
            line(player, "Level 20 Magic", line++, hasLevelStat(player, Skills.MAGIC, 20))
            line(player, "Level 30 Smithing", line++, hasLevelStat(player, Skills.SMITHING, 30))
            line(player,"I must have completed the following quests:", line++) // After - I have completed all of the required quests:
            line(player, "Elemental Workshop I", line++, isQuestComplete(player, Quests.ELEMENTAL_WORKSHOP_I))
        }

        // stage 1, found the beaten book
        if (stage >= 1) {
            line(player, "I have found a book at the Dig Site.", line++, true)
            // in-progress stage text
            if(stage == 1) line(player, "I should read the book.", line++)
            line++
        }

        // stage 2, read the beaten book
        if (stage >= 2) {
            line(player, "The book tells of a way to !!make elemental items??", line++, true)
            line(player, "even more !!powerful?? than before.", line++, true)
            // in-progress stage text
            if(stage == 2) line(player, "I should read the scroll.", line++)
            line++
        }

        // stage 3, read the scroll from the beaten book
        if (stage >= 3) {
            line(player, "The note found with the book has a !!strange code?? written on it.", line++, stage > 3)
            // in-progress stage text
            if(stage == 3) line(player, "I wonder what it means.", line++)
            line++
        }

        // stage 4, finding the key
        if (stage >= 4) {
            line(player, "By following the code on the !!note?? I was able to !!find a key??.", line++, stage > 4)
            // in-progress stage text
            if(stage == 4) line(player, "Now I just have to find what it opens.", line++)
            line++
        }

        // stage 5, going down the center hatch and getting the machines fixed
        if (stage >= 5) {
            line(player, "Climbing down through the hatch I found a new area in the", line++, (getVarbit(player, EW2_CRANE_STATE) != 0 && JunctionBoxInterface.isSolutionCorrect(player) && getVarbit(player, EW2_PIPE) != 0 && getVarbit(player, EW2_ALL_COGS) == 57))
            line(player, "workshop; there are 4 machines on this level to !!fix??:", line++, (getVarbit(player, EW2_CRANE_STATE) != 0 && JunctionBoxInterface.isSolutionCorrect(player) && getVarbit(player, EW2_PIPE) != 0 && getVarbit(player, EW2_ALL_COGS) == 57))

            if(getVarbit(player, EW2_CRANE_STATE) == 0) line(player, "The crane - this looks in a state of disrepair.", line++)
            // "The crane - this looks in a state of disrepair, but I have \n created a replacement claw for it."
            if(getVarbit(player, EW2_CRANE_STATE) != 0) line(player, "The crane - now the claw has been replaced the crane", line++, true)
            if(getVarbit(player, EW2_CRANE_STATE) != 0) line(player, "looks like it's in good working order.", line++, true)
            line++

            if(!JunctionBoxInterface.isSolutionCorrect(player))line(player, "The press - I should see if this needs fixing.", line++)
            // "The press - I should try to fit all the pipes in the right order."
            if(JunctionBoxInterface.isSolutionCorrect(player)) line(player, "The press - the pipes have now been fitted and ", line++, true)
            if(JunctionBoxInterface.isSolutionCorrect(player)) line(player, "it should work as long as it's powered.", line++, true)
            line++

            if(getVarbit(player, EW2_PIPE) == 0) line(player, "The tank - I'll need to fix this before I can use it.", line++)
            if(getVarbit(player, EW2_PIPE) != 0) line(player, "The tank - this should work fine now I have replaced the pipe.", line++, true)
            line++

            if(getVarbit(player, EW2_ALL_COGS) != 57) line(player, "A wind tunnel - it looks like a few bits are missing from it.", line++)
            if(getVarbit(player, EW2_ALL_COGS) == 57) line(player, "A wind tunnel - I have fitted all the cogs on to the pins.", line++, true)
            if(getVarbit(player, EW2_ALL_COGS) == 57) line(player, "The wind tunnel should now work as long as it's powered.", line++, true)
            line++
        }

        // stage 6, inside the workshop. put bar on cart
        if (stage >= 6 && getAttribute(player, ATTR_JIGGED, false)) {
            line(player, "I have found a track-mounted jig", line++, stage > 6)
            line(player, "on which I was able to clamp an elemental bar.", line++, stage > 6)
            line++
        }

        // stage 6, inside the workshop. dipped
        if (stage >= 6 && getAttribute(player, ATTR_DIPPED, false)) {
            line(player, "I was able to operate the crane and dip the elemental bar", line++, stage > 6)
            line(player, "into the lava. The bar is now very hot.", line++, stage > 6)
            line++
        }

        // stage 6, inside the workshop. flattened bar
        if (stage >= 6 && getAttribute(player, ATTR_FLATTENED, false)) {
            line(player, "I used the press to flatten the hot elemental bar.", line++, stage > 6)
            line++
        }

        // stage 6, inside the workshop. water tanked bar
        if (stage >= 6 && getAttribute(player, ATTR_WATERED, false)) {
            line(player, "The water tank was used to cool the elemental bar down.", line++, stage > 6)
            line++
        }

        // stage 6, inside the workshop. wind tunneled bar
        if (stage >= 6 && getAttribute(player, ATTR_WINDED, false)) {
            line(player, "The wind tunnel proved a great way to dry off the wet bar.", line++, stage > 6)
            line++
        }

        // stage 7, retrieve primed bar
        if (stage >= 7) {
            line(player, "I have primed a bar of elemental ore, I wonder what to do", line++, stage > 7)
            line(player, "with it.", line++, stage > 7)
            line++
        }

        // stage 8, went through the mind door. I skip using this due to lack of source
        /*
        if (stage >= 8) {
            line(player, "", line++, stage > 8)
            line++
        }
        */

        // stage 9, placed a primed bar
        if (stage >= 9) {
            line(player, "I placed the primed bar on the device", line++, stage > 9)
            line(player, "found in the room with the mind symbols on the door.", line++, stage > 9)
            line++
        }

        // stage 10, sat in the extractor
        if (stage >= 10) {
            line(player, "Operating the device charged the", line++, stage > 10)
            line(player, "primed bar with some of my own mind power.", line++, stage > 10)
            line++
            line(player, "I have made an elemental mind bar,", line++, stage > 10)
            line(player, "now I just have to make one of the helms from it.", line++, stage > 10)
            line++
        }

        // stage 11, smithed a mind helmet, quest complete
        if (stage >= 11) {
            line(player, "Creating the elemental mind helm", line++, stage > 11)
            line(player, "was easy with the book for instruction.", line++, stage > 11)
        }

        // quest complete
        if (stage >= 100) {
            line++
            line(player,"<col=FF0000>QUEST COMPLETE!</col>", line)
        }
    }

    // use ::setqueststage 53 0 to reset quest.
    override fun reset(player: Player) {
        // these varps wipes all the varbits
        setVarp(player, EW2_VARP1, 0, true)
        setVarp(player, EW2_VARP2, 0, true)

        // remove attributes
        removeAttribute(player, ATTR_JIGGED)
        removeAttribute(player, ATTR_DIPPED)
        removeAttribute(player, ATTR_FLATTENED)
        removeAttribute(player, ATTR_WATERED)
        removeAttribute(player, ATTR_WINDED)

        // sync the quest tab
        player.getQuestRepository().syncronizeTab(player)
        super.reset(player)
    }

    override fun setStage(player: Player, stage: Int) {
        super.setStage(player, stage)
        this.updateVarps(player)
    }

    override fun updateVarps(player: Player) {
        // The quest stages are perfectly aligned with the varp
        if (getQuestStage(player, Quests.ELEMENTAL_WORKSHOP_II) >= 11) {
            setVarbit(player, EW2_STAGE, 11, true) // except for stage 100 which is varp set to 11.
        } else {
            setVarbit(player, EW2_STAGE, getQuestStage(player, Quests.ELEMENTAL_WORKSHOP_II), true)
        }
    }

    override fun finish(player: Player) {
        var ln = 10
        super.finish(player)
        player.packetDispatch.sendString("You have completed the Elemental Workshop II Quest!", 277, 4)
        player.packetDispatch.sendItemZoomOnInterface(Items.MIND_HELMET_9733, 230, 277, 5)

        drawReward(player, "1 Quest Point", ln++)
        drawReward(player, "7500 Smithing XP,", ln++)
        drawReward(player, "7500 Crafting XP,", ln++)
        drawReward(player, "Ability to make and equip", ln++)
        drawReward(player, "elemental mind equipment", ln)

        rewardXP(player, Skills.SMITHING, 7500.0)
        rewardXP(player, Skills.CRAFTING, 7500.0)

        removeAttribute(player, ATTR_JIGGED)
        removeAttribute(player, ATTR_DIPPED)
        removeAttribute(player, ATTR_FLATTENED)
        removeAttribute(player, ATTR_WATERED)
        removeAttribute(player, ATTR_WINDED)
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }
}