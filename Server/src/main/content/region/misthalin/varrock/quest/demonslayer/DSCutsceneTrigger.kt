package content.region.misthalin.varrock.quest.demonslayer

import core.api.*
import core.game.world.map.zone.ZoneBorders
import core.game.activity.ActivityManager
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import org.rs09.consts.Items
import content.data.Quests

class DSCutsceneTrigger : MapArea {
  
  override fun defineAreaBorders() : Array<ZoneBorders> {
    return arrayOf(ZoneBorders(3222, 3364, 3234, 3375))
  }

  override fun areaEnter(entity: Entity) {
    if (entity !is Player) return

    val quest = entity.questRepository.getQuest(Quests.DEMON_SLAYER)
    val alreadyInCutscene = getAttribute(entity, "demon-slayer:cutscene", false)
    val hasSilverlight = inInventory(entity, Items.SILVERLIGHT_2402) || inEquipment(entity, Items.SILVERLIGHT_2402)

    if (quest.getStage(entity) == 30 && !alreadyInCutscene && hasSilverlight) {
      ActivityManager.start(entity, "Demon Slayer Cutscene", false)
      setAttribute(entity, "demon-slayer:cutscene", true)
    }
  }
}
