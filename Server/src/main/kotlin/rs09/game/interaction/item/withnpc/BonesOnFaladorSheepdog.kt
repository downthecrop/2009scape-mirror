package rs09.game.interaction.item.withnpc

import api.removeItem
import api.sendChat
import api.sendDialogue
import core.game.content.global.Bones
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Animations
import org.rs09.consts.NPCs
import rs09.game.interaction.InteractionListener

class BonesOnFaladorSheepdog : InteractionListener {
    companion object {
        private const val SHEEP_DOG_NPC = NPCs.SHEEPDOG_2311
        private val BONES = Bones.array
        private val BONE_BURY_ANIMATION = Animation(Animations.HUMAN_BURYING_BONES_827)
    }

    override fun defineListeners() {
        onUseWith(NPC, BONES, SHEEP_DOG_NPC) { player, used, with ->
            if (removeItem(player, used.asItem())) {
                player.animate(BONE_BURY_ANIMATION)
                sendDialogue(player, "You give the dog some nice ${used.name.toLowerCase()}. It happily gnaws on them.")
                sendChat(with.asNpc(), "Woof woof!")
            }
            return@onUseWith true
        }
    }
}