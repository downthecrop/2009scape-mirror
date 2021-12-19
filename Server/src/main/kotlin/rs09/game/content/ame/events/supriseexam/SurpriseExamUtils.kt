package rs09.game.content.ame.events.supriseexam

import api.*
import core.game.node.entity.player.Player
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Location
import org.rs09.consts.Components
import org.rs09.consts.Items

object SurpriseExamUtils {

    val SE_KEY_LOC = "supexam:loc"
    val SE_KEY_INDEX = "supexam:index"
    val SE_LOGOUT_KEY = "suprise_exam"
    val SE_DOOR_KEY = "supexam:door"
    val INTER_PATTERN_CHILDS = intArrayOf(6,7,8)
    val INTER_OPTION_CHILDS = intArrayOf(10,11,12,13)
    val SE_DOORS = intArrayOf(2188,2189,2192,2193)
    val INTERFACE = Components.PATTERN_NEXT_103
    val SE_KEY_CORRECT = "supexam:correct"
    val sets = arrayOf(
        intArrayOf(Items.GARDENING_TROWEL_5325,Items.SECATEURS_5329,Items.SEED_DIBBER_5343,Items.RAKE_5341),
        intArrayOf(Items.SALMON_329,Items.SHARK_385,Items.TROUT_333,Items.SHRIMPS_315),
        intArrayOf(Items.BRONZE_SWORD_1277,Items.WOODEN_SHIELD_1171,Items.BRONZE_MED_HELM_1139,Items.ADAMANT_BATTLEAXE_1371),
        intArrayOf(Items.FLY_FISHING_ROD_309,Items.BARBARIAN_ROD_11323,Items.SMALL_FISHING_NET_303,Items.HARPOON_311)
    )

    fun teleport(player: Player){
        player.setAttribute(SE_KEY_LOC,player.location)

        registerLogoutListener(player, SE_LOGOUT_KEY){p ->
            teleport(p, p.getAttribute(SE_KEY_LOC, p.location))
        }

        player.properties.teleportLocation = Location.create(1886, 5025, 0)
    }

    fun cleanup(player: Player){
        player.properties.teleportLocation = player.getAttribute(SE_KEY_LOC,null)
        clearLogoutListener(player, SE_LOGOUT_KEY)
        player.removeAttribute(SE_KEY_LOC)
        player.removeAttribute(SE_KEY_INDEX)
        player.removeAttribute(SE_KEY_CORRECT)
        player.pulseManager.run(object : Pulse(2){
            override fun pulse(): Boolean {
                val reward = Item(Items.BOOK_OF_KNOWLEDGE_11640)
                if(!player.inventory.add(reward)){
                    GroundItemManager.create(reward,player)
                }
                return true
            }
        })
    }

    fun generateInterface(player: Player){
        val set = sets.random()
        val setIndex = sets.indexOf(set)

        val tempList = set.toList().shuffled().toMutableList()
        val correctOpt = tempList.random()
        val optIndex = tempList.indexOf(correctOpt)

        tempList.remove(correctOpt)
        for(i in INTER_PATTERN_CHILDS.indices) player.packetDispatch.sendItemOnInterface(tempList[i],1, INTERFACE, INTER_PATTERN_CHILDS[i])
        for(i in INTER_OPTION_CHILDS.indices){
            if(i == optIndex) player.packetDispatch.sendItemOnInterface(correctOpt,1, INTERFACE, INTER_OPTION_CHILDS[i])
            else player.packetDispatch.sendItemOnInterface(getFalseSet(setIndex)[i],1, INTERFACE, INTER_OPTION_CHILDS[i])
        }
        player.setAttribute(SE_KEY_INDEX,optIndex)
    }

    fun getFalseSet(trueSetIndex: Int): IntArray{
        val tempSets = sets.toMutableList()
        tempSets.removeAt(trueSetIndex)
        return tempSets.random()
    }

    fun pickRandomDoor(player: Player){
        player.setAttribute(SE_DOOR_KEY, SE_DOORS.random())
    }
}