package rs09.game.content.quest.members.thefremenniktrials

import api.*
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item

class Draugen(val player: Player) : NPC(1279,player.location?.transform(1,0,0)){

    override fun init() {
        super.init()
        isRespawn = false
    }

    override fun handleTickActions() {
        super.handleTickActions()
        if(!properties.combatPulse.isAttacking){
            properties.combatPulse.attack(player)
        }
        if (!player.isActive) {
            removeAttribute(player, "fremtrials:draugen-spawned")
            this.clear()
        }
    }

    override fun finalizeDeath(killer: Entity?) {
        super.finalizeDeath(killer)
        setAttribute(player, "/save:fremtrials:draugen-killed",true)
        removeAttribute(player, "fremtrials:draugen-loc")
        removeItem(player, Item(3696), Container.INVENTORY)
        addItem(player, 3697, 1, Container.INVENTORY)
    }
}