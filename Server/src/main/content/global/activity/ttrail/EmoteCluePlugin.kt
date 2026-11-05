package content.global.activity.ttrail

import core.api.EquipmentSlot
import core.game.node.entity.player.link.emote.Emotes
import core.game.world.map.zone.ZoneBorders
import core.plugin.Plugin
import core.plugin.ClassScanner
import org.rs09.consts.Items

/**
 * Initializes the emote clue plugins.
 */

// TODO: This plugin should be converted to a listener as part of a larger rewrite of Treasure Trails.

class EmoteCluePlugin : EmoteClueScroll {

    /**
     * Constructs a new [EmoteCluePlugin] dummy instance for plugin loading.
     */
    constructor() : super(null, -1, null, null, null, null, null)

    /**
     * Constructs a new [EmoteCluePlugin] instance.
     * @param name the name.
     * @param clueId the clue id.
     * @param level the level.
     * @param emote the Emotes.
     * @param commenceEmote the commence Emotes.
     * @param equipment the equipment.
     * @param clue the clue.
     * @param borders the borders.
     */
    constructor(name: String?, clueId: Int, level: ClueLevel?, emote: Emotes?, commenceEmote: Emotes?, equipment: Array<IntArray>?, clue: String?, vararg borders: ZoneBorders) : super(name, clueId, level, emote, commenceEmote, equipment, clue, *borders)

    /**
     * Constructs a new [EmoteCluePlugin] instance without commence emote.
     */
    constructor(name: String?, clueId: Int, level: ClueLevel?, emote: Emotes?, equipment: Array<IntArray>?, clue: String?, vararg borders: ZoneBorders) : super(name, clueId, level, emote, null, equipment, clue, *borders)

    /**
     * Constructs a new [EmoteCluePlugin] requiring empty slots.
     */
    constructor(name: String?, clueId: Int, level: ClueLevel?, emote: Emotes?, equipment: Array<IntArray>?, emptySlots: IntArray?, clue: String?, vararg borders: ZoneBorders) : super(name, clueId, level, emote, null, equipment, emptySlots, clue, *borders)

    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        var emote = Emotes.BECKON
        register(EmoteCluePlugin("digsite-beckon", Items.CLUE_SCROLL_2677, ClueLevel.MEDIUM, emote, Emotes.BOW, arrayOf(intArrayOf(Items.SNAKESKIN_BOOTS_6328), intArrayOf(Items.IRON_PICKAXE_1267), intArrayOf(Items.HAT_658)), "Beckon in the Digsite, near the<br>eastern winch. Bow or curtsy<br>before you talk to me.<br>Equip a green gnome hat,<br>snakeskin boots and an<br>iron pickaxe.", ZoneBorders(3368, 3425, 3371, 3429)))
        register(EmoteCluePlugin("tai-bwo-beckon", Items.CLUE_SCROLL_2678, ClueLevel.MEDIUM, emote, Emotes.CLAP, arrayOf(intArrayOf(Items.GREEN_DHIDE_CHAPS_1099), intArrayOf(Items.RING_OF_DUELLING8_2552, Items.RING_OF_DUELLING7_2554, Items.RING_OF_DUELLING6_2556, Items.RING_OF_DUELLING5_2558, Items.RING_OF_DUELLING4_2560, Items.RING_OF_DUELLING3_2562, Items.RING_OF_DUELLING2_2564, Items.RING_OF_DUELLING1_2566), intArrayOf(Items.MITHRIL_MED_HELM_1143)), "Beckon in Tai Bwo<br>Wannai. Clap before<br>you talk to me.<br>Equip green<br>dragonhide chaps, a<br>ring of dueling and a<br>mithril medium helmet.", ZoneBorders(2771, 3053, 2814, 3074)))
        emote = Emotes.BLOW_KISS
        register(EmoteCluePlugin("shilo-kiss", Items.CLUE_SCROLL_3540, ClueLevel.HARD, emote, arrayOf(intArrayOf(Items.MYSTIC_HAT_4089), intArrayOf(Items.BONE_SPEAR_5016), intArrayOf(Items.RUNE_PLATEBODY_1127)), "Blow a kiss between<br>the tables in Shilo<br>Village bank. Beware<br>of double agents!<br>Equip a blue mystic<br>hat, bone spear and<br>rune plate body.", ZoneBorders(2851, 2954, 2853, 2952)))
        emote = Emotes.BOW
        register(EmoteCluePlugin("legends-guild", Items.CLUE_SCROLL_2682, ClueLevel.EASY, emote, arrayOf(intArrayOf(Items.IRON_PLATELEGS_1067), intArrayOf(Items.EMERALD_AMULET_1696), intArrayOf(Items.OAK_LONGBOW_845)), "Bow outside the entrance<br>to the Legends' Guild.<br>Equip iron platelegs, an<br>emerald amulet and an oak<br>longbow.", ZoneBorders(2726, 3346, 2731, 3349)))
        emote = Emotes.RASPBERRY
        register(EmoteCluePlugin("monkey-cage", Items.CLUE_SCROLL_2679, ClueLevel.EASY, emote, arrayOf(intArrayOf(Items.STUDDED_BODY_1133), intArrayOf(Items.BRONZE_PLATELEGS_1075), intArrayOf(Items.STAFF_1379)), "Blow a raspberry at the<br>monkey cage in<br>Ardougne Zoo.<br>Equip a studded<br>leather body, bronze<br>platelegs and a normal<br>staff with no orb.", ZoneBorders(2597, 3274, 2609, 3284)))
        register(EmoteCluePlugin("keep-le-faye", Items.CLUE_SCROLL_2680, ClueLevel.EASY, emote, arrayOf(intArrayOf(Items.COIF_1169), intArrayOf(Items.IRON_PLATEBODY_1115), intArrayOf(Items.LEATHER_GLOVES_1059)), "Blow raspberries out<br>the entrance to Keep Le<br>Faye.<br>Equip a coif, an iron<br>platebody and leather<br>gloves.", ZoneBorders(2759, 3401, 2762, 3403)))
        register(EmoteCluePlugin("fishing-guild-blow", Items.CLUE_SCROLL_2683, ClueLevel.HARD, emote, arrayOf(intArrayOf(Items.ELEMENTAL_SHIELD_2890), intArrayOf(Items.BLUE_DHIDE_CHAPS_2493), intArrayOf(Items.RUNE_WARHAMMER_1347)), "Blow a raspberry in the<br>Fishing Guild bank. Beware<br>of double agents!<br>Equip an elemental shield,<br>blue dragonhide chaps<br>and a rune warhammer.", ZoneBorders(2586, 3422, 2589, 3418), ZoneBorders(2588, 3419, 2592, 3413)))
        emote = Emotes.CHEER
        register(EmoteCluePlugin("druid-circle-cheer", Items.CLUE_SCROLL_2684, ClueLevel.EASY, emote, arrayOf(intArrayOf(Items.BOOTS_4310), intArrayOf(Items.WIZARD_HAT_579), intArrayOf(Items.BRONZE_2H_SWORD_1307)), "Cheer at the Druids'<br>Circle.<br>Equip a blue wizard<br>hat, a bronze<br>two-handed sword and<br>HAM boots.", ZoneBorders(2920, 3478, 2930, 3940)))
        register(EmoteCluePlugin("games-room-cheer", Items.CLUE_SCROLL_2685, ClueLevel.EASY, emote, emptyArray<IntArray>(), intArrayOf(EquipmentSlot.HEAD.ordinal, EquipmentSlot.CAPE.ordinal, EquipmentSlot.NECK.ordinal, EquipmentSlot.WEAPON.ordinal, EquipmentSlot.CHEST.ordinal, EquipmentSlot.SHIELD.ordinal, EquipmentSlot.LEGS.ordinal, EquipmentSlot.HANDS.ordinal, EquipmentSlot.FEET.ordinal, EquipmentSlot.RING.ordinal, EquipmentSlot.AMMO.ordinal), "Cheer at the games<br>room.<br>Have nothing equipped<br>at all when you do.", ZoneBorders(2194, 4946, 2221, 4973)))
        register(EmoteCluePlugin("barb-agility-cheer", Items.CLUE_SCROLL_2686, ClueLevel.MEDIUM, emote, arrayOf(intArrayOf(Items.STEEL_PLATEBODY_1119), intArrayOf(Items.MAPLE_SHORTBOW_853), WILDY_CAPES), "Cheer in the Barbarian<br>Agility Arena.<br>Headbang before you<br>talk to me.<br>Equip a steel plate<br>body, maple shortbow, and a wilderness<br>cape.", ZoneBorders(2550, 3556, 2553, 3559), ZoneBorders(2529, 3542, 2553, 3559)))
        register(EmoteCluePlugin("edge-gen-cheer", Items.CLUE_SCROLL_2687, ClueLevel.MEDIUM, emote, Emotes.DANCE, arrayOf(intArrayOf(Items.BROWN_APRON_1757), intArrayOf(Items.LEATHER_BOOTS_1061), intArrayOf(Items.LEATHER_GLOVES_1059)), "Cheer in the Edgeville general<br>store. Dance<br>before you talk to me.<br>Equip a brown apron,<br>leather boots and<br>leather gloves.", ZoneBorders(3076, 3507, 3084, 3513)))
        register(EmoteCluePlugin("ogre-pen-cheer", Items.CLUE_SCROLL_2688, ClueLevel.MEDIUM, emote, Emotes.ANGRY, arrayOf(intArrayOf(Items.GREEN_DHIDE_BODY_1135), intArrayOf(Items.GREEN_DHIDE_CHAPS_1099), intArrayOf(Items.STEEL_SQ_SHIELD_1177)), "Cheer in the Ogre Pen<br>in the Training Camp.<br>Show you are angry<br>before you talk to me.<br>Equip a green<br>dragonhide body and<br>chaps and a steel<br>square shield.", ZoneBorders(2523, 3373, 2533, 3377)))
        emote = Emotes.CLAP
        register(EmoteCluePlugin("exam-room-clap", Items.CLUE_SCROLL_2689, ClueLevel.EASY, emote, arrayOf(intArrayOf(Items.WHITE_APRON_1005), intArrayOf(Items.BOOTS_628), intArrayOf(Items.LEATHER_GLOVES_1059)), "Clap in the main exam room<br>in the Exam Centre.<br>Equip a white apron, green<br>gnome boots and leather<br>gloves.", ZoneBorders(3357, 3332, 3367, 3348)))
        register(EmoteCluePlugin("wizard-tower-clap", Items.CLUE_SCROLL_2690, ClueLevel.EASY, emote, arrayOf(intArrayOf(Items.IRON_MED_HELM_1137), intArrayOf(Items.EMERALD_RING_1639), intArrayOf(Items.WHITE_APRON_1005)), "Clap on the causeway to the<br>Wizards' Tower.<br>Equip an iron medium<br>helmet, emerald ring and a<br>white apron.", ZoneBorders(3112, 3173, 3115, 3206)))
        register(EmoteCluePlugin("ardgoune-mill-clap", Items.CLUE_SCROLL_2691, ClueLevel.EASY, emote, arrayOf(intArrayOf(Items.ROBE_TOP_640), intArrayOf(Items.HAM_ROBE_4300), intArrayOf(Items.TIARA_5525)), "Clap on the top level of the<br>mill, north of East Ardougne.<br>Equip a blue gnome robe top,<br>HAM robe bottom and an<br>unenchanted tiara.", ZoneBorders(2628, 3382, 2636, 3390, 2)))
        register(EmoteCluePlugin("seers-court-clap", Items.CLUE_SCROLL_2692, ClueLevel.MEDIUM, emote, Emotes.SPIN, arrayOf(intArrayOf(Items.ADAMANT_HALBERD_3200), intArrayOf(Items.MYSTIC_ROBE_BOTTOM_4093), intArrayOf(Items.DIAMOND_RING_1643)), "Clap in Seers court house.<br>Spin before you talk to me.<br>Equip an adamant halberd,<br>blue mystic robe bottom and<br>a diamond ring.", ZoneBorders(2732, 3467, 2739, 3471)))
        // emote = Emotes.CRY;
        register(EmoteCluePlugin("catherby-range-cry", Items.CLUE_SCROLL_2693, ClueLevel.MEDIUM, Emotes.CRY, Emotes.BOW, arrayOf(intArrayOf(Items.BOOTS_630), intArrayOf(Items.HARDLEATHER_BODY_1131), intArrayOf(Items.SILVER_SICKLE_2961)), "Cry in the Catherby<br>Ranging shop. Bow before you talk to me.<br>Equip blue gnome<br>boots, a hard leather<br>body and an<br>unblessed silver<br>sickle.", ZoneBorders(2821, 3441, 2825, 3445)))
        register(EmoteCluePlugin("catherby-shore-cry", Items.CLUE_SCROLL_2694, ClueLevel.MEDIUM, Emotes.CRY, Emotes.LAUGH, arrayOf(intArrayOf(Items.ADAMANT_SQ_SHIELD_1183), intArrayOf(Items.BONE_DAGGER_8872), intArrayOf(Items.MITHRIL_PLATEBODY_1121)), "Cry on the shore of<br>Catherby beach.<br>Laugh before you talk to me.<br>Equip an adamant sq<br>shield, a bone dagger<br>and mithril platebody.", ZoneBorders(2849, 3423, 2857, 3430), ZoneBorders(2845, 3428, 2852, 3430)))
        emote = Emotes.DANCE
        register(EmoteCluePlugin("draynor-cross-dance", Items.CLUE_SCROLL_2695, ClueLevel.EASY, emote, arrayOf(intArrayOf(Items.IRON_CHAINBODY_1101), intArrayOf(Items.SAPPHIRE_RING_1637), intArrayOf(Items.LONGBOW_839)), "Dance at the<br>crossroads north of<br>Draynor.<br>Equip an iron chain<br>body, a sapphire ring<br>and a longbow.", ZoneBorders(3109, 3293, 3110, 3296), ZoneBorders(3108, 3294, 3111, 3295)))
        register(EmoteCluePlugin("fally-party-dance", Items.CLUE_SCROLL_2696, ClueLevel.EASY, emote, arrayOf(intArrayOf(Items.STEEL_FULL_HELM_1157), intArrayOf(Items.STEEL_PLATEBODY_1119), intArrayOf(Items.IRON_PLATESKIRT_1081)), "Dance in the Party<br>Room.<br>Equip a steel full<br>helmet, steel<br>platebody and an iron<br>plateskirt.", ZoneBorders(3041, 3372, 3050, 3384), ZoneBorders(3051, 3371, 3054, 3385), ZoneBorders(3073, 3371, 3040, 3385)))
        register(EmoteCluePlugin("fish-guild-jig", Items.CLUE_SCROLL_2697, ClueLevel.EASY, Emotes.JIG, arrayOf(intArrayOf(Items.EMERALD_RING_1639), intArrayOf(Items.SAPPHIRE_AMULET_1694), intArrayOf(Items.BRONZE_CHAINBODY_1103)), "Dance a jig by the<br>entrance to the<br>Fishing Guild.<br>Equip an emerald ring,<br>a sapphire amulet,<br>and a bronze chain<br>body.", ZoneBorders(2610, 3392, 2612, 3393)))
        register(EmoteCluePlugin("lumb-shack-dance", Items.CLUE_SCROLL_2698, ClueLevel.EASY, emote, arrayOf(intArrayOf(Items.BRONZE_DAGGER_1205), intArrayOf(Items.IRON_FULL_HELM_1153), intArrayOf(Items.GOLD_RING_1635)), "Dance in<br>the shack in Lumbridge Swamp.<br>Equip a bronze<br>dagger, iron full helmet<br>and a gold ring.", ZoneBorders(3202, 3167, 3205, 3170)))
        register(EmoteCluePlugin("lumb-cave-dance", Items.CLUE_SCROLL_2699, ClueLevel.MEDIUM, emote, Emotes.BLOW_KISS, arrayOf(intArrayOf(Items.STAFF_OF_AIR_1381), intArrayOf(Items.BRONZE_FULL_HELM_1155), intArrayOf(Items.AMULET_OF_POWER_1731)), "Dance in the dark<br>cave beneath<br>Lumbridge Swamp.<br>Blow a kiss before<br>you talk to me.<br>Equip an air staff,<br>bronze full helm and<br>an amulet of power.", ZoneBorders(3139, 9534, 3260, 9587)))
        register(EmoteCluePlugin("shantay-guild-jig", Items.CLUE_SCROLL_2700, ClueLevel.MEDIUM, Emotes.JIG, Emotes.BOW, arrayOf(intArrayOf(Items.BRUISE_BLUE_SNELM_3343), intArrayOf(Items.STAFF_OF_AIR_1381), intArrayOf(Items.BRONZE_SQ_SHIELD_1173)), "Dance a jig under<br>Shantay's Awning.<br>Bow before you talk to<br>me.<br>Equip a pointed blue<br>snail helmet, an air<br>staff and a bronze<br>square shield.", ZoneBorders(3302, 3122, 3305, 3125)))
        register(EmoteCluePlugin("cat-entrance-dance", Items.CLUE_SCROLL_2701, ClueLevel.HARD, emote, arrayOf(intArrayOf(Items.RING_OF_LIFE_2570), intArrayOf(Items.AMULET_OF_GLORY_1704), intArrayOf(Items.ADAMANT_2H_SWORD_1317)), "Dance at the<br>cat doored pyramid in<br>Sophanem. Beware of double agents!<br>Equip a ring of life<br>an uncharged amulet of glory<br>and an adamant two handed sword.", ZoneBorders(3293, 2781, 3296, 2782)))
        emote = Emotes.HEADBANG
        register(EmoteCluePlugin("al-kharid-headbang", Items.CLUE_SCROLL_2702, ClueLevel.EASY, emote, arrayOf(intArrayOf(Items.DESERT_SHIRT_1833), intArrayOf(Items.LEATHER_GLOVES_1059), intArrayOf(Items.LEATHER_BOOTS_1061)), "Headbang in the mine north of Al<br>Kharid.<br>Equip a desert shirt, leather gloves and<br>leather boots.", ZoneBorders(3297, 3286, 3301, 3316)))
        // emote = Emotes.JUMP_FOR_JOY;
        register(EmoteCluePlugin("beehive-jump", Items.CLUE_SCROLL_2703, ClueLevel.EASY, Emotes.JUMP_FOR_JOY, arrayOf(intArrayOf(Items.DESERT_SHIRT_1833), intArrayOf(Items.ROBE_BOTTOMS_648), intArrayOf(Items.STEEL_AXE_1353)), "Jump for joy at the beehives.<br>Equip a desert shirt, green<br>gnome robe bottoms and a<br>steel axe.", ZoneBorders(2752, 3437, 2766, 3450)))
        register(EmoteCluePlugin("yanille-jump", Items.CLUE_SCROLL_2704, ClueLevel.MEDIUM, Emotes.JUMP_FOR_JOY, Emotes.JIG, arrayOf(intArrayOf(Items.BROWN_APRON_1757), intArrayOf(Items.ADAMANT_MED_HELM_1145), intArrayOf(Items.SNAKESKIN_CHAPS_6324)), "Jump for joy in Yanille<br>bank. Dance a jig before you<br>talk to me.<br>Equip a brown apron,<br>adamantite medium helmet<br>and snakeskin chaps.", ZoneBorders(2609, 3088, 2614, 3097)))
        register(EmoteCluePlugin("tzhaar-jump", Items.CLUE_SCROLL_2705, ClueLevel.MEDIUM, Emotes.JUMP_FOR_JOY, Emotes.SHRUG, arrayOf(intArrayOf(Items.STEEL_LONGSWORD_1295), intArrayOf(Items.BLUE_DHIDE_BODY_2499), intArrayOf(Items.MYSTIC_GLOVES_4095)), "Jump for joy in the TzHaar<br>sword shop. Shrug before you<br>talk to me.<br>Equip a Steel longsword,<br>Blue D'hide body and blue<br>mystic gloves.", ZoneBorders(2477, 5144, 2480, 5147)))
        // emote = Emotes.LAUGH
        register(EmoteCluePlugin("jokul-tent-laugh", Items.CLUE_SCROLL_2706, ClueLevel.HARD, Emotes.LAUGH, arrayOf(intArrayOf(Items.RUNE_FULL_HELM_1163), intArrayOf(Items.BLUE_DHIDE_CHAPS_2493), intArrayOf(Items.FIRE_BATTLESTAFF_1393)), "Laugh in the Jokul's tent in the<br>Mountain Camp.<br>Beware of double agents! Equip a<br>rune full helmet, blue dragonhide<br>chaps and a fire battlestaff.", ZoneBorders(2811, 3678, 2813, 3682)))
        emote = Emotes.PANIC
        register(EmoteCluePlugin("limestone-mine-panic", Items.CLUE_SCROLL_2707, ClueLevel.EASY, emote, arrayOf(intArrayOf(Items.BRONZE_PLATELEGS_1075), intArrayOf(Items.STEEL_PICKAXE_1269), intArrayOf(Items.STEEL_MED_HELM_1141)), "Panic in the<br>Limestone Mine.<br>Equip bronze<br>platelegs, a steel<br>pickaxe and a steel<br>medium helmet.", ZoneBorders(3368, 3496, 3374, 3505)))
        register(EmoteCluePlugin("fish-trawler-panic", Items.CLUE_SCROLL_2708, ClueLevel.EASY, emote, emptyArray<IntArray>(), intArrayOf(EquipmentSlot.HEAD.ordinal, EquipmentSlot.CAPE.ordinal, EquipmentSlot.NECK.ordinal, EquipmentSlot.WEAPON.ordinal, EquipmentSlot.CHEST.ordinal, EquipmentSlot.SHIELD.ordinal, EquipmentSlot.LEGS.ordinal, EquipmentSlot.HANDS.ordinal, EquipmentSlot.FEET.ordinal, EquipmentSlot.RING.ordinal, EquipmentSlot.AMMO.ordinal), "Panic on the pier<br>where you catch the<br>Fishing trawler.<br>Have nothing equipped<br>at all when you do.", ZoneBorders(2675, 3162, 2677, 3175)))
        emote = Emotes.SALUTE
        register(EmoteCluePlugin("banana-salute", Items.CLUE_SCROLL_2710, ClueLevel.HARD, emote, arrayOf(intArrayOf(Items.DIAMOND_RING_1643), intArrayOf(Items.AMULET_OF_POWER_1731)), intArrayOf(EquipmentSlot.CHEST.ordinal, EquipmentSlot.LEGS.ordinal), "Salute in the banana<br>plantation. Beware of<br>double agents!<br>Equip a diamond ring,<br>amulet of power, and nothing on<br>your chest or legs.", ZoneBorders(2912, 3174, 2934, 3154), ZoneBorders(2934, 3153, 2932, 3152), ZoneBorders(2935, 3612, 2936, 3159), ZoneBorders(2911, 3172, 2910, 3156), ZoneBorders(2909, 3156, 2908, 3169), ZoneBorders(2907, 3167, 2907, 3158), ZoneBorders(2906, 3159, 2905, 3164)))
        emote = Emotes.SHRUG
        register(EmoteCluePlugin("rimmington-shrug", Items.CLUE_SCROLL_2711, ClueLevel.EASY, emote, arrayOf(intArrayOf(Items.GOLD_NECKLACE_1654), intArrayOf(Items.GOLD_RING_1635), intArrayOf(Items.BRONZE_SPEAR_1237)), "Shrug in the mine near<br>Rimmington.<br>Equip a gold<br>necklace, a gold ring<br>and a bronze spear.", ZoneBorders(2970, 3230, 2987, 3251)))
        register(EmoteCluePlugin("catherby-shrug", Items.CLUE_SCROLL_2712, ClueLevel.MEDIUM, emote, Emotes.YAWN, arrayOf(intArrayOf(Items.MAPLE_LONGBOW_851), intArrayOf(Items.GREEN_DHIDE_CHAPS_1099), intArrayOf(Items.IRON_MED_HELM_1137)), "Shrug in Catherby<br>bank. Yawn before<br>you talk to me.<br>Equip a maple<br>longbow, green d'hide<br>chaps and an iron<br>med helm.", ZoneBorders(2806, 3438, 2812, 3442)))
        register(EmoteCluePlugin("zammy-altar-shrug", Items.CLUE_SCROLL_2713, ClueLevel.HARD, emote, arrayOf(intArrayOf(Items.RUNE_PLATELEGS_1079), intArrayOf(Items.IRON_PLATEBODY_1115), intArrayOf(Items.BLUE_DHIDE_VAMB_2487)), "Shrug in the Zamorak temple found<br>in the Eastern Wilderness.<br>Beware of double agents!<br>Equip rune plate legs, an iron<br>platebody and blue dragonhide<br>vambraces.", ZoneBorders(3237, 3614, 3242, 3603), ZoneBorders(3234, 3611, 3245, 3606), ZoneBorders(3235, 3612, 3243, 3604), ZoneBorders(3236, 3613, 3244, 3605), ZoneBorders(3239, 3616, 3240, 3615)))
        emote = Emotes.SPIN
        register(EmoteCluePlugin("rimmington-cross-spin", Items.CLUE_SCROLL_2716, ClueLevel.EASY, emote, arrayOf(intArrayOf(Items.HAT_658), intArrayOf(Items.ROBE_TOP_642), intArrayOf(Items.LEATHER_CHAPS_1095)), "Spin at the crossroads<br>north of Rimmington.<br>Equip a green gnome<br>hat, cream gnome top<br>and leather chaps.", ZoneBorders(2979, 3275, 2985, 3279)))
        register(EmoteCluePlugin("draynor-manor-spin", Items.CLUE_SCROLL_2719, ClueLevel.EASY, emote, arrayOf(intArrayOf(Items.IRON_PLATEBODY_1115), intArrayOf(Items.STUDDED_CHAPS_1097), intArrayOf(Items.BRONZE_FULL_HELM_1155)), "Spin in the Draynor<br>Manor by the fountain.<br>Equip an iron<br>platebody, studded<br>leather chaps and a<br>bronze full helmet.", ZoneBorders(3085, 3332, 3090, 3337)))
        register(EmoteCluePlugin("varrock-court-spin", Items.CLUE_SCROLL_2722, ClueLevel.EASY, emote, arrayOf(intArrayOf(Items.BLACK_AXE_1361), intArrayOf(Items.COIF_1169), intArrayOf(Items.RUBY_RING_1641)), "Spin in the Varrock<br>Castle courtyard.<br>Equip a black axe, a<br>coif and a ruby ring.", ZoneBorders(3202, 3459, 3223, 3468)))
        register(EmoteCluePlugin("barb-bridge-spin", Items.CLUE_SCROLL_2723, ClueLevel.MEDIUM, emote, Emotes.SALUTE, arrayOf(intArrayOf(Items.GLOVES_2942), intArrayOf(Items.STEEL_KITESHIELD_1193), intArrayOf(Items.MITHRIL_FULL_HELM_1159)), "Spin on the bridge by<br>the Barbarian Village.<br>Salute before you talk<br>to me.<br>Equip purple gloves, a<br>steel kiteshield and a<br>mithril full helmet.", ZoneBorders(3102, 3419, 3108, 3422)))
        emote = Emotes.THINK
        register(EmoteCluePlugin("lumb-wheat-think", Items.CLUE_SCROLL_2725, ClueLevel.EASY, emote, arrayOf(intArrayOf(Items.ROBE_TOP_640), intArrayOf(Items.ROBE_BOTTOMS_654), intArrayOf(Items.OAK_SHORTBOW_843)), "Think in middle of the<br>wheat field by the<br>lumbridge mill.<br>Equip a blue gnome<br> robetop, a turquoise<br> gnome robe bottom<br>and an oak shortbow.", ZoneBorders(3157, 3298, 3159, 3300)))
        register(EmoteCluePlugin("observatory-think", Items.CLUE_SCROLL_2727, ClueLevel.MEDIUM, emote, Emotes.SPIN, arrayOf(intArrayOf(Items.MITHRIL_CHAINBODY_1109), intArrayOf(Items.GREEN_DHIDE_CHAPS_1099), intArrayOf(Items.RUBY_AMULET_1698)), "Think in the centre of<br>the Observatory. Spin<br>before you talk to me.<br>Equip a mithril chain<br>body, green<br>dragonhide chaps and<br>a ruby amulet.", ZoneBorders(2439, 3160, 2442, 3163)))
        emote = Emotes.WAVE
        register(EmoteCluePlugin("lumber-wave", Items.CLUE_SCROLL_2729, ClueLevel.EASY, emote, arrayOf(intArrayOf(Items.HARDLEATHER_BODY_1131), intArrayOf(Items.LEATHER_CHAPS_1095), intArrayOf(Items.BRONZE_AXE_1351)), "Wave along the south fence of<br>the Lumber Yard.<br>Equip a hard leather body,<br>leather chaps and a<br>bronze axe.", ZoneBorders(3306, 3490, 3309, 3492)))
        register(EmoteCluePlugin("fally-gem-wave", Items.CLUE_SCROLL_2731, ClueLevel.EASY, emote, arrayOf(intArrayOf(Items.MITHRIL_PICKAXE_1273), intArrayOf(Items.BLACK_PLATEBODY_1125), intArrayOf(Items.IRON_KITESHIELD_1191)), "Wave in the Falador<br>gem store.<br>Equip a Mithril pickaxe,<br>Black platebody and<br>an Iron Kiteshield.", ZoneBorders(2944, 3332, 2946, 3337)))
        register(EmoteCluePlugin("mudskipper-wave", Items.CLUE_SCROLL_2733, ClueLevel.EASY, emote, arrayOf(intArrayOf(Items.CAPE_1019), intArrayOf(Items.LEATHER_CHAPS_1095), intArrayOf(Items.STEEL_MACE_1424)), "Wave on Mudskipper Point.<br>Equip a black cape, leather<br>chaps and a steel mace.", ZoneBorders(2979, 3105, 3008, 3115)))
        emote = Emotes.YAWN
        register(EmoteCluePlugin("varrock-library-yawn", Items.CLUE_SCROLL_2735, ClueLevel.EASY, emote, arrayOf(intArrayOf(Items.ROBE_TOP_638), intArrayOf(Items.HAM_ROBE_4300), intArrayOf(Items.IRON_WARHAMMER_1335)), "Yawn in the Varrock<br>library.<br>Equip a green gnome<br>robe top, HAM robe<br>bottom and an iron<br>warhammer.", ZoneBorders(3207, 3490, 3214, 3497), ZoneBorders(3214, 3494, 3217, 3497)))
        register(EmoteCluePlugin("draynor-market-yawn", Items.CLUE_SCROLL_2737, ClueLevel.EASY, emote, arrayOf(intArrayOf(Items.STUDDED_CHAPS_1097), intArrayOf(Items.IRON_KITESHIELD_1191), intArrayOf(Items.STEEL_LONGSWORD_1295)), "Yawn in Draynor<br>Marketplace.<br>Equip studded leather<br>chaps, an iron<br>kiteshield and a steel<br>longsword.", ZoneBorders(3075, 3245, 3086, 3255)))
        register(EmoteCluePlugin("castle-wars-yawn", Items.CLUE_SCROLL_2739, ClueLevel.MEDIUM, emote, Emotes.SHRUG, arrayOf(intArrayOf(Items.RUBY_AMULET_1698), intArrayOf(Items.MITHRIL_SCIMITAR_1329), WILDY_CAPES), "Yawn in the Castle<br>Wars lobby. Shrug<br>before you talk to me.<br>Equip ruby amulet, a<br>mithril scimitar and a<br>Wilderness cape.", ZoneBorders(2434, 3061, 2464, 3102)))
        register(EmoteCluePlugin("rogue-gen-yawn", Items.CLUE_SCROLL_2741, ClueLevel.HARD, emote, arrayOf(intArrayOf(Items.ADAMANT_SQ_SHIELD_1183), intArrayOf(Items.BLUE_DHIDE_VAMB_2487), intArrayOf(Items.RUNE_PICKAXE_1275)), "Yawn in the rogues'<br>general store. Beware<br>of double agents!<br>Equip an adamant<br>square shield, blue<br>dragon vambraces<br>and a rune pickaxe.", ZoneBorders(3024, 3699, 3027, 3704)))

        ClassScanner.definePlugin(UriNPC())

        @Suppress("UNCHECKED_CAST")
        return this as Plugin<Any>
    }

    override fun fireEvent(identifier: String, vararg args: Any?): Any? {
        return null
    }
}