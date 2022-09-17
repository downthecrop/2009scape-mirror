package rs09.game.content.dialogue
import core.game.node.entity.player.Player
import rs09.game.content.dialogue.DialogueFile
import rs09.tools.END_DIALOGUE

abstract class DialogueBuilderFile : DialogueFile() {
    var data: ArrayList<DialogueClause> = ArrayList()
    //var stages: ArrayList<Int> = ArrayList()
    abstract fun create(b: DialogueBuilder)
    init {
        create(DialogueBuilder(this))
    }
    override fun handle(componentID: Int, buttonID: Int) {
        for((i, clause) in data.iterator().withIndex()) {
            if(clause.predicate(player!!)) {
                stage = clause.handle(this, componentID, buttonID, stage)
                if(stage == END_DIALOGUE) {
                    end()
                }
                return
            }
        }
    }
}

interface DialogueNode {
    fun handle(df: DialogueFile, componentID: Int, buttonID: Int, stage: Int): Int
}

class NpcLNode(val value: String): DialogueNode {
    override fun handle(df: DialogueFile, componentID: Int, buttonID: Int, stage: Int): Int {
        df.npcl(value)
        return stage + 1
    }
}
class NpcNode(val values: Array<String>): DialogueNode {
    override fun handle(df: DialogueFile, componentID: Int, buttonID: Int, stage: Int): Int {
        df.npc(*values)
        return stage + 1
    }
}
class PlayerLNode(val value: String): DialogueNode {
    override fun handle(df: DialogueFile, componentID: Int, buttonID: Int, stage: Int): Int {
        df.playerl(value)
        return stage + 1
    }
}
class ClosureNode(val f: (Player) -> Int): DialogueNode {
    override fun handle(df: DialogueFile, componentID: Int, buttonID: Int, stage: Int): Int {
        return f(df.player!!)
    }
}

class OptionEntry(val text: String, val nextStage: Int, val predicate: (Player) -> Boolean = { _ -> true }) {}

class OptionsNode(var options: ArrayList<OptionEntry>): DialogueNode {
    fun optionNames(player: Player): Array<String?> {
        return options.asSequence().filter({ it.predicate(player) }).map({ it.text }).toList().toTypedArray()
    }
    override fun handle(df: DialogueFile, componentID: Int, buttonID: Int, stage: Int): Int {
        val tmp: Array<String?> = optionNames(df.player!!)
        if(tmp.size > 1) {
            df.options(*tmp)
            return stage + 1
        } else if(tmp.size == 1) {
            val tmp: List<OptionEntry> = options.asSequence().filter({ it.predicate(df.player!!) }).toList()
            df.stage = tmp[0].nextStage
            df.handle(componentID, 0)
            return df.stage
        } else {
            return END_DIALOGUE
        }
    }
}
class OptionsDispatchNode(var options: ArrayList<OptionEntry>): DialogueNode {
    override fun handle(df: DialogueFile, componentID: Int, buttonID: Int, stage: Int): Int {
        val tmp: List<OptionEntry> = options.asSequence().filter({ it.predicate(df.player!!) }).toList()
        df.stage = tmp[buttonID-1].nextStage
        df.handle(componentID, 0)
        return df.stage
    }
}

class DialogueClause(val predicate: (player: Player) -> Boolean, val nodes: ArrayList<DialogueNode>) {
    fun handle(df: DialogueFile, componentID: Int, buttonID: Int, stage: Int): Int {
        if(stage < nodes.size) {
            return nodes[stage].handle(df, componentID, buttonID, stage)
        } else {
            return END_DIALOGUE
        }
    }
}

class DialogueOptionsBuilder(var target: DialogueBuilderFile, val clauseIndex: Int, var options: ArrayList<OptionEntry>) {
    fun option(value: String): DialogueBuilder {
        options.add(OptionEntry(value, target.data[clauseIndex].nodes.size))
        return DialogueBuilder(target, clauseIndex)
    }

    fun optionIf(value: String, predicate: (Player) -> Boolean): DialogueBuilder {
        options.add(OptionEntry(value, target.data[clauseIndex].nodes.size, predicate))
        return DialogueBuilder(target, clauseIndex)
    }
}

class DialogueBuilder(var target: DialogueBuilderFile, var clauseIndex: Int = -1) {
    fun onPredicate(predicate: (player: Player) -> Boolean): DialogueBuilder {
        target.data.add(DialogueClause(predicate, ArrayList()))
        clauseIndex = target.data.size - 1
        return this
    }
    fun onQuestStages(name: String, vararg stages: Int): DialogueBuilder {
        return onPredicate() { player ->
            val questStage = player.questRepository.getStage(name)
            return@onPredicate stages.contains(questStage)
        }
    }
    fun playerl(value: String): DialogueBuilder {
        target.data[clauseIndex].nodes.add(PlayerLNode(value))
        return this
    }
    fun npcl(value: String): DialogueBuilder {
        target.data[clauseIndex].nodes.add(NpcLNode(value))
        return this
    }
    fun npc(vararg values: String): DialogueBuilder {
        target.data[clauseIndex].nodes.add(NpcNode(values as Array<String>))
        return this
    }
    fun endWith(f: (Player) -> Unit) {
        target.data[clauseIndex].nodes.add(ClosureNode({ player ->
            f(player)
            return@ClosureNode END_DIALOGUE
        }))
    }
    fun end() {
        target.data[clauseIndex].nodes.add(ClosureNode({ _ ->
            return@ClosureNode END_DIALOGUE
        }))
    }
    fun options(): DialogueOptionsBuilder {
        var options: ArrayList<OptionEntry> = ArrayList()
        val node = OptionsNode(options)
        val dispatchNode = OptionsDispatchNode(options)
        target.data[clauseIndex].nodes.add(node)
        target.data[clauseIndex].nodes.add(dispatchNode)
        return DialogueOptionsBuilder(target, clauseIndex, options)
    }
}
