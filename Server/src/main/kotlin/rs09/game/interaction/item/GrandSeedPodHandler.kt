package rs09.game.interaction.item

import core.cache.def.impl.ItemDefinition
import core.game.content.global.travel.glider.GliderPulse
import core.game.content.global.travel.glider.Gliders
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs09.consts.Items
import rs09.game.interaction.InteractionListener
import rs09.game.world.GameWorld
import api.*

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
class GrandSeedPodHandler : InteractionListener() {

    override fun defineListeners() {
        on(Items.GRAND_SEED_POD_9469, ITEM, "squash", "launch"){player, _ ->
            when(getUsedOption(player)){
                "launch" -> submitWorldPulse(LaunchPulse(player))
                "squash" -> submitWorldPulse(SquashPulse(player))
            }
            removeItem(player, Items.GRAND_SEED_POD_9469, api.Container.INVENTORY)
            lock(player, 50)
            return@on true
        }
    }

    class LaunchPulse(val player: Player): Pulse(){
        var counter = 0
        override fun pulse(): Boolean {
            when(counter++){
                1 -> visualize(player, LAUNCH_ANIMATION, LAUNCH_GRAPHICS)
                3 -> rewardXP(player, Skills.FARMING, 100.0)
                4 -> submitWorldPulse(GliderPulse(2,player,Gliders.TA_QUIR_PRIW)).also { return true }
            }
            return false
        }
    }

    class SquashPulse(val player: Player) : Pulse(){
        var counter = 0
        override fun pulse(): Boolean {
            when(counter++){
                1 -> visualize(player, SQUASH_ANIM_BEGIN, SQUASH_GRAPHICS_BEGIN)
                4 -> animate(player, 1241, true)
                5 -> teleport(player, Location.create(2464, 3494, 0))
                6 -> visualize(player, anim = 1241, gfx = SQUASH_GRAPHICS_END)
                8 -> animate(player, SQUASH_ANIM_END, true).also { adjustLevel(player, Skills.FARMING, -5) }
                9 -> unlock(player).also { return true }
            }
            return false
        }
    }
}