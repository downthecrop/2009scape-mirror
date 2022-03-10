package rs09.game.content.ame.events.treespirit

import api.addItemOrDrop
import api.getWorldTicks
import api.produceGroundItem
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.tools.RandomFunction
import org.rs09.consts.Items
import rs09.game.content.ame.RandomEventNPC
import rs09.game.content.global.WeightBasedTable
import rs09.game.content.global.WeightedItem


val ids = 438..443

class TreeSpiritRENPC(override var loot: WeightBasedTable? = null) : RandomEventNPC(438){
    override fun talkTo(npc: NPC) {}
    override fun init() {
        super.init()
        val index = (player.properties.combatLevel / 20) - 1
        val id = ids.toList()[index]
        this.transform(id)
        this.attack(player)
        sendChat("Leave these woods and never return!")
        this.isRespawn = false
    }
    override fun finalizeDeath(killer: Entity?) {
        super.finalizeDeath(killer)
    }
    override fun tick() {
        if(!player.location.withinDistance(this.location,8)){
            this.terminate()
        }
        super.tick()
        if(!player.viewport.currentPlane.npcs.contains(this)) this.clear()
    }
}