package rs09.game.node.entity.player.link.diary.events

import api.events.*
import api.inBorders
import core.game.content.global.Bones
import core.game.content.global.travel.canoe.Canoe
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.world.map.zone.ZoneBorders
import org.rs09.consts.Components
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery
import rs09.game.content.dialogue.region.varrock.ElsieDialogue
import rs09.game.interaction.inter.FairyRing
import rs09.game.node.entity.player.link.diary.AreaDiaryTask
import rs09.game.node.entity.player.link.diary.DiaryEventHookBase
import rs09.game.node.entity.player.link.diary.DiaryLevel
import rs09.game.node.entity.skill.magic.TeleportMethod
import rs09.game.node.entity.skill.magic.spellconsts.Modern

class VarrockAchivementDiary : DiaryEventHookBase(DiaryType.VARROCK) {
    companion object {
        private val VARROCK_ROOF_AREA = ZoneBorders(3201, 3467, 3225, 3497, 3)
        private val SOS_LEVEL_2_AREA = ZoneBorders(2040, 5241, 2046, 5246)

        private val STRAY_DOGS = arrayOf(
            NPCs.STRAY_DOG_4766, NPCs.STRAY_DOG_4767,
            NPCs.STRAY_DOG_5917, NPCs.STRAY_DOG_5918
        )

        object EasyTasks {
            const val THESSALIA_BROWSE_CLOTHES = 0
            const val AUBURY_TELEPORT_ESSENCE_MINE = 1
            const val MINE_IRON_SOUTHEAST = 2
            const val MAKE_PLANK_SAWMILL = 3
            const val VISIT_SOS_LEVEL2 = 4
            const val JUMP_OVER_FENCE_SOUTH = 5
            const val LUMBERYARD_CHOP_DYING_TREE = 6
            const val BUY_VARROCK_HERALD = 7
            const val GIVE_STRAY_DOG_A_BONE = 8
            const val BARBARIAN_VILLAGE_SPIN_A_BOWL = 9
            const val EDGEVILLE_ENTER_DUNGEON_SOUTH = 10
            const val MOVE_POH_TO_VARROCK = 11
            const val SPEAK_TO_HAIG_HALEN_50QP = 12
            const val ENTER_EARTH_ALTAR = 13
            const val ELSIE_TELL_A_STORY = 14
            const val PATERDOMUS_MINE_LIMESTONE = 15
            const val BARBARIAN_VILLAGE_CATCH_TROUT = 16
            const val SEWERS_CUT_COBWEB = 17
            const val FIND_HIGHEST_POINT = 18
        }

        object MediumTasks {
            const val APOTHECARY_MAKE_STRENGTH_POTION = 0
            const val CHAMPIONS_GUILD_VISIT = 1
            const val TAKE_DAGONHAI_CHAOS_PORTAL_SHORTCUT = 2
            const val RAT_POLE_FULL_RAT_COMPLEMENT = 3
            const val SEWER_GATHER_RED_SPIDERS_EGGS = 4
            const val USE_SPIRIT_TREE_NORTH = 5
            const val PERFORM_ALL_SOS_EMOTES = 6
            const val SELECT_KITTEN_COLOR = 7
            const val USE_GE_UNDER_WALL_SHORTCUT = 8
            const val ENTER_SOULBANE_RIFT = 9
            const val DIGSITE_PENDANT_TELEPORT = 10
            const val CRAFT_EARTH_TIARA = 11
            const val PALACE_PICKPOCKET_GUARD = 12
            const val CAST_VARROCK_TELEPORT_SPELL = 13
            const val VANNAKA_GET_SLAYER_TASK = 14
            const val SAWMILL_BUY_20_MAHOGANY_PLANKS = 15
            const val PICK_FROM_WHITE_TREE = 16
            const val HOTAIR_BALLOON_TRAVEL_SOMEWHERE = 17
            const val GERTRUDE_GET_CAT_TRAINING_MEDAL = 18
            const val DIAL_FAIRY_RING_WEST = 19
            const val OZIACH_BROWSE_STORE = 20
        }

        object HardTasks {
            const val PICK_POISON_IVY_FARMING_PATCH = 0
            const val USE_MOSS_GIANT_PIPE_SHORTCUT = 1
            const val FANCY_DRESS_SELLER_TRADE_FURS = 2
            const val SMITH_ADAMANT_MED_HELM_SOUTHEAST = 3
            const val SPEAK_TO_ORLANDO_SMITH_153_KUDOS = 4
            const val GIVE_WEAKLAX_A_PIE = 5
            const val CRAFT_AIR_BATTLESTAFF = 6
            const val GIVE_POH_TROPICAL_WOOD_OR_FANCY_STONE_FINISH = 7
            const val MAKE_VARROCK_TELEPORT_TABLET_OR_MAHOGANY_LECTERN = 8
            const val OBTAIN_NEW_SET_OF_FAMILY_CREST_GAUNTLETS = 9
            const val EDGEVILLE_MAKE_WAKA_CANOE = 10
            const val EDGEVILLE_TELEPORT_USING_ANCIENT_MAGICKS = 11
            const val BARBARIAN_VILLAGE_TELEPORT_USING_SKULL_SCEPTRE = 12
        }
    }

    override val areaTasks get() = arrayOf(
        AreaDiaryTask(
            VARROCK_ROOF_AREA,
            DiaryLevel.EASY,
            EasyTasks.FIND_HIGHEST_POINT
        ),

        AreaDiaryTask(
            SOS_LEVEL_2_AREA,
            DiaryLevel.EASY,
            EasyTasks.VISIT_SOS_LEVEL2
        )
    )

    override fun onResourceProduced(player: Player, event: ResourceProducedEvent) {
        when (player.viewport.region.id) {
            12341 -> if (event.itemId == Items.RAW_TROUT_335) {
                finishTask(
                    player,
                    DiaryLevel.EASY,
                    EasyTasks.BARBARIAN_VILLAGE_CATCH_TROUT
                )
            }

            13108 -> if (event.itemId == Items.IRON_ORE_440) {
                finishTask(
                    player,
                    DiaryLevel.EASY,
                    EasyTasks.MINE_IRON_SOUTHEAST
                )
            }

            13110 -> {
                if (event.itemId == Items.LOGS_1511
                    && event.source.id == Scenery.DYING_TREE_24168
                ) {
                    finishTask(
                        player,
                        DiaryLevel.EASY,
                        EasyTasks.LUMBERYARD_CHOP_DYING_TREE
                    )
                }
            }

            13366 -> if (event.itemId == Items.LIMESTONE_3211) {
                finishTask(
                    player,
                    DiaryLevel.EASY,
                    EasyTasks.PATERDOMUS_MINE_LIMESTONE
                )
            }
        }
    }

    override fun onTeleported(player: Player, event: TeleportEvent) {
        when (event.source) {
            is NPC -> if (event.method == TeleportMethod.NPC && event.source.id == NPCs.AUBURY_553) {
                finishTask(
                    player,
                    DiaryLevel.EASY,
                    EasyTasks.AUBURY_TELEPORT_ESSENCE_MINE
                )
            }
        }
    }

    override fun onInteracted(player: Player, event: InteractionEvent) {
        when (player.viewport.region.id) {
            12342 -> if (event.target.id == 26934) {
                finishTask(
                    player,
                    DiaryLevel.EASY,
                    EasyTasks.EDGEVILLE_ENTER_DUNGEON_SOUTH
                )
            }
        }
    }

    override fun onButtonClicked(player: Player, event: ButtonClickEvent) {
        /* This gets fired even on the login screen, and we don't have a region there, so... */
        player.viewport.region?.let {
            when (it.id) {
                12342 -> {
                    if (event.iface == Components.CANOE_52
                        && Canoe.getCanoeFromChild(event.buttonId) == Canoe.WAKA) {
                        finishTask(
                            player,
                            DiaryLevel.HARD,
                            HardTasks.EDGEVILLE_MAKE_WAKA_CANOE
                        )
                    }
                }
            }
        }
    }

    override fun onDialogueOptionSelected(player: Player, event: DialogueOptionSelectionEvent) {
        when (event.dialogue) {
            is ElsieDialogue -> if (event.currentStage == 12) {
                finishTask(
                    player,
                    DiaryLevel.EASY,
                    EasyTasks.ELSIE_TELL_A_STORY
                )
            }
        }
    }

    override fun onUsedWith(player: Player, event: UseWithEvent) {
        when (event.used) {
            in Bones.array -> if (event.with in STRAY_DOGS) {
                finishTask(
                    player,
                    DiaryLevel.EASY,
                    EasyTasks.GIVE_STRAY_DOG_A_BONE
                )
            }
        }
    }

    override fun onInterfaceOpened(player: Player, event: InterfaceOpenEvent) {
        when (event.component.id) {
            Components.THESSALIA_CLOTHES_MALE_591,
            Components.THESSALIA_CLOTHES_FEMALE_594 -> {
                finishTask(
                    player,
                    DiaryLevel.EASY,
                    EasyTasks.THESSALIA_BROWSE_CLOTHES
                )
            }
        }
    }

    override fun onSpellCast(player: Player, event: SpellCastEvent) {
        when (event.spellId) {
            Modern.VARROCK_TELEPORT -> {
                finishTask(
                    player,
                    DiaryLevel.MEDIUM,
                    MediumTasks.CAST_VARROCK_TELEPORT_SPELL
                )
            }
        }
    }

    override fun onFairyRingDialed(player: Player, event: FairyRingDialEvent) {
        if (event.fairyRing == FairyRing.DKR) {
            finishTask(
                player,
                DiaryLevel.MEDIUM,
                MediumTasks.DIAL_FAIRY_RING_WEST
            )
        }
    }
}