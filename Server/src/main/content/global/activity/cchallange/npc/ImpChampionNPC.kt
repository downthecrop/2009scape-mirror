package content.global.activity.cchallange.npc

import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs

/**
 * Represents the Imp champion NPC for Champions challenge.
 */

@Initializable
class ImpChampionNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {
    var clearTime = 0
    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return ImpChampionNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.IMP_CHAMPION_3062)
    }

    override fun handleTickActions() {
        super.handleTickActions()
        if (clearTime++ > 288) poofClear(this)
    }

    companion object {
        fun spawnImpChampion(player: Player) {
            val champion = ImpChampionNPC(NPCs.IMP_CHAMPION_3062)
            champion.location = location(3170, 9758, 0)
            champion.isWalks = true
            champion.isAggressive = true
            champion.isActive = false

            if (champion.asNpc() != null && champion.isActive) {
                champion.properties.teleportLocation = champion.properties.spawnLocation
            }

            champion.isActive = true
            GameWorld.Pulser.submit(object : Pulse(0, champion) {
                override fun pulse(): Boolean {
                    champion.init()
                    registerHintIcon(player, champion)
                    champion.attack(player)
                    return true
                }
            })
        }
    }

    override fun checkImpact(state: BattleState) {
        super.checkImpact(state)
        val player = state.attacker
        if (player is Player) {
            val w = player.getExtension<WeaponInterface>(WeaponInterface::class.java)
            if (state.style == CombatStyle.MELEE || state.style == CombatStyle.MAGIC || state.style == CombatStyle.RANGE) {
                state.neutralizeHits()
                state.estimatedHit = state.maximumHit
            }
            if (w.weaponInterface.interfaceId == 10) {
                sendMessage(player, "You cannot use special attack in this challenge.")
                if (state.estimatedHit > -1) {
                    state.estimatedHit = 0
                    return
                }
                if (state.secondaryHit > -1) {
                    state.secondaryHit = 0
                    return
                }
            }
        }
    }

    override fun finalizeDeath(killer: Entity?) {
        if (killer is Player) {
            lock(killer, 2)
            runTask(killer, 1) {
                openInterface(killer, 63)
                setInterfaceText(killer, "Well done, you defeated the Imp Champion!", 63, 2)
                killer.packetDispatch.sendItemZoomOnInterface(Items.CHAMPION_SCROLL_6803, 260, 63, 3)
                setInterfaceText(killer, "160 Slayer Xp", 63, 6)
                setInterfaceText(killer, "160 Hitpoint Xp", 63, 7)
            }
            setVarbit(killer, 1457, 1, true)
            rewardXP(killer, Skills.HITPOINTS, 160.0)
            rewardXP(killer, Skills.SLAYER, 160.0)
            removeAttribute("championsarena:start")
            clearHintIcon(killer)
        }
        clear()
        super.finalizeDeath(killer)
    }
}