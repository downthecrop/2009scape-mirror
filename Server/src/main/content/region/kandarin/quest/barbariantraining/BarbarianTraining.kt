package content.region.kandarin.quest.barbariantraining

/**
 * Attribute keys for the Barbarian Training miniquest and related activities.
 * @author Bishop
 */

object BarbarianTraining {
    // Barbarian training attributes which hold an Int 0-3:
    // 0 = not started, 1 = started, 2 = ready to turn in, 3 = done
    const val attributeRod           = "/save:barbtraining:rod"
    const val attributeFist          = "/save:barbtraining:fist"
    const val attributeHerblore      = "/save:barbtraining:herblore"
    const val attributeSpear         = "/save:barbtraining:spear"
    const val attributeHasta         = "/save:barbtraining:hasta"
    const val attributeBowFire       = "/save:barbtraining:bowfire"
    const val attributePyreShip      = "/save:barbtraining:pyreship"
    // Employed when the player has not progressed the Tai Bwo Wannai
    // Trio quest far enough to proceed with barbarian spear training.
    const val attributeSpearRejected = "/save:barbtraining:spear-rejected"
    // Keeps track of the player's count of bones yet to bury that
    // will be supplied with bonus XP from burning pyre ships.
    const val attributePyrePrayerBonus = "/save:barbtraining:pyre-prayer-bonus"
    // Keeps track of if the player has been warned of the dangers
    // of the Ancient Cavern.
    const val attributeWhirlpool = "/save:barbtraining:whirlpool"
    // Keeps track of if the player currently has the attention
    // of a Ferocious Barbarian Spirit.
    const val attributeFerociousSpirit = "barbtraining:ferocious-spirit"
    // Prefix for My Notes page tracking: append page number (1-26).
    // ex. "${BarbarianTraining.attributeMyNotesPage}$pageNum"
    const val attributeMyNotesPage = "/save:mynotes:page:"
}