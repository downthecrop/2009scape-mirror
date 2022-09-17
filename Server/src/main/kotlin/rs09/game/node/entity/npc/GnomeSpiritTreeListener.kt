package rs09.game.node.entity.npc

import api.isQuestComplete
import api.openDialogue
import api.sendDialogue
import api.teleport
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import org.rs09.consts.NPCs
import rs09.game.content.dialogue.DialogueFile
import rs09.game.content.quest.members.tree.TreeGnomeVillage
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.IntType
import rs09.game.world.GameWorld.Pulser
import rs09.tools.END_DIALOGUE

class GnomeSpiritTreeListener: InteractionListener {
    val spiritTrees = intArrayOf(1317,1293,1294)

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
        Location(3184, 3508, 0)
    )

    private val ANIMATIONS = arrayOf(Animation(7082), Animation(7084))
    private val GRAPHICS = arrayOf(Graphics(1228), Graphics(1229))

    fun hasQuestCompleted(player: Player): Boolean {
        if (!isQuestComplete(player, TreeGnomeVillage.questName)) {
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