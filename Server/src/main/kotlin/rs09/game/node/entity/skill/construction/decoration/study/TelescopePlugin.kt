package rs09.game.node.entity.skill.construction.decoration.study

import core.cache.def.impl.SceneryDefinition
import core.game.component.Component
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.scenery.Scenery
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction
import rs09.game.content.global.worldevents.WorldEvents
import rs09.game.content.global.worldevents.shootingstar.ShootingStarEvent
import rs09.game.world.GameWorld.Pulser
import java.util.concurrent.TimeUnit

@Initializable
class TelescopePlugin : OptionHandler() {
    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any?>? {
        SceneryDefinition.forId(13656).handlers["option:observe"] = this
        SceneryDefinition.forId(13657).handlers["option:observe"] = this
        SceneryDefinition.forId(13658).handlers["option:observe"] = this
        return this
    }

    override fun handle(player: Player?, node: Node?, option: String?): Boolean {
        val star = (WorldEvents.get("shooting-stars") as ShootingStarEvent).star
        val delay: Int = 25000 + (25000 / 3)
        val timeLeft = delay - star.ticks
        val fakeTimeLeftBecauseFuckPlayers = TimeUnit.MILLISECONDS.toMinutes(timeLeft * 600L) + if(RandomFunction.random(0,100) % 2 == 0) 2 else -2
        val obj = node?.asScenery() as Scenery
        player?.lock()
        player?.animate(ANIMATION)
        player?.interfaceManager?.open(Component(782)).also { player?.unlock()
        Pulser.submit(object : Pulse(2, player) {
            override fun pulse(): Boolean {
                if (obj.isActive) {
                    player?.dialogueInterpreter?.sendDialogue("You see a shooting star! The star looks like it will land","in about $fakeTimeLeftBecauseFuckPlayers minutes!")
                    return true
                }
                return true
            }
        })
        return true
    }
    }

    companion object {
        private val ANIMATION = Animation.create(3649)
    }
}