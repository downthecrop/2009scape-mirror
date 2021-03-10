package core.game.content.dialogue

import core.game.component.Component
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.tools.START_DIALOGUE

abstract class DialogueFile {
    var player: Player? = null
    var npc: NPC? = null
    var interpreter: DialogueInterpreter? = null
    var stage = START_DIALOGUE
    abstract fun handle(componentID: Int, buttonID: Int)
    fun load(player: Player, npc: NPC, interpreter: DialogueInterpreter): DialogueFile{
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

    fun end(){
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
     * Use when you've entered a DialogueFile but current state does not match any possible conditionals.
     * Sort-of a fail-safe in a sense.
     */
    fun abandonFile(){
        interpreter!!.dialogue.file = null
        player("Huh. Nevermind.")
    }

}