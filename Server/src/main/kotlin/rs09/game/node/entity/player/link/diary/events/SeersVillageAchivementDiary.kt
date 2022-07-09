package rs09.game.node.entity.player.link.diary.events

import api.*
import api.events.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery
import rs09.game.interaction.inter.FairyRing
import rs09.game.interaction.item.withnpc.PoisonChaliceOnKingArthurDialogue
import rs09.game.node.entity.player.link.diary.DiaryEventHookBase
import rs09.game.node.entity.player.link.diary.DiaryLevel

class SeersVillageAchivementDiary : DiaryEventHookBase(DiaryType.SEERS_VILLAGE) {
    companion object {
        private const val ATTRIBUTE_CUT_YEW_COUNT = "diary:seers:cut-yew"
        private const val ATTRIBUTE_BASS_CAUGHT = "diary:seers:bass-caught"
        private const val ATTRIBUTE_SHARK_CAUGHT_COUNT = "diary:seers:shark-caught"
        private const val ATTRIBUTE_SHARK_COOKED_COUNT = "diary:seers:shark-cooked"
        private const val ATTRIBUTE_ELEMENTAL_KILL_FLAGS = "diary:seers:elemental-kills"
        private const val ATTRIBUTE_ARCHER_KILL_FLAGS = "diary:seers:archer-kills"
        private const val ATTRIBUTE_COAL_TRUCK_FULL = "diary:seers:coal-truck-full"

        private val SEERS_VILLAGE_AREA = ZoneBorders(2687, 3455, 2742, 3507)
        private val SEERS_BANK_AREA = ZoneBorders(2721, 3490, 2730, 3493)
        private val SEERS_COAL_TRUCKS_AREA = ZoneBorders(2690, 3502, 2699, 3508)

        private val RANGING_GUILD_LOCATION = Location(2657, 3439)

        private val COMBAT_BRACELETS = arrayOf(
            Items.COMBAT_BRACELET_11126, Items.COMBAT_BRACELET4_11118,
            Items.COMBAT_BRACELET3_11120, Items.COMBAT_BRACELET2_11122,
            Items.COMBAT_BRACELET1_11124
        )

        private val RANGING_GUILD_ARCHERS = arrayOf(
            NPCs.TOWER_ARCHER_688, NPCs.TOWER_ARCHER_689,
            NPCs.TOWER_ARCHER_690, NPCs.TOWER_ARCHER_691
        )

        private val WORKSHOP_ELEMENTALS = arrayOf(
            NPCs.FIRE_ELEMENTAL_1019, NPCs.EARTH_ELEMENTAL_1020,
            NPCs.AIR_ELEMENTAL_1021, NPCs.WATER_ELEMENTAL_1022
        )

        object EasyTasks {
            const val PICK_5_FLAX = 0
            const val WALK_CLOCKWISE_AROUND_MYSTERIOUS_STATUE = 1
            const val SIR_GALAHAD_MAKE_TEA = 2
            const val TAKE_POISON_TO_KING_ARTHUR = 3
            const val SPIN_5_BOW_STRINGS = 4
            const val SINCLAIR_MANSION_FILL_5_POTS_WITH_FLOUR = 5
            const val FORESTERS_ARMS_GIVE_5_LOCALS_GLASS_OF_CIDER = 6
            const val PLANT_JUTE = 7
            const val SINCLAIR_MANSION_USE_CHURN = 8
            const val BUY_CANDLE = 9
            const val PRAY_AT_ALTAR = 10
            const val CATCH_MACKEREL = 11
        }

        object MediumTasks {
            const val SINCLAIR_MANSION_USE_FREMENNIK_SHORTCUT = 0
            const val THORMAC_SORCERER_TALK_ABOUT_MYSTIC_STAVES = 1
            const val TRANSPORT_FULL_LOAD_OF_COAL = 2
            const val FIND_HIGHEST_POINT = 3
            const val DEFEAT_EACH_ELEMENTAL_TYPE = 4
            const val TELEPORT_TO_CAMELOT = 5
            const val RANGING_GUILD_KILL_EACH_TOWER_GUARD = 6
            const val RANGING_GUILD_JUDGE_1000_ARCHERY_TICKETS = 7
            const val RANGING_GUILD_BUY_SOMETHING_FOR_TICKETS = 8
            const val USE_FAMILIAR_TO_LIGHT_MAPLE_LOGS = 9
            const val HARRY_GET_PET_FISH = 10
            const val CATHERBY_CATCH_AND_COOK_BASS = 11
        }

        object HardTasks {
            const val RANGING_GUILD_TELEPORT = 0
            const val CUT_5_YEW_LOGS = 1
            const val FLETCH_MAGIC_SHORTBOW_INSIDE_BANK = 2
            const val ENTER_SEERS_COURTHOUSE_WITH_PIETY = 3
            const val DIAL_FAIRY_RING_MCGRUBORS_WOOD = 4
            const val LIGHT_MAGIC_LOG = 5
            const val HIGH_ALCH_MAGIC_SHORTBOW_INSIDE_BANK = 6
            const val CATHERBY_CATCH_5_SHARKS = 7
            const val CATHERBY_COOK_5_SHARKS_WITH_COOKING_GAUNTLETS = 8
            const val CHARGE_5_WATER_ORBS_AT_ONCE = 9
            const val GRAPPLE_FROM_WATER_OBELISK_TO_CATHERBY_SHORE = 10
        }
    }

    override fun onResourceProduced(player: Player, event: ResourceProducedEvent) {
        when (player.viewport.region.id) {
            10806 -> if (event.itemId == Items.YEW_LOGS_1515) {
                progressIncrementalTask(
                    player,
                    DiaryLevel.HARD,
                    HardTasks.CUT_5_YEW_LOGS,
                    ATTRIBUTE_CUT_YEW_COUNT,
                    5
                )
            }

            11317 -> when (event.itemId) {
                Items.RAW_MACKEREL_353 -> {
                    finishTask(
                        player,
                        DiaryLevel.EASY,
                        EasyTasks.CATCH_MACKEREL
                    )
                }

                Items.RAW_BASS_363 -> {
                    fulfillTaskRequirement(
                        player,
                        DiaryLevel.MEDIUM,
                        MediumTasks.CATHERBY_CATCH_AND_COOK_BASS,
                        ATTRIBUTE_BASS_CAUGHT
                    )
                }

                Items.BASS_365 -> {
                    whenTaskRequirementFulfilled(player, ATTRIBUTE_BASS_CAUGHT) {
                        finishTask(
                            player,
                            DiaryLevel.MEDIUM,
                            MediumTasks.CATHERBY_CATCH_AND_COOK_BASS
                        )
                    }
                }

                Items.RAW_SHARK_383 -> {
                    progressIncrementalTask(
                        player,
                        DiaryLevel.HARD,
                        HardTasks.CATHERBY_CATCH_5_SHARKS,
                        ATTRIBUTE_SHARK_CAUGHT_COUNT,
                        5
                    )
                }

                Items.SHARK_385 -> {
                    if (isEquipped(player, Items.COOKING_GAUNTLETS_775)) {
                        progressIncrementalTask(
                            player,
                            DiaryLevel.HARD,
                            HardTasks.CATHERBY_COOK_5_SHARKS_WITH_COOKING_GAUNTLETS,
                            ATTRIBUTE_SHARK_COOKED_COUNT,
                            5
                        )
                    }
                }
            }
        }
    }

    override fun onNpcKilled(player: Player, event: NPCKillEvent) {
        when (player.viewport.region.id) {
            10906 -> if (event.npc.id in WORKSHOP_ELEMENTALS) {
                progressFlaggedTask(
                    player,
                    DiaryLevel.MEDIUM,
                    MediumTasks.DEFEAT_EACH_ELEMENTAL_TYPE,
                    ATTRIBUTE_ELEMENTAL_KILL_FLAGS,
                    1 shl (event.npc.id - NPCs.FIRE_ELEMENTAL_1019),
                    0xF
                )
            }

            10549 -> if (event.npc.id in RANGING_GUILD_ARCHERS) {
                progressFlaggedTask(
                    player,
                    DiaryLevel.MEDIUM,
                    MediumTasks.RANGING_GUILD_KILL_EACH_TOWER_GUARD,
                    ATTRIBUTE_ARCHER_KILL_FLAGS,
                    /* Thanks for sequential NPC IDs, Jagex! */
                    1 shl (event.npc.id - NPCs.TOWER_ARCHER_688),
                    0xF
                )
            }
        }
    }

    override fun onTeleported(player: Player, event: TeleportEvent) {
        when (event.source) {
            is Item -> if (event.source.id in COMBAT_BRACELETS) {
                if (event.location == RANGING_GUILD_LOCATION) {
                    finishTask(
                        player,
                        DiaryLevel.HARD,
                        HardTasks.RANGING_GUILD_TELEPORT
                    )
                }
            }
        }
    }

    override fun onFireLit(player: Player, event: LitFireEvent) {
        when {
            inBorders(player, SEERS_VILLAGE_AREA) -> {
                if (event.logId == Items.MAGIC_LOGS_1513) {
                    finishTask(
                        player,
                        DiaryLevel.HARD,
                        HardTasks.LIGHT_MAGIC_LOG
                    )
                }
            }
        }
    }

    override fun onInteracted(player: Player, event: InteractionEvent) {
        when {
            inBorders(player, SEERS_COAL_TRUCKS_AREA) -> {
                whenTaskRequirementFulfilled(player, ATTRIBUTE_COAL_TRUCK_FULL) {
                    if (event.option == "remove-coal") {
                        finishTask(
                            player,
                            DiaryLevel.MEDIUM,
                            MediumTasks.TRANSPORT_FULL_LOAD_OF_COAL
                        )
                    }
                }
            }
        }
    }

    override fun onDialogueOptionSelected(player: Player, event: DialogueOptionSelectionEvent) {
        when (event.dialogue) {
            is PoisonChaliceOnKingArthurDialogue -> {
                if (event.currentStage == 4) {
                    finishTask(
                        player,
                        DiaryLevel.EASY,
                        EasyTasks.TAKE_POISON_TO_KING_ARTHUR
                    )
                }
            }
        }
    }

    override fun onAttributeSet(player: Player, event: AttributeSetEvent) {
        when (event.attribute) {
            "/save:coal-truck-inventory" -> {
                if (event.value !is Int) return

                if (event.value >= 120) {
                    fulfillTaskRequirement(
                        player,
                        DiaryLevel.MEDIUM,
                        MediumTasks.TRANSPORT_FULL_LOAD_OF_COAL,
                        ATTRIBUTE_COAL_TRUCK_FULL
                    )
                }
            }
        }
    }

    override fun onItemAlchemized(player: Player, event: ItemAlchemizationEvent) {
        if (inBorders(player, SEERS_BANK_AREA)) {
            if (event.itemId == Items.MAGIC_SHORTBOW_861 && event.isHigh) {
                finishTask(
                    player,
                    DiaryLevel.HARD,
                    HardTasks.HIGH_ALCH_MAGIC_SHORTBOW_INSIDE_BANK
                )
            }
        }
    }

    override fun onFairyRingDialed(player: Player, event: FairyRingDialEvent) {
        if (event.fairyRing == FairyRing.ALS) {
            finishTask(
                player,
                DiaryLevel.HARD,
                HardTasks.DIAL_FAIRY_RING_MCGRUBORS_WOOD
            )
        }
    }
}