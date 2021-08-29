package rs09.game.interaction.`object`

import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Items
import rs09.game.interaction.InteractionListener
import rs09.game.world.GameWorld
import java.util.concurrent.TimeUnit

private const val CHEST = 2827
class GutanothChestInteractionHandler : InteractionListener(){

    override fun defineListeners() {

        on(CHEST,SCENERY,"open"){ player, node ->
            val delay = player.getAttribute("gutanoth-chest-delay", 0L)
            GameWorld.Pulser.submit(ChestPulse(player,System.currentTimeMillis() > delay, node as Scenery))
            return@on true
        }

    }

    class ChestPulse(val player: Player, val isLoot: Boolean, val chest: Scenery): Pulse(){
        var ticks = 0
        override fun pulse(): Boolean {
            when(ticks++){
                0 -> {
                    player.lock()
                    player.animator.animate(Animation(536))
                    SceneryBuilder.replace(chest, Scenery(2828, chest.location, chest.rotation), 5)
                }
                2 -> {lootChest(player)}
                3 -> {player.unlock(); return true}
            }
            return false
        }


        fun lootChest(player: Player){
            if (isLoot) {
                player.setAttribute("/save:gutanoth-chest-delay", System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(15))
            } else {
                player.sendMessage("You open the chest and find nothing.")
                return
            }
            val reward = Rewards.values().random()
            player.sendChat(reward.message)
            when(reward.type){
                Type.ITEM -> if(!player.inventory.add(Item(reward.id))) GroundItemManager.create(Item(reward.id),player.location)
                Type.NPC -> {
                    val npc = NPC(reward.id)
                    npc.location = player.location
                    npc.isAggressive = true
                    npc.isRespawn = false
                    npc.properties.combatPulse.attack(player)
                    npc.init()
                }
            }
        }

        enum class Rewards(val id: Int,val type: Type,val message: String){
            BONES(Items.BONES_2530, Type.ITEM, "Oh! Some bones. Delightful."),
            EMERALD(Items.EMERALD_1605, Type.ITEM, "Ooh! A lovely emerald!"),
            ROTTEN_APPLE(Items.ROTTEN_APPLE_1984, Type.ITEM, "Oh, joy, spoiled fruit! My favorite!"),
            CHAOS_DWARF(119, Type.NPC, "You've gotta be kidding me, a dwarf?!"),
            RAT(47, Type.NPC, "Eek!"),
            SCORPION(1477, Type.NPC, "Zoinks!"),
            SPIDER(1004, Type.NPC, "Awh, a cute lil spidey!")
        }

        enum class Type {
            ITEM,
            NPC
        }
    }
}