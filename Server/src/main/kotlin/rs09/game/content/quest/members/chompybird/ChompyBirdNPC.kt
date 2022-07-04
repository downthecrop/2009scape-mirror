package rs09.game.content.quest.members.chompybird

import api.*

import org.rs09.consts.NPCs
import org.rs09.consts.Items
import org.rs09.consts.Animations

import core.plugin.Initializable
import core.game.system.task.Pulse
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.world.update.flag.context.Animation
import core.game.world.map.Location
import core.game.world.map.path.Pathfinder
import core.game.interaction.MovementPulse

@Initializable
class ChompyBirdNPC : AbstractNPC {
  constructor() : super(NPCs.CHOMPY_BIRD_1550, null, true)
  private constructor(id: Int, location: Location) : super(id, location) {}

  var timeToLive = 100

  override fun init() {
    super.init()
    val toad = getAttribute<NPC?>("toad", null)
    animate(Animation(Animations.CHOMPY_SPAWN_6766))

    if (toad != null) {
      submitIndividualPulse(this, object : MovementPulse(this, toad, Pathfinder.SMART) {
        var bufferTicks = 7
        override fun pulse() : Boolean {
          if (bufferTicks-- == 0) {
            setAttribute("toad_eaten", true)
            toad.clear()
            return true
          }
          return false
        }
      })
    }
  }

  override fun construct(id: Int, location: Location, vararg objects: Any?): AbstractNPC {
    val npc = ChompyBirdNPC(id, location)
    npc.isRespawn = false
    return npc
  }

  override fun getIds() : IntArray { 
    return intArrayOf(NPCs.CHOMPY_BIRD_1550)
  }

  override fun isAttackable(entity: Entity, style: CombatStyle, message: Boolean) : Boolean { 
    if (entity !is Player) return true

    var attackable = super.isAttackable(entity, style, message) 
    val hasOgreBow = inEquipment(entity, Items.OGRE_BOW_2883) 

    if (!hasOgreBow) {
      sendMessage(entity, "You need an ogre bow to hunt chompy birds.")
    }

    return attackable && hasOgreBow
  }

  override fun canAttack(victim: Entity) : Boolean {
    return false
  }

  override fun checkImpact(state: BattleState) {
    super.checkImpact(state)
    if (getAttribute("toad_eaten", false) || state.attacker.id == NPCs.RANTZ_1010) {
      impactHandler.disabledTicks = 10
      locks.lockInteractions(10)
      sendChat("Squawk!")
      submitWorldPulse(object : Pulse(2) {
        var counter = 0
        override fun pulse() : Boolean {
          when (counter++) {
            1 -> animate(Animation(Animations.CHOMPY_FLY_AWAY_6767))
            2 -> clear()
            3 -> return true
          }
          return false
        }
      })
    } else {
      sendChat("Screech!")
    }
  }

  override fun handleTickActions() {
    if (id == NPCs.CHOMPY_BIRD_1550) {
      if (getAttribute("toad_eaten", false)) {
        super.handleTickActions()
      }
      if (timeToLive-- <= 0 && !this.inCombat()) {
        animate(Animation(Animations.CHOMPY_FLY_AWAY_6767))
        runTask(this, 2) { clear() }
        timeToLive = 100
      }
    } 
  }

  override fun finalizeDeath(killer: Entity) {
    properties.teleportLocation = null
    runTask(this, 2) { 
      val newSelf = transform(NPCs.CHOMPY_BIRD_1016)
      newSelf.isNeverWalks = true
      newSelf.isWalks = false
      newSelf.walkRadius = 0
    }
  }

}
