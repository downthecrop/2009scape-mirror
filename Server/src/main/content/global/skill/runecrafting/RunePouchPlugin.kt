package content.global.skill.runecrafting

import core.cache.def.impl.ItemDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Plugin
import org.rs09.consts.Items
import core.tools.colorize

/**
 * Handles the rune pouches.
 * @author Ceikry, Player Name
 */
class RunePouchPlugin : OptionHandler() {
    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any>? {
        for (i in 5509..5515) {
            ItemDefinition.forId(i).handlers["option:fill"] = this
            ItemDefinition.forId(i).handlers["option:empty"] = this
            ItemDefinition.forId(i).handlers["option:check"] = this
            ItemDefinition.forId(i).handlers["option:drop"] = this
        }
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        val rEssAmt = player.inventory.getAmount(Items.RUNE_ESSENCE_1436)
        val pEssAmt = player.inventory.getAmount(Items.PURE_ESSENCE_7936)
        var preferenceFlag = 0 //0 -> rune ess, 1 -> pure ess
        if(rEssAmt - pEssAmt == 0 && option == "fill") return true
        if(rEssAmt > pEssAmt) preferenceFlag = 0 else preferenceFlag = 1

        val essence = Item(
                if(preferenceFlag == 0) Items.RUNE_ESSENCE_1436 else Items.PURE_ESSENCE_7936,
                if(preferenceFlag == 0) rEssAmt else pEssAmt
        )

        when (option) {
            "fill"  -> player.pouchManager.addToPouch(node.id, essence.amount, essence.id)
            "empty" -> player.pouchManager.withdrawFromPouch(node.id)
            "check" -> player.pouchManager.checkAmount(node.id)
            "drop"  -> player.dialogueInterpreter.open(9878,Item(node.id))
        }
        return true
    }

    override fun isWalk(): Boolean {
        return false
    }
}
