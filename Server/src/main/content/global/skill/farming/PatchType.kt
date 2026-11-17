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
    EVIL_TURNIP_PATCH(5),
    CALQUAT_TREE_PATCH(160),

    // Garden of Tranquility patches
    VINE_PATCH(5),
    PINK_ORCHID_PATCH(5),
    YELLOW_ORCHID_PATCH(5),
    SNOWDROP_PATCH(5),
    DELPHINIUM_PATCH(5),
    PINK_ROSE_PATCH(5),
    RED_ROSE_PATCH(5),
    WHITE_ROSE_PATCH(5),
    WHITE_TREE_PATCH(160);

    /**
     * Returns the display name of this PatchType.
     */
    fun displayName(): String = name.lowercase().replace("_", " ")
}
