package rs09.game.content.global.worldevents.shootingstar

import core.game.content.global.worldevents.shootingstar.ScoreboardHandler
import core.game.content.global.worldevents.shootingstar.ShootingStarScoreboard
import core.game.content.global.worldevents.shootingstar.StarChartPlugin
import core.game.system.task.Pulse
import org.json.simple.JSONObject
import rs09.ServerStore
import rs09.game.content.global.worldevents.PluginSet
import rs09.game.content.global.worldevents.WorldEvent
import rs09.game.content.global.worldevents.WorldEvents
import rs09.game.world.GameWorld


/**
 * The world event class for shooting stars. Keeps track of event-related data like the ShootingStar instance.
 * Also handles spawning new shooting stars based on time.
 * @author Ceikry
 */
class ShootingStarEvent : WorldEvent("shooting-stars") {
    val star = ShootingStar()
    val tickDelay = if(GameWorld.settings?.isDevMode == true) 200 else 25000


    override fun initialize() {
        plugins = PluginSet(
                ScoreboardHandler(),
                ShootingStarScoreboard(),
                StarChartPlugin(),
                ShootingStarCommands(),
                StarSpriteDialogue(),
                ShootingStarLogin()
        )
        super.initialize()
        GameWorld.Pulser.submit(StarPulse())
        log("Shooting Star event has been initialized.")
    }

    override fun checkTrigger(): Boolean {
        /**
         * Spawn a new star only if: star's ticks are greater than the tickDelay and star sprite is not spawned OR,
         * neither the star or the sprite are spawned.
         */
        star.ticks += 10
        if ((star.ticks >= tickDelay && !star.spriteSpawned) || (!star.isSpawned && !star.spriteSpawned)) {
            return true
        }

        /**
         * Clear the sprite when we have passed the tickDelay + (1/3) of the tickDelay.
         * This gives players a little extra time before the star respawns to run to the bank and grab
         * stardust or whatever. Once the sprite is cleared a new star will be allowed to spawn.
         */
        val maxDelay = tickDelay + (tickDelay / 3)
        if(star.ticks > maxDelay && star.spriteSpawned){
            star.clearSprite()
        }

        return false
    }

    override fun checkActive(): Boolean {
        return true //this event is always active.
    }

    override fun fireEvent() {
        log("Fired new shooting star event.")
        star.fire()
    }

    /**
     * Handles checking star status and spawning new ones if necessary.
     */
    class StarPulse : Pulse(10){
        override fun pulse(): Boolean {
            val event = WorldEvents.get("shooting-stars")
            event ?: return true

            if(event.checkTrigger()){
                event.fireEvent()
            }

            return false //always returns false because it needs to run forever.
        }
    }

    companion object {
        fun getStoreFile() : JSONObject {
            return ServerStore.getArchive("shooting-star")
        }
    }
}