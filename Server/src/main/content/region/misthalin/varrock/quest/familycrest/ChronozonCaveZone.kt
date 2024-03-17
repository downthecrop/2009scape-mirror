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
import core.game.node.item.Item
import org.rs09.consts.Items
import org.rs09.consts.NPCs


@Initializable
class ChronozonCaveZone: MapZone("FC ChronozoneZone", true), Plugin<Unit> {

    val spawnLoc = Location(3086, 9936, 0)
    var chronozon = ChronozonNPC(NPCs.CHRONOZON_667, spawnLoc)

    override fun configure() {
        register(ZoneBorders(3082, 9929, 3091, 9940))
    }

    override fun move(e: Entity?, from: Location?, to: Location?): Boolean {
        return super.move(e, from, to)
    }

    override fun enter(e: Entity?): Boolean {
        if (e != null) {
            if (e.isPlayer) {
                val player = e as Player
                if (player.questRepository.getQuest("Family Crest").getStage(e) in (19..99) &&
                    !player.hasItem(Item(Items.CREST_PART_781))){
                    // Chronozon is allowed to spawn (quest stage right and the player doesn't have the crest part)
                    // Now check there is not one already
                    if(!RegionManager.getLocalNpcs(spawnLoc, 5).contains(chronozon)){
                        chronozon.setPlayer(e)
                        chronozon.isRespawn = false
                        chronozon.location = spawnLoc
                        chronozon.init()
                    }
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
