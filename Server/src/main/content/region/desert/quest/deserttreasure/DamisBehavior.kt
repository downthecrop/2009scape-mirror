package content.region.desert.quest.deserttreasure

import core.api.*
import core.game.interaction.QueueStrength
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.player.Player
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import org.rs09.consts.Items
import org.rs09.consts.NPCs

class DamisBehavior : NPCBehavior(NPCs.DAMIS_1974, NPCs.DAMIS_1975) {

    var clearTime = 0

    override fun canBeAttackedBy(self: NPC, attacker: Entity, style: CombatStyle, shouldSendMessage: Boolean): Boolean {
        if (attacker is Player) {
            if (attacker == getAttribute<Player?>(self, "target", null)) {
                return true
            }
            sendMessage(attacker, "It's not after you...")
        }
        return false
    }

    override fun tick(self: NPC): Boolean {
        val player: Player? = getAttribute<Player?>(self, "target", null)
        if (clearTime++ > 800) {
            clearTime = 0
            if (player != null) {
                sendMessage(player, "Damis has vanished once more into the shadows...")
                removeAttribute(player, DesertTreasure.attributeDamisInstance)
            }
            poofClear(self)
        }
        return true
    }

    override fun beforeDamageReceived(self: NPC, attacker: Entity, state: BattleState) {
        if (attacker is Player) {
            if (state.estimatedHit + Integer.max(state.secondaryHit, 0) >= self.skills.lifepoints && self.id == NPCs.DAMIS_1974) {
                state.estimatedHit = self.skills.lifepoints + 1
                state.secondaryHit = -1

                transformNpc(self, NPCs.DAMIS_1975, 500)
                self.skills.lifepoints = self.skills.maximumLifepoints
                sendChat(self, "Armour... is for restraint, not... protection...")
                queueScript(self, 2, QueueStrength.NORMAL) { stage: Int ->
                    sendChat(self, "Now I show... you... my true power!")
                    self.properties.attackSpeed = 3
                    return@queueScript stopExecuting(self)
                }
            }
        }
    }

    override fun beforeAttackFinalized(self: NPC, victim: Entity, state: BattleState) {
        // In second form, drain prayer by 5.
        if (self.id == NPCs.DAMIS_1975) {
            victim.skills.decrementPrayerPoints(5.0)
        }
    }



    override fun onDeathFinished(self: NPC, killer: Entity) {
        if (killer is Player) {
            if (self.id == NPCs.DAMIS_1975) {
                if (DesertTreasure.getSubStage(killer, DesertTreasure.attributeShadowStage) == 3) {
                    GroundItemManager.create(Item(Items.SHADOW_DIAMOND_4673), self.location, killer)
                    DesertTreasure.setSubStage(killer, DesertTreasure.attributeShadowStage, 100)
                    removeAttribute(killer, DesertTreasure.attributeFareedInstance)
                }
            }
        }
    }
}