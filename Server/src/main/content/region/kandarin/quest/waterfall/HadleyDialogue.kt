package content.region.kandarin.quest.waterfall

import core.ServerConstants
import core.api.*
import core.game.dialogue.ChatAnim
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialogueOption
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import org.rs09.consts.Items
import org.rs09.consts.NPCs

class HadleyDialogue : InteractionListener {
    override fun defineListeners() {
        on(NPCs.HADLEY_302, IntType.NPC, "talk-to") { player, node ->
            DialogueLabeller.open(player, HadleyDialogueFile(), node as NPC)
            return@on true
        }
    }
}

class HadleyDialogueFile : DialogueLabeller() {
    override fun addConversation() {
        val gender = if (player!!.isMale) "Sir" else "Lady"

        player(ChatAnim.NEUTRAL, "Hello there.")
        exec { player, _ ->
            if (inInventory(player, Items.BOOK_ON_BAXTORIAN_292)) {
                loadLabel(player, "book")
            } else {
                loadLabel(player, "no_book")
            }
        }

        label("book")
            npc(ChatAnim.HALF_GUILTY, "I hope you're enjoying your stay, there should be lots", "of useful information in that book: places to go, people to", "see.")

        label("no_book")
            npc(ChatAnim.NEUTRAL_FAST, "Are you on holiday? If so you've come to the right", "place. I'm Hadley the tourist guide, anything you need",
                "to know just ask me " /*sic*/+ "we have some of the most unspoilt", "wildlife and scenery in ${ServerConstants.SERVER_NAME}.")
            npc(ChatAnim.NEUTRAL, "People come from miles around to fish in the clear lakes", "or to wander the beautiful hillsides")
            player(ChatAnim.NEUTRAL, "It is quite pretty.")
            npc(ChatAnim.NEUTRAL_FAST, "Surely pretty is an understatement kind $gender.", "Beautiful, amazing or possibly life-changing would be",
                "more suitable wording. Have you seen the Baxtorian", "waterfall? Named after the elf king who was buried")
            npc(ChatAnim.NEUTRAL, "beneath.")
            options(
                DialogueOption("elf_king", "Can you tell me what happened to the elf king?", expression = ChatAnim.NEUTRAL),
                DialogueOption("visiting", "Where else is worth visiting around here?", expression = ChatAnim.NEUTRAL),
                DialogueOption("treasure", "Is there treasure under the waterfall?", expression = ChatAnim.NEUTRAL),
                DialogueOption("bye", "Thanks then, goodbye.", expression = ChatAnim.NEUTRAL),
            )

        label("elf_king")
            npc(ChatAnim.NEUTRAL_FAST, "There are many myths about Baxtorian. One popular", "story is that after defending his kingdom against the",
                "invading dark forces from the west, Baxtorian returned", "to find his wife Glarial had been captured by the")
            npc(ChatAnim.NEUTRAL, "enemy!")
            npc(ChatAnim.NEUTRAL_FAST, "This destroyed Baxtorian, after years of searching he", "became a recluse. In the secret home he had made for",
                "Glarial under the waterfall, he never came out and it is", "told that only Glarial could enter.")
            player(ChatAnim.NEUTRAL, "What happened to him?")
            npc(ChatAnim.NEUTRAL, "Oh, I don't know. I believe we have some pages on him", "upstairs in our archives. If you wish to look at them",
                "please be careful, they're all pretty delicate.")
            options(
                DialogueOption("visiting", "Where else is worth visiting around here?", expression = ChatAnim.NEUTRAL),
                DialogueOption("treasure", "Is there treasure under the waterfall?", expression = ChatAnim.NEUTRAL),
                DialogueOption("bye", "Thanks then, goodbye.", expression = ChatAnim.NEUTRAL),
            )

        label("visiting")
            npc(ChatAnim.NEUTRAL, "There is a lovely spot for a picnic on the hill to the", "north east, there lies a monument to the deceased elven",
                "queen Glarial. It really is quite pretty.")
            player(ChatAnim.NEUTRAL, "Who was queen Glarial?")
            npc(ChatAnim.NEUTRAL, "Baxtorian's wife, the only person who could also enter", "the waterfall. She was queen when this land was",
                "inhabited by elven kind. Glarial was kidnapped while")
            npc(ChatAnim.NEUTRAL, "Baxtorian was away, but they eventually recovered her", "body and brought her home to rest.")
            player(ChatAnim.SAD, "That's sad.")
            npc(ChatAnim.NEUTRAL, "True, I believe there's some information about her", "upstairs, if you look at them please be careful.")
            options(
                DialogueOption("elf_king", "Can you tell me what happened to the elf king?", expression = ChatAnim.NEUTRAL),
                DialogueOption("treasure", "Is there treasure under the waterfall?", expression = ChatAnim.NEUTRAL),
                DialogueOption("bye", "Thanks then, goodbye.", expression = ChatAnim.NEUTRAL),
            )

        label("treasure")
            npc(ChatAnim.NEUTRAL, "Ha ha... another treasure hunter. Well if there is " /*sic*/+ "no", "one's been able to get to it. They've been searching that",
                "river for decades, all to no avail.")
            options(
                DialogueOption("elf_king", "Can you tell me what happened to the elf king?", expression = ChatAnim.NEUTRAL),
                DialogueOption("visiting", "Where else is worth visiting around here?", expression = ChatAnim.NEUTRAL),
                DialogueOption("bye", "Thanks then, goodbye.", expression = ChatAnim.NEUTRAL),
            )

        label("bye")
            npc(ChatAnim.NEUTRAL, "Enjoy your visit.")

    }
}