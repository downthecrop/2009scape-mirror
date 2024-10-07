package core.game.global.action;

import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.diary.DiaryType;
import core.game.world.map.Location;

import java.util.Arrays;
import java.util.HashMap;

public enum SpecialLadders implements LadderAchievementCheck {
    HAM_STORAGE_UP(new Location(2567,5185,0), new Location(3166,9623,0)),
    GEM_MINE(new Location(2821,2996,0), new Location(2838,9387,0)),
    GEM_MINE_UP(new Location(2838,9388,0), new Location(2820, 2996, 0)),
    BEAR_CAGE_UP(Location.create(3230, 9904, 0),Location.create(3231, 3504, 0)),
    BEAR_CAGE_DOWN(Location.create(3230,3508,0),Location.create(3229, 9904, 0)),
    GLARIAL_EXIT(Location.create(2556,9844,0),Location.create(2557,3444,0)),
    SWENSEN_DOWN(Location.create(2644, 3657, 0), Location.create(2631, 10006, 0)),
    SWENSEN_UP(Location.create(2665, 10037, 0),Location.create(2649, 3661, 0)),
    FOG_ENTER(Location.create(3240,3575,0),Location.create(1675,5599,0)),
    FOG_LEAVE(Location.create(1673,5598,0),Location.create(3242, 3574, 0)),
    INTRO_ENTER(Location.create(3230,3241,0),Location.create(3290, 4936, 0)),
    INTRO_LEAVE(Location.create(3290, 4935, 0),Location.create(3230, 3240, 0)),
    JATIZSO_MINE_UP(Location.create(2406,10188,0),Location.create(2397, 3811, 0)),
    JATIZSO_MINE_DOWN(Location.create(2397, 3812, 0), Location.create(2405, 10188, 0)),
    JATIZSO_SHOUT_TOWER_UP(Location.create(2373, 3800, 2),Location.create(2374, 3800, 0)),
    JATIZSO_SHOUT_TOWER_DOWN(Location.create(2373, 3800, 0),Location.create(2374, 3800, 2)),

    // sendMessage(player, "You descend into the somewhat smoky depths of the well, to the accompaniment of")
    // sendMessage(player, "eery wails.") https://youtu.be/x8abdpkJ6ZA
    POLLNIVNEACH_SLAYER_DUNGEON_UP(Location.create(3358,2971,0), Location.create(3359,9354,0)),
    // sendMessage(player, "You nimbly climb up the bucket rope, emerging into Pollnivneach's bustling square.") https://youtu.be/LVwbmCNjlzQ
    POLLNIVNEACH_SLAYER_DUNGEON_DOWN(Location.create(3358,9352,0), Location.create(3358,2970,0)),
    ALKHARID_ZEKE_UP(Location.create(3284,3186,0), Location.create(3284,3190,1)),
    ALKHARID_ZEKE_DOWN(Location.create(3284,3190,1), Location.create(3284,3186,0)),
    ALKHARID_CRAFTING_UP(Location.create(3311,3187,0),Location.create(3314,3187,1)),
    ALKHARID_CRAFTING_DOWN(Location.create(3314,3187,1),Location.create(3310,3187,0)),
    ALKHARID_SOCRCERESS_UP(Location.create(3325,3142,0),Location.create(3325,3139,1)),
    ALKHARID_SOCRCERESS_DOWN(Location.create(3325,3139,1),Location.create(3325,3143,0)),

    CLOCKTOWER_HIDDEN_LADDER(Location.create(2572,9631,0),Location.create(2572,3230,0)),

    DRAYNOR_SEWER_SOUTHEAST_DOWN(new Location(3118, 3244, 0), new Location(3118, 9643, 0)),
    DRAYNOR_SEWER_SOUTHEAST_UP(new Location(3118, 9643, 0), new Location(3118, 3243, 0)),
    DRAYNOR_SEWER_NORTHWEST_DOWN(new Location(3084, 3272, 0), new Location(3085, 9672, 0)),
    DRAYNOR_SEWER_NORTHWEST_UP(new Location(3084, 9672, 0), new Location(3084, 3271, 0)),

    BURTHORPE_HEROES_GUILD_STAIRS_UP(new Location(2895,3513,0), new Location(2897, 3513,1)),

    TREE_GNOME_STRONGHOLD_WEST_BAR_STAIRS_UP( new Location(2417, 3490, 0), new Location(2418, 3492,1)),
    TREE_GNOME_STRONGHOLD_WEST_BAR_STAIRS_DOWN(new Location(2418, 3491,1), new Location(2419, 3491, 0)),

    WATERBIRTH_ISLAND_DUNGEON_SUBLEVEL_2_WALLASALKI_3_LADDER_DOWN(new Location(1799,4387,2), new Location(1799,4388,1)),

    FALADOR_WHITE_KNIGHT_CASTLE_WEST_TOWER_STAIRS_UP(new Location(2960, 3338,1), new Location(2959,3339,2)),
    FALADOR_WHITE_KNIGHT_CASTLE_WEST_TOWER_STAIRS_DOWN(new Location(2960,3339,2), new Location(2960,3340,1)),

    CASTLEWARS_SARADOMIN_MAIN_FLOOR_STAIRS_DOWN(Location.create(2419, 3080, 1), Location.create(2419, 3077, 0)),
    CASTLEWARS_SARADOMIN_MAIN_FLOOR_STAIRS_UP(Location.create(2428, 3081, 1), Location.create(2430, 3080, 2)),
    CASTLEWARS_SARADOMIN_OUTER_WALL_STAIRS_UP(Location.create(2417, 3077, 0), Location.create(2416, 3075, 0)),
    CASTLEWARS_SARADOMIN_OUTER_WALL_STAIRS_DOWN(Location.create(2417, 3075, 0), Location.create(2417, 3078, 0)),

    CASTLEWARS_ZAMORAK_TOP_FLOOR_DOWN(Location.create(2374, 3133, 3), Location.create(2374, 3130, 2)),
    CASTLEWARS_ZAMORAK_MAIN_FLOOR_STAIRS_UP(Location.create(2380, 3129, 0), Location.create(2379, 3127, 1)),
    CASTLEWARS_ZAMORAK_OUTERWALL_STAIRS_UP(Location.create(2382, 3130, 0), Location.create(2383, 3132, 0)),
    CASTLEWARS_ZAMOUTER_WALL_STAIRS_DOWN(Location.create(2382, 3132, 0), Location.create(2382, 3129, 0)),

    WHITE_WOLF_MOUNTAIN_FAKE_LADDER_1_UP(Location.create(2837, 9927, 0), Location.create(2837, 3527, 0)),
    WHITE_WOLF_MOUNTAIN_FAKE_LADDER_2_UP(Location.create(2823, 9930, 0), Location.create(2823, 3529, 0)),

    PORT_SARIM_RAT_PITS_DOWN(new Location(3018,3232,0), new Location(2962,9650,0)) {
        @Override
        public void checkAchievement(Player player) {
            player.getAchievementDiaryManager().finishTask(player,DiaryType.FALADOR,1,11);
        }
    },
    PORT_SARIM_RAT_PITS_UP(new Location(2962,9651,0), new Location(3018,3231,0)),
    ENTRANA_GLASSBLOWING_PIPE_HOUSE_DOWN(new Location(2816, 3352, 1), new Location(2817, 3352, 0)),
    PATERDOMUS_TEMPLE_STAIRCASE_NORTH_UP(new Location(3415, 3490, 0), new Location(3415, 3491, 1)),
    PATERDOMUS_TEMPLE_STAIRCASE_NORTH_DOWN(new Location(3415, 3491, 1), new Location(3414,3491,0)),
    PATERDOMUS_TEMPLE_STAIRCASE_SOUTH_DOWN(new Location(3415, 3486,1), new Location(3414, 3486,0)),
    PHASMATYS_BAR_DOWN(new Location(3681,3498,0), new Location(3682,9961,0)),
    PHASMATYS_BAR_UP(new Location(3682,9962,0), new Location(3681,3497,0)),
    SEERS_VILLAGE_SPINNING_HOUSE_ROOFTOP_UP(new Location(2715,3472,1), new Location(2714,3472,3)) {
        @Override
        public void checkAchievement(Player player) {
            player.getAchievementDiaryManager().finishTask(player,DiaryType.SEERS_VILLAGE,1,3);
        }
    },
    SEERS_VILLAGE_SPINNING_HOUSE_ROOFTOP_DOWN(new Location(2715,3472,3), new Location(2714,3472,1));

    private static HashMap<Location,Location> destinationMap = new HashMap<>();
    private static HashMap<Location,SpecialLadders> ladderMap = new HashMap<>();
    static {
        Arrays.stream(SpecialLadders.values()).forEach(entry -> {
            destinationMap.putIfAbsent(entry.ladderLoc,entry.destLoc);
        });
        Arrays.stream(SpecialLadders.values()).forEach(entry -> {
            ladderMap.putIfAbsent(entry.ladderLoc, entry);
        });
    }

    private Location ladderLoc,destLoc;
    SpecialLadders(Location ladderLoc, Location destLoc){
        this.ladderLoc = ladderLoc;
        this.destLoc = destLoc;
    }

    public static void add(Location from, Location to){
        destinationMap.put(from,to);
    }

    public static Location getDestination(Location loc){
        return destinationMap.get(loc);
    }
    public static SpecialLadders getSpecialLadder(Location loc) {
        return ladderMap.get(loc);
    }

}