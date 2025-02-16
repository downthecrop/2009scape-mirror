package content.global.ame.events.drilldemon

import content.global.ame.kidnapPlayer
import content.global.ame.returnPlayer
import core.api.*
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Items
import org.rs09.consts.NPCs

object DrillDemonUtils {
    val DD_KEY_TASK = "/save:drilldemon:task"
    val DD_SIGN_VARP = 531
    val DD_SIGN_JOG = 0
    val DD_SIGN_SITUP = 1
    val DD_SIGN_PUSHUP = 2
    val DD_SIGN_JUMP = 3
    val DD_CORRECT_OFFSET = "/save:drilldemon:offset"
    val DD_CORRECT_COUNTER = "/save:drilldemon:numcorrect"
    val DD_AREA = ZoneBorders(3158, 4817, 3168, 4823)
    val DD_NPC = NPCs.SERGEANT_DAMIEN_2790

    fun teleport(player: Player) {
        kidnapPlayer(player, Location.create(3163, 4819, 0), TeleportManager.TeleportType.INSTANT)
        player.interfaceManager.closeDefaultTabs()
        setComponentVisibility(player, 548, 69, true)
        setComponentVisibility(player, 746, 12, true)
    }

    fun changeSignsAndAssignTask(player: Player) {
        setVarp(player, DD_SIGN_VARP, 0)
        val tempList = arrayListOf(DD_SIGN_JOG, DD_SIGN_JUMP, DD_SIGN_PUSHUP, DD_SIGN_SITUP).shuffled().toMutableList()
        val tempOffsetList = arrayListOf(1335, 1336, 1337, 1338).shuffled().toMutableList()
        val task = tempList.random()
        val taskOffset = tempOffsetList.random()

        setAttribute(player, DD_KEY_TASK, task)
        setAttribute(player, DD_CORRECT_OFFSET, taskOffset)

        tempList.remove(task)
        tempOffsetList.remove(taskOffset)
        setVarbit(player, taskOffset, task)
        for (i in 0 until tempList.size) {
            setVarbit(player, tempOffsetList[i], tempList[i], true)
        }
    }

    fun getVarbitForId(id: Int): Int {
        return when (id) {
            10076 -> 1335
            10077 -> 1336
            10078 -> 1337
            10079 -> 1338
            else -> 0
        }
    }

    fun getMatTask(id: Int, player: Player): Int {
        return getVarbit(player, getVarbitForId(id))
    }

    fun cleanup(player: Player) {
        returnPlayer(player)
        removeAttributes(player, DD_KEY_TASK, DD_CORRECT_OFFSET, DD_CORRECT_COUNTER)
        player.interfaceManager.openDefaultTabs()
        setComponentVisibility(player, 548, 69, false)
        setComponentVisibility(player, 746, 12, false)
    }

    fun animationForTask(task: Int): Animation {
        return when (task) {
            DD_SIGN_SITUP -> Animation(2763)
            DD_SIGN_PUSHUP -> Animation(2762)
            DD_SIGN_JUMP -> Animation(2761)
            DD_SIGN_JOG -> Animation(2764)
            else -> Animation(-1)
        }
    }

    fun reward(player: Player) {
        queueScript(player, 2, QueueStrength.SOFT) {
            val hasHat = hasAnItem(player, Items.CAMO_HELMET_6656).container != null
            val hasShirt = hasAnItem(player, Items.CAMO_TOP_6654).container != null
            val hasPants = hasAnItem(player, Items.CAMO_BOTTOMS_6655).container != null
            when {
                !hasHat -> addItemOrDrop(player, Items.CAMO_HELMET_6656)
                !hasShirt -> addItemOrDrop(player, Items.CAMO_TOP_6654)
                !hasPants -> addItemOrDrop(player, Items.CAMO_BOTTOMS_6655)
                else -> addItemOrDrop(player, Items.COINS_995, 500)
            }
            return@queueScript stopExecuting(player)
        }

    }
}
