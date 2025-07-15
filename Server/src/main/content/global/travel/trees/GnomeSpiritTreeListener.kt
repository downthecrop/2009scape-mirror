package content.global.travel.trees

import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import org.rs09.consts.NPCs
import core.game.dialogue.DialogueFile
import core.game.interaction.InteractionListener
import core.game.interaction.IntType
import core.game.world.GameWorld.Pulser
import core.tools.END_DIALOGUE
import content.data.Quests
import core.api.*

class GnomeSpiritTreeListener: InteractionListener {
    val spiritTrees = intArrayOf(1317,1293,1294,8355)

    override fun defineListeners() {
        on(spiritTrees, IntType.SCENERY, "talk-to"){ player, _ ->
            openDialogue(player, GnomeSpiritTreeDialogue(), NPC(NPCs.SPIRIT_TREE_3636))
            return@on true
        }
        on(spiritTrees, IntType.SCENERY, "teleport"){ player, _ ->
            openDialogue(player, GnomeSpiritTreeTeleportDialogue(), NPC(NPCs.SPIRIT_TREE_3636))
            return@on true
        }
    }
}

class GnomeSpiritTreeTeleportDialogue: DialogueFile() {
    private val LOCATIONS = arrayOf(
        Location(2542, 3170, 0),
        Location(2461, 3444, 0),
        Location(2556, 3259, 0),
        Location(3184, 3508, 0),
        Location(3060, 3256, 0), //Port Sarim
        Location(2613, 3856, 0), //Etceteria
        Location(2800, 3203, 0) //Brimhaven

    )

    private val ANIMATIONS = arrayOf(Animation(7082), Animation(7084))
    private val GRAPHICS = arrayOf(Graphics(1228), Graphics(1229))

    fun hasQuestCompleted(player: Player): Boolean {
        if (!isQuestComplete(player, Quests.TREE_GNOME_VILLAGE)) {
            sendDialogue(player, "The tree doesn't feel like talking.")
            stage = END_DIALOGUE
            return false
        }
        return true
    }

    private fun sendTeleport(player: Player, location: Location) {
        end()
        Pulser.submit(object : Pulse(1, player) {
            var count = 0
            override fun pulse(): Boolean {
                when (count) {
                    0 -> {
                        player.animate(ANIMATIONS[0])
                        player.graphics(GRAPHICS[0])
                    }
                    3 -> { teleport(player,location) }
                    5 -> {
                        player.animate(ANIMATIONS[1])
                        player.graphics(GRAPHICS[1])
                        player.face(null)
                        if (player.location.withinDistance(Location.create(3184, 3508, 0))) {
                            player.achievementDiaryManager.finishTask(player, DiaryType.VARROCK, 1, 5)
                        }
                        return true
                    }
                }
                count++
                return false
            }
        })
    }

    override fun handle(componentID: Int, buttonID: Int) {
        if(!GnomeSpiritTreeTeleportDialogue().hasQuestCompleted(player!!)) {
            stage = END_DIALOGUE
            return
        }

        var plantedSpiritTreeLocation = when {
            getVarbit(player!!, 720) == 20 -> "Port Sarim"
            getVarbit(player!!, 722) == 20 -> "Etceteria"
            getVarbit(player!!, 724) == 20 -> "Brimhaven"
            else -> null
            //There should never be a case where more than one spirit tree is planted.
        }
        if(plantedSpiritTreeLocation == null) {
        when (stage) {
            0 -> interpreter!!.sendOptions(
                    "Where would you like to go?",
                    "Tree Gnome Village",
                    "Tree Gnome Stronghold",
                    "Battlefield of Khazard",
                    "Grand Exchange"
                ).also { stage++ }
            1 -> when (buttonID) {
                    1 -> sendTeleport(player!!, LOCATIONS[0])
                    2 -> sendTeleport(player!!, LOCATIONS[1])
                    3 -> sendTeleport(player!!, LOCATIONS[2])
                    4 -> sendTeleport(player!!, LOCATIONS[3])
                }
            }
        }
        else when (stage) {
            0 -> interpreter!!.sendOptions(
                "Where would you like to go?",
                "Tree Gnome Village",
                "Tree Gnome Stronghold",
                "Battlefield of Khazard",
                "Grand Exchange",
                plantedSpiritTreeLocation
            ).also { stage++ }
            1 -> when (buttonID) {
                1 -> sendTeleport(player!!, LOCATIONS[0])
                2 -> sendTeleport(player!!, LOCATIONS[1])
                3 -> sendTeleport(player!!, LOCATIONS[2])
                4 -> sendTeleport(player!!, LOCATIONS[3])
                5 -> when (plantedSpiritTreeLocation) {
                        "Port Sarim" -> sendTeleport(player!!,LOCATIONS[4])
                        "Etceteria" -> sendTeleport(player!!,LOCATIONS[5])
                        "Brimhaven" -> sendTeleport(player!!,LOCATIONS[6])
                        else -> stage = END_DIALOGUE
                    }
            }
        }
    }
}

class GnomeSpiritTreeDialogue: DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        if(!GnomeSpiritTreeTeleportDialogue().hasQuestCompleted(player!!)) {
            stage = END_DIALOGUE
            return
        }
        when (stage) {
            0 -> npcl("If you are a friend of the gnome people, you are a friend of mine, Do you wish to travel?").also{ stage++ }
            1 -> { openDialogue(player!!, GnomeSpiritTreeTeleportDialogue()) }
        }
    }
}