package core.game.content.dialogue

import core.game.component.Component
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player

abstract class DialogueFile {
    var player: Player? = null
    var npc: NPC? = null
    var interpreter: DialogueInterpreter? = null
    var stage = 0
    abstract fun handle(componentID: Int, buttonID: Int)
    fun load(player: Player, npc: NPC, interpreter: DialogueInterpreter): DialogueFile{
        val newFile = this::class.java.newInstance()
        newFile.player = player
        newFile.npc = npc
        newFile.interpreter = interpreter
        return newFile
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
}