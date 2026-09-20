package content.region.misthalin.wiztower.handlers.rcguild

import content.minigame.greatorbproject.OrbProjLobby.joinLobby
import content.minigame.greatorbproject.Team
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialogueOption
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import org.rs09.consts.NPCs

/**
 * Wizard Vief of the Runecrafting Guild
 */

class WizardViefDialogue : InteractionListener {
    override fun defineListeners() {
        on(intArrayOf(NPCs.WIZARD_VIEF_8030), IntType.NPC, "talk-to") { player, node ->
            DialogueLabeller.open(player, WizardViefDialogueFile(), node as NPC)
            return@on true
        }
    }
}

class WizardViefDialogueFile : DialogueLabeller() {
    override fun addConversation() {

        npc("Ah! You'll help me, won't you?")
        options(
            DialogueOption("check", "Yes, I'll help."),
            DialogueOption("no", "No."),
            DialogueOption("wat", "Help with what?"),
        )

        label("check")
        exec { player, _ ->
            if (joinLobby(player, Team.YELLOW)) {
                loadLabel(player, "yes")
            } else {
                loadLabel(player, "end")
            }
        }

        label("end")
        goto("nowhere")

        label("yes")
        npc("Excellent. Here are your wands. Wait here and Wizard Elriss will open the portal when there are enough people to start. Now, have I told you what to do yet?")
        options(
            DialogueOption("rules", "No. What am I supposed to do?"),
            DialogueOption("schmules", "Yes, I know what to do."),
        )

        label("no")
        npc("Suit yourself. I'm sure I'll manage without you.")
        goto("nowhere")

        label("wat")
        npc("You must join The Great Orb Project! It's a wonderful name, isn't it? All my idea.")
        npc("Oh, I'm sorry. Haven't I told you what to do yet?")
        options(
            DialogueOption("rules", "No. What am I supposed to do?"),
            DialogueOption("schmules", "Yes, I know what to do."),
        )

        label("rules")
        npc("It's quite complicated, but a runecrafter of your calibre should have no trouble understanding. After all, most of the people they let in here are highly intelligent.")
        npc("The Runecrafting altars are sources of energy. Every time we craft runes, that energy comes out of the altars and into the essence.")
        npc("Sometimes, too much energy comes out, and it forms into yellow energy orbs that hang in the air. These attract another form of energy from the surrounding space, which manifests as green orbs.")
        npc("The yellow orbs need to be put back into the altar. The green orbs should be kept away.")
        npc("You can move the orbs around by attracting and repelling them using special wands. I'll give you a third wand to create barriers.")
        npc("Barriers will stop green orbs from getting to the altar.")
        npc("Yellow orbs are good, green ones are bad. Got that?")
        options(
            DialogueOption("team jacob", "Green good, yellow bad. Got it."),
            DialogueOption("team edward", "Yellow good, green bad. Got it."),
            DialogueOption("rewards", "What's in it for me, anyway?"),
            DialogueOption("u sure?", "How can you be sure you're right?"),
        )

        label("team jacob")
        npc("Weren't you paying attention? Yellow orbs are good, green ones are bad! Move the yellow ones next to the altar and keep the green ones away.")
        npc("Acantha might have told you that the green orbs are good, but she has her calculations the wrong way round. Don't let her fool you!")
        goto("nowhere")

        label("team edward")
        npc("That's right! I can tell that you belong in the Runecrafting Guild.")
        npc("Acantha's apprentices have it the wrong way round. They'll be trying to get the green orbs to the altars and keep the yellow ones away. You mustn't let them!")
        goto("nowhere")

        label("rewards")
        npc("A sensible question. Of course, I know you're busy","and I wouldn't expect you to work for nothing. If you","succeed in getting more yellow orbs into the altar than","the green team gets green orbs, you win the altar.")
        npc("You will be rewarded with rune essence for the capture. Wizard Elriss and I have arranged a token scheme.")
        npc("Win the most altars and I'll give you tokens, and Wizard Elriss can exchange these for rewards. Talk to her for more details about how the tokens are awarded.")
        goto("nowhere")

        label("u sure?")
        npc("I...but...isn't it obvious?")
        npc("You just need to take Wizard Hallam's discoveries about the elemental rune energy signatures and then extrapolate to include all rune types.")
        npc("Then, if you apply a Lefebvre transformation to them... I assume you know the Lefebvre equations?")
        player("Well, I-")
        npc("Of course you do, I shouldn't have asked. Anyway, if you do that, you'll easily see the signature of an external manifestation of Runecrafting energy. Then you need to apply Wizard Beenay's theory of light...")
        line("Wizard Vief continues at length and you don't understand any of it.")
        goto("nowhere")

        label("schmules")
        npc("Excellent.")
        goto("nowhere")
    }
}