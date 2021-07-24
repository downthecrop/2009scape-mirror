package rs09.game.content.activity.fog

import api.ContentAPI
import core.game.node.entity.combat.DeathTask
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import org.rs09.consts.Items
import kotlin.math.max

object FOGUtils {

    const val TIMER_VARP = 1215

    fun getRandomStartLocation(): Location {
        return startLocations.random()
    }

    fun getSession(player: Player): FOGSession? {
        return player.getAttribute("fog-session")
    }

    fun carryingStone(player: Player): Boolean {
        return ContentAPI.inEquipment(player, Items.STONE_OF_POWER_12845)
    }

    fun isHunted(player: Player): Boolean {
        val session = getSession(player) ?: return false
        return session.hunted == player
    }

    fun isHunter(player: Player): Boolean {
        return !isHunted(player)
    }

    fun incrementHuntedScore(session: FOGSession, amount: Int){
        if(DeathTask.isDead(session.hunted)) return

        when(session.round){
            1 -> session.playerBScore += amount
            2 -> session.playerAScore += amount
        }


    }

    fun isInHouse(player: Player): Boolean {
        for(z in houseZones){
            if(z.insideBorder(player)) return true
        }
        return false
    }

    fun updateHuntStatus(player: Player, hunter: Boolean){
        player.packetDispatch.sendInterfaceConfig(730, 25, hunter)
        player.packetDispatch.sendInterfaceConfig(730, 26, !hunter)
        if(hunter){
            player.packetDispatch.sendString(getSession(player)?.hunted?.name ?: "AAAAA", 730, 18)
        } else {
            player.packetDispatch.sendString(getSession(player)?.hunter?.name ?: "AAAAA", 730, 18)
        }
    }

    fun getTickScore(player: Player): Int {
        return max(50 - player.location.getDistance(getClosestCentralTile(player)).toInt(), 10)
    }

    fun getClosestCentralTile(player: Player): Location{
        var closest = centralTiles.first()
        for(loc in centralTiles){
            if(player.location.getDistance(loc) < player.location.getDistance(closest)) closest = loc
        }
        return closest
    }

    fun hideStoneNotice(player: Player){
        player.packetDispatch.sendInterfaceConfig(730, 23, true)
    }

    fun showStoneNotice(player: Player){
        player.packetDispatch.sendInterfaceConfig(730, 23, false)
    }

    val houseZones = arrayOf(
        ZoneBorders(1650, 5702, 1652, 5704),
        ZoneBorders(1666, 5703, 1668, 5705),
        ZoneBorders(1675, 5687, 1677, 5689),
        ZoneBorders(1655, 5682, 1657, 5684)
    )

    val startLocations = arrayOf(
        Location.create(1627, 5708, 0),
        Location.create(1624, 5697, 0),
        Location.create(1626, 5692, 0),
        Location.create(1629, 5685, 0),
        Location.create(1631, 5680, 0),
        Location.create(1635, 5672, 0),
        Location.create(1642, 5665, 0),
        Location.create(1648, 5660, 0),
        Location.create(1655, 5657, 0),
        Location.create(1660, 5657, 0),
        Location.create(1666, 5658, 0),
        Location.create(1673, 5660, 0),
        Location.create(1681, 5663, 0),
        Location.create(1687, 5665, 0),
        Location.create(1692, 5669, 0),
        Location.create(1695, 5674, 0),
        Location.create(1700, 5681, 0),
        Location.create(1701, 5685, 0),
        Location.create(1703, 5690, 0),
        Location.create(1697, 5697, 0),
        Location.create(1694, 5706, 0),
        Location.create(1690, 5713, 0),
        Location.create(1687, 5717, 0),
        Location.create(1681, 5724, 0),
        Location.create(1675, 5727, 0),
        Location.create(1669, 5729, 0),
        Location.create(1663, 5732, 0),
        Location.create(1656, 5730, 0),
        Location.create(1647, 5727, 0),
        Location.create(1637, 5723, 0),
        Location.create(1634, 5719, 0),
        Location.create(1627, 5712, 0),
        Location.create(1624, 5704, 0),
        Location.create(1624, 5695, 0),
        Location.create(1628, 5686, 0),
        Location.create(1633, 5678, 0),
        Location.create(1636, 5670, 0),
        Location.create(1642, 5665, 0),
        Location.create(1648, 5660, 0),
        Location.create(1655, 5657, 0),
        Location.create(1633, 5718, 0),
        Location.create(1644, 5727, 0),
        Location.create(1656, 5731, 0),
        Location.create(1667, 5731, 0),
        Location.create(1682, 5722, 0),
        Location.create(1691, 5711, 0)
    )

    val centralTiles = arrayOf(
        Location.create(1663, 5695, 0),
        Location.create(1664, 5695, 0),
        Location.create(1664, 5696, 0),
        Location.create(1663, 5696, 0)
    )
}