package rs09.game.content.quest.members.chompybird

import api.*

import org.rs09.consts.NPCs
import org.rs09.consts.Items
import org.rs09.consts.Animations

import core.plugin.Initializable
import core.tools.RandomFunction
import core.game.system.task.Pulse

import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle

import core.game.world.map.Location
import core.game.world.map.path.Pathfinder
import core.game.interaction.MovementPulse
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.IntType
import core.game.world.update.flag.context.Animation

@Initializable
class ChompyBirdNPC : AbstractNPC, InteractionListener {
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

  override fun attack(node: Node) {}

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

  override fun tick() {
    if (this.inCombat() && this.skills.lifepoints > 0) {
      val newLoc = this.location.transform(
        RandomFunction.random(-2,2),
        RandomFunction.random(-2,2),
        0
      )
      submitIndividualPulse(this, object : MovementPulse(this, newLoc, Pathfinder.DUMB) {
        override fun pulse() : Boolean { return true }
      })
    }
    super.tick()
  }

  override fun handleTickActions() {
    if (id == NPCs.CHOMPY_BIRD_1550) {
      if (getAttribute("toad_eaten", false)) {
        super.handleTickActions()
      }
      if (timeToLive-- <= 0) {
        animate(Animation(Animations.CHOMPY_FLY_AWAY_6767))
        runTask(this, 2) { clear() }
        timeToLive = 100
      }
    } else {
      if (timeToLive-- <= 0)
        clear()
    }
  }

  override fun finalizeDeath(killer: Entity) {
    properties.teleportLocation = null 
    val newSelf = transform(NPCs.CHOMPY_BIRD_1016)
    newSelf.isNeverWalks = true
    newSelf.isWalks = false
    newSelf.walkRadius = 0
    timeToLive = 200
    
    this.pulseManager.clear()
    this.walkingQueue.reset()

    if (killer is Player) {
      sendMessage(killer, "You scratch a notch on your bow for the chompy bird kill.")
      val old = killer.getAttribute("chompy-kills", 0)
      killer.setAttribute("/save:chompy-kills", old + 1)
      ChompyHat.checkForNewRank(killer)
    }
  }
  
  override fun defineListeners() {
    on(NPCs.CHOMPY_BIRD_1016, IntType.NPC, "pluck") { player, node -> 
      val bird = node.asNpc() 

      if (!bird.getAttribute("plucked", false)) {
        addItem(player, Items.FEATHER_314, RandomFunction.random(25, 32))
        produceGroundItem(player, Items.BONES_526, 1, bird.location)
        produceGroundItem(player, Items.RAW_CHOMPY_2876, 1, bird.location)
        bird.clear()
      }

      bird.setAttribute("plucked", true)

      return@on true
    }

    on(Items.OGRE_BOW_2883, IntType.ITEM, "check kills") { player, _ -> 
      val amount = player.getAttribute("chompy-kills", 0)
      sendDialogue(player, "You have killed $amount chompy birds.")
      return@on true
    }
  }
}
