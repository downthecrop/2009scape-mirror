package content.region.morytania.quest.hauntedmine

import content.data.Quests
import content.region.morytania.quest.lairoftarn.LairOfTarn
import core.api.*
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.world.map.path.ClipMaskSupplier
import core.plugin.Initializable
import org.rs09.consts.Items

/**
 * Haunted Mine Quest
 */

/*Some of the  sources used:
 * Primary Source (March 2008): https://www.youtube.com/watch?v=g3F4tC1KOck
 * Secondary source (May 2009): https://www.youtube.com/watch?v=CD77NeKz1J4
 * Secondary source (Feb 2009): https://www.youtube.com/watch?v=PMn0LRo4MCo
 * https://web.archive.org/web/20081216051045/http://runescape.wikia.com/wiki/Haunted_Mine
 */

@Initializable
class HauntedMine : Quest(Quests.HAUNTED_MINE, 73, 72, 2, HAUNTED_MINE_VARP, 0, 1, 11) {

    companion object {
        // main quest varp
        const val HAUNTED_MINE_VARP = 382

        // points settings varp and varbits
        //const val POINTS_SETTINGS_VARP = 383
        const val LEVER_A_VARBIT = 2386
        const val LEVER_B_VARBIT = 2385
        const val LEVER_C_VARBIT = 2387
        const val LEVER_D_VARBIT = 2388
        const val LEVER_E_VARBIT = 2389
        const val LEVER_I_VARBIT = 2390
        const val LEVER_J_VARBIT = 2391
        const val LEVER_K_VARBIT = 2392

        const val POINTS_SETTINGS_IFACE = 144
        const val LIFT_MACHINERY_VARBIT = 2060

        const val ATTR_KEEP_FUNGUS = "hauntedmine-keepfungus"                      // keeps the fungus on you during the cutscene.
        const val ATTR_VALVE_UNLOCKED = "/save:quest:hauntedmine-valveunlocked"    // True after you use Zealot's Key with the water valve
        const val ATTR_FUNGUS_PLACED = "/save:quest:hauntedmine-fungusplaced"      // True after you put a fungus in the minecart
        const val ATTR_CART_SENT_SUCCESS = "/save:quest:hauntedmine-cartsent"      // True after you solve the cart puzzle and have a fungus in cart
        const val ATTR_KEY_MENTIONED = "/save:quest:hauntedmine-keymentioned"      // True after the player asks about a way into the mines.
        const val ATTR_KEY_RETURNED = "/save:quest:hauntedmine-zealotkeyreturned"  // True after you return the zealot's key after the quest. Note that this should not reset after quest completion
        const val ATTR_DAYTH_FIGHT = "dayth_active"                                // True during the boss fight and removed on Treus Dayth's death.
        const val ATTR_HAUNTED_TARGET = "target"                                   // Treus Dayth's target player

    }

    // Quest Journal ref: https://youtu.be/CD77NeKz1J4?si=AxNEg5YZU5oJ3T11&t=231
    // Contains pre-starting journal entry: https://www.youtube.com/watch?v=PMn0LRo4MCo
    // The video fragment matches the current RS3 journal: https://runescape.wiki/w/Transcript:Haunted_Mine/Journal
    override fun drawJournal(player: Player, stage: Int) {
        super.drawJournal(player, stage)
        var line = 12
        val stage = getQuestStage(player, Quests.HAUNTED_MINE)

        if (stage == 0) {
            line(player, "I can start this quest by speaking to the !!Saradominist??", line++, false)
            line(player, "!!Zealot?? outside the !!mines of Morytania??.", line++, false)
            line(player, "To complete this quest I need:", line++, false)
            line(player, "Level 15 Agility.", line++, hasLevelStat(player, Skills.AGILITY, 15))    // to go over a minecart at the entrance to the mine
            line(player, "Level 35 Crafting.", line++, hasLevelStat(player, Skills.CRAFTING, 35))  // to cut a salve shard from the crystal outcrop
            line(player, "Access to Morytania.", line++, isQuestComplete(player, Quests.PRIEST_IN_PERIL)) // to be able to access Morytania
            line(player, "I must be able to !!defeat a level 95 enemy.??", line++, false)                 // git gud
            limitScrolling(player, line, true)
        }
        if (stage >= 1) { // set stage 1 after talking to Zealot.
            line(player, "I met a fanatic Saradominist just outside the Morytanian", line++, stage > 1)
            line(player, "mines. He told me that the mines had been sealed, but", line++, stage > 1)
            line(player, "contained within them !!crystals?? of a great power over evil.", line++, stage > 1)
        }
        if (stage >= 2) { // set stage 2 after first entering the mine.
            line(player, "I managed to get into the mines via a thin tunnel high up in", line++, stage > 2)
            line(player, "the mountains.", line++, stage > 2)
        }
        if (stage >= 3) { // set stage 3 after entering level 4 (fungus and cart level)
            line(player, "Now I must reach the !!lower levels?? where apparently the", line++, stage > 3)
            line(player, "!!crystals?? can be found.", line++, stage > 3)
        }
        if (stage >= 4) { // set stage 4 after going down the lift to level 5
            line(player, "I managed to start the !!lift?? which allowed me to get to", line++, stage > 4)
            line(player, "the !!lower levels??.", line++, stage > 4)
            line(player, "Now I can continue my search for the !!crystals??.", line++, stage > 4)
        }
        if (stage >= 5) { // set stage 5 if you go down to level 6 without a fungus.
            line(player, "The !!lower levels?? of the mine were too !!dark?? to navigate.", line++, stage > 5)
        }
        if (stage >= 6) { // set stage 6 if you go down to level 6 with a fungus.
            line(player, "I had to use a !!glowing fungus?? from the upper levels", line++, stage > 6)
            line(player, "to !!Light?? the way.", line++, stage > 6)
        }
        if (stage >= 7) { // set stage 7 after you've killed dayth.
            line(player, "I found a !!key?? in the !!lower levels??. I had to fight a", line++, stage > 7)
            line(player, "powerful !!ghost?? who tried to prevent me taking it.", line++, stage > 7)
        }
        if (stage >= 8) { // set stage 8 after you cut a shard.
            line(player, "I used this !!key?? to reach the legendary !!crystals??, from", line++, stage > 8)
            line(player, "which I cut a !!salve shard?? to aid in fighting the undead.", line++, stage > 8)
        }
        if (stage >= 100) { // complete quest after you've cut a shard.
            line++
            line(player, "<col=FF0000>QUEST COMPLETE!</col>", line++, false)
        }
        limitScrolling(player, line, false)
    }

    // ::setqueststage 73 0 to reset quest
    override fun reset(player: Player) {
        removeAttribute(player, ATTR_FUNGUS_PLACED)
        removeAttribute(player, ATTR_CART_SENT_SUCCESS)
        removeAttribute(player, ATTR_KEY_MENTIONED)

        setVarbit(player, HAUNTED_MINE_VARP, 0, true) // resets the quest and closes tarn's door.

        // reset tarn, too
        removeAttribute(player, LairOfTarn.ATTR_KILLED_TARN)
    }

    override fun setStage(player: Player, stage: Int) {
        super.setStage(player, stage)
        this.updateVarps(player)
    }

    override fun updateVarps(player: Player) {

        // Stage Varbit values
        // These values were taken from: https://chisel.weirdgloop.org/varbs/display?varplayer=382
        if (getQuestStage(player, Quests.HAUNTED_MINE) == 0) {
            setVarbit(player, HAUNTED_MINE_VARP, 0, true) // marks quest as red.
        }
        if (getQuestStage(player, Quests.HAUNTED_MINE) <= 1) {
            setVarbit(player, HAUNTED_MINE_VARP, 1, true) // marks quest as yellow. true after talking to zealot
        }
        if (getQuestStage(player, Quests.HAUNTED_MINE) <= 2) {
            setVarbit(player, HAUNTED_MINE_VARP, 3, true) // true after entering mine
        }
        if (getQuestStage(player, Quests.HAUNTED_MINE) <= 4) {
            setVarbit(player, HAUNTED_MINE_VARP, 4, true) // true after opening the water valve
        }
        if (getQuestStage(player, Quests.HAUNTED_MINE) <= 5) {
            setVarbit(player, HAUNTED_MINE_VARP, 6, true) // true after going into the dark fucking cave
        }
        if (getQuestStage(player, Quests.HAUNTED_MINE) <= 6) {
            setVarbit(player, HAUNTED_MINE_VARP, 7, true) // true after getting to level 6
        }
        if (getQuestStage(player, Quests.HAUNTED_MINE) <= 7) {
            setVarbit(player, HAUNTED_MINE_VARP, 8, true) // true after spawning dayth
        }
        if (getQuestStage(player, Quests.HAUNTED_MINE) >= 100) {
            setVarbit(player, HAUNTED_MINE_VARP, 11, true) // marks quest as green and opens tarn's door. true after cutting the crystal
        }
    }

    override fun finish(player: Player) {
        var ln = 10
        super.finish(player)
        player.packetDispatch.sendString("You have completed the Haunted Mine Quest!", 277, 4)
        player.packetDispatch.sendItemZoomOnInterface(Items.SALVE_AMULET_4081, 230, 277, 5)

        drawReward(player, "2 quest points", ln++)
        drawReward(player, "22,000 Strength XP", ln++)
        drawReward(player, "Access to Tarn's Lair", ln++)

        rewardXP(player, Skills.STRENGTH, 22000.0)
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }
}

// this is a clipping mask I wrote to give NPCs the ability to path through walls. use with caution or they will wander off.
object GhostClipper : ClipMaskSupplier {
    override fun getClippingFlag(z: Int, x: Int, y: Int): Int {
        return 0
    }
}
