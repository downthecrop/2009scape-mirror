package rs09.game.content.activity.pyramidplunder

import api.*
import core.game.component.Component
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.world.map.Direction
import core.game.world.map.Location
import core.tools.RandomFunction
import org.rs09.consts.Components
import org.rs09.consts.Items

/**
 * A collection of methods needed for Pyramid Plunder.
 */
object PlunderUtils {
    fun hasPlayer(player: Player): Boolean
    {
        return PlunderData.playerLocations[player] != null
    }

    fun registerPlayer(player: Player)
    {
        PlunderData.players.add(player)
        PlunderData.timeLeft[player] = 500 //5 minutes
    }

    fun unregisterPlayer(player: Player)
    {
        PlunderData.players.remove(player)
        PlunderData.playerLocations.remove(player)
        PlunderData.timeLeft.remove(player)
    }

    fun expel(player: Player, summonMummy: Boolean)
    {
        teleport(player, Location.create(3288, 2802, 0))
        unregisterPlayer(player)
        resetOverlay(player)
        resetObjectVarbits(player)

        if(!summonMummy)
        {
            sendDialogue(player, "You've run out of time and the guardian mummy has thrown you out.")
        }
        else
        {
            sendDialogue(player, "You've been expelled by the guardian mummy.")
        }
    }

    fun decrementTimeRemaining() : ArrayList<Player>
    {
        val timesUp = ArrayList<Player>()
        PlunderData.timeLeft.forEach { player, left ->
            if(left <= 0) timesUp.add(player)
            PlunderData.timeLeft[player] = left - 1
            updateOverlay(player)
        }
        return timesUp
    }

    fun loadNextRoom(player: Player)
    {
        val current = PlunderData.playerLocations[player]

        val next: PlunderRoom = if(current == null)
            getRoom(1)
        else
            getRoom(current.room + 1)

        if(PlunderData.playerLocations.filter { it.value == next }.isEmpty()) //if no one is in the next room
        {
            PlunderData.doors[next.room - 1] = PlunderData.doorVarbits.random() //reshuffle the next room's exit door
        }

        teleport(player, next.entrance)
        PlunderData.playerLocations[player] = next
    }

    fun getRoom(number: Int) : PlunderRoom
    {
        return PlunderData.rooms[number - 1]
    }

    fun getRoom(player: Player): PlunderRoom?
    {
        return PlunderData.playerLocations[player]
    }

    fun getRoomLevel(player: Player): Int
    {
        return 11 + (10 * getRoom(player)!!.room)
    }

    fun getSpearDestination(player: Player): Location
    {
        val room = getRoom(player)!!
        return when(room.spearDirection)
        {
            Direction.NORTH -> player.location.transform(0, 3, 0)
            Direction.SOUTH -> player.location.transform(0, -3, 0)
            Direction.WEST -> player.location.transform(-3, 0, 0)
            Direction.EAST -> player.location.transform(3, 0, 0)
            else -> player.location
        }
    }

    fun getUrnXp(player: Player, check: Boolean): Double
    {
        val room = getRoom(player)!!.room
        return if(check)
        {
            when(room)
            {
                1 -> 20.0
                2 -> 30.0
                3 -> 50.0
                4 -> 70.0
                5 -> 100.0
                6 -> 150.0
                7 -> 225.0
                8 -> 275.0
                else -> 0.0
            }
        }
        else
        {
            when(room)
            {
                1 -> 60.0
                2 -> 90.0
                3 -> 150.0
                4 -> 215.0
                5 -> 300.0
                6 -> 450.0
                7 -> 675.0
                8 -> 825.0
                else -> 0.0
            }
        }
    }

    fun resetObjectVarbits(player: Player)
    {
        var varp = player.varpManager.get(821)
        varp.clear()
        varp.send(player)
        varp = player.varpManager.get(820)
        varp.clear()
        varp.send(player)
        PlunderData.doorVarbits.forEach { player.varpManager.setVarbit(it, 0) }
    }

    fun openOverlay(player: Player)
    {
        player.interfaceManager.openOverlay(Component(Components.NTK_OVERLAY_428))
        updateOverlay(player)
    }

    fun updateOverlay(player: Player)
    {
        player.varpManager.setVarbit(2375, 500 - (PlunderData.timeLeft[player] ?: 0))
        player.varpManager.setVarbit(2376, 11 + (getRoom(player)!!.room * 10))
        player.varpManager.setVarbit(2377, getRoom(player)!!.room)
    }

    fun resetOverlay(player: Player)
    {
        player.varpManager.setVarbit(2375, 0)
        player.packetDispatch.resetInterface(Components.NTK_OVERLAY_428)
        player.interfaceManager.closeOverlay()
    }

    fun getDoor(player: Player) : Int
    {
        val room = getRoom(player)!!.room
        return PlunderData.doors[room - 1]
    }

    fun rollSceptre(player: Player): Boolean
    {
        val room = getRoom(player)!!.room
        val chance = when(room)
        {
            1 -> 1500
            2 -> 1350
            3 -> 1250
            4 -> 1150
            else -> 1000
        }

        if(RandomFunction.roll(chance))
        {
            expel(player, true)
            runTask(player, 2)
            {
                addItemOrDrop(player, Items.PHARAOHS_SCEPTRE_9050)
                sendNews("${player.username} has received a Pharaoh's Sceptre while doing Pyramid Plunder!")
            }
            return true
        }
        return false
    }

    fun getSarcophagusXp(player: Player) : Double
    {
        val room = getRoom(player)!!.room
        return when(room)
        {
            1 -> 20.0
            2 -> 30.0
            3 -> 50.0
            4 -> 70.0
            5 -> 100.0
            6 -> 150.0
            7 -> 225.0
            8 -> 275.0
            else -> 0.0
        }
    }

    fun getChestXp(player: Player): Double
    {
        val room = getRoom(player)!!.room
        return when(room)
        {
            1 -> 60.0
            2 -> 90.0
            3 -> 150.0
            4 -> 215.0
            5 -> 300.0
            6 -> 450.0
            7 -> 675.0
            8 -> 825.0
            else -> 0.0
        } * 0.66
    }

    fun getDoorXp(player: Player, lockpick: Boolean) : Double
    {
        val room = getRoom(player)!!.room
        var reward = when(room)
        {
            1 -> 60.0
            2 -> 90.0
            3 -> 150.0
            4 -> 215.0
            5 -> 300.0
            6 -> 450.0
            7 -> 675.0
            8 -> 825.0
            else -> 0.0
        } * 0.66

        if(lockpick)
            reward /= 2.0

        return reward
    }

    fun rollArtifact(player: Player, tier: Int) : Int
    {
        //tier 1 -> urn
        //tier 2 -> sarcophagus
        //tier 3 -> chest
        val room = getRoom(player)!!.room
        val divisor = (room * 2) * (tier * 35)
        val goldRate = divisor / 650.0
        val stoneRate = divisor / 250.0

        val roll = RandomFunction.RANDOM.nextDouble()
        if(goldRate > roll)
            return PlunderData.artifacts[2].random()
        if(stoneRate > roll)
            return PlunderData.artifacts[1].random()
        return PlunderData.artifacts[0].random()
    }

    fun checkEntranceSwitch()
    {
        if(System.currentTimeMillis() > PlunderData.nextEntranceSwitch)
        {
            PlunderData.currentEntrance = PlunderData.pyramidEntranceVarbits.random()
            PlunderData.nextEntranceSwitch = System.currentTimeMillis() + (60000 * 15)

        }
    }

    fun checkEntrance(door: Node): Boolean
    {
        return door.asScenery().definition.varbitID == PlunderData.currentEntrance
    }

    fun rollUrnSuccess(player: Player, charmed: Boolean = false): Boolean
    {
        val level = getDynLevel(player, Skills.THIEVING)

        if (getRoom(player) == null) {
            return false
        }

        val room = getRoom(player)!!.room
        return RandomFunction.random(level) > (room * if(charmed) 2 else 4)
    }
}