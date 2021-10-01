package rs09.game.node.entity.skill.crafting.lightsources

import core.game.container.Container
import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin

/**
 * Method used to light various light sources
 * @author Ceikry
 */
@Initializable
class LightSourceLighter : UseWithHandler(590,36,38){
    /**
     * For Candles:
     * 36 + tinderbox = 33
     * 38 + tinderbox = 32
     *
     * For torches:
     * 596 + tinderbox = 594
     *
     * For Candle lanterns:
     * 4529 + tinderbox = 4531
     * 4532 + tinderbox = 4534
     * Required Level: 4 Firemaking
     *
     * For oil lamps:
     * 4522 + tinderbox = 4524
     * Required Level: 12 Firemaking
     *
     * For oil lanterns:
     * 4537 + tinderbox = 4539
     * Required Level: 26 Firemaking
     *
     * For bullseye lanterns:
     * 4548 + tinderbox = 4550
     * 4701 + tinderbox = 4702
     * 9064 + tinderbox = 9065
     * Required Level: 49 Firemaking
     */

    override fun newInstance(arg: Any?): Plugin<Any> {
        addHandler(590,   ITEM_TYPE, this)
        addHandler(596,  ITEM_TYPE, this)
        addHandler(4529, ITEM_TYPE, this)
        addHandler(4532, ITEM_TYPE, this)
        addHandler(4522, ITEM_TYPE, this)
        addHandler(4537, ITEM_TYPE, this)
        addHandler(4548, ITEM_TYPE, this)
        addHandler(4701, ITEM_TYPE, this)
        addHandler(9064, ITEM_TYPE, this)
        return this
    }

    override fun handle(event: NodeUsageEvent?): Boolean {
        event ?: return false
        val inventory = event.player.inventory
        val used = if(event.used.id == 590) event.usedWith.asItem() else event.used.asItem() //compensation for bad two-way use with handler shit

        val lightSource = LightSources.forId(used.id)

        lightSource ?: return false

        if(!light(event.player,used,lightSource)){
            event.player.sendMessage("You need a Firemaking level of at least ${lightSource.levelRequired} to light this.")
        }

        return true
    }

    fun Container.replace(item: Item, with: Item){
        remove(item)
        add(with)
    }

    fun light(player: Player, item: Item, lightSource: LightSources): Boolean{
        val requiredLevel = lightSource.levelRequired
        val playerLevel = player.skills.getLevel(Skills.FIREMAKING)

        if(playerLevel < requiredLevel) return false

        player.inventory.replace(item,Item(lightSource.litID))
        return true
    }
}
