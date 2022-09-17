package rs09.game.interaction.item.withnpc

import api.*
import core.game.content.global.Bones
import core.game.content.global.Meat
import core.game.content.global.MeatState
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Animations
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.IntType

class FeedFaladorSheepdog : InteractionListener {
    companion object {
        private const val SHEEP_DOG_NPC = NPCs.SHEEPDOG_2311
        private val FEED_ANIMATION = Animation(Animations.HUMAN_BURYING_BONES_827)
        private val CONSUMABLE_BONES = Bones.array
            .filter { it != Items.BURNT_BONES_528 }
            .toHashSet()
        private val CONSUMABLE_MEATS = Meat.values()
            .filter { it.state == MeatState.INEDIBLE_RAW || it.state == MeatState.EDIBLE_COOKED }
            .map { it.id }
            .toHashSet()
    }

    override fun defineListeners() {
        onUseAnyWith(IntType.NPC, SHEEP_DOG_NPC) { player, used, with ->
            if (CONSUMABLE_BONES.contains(used.id)) {
                if (!removeItem(player, used.asItem())) {
                    return@onUseAnyWith true
                }
                sendDialogue(player, "You give the dog some nice ${used.name.toLowerCase()}. It happily gnaws on them.")
            } else if (CONSUMABLE_MEATS.contains(used.id)) {
                if (!removeItem(player, used.asItem())) {
                    return@onUseAnyWith true
                }
                sendDialogue(player, "You give the dog a nice piece of meat. It gobbles it up.")
            } else {
                sendMessage(player, "The dog doesn't seem interested in that.")
                sendChat(with.asNpc(), "Grrrr!")
                return@onUseAnyWith true
            }

            animate(player, FEED_ANIMATION)
            sendChat(with.asNpc(), "Woof woof!")

            return@onUseAnyWith true
        }
    }
}