package content.region.misthalin.wiztower.handlers.rcguild

import content.minigame.greatorbproject.OrbProjLobby
import content.minigame.greatorbproject.OrbProjLobby.joinLobby
import content.minigame.greatorbproject.Team
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialogueOption
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import org.rs09.consts.NPCs

/**
 * The wizards of the Runecrafting Guild
 */

class WizardDialogue : InteractionListener {
    override fun defineListeners() {
        on(intArrayOf(
            NPCs.WIZARD_8033, NPCs.WIZARD_8034,
            NPCs.WIZARD_8035, NPCs.WIZARD_8036,
            NPCs.WIZARD_8037, NPCs.WIZARD_8038,
            NPCs.WIZARD_8039, NPCs.WIZARD_8040),
            IntType.NPC, "talk-to") { player, node ->
            DialogueLabeller.open(player, WizardDialogueFile(), node as NPC)
            return@on true
        }
    }
}

class WizardDialogueFile : DialogueLabeller() {
    override fun addConversation() {
        npc("Hello!")
        options(
            DialogueOption("orb", "I want to join the orb project!"),
            DialogueOption("nm", "Never mind.")
        )

        label("orb")
        npc("You can speak to Wizard Acantha or Wizard Vief if you want to join a particular team. Or if you like I could assign you to whichever team has fewer members.")
        options(
            DialogueOption("team", "Please assign me to a team."),
            DialogueOption("wiz", "I'll talk to one of the wizards.")
        )

        label("team")
        exec { player, _ ->
            // join the team with the fewer players, otherwise join yellow because Vief is cooler
            if (OrbProjLobby.greenLobby.size < OrbProjLobby.yellowLobby.size) {
                joinLobby(player, Team.GREEN)
                loadLabel(player, "nm")
            } else {
                joinLobby(player, Team.YELLOW)
                loadLabel(player, "nm")
            }
        }

        label("wiz")
        npc("Okay!")
        goto("nowhere")

        label("nm")
        goto("nowhere")

    }
}