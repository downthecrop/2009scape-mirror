package rs09.game.node.entity.skill.crafting.lightsources

enum class LightSources(val emptyID: Int, val fullID: Int, val litID: Int, val levelRequired: Int, val openFlame: Boolean) {
    WHITE_CANDLE(0,36,33,0,true),
    BLACK_CANDLE(0,38,32,0,true),
    TORCH(0,596,594,0,true),
    WHITE_CANDLE_LANTERN(4527,4529,4531,4,true),
    BLACK_CANDLE_LANTERN(4527,4532,4534,4,true),
    OIL_LAMP(4525,4522,4524,12,true),
    OIL_LANTERN(4535,4537,4539,26,false),
    BULLSEYE_LANTERN(4546,4548,4550,49,false),
    SAPPHIRE_LANTERN(0,4701,4702,49,false),
    EMERALD_LANTERN(0,9064,9065,49,false);

    companion object {
        fun forId(id: Int): LightSources? {
            return when (id) {
                36, 33 -> WHITE_CANDLE
                38, 32 -> BLACK_CANDLE
                596, 594 -> TORCH
                4529, 4531 -> WHITE_CANDLE_LANTERN
                4532, 4534 -> BLACK_CANDLE_LANTERN
                4522, 4524 -> OIL_LAMP
                4537, 4539 -> OIL_LANTERN
                4548, 4550 -> BULLSEYE_LANTERN
                4701, 4702 -> SAPPHIRE_LANTERN
                9064, 9065 -> EMERALD_LANTERN
                else -> null
            }
        }
    }
}
