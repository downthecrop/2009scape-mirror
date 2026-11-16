package core.game.interaction

import core.api.*
import core.game.bots.AIPlayer
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.GroundItem
import core.game.node.scenery.Scenery
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.map.path.Pathfinder
import core.tools.Log
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.Integer.max

open class ScriptProcessor {
    var currentScript: Script<*>? = null
    private val queue = ArrayList<Script<*>>()

    var delay = 0
    var persistent = false

    fun processQueue() : Boolean {
        var anyExecuted = false
        val strongInQueue = hasTypeInQueue(QueueStrength.STRONG)

        if (strongInQueue) {
            if (this is EntityScriptProcessor && entity is Player) {
                closeAllInterfaces(entity)
            }
            removeWeakScripts()
        }

        val toRemove = ArrayList<Script<*>>()

        for (i in 0 until queue.size) {
            when (val script = queue[i]) {
                is QueuedScript -> {
                    if (this is EntityScriptProcessor && entity.delayed() && script.strength != QueueStrength.SOFT)
                        continue
                    if (script.nextExecution > GameWorld.ticks)
                        continue
                    if (this is EntityScriptProcessor && entity is Player && script.strength == QueueStrength.STRONG) {
                        closeAllInterfaces(entity)
                    }
                    script.nextExecution = GameWorld.ticks + 1
                    val finished = executeScript(script)
                    script.state++
                    if (finished && !script.persist)
                        toRemove.add(script)
                    else if (finished)
                        script.state = 0
                    anyExecuted = true
                }
                is QueuedUseWith -> {
                    if (this !is EntityScriptProcessor) {
                        toRemove.add(script)
                        log(this::class.java, Log.ERR, "Tried to queue an item UseWith interaction for the whole world!")
                        continue
                    }
                    if (entity.delayed() && script.strength != QueueStrength.SOFT)
                        continue
                    if (entity !is Player) {
                        toRemove.add(script)
                        log(this::class.java, Log.ERR, "Tried to queue an item UseWith interaction for a non-player entity!")
                        continue
                    }
                    if (script.nextExecution > GameWorld.ticks)
                        continue
                    if ((script.strength == QueueStrength.STRONG)) {
                        closeAllInterfaces(entity)
                    }
                    script.nextExecution = GameWorld.ticks + 1
                    val finished = executeScript(script)
                    script.state++
                    if (finished && !script.persist)
                        toRemove.add(script)
                    else if (finished)
                        script.state = 0
                    anyExecuted = true
                }
            }
        }

        queue.removeAll(toRemove.toSet())
        return !anyExecuted
    }

    fun isPersist(script: Script<*>) : Boolean {
        return script.persist
    }

    fun executeScript(script: Script<*>) : Boolean {
        currentScript = script
        try {
            if (this is EntityScriptProcessor) {
                when (script) {
                    is Interaction -> return script.execution.invoke(entity as? Player ?: return true, interactTarget ?: return true, script.state)
                    is UseWithInteraction -> return script.execution.invoke(entity as? Player ?: return true, script.used, script.with, script.state)
                    is QueuedUseWith -> return script.execution.invoke(entity as? Player ?: return true, script.used, script.with, script.state)
                }
            }
            if (script is QueuedScript) return script.execution.invoke(script.state)
        } catch (e: Exception) {
            val sw = StringWriter()
            val pw = PrintWriter(sw)
            e.printStackTrace(pw)
            log(this::class.java, Log.ERR, "Error processing ${script::class.java.simpleName} - stopping the script. Exception follows: $sw")
            reset()
        }
        currentScript = null
        return true
    }

    fun removeWeakScripts() {
        queue.removeAll(queue.filter { it is QueuedScript && it.strength == QueueStrength.WEAK || it is QueuedUseWith && it.strength == QueueStrength.WEAK }.toSet())
    }

    fun removeNormalScripts() {
        queue.removeAll(queue.filter { it is QueuedScript && it.strength == QueueStrength.NORMAL || it is QueuedUseWith && it.strength == QueueStrength.NORMAL }.toSet())
    }

    open fun reset() {
        currentScript = null
        persistent = false
    }

    fun addToQueue(script: Script<*>, strength: QueueStrength) {
        if (script !is QueuedScript && script !is QueuedUseWith) {
            log(this::class.java, Log.ERR, "Tried to queue ${script::class.java.simpleName} as a queueable script but it's not!")
            return
        }
        if (this is EntityScriptProcessor && entity is Player && strength == QueueStrength.STRONG) {
            closeAllInterfaces(entity)
        }
        script.nextExecution = max(GameWorld.ticks + 1, script.nextExecution)
        queue.add(script)
    }

    open fun getActiveScript() : Script<*>? {
        return currentScript
    }

    fun hasTypeInQueue(type: QueueStrength) : Boolean {
        for (script in queue) {
            if (script is QueuedScript && script.strength == type)
                return true
            else if (script is QueuedUseWith && script.strength == type)
                return true
        }
        return false
    }
}

class EntityScriptProcessor(val entity: Entity) : ScriptProcessor() {
    private var apScript: Script<*>? = null
    private var opScript: Script<*>? = null
    var interactTarget: Node? = null

    private var interacted = false
    var apRangeCalled = false
    var apRange = 10
    private var targetDestination: Location? = null

    fun preMovement() {
        var allSkipped = false
        while (!allSkipped) {
            allSkipped = processQueue()
        }

        if (isStunned(entity)) return
        if (entity.delayed()) return

        var canProcess = !entity.delayed()
        if (entity is Player && entity !is AIPlayer)
            canProcess = canProcess && !entity.hasModalOpen()

        if (entity !is Player) return
        if (!entity.delayed() && canProcess && interactTarget != null) {
            if (opScript != null && inOperableDistance()) {
                face(entity, interactTarget?.getFaceLocation(entity.location) ?: return reset())
                processInteractScript(opScript ?: return reset())
            }
            else if (apScript != null && inApproachDistance(apScript ?: return reset())) {
                face(entity, interactTarget?.getFaceLocation(entity.location) ?: return reset())
                processInteractScript(apScript ?: return reset())
            }
            else if (apScript == null && opScript == null && inOperableDistance()) {
                sendMessage(entity, "Nothing interesting happens.")
            }
        }
    }

    fun postMovement(didMove: Boolean) {
        if (didMove)
            entity.clocks[Clocks.MOVEMENT] = GameWorld.ticks + if (entity.walkingQueue.isRunning) 0 else 1
        var canProcess = !entity.delayed()
        if (entity is Player && entity !is AIPlayer)
            canProcess = canProcess && !entity.interfaceManager.isOpened && !entity.interfaceManager.hasChatbox()

        if (entity !is Player) return
        if (!entity.delayed() && canProcess && interactTarget != null && !interacted) {
            if (opScript != null && inOperableDistance()) {
                face(entity, interactTarget?.centerLocation ?: return reset())
                processInteractScript(opScript ?: return reset())
            }
            else if (apScript != null && inApproachDistance(apScript ?: return reset())) {
                face(entity, interactTarget?.centerLocation ?: return reset())
                processInteractScript(apScript ?: return reset())
            }
            else if (apScript == null && opScript == null && inOperableDistance()) {
                sendMessage(entity, "Nothing interesting happens.")
            }
        }
        if (canProcess && (apScript != null || opScript != null)) {
            if (!interacted && !didMove && finishedMoving(entity)) {
                sendMessage(entity, "I can't reach that!")
                reset()
            }
        }
        if (interacted && !apRangeCalled && !persistent) reset()
        if (interactTarget != null && interactTarget?.isActive != true) reset()
    }

    private fun processInteractScript(script: Script<*>) {
        if (interactTarget == null || !interactTarget!!.isActive) {
            log(this::class.java, Log.FINE, "Interact target $interactTarget no longer active, cancelling interaction.")
            reset()
        }
        if (script.nextExecution < GameWorld.ticks) {
            val finished = executeScript(script)
            script.state++
            if (finished && isPersist(script))
                script.state = 0
            interacted = true
        }
    }

    private fun inApproachDistance(script: Script<*>) : Boolean {
        val distance = when (script) {
            is Interaction -> script.distance
            is UseWithInteraction -> script.distance
            else -> 10
        }
        targetDestination?.let {
            return it.location.getDistance(entity.location) <= distance && hasLineOfSight(entity, it)
        }
        return false
    }

    private fun inOperableDistance() : Boolean {
        targetDestination?.let {
            return it.cardinalTiles.any {loc -> loc == entity.location} && hasLineOfSight(entity, it)
        }
        return false
    }

    override fun reset() {
        super.reset()
        apScript = null
        opScript = null
        apRangeCalled = false
        interacted = false
        apRange = 10
        interactTarget = null
        targetDestination = null
        resetAnimator(entity as? Player ?: return)
    }

    fun setInteractionScript(target: Node, script: Script<*>?) {
        if (apScript != null && script != null && script.execution == apScript!!.execution) return
        if (opScript != null && script != null && script.execution == opScript!!.execution) return
        reset()
        interactTarget = target
        if (script != null) {
            apRange = when(script) {
                is Interaction -> script.distance
                is UseWithInteraction -> script.distance
                else -> 10
            }
            persistent = script.persist
            if (apRange == -1)
                opScript = script
            else
                apScript = script
            targetDestination = when (interactTarget) {
                is NPC -> DestinationFlag.ENTITY.getDestination(entity, interactTarget)
                is Scenery -> {
                    val basicPath = Pathfinder.find(entity, interactTarget)
                    val path = basicPath.points.lastOrNull()
                    if (basicPath.isMoveNear) {
                        target.location
                        return
                    }
                    if (path == null) {
                        clearScripts(entity)
                        return
                    }
                    Location.create(path.x, path.y, entity.location.z)
                }
                is GroundItem -> DestinationFlag.ITEM.getDestination(entity, interactTarget)
                else -> target.location
            }
        }
    }

    override fun getActiveScript() : Script<*>? {
        return currentScript ?: getActiveInteraction()
    }

    private fun getActiveInteraction() : Script<*>? {
        return opScript ?: apScript
    }
}
