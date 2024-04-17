package core.game.system.timer.impl

import core.api.*
import core.api.Event
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.prayer.PrayerType
import core.game.node.entity.skill.Skills
import core.game.system.timer.*
import core.game.event.*

import org.rs09.consts.Items

class SkillRestore : RSTimer (1, "skillrestore", isAuto = true, isSoft = true) {
    val ticksSinceLastRestore = IntArray (25)
    val restoreTicks = IntArray (25) { 100 }

    override fun run (entity: Entity) : Boolean {
        var skills = entity.skills

        for (i in 0 until 24) {
            if (i == Skills.PRAYER || i == Skills.SUMMONING) continue
            if (ticksSinceLastRestore[i]++ >= restoreTicks[i]) {
                if (i == Skills.HITPOINTS && entity.skills.lifepoints < entity.skills.maximumLifepoints) {
                    skills.heal (getHealAmount(entity))
                } else {
                    val max = getStatLevel (entity, i)
                    val current = getDynLevel (entity, i)

                    if (current != max)
                        skills.updateLevel (i, if (current < max) 1 else -1, max)
                }
                ticksSinceLastRestore[i] = 0
            }
        }

        if (entity is Player && ticksSinceLastRestore[24]++ >= 50) {
            entity.settings.setSpecialEnergy (kotlin.math.min (100, entity.settings.specialEnergy + 10))
            ticksSinceLastRestore[24] = 0
        }

        return true
    }

    override fun onRegister (entity: Entity) {
        entity.hook (Event.PrayerActivated, PrayerActivatedHook)
        entity.hook (Event.PrayerDeactivated, PrayerDeactivatedHook)
        (entity as? Player)?.debug("Registered skill restoration timer.")
    }

    private fun getHealAmount (entity: Entity) : Int {
        if (entity !is Player) return 1

        val gloves = getItemFromEquipment (entity, EquipmentSlot.HANDS)
        if (gloves == null || gloves.id != Items.REGEN_BRACELET_11133)
            return 1
        else return 2
    }

    object PrayerActivatedHook : EventHook <PrayerActivatedEvent> {
        override fun process (entity: Entity, event: PrayerActivatedEvent) {
            val restore = getOrStartTimer <SkillRestore> (entity)
            
            when (event.type) {
                PrayerType.RAPID_HEAL -> { 
                    restore.restoreTicks [Skills.HITPOINTS] = 50
                    restore.ticksSinceLastRestore [Skills.HITPOINTS] = 0
                }
                PrayerType.RAPID_RESTORE -> {
                    for (i in 0 until 24) {
                        if (i == Skills.HITPOINTS || i == Skills.PRAYER || i == Skills.SUMMONING) continue
                        restore.restoreTicks [i] = 50
                        restore.ticksSinceLastRestore [i] = 0
                    }
                }
                else -> {}
            }
        }
    }

    object PrayerDeactivatedHook : EventHook <PrayerDeactivatedEvent> {
        override fun process (entity: Entity, event: PrayerDeactivatedEvent) {
            val restore = getOrStartTimer <SkillRestore> (entity)

            when (event.type) {
                PrayerType.RAPID_HEAL -> {
                    restore.restoreTicks [Skills.HITPOINTS] = 100
                    restore.ticksSinceLastRestore [Skills.HITPOINTS] = 0
                }
                PrayerType.RAPID_RESTORE -> {
                    for (i in 0 until 24) {
                        if (i == Skills.HITPOINTS || i == Skills.PRAYER || i == Skills.SUMMONING) continue
                        restore.restoreTicks [i] = 100
                        restore.ticksSinceLastRestore [i] = 0
                    }
                }
                else -> {}
            }
        }
    }
}
