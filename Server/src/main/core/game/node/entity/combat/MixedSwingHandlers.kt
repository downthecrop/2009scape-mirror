package core.game.node.entity.combat

import core.game.node.entity.Entity

/**
 * A basket of swing handlers for use by various slayer monsters which use different
 * stats to calculate their offence compared to their victim's defence.
 */

/**
 * Magical Ranged: NPC rolls ranged offence, victim rolls magic defence
 * TODO: Give this to waterfiends, zygomites, and mithril dragons
 * https://runescape.wiki/w/Waterfiend?oldid=4067322
 * https://runescape.wiki/w/Mutated%20zygomite?oldid=4039951
 * https://oldschool.runescape.wiki/w/Mithril_dragon
 */
object MagicalRangedSwingHandler : RangeSwingHandler() {
    override fun calculateDefence(victim: Entity?, attacker: Entity?): Int {
        return CombatStyle.MAGIC.swingHandler.calculateDefence(victim, attacker)
    }
}

/**
 * Magical Melee: NPC rolls melee offence, victim rolls magic defence
 * TODO: Give this to banshees, bloodvelds, cave horrors, jellies, icefiends, and pyrefiends
 * https://runescape.wiki/w/Banshee?oldid=4069943
 * https://runescape.wiki/w/Bloodveld?oldid=4068546
 * https://runescape.wiki/w/Cave%20horror?oldid=4068892
 * https://runescape.wiki/w/Jelly?oldid=4053448
 * https://oldschool.runescape.wiki/w/Icefiend
 * https://runescape.wiki/w/Pyrefiend?oldid=4069708
 */
object MagicalMeleeSwingHandler : MeleeSwingHandler() {
    override fun calculateDefence(victim: Entity?, attacker: Entity?): Int {
        return CombatStyle.MAGIC.swingHandler.calculateDefence(victim, attacker)
    }
}

/**
 * Ranged Melee: NPC rolls melee offence, victim rolls ranged defence
 * TODO: Give this to dust devils
 * https://oldschool.runescape.wiki/w/Dust_devil
 */
object RangedMeleeSwingHandler : MeleeSwingHandler() {
    override fun calculateDefence(victim: Entity?, attacker: Entity?): Int {
        return CombatStyle.RANGE.swingHandler.calculateDefence(victim, attacker)
    }
}