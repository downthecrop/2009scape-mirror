package content.global.handlers.item.equipment.special

import core.ServerConstants
import core.api.playAudio
import core.game.container.impl.EquipmentContainer
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.MeleeSwingHandler
import core.game.node.entity.combat.equipment.Weapon
import core.game.node.entity.impl.Animator.Priority
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.audio.Audio
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction
import org.rs09.consts.Items
import org.rs09.consts.Sounds


/**
 * Handles the Dragon axe special attack.
 * The OSRS special attack can be used instead by setting dragon_axe_use_osrs_spec server constant to true
 * @author Crash
 */
@Initializable
class ClobberSpecialHandler : MeleeSwingHandler(), Plugin<Any> {
    override fun newInstance(arg: Any?): Plugin<Any> {
        CombatStyle.MELEE.swingHandler.register(ITEM.id, this)
        return this
    }

    override fun fireEvent(identifier: String, vararg args: Any): Any {
        return if (ServerConstants.DRAGON_AXE_USE_OSRS_SPEC && identifier == "instant_spec") true else Unit
    }

    override fun visualize(entity: Entity, victim: Entity?, state: BattleState?) {
        entity.visualize(ANIMATION, GRAPHIC)
    }

    override fun swing(entity: Entity?, victim: Entity?, state: BattleState?): Int {
        return if (ServerConstants.DRAGON_AXE_USE_OSRS_SPEC) {
            swingOSRS(entity)
        } else {
            swingAuthentic(entity, victim, state)
        }
    }

    /**
     * https://runescape.wiki/w/Dragon_hatchet?oldid=1107166
     */
    private fun swingAuthentic(entity: Entity?, victim: Entity?, state: BattleState?): Int {
        val player = entity as? Player ?: return -1
        if (victim == null) return -1
        if (state == null) return -1
        if (!player.settings.drainSpecial(SPECIAL_ENERGY)) return -1

        state.style = CombatStyle.MELEE
        state.weapon = Weapon(player.equipment[EquipmentContainer.SLOT_WEAPON])
        var hit = 0
        if (isAccurateImpact(entity, victim, CombatStyle.MELEE)) {
            val max = calculateHit(entity, victim, 1.0)
            state.maximumHit = max
            hit = RandomFunction.random(max + 1)
        }
        state.estimatedHit = hit

        victim.skills.updateLevel(Skills.DEFENCE, -hit, 0)
        victim.skills.updateLevel(Skills.MAGIC, -hit, 0)

        playAudio(player, Sounds.CLOBBER_2531, 20)
        return 1
    }

    /**
     * https://oldschool.runescape.wiki/w/Dragon_axe
     */
    private fun swingOSRS(entity: Entity?): Int {
        val player = entity as? Player ?: return -1
        if (!player.settings.drainSpecial(SPECIAL_ENERGY)) return -1

        player.sendChat("Chop chop!")
        player.skills.updateLevel(Skills.WOODCUTTING, 3, player.skills.getStaticLevel(Skills.WOODCUTTING) + 3)
        visualize(player, null, null)
        playAudio(player, Sounds.CLOBBER_2531, 20)
        return -1
    }

    companion object {
        /**
         * The special energy required.
         */
        private const val SPECIAL_ENERGY = 100

        /**
         * The attack animation.
         */
        private val ANIMATION = Animation(2876, Priority.HIGH)

        /**
         * The graphic.
         */
        private val GRAPHIC = Graphics(479, 96)

        /**
         * The item.
         */
        private val ITEM = Item(Items.DRAGON_AXE_6739)
    }
}