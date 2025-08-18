package content.region.desert.quest.deserttreasure

import content.region.kandarin.quest.templeofikov.TempleOfIkov
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.interaction.QueueStrength
import core.game.node.entity.Entity
import core.game.node.entity.combat.*
import core.game.node.entity.combat.equipment.SwitchAttack
import core.game.node.entity.impl.Projectile
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.prayer.PrayerType
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.tools.END_DIALOGUE
import org.rs09.consts.NPCs

class DessousMeleeBehavior : NPCBehavior(NPCs.DESSOUS_1914, NPCs.DESSOUS_1915) {
    private var disappearing = false;

    override fun canBeAttackedBy(self: NPC, attacker: Entity, style: CombatStyle, shouldSendMessage: Boolean): Boolean {
        if (attacker is Player) {
            if (attacker == getAttribute<Player?>(self, "target", null)) {
                return true
            }
            sendMessage(attacker, "It's not after you...")
        }
        return false
    }

    override fun tick(self: NPC): Boolean{
        if (disappearing) {
            return true
        }
        val player: Player? = getAttribute<Player?>(self, "target", null)
        if (player == null || !self.location.withinDistance(self.properties.spawnLocation, self.walkRadius)) {
            if (player != null && !disappearing) {
                disappearing = true
                sendMessage(player, "Dessous returns to his grave, bored of toying with you.")
                removeAttribute(player, DesertTreasure.attributeDessousInstance)
            }
            poofClear(self)
        }

        // Dessous just continually hisses independently of projectile fires.
        if (self.id == NPCs.DESSOUS_1915 && self.properties.combatPulse.isInCombat) {
            animate(self, Animation(1914))
        }
        // This is probably the prayer flicking nonsense.
        if (self.id == NPCs.DESSOUS_1914 && player != null && player.prayer.get(PrayerType.PROTECT_FROM_MELEE)) {
            self.transform(NPCs.DESSOUS_1915)
            Graphics.send(Graphics(86), self.location)
        } else if (self.id == NPCs.DESSOUS_1915 && player != null && (player.prayer.get(PrayerType.PROTECT_FROM_MAGIC) || player.prayer.get(PrayerType.PROTECT_FROM_MISSILES))) {
            self.transform(NPCs.DESSOUS_1914)
            Graphics.send(Graphics(86), self.location)
        }
        return true
    }

    override fun getSwingHandlerOverride(self: NPC, original: CombatSwingHandler): CombatSwingHandler {
        if (self.id == NPCs.DESSOUS_1915) {
            // 2 x 5HP (One Magic, One Ranged)
            return CombatHandler()
        } else {
            // Fast melee attack 3 ticks up to 19HP
            return original
        }
    }

    override fun beforeAttackFinalized(self: NPC, victim: Entity, state: BattleState) {
        // Teleport nearer, if too far.
        if (victim is Player) {
            if (victim.location.getDistance(self.location) >= 5) {
                Graphics.send(Graphics(86), self.location)
                self.properties.teleportLocation = victim.location
                Graphics.send(Graphics(86), self.location)
            }
        }
    }

    override fun onDeathFinished(self: NPC, killer: Entity) {
        if (killer is Player) {
            val player = killer
            if (DesertTreasure.getSubStage(player, DesertTreasure.attributeBloodStage) == 2) {
                DesertTreasure.setSubStage(player, DesertTreasure.attributeBloodStage, 3)
            }
            removeAttribute(player, DesertTreasure.attributeDessousInstance)
            openDialogue(player, object : DialogueFile() {
                override fun handle(componentID: Int, buttonID: Int) {
                    when (stage) {
                        0 -> player(FacialExpression.ANGRY, "Well that's Dessous dead, but where is the Diamond he", "was supposed to have?").also { stage++ }
                        1 -> playerl(FacialExpression.ANGRY, "If Malak lied to me about it, he is going to pay!").also {
                            stage = END_DIALOGUE
                        }
                    }
                }
            })
        }
    }

    /** Handler for ranged. */
    class CombatHandler : MultiSwingHandler(
            SwitchAttack(CombatStyle.MAGIC.swingHandler, null),
            SwitchAttack(CombatStyle.RANGE.swingHandler, null)
    ) {
        override fun swing(entity: Entity?, victim: Entity?, state: BattleState?): Int {
            if (entity is NPC && victim is Player) {
                val projectile = Projectile.create(
                        victim.location.transform(Location(intArrayOf(3, -3).random(),intArrayOf(3, -3).random())),
                        victim.location,
                        350,
                        0,
                        0,
                        0,
                        60,
                        0,
                        255
                )
                // 2 x 5HP (One Magic, One Ranged)
                state!!.estimatedHit = 5
                state.secondaryHit = 5 // I have no idea what I'm doing
                queueScript(entity, 0, QueueStrength.STRONG) { stage: Int ->
                    when (stage) {
                        0 -> {
                            sendChat(entity, "Hssssssssssss")
                            projectile.send()
                            return@queueScript delayScript(entity, entity.location.getDistance(victim.location).toInt())
                        }
                        1 -> {
                            return@queueScript stopExecuting(entity)
                        }
                        else -> return@queueScript stopExecuting(entity)
                    }
                }

            }
            return super.swing(entity, victim, state)
        }
    }
}