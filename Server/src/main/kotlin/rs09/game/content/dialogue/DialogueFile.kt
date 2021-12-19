package rs09.game.content.dialogue

import api.splitLines
import core.game.component.Component
import core.game.content.dialogue.DialogueInterpreter
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import rs09.tools.START_DIALOGUE

abstract class DialogueFile {
    var player: Player? = null
    var npc: NPC? = null
    var interpreter: DialogueInterpreter? = null
    open var stage = START_DIALOGUE
    var dialoguePlugin: DialoguePlugin? = null

    abstract fun handle(componentID: Int, buttonID: Int)
    fun load(player: Player, npc: NPC?, interpreter: DialogueInterpreter): DialogueFile{
        this.player = player
        this.npc = npc
        this.interpreter = interpreter
        return this
    }

    open fun npc(vararg messages: String?): Component? {
        return if (npc == null) {
            interpreter!!.sendDialogues(
                npc!!.id,
                if (npc!!.id > 8591) FacialExpression.OLD_NORMAL else FacialExpression.FRIENDLY,
                *messages
            )
        } else interpreter!!.sendDialogues(
            npc,
            if (npc!!.id > 8591) FacialExpression.OLD_NORMAL else FacialExpression.FRIENDLY,
            *messages
        )
    }

    open fun npc(id: Int, vararg messages: String?): Component? {
        return interpreter!!.sendDialogues(id, FacialExpression.FRIENDLY, *messages)
    }

    open fun npc(expression: FacialExpression?, vararg messages: String?): Component? {
        return if (npc == null) {
            interpreter!!.sendDialogues(0, expression, *messages)
        } else interpreter!!.sendDialogues(npc, expression, *messages)
    }

    open fun player(vararg messages: String?): Component? {
        return interpreter!!.sendDialogues(player, null, *messages)
    }

    open fun player(expression: FacialExpression?, vararg messages: String?): Component? {
        return interpreter!!.sendDialogues(player, expression, *messages)
    }

    /**
     * Use the automatic linesplitting feature in DialUtils to produce npc dialogues
     * @param expr the FacialExpression to use, located in the FacialExpression enum.
     * @param msg the message for the NPC to say
     */
    open fun npcl(expr: FacialExpression?, msg: String?): Component? {
        return npc(expr, *splitLines(msg!!))
    }

    open fun npcl(msg: String?): Component? {
        return npc(*splitLines(msg!!))
    }

    /**
     * Use the automatic linesplitting feature in DialUtils to produce player dialogues
     * @param expr the FacialExpression to use, located in the FacialExpression enum.
     * @param msg the message for the player to say
     */
    open fun playerl(expr: FacialExpression?, msg: String?): Component? {
        return player(expr, *splitLines(msg!!))
    }

    open fun playerl(msg: String?): Component? {
        return player(*splitLines(msg!!))
    }

    open fun end(){
        if(interpreter != null) interpreter!!.close()
    }

    open fun sendNormalDialogue(entity: Entity?, expression: FacialExpression?, vararg messages: String?) {
        interpreter!!.sendDialogues(entity, expression, *messages)
    }

    open fun options(vararg options: String?) {
        interpreter!!.sendOptions("Select an Option", *options)
    }

    /**
     * Use in place of setting the stage to END_DIALOGUE when you want to return to the default dialogue plugin at START_DIALOGUE
     */
    fun endFile(){
        interpreter!!.dialogue.file = null

    }

    /**
     * Return to the default dialogue plugin at the given stage.
     * Should be used in place of setting a new stage. I.E. instead of "stage = END_DIALOGUE" use "returnAtStage(whatever stage)"
     * @param stage The stage to return to.
     */
    fun returnAtStage(toStage: Int){
        dialoguePlugin!!.file = null
        dialoguePlugin!!.stage = toStage
    }

    /**
     * Use when you've entered a DialogueFile but current state does not match any possible conditionals.
     * Sort-of a fail-safe in a sense.
     */
    fun abandonFile(){
        interpreter!!.dialogue.file = null
        player("Huh. Nevermind.")
    }

    open fun getCurrentStage(): Int{
        return stage
    }

    fun Int.substage(stage: Int): Int{
        return this + stage
    }

    fun dialogue(vararg messages: String){
        player?.dialogueInterpreter?.sendDialogue(*messages)
    }

}
