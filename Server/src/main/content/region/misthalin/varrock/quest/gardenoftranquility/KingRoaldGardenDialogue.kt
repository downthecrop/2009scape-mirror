package content.region.misthalin.varrock.quest.gardenoftranquility

import core.api.inEquipment
import core.game.dialogue.ChatAnim
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialogueOption
import core.game.node.entity.player.Player
import org.rs09.consts.Items

/**
 * King Roald during Garden of Tranquility.
 */

class KingRoaldGardenDialogue(player: Player? = null) : DialogueLabeller() {

    override fun addConversation() {
        player(ChatAnim.ASKING, "Hello there. Would you like to follow me for a minute?")
        npc(ChatAnim.ANGRY, "Follow you?!? I am the King! I am the followed, not the follower!!")
        exec { player, _ ->
            if (inEquipment(player, Items.RING_OF_CHAROSA_6465)) {
                loadLabel(player, "charos_options")
            } else {
                loadLabel(player, "argumentative_path")
            }
        }

        label("charos_options")
        options(
            DialogueOption("argumentative_path", "Well, if you can't be bothered then neither can I.", skipPlayer = true),
            DialogueOption("charm_path", "[Charm] Of course, your majesty - please forgive me.",
                spokenText = "Of course, your majesty - please forgive me.")
        )

        label("argumentative_path")
        player(ChatAnim.NEUTRAL, "Well, if you can't be bothered then neither can I.")
        npc("Suit yourself.")
        player("No, you suit yourself.")
        npc("You're quite argumentative for a peasant.")
        player("I'm not the one who's arguing, you are.")
        npc("Then why do you keep answering me back?")
        player("No, you're answering me back. Well, stop talking and I'll stop answering.")
        npc("No, you stop talking.")
        player("I have stopped talking.")
        npc("No you haven't, you're doing it now.")
        player("No, I'm just answering what you're saying.")
        npc("Okay, so if I don't say anything, you won't answer me back.")
        player("Suits me.")
        npc("Go on, then.")
        player("No, you first.")
        line("King Roald doesn't say anything to you.")
        line("You don't say anything back.")
        npc("See, that's better.")
        player("Yes, much better.")
        npc("What were you saying about me following you somewhere?")
        player("I can't remember, it was such a long time ago.")
        npc("Ah, that's a shame - I could have done with a breath of fresh air.")
        player("Oh well.")
        npc("Yes, never mind.")
        player("See you around.")
        npc("Yes, see you later.")
        npc("I thought you were going.")
        player("I was until you spoke to me again.")
        npc("I'm sorry, I'll stop speaking to you then.")
        player("See you around.")
        npc("Yes, see you later.")
        goto("end")

        label("charm_path")
        npc(ChatAnim.NEUTRAL, "Forgiveness is something that cannot be earned so easily. And I'm certainly not following you off on some wild goose chase when I have important matters of state to attend to.")
        options(
            DialogueOption("fed_up", "That's it - I've had it with royalty."),
            DialogueOption("queen_request", "[Charm] The Queen asked me to bring you.",
                spokenText = "The Queen asked me to bring you.")
        )

        label("fed_up")
        npc("And I've had enough of peasantry! Get out of my castle!")
        goto("end")

        label("queen_request")
        npc(ChatAnim.SCARED, "Ellie?")
        npc(ChatAnim.SCARED, "Now that's a different matter. Lead on, peasant - let's see what my darling wife has been up to now.")

        exec { player, _ ->
            GardenEndingCutscene(player).start()
        }
        goto("end")
    }
}