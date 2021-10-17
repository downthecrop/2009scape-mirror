package rs09.game.content.ame.events.evilchicken

import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.tools.RandomFunction
import org.rs09.consts.Items
import rs09.game.content.ame.RandomEventNPC
import rs09.game.content.global.WeightBasedTable

val ids = 2463..2468

class EvilChickenNPC(override var loot: WeightBasedTable? = null) : RandomEventNPC(2463) {
    override fun talkTo(npc: NPC) {}

    override fun init() {
        super.init()
        val index = (player.properties.combatLevel / 20) - 1
        val id = ids.toList()[index]

        this.transform(id)
        this.attack(player)
        this.isRespawn = false
    }

    override fun finalizeDeath(killer: Entity?) {
        super.finalizeDeath(killer)
        GroundItemManager.create(Item(Items.FEATHER_314, RandomFunction.random(50,300)), this.dropLocation, player)
    }

    override fun tick() {
        super.tick()
        if(!player.viewport.currentPlane.npcs.contains(this)) this.clear()
    }
}