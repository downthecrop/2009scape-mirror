package content.region.morytania.quest.hauntedmine

import content.data.Quests
import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.combat.*
import core.game.node.entity.combat.equipment.SwitchAttack
import core.game.node.entity.impl.Animator.Priority
import core.game.node.entity.impl.Projectile
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.path.ClipMaskSupplier
import core.game.world.repository.Repository.getPlayerByName
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.tools.RandomFunction
import org.rs09.consts.NPCs

/**
 * Treus Dayth, the boss of the Haunted Mine quest.
 */

// dayth minimizes himself and zooms around through walls
// dayth attacks with melee, or throws pickaxes at you (ranged) when out of melee distance.
// the pickaxes actually look like they spawn near-ish the player and not as a projectile from attacker to target.

// https://youtu.be/CD77NeKz1J4?si=LGsfJJpAV0oAk3se&t=498 reference

class TreusDaythNPCBehavior : NPCBehavior(NPCs.TREUS_DAYTH_1540) {

    private val DAYTH_MELEE = 5540
    private val DAYTH_RANGE = 5545
    private val DAYTH_PROJ = 309
    private val DAYTH_PROJ_GFX = 308
    private val attrFlee = "fleeing"

    override fun tick(self: NPC): Boolean {

        // get target (attribute set when dayth spawns)
        val targetName = getAttribute<String?>(self, HauntedMine.ATTR_HAUNTED_TARGET, null)
        val target = getPlayerByName(targetName)

        // poofclear if target runs too far or leaves
        if (target == null
            || !target.isActive
            || !self.location.withinDistance(target.location, 32)
        ) {
            self.walkingQueue.reset()
            self.properties.combatPulse.stop()
            poofClear(self)
            if (target != null) removeAttribute(target, HauntedMine.ATTR_DAYTH_FIGHT)
        }

        // keep attacking target
        if (!getAttribute(self, attrFlee, false)) {
            self.attack(target)
        }

        return super.tick(self)
    }

    override fun afterDamageReceived(self: NPC, attacker: Entity, state: BattleState) {
        // after player hits, roll a 1/3 chance to see if you flee (it not already fleeing)
        if (!getAttribute(self, attrFlee, false) && RandomFunction.roll(3)) {
            runAwayGhost(self)
        }
        super.afterDamageReceived(self, attacker, state)
    }

    override fun canBeAttackedBy(self: NPC, attacker: Entity, style: CombatStyle, shouldSendMessage: Boolean): Boolean {
        if (attacker is Player) {

            // get target
            val targetName = getAttribute<String?>(self, HauntedMine.ATTR_HAUNTED_TARGET, null)
            val target = getPlayerByName(targetName)

            if (attacker == target) {
                return true
            }
            sendMessage(attacker, "It's not after you...")
        }
        return false
    }

    // clear the minecarts when he dies. They otherwise have a 1000-tick clear like Dayth.
    override fun onDeathStarted(self: NPC, killer: Entity) {
        val carts = findLocalNPCs(killer, intArrayOf(NPCs.MINE_CART_1544), 32)
        carts.forEach { cart ->
            cart.clear()
        }
        super.onDeathStarted(self, killer)
    }

    // advance the quest
    override fun onDeathFinished(self: NPC, killer: Entity) {
        if (killer is Player) {
            if (getQuestStage(killer, Quests.HAUNTED_MINE) <= 6) {
                setQuestStage(killer, Quests.HAUNTED_MINE, 7)
                sendDialogue(killer, "As you make the killing strike, the machinery within the room falls silent.")
                removeAttribute(killer, HauntedMine.ATTR_DAYTH_FIGHT)
            }
        }
        self.clear()
        super.onDeathFinished(self, killer)
    }

    // preset locations dayth can move between.
    private val fleeLocations = arrayOf(
        Location.create(2782, 4461, 0),
        Location.create(2787, 4462, 0),
        Location.create(2795, 4461, 0),
        Location.create(2793, 4457, 0),
        Location.create(2790, 4454, 0),
        Location.create(2791, 4450, 0),
        Location.create(2793, 4452, 0),
        Location.create(2793, 4447, 0),
        Location.create(2790, 4444, 0),
        Location.create(2787, 4445, 0),
        Location.create(2786, 4447, 0),
        Location.create(2784, 4447, 0),
        Location.create(2783, 4449, 0),
        Location.create(2783, 4453, 0),
        Location.create(2785, 4452, 0),
        Location.create(2787, 4449, 0),
        Location.create(2787, 4454, 0),
        Location.create(2785, 4456, 0),
        Location.create(2780, 4455, 0),
        Location.create(2796, 4459, 0),
        Location.create(2782, 4457, 0)
    )

    // pick a random flee tile that is not the current one
    private fun runAwayGhost(self: NPC) {
        val target = fleeLocations
            .filter { it != self.location }
            .random()

        // teleport away instead of "running"
        teleport(self, target)

        // todo: authentically, Dayth should run away and not teleport. Either forceMove needs to support NPCs, or NPCs need to support running.
        // this movement works, but forcewalk moves too slowly
        /*queueScript(self, 0, QueueStrength.STRONG) { count ->
            // move
            if (count == 0) {
                setAttribute(self, attrFlee, true)
                self.properties.combatPulse.stop()
                //self.walkingQueue.isRunning = true
                forceWalk(self, target, "smart")
                return@queueScript delayScript(self, 1)
            }
            // keep waiting until move is complete
            if (!finishedMoving(self)) {
                return@queueScript delayScript(self, 1)
            }
            // set no longer fleeing
            setAttribute(self, attrFlee, false)
            return@queueScript stopExecuting(self)
        }*/
    }

    // switches between melee or spawning magic picks to hit the player with.
    // copying Ovenbread's comment I saw from another file because I have it framed at home:
    //      "All these combat shit is the most trash level thing to use or decipher." - Ovenbread, 2025
    private val combatHandler = object : MultiSwingHandler(
        true,
        SwitchAttack(
            CombatStyle.MELEE.swingHandler,
            Animation(DAYTH_MELEE, Priority.HIGH)
        ),
        SwitchAttack(
            CombatStyle.RANGE.swingHandler,
            Animation(DAYTH_RANGE, Priority.HIGH),
            null,
            null,
            Projectile.create(
                null as Entity?,
                null,
                DAYTH_PROJ,
                15,
                15,
                10,
                50,
                14,
                255
            )
        )
    ) {
        // make dayth attack from anywhere and fire through walls
        override fun canSwing(entity: Entity, victim: Entity): InteractionType {
            // if fleeing, no attack
            if (getAttribute(entity, attrFlee, false)) {
                return InteractionType.NO_INTERACT
            }

            // keep attacking, even through walls or at a distance
            return InteractionType.STILL_INTERACT
        }

        // override the projectile so it starts offset from the player
        // these are magically summoned pickaxes to hit the player
        override fun visualize(entity: Entity, victim: Entity?, state: BattleState?) {
            if (current.startGraphic != null) {
                visualize(entity, current.animation, current.startGraphic)
            } else {
                animate(entity, current.animation)
            }
            if (victim == null) return

            val offsets = listOf(-2, -1, 1, 2)
            val origin = victim.location.transform(offsets.random(), offsets.random(), 0)
            val speed = (46 + origin.getDistance(victim.location) * 5).toInt()

            val proj = Projectile.create(null as Entity?, victim, DAYTH_PROJ, 15, 15, 10, speed, 14, 0)
            sendGraphics(Graphics(DAYTH_PROJ_GFX, 100), origin)
            proj.sourceLocation = origin
            proj.send()
        }
    }

    override fun getSwingHandlerOverride(self: NPC, original: CombatSwingHandler): CombatSwingHandler {
        return combatHandler
    }

    // this lets him walk through walls like a ghost.
    override fun getClippingSupplier(self: NPC): ClipMaskSupplier {
        return if (getAttribute(self, attrFlee, false)) {
            GhostClipper
        } else {
            StandardClipMaskSupplier
        }
    }
}