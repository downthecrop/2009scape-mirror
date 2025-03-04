package content.region.morytania.handlers

import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import org.rs09.consts.NPCs
import core.game.bots.AIPlayer
import core.game.dialogue.DialogueFile
import core.game.world.GameWorld
import content.data.Quests

class MorytaniaArea : MapArea {
    override fun defineAreaBorders(): Array<ZoneBorders> {
       return arrayOf(
               ZoneBorders(3426, 3191, 3715, 3588), //Morytania overworld
               ZoneBorders(3520, 9856, 3583, 9919) //Werewolf agility course
       )
    }

    override fun areaEnter(entity: Entity) {
        if (entity is Player && entity !is AIPlayer && (
            !isQuestComplete(entity, Quests.PRIEST_IN_PERIL) || //not allowed to be anywhere in Morytania
            defineAreaBorders()[1].insideBorder(entity) //Werewolf agility course is not implemented
        )) {
            kickThemOut(entity)
        }
    }

    private fun kickThemOut(entity: Player) {
        val watchdog = NPC(NPCs.ABIDOR_CRANK_3635)
        watchdog.isNeverWalks = true
        watchdog.isWalks = false
        watchdog.location = entity.location
        watchdog.init()
        entity.lock()

        runTask(watchdog, 1) {
            watchdog.moveStep()
            watchdog.face(entity)
            openDialogue(entity, FuckOffDialogue(), watchdog)
            GameWorld.Pulser.submit(object : Pulse() {
                override fun pulse(): Boolean {
                    if (getAttribute(entity, "teleporting-away", false))
                        return true
                    if (!entity.isActive)
                        poofClear(watchdog)
                    if (entity.dialogueInterpreter.dialogue == null || entity.dialogueInterpreter.dialogue.file == null)
                        openDialogue(entity, FuckOffDialogue(), watchdog)
                    return !watchdog.isActive || !entity.isActive
                }
            })
        }
    }

    class FuckOffDialogue : DialogueFile() {
        override fun handle(componentID: Int, buttonID: Int) {
            when(stage) {
                0 -> npcl(core.game.dialogue.FacialExpression.WORRIED, "Oh this is no good, you surely will not survive here. Let me take you back.").also { stage++ }
                1 -> {
                    end()
                    visualize(npc!!, 1818, 343)
                    sendGraphics(342, player!!.location)
                    setAttribute(player!!, "teleporting-away", true)
                    runTask(player!!, 3) {
                        poofClear(npc!!)
                        teleport(player!!, Location.create(3402, 3485, 0))
                        unlock(player!!)
                        removeAttribute(player!!, "teleporting-away")
                    }
                }

            }
        }
    }
}


