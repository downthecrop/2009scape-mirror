package rs09.game.content.quest.members.tree

import api.*
import core.game.content.global.action.DoorActionHandler
import core.game.node.Node
import core.game.node.entity.impl.ForceMovement
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.agility.AgilityHandler
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.system.task.Pulse
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import rs09.game.content.dialogue.DialogueFile
import rs09.game.content.quest.members.tree.TreeGnomeVillage.Companion.mazeEntrance
import rs09.game.content.quest.members.tree.TreeGnomeVillage.Companion.mazeVillage
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.IntType
import rs09.game.world.GameWorld

class TreeGnomeVillageListeners : InteractionListener {
    private val ballista = 2181
    private val crumbledWall = 12762
    private val closedChest = 2183
    private val looseRailing = 2186
    private val openedChest = 2182
    private val strongholdDoor = 2184

    override fun defineDestinationOverrides() {
        setDest(IntType.NPC, intArrayOf(NPCs.TRACKER_GNOME_2_482),"talk-to"){ _, _ ->
            return@setDest Location.create(2524, 3256, 0)
        }
    }

    override fun defineListeners() {
        on(NPCs.KING_BOLREN_469, IntType.NPC, "talk-to"){ player, npc ->
            openDialogue(player, KingBolrenDialogue(), npc)
            return@on true
        }
        on(NPCs.ELKOY_5179, IntType.NPC, "talk-to"){ player, npc ->
            openDialogue(player, ElkoyDialogue(), npc)
            return@on true
        }
        on(NPCs.ELKOY_5182, IntType.NPC, "talk-to"){ player, npc ->
            openDialogue(player, ElkoyDialogue(), npc)
            return@on true
        }
        on(NPCs.ELKOY_5182, IntType.NPC, "follow"){ player, _ ->
            ElkoyDialogue().travelCutscene(player, if (player.location.y > 3161) mazeVillage else mazeEntrance)
            return@on true
        }
        on(NPCs.COMMANDER_MONTAI_470, IntType.NPC, "talk-to"){ player, npc ->
            openDialogue(player, CommanderMontaiDialogue(), npc)
            return@on true
        }
        on(NPCs.TRACKER_GNOME_1_481, IntType.NPC, "talk-to"){ player, npc ->
            openDialogue(player, TrackerGnomeOneDialogue(), npc)
            return@on true
        }
        on(NPCs.TRACKER_GNOME_2_482, IntType.NPC, "talk-to"){ player, npc ->
            openDialogue(player, TrackerGnomeTwoDialogue(), npc)
            return@on true
        }
        on(NPCs.TRACKER_GNOME_3_483, IntType.NPC, "talk-to"){ player, npc ->
            openDialogue(player, TrackerGnomeThreeDialogue(), npc)
            return@on true
        }
        on(NPCs.KHAZARD_WARLORD_477, IntType.NPC, "talk-to"){ player, npc ->
            openDialogue(player, KhazardWarlordDialogue(), npc)
            return@on true
        }
        on(NPCs.REMSAI_472, IntType.NPC, "talk-to"){ player, npc ->
            openDialogue(player, RemsaiDialogue(), npc)
            return@on true
        }
        on(NPCs.LOCAL_GNOME_484, IntType.NPC, "talk-to"){ player, npc ->
            openDialogue(player, LocalGnomeDialogue(), npc)
            return@on true
        }
        on(ballista, IntType.SCENERY, "fire"){ player, _ ->
            openDialogue(player, BallistaDialogue())
            return@on true
        }
        on(looseRailing, IntType.SCENERY, "squeeze-through"){ player, _ ->
            if(player.location != location(2516,3161,0)) {
                squeezeThrough(player)
            } else {
                player.pulseManager.run(object : Pulse(0) {
                    var count = 0
                    override fun pulse(): Boolean {
                        when (count) {
                            0 -> ForceMovement.run(player,player.location,player.location.transform(Direction.WEST,1))
                            2 -> squeezeThrough(player)
                            3 -> return true
                        }
                        count++
                        return false
                    }
                })
            }
            return@on true
        }
        on(crumbledWall, IntType.SCENERY, "climb-over"){ player, _ ->
            openDialogue(player, ClimbWall())
            return@on true
        }
        on(closedChest, IntType.SCENERY, "open"){ player, node ->
            SceneryBuilder.replace(node.asScenery(), Scenery(openedChest, node.location, node.asScenery().rotation),10)
            val upperGuard: NPC = RegionManager.getNpc(player.location, NPCs.KHAZARD_COMMANDER_478, 6) ?: return@on true
            upperGuard.sendChat("Oi. You! Get out of there.")
            upperGuard.attack(player)
            return@on true
        }
        on(openedChest, IntType.SCENERY, "search"){ player, _ ->
            if(!inInventory(player,Items.ORB_OF_PROTECTION_587)){
                sendDialogue(player,"You search the chest. Inside you find the gnomes' stolen orb of protection.")
                addItemOrDrop(player,Items.ORB_OF_PROTECTION_587)
            }
            return@on true
        }
        on(strongholdDoor, IntType.SCENERY, "open"){ player, node ->
            if(player.location.y >= 3251){
                DoorActionHandler.handleAutowalkDoor(player, node as Scenery)
            }
            return@on true
        }
        on(NPCs.LIEUTENANT_SCHEPBUR_3817, IntType.NPC, "talk-to"){player, node ->
            openDialogue(player, LieutenantSchepburDialogue(),node)
            return@on true
        }
    }
}

fun squeezeThrough(player: Player){
    val squeezeAnim = Animation.create(3844)
    if(player.location.y >= 3161) {
        AgilityHandler.forceWalk(
            player,
            -1,
            player.location,
            player.location.transform(Direction.SOUTH, 1),
            squeezeAnim,
            5,
            0.0,
            null
        )
    } else {
        AgilityHandler.forceWalk(
            player,
            -1,
            player.location,
            player.location.transform(Direction.NORTH, 1),
            squeezeAnim,
            5,
            0.0,
            null
        )
    }
}

private class ClimbWall : DialogueFile() {
    val climbAnimation = Animation(839)
    val wallLoc = Location(2509,3253,0)
    override fun handle(componentID: Int, buttonID: Int) {
        if(questStage(player!!, TreeGnomeVillage.questName) > 30){
            val northSouth = if (player!!.location.y <= wallLoc.y) Direction.NORTH else Direction.SOUTH
            when(stage){
                0 -> sendDialogue(player!!,"The wall has been reduced to rubble. It should be possible to climb over the remains").also{ stage++ }
                1 -> AgilityHandler.forceWalk(player!!, -1, player!!.location, player!!.location.transform(northSouth, 2), climbAnimation, 20, 0.0, null).also {
                    end()
                    if(northSouth == Direction.SOUTH) return
                    val lowerGuard: NPC = RegionManager.getNpc(player!!.location, NPCs.KHAZARD_COMMANDER_478, 6) ?: return
                    GameWorld.Pulser.submit(object : Pulse(0) {
                        var count = 0
                        override fun pulse(): Boolean {
                            when (count) {
                                0 -> {
                                    player!!.lock(4)
                                    lowerGuard.sendChat("What? How did you manage to get in here.")
                                }
                                2 -> {
                                    player!!.sendChat("I've come for the orb.")
                                }
                                3 -> {
                                    lowerGuard.sendChat("I'll never let you take it.")
                                    lowerGuard.attack(player)
                                    player!!.unlock()
                                    return true
                                }
                            }
                            count++
                            return false
                        }
                    })
                }
            }
        }
    }
}