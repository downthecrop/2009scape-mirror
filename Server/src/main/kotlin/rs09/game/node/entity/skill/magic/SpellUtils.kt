package rs09.game.node.entity.skill.magic

import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.magic.CombinationRune
import core.game.node.entity.skill.magic.MagicStaff
import core.game.node.entity.skill.magic.Runes
import core.game.node.item.Item

object SpellUtils {
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

    fun hasRune(p:Player,rune:Item):Boolean{
        val removeItems = p.getAttribute("spell:runes",ArrayList<Item>())
        if(usingStaff(p,rune.id)) return true
        if(p.inventory.containsItem(rune)){
            removeItems.add(rune)
            p.setAttribute("spell:runes",removeItems)
        }

        val baseAmt = p.inventory.getAmount(rune.id)
        var amtRemaining = rune.amount - baseAmt
        val possibleComboRunes = CombinationRune.eligibleFor(Runes.forId(rune.id))
        for (r in possibleComboRunes) {
            if (p.inventory.containsItem(Item(r.id)) && amtRemaining > 0) {
                val amt = p.inventory.getAmount(r.id)
                if (amtRemaining <= amt) {
                    removeItems.add(Item(r.id,amtRemaining))
                    amtRemaining = 0
                    break
                }
                removeItems.add(Item(r.id,p.inventory.getAmount(r.id)))
                amtRemaining -= p.inventory.getAmount(r.id)
            }
        }
        p.setAttribute("spell:runes",removeItems)
        return amtRemaining <= 0
    }

    fun hasRune(p: Player, item: Item, toRemove: MutableList<Item?>, message: Boolean): Boolean {
        if (!usingStaff(p, item.id)) {
            val hasBaseRune = p.inventory.contains(item.id, item.amount)
            if (!hasBaseRune) {
                val baseAmt = p.inventory.getAmount(item.id)
                if (baseAmt > 0) {
                    toRemove.add(Item(item.id, p.inventory.getAmount(item.id)))
                }
                var amtRemaining = item.amount - baseAmt
                val possibleComboRunes = CombinationRune.eligibleFor(Runes.forId(item.id))
                for (r in possibleComboRunes) {
                    if (p.inventory.containsItem(Item(r.id)) && amtRemaining > 0) {
                        val amt = p.inventory.getAmount(r.id)
                        if (amtRemaining < amt) {
                            toRemove.add(Item(r.id, amtRemaining))
                            amtRemaining = 0
                            continue
                        }
                        amtRemaining -= p.inventory.getAmount(r.id)
                        toRemove.add(Item(r.id, p.inventory.getAmount(r.id)))
                    }
                }
                return if (amtRemaining <= 0) {
                    true
                } else {
                    p.packetDispatch.sendMessage("You don't have enough " + item.name + "s to cast this spell.")
                    false
                }
            }
            toRemove.add(item)
            return true
        }
        return true
    }

    fun attackableNPC(npc: NPC): Boolean{
        return npc.definition.hasAction("attack")
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