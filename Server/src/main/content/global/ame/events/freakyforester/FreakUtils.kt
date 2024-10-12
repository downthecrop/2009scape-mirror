package content.global.ame.events.freakyforester

import core.ServerConstants
import core.api.*
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.tools.RandomFunction

object FreakUtils{
    const val freakNpc = NPCs.FREAKY_FORESTER_2458
    const val freakPreviousLoc = "/save:original-loc"
    const val freakTask = "/save:freakyf:task"
    const val freakComplete = "/save:freakyf:complete"
    const val pheasantKilled = "freakyf:killed"
    val freakArea = ZoneBorders(2587, 4758, 2616, 4788)
    fun giveFreakTask(player: Player) {
        when(RandomFunction.getRandom(4)) {
            0 -> setAttribute(player, freakTask, NPCs.PHEASANT_2459)
            1 -> setAttribute(player, freakTask, NPCs.PHEASANT_2460)
            2 -> setAttribute(player, freakTask, NPCs.PHEASANT_2461)
            3 -> setAttribute(player, freakTask, NPCs.PHEASANT_2462)
            else -> setAttribute(player, freakTask, NPCs.PHEASANT_2459)
        }
        player.dialogueInterpreter.open(FreakyForesterDialogue(), freakNpc)
    }

    fun teleport(player: Player) {
        if (getAttribute(player, freakPreviousLoc,null) == null) {
            setAttribute(player, freakPreviousLoc, player.location)
        }
        teleport(player, Location.create(2599, 4777 ,0))
    }

    fun cleanup(player: Player) {
        player.locks.unlockTeleport()
        player.properties.teleportLocation = getAttribute(player,freakPreviousLoc, ServerConstants.HOME_LOCATION)
        removeAttributes(player, freakPreviousLoc, freakTask, freakComplete, pheasantKilled)
        removeAll(player, Items.RAW_PHEASANT_6178)
        removeAll(player, Items.RAW_PHEASANT_6178, Container.BANK)
        removeAll(player, Items.RAW_PHEASANT_6179)
        removeAll(player, Items.RAW_PHEASANT_6179, Container.BANK)
    }

    fun reward(player: Player){
        val hasHat = hasAnItem(player, Items.LEDERHOSEN_HAT_6182).container != null
        val hasTop = hasAnItem(player, Items.LEDERHOSEN_TOP_6180).container != null
        val hasShort = hasAnItem(player, Items.LEDERHOSEN_SHORTS_6181).container != null
        sendNPCDialogue(player, freakNpc, "You get a lederhosen item as a reward for your help, many thanks!")
        when{
            (!hasHat) -> addItemOrDrop(player, Items.LEDERHOSEN_HAT_6182, 1)
            (!hasTop) -> addItemOrDrop(player, Items.LEDERHOSEN_TOP_6180, 1)
            (!hasShort) -> addItemOrDrop(player, Items.LEDERHOSEN_SHORTS_6181, 1)
            else ->{
                sendNPCDialogue(player, freakNpc, "You get some money for your help, many thanks!")
                addItemOrDrop(player, Items.COINS_995, 500)
            }
        }
    }
}