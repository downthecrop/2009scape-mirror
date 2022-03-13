package rs09.game.interaction.item.withobject

import api.*
import core.game.node.entity.player.link.diary.DiaryType
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Items
import rs09.game.interaction.InteractionListener

/**
 * Handles filling most water sources.
 * @author Ceikry
 */
class WaterSourceListener : InteractionListener() {
    //this is ugly!
    private val waterSources = intArrayOf(21355, 16302, 6827, 11661, 24160, 34577, 15936, 15937, 15938, 23920, 35469, 24265, 153, 879, 880, 2864, 6232, 10436, 10437, 10827, 11007, 11759, 21764, 22973, 24161, 24214, 24265, 28662, 30223, 30820, 34579, 36781, 873, 874, 4063, 6151, 8699, 9143, 9684, 10175, 12279, 12974, 13563, 13564, 14868, 14917, 15678, 16704, 16705, 20358, 22715, 24112, 24314, 25729, 25929, 26966, 29105, 33458, 34082, 34411, 34496, 34547, 34566, 35762, 36971, 37154, 37155, 878, 884, 3264, 3305, 3359, 4004, 4005, 6097, 6249, 6549, 8747, 8927, 11793, 12201, 12897, 24166, 26945, 31359, 32023, 32024, 34576, 35671, 40063, 13561, 13563, 13559, 12089)

    private val nonWellableMsg = "If I drop my @ down there, I don't think I'm likely to get it back."
    private val animation = Animation(832)

    override fun defineListeners()
    {
        onUseWith(SCENERY, WaterVessel.getInputs(), *waterSources){player, used, with ->
            val vessel = WaterVessel.forId(used.id) ?: return@onUseWith false

            if(with.name.contains("well", ignoreCase = true) && !vessel.wellable)
            {
                sendMessage(player, formatMsgText(used.name, nonWellableMsg))
                return@onUseWith true
            }

            //ugly achievement code, achievement system sux
            if(vessel == WaterVessel.BUCKET && with.id == 11661)
            {
                if(!player.achievementDiaryManager.getDiary(DiaryType.FALADOR).isComplete(0,7))
                    player.achievementDiaryManager.getDiary(DiaryType.FALADOR).updateTask(player, 0, 7, true)
            }

            player.pulseManager.run(object : Pulse(1){
                override fun pulse(): Boolean {
                    if(removeItem(player, used.id))
                    {
                        animate(player, animation)
                        sendMessage(player, formatMsgText(used.name, vessel.fillMsg))
                        addItemOrDrop(player, vessel.output)
                    }
                    return !vessel.autofill || amountInInventory(player, used.id) == 0
                }
            })

            return@onUseWith true
        }
    }

    private fun formatMsgText(name: String, template: String): String
    {
        val sb = StringBuilder()
        val templateChars = template.toCharArray()
        val nameChars = name.toCharArray()

        for(tc in templateChars)
        {
            if(tc == '@'){
                for(nc in nameChars)
                {
                    if (nc == '(') break
                    else sb.append(nc.toLowerCase())
                }
            }
            else sb.append(tc)
        }

        return sb.toString()
    }

    internal enum class WaterVessel(val inputs: IntArray, val output: Int, val wellable: Boolean = false, val autofill: Boolean = true, val fillMsg: String = "You fill the @.")
    {
        BUCKET(
                inputs = intArrayOf(Items.BUCKET_1925),
                output = Items.BUCKET_OF_WATER_1929,
                wellable = true
        ),
        VIAL(
                inputs = intArrayOf(Items.VIAL_229),
                output = Items.VIAL_OF_WATER_227,
        ),
        JUG(
                inputs = intArrayOf(Items.JUG_1935),
                output = Items.JUG_OF_WATER_1937
        ),
        BOWL(
                inputs = intArrayOf(Items.BOWL_1923),
                output = Items.BOWL_OF_WATER_1921
        ),
        WATERING_CAN(
                inputs = intArrayOf(Items.WATERING_CAN_5331,Items.WATERING_CAN1_5333, Items.WATERING_CAN2_5334, Items.WATERING_CAN3_5335, Items.WATERING_CAN4_5336, Items.WATERING_CAN5_5337, Items.WATERING_CAN6_5338, Items.WATERING_CAN7_5339),
                output = Items.WATERING_CAN8_5340
        ),
        WATER_SKIN(
                inputs = intArrayOf(Items.WATERSKIN0_1831, Items.WATERSKIN1_1829, Items.WATERSKIN2_1827, Items.WATERSKIN3_1825),
                output = Items.WATERSKIN4_1823
        ),
        FISHBOWL(
                inputs = intArrayOf(Items.FISHBOWL_6667),
                output = Items.FISHBOWL_6668
        );

        companion object
        {
            //map our input item IDs to their respective WaterVessel entry
            private val itemMap = HashMap<Int, WaterVessel>()

            init {
                for(entry in values())
                {
                    entry.inputs.forEach { itemMap[it] = entry }
                }
            }

            //return a nice list of all the input IDs
            fun getInputs(): IntArray
            {
                return itemMap.keys.toIntArray()
            }

            fun forId(id: Int): WaterVessel?
            {
                return itemMap[id]
            }
        }
    }
}
