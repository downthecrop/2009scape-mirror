package rs09.game.content.quest.members.chompybird

import api.*

import org.rs09.consts.NPCs
import org.rs09.consts.Items
import org.rs09.consts.Graphics

import rs09.game.interaction.InteractionListener
import rs09.game.interaction.IntType
import rs09.game.system.command.Privilege
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

    if (!chompySpawned && RandomFunction.random(20) == 5) { //Arbitrary random number matching (ryan: updated this from 1/5 to 1/20 because 1/5 was debug stuff.) 
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


class BloatedToadListeners : InteractionListener, StartupListener, Commands {
  lateinit var borders: ZoneBorders
  val extraBorders = ArrayList<ZoneBorders>()

  override fun startup() {
    borders = ZoneBorders(2368, 2944, 2687, 3071)
    borders.addException(ZoneBorders.forRegion(10287))
    borders.addException(ZoneBorders.forRegion(10031))
    borders.addException(ZoneBorders.forRegion(9775))
  }

  override fun defineCommands() {
    define("toadzone", Privilege.MODERATOR, "", "Toad inflation.") {player, _ -> 
      val swloc = player.location.transform(-15,-15,0)
      val neloc = player.location.transform(15,15,0)
      val newBorders = ZoneBorders(swloc.x, swloc.y, neloc.x, neloc.y) 
      extraBorders.add(newBorders)
      for (i in 0 until 10) {
        val npc = NPC(NPCs.SWAMP_TOAD_1013, newBorders.randomLoc)
        npc.isRespawn = true
        npc.isWalks = true
        npc.isNeverWalks = false
        npc.init()
      }
    }

    define("toadbomb", Privilege.ADMIN, "", "Toad detoadation.") {player, _ -> 
      val swloc = player.location.transform(-5,-5,0)
      val neloc = player.location.transform(5,5,0)
      for (x in swloc.x until neloc.x) {
        for (y in swloc.y until neloc.y) {
          val npc = BloatedToadNPC().construct(NPCs.BLOATED_TOAD_1014, Location.create(x, y, swloc.z))
          npc.isNeverWalks = true
          npc.isWalks = false
          (npc as BloatedToadNPC).ticksToLive = 5
          npc.init()
        }
      }
    }
  } 

  override fun defineListeners() {
    on(Items.BLOATED_TOAD_2875, IntType.ITEM, "drop") {player, used ->
      val quest = player.questRepository.getQuest("Big Chompy Bird Hunting")
      val inExtraBorder = extraBorders.filter { it.insideBorder(player) }.count() > 0

      if (!borders.insideBorder(player) && !inExtraBorder) {
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

    on(Items.BLOATED_TOAD_2875, IntType.ITEM, "release") { player, used ->
      removeItem(player, used.asItem())
      sendPlayerDialogue(player, "Free the fat toadsies!")
      return@on true
    }

    on(Items.BLOATED_TOAD_2875, IntType.ITEM, "release all") { player, used ->
      removeAll(player, used.asItem())
      sendPlayerDialogue(player, "Free the fat toadsies!")
      return@on true
    }
  }
}
