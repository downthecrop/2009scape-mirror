package rs09.game.content.global.worldevents.holiday.halloween

import api.*
import core.game.component.Component
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.tools.RandomFunction
import org.rs09.consts.Components
import rs09.ServerStore
import rs09.ServerStore.Companion.getInt
import rs09.ServerStore.Companion.getString
import rs09.game.content.dialogue.DialogueFile
import rs09.game.interaction.InteractionListener
import rs09.game.world.GameWorld
import rs09.tools.END_DIALOGUE

class TrickOrTreatHandler : InteractionListener() {
    override fun defineListeners() {
        on(NPC, "trick-or-treat"){player, node ->
            val hasDone5 = getDailyTrickOrTreats(player) == 5
            val hasDoneMe = getTrickOrTreatedNPCs(player).contains(node.name.toLowerCase())

            if(hasDone5){
                sendNPCDialogue(player, node.id, "My informants tell me you've already collected candy from 5 people today.", FacialExpression.FRIENDLY)
                return@on true
            }

            if(hasDoneMe){
                sendNPCDialogue(player, node.id, "You've already asked me today! Don't get greedy, now.", FacialExpression.ANNOYED)
                return@on true
            }

            player.dialogueInterpreter.open(object : DialogueFile(){
                override fun handle(componentID: Int, buttonID: Int) {
                    when(stage){
                        0 -> playerl(FacialExpression.FRIENDLY, "Trick or treat!").also { if(RandomFunction.roll(20)) stage = 10 else stage++ }
                        1 -> npcl(FacialExpression.FRIENDLY, "Very well, then, here you are my friend.").also { stage++ }
                        2 -> {
                            player.dialogueInterpreter.sendItemMessage(14084, "They hand you a nicely-wrapped candy.")
                            addItemOrDrop(player, 14084, 1)
                            registerNpc(player, npc!!)
                            incrementDailyToT(player)
                            stage = END_DIALOGUE
                        }

                        10 -> npcl(FacialExpression.EVIL_LAUGH, "I CHOOSE TRICK!").also { player.lock(); GameWorld.submit(object : Pulse() {
                            var counter = 0
                            override fun pulse(): Boolean {
                                //gfx 1898
                                when(counter++){
                                    0 -> npc!!.visualize(Animation(1979), Graphics(1898)).also { npc!!.faceLocation(player.location) }
                                    2 -> player.dialogueInterpreter.close()
                                    5 -> player.interfaceManager.open(Component(Components.FADE_TO_BLACK_120))
                                    8 -> player.properties.teleportLocation = Location.create(3106, 3382, 0)
                                    12 -> {
                                        player.interfaceManager.close()
                                        player.interfaceManager.open(Component(Components.FADE_FROM_BLACK_170))
                                        registerNpc(player, npc!!)
                                    }
                                    15 -> player.interfaceManager.close().also { player.unlock() }
                                    16 -> return true
                                }
                                return false
                            }
                        }) }
                    }
                }
            }, node.asNpc())
            return@on true
        }
    }

    fun incrementDailyToT(player: Player){
        ServerStore.getArchive("daily-tot-total")[player.username.toLowerCase()] = getDailyTrickOrTreats(player) + 1
    }

    fun getDailyTrickOrTreats(player: Player) : Int {
        return ServerStore.getArchive("daily-tot-total").getInt(player.username.toLowerCase())
    }

    fun getTrickOrTreatedNPCs(player: Player): String {
        return ServerStore.getArchive("daily-tot-npcs").getString(player.username.toLowerCase())
    }

    fun registerNpc(player: Player, npc: NPC){
        var soFar = getTrickOrTreatedNPCs(player)
        soFar += ":" + npc.name.toLowerCase() + ":"
        ServerStore.getArchive("daily-tot-npcs")[player.username.toLowerCase()] = soFar
    }
}