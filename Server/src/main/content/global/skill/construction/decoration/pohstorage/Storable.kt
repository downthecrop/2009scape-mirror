package content.global.skill.construction.decoration.pohstorage

import content.data.Quests
import core.api.getItemName
import core.api.isQuestComplete
import core.game.node.entity.player.Player
import core.game.world.GameWorld
import org.rs09.consts.Items

/**
 * Represents a database of all the storable items in the POH Costume Room and Bookcase across all box types.
 */

/*
 * The below function should be copied into each book's listener to store the book in the POH. It is part of ContentAPI.
 * import core.api.*
 * storeBookInHouse(player, node)
 *
 * Example: src/main/content/region/kandarin/quest/observatoryquest/AstronomyBook.kt
 *
 * todo: Periodically, this storables list should be audited as new books are added to the game. Make sure the new books are going in the bookcase.
 */

// todo: Right now there is no logic to check:
//       1. for complete or incomplete sets
//       2. for single instances of an item
//       This allows things like storing multiple god capes, a Rune (g) set with both platelegs and plateskirt,
//       an elegant shirt/pants and blouse/skirt at once, and all 3 sets of castle wars armour at once. I don't
//       think any of this is game-breaking, so have chosen to omit this logic for now.

enum class Storable(
    val displayId: Int,           // display ID is the item that is shown in the UI
    val takeIds: IntArray,        // take IDs is the item(s) that are taken from the player's inventory
    val type: StorableFamily,     // the family (mapped to container) that the items get stored in
    val setName: String = getItemName(displayId), // the set/book name for each item. defaults to display item

    // special properties just for the books
    val bitIndex: Int = -1,      // This index is used to save the big list of books the player has read, instead of having an individual attribute per book.
    // todo make the quest requirement check part of a separate object against this list of books. only a subset of books are applicable.
    val questReq: Quests? = null // Once read, books should only show up in the POH after completing their attached quest. https://runescape.wiki/w/Bookcase?oldid=1650176
) {
    // Books - https://runescape.wiki/w/List_of_texts_and_tomes?oldid=848552
    AbyssalBook(Items.ABYSSAL_BOOK_5520, intArrayOf(Items.ABYSSAL_BOOK_5520), type = StorableFamily.BOOKCASE, "Abyssal Research Notes", bitIndex = 0), // abyss miniquest - IMPLEMENTED
    AncientBook(Items.ANCIENT_BOOK_7633, intArrayOf(Items.ANCIENT_BOOK_7633), type = StorableFamily.BOOKCASE, "The Sleeping Seven", bitIndex = 1, Quests.HORROR_FROM_THE_DEEP), // horror from the deep quest
    AncientDiary(Items.DIARY_3846, intArrayOf(Items.DIARY_3846), type = StorableFamily.BOOKCASE, "Ancient Diary", bitIndex = 2, Quests.HORROR_FROM_THE_DEEP), // horror from the deep quest
    ArenaBook(Items.ARENA_BOOK_6891, intArrayOf(Items.ARENA_BOOK_6891), type = StorableFamily.BOOKCASE, "Magic Training Arena Lore Book", bitIndex = 3), // mage training arena minigame
    AstronomyBook(Items.ASTRONOMY_BOOK_600, intArrayOf(Items.ASTRONOMY_BOOK_600), type = StorableFamily.BOOKCASE, "Astronomy Book", bitIndex = 4, Quests.OBSERVATORY_QUEST), // observatory quest - IMPLEMENTED
    BatteredBook(Items.BATTERED_BOOK_2886, intArrayOf(Items.BATTERED_BOOK_2886), type = StorableFamily.BOOKCASE, "Book of the Elemental Shield", bitIndex = 5, Quests.ELEMENTAL_WORKSHOP_I), // elemental workshop I quest - IMPLEMENTED
    BatteredTome(Items.BATTERED_TOME_7634, intArrayOf(Items.BATTERED_TOME_7634), type = StorableFamily.BOOKCASE, "Histories of Hallowland", bitIndex = 6, Quests.IN_AID_OF_THE_MYREQUE), // in aid of myreque quest
    BeatenBook(Items.BEATEN_BOOK_9717, intArrayOf(Items.BEATEN_BOOK_9717), type = StorableFamily.BOOKCASE, "Book of the Elemental Helm", bitIndex = 7, Quests.ELEMENTAL_WORKSHOP_II), // elemental workshop II quest
    BigBookOfBangs(Items.BIG_BOOK_OF_BANGS_3230, intArrayOf(Items.BIG_BOOK_OF_BANGS_3230), type = StorableFamily.BOOKCASE, "Big Book of Bangs", bitIndex = 8, Quests.REGICIDE), // regicide quest
    BindingBook(Items.BINDING_BOOK_730, intArrayOf(Items.BINDING_BOOK_730), type = StorableFamily.BOOKCASE, "Book of Binding", bitIndex = 9, Quests.LEGENDS_QUEST), // legends quest - IMPLEMENTED
    BirdBook(Items.BIRD_BOOK_10173, intArrayOf(Items.BIRD_BOOK_10173), type = StorableFamily.BOOKCASE, "William Oddity's Guide to the Avian", bitIndex = 10, Quests.EAGLES_PEAK), // eagles peak quest
    BookOnBaxtorian(Items.BOOK_ON_BAXTORIAN_292, intArrayOf(Items.BOOK_ON_BAXTORIAN_292), type = StorableFamily.BOOKCASE, "Book on Baxtorian", bitIndex = 11, Quests.WATERFALL_QUEST), // waterfall quest - IMPLEMENTED
    BookOnChemicals(Items.BOOK_ON_CHEMICALS_711, intArrayOf(Items.BOOK_ON_CHEMICALS_711), type = StorableFamily.BOOKCASE, "Volatile chemicals - Experimental Test Notes", bitIndex = 12, Quests.THE_DIG_SITE), // dig site quest - IMPLEMENTED
    BookOPiracy(Items.BOOK_O_PIRACY_7144, intArrayOf(Items.BOOK_O_PIRACY_7144), type = StorableFamily.BOOKCASE, "Book o' Piracy", bitIndex = 13, Quests.CABIN_FEVER), // cabin fever quest
    BrewinGuide(Items.BREWIN_GUIDE_8989, intArrayOf(Items.BREWIN_GUIDE_8989), type = StorableFamily.BOOKCASE, "Brewin' Guide", bitIndex = 14), // trouble brewing quest/minigame
    BurntDiary(Items.BURNT_DIARY_7965, intArrayOf(Items.BURNT_DIARY_7965), type = StorableFamily.BOOKCASE, "Burnt Diary", bitIndex = 15, Quests.ROYAL_TROUBLE), // royal trouble quest
    CadarnLineage(Items.CADARN_LINEAGE_4209, intArrayOf(Items.CADARN_LINEAGE_4209), type = StorableFamily.BOOKCASE, "Cadarn Lineage", bitIndex = 16, Quests.ROVING_ELVES), // roving elves quest
    ClockworkBook(Items.CLOCKWORK_BOOK_10594, intArrayOf(Items.CLOCKWORK_BOOK_10594), type = StorableFamily.BOOKCASE, "Clockwork Toys - A Clockwork Mechanism, Chapter 1.0", bitIndex = 17, Quests.COLD_WAR), // cold war quest
    CocktailGuide(Items.COCKTAIL_GUIDE_2023, intArrayOf(Items.COCKTAIL_GUIDE_2023), type = StorableFamily.BOOKCASE, "The Blurberry Cocktail Guide", bitIndex = 18), // gnome restaurant minigame
    ConstructionGuide(Items.CONSTRUCTION_GUIDE_8463, intArrayOf(Items.CONSTRUCTION_GUIDE_8463), type = StorableFamily.BOOKCASE, "Guide to Construction", bitIndex = 19), // after you buy a house from estate agent - IMPLEMENTED ish
    CrumblingTome(Items.CRUMBLING_TOME_4707, intArrayOf(Items.CRUMBLING_TOME_4707), type = StorableFamily.BOOKCASE, "Legend of the Brothers", bitIndex = 20), // barrows minigame
    CrystalSinging(Items.CRYSTAL_OF_SEREN_4313, intArrayOf(Items.CRYSTAL_OF_SEREN_4313), type = StorableFamily.BOOKCASE, "Crystal singing for beginners", bitIndex = 21, Quests.ROVING_ELVES), // roving elves quest
    DagonhaiHistory(Items.DAGONHAI_HISTORY_11001, intArrayOf(Items.DAGONHAI_HISTORY_11001), type = StorableFamily.BOOKCASE, "History of the Dagon'hai", bitIndex = 22), // found in varrock castle library
    DemonicTome(Items.DEMONIC_TOME_6749, intArrayOf(Items.DEMONIC_TOME_6749), type = StorableFamily.BOOKCASE, "The Confessions of Ellemar", bitIndex = 23, Quests.SHADOW_OF_THE_STORM), // shadow of the storm quest
    DiaryHerbi(Items.DIARY_3395, intArrayOf(Items.DIARY_3395), type = StorableFamily.BOOKCASE, "Diary of Herbi Flax", bitIndex = 24, Quests.SHADES_OF_MORTTON), // shades of mort'ton quest
    DrozalsJournal(Items.JOURNAL_6755, intArrayOf(Items.JOURNAL_6755), type = StorableFamily.BOOKCASE, "Drozal's Journal", bitIndex = 25, Quests.MAKING_HISTORY), // making history quest
    DwarvenLore(Items.DWARVEN_LORE_4568, intArrayOf(Items.DWARVEN_LORE_4568), type = StorableFamily.BOOKCASE, "The Arzinian Being of Bordanzan", bitIndex = 26, Quests.BETWEEN_A_ROCK), // between a rock quest
    EasternDiscovery(Items.EASTERN_DISCOVERY_6075, intArrayOf(Items.EASTERN_DISCOVERY_6075), type = StorableFamily.BOOKCASE, "The Exploration of the Eastern Realm", bitIndex = 27, Quests.MOURNINGS_END_PART_I), // mourning's end I quest
    EasternSettlement(Items.EASTERN_SETTLEMENT_6077, intArrayOf(Items.EASTERN_SETTLEMENT_6077), type = StorableFamily.BOOKCASE, "The Settlement of the East", bitIndex = 28, Quests.MOURNINGS_END_PART_I), // mourning's end I quest
    EdernsJournal(Items.EDERNS_JOURNAL_6649, intArrayOf(Items.EDERNS_JOURNAL_6649), type = StorableFamily.BOOKCASE, "Journal of Nissyen Edern", bitIndex = 29, Quests.MOURNINGS_END_PART_II), // mourning's end II quest
    ExplorersNotes(Items.EXPLORERS_NOTES_11677, intArrayOf(Items.EXPLORERS_NOTES_11677), type = StorableFamily.BOOKCASE, "Beyond Trollheim", bitIndex = 30), // found in keldagrim library
    EmbalmingManual(Items.EMBALMING_MANUAL_4686, intArrayOf(Items.EMBALMING_MANUAL_4686), type = StorableFamily.BOOKCASE, "The Little Book of Embalming", bitIndex = 31, Quests.ICTHLARINS_LITTLE_HELPER), // icthlarin's little helper quest
    FarmingManual(Items.FARMING_MANUAL_9903, intArrayOf(Items.FARMING_MANUAL_9903), type = StorableFamily.BOOKCASE, "Farmer Gricoller's Farming Manual", bitIndex = 32, Quests.MY_ARMS_BIG_ADVENTURE), // my arm's big adventure quest
    FeatheredJournal(Items.FEATHERED_JOURNAL_10179, intArrayOf(Items.FEATHERED_JOURNAL_10179), type = StorableFamily.BOOKCASE, "Feathered Journal of Arthur Artimus", bitIndex = 33, Quests.EAGLES_PEAK), // eagle's peak
    GameBook(Items.GAME_BOOK_7681, intArrayOf(Items.GAME_BOOK_7681), type = StorableFamily.BOOKCASE, "Party Pete's Bumper Book of Games", bitIndex = 34), // poh game room. I think the bookshelf is the only source for this
    GhrimsBook(Items.GHRIMS_BOOK_3901, intArrayOf(Items.GHRIMS_BOOK_3901), type = StorableFamily.BOOKCASE, "Managing Thine Kingdom for Noobes", bitIndex = 35, Quests.THRONE_OF_MISCELLANIA), // kingdom of misc quest
    GiannesCookBook(Items.GIANNES_COOK_BOOK_2167, intArrayOf(Items.GIANNES_COOK_BOOK_2167), type = StorableFamily.BOOKCASE, "Gianne's Cook Book", bitIndex = 36), // gnome retaurant minigame - IMPLEMENTED
    GlassblowingBook(Items.GLASSBLOWING_BOOK_11656, intArrayOf(Items.GLASSBLOWING_BOOK_11656), type = StorableFamily.BOOKCASE, "Glassblowing Book", bitIndex = 37), // found at the observatory
    GoblinBook(Items.GOBLIN_BOOK_10999, intArrayOf(Items.GOBLIN_BOOK_10999), type = StorableFamily.BOOKCASE, "The Book of the Big High War God", bitIndex = 38), // dropped by goblins - IMPLEMENTED
    GoblinSymbolBook(Items.GOBLIN_SYMBOL_BOOK_5009, intArrayOf(Items.GOBLIN_SYMBOL_BOOK_5009), type = StorableFamily.BOOKCASE, "A History of the Goblin Race", bitIndex = 39, Quests.THE_LOST_TRIBE), // lost tribe quest - IMPLEMENTED
    ElvenBook(Items.THE_GREAT_DIVIDE_6079, intArrayOf(Items.THE_GREAT_DIVIDE_6079), type = StorableFamily.BOOKCASE, "The Great Divide", bitIndex = 40, Quests.MOURNINGS_END_PART_I), // mourning's end I quest
    GuideBook(Items.GUIDE_BOOK_1856, intArrayOf(Items.GUIDE_BOOK_1856), type = StorableFamily.BOOKCASE, "Tourist Guide to Ardougne", bitIndex = 41), // found by estate agent in ardy - IMPLEMENTED
    AHandwrittenBook(Items.A_HANDWRITTEN_BOOK_9627, intArrayOf(Items.A_HANDWRITTEN_BOOK_9627), type = StorableFamily.BOOKCASE, "Crystal Singing for Beginners (trans. Oaknock) (Transcribed from the original Elvish by Oaknock the Engineer)", bitIndex = 42, Quests.THE_EYES_OF_GLOUPHRIE), // eyes of glouphrie quest
    HermansBook(Items.HERMANS_BOOK_7951, intArrayOf(Items.HERMANS_BOOK_7951), type = StorableFamily.BOOKCASE, "Dionysius: A Legend in His Own Lifetime", bitIndex = 43, Quests.SWAN_SONG), // swan song quest
    HistoryOfIban(Items.HISTORY_OF_IBAN_1494, intArrayOf(Items.HISTORY_OF_IBAN_1494), type = StorableFamily.BOOKCASE, "The Tale of Iban", bitIndex = 44, Quests.UNDERGROUND_PASS), // underground pass quest
    InstructionManual(Items.INSTRUCTION_MANUAL_5, intArrayOf(Items.INSTRUCTION_MANUAL_5), type = StorableFamily.BOOKCASE, "Dwarf Multicannon Manual", bitIndex = 45, Quests.DWARF_CANNON), // dwarf cannon quest - IMPLEMENTED
    JossiksJournal(Items.JOURNAL_3845, intArrayOf(Items.JOURNAL_3845), type = StorableFamily.BOOKCASE, "Jossik's Diary", bitIndex = 46, Quests.HORROR_FROM_THE_DEEP), // horror from the deep quest
    LeatherBook(Items.LEATHER_BOOK_7635, intArrayOf(Items.LEATHER_BOOK_7635), type = StorableFamily.BOOKCASE, "Modern Day Morytania", bitIndex = 47, Quests.IN_AID_OF_THE_MYREQUE), // in aid of the myreque quest
    Manual(Items.MANUAL_3847, intArrayOf(Items.MANUAL_3847), type = StorableFamily.BOOKCASE, "Lighthouse Manual", bitIndex = 48, Quests.HORROR_FROM_THE_DEEP), // horror from the deep quest
    MoonclanManual(Items.MOONCLAN_MANUAL_9078, intArrayOf(Items.MOONCLAN_MANUAL_9078), type = StorableFamily.BOOKCASE, "Basics of Magic", bitIndex = 49, Quests.LUNAR_DIPLOMACY), // lunar diplomacy quest
    MyNotes(Items.MY_NOTES_11339, intArrayOf(Items.MY_NOTES_11339), type = StorableFamily.BOOKCASE, "'My notes'", bitIndex = 50), // during barbarian training
    OldJournal(Items.OLD_JOURNAL_1493, intArrayOf(Items.OLD_JOURNAL_1493), type = StorableFamily.BOOKCASE, "The Journal of Randas", bitIndex = 51, Quests.UNDERGROUND_PASS), // underground pass quest
    OldTome(Items.OLD_TOME_13593, intArrayOf(Items.OLD_TOME_13593), type = StorableFamily.BOOKCASE, "Old Tome", bitIndex = 52, Quests.MEETING_HISTORY), // meeting history quest
    PrayerBook(Items.PRAYER_BOOK_10890, intArrayOf(Items.PRAYER_BOOK_10890), type = StorableFamily.BOOKCASE, "Prayer of Deliverance from Poisons", bitIndex = 53, Quests.THE_GREAT_BRAIN_ROBBERY), // the great brain robbery quest
    PieRecipeBook(Items.PIE_RECIPE_BOOK_7162, intArrayOf(Items.PIE_RECIPE_BOOK_7162), type = StorableFamily.BOOKCASE, "Pie Recipe Book", bitIndex = 54), // from romily weaklax
    PrifddinasHistory(Items.PRIFDDINAS_HISTORY_6073, intArrayOf(Items.PRIFDDINAS_HISTORY_6073), type = StorableFamily.BOOKCASE, "The Creation of Prifddinas", bitIndex = 55), // found in lletya bookshelves
    QueenHelpBook(Items.QUEEN_HELP_BOOK_10562, intArrayOf(Items.QUEEN_HELP_BOOK_10562), type = StorableFamily.BOOKCASE, "Queen Help Book", bitIndex = 56), // barbarian assault minigame
    RatPitsManual(Items.BOOK_6767, intArrayOf(Items.BOOK_6767), type = StorableFamily.BOOKCASE, "The RatPits Manual", bitIndex = 57, Quests.RATCATCHERS), // rat catchers quest
    ScabariteNotes(Items.SCABARITE_NOTES_11975, intArrayOf(Items.SCABARITE_NOTES_11975), type = StorableFamily.BOOKCASE, "My Notes, Archaeological exploration and the cult of Scabaras", bitIndex = 58, Quests.DEALING_WITH_SCABARAS), // dealing with scabaras quest
    SecurityBook(Items.SECURITY_BOOK_9003, intArrayOf(Items.SECURITY_BOOK_9003), type = StorableFamily.BOOKCASE, "" + GameWorld.settings!!.name + " Account Security", bitIndex = 59), // from stronghold of security - IMPLEMENTED
    ShamansTome(Items.SHAMANS_TOME_729, intArrayOf(Items.SHAMANS_TOME_729), type = StorableFamily.BOOKCASE, "Shaman's Tome", bitIndex = 60, Quests.LEGENDS_QUEST), // legends quest - IMPLEMENTED
    TheShieldOfArravBook(Items.BOOK_757, intArrayOf(Items.BOOK_757), type = StorableFamily.BOOKCASE, "The Shield of Arrav", bitIndex = 61, Quests.SHIELD_OF_ARRAV), // shield of arrav quest - IMPLEMENTED
    SinkethsDiary(Items.SINKETHS_DIARY_11002, intArrayOf(Items.SINKETHS_DIARY_11002), type = StorableFamily.BOOKCASE, "Sin'keth's Diary", bitIndex = 62, Quests.WHAT_LIES_BELOW), // what lies below quest - IMPLEMENTED
    StrongholdNotes(Items.STRONGHOLD_NOTES_9004, intArrayOf(Items.STRONGHOLD_NOTES_9004), type = StorableFamily.BOOKCASE, "Stronghold of Security - Notes", bitIndex = 63), // from stronghold of security - IMPLEMENTED
    TarnsDiary(Items.TARNS_DIARY_10587, intArrayOf(Items.TARNS_DIARY_10587), type = StorableFamily.BOOKCASE, "The Diary of Tarn Razorlor", bitIndex = 64), // lair of tarn miniquest
    Translation(Items.TRANSLATION_4655, intArrayOf(Items.TRANSLATION_4655), type = StorableFamily.BOOKCASE, "Translation Primer (Four Diamonds Translation)", bitIndex = 65, Quests.DESERT_TREASURE), // desert treasure quest - IMPLEMENTED
    TranslationBook(Items.TRANSLATION_BOOK_784, intArrayOf(Items.TRANSLATION_BOOK_784), type = StorableFamily.BOOKCASE, "Gnome-English Translation Dictionary", bitIndex = 66, Quests.THE_EYES_OF_GLOUPHRIE), // eyes of glouphrie quest - IMPLEMENTED
    TzhaarTouristGuide(Items.TZHAAR_TOURIST_GUIDE_13244, intArrayOf(Items.TZHAAR_TOURIST_GUIDE_13244), type = StorableFamily.BOOKCASE, "Tzhaar Tourist Guide", bitIndex = 67, Quests.TOKTZ_KET_DILL), // toktz-ket-dill quest
    VarmensNotes(Items.VARMENS_NOTES_4616, intArrayOf(Items.VARMENS_NOTES_4616), type = StorableFamily.BOOKCASE, "Notes from Varmen's Expedition to Uzer", bitIndex = 68, Quests.THE_GOLEM), // the golem quest - IMPLEMENTED
    WitchesDiary(Items.DIARY_2408, intArrayOf(Items.DIARY_2408), type = StorableFamily.BOOKCASE, "Witch's Diary", bitIndex = 69, Quests.WITCHS_HOUSE), // witch's house quest - IMPLEMENTED
    YewnocksNotes(Items.YEWNOCKS_NOTES_11750, intArrayOf(Items.YEWNOCKS_NOTES_11750), type = StorableFamily.BOOKCASE, "Yewnock's Notes on Crystals", bitIndex = 70, Quests.THE_PATH_OF_GLOUPHRIE), // path of glouphrie quest

    // Capes - https://runescape.wiki/w/Cape_rack?oldid=848849
    LegendsCape(Items.CAPE_OF_LEGENDS_1052, intArrayOf(Items.CAPE_OF_LEGENDS_1052), type = StorableFamily.CAPE_RACK, "Legends cape"),
    ObsidianCape(Items.OBSIDIAN_CAPE_6568, intArrayOf(Items.OBSIDIAN_CAPE_6568), type = StorableFamily.CAPE_RACK),
    FireCape(Items.FIRE_CAPE_6570, intArrayOf(Items.FIRE_CAPE_6570), type = StorableFamily.CAPE_RACK),
    TeamCape(Items.TEAM_1_CAPE_10638, (4315..4413 step 2).toList().toIntArray(),  type = StorableFamily.CAPE_RACK, "Wilderness cape"),
    GodCape(Items.GUTHIX_CAPE_2413, intArrayOf(Items.GUTHIX_CAPE_2413, Items.SARADOMIN_CAPE_2412, Items.ZAMORAK_CAPE_2414), type = StorableFamily.CAPE_RACK, "God cape"),
    AttackCape(Items.ATTACK_CAPE_10639, intArrayOf(Items.ATTACK_CAPE_9747, Items.ATTACK_HOOD_9749, Items.ATTACK_CAPET_9748), type = StorableFamily.CAPE_RACK_SKILL),
    DefenceCape(Items.DEFENCE_CAPE_10641, intArrayOf(Items.DEFENCE_CAPE_9753, Items.DEFENCE_CAPET_9754, Items.DEFENCE_HOOD_9755),  type = StorableFamily.CAPE_RACK_SKILL),
    StrengthCape(Items.STRENGTH_CAPE_10640, intArrayOf(Items.STRENGTH_CAPE_9750, Items.STRENGTH_CAPET_9751, Items.STRENGTH_HOOD_9752),  type = StorableFamily.CAPE_RACK_SKILL),
    HitpointsCape(Items.HITPOINTS_CAPE_10647, intArrayOf(Items.HITPOINTS_CAPE_9768, Items.HITPOINTS_CAPET_9769, Items.HITPOINTS_HOOD_9770),  type = StorableFamily.CAPE_RACK_SKILL),
    AgilityCape(Items.AGILITY_CAPE_10648, intArrayOf(Items.AGILITY_CAPE_9771, Items.AGILITY_CAPET_9772, Items.AGILITY_HOOD_9773),  type = StorableFamily.CAPE_RACK_SKILL),
    CookingCape(Items.COOKING_CAPE_10658, intArrayOf(Items.COOKING_CAPE_9801, Items.COOKING_CAPET_9802, Items.COOKING_HOOD_9803), type = StorableFamily.CAPE_RACK_SKILL),
    ConstructionCape(Items.CONSTRUCT_CAPE_10654, intArrayOf(Items.CONSTRUCT_CAPE_9789, Items.CONSTRUCT_CAPET_9790, Items.CONSTRUCT_HOOD_9791), type = StorableFamily.CAPE_RACK_SKILL, "Construction cape"),
    CraftingCape(Items.CRAFTING_CAPE_10651, intArrayOf(Items.CRAFTING_CAPE_9780, Items.CRAFTING_CAPET_9781, Items.CRAFTING_HOOD_9782),  type = StorableFamily.CAPE_RACK_SKILL),
    FarmingCape(Items.FARMING_CAPE_10661, intArrayOf(Items.FARMING_CAPE_9810, Items.FARMING_CAPET_9811, Items.FARMING_HOOD_9812), type = StorableFamily.CAPE_RACK_SKILL),
    FiremakingCape(Items.FIREMAKING_CAPE_10659, intArrayOf(Items.FIREMAKING_CAPE_9804, Items.FIREMAKING_CAPET_9805, Items.FIREMAKING_HOOD_9806), type = StorableFamily.CAPE_RACK_SKILL),
    FishingCape(Items.FISHING_CAPE_10657, intArrayOf(Items.FISHING_CAPE_9798, Items.FISHING_CAPET_9799, Items.FISHING_HOOD_9800), type = StorableFamily.CAPE_RACK_SKILL),
    FletchingCape(Items.FLETCHING_CAPE_10652, intArrayOf(Items.FLETCHING_CAPE_9783, Items.FLETCHING_CAPET_9784, Items.FLETCHING_HOOD_9785),  type = StorableFamily.CAPE_RACK_SKILL),
    HerbloreCape(Items.HERBLORE_CAPE_10649, intArrayOf(Items.HERBLORE_CAPE_9774, Items.HERBLORE_CAPET_9775, Items.HERBLORE_HOOD_9776),  type = StorableFamily.CAPE_RACK_SKILL),
    MagicCape(Items.MAGIC_CAPE_10644, intArrayOf(Items.MAGIC_CAPE_9762, Items.MAGIC_CAPET_9763, Items.MAGIC_HOOD_9764),  type = StorableFamily.CAPE_RACK_SKILL),
    MiningCape(Items.MINING_CAPE_10655, intArrayOf(Items.MINING_CAPE_9792, Items.MINING_CAPET_9793, Items.MINING_HOOD_9794), type = StorableFamily.CAPE_RACK_SKILL),
    PrayerCape(Items.PRAYER_CAPE_10643, intArrayOf(Items.PRAYER_CAPE_9759, Items.PRAYER_CAPET_9760, Items.PRAYER_HOOD_9761),  type = StorableFamily.CAPE_RACK_SKILL),
    RangingCape(Items.RANGING_CAPE_10642, intArrayOf(Items.RANGING_CAPE_9756, Items.RANGING_CAPET_9757, Items.RANGING_HOOD_9758),  type = StorableFamily.CAPE_RACK_SKILL),
    RunecraftCape(Items.RUNECRAFT_CAPE_10645, intArrayOf(Items.RUNECRAFT_CAPE_9765, Items.RUNECRAFT_CAPET_9766, Items.RUNECRAFTING_HOOD_9767),  type = StorableFamily.CAPE_RACK_SKILL, "Runecrafting cape"),
    SlayerCape(Items.SLAYER_CAPE_10653, intArrayOf(Items.SLAYER_CAPE_9786, Items.SLAYER_CAPET_9787, Items.SLAYER_HOOD_9788), type = StorableFamily.CAPE_RACK_SKILL),
    SmithingCape(Items.SMITHING_CAPE_10656, intArrayOf(Items.SMITHING_CAPE_9795, Items.SMITHING_CAPET_9796, Items.SMITHING_HOOD_9797), type = StorableFamily.CAPE_RACK_SKILL),
    ThievingCape(Items.THIEVING_CAPE_10650, intArrayOf(Items.THIEVING_CAPE_9777, Items.THIEVING_CAPET_9778, Items.THIEVING_HOOD_9779),  type = StorableFamily.CAPE_RACK_SKILL),
    WoodcuttingCape(Items.WOODCUTTING_CAPE_10660, intArrayOf(Items.WOODCUTTING_CAPE_9807, Items.WOODCUT_CAPET_9808, Items.WOODCUTTING_HOOD_9809), type = StorableFamily.CAPE_RACK_SKILL),
    HunterCape(Items.HUNTER_CAPE_10646, intArrayOf(Items.HUNTER_CAPE_9948, Items.HUNTER_CAPET_9949, Items.HUNTER_HOOD_9950),  type = StorableFamily.CAPE_RACK_SKILL),
    QuestCape(Items.QUEST_POINT_CAPE_10662, intArrayOf(Items.QUEST_POINT_CAPE_9813, Items.QUEST_POINT_HOOD_9814), type = StorableFamily.CAPE_RACK_SKILL),
    SummoningCape(Items.SUMMONING_CAPE_12524, intArrayOf(Items.SUMMONING_CAPE_12169, Items.SUMMONING_CAPET_12170, Items.SUMMONING_HOOD_12171), type = StorableFamily.CAPE_RACK_SKILL),
    SpottedCape(Items.SPOTTED_CAPE_10663, intArrayOf(Items.SPOTTED_CAPE_10663), type = StorableFamily.CAPE_RACK, "Spotted hunting cape"),
    SpottierCape(Items.SPOTTIER_CAPE_10664, intArrayOf(Items.SPOTTIER_CAPE_10664), type = StorableFamily.CAPE_RACK, "Spottier hunting cape"),

    // Fancy Dress - https://runescape.wiki/w/Fancy_dress_box?oldid=848873
    MimeCostume(Items.MIME_MASK_10629, intArrayOf(Items.MIME_MASK_3057, Items.MIME_TOP_3058, Items.MIME_LEGS_3059, Items.MIME_GLOVES_3060, Items.MIME_BOOTS_3061), type = StorableFamily.FANCY_DRESS, "Mime Costume"),
    RoyalFrogCostume(Items.PRINCESS_BLOUSE_10630, intArrayOf(Items.PRINCE_TUNIC_6184, Items.PRINCE_LEGGINGS_6185, Items.PRINCESS_BLOUSE_6186, Items.PRINCESS_SKIRT_6187), type = StorableFamily.FANCY_DRESS, "Royal frog costume"),
    FrogMask(Items.FROG_MASK_10721, intArrayOf(Items.FROG_MASK_6188), type = StorableFamily.FANCY_DRESS),
    ZombieOutfit(Items.ZOMBIE_SHIRT_10631, intArrayOf(Items.ZOMBIE_MASK_7594, Items.ZOMBIE_SHIRT_7592, Items.ZOMBIE_TROUSERS_7593, Items.ZOMBIE_GLOVES_7595, Items.ZOMBIE_BOOTS_7596), type = StorableFamily.FANCY_DRESS, "Zombie Outfit"),
    CamoOutfit(Items.CAMO_TOP_10632, intArrayOf(Items.CAMO_HELMET_6656, Items.CAMO_TOP_6654, Items.CAMO_BOTTOMS_6655), type = StorableFamily.FANCY_DRESS, "Camo outfit"),
    LederhosenOutfit(Items.LEDERHOSEN_TOP_10633, intArrayOf(Items.LEDERHOSEN_TOP_6180, Items.LEDERHOSEN_SHORTS_6181, Items.LEDERHOSEN_HAT_6182), type = StorableFamily.FANCY_DRESS, "Lederhosen outfit"),
    ShadeRobes(Items.SHADE_ROBE_10634, intArrayOf(Items.SHADE_ROBE_546, Items.SHADE_ROBE_548), type = StorableFamily.FANCY_DRESS, "Shade robes"),

    // Toys - https://runescape.wiki/w/Toy_box?oldid=848855
    BunnyEars(Items.BUNNY_EARS_10734, intArrayOf(Items.BUNNY_EARS_1037), type = StorableFamily.TOY_BOX),
    Scythe(Items.SCYTHE_10735, intArrayOf(Items.SCYTHE_1419), type = StorableFamily.TOY_BOX),
    YoYo(Items.YO_YO_10733, intArrayOf(Items.YO_YO_4079), type = StorableFamily.TOY_BOX),
    RubberChicken(Items.RUBBER_CHICKEN_10732, intArrayOf(Items.RUBBER_CHICKEN_4566), type = StorableFamily.TOY_BOX),
    ZombieHead(Items.ZOMBIE_HEAD_10731, intArrayOf(Items.ZOMBIE_HEAD_6722), type = StorableFamily.TOY_BOX),
    EasterRing(Items.EASTER_RING_10729, intArrayOf(Items.EASTER_RING_7927), type = StorableFamily.TOY_BOX),
    BobbleHat(Items.BOBBLE_HAT_9815, intArrayOf(Items.BOBBLE_HAT_6856), type = StorableFamily.TOY_BOX),
    BobbleScarf(Items.BOBBLE_SCARF_9816, intArrayOf(Items.BOBBLE_SCARF_6857), type = StorableFamily.TOY_BOX),
    JesterHat(Items.JESTER_HAT_6858, intArrayOf(Items.JESTER_HAT_6858), type = StorableFamily.TOY_BOX),
    JesterScarf(Items.JESTER_SCARF_6859, intArrayOf(Items.JESTER_SCARF_6859), type = StorableFamily.TOY_BOX),
    TriJesterHat(Items.TRI_JESTER_HAT_6860, intArrayOf(Items.TRI_JESTER_HAT_6860), type = StorableFamily.TOY_BOX),
    TriJesterScarf(Items.TRI_JESTER_SCARF_6861, intArrayOf(Items.TRI_JESTER_SCARF_6861), type = StorableFamily.TOY_BOX),
    WoollyHat(Items.WOOLLY_HAT_6862, intArrayOf(Items.WOOLLY_HAT_6862), type = StorableFamily.TOY_BOX),
    WoollyScarf(Items.WOOLLY_SCARF_6863, intArrayOf(Items.WOOLLY_SCARF_6863), type = StorableFamily.TOY_BOX),
    Marionette(Items.BLUE_MARIONETTE_10730, intArrayOf(Items.BLUE_MARIONETTE_6865,Items.GREEN_MARIONETTE_6866,Items.RED_MARIONETTE_6867), type = StorableFamily.TOY_BOX), // this authentically displays as 'blue marionette' in the source video
    JackLanternMask(Items.JACK_LANTERN_MASK_10723, intArrayOf(Items.JACK_LANTERN_MASK_9920), type = StorableFamily.TOY_BOX),
    SkeletonBoots(Items.SKELETON_BOOTS_10724, intArrayOf(Items.SKELETON_BOOTS_9921), type = StorableFamily.TOY_BOX),
    SkeletonGloves(Items.SKELETON_GLOVES_10725, intArrayOf(Items.SKELETON_GLOVES_9922), type = StorableFamily.TOY_BOX),
    SkeletonLeggings(Items.SKELETON_LEGGINGS_10726, intArrayOf(Items.SKELETON_LEGGINGS_9923), type = StorableFamily.TOY_BOX),
    SkeletonShirt(Items.SKELETON_SHIRT_10727, intArrayOf(Items.SKELETON_SHIRT_9924), type = StorableFamily.TOY_BOX),
    SkeletonMask(Items.SKELETON_MASK_10728, intArrayOf(Items.SKELETON_MASK_9925), type = StorableFamily.TOY_BOX),
    ReindeerHat(Items.REINDEER_HAT_10722, intArrayOf(Items.REINDEER_HAT_10507), type = StorableFamily.TOY_BOX),
    WintumberTree(Items.WINTUMBER_TREE_10508, intArrayOf(Items.WINTUMBER_TREE_10508), type = StorableFamily.TOY_BOX),
    ChickenFeet(Items.CHICKEN_FEET_11019, intArrayOf(Items.CHICKEN_FEET_11019), type = StorableFamily.TOY_BOX),
    ChickenLegs(Items.CHICKEN_LEGS_11022, intArrayOf(Items.CHICKEN_LEGS_11022), type = StorableFamily.TOY_BOX),
    ChickenWings(Items.CHICKEN_WINGS_11020, intArrayOf(Items.CHICKEN_WINGS_11020), type = StorableFamily.TOY_BOX),
    ChickenHead(Items.CHICKEN_HEAD_11021, intArrayOf(Items.CHICKEN_HEAD_11021), type = StorableFamily.TOY_BOX),
    GrimReaperHood(Items.GRIM_REAPER_HOOD_11789, intArrayOf(Items.GRIM_REAPER_HOOD_11789), type = StorableFamily.TOY_BOX),
    SnowGlobe(Items.SNOW_GLOBE_11949, intArrayOf(Items.SNOW_GLOBE_11949), type = StorableFamily.TOY_BOX),
    ChocaticeCape(Items.CHOCATRICE_CAPE_12634, intArrayOf(Items.CHOCATRICE_CAPE_12645), type = StorableFamily.TOY_BOX),
    WarlockTop(Items.WARLOCK_TOP_14076, intArrayOf(Items.WARLOCK_TOP_14076, Items.WITCH_TOP_14078), type = StorableFamily.TOY_BOX),
    WarlockLegs(Items.WARLOCK_LEGS_14077, intArrayOf(Items.WARLOCK_LEGS_14077, Items.WITCH_SKIRT_14079), type = StorableFamily.TOY_BOX),
    WarlockCloak(Items.WARLOCK_CLOAK_14081, intArrayOf(Items.WARLOCK_CLOAK_14081, Items.WITCH_CLOAK_14088), type = StorableFamily.TOY_BOX),
    SantaCostumeTop(Items.SANTA_COSTUME_TOP_14595, intArrayOf(Items.SANTA_COSTUME_TOP_14595, Items.SANTA_COSTUME_TOP_14600), type = StorableFamily.TOY_BOX),
    SantaCostumeLegs(Items.SANTA_COSTUME_LEGS_14603, intArrayOf(Items.SANTA_COSTUME_LEGS_14603, Items.SANTA_COSTUME_LEGS_14604), type = StorableFamily.TOY_BOX),
    SantaCostumeGloves(Items.SANTA_COSTUME_GLOVES_14602, intArrayOf(Items.SANTA_COSTUME_GLOVES_14602), type = StorableFamily.TOY_BOX),
    SantaCostumeBoots(Items.SANTA_COSTUME_BOOTS_14605, intArrayOf(Items.SANTA_COSTUME_BOOTS_14605), type = StorableFamily.TOY_BOX),
    IceAmulet(Items.ICE_AMULET_14596, intArrayOf(Items.ICE_AMULET_14596), type = StorableFamily.TOY_BOX),
    Cornucopia(Items.CORNUCOPIA_14570, intArrayOf(Items.CORNUCOPIA_14537), type = StorableFamily.TOY_BOX),

    // Treasure Trails - low - https://runescape.wiki/w/Treasure_chest?oldid=848831
    TrimmedBlackArmour(Items.BLACK_PLATEBODY_T_10690, intArrayOf(Items.BLACK_FULL_HELMT_2587, Items.BLACK_PLATEBODY_T_2583, Items.BLACK_PLATELEGS_T_2585, Items.BLACK_KITESHIELD_T_2589, Items.BLACK_PLATESKIRT_T_3472), type = StorableFamily.TREASURE_CHEST_LOW, "Trimmed black armour"),
    GoldTrimmedBlackArmour(Items.BLACK_PLATEBODY_G_10691, intArrayOf(Items.BLACK_FULL_HELMG_2595, Items.BLACK_PLATEBODY_G_2591, Items.BLACK_PLATELEGS_G_2593, Items.BLACK_KITESHIELD_G_2597, Items.BLACK_PLATESKIRT_G_3473), type = StorableFamily.TREASURE_CHEST_LOW, "Gold-trimmed black armour"),
    BlackHeraldicH1(Items.BLACK_HELM_H1_10699, intArrayOf(Items.BLACK_HELM_H1_10306), type = StorableFamily.TREASURE_CHEST_LOW, "Black heraldic helm"),
    BlackHeraldicH2(Items.BLACK_HELM_H2_10700, intArrayOf(Items.BLACK_HELM_H2_10308), type = StorableFamily.TREASURE_CHEST_LOW, "Black heraldic helm"),
    BlackHeraldicH3(Items.BLACK_HELM_H3_10701, intArrayOf(Items.BLACK_HELM_H3_10310), type = StorableFamily.TREASURE_CHEST_LOW, "Black heraldic helm"),
    BlackHeraldicH4(Items.BLACK_HELM_H4_10702, intArrayOf(Items.BLACK_HELM_H4_10312), type = StorableFamily.TREASURE_CHEST_LOW, "Black heraldic helm"),
    BlackHeraldicH5(Items.BLACK_HELM_H5_10703, intArrayOf(Items.BLACK_HELM_H5_10314), type = StorableFamily.TREASURE_CHEST_LOW, "Black heraldic helm"),
    BlackHeraldicKiteshieldH1(Items.BLACK_SHIELDH1_10665, intArrayOf(Items.BLACK_SHIELDH1_7332), type = StorableFamily.TREASURE_CHEST_LOW, "Black heraldic kiteshield"),
    BlackHeraldicKiteshieldH2(Items.BLACK_SHIELDH2_10668, intArrayOf(Items.BLACK_SHIELDH2_7338), type = StorableFamily.TREASURE_CHEST_LOW, "Black heraldic kiteshield"),
    BlackHeraldicKiteshieldH3(Items.BLACK_SHIELDH3_10671, intArrayOf(Items.BLACK_SHIELDH3_7344), type = StorableFamily.TREASURE_CHEST_LOW, "Black heraldic kiteshield"),
    BlackHeraldicKiteshieldH4(Items.BLACK_SHIELDH4_10674, intArrayOf(Items.BLACK_SHIELDH4_7350), type = StorableFamily.TREASURE_CHEST_LOW, "Black heraldic kiteshield"),
    BlackHeraldicKiteshieldH5(Items.BLACK_SHIELDH5_10677, intArrayOf(Items.BLACK_SHIELDH5_7356), type = StorableFamily.TREASURE_CHEST_LOW, "Black heraldic kiteshield"),
    TrimmedStuddedArmour(Items.STUDDED_BODY_T_10681, intArrayOf(Items.STUDDED_BODY_T_7364, Items.STUDDED_CHAPS_T_7368), type = StorableFamily.TREASURE_CHEST_LOW, "Fur-trimmed studded leather"),
    GoldTrimmedStuddedArmour(Items.STUDDED_BODY_G_10680, intArrayOf(Items.STUDDED_BODY_G_7362, Items.STUDDED_CHAPS_G_7366), type = StorableFamily.TREASURE_CHEST_LOW, "Gold-trimmed studded leather"),
    TrimmedWizardRobes(Items.WIZARD_ROBE_T_10687, intArrayOf(Items.WIZARD_HAT_T_7396, Items.WIZARD_ROBE_T_7392, Items.BLUE_SKIRT_T_7388), type = StorableFamily.TREASURE_CHEST_LOW, "Trimmed wizard's robes"),
    GoldTrimmedWizardRobes(Items.WIZARD_ROBE_G_10686, intArrayOf(Items.WIZARD_HAT_G_7394, Items.WIZARD_ROBE_G_7390, Items.BLUE_SKIRT_G_7386), type = StorableFamily.TREASURE_CHEST_LOW, "Gold-trimmed wizard's robes"),
    WizardBoots(Items.WIZARD_BOOTS_10689, intArrayOf(Items.WIZARD_BOOTS_2579), type = StorableFamily.TREASURE_CHEST_LOW),
    TrimmedAmuletOfMagic(Items.AMULET_OF_MAGICT_10738, intArrayOf(Items.AMULET_OF_MAGICT_10366), type = StorableFamily.TREASURE_CHEST_LOW, "Trimmed amulet of magic"),
    HighwaymanMask(Items.HIGHWAYMAN_MASK_10692, intArrayOf(Items.HIGHWAYMAN_MASK_2631), type = StorableFamily.TREASURE_CHEST_LOW),
    Pantaloons(Items.PANTALOONS_10744, intArrayOf(Items.PANTALOONS_10396), type = StorableFamily.TREASURE_CHEST_LOW),
    PowderedWig(Items.A_POWDERED_WIG_10740, intArrayOf(Items.A_POWDERED_WIG_10392), type = StorableFamily.TREASURE_CHEST_LOW),
    FlaredTrousers(Items.FLARED_TROUSERS_10742, intArrayOf(Items.FLARED_TROUSERS_10394), type = StorableFamily.TREASURE_CHEST_LOW),
    SleepingCap(Items.SLEEPING_CAP_10746, intArrayOf(Items.SLEEPING_CAP_10398), type = StorableFamily.TREASURE_CHEST_LOW),
    BlackBeret(Items.BLACK_BERET_10694, intArrayOf(Items.BLACK_BERET_2635), type = StorableFamily.TREASURE_CHEST_LOW),
    WhiteBeret(Items.WHITE_BERET_10695, intArrayOf(Items.WHITE_BERET_2637), type = StorableFamily.TREASURE_CHEST_LOW),
    BlueBeret(Items.BLUE_BERET_10693, intArrayOf(Items.BLUE_BERET_2633), type = StorableFamily.TREASURE_CHEST_LOW),
    BobTheCatShirt(Items.BOB_SHIRT_10714, intArrayOf(Items.BOB_SHIRT_10316), type = StorableFamily.TREASURE_CHEST_LOW, "Bob the Cat shirt (red)"),
    BobTheCatShirt1(Items.BOB_SHIRT_10715, intArrayOf(Items.BOB_SHIRT_10318), type = StorableFamily.TREASURE_CHEST_LOW, "Bob the Cat shirt (blue)"),
    BobTheCatShirt2(Items.BOB_SHIRT_10716, intArrayOf(Items.BOB_SHIRT_10320), type = StorableFamily.TREASURE_CHEST_LOW, "Bob the Cat shirt (green)"),
    BobTheCatShirt3(Items.BOB_SHIRT_10717, intArrayOf(Items.BOB_SHIRT_10322), type = StorableFamily.TREASURE_CHEST_LOW, "Bob the Cat shirt (black)"),
    BobTheCatShirt4(Items.BOB_SHIRT_10718, intArrayOf(Items.BOB_SHIRT_10324), type = StorableFamily.TREASURE_CHEST_LOW, "Bob the Cat shirt (purple)"),
    RedElegant(Items.ELEGANT_SHIRT_10750, intArrayOf(Items.RED_ELE_SHIRT_10404, Items.RED_ELE_LEGS_10406, Items.RED_ELE_BLOUSE_10424, Items.RED_ELE_SKIRT_10426), type = StorableFamily.TREASURE_CHEST_LOW, "Elegant clothes (red)"),
    BlueElegant(Items.ELEGANT_SHIRT_10752, intArrayOf(Items.BLUE_ELE_SHIRT_10408, Items.BLUE_ELE_LEGS_10410, Items.BLUE_ELE_BLOUSE_10428, Items.BLUE_ELE_SKIRT_10430), type = StorableFamily.TREASURE_CHEST_LOW, "Elegant clothes (blue)"),
    GreenElegant(Items.ELEGANT_SHIRT_10754, intArrayOf(Items.GREEN_ELE_SHIRT_10412,Items.GREEN_ELE_LEGS_10414, Items.GREEN_ELE_BLOUSE_10432, Items.GREEN_ELE_SKIRT_10434), type = StorableFamily.TREASURE_CHEST_LOW, "Elegant clothes (green)"),
    BeretAndMask(Items.BERET_MASK_11278, intArrayOf(Items.BERET_AND_MASK_11282), type = StorableFamily.TREASURE_CHEST_LOW),
    BlackCane(Items.BLACK_CANE_13163, intArrayOf(Items.BLACK_CANE_13095), type = StorableFamily.TREASURE_CHEST_LOW),
    SpikedHelmet(Items.SPIKED_HELMET_13168, intArrayOf(Items.SPIKED_HELMET_13105), type = StorableFamily.TREASURE_CHEST_LOW),

    // Treasure Trails - medium - https://runescape.wiki/w/Treasure_chest?oldid=848831
    TrimmedAdamantiteArmour(Items.ADAM_PLATEBODY_T_10697, intArrayOf(Items.ADAM_FULL_HELMT_2605, Items.ADAM_PLATEBODY_T_2599, Items.ADAM_PLATELEGS_T_2601, Items.ADAM_KITESHIELD_T_2603, Items.ADAM_PLATESKIRT_T_3474), type = StorableFamily.TREASURE_CHEST_MED, "Trimmed adamantite armour"),
    GoldTrimmedAdamantiteArmour(Items.ADAM_PLATEBODY_G_10698, intArrayOf(Items.ADAM_FULL_HELMG_2613, Items.ADAM_PLATEBODY_G_2607, Items.ADAM_PLATELEGS_G_2609, Items.ADAM_KITESHIELD_G_2611, Items.ADAM_PLATESKIRT_G_3475), type = StorableFamily.TREASURE_CHEST_MED, "Gold-trimmed adamantite armour"),
    AdamantHeraldicH1(Items.ADAMANT_HELM_H1_10709, intArrayOf(Items.ADAMANT_HELM_H1_10296), type = StorableFamily.TREASURE_CHEST_MED, "Adamant heraldic helm"),
    AdamantHeraldicH2(Items.ADAMANT_HELM_H2_10710, intArrayOf(Items.ADAMANT_HELM_H2_10298), type = StorableFamily.TREASURE_CHEST_MED, "Adamant heraldic helm"),
    AdamantHeraldicH3(Items.ADAMANT_HELM_H3_10711, intArrayOf(Items.ADAMANT_HELM_H3_10300), type = StorableFamily.TREASURE_CHEST_MED, "Adamant heraldic helm"),
    AdamantHeraldicH4(Items.ADAMANT_HELM_H4_10712, intArrayOf(Items.ADAMANT_HELM_H4_10302), type = StorableFamily.TREASURE_CHEST_MED, "Adamant heraldic helm"),
    AdamantHeraldicH5(Items.ADAMANT_HELM_H5_10713, intArrayOf(Items.ADAMANT_HELM_H5_10304), type = StorableFamily.TREASURE_CHEST_MED, "Adamant heraldic helm"),
    AdamantKiteshieldH1(Items.ADAMANT_SHIELDH1_10666, intArrayOf(Items.ADAMANT_SHIELDH1_7334), type = StorableFamily.TREASURE_CHEST_MED, "Adamant heraldic kiteshield"),
    AdamantKiteshieldH2(Items.ADAMANT_SHIELDH2_10669, intArrayOf(Items.ADAMANT_SHIELDH2_7340), type = StorableFamily.TREASURE_CHEST_MED, "Adamant heraldic kiteshield"),
    AdamantKiteshieldH3(Items.ADAMANT_SHIELDH3_10672, intArrayOf(Items.ADAMANT_SHIELDH3_7346), type = StorableFamily.TREASURE_CHEST_MED, "Adamant heraldic kiteshield"),
    AdamantKiteshieldH4(Items.ADAMANT_SHIELDH4_10675, intArrayOf(Items.ADAMANT_SHIELDH4_7352), type = StorableFamily.TREASURE_CHEST_MED, "Adamant heraldic kiteshield"),
    AdamantKiteshieldH5(Items.ADAMANT_SHIELDH5_10678, intArrayOf(Items.ADAMANT_SHIELDH5_7358), type = StorableFamily.TREASURE_CHEST_MED, "Adamant heraldic kiteshield"),
    TrimmedGreenDhide(Items.DHIDE_BODY_T_10683, intArrayOf(Items.DHIDE_BODY_T_7372, Items.DHIDE_CHAPS_T_7380), type = StorableFamily.TREASURE_CHEST_MED, "Trimmed green dragonhide armour"),
    GoldTrimmedGreenDhide(Items.DHIDE_BODYG_10682, intArrayOf(Items.DHIDE_BODYG_7370, Items.DHIDE_CHAPS_G_7378), type = StorableFamily.TREASURE_CHEST_MED, "Gold-trimmed green dragonhide armour"),
    RangerBoots(Items.RANGER_BOOTS_10696, intArrayOf(Items.RANGER_BOOTS_2577), type = StorableFamily.TREASURE_CHEST_MED),
    TrimmedAmuletOfStrength(Items.STRENGTH_AMULETT_10736, intArrayOf(Items.STRENGTH_AMULETT_10364), type = StorableFamily.TREASURE_CHEST_MED, "Trimmed amulet of strength"),
    RedHeadband(Items.RED_HEADBAND_10768, intArrayOf(Items.RED_HEADBAND_2645), type = StorableFamily.TREASURE_CHEST_MED),
    BlackHeadband(Items.BLACK_HEADBAND_10770, intArrayOf(Items.BLACK_HEADBAND_2647), type = StorableFamily.TREASURE_CHEST_MED),
    BrownHeadband(Items.BROWN_HEADBAND_10772, intArrayOf(Items.BROWN_HEADBAND_2649), type = StorableFamily.TREASURE_CHEST_MED),
    RedBoater(Items.RED_BOATER_10758, intArrayOf(Items.RED_BOATER_7319), type = StorableFamily.TREASURE_CHEST_MED, "Red straw boater"),
    OrangeBoater(Items.ORANGE_BOATER_10760, intArrayOf(Items.ORANGE_BOATER_7321), type = StorableFamily.TREASURE_CHEST_MED, "Orange straw boater"),
    GreenBoater(Items.GREEN_BOATER_10762, intArrayOf(Items.GREEN_BOATER_7323), type = StorableFamily.TREASURE_CHEST_MED, "Green straw boater"),
    BlueBoater(Items.BLUE_BOATER_10764, intArrayOf(Items.BLUE_BOATER_7325), type = StorableFamily.TREASURE_CHEST_MED, "Blue straw boater"),
    BlackBoater(Items.BLACK_BOATER_10766, intArrayOf(Items.BLACK_BOATER_7327), type = StorableFamily.TREASURE_CHEST_MED, "Black straw boater"),
    BlackElegant(Items.ELEGANT_SHIRT_10748, intArrayOf(Items.BLACK_ELE_SHIRT_10400, Items.BLACK_ELE_LEGS_10402, Items.WHITE_ELE_BLOUSE_10420, Items.WHITE_ELE_SKIRT_10422), type = StorableFamily.TREASURE_CHEST_MED, "Elegant clothes (black)"),
    PurpleElegant(Items.ELEGANT_SHIRT_10756, intArrayOf(Items.PURPLE_ELE_BLOUSE_10436, Items.PURPLE_ELE_SKIRT_10438), type = StorableFamily.TREASURE_CHEST_MED, "Elegant clothes (purple)"),
    AdamantCane(Items.ADAMANT_CANE_13164, intArrayOf(Items.ADAMANT_CANE_13097), type = StorableFamily.TREASURE_CHEST_MED),
    SheepMask(Items.SHEEP_MASK_13169, intArrayOf(Items.SHEEP_MASK_13107), type = StorableFamily.TREASURE_CHEST_MED),
    BatMask(Items.BAT_MASK_13171, intArrayOf(Items.BAT_MASK_13111), type = StorableFamily.TREASURE_CHEST_MED),
    WolfMask(Items.WOLF_MASK_13173, intArrayOf(Items.WOLF_MASK_13115), type = StorableFamily.TREASURE_CHEST_MED),
    PenguinMask(Items.PENGUIN_MASK_13170, intArrayOf(Items.PENGUIN_MASK_13109), type = StorableFamily.TREASURE_CHEST_MED),
    CatMask(Items.CAT_MASK_13172, intArrayOf(Items.CAT_MASK_13113), type = StorableFamily.TREASURE_CHEST_MED),
    PithHelmet(Items.PITH_HELMET_13167, intArrayOf(Items.PITH_HELMET_13103), type = StorableFamily.TREASURE_CHEST_MED),

    // Treasure Trails - hard - https://runescape.wiki/w/Treasure_chest?oldid=848831
    TrimmedRuneArmour(Items.RUNE_PLATEBODY_T_10800, intArrayOf(Items.RUNE_FULL_HELM_T_2627, Items.RUNE_PLATEBODY_T_2623, Items.RUNE_PLATELEGS_T_2625, Items.RUNE_KITESHIELD_T_2629, Items.RUNE_PLATESKIRT_T_3477), type = StorableFamily.TREASURE_CHEST_HIGH, "Trimmed rune armour"),
    GoldTrimmedRuneArmour(Items.RUNE_PLATEBODY_G_10798, intArrayOf(Items.RUNE_FULL_HELMG_2619, Items.RUNE_PLATEBODY_G_2615, Items.RUNE_PLATELEGS_G_2617, Items.RUNE_KITESHIELD_G_2621, Items.RUNE_PLATESKIRT_G_3476), type = StorableFamily.TREASURE_CHEST_HIGH, "Gold-trimmed rune armour"),
    GildedArmour(Items.GILDED_PLATEBODY_10782, intArrayOf(Items.GILDED_FULL_HELM_3486, Items.GILDED_PLATEBODY_3481, Items.GILDED_PLATELEGS_3483, Items.GILDED_KITESHIELD_3488, Items.GILDED_PLATESKIRT_3485), type = StorableFamily.TREASURE_CHEST_HIGH, "Gold-plated rune armour"),
    RuneHelmH1(Items.RUNE_HELM_H1_10704, intArrayOf(Items.RUNE_HELM_H1_10286), type = StorableFamily.TREASURE_CHEST_HIGH, "Rune heraldic helm"),
    RuneHelmH2(Items.RUNE_HELM_H2_10705, intArrayOf(Items.RUNE_HELM_H2_10288), type = StorableFamily.TREASURE_CHEST_HIGH, "Rune heraldic helm"),
    RuneHelmH3(Items.RUNE_HELM_H3_10706, intArrayOf(Items.RUNE_HELM_H3_10290), type = StorableFamily.TREASURE_CHEST_HIGH, "Rune heraldic helm"),
    RuneHelmH4(Items.RUNE_HELM_H4_10707, intArrayOf(Items.RUNE_HELM_H4_10292), type = StorableFamily.TREASURE_CHEST_HIGH, "Rune heraldic helm"),
    RuneHelmH5(Items.RUNE_HELM_H5_10708, intArrayOf(Items.RUNE_HELM_H5_10294), type = StorableFamily.TREASURE_CHEST_HIGH, "Rune heraldic helm"),
    RuneShieldH1(Items.RUNE_SHIELDH1_10667, intArrayOf(Items.RUNE_SHIELDH1_7336), type = StorableFamily.TREASURE_CHEST_HIGH, "Rune heraldic kiteshield"),
    RuneShieldH2(Items.RUNE_SHIELDH2_10670, intArrayOf(Items.RUNE_SHIELDH2_7342), type = StorableFamily.TREASURE_CHEST_HIGH, "Rune heraldic kiteshield"),
    RuneShieldH3(Items.RUNE_SHIELDH3_10673, intArrayOf(Items.RUNE_SHIELDH3_7348), type = StorableFamily.TREASURE_CHEST_HIGH, "Rune heraldic kiteshield"),
    RuneShieldH4(Items.RUNE_SHIELDH4_10676, intArrayOf(Items.RUNE_SHIELDH4_7354), type = StorableFamily.TREASURE_CHEST_HIGH, "Rune heraldic kiteshield"),
    RuneShieldH5(Items.RUNE_SHIELDH5_10679, intArrayOf(Items.RUNE_SHIELDH5_7360), type = StorableFamily.TREASURE_CHEST_HIGH, "Rune heraldic kiteshield"),
    ZamorakRuneArmour(Items.ZAMORAK_PLATEBODY_10776, intArrayOf(Items.ZAMORAK_FULL_HELM_2657, Items.ZAMORAK_PLATEBODY_2653, Items.ZAMORAK_PLATELEGS_2655, Items.ZAMORAK_KITESHIELD_2659, Items.ZAMORAK_PLATESKIRT_3478), type = StorableFamily.TREASURE_CHEST_HIGH, "Zamorak rune armour"),
    SaradominRuneArmour(Items.SARADOMIN_PLATE_10778, intArrayOf(Items.SARADOMIN_FULL_HELM_2665, Items.SARADOMIN_PLATEBODY_2661, Items.SARADOMIN_PLATELEGS_2663, Items.SARADOMIN_KITESHIELD_2667, Items.SARADOMIN_PLATESKIRT_3479), type = StorableFamily.TREASURE_CHEST_HIGH, "Saradomin rune armour"),
    GuthixRuneArmour(Items.GUTHIX_PLATEBODY_10780, intArrayOf(Items.GUTHIX_FULL_HELM_2673, Items.GUTHIX_PLATEBODY_2669, Items.GUTHIX_PLATELEGS_2671, Items.GUTHIX_KITESHIELD_2675, Items.GUTHIX_PLATESKIRT_3480), type = StorableFamily.TREASURE_CHEST_HIGH, "Guthix rune armour"),
    TrimmedBlueDhide(Items.DHIDE_BODY_T_10685, intArrayOf(Items.DHIDE_BODY_T_7376, Items.DHIDE_CHAPS_T_7384), type = StorableFamily.TREASURE_CHEST_HIGH, "Trimmed blue dragonhide armour"),
    GoldTrimmedBlueDhide(Items.DHIDE_BODY_G_10684, intArrayOf(Items.DHIDE_BODY_G_7374, Items.DHIDE_CHAPS_G_7382), type = StorableFamily.TREASURE_CHEST_HIGH, "Gold-trimmed blue dragonhide armour"),
    SaradominBlessedDhide(Items.SARADOMIN_DHIDE_10792, intArrayOf(Items.SARADOMIN_DHIDE_10386, Items.SARADOMIN_CHAPS_10388, Items.SARADOMIN_BRACERS_10384, Items.SARADOMIN_COIF_10390), type = StorableFamily.TREASURE_CHEST_HIGH, "Saradomin blessed dragonhide"),
    GuthixBlessedDhide(Items.GUTHIX_DRAGONHIDE_10794, intArrayOf(Items.GUTHIX_DRAGONHIDE_10378, Items.GUTHIX_CHAPS_10380, Items.GUTHIX_BRACERS_10376, Items.GUTHIX_COIF_10382), type = StorableFamily.TREASURE_CHEST_HIGH, "Guthix blessed dragonhide"),
    ZamorakBlessedDhide(Items.ZAMORAK_DHIDE_10790, intArrayOf(Items.ZAMORAK_DHIDE_10370, Items.ZAMORAK_CHAPS_10372, Items.ZAMORAK_BRACERS_10368, Items.ZAMORAK_COIF_10374), type = StorableFamily.TREASURE_CHEST_HIGH, "Zamorak blessed dragonhide"),
    RobinHoodHat(Items.ROBIN_HOOD_HAT_10796, intArrayOf(Items.ROBIN_HOOD_HAT_2581), type = StorableFamily.TREASURE_CHEST_HIGH),
    EnchantedRobes(Items.ENCHANTED_TOP_10688, intArrayOf(Items.ENCHANTED_HAT_7400, Items.ENCHANTED_TOP_7399, Items.ENCHANTED_ROBE_7398), type = StorableFamily.TREASURE_CHEST_HIGH, "Enchanted robes"),
    SaradominVestments(Items.SARADOMIN_ROBE_TOP_10784, intArrayOf(Items.SARADOMIN_MITRE_10452, Items.SARADOMIN_ROBE_TOP_10458, Items.SARADOMIN_ROBE_LEGS_10464, Items.SARADOMIN_STOLE_10470, Items.SARADOMIN_CLOAK_10446, Items.SARADOMIN_CROZIER_10440), type = StorableFamily.TREASURE_CHEST_HIGH, "Saradomin vestments"),
    ZamorakVestments(Items.ZAMORAK_ROBE_TOP_10786, intArrayOf(Items.ZAMORAK_MITRE_10456, Items.ZAMORAK_ROBE_TOP_10460, Items.ZAMORAK_ROBE_LEGS_10468, Items.ZAMORAK_STOLE_10474, Items.ZAMORAK_CLOAK_10450, Items.ZAMORAK_CROZIER_10444), type = StorableFamily.TREASURE_CHEST_HIGH, "Zamorak vestments"),
    GuthixVestments(Items.GUTHIX_ROBE_TOP_10788, intArrayOf(Items.GUTHIX_MITRE_10454, Items.GUTHIX_ROBE_TOP_10462, Items.GUTHIX_ROBE_LEGS_10466, Items.GUTHIX_STOLE_10472, Items.GUTHIX_CLOAK_10448, Items.GUTHIX_CROZIER_10442), type = StorableFamily.TREASURE_CHEST_HIGH, "Guthix vestments"),
    TrimmedAmuletOfGlory(Items.AMULET_OF_GLORYT_10719, intArrayOf(Items.AMULET_OF_GLORYT_10362), type = StorableFamily.TREASURE_CHEST_HIGH, "Trimmed amulet of glory"),
    PiratesHat(Items.PIRATES_HAT_10774, intArrayOf(Items.PIRATES_HAT_2651), type = StorableFamily.TREASURE_CHEST_HIGH),
    CavalierMask(Items.CAVALIER_MASK_11277, intArrayOf(Items.CAVALIER_AND_MASK_11280), type = StorableFamily.TREASURE_CHEST_HIGH),
    DarkCavalier(Items.DARK_CAVALIER_10804, intArrayOf(Items.DARK_CAVALIER_2641), type = StorableFamily.TREASURE_CHEST_HIGH, "Dark brown cavalier"),
    TanCavalier(Items.TAN_CAVALIER_10802, intArrayOf(Items.TAN_CAVALIER_2639), type = StorableFamily.TREASURE_CHEST_HIGH),
    BlackCavalier(Items.BLACK_CAVALIER_10806, intArrayOf(Items.BLACK_CAVALIER_2643), type = StorableFamily.TREASURE_CHEST_HIGH),
    RuneCane(Items.RUNE_CANE_13165, intArrayOf(Items.RUNE_CANE_13099), type = StorableFamily.TREASURE_CHEST_HIGH),
    TopHat(Items.TOP_HAT_13166, intArrayOf(Items.TOP_HAT_13101), type = StorableFamily.TREASURE_CHEST_HIGH),

    // Magic Wardrobe - https://runescape.wiki/w/Magic_wardrobe?oldid=848846
    MysticBlueSet(Items.MYSTIC_HAT_10601, intArrayOf(Items.MYSTIC_HAT_4089, Items.MYSTIC_ROBE_TOP_4091, Items.MYSTIC_ROBE_BOTTOM_4093, Items.MYSTIC_GLOVES_4095, Items.MYSTIC_BOOTS_4097), type = StorableFamily.MAGIC_WARDROBE, "Mystic robes"),
    MysticDarkSet(Items.MYSTIC_HAT_10602, intArrayOf(Items.MYSTIC_HAT_4099, Items.MYSTIC_ROBE_TOP_4101, Items.MYSTIC_ROBE_BOTTOM_4103, Items.MYSTIC_GLOVES_4105, Items.MYSTIC_BOOTS_4107), type = StorableFamily.MAGIC_WARDROBE, "Dark mystic robes"),
    MysticLightSet(Items.MYSTIC_HAT_10603, intArrayOf(Items.MYSTIC_HAT_4109, Items.MYSTIC_ROBE_TOP_4111, Items.MYSTIC_ROBE_BOTTOM_4113, Items.MYSTIC_GLOVES_4115, Items.MYSTIC_BOOTS_4117), type = StorableFamily.MAGIC_WARDROBE, "Light mystic robes"),
    SkeletalSet(Items.SKELETAL_HELM_10604, intArrayOf(Items.SKELETAL_HELM_6137, Items.SKELETAL_TOP_6139, Items.SKELETAL_BOTTOMS_6141, Items.SKELETAL_GLOVES_6153, Items.SKELETAL_BOOTS_6147), type = StorableFamily.MAGIC_WARDROBE, "Skeletal"),
    InfinitySet(Items.INFINITY_TOP_10605, intArrayOf(Items.INFINITY_HAT_6918, Items.INFINITY_TOP_6916, Items.INFINITY_BOTTOMS_6924, Items.INFINITY_GLOVES_6922, Items.INFINITY_BOOTS_6920), type = StorableFamily.MAGIC_WARDROBE, "Infinity robes"),
    SplitbarkSet(Items.SPLITBARK_HELM_10606, intArrayOf(Items.SPLITBARK_HELM_3385, Items.SPLITBARK_BODY_3387, Items.SPLITBARK_LEGS_3389, Items.SPLITBARK_GAUNTLETS_3391, Items.SPLITBARK_BOOTS_3393), type = StorableFamily.MAGIC_WARDROBE, "Splitbark armour"),
    GhostlySet(Items.GHOSTLY_BOOTS_10607, intArrayOf(Items.GHOSTLY_ROBE_6107, Items.GHOSTLY_ROBE_6108, Items.GHOSTLY_HOOD_6109, Items.GHOSTLY_GLOVES_6110, Items.GHOSTLY_BOOTS_6106, Items.GHOSTLY_CLOAK_6111), type = StorableFamily.MAGIC_WARDROBE, "Ghostly robes"),
    MoonclanSet(Items.MOONCLAN_HAT_10608, intArrayOf(Items.MOONCLAN_HELM_9068, Items.MOONCLAN_ARMOUR_9070, Items.MOONCLAN_SKIRT_9071, Items.MOONCLAN_GLOVES_9072, Items.MOONCLAN_BOOTS_9073), type = StorableFamily.MAGIC_WARDROBE, "Moonclan robes"),
    LunarSet(Items.LUNAR_HELM_10609, intArrayOf(Items.LUNAR_HELM_9096, Items.LUNAR_TORSO_9097, Items.LUNAR_LEGS_9098, Items.LUNAR_GLOVES_9099, Items.LUNAR_BOOTS_9100, Items.LUNAR_CAPE_9101, Items.LUNAR_AMULET_9102, Items.LUNAR_RING_9104), type = StorableFamily.MAGIC_WARDROBE, "Lunar robes"),
    RunecrafterYellow(Items.RUNECRAFTER_HAT_13656, intArrayOf(Items.RUNECRAFTER_HAT_13615, Items.RUNECRAFTER_ROBE_13614, Items.RUNECRAFTER_SKIRT_13617), type = StorableFamily.MAGIC_WARDROBE, "Runecrafter robes (yellow)"),
    RunecrafterGreen(Items.RUNECRAFTER_HAT_13657, intArrayOf(Items.RUNECRAFTER_HAT_13620, Items.RUNECRAFTER_ROBE_13619, Items.RUNECRAFTER_SKIRT_13622), type = StorableFamily.MAGIC_WARDROBE, "Runecrafter robes (green)"),
    RunecrafterBlue(Items.RUNECRAFTER_HAT_13658, intArrayOf(Items.RUNECRAFTER_HAT_13625, Items.RUNECRAFTER_ROBE_13624, Items.RUNECRAFTER_SKIRT_13627), type = StorableFamily.MAGIC_WARDROBE, "Runecrafter robes (blue)"),
    DagonHaiSet(Items.DAGONHAI_ROBE_TOP_14497, intArrayOf(Items.DAGONHAI_HAT_14499, Items.DAGONHAI_ROBE_TOP_14497, Items.DAGONHAI_ROBE_BOTTOM_14501), type = StorableFamily.MAGIC_WARDROBE, "Dagon'Hai Robes"),

    // Armour Case - https://runescape.wiki/w/Armour_case?oldid=848835
    DecorativeArmour(Items.DECORATIVE_ARMOUR_10610, intArrayOf(Items.DECORATIVE_ARMOUR_4069, Items.DECORATIVE_SWORD_4068, Items.DECORATIVE_ARMOUR_4070, Items.DECORATIVE_HELM_4071, Items.DECORATIVE_SHIELD_4072, Items.DECORATIVE_ARMOUR_4504, Items.DECORATIVE_SWORD_4503, Items.DECORATIVE_ARMOUR_4505, Items.DECORATIVE_HELM_4506, Items.DECORATIVE_SHIELD_4507, Items.DECORATIVE_ARMOUR_4509, Items.DECORATIVE_SWORD_4508, Items.DECORATIVE_ARMOUR_4510, Items.DECORATIVE_HELM_4511, Items.DECORATIVE_SHIELD_4512), type = StorableFamily.ARMOUR_CASE, "Castlewars armour"), // should be any one colour set per wiki
    VoidKnightArmour(Items.VOID_KNIGHT_TOP_10611, intArrayOf(Items.VOID_KNIGHT_TOP_8839, Items.VOID_KNIGHT_ROBE_8840, Items.VOID_KNIGHT_GLOVES_8842), type = StorableFamily.ARMOUR_CASE, "Void Knight armour"),
    RogueArmour(Items.ROGUE_MASK_10612, intArrayOf(Items.ROGUE_MASK_5554, Items.ROGUE_TOP_5553, Items.ROGUE_TROUSERS_5555), type = StorableFamily.ARMOUR_CASE, "Rogue armour"),
    SpinedArmour(Items.SPINED_HELM_10614, intArrayOf(Items.SPINED_HELM_6131, Items.SPINED_BODY_6133, Items.SPINED_CHAPS_6135, Items.SPINED_BOOTS_6143, Items.SPINED_GLOVES_6149), type = StorableFamily.ARMOUR_CASE, "Spined armour"),
    RockshellArmour(Items.ROCK_SHELL_HELM_10613, intArrayOf(Items.ROCK_SHELL_HELM_6128, Items.ROCK_SHELL_PLATE_6129, Items.ROCK_SHELL_LEGS_6130, Items.ROCK_SHELL_BOOTS_6145, Items.ROCK_SHELL_GLOVES_6151), type = StorableFamily.ARMOUR_CASE, "Rockshell armour"),
    TribalMaskP(Items.TRIBAL_MASK_10615, intArrayOf(Items.TRIBAL_MASK_6335), type = StorableFamily.ARMOUR_CASE, "Broodoo mask (poison)"),
    TribalMaskD(Items.TRIBAL_MASK_10616, intArrayOf(Items.TRIBAL_MASK_6337), type = StorableFamily.ARMOUR_CASE, "Broodoo mask (disease)"),
    TribalMaskC(Items.TRIBAL_MASK_10617, intArrayOf(Items.TRIBAL_MASK_6339), type = StorableFamily.ARMOUR_CASE, "Broodoo mask (combat)"),
    WhiteKnightArmour(Items.WHITE_PLATEBODY_10618, intArrayOf(Items.WHITE_PLATEBODY_6617, Items.WHITE_FULL_HELM_6623, Items.WHITE_PLATELEGS_6625, Items.WHITE_PLATESKIRT_6627), type = StorableFamily.ARMOUR_CASE, "White knight armour"),
    TempleKnightInitiateArmour(Items.INITIATE_HAUBERK_10619, intArrayOf(Items.INITIATE_SALLET_5574, Items.INITIATE_HAUBERK_5575, Items.INITIATE_CUISSE_5576), type = StorableFamily.ARMOUR_CASE, "Temple Knight initiate armour"),
    TempleKnightProselyteArmour(Items.PROSELYTE_HAUBERK_10620, intArrayOf(Items.PROSELYTE_SALLET_9672, Items.PROSELYTE_HAUBERK_9674, Items.PROSELYTE_CUISSE_9676, Items.PROSELYTE_TASSET_9678), type = StorableFamily.ARMOUR_CASE, "Temple Knight proselyte armour"),
    MournerGear(Items.MOURNER_TOP_10621, intArrayOf(Items.MOURNER_TOP_6065, Items.MOURNER_TROUSERS_6066, Items.MOURNER_BOOTS_6069, Items.MOURNER_CLOAK_6070), type = StorableFamily.ARMOUR_CASE, "Mourner gear"),
    GraahkHunterGear(Items.GRAAHK_TOP_10624, intArrayOf(Items.GRAAHK_TOP_10049, Items.GRAAHK_LEGS_10047, Items.GRAAHK_HEADDRESS_10051), type = StorableFamily.ARMOUR_CASE, "Graahk hunter gear"),
    LarupiaHunterGear(Items.LARUPIA_TOP_10623, intArrayOf(Items.LARUPIA_TOP_10043, Items.LARUPIA_LEGS_10041, Items.LARUPIA_HAT_10045), type = StorableFamily.ARMOUR_CASE, "Larupia hunter gear"),
    KyattHunterGear(Items.KYATT_TOP_10622, intArrayOf(Items.KYATT_TOP_10037, Items.KYATT_LEGS_10035, Items.KYATT_HAT_10039), type = StorableFamily.ARMOUR_CASE, "Kyatt hunter gear"),
    PolarCamouflageGear(Items.POLAR_CAMO_TOP_10628, intArrayOf(Items.POLAR_CAMO_TOP_10628, Items.POLAR_CAMO_LEGS_10067), type = StorableFamily.ARMOUR_CASE, "Polar camouflage gear"),
    JungleCamouflageGear(Items.JUNGLE_CAMO_TOP_10626, intArrayOf(Items.JUNGLE_CAMO_TOP_10057, Items.JUNGLE_CAMO_LEGS_10059), type = StorableFamily.ARMOUR_CASE, "Jungle camouflage gear"),
    WoodCamouflageGear(Items.WOOD_CAMO_TOP_10625, intArrayOf(Items.WOOD_CAMO_TOP_10053, Items.WOOD_CAMO_LEGS_10055), type = StorableFamily.ARMOUR_CASE, "Wood camouflage gear"),
    DesertCamouflageGear(Items.DESERT_CAMO_TOP_10627, intArrayOf(Items.DESERT_CAMO_TOP_10061, Items.DESERT_CAMO_LEGS_10063), type = StorableFamily.ARMOUR_CASE, "Desert camouflage gear"),
    BuildersCostume(Items.BUILDERS_SHIRT_10863, intArrayOf(Items.BUILDERS_SHIRT_10863, Items.BUILDERS_TROUSERS_10864, Items.BUILDERS_BOOTS_10865), type = StorableFamily.ARMOUR_CASE, "Builder's costume"),
    LumberjackCostume(Items.LUMBERJACK_TOP_10945, intArrayOf(Items.LUMBERJACK_TOP_10939, Items.LUMBERJACK_LEGS_10940, Items.LUMBERJACK_HAT_10941), type = StorableFamily.ARMOUR_CASE, "Lumberjack costume"),
    BomberJacketCostume(Items.BOMBER_JACKET_11135, intArrayOf(Items.BOMBER_JACKET_9944, Items.BOMBER_CAP_9945), type = StorableFamily.ARMOUR_CASE, "Bomber jacket costume"),
    HAMRobes(Items.HAM_SHIRT_11274, intArrayOf(Items.HAM_SHIRT_4298, Items.HAM_ROBE_4300, Items.HAM_HOOD_4302, Items.HAM_CLOAK_4304, Items.HAM_LOGO_4306, Items.BOOTS_4310, Items.GLOVES_4308), type = StorableFamily.ARMOUR_CASE, "H.A.M. robes"),
    VoidMelee(Items.VOID_MELEE_HELM_11676, intArrayOf(Items.VOID_MELEE_HELM_11665), type = StorableFamily.ARMOUR_CASE, "Void melee helm"),
    VoidRanger(Items.VOID_RANGER_HELM_11675, intArrayOf(Items.VOID_RANGER_HELM_11664), type = StorableFamily.ARMOUR_CASE, "Void ranger helm"),
    VoidMage(Items.VOID_MAGE_HELM_11674, intArrayOf(Items.VOID_MAGE_HELM_11663), type = StorableFamily.ARMOUR_CASE, "Void mage helm"),
    BlackNaval(Items.BLACK_NAVAL_SHIRT_13185, intArrayOf(Items.BLACK_NAVAL_SHIRT_8956, Items.BLACK_NAVY_SLACKS_8995, Items.BLACK_TRICORN_HAT_8963),type = StorableFamily.ARMOUR_CASE, "Black naval costume"),
    RedNaval(Items.RED_NAVAL_SHIRT_13183, intArrayOf(Items.RED_TRICORN_HAT_8961, Items.RED_NAVAL_SHIRT_8954, Items.RED_NAVY_SLACKS_8993), type = StorableFamily.ARMOUR_CASE, "Red naval costume"),
    BlueNaval(Items.BLUE_NAVAL_SHIRT_13181, intArrayOf(Items.BLUE_TRICORN_HAT_8959, Items.BLUE_NAVAL_SHIRT_8952, Items.BLUE_NAVY_SLACKS_8991), type = StorableFamily.ARMOUR_CASE, "Blue naval costume"),
    BrownNaval(Items.BROWN_NAVAL_SHIRT_13184, intArrayOf(Items.BROWN_TRICORN_HAT_8962, Items.BROWN_NAVAL_SHIRT_8955, Items.BROWN_NAVY_SLACKS_8994), type = StorableFamily.ARMOUR_CASE, "Brown naval costume"),
    GreenNaval(Items.GREEN_NAVAL_SHIRT_13182, intArrayOf(Items.GREEN_TRICORN_HAT_8960, Items.GREEN_NAVAL_SHIRT_8953, Items.GREEN_NAVY_SLACKS_8992), type = StorableFamily.ARMOUR_CASE, "Green naval costume"),
    GreyNaval(Items.GREY_NAVAL_SHIRT_13187, intArrayOf(Items.GREY_TRICORN_HAT_8965, Items.GREY_NAVAL_SHIRT_8958, Items.GREY_NAVY_SLACKS_8997), type = StorableFamily.ARMOUR_CASE, "Grey naval costume"),
    PurpleNaval(Items.PURPLE_NAVAL_SHIRT_13186, intArrayOf(Items.PURPLE_TRICORN_HAT_8964, Items.PURPLE_NAVAL_SHIRT_8957, Items.PURPLE_NAVY_SLACKS_8996), type = StorableFamily.ARMOUR_CASE, "Purple naval costume"),
    EliteBlackArmour(Items.ELITE_BLACK_PLATEBODY_14492, intArrayOf(Items.ELITE_BLACK_FULL_HELM_14494, Items.ELITE_BLACK_PLATEBODY_14492, Items.ELITE_BLACK_PLATELEGS_14490), type = StorableFamily.ARMOUR_CASE, "Elite Black Armour")
    ;

    companion object {
        /**
         * Map of all item ids to their [Storable].
         */
        private val idToStorable: Map<Int, Storable> by lazy {
            values().flatMap { storable ->
                storable.takeIds.map { id -> id to storable }
            }.toMap()
        }

        /**
         * Set of all item ids across all [Storable].
         */
        private val allItemIds: Set<Int> by lazy { idToStorable.keys }

        /**
         * Precomputed set of item ids for each Storable for fast lookup.
         */
        private val storableIdSets: Map<Storable, Set<Int>> by lazy {
            values().associateWith { it.takeIds.toSet() }
        }

        /**
         * Returns all item ids from all storables.
         */
        fun allIds(): Set<Int> = allItemIds

        /**
         * Finds the storable for the given item id.
         */
        fun fromId(id: Int): Storable? = idToStorable[id]

        /**
         * Checks if the player owns at least one item from
         * the storable in equipment or bank.
         */
        fun hasStorable(player: Player, storable: Storable): Boolean {
            val ownedIds = (player.equipment.toArray() + player.bank.toArray())
                .mapNotNull { it?.id }
                .toSet()
            return storableIdSets[storable]?.any { it in ownedIds } ?: false
        }

        /**
         * Checks if the player owns any item
         * from any storable in equipment or bank.
         */
        fun hasAnyStorable(player: Player): Boolean {
            val ownedIds = (player.equipment.toArray() + player.bank.toArray())
                .mapNotNull { it?.id }
                .toSet()
            return allItemIds.any { it in ownedIds }
        }

        /**
         * Checks if the player has at least one
         * item from the given storable equipped.
         */
        fun hasStorableEquipped(player: Player, storable: Storable): Boolean {
            val equippedIds = player.equipment.toArray().mapNotNull { it?.id }.toSet()
            return storableIdSets[storable]?.any { it in equippedIds } ?: false
        }

        /**
         * Checks if the player has at least one
         * item from the given storable in inventory.
         */
        fun hasStorableInInventory(player: Player, storable: Storable): Boolean {
            val inventoryIds = player.inventory.toArray().mapNotNull { it?.id }.toSet()
            return storableIdSets[storable]?.any { it in inventoryIds } ?: false
        }

        /**
         * Gets all [Storable]s that the player owns in their bank.
         */
        fun getStorablesInBank(player: Player): List<Storable> {
            val bankIds = player.bank.toArray().mapNotNull { it?.id }.toSet()
            return values().filter { storable ->
                storableIdSets[storable]?.any { it in bankIds } ?: false
            }
        }
    }
}

