package core.game.dialogue

/**
 * Topic/IfTopic system backported from my personal project
 * @author Ceikry
 */
open class Topic<T>(val expr: core.game.dialogue.FacialExpression, val text: String, val toStage: T, val skipPlayer: Boolean = false) {
    constructor(text: String, toStage: T, skipPlayer: Boolean = false) : this(core.game.dialogue.FacialExpression.ASKING, text, toStage, skipPlayer)
}
class IfTopic<T>(expr: core.game.dialogue.FacialExpression, text: String, toStage: T, val showCondition: Boolean, skipPlayer: Boolean = false) : Topic<T>(expr, text, toStage, skipPlayer) {
    constructor(text: String, toStage: T, showCondition: Boolean, skipPlayer: Boolean = false) : this(core.game.dialogue.FacialExpression.ASKING, text, toStage, showCondition, skipPlayer)
}