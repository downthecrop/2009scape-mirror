package rs09.game.content.dialogue

import core.game.content.dialogue.FacialExpression

/**
 * Topic/IfTopic system backported from my personal project
 * @author Ceikry
 */
open class Topic<T>(val expr: FacialExpression, val text: String, val toStage: T) {
    constructor(text: String, toStage: T) : this(FacialExpression.ASKING, text, toStage)
}
class IfTopic<T>(expr: FacialExpression, text: String, toStage: T, val showCondition: Boolean) : Topic<T>(expr, text, toStage) {
    constructor(text: String, toStage: T, showCondition: Boolean) : this(FacialExpression.ASKING, text, toStage, showCondition)
}