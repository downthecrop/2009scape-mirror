package core.game.interaction

import core.game.node.Node
import core.game.node.entity.player.Player

typealias UseWithExecutor = (Player, Node, Node, Int) -> Boolean
typealias InteractExecutor = (Player, Node, Int) -> Boolean
typealias VoidExecutor = (Int) -> Boolean

enum class QueueStrength {
    WEAK,
    NORMAL,
    STRONG,
    SOFT
}

open class Script<T> (val execution: T, val persist: Boolean) {
    var state: Int = 0
    var nextExecution = 0
}

class Interaction(execution: InteractExecutor, val distance: Int, persist: Boolean) : Script<InteractExecutor>(execution, persist)
class UseWithInteraction(execution: UseWithExecutor, val distance: Int, persist: Boolean, val used: Node, val with: Node) : Script<UseWithExecutor>(execution, persist)
class QueuedScript(executor: VoidExecutor, val strength: QueueStrength, persist: Boolean) : Script<VoidExecutor>(executor, persist)
class QueuedUseWith(executor: UseWithExecutor, val strength: QueueStrength, persist: Boolean, val used: Node, val with: Node) : Script<UseWithExecutor>(executor, persist)