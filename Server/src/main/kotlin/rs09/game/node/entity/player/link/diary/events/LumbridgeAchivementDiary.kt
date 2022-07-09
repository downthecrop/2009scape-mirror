package rs09.game.node.entity.player.link.diary.events

import api.events.*
import api.inBorders
import api.questStage
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.world.map.zone.ZoneBorders
import org.rs09.consts.Components
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery
import rs09.game.content.dialogue.region.lumbridge.DukeHoracioDialogue
import rs09.game.content.jobs.JobType
import rs09.game.content.quest.free.dragonslayer.DukeHoracioDSDialogue
import rs09.game.node.entity.player.link.diary.AreaDiaryTask
import rs09.game.node.entity.player.link.diary.DiaryEventHookBase
import rs09.game.node.entity.player.link.diary.DiaryLevel
import rs09.game.node.entity.skill.magic.TeleportMethod
import rs09.game.node.entity.skill.magic.spellconsts.Modern

class LumbridgeAchivementDiary : DiaryEventHookBase(DiaryType.LUMBRIDGE) {

    companion object {
        private val DEAD_TREES = arrayOf(
            Scenery.DEAD_TREE_1282,
            Scenery.DEAD_TREE_1286,
            Scenery.DEAD_TREE_1365
        )

        private val ZOMBIES = arrayOf(
            NPCs.ZOMBIE_73,
            NPCs.ZOMBIE_74
        )

        private val CASTLE_ROOF_AREA = ZoneBorders(3207, 3215, 3210, 3222, 3)
        private val CASTLE_COURTYARD_AREA = ZoneBorders(3226, 3229, 3217, 3208)
        private val COW_PEN_AREA_1 = ZoneBorders(3253, 3255, 3265, 3297)
        private val COW_PEN_AREA_2 = ZoneBorders(3245, 3278, 3253, 3298)
        private val WIZARDS_TOWER_TOP_FLOOR_AREA = ZoneBorders(3103, 3155, 3115, 3165, 2)

        object BeginnerTasks {
            const val CASTLE_CLIMB_TO_HIGHEST_POINT = 0
            const val CASTLE_RAISE_FLAG_ON_ROOF = 1
            const val CASTLE_SPEAK_TO_DUKE_HORACIO = 2
            const val SPEAK_TO_DOOMSAYER = 3
            const val AL_KHARID_PASS_THROUGH_GATE = 4
            const val CHAMPIONS_GUILD_MINE_CLAY = 5
            const val BARBARIAN_VILLAGE_MAKE_SOFT_CLAY = 6
            const val BARBARIAN_VILLAGE_FIRE_A_POT = 7
            const val DRAYNOR_ENTER_SPOOKY_MANSION_COURTYARD = 8
            const val DRAYNOR_VISIT_MARKET = 9
            const val DRAYNOR_TALK_TO_TOWNCRIER_ABOUT_RULES = 10
            const val WIZARDS_TOWER_CLIMB_TO_TOP = 11
            const val SWAMP_MINE_COPPER_ORE = 12
            const val SWAMP_CATCH_SHRIMPS = 13
            const val SWAMP_FISHING_TUTOR_GET_A_JOB = 14
            const val BROWSE_FATHER_AERECK_GRAVES = 15
            const val CHURCH_PLAY_ORGAN = 16
            const val LUMBRIDGE_GUIDE_TALK_ABOUT_SOS = 17
            const val BROWSE_GENERAL_STORE = 18
            const val VISIT_FRED_THE_FARMER = 19
            const val WINDMILL_MAKE_FLOUR = 20
        }

        object EasyTasks {
            const val AL_KHARID_MINE_IRON = 0
            const val COWFIELD_OBTAIN_COW_HIDE = 1
            const val AL_KHARID_TAN_COW_HIDE = 2
            const val CRAFT_LEATHER_GLOVES = 3
            const val RIVER_CATCH_PIKE = 4
            const val SMELT_STEEL_BAR = 5
            const val SWAMP_SEARCH_SHED = 6
            const val SWAMP_KILL_GIANT_RAT = 7
            const val SWAMP_CUT_DEAD_TREE = 8
            const val SWAMP_LIGHT_NORMAL_LOGS = 9
            const val SWAMP_COOK_RAT_MEAT_ON_CAMPFIRE = 10
            const val SWAMP_WATER_ALTAR_CRAFT_RUNE = 11
            const val SWAMP_REPLACE_GHOSTSPEAK_AMULET = 12
            const val WIZARDS_TOWER_TAUNT_DEMON = 13
            const val WIZARDS_TOWER_TELEPORT_ESSENCE_MINE = 14
            const val DRAYNOR_ACCESS_BANK = 15
            const val DRAYNOR_WISEOLDMAN_CHECK_JUNK = 16
            const val DRAYNOR_WISEOLDMAN_PEEK_TELESCOPE = 17
            const val DRAYNOR_JAIL_SEWER_KILL_ZOMBIE = 18
        }

        object MediumTasks {
            const val DRAYNOR_JAIL_SEWER_SMITH_STEEL_LONGSWORD = 0
            const val RIDE_GNOMECOPTER = 1
            const val CAST_LUMBRIDGE_TELEPORT = 2
            const val CASTLE_LIGHT_WILLOW_LOGS = 3
            const val CASTLE_COOK_LOBSTER_ON_RANGE = 4
            const val CASTLE_OBTAIN_ANTIDRAGON_SHIELD = 5
            const val RIVER_GATHER_WILLOW_LOGS = 6
            const val SMELT_SILVER_BAR = 7
            const val CRAFT_HOLY_SYMBOL = 8
            const val RIVER_CATCH_SALMON = 9
            const val AL_KHARID_MINE_SILVER = 10
            const val SWAMP_MINE_COAL = 11
        }
    }

    override val areaTasks
        get() = arrayOf(
            AreaDiaryTask(
                CASTLE_ROOF_AREA,
                DiaryLevel.BEGINNER,
                BeginnerTasks.CASTLE_CLIMB_TO_HIGHEST_POINT
            ),

            AreaDiaryTask(
                WIZARDS_TOWER_TOP_FLOOR_AREA,
                DiaryLevel.BEGINNER,
                BeginnerTasks.WIZARDS_TOWER_CLIMB_TO_TOP
            )
        )

    override fun onResourceProduced(player: Player, event: ResourceProducedEvent) {
        when (player.viewport.region.id) {
            12596 -> if (event.itemId == Items.CLAY_434) {
                finishTask(
                    player,
                    DiaryLevel.BEGINNER,
                    BeginnerTasks.CHAMPIONS_GUILD_MINE_CLAY
                )
            }

            12593, 12849 -> {
                when (event.itemId) {
                    Items.RAW_SHRIMPS_317 -> {
                        finishTask(
                            player,
                            DiaryLevel.BEGINNER,
                            BeginnerTasks.SWAMP_CATCH_SHRIMPS
                        )
                    }

                    Items.COPPER_ORE_436 -> {
                        finishTask(
                            player,
                            DiaryLevel.BEGINNER,
                            BeginnerTasks.SWAMP_MINE_COPPER_ORE
                        )
                    }

                    Items.COAL_453 -> {
                        finishTask(
                            player,
                            DiaryLevel.MEDIUM,
                            MediumTasks.SWAMP_MINE_COAL
                        )
                    }

                    Items.LOGS_1511 -> {
                        if (event.source.id in DEAD_TREES) {
                            finishTask(
                                player,
                                DiaryLevel.EASY,
                                EasyTasks.SWAMP_CUT_DEAD_TREE
                            )
                        }
                    }

                    Items.COOKED_MEAT_2142 -> {
                        if (event.original == Items.RAW_RAT_MEAT_2134) {
                            finishTask(
                                player,
                                DiaryLevel.EASY,
                                EasyTasks.SWAMP_COOK_RAT_MEAT_ON_CAMPFIRE
                            )
                        }
                    }
                }
            }

            12850 -> when (event.itemId) {
                Items.WILLOW_LOGS_1519 -> {
                    finishTask(
                        player,
                        DiaryLevel.MEDIUM,
                        MediumTasks.RIVER_GATHER_WILLOW_LOGS
                    )
                }

                Items.RAW_PIKE_349 -> {
                    finishTask(
                        player,
                        DiaryLevel.EASY,
                        EasyTasks.RIVER_CATCH_PIKE
                    )
                }

                Items.RAW_SALMON_331 -> {
                    finishTask(
                        player,
                        DiaryLevel.MEDIUM,
                        MediumTasks.RIVER_CATCH_SALMON
                    )
                }

                Items.LOBSTER_379 -> {
                    if (event.original == Items.RAW_LOBSTER_377
                        && event.source.id == Scenery.COOKING_RANGE_114
                    ) {
                        finishTask(
                            player,
                            DiaryLevel.MEDIUM,
                            MediumTasks.CASTLE_COOK_LOBSTER_ON_RANGE
                        )
                    }
                }

                Items.UNSTRUNG_SYMBOL_1714 -> {
                    if (event.original == Items.SILVER_BAR_2355
                        && event.source.id == Scenery.FURNACE_36956
                    ) {
                        finishTask(
                            player,
                            DiaryLevel.MEDIUM,
                            MediumTasks.CRAFT_HOLY_SYMBOL
                        )
                    }
                }
            }

            13107 -> when (event.itemId) {
                Items.IRON_ORE_440 -> {
                    finishTask(
                        player,
                        DiaryLevel.EASY,
                        EasyTasks.AL_KHARID_MINE_IRON
                    )
                }

                Items.SILVER_ORE_442 -> {
                    finishTask(
                        player,
                        DiaryLevel.MEDIUM,
                        MediumTasks.AL_KHARID_MINE_SILVER
                    )
                }
            }
        }
    }

    override fun onNpcKilled(player: Player, event: NPCKillEvent) {
        when (player.viewport.region.id) {
            12593, 12849 -> if (event.npc.id == NPCs.GIANT_RAT_86) {
                finishTask(
                    player,
                    DiaryLevel.EASY,
                    EasyTasks.SWAMP_KILL_GIANT_RAT
                )
            }

            12438, 12439 -> if (event.npc.id in ZOMBIES) {
                finishTask(
                    player,
                    DiaryLevel.EASY,
                    EasyTasks.DRAYNOR_JAIL_SEWER_KILL_ZOMBIE
                )
            }
        }
    }

    override fun onTeleported(player: Player, event: TeleportEvent) {
        when (event.source) {
            is NPC -> if (event.method == TeleportMethod.NPC && event.source.id == NPCs.SEDRIDOR_300) {
                finishTask(
                    player,
                    DiaryLevel.EASY,
                    EasyTasks.WIZARDS_TOWER_TELEPORT_ESSENCE_MINE
                )
            }
        }
    }

    override fun onFireLit(player: Player, event: LitFireEvent) {
        when (player.viewport.region.id) {
            12593, 12849 -> if (event.logId == Items.LOGS_1511) {
                finishTask(
                    player,
                    DiaryLevel.EASY,
                    EasyTasks.SWAMP_LIGHT_NORMAL_LOGS
                )
            }

            12850 -> if (event.logId == Items.WILLOW_LOGS_1519) {
                if (inBorders(player, CASTLE_COURTYARD_AREA)) {
                    finishTask(
                        player,
                        DiaryLevel.MEDIUM,
                        MediumTasks.CASTLE_LIGHT_WILLOW_LOGS
                    )
                }
            }
        }
    }

    override fun onInteracted(player: Player, event: InteractionEvent) {
        when (player.viewport.region.id) {
            12337 -> if (event.target.id == Scenery.RAILING_37668
                && event.option == "taunt-through"
            ) {
                finishTask(
                    player,
                    DiaryLevel.EASY,
                    EasyTasks.WIZARDS_TOWER_TAUNT_DEMON
                )
            }
        }
    }

    override fun onAttributeRemoved(player: Player, event: AttributeRemoveEvent) {
        when (event.attribute) {
            "gc:flying" -> {
                finishTask(
                    player,
                    DiaryLevel.MEDIUM,
                    MediumTasks.RIDE_GNOMECOPTER
                )
            }
        }
    }

    override fun onDialogueOpened(player: Player, event: DialogueOpenEvent) {
        if (event.dialogue is DukeHoracioDialogue) {
            finishTask(
                player,
                DiaryLevel.BEGINNER,
                BeginnerTasks.CASTLE_SPEAK_TO_DUKE_HORACIO
            )
        }
    }

    override fun onDialogueOptionSelected(player: Player, event: DialogueOptionSelectionEvent) {
        when (event.dialogue) {
            is DukeHoracioDSDialogue -> {
                val dragonSlayerStage = questStage(player, "Dragon Slayer")

                if ((dragonSlayerStage == 100 && event.currentStage == 4)
                    || event.currentStage == 12) {
                    finishTask(
                        player,
                        DiaryLevel.MEDIUM,
                        MediumTasks.CASTLE_OBTAIN_ANTIDRAGON_SHIELD
                    )
                }
            }
        }
    }


    override fun onPickedUp(player: Player, event: PickUpEvent) {
        when (player.viewport.region.id) {
            12850, 12851 -> {
                if (event.itemId == Items.COWHIDE_1739) {
                    if (inBorders(player, COW_PEN_AREA_1)
                        || inBorders(player, COW_PEN_AREA_2)
                    ) {
                        finishTask(
                            player,
                            DiaryLevel.EASY,
                            EasyTasks.COWFIELD_OBTAIN_COW_HIDE
                        )
                    }
                }
            }
        }
    }

    override fun onInterfaceOpened(player: Player, event: InterfaceOpenEvent) {
        when (player.viewport.region.id) {
            12338 -> if (event.component.id == Components.BANK_V2_MAIN_762) {
                finishTask(
                    player,
                    DiaryLevel.EASY,
                    EasyTasks.DRAYNOR_ACCESS_BANK
                )
            }

            12850 -> if (event.component.id == Components.SHOP_TEMPLATE_620) {
                finishTask(
                    player,
                    DiaryLevel.BEGINNER,
                    BeginnerTasks.BROWSE_GENERAL_STORE
                )
            }
        }
    }

    override fun onSpellCast(player: Player, event: SpellCastEvent) {
        when (event.spellId) {
            Modern.LUMBRIDGE_TELEPORT -> {
                finishTask(
                    player,
                    DiaryLevel.MEDIUM,
                    MediumTasks.CAST_LUMBRIDGE_TELEPORT
                )
            }
        }
    }

    override fun onJobAssigned(player: Player, event: JobAssignmentEvent) {
        when (player.viewport.region.id) {
            12849 -> if (event.employerNpc.id == NPCs.FISHING_TUTOR_4901) {
                finishTask(
                    player,
                    DiaryLevel.BEGINNER,
                    BeginnerTasks.SWAMP_FISHING_TUTOR_GET_A_JOB
                )
            }
        }
    }
}