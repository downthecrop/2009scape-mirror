package content.region.misc.apeatoll.dialogue.marim

import core.api.openDialogue
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

@Initializable
class PadulahDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, PadulahDialogueFile(), npc)
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return PadulahDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.PADULAH_1447)
    }
}

class PadulahDialogueFile: DialogueBuilderFile() {

    override fun create(b: DialogueBuilder) {
        b.defaultDialogue().npcl(
            "What do you want?"
        ).playerl(
            FacialExpression.NEUTRAL,
            "Oh, nothing in particular really."
        ).npcl(
            "Well stop distracting me then. I'm meant to be guarding this sacred statue from the temple of Marimbo."
        ).options().let { optionsBuilder -> optionsBuilder.option("Where is the temple of Marimbo?")
            .npcl(
            "You're not from around here are you?"
        ).playerl(
            FacialExpression.NEUTRAL,
            "Actually I'm not. I'm a visitor from foreign lands."
        ).npcl(
            "Very well, the temple is to the east of the village."
        ).end()
        optionsBuilder.option("What is the statue of? ")
            .npcl(
            "It's of Marimbo, you cretin!"
        ).playerl(
            FacialExpression.NEUTRAL,
            "Ah yes. How stupid of me not to see the likeness."
        ).end()
        optionsBuilder.option("I'll be back later.")
            .npcl(
            "I wouldn't count on it."
        ).playerl(
            FacialExpression.WORRIED,
            "What?!"
        ).npcl(
            "Oh, nothing."
        ).end()
        }
    }
}