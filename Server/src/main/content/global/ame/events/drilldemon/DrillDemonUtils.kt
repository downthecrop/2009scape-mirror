package content.global.ame.events.drilldemon

import core.api.*
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Items
import org.rs09.consts.NPCs

object DrillDemonUtils {
    val DD_KEY_NPC = "drilldemon:npc"
    val DD_KEY_TASK = "drilldemon:task"
    val DD_KEY_RETURN_LOC = "drilldemon:original-loc"
    val DD_SIGN_VARP = 531
    val DD_SIGN_JOG = 0
    val DD_SIGN_SITUP = 1
    val DD_SIGN_PUSHUP = 2
    val DD_SIGN_JUMP = 3
    val DD_CORRECT_OFFSET = "drilldemon:offset"
    val DD_CORRECT_COUNTER = "drilldemon:numcorrect"

    fun teleport(player: Player){
        player.setAttribute(DD_KEY_RETURN_LOC,player.location)
        player.properties.teleportLocation = Location.create(3163, 4819, 0)
        player.interfaceManager.closeDefaultTabs()
        player.packetDispatch.sendInterfaceConfig(548, 69, true)
        player.packetDispatch.sendInterfaceConfig(746, 12, true)

        registerLogoutListener(player, "drilldemon"){p ->
            teleport(p, player.getAttribute(DD_KEY_RETURN_LOC, p.location))
        }

        changeSignsAndAssignTask(player)
    }

    fun changeSignsAndAssignTask(player: Player){
        setVarp(player, DD_SIGN_VARP, 0)
        val tempList = arrayListOf(DD_SIGN_JOG, DD_SIGN_JUMP, DD_SIGN_PUSHUP, DD_SIGN_SITUP).shuffled().toMutableList()
        val tempOffsetList = arrayListOf(1335, 1336, 1337, 1338).shuffled().toMutableList()
        val task = tempList.random()
        val taskOffset = tempOffsetList.random()

        player.setAttribute(DD_KEY_TASK,task)
        player.setAttribute(DD_CORRECT_OFFSET,taskOffset)

        tempList.remove(task)
        tempOffsetList.remove(taskOffset)
        setVarbit(player, taskOffset, task)
        for(i in 0 until tempList.size){
            setVarbit(player, tempOffsetList[i], tempList[i])
        }

        player.dialogueInterpreter.sendDialogues(NPCs.SERGEANT_DAMIEN_2790, core.game.dialogue.FacialExpression.OLD_NORMAL,when(task){
            DD_SIGN_JOG -> "Get over there and jog in place!"
            DD_SIGN_JUMP -> "I need 40 jumping jacks stat!"
            DD_SIGN_PUSHUP -> "Get over there and give me 20 pushups!"
            DD_SIGN_SITUP -> "I need 30 situps pronto!"
            else -> "REEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE"
        })
        player.unlock()
    }

    fun getVarbitForId(id: Int): Int{
        return when(id){
            10076 -> 1335
            10077 -> 1336
            10078 -> 1337
            10079 -> 1338
            else -> 0
        }
    }

    fun getMatTask(id: Int, player: Player): Int{
        return getVarbit(player, getVarbitForId(id))
    }

    fun cleanup(player: Player){
        clearLogoutListener(player, "drilldemon")
        player.properties.teleportLocation = player.getAttribute(DD_KEY_RETURN_LOC)
        player.removeAttribute(DD_KEY_NPC)
        player.removeAttribute(DD_KEY_RETURN_LOC)
        player.removeAttribute(DD_KEY_TASK)
        player.removeAttribute(DD_CORRECT_OFFSET)
        player.removeAttribute(DD_CORRECT_COUNTER)
        player.interfaceManager.openDefaultTabs()
        player.packetDispatch.sendInterfaceConfig(548, 69, false)
        player.packetDispatch.sendInterfaceConfig(746, 12, false)
    }

    fun animationForTask(task: Int): Animation {
        return when(task){
            DD_SIGN_SITUP -> Animation(2763)
            DD_SIGN_PUSHUP -> Animation(2762)
            DD_SIGN_JUMP -> Animation(2761)
            DD_SIGN_JOG -> Animation(2764)
            else -> Animation(-1)
        }
    }

    fun reward(player: Player){
        val hasHat = player.inventory.contains(Items.CAMO_HELMET_6656,1) || player.bank.contains(Items.CAMO_HELMET_6656,1) || player.equipment.contains(Items.CAMO_HELMET_6656,1)
        val hasShirt = player.inventory.contains(Items.CAMO_TOP_6654,1) || player.bank.contains(Items.CAMO_TOP_6654,1) || player.equipment.contains(Items.CAMO_TOP_6654,1)
        val hasPants = player.inventory.contains(Items.CAMO_BOTTOMS_6655,1) || player.bank.contains(Items.CAMO_BOTTOMS_6655,1) || player.equipment.contains(Items.CAMO_BOTTOMS_6655,1)
        val reward = if(!hasHat){
            Item(Items.CAMO_HELMET_6656)
        } else if(!hasShirt){
            Item(Items.CAMO_TOP_6654)
        } else if(!hasPants){
            Item(Items.CAMO_BOTTOMS_6655)
        } else {
            Item(Items.COINS_995, 500)
        }
        if(!player.inventory.add(reward)){
            GroundItemManager.create(reward,player)
        }
    }
}
