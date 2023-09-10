package content.region.kandarin.quest.grandtree

import content.region.kandarin.quest.grandtree.TheGrandTree.Companion.questName
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Sounds

class GrandTreeListeners: InteractionListener {

    val roots = arrayOf(
        Location(2467,9896,0),
        Location(2468,9890,0),
        Location(2465,9891,0),
        Location(2465,9891,0),
        Location(2473,9897,0),
    )

    val hazelmerescroll = Items.HAZELMERES_SCROLL_786

    val hazelmerescrollText = arrayOf(
    "<col=FFF900>Es lemanto meso pro eis prit ta Cinqo mond.</col>",
    "<col=FFF900>Mi lovos ta lemanto Daconia arpos</col>",
    "<col=FFF900>et Daconia arpos eto meriz ta priw!</col>",
    )

    fun unlockTUZODoor(player: Player) {
        if (getAttribute(player, "/save:grandtree:twig1", false) &&
            getAttribute(player, "/save:grandtree:twig2", false) &&
            getAttribute(player, "/save:grandtree:twig3", false) &&
            getAttribute(player, "/save:grandtree:twig4", false)
        ){
            sendDialogue(player,"With a grinding of machinery, a trapdoor snaps open!")
            //SceneryBuilder.replace(Scenery(2444, Location(2487,3464,2), 22 , 2), Scenery(2445, Location(2487,3464,2), 22 , 2),20)
        }
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.NPC, intArrayOf(NPCs.CHARLIE_673),"talk-to"){ player, _ ->
            return@setDest player.location
        }
    }

    override fun defineListeners() {
        on(NPCs.KING_NARNODE_SHAREEN_670, IntType.NPC, "talk-to"){ player, npc ->
            val aboveground = 9782
            if(player.location.regionId == aboveground)
                openDialogue(player, KingNarnodeDialogue(), npc)
            else
                openDialogue(player, KingNarnodeUnderGroundDialogue(), npc)
            return@on true
        }
        on(NPCs.HAZELMERE_669, IntType.NPC, "talk-to"){ player, npc ->
            openDialogue(player, HazelmereDialogue(), npc)
            return@on true
        }
        on(NPCs.GLOUGH_671, IntType.NPC, "talk-to"){ player, npc ->
            openDialogue(player, GloughDialogue(), npc)
            return@on true
        }
        on(intArrayOf(NPCs.SHIPYARD_WORKER_675, NPCs.SHIPYARD_WORKER_38, NPCs.SHIPYARD_WORKER_39), IntType.NPC, "talk-to"){ player, npc ->
            openDialogue(player, ShipyardWorkerGenericDialogue(), npc)
            return@on true
        }
        on(NPCs.CHARLIE_673, IntType.NPC, "talk-to"){ player, npc ->
            openDialogue(player, CharlieDialogue(), npc)
            return@on true
        }
        on(NPCs.CAPTAIN_ERRDO_3811, IntType.NPC, "talk-to"){ player, npc ->
            openDialogue(player, CaptainErrdoDialogue(), npc)
            return@on true
        }
        on(NPCs.ANITA_672, IntType.NPC, "talk-to"){ player, npc ->
            openDialogue(player, AnitaDialogue(), npc)
            return@on true
        }
        on(hazelmerescroll, IntType.ITEM, "read") { player, node ->
            hazelmereScroll(player, node.asItem())
            return@on true
        }

        on(2444, IntType.SCENERY, "open"){ player, node ->
            if(node.location == Location(2487,3464,2) && !isQuestComplete(player, questName)){
                if(getAttribute(player, "/save:grandtree:twig1", false) &&
                    getAttribute(player, "/save:grandtree:twig2", false) &&
                    getAttribute(player, "/save:grandtree:twig3", false) &&
                    getAttribute(player, "/save:grandtree:twig4", false)){
                    player.animator.animate(Animation(828))
                    BlackDemonCutscene(player).start()
                }
            }
            return@on true
        }

        on(2446, IntType.SCENERY, "open"){ player, node ->
            if(node.location == Location(2463, 3497, 0) && isQuestComplete(player!!, questName)){
                player.animator.animate(Animation(828))
                // Go to tunnels
                teleport(player, Location(2464, 9897, 0))
            }
            return@on true
        }

        onUseWith(IntType.SCENERY, 788, 2436){ player, used, with ->
            SceneryBuilder.replace(Scenery(2436, Location(2482,3462,1)),Scenery(2437, Location(2482,3462,1)),2)
            sendDialogue(player,"You found a scroll!")
            addItemOrDrop(player, Items.INVASION_PLANS_794)
            if(getQuestStage(player!!, questName) < 60)
                setQuestStage(player!!, questName, 60)
            return@onUseWith true
        }
        onUseWith(IntType.SCENERY, Items.TWIGS_789, 2440){ player, used, with ->
            setAttribute(player, "/save:grandtree:twig1", true)
            removeItem(player, used.asItem())
            GroundItemManager.create(used.asItem(), with.location, player)
            unlockTUZODoor(player)
            return@onUseWith true
        }
        onUseWith(IntType.SCENERY, Items.TWIGS_790, 2441){ player, used, with ->
            setAttribute(player, "/save:grandtree:twig2", true)
            removeItem(player, used.asItem())
            GroundItemManager.create(used.asItem(), with.location, player)
            unlockTUZODoor(player)
            return@onUseWith true
        }
        onUseWith(IntType.SCENERY, Items.TWIGS_791, 2442){ player, used, with ->
            setAttribute(player, "/save:grandtree:twig3", true)
            removeItem(player, used.asItem())
            GroundItemManager.create(used.asItem(), with.location, player)
            unlockTUZODoor(player)
            return@onUseWith true
        }
        onUseWith(IntType.SCENERY, Items.TWIGS_792, 2443){ player, used, with ->
            setAttribute(player, "/save:grandtree:twig4", true)
            removeItem(player, used.asItem())
            GroundItemManager.create(used.asItem(), with.location, player)
            unlockTUZODoor(player)
            return@onUseWith true
        }
        // Removing twigs
        on(Items.TWIGS_789, IntType.GROUNDITEM, "take"){ player, node ->
            setAttribute(player, "/save:grandtree:twig1", false)
            return@on true
        }
        on(Items.TWIGS_790, IntType.GROUNDITEM, "take"){ player, node ->
            setAttribute(player, "/save:grandtree:twig2", false)
            return@on true
        }
        on(Items.TWIGS_791, IntType.GROUNDITEM, "take"){ player, node ->
            setAttribute(player, "/save:grandtree:twig3", false)
            return@on true
        }
        on(Items.TWIGS_792, IntType.GROUNDITEM, "take"){ player, node ->
            setAttribute(player, "/save:grandtree:twig4", false)
            return@on true
        }
        on(2435, IntType.SCENERY, "search"){ player, _ ->
            if(getQuestStage(player, questName) == 47){
                sendItemDialogue(player, Items.GLOUGHS_JOURNAL_785,"You've found Glough's Journal!")
                addItemOrDrop(player, Items.GLOUGHS_JOURNAL_785)
            }
            return@on true
        }

        // Roots for Daconia rock
        on(32319, IntType.SCENERY, "search"){ player, node ->
            if(getQuestStage(player, questName) < 99 || player.hasItem(Item(Items.DACONIA_ROCK_793))){ return@on true; }
            // RNG for which root the rock is under
            if(node.location == roots[getAttribute(player,"grandtree:rock",1)]){
                sendItemDialogue(player, Item(Items.DACONIA_ROCK_793), "You've found a Daconia rock!")
                addItemOrDrop(player,Items.DACONIA_ROCK_793)
            }
            return@on true
        }
        // Gate Karamja
        on(2439, IntType.SCENERY, "open"){ player, _ ->
            openDialogue(player, ShipyardWorkerDialogue(), NPC(NPCs.SHIPYARD_WORKER_675))
            return@on true
        }
        on(2438, IntType.SCENERY, "open"){ player, _ ->
            openDialogue(player, ShipyardWorkerDialogue(), NPC(NPCs.SHIPYARD_WORKER_675))
            return@on true
        }
        on(2451, IntType.SCENERY, "push"){ player, roots ->
            if (hasRequirement(player, "The Grand Tree")) {
                val outsideMine = player.location == Location.create(2467, 9903, 0) || player.location == Location.create(2468, 9903, 0)
                if(outsideMine) {
                    forceMove(player, player.location, player.location.transform(0, 2, 0), 25, 60, null, 819)
                } else {
                    forceMove(player, player.location, player.location.transform(0, -2, 0), 25, 60, null, 819)
                }
                animate(player, 2572, false)
                animateScenery(roots.asScenery(), 452)
                playAudio(player, Sounds.TANGLEVINE_APPEAR_2316)
            }
            return@on true
        }
    }

    private fun hazelmereScroll(player: Player, item: Item) {
        val id = item.id
        openInterface(player, 222).also {
            when (id) {
                hazelmerescroll -> setInterfaceText(player, hazelmerescrollText.joinToString("<br>"), 222, 6)
            }
        }
    }
}

