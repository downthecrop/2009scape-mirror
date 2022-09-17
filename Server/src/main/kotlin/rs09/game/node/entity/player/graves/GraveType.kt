package rs09.game.node.entity.player.graves

import org.rs09.consts.NPCs

enum class GraveType(val npcId: Int, val cost: Int, val durationMinutes: Int, val isMembers: Boolean, val requiredQuest: String = "", val text: String) {
    MEM_PLAQUE(NPCs.GRAVE_MARKER_6565, 0, 2, false, text = "In memory of @name,<br>who died here."),
    FLAG(NPCs.GRAVE_MARKER_6568, 50, 2, false, text = MEM_PLAQUE.text),
    SMALL_GS(NPCs.GRAVESTONE_6571, 500, 2, false, text = "In loving memory of our dear friend @name,<br>who died in this place @mins ago."),
    ORNATE_GS(NPCs.GRAVESTONE_6574, 5000, 3, false, text = SMALL_GS.text),
    FONT_OF_LIFE(NPCs.GRAVESTONE_6577, 50000, 4, true, text = "In your travels,<br>pause awhile to remember @name,<br>who passed away at this spot."),
    STELE(NPCs.STELE_6580, 50000, 4, true, text = FONT_OF_LIFE.text),
    SARA_SYMBOL(NPCs.SARADOMIN_SYMBOL_6583, 50000, 4, true, text = "@name,<br>an enlightened servant of Saradomin,<br>perished in this place."),
    ZAM_SYMBOL(NPCs.ZAMORAK_SYMBOL_6586, 50000, 4, true, text = 	"@name,<br>a most bloodthirsty follower of Zamorak,<br>perished in this place."),
    GUTH_SYMBOL(NPCs.GUTHIX_SYMBOL_6589, 50000, 4, true, text = "@name,<br>who walked with the Balance of Guthix,<br>perished in this place."),
    BAND_SYMBOL(NPCs.BANDOS_SYMBOL_6592, 50000, 4, true, requiredQuest = "Land of the Goblins", text = "@name,<br>a vicious warrior dedicated to Bandos,<br>perished in this place. "),
    ARMA_SYMBOL(NPCs.ARMADYL_SYMBOL_6595, 50000, 4, true, requiredQuest = "Temple of Ikov", text = "@name,<br>a follower of the Law of Armadyl,<br>perished in this place."),
    ZARO_SYMBOL(NPCs.MEMORIAL_STONE_6598, 50000, 4, true, requiredQuest = "Desert Treasure", text = "@name,<br>servant of the Unknown Power,<br>perished in this place."),
    ANGEL_DEATH(NPCs.MEMORIAL_STONE_6601, 500000, 5, true, text = "Ye frail mortals who gaze upon this sight,<br>forget not the fate of @name, once mighty, now<br>surrendered to the inescapable grasp of destiny.<br><i>Requiescat in pace.</i>");

    companion object {
        val ids = values().fold(ArrayList<Int>()) {list, type ->
            list.add(type.npcId)
            list.add(type.npcId + 1)
            list.add(type.npcId + 2)
            list
        }.toIntArray()
    }
}