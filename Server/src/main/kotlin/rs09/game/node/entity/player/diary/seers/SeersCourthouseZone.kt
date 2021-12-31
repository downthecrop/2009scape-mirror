package rs09.game.node.entity.player.diary.seers

import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.player.link.prayer.PrayerType
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneBuilder
import core.plugin.Initializable
import core.plugin.Plugin

@Initializable
class SeersCourthouseZone : MapZone("seers-courthouse", true), Plugin<Any?>{

    override fun newInstance(arg: Any?): SeersCourthouseZone {
        ZoneBuilder.configure(this)
        return this
    }

    override fun configure() {
        super.register(ZoneBorders(2735,3471,2736,3471))
    }

    override fun enter(e: Entity?): Boolean {
        if(e is Player && !e.isArtificial){
            if(!e.achievementDiaryManager.hasCompletedTask(DiaryType.SEERS_VILLAGE,2,3) && e.prayer.active.contains(PrayerType.PIETY)){
                e.achievementDiaryManager.finishTask(e,DiaryType.SEERS_VILLAGE,2,3)
            }
        }
        return super.enter(e)
    }

    override fun fireEvent(identifier: String?, vararg args: Any?): Any {
        return Unit
    }

    override fun leave(e: Entity?, logout: Boolean): Boolean {
        return super.leave(e, logout)
    }
}