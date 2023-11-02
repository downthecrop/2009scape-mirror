package core.game.dialogue

/**
 * Topic/IfTopic system backported from my personal project
 * @author Ceikry
 */
open class Topic<T> @JvmOverloads constructor(val expr: FacialExpression, val text: String, val toStage: T, val skipPlayer: Boolean = false) {
    @JvmOverloads
    constructor(text: String, toStage: T, skipPlayer: Boolean = false) : this(FacialExpression.ASKING, text, toStage, skipPlayer)
}
class IfTopic<T> @JvmOverloads constructor(expr: FacialExpression, text: String, toStage: T, val showCondition: Boolean, skipPlayer: Boolean = false) : Topic<T>(expr, text, toStage, skipPlayer) {
    @JvmOverloads
    constructor(text: String, toStage: T, showCondition: Boolean, skipPlayer: Boolean = false) : this(core.game.dialogue.FacialExpression.ASKING, text, toStage, showCondition, skipPlayer)
}
