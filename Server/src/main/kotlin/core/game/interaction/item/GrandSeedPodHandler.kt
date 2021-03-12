package core.game.interaction.item

import core.cache.def.impl.ItemDefinition
import core.tools.Items
import core.game.content.global.travel.glider.GliderPulse
import core.game.content.global.travel.glider.Gliders
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.plugin.Plugin
import core.game.node.entity.skill.Skills

private const val SQUASH_GRAPHICS_BEGIN = 767
private const val SQUASH_GRAPHICS_END = 769
private const val SQUASH_ANIM_BEGIN = 4544
private const val SQUASH_ANIM_END = 4546
private const val LAUNCH_GRAPHICS = 768
private const val LAUNCH_ANIMATION = 4547

/**
 * Handles the grand tree pod options
 * @author Ceikry
 */
@Initializable
class GrandSeedPodHandler : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        val def = ItemDefinition.forId(Items.GRAND_SEED_POD_9469)
        def.handlers["option:squash"] = this
        def.handlers["option:launch"] = this
        return this
    }

    override fun handle(player: Player?, node: Node?, option: String?): Boolean {
        player ?: return false
        node   ?: return false
        option ?: return false
        when(option){
            "squash" -> {
                GameWorld.Pulser.submit(SquashPulse(player))
            }
            "launch" -> {
                GameWorld.Pulser.submit(LaunchPulse(player))
            }

        }
        player.inventory.remove(Item(node.id,1))
        player.lock()
        return true
    }

    class LaunchPulse(val player: Player): Pulse(){
        var counter = 0
        override fun pulse(): Boolean {
            when(counter++){
                1 -> player.visualize(Animation(LAUNCH_ANIMATION),Graphics(LAUNCH_GRAPHICS))
                3 -> player.skills.addExperience(Skills.FARMING,100.0)
                4 -> GameWorld.Pulser.submit(GliderPulse(2,player,Gliders.TA_QUIR_PRIW)).also { return true }
            }
            return false
        }
    }

    class SquashPulse(val player: Player) : Pulse(){
        var counter = 0
        override fun pulse(): Boolean {
            when(counter++){
                1 -> player.visualize(Animation(SQUASH_ANIM_BEGIN), Graphics(SQUASH_GRAPHICS_BEGIN))
                4 -> player.animator.forceAnimation(Animation(1241))
                5 -> player.properties.teleportLocation = Location.create(2464, 3494, 0)
                6 -> player.visualize(Animation(1241),Graphics(SQUASH_GRAPHICS_END))
                8 -> player.animator.forceAnimation(Animation(SQUASH_ANIM_END)).also { player.skills.setLevel(Skills.FARMING,player.skills.getStaticLevel(Skills.FARMING) - 5) }
                9 -> player.unlock().also { return true }
            }
            return false
        }
    }
}