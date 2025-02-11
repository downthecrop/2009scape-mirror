package content.global.ame.events.quizmaster

import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction
import org.rs09.consts.NPCs

class QuizMasterBorders : MapArea {
    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(getRegionBorders(7754))
    }

    override fun getRestrictions(): Array<ZoneRestriction> {
        return arrayOf(ZoneRestriction.RANDOM_EVENTS, ZoneRestriction.CANNON, ZoneRestriction.FOLLOWERS, ZoneRestriction.TELEPORT, ZoneRestriction.OFF_MAP)
    }

    override fun areaEnter(entity: Entity) {
        if (entity is Player) {
            entity.interfaceManager.removeTabs(0, 1, 2, 3, 4, 5, 6, 12)
            face(entity, Location(1952, 4768, 1))
            animate(entity,2378)
            openDialogue(entity, QuizMasterDialogueFile(), NPC(NPCs.QUIZ_MASTER_2477))
        }
    }

    override fun areaLeave(entity: Entity, logout: Boolean) {
        if (entity is Player) {
            entity.interfaceManager.restoreTabs()
            //closeOverlay(entity)
        }
    }

}