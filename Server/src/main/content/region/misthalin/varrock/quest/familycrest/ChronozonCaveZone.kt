package plugin.quest.members.familycrest


import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneBuilder
import core.plugin.Initializable
import core.plugin.Plugin
import content.region.misthalin.varrock.quest.familycrest.ChronozonNPC


@Initializable
class ChronozonCaveZone: MapZone("FC ChronozoneZone", true), Plugin<Unit> {

    val triggers = ArrayList<Location>()
    var chronozon = ChronozonNPC(667, Location(3086, 9936, 0))
    override fun configure() {
        register(ZoneBorders(3082, 9929, 3091, 9940))
        triggers.add(Location.create(3083, 9939))
        triggers.add(Location.create(3084, 9939))
        triggers.add(Location.create(3085, 9939))
        triggers.add(Location.create(3086, 9939))
        triggers.add(Location.create(3087, 9939))
        triggers.add(Location.create(3088, 9939))
        triggers.add(Location.create(3089, 9939))
        triggers.add(Location.create(3090, 9939))
    }

    override fun move(e: Entity?, from: Location?, to: Location?): Boolean {
        return super.move(e, from, to)
    }

    override fun enter(e: Entity?): Boolean {
        if (e != null) {
            if (e.isPlayer) {
                chronozon = ChronozonNPC(667, Location(3086, 9936, 0))
                var player = e as Player
                if (player.questRepository.getQuest("Family Crest").getStage(e) == 19 && !RegionManager.getLocalNpcs(
                        Location(3086, 9936, 0),
                        5
                    ).contains(chronozon)
                ) {
                    chronozon.setPlayer(e);
                    chronozon.isRespawn = false
                    chronozon.init()
                }
            }
            return true
        }
        return false
    }
    override fun newInstance(arg: Unit?): Plugin<Unit> {
        ZoneBuilder.configure(this)
        return this
    }

    override fun fireEvent(identifier: String?, vararg args: Any?): Any {
        return UInt
    }


}
