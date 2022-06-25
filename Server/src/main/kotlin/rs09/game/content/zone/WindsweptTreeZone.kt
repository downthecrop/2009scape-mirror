package rs09.game.node.entity.player.diary.seers

import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.game.world.map.zone.MapZone
import core.game.node.entity.player.link.diary.DiaryType
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneBuilder
import core.plugin.Plugin

@Initializable
class WindsweptTreeZone : MapZone("windswept-tree", true), Plugin<Any?> {


    override fun newInstance(arg: Any?): WindsweptTreeZone {
        ZoneBuilder.configure(this)
        return this
    }

    override fun configure() {
        super.register(ZoneBorders(2746, 3733, 2746, 3735))
        super.register(ZoneBorders(2570, 3732, 2570, 3736))
    }

    override fun enter(e: Entity?): Boolean {
        return super.enter(e)
    }

    override fun fireEvent(identifier: String?, vararg args: Any?): Any {
        return Unit
    }

    override fun locationUpdate(e: Entity?, last: Location?) {
        if (e is Player && !e.isArtificial) {
            val player = e.asPlayer()
            if (!player.achievementDiaryManager.hasCompletedTask(DiaryType.FREMENNIK, 0, 2)) {
                player.achievementDiaryManager.finishTask(player, DiaryType.FREMENNIK, 0, 2)
                println("Fremennik Easy Diary Task 2 Completed!")
            } else {
                player.achievementDiaryManager.finishTask(player, DiaryType.FREMENNIK, 0, 2)
            }
        }
    }
}

  //  override fun leave(e: Entity?, logout: Boolean) {
    //    if (e is Player) {
      //      super.leave(e, logout)
        //}
    //}}
