package rs09.game.content.ame.events.evilchicken

import api.getWorldTicks
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
    val phrases = arrayOf("Bwuk","Bwuk bwuk bwuk","Flee from me, @name!","Begone, @name!","Bwaaaauuuk bwuk bwuk","MUAHAHAHAHAAA!")
    override fun talkTo(npc: NPC) {}

    override fun init() {
        super.init()
        val index = (player.properties.combatLevel / 20) - 1
        val id = ids.toList()[index]
        this.transform(id)
        this.attack(player)
        sendChat(phrases.random().replace("@name",player.name.capitalize()))
        this.isRespawn = false
    }

    override fun finalizeDeath(killer: Entity?) {
        super.finalizeDeath(killer)
        GroundItemManager.create(Item(Items.FEATHER_314, RandomFunction.random(50,300)), this.dropLocation, player)
    }

    override fun tick() {
        if(!player.location.withinDistance(this.location,8)){
            this.terminate()
        }
        if(getWorldTicks() % 10 == 0){
            sendChat(phrases.random().replace("@name",player.name.capitalize()))
        }
        super.tick()
        if(!player.viewport.currentPlane.npcs.contains(this)) this.clear()
    }
}