package content.global.skill.cooking

import core.api.*
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import org.rs09.consts.Items

/**
 * This deals with all tea mixtures
 * cup of tea
 * nettle tea
 * nettle water
 * nettle tea (cup)
 * poh tea
 * clay
 * porcelain
 * gold trim
 * Flask
 */
class TeaInteraction : InteractionListener {
    private val teaMilkMap = mapOf(
        Items.NETTLE_TEA_4239 to Items.NETTLE_TEA_4240,
        Items.CUP_OF_TEA_4242 to Items.CUP_OF_TEA_4243,
        Items.CUP_OF_TEA_4245 to Items.CUP_OF_TEA_4246,
        Items.CUP_OF_TEA_7730 to Items.CUP_OF_TEA_7731,
        Items.CUP_OF_TEA_7733 to Items.CUP_OF_TEA_7734,
        Items.CUP_OF_TEA_7736 to Items.CUP_OF_TEA_7737,
    )

    private val teaCupMap = mapOf(
        Items.NETTLE_TEA_4239 to Items.CUP_OF_TEA_4242,
        Items.NETTLE_TEA_4240 to Items.CUP_OF_TEA_4243,
        Items.BOWL_OF_HOT_WATER_4456 to Items.CUP_OF_HOT_WATER_4460
    )

    // This is specifically for the porcelain cup without a handle for Ghost Ahoy
    private val teaCupPorcelainMap = mapOf(
        Items.NETTLE_TEA_4239 to Items.CUP_OF_TEA_4245,
        Items.NETTLE_TEA_4240 to Items.CUP_OF_TEA_4246
    )

    private val emptyTeaPot = intArrayOf(
        Items.TEAPOT_7702,
        Items.TEAPOT_7714,
        Items.TEAPOT_7726
    )

    private val leafTeaPot = intArrayOf(
        Items.TEAPOT_WITH_LEAVES_7700,
        Items.TEAPOT_WITH_LEAVES_7712,
        Items.TEAPOT_WITH_LEAVES_7724
    )

    private val filledTeaPot = intArrayOf(
        Items.POT_OF_TEA_4_7692,
        Items.POT_OF_TEA_3_7694,
        Items.POT_OF_TEA_2_7696,
        Items.POT_OF_TEA_1_7698,
        Items.POT_OF_TEA_4_7704,
        Items.POT_OF_TEA_3_7706,
        Items.POT_OF_TEA_2_7708,
        Items.POT_OF_TEA_1_7710,
        Items.POT_OF_TEA_4_7716,
        Items.POT_OF_TEA_3_7718,
        Items.POT_OF_TEA_2_7720,
        Items.POT_OF_TEA_1_7722,
    )

    private val emptyBuilderCup = intArrayOf(
        Items.EMPTY_CUP_7728,
        Items.PORCELAIN_CUP_7732,
        Items.PORCELAIN_CUP_7735
    )

    override fun defineListeners() {
        onUseWith(ITEM, teaMilkMap.keys.toIntArray(), Items.BUCKET_OF_MILK_1927){ player, used, with ->
            if (removeItem(player, used) && removeItem(player, with)){
                addItem(player, teaMilkMap[used.id]!!)
                addItem(player, Items.BUCKET_1925)
            }
            return@onUseWith true
        }

        onUseWith(ITEM, teaCupMap.keys.toIntArray(), Items.EMPTY_CUP_1980){ player, used, with ->
            if (used.id != Items.BOWL_OF_HOT_WATER_4456){
                if (getDynLevel(player, Skills.COOKING) < 20){
                    sendDialogue(player, "You need a cooking level of 20 to make tea.")
                    return@onUseWith false
                }
            }
            if (removeItem(player, used) && removeItem(player, with)) {
                addItem(player, teaCupMap[used.id]!!)
                addItem(player, Items.BOWL_1923)
            }
            return@onUseWith true
        }

        onUseWith(ITEM, Items.BOWL_OF_WATER_1921, Items.NETTLES_4241) { player, used, with ->
            if (removeItem(player, used) && removeItem(player, with)){
                addItem(player, Items.NETTLE_WATER_4237)
            }
            return@onUseWith true
        }

        onUseWith(ITEM, teaCupPorcelainMap.keys.toIntArray(), Items.PORCELAIN_CUP_4244){player, used, with ->
            if (getDynLevel(player, Skills.COOKING) < 20){
                sendDialogue(player, "You need a cooking level of 20 to make tea.")
                return@onUseWith false
            }

            if(removeItem(player, used) && removeItem(player, with)){
                addItem(player, teaCupPorcelainMap[used.id]!!)
                addItem(player, Items.BOWL_1923)
            }
            return@onUseWith true
        }

        onUseWith(ITEM, emptyTeaPot, Items.TEA_LEAVES_7738) { player, used, with ->
            if (removeItem(player, used) && removeItem(player, with)){
                addItem(player, used.id - 2)
            }
            return@onUseWith true
        }

        onUseWith(ITEM, leafTeaPot, Items.HOT_KETTLE_7691) { player, used, with ->
            if (getDynLevel(player, Skills.COOKING) < 20){
                sendDialogue(player,"You need to be level 20 to make tea.")
                return@onUseWith false
            }
            if (removeItem(player, used) && removeItem(player, with)){
                addItem(player, Items.KETTLE_7688)
                addItem(player, used.id - 8)
                rewardXP(player, Skills.COOKING, 53.0)

            }
            return@onUseWith true
        }

        onUseWith(ITEM, filledTeaPot, *emptyBuilderCup) { player, used, with ->
            return@onUseWith fillBuildersTea(player, used, with)
        }
    }

    private fun fillBuildersTea(player: Player, used: Node, with: Node) : Boolean {
        if (removeItem(player, used) && removeItem(player, with)){
            // Thanks tea pot with tea leaves
            if (used.id + 4 in emptyTeaPot){
                addItem(player, used.id + 4)
            }
            else{
                addItem(player, used.id + 2)
            }
            // Why do these cups have a noted form!
            if (with.id == Items.EMPTY_CUP_7728){
                addItem(player, with.id + 2)
            }
            else{
                addItem(player, with.id + 1)
            }
            return true
        }
        return false
    }
}
