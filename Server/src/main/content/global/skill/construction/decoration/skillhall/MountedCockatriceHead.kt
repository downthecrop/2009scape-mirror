package content.global.skill.construction.decoration.skillhall

import content.global.skill.construction.HouseManager
import core.api.openDialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.world.repository.Repository
import core.tools.END_DIALOGUE
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery as Sceneries

class MountedCockatriceHead : InteractionListener {
    override fun defineListeners() {
        on(Sceneries.COCKATRICE_HEAD_13482, IntType.SCENERY, "talk-to") { player, _ ->
            val house = player.getAttribute<HouseManager>("poh_entry", player.houseManager)
            if (house == player.houseManager) {
                openDialogue(player, MountedCockatriceHeadOwnerDialogue())
            } else {
                val ownerName = Repository.players.first { it.houseManager == house }.username.capitalize()
                openDialogue(player, MountedCockatriceHeadGuestDialogue(ownerName))
            }
            return@on true
        }
    }
}

class MountedCockatriceHeadOwnerDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> npcl(NPCs.COCKATRICE_4227, FacialExpression.CHILD_NORMAL, "You deaded me!").also { stage++ }
            1 -> playerl(FacialExpression.NEUTRAL, "Well, yes.").also { stage++ }
            2 -> npcl(NPCs.COCKATRICE_4227, FacialExpression.CHILD_NORMAL, "What did you do that for?").also { stage++ }
            3 -> options("A Slayer Master told me to.", "So I could mount your head on my wall.", "I just wanted to.").also { stage++ }
            4 -> when (buttonID) {
                1 -> playerl(FacialExpression.NEUTRAL, "A Slayer Master told me to.").also { stage = 10 }
                2 -> playerl(FacialExpression.NEUTRAL, "So I could mount your head on my wall.").also { stage = 20 }
                3 -> playerl(FacialExpression.NEUTRAL, "I just wanted to.").also { stage = 30 }
            }
            10 -> npcl(NPCs.COCKATRICE_4227, FacialExpression.CHILD_SAD, "Why do the Slayer Masters all pick on poor cockatrice?").also { stage++ }
            11 -> playerl(FacialExpression.NEUTRAL, "They pick on lots of other creatures too.").also { stage++ }
            12 -> npcl(NPCs.COCKATRICE_4227, FacialExpression.CHILD_SAD, "Then mount one of them on your wall and let poor cockatrice rest in peace!").also { stage = END_DIALOGUE }
            20 -> npcl(NPCs.COCKATRICE_4227, FacialExpression.CHILD_SAD, "Another cockatrice falls victim to the dreaded mirror shield!").also { stage++ }
            21 -> playerl(FacialExpression.NEUTRAL, "Don't take it personally. You look good on my wall.").also { stage++ }
            22 -> npcl(NPCs.COCKATRICE_4227, FacialExpression.CHILD_SAD, "I don't care! I think I looked better with a body!").also { stage = END_DIALOGUE }
            30 -> npcl(NPCs.COCKATRICE_4227, FacialExpression.CHILD_ANGRY, "You dirty rotten swine, you!").also { stage++ }
            31 -> playerl(FacialExpression.AMAZED, "Steady on.").also { stage++ }
            32 -> npcl(NPCs.COCKATRICE_4227, FacialExpression.CHILD_ANGRY, "I will kill you with my paralyzing-type magic eyes look!").also { stage++ }
            33 -> npcl(NPCs.COCKATRICE_4227, FacialExpression.CHILD_ANGRY, "Dots appear in air between eyes and victim. Dot! Dot! Dotty!").also { stage++ }
            34 -> playerl(FacialExpression.NEUTRAL, "Er, nothing's happening...").also { stage++ }
            35 -> npcl(NPCs.COCKATRICE_4227, FacialExpression.CHILD_ANGRY, "Concentrates mental power. Eyes narrow beak clenches veins on head stand out.").also { stage++ }
            36 -> npcl(NPCs.COCKATRICE_4227, FacialExpression.CHILD_ANGRY, "Strain!").also { stage++ }
            37 -> playerl(FacialExpression.ASKING, "You're dead, cockatrice. Your eyes are glass beads. It won't work.").also { stage++ }
            38 -> npcl(NPCs.COCKATRICE_4227, FacialExpression.CHILD_ANGRY, "STRA-A-AIN!").also { stage++ }
            39 -> playerl(FacialExpression.ASKING, "I think I'll leave you to it.").also { stage = END_DIALOGUE }
        }
    }
}

class MountedCockatriceHeadGuestDialogue(private val ownerName: String) : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> playerl(FacialExpression.HAPPY, "Hey, a cockatrice!").also { stage++ }
            1 -> npcl(NPCs.COCKATRICE_4227, FacialExpression.CHILD_SAD, "$ownerName deaded me! That wasn't very nice!").also { stage = END_DIALOGUE }
        }
    }
}
