package content.global.handlers.item.withnpc

import core.api.removeItem
import core.api.sendChat
import core.api.sendMessage
import content.global.skill.prayer.Bones
import core.game.node.entity.npc.NPC
import core.game.node.item.Item
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import core.game.interaction.InteractionListener
import core.game.interaction.IntType

class BonesOnStrayDog : InteractionListener {
    override fun defineListeners() {
        val bones = Bones.array

        val dogs = intArrayOf(
            NPCs.STRAY_DOG_4766,
            NPCs.STRAY_DOG_4767,
            NPCs.STRAY_DOG_5917,
            NPCs.STRAY_DOG_5918
        )

        onUseWith(IntType.NPC, bones, *dogs) { player, used, with ->
            used as Item; with as NPC

            var woof = "Woof"

            if (removeItem(player, used)) {
                sendMessage(
                    player,
                    "You feed the ${with.definition.name.lowercase()} your ${used.definition.name.lowercase()}."
                )

                when (used.id) {
                    Items.BURNT_BONES_528 -> {
                        sendMessage(
                            player,
                            "The dog looks at you, disappointed, but takes the bones anyway."
                        )

                        woof = "Woof..."
                    }

                    Items.FAYRG_BONES_4830,
                    Items.RAURG_BONES_4832,
                    Items.OURG_BONES_4834 -> {
                        sendMessage(
                            player,
                            "The dog looks at you, confused, but takes the bones anyway."
                        )

                        woof = "Woof..?"
                    }

                    Items.BABYDRAGON_BONES_534,
                    Items.BIG_BONES_532 -> {
                        sendMessage(
                            player,
                            "The dog seems to be very happy."
                        )

                        woof = "Woof!"
                    }

                    Items.WYVERN_BONES_6812,
                    Items.DRAGON_BONES_536 -> {
                        sendMessage(
                            player,
                            "The dog seems to be extremely overjoyed."
                        )

                        woof = "WOOF!"
                    }
                }

                sendChat(with, woof)
            }

            return@onUseWith true
        }
    }
}