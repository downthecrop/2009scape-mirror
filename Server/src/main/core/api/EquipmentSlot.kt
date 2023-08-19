package core.api

enum class EquipmentSlot {
    HEAD,
    CAPE,
    NECK,
    WEAPON,
    CHEST,
    SHIELD,
    HIDDEN_1,
    LEGS,
    HIDDEN_2,
    HANDS,
    FEET,
    HIDDEN_3,
    RING,
    AMMO;

    companion object {
        private val slotMap = HashMap<String, EquipmentSlot>()

        init {
            slotMap["head"] = HEAD; slotMap["helm"] = HEAD; slotMap ["helmet"] = HEAD
            slotMap["cape"] = CAPE; slotMap["back"] = CAPE
            slotMap["neck"] = NECK; slotMap["amulet"] = NECK
            slotMap["weapon"] = WEAPON; slotMap["main"] = WEAPON
            slotMap["chest"] = CHEST; slotMap["body"] = CHEST; slotMap["torso"] = CHEST
            slotMap["shield"] = SHIELD; slotMap["off"] = SHIELD
            slotMap["legs"] = LEGS; slotMap["leg"] = LEGS
            slotMap["hands"] = HANDS; slotMap["hand"] = HANDS; slotMap["brace"] = HANDS; slotMap["bracelet"] = HANDS
            slotMap["feet"] = FEET
            slotMap["ring"] = RING
            slotMap["ammo"] = AMMO; slotMap["ammunition"] = AMMO
        }

        /**
         * Return the equipment slot by name. Return null if matching no slot.
         */
        fun slotByName(input: String): EquipmentSlot? {
            return slotMap[input.lowercase()]
        }
    }
}