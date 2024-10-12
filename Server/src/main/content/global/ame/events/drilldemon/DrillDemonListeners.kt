package content.global.ame.events.drilldemon

import core.api.*
import core.game.node.entity.npc.NPC
import org.rs09.consts.NPCs
import core.game.interaction.InteractionListener
import core.game.interaction.IntType
import core.game.interaction.QueueStrength
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction
import core.tools.secondsToTicks

import org.rs09.consts.Sounds

class DrillDemonListeners : InteractionListener, MapArea {
    val MATS = intArrayOf(10076,10077,10078,10079)
    override fun defineListeners() {

        on(DrillDemonUtils.DD_NPC, IntType.NPC, "talk-to") { player, _ ->
            if (inBorders(player, DrillDemonUtils.DD_AREA)) {
                openDialogue(player, SeargentDamienDialogue(isCorrect = true), DrillDemonUtils.DD_NPC)
            } else {
                sendMessage(player, "They aren't interested in talking to you.")
            }
            return@on true
        }

        on(MATS, IntType.SCENERY, "use") { player, node ->
            val correctTask = getAttribute(player, DrillDemonUtils.DD_KEY_TASK, -1)
            if (correctTask == -1) {
                sendMessage(player,"You can't do that right now.")
                return@on  true
            }

            val task = DrillDemonUtils.getMatTask(node.id, player)
            val npc = NPC(NPCs.SERGEANT_DAMIEN_2790)
            val anim = DrillDemonUtils.animationForTask(task)

            lock(player, secondsToTicks(30))
            player.walkingQueue.reset()
            player.walkingQueue.addPath(node.location.x,4820)
            queueScript(player, 0, QueueStrength.SOFT) { stage: Int ->
                when (stage) {
                    0 -> {
                        player.faceLocation(player.location.transform(0, -1, 0))
                        return@queueScript delayScript(player, 2)
                    }
                    1 -> {
                        animate(player, DrillDemonUtils.animationForTask(task))
                        when (task) {
                            DrillDemonUtils.DD_SIGN_JOG -> playAudio(player, Sounds.RUNONSPOT_2484, 0, 5)
                            DrillDemonUtils.DD_SIGN_SITUP -> playAudio(player, Sounds.SITUPS_2486, 40, 5)
                            DrillDemonUtils.DD_SIGN_PUSHUP -> playAudio(player, Sounds.PRESSUPS_2481, 25, 5)
                            DrillDemonUtils.DD_SIGN_JUMP -> playAudio(player, Sounds.STAR_JUMP_2492, 0, 5)
                        }
                        return@queueScript delayScript(player, anim.duration + 2)
                    }
                    2 -> {
                        DrillDemonUtils.changeSignsAndAssignTask(player)
                        if (task == correctTask) {
                            player.incrementAttribute(DrillDemonUtils.DD_CORRECT_COUNTER)
                            openDialogue(player, SeargentDamienDialogue(true), npc)
                        } else {
                            openDialogue(player, SeargentDamienDialogue(false), npc)
                        }
                        return@queueScript stopExecuting(player)
                    }
                    else -> return@queueScript stopExecuting(player)
                }
            }
            return@on true
        }
    }

    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(DrillDemonUtils.DD_AREA)
    }

    override fun getRestrictions(): Array<ZoneRestriction> {
        return arrayOf(ZoneRestriction.RANDOM_EVENTS, ZoneRestriction.CANNON, ZoneRestriction.FOLLOWERS, ZoneRestriction.OFF_MAP)
    }

    override fun areaEnter(entity: Entity) {
        if (entity is Player) {
            entity.asPlayer().interfaceManager.closeDefaultTabs()
            entity.locks.lockTeleport(1000000)
            setComponentVisibility(entity.asPlayer(), 548, 69, true)
            setComponentVisibility(entity.asPlayer(), 746, 12, true)
        }
    }
}
