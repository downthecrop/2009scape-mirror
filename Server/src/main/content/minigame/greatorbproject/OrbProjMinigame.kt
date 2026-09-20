package content.minigame.greatorbproject

import content.global.skill.construction.decoration.pohstorage.StorableFamily
import content.global.skill.construction.decoration.pohstorage.StorageInterface
import content.minigame.greatorbproject.OrbProjUtils.ATTR_ACTIVE_BARRIER
import content.minigame.greatorbproject.OrbProjUtils.BARRIER_DOWN
import content.minigame.greatorbproject.OrbProjUtils.BARRIER_UP
import content.minigame.greatorbproject.OrbProjUtils.guildStartLoc
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.RegionManager
import core.game.world.map.zone.ZoneBorders
import core.tools.RandomFunction
import org.rs09.consts.*

/**
 * The Great Orb Project - A Runecrafter's Favorite!
 */

class OrbProjMinigame : InteractionListener, TickListener, LogoutListener, MapArea {

    // TODO: there are currently no bonuses implemented for wearing the runecrafter robes during a game.
    //  I need to find better info on what the bonuses are.
    //  Wiki says orb speed is boosted and wand reach is extended.

    // TODO: Find a source and see if you are allowed to teleport or interact with the exit portals during a round.
    //  This would solve a lot of weird round-abandoning edge cases.

    /*
     * Sources:
     * Wiki: https://runescape.wiki/w/The_Great_Orb_Project?oldid=899925, https://runescape.wiki/w/The_Great_Orb_Project
     * General: https://www.youtube.com/watch?v=-0JCJeHGF1Y
     * Two sources that show that the game is non-instanced (there are outside players): https://www.youtube.com/watch?v=qgK8Ek_zh38, https://www.youtube.com/watch?v=eNoLy1iBUZ0
     * Source of barrier anims and some acantha text at the end: https://www.youtube.com/watch?v=lReO-E1XuOs, https://www.youtube.com/watch?v=4eCh2QsZlcY&t=47s
     */

    companion object {
        private val joiningWizards = intArrayOf(
            NPCs.WIZARD_VIEF_8030, NPCs.WIZARD_ACANTHA_8031,
            NPCs.WIZARD_8033, NPCs.WIZARD_8034, NPCs.WIZARD_8035, NPCs.WIZARD_8036,
            NPCs.WIZARD_8037, NPCs.WIZARD_8038, NPCs.WIZARD_8039, NPCs.WIZARD_8040
        )

        private val orbs = intArrayOf(
            NPCs.GREEN_ORB_8027, NPCs.GREEN_ORB_8028,
            NPCs.YELLOW_ORB_8023, NPCs.YELLOW_ORB_8024
        )
    }

    // register regions as special map area so death can be overridden (below)
    override fun defineAreaBorders(): Array<ZoneBorders> {
        return OrbProjUtils.validRegions.map { regionId ->
            ZoneBorders.forRegion(regionId)
        }.toTypedArray()
    }

    // do not allow a player to die. minigame is safe.
    override fun canStartDeath(entity: Entity, killer: Entity): Boolean? {
        val session = OrbProjSession.active
        if (entity.isPlayer && (OrbProjLobby.greenLobby.contains(entity) || OrbProjLobby.yellowLobby.contains(entity) || (session != null && session.participants.contains(entity)))) {
            // heal the player back to full health
            entity.fullRestore()
            if (session != null ) {
                // remove from active game
                OrbProjSession.active?.removePlayer(entity.asPlayer())
            } else {
                // remove from lobby
                OrbProjLobby.leaveLobby(entity.asPlayer())
            }
            return false
        }
        return super.canStartDeath(entity, killer)
    }

    // the overall tick for the game
    override fun tick() {

        // lobby needs at least 2 players on each team (authentically), then counts down 60s (100 ticks) to start a game
        OrbProjLobby.processLobby()

        // if a game is active, process the game logic
        OrbProjSession.active?.tick()

    }

    // handles logouts if the player is in a game or lobby
    override fun logout(player: Player) {
        val inLobby = OrbProjLobby.yellowLobby.contains(player) || OrbProjLobby.greenLobby.contains(player)
        val inGame = OrbProjSession.active?.participants?.contains(player) == true

        // remove items and kick player from session
        if (inLobby || inGame) {
            // start a penalty timer for abandoning an active game
            if (!player.isArtificial && inGame) {
                val penalty = if (player.isAdmin) 100 else 1000
                registerTimer(player, OrbProjAbandonTimer(penalty))
            }

            OrbProjUtils.removeFromTeamAndTeleportGuild(player)
            OrbProjLobby.leaveLobby(player)
            OrbProjSession.active?.removePlayer(player)
            OrbProjUtils.removeBarriers(player)
            player.location = guildStartLoc
        }
    }

    // interaction listeners used in the game. joining teams, interacting with orbs, portals, barriers, etc.
    override fun defineListeners() {

        // token exchange
        on(intArrayOf(NPCs.WIZARD_ELRISS_8032), IntType.NPC, "exchange") { player, _ ->
            openInterface(player, Components.RCGUILD_REWARDS_779)
            return@on true
        }

        // join team. max team size of 5 on ea
        on(joiningWizards, IntType.NPC, "join", "join-game") { player, npc ->

            sendDialogueOptions(player, "Are you sure you want to join?", "Yes.", "No.")
            addDialogueAction(player) { player, button ->
                when (button) {
                    // yes
                    2 -> {
                        when (npc.id) {
                            NPCs.WIZARD_VIEF_8030 -> OrbProjLobby.joinLobby(player, Team.YELLOW)
                            NPCs.WIZARD_ACANTHA_8031 -> OrbProjLobby.joinLobby(player, Team.GREEN)
                            else -> {
                                // join the team with the fewer players, or else random
                                if (OrbProjLobby.greenLobby.size < OrbProjLobby.yellowLobby.size) {
                                    OrbProjLobby.joinLobby(player, Team.GREEN)
                                } else {
                                    // join random team
                                    if (RandomFunction.nextBool()) {
                                        OrbProjLobby.joinLobby(player, Team.YELLOW)
                                    } else {
                                        OrbProjLobby.joinLobby(player, Team.GREEN)
                                    }
                                }
                            }
                        }
                    }

                    // no
                    3 -> return@addDialogueAction
                }
                return@addDialogueAction
            }
            return@on true
        }

        // portal to next altar
        on(NPCs.PORTAL_8019, IntType.NPC, "enter") { player, _ ->
            OrbProjSession.active?.handlePortal(player)
            return@on true
        }

        // barrier generators. Each player only gets one barrier
        on(intArrayOf(Items.GREEN_BARRIER_GENERATOR_13647, Items.YELLOW_BARRIER_GENERATOR_13648), IntType.ITEM, "make-barrier") { player, node ->

            // can't make a barrier if the game isn't running
            if (OrbProjSession.active == null) {
                return@on false
            }

            // check if the player already has a barrier and remove it
            OrbProjUtils.removeBarriers(player)

            // get and spawn barrier
            val barrierId = if (node.id == Items.GREEN_BARRIER_GENERATOR_13647) {
                Scenery.BARRIER_38377
            } else {
                Scenery.BARRIER_38378
            }

            val newBarrier = addScenery(barrierId, player.location, 0, 10)

            animate(player, BARRIER_UP)
            setAttribute(player, ATTR_ACTIVE_BARRIER, newBarrier)

            return@on true
        }

        // destroy barriers. Immediately succeed if you are the team player, otherwise it's a % chance.
        on(intArrayOf(Scenery.BARRIER_38377, Scenery.BARRIER_38378), IntType.SCENERY, "destroy") { player, node ->
            val scenery = node.asScenery()
            val team = getAttribute(player, OrbProjUtils.ATTR_GOP_TEAM, Team.NONE)
            val isGreenBarrier = scenery.id == Scenery.BARRIER_38377
            val barrierTeam = if (isGreenBarrier) Team.GREEN else Team.YELLOW

            if (team == barrierTeam) {
                animate(player, BARRIER_DOWN)
                removeScenery(scenery)
                sendMessage(player, "You break the barrier.") // all the message text is a guess
            } else {
                // chance to destroy is a guess. I use 50%
                if (kotlin.random.Random.nextBoolean()) {
                    animate(player, BARRIER_DOWN)
                    removeScenery(scenery)
                    sendMessage(player, "You break the barrier.")
                } else {
                    sendMessage(player, "You fail to break the barrier.")
                }
            }
            return@on true
        }

        // pass through barriers
        on(intArrayOf(Scenery.BARRIER_38377, Scenery.BARRIER_38378), IntType.SCENERY, "pass-through") { player, node ->
            // face barrier, then try to walk through
            queueScript(player, 1) { stage ->
                when (stage) {
                    0 -> {
                        face(player, node)
                        return@queueScript keepRunning(player)
                    }
                    1 -> {
                        // calc new loc
                        val dx = Integer.signum(node.location.x - player.location.x)
                        val dy = Integer.signum(node.location.y - player.location.y)
                        val destination = node.location.transform(dx, dy, 0)

                        // check if walkable
                        if (RegionManager.isTeleportPermitted(destination)) {
                            forceMove(player, player.location, destination, 0, 1)
                        } else {
                            sendMessage(player, "Something is blocking the other side.")
                        }
                        return@queueScript stopExecuting(player)
                    }
                }
                return@queueScript stopExecuting(player)
            }
            return@on stopExecuting(player)
        }

        // keep your team hat on. message text is a guess, but one of the hats is not equippable so you'd better keep that shit on.
        onUnequip(intArrayOf(Items.RUNECRAFTER_HAT_13613, Items.RUNECRAFTER_HAT_13612)) { player, _ ->
            sendMessage(player, "You can't remove that hat during the game.")
            return@onUnequip false
        }

        // destroying the team items will kick you
        on(intArrayOf(
            Items.GREEN_ATTRACTOR_13645, Items.GREEN_REPELLER_13646, Items.GREEN_BARRIER_GENERATOR_13647,
            Items.YELLOW_ATTRACTOR_13643, Items.YELLOW_REPELLER_13644, Items.YELLOW_BARRIER_GENERATOR_13648
        ), IntType.ITEM, "destroy") { player, item ->
            // destroy items dialog taken from dropListener
            player.dialogueInterpreter.sendDestroyItem(item.id, item.name)
            addDialogueAction(player) { player, button ->
                // "yes"
                if (button == 3) {
                    playAudio(player, Sounds.DESTROY_OBJECT_2381)
                    OrbProjLobby.leaveLobby(player)
                    OrbProjSession.active?.removePlayer(player)
                    OrbProjUtils.removeFromTeamAndTeleportGuild(player)
                }
            }
            return@on true
        }

        // orb attract interact
        on(intArrayOf(NPCs.GREEN_ORB_8027, NPCs.YELLOW_ORB_8023), IntType.NPC, "attract") { player, npc ->
            // check if player is in the session
            val session = OrbProjSession.active
            if (session != null && session.participants.contains(player)) {
                val wand = getItemFromEquipment(player, EquipmentSlot.WEAPON)
                when (wand?.id) {
                    // needs attractor wand (green team)
                    Items.GREEN_ATTRACTOR_13645 -> {
                        if (session.ticksRemaining < 50 && npc.id == NPCs.YELLOW_ORB_8023) {
                            // in the last 30 seconds, you can only interact with team orbs.
                            sendMessage(player, "You can only interact with the green orbs in the last 30 seconds of the round.")
                        } else {
                            session.currentRound?.orbTargetLock(npc.asNpc(), player, attract = true, Team.GREEN)
                        }
                    }

                    // needs attractor wand (yellow team)
                    Items.YELLOW_ATTRACTOR_13643 -> {
                        if (session.ticksRemaining < 50 && npc.id == NPCs.GREEN_ORB_8027) {
                            // in the last 30 seconds, you can only interact with team orbs
                            sendMessage(player, "You can only interact with the yellow orbs in the last 30 seconds of the round.")
                        } else {
                            session.currentRound?.orbTargetLock(npc.asNpc(), player, attract = true, Team.YELLOW)
                        }
                    }

                    else -> sendMessage(player, "You need to wield your attractor wand for this.")
                }
            }
            return@on true
        }

        // orb repel interact
        on(intArrayOf(NPCs.GREEN_ORB_8028, NPCs.YELLOW_ORB_8024), IntType.NPC, "repel") { player, npc ->
            // check if player is in the session
            val session = OrbProjSession.active
            if (session != null && session.participants.contains(player)) {
                val wand = getItemFromEquipment(player, EquipmentSlot.WEAPON)
                when (wand?.id) {
                    // needs repeller wand (green team)
                    Items.GREEN_REPELLER_13646 -> {
                        if (session.ticksRemaining < 50 && npc.id == NPCs.YELLOW_ORB_8024) {
                            // in the last 30 seconds, you can only interact with team orbs.
                            sendMessage(player, "You can only interact with the green orbs in the last 30 seconds of the round.")
                        } else {
                            session.currentRound?.orbTargetLock(npc.asNpc(), player, attract = false, Team.GREEN)
                        }
                    }

                    // needs repeller wand (yellow team)
                    Items.YELLOW_REPELLER_13644 -> {
                        if (session.ticksRemaining < 50 && npc.id == NPCs.GREEN_ORB_8028) {
                            // in the last 30 seconds, you can only interact with team orbs.
                            sendMessage(player, "You can only interact with the yellow orbs in the last 30 seconds of the round.")
                        } else {
                            session.currentRound?.orbTargetLock(npc.asNpc(), player, attract = false, Team.YELLOW)
                        }
                    }

                    else -> sendMessage(player, "You need to wield your repeller wand for this.")
                }
            }
            return@on true
        }

        // replace the wand you're wielding with the one from inventory
        fun replaceWand(player: Player, current: Item, new: Int) {
            // remove the new wand from player's inv
            if (removeItem(player, new)) {
                // equips the new wand in the slot. returns the value of the previously-equipped wand
                val prevWand = replaceSlot(player, current.slot, Item(new), current, container = Container.EQUIPMENT)

                // it shouldn't ever be null because I checked for that below already
                if (prevWand != null) {
                    // adds the previously-equipped wand back to inventory
                    addItem(player, prevWand.id)
                }
            }
        }

        // change orb from attract to repel, and change wand
        on(orbs, IntType.NPC, "change-wand") { player, npc ->
            // change orb
            OrbProjSession.active?.currentRound?.transformOrb(player, npc.asNpc())

            // change wand
            val currentWand = getItemFromEquipment(player, EquipmentSlot.WEAPON) ?: return@on true
            when (currentWand.id) {
                Items.GREEN_ATTRACTOR_13645 -> replaceWand(player, currentWand, Items.GREEN_REPELLER_13646)
                Items.YELLOW_ATTRACTOR_13643 -> replaceWand(player, currentWand, Items.YELLOW_REPELLER_13644)
                Items.GREEN_REPELLER_13646 -> replaceWand(player, currentWand, Items.GREEN_ATTRACTOR_13645)
                Items.YELLOW_REPELLER_13644 -> replaceWand(player, currentWand, Items.YELLOW_ATTRACTOR_13643)
            }
            return@on true
        }

        // override destination for orbs (you can interact up to 10 tiles away just like it is ranged or magic combat)
        setDest(IntType.NPC, orbs, "attract", "repel", "change-wand") { entity, node ->
            val dist = entity.location.getDistance(node.location)
            val hasLos = core.game.node.entity.combat.CombatSwingHandler.isProjectileClipped(entity, node.asNpc(), false)

            // check that player is within 10 tiles and has line of sight
            if (dist <= 10 && hasLos) {
                return@setDest entity.location
            }

            // otherwise continue
            return@setDest node.location
        }
    }
}