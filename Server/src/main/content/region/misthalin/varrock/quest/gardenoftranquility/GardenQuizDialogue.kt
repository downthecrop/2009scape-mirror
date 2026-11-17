package content.region.misthalin.varrock.quest.gardenoftranquility

import content.data.Quests
import content.region.misthalin.varrock.quest.gardenoftranquility.GardenQuizInterface.Companion.CORRECT_ANSWER_COUNT
import core.api.*
import core.game.dialogue.ChatAnim
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialogueOption
import core.game.node.item.Item
import org.rs09.consts.Items

class GardenQuizDialogueFile(private val passedQuiz: Boolean) : DialogueLabeller() {

    override fun addConversation() {
        exec { player, _ ->
            CORRECT_ANSWER_COUNT = 0
            loadLabel(player, "start")
        }

        label("start")
        player(ChatAnim.ASKING, "Okay, I've answered your questions - what's the verdict?")
        npc("Mmmm, it was close but...")

        exec { player, _ ->
            if (passedQuiz) {
                loadLabel(player, "passed")
            } else {
                loadLabel(player, "failed")
            }
        }

        label("failed")
        npc("You clearly do not have what it takes to use the Ring of Charos.")
        goto("end")

        label("passed")
        npc(ChatAnim.HAPPY, "Congratulations! You have clearly got what it takes to use the Ring of Charos. Wait a moment, @name...")
        exec { player, _ ->
            // Activate the ring.
            if (removeItem(player, Items.RING_OF_CHAROS_4202, container = Container.INVENTORY)) {
                addItemOrDrop(player, Items.RING_OF_CHAROSA_6465)
                loadLabel(player, "ring_inventory")
            }

            if (removeItem(player, Items.RING_OF_CHAROS_4202, container = Container.EQUIPMENT)) {
                replaceSlot(player, EquipmentSlot.RING.ordinal, Items.RING_OF_CHAROSA_6465.asItem(), container = Container.EQUIPMENT)
                loadLabel(player, "ring_equipped")
            }

            setQuestStage(player, Quests.GARDEN_OF_TRANQUILITY, 40)
        }

        label("ring_equipped")
        item(Item(Items.RING_OF_CHAROSA_6465), "The wise old man passes his hand slowly over the Ring of Charos that encircles your ring-finger. Gradually, you feel the ring begin to vibrate with gentle energy.")
        goto("post_ring")

        label("ring_inventory")
        item(Item(Items.RING_OF_CHAROSA_6465), "The wise old man passes his hand slowly over the Ring of Charos that lies in your outstretched palm. Gradually, you feel the ring begin to vibrate with gentle energy.")
        goto("post_ring")

        label("post_ring")
        npc("The Ring of Charos has now been opened to you, @name. You may use its power to charm the weak-minded, but it will only work whilst you are wearing it.")
        options(
            DialogueOption("notcharming", "Thanks for your help, Wise Old Man."),
            DialogueOption("junk_check", "Could you check my things for junk, please?"),
            DialogueOption("lore_start", "I'd just like to ask you something."),
            DialogueOption("charmed", "[Charm] I think you wanted to give me a thousand coins...", "I think you wanted to give me a thousand coins...",
                optionIf = { player, _ -> inEquipment(player, Items.RING_OF_CHAROSA_6465) }
            )
        )

        label("notcharming")
        npc("Funny, I thought you would take the chance to test the power of the ring on me! No matter - leave me be now, I have important surveillance duties to carry out.")
        goto("end")

        label("charmed")
        npc("Ha ha! I thought you might try that!! But remember, @name - the power of the Ring of Charos will not work with everybody, especially not a wise old bird like me!")
        goto("end")
    }
}