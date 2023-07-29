package core.game.node.entity.skill

import core.api.getStatLevel
import core.game.node.entity.player.Player

/**
 * Skill levels with unlocks
 **/

private val attack: IntArray = intArrayOf(5, 10, 15, 20, 30, 40, 50, 60, 70, 75 ,78, 99)
private val mining: IntArray = intArrayOf(6, 10, 15, 20, 21, 30, 31, 35, 40, 41, 45, 46, 50, 55, 60, 70, 80, 85, 90, 99)
private val smithing: IntArray = intArrayOf(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 46, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 68, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99)
private val defence: IntArray = intArrayOf(5, 10, 20, 25, 30, 40, 45, 50, 55, 60, 65, 70, 75, 78, 80, 99)
private val herblore: IntArray = intArrayOf(3, 5, 8, 11, 12, 19, 20, 22, 25, 26, 30, 31, 34, 35, 36, 38, 39, 40, 44, 45, 48, 50, 52, 53, 54, 55, 57, 59, 60, 63, 65, 66, 67, 68, 69, 70, 72, 73, 75, 76, 78, 80, 81, 82, 99)
private val fishing: IntArray = intArrayOf(5, 10, 15, 16, 20, 23, 25, 28, 30, 33, 35, 38, 40, 46, 48, 50, 53, 55, 58, 62, 65, 68, 70, 76, 79, 81, 90, 96, 99)
private val ranged: IntArray = intArrayOf(5, 10, 16, 19, 20, 21, 25, 26, 28, 31, 30, 33, 35, 36, 37, 39, 40, 42, 45, 46, 50, 55, 60, 61, 65, 70, 78, 80, 99)
private val thieving: IntArray = intArrayOf(2, 5, 10, 13, 15, 20, 22, 25, 27, 28, 30, 32, 35, 36, 38, 40, 42, 43, 44, 45, 47, 49, 50, 52, 53, 55, 59, 63, 65, 70, 72, 75, 78, 80, 82, 85, 99)
private val cooking: IntArray = intArrayOf(4, 5, 6, 7, 8, 10, 11, 12, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 35, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 53, 54, 55, 57, 58, 59, 60, 62, 64, 65, 67, 68, 70, 80, 82, 85, 91, 95, 99)
private val prayer: IntArray = intArrayOf(2, 4, 7, 8, 9, 10, 13, 16, 19, 22, 25, 26, 28, 31, 34, 35, 37, 40, 43, 44, 45, 46, 49, 52, 60, 70, 85, 90, 99)
private val crafting: IntArray = intArrayOf(3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 60, 62, 63, 64, 66, 67, 68, 70, 71, 73, 74, 75, 77, 79, 80, 82, 84, 85, 87, 90, 99)
private val firemaking: IntArray = intArrayOf(4, 5, 11, 12, 15, 20, 21, 25, 26, 30, 33, 35, 40, 42, 43, 45, 47, 48, 49, 50, 52, 53, 55, 58, 59, 60, 62, 63, 65, 68, 70, 72, 75, 76, 78, 79, 80, 83, 85, 87, 89, 92, 95, 99)
private val magic: IntArray = intArrayOf(4, 5, 7, 9, 11, 13, 14, 15, 17, 19, 20, 21, 23, 24, 25, 27, 29, 30, 31, 32, 33, 35, 37, 39, 40, 43, 45, 47, 49, 50, 51, 52, 53, 54, 55, 56, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 90, 91, 92, 93, 94, 95, 96, 97, 99)
private val fletching: IntArray = intArrayOf(5, 7, 9, 10, 11, 15, 18, 20, 22, 24, 25, 26, 30, 32, 33, 35, 37, 38, 39, 40, 41, 42, 43, 45, 46, 48, 49, 50, 51, 52, 53, 54, 55, 55, 56, 58, 59, 60, 61, 62, 63, 65, 67, 69, 70, 71, 73, 75, 77, 80, 81, 85, 90, 95, 99)
private val woodcutting: IntArray = intArrayOf(6, 10, 12, 15, 20, 21, 27, 30, 31, 35, 37, 40, 41, 42, 44, 45, 50, 54, 56, 57, 58, 60, 61, 75, 80, 82, 99)
private val runecrafting: IntArray = intArrayOf(2, 5, 6, 9, 10, 11, 13, 14, 15, 19, 20, 22, 23, 26, 27, 28, 33, 35, 38, 40, 42, 44, 46, 52, 54, 55, 56, 57, 59, 65, 66, 70, 74, 76, 77, 78, 82, 84, 91, 92, 95, 98, 99)
private val slayer: IntArray = intArrayOf(5, 7, 10, 15, 17, 20, 22, 25, 30, 32, 33, 35, 37, 39, 40, 42, 45, 47, 50, 51, 52, 55, 56, 57, 58, 59, 60, 63, 65, 68, 70, 72, 75, 80, 83, 85, 90, 99)
private val farming: IntArray = intArrayOf(2, 3, 4, 5, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 36, 38, 39, 42, 44, 45, 47, 48, 50, 51, 52, 53, 55, 56, 57, 59, 60, 62, 63, 67, 68, 70, 72, 73, 75, 79, 83, 85, 99)

/**
 * Gets the jingleId to play based on skill slot id and skill level
 * @param player the player
 * @param slot the skill slot id
 **/
fun getSkillJingle(player: Player, slot: Int) : Int {
    val skillLevel = getStatLevel(player, slot)
    when (slot) {
        0 -> return if (attack.contains(skillLevel)) 30 else 29
        1 -> return if (defence.contains(skillLevel)) 38 else 37
        2 -> return if (skillLevel < 50) 65 else 66 //Strength
        3 -> return if (skillLevel < 50) 47 else 48 //Hitpoints
        4 -> return if (ranged.contains(skillLevel)) 58 else 57
        5 -> return if (prayer.contains(skillLevel)) 56 else 55
        6 -> return if (magic.contains(skillLevel)) 52 else 51
        7 -> return if (cooking.contains(skillLevel)) 34 else 33
        8 -> return if (woodcutting.contains(skillLevel)) 70 else 69
        9 -> return if (fletching.contains(skillLevel)) 44 else 43
        10 -> return if (fishing.contains(skillLevel)) 42 else 41
        11 -> return if (firemaking.contains(skillLevel)) 40 else 39
        12 -> return if (crafting.contains(skillLevel)) 36 else 35
        13 -> return if (smithing.contains(skillLevel)) 64 else 63
        14 -> return if (mining.contains(skillLevel)) 54 else 53
        15 -> return if (herblore.contains(skillLevel)) 46 else 45
        16 -> return 28 //Agility
        17 -> return if (thieving.contains(skillLevel)) 68 else 67
        18 -> return if (slayer.contains(skillLevel)) 62 else 61
        19 -> return if (farming.contains(skillLevel)) 11 else 10
        20 -> return if (runecrafting.contains(skillLevel)) 60 else 59
        21 -> return if ((skillLevel % 2 == 0)) 49 else 50 //Hunter
        22 -> return if ((skillLevel % 10 == 0)) 31 else 32 //Construction
        23 -> return if ((skillLevel < 50)) 300 else 301 //Summoning
    }
    return 40
}
