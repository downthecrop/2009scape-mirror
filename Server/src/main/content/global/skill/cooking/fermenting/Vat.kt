package content.global.skill.cooking.fermenting

import core.api.*
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import org.rs09.consts.Items


class Vat (var player: Player, private val brewingVat : BrewingVat, var theStuff: Boolean,
           var nextBrew: Long, var stage: BrewingStage,
           var brewingItem : Brewable?) {

    private var barrelItem: Brewable? = null

    constructor(player: Player, brewingVat: BrewingVat) :
            this(player, brewingVat, false, 0, BrewingStage.EMPTY, null)

    fun addTheStuff() {
        if (!theStuff && removeItem(player, Item(Items.THE_STUFF_8988, 1))) {
            theStuff = true

            lock(player, Animation(2283).duration)
            animate(player, Animation(2283))

            sendMessage(player, "You add The Stuff to the mixture in the vat.")

        } else { sendMessage(player, "Nothing interesting happens.") }
    }

    fun addIngredient(item: Item): Boolean {
        val ingredient = Brewable.getBrewable(item.id) ?: return false
        if (getDynLevel(player, Skills.COOKING) < ingredient.level) {
            sendDialogue(player, "You need a Cooking level of at least ${ingredient.level} in order to brew ${ingredient.displayName}.")
            return false
        }
        if (!inInventory(player, item.id, ingredient.ingredientAmount)) {
            val ingredientWord = if (ingredient.itemID == Items.MUSHROOM_6004) item.name+"s" else item.name
            sendDialogue(player, "You need 4 $ingredientWord.")
            return false
        }
        queueScript(player, 0, QueueStrength.SOFT) { counter ->
            when (counter) {
                0 -> {
                    if (!removeItem(player, Item(item.id, 1))) { return@queueScript stopExecuting(player) }
                    if (ingredient.itemID == Items.APPLE_MUSH_5992) {
                        addItem(player, Items.BUCKET_1925)
                    }

                    stopWalk(player)
                    animate(player, Animation(2295))

                    val ingredientWord = if (item.id == Items.MUSHROOM_6004) item.name+"s" else item.name
                    sendMessage(player, "You add some $ingredientWord to the vat.")

                    if (ingredient.ingredientAmount == 1) {
                        lock(player, Animation(2295).duration)

                        brewingItem = ingredient
                        stage = BrewingStage.MIXED
                        updateVat()

                        return@queueScript stopExecuting(player)
                    }

                    lock(player, Animation(2295).duration * 4 + 3)
                    return@queueScript delayScript(player, Animation(2295).duration + 1)
                }
                1, 2 -> {
                    if (!removeItem(player, Item(item.id, 1))) { return@queueScript stopExecuting(player) }
                    if (ingredient.itemID == Items.APPLE_MUSH_5992) {
                        addItem(player, Items.BUCKET_1925)
                    }

                    stopWalk(player)
                    animate(player, Animation(2295))

                    return@queueScript delayScript(player, Animation(2295).duration + 1)
                }
                3 -> {
                    if (!removeItem(player, Item(item.id, 1))) { return@queueScript stopExecuting(player) }
                    if (ingredient.itemID == Items.APPLE_MUSH_5992) {
                        addItem(player, Items.BUCKET_1925)
                    }

                    stopWalk(player)
                    animate(player, Animation(2295))

                    brewingItem = ingredient
                    stage = BrewingStage.MIXED
                    updateVat()

                    return@queueScript delayScript(player, Animation(2295).duration)
                }
                4 -> {
                    stopWalk(player)
                    return@queueScript stopExecuting(player)
                }
                else -> return@queueScript stopExecuting(player)
            }
        }
        return true
    }

    fun addYeast() {
        if (removeItem(player, Item(Items.ALE_YEAST_5767, 1))) {
            addItem(player, Items.EMPTY_POT_1931)

            lock(player, Animation(2283).duration)
            animate(player, Animation(2295))

            sendMessage(player, "You add a pot of ale yeast to the vat.")
            sendMessage(player, "The contents of the vat have begun to ferment.")

            stage = BrewingStage.BREWING1
            nextBrew = System.currentTimeMillis() + (brewCycleTime * 600) // ticks to ms
            updateVat()
        } else {
            sendMessage(player, "Nothing interesting happens.")
        }
    }

    fun brew(forceStep: Boolean = false, forceGood: Boolean = false, forceMature: Boolean = false, forceBad: Boolean = false) : Boolean {
        val resp = internalBrew(forceStep, forceGood, forceMature, forceBad)
        updateVat()
        return resp
    }

    fun canBrew() : Boolean {
        return listOf(BrewingStage.BREWING1, BrewingStage.BREWING2).any { it == stage }
    }

    /** The meat of the brewing mechanic lives in the following internalBrew function
     * No one has clearly documented how this should work
     * Period sources are vague, unaccountable, and/or vary wildly in their claims
     * https://x.com/JagexAsh/status/994867369460813824
     * Ash states that it was challenging to follow when he was trying to do it in OSRS
     *
     * If a good source is uncovered, this section can easily be configured to match it
     *
     * Current config results in the following behavior:
     * Cooking level does not play a factor
     * Chance to advance a step (or go bad) - 20%
     * Chance to go bad when advancing      - 25%
     * Chance to mature (w/o The Stuff)     - 05%
     * Chance to mature (w/ The Stuff)      - 50%
     * Time per cycle (in BrewGrowth.kt)    - 6 hours, or 36000 ticks
     */

    private fun internalBrew(forceStep: Boolean, forceGood: Boolean, forceMature: Boolean, forceBad: Boolean):Boolean {
        nextBrew = System.currentTimeMillis() + (brewCycleTime * 600) // Ticks to ms
        if (listOf(BrewingStage.BREWING1, BrewingStage.BREWING2).all{ it != stage}) {
            return false
        }

        // Step advancement check
        if (RandomFunction.random(5) == 0 || forceStep || forceGood || forceMature || forceBad) {
            if ((RandomFunction.random(4) == 0 || forceBad) && !forceGood && !forceMature) {
                stage = BrewingStage.BAD
                return false
            }
            stage = if (stage == BrewingStage.BREWING1) BrewingStage.BREWING2 else BrewingStage.DONE
        }

        // Maturity check
        if (stage == BrewingStage.DONE) {
            sendMessage(player, "Perhaps I should have a look and see if my ${brewingItem!!.displayName} has brewed...")
            val roll = if (theStuff) 2 else 20
            if ((RandomFunction.random(roll) == 0 || forceMature) && !forceGood) {
                stage = BrewingStage.MATURE
            }
            return false
        }
        return true
    }

    fun turnValve(){
        if (stage == BrewingStage.EMPTY) {
            queueScript(player, 1) {
                sendMessage(player, "Nothing interesting happens.")
                return@queueScript stopExecuting(player)
            }
            return
        }
        if (getVarbit(player,this.brewingVat.varbit + 2) > 0) {
            queueScript(player, 1) {
                sendMessage(player, "The barrel is already full.")
                return@queueScript stopExecuting(player)
            }
            return
        }
        if (stage == BrewingStage.BREWING1 || stage == BrewingStage.BREWING2) {
            queueScript(player,1) {
                sendMessage(player, "The contents of the vat haven't finished fermenting yet.")
                return@queueScript stopExecuting(player)
            }
            return
        }
        lock(player, Animation(780).duration)
        animate(player, Animation(780))

        sendMessage(player, "You turn the valve.")

        if (stage == BrewingStage.MATURE || stage == BrewingStage.DONE) {
            rewardXP(player, Skills.COOKING, brewingItem!!.levelXP)
            queueScript(player, 2, QueueStrength.SOFT) {
                sendMessage(player, "The barrel now contains 8 pints of ${brewingItem!!.displayName}.")
                fillBarrel()
                emptyVat()
                updateVat()
                return@queueScript stopExecuting(player)
            }
        } else {
            fillBarrel()
            emptyVat()
            updateVat()
        }
    }

    private fun emptyVat() {
        brewingItem = null
        theStuff = false
        stage = BrewingStage.EMPTY
        updateVat()
    }

    private fun fillBarrel() {
        var varbitValue = 0
        when (stage){
            BrewingStage.EMPTY -> Unit
            BrewingStage.WATER,
            BrewingStage.MALT,
            BrewingStage.MIXED,
            BrewingStage.BREWING1,
            BrewingStage.BREWING2 -> {
                varbitValue = 4
            }
            BrewingStage.DONE,
            BrewingStage.MATURE -> {
                barrelItem = brewingItem
                val barrelAmount = 8

                varbitValue = barrelItem?.barrelVarBitOffset ?: 0

                if (barrelItem != null){
                    varbitValue += (barrelAmount - 1)
                    if (stage == BrewingStage.MATURE) {
                        varbitValue += 128
                    }
                }
            }
            BrewingStage.BAD ->{
                varbitValue = if(brewingItem == Brewable.CIDER) 2 else 1
            }
        }
        setVarbit(player, this.brewingVat.varbit + 2, varbitValue, true)
    }

    fun levelBarrel(container: Int): Boolean {
        var value =  getVarbit(player, this.brewingVat.varbit + 2)
        if (value == 0) return false

        // Check for mature bit (128) and strip it for calculations
        val isMature = value >= 128
        val baseValue = if (isMature) value - 128 else value
        var servingsLeft = if(baseValue == 3) 1 else (baseValue % 8) + 1

        val brewable =
            if (baseValue == 3) Brewable.KELDA_STOUT
            else Brewable.values()[(baseValue/8) - 1]

        val product = brewable.product

        if (container == Items.BEER_GLASS_1919) {
            if(removeItem(player, container)) {
                animate(player, Animation(2285))
                value--
                servingsLeft--
                val productIndex = if (isMature) 2 else 0
                sendMessage(player, "You pour a glass of ${brewable.displayName}.")
                addItem(player, product[productIndex])
            }
        }
        else if (brewable == Brewable.KELDA_STOUT) {
            sendDialogue(player, "You must use a glass to level Kelda Stout.")
            return false
        }
        else if (container == Items.CALQUAT_KEG_5769) {
            if(removeItem(player, container)){
                animate(player, Animation(2284))
                val servingsToTake = minOf(servingsLeft, 4)
                value -= servingsToTake
                servingsLeft -= servingsToTake
                val filledKeg = CalquatDecant.calquatDrinks[brewable]!!
                addItem(player, CalquatDecant.getKegByDose(filledKeg, servingsToTake, isMature))
            }
        }
        if (servingsLeft == 0) {
            sendMessage(player, "The barrel is now empty.")
            value = 0
        }
        setVarbit(player, this.brewingVat.varbit + 2, value, true)
        return servingsLeft != 0
    }

    fun drainBarrel() {
        sendMessage(player, "You drain the barrel.")
        setVarbit(player, this.brewingVat.varbit + 2, 0, true)

    }

    fun isVatEmpty(): Boolean {
        return stage == BrewingStage.EMPTY
    }

    fun isBarrelEmpty(): Boolean {
        return getVarbit(player, (this.brewingVat.varbit + 2)) == 0
    }

    private fun getVatDisplay(): Int {
        return when (stage){
            BrewingStage.EMPTY -> 0
            BrewingStage.WATER -> 1
            BrewingStage.MALT -> 2
            BrewingStage.MIXED -> brewingItem?.vatVarBitOffset ?: run { 0 }
            BrewingStage.BREWING1 -> (brewingItem?.vatVarBitOffset?.plus(1)) ?: run { 0 }
            BrewingStage.BREWING2 -> (brewingItem?.vatVarBitOffset?.plus(2)) ?: run { 0 }
            BrewingStage.DONE -> (brewingItem?.vatVarBitOffset?.plus(3)) ?: run { 0 }
            BrewingStage.MATURE -> (brewingItem?.vatVarBitOffset?.plus(4)) ?: run { 0 }
            BrewingStage.BAD -> if (brewingItem == Brewable.CIDER) 65 else 64
        }
    }

    fun updateVat(){
        setVarbit(player, this.brewingVat.varbit, this.getVatDisplay(), true)
    }

}


enum class BrewingStage {
    EMPTY,
    WATER,
    MALT,
    MIXED,
    BREWING1,
    BREWING2,
    DONE,
    MATURE,
    BAD;
}


enum class BrewingVat(val varbit: Int) {
    KELDAGRIM(736),
    PORT_PHAS(737);

    fun getVat(player: Player): Vat {
        val vat = getOrStartTimer<BrewGrowth>(player)
        return vat.getVat(this, true)
    }

}