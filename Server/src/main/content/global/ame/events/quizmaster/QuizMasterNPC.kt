package content.global.ame.events.quizmaster

import content.global.ame.RandomEventNPC
import core.api.*
import core.api.utils.WeightBasedTable
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.system.timer.impl.AntiMacro
import core.game.world.map.Location
import core.game.world.update.flag.context.Graphics
import org.rs09.consts.NPCs
import org.rs09.consts.Sounds

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
        queueScript(player, 4, QueueStrength.SOFT) { stage: Int ->
            when (stage) {
                0 -> {
                    lock(player, 6)
                    sendGraphics(Graphics(1576, 0, 0), player.location)
                    animate(player,8939)
                    playAudio(player, Sounds.TELEPORT_ALL_200)
                    return@queueScript delayScript(player, 3)
                }
                1 -> {
                    if (getAttribute<Location?>(player, QuizMasterDialogueFile.QUIZMASTER_ATTRIBUTE_RETURN_LOC, null) == null) {
                        setAttribute(player, QuizMasterDialogueFile.QUIZMASTER_ATTRIBUTE_RETURN_LOC, player.location)
                    }
                    setAttribute(player, QuizMasterDialogueFile.QUIZMASTER_ATTRIBUTE_QUESTIONS_CORRECT, 0)
                    //MazeInterface.initMaze(player)
                    teleport(player, Location(1952, 4764, 1))
                    AntiMacro.terminateEventNpc(player)
                    sendGraphics(Graphics(1577, 0, 0), player.location)
                    animate(player,8941)
                    sendMessage(player, "Answer four questions correctly in a row to be teleported back where you came from.")
                    sendMessage(player, "You will need to relog in if you lose the quiz dialog.") // Inauthentic, but there to notify the player in case.
                    return@queueScript delayScript(player, 6)
                }
                2 -> {
                    face(player, Location(1952, 4768, 1))
                    animate(player,2378)
                    // This is not needed as when you enter the QuizMasterBorders, it should fire off the dialogue
                    // openDialogue(player, QuizMasterDialogueFile(), this.asNpc())
                    return@queueScript stopExecuting(player)
                }
                else -> return@queueScript stopExecuting(player)
            }

        }
    }

    override fun talkTo(npc: NPC) {
        openDialogue(player, QuizMasterDialogueFile(), this.asNpc())
    }
}