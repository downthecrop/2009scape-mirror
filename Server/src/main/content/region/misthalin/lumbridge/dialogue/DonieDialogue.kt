package content.region.misthalin.lumbridge.dialogue

import core.api.*
import core.game.dialogue.*
import core.game.node.entity.player.Player
import core.game.world.GameWorld
import core.game.world.GameWorld.settings
import core.plugin.Initializable
import org.rs09.consts.NPCs

@Initializable
class DonieDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player): DialoguePlugin {
        return DonieDialogue(player)
    }
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, DonieDialogueFile(), npc)
        return false
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DONIE_2238)
    }
}

class DonieDialogueFile : DialogueLabeller() {
    override fun addConversation() {
        assignToIds(NPCs.DONIE_2238)

        npc(ChatAnim.FRIENDLY, "Hello there, can I help you?")
        options(
                DialogueOption("whereami", "Where am I?", expression = ChatAnim.THINKING),
                DialogueOption("howareyou", "How are you today?"),
                DialogueOption("shoelace", "Your shoe lace is untied."),
        )

        label("whereami")
        npc("This is the town of Lumbridge my friend.")

        label("howareyou")
        npc("Aye, not too bad thank you. Lovely weather in", ""+ (GameWorld.settings?.name ?: "2009Scape") +" this fine day.")
        player("Weather?")
        npc("Yes weather, you know.")
        npc("The state or condition of the atmosphere at a time and", "place, with respect to variables such as temperature,", "moisture, wind velocity, and barometric pressure.")
        player("...")
        npc("Not just a pretty face eh? Ha ha ha.")

        label("shoelace")
        npc(ChatAnim.ANGRY, "No it's not!")
        player("No you're right. I have nothing to back that up.")
        npc(ChatAnim.ANGRY, "Fool! Leave me alone!")

    }
}