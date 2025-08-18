package content.region.desert.quest.deserttreasure

import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.combat.*
import core.game.node.entity.combat.equipment.SwitchAttack
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.player.Player
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import org.rs09.consts.NPCs

// https://www.youtube.com/watch?v=xeu6Ncmt1fY

class KamilBehavior : NPCBehavior(NPCs.KAMIL_1913) {
    private var disappearing = false

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
        if (disappearing) {
            return true
        }
        val player: Player? = getAttribute<Player?>(self, "target", null)
        if (player == null || !self.location.withinDistance(self.properties.spawnLocation, (self.walkRadius*1.5).toInt())) {
            if (player != null && !disappearing) {
                disappearing = true
                sendMessage(player, "Kamil vanishes on an icy wind...")
                removeAttribute(player, DesertTreasure.attributeKamilInstance)
            }
            poofClear(self)
        }
        return true
    }

    override fun onDeathFinished(self: NPC, killer: Entity) {
        if (killer is Player) {
            if (DesertTreasure.getSubStage(killer, DesertTreasure.attributeIceStage) == 2) {
                DesertTreasure.setSubStage(killer, DesertTreasure.attributeIceStage, 3)
                removeAttribute(killer, DesertTreasure.attributeKamilInstance)
                sendPlayerDialogue(killer, "Well, that must have been the 'bad man' that the troll kid was on about... His parents must be up ahead somewhere.")
            }
        }
    }

    override fun getSwingHandlerOverride(self: NPC, original: CombatSwingHandler): CombatSwingHandler {
        return KamilCombatHandler()
    }
}

// All these combat shit is the most trash level thing to use or decipher.
class KamilCombatHandler: MultiSwingHandler(
        SwitchAttack(CombatStyle.MELEE.swingHandler, null),
) {
    override fun impact(entity: Entity?, victim: Entity?, state: BattleState?) {
        if (victim is Player) {
            // This is following RevenantCombatHandler.java, no idea if this is good.
            // I can't be bothered to fix fucking frozen. The player can hit through frozen. What the fuck is frozen for then, to glue his fucking legs???
            if (RandomFunction.roll(3) && !hasTimerActive(victim, "frozen") && !hasTimerActive(victim, "frozen:immunity")) {
                sendChat(entity as NPC, "Sallamakar Ro!") // Salad maker roll.
                impact(victim, 5)
                impact(victim, 5)
                registerTimer(victim, spawnTimer("frozen", 7, true))
                sendMessage(victim, "You've been frozen!")
                // FIXME: before the below vfx hits, there should be another one that looks kinda like a wind wave exploding at the player's feet. Hope somebody finds the id.
                sendGraphics(539, victim.location)
                victim.properties.combatPulse.stop() // Force the victim to stop fighting. Whatever.
                // FIXME: sfx
            }else {
                animate(entity!!, Animation(440))
            }
        }
        super.impact(entity, victim, state)
    }
}
