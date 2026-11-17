package content.region.misthalin.varrock.quest.gardenoftranquility

import content.data.Quests
import content.global.skill.farming.FarmingPatch
import content.global.skill.farming.PatchType
import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs09.consts.Items

/**
 * Garden of Tranquility Quest
 * @author Ovenbread, Syndromeramo, Oftheshire
 * Extra credit to DeadlyGenga for confirming authentic statue behavior on RS3.
 */

@Initializable
class GardenOfTranquility : Quest(Quests.GARDEN_OF_TRANQUILITY, 66, 65, 2, GOT_VARP_1, VB_GOT_STAGE, 0, 1, 60) {

    companion object {
        /* Quest stages:
         * stage 10: start quest by talking to queen
         * stage 20: wise old man told you about charos, but you didn't have the ring (not implemented)
         * stage 30: wise old man quizzed you, but you failed (not implemented)
         * stage 40: received ring of charos (a) from wise old man. Player then proceeds to get and plant all the garden shit.
         * stage 50: talk to queen, all plants grown
         * stage 60: talk to queen again, finish quest
         */

        const val ATTR_STATUE_OWNER = "statueowner" // used when pushing the statues
        const val STATUE_LOGOUT = "got-statue"      // string for logout listener when moving statues

        // varps that hold all the varbits below
        const val GOT_VARP_1 = 553             // VARP 553: varbits 961-978
        const val GOT_VARP_2 = 554             // VARP 554: varbits 979-988

        // quest stage varbit, see above for stages
        const val VB_GOT_STAGE = 961           // main quest stage varbit

        // statues
        const val VB_GUARD_CUTSCENE = 962      // 1 if triggered
        const val VB_SARA_STATUE = 963         // 0 statue in fally, 1 statue gone, 2 statue in garden
        const val VB_KING_STATUE = 964         // 0 statue in lumby, 1 statue gone, 2 statue in garden
        const val VB_RING_WELL = 966           // 0 if not thrown in well, 1 if thrown in well (irretrievable except via fishing out)

        // Elstan, Falador. Plant and harvest marigolds
        const val VB_ELSTAN_STAGE = 967        // 0 before agreeing, 1 after agreeing, 2 after planting, 3 after harvesting marigolds, 4 after receiving seeds

        // Lyra, Morytania. Plant and grow onions
        const val VB_LYRA_STAGE = 968          // 0 before agreeing, 1 after agreeing, 2 after planting and before talking, 3 after receiving seeds
        const val VB_LYRA_NORTH = 969          // 1 if planted in NW after agreement
        const val VB_LYRA_SOUTH = 970          // 1 if planted in SE after agreement

        // Kragen, Ardougne. Plant and grow cabbages
        const val VB_KRAGEN_STAGE = 971        // 0 before agreeing, 1 after agreeing, 2 after planting and before talking, 3 after receiving seeds
        const val VB_KRAGEN_NORTH = 974        // 1 if planted in N after agreement
        const val VB_KRAGEN_SOUTH = 975        // 1 if planted in S after agreement

        // Bernald, Burthorpe. Cure his sick vines
        const val VB_VINE_SEEDS = 972          // 1 after getting vine seeds
        const val VB_ALAIN = 978               // 1 if you've talked to Alain about strong plant cure
        const val VB_BURTHORPE_VINES = 988     // 0 diseased, 1 after talking with Bernald, 2 after trying plant cure, 3 after talking with Bernald again, 4 cured

        // Dantaera, Catherby. Get a cutting of the White Tree of definitely-not Gondor
        const val VB_DANTAERA_STAGE = 976      // 0 before agreeing, 1 after agreeing, 2 after tree is cut for first time

        // Brother Althric, roses, makes you destroy the definitely-not One Ring
        const val VB_BRO_ALTHRIC = 977         // 0 before Charos attempt, 1 after first conversation, 2 after second conversation

        // quest patch varbits
        const val VB_RED_ROSE_PATCH = 979      // 0/1/2 weedy, 3 clear, 4 planted, 5/6 growing, 7 grown
        const val VB_WHITE_ROSE_PATCH = 980    // 0/1/2 weedy, 3 clear, 4 planted, 5/6 growing, 7 grown
        const val VB_PINK_ROSE_PATCH = 981     // 0/1/2 weedy, 3 clear, 4 planted, 5/6 growing, 7 grown
        const val VB_DELPHINIUM_PATCH = 982    // 0/1/2 weedy, 3 clear, 4 planted, 5/6 growing, 7 grown
        const val VB_SNOWDROP_PATCH = 983      // 0/1/2 weedy, 3 clear, 4 planted, 5/6 growing, 7 grown
        const val VB_YELLOW_ORCHID_PATCH = 986 // 0/1/2 weedy, 3 clear, 4 planted, 5/6 growing, 7 grown
        const val VB_WHITE_TREE_PATCH = 985    // 0/1/2 weedy, 3 clear, 4 planted, 8 grown, 9 fruited
        const val VB_PINK_ORCHID_PATCH = 984   // 0/1/2 weedy, 3 clear, 4 planted, 5/6 growing, 7 grown
        const val VB_VINE_PATCH = 987          // 0/1/2 weedy, 3 clear, 4 planted, 5/6 growing, 7 grown

        //const val varbit973 = 973 // unused varbit, I don't know what it's used for, if anything.

        // defines for the patches in the eponymous garden of tranquility
        val questPatches = arrayListOf(
            FarmingPatch.GOT_VINE,
            FarmingPatch.GOT_DELPHINIUM,
            FarmingPatch.GOT_SNOWDROP,
            FarmingPatch.GOT_WHITE_TREE,
            FarmingPatch.GOT_YELLOW_ORCHID,
            FarmingPatch.GOT_PINK_ORCHID,
            FarmingPatch.GOT_RED_ROSE,
            FarmingPatch.GOT_WHITE_ROSE,
            FarmingPatch.GOT_PINK_ROSE
        )

        val questPatchTypes = arrayListOf(
            PatchType.DELPHINIUM_PATCH,
            PatchType.PINK_ORCHID_PATCH,
            PatchType.PINK_ROSE_PATCH,
            PatchType.RED_ROSE_PATCH,
            PatchType.SNOWDROP_PATCH,
            PatchType.WHITE_ROSE_PATCH,
            PatchType.WHITE_TREE_PATCH,
            PatchType.YELLOW_ORCHID_PATCH,
            PatchType.VINE_PATCH
        )

        fun hasRequirements(player: Player): Boolean {
            return arrayOf(
                hasLevelStat(player, Skills.FARMING, 25),
                isQuestComplete(player, Quests.CREATURE_OF_FENKENSTRAIN),
            ).all { it }
        }

        // The plantpots have different varbit states than the rest of the farming patches because fuck you.
        // 0 is empty patch and 1 is ready to plant. They cannot be weeded.
        fun isPlantPot(type: PatchType) = type == PatchType.PINK_ORCHID_PATCH || type == PatchType.YELLOW_ORCHID_PATCH
    }

    override fun drawJournal(player: Player, stage: Int) {
        super.drawJournal(player, stage)
        var line = 12
        val stage = getStage(player)

        val started = getQuestStage(player, Quests.GARDEN_OF_TRANQUILITY) > 0

        if (!started) {
            line(player, "To start this quest I need to speak to !!Queen Ellamaria?? in", line++)
            line(player, "the !!eastern garden of Varrock Palace??.", line++)
            line(player, "I need to complete the following quests:", line++)
            line(player, "!!Creature of Fenkenstrain??", line++, isQuestComplete(player, Quests.CREATURE_OF_FENKENSTRAIN))
            line(player, "Also, to complete this quest I need the following:", line++)
            line(player, "!!Level 25 Farming??", line++, getStatLevel(player, Skills.FARMING) >= 25)
        } else {
            line(player, "I spoke to Queen Ellamaria of Varrock Palace, and agreed", line++, true)
            line(player, "to grow her a garden to surprise her husband, King Roald.", line++, true)

            if (stage >= 40) {
                line(player, "The Wise Old Man of Draynor Village told me more about the Ring", line++, true)
                line(player, "of Charos, and unlocked its hidden powers. When worn, it", line++, true)
                line(player, "can be used to influence the weak-minded.", line++, true)
            } else if (stage >= 10) {
                line(player, "I need to speak to the !!wise old man?? who can be found in", line++, false)
                line(player, "!!Draynor Village?? - the Queen believes that he can help me", line++, false)
                line(player, "!!to learn about diplomacy.", line++, false)
            }

            if (stage >= 100) {
                // They disappear at the end
            } else if (stage >= 10) {
                // These are only crossed out when you plant/place them in the garden and the plants are fully grown.
                line(player, "I have so far grown these plants in the palace garden:", line++, false)
                line(player, "!!Delphiniums?? (see Elstan near Falador)", line++, getVarbit(player, VB_DELPHINIUM_PATCH) == 7)
                line(player, "!!Pink orchids?? (see Lyra near Port Phasmatys)", line++, getVarbit(player, VB_PINK_ORCHID_PATCH) == 7)
                line(player, "!!Yellow orchids?? (see Lyra near Port Phasmatys)", line++, getVarbit(player, VB_YELLOW_ORCHID_PATCH) == 7)
                line(player, "!!Snowdrops?? (see Kragen near Ardougne)", line++, getVarbit(player, VB_SNOWDROP_PATCH) == 7)
                line(player, "!!Burthorpe vine?? (see Bernald near Taverley)", line++, getVarbit(player, VB_VINE_PATCH) == 7)
                line(player, "!!White rosebush?? (get seeds from the Monastery)", line++, getVarbit(player, VB_WHITE_ROSE_PATCH) == 7)
                line(player, "!!Pink rosebush?? (get seeds from the Monastery)", line++, getVarbit(player, VB_PINK_ROSE_PATCH) == 7)
                line(player, "!!Red rosebush?? (get seeds from the Monastery)", line++, getVarbit(player, VB_RED_ROSE_PATCH) == 7)
                line(player, "!!A White Tree?? (see Dantaera near Catherby)", line++, getVarbit(player, VB_WHITE_TREE_PATCH) == 8)
                line(player, "!!A statue of Saradomin?? (from Falador)", line++, getVarbit(player, VB_SARA_STATUE) == 2)
                line(player, "!!A statue of a Misthalin King?? (from outside Lumbridge", line++, getVarbit(player, VB_KING_STATUE) == 2)
                line(player, "Castle)", line++, getVarbit(player, VB_KING_STATUE) == 2)
            }

            if (stage >= 100) {
                line(player, "I grew a garden for King Roald and then brought him to see it.", line++, true)
                line(player, "He seemed very pleased with it - well, at least, after", line++, true)
                line(player, "Queen Ellamaria had a little 'chat' with him.", line++, true)
                line++
                line(player, "<col=FF0000>QUEST COMPLETE!</col>", line)
            }
        }
    }

    override fun finish(player: Player) {
        var ln = 10
        super.finish(player)
        player.packetDispatch.sendString("You have completed Garden of Tranquility!", 277, 4)
        player.packetDispatch.sendItemZoomOnInterface(Items.COMPOST_POTION4_6470, 230, 277, 5)

        drawReward(player, "2 Quest Points", ln++)
        drawReward(player, "5000 Farming XP", ln++)
        drawReward(player, "1 apple tree seed, 1 acorn,", ln++)
        drawReward(player, "and 5 guam seeds", ln++)
        drawReward(player, "Supercompost Potion", ln++)

        rewardXP(player, Skills.FARMING, 5000.0)
        addItemOrDrop(player, Items.COMPOST_POTION4_6470, 1)
        addItemOrDrop(player, Items.APPLE_TREE_SEED_5283, 1)
        addItemOrDrop(player, Items.ACORN_5312, 1)
        addItemOrDrop(player, Items.GUAM_SEED_5291, 5)
    }

    // use ::setqueststage 66 0 to reset the quest
    // clears all the varbits by resetting the two varps that contain all the varbits
    override fun reset(player: Player) {
        setVarp(player, GOT_VARP_1, 0, true)
        setVarp(player, GOT_VARP_2, 0, true)
        player.getQuestRepository().syncronizeTab(player)
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }
}