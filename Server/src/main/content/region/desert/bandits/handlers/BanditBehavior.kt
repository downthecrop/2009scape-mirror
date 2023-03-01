package content.region.desert.bandits.handlers

import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.world.map.RegionManager
import core.tools.RandomFunction
import org.rs09.consts.NPCs

class BanditBehavior : NPCBehavior(NPCs.BANDIT_1926) {
    override fun tick(self: NPC): Boolean {
        if (!self.inCombat() && RandomFunction.roll(3) && getWorldTicks() % 5 == 0) {
            val players = RegionManager.getLocalPlayers(self, 5)
            for (player in players) {
                if (player.inCombat()) continue
                if (hasGodItem(player, God.SARADOMIN)) {
                    sendChat(self, "Prepare to die, Saradominist scum!")
                    self.attack(player)
                    break
                }
                else if (hasGodItem(player, God.ZAMORAK)) {
                    sendChat(self, "Prepare to die, Zamorakian scum!")
                    self.attack(player)
                    break
                }
            }
        }
        return true
    }

    override fun afterDamageReceived(self: NPC, attacker: Entity, state: BattleState) {
        if (getAttribute(self, "alerted-others", false)) return
        val otherBandits = RegionManager.getLocalNpcs(self, 3).filter { it.id == self.id }
        for (bandit in otherBandits) {
            if (!bandit.inCombat())
                bandit.attack(attacker)
        }
        setAttribute(self, "alerted-others", true)
    }

    override fun onDeathStarted(self: NPC, killer: Entity) {
        removeAttribute(self, "alerted-others")
    }
}