package content.global.skill.construction

import content.data.Quests
import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import org.rs09.consts.Items

/**
 * Enumerates the various crests the player can purchase for the Construction skill, as well as their requirements.
 * The NULL CrestType represents players who have yet to receive a crest from Sir Renitee.
 * ORDINAL BOUND
 * @author Bishop
 */

enum class CrestType(val crestName: String, val cost: Int = 5000, private val requirement: (Player) -> Boolean = { true }) {

    ARRAV("the Shield of Arrav symbol",
        requirement = { isQuestComplete(it, Quests.SHIELD_OF_ARRAV) }),
    ASGARNIA("the symbol of Asgarnia"),
    DORGESHUUN("the Dorgeshuun brooch",
        requirement = { isQuestComplete(it, Quests.THE_LOST_TRIBE) }),
    DRAGON("a dragon",
        requirement = { isQuestComplete(it, Quests.DRAGON_SLAYER) }),
    FAIRY("a fairy",
        requirement = { isQuestComplete(it, Quests.LOST_CITY) }),
    GUTHIX("the symbol of Guthix",
        requirement = { hasLevelStat(it, Skills.PRAYER, 70) }),
    HAM("the symbol of the HAM cult"),
    HORSE("a horse",
        requirement = { anyInInventory(it, Items.TOY_HORSEY_2520, Items.TOY_HORSEY_2522, Items.TOY_HORSEY_2524, Items.TOY_HORSEY_2526) }),
    JOGRE("Jiggig"),
    KANDARIN("the symbol of Kandarin"),
    MISTHALIN("the symbol of Misthalin"),
    MONEY("a bag of money",
        cost = 500000),
    SARADOMIN("the symbol of Saradomin",
        requirement = { hasLevelStat(it, Skills.PRAYER, 70) }),
    SKULL("a skull",
        requirement = { it.skullManager.isSkulled }),
    VARROCK("the symbol of Varrock"),
    ZAMORAK("the symbol of Zamorak",
        requirement = { hasLevelStat(it, Skills.PRAYER, 70) }),
    NULL("no crest",
        cost = 0, requirement = { false });

    fun eligible(player: Player): Boolean = requirement(player)
}