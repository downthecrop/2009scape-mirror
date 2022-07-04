package rs09.game.node.entity.player.link.diary.events

import api.MapArea
import api.events.EventHook
import api.events.InteractionEvent
import api.events.ResourceProducedEvent
import api.events.SpellCastEvent
import api.inBorders
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.world.map.zone.ZoneBorders
import org.rs09.consts.Items
import org.rs09.consts.Scenery
import rs09.game.Event
import rs09.game.node.entity.player.link.diary.DiaryEventHookBase
import rs09.game.node.entity.player.link.diary.DiaryLevel
import rs09.game.node.entity.skill.magic.spellconsts.Modern

class VarrockDiaryEventHooks : MapArea, DiaryEventHookBase() {
    companion object {
        private val VARROCK_ROOF_AREA = ZoneBorders(3201, 3467, 3225, 3497, 3)
        private val SOS_LEVEL_2_AREA = ZoneBorders(2040, 5241, 2046, 5246)

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
            const val DIAL_TO_FAIRY_RING_WEST = 19
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

    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(
            VARROCK_ROOF_AREA,
            SOS_LEVEL_2_AREA
        )
    }

    override fun areaEnter(entity: Entity) {
        if (entity !is Player) return

        when {
            inBorders(entity, VARROCK_ROOF_AREA) -> {
                finishTask(
                    entity,
                    DiaryType.VARROCK,
                    DiaryLevel.EASY,
                    EasyTasks.FIND_HIGHEST_POINT
                )
            }

            inBorders(entity, SOS_LEVEL_2_AREA) -> {
                finishTask(
                    entity,
                    DiaryType.VARROCK,
                    DiaryLevel.EASY,
                    EasyTasks.VISIT_SOS_LEVEL2
                )
            }
        }
    }

    override fun login(player: Player) {
        player.hook(Event.Interaction, InteractionEvents)
        player.hook(Event.ResourceProduced, ResourceProductionEvents)
        player.hook(Event.SpellCast, SpellCastEvents)
    }

    private object InteractionEvents : EventHook<InteractionEvent> {
        override fun process(entity: Entity, event: InteractionEvent) {
            if (entity !is Player) return

            when (entity.viewport.region.id) {
                12342 -> {
                    if (event.target.id == 26934) {
                        finishTask(
                            entity,
                            DiaryType.VARROCK,
                            DiaryLevel.EASY,
                            EasyTasks.EDGEVILLE_ENTER_DUNGEON_SOUTH
                        )
                    }
                }
            }
        }
    }

    private object ResourceProductionEvents : EventHook<ResourceProducedEvent> {
        override fun process(entity: Entity, event: ResourceProducedEvent) {
            if (entity !is Player) return

            when (entity.viewport.region.id) {
                12341 -> if (event.itemId == Items.RAW_TROUT_335) {
                    finishTask(
                        entity,
                        DiaryType.VARROCK,
                        DiaryLevel.EASY,
                        EasyTasks.BARBARIAN_VILLAGE_CATCH_TROUT
                    )
                }

                13108 -> if (event.itemId == Items.IRON_ORE_440) {
                    finishTask(
                        entity,
                        DiaryType.VARROCK,
                        DiaryLevel.EASY,
                        EasyTasks.MINE_IRON_SOUTHEAST
                    )
                }

                13110 -> {
                    if (event.itemId == Items.LOGS_1511
                        && event.source.id == Scenery.DYING_TREE_24168) {
                        finishTask(
                            entity,
                            DiaryType.VARROCK,
                            DiaryLevel.EASY,
                            EasyTasks.LUMBERYARD_CHOP_DYING_TREE
                        )
                    }
                }

                13366 -> if (event.itemId == Items.LIMESTONE_3211) {
                    finishTask(
                        entity,
                        DiaryType.VARROCK,
                        DiaryLevel.EASY,
                        EasyTasks.PATERDOMUS_MINE_LIMESTONE
                    )
                }
            }
        }
    }

    private object SpellCastEvents : EventHook<SpellCastEvent> {
        override fun process(entity: Entity, event: SpellCastEvent) {
            if (entity !is Player) return

            when (event.spellId) {
                Modern.VARROCK_TELEPORT -> {
                    finishTask(
                        entity,
                        DiaryType.VARROCK,
                        DiaryLevel.MEDIUM,
                        MediumTasks.CAST_VARROCK_TELEPORT_SPELL
                    )
                }
            }
        }
    }
}