package core.game.node.entity.combat

import content.global.skill.summoning.familiar.Familiar
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Direction.getDirection
import core.game.world.map.RegionManager.getLocalEntities
import core.game.world.map.zone.impl.WildernessZone

private fun isAttackable(victim: Entity, attacker: Entity, combatStyle: CombatStyle, wilderness: Boolean): Boolean {
    if (wilderness) {
        return WildernessZone.getInstance().continueAttack(attacker, victim, combatStyle, false)
    }
    return victim.isAttackable(attacker, combatStyle, false)
}

fun findMultihitTargets(target: Entity, attacker: Entity, combatStyle: CombatStyle): List<Entity> {
    val targets = getLocalEntities(target.location, 1).filter { it != attacker && it != target && isAttackable(it, attacker, combatStyle, false) }
    return listOf(target) + targets.take(9) // https://runescape.wiki/w/Vesta%27s_spear "the opponent and up to 9 additional targets in a 3x3 area around the player"; https://oldschool.runescape.wiki/w/Ice_Barrage "up to nine targets"
}

fun findMultihitTargetsForDragonHalberd(target: Entity, attacker: Entity): List<Entity> {
    val direction = getDirection(target.location.x - attacker.location.x, target.location.y - attacker.location.y)
    val targets = ArrayList<Entity>() // should not add the victim here
    for (delta in listOf(-1, 1)) {
        val add = getLocalEntities(target.location.transform(delta * direction.stepY, delta * direction.stepX, 0), 0)
        targets += add.filter { it != attacker && isAttackable(it, attacker, CombatStyle.RANGE, false) }.take(1) // RANGE is correct because halberds are able to attack from a distance
    }
    return targets
}

fun findMultihitTargetsForD2h(target: Entity, attacker: Entity, combatStyle: CombatStyle): List<Entity> {
    val targets = getLocalEntities(attacker.location, 1).filter { it != attacker && it != target && isAttackable(it, attacker, combatStyle, false) }
    return listOf(target) + targets.filter { it.location.withinDistance(attacker.location, 1) }.take(14) // runescape.wiki/w/Dragon_2h_sword?oldid=859949 "up to 14 enemies in the squares around the player (Only directly in front of and to the sides [and behind], it does not attack those diagonal of the player)"
}

fun findMultihitTargetsForChinchompa(target: Entity, attacker: Entity): List<Entity> {
    val allEntities = getLocalEntities(target.location, 1)
    val targetCandidates = allEntities.filter { it != attacker && it != target && isAttackable(it, attacker, CombatStyle.RANGE, false) }
    val targets: List<Entity>?
    if (target is Player) {
        val potentialOwners = allEntities.filterIsInstance<Player>()
        fun isFamiliarOf(entity: Entity, players: List<Player>): Boolean {
            if (entity !is Familiar) {
                return false
            }
            return entity.owner in players && isAttackable(entity.owner, attacker, CombatStyle.RANGE, true)
        }
        targets = targetCandidates.filter { (it is Player && isAttackable(it, attacker, CombatStyle.RANGE, true)) || isFamiliarOf(it, potentialOwners) }
    } else {
        val potentialFamiliars = allEntities.filterIsInstance<NPC>()
        fun isOwnerOf(entity: Entity, npcs: List<NPC>): Boolean {
            if (entity !is Player) {
                return false
            }
            for (npc in npcs) {
                if (npc is Familiar && npc.owner == entity) {
                    return isAttackable(entity, attacker, CombatStyle.RANGE, true)
                }
            }
            return false
        }
        targets = targetCandidates.filter { it is NPC || isOwnerOf(it, potentialFamiliars) }
    }
    return listOf(target) + targets.take(9) // https://runescape.wiki/w/Chinchompa "up to 9 enemies within one tile of the target" for a total of 10
}
