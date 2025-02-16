package content.global.skill.cooking.recipe

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import org.rs09.consts.Items

class HangoverRecipe : InteractionListener {

    companion object{
        private const val SNAPE_GRASS = Items.SNAPE_GRASS_231
        private const val HANGOVER_CURE = Items.HANGOVER_CURE_1504
        private const val BUCKET_OF_MILK = Items.BUCKET_OF_MILK_1927
        private const val CHOCOLATE_DUST = Items.CHOCOLATE_DUST_1975
        private const val CHOCOLATE_MILK = Items.CHOCOLATEY_MILK_1977
    }

    override fun defineListeners() {
        onUseWith(IntType.ITEM, CHOCOLATE_DUST, BUCKET_OF_MILK) { player, _, _ ->
            if(hasLevelDyn(player, Skills.COOKING, 4)){
                if(removeItem(player, CHOCOLATE_DUST) and removeItem(player, BUCKET_OF_MILK)){
                    addItem(player, CHOCOLATE_MILK)
                    sendItemDialogue(player, CHOCOLATE_MILK, "You mix the chocolate into the bucket.")
                }
            }
            else {
                sendDialogue(player, "You need a Cooking level of at least 4 to make chocolate milk.")
            }
            return@onUseWith true
        }

        onUseWith(IntType.ITEM, SNAPE_GRASS, CHOCOLATE_MILK) { player, _, _ ->
            if (removeItem(player, SNAPE_GRASS) && removeItem(player, CHOCOLATE_MILK))
            {
                        sendItemDialogue(player, HANGOVER_CURE, "You mix the snape grass into the bucket.")
                        addItem(player, HANGOVER_CURE)
                        return@onUseWith true
            }
            return@onUseWith false
        }

    }
}
