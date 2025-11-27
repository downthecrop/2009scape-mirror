package content.global.ame.events.quizmaster

import content.global.ame.RandomEventNPC
import content.global.ame.kidnapPlayer
import core.api.*
import core.api.utils.WeightBasedTable
import core.game.node.entity.npc.NPC
import core.game.world.map.Location
import org.rs09.consts.NPCs

/**
 * Quiz Master NPC:
 *
 * https://www.youtube.com/watch?v=EFAWSiPTfcM
 * https://www.youtube.com/watch?v=caWn7pE2mkE
 * https://www.youtube.com/watch?v=Bc1gAov2o4w
 * https://www.youtube.com/watch?v=oHU8-MUarxE
 * https://www.youtube.com/watch?v=wvjYiF4v9tI
 * https://www.youtube.com/watch?v=dC6rlSnXEfw
 */
class QuizMasterNPC(var type: String = "", override var loot: WeightBasedTable? = null) : RandomEventNPC(NPCs.QUIZ_MASTER_2477) {
    override fun init() {
        super.init()
        sendChat("Hey ${player.username}! It's your lucky day!")
        face(player)
        kidnapPlayer(this, player, Location(1952, 4764, 0)) { player, _ ->
            setAttribute(player, QuizMasterDialogueFile.QUIZMASTER_ATTRIBUTE_QUESTIONS_CORRECT, 0)
            sendMessage(player, "Answer four questions correctly in a row to be teleported back where you came from.")
            sendMessage(player, "You will need to relog in if you lose the quiz dialog.") // Inauthentic, but there to notify the player in case.
            face(player, Location(1952, 4768, 1))
            animate(player,2378)
            // Quiz dialogue gets opened automatically on zone entry.
        }
    }

    override fun talkTo(npc: NPC) {
        openDialogue(player, QuizMasterDialogueFile(), this.asNpc())
    }
}