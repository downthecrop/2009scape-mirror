package rs09.game.node.entity.skill.skillcapeperks

enum class Skillcape {
    ATTACK,
    STRENGTH,
    DEFENCE,
    RANGING,
    PRAYER,
    MAGIC,
    RUNECRAFTING,
    HITPOINTS,
    AGILITY,
    HERBLORE,
    THIEVING,
    CRAFTING,
    FLETCHING,
    SLAYER,
    CONSTRUCTION,
    MINING,
    SMITHING,
    FISHING,
    COOKING,
    FIREMAKING,
    WOODCUTTING,
    FARMING,
    HUNTING,
    SUMMONING,
    NONE
    ;
    companion object{
        @JvmStatic
        fun forId(id: Int): Skillcape{
            return when(id){
                9747,9748 -> ATTACK
                9750,9751 -> STRENGTH
                9753,9754 -> DEFENCE
                9756,9757 -> RANGING
                9759,9760 -> PRAYER
                9762,9763 -> MAGIC //magic
                9765,9766 -> RUNECRAFTING //runecrafting
                9768,9769 -> HITPOINTS //hp
                9771,9772 -> AGILITY //agility
                9774,9775 -> HERBLORE //herblore
                9777,9778 -> THIEVING //thieving
                9780,9781 -> CRAFTING //crafting
                9783,9784 -> FLETCHING //fletching
                9786,9787 -> SLAYER //slayer
                9789,9790 -> CONSTRUCTION //construction
                9792,9793 -> MINING //mining
                9795,9796 -> SMITHING //smithing
                9798,9799 -> FISHING //fishing
                9801,9802 -> COOKING //cooking
                9804,9805 -> FIREMAKING //firemaking
                9807,9808 -> WOODCUTTING //woodcutting
                9810,9811 -> FARMING //farming
                9948,9949 -> HUNTING //hunting
                12169,12170 -> SUMMONING //summoning
                else -> NONE
            }
        }
    }
}