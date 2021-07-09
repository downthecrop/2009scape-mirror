package rs09.game.interaction.region

import api.ContentAPI
import core.game.node.entity.player.link.audio.Audio
import core.game.system.task.Pulse
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import org.rs09.consts.NPCs
import rs09.game.camerautils.PlayerCamera
import rs09.game.interaction.InteractionListener

class JatizsoListeners : InteractionListener() {
    val GATES_CLOSED = intArrayOf(21403,21405)
    val NORTH_GATE_ZONE = ZoneBorders(2414,3822,2417,3825)
    val WEST_GATE_ZONE = ZoneBorders(2386,3797,2390,3801)
    val SOUTH_GAE_ZONE = ZoneBorders(2411,3795,2414,3799)
    val BELL = 21394
    val LINES = arrayOf(
        arrayOf(
            "YOU WOULDN'T KNOW HOW TO FIGHT A TROLL IF IT CAME UP AND BIT YER ARM OFF",
            "YAK LOVERS",
            "CALL THAT A TOWN? I'VE SEEN BETTER HAMLETS!",
            "AND YOUR FATHER SMELLED OF WINTERBERRIES!",
            "WOODEN BRIDGES ARE RUBBISH!",
            "OUR KING'S BETTER THAN YOUR BURGHER!",
        ),
        arrayOf(
             "YOU WOULDN'T KNOW HOW TO SHAVE A YAK IF YOUR LIFE DEPENDED ON IT",
            "MISERABLE LOSERS",
            "YOUR MOTHER WAS A HAMSTER!",
            "AT LEAST WE HAVE SOMETHING OTHER THAN SMELLY FISH!",
            "AT LEAST WE CAN COOK!",
        )
    )
    override fun defineListeners() {
        on(GATES_CLOSED, SCENERY, "open"){player, node ->
            if(NORTH_GATE_ZONE.insideBorder(player)){
                if(node.id == GATES_CLOSED.first()){
                    val other = ContentAPI.getScenery(node.location.transform(1, 0, 0)) ?: return@on true
                    ContentAPI.replaceScenery(node.asScenery(), node.id + 1, -1, Direction.EAST)
                    ContentAPI.replaceScenery(other.asScenery(), other.id - 1, -1, Direction.SOUTH)
                } else {
                    val other = ContentAPI.getScenery(node.location.transform(-1, 0, 0)) ?: return@on true
                    ContentAPI.replaceScenery(node.asScenery(), node.id + 1, -1, Direction.SOUTH)
                    ContentAPI.replaceScenery(other.asScenery(), other.id, -1, Direction.EAST)
                }

                ContentAPI.playAudio(player, ContentAPI.getAudio(81))
            } else if(WEST_GATE_ZONE.insideBorder(player)){
                if(node.id == GATES_CLOSED.first()){
                    val other = ContentAPI.getScenery(node.location.transform(0, 1, 0)) ?: return@on true
                    ContentAPI.replaceScenery(node.asScenery(), node.id + 1, -1, Direction.WEST)
                    ContentAPI.replaceScenery(other.asScenery(), other.id + 1, -1, Direction.NORTH)
                } else {
                    val other = ContentAPI.getScenery(node.location.transform(0, -1, 0)) ?: return@on true
                    ContentAPI.replaceScenery(node.asScenery(), node.id + 1, -1, Direction.NORTH)
                    ContentAPI.replaceScenery(other.asScenery(), other.id + 1, -1, Direction.WEST)
                }
            } else if(SOUTH_GAE_ZONE.insideBorder(player)){
                if(node.id == GATES_CLOSED.first()){
                    val other = ContentAPI.getScenery(node.location.transform(-1, 0, 0)) ?: return@on true
                    ContentAPI.replaceScenery(node.asScenery(), node.id + 1, -1, Direction.SOUTH)
                    ContentAPI.replaceScenery(other.asScenery(), other.id - 1, -1, Direction.EAST)
                } else {
                    val other = ContentAPI.getScenery(node.location.transform(1, 0, 0)) ?: return@on true
                    ContentAPI.replaceScenery(node.asScenery(), node.id + 1, -1, Direction.EAST)
                    ContentAPI.replaceScenery(other.asScenery(), other.id, -1, Direction.SOUTH)
                }
            }
            ContentAPI.playAudio(player, ContentAPI.getAudio(81))
            return@on true
        }

        on(NPCs.GUARD_5489, NPC, "watch-shouting"){player, node ->
            ContentAPI.lock(player, 200)
            ContentAPI.face(node.asNpc(), Location.create(2371, 3801, 2))
            node.asNpc().isRespawn = false
            ContentAPI.submitIndividualPulse(player, object : Pulse(4){
                var id = node.id
                var counter = 0
                val other = ContentAPI.findLocalNPC(player, getOther(node.id))

                override fun start() {
                    other?.isRespawn = false
                }

                override fun pulse(): Boolean {
                    val npc = ContentAPI.findLocalNPC(player, id) ?: return false
                    val index = when(id){
                        node.id -> 0
                        else -> 1
                    }
                    if(index == 1 && counter == 5) return true
                    ContentAPI.sendChat(npc, LINES[index][counter])
                    if(npc.id != node.id) counter++
                    id = getOther(id)
                    return false
                }

                override fun stop() {
                    ContentAPI.unlock(player)
                    other?.isRespawn = true
                    node.asNpc().isRespawn = true
                    super.stop()
                }

                fun getOther(npc: Int): Int{
                    return when(npc){
                        NPCs.GUARD_5489 -> NPCs.GUARD_5490
                        NPCs.GUARD_5490 -> NPCs.GUARD_5489
                        else -> NPCs.GUARD_5489
                    }
                }
            })
            return@on true
        }

        on(BELL, SCENERY, "ring-bell"){player, _ ->
            ContentAPI.playAudio(player, Audio(15))
            ContentAPI.sendMessage(player, "You ring the warning bell, but everyone ignores it!")
            return@on true
        }

        setDest(NPC, NPCs.MAGNUS_GRAM_5488){_ ->
            return@setDest Location.create(2416, 3801, 0)
        }
    }
}