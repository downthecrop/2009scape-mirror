package rs09.game.interaction.player

import core.game.interaction.Interaction
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.impl.Projectile
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.map.path.Pathfinder
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs09.consts.Items
import rs09.game.world.GameWorld
import rs09.tools.stringtools.colorize

val snowball = Item(Items.SNOWBALL_11951)
val THROW_ANIMATION = Animation(7530)
val THROW_GRAPHICS = Graphics(860)

@Initializable
class PeltOptionPlugin : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        return this
    }

    override fun handle(player: Player?, node: Node?, option: String?): Boolean {
        player ?: return false
        val other = node?.asPlayer()
        val delay = 41
        val speed = 60
        val distance = Location.getDistance(player.location, other?.getLocation()).toInt()
        val projectileSpeed = delay + speed + distance * 5
        val hitDelay = (projectileSpeed * .02857).toInt()

        if(!Pathfinder.find(player,other,false, Pathfinder.PROJECTILE).isSuccessful){
            player.dialogueInterpreter.sendDialogue("You can't reach them!")
            return true
        }

        if(player.inventory.remove(snowball) || player.equipment.remove(snowball)){
            player.lock()
            GameWorld.Pulser.submit(object : Pulse(hitDelay + 1){
                override fun pulse(): Boolean {
                    other?.animator?.graphics(Graphics(1282))
                    return true
                }
            })
            GameWorld.Pulser.submit(object : Pulse() {
                var counter = 0
                override fun pulse(): Boolean {
                    when (counter++) {
                        0 -> {
                            player.lock()
                            player.face(other)
                            player.animator.forceAnimation(THROW_ANIMATION)
                        }
                        1 -> {
                            Projectile.create(player, other, 861, 30, 10).send()
                            player.face(player)
                            player.unlock()
                        }
                        5 -> other?.sendMessage(colorize("%R${player.username} has pelted you with a snowball."))
                        6 -> return true
                    }
                    return false
                }
            })
        } else {
            player.sendMessage("You have no more snowballs.")
            Interaction.sendOption(player, 0, "null")
        }
        return true
    }

    override fun isWalk(): Boolean {
        return false
    }

}