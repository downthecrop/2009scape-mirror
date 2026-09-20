package content.global.skill.construction

import content.data.Quests
import core.api.*
import core.game.node.entity.player.Player
import org.rs09.consts.Items

/**
 * Enumerates the various paintings the player can purchase for the Construction skill, as well as their requirements.
 * @author Bishop
 */

enum class PaintingType(val paintingId: Int, val cost: Int = 1000, private val requirement: (Player) -> Boolean = { true }) {

    ARTHUR(Items.ARTHUR_PORTRAIT_7995,
        requirement = { isQuestComplete(it, Quests.HOLY_GRAIL) }),
    ELENA(Items.ELENA_PORTRAIT_7996,
        requirement = { isQuestComplete(it, Quests.PLAGUE_CITY) }),
    ALVIS(Items.KELDAGRIM_PORTRAIT_7997,
        requirement = { isQuestComplete(it, Quests.THE_GIANT_DWARF) }),
    MISC(Items.MISC_PORTRAIT_7998,
        requirement = { isQuestComplete(it, Quests.THRONE_OF_MISCELLANIA) }),
    DESERT(Items.DESERT_PAINTING_7999,
        cost = 2000,
        requirement = { isQuestComplete(it, Quests.THE_FEUD) && isQuestComplete(it, Quests.THE_GOLEM) &&
                isQuestComplete(it, Quests.THE_TOURIST_TRAP)}),
    ISAFDAR(Items.ISAFDAR_PAINTING_8000,
        cost = 2000,
        requirement = { isQuestComplete(it, Quests.ROVING_ELVES) }),
    KARAMJA(Items.KARAMJA_PAINTING_8001,
        cost = 2000,
        requirement = { isQuestComplete(it, Quests.PIRATES_TREASURE) && isQuestComplete(it, Quests.SHILO_VILLAGE) &&
                isQuestComplete(it, Quests.TAI_BWO_WANNAI_TRIO) }),
    LUMBRIDGE(Items.LUMBRIDGE_PAINTING_8002,
        cost = 2000,
        requirement = { isQuestComplete(it, Quests.COOKS_ASSISTANT) && isQuestComplete(it, Quests.RUNE_MYSTERIES) &&
                isQuestComplete(it, Quests.THE_RESTLESS_GHOST) }),
    MORYTANIA(Items.MORYTANIA_PAINTING_8003,
        cost = 2000,
        requirement = { isQuestComplete(it, Quests.CREATURE_OF_FENKENSTRAIN) && isQuestComplete(it, Quests.GHOSTS_AHOY) &&
                isQuestComplete(it, Quests.HAUNTED_MINE) && isQuestComplete(it, Quests.SHADES_OF_MORTTON) }),
    MAP_SMALL(Items.SMALL_MAP_8004,
        requirement = { getQuestPoints(it) > 50 }),
    MAP_MEDIUM(Items.MEDIUM_MAP_8005,
        requirement = { getQuestPoints(it) > 100 }),
    MAP_LARGE(Items.LARGE_MAP_8006,
        requirement = { getQuestPoints(it) > 150 });

    fun eligible(player: Player): Boolean = requirement(player)
}