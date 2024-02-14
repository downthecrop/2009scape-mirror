package content.global.skill.farming

enum class PatchType(val stageGrowthTime: Int) {
    ALLOTMENT(10),
    HOPS_PATCH(10),
    TREE_PATCH(40),
    FRUIT_TREE_PATCH(160),
    BUSH_PATCH(20),
    FLOWER_PATCH(5),
    HERB_PATCH(20),
    SPIRIT_TREE_PATCH(295),
    MUSHROOM_PATCH(30),
    BELLADONNA_PATCH(80),
    CACTUS_PATCH(60),
    EVIL_TURNIP_PATCH(5);

    /**
     * Returns the display name of this PatchType.
     */
    fun displayName(): String = name.lowercase().replace("_", " ")
}
