package content.global.activity.jobs.impl

import content.global.activity.jobs.Job
import content.global.activity.jobs.JobType
import core.game.node.entity.skill.Skills

/**
 * An enum of the possible production jobs that can be assigned to a player.
 *
 * Note: Due to how player save files keep track of the player's current job, it is essential that
 * new entries are only appended to the end of this enum, and the ordering of existing entries is not changed.
 */
enum class ProductionJobs(
    override val lower: Int,
    override val upper: Int,
    val lvlReq: Int,
    val itemId: Int,
    val skill: Int,
    override val employer: Employers
) : Job {
    LOG(20, 28, 1, 1511, Skills.WOODCUTTING, Employers.WOODCUTTING_TUTOR),
    COWHIDES(20, 28, 1, 1739, 0, Employers.HANS),
    OAK(22, 28, 15, 1521, Skills.WOODCUTTING, Employers.WOODCUTTING_TUTOR),
    WATER_RUNE(20, 28, 5, 555, Skills.RUNECRAFTING, Employers.HANS),
    EARTH_RUNE(20, 28, 9, 557, Skills.RUNECRAFTING, Employers.AGGIE),
    FIRE_RUNE(20, 28, 14, 554, Skills.RUNECRAFTING, Employers.AGGIE),
    AIR_RUNE(20, 28, 1, 556, Skills.RUNECRAFTING, Employers.HANS),
    RUNE_ESS(20, 28, 1, 1436, Skills.MINING, Employers.MINING_TUTOR),
    BALL_OF_WOOL(20, 28, 1, 1759, Skills.CRAFTING, Employers.GILLIE_GROATS),
    LEATHER_GLOVE(20, 28, 1, 1059, Skills.CRAFTING, Employers.GILLIE_GROATS),
    WILLOW(22, 28, 30, 1519, Skills.WOODCUTTING, Employers.WOODCUTTING_TUTOR),
    LEATHER_BOOT(24, 24, 1, 1061, Skills.CRAFTING, Employers.HANS),
    BRONZE_DAGGER(24, 24, 1, 1205, Skills.SMITHING, Employers.SMELTING_TUTOR),
    BRONZE_HELMS(22, 22, 1, 1139, Skills.SMITHING, Employers.SMELTING_TUTOR),
    BRONZE_FULL_HELM(10, 10, 7, 1155, Skills.SMITHING, Employers.SMELTING_TUTOR),
    BRONZE_CHAINBODY(10, 10, 11, 1103, Skills.SMITHING, Employers.SMELTING_TUTOR),
    BRONZE_2H(10, 10, 14, 1307, Skills.SMITHING, Employers.SMELTING_TUTOR),
    BRONZE_SCIM(10, 10, 5, 1321, Skills.SMITHING, Employers.SMELTING_TUTOR),
    BRONZE_PLATEBODY(9, 10, 18, 1117, Skills.SMITHING, Employers.SMELTING_TUTOR),
    BRONZE_PLATELEGS(9, 10, 16, 1075, Skills.SMITHING, Employers.SMELTING_TUTOR),
    BRONZE_PLATESKIRTS(9, 10, 16, 1087, Skills.SMITHING, Employers.SMELTING_TUTOR),
    BRONZE_WARHAMMER(9, 9, 9, 1337, Skills.SMITHING, Employers.SMELTING_TUTOR),
    IRON_DAGGER(13, 13, 15, 1203, Skills.SMITHING, Employers.SMELTING_TUTOR),
    IRON_CHAINBODY(10, 10, 26, 1101, Skills.SMITHING, Employers.SMELTING_TUTOR),
    IRON_2H(9, 9, 29, 1309, Skills.SMITHING, Employers.SMELTING_TUTOR),
    STEEL_BATTLEAXE(9, 9, 40, 1365, Skills.SMITHING, Employers.SMELTING_TUTOR),
    STEEL_SCIMITAR(9, 9, 35, 1325, Skills.SMITHING, Employers.SMELTING_TUTOR),
    STEEL_PLATEBODY(9, 10, 48, 1119, Skills.SMITHING, Employers.SMELTING_TUTOR),
    STEEL_WARHAMMER(9, 10, 39, 1339, Skills.SMITHING, Employers.SMELTING_TUTOR),
    LEATHER_CHAPS(22, 28, 18, 1095, Skills.CRAFTING, Employers.CRAFTING_TUTOR),
    LEATHER_BODY(22, 28, 14, 1129, Skills.CRAFTING, Employers.CRAFTING_TUTOR),
    LEATHER_COWL(22, 28, 9, 1167, Skills.CRAFTING, Employers.CRAFTING_TUTOR),
    LEATHER_COIF(22, 28, 38, 1169, Skills.CRAFTING, Employers.CRAFTING_TUTOR),
    RAW_SHRIMP(22, 28, 1, 317, Skills.FISHING, Employers.FISHING_TUTOR),
    COOKED_SHRIMP(22, 28, 1, 315, Skills.COOKING, Employers.COOKING_TUTOR),
    RAW_CRAYFISH(22, 28, 1, 13435, Skills.FISHING, Employers.FISHING_TUTOR),
    COOKED_CRAYFISH(22, 28, 1, 13433, Skills.COOKING, Employers.COOKING_TUTOR),
    RAW_TROUT(22, 28, 20, 335, Skills.FISHING, Employers.FISHING_TUTOR),
    COOKED_TROUT(22, 28, 15, 333, Skills.COOKING, Employers.COOKING_TUTOR),
    RAW_SALMON(22, 28, 30, 331, Skills.FISHING, Employers.FISHING_TUTOR),
    COOKED_SALMON(22, 28, 25, 329, Skills.COOKING, Employers.COOKING_TUTOR),
    CAKE(12, 16, 40, 1891, Skills.COOKING, Employers.GILLIE_GROATS),
    MEAT_PIE(12, 16, 20, 2327, Skills.COOKING, Employers.COOKING_TUTOR),
    PLAIN_PIZZA(12, 16, 35, 2289, Skills.COOKING, Employers.COOKING_TUTOR),
    MEAT_PIZZA(12, 16, 45, 2293, Skills.COOKING, Employers.COOKING_TUTOR),
    ANCHOVY_PIZZA(12, 16, 55, 2297, Skills.COOKING, Employers.COOKING_TUTOR),
    REDBERRY_PIE(12, 16, 10, 2325, Skills.COOKING, Employers.GILLIE_GROATS),
    COPPER_ORE(22, 28, 1, 436, Skills.MINING, Employers.MINING_TUTOR),
    TIN_ORE(23, 26, 1, 438, Skills.MINING, Employers.MINING_TUTOR),
    IRON_ORE(24, 24, 15, 440, Skills.MINING, Employers.MINING_TUTOR),
    SILVER_ORE(22, 28, 20, 442, Skills.MINING, Employers.PRAYER_TUTOR),
    COAL(22, 26, 30, 453, Skills.MINING, Employers.MINING_TUTOR),
    GOLD_ORE(22, 24, 40, 444, Skills.MINING, Employers.MINING_TUTOR),
    BRONZE_BAR(10, 12, 1, 2349, Skills.SMITHING, Employers.SMELTING_TUTOR),
    IRON_BAR(22, 28, 15, 2351, Skills.SMITHING, Employers.SMELTING_TUTOR),
    STEEL_BAR(22, 28, 30, 2353, Skills.SMITHING, Employers.SMELTING_TUTOR),
    ASHES(26, 26, 1, 592, 0, Employers.AGGIE);

    override val type = JobType.PRODUCTION
}