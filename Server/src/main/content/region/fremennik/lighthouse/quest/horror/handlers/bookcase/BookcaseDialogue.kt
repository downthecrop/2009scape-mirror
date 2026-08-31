package content.region.fremennik.lighthouse.quest.horror.handlers.bookcase

import core.api.addItemOrDrop
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialogueOption
import org.rs09.consts.Items

class BookcaseDialogue : DialogueLabeller() {
    override fun addConversation() {
        line("There are three books here that look important...","What would you like to do?")
        options(
            DialogueOption("manual", "Take the Lighthouse Manual", skipPlayer = true),
            DialogueOption("diary", "Take the ancient Diary", skipPlayer = true),
            DialogueOption("journal", "Take Jossik's Journal", skipPlayer = true),
            DialogueOption("all", "Take all three books", skipPlayer = true)
        )

        label("manual")
        exec { player, _ ->
            addItemOrDrop(player, Items.MANUAL_3847)
            end()
        }

        label("diary")
        exec { player, _ ->
            addItemOrDrop(player, Items.DIARY_3846)
            end()
        }

        label("journal")
        exec { player, _ ->
            addItemOrDrop(player, Items.JOURNAL_3845)
            end()
        }

        label("all")
        exec { player, _ ->
            addItemOrDrop(player, Items.MANUAL_3847)
            addItemOrDrop(player, Items.DIARY_3846)
            addItemOrDrop(player, Items.JOURNAL_3845)
            end()
        }
    }
}