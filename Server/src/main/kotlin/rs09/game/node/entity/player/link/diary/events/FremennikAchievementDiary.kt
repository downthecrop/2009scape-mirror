package rs09.game.node.entity.player.link.diary.events

import api.events.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.world.map.zone.ZoneBorders
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery
import rs09.game.content.dialogue.region.barbarianassault.CaptainCainDialogue
import rs09.game.content.dialogue.region.rellekka.HuntingExpertRellekkaDialogue
import rs09.game.interaction.inter.FairyRing
import rs09.game.node.entity.player.link.diary.AreaDiaryTask
import rs09.game.node.entity.player.link.diary.DiaryEventHookBase
import rs09.game.node.entity.player.link.diary.DiaryLevel

class FremennikAchievementDiary : DiaryEventHookBase(DiaryType.FREMENNIK) {
    companion object {
        private const val ATTRIBUTE_SEAWEED_PICKED = "diary:fremennik:seaweed-picked"
        private const val ATTRIBUTE_ROCK_CRAB_KILLCOUNT = "diary:fremennik:rock-crabs-killed"
        private const val ATTRIBUTE_BARBARIAN_FISHING_TRAINING = "barbtraining:fishing"
        private const val ATTRIBUTE_BARBARIAN_HUNTING_TRAINING = "barbtraining:hunting"

        private val WINDSWEPT_TREE_AREA = ZoneBorders(2743, 3718, 2750, 3737)

        private val FISHING_SPOTS = arrayOf(
            NPCs.FISHING_SPOT_309, NPCs.FISHING_SPOT_334,
            NPCs.FISHING_SPOT_324, NPCs.FISHING_SPOT_322
        )

        private val ROCK_CRABS = arrayOf(
            NPCs.ROCK_CRAB_1265, NPCs.ROCK_CRAB_1267
        )

        private val CAVE_CRAWLERS = arrayOf(
            NPCs.CAVE_CRAWLER_1600, NPCs.CAVE_CRAWLER_1601,
            NPCs.CAVE_CRAWLER_1602, NPCs.CAVE_CRAWLER_1603
        )

        object EasyTasks {
            const val SLAYER_CAVE_KILL_CAVE_CRAWLER = 0
            const val KILL_5_ROCK_CRABS = 1
            const val MAINLAND_FIND_HIGHEST_TREE = 2
            const val BARBARIAN_ASSAULT_VIEW_REWARDS = 3
            const val OTTO_GODBLESSED_LEARN_BARBARIAN_FISHING = 4
            const val PICK_3_SEAWEED = 5
            const val FIND_HUNTING_EXPERT = 6
            const val PIER_CATCH_FISH = 7
            const val GATE_OBELISK_RECHARGE_POINTS = 8
            const val KILL_ADULT_BLACK_UNICORN = 9
        }

        object MediumTasks {
            const val LEARN_HISTORY_FROM_CHIEFTAIN_BRUNDT = 0
            const val TOWER_GUARDS_WATCH_SHOUTING_MATCH = 1
            const val INTERACT_WITH_PET_ROCK = 2
            const val MAKE_3_VIALS = 3
            const val CHARM_FOSSEGRIMEN_RAW_BASS = 4
            const val KILL_ICE_TROLL_WHILE_WEARING_YAK_ARMOR = 5
            const val MAKE_CHEESE_WITH_CHURN = 6
            const val DIAL_FAIRY_RING_MOUNTAINTOP = 7
            const val BROWSE_BOOT_COLORS_YRSA = 8
            const val HUNT_SABRE_TOOTHED_KYATT = 9
            const val STEAL_FISH_FROM_STALL = 10
        }

        object HardTasks {
            const val KILL_3_DAGANNOTHS = 0
            const val FREMENNIK_HONORIFIC_ARMOR = 1
            const val COMPLETE_BARBARIAN_OUTPOST_AGILITY_COURSE = 2
            const val LUNAR_ISLE_MINE_PURE_ESSENCE = 3
            const val MAKE_BARBARIAN_PYRE_SHIP_ARCTIC_PINE = 4
            const val CATCH_TUNE_WITHOUT_HARPOON = 5
            const val BAKE_PIE_WITH_MAGIC = 6
            const val KILL_MITHRIL_DRAGON = 7
            const val GET_MAHOGANY_FROM_ETCETERIA = 8
        }
    }

    override val areaTasks get() = arrayOf(
         AreaDiaryTask(
            WINDSWEPT_TREE_AREA,
            DiaryLevel.EASY,
            EasyTasks.MAINLAND_FIND_HIGHEST_TREE
        )
    )

    override fun onAttributeSet(player: Player, event: AttributeSetEvent) {
        when (event.attribute) {
            "/save:${ATTRIBUTE_BARBARIAN_FISHING_TRAINING}" -> {
                if (event.value !is Boolean) return

                if (event.value) {
                    finishTask(
                        player,
                        DiaryLevel.EASY,
                        EasyTasks.OTTO_GODBLESSED_LEARN_BARBARIAN_FISHING
                    )
                }
            }
        }
    }

    override fun onDialogueOpened(player: Player, event: DialogueOpenEvent) {
        when (event.dialogue) {
            is CaptainCainDialogue -> {
                finishTask(
                    player,
                    DiaryLevel.EASY,
                    EasyTasks.BARBARIAN_ASSAULT_VIEW_REWARDS
                )
            }

            is HuntingExpertRellekkaDialogue -> {
                finishTask(
                    player,
                    DiaryLevel.EASY,
                    EasyTasks.FIND_HUNTING_EXPERT
                )
            }
        }
    }

    override fun onResourceProduced(player: Player, event: ResourceProducedEvent) {
        when (player.viewport.region.id) {
            10553 -> if (event.source.id in FISHING_SPOTS) {
                finishTask(
                    player,
                    DiaryLevel.EASY,
                    EasyTasks.PIER_CATCH_FISH
                )
            }
        }
    }

    override fun onNpcKilled(player: Player, event: NPCKillEvent) {
        when (player.viewport.region.id) {
            10042, 10554 -> if (event.npc.id in ROCK_CRABS) {
                progressIncrementalTask(
                    player,
                    DiaryLevel.EASY,
                    EasyTasks.KILL_5_ROCK_CRABS,
                    ATTRIBUTE_ROCK_CRAB_KILLCOUNT,
                    5
                )
            }

            11164 -> if (event.npc.id in CAVE_CRAWLERS) {
                finishTask(
                    player,
                    DiaryLevel.EASY,
                    EasyTasks.SLAYER_CAVE_KILL_CAVE_CRAWLER
                )
            }

            10808, 10809 -> if (event.npc.id == NPCs.BLACK_UNICORN_133) {
                finishTask(
                    player,
                    DiaryLevel.EASY,
                    EasyTasks.KILL_ADULT_BLACK_UNICORN
                )
            }
        }
    }

    override fun onPickedUp(player: Player, event: PickUpEvent) {
        when (player.viewport.region.id) {
            10810 -> if (event.itemId == Items.SEAWEED_401) {
                progressIncrementalTask(
                    player,
                    DiaryLevel.EASY,
                    EasyTasks.PICK_3_SEAWEED,
                    ATTRIBUTE_SEAWEED_PICKED,
                    3
                )
            }
        }
    }

    override fun onFairyRingDialed(player: Player, event: FairyRingDialEvent) {
        if (event.fairyRing == FairyRing.DKS) {
            finishTask(
                player,
                DiaryLevel.MEDIUM,
                MediumTasks.DIAL_FAIRY_RING_MOUNTAINTOP
            )
        }
    }

    override fun onSummoningPointsRecharged(player: Player, event: SummoningPointsRechargeEvent) {
        when (player.viewport.region.id) {
            10552 -> if (event.obelisk.id == Scenery.SMALL_OBELISK_29944) {
                finishTask(
                    player,
                    DiaryLevel.EASY,
                    EasyTasks.GATE_OBELISK_RECHARGE_POINTS
                )
            }
        }
    }
}