package content.global.skill.slayer

import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.CombatSwingHandler
import core.game.node.entity.combat.MultiSwingHandler
import core.game.node.entity.combat.equipment.SwitchAttack
import core.game.node.entity.impl.Animator.Priority
import core.game.node.entity.impl.Projectile
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.world.update.flag.context.Animation

class WaterfiendBehavior : NPCBehavior(*Tasks.WATERFIENDS.ids) {
    private val combatHandler = MultiSwingHandler(
            true,
            SwitchAttack(
                    CombatStyle.MAGIC.swingHandler,
                    Animation(1581, Priority.HIGH),
                    null,
                    null,
                    Projectile.create(
                            null as Entity?,
                            null,
                            500,
                            15,
                            30,
                            50,
                            50,
                            14,
                            255
                    )
            ),
            SwitchAttack(
                    CombatStyle.RANGE.swingHandler,
                    Animation(1581, Priority.HIGH),
                    null,
                    null,
                    Projectile.create(
                            null as Entity?,
                            null,
                            16,
                            15,
                            30,
                            50,
                            50,
                            14,
                            255
                    )
            )
    )
    override fun getSwingHandlerOverride(self: NPC, original: CombatSwingHandler): CombatSwingHandler {
        return combatHandler
    }
}