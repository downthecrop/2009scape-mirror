package rs09.game.node.entity.player.link.diary.events

import api.events.NPCKillEvent
import api.events.PickUpEvent
import api.events.ResourceProducedEvent
import api.inBorders
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.world.map.zone.ZoneBorders
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import rs09.game.node.entity.player.link.diary.DiaryEventHookBase
import rs09.game.node.entity.player.link.diary.DiaryLevel

class KaramjaAchievementDiary : DiaryEventHookBase(DiaryType.KARAMJA) {
    companion object {
        private const val ATTRIBUTE_SEAWEED_PICKED = "diary:karamja:seaweed-picked"
        private const val ATTRIBUTE_PALM_LEAF_PICKED = "diary:karamja:palm-leaf-picked"

        private val MAIN_ISLAND_AREA = ZoneBorders(2749, 2886, 2972, 3132)
        private val BANANA_FISHING_SPOT_AREA = ZoneBorders(2923, 3173, 2928, 3182)
        private val TAI_BWO_WANNAI_WOODCUTTING_AREA = ZoneBorders(2817, 3076, 2829, 3090)

        private val KET_ZEKS = arrayOf(
            NPCs.KET_ZEK_2743,
            NPCs.KET_ZEK_2744
        )

        private val METAL_DRAGONS = arrayOf(
            NPCs.BRONZE_DRAGON_1590,
            NPCs.IRON_DRAGON_1591,
            NPCs.STEEL_DRAGON_1592
        )

        object EasyTasks {
            const val PICK_5_BANANAS = 0
            const val TRAVEL_TO_MOSS_GIANTS_VIA_ROPESWING = 1
            const val BRIMHAVEN_MINE_GOLD = 2
            const val MUSA_POINT_CHARTER_SHIP_TO_PORT_SARIM = 3
            const val BRIMHAVEN_CHARTER_SHIP_TO_ARDOUGNE = 4
            const val CAIRN_ISLE_VISIT = 5
            const val USE_FISHING_SPOTS_BANANA_PLANTATION = 6
            const val PICK_5_SEAWEED = 7
            const val ATTEMPT_TZHAAR_FIGHT_PITS_OR_FIGHT_CAVE = 8
            const val POTHOLE_DUNGEON_KILL_JOGRE = 9
        }

        object MediumTasks {
            const val BRIMHAVEN_CLAIM_AGILITY_ARENA_TICKET = 0
            const val FIND_HIDDEN_WALL_BELOW_VOLCANO = 1
            const val CRANDOR_ISLAND_VISIT = 2
            const val USE_VIGROY_HAJEDY_CARTS = 3
            const val TAI_BWO_WANNAI_EARN_FULL_FAVOR = 4
            const val COOK_SPIDER_ON_STICK = 5
            const val CAIRN_ISLE_CHARTER_LADY_OF_THE_WAVES = 6
            const val CUT_TEAK_TREE = 7
            const val CUT_MAHOGANY_TREE = 8
            const val CATCH_KARAMBWAN = 9
            const val EXCHANGE_GEMS_TUBER_TRADING_STICKS_FOR_MACHETE = 10
            const val GNOME_GLIDE_TO_KARAMJA = 11
            const val BRIMHAVEN_GROW_HEALTHY_FRUIT_TREE = 12
            const val TRAP_HORNED_GRAAHK = 13
            const val BRIMHAVEN_DUNGEON_CHOP_VINES = 14
            const val BRIMHAVEN_DUNGEON_CROSS_LAVA_STEPPING_STONES = 15
            const val CHARTER_SHIP_EAST = 16
            const val MINE_RED_TOPAZ = 17
        }

        object HardTasks {
            const val FIGHT_PITS_BECOME_CHAMPION = 0
            const val FIGHT_CAVE_KILL_KET_ZEK = 1
            const val EAT_OOMLIE_WRAP = 2
            const val CRAFT_NATURE_RUNES = 3
            const val COOK_KARAMBWAN_PROPERLY = 4
            const val KHARAZI_JUNGLE_KILL_DEATHWING = 5
            const val VOLCANO_USE_CROSSBOW_SHORTCUT_SOUTH = 6
            const val PICK_5_PALM_LEAVES = 7
            const val DURADEL_LAPALOK_GET_SLAYER_TASK = 8
            const val BRIMHAVEN_DUNGEON_KILL_METAL_DRAGON = 9
        }
    }

    override fun onResourceProduced(player: Player, event: ResourceProducedEvent) {
        when (player.viewport.region.id) {
            11310, 11410 -> if (event.itemId == Items.UNCUT_RED_TOPAZ_1629) {
                finishTask(
                    player,
                    DiaryLevel.MEDIUM,
                    MediumTasks.MINE_RED_TOPAZ
                )
            }

            10802 -> if (event.itemId == Items.GOLD_ORE_444) {
                finishTask(
                    player,
                    DiaryLevel.EASY,
                    EasyTasks.BRIMHAVEN_MINE_GOLD
                )
            }
        }

        when {
            inBorders(player, BANANA_FISHING_SPOT_AREA) -> {
                when (event.source.id) {
                    NPCs.FISHING_SPOT_323,
                    NPCs.FISHING_SPOT_333 -> {
                        finishTask(
                            player,
                            DiaryLevel.EASY,
                            EasyTasks.USE_FISHING_SPOTS_BANANA_PLANTATION
                        )
                    }
                }
            }

            inBorders(player, TAI_BWO_WANNAI_WOODCUTTING_AREA) -> {
                when (event.itemId) {
                    Items.MAHOGANY_LOGS_6332 -> {
                        finishTask(
                            player,
                            DiaryLevel.MEDIUM,
                            MediumTasks.CUT_MAHOGANY_TREE
                        )
                    }

                    Items.TEAK_LOGS_6333 -> {
                        finishTask(
                            player,
                            DiaryLevel.MEDIUM,
                            MediumTasks.CUT_TEAK_TREE
                        )
                    }
                }
            }
        }
    }

    override fun onNpcKilled(player: Player, event: NPCKillEvent) {
        when (player.viewport.region.id) {
            10899, 10900 -> if (event.npc.id in METAL_DRAGONS) {
                finishTask(
                    player,
                    DiaryLevel.HARD,
                    HardTasks.BRIMHAVEN_DUNGEON_KILL_METAL_DRAGON
                )
            }

            11412 -> if (event.npc.id == NPCs.JOGRE_113) {
                finishTask(
                    player,
                    DiaryLevel.EASY,
                    EasyTasks.POTHOLE_DUNGEON_KILL_JOGRE
                )
            }

            /* Ket-Zek only appears in an instanced Fight Cave area. */
            else -> if (event.npc.id in KET_ZEKS) {
                finishTask(
                    player,
                    DiaryLevel.HARD,
                    HardTasks.FIGHT_CAVE_KILL_KET_ZEK
                )
            }
        }
    }

    override fun onPickedUp(player: Player, event: PickUpEvent) {
        when {
            inBorders(player, MAIN_ISLAND_AREA) -> {
                if (event.itemId == Items.SEAWEED_401) {
                    progressIncrementalTask(
                        player,
                        DiaryLevel.EASY,
                        EasyTasks.PICK_5_SEAWEED,
                        ATTRIBUTE_SEAWEED_PICKED,
                        5
                    )
                }

                if (event.itemId == Items.PALM_LEAF_2339) {
                    progressIncrementalTask(
                        player,
                        DiaryLevel.HARD,
                        HardTasks.PICK_5_PALM_LEAVES,
                        ATTRIBUTE_PALM_LEAF_PICKED,
                        5
                    )
                }
            }
        }
    }
}