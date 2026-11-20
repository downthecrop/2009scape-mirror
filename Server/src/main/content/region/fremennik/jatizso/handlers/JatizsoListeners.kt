package content.region.fremennik.jatizso.handlers

import core.api.*
import core.game.node.entity.npc.NPC
import core.game.system.task.Pulse
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import org.rs09.consts.NPCs
import content.region.fremennik.jatizso.dialogue.LeftieRightieDialogue
import core.game.interaction.InteractionListener
import core.game.interaction.IntType
import org.rs09.consts.Sounds

class JatizsoListeners : InteractionListener {
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
        on(TOWER_GUARDS, IntType.NPC, "watch-shouting"){ player, _ ->
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

        on(BELL, IntType.SCENERY, "ring-bell"){ player, _ ->
            playAudio(player, Sounds.STEEL_15)
            sendMessage(player, "You ring the warning bell, but everyone ignores it!")
            return@on true
        }

        on(GUARDS, IntType.NPC, "talk-to"){ player, node ->
            player.dialogueInterpreter.open(LeftieRightieDialogue(), NPC(NPCs.GUARD_5491))
            return@on true
        }

        on(KING_CHEST, IntType.SCENERY, "open"){ player, node ->
            sendPlayerDialogue(player, "I probably shouldn't mess with that.", core.game.dialogue.FacialExpression.HALF_THINKING)
            return@on true
        }

        setDest(IntType.NPC, NPCs.MAGNUS_GRAM_5488){ _, _ ->
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