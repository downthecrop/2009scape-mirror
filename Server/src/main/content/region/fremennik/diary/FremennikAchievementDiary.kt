package content.region.fremennik.diary

import content.global.handlers.iface.FairyRing
import content.minigame.barbassault.CaptainCainDialogue
import content.region.fremennik.jatizso.dialogue.TowerGuardDialogue
import content.region.fremennik.rellekka.dialogue.HuntingExpertRellekkaDialogue
import content.region.fremennik.rellekka.quest.thefremenniktrials.ChieftanBrundt
import core.api.inBorders
import core.api.inEquipment
import core.game.diary.AreaDiaryTask
import core.game.diary.DiaryEventHookBase
import core.game.diary.DiaryLevel
import core.game.event.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.SpellBookManager
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.world.map.zone.ZoneBorders
import org.rs09.consts.*

class FremennikAchievementDiary : DiaryEventHookBase(DiaryType.FREMENNIK) {
    companion object {
        private const val ATTRIBUTE_SEAWEED_PICKED = "diary:fremennik:seaweed-picked"
        private const val ATTRIBUTE_ROCK_CRAB_KILLCOUNT = "diary:fremennik:rock-crabs-killed"
        private const val ATTRIBUTE_BARBARIAN_FISHING_TRAINING = "barbtraining:fishing"
        private const val ATTRIBUTE_BARBARIAN_HUNTING_TRAINING = "barbtraining:hunting"
        private const val ATTRIBUTE_DAGANNOTHS_KILLCOUNT = "diary:fremennik:dagannoths-killed"

        private val WINDSWEPT_TREE_AREA = ZoneBorders(2743, 3718, 2750, 3737)
        private val YRSA_SHOP_BORDERS = ZoneBorders(2622, 3672, 2626, 3676)
        private val RELLEKKA_HUNTING_AREA = ZoneBorders(2723, 3776, 2743, 3796)
        private val ECTERNIA_TREE_AREA = ZoneBorders(2592, 3887,2620, 3899)
        private val RELLEKKA_BLACKSMITH_AREA = ZoneBorders(2616, 3658, 2621, 3668)

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

        private val DAGANNOTHS = arrayOf(
            NPCs.DAGANNOTH_2455, NPCs.DAGANNOTH_2456,
            NPCs.DAGANNOTH_PRIME_2882, NPCs.DAGANNOTH_REX_2883, NPCs.DAGANNOTH_SUPREME_2881
        )

        private val ICE_TROLLS = arrayOf(
            NPCs.ICE_TROLL_RUNT_5521, NPCs.ICE_TROLL_MALE_5522, NPCs.ICE_TROLL_FEMALE_5523, NPCs.ICE_TROLL_GRUNT_5524,
            NPCs.ICE_TROLL_RUNT_5525, NPCs.ICE_TROLL_MALE_5526, NPCs.ICE_TROLL_FEMALE_5527, NPCs.ICE_TROLL_GRUNT_5528,
            NPCs.ICE_TROLL_RUNT_5473, NPCs.ICE_TROLL_MALE_5474, NPCs.ICE_TROLL_FEMALE_5475, NPCs.ICE_TROLL_GRUNT_5476
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
            "/save:$ATTRIBUTE_BARBARIAN_FISHING_TRAINING" -> {
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

    override fun onDialogueOptionSelected(player: Player, event: DialogueOptionSelectionEvent) {
        when (event.dialogue) {
            is TowerGuardDialogue -> {
                if (event.currentStage == 10) {
                    finishTask(
                        player,
                        DiaryLevel.MEDIUM,
                        MediumTasks.TOWER_GUARDS_WATCH_SHOUTING_MATCH
                    )
                }
            }

            is ChieftanBrundt -> {
                if (event.currentStage == 615){
                    finishTask(
                        player,
                        DiaryLevel.MEDIUM,
                        MediumTasks.LEARN_HISTORY_FROM_CHIEFTAIN_BRUNDT
                    )
                }
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
            } else if (event.itemId == Items.CHEESE_1985) {
                finishTask(
                        player,
                        DiaryLevel.MEDIUM,
                        MediumTasks.MAKE_CHEESE_WITH_CHURN
                )
            } else if (event.source.id == Scenery.FISH_STALL_4277) {
                finishTask(
                        player,
                        DiaryLevel.MEDIUM,
                        MediumTasks.STEAL_FISH_FROM_STALL
                )
            }

            /* After Royal trouble quest
            10300 -> when (event.itemId) {
                Items.MAHOGANY_LOGS_6332 -> {
                    if (inBorders(player, ECTERNIA_TREE_AREA)) {
                        finishTask(
                            player,
                            DiaryLevel.HARD,
                            HardTasks.GET_MAHOGANY_FROM_ETCETERIA
                        )
                    }
                }
            }*/
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

            9886, 10142, 11589 -> if (event.npc.id in DAGANNOTHS) {
                progressIncrementalTask(
                    player,
                    DiaryLevel.HARD,
                    HardTasks.KILL_3_DAGANNOTHS,
                    ATTRIBUTE_DAGANNOTHS_KILLCOUNT,
                    3
                )
            }

            6995 -> if (event.npc.id == NPCs.MITHRIL_DRAGON_5363) {
                finishTask(
                    player,
                    DiaryLevel.HARD,
                    HardTasks.KILL_MITHRIL_DRAGON
                )
            }

            9276, 9532 -> if (event.npc.id in ICE_TROLLS && inEquipment(player, Items.YAK_HIDE_ARMOUR_10822,1)) {
                finishTask(
                    player,
                    DiaryLevel.MEDIUM,
                    MediumTasks.KILL_ICE_TROLL_WHILE_WEARING_YAK_ARMOR
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

    override fun onInteracted(player: Player, event: InteractionEvent) {
        when (player.viewport.region.id) {
            10811 -> if (event.target.id == Scenery.COLLAPSED_TRAP_19233 && inBorders(player, RELLEKKA_HUNTING_AREA) && event.option == "dismantle") {
                finishTask(
                    player,
                    DiaryLevel.MEDIUM,
                    MediumTasks.HUNT_SABRE_TOOTHED_KYATT
                )
            }
        }

        if (event.option == "watch-shouting") {
            finishTask(
                player,
                DiaryLevel.MEDIUM,
                MediumTasks.TOWER_GUARDS_WATCH_SHOUTING_MATCH
            )
        }

        if (event.target.id == Items.PET_ROCK_3695 && event.option == "interact") {
            finishTask(
                player,
                DiaryLevel.MEDIUM,
                MediumTasks.INTERACT_WITH_PET_ROCK
            )
        }

        // You can alternatively browse her regular clothing store to complete the task, no purchase necessary.
        if (event.target.id == NPCs.YRSA_1301 && event.option == "trade" && player.questRepository.isComplete("Fremennik Trials") && inBorders(player, YRSA_SHOP_BORDERS)) {
            finishTask(
                player,
                DiaryLevel.MEDIUM,
                MediumTasks.BROWSE_BOOT_COLORS_YRSA
            )
        }
    }

    override fun onSpellCast(player: Player, event: SpellCastEvent) {
        if (event.spellBook == SpellBookManager.SpellBook.LUNAR && event.spellId == 15) {
            finishTask(
                player,
                DiaryLevel.HARD,
                HardTasks.BAKE_PIE_WITH_MAGIC
            )
        }
    }

    override fun onButtonClicked(player: Player, event: ButtonClickEvent) {
        when {
            inBorders(player, RELLEKKA_BLACKSMITH_AREA) -> {
                if (event.iface == Components.CRAFTING_GLASS_542 && event.buttonId == 38 && player.skills.getLevel(Skills.CRAFTING) >= 33) {
                    finishTask(
                        player,
                        DiaryLevel.MEDIUM,
                        MediumTasks.MAKE_3_VIALS
                    )
                }
            }
        }
    }
}