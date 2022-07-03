package rs09.game.node.entity.player.link.diary

import api.*
import api.events.*
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.slayer.Tasks
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import org.rs09.consts.Components
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery
import rs09.game.Event
import rs09.game.system.command.Privilege

private const val TASK_DRAYNOR_BANK = 15
private const val TASK_LUMBRIDGE_GENERAL_STORE = 18

class DiaryEventHook : LoginListener {
    override fun login(player: Player) {
        player.hook(Event.ResourceProduced, DiaryGatherHooks)
        player.hook(Event.Teleport, DiaryTeleportHooks)
        player.hook(Event.NPCKilled, DiaryCombatHooks)
        player.hook(Event.FireLit, DiaryFireHooks)
        player.hook(Event.Interaction, DiaryInteractionEvents)
        player.hook(Event.InterfaceOpened, DiaryInterfaceOpenEvents)
        player.hook(Event.InterfaceClosed, DiaryInterfaceCloseEvents)
        player.hook(Event.PickedUp, DiaryPickupEvents)
    }

    companion object {
        private fun finishTask(entity: Player, diary: DiaryType, index: Int, task: Int) {
            entity.achievementDiaryManager.finishTask(entity, diary, index, task)
        }
        private fun taskCompleted(entity: Player, diary: DiaryType,index: Int,task: Int): Boolean {
            return entity.achievementDiaryManager.hasCompletedTask(diary,index,task)
        }
    }

    private object DiaryInterfaceCloseEvents : EventHook<InterfaceCloseEvent> {
        override fun process(entity: Entity, event: InterfaceCloseEvent) {
            // TODO("Not yet implemented")
        }
    }

    private object DiaryInterfaceOpenEvents : EventHook<InterfaceOpenEvent> {
        override fun process(entity: Entity, event: InterfaceOpenEvent) {
            if (entity !is Player)
                return

            when (entity.viewport.region.id) {
                12338 -> if (event.component.id == Components.BANK_V2_MAIN_762) {
                    finishTask(
                        entity,
                        DiaryType.LUMBRIDGE,
                        1,
                        TASK_DRAYNOR_BANK
                    )
                }

                12850 -> if (event.component.id == Components.SHOP_TEMPLATE_620) {
                    finishTask(
                        entity,
                        DiaryType.LUMBRIDGE,
                        0,
                        TASK_LUMBRIDGE_GENERAL_STORE
                    )
                }
            }
        }
    }

    private object DiaryInteractionEvents : EventHook<InteractionEvent> {
        override fun process(entity: Entity, event: InteractionEvent) {
            if (entity !is Player) return
            val regionId = entity.viewport.region.id

            if (event.target is core.game.node.scenery.Scenery)
                when (event.target.id) {
                    11729, 11727 -> if (regionId == 11828) finishTask(entity, DiaryType.FALADOR, 0, 3)
                    11889 -> if (regionId == 11572 && isEquipped(entity, Items.PROSELYTE_SALLET_9672) && isEquipped(
                            entity,
                            Items.PROSELYTE_HAUBERK_9674
                        ) && isEquipped(entity, Items.PROSELYTE_CUISSE_9676)
                    ) finishTask(entity, DiaryType.FALADOR, 2, 0)
                    30941 -> if (regionId == 12184) finishTask(entity, DiaryType.FALADOR, 2, 6)
                    36771 -> if (regionId == 12850) finishTask(entity, DiaryType.LUMBRIDGE, 0, 0)
                    12537 -> if (regionId == 12337) finishTask(entity, DiaryType.LUMBRIDGE, 0, 11)
                    26934 -> if (regionId == 12342) finishTask(entity, DiaryType.VARROCK, 0, 10)
                    24350, 24361 -> if (regionId == 12854) finishTask(entity, DiaryType.VARROCK, 0, 18)
                    in 115..122 -> if (event.option == "burst") finishTask(entity, DiaryType.FALADOR, 0, 12)
                    16149 -> finishTask(entity, DiaryType.VARROCK, 0, 4)
                    29944 -> if(regionId == 10552 && event.option == "renew-points") finishTask(entity, DiaryType.FREMENNIK,0,8)
                    //18137 -> if(regionId == 10810 && event.option == "examine") finishTask(entity, DiaryType.FREMENNIK,0,2)
                    //2112 -> if()
                }
        }
    }

    private object DiaryFireHooks : EventHook<LitFireEvent> {
        val lumCastleBorders = ZoneBorders(3216, 3207, 3225, 3233, 0)
        override fun process(entity: Entity, event: LitFireEvent) {
            if (entity !is Player) return
            val region = entity.viewport.region.id

            when (region) {
                10806 -> if (event.logId == Items.MAGIC_LOGS_1513) finishTask(entity, DiaryType.SEERS_VILLAGE, 2, 5)
                12593, 12849 -> if (event.logId == Items.LOGS_1511) finishTask(entity, DiaryType.LUMBRIDGE, 1, 9)

                else -> {
                    if (event.logId == Items.WILLOW_LOGS_1519 && lumCastleBorders.insideBorder(entity))
                        finishTask(entity, DiaryType.LUMBRIDGE, 2, 3)
                }
            }
        }
    }

    private object DiaryPickupEvents : EventHook<PickUpEvent>
    {
        override fun process(entity: Entity, event: PickUpEvent) {
            if(entity !is Player) return
            val regionId = entity.viewport.region.id

            val Karamja = intArrayOf(10801,10802,11053,11054,11055,11056,11057,11058,11309,
                11310,11311,11312,11313,11314,11565,11566,11567,11568,11569,11821,11822,11823)

                when(event.itemId){

                    Items.SEAWEED_401 -> {
                        if(regionId == 10810 && !taskCompleted(entity,DiaryType.FREMENNIK,0,5)){
                            when(entity.getAttribute("RellekaSeaweed",0)){
                                0 -> entity.setAttribute("/save:RellekaSeaweed",1)
                                1 -> entity.incrementAttribute("RellekaSeaweed")
                                2 -> {
                                    finishTask(entity, DiaryType.FREMENNIK,0,5)
                                    entity.removeAttribute("RellekkaSeaweed")
                                }
                            }
                        }
                        if(regionId in Karamja && !taskCompleted(entity,DiaryType.KARAMJA, 0, 7)){
                            when(entity.getAttribute("KaramjaSeaweed", 0)){
                                0 -> entity.setAttribute("/save:KaramjaSeaweed",1)
                                in 1..3 -> entity.incrementAttribute("KaramjaSeaweed")
                                4 -> {
                                    finishTask(entity,DiaryType.KARAMJA,0,7)
                                    entity.removeAttribute("KaramjaSeaweed")
                                }
                            }
                        }
                    }

                    Items.PALM_LEAF_2339 -> {
                        if(regionId in Karamja && !taskCompleted(entity,DiaryType.KARAMJA,2,7)){
                            when(entity.getAttribute("KaramjaPalms",0)){
                                0 -> entity.setAttribute("/save:KaramjaPalms",1)
                                in 1..3 -> entity.incrementAttribute("KaramjaPalms")
                                4 -> {
                                    finishTask(entity,DiaryType.KARAMJA,2,7)
                                    entity.removeAttribute("KaramjaPalms")
                                }
                            }
                        }
                    }
                }
            }
    }

    private object DiaryGatherHooks : EventHook<ResourceProducedEvent> {
        override fun process(entity: Entity, event: ResourceProducedEvent) {
            if (entity !is Player) return
            val regionId = entity.viewport.region.id
            val RellekkaFishingSpots = intArrayOf(NPCs.FISHING_SPOT_324, NPCs.FISHING_SPOT_334, NPCs.FISHING_SPOT_322, NPCs.FISHING_SPOT_309)
            when(event.itemId)
            {
                //Cut a log from a teak tree
                Items.TEAK_LOGS_6333 -> finishTask(entity, DiaryType.KARAMJA, 1, 7)
                //Cut a log from a mahogany tree
                Items.MAHOGANY_LOGS_6332 -> finishTask(entity, DiaryType.KARAMJA, 1, 8)
                Items.LOGS_1511 -> {
                    //Cut down a dying tree in the Lumber Yard
                    if (event.source.id == Scenery.DYING_TREE_24168 && regionId == 13110)
                        finishTask(entity, DiaryType.VARROCK, 0, 6)
                }
                Items.YEW_LOGS_1515 -> {
                    if (regionId == 10806) {
                        setAttribute(
                            entity,
                            "/save:diary:seers:cut-yew",
                            getAttribute(entity, "diary:seers:cut-yew", 0) + 1
                        )
                        if (getAttribute(entity, "diary:seers:cut-yew", 0) >= 5)
                            finishTask(entity, DiaryType.SEERS_VILLAGE, 2, 1)
                    }
                }
                Items.WILLOW_LOGS_1519 -> if (regionId == 12850) finishTask(entity, DiaryType.LUMBRIDGE, 2, 6)

                Items.RAW_MACKEREL_353 -> if (regionId == 11317) finishTask(entity, DiaryType.SEERS_VILLAGE, 0, 11)

                Items.RAW_BASS_363 -> if (regionId == 11317 && !getAttribute(
                        entity,
                        "diary:seers:bass-caught",
                        false
                    )
                ) entity.setAttribute("/save:diary:seers:bass-caught", true)

                Items.RAW_SHARK_383 -> if (regionId == 11317) setAttribute(
                    entity,
                    "/save:diary:seers:shark-caught",
                    getAttribute(entity, "diary:seers:shark-caught", 0) + 1
                )

                Items.RAW_SHRIMPS_2514, Items.RAW_SHRIMPS_317 -> if (regionId == 12849) finishTask(
                    entity,
                    DiaryType.LUMBRIDGE,
                    0,
                    13
                )

                Items.RAW_PIKE_349 -> if (regionId == 12850) finishTask(entity, DiaryType.LUMBRIDGE, 1, 4)

                Items.RAW_SALMON_331 -> if (regionId == 12850) finishTask(entity, DiaryType.LUMBRIDGE, 2, 9)

                Items.RAW_TROUT_335 -> if (regionId == 12341) finishTask(entity, DiaryType.VARROCK, 0, 16)

                Items.IRON_ORE_440 -> {
                    if (regionId == 13108) finishTask(entity, DiaryType.VARROCK, 0, 2)
                    else if (regionId == 13107) finishTask(entity, DiaryType.LUMBRIDGE, 1, 0)
                }

                Items.LIMESTONE_3211 -> if (regionId == 13366) finishTask(entity, DiaryType.VARROCK, 0, 15)

                Items.GOLD_ORE_444 -> if (regionId == 10802) finishTask(entity, DiaryType.KARAMJA, 0, 2)

                Items.UNCUT_RED_TOPAZ_1629 -> if (regionId == 11310 || regionId == 11410) finishTask(
                    entity,
                    DiaryType.KARAMJA,
                    1,
                    18
                )

                Items.SOFT_CLAY_1761 -> if (regionId == 12596) finishTask(entity, DiaryType.LUMBRIDGE, 0, 5)

                Items.COPPER_ORE_436 -> if (regionId == 12849) finishTask(entity, DiaryType.LUMBRIDGE, 0, 12)

                Items.SILVER_ORE_442 -> if (regionId == 13107) finishTask(entity, DiaryType.LUMBRIDGE, 2, 10)

                Items.COAL_453 -> if (regionId == 12593) finishTask(entity, DiaryType.LUMBRIDGE, 2, 11)

                Items.BASS_365 -> if (regionId == 11317 && getAttribute(
                        entity,
                        "diary:seers:bass-caught",
                        false
                    )
                ) finishTask(entity, DiaryType.SEERS_VILLAGE, 1, 11)

                Items.SHARK_385 -> if (regionId == 11317 && getAttribute(
                        entity,
                        "diary:seers:shark-cooked",
                        false
                    ) && isEquipped(entity, Items.COOKING_GAUNTLETS_775)
                ) entity.incrementAttribute("/save:diary:seers:shark-cooked")

                Items.LOBSTER_379 -> if (event.source.id == Scenery.COOKING_RANGE_114) finishTask(
                    entity,
                    DiaryType.LUMBRIDGE,
                    2,
                    4
                )
            }

            when (event.original) {
                Items.RAW_RAT_MEAT_2134 -> if ((regionId == 12593 || regionId == 12849) && event.itemId == Items.COOKED_MEAT_2142) finishTask(
                    entity,
                    DiaryType.LUMBRIDGE,
                    1,
                    10
                )
            }

            if (getAttribute(entity, "diary:seers:shark-caught", 0) >= 5)
                finishTask(entity, DiaryType.SEERS_VILLAGE, 2, 7)
            if (getAttribute(entity, "diary:seers:shark-cooked", 0) >= 5)
                finishTask(entity, DiaryType.SEERS_VILLAGE, 2, 8)

            if ((regionId == 12593 || regionId == 12849) && event.source.name.startsWith("dead", true))
                finishTask(entity, DiaryType.LUMBRIDGE, 1, 8)

            if (event.source.id == NPCs.FISHING_SPOT_333 && entity.zoneMonitor.isInZone("karamja"))
                finishTask(entity, DiaryType.KARAMJA, 0, 6)

            if (event.source.id == Scenery.YEW_TREE_8513 && regionId == 11828)
                finishTask(entity, DiaryType.FALADOR, 2, 3)

            if (event.source.id == Scenery.COOKING_RANGE_114 && regionId == 12850)
                finishTask(entity, DiaryType.LUMBRIDGE, 0, 7)

            if(event.source.id == NPCs.FISHING_SPOT_324 && regionId == 10553)
                finishTask(entity, DiaryType.FREMENNIK, 0, 7)

                //Fish off of any of Rellekka's piers
                //in RellekkaFishingSpots -> {
                   // if(entity.viewport.region.id == 10553)
                        //finishTask(entity, DiaryType.FREMENNIK, 0, 7)
                //}
        }
    }

    private object DiaryTeleportHooks : EventHook<TeleportEvent> {
        val rangingGuild = Location(2657, 3439, 0)
        override fun process(entity: Entity, event: TeleportEvent) {
            if(entity !is Player) return
            when(event.source){
                1 -> if (event.location.withinDistance(rangingGuild)) {
                    finishTask(entity, DiaryType.SEERS_VILLAGE, 2, 0)
                }
            }
        }
    }

    private object DiaryCombatHooks : EventHook<NPCKillEvent> {
        val faladorDucks = intArrayOf(NPCs.DUCK_46, NPCs.DUCK_2693, NPCs.DUCK_6113)
        val lumbridgeCows = intArrayOf(81, 397, 955, 1766, 1767, 3309)
        val elementalNPCs = intArrayOf(1019, 1020, 1021, 1022)
        val wyverns = Tasks.SKELETAL_WYVERN.npcs
        val metalDragons =
            intArrayOf(NPCs.BRONZE_DRAGON_1590, NPCs.IRON_DRAGON_1591, NPCs.STEEL_DRAGON_1592, NPCs.STEEL_DRAGON_3590)
        val lumZombies = intArrayOf(NPCs.ZOMBIE_73, NPCs.ZOMBIE_74)
        val fremCrabs = intArrayOf(NPCs.ROCK_CRAB_1265,NPCs.ROCK_CRAB_1267,NPCs.GIANT_ROCK_CRAB_2452,NPCs.GIANT_ROCK_CRAB_2885)
        val fremCrawlers = intArrayOf(NPCs.CAVE_CRAWLER_1600,NPCs.CAVE_CRAWLER_1601,NPCs.CAVE_CRAWLER_1602,NPCs.CAVE_CRAWLER_1603,NPCs.CAVE_CRAWLER_7787,NPCs.CAVE_CRAWLER_7812)

        override fun process(entity: Entity, event: NPCKillEvent) {
            if (entity !is Player) return
            when (event.npc.id) {
                //FALADOR LEVEL 0 TASK 10
                in faladorDucks -> {
                    if (entity.location.withinDistance(Location(2991, 3383, 0)))
                        finishTask(entity, DiaryType.FALADOR, 0, 9)
                }

                in lumbridgeCows -> {
                    // Obtain a cow-hide from a cow in the field north-east of Lumbridge
                    if (entity.viewport.region.id == 12850 || entity.viewport.region.id == 12851) {
                        finishTask(entity, DiaryType.LUMBRIDGE, 1, 1)
                    }
                }

                in fremCrawlers ->{
                    if(entity.viewport.region.id == 11164){
                        finishTask(entity,DiaryType.FREMENNIK,0,0)
                    }
                }

                in fremCrabs -> {
                    if (entity.viewport.region.id == 10810 || entity.viewport.region.id == 10042) {
                        when(entity.getAttribute("FremCrabs",0)){
                            0 -> entity.setAttribute("/save:FremCrabs",1)
                            in 1..3 -> entity.incrementAttribute("FremCrabs")
                            4 -> finishTask(entity,DiaryType.FREMENNIK, 0, 1)
                        }
                    }
                }

                NPCs.BLACK_UNICORN_133 -> {
                    if (entity.viewport.region.id in 10808..10809){
                        finishTask(entity,DiaryType.FREMENNIK,0,9)
                    }
                }

                NPCs.JOGRE_113 -> {
                    if (entity.viewport.region.id == 11412)
                        finishTask(entity, DiaryType.KARAMJA, 0, 9)
                }

                in elementalNPCs -> {
                    if (!entity.location.withinDistance(Location(2719, 9889, 0), 100))
                        return
                    when (event.npc.id) {
                        1019 -> setAttribute(entity, "/save:diary:seers:elemental:fire", true)
                        1020 -> setAttribute(entity, "/save:diary:seers:elemental:earth", true)
                        1021 -> setAttribute(entity, "/save:diary:seers:elemental:air", true)
                        1022 -> setAttribute(entity, "/save:diary:seers:elemental:water", true)
                    }
                    for (element in arrayOf("fire", "earth", "air", "water"))
                        if (!getAttribute(entity, "diary:seers:$element", false))
                            return
                    finishTask(entity, DiaryType.SEERS_VILLAGE, 1, 4)
                }

                NPCs.KET_ZEK_2743, NPCs.KET_ZEK_2744 -> finishTask(entity, DiaryType.KARAMJA, 2, 1)

                in metalDragons -> {
                    if (entity.viewport.region.id == 10899 || entity.viewport.region.id == 10900)
                        finishTask(entity, DiaryType.KARAMJA, 2, 9)
                }

                in wyverns -> finishTask(entity, DiaryType.FALADOR, 2, 8)

                in lumZombies -> {
                    if (entity.viewport.region.id == 12438 || entity.viewport.region.id == 12439)
                        finishTask(entity, DiaryType.LUMBRIDGE, 1, 18)
                }

                NPCs.GIANT_RAT_86 -> {
                    if (entity.viewport.region.id == 12593 || entity.viewport.region.id == 12849)
                        finishTask(entity, DiaryType.LUMBRIDGE, 1, 7)
                }

                NPCs.TOWER_ARCHER_688 -> setAttribute(
                    entity,
                    "/save:diary:seers:tower-archers",
                    getAttribute(entity, "diary:seers:tower-archers", 0) or 1
                )
                NPCs.TOWER_ARCHER_689 -> setAttribute(
                    entity,
                    "/save:diary:seers:tower-archers",
                    getAttribute(entity, "diary:seers:tower-archers", 0) or (1 shl 1)
                )
                NPCs.TOWER_ARCHER_690 -> setAttribute(
                    entity,
                    "/save:diary:seers:tower-archers",
                    getAttribute(entity, "diary:seers:tower-archers", 0) or (1 shl 2)
                )
                NPCs.TOWER_ARCHER_691 -> setAttribute(
                    entity,
                    "/save:diary:seers:tower-archers",
                    getAttribute(entity, "diary:seers:tower-archers", 0) or (1 shl 3)
                )

            }

            if (getAttribute(entity, "diary:seers:tower-archers", 0) == 0xF)
                finishTask(entity, DiaryType.SEERS_VILLAGE, 1, 6)
        }
    }
}
