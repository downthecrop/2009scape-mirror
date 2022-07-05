package rs09.game.node.entity.player.link.diary.events

import api.*
import api.events.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import org.rs09.consts.Items
import rs09.game.node.entity.player.link.diary.DiaryEventHookBase
import rs09.game.node.entity.player.link.diary.DiaryLevel

class SeersVillageDiaryEventHooks : MapArea, DiaryEventHookBase() {
    companion object {
        private const val ATTRIBUTE_CUT_YEW_COUNT = "diary:seers:cut-yew"
        private const val ATTRIBUTE_BASS_CAUGHT = "diary:seers:bass-caught"
        private const val ATTRIBUTE_SHARK_CAUGHT_COUNT = "diary:seers:shark-caught"
        private const val ATTRIBUTE_SHARK_COOKED_COUNT = "diary:seers:shark-cooked"

        private val SEERS_VILLAGE_AREA = ZoneBorders(2687, 3455, 2742, 3507)
        private val SEERS_BANK_AREA = ZoneBorders(2721, 3490, 2730, 3493)

        private val RANGING_GUILD_LOCATION = Location(2657, 3439)

        private val COMBAT_BRACELETS = arrayOf(
            Items.COMBAT_BRACELET_11126, Items.COMBAT_BRACELET4_11118,
            Items.COMBAT_BRACELET3_11120, Items.COMBAT_BRACELET2_11122,
            Items.COMBAT_BRACELET1_11124
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
            const val RANGING_GUILD_KILL_1_GUARD_ON_EACH_TOWER = 6
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
            const val MCGRUBORS_WOOD_USE_FAIRY_RING = 4
            const val LIGHT_MAGIC_LOG = 5
            const val HIGH_ALCH_MAGIC_SHORTBOW_INSIDE_BANK = 6
            const val CATHERBY_CATCH_5_SHARKS = 7
            const val CATHERBY_COOK_5_SHARKS_WITH_COOKING_GAUNTLETS = 8
            const val CHARGE_5_WATER_ORBS_AT_ONCE = 9
            const val GRAPPLE_FROM_WATER_OBELISK_TO_CATHERBY_SHORE = 10
        }
    }

    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf()
    }

    override fun onAttributeSet(player: Player, event: AttributeSetEvent) {
        when (event.attribute) {
            "/save:${ATTRIBUTE_CUT_YEW_COUNT}" -> {
                if (event.value !is Int) return

                if (event.value >= 5) {
                    finishTask(
                        player,
                        DiaryType.SEERS_VILLAGE,
                        DiaryLevel.HARD,
                        HardTasks.CUT_5_YEW_LOGS
                    )
                }
            }

            "/save:${ATTRIBUTE_SHARK_CAUGHT_COUNT}" -> {
                if (event.value !is Int) return

                if (event.value >= 5) {
                    finishTask(
                        player,
                        DiaryType.SEERS_VILLAGE,
                        DiaryLevel.HARD,
                        HardTasks.CATHERBY_CATCH_5_SHARKS
                    )
                }
            }

            "/save:${ATTRIBUTE_SHARK_COOKED_COUNT}" -> {
                if (event.value !is Int) return

                if (event.value >= 5) {
                    finishTask(
                        player,
                        DiaryType.SEERS_VILLAGE,
                        DiaryLevel.HARD,
                        HardTasks.CATHERBY_COOK_5_SHARKS_WITH_COOKING_GAUNTLETS
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
                        DiaryType.SEERS_VILLAGE,
                        DiaryLevel.HARD,
                        HardTasks.LIGHT_MAGIC_LOG
                    )
                }
            }
        }
    }

    override fun onResourceProduced(player: Player, event: ResourceProducedEvent) {
        when (player.viewport.region.id) {
            10806 -> if (event.itemId == Items.YEW_LOGS_1515) {
                setAttribute(
                    player,
                    "/save:${ATTRIBUTE_CUT_YEW_COUNT}",
                    getAttribute(player, ATTRIBUTE_CUT_YEW_COUNT, 0) + 1
                )
            }

            11317 -> when (event.itemId) {
                Items.RAW_MACKEREL_353 -> {
                    finishTask(
                        player,
                        DiaryType.SEERS_VILLAGE,
                        DiaryLevel.EASY,
                        EasyTasks.CATCH_MACKEREL
                    )
                }

                Items.RAW_BASS_363 -> {
                    if (!getAttribute(player, ATTRIBUTE_BASS_CAUGHT, false)) {
                        setAttribute(
                            player,
                            "/save:${ATTRIBUTE_BASS_CAUGHT}",
                            true
                        )
                    }
                }

                Items.BASS_365 -> {
                    if (getAttribute(player, ATTRIBUTE_BASS_CAUGHT, false)) {
                        finishTask(
                            player,
                            DiaryType.SEERS_VILLAGE,
                            DiaryLevel.MEDIUM,
                            MediumTasks.CATHERBY_CATCH_AND_COOK_BASS
                        )
                    }
                }

                Items.RAW_SHARK_383 -> {
                    setAttribute(
                        player,
                        "/save:${ATTRIBUTE_SHARK_CAUGHT_COUNT}",
                        getAttribute(player, ATTRIBUTE_SHARK_CAUGHT_COUNT, 0) + 1
                    )
                }

                Items.SHARK_385 -> {
                    if (isEquipped(player, Items.COOKING_GAUNTLETS_775)) {
                        setAttribute(
                            player,
                            "/save:${ATTRIBUTE_SHARK_COOKED_COUNT}",
                            getAttribute(player, ATTRIBUTE_SHARK_COOKED_COUNT, 0) + 1
                        )
                    }
                }
            }
        }
    }

    override fun onTeleported(player: Player, event: TeleportEvent) {
        when (event.source) {
            is Item -> if (event.source.id in COMBAT_BRACELETS) {
                if (player.location.equals(RANGING_GUILD_LOCATION)) {
                    finishTask(
                        player,
                        DiaryType.SEERS_VILLAGE,
                        DiaryLevel.HARD,
                        HardTasks.RANGING_GUILD_TELEPORT
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
                    DiaryType.SEERS_VILLAGE,
                    DiaryLevel.HARD,
                    HardTasks.HIGH_ALCH_MAGIC_SHORTBOW_INSIDE_BANK
                )
            }
        }
    }
}