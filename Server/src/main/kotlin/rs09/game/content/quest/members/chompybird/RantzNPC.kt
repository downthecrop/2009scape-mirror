package rs09.game.content.quest.members.chompybird

import api.*

import org.rs09.consts.NPCs

import core.plugin.Initializable
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders

@Initializable
class RantzNPC : AbstractNPC, MapArea {
  constructor() : super(NPCs.RANTZ_1010, null, true)
  private constructor(id: Int, location: Location) : super(id, location)

  override fun construct(id: Int, location: Location, vararg objects: Any?) : AbstractNPC {
    return RantzNPC(id, location)
  }

  override fun defineAreaBorders() : Array<ZoneBorders> {
    return arrayOf(ZoneBorders(2629, 2962, 2638, 2973))
  }

  override fun areaEnter(entity: Entity) {
    if (entity !is NPC) return
    if (entity.id != NPCs.CHOMPY_BIRD_1550) return

    val owner = getAttribute<Player?>(entity, "owner", null) ?: return
    val quest = owner.questRepository.getQuest("Big Chompy Bird Hunting")

    if (quest.getStage(owner) !in 40..50 || entity.getAttribute("attacked", false)) return
    
    sendChat("Hey, dere's da chompy, I's gonna shoot it.")
    sendMessage(owner, "Rantz: Hey, dere's da chompy, I's gonna shoot it.")
    sendMessage(owner, "Rantz keeps missing the chompy bird.")
    sendMessage(owner, "Rantz: Grrr...de'ese arrows are rubbish.")

    attack(entity)
    entity.setAttribute("attacked", true)
    quest.setStage(owner, 50)
  }

  override fun getIds() : IntArray {
    return intArrayOf(NPCs.RANTZ_1010)
  }
}
