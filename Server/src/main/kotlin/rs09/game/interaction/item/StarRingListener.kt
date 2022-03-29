package rs09.game.interaction.item

import api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import org.json.simple.JSONObject
import org.rs09.consts.Items
import rs09.ServerStore
import rs09.ServerStore.Companion.getBoolean
import rs09.game.content.dialogue.DialogueFile
import rs09.game.content.global.worldevents.WorldEvents
import rs09.game.content.global.worldevents.shootingstar.ShootingStar
import rs09.game.content.global.worldevents.shootingstar.ShootingStarEvent
import rs09.game.interaction.InteractionListener
import java.util.concurrent.TimeUnit

class StarRingListener : InteractionListener(){

    val RING = Items.RING_OF_THE_STAR_SPRITE_14652

    override fun defineListeners() {
        on(RING, ITEM, "rub", "operate"){player, node ->
            val star = WorldEvents.get("shooting-stars") as? ShootingStarEvent

            if(star == null) sendDialogue(player, "There is currently no active star.").also { return@on true }

            if(getStoreFile().getBoolean(player.username.toLowerCase())){
                sendDialogue(player, "The ring is still recharging.")
                return@on true
            }

            val condition: (Player) -> Boolean = when(star?.star!!.location.toLowerCase()){
                "canifis bank" -> { p -> p.questRepository.isComplete("Priest in Peril")}
                "crafting guild" -> {p -> hasLevelStat(p, Skills.CRAFTING, 40) }
                "south crandor mining site" -> {p -> p.questRepository.isComplete("Dragon Slayer")}
                else -> {_ -> true}
            }

            if(!condition.invoke(player) || player.skullManager.isWilderness){
                sendDialogue(player, "Magical forces prevent your teleportation.")
                return@on true
            }

            val shouldWarn = when(star.star.location){
                "North Edgeville mining site",
                "Southern wilderness mine",
                "Pirates' Hideout mine",
                "Lava Maze mining site",
                "Mage Arena bank" -> true
                else -> false
            }

            openDialogue(player, RingDialogue(shouldWarn, star.star))

            return@on true
        }
    }

    internal class RingDialogue(val shouldWarn: Boolean, val star: ShootingStar) : DialogueFile(){
        override fun handle(componentID: Int, buttonID: Int) {
            if(shouldWarn){
                when(stage) {
                    0 -> dialogue("WARNING: That mining site is located in the wilderness.").also { stage++ }
                    1 -> player!!.dialogueInterpreter.sendOptions("Continue?","Yes","No").also { stage++ }
                    2 -> when(buttonID){
                        1 -> teleport(player!!, star).also { end() }
                        2 -> end()
                    }
                }
            } else {
                when(stage){
                    0 -> player!!.dialogueInterpreter.sendOptions("Teleport to the Star?", "Yes", "No").also { stage++ }
                    1 -> when(buttonID){
                        1 -> teleport(player!!, star).also { end() }
                        2 -> end()
                    }
                }
            }
        }

        fun teleport(player: Player, star: ShootingStar){
            teleport(player, star.crash_locations[star.location]!!.transform(0, -1, 0), TeleportManager.TeleportType.MINIGAME)
            getStoreFile()[player.username.toLowerCase()] = true
        }
    }

    companion object {
        fun getStoreFile(): JSONObject {
            return ServerStore.getArchive("daily-star-ring")
        }
    }
}