package content.global.skill.cooking.fermenting

import core.api.*
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Items
import org.rs09.consts.Scenery


const val KELDAGRIM_LOC = 11679
const val PORT_PHASMATYS_LOC = 14747

val varbits = mapOf(
    PORT_PHASMATYS_LOC to BrewingVat.PORT_PHAS,
    KELDAGRIM_LOC to BrewingVat.KELDAGRIM
)

class BrewingListener : InteractionListener {

    private val aleIngredientsId = Brewable.getIngredients()

    private val vatsNeedingYeast = intArrayOf(
        Scenery.FERMENTING_VAT_7444, // Hammerstone hops
        Scenery.FERMENTING_VAT_7449, // Asgarnian hops
        Scenery.FERMENTING_VAT_7454, // Harralander
        Scenery.FERMENTING_VAT_7459, // Yanillian hops
        Scenery.FERMENTING_VAT_7464, // Krandorian hops
        Scenery.FERMENTING_VAT_7469, // Bittercap mushrooms
        Scenery.FERMENTING_VAT_7474, // Oak roots
        Scenery.FERMENTING_VAT_7479, // Chocolate dust
        Scenery.FERMENTING_VAT_7484, // Wildblood hops
        Scenery.FERMENTING_VAT_7489, // Apple mush
        Scenery.FERMENTING_VAT_8871  // Kelda hops
    )

    // Const Lib is missing some of these
    private val drainBarrels = intArrayOf(
        Scenery.BARREL_7408,
        Scenery.BARREL_7409,
        Scenery.BARREL_7410,

    )
    private val barrels = intArrayOf(
        Scenery.KELDA_STOUT_8870,
        Scenery.DWARVEN_STOUT_7411,
        Scenery.ASGARNIAN_ALE_7413,
        Scenery.GREENMANS_ALE_7415,
        Scenery.WIZARDS_MIND_BOMB_7417,
        Scenery.DRAGON_BITTER_7419,
        Scenery.MOONLIGHT_MEAD_7421,
        Scenery.AXEMAN_S_FOLLY_7423,
        Scenery.CHEF_S_DELIGHT_7425,
        Scenery.SLAYER_S_RESPITE_7427,
        Scenery.CIDER_7429,
        Scenery.MATURE_DWARVEN_STOUT_7412,
        Scenery.MATURE_ASGARNIAN_ALE_7414,
        Scenery.MATURE_GREENMANS_ALE_7416,
        Scenery.MATURE_WIZARDS_MIND_BOMB_7418,
        Scenery.MATURE_DRAGON_BITTER_7420,
        Scenery.MATURE_MOONLIGHT_MEAD_7422,
        Scenery.MATURE_AXEMAN_S_FOLLY_7424,
        Scenery.MATURE_CHEF_S_DELIGHT_7426,
        Scenery.MATURE_SLAYER_S_RESPITE_7428,
        Scenery.MATURE_CIDER_7430
    )


    override fun defineListeners() {

        // Fill an empty vat with water - self-contained operation
        onUseWith(SCENERY, Items.BUCKET_OF_WATER_1929, Scenery.FERMENTING_VAT_7437) { player, _, _ ->
            if (removeItem(player, Item(Items.BUCKET_OF_WATER_1929, 2))) {
                queueScript(player, 0) {
                    addItem(player, Items.BUCKET_1925, 2)

                    lock(player, Animation(2283).duration)
                    animate(player, Animation(2283))

                    sendMessage(player, "You add some water to the vat.")

                    val vatVarbit = varbits.getValue(player.location.regionId).varbit
                    setVarbit(player, vatVarbit, 1, true)
                    return@queueScript (stopExecuting(player))
                }
            }
            else{
                sendDialogue(player, "You need 2 buckets of water.")
            }
            return@onUseWith true
        }

        // Fill an empty vat with apple mush
        onUseWith(SCENERY, Items.APPLE_MUSH_5992, Scenery.FERMENTING_VAT_7437) { player, used, _ ->
            getVat(player).addIngredient(used as Item)
            return@onUseWith true
        }

        // Add barley malt to a vat with water - self-contained operation
        onUseWith(SCENERY, Items.BARLEY_MALT_6008, Scenery.FERMENTING_VAT_7438) { player, _, _ ->
            if (inInventory(player, Items.BARLEY_MALT_6008, 2)) {
                queueScript(player, 0, QueueStrength.SOFT) { counter ->
                    when (counter) {
                        0 -> {
                            if (!removeItem(player, Item(Items.BARLEY_MALT_6008, 1))) { return@queueScript stopExecuting(player) }

                            lock(player, Animation(2295).duration * 2 + 2)
                            animate(player, Animation(2295))

                            sendMessage(player, "You add some barley malt to the vat.")

                            return@queueScript delayScript(player, Animation(2295).duration + 1)
                        }
                        1 -> {
                            if (!removeItem(player, Item(Items.BARLEY_MALT_6008, 1))) { return@queueScript stopExecuting(player) }

                            stopWalk(player)
                            animate(player, Animation(2295))

                            val vatVarbit = varbits.getValue(player.location.regionId).varbit
                            setVarbit(player, vatVarbit, 2, true)

                            return@queueScript delayScript(player, Animation(2295).duration + 1)
                        }
                        2 -> {
                            stopWalk(player)
                            return@queueScript stopExecuting(player)
                        }
                        else -> return@queueScript stopExecuting(player)
                    }
                }
            }
            else{
                sendDialogue(player, "You need 2 barley malt.")
            }
            return@onUseWith true
        }

        // Add The Stuff to a vat with malt
        onUseWith(SCENERY, Items.THE_STUFF_8988, Scenery.FERMENTING_VAT_7441) { player, _, _ ->
            if (inInventory(player, Items.THE_STUFF_8988, 1)) {
                getVat(player).addTheStuff()
            }
            return@onUseWith true
        }

        // Add ingredients to a vat with malt
        onUseWith(SCENERY, aleIngredientsId, Scenery.FERMENTING_VAT_7441) { player, used, _ ->
            if (used.id == Items.APPLE_MUSH_5992) {
                sendDialogue(player, "Apple mush needs to be added to an empty vat.")
                return@onUseWith false
            }
            // If you try to make Kelda Stout in Port Phasmatys the vat deletes itself forever
            if (used.id == Items.KELDA_HOPS_6113 && player.location.regionId != KELDAGRIM_LOC) {
                sendMessage(player, "Nothing interesting happens.")
                return@onUseWith false
            }
            // Branch off into Kelda Stout logic
            if (used.id == Items.KELDA_HOPS_6113 && player.location.regionId == KELDAGRIM_LOC) {
                KeldaBypasses.keldaAddHops(player)
                return@onUseWith true
            }
            getVat(player).addIngredient(used as Item)
            return@onUseWith true
        }

        // Add yeast to a vat that's ready to brew
        onUseWith(SCENERY, Items.ALE_YEAST_5767, *vatsNeedingYeast) { player, _, with ->
            if (inInventory(player, Items.ALE_YEAST_5767, 1)) {
                // Branch off into Kelda Stout logic
                if (with.id == Scenery.FERMENTING_VAT_8871 &&
                    player.location.regionId == KELDAGRIM_LOC &&
                    getVarbit(player, 736) == 68) { // 68 = Kelda MIXED state
                    KeldaBypasses.keldaAddYeast(player)
                    return@onUseWith true
                }
                getVat(player).addYeast()
            }
            return@onUseWith true
        }

        // Turn the valve to empty a vat into a barrel
        on(intArrayOf(Scenery.VALVE_7442, Scenery.VALVE_7443), SCENERY, "turn") { player, _ ->
            // Branch off into Kelda Stout logic
            when {
                (player.location.regionId == KELDAGRIM_LOC && KeldaBypasses.keldaIsBrewing(player)) ->
                    sendMessage(player, "The contents of the vat haven't finished fermenting yet.")
                (player.location.regionId == KELDAGRIM_LOC && KeldaBypasses.keldaIsComplete(player)) ->
                    KeldaBypasses.keldaTurnValve(player)
                else -> getVat(player).turnValve()
            }
            return@on true
        }

        // Drain a barrel of bad ale
        on(drainBarrels, SCENERY, "Drain") { player, _ ->
            getVat(player).drainBarrel()
            return@on true
        }

        // Level a barrel (clicking on it)
        on(barrels, SCENERY, "Level") { player, _ ->
            queueScript(player, 1) {
                val goAgain : Boolean
                if (inInventory(player, Items.BEER_GLASS_1919)) {
                    goAgain = getVat(player).levelBarrel(Items.BEER_GLASS_1919)
                }
                else if (inInventory(player, Items.CALQUAT_KEG_5769)) {
                    goAgain = getVat(player).levelBarrel(Items.CALQUAT_KEG_5769)
                }
                else{
                    goAgain = false
                    sendDialogue(player, "You need a container to level the barrel.")
                }
                return@queueScript if(goAgain) delayScript(player, 1) else stopExecuting(player)
            }
            return@on true
        }

        // Level a barrel (using a container on it)
        onUseWith(SCENERY, intArrayOf(Items.BEER_GLASS_1919, Items.CALQUAT_KEG_5769), *barrels) { player, used, _ ->
            getVat(player).levelBarrel(used.id)
            return@onUseWith true
        }
    }

    private fun getVat(player: Player) : Vat {
        return  varbits.getValue(player.location.regionId).getVat(player)
    }
}