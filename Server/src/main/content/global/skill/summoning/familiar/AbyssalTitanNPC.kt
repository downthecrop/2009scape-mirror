package content.global.skill.summoning.familiar

import core.api.Container
import core.api.addItem
import core.api.amountInInventory
import core.api.removeItem
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs

/**
 * Represents the Abyssal Titan familiar.
 * @author Aero
 * @author Splinter
 */
@Initializable
class AbyssalTitanNPC
constructor(owner: Player? = null, id: Int = NPCs.ABYSSAL_TITAN_7349) :
    BurdenBeast(owner, id, 3200, Items.ABYSSAL_TITAN_POUCH_12796, 6, 7, WeaponInterface.STYLE_ACCURATE) {
    override fun construct(owner: Player, id: Int): Familiar {
        return AbyssalTitanNPC(owner, id)
    }

    override fun isAllowed(owner: Player, item: Item): Boolean {
        return when (item.id) {
            Items.RUNE_ESSENCE_1436, Items.PURE_ESSENCE_7936 -> super.isAllowed(owner, item)
            else -> {
                owner.packetDispatch.sendMessage("Your familiar can only hold unnoted essence.")
                false
            }
        }
    }

    override fun specialMove(special: FamiliarSpecial): Boolean {
        val playerRuneEssenceAmount = amountInInventory(owner, Items.RUNE_ESSENCE_1436)
        val playerPureEssenceAmount = amountInInventory(owner, Items.PURE_ESSENCE_7936)
        val beastRuneEssenceAmount = this.container.getAmount(Items.RUNE_ESSENCE_1436)
        val beastPureEssenceAmount = this.container.getAmount(Items.PURE_ESSENCE_7936)
        val totalRuneEssence = playerRuneEssenceAmount + beastRuneEssenceAmount
        val totalPureEssence = playerPureEssenceAmount + beastPureEssenceAmount

        if ((totalRuneEssence + totalPureEssence) == 0) {
            owner.sendMessage("You have no essence to send to the bank.")
            return false
        }

        if (!owner.bank.hasSpaceFor(
                Item(Items.RUNE_ESSENCE_1436, totalRuneEssence),
                Item(Items.PURE_ESSENCE_7936, totalPureEssence)
            )
        ) {
            owner.sendMessage("You have no space in your bank to deposit your essence.")
            return false
        }


        if (removeItem(owner, Item(Items.RUNE_ESSENCE_1436, playerRuneEssenceAmount))) {
            addItem(owner, Items.RUNE_ESSENCE_1436, playerRuneEssenceAmount, Container.BANK)
        }

        if (removeItem(owner, Item(Items.PURE_ESSENCE_7936, playerPureEssenceAmount))) {
            addItem(owner, Items.PURE_ESSENCE_7936, playerPureEssenceAmount, Container.BANK)
        }

        if (this.container.remove(Item(Items.RUNE_ESSENCE_1436, beastRuneEssenceAmount))) {
            addItem(owner, Items.RUNE_ESSENCE_1436, beastRuneEssenceAmount, Container.BANK)
        }

        if (this.container.remove(Item(Items.PURE_ESSENCE_7936, beastPureEssenceAmount))) {
            addItem(owner, Items.PURE_ESSENCE_7936, beastPureEssenceAmount, Container.BANK)
        }

        owner.sendMessage("The titan sends $totalRuneEssence rune essence and $totalPureEssence pure essence to your bank.")
        return true
    }

    override fun visualizeSpecialMove() {
        owner.visualize(Animation.create(7660), Graphics.create(1316))
        visualize(Animation.create(7694), Graphics.create(1457))
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ABYSSAL_TITAN_7349, NPCs.ABYSSAL_TITAN_7350)
    }
}