package rs09.game.content.quest.members.chompybird

import api.*

import org.rs09.consts.NPCs

import core.plugin.Initializable
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location

@Initializable
class RantzNPC : AbstractNPC {
  constructor() : super(NPCs.RANTZ_1010, null, true)
  private constructor(id: Int, location: Location) : super(id, location)

  override fun construct(id: Int, location: Location, vararg objects: Any?) : AbstractNPC {
    return RantzNPC(id, location)
  }

  override fun getIds() : IntArray {
    return intArrayOf(NPCs.RANTZ_1010)
  }

  override fun handleTickActions() { 
    super.handleTickActions()
    val chompy = findLocalNPC(this, NPCs.CHOMPY_BIRD_1550) as? ChompyBirdNPC ?: return

    val owner = getAttribute<Player?>(chompy, "owner", null) ?: return
    val quest = owner.questRepository.getQuest("Big Chompy Bird Hunting")

    if (quest.getStage(owner) !in 40..50 || chompy.getAttribute("attacked", false)) return
    
    sendChat("Hey, dere's da chompy, I's gonna shoot it.")
    sendMessage(owner, "Rantz: Hey, dere's da chompy, I's gonna shoot it.")
    sendMessage(owner, "Rantz keeps missing the chompy bird.")
    sendMessage(owner, "Rantz: Grrr...de'ese arrows are rubbish.")

    attack(chompy)
    chompy.setAttribute("attacked", true)
    quest.setStage(owner, 50)
  }
}
