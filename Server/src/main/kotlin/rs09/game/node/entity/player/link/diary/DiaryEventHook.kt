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



class DiaryEventHook : LoginListener {
    override fun login(player: Player) {
        player.hook(Event.ResourceProduced, DiaryGatherHooks)
        player.hook(Event.NPCKilled, DiaryCombatHooks)
        player.hook(Event.Interaction, DiaryInteractionEvents)
        player.hook(Event.PickedUp, DiaryPickupEvents)
    }

    private object DiaryInteractionEvents : EventHook<InteractionEvent> {
        override fun process(entity: Entity, event: InteractionEvent) {
            if (entity !is Player) return
            val regionId = entity.viewport.region.id

            if (event.target is core.game.node.scenery.Scenery)
                when (event.target.id) {
                    29944 -> if(regionId == 10552 && event.option == "renew-points") finishTask(entity, DiaryType.FREMENNIK,0,8)
                    //18137 -> if(regionId == 10810 && event.option == "examine") finishTask(entity, DiaryType.FREMENNIK,0,2)
                    //2112 -> if()
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

                Items.GOLD_ORE_444 -> if (regionId == 10802) finishTask(entity, DiaryType.KARAMJA, 0, 2)

                Items.UNCUT_RED_TOPAZ_1629 -> if (regionId == 11310 || regionId == 11410) finishTask(
                    entity,
                    DiaryType.KARAMJA,
                    1,
                    18
                )
            }

            if (event.source.id == NPCs.FISHING_SPOT_333 && entity.zoneMonitor.isInZone("karamja"))
                finishTask(entity, DiaryType.KARAMJA, 0, 6)

            if(event.source.id == NPCs.FISHING_SPOT_324 && regionId == 10553)
                finishTask(entity, DiaryType.FREMENNIK, 0, 7)

                //Fish off of any of Rellekka's piers
                //in RellekkaFishingSpots -> {
                   // if(entity.viewport.region.id == 10553)
                        //finishTask(entity, DiaryType.FREMENNIK, 0, 7)
                //}
        }
    }

    private object DiaryCombatHooks : EventHook<NPCKillEvent> {
        val elementalNPCs = intArrayOf(1019, 1020, 1021, 1022)
        val metalDragons =
            intArrayOf(NPCs.BRONZE_DRAGON_1590, NPCs.IRON_DRAGON_1591, NPCs.STEEL_DRAGON_1592, NPCs.STEEL_DRAGON_3590)
        val lumZombies = intArrayOf(NPCs.ZOMBIE_73, NPCs.ZOMBIE_74)
        val fremCrabs = intArrayOf(NPCs.ROCK_CRAB_1265,NPCs.ROCK_CRAB_1267,NPCs.GIANT_ROCK_CRAB_2452,NPCs.GIANT_ROCK_CRAB_2885)
        val fremCrawlers = intArrayOf(NPCs.CAVE_CRAWLER_1600,NPCs.CAVE_CRAWLER_1601,NPCs.CAVE_CRAWLER_1602,NPCs.CAVE_CRAWLER_1603,NPCs.CAVE_CRAWLER_7787,NPCs.CAVE_CRAWLER_7812)

        override fun process(entity: Entity, event: NPCKillEvent) {
            if (entity !is Player) return
            when (event.npc.id) {
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
