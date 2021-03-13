package rs09.game.node.entity.skill.runecrafting

import core.cache.def.impl.ItemDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Plugin
import org.rs09.consts.Items
import rs09.tools.stringtools.colorize

/**
 * Handles the rune pouches.
 * @author Ceikry
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


        if(player.pouchManager.isDecayedPouch(node.id)){
            player.debug("E2")
            when(option) { //Handling for IF the pouch has already completely decayed
                "drop" -> player.dialogueInterpreter.open(9878,Item(node.id))
                else -> player.sendMessage(colorize("%RThis pouch has completely decayed and needs to be repaired."))
            }
        } else {
            player.debug("E")
            when (option) { //Normal handling
                "fill" -> player.pouchManager.addToPouch(node.id, essence.amount, essence.id)
                "empty" -> player.pouchManager.withdrawFromPouch(node.id)
                "check" -> player.pouchManager.checkAmount(node.id)
                "drop" -> player.dialogueInterpreter.open(9878,Item(node.id))
            }
        }
        return true
    }

    override fun isWalk(): Boolean {
        return false
    }
}