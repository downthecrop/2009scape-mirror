package rs09.game.content.quest.members.chompybird

import api.*

import org.rs09.consts.NPCs
import org.rs09.consts.Items
import org.rs09.consts.Graphics

import rs09.game.interaction.InteractionListener
import core.plugin.Initializable
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.RegionManager
import core.game.world.map.Location
import core.tools.RandomFunction

@Initializable
class BloatedToadNPC : AbstractNPC {
  constructor() : super(NPCs.BLOATED_TOAD_1014, null, true) {}
  private constructor(id: Int, location: Location) : super(id, location) {}
  
  var ticksToLive = 100
  var chompySpawned = false

  override fun construct(id: Int, location: Location, vararg objects: Any?): AbstractNPC {
    val npc = BloatedToadNPC(id, location)
    npc.isWalks = false
    npc.isNeverWalks = true
    npc.isRespawn = false
    return npc
  }

  override fun getIds() : IntArray { 
    return intArrayOf(NPCs.BLOATED_TOAD_1014)
  }

  override fun handleTickActions() {
    super.handleTickActions()
    
    if (ticksToLive-- <= 0) {
      val toDamage = RegionManager.getLocalEntitys(this.location, 2)

      for (entity in toDamage) {
        if (entity == this) continue
        impact(entity, RandomFunction.random(1, 3))
      }

      sendGraphics(Graphics.TOAD_DETONATION_240, this.location)

      clear()
    }

    if (!chompySpawned && RandomFunction.random(5) == 3) { 
      val chompy = NPC.create(NPCs.CHOMPY_BIRD_1550, this.location)
      val spawn = getPathableRandomLocalCoordinate(chompy, 4, this.location, 3)
      if (spawn == null || spawn == this.location) return

      val owner = getAttribute<Player?>("owner", null) ?: return

      chompy.isRespawn = false
      chompy.location = spawn
      chompy.setAttribute("owner", owner)
      chompy.setAttribute("toad", this)
      chompy.walkingQueue.update() 

      chompy.init()
      sendChat(chompy, "Squawk!")
      clearHintIcon(owner)
      registerHintIcon(owner, chompy)
      chompySpawned = true
      ticksToLive = 100
    }
  }

}


class BloatedToadListeners : InteractionListener, StartupListener {
  lateinit var borders: ZoneBorders

  override fun startup() {
    borders = ZoneBorders(2368, 2944, 2687, 3071)
    borders.addException(ZoneBorders.forRegion(10287))
    borders.addException(ZoneBorders.forRegion(10031))
    borders.addException(ZoneBorders.forRegion(9775))
  }

  override fun defineListeners() {
    on(Items.BLOATED_TOAD_2875, ITEM, "drop") {player, used ->
      val quest = player.questRepository.getQuest("Big Chompy Bird Hunting")

      if (!borders.insideBorder(player)) {
        sendPlayerDialogue(player, "I probably wouldn't catch many chompies here.")
        return@on true
      }

      if (quest.getStage(player) < 30) {
        sendPlayerDialogue(player, "I don't know what you want from me.")
        return@on true
      }

      if (quest.getStage(player) in 30..40) {
        if (player.location == Location.create(2635, 2966, 0) || player.location == Location.create(2636, 2966, 0)){
          quest.setStage(player, 40)
        } else {
          sendPlayerDialogue(player, "I should probably put this where Rantz told me.")
          return@on true
        }
      }

      if (removeItem(player, used.asItem())) {
        val toad = core.game.node.entity.npc.NPC.create(NPCs.BLOATED_TOAD_1014, player.location)
        setAttribute(toad, "owner", player)
        toad.init()
      }
      return@on true
    }
  }
}
