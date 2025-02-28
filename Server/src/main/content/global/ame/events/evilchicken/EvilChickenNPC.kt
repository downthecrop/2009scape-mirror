package content.global.ame.events.evilchicken

import core.api.getWorldTicks
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.tools.RandomFunction
import org.rs09.consts.Items
import content.global.ame.RandomEventNPC
import core.api.utils.WeightBasedTable

val ids = (2463..2468).toList()

class EvilChickenNPC(override var loot: WeightBasedTable? = null) : RandomEventNPC(2463) {
    val phrases = arrayOf("Bwuk","Bwuk bwuk bwuk","Flee from me, @name!","Begone, @name!","Bwaaaauuuk bwuk bwuk","MUAHAHAHAHAAA!")
    override fun talkTo(npc: NPC) {}

    override fun init() {
        super.init()
        val id = idForCombatLevel(ids, player)
        this.transform(id)
        this.attack(player)
        sendChat(phrases.random().replace("@name",player.username.capitalize()))
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
            sendChat(phrases.random().replace("@name",player.username.capitalize()))
        }
        super.tick()
        if(!player.viewport.currentPlane.npcs.contains(this)) this.clear()
    }
}
