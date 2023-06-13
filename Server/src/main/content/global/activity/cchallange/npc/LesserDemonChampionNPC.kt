package content.global.activity.cchallange.npc

import core.api.*
import core.game.container.impl.EquipmentContainer
import core.game.global.action.EquipHandler
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
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
 * Represents the Lesser demon champion NPC for Champions challenge.
 */

@Initializable
class LesserDemonChampionNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {
    var clearTime = 0
    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return LesserDemonChampionNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.LESSER_DEMON_CHAMPION_3064)
    }

    override fun handleTickActions() {
        super.handleTickActions()
        if (clearTime++ > 288) poofClear(this)
    }

    companion object {
        fun spawnLesserDemonChampion(player: Player) {
            val champion = LesserDemonChampionNPC(NPCs.LESSER_DEMON_CHAMPION_3064)
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
            if (!player.equipment[3].hasItemPlugin()) {
                state.neutralizeHits()
                state.estimatedHit = state.maximumHit
            } else {
                EquipHandler.unequip(player, EquipmentContainer.SLOT_WEAPON, id)
                sendMessage(player, "You cannot use weapons in this challenge.")
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
                setInterfaceText(killer, "Well done, you defeated the Lesser Demon Champion!", 63, 2)
                killer.packetDispatch.sendItemZoomOnInterface(Items.CHAMPION_SCROLL_6805, 260, 63, 3)
                setInterfaceText(killer, "592 Slayer Xp", 63, 6)
                setInterfaceText(killer, "592 Hitpoint Xp", 63, 7)
            }
            setVarbit(killer, 1459, 1, true)
            rewardXP(killer, Skills.HITPOINTS, 592.0)
            rewardXP(killer, Skills.SLAYER, 592.0)
            removeAttribute("championsarena:start")
            clearHintIcon(killer)
        }
        clear()
        super.finalizeDeath(killer)
    }
}