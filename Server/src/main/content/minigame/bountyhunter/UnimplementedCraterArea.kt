package content.minigame.bountyhunter.handlers

import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import org.rs09.consts.NPCs
import core.game.dialogue.DialogueFile
import core.game.world.GameWorld

class UnimplementedCraterArea : MapArea {
    override fun defineAreaBorders(): Array<ZoneBorders> {
       return arrayOf(
               ZoneBorders(3200, 5632, 3391, 5823)
       )
    }

    override fun areaEnter(entity: Entity) {
        if (entity is Player && (
            defineAreaBorders()[0].insideBorder(entity)
        )) {
            kickThemOut(entity)
        }
    }

    private fun kickThemOut(entity: Player) {
        val watchdog = NPC(NPCs.BANKER_6538)
        watchdog.isNeverWalks = true
        watchdog.isWalks = false
        watchdog.location = entity.location
        watchdog.init()
        entity.lock()

        runTask(watchdog, 1) {
            watchdog.moveStep()
            watchdog.face(entity)
            openDialogue(entity, UnimplementedCraterDialogue(), watchdog)
            GameWorld.Pulser.submit(object : Pulse() {
                override fun pulse(): Boolean {
                    if (getAttribute(entity, "teleporting-away", false))
                        return true
                    if (!entity.isActive)
                        poofClear(watchdog)
                    if (entity.dialogueInterpreter.dialogue == null || entity.dialogueInterpreter.dialogue.file == null)
                        openDialogue(entity, UnimplementedCraterDialogue(), watchdog)
                    return !watchdog.isActive || !entity.isActive
                }
            })
        }
    }

    class UnimplementedCraterDialogue : DialogueFile() {
        override fun handle(componentID: Int, buttonID: Int) {
            when(stage) {
                0 -> npcl(core.game.dialogue.FacialExpression.WORRIED, "This is unimplemented content, and you are now stuck. Don't worry, I'll get you out of here!").also { stage++ }
                1 -> {
                    end()
                    visualize(npc!!, 1818, 343)
                    sendGraphics(342, player!!.location)
                    setAttribute(player!!, "teleporting-away", true)
                    runTask(player!!, 3) {
                        poofClear(npc!!)
                        teleport(player!!, Location.create(3179, 3685, 0))
                        unlock(player!!)
                        removeAttribute(player!!, "teleporting-away")
                    }
                }
            }
        }
    }
}

