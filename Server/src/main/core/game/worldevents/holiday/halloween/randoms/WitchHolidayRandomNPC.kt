package core.game.worldevents.holiday.halloween.randoms

import core.api.*
import core.game.interaction.QueueStrength
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.npc.NPC
import core.game.worldevents.holiday.ResetHolidayAppearance
import core.game.worldevents.holiday.HolidayRandomEventNPC
import core.game.worldevents.holiday.HolidayRandoms
import core.tools.RandomFunction
import org.rs09.consts.Sounds

class WitchHolidayRandomNPC() : HolidayRandomEventNPC(611) {
    override fun init() {
        super.init()
        when (RandomFunction.getRandom(4)) {
            0 -> {
                queueScript(this, 6, QueueStrength.SOFT) { stage: Int ->
                    when (stage) {
                        0 -> {
                            sendChat(this, "Brackium Emendo!")
                            this.face(player)
                            playGlobalAudio(this.location, Sounds.CONFUSE_CAST_AND_FIRE_119)
                            animate(this, 1978)
                            spawnProjectile(this, player, 109)
                            return@queueScript delayScript(this, 2)
                        }
                        1 -> {
                            player.appearance.transformNPC(3138)
                            playAudio(player, Sounds.SKELETON_RESURRECT_1687)
                            registerTimer(player, ResetHolidayAppearance())
                            return@queueScript delayScript(this, 4)
                        }
                        2 -> {
                            sendChat(this, "That was not right...")
                            visualize(this, 857, -1)
                            HolidayRandoms.terminateEventNpc(player)
                            return@queueScript stopExecuting(this)
                        }
                        else -> return@queueScript stopExecuting(this)
                    }
                }
            }
            1 -> {
                queueScript(this, 6, QueueStrength.SOFT) { stage: Int ->
                    when (stage) {
                        0 -> {
                            sendChat(this, "Bombarda!")
                            this.face(player)
                            playGlobalAudio(this.location, Sounds.CURSE_CAST_AND_FIRE_127)
                            animate(this, 1978)
                            spawnProjectile(this, player, 109)
                            return@queueScript delayScript(this, 2)
                        }
                        1 -> {
                            playGlobalAudio(player.location, Sounds.EXPLOSION_1487)
                            visualize(player, -1, 659)
                            val hit = if (player.skills.lifepoints < 5) 0 else 2
                            impact(player, hit, ImpactHandler.HitsplatType.NORMAL)
                            return@queueScript delayScript(this, 2)
                        }
                        2 -> {
                            HolidayRandoms.terminateEventNpc(player)
                            return@queueScript stopExecuting(this)
                        }
                        else -> return@queueScript stopExecuting(this)
                    }
                }
            }
            2 -> {
                queueScript(this, 6, QueueStrength.SOFT) {stage: Int ->
                    when (stage) {
                        0 -> {
                            sendChat(this, "Tarantallegra!")
                            this.face(player)
                            playGlobalAudio(this.location, Sounds.WEAKEN_CAST_AND_FIRE_3011)
                            animate(this, 1978)
                            spawnProjectile(this, player, 109)
                            return@queueScript delayScript(this, 2)
                        }
                        1 -> {
                            animate(player, 3543, true)
                            sendMessage(player, "You suddenly burst into dance.")
                            return@queueScript delayScript(this, 2)
                        }
                        2 -> {
                            visualize(this, 861, -1)
                            playGlobalAudio(this.location, Sounds.HUMAN_LAUGH_1_3071)
                            HolidayRandoms.terminateEventNpc(player)
                            return@queueScript stopExecuting(this)
                        }
                        else -> return@queueScript stopExecuting(this)
                    }
                }
            }
            3 -> {
                queueScript(this, 6, QueueStrength.SOFT) {stage: Int ->
                    when (stage) {
                        0 -> {
                            sendChat(this, "Vespertilio!")
                            this.face(player)
                            playGlobalAudio(this.location, Sounds.WEAKEN_CAST_AND_FIRE_3011)
                            animate(this, 1978)
                            spawnProjectile(this, player, 109)
                            return@queueScript delayScript(this, 2)
                        }
                        1 -> {
                            visualize(player, 10530, 1863)
                            playGlobalAudio(player.location, Sounds.VAMPIRE_SUMMON_1899)
                            return@queueScript delayScript(this, 4)
                        }
                        2 -> {
                            player.appearance.transformNPC(6835)
                            HolidayRandoms.terminateEventNpc(player)
                            registerTimer(player, ResetHolidayAppearance())
                            return@queueScript stopExecuting(this)
                        }
                        else -> return@queueScript stopExecuting(this)
                    }
                }
            }
            4 -> {
                queueScript(this, 6, QueueStrength.SOFT) {stage: Int ->
                    when (stage) {
                        0 -> {
                            sendChat(this, "Sella!")
                            this.face(player)
                            playGlobalAudio(this.location, Sounds.WEAKEN_CAST_AND_FIRE_3011)
                            animate(this, 1978)
                            spawnProjectile(this, player, 109)
                            return@queueScript delayScript(this, 2)
                        }
                        1 -> {
                            player.appearance.transformNPC(3293)
                            playGlobalAudio(player.location, Sounds.KR_JUDGE_HAMMER_3822)
                            registerTimer(player, ResetHolidayAppearance())
                            return@queueScript delayScript(this, 4)
                        }
                        2 -> {
                            HolidayRandoms.terminateEventNpc(player)
                            return@queueScript stopExecuting(this)
                        }
                        else -> return@queueScript stopExecuting(this)
                    }
                }
            }
        }
    }

    override fun talkTo(npc: NPC) {
    }
}