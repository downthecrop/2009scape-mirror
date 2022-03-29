package rs09.game.node.entity.skill.slayer

import api.events.EventHook
import api.events.NPCKillEvent
import api.rewardXP
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.entity.skill.slayer.Master

object SlayerKillHook : EventHook<NPCKillEvent>
{
    override fun process(entity: Entity, event: NPCKillEvent) {
        val npc = event.npc
        val player = entity as? Player ?: return
        val slayer = player.slayer
        val flags = slayer.flags

        if (slayer.hasTask()) {
            rewardXP(player, Skills.SLAYER, npc.skills.maximumLifepoints.toDouble())
            slayer.decrementAmount(1)
            if(slayer.hasTask()) return
            flags.taskStreak = flags.taskStreak + 1
            flags.completedTasks = flags.completedTasks + 1
            if ((flags.completedTasks > 4 || flags.canEarnPoints()) && flags.getMaster() != Master.TURAEL && flags.getPoints() < 64000) {
                var points = flags.getMaster().taskPoints[0]
                if (flags.taskStreak % 50 == 0) {
                    points = flags.getMaster().taskPoints[2]
                } else if (flags.taskStreak % 10 == 0) {
                    points = flags.getMaster().taskPoints[1]
                }
                flags.incrementPoints(points)
                if (flags.getPoints() > 64000) {
                    flags.setPoints(64000)
                }
                player.sendMessages("You've completed " + flags.taskStreak + " tasks in a row and received " + points + " points, with a total of " + flags.getPoints(), "You have completed " + flags.completedTasks + " tasks in total. Return to a Slayer master.")
            } else if (flags.completedTasks == 4) {
                player.sendMessage("You've completed your task; you will start gaining points on your next task!")
                flags.flagCanEarnPoints()
            } else if (flags.getMaster() == Master.TURAEL) {
                player.sendMessages("You've completed your task; Tasks from Turael do not award points.", "Return to a Slayer master.")
            } else {
                player.sendMessages("You've completed your task; Complete " + (4 - flags.completedTasks) + " more task(s) to start gaining points.", "Return to a Slayer master.")
            }
        }
    }
}