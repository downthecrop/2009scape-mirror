import api.*
import api.events.EventHook
import api.events.SelfDeath
import core.game.component.Component
import core.game.content.dialogue.FacialExpression
import core.game.interaction.MovementPulse
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.Region
import core.game.world.map.RegionManager
import core.game.world.map.build.DynamicRegion
import org.rs09.consts.Components
import rs09.ServerConstants
import rs09.game.Event
import rs09.game.camerautils.PlayerCamera
import rs09.game.system.SystemLogger
import rs09.game.world.GameWorld

/**
 * A utility class for making cutscenes.
 * @author Ceikry
 */
abstract class Cutscene(val player: Player) {
    lateinit var region: Region
    lateinit var base: Location
    var exitLocation: Location = player.location.transform(0,0,0)
    var ended = false

    val camera = PlayerCamera(player)
    private val addedNPCs = HashMap<Int, ArrayList<NPC>>()

    abstract fun setup()
    abstract fun runStage(stage: Int)

    /**
     * Creates a new dynamic copy of the region identified by regionId, sets this cutscene's region as this new copy, and clears any cutscene-spawned NPCs from the previous region.
     * @param regionId the region ID to duplicate.
     */
    fun loadRegion(regionId: Int)
    {
        clearNPCs()
        logCutscene("Creating new instance of region $regionId for ${player.username}.")
        region = DynamicRegion.create(regionId)
        logCutscene("Dynamic region instantiated for ${player.username}. Global coordinates: ${region.baseLocation}.")
        base = region.baseLocation
    }

    /**
     * Fade the player's view to black. This process can be safely assumed to take about 8 ticks to complete.
     */
    fun fadeToBlack()
    {
        logCutscene("Fading ${player.username}'s screen to black.")
        player.interfaceManager.closeOverlay()
        player.interfaceManager.openOverlay(Component(Components.FADE_TO_BLACK_120))
    }

    /**
     * Fade the player's view from black back to normal. This process can be safely assumed to take about 6 ticks to complete.
     */
    fun fadeFromBlack()
    {
        logCutscene("Fading ${player.username}'s screen from black to normal.")
        player.interfaceManager.closeOverlay()
        player.interfaceManager.openOverlay(Component(Components.FADE_FROM_BLACK_170))
    }

    /**
     * Teleports an entity to a pair of coordinates within the currently active cutscene region
     * @param entity the entity to teleport
     * @param regionX the region X coordinate to teleport the entity to (0-63)
     * @param regionY the region Y coordinate to teleport the entity to (0-63)
     * @param plane (optional) the plane to teleport to (0-3)
     */
    fun teleport(entity: Entity, regionX: Int, regionY: Int, plane: Int = 0)
    {
        val newLoc = base.transform(regionX, regionY, plane)
        logCutscene("Teleporting ${entity.username} to coordinates: LOCAL[$regionX,$regionY,$plane] GLOBAL[${newLoc.x},${newLoc.y},$plane].")
        entity.properties.teleportLocation = newLoc
    }

    /**
     * Moves an entity to the given coordinates within the currently active cutscene region
     * @param entity the entity to move
     * @param regionX the region X coordinate to move the entity to (0-63)
     * @param regionY the region Y coordinate to move the entity to (0-63)
     */
    fun move(entity: Entity, regionX: Int, regionY: Int)
    {
        logCutscene("Moving ${entity.username} to LOCAL[$regionX,$regionY].")
        entity.pulseManager.run(object : MovementPulse(entity, base.transform(regionX,regionY,0)) {
            override fun pulse(): Boolean {
                return true
            }
        })
    }

    /**
     * Sends a dialogue to the player using the given NPC ID, which updates the cutscene stage by default when continued.
     * @param npcId the ID of the NPC to send a dialogue for
     * @param expression the FacialExpression the NPC should use
     * @param message the message to send
     * @param onContinue (optional) a method that runs when the dialogue is "continued." Increments the cutscene stage by default.
     */
    fun dialogueUpdate(npcId: Int, expression: FacialExpression, message: String, onContinue: () -> Unit = {incrementStage()})
    {
        logCutscene("Sending NPC dialogue update.")
        sendNPCDialogue(player, npcId, message, expression)
        player.dialogueInterpreter.addAction { _,_ -> onContinue.invoke() }
    }

    /**
     * Sends a non-NPC dialogue to the player, which updates the cutscene stage by default when continued
     * @param message the message to send
     * @param onContinue (optional) a method that runs when the dialogue is "continued." Increments the cutscene stage by default.
     */
    fun dialogueUpdate(message: String, onContinue: () -> Unit = {incrementStage()})
    {
        logCutscene("Sending standard dialogue update.")
        sendDialogue(player, message)
        player.dialogueInterpreter.addAction {_,_ -> onContinue.invoke()}
    }

    /**
     * Sends a player dialogue, which updates the cutscene stage by default when continued
     * @param expression the FacialExpression to use
     * @param message the message to send
     * @param onContinue (optional) a method that runs when the dialogue is "continued." Increments the cutscene stage by default.
     */
    fun playerDialogueUpdate(expression: FacialExpression, message: String, onContinue: () -> Unit = {incrementStage()})
    {
        logCutscene("Sending player dialogue update")
        sendPlayerDialogue(player, message, expression)
        player.dialogueInterpreter.addAction{_,_ -> onContinue.invoke()}
    }

    /**
     * Updates the stage after a set number of ticks.
     * @param ticks the number of ticks to wait
     * @param newStage (optional) the new stage to update to. If not passed, stage is incremented instead.
     */
    fun timedUpdate(ticks: Int, newStage: Int = -1)
    {
        logCutscene("Executing timed updated for $ticks ticks.")
        GameWorld.Pulser.submit(object : Pulse(ticks)
        {
            override fun pulse(): Boolean {
                if(newStage == -1)
                    incrementStage()
                else
                    updateStage(newStage)
                return true
            }
        })
    }

    /**
     * Retrieves the first NPC from the added-to-cutscene NPCs with a matching ID.
     * @param id the ID to grab for.
     * @return the first NPC, or null if there are no matching NPCs.
     */
    fun getNPC(id: Int): NPC?
    {
        return addedNPCs[id]?.firstOrNull()
    }

    /**
     * Retrieves all NPCs from the added-to-cutscene NPCs with a matching ID.
     * @param id the ID to grab for.
     * @return an ArrayList of the matching NPCs, which will be empty if there were no matches.
     */
    fun getNPCs(id: Int): ArrayList<NPC>
    {
        return addedNPCs[id] ?: ArrayList()
    }

    /**
     * Retrieves the object in the region at the given regionX and regionY
     * @param regionX the region-local X coordinate to grab the object from (0-63)
     * @param regionY the region-local Y coordinate to grab the object from (0-63)
     * @param plane the plane to grab the object from (0-3)
     */
    fun getObject(regionX: Int, regionY: Int, plane: Int = 0): Scenery?
    {
        val obj = RegionManager.getObject(base.transform(regionX,regionY,plane))
        logCutscene("Retrieving object at LOCAL[$regionX,$regionY], GOT: ${obj?.definition?.name ?: "null"}.")
        return obj
    }

    /**
     * Adds an NPC to the cutscene with the given ID, at the region-local X and Y coordinates.
     * @param id the ID of the NPC to add.
     * @param regionX the region-local X coordinate to place the NPC at
     * @param regionY the region-local Y coordinate to place the NPC at
     * @param direction the direction the NPC faces when it is initially placed.
     * @param plane the plane to place te NPC on. Default is 0.
     */
    fun addNPC(id: Int, regionX: Int, regionY: Int, direction: Direction, plane: Int = 0)
    {
        val npc = NPC(id)
        npc.isRespawn = false
        npc.isAggressive = false
        npc.isWalks = false
        npc.location = base.transform(regionX, regionY, plane)
        npc.init()
        npc.faceLocation(npc.location.transform(direction))

        val npcs = addedNPCs[id] ?: ArrayList()
        npcs.add(npc)
        addedNPCs[id] = npcs
        logCutscene("Added NPC $id at location LOCAL[$regionX,$regionY,$plane] GLOBAL[${npc.location.x},${npc.location.y},$plane]")
    }

    fun start()
    {
        logCutscene("Starting cutscene for ${player.username}.")
        region = RegionManager.forId(player.location.regionId)
        base = RegionManager.forId(player.location.regionId).baseLocation
        setup()
        runStage(player.getCutsceneStage())
        setAttribute(player, ATTRIBUTE_CUTSCENE, this)
        setAttribute(player, ATTRIBUTE_CUTSCENE_STAGE, 0)
        player.interfaceManager.removeTabs(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14)
        player.properties.isSafeZone = true
        player.properties.safeRespawn = player.location
        player.lock()
        player.hook(Event.SelfDeath, CUTSCENE_DEATH_HOOK)
        player.logoutListeners["cutscene"] = {player -> player.location = exitLocation; player.getCutscene()?.end() }
        player.antiMacroHandler.enabled = false
    }

    /**
     * Ends this cutscene, fading the screen to black, teleporting the player to the exit location, and then fading it back in and executing the endActions passed to this method.
     * @param endActions (optional) a method that executes when the cutscene fully completes
     */
    fun end(endActions: (() -> Unit)? = null)
    {
        ended = true
        fadeToBlack()
        GameWorld.Pulser.submit(object : Pulse(){
            var tick: Int = 0
            override fun pulse(): Boolean {
                when(tick++)
                {
                    8 -> player.properties.teleportLocation = exitLocation
                    9 -> fadeFromBlack()
                    16 -> {
                        endActions?.invoke()
                        player.removeAttribute(ATTRIBUTE_CUTSCENE)
                        player.removeAttribute(ATTRIBUTE_CUTSCENE_STAGE)
                        player.properties.isSafeZone = false
                        player.properties.safeRespawn = ServerConstants.HOME_LOCATION
                        player.interfaceManager.restoreTabs()
                        player.unlock()
                        clearNPCs()
                        player.unhook(CUTSCENE_DEATH_HOOK)
                        player.logoutListeners.remove("cutscene")
                        player.antiMacroHandler.enabled = true
                        return true
                    }
                }
                return false
            }
        })
    }

    /**
     * Moves the camera to the specified regionX and regionY
     * @param regionX the region-local X coordinate to move the camera to (0-63)
     * @param regionY the region-local Y coordinate to move the camera to (0-63)
     * @param height (optional) the height of the camera, defaults to 300.
     * @param speed (optional) the speed of the camera transition, defaults to 100.
     */
    fun moveCamera(regionX: Int, regionY: Int, height: Int = 300, speed: Int = 100)
    {
        val globalLoc = base.transform(regionX, regionY, 0)
        camera.panTo(globalLoc.x, globalLoc.y, height, speed)
    }

    /**
     * Rotates the camera to face the given region-local X and Y coordinates
     * @param regionX the region-local X coordinate to rotate the camera to (0-63)
     * @param regionY the region-local Y coordinate to rotate the camera to (0-63)
     * @param height (optional) the height of the camera, defaults to 300.
     * @param speed (optional) the speed of the camera transition, defaults to 100.
     */
    fun rotateCamera(regionX: Int, regionY: Int, height: Int = 300, speed: Int = 100)
    {
        val globalLoc = base.transform(regionX, regionY, 0)
        camera.rotateTo(globalLoc.x, globalLoc.y, height, speed)
    }

    /**
     * Sets the location the player is placed at when the cutscene ends.
     */
    fun setExit(location: Location)
    {
        exitLocation = location
    }

    private fun loadCurrentStage()
    {
        if(ended) return
        runStage(player.getCutsceneStage())
    }

    /**
     * Increments the player's stage attribute by 1
     */
    fun incrementStage()
    {
        setAttribute(player, ATTRIBUTE_CUTSCENE_STAGE, player.getCutsceneStage() + 1)
        loadCurrentStage()
    }

    /**
     * Sets the player's stage attribute to a new stage
     */
    fun updateStage(newStage: Int)
    {
        setAttribute(player, ATTRIBUTE_CUTSCENE_STAGE, newStage)
        loadCurrentStage()
    }

    fun logCutscene(message: String)
    {
        if(ServerConstants.LOG_CUTSCENE)
            SystemLogger.logInfo("[CUTSCENE: ${this.javaClass.simpleName}] $message")
    }

    fun clearNPCs() {
        for(entry in addedNPCs.entries)
        {
            logCutscene("Clearing ${entry.value.size} NPCs with ID ${entry.key} for ${player.username}.")
            for(npc in entry.value) npc.clear()
        }
        addedNPCs.clear()
    }

    companion object {
        const val ATTRIBUTE_CUTSCENE = "cutscene"
        const val ATTRIBUTE_CUTSCENE_STAGE = "cutscene:stage"
        object CUTSCENE_DEATH_HOOK : EventHook<SelfDeath>
        {
            override fun process(entity: Entity, event: SelfDeath) {
                if(entity !is Player) return
                entity.getCutscene()?.end() ?: entity.unhook(this)
            }
        }
    }
}