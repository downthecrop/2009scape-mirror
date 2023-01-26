package core.game.dialogue
import core.game.node.entity.player.Player
import core.tools.END_DIALOGUE

abstract class DialogueBuilderFile : core.game.dialogue.DialogueFile() {
    var data: ArrayList<core.game.dialogue.DialogueClause> = ArrayList()
    //var stages: ArrayList<Int> = ArrayList()
    abstract fun create(b: core.game.dialogue.DialogueBuilder)
    init {
        create(core.game.dialogue.DialogueBuilder(this))
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
    fun handle(df: core.game.dialogue.DialogueFile, componentID: Int, buttonID: Int, stage: Int): Int
}

class NpcLNode(val value: String): core.game.dialogue.DialogueNode {
    override fun handle(df: core.game.dialogue.DialogueFile, componentID: Int, buttonID: Int, stage: Int): Int {
        df.npcl(value)
        return stage + 1
    }
}
class NpcNode(val values: Array<String>): core.game.dialogue.DialogueNode {
    override fun handle(df: core.game.dialogue.DialogueFile, componentID: Int, buttonID: Int, stage: Int): Int {
        df.npc(*values)
        return stage + 1
    }
}
class PlayerLNode(val value: String): core.game.dialogue.DialogueNode {
    override fun handle(df: core.game.dialogue.DialogueFile, componentID: Int, buttonID: Int, stage: Int): Int {
        df.playerl(value)
        return stage + 1
    }
}
class ClosureNode(val f: (Player) -> Int): core.game.dialogue.DialogueNode {
    override fun handle(df: core.game.dialogue.DialogueFile, componentID: Int, buttonID: Int, stage: Int): Int {
        return f(df.player!!)
    }
}

class OptionEntry(val text: String, val nextStage: Int, val predicate: (Player) -> Boolean = { _ -> true }) {}

class OptionsNode(var options: ArrayList<core.game.dialogue.OptionEntry>): core.game.dialogue.DialogueNode {
    fun optionNames(player: Player): Array<String?> {
        return options.asSequence().filter({ it.predicate(player) }).map({ it.text }).toList().toTypedArray()
    }
    override fun handle(df: core.game.dialogue.DialogueFile, componentID: Int, buttonID: Int, stage: Int): Int {
        val tmp: Array<String?> = optionNames(df.player!!)
        if(tmp.size > 1) {
            df.options(*tmp)
            return stage + 1
        } else if(tmp.size == 1) {
            val tmp: List<core.game.dialogue.OptionEntry> = options.asSequence().filter({ it.predicate(df.player!!) }).toList()
            df.stage = tmp[0].nextStage
            df.handle(componentID, 0)
            return df.stage
        } else {
            return END_DIALOGUE
        }
    }
}
class OptionsDispatchNode(var options: ArrayList<core.game.dialogue.OptionEntry>): core.game.dialogue.DialogueNode {
    override fun handle(df: core.game.dialogue.DialogueFile, componentID: Int, buttonID: Int, stage: Int): Int {
        val tmp: List<core.game.dialogue.OptionEntry> = options.asSequence().filter({ it.predicate(df.player!!) }).toList()
        df.stage = tmp[buttonID-1].nextStage
        df.handle(componentID, 0)
        return df.stage
    }
}

class DialogueClause(val predicate: (player: Player) -> Boolean, val nodes: ArrayList<core.game.dialogue.DialogueNode>) {
    fun handle(df: core.game.dialogue.DialogueFile, componentID: Int, buttonID: Int, stage: Int): Int {
        if(stage < nodes.size) {
            return nodes[stage].handle(df, componentID, buttonID, stage)
        } else {
            return END_DIALOGUE
        }
    }
}

class DialogueOptionsBuilder(var target: core.game.dialogue.DialogueBuilderFile, val clauseIndex: Int, var options: ArrayList<core.game.dialogue.OptionEntry>) {
    fun option(value: String): core.game.dialogue.DialogueBuilder {
        options.add(core.game.dialogue.OptionEntry(value, target.data[clauseIndex].nodes.size))
        return core.game.dialogue.DialogueBuilder(target, clauseIndex)
    }

    fun optionIf(value: String, predicate: (Player) -> Boolean): core.game.dialogue.DialogueBuilder {
        options.add(core.game.dialogue.OptionEntry(value, target.data[clauseIndex].nodes.size, predicate))
        return core.game.dialogue.DialogueBuilder(target, clauseIndex)
    }
}

class DialogueBuilder(var target: core.game.dialogue.DialogueBuilderFile, var clauseIndex: Int = -1) {
    fun onPredicate(predicate: (player: Player) -> Boolean): core.game.dialogue.DialogueBuilder {
        target.data.add(core.game.dialogue.DialogueClause(predicate, ArrayList()))
        clauseIndex = target.data.size - 1
        return this
    }
    fun onQuestStages(name: String, vararg stages: Int): core.game.dialogue.DialogueBuilder {
        return onPredicate() { player ->
            val questStage = player.questRepository.getStage(name)
            return@onPredicate stages.contains(questStage)
        }
    }
    fun playerl(value: String): core.game.dialogue.DialogueBuilder {
        target.data[clauseIndex].nodes.add(core.game.dialogue.PlayerLNode(value))
        return this
    }
    fun npcl(value: String): core.game.dialogue.DialogueBuilder {
        target.data[clauseIndex].nodes.add(core.game.dialogue.NpcLNode(value))
        return this
    }
    fun npc(vararg values: String): core.game.dialogue.DialogueBuilder {
        target.data[clauseIndex].nodes.add(core.game.dialogue.NpcNode(values as Array<String>))
        return this
    }
    fun endWith(f: (Player) -> Unit) {
        target.data[clauseIndex].nodes.add(core.game.dialogue.ClosureNode({ player ->
            f(player)
            return@ClosureNode END_DIALOGUE
        }))
    }
    fun end() {
        target.data[clauseIndex].nodes.add(core.game.dialogue.ClosureNode({ _ ->
            return@ClosureNode END_DIALOGUE
        }))
    }
    fun options(): core.game.dialogue.DialogueOptionsBuilder {
        var options: ArrayList<core.game.dialogue.OptionEntry> = ArrayList()
        val node = core.game.dialogue.OptionsNode(options)
        val dispatchNode = core.game.dialogue.OptionsDispatchNode(options)
        target.data[clauseIndex].nodes.add(node)
        target.data[clauseIndex].nodes.add(dispatchNode)
        return core.game.dialogue.DialogueOptionsBuilder(target, clauseIndex, options)
    }
}
