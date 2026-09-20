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
 * Wizard Acantha of the Runecrafting Guild
 */

class WizardAcanthaDialogue : InteractionListener {
    override fun defineListeners() {
        on(intArrayOf(NPCs.WIZARD_ACANTHA_8031), IntType.NPC, "talk-to") { player, node ->
            DialogueLabeller.open(player, WizardAcanthaDialogueFile(), node as NPC)
            return@on true
        }
    }
}

class WizardAcanthaDialogueFile : DialogueLabeller() {
    override fun addConversation() {
        npc("You look slightly more intelligent than the rest of these goons. Will you help me?")
        options(
            DialogueOption("check", "Yes, I'll help."),
            DialogueOption("no", "No."),
            DialogueOption("wat", "Help with what?"),
        )

        label("check")
        exec { player, _ ->
            if (joinLobby(player, Team.GREEN)) {
                loadLabel(player, "yes")
            } else {
                loadLabel(player, "end")
            }
        }

        label("end")
        goto("nowhere")

        label("yes")
        npc("I knew you were an intelligent ${if (player!!.isMale) "lad" else "lass"}. Here are your wands. When each team has enough people, Wizard Elriss will open the portal and you can get started. While we wait, shall I tell you what you need to do?")
        options(
            DialogueOption("rules", "No. What am I supposed to do?"),
            DialogueOption("i know it all", "Yes, I know what to do."),
        )

        label("rules")
        npc("When we began formally studying the rune altars, we saw a spike in the energy fields surrounding altars when there is an increase in rune production.")
        npc("When an essence is used on the altar, energy is transferred and bound into the essence, creating a rune. As with all transfers of energy, some is lost in the process.")
        npc("This lost energy, manifested as green orbs, needs to be returned to the altar to keep them well maintained.")
        npc("When the green energy escapes during the transfer, yellow energy particles in the air gather around the pure green energy.")
        npc("They form into opposing orbs and must be kept away as they are impure energy forms. I need you to put the green orbs back in the altar.")
        npc("I will give you two wands to move the orbs around and a third wand to create barriers to prevent those pesky yellow orbs from getting in.")
        npc("To further this cause, we've set up a project to repair the altars.")
        npc("I wanted to just call it The Orb Project, but the buffoon wanted it to sound grander, so now it's The Great Orb Project. As if it needed an adjective.")
        goto("zone out")

        label("i know it all")
        npc("Repetition is good for understanding. Tell me what you know, ${if (player!!.isMale) "Mr." else "Ms."} Smarty Pants.")
        goto("zone out")

        label("no")
        npc("Humph. The youth today have no sense of adventure.")
        goto("nowhere")

        label("wat")
        npc("Don't you keep up with the Runecrafting Theory Journal? We've been making major discoveries! The youth of today are so lazy. Shall I tell you about it?")
        player("Sure. What am I supposed to do?")
        goto("rules")

        label("zone out")
        options(
            DialogueOption("green", "Green orbs into the altar, yellow orbs away."),
            DialogueOption("yellow", "Yellow orbs into altar, green orbs away."),
            DialogueOption("rewards", "So, what do I get for all this hard work?"),
            DialogueOption("good and evil", "How can you be sure about which orb is good?"),
        )

        label("green")
        npc("You won't be winning any awards for verbosity, but you got the gist of it.")
        goto("nowhere")

        label("yellow")
        npc("Read my lips, whippersnapper!","NO YELLOW ORBS IN THE ALTAR.")
        player("Eek! Okay, okay. Green orbs into the altar, yellow orbs away.")
        goto("nowhere")

        label("rewards")
        npc("Aaaah, a true mercenary. Although we cannot agree scientifically, Vief and I have decided to reward those who help us.")
        npc("If you get more green orbs into the altar than the yellow team gets yellow orbs, you win the altar. You will receive rune essence each round you win.")
        npc("The team who winds the most altars will receive tokens which you can give to Wizard Elriss in exchange for a reward. I don't know the exact details, so it's best to speak to her.")
        goto("nowhere")

        label("good and evil")
        npc("Do you think I got to where I am today on false calculations? I researched it, that's how I know! There was a time when my opinion was respected and accepted without question.")
        npc("Age steals that from you. I will not explain my findings to fools. If you want to see how I reached my conclusions, you should keep up with the scientific wizarding community.")
        npc("I have no time for laziness! Wizard Vief, in an attempt to progress his career, has decided to disagree with my findings and cast doubt upon my ability as a wizard.")
        npc("What better way to attract attention to one's self than disagree with those more knowledgeable? Pay no attention to that man, he's a buffoon.")
        goto("nowhere")
    }
}