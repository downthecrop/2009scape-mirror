package content.global.skill.summoning.familiar

import core.game.node.entity.Entity
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillBonus
import core.game.node.entity.skill.Skills
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs09.consts.Items
import org.rs09.consts.NPCs

/**
 * Represents the Spirit Graahk familiar.
 * @author Aero & Bishop
 */
@Initializable
class SpiritGraahkNPC @JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.SPIRIT_GRAAHK_7363) :
    Familiar(owner, id, 4900, Items.SPIRIT_GRAAHK_POUCH_12810, 3, WeaponInterface.STYLE_AGGRESSIVE) {

    init {
        boosts.add(SkillBonus(Skills.HUNTER, 5.0))
    }

    override fun construct(owner: Player?, id: Int): Familiar {
        return SpiritGraahkNPC(owner, id)
    }

    override fun specialMove(special: FamiliarSpecial): Boolean {
        val target: Entity = special.target ?: return false
        if (!canCombatSpecial(target)) {
            return false
        }
        properties.combatPulse.attack(target)
        return true
    }

    override fun visualizeSpecialMove() {
        owner.visualize(Animation.create(7660), Graphics.create(1316))
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SPIRIT_GRAAHK_7363, NPCs.SPIRIT_GRAAHK_7364)
    }

    override fun getText(): String {
        return if (RandomFunction.random(2) == 0) "Howl!" else "Rowr!"
    }
}
