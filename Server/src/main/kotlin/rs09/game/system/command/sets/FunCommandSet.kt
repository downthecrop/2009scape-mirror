package rs09.game.system.command.sets

import api.*
import core.game.content.quest.tutorials.tutorialisland.CharacterDesign
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import rs09.game.content.dialogue.DialogueFile
import rs09.game.interaction.InteractionListeners
import rs09.game.interaction.SpadeDigListener
import rs09.game.system.command.Command
import rs09.game.system.command.Privilege
import rs09.game.world.GameWorld
import rs09.tools.END_DIALOGUE
import java.util.*

@Initializable
class FunCommandSet : CommandSet(Privilege.ADMIN) {

    var npcs: List<NPC> = ArrayList()


    override fun defineCommands() {

        /**
         * Force animation + messages on all NPCs in a radius of 10 from the player.
         */
        define("npcareaanim") { player, args ->
            if (args.size < 3) {
                reject(player, "Syntax error: ::npcareaanim <Animation ID> <String>")
            }
            npcs = RegionManager.getLocalNpcs(player.location, 10)
            for (n in npcs) {
                n.sendChat(args.slice(2 until args.size).joinToString(" "))
                n.lock(6)
                n.faceTemporary(player, 6)
                n.animator.animate(Animation(args[1].toInt()))
                n.animate(Animation.create(-1), 6)
            }
        }


        /**
         * Transform a player's appearance into that of an NPC.
         */
        define("pnpc", Privilege.MODERATOR){ player, args ->
            if(args.size < 2){
                reject(player, "Usage: ::pnpc <npcid>")
                return@define
            }

            val pnpc_id = args[1].toIntOrNull()
            if(pnpc_id == null){
                reject(player, "<npcid> must be a valid integer.")
            }

            player.appearance.transformNPC(pnpc_id!!)
            notify(player,"Transformed into NPC $pnpc_id")
        }


        /**
         * Open bank
         */
        define("bank"){ player, _ ->
            player.getBank().open()
        }

        /**
         * Toggle invisibility
         */
        define("invis"){ player, _ ->
            player.isInvisible = !player.isInvisible
            notify(player,"You are now ${if (player.isInvisible) "invisible" else "visible"} to others.")
        }


        /**
         * Toggle 1-hit kills
         */
        define("1hit"){ player, _ ->
            player.setAttribute("1hko", !player.getAttribute("1hko", false))
            notify(player,"1-hit KO mode " + if (player.getAttribute("1hko", false)) "on." else "off.")
        }


        /**
         * Toggle god mode
         */
        define("god"){ player, _ ->
            player.setAttribute("godMode", !player.getAttribute("godMode", false))
            notify(player,"God mode ${if (player.getAttribute("godMode", false)) "enabled." else "disabled."}")
        }


        /**
         * Go on Mr Bones' Wild Ride
         */
        define("mrboneswildride"){ player, _ ->
            val boneMode = !player.getAttribute("boneMode",false)
            player.setAttribute("boneMode", boneMode)
            notify(player,"Bone Mode ${if (boneMode) "<col=00ff00>ENGAGED</col>." else "<col=ff0000>POWERING DOWN</col>."}")
            player.appearance.rideCart(boneMode)
            if (player.appearance.isRidingMinecart) {
                var i = 0
                GameWorld.Pulser.submit(object : Pulse(1, player) {
                    override fun pulse(): Boolean {
                        if (i++ % 12 == 0) player.sendChat("I want to get off Mr. Bones Wild Ride.")
                        player.moveStep()
                        return !player.appearance.isRidingMinecart
                    }
                })
            }
        }


        /**
         * Opens up the makeover interface
         */
        define("makeover", Privilege.MODERATOR){ player, _ ->
            CharacterDesign.open(player)
        }

        /**
         * Bury inventory at current location
         */
        define("bury"){player, _ ->
            if(player.inventory.isEmpty){
                reject(player, "You have no items to bury.")
            }

            player.dialogueInterpreter.open(object : DialogueFile(){
                override fun handle(componentID: Int, buttonID: Int) {
                    when(stage){
                        0 -> dialogue("This will bury your whole inventory in this spot.","Are you sure?").also { stage++ }
                        1 -> options("Yes","No").also { stage++ }
                        2 -> when(buttonID){
                            1 -> bury(player).also { end() }
                            2 -> stage = END_DIALOGUE
                        }
                    }
                }
            })

        }
    }

    fun bury(player: Player){
        val loc = Location.create(player.location)
        val inv = player.inventory.toArray().filterNotNull()
        SpadeDigListener.registerListener(player.location){p ->
            for(item in inv){
                addItemOrDrop(p, item.id, item.amount)
                sendMessage(p, "You dig and find ${if(item.amount > 1) "some" else "a"} ${item.name}")
            }
            sendNews("${player.username} has found the hidden treasure! Congratulations!!!")
            SpadeDigListener.listeners.remove(loc)
        }
        player.inventory.clear()
        notify(player, "You have buried your loot at ${loc.toString()}")
    }

}
