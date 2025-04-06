package content.region.kandarin.feldip.quest.zogreflesheaters

import content.global.handlers.item.equipment.special.DragonfireSwingHandler
import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.CombatSwingHandler
import core.game.node.entity.combat.MultiSwingHandler
import core.game.node.entity.combat.equipment.SwitchAttack
import core.game.node.entity.combat.spell.SpellType
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.timer.impl.Disease
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.tools.RandomFunction
import org.rs09.consts.Items
import org.rs09.consts.NPCs

class SlashBashBehavior : NPCBehavior(NPCs.SLASH_BASH_2060) {
    override fun canBeAttackedBy(self: NPC, attacker: Entity, style: CombatStyle, shouldSendMessage: Boolean): Boolean {
        if (attacker is Player) {
            if (attacker == getAttribute<Player?>(self, "target", null)) {
                return true
            }
            sendMessage(attacker, "It's not after you...")
        }
        return false
    }

    override fun beforeAttackFinalized(self: NPC, victim: Entity, state: BattleState) {
        if (victim is Player) {
            val disease = getOrStartTimer<Disease>(victim, 25)
            disease.hitsLeft = 25
        }
    }

    override fun beforeDamageReceived(self: NPC, attacker: Entity, state: BattleState) {
        if (attacker is Player) {
            if (inEquipment(attacker, Items.COMP_OGRE_BOW_4827)) {
                return
            }
            if (state.spell != null && state.spell.type == SpellType.CRUMBLE_UNDEAD) {
                state.estimatedHit = (state.estimatedHit * 0.5).toInt()
                if (state.secondaryHit > 0) {
                    state.secondaryHit = (state.secondaryHit * 0.5).toInt()
                }
                return
            }
            state.estimatedHit = (state.estimatedHit * 0.25).toInt()
            if (state.secondaryHit > 0) {
                state.secondaryHit = (state.secondaryHit * 0.25).toInt()
            }
        }
    }

    override fun onDropTableRolled(self: NPC, killer: Entity, drops: ArrayList<Item>) {
        super.onDropTableRolled(self, killer, drops)
        if (killer is Player && getQuestStage(killer, ZogreFleshEaters.questName) == 8) {
            drops.add(Item(Items.ZOGRE_BONES_4812, 2))
            drops.add(Item(Items.OURG_BONES_4834, 3))
            drops.add(Item(Items.OGRE_ARTEFACT_4818))
            setQuestStage(killer, ZogreFleshEaters.questName, 9)
            removeAttribute(killer, ZogreFleshEaters.attributeSlashBashInstance)
        }
    }

    var clearTime = 0
    override fun tick(self: NPC): Boolean {
        val player: Player? = getAttribute<Player?>(self, "target", null)
        // You have 500 ticks to kill this guy
        if (clearTime++ > 500) {
            poofClear(self)
            clearTime = 0
            if (player != null) {
                removeAttribute(player, ZogreFleshEaters.attributeSlashBashInstance)
            }

        }
        return true
    }

    /** MELEE Swing */
    private val COMBAT_HANDLER = MultiSwingHandler(SwitchAttack(CombatStyle.MELEE.swingHandler, Animation(359)))
    /** RANGE Swing (Projectile) */
    private val COMBAT_HANDLER_FAR = MultiSwingHandler(SwitchAttack(CombatStyle.RANGE.swingHandler, Animation(359), Graphics(499)))

    override fun getSwingHandlerOverride(self: NPC, original: CombatSwingHandler): CombatSwingHandler {
        val victim = self.properties.combatPulse.getVictim() ?: return original
        if (victim !is Player) return original

        return if (victim.location.getDistance(self.location) >= 2)
            COMBAT_HANDLER_FAR
        else
            COMBAT_HANDLER
    }
}