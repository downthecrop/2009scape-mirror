package content.minigame.greatorbproject

import content.minigame.greatorbproject.OrbProjAbandonTimer.Companion.GOP_ABANDON
import content.minigame.greatorbproject.OrbProjBot.Companion.newBot
import content.minigame.greatorbproject.OrbProjOverlay.GOP_OVERLAY
import core.api.*
import core.game.bots.AIPlayer
import core.game.component.Component
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.tools.ticksToSeconds
import org.rs09.consts.*

/**
 * Handles the lobby for Great Orb Project:
 * 1. Joining a team.
 * 2. Waiting for enough players (and spawning some bots).
 * 3. Starting an OrbProjSession once the lobby timer runs out.
 */

object OrbProjLobby {

    var greenLobby = mutableListOf<Player>()
    var yellowLobby = mutableListOf<Player>()

    // there is a 100 tick (60 second) lobby timer once there are enough players to start
    const val LOBBY_TIME = 100
    var lobbyTimer = LOBBY_TIME

    // join the lobby. player needs 2 free inv spaces, head and weapon slots free, and can't be part of a team already
    fun joinLobby(player: Player, team: Team): Boolean {

        // only one game can be active at a time. Source: RuneHQ
        if (OrbProjSession.active != null) {
            // this needs an authentic source
            val round = OrbProjSession.active!!.currentAltarIndex
            val maxRounds = OrbProjSession.active!!.altars.size - 1
            sendDialogue(player, "There is an active game running. Try joining again once that game is finished. Current round: $round of $maxRounds.")
            return false
        }

        // you get a penalty timer if you abandon a game
        if (hasTimerActive(player, GOP_ABANDON)) {
            sendMessage(player, "You recently abandoned a game and must wait before joining a team.") // text is made up

            // debug info
            val timer = getTimer(player, GOP_ABANDON)
            if (timer != null) {
                val next = timer.nextExecution.minus(getWorldTicks().coerceAtLeast(0))
                val seconds = ticksToSeconds(next)
                player.debug("DEBUG: Penalty time left: $seconds seconds.")
            }

            return false
        }

        // check team size limit (5v5)
        val currentTeamSize = if (team == Team.GREEN) greenLobby.size else yellowLobby.size
        if (currentTeamSize >= 5) {
            sendMessage(player, "That team is currently full.")
            return false
        }

        // check player is not currently on a team, and join team
        if (!greenLobby.contains(player) || !yellowLobby.contains(player)) {
            if (freeSlots(player) >= 2
                && getItemFromEquipment(player, EquipmentSlot.HEAD) == null
                && getItemFromEquipment(player, EquipmentSlot.WEAPON) == null) {
                // add team items and assign team
                if (team == Team.GREEN) {
                    greenLobby.add(player)
                    addItem(player, Items.GREEN_REPELLER_13646)
                    addItem(player, Items.GREEN_BARRIER_GENERATOR_13647)
                    replaceSlot(player, EquipmentSlot.HEAD.ordinal, Item(Items.RUNECRAFTER_HAT_13613), container = Container.EQUIPMENT)
                    replaceSlot(player, EquipmentSlot.WEAPON.ordinal, Item(Items.GREEN_ATTRACTOR_13645), container = Container.EQUIPMENT)
                    sendNPCDialogue(player, NPCs.WIZARD_ACANTHA_8031, "Welcome to the green team, the place for intelligent individuals.")
                } else {
                    yellowLobby.add(player)
                    addItem(player, Items.YELLOW_REPELLER_13644)
                    addItem(player, Items.YELLOW_BARRIER_GENERATOR_13648)
                    replaceSlot(player, EquipmentSlot.HEAD.ordinal, Item(Items.RUNECRAFTER_HAT_13612), container = Container.EQUIPMENT)
                    replaceSlot(player, EquipmentSlot.WEAPON.ordinal, Item(Items.YELLOW_ATTRACTOR_13643), container = Container.EQUIPMENT)
                    sendNPCDialogue(player, NPCs.WIZARD_VIEF_8030, "Excellent! You are now part of my team.")
                }

                // if this is the first player to join, spawn some bots
                if (greenLobby.size + yellowLobby.size < 2 && !player.isArtificial) {
                    repeat(2) {
                        newBot(Team.YELLOW)
                        newBot(Team.GREEN)

                        // reset the terminator. "I'll be back"
                        OrbProjBot.gameTerminated = false
                    }
                }

                // open overlay
                openInterface(player, GOP_OVERLAY)
                updateOverlay(player, team)

                return true
            } else {
                if (team == Team.GREEN) sendNPCDialogue(player, NPCs.WIZARD_ACANTHA_8031, "You need your head, right hand, and two free inventory slots, or I can't give you the necessary equipment.", FacialExpression.ANGRY)
                else sendNPCDialogue(player, NPCs.WIZARD_VIEF_8030, "All that junk you're carrying will interfere with the magic. I'll need to give you a hat, put something in your hand, and two things in your inventory.")

                return false
            }
        } else {
            sendMessage(player, "You are already assigned to a team.")
            return false
        }
    }

    // update the overlay when a player joins the lobby. There's a separate overlay updater in OrbProjSession
    fun updateOverlay(player: Player, team: Team) {
        val emptyWinners = Array(8) { Team.NONE }

        // update text. either the lobby needs more players, or the lobby has enough players and is counting down to start the round.
        val isCountingDown = greenLobby.size >= 2 && yellowLobby.size >= 2
        val stateText = if (isCountingDown) "Waiting for portal to open" else "Waiting for players to join"

        // update the actual interface
        OrbProjOverlay.updateInterface(
            player = player,
            team = team,
            portalOpen = false,
            greenScore = 0,
            yellowScore = 0,
            stateText = stateText,
            currentAltarIndex = -1,
            roundWinners = emptyWinners
        )

        // Show the lobby timer
        OrbProjOverlay.updateRoundTimer(player, team, lobbyTimer, LOBBY_TIME)
    }

    // process the lobby and start the game
    fun processLobby() {
        // check for players that have left the area
        checkLobbyOutOfBounds()

        // check lobby size
        val total = greenLobby.size + yellowLobby.size
        if (total == 0) return

        // check that there is at least one human
        val nonClankersGreen = greenLobby.any { !it.isArtificial }
        val nonClankersYellow = yellowLobby.any { !it.isArtificial }

        // if there are only bots left, clear them from the lobby
        if (!nonClankersGreen && !nonClankersYellow) {
            val participants = greenLobby + yellowLobby
            participants.forEach { player ->
                if (player is AIPlayer) {
                    AIPlayer.deregister(player.uid)
                }
            }
            participants.forEach { leaveLobby(it) }
            greenLobby.clear()
            yellowLobby.clear()

            // flag back to the bot script to stop
            OrbProjBot.gameTerminated = true

            // reset timer
            lobbyTimer = LOBBY_TIME

            return
        }

        // need at least 2 on each team to play
        if (greenLobby.size >= 2 && yellowLobby.size >= 2) {
            lobbyTimer--
            yellowLobby.forEach { player -> updateOverlay(player, Team.YELLOW) }
            greenLobby.forEach { player -> updateOverlay(player, Team.GREEN) }
            if (lobbyTimer <= 0 && OrbProjSession.active == null) {
                startGame()
                lobbyTimer = LOBBY_TIME
            }
        }
    }

    // checks if a player has left the lobby area
    private fun checkLobbyOutOfBounds() {
        val lobbyPlayers = greenLobby + yellowLobby
        lobbyPlayers.toList().forEach { player ->
            // if player isn't anywhere inside the GOP instances (guild or any altar), instantly desert them
            if (!OrbProjUtils.validRegions.contains(player.location.regionId)) {
                leaveLobby(player)
            }
        }
    }

    // does what it says
    private fun startGame() {
        // create the list of participants in the lobby
        val allParticipants = mutableListOf<Player>()

        // add green team players
        greenLobby.forEach { player ->
            setAttribute(player, OrbProjUtils.ATTR_GOP_TEAM, Team.GREEN)
            allParticipants.add(player)
        }

        // add yellow team players
        yellowLobby.forEach { player ->
            setAttribute(player, OrbProjUtils.ATTR_GOP_TEAM, Team.YELLOW)
            allParticipants.add(player)
        }

        if (allParticipants.isEmpty()) return

        // start a new session with the combined list
        val session = OrbProjSession(allParticipants)
        OrbProjSession.active = session

        // clear lobbies
        greenLobby.clear()
        yellowLobby.clear()

        // start
        session.start()
    }

    // handles leaving the lobby
    fun leaveLobby(player: Player) {
        if (greenLobby.remove(player) || yellowLobby.remove(player)) {
            // remove from team
            OrbProjUtils.removeFromTeamAndTeleportGuild(player)

            // I use this to close the overlay because contentAPI doesn't work, apparently this overlay needs a sledgehammer to coerce it.
            player.interfaceManager.close(Component(Components.RCGUILD_OVERLAY_781))
        }
    }
}
