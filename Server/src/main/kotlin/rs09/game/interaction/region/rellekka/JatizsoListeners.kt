package rs09.game.interaction.region.rellekka

import api.*
import core.game.content.dialogue.FacialExpression
import core.game.content.global.action.SpecialLadders
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.link.audio.Audio
import core.game.system.task.Pulse
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import org.rs09.consts.NPCs
import rs09.game.content.dialogue.region.jatizso.LeftieRightieDialogue
import rs09.game.interaction.InteractionListener

class JatizsoListeners : InteractionListener() {
    val GATES_CLOSED = intArrayOf(21403,21405)
    val NORTH_GATE_ZONE = ZoneBorders(2414,3822,2417,3825)
    val WEST_GATE_ZONE = ZoneBorders(2386,3797,2390,3801)
    val SOUTH_GAE_ZONE = ZoneBorders(2411,3795,2414,3799)
    val BELL = 21394
    val TOWER_GUARDS = intArrayOf(NPCs.GUARD_5490, NPCs.GUARD_5489)
    val GUARDS = intArrayOf(NPCs.GUARD_5491, NPCs.GUARD_5492)
    val KING_CHEST = intArrayOf(21299,21300)
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
                    val other = getScenery(node.location.transform(1, 0, 0)) ?: return@on true
                    replaceScenery(node.asScenery(), node.id + 1, -1, Direction.EAST)
                    replaceScenery(other.asScenery(), other.id - 1, -1, Direction.NORTH_EAST)
                } else {
                    val other = getScenery(node.location.transform(-1, 0, 0)) ?: return@on true
                    replaceScenery(node.asScenery(), node.id + 1, -1, Direction.NORTH_EAST)
                    replaceScenery(other.asScenery(), other.id, -1, Direction.EAST)
                }
            } else if(WEST_GATE_ZONE.insideBorder(player)){
                if(node.id == GATES_CLOSED.first()){
                    val other = getScenery(node.location.transform(0, 1, 0)) ?: return@on true
                    replaceScenery(node.asScenery(), node.id + 1, -1, Direction.WEST)
                    replaceScenery(other.asScenery(), other.id + 1, -1, Direction.NORTH)
                } else {
                    val other = getScenery(node.location.transform(0, -1, 0)) ?: return@on true
                    replaceScenery(node.asScenery(), node.id + 1, -1, Direction.NORTH)
                    replaceScenery(other.asScenery(), other.id + 1, -1, Direction.WEST)
                }
            } else if(SOUTH_GAE_ZONE.insideBorder(player)){
                if(node.id == GATES_CLOSED.first()){
                    val other = getScenery(node.location.transform(-1, 0, 0)) ?: return@on true
                    replaceScenery(node.asScenery(), node.id + 1, -1, Direction.SOUTH)
                    replaceScenery(other.asScenery(), other.id - 1, -1, Direction.EAST)
                } else {
                    val other = getScenery(node.location.transform(1, 0, 0)) ?: return@on true
                    replaceScenery(node.asScenery(), node.id + 1, -1, Direction.EAST)
                    replaceScenery(other.asScenery(), other.id, -1, Direction.SOUTH)
                }
            }
            playAudio(player, getAudio(81))
            return@on true
        }

        on(TOWER_GUARDS, NPC, "watch-shouting"){player, _ ->
            val local = findLocalNPC(player, NPCs.GUARD_5489)
            lock(player, 200)
            face(local!!, Location.create(2371, 3801, 2))
            local.asNpc().isRespawn = false
            submitIndividualPulse(player, object : Pulse(4){
                var id = NPCs.GUARD_5489
                var counter = 0
                val other = findLocalNPC(player, getOther(NPCs.GUARD_5489))

                override fun start() {
                    other?.isRespawn = false
                }

                override fun pulse(): Boolean {
                    val npc = findLocalNPC(player, id) ?: return false
                    val index = when(id){
                        NPCs.GUARD_5489 -> 0
                        else -> 1
                    }
                    if(index == 1 && counter == 5) return true
                    sendChat(npc, LINES[index][counter])
                    if(npc.id != NPCs.GUARD_5489) counter++
                    id = getOther(id)
                    return false
                }

                override fun stop() {
                    unlock(player)
                    other?.isRespawn = true
                    local.asNpc().isRespawn = true
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
            playAudio(player, Audio(15))
            sendMessage(player, "You ring the warning bell, but everyone ignores it!")
            return@on true
        }

        on(GUARDS, NPC, "talk-to"){player, node ->
            player.dialogueInterpreter.open(LeftieRightieDialogue(), NPC(NPCs.GUARD_5491))
            return@on true
        }

        on(KING_CHEST, SCENERY, "open"){player, node ->
            sendPlayerDialogue(player, "I probably shouldn't mess with that.", FacialExpression.HALF_THINKING)
            return@on true
        }

        setDest(NPC, NPCs.MAGNUS_GRAM_5488){_,_ ->
            return@setDest Location.create(2416, 3801, 0)
        }

        //Climb handling for the ladders in the towers around the city walls.
        addClimbDest(Location.create(2388, 3804, 0),Location.create(2387, 3804, 2))
        addClimbDest(Location.create(2388, 3804, 2),Location.create(2387, 3804, 0))
        addClimbDest(Location.create(2388, 3793, 0),Location.create(2387, 3793, 2))
        addClimbDest(Location.create(2388, 3793, 2),Location.create(2387, 3793, 0))
        addClimbDest(Location.create(2410, 3823, 0),Location.create(2410, 3824, 2))
        addClimbDest(Location.create(2410, 3823, 2),Location.create(2410, 3824, 0))
        addClimbDest(Location.create(2421, 3823, 0),Location.create(2421, 3824, 2))
        addClimbDest(Location.create(2421, 3823, 2),Location.create(2421, 3824, 0))
    }
}