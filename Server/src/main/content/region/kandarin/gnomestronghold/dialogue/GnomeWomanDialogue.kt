package content.region.kandarin.gnomestronghold.dialogue

import core.api.addItemOrDrop
import core.api.openDialogue
import core.game.dialogue.ChatAnim
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialoguePlugin
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs

/**
 * Handled the dialogue for the Gnome Woman NPCs in the Tree Gnome Stronghold.
 * @author Broseki
 */
@Initializable
class GnomeWomanDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player?): DialoguePlugin {
        return GnomeWomanDialogue(player)
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        // Pick a random number between 0 - 5 to determine the dialogue tree to use
        val randomDialogue = (0..5).random()

        when (randomDialogue) {
            0 -> openDialogue(player, GnomeWomanDialogueFile0(), npc)
            1 -> openDialogue(player, GnomeWomanDialogueFile1(), npc)
            2 -> openDialogue(player, GnomeWomanDialogueFile2(), npc)
            3 -> openDialogue(player, GnomeWomanDialogueFile3(), npc)
            4 -> openDialogue(player, GnomeWomanDialogueFile4(), npc)
            else -> openDialogue(player, GnomeWomanDialogueFile5(), npc)
        }
        return false
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GNOME_WOMAN_168, NPCs.GNOME_WOMAN_169)
    }
}

class GnomeWomanDialogueFile0 : DialogueLabeller() {
    override fun addConversation() {
        player(ChatAnim.FRIENDLY, "Hello.")
        npc(ChatAnim.OLD_HAPPY, "Hello adventurer. Here are some wise words:")
        player(ChatAnim.FRIENDLY, "OK.")
        npc(ChatAnim.OLD_CALM_TALK1, "Happiness is inward and not outward. So it does not depend on what we have but on what we are!")
    }
}

class GnomeWomanDialogueFile1 : DialogueLabeller() {
    override fun addConversation() {
        player(ChatAnim.FRIENDLY, "Hello.")
        npc(ChatAnim.OLD_HAPPY, "Hi. I've never seen so many humans in my life.")
        player(ChatAnim.LAUGH, "I've never seen so many gnomes!")
        npc(ChatAnim.OLD_LAUGH1, "So we're both learning.")
    }
}

class GnomeWomanDialogueFile2 : DialogueLabeller() {
    override fun addConversation() {
        player(ChatAnim.FRIENDLY, "Hello.")
        npc(ChatAnim.OLD_HAPPY, "Hello traveller. Are you eating properly? You look tired.")
        player(ChatAnim.FRIENDLY, "I think so.")
        npc(ChatAnim.OLD_CALM_TALK1, "Here, get this worm down you. It'll do you the world of good.")
        item(Item(Items.KING_WORM_2162), "The gnome gives you a worm.")
        exec { player, npc ->
            addItemOrDrop(player, Items.KING_WORM_2162)
        }
        player(ChatAnim.HAPPY, "Thanks!")
    }
}

class GnomeWomanDialogueFile3 : DialogueLabeller() {
    override fun addConversation() {
        player(ChatAnim.FRIENDLY, "Hello.")
        npc(ChatAnim.OLD_HAPPY, "Well good day to you kind sir. Are you new to these parts?")
        player(ChatAnim.FRIENDLY
            , "Kind of.")
        npc(ChatAnim.OLD_HAPPY, "Well if you're looking for a good night out: Blurberry's cocktail bar is great!")
    }
}

class GnomeWomanDialogueFile4 : DialogueLabeller() {
    override fun addConversation() {
        player(ChatAnim.FRIENDLY, "Hello.")
        npc(ChatAnim.OLD_CALM_TALK1, "Some people grumble because roses have thorns. I'm thankful that thorns have roses!")
        player(ChatAnim.HAPPY, "Good attitude!")
    }
}

class GnomeWomanDialogueFile5 : DialogueLabeller() {
    override fun addConversation() {
        player(ChatAnim.FRIENDLY, "Hello.")
        player(ChatAnim.FRIENDLY, "How are you?")
        npc(ChatAnim.OLD_HAPPY, "Not bad, a little worn out.")
        player(ChatAnim.FRIENDLY, "Maybe you should have a lie down.")
        npc(ChatAnim.OLD_HAPPY, "With three kids to feed I've no time for naps!")
        player(ChatAnim.HAPPY, "Sounds like hard work!")
        npc(ChatAnim.OLD_HAPPY, "It is but they're worth it.")
    }
}
