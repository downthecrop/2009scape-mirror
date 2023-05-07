package content.region.misthalin.draynor.handlers

import content.global.skill.agility.AgilityHandler
import core.api.hasLevelDyn
import core.api.sendDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.world.map.Direction
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Scenery

class DraynorManorShortcutListener : InteractionListener {
    override fun defineListeners() {

        val BROKEN_RAILING = Scenery.BROKEN_RAILING_37703
        val SQUEEZE_ANIMATION = 3844

        on(BROKEN_RAILING, IntType.SCENERY, "squeeze-through") { player, _ ->
            if (!hasLevelDyn(player, Skills.AGILITY, 28)) {
                sendDialogue(player, "You need an Agility level of at least 28 to do this.")
                return@on true
            }

            val squeezeAnim = Animation.create(SQUEEZE_ANIMATION)
            if (player.location.x >= 3086) {
                AgilityHandler.forceWalk(
                    player, -1, player.location,
                    player.location.transform(Direction.WEST, 1),
                    squeezeAnim, 5, 0.0, null
                )
            } else {
                AgilityHandler.forceWalk(
                    player, -1, player.location,
                    player.location.transform(Direction.EAST, 1),
                    squeezeAnim, 5, 0.0, null
                )
            }
            return@on true
        }
    }
}