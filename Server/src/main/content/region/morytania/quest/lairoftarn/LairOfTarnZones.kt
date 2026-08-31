package content.region.morytania.quest.lairoftarn

import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.system.timer.PersistTimer
import core.game.system.timer.RSTimer
import core.game.system.timer.TimerFlag
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction
import core.tools.RandomFunction.random

/**
 * Zones covering Tarn's Lair.
 */

// listens for run-ins with the traps
class LairOfTarnZone : MapArea {

    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(
            ZoneBorders(3136, 4544, 3199, 4671)
        )
    }

    // cannons are not allowed. This area is multicombat.
    override fun getRestrictions(): Array<ZoneRestriction> {
        return arrayOf(
            ZoneRestriction.CANNON
        )
    }

    var floorTraps: List<Location?> = listOf<Location?>(
        Location.create(3167, 4549, 0),
        Location.create(3165, 4553, 0),
        Location.create(3167, 4553, 0),
        Location.create(3158, 4550, 0),
        Location.create(3176, 4550, 0),
        Location.create(3174, 4552, 0),
        Location.create(3178, 4548, 0),
        Location.create(3177, 4547, 0),
        Location.create(3183, 4551, 0),
        Location.create(3185, 4551, 0),
        Location.create(3190, 4548, 0),
        Location.create(3191, 4551, 0),
        Location.create(3187, 4561, 0),
        Location.create(3183, 4563, 0),
        Location.create(3181, 4561, 0),
        Location.create(3182, 4560, 0),
        Location.create(3179, 4559, 0),
        Location.create(3192, 4554, 0),
        Location.create(3195, 4580, 1),
        Location.create(3191, 4584, 1),
        Location.create(3185, 4586, 1),
        Location.create(3180, 4585, 1),
        Location.create(3175, 4584, 1),
        Location.create(3176, 4581, 1),
        Location.create(3186, 4576, 0),
        Location.create(3182, 4574, 0),
        Location.create(3184, 4574, 0),
        Location.create(3186, 4574, 0),
        Location.create(3184, 4572, 0),
        Location.create(3167, 4587, 0),
        Location.create(3169, 4589, 0),
        Location.create(3167, 4591, 0),
        Location.create(3171, 4598, 0),
        Location.create(3168, 4599, 0),
        Location.create(3164, 4597, 0),
        Location.create(3161, 4598, 0),
        Location.create(3149, 4600, 0),
        Location.create(3147, 4596, 0),
        Location.create(3147, 4594, 0),
        Location.create(3143, 4594, 0),
        Location.create(3143, 4598, 0),
        Location.create(3158, 4558, 1),
        Location.create(3193, 4602, 1),
        Location.create(3159, 4561, 1),
        Location.create(3157, 4561, 1),
        Location.create(3161, 4563, 1),
        Location.create(3163, 4563, 1),
        Location.create(3168, 4562, 1),
        Location.create(3169, 4559, 1),
        Location.create(3174, 4560, 1),
        Location.create(3174, 4562, 1),
        Location.create(3192, 4595, 1),
        Location.create(3194, 4597, 1),
        Location.create(3173, 4569, 1),
        Location.create(3175, 4569, 1),
        Location.create(3165, 4569, 0),
        Location.create(3163, 4569, 0),
        Location.create(3162, 4566, 0),
        Location.create(3162, 4559, 0),
        Location.create(3152, 4558, 0),
        Location.create(3144, 4554, 0),
        Location.create(3141, 4555, 0),
        Location.create(3152, 4552, 0),
        Location.create(3150, 4548, 0),
        Location.create(3143, 4547, 1),
        Location.create(3141, 4549, 1),
        Location.create(3149, 4574, 1),
        Location.create(3146, 4573, 1),
        Location.create(3143, 4574, 1),
        Location.create(3144, 4583, 1),
        Location.create(3153, 4583, 0),
        Location.create(3158, 4582, 0),
        Location.create(3159, 4579, 0)
    )

    // each trap is two tiles wide
    var wallTraps: List<Location?> = listOf<Location?>(
        Location.create(3196, 4557, 0),
        Location.create(3196, 4558, 0),

        Location.create(3196, 4562, 0),
        Location.create(3196, 4563, 0),

        Location.create(3147, 4589, 1),
        Location.create(3146, 4589, 1),

        Location.create(3149, 4604, 1),
        Location.create(3150, 4604, 1),

        Location.create(3154, 4604, 1),
        Location.create(3155, 4604, 1),

        Location.create(3150, 4565, 1),
        Location.create(3149, 4565, 1),

        Location.create(3151, 4569, 1),
        Location.create(3151, 4570, 1),

        Location.create(3155, 4592, 1),
        Location.create(3155, 4593, 1),
    )

    override fun entityStep(entity: Entity, location: Location, lastLocation: Location) {
        if (entity is Player) {
            if (floorTraps.contains(entity.location) && entity.location != LairOfTarnListeners.lastTrap) {
                sendMessage(entity, "You triggered a floor trap!") // TODO: there is also "You triggered a floor trap, but fortunately weren't standing on it!"
                impact(entity, random(1, 5))
                sendChat(entity, "Ouch!")

            } else if (wallTraps.contains(entity.getLocation())) {
                lockInteractions(entity, 4)
                entity.walkingQueue.walkBack()
                sendMessage(entity, "You activate a spear trap, but quickly escape.")
                impact(entity, random(1, 5))
                sendChat(entity, "Ouch!")
                // TODO: animate the wall traps
            }
        }
        super.entityStep(entity, location, lastLocation)
    }
}

// zone covering the Terror Dog portion of Tarn's Lair and the boss fight. it continuously drains prayer.
class LairOfTarnPrayerDrainZone : MapArea {

    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(
            ZoneBorders(3136, 4608, 3199, 4671)
        )
    }

    // start the prayer drain timer
    override fun areaEnter(entity: Entity) {
        if (entity is Player && !getAttribute(entity, LairOfTarn.ATTR_TARN_CUTSCENE, false)) {
            // exact prayer drain rate is unknown, but in one of the video sources, with Protect Melee active prayer drains a bit faster than 1 point per second
            sendMessage(entity, "The inherent evil of the area rapidly drains your prayer points!")
            getOrStartTimer<LairOfTarnPrayerDrainTimer>(entity)
        }
    }

    // remove timer if player leaves
    override fun areaLeave(entity: Entity, logout: Boolean) {
        if (entity is Player) {
            removeTimer(entity, "tarnprayerdrain")
        }
    }
}

// timer to drain prayer
class LairOfTarnPrayerDrainTimer : PersistTimer(6, "tarnprayerdrain", flags = arrayOf(TimerFlag.ClearOnDeath)) {

    // return false stops the timer, return true keeps it running
    override fun run(entity: Entity): Boolean {

        // drain prayer
        entity.getSkills().decrementPrayerPoints(1.0)

        return true
    }

    override fun onRegister(entity: Entity) {
        if (entity !is Player) return
    }

    override fun getTimer(vararg args: Any): RSTimer {
        val t = LairOfTarnPrayerDrainTimer()
        t.runInterval = args.getOrNull(0) as? Int ?: 6
        return t
    }
}