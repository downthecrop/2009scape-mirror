package content.global.skill.magic

import core.game.node.entity.combat.spell.CombinationRune
import core.game.node.entity.combat.spell.MagicStaff
import core.game.node.entity.combat.spell.Runes
import core.game.node.entity.player.Player
import core.game.node.item.Item
import kotlin.math.min

object SpellUtils {
    /**
     * Validates that the spell packet's interface matches the player's actual server-side spellbook.
     * This prevents exploits where the client has a stale spellbook interface (e.g., after Spellbook Swap reverts).
     * @param player The player casting the spell
     * @param packetInterfaceId The interface ID from the client's spell packet
     * @return true if the interfaces match (valid), false if there's a client/server desync
     */
    @JvmStatic
    fun validateSpellbookInterface(player : Player, packetInterfaceId : Int) : Boolean {
	val actualSpellbook = player.spellBookManager.spellBook
	return packetInterfaceId == actualSpellbook
    }
    fun usingStaff(p: Player, rune: Int): Boolean {
        val weapon = p.equipment[3] ?: return false
        val staff = MagicStaff.forId(rune) ?: return false
        val staves = staff.staves
        for (id in staves) {
            if (weapon.id == id) {
                return true
            }
        }
        return false
    }

    /**
     * Validates if the player has the necessary runes to cast a spell.
     *
     * If the player is able to cast the spell, the "spell:runes" attribute will be set to the list of items that should
     * be removed from the player's inventory after successfully casting the spell. This accounts for staves and
     * combination runes.
     *
     * @param p The player casting the spell
     * @param runes The runes and other items required to cast the spell
     * @return null if the player can cast the spell or an Item representing at least one of the runes the player is missing
     */
    @JvmStatic
    fun hasRunes(p: Player, runes: Array<Item>): Item? {
        val cost = HashMap<Int, Int>()
        // `runes` are mostly actual runes but occasionally other items like staves or unpowered orbs
        for (rune in runes) {
            if (usingStaff(p, rune.id)) continue
            cost[rune.id] = cost.getOrDefault(rune.id, 0) + rune.amount
        }

        val toRemove = ArrayList<Item>()

        // Combination runes are used before elemental runes.
        // https://runescape.wiki/w/Runecrafting?oldid=2618332#Function_and_Usage_of_Combination_Runes:
        for (combo in CombinationRune.values()) {
            val available = p.inventory.getAmount(combo.id)
            val maxUsage = combo.types.mapNotNull { cost[it.id] }.maxOrNull() ?: 0
            if (maxUsage > 0 && available >= 0) {
                val usage = min(maxUsage, available)

                toRemove.add(Item(combo.id, usage))
                // Even if a spell uses both parts of a combo rune, it should only consume a single rune. For example,
                // a spell that requires an air rune and an earth rune should only consume a single dust rune.
                // https://youtu.be/9gAiqEmF-Hc?t=67
                combo.types.forEach { cost[it.id] = cost.getOrDefault(it.id, 0) - usage }
            }
        }

        for ((runeId, amount) in cost) {
            if (amount <= 0) continue

            val available = p.inventory.getAmount(runeId)
            if (available < amount) {
                return Item(runeId, amount)
            }

            toRemove.add(Item(runeId, amount))
        }

        p.setAttribute("spell:runes", toRemove)
        return null
    }

    @JvmStatic
    fun getBookFromInterface(id: Int): String{
        return when(id){
            192 -> "modern"
            193 -> "ancient"
            430 -> "lunar"
            else -> "none"
        }
    }
}