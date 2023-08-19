package content.global.skill.hunter.implings

import core.api.utils.*
import core.game.world.map.*
import core.game.world.map.path.*
import core.game.world.map.build.*

enum class Impling (val npcId: Int, val puroId: Int) {
    Baby     (1028, 6055),
    Young    (1029, 6056),
    Gourmet  (1030, 6057),
    Earth    (1031, 6058),
    Essence  (1032, 6059),
    Eclectic (1033, 6060),
    Ninja    (6053, 6063),
    Nature   (1034, 6061),
    Magpie   (1035, 6062),
    Dragon   (6054, 6064);

    companion object {
        fun getIds() : IntArray {
            val list = ArrayList<Int>()
            for (imp in values()){
                list.add(imp.npcId)
                list.add(imp.puroId)
            }
            return list.toIntArray()
        }
    }
}

enum class ImplingSpawner (val npcId: Int, val table: WeightedTable<Impling>) {
    LowTier (npcId = 1024, WeightedTable.create 
    (
        Pair (Impling.Baby,    20.0),
        Pair (Impling.Young,   20.0),
        Pair (Impling.Gourmet, 20.0),
        Pair (Impling.Earth,   20.0),
        Pair (Impling.Essence, 10.0),
        Pair (Impling.Eclectic,10.0)
    )),
    MidTier (npcId = 1025, WeightedTable.create
    (
        Pair (Impling.Gourmet,  10.0),
        Pair (Impling.Earth,    10.0),
        Pair (Impling.Essence,  20.0),
        Pair (Impling.Eclectic, 37.0),
        Pair (Impling.Nature,   20.0),
        Pair (Impling.Magpie,    2.0),
        Pair (Impling.Ninja,     1.0)
    )),
    HighTier (npcId = 1026, WeightedTable.create
    (
        Pair (Impling.Nature, 10.0),
        Pair (Impling.Magpie, 50.0),
        Pair (Impling.Ninja,  30.0),
        Pair (Impling.Dragon, 10.0)
    )),
    LowPuroTier (npcId = 6065, WeightedTable.create
    (
        Pair (Impling.Baby,    20.0),
        Pair (Impling.Young,   20.0),
        Pair (Impling.Gourmet, 20.0),
        Pair (Impling.Earth,   20.0),
        Pair (Impling.Essence, 10.0),
        Pair (Impling.Eclectic,10.0)
    )),
    MidPuroTier (npcId = 6066, WeightedTable.create
    (
        Pair (Impling.Gourmet,  10.0),
        Pair (Impling.Earth,    10.0),
        Pair (Impling.Essence,  20.0),
        Pair (Impling.Eclectic, 37.0),
        Pair (Impling.Nature,   20.0),
        Pair (Impling.Magpie,    2.0),
        Pair (Impling.Ninja,     1.0)
    )),
    HighPuroTier (npcId = 6067, WeightedTable.create
    (
        Pair (Impling.Nature, 150.0),
        Pair (Impling.Magpie, 114.0),
        Pair (Impling.Ninja,   37.0),
        Pair (Impling.Dragon,  10.0),
    )),
    Nothing (npcId = -1, WeightedTable<Impling>());

    companion object {
        private val idMap = values().map { it.npcId to it }.toMap()

        fun forId (id: Int) : ImplingSpawner? {
            return idMap[id]
        }

        fun getIds() : IntArray {
            return idMap.keys.toIntArray()
        }
    }
}

enum class ImplingSpawnTypes (val table: WeightedTable<ImplingSpawner>, val spawnRolls: Int) {
    Standard (WeightedTable.create
    (
        Pair (ImplingSpawner.LowTier, 14.0),
        Pair (ImplingSpawner.MidTier,  7.0),
        Pair (ImplingSpawner.HighTier, 4.0),
        Pair (ImplingSpawner.Nothing, 75.0)
    ), spawnRolls = 3),
    LowTierOnly (WeightedTable.create
    (
        Pair (ImplingSpawner.LowTier, 100.0)
    ), spawnRolls = 1),
    MidTierOnly (WeightedTable.create
    (
        Pair (ImplingSpawner.MidTier, 100.0)
    ), spawnRolls = 1),
    HighTierOnly (WeightedTable.create
    (
        Pair (ImplingSpawner.HighTier, 100.0)
    ), spawnRolls = 1),
    HighPuroTierOnly (WeightedTable.create
    (
        Pair (ImplingSpawner.HighPuroTier, 100.0)
    ), spawnRolls = 1)
}

enum class ImplingSpawnLocations (val type: ImplingSpawnTypes, vararg val locations: Location) {
    StandardSpawns (ImplingSpawnTypes.Standard, 
        Location.create(2204, 3232, 0),
        Location.create(2582, 2974, 0),
        Location.create(2522, 3105, 0),
        Location.create(2470, 3221, 0),
        Location.create(2593, 3251, 0),
        Location.create(2735, 3354, 0),
        Location.create(2646, 3424, 0),
        Location.create(2462, 3429, 0),
        Location.create(2386, 3513, 0),
        Location.create(2335, 3649, 0),
        Location.create(2740, 3536, 0),
        Location.create(2654, 3609, 0),
        Location.create(2724, 3769, 0),
        Location.create(2817, 3513, 0),
        Location.create(2844, 3154, 0),
        Location.create(2844, 3033, 0),
        Location.create(2841, 2926, 0),
        Location.create(2907, 3491, 0),
        Location.create(3020, 3525, 0),
        Location.create(3021, 3424, 0),
        Location.create(2981, 3276, 0),
        Location.create(3135, 3377, 0),
        Location.create(3149, 3233, 0),
        Location.create(3170, 3004, 0),
        Location.create(3239, 3289, 0),
        Location.create(3287, 3271, 0),
        Location.create(3418, 3124, 0),
        Location.create(3356, 3010, 0),
        Location.create(3550, 3529, 0),
        Location.create(3449, 3488, 0),
        Location.create(3441, 3352, 0)
    ),
    LowTierOnlySpawns (ImplingSpawnTypes.LowTierOnly,
        Location.create(2348, 3610, 0),
        Location.create(2277, 3186, 0),
        Location.create(2459, 3085, 0),
        Location.create(2564, 3393, 0),
        Location.create(2780, 3463, 0),
        Location.create(2966, 3411, 0),
        Location.create(3094, 3237, 0),
        Location.create(3281, 3427, 0),
        Location.create(3278, 3160, 0)
    )
}

object ImplingClipper : ClipMaskSupplier {
    override fun getClippingFlag (z: Int, x: Int, y: Int) : Int {
        var flag = RegionManager.getClippingFlag(z, x, y)
        return flag and (RegionFlags.SOLID_TILE.inv()) and (RegionFlags.TILE_OBJECT.inv()) //Allow walking on water and flying over small objects, but keep all other tile flags the same.
    }
}
