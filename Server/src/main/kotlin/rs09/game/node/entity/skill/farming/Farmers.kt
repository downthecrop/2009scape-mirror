package rs09.game.node.entity.skill.farming

enum class Farmers(val id: Int, val patches: Array<FarmingPatch>) {
    LYRA(2326, arrayOf(FarmingPatch.PORT_PHAS_ALLOTMENT_NW,FarmingPatch.PORT_PHAS_ALLOTMENT_SE)),
    ELSTAN(2323, arrayOf(FarmingPatch.S_FALADOR_ALLOTMENT_NW,FarmingPatch.S_FALADOR_ALLOTMENT_SE)),
    HESKEL(2340, arrayOf(FarmingPatch.N_FALADOR_TREE)),
    ALAIN(2339, arrayOf(FarmingPatch.TAVERLY_TREE)),
    DANTAERA(2324, arrayOf(FarmingPatch.CATHERBY_ALLOTMENT_N,FarmingPatch.CATHERBY_ALLOTMENT_S)),
    ELLENA(2331, arrayOf(FarmingPatch.CATHERBY_FRUIT_TREE)),
    GARTH(2330,arrayOf(FarmingPatch.BRIMHAVEN_FRUIT_TREE)),
    GILETH(2344,arrayOf(FarmingPatch.TREE_GNOME_VILLAGE_FRUIT_TREE)),
    AMAETHWR(2860,arrayOf(FarmingPatch.LLETYA_FRUIT_TREE)),
    SELENA(2332, arrayOf(FarmingPatch.YANILLE_HOPS)),
    KRAGEN(2325, arrayOf(FarmingPatch.ARDOUGNE_ALLOTMENT_N,FarmingPatch.ARDOUGNE_ALLOTMENT_S)),
    BOLONGO(2343, arrayOf(FarmingPatch.GNOME_STRONGHOLD_FRUIT_TREE)),
    PRISSY_SCILLA(1037, arrayOf(FarmingPatch.GNOME_STRONGHOLD_TREE)),
    FAYETH(2342, arrayOf(FarmingPatch.LUMBRIDGE_TREE)),
    TREZNOR(2341, arrayOf(FarmingPatch.VARROCK_TREE)),
    VASQUEN(2333, arrayOf(FarmingPatch.LUMBRIDGE_HOPS)),
    RHONEN(2334, arrayOf(FarmingPatch.MCGRUBOR_HOPS)),
    FRANCIS(2327, arrayOf(FarmingPatch.ENTRANA_HOPS)),
    DREVEN(2335, arrayOf(FarmingPatch.CHAMPIONS_GUILD_BUSH)),
    TARIA(2336, arrayOf(FarmingPatch.RIMMINGTON_BUSH)),
    TORRELL(2338, arrayOf(FarmingPatch.ARDOUGNE_BUSH));

    companion object{
        @JvmField
        val farmers = values().map { it.id to it }.toMap()

        @JvmStatic
        fun forId(id: Int): Farmers?{
            return farmers[id]
        }
    }
}