package core.game.content.global.shop

import api.LoginListener
import api.getAttribute
import api.setAttribute
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.task.Pulse
import org.rs09.consts.Items
import rs09.game.world.GameWorld
import java.lang.Integer.min
import kotlin.collections.HashMap

object CulinomancerShop : LoginListener {
    // Our shop mappings - shops are individualized due to the differing items based on player's QP.
    // Maps player UID -> shop
    private val foodShops = HashMap<Int, Shop>()
    private val gearShops = HashMap<Int, Shop>()

    //Open methods for the shops - should check player's QP and whether they already have a container generated
    @JvmStatic fun openShop(player: Player, food: Boolean)
    {
        getShop(player, food).open(player)
    }

    //Retrieve a player's shop - should generate the shop if it does not exist.
    fun getShop(player: Player, food: Boolean) : Shop
    {
        val uid = player.details.uid
        val points = player.questRepository.points
        val tier = (points / 18)
        if(tier != getAttribute(player, "culino-tier", 0)) //If player tier has changed
        {
            foodShops.remove(uid) //Clear the previous shops, so they can regenerate with the new tier
            gearShops.remove(uid)
        }
        return if(food) {
            val shop = foodShops[uid] ?: Shop("Culinomancer's Chest Tier $tier", generateFoodStock(points),false)
            foodShops[uid] = shop
            shop
        } else {
            val shop = gearShops[uid] ?: Shop("Culinomancer's Chest Tier $tier", generateGearStock(points), false)
            gearShops[uid] = shop
            shop
        }
    }

    //Generate default food stock based on an amount of total QP.
    private fun generateFoodStock(points: Int) : Array<Item>
    {
        val stock = Array(foodStock.size) { Item(0, 0) }
        val maxQty = when(val qpTier = (points / 18) - 1)
        {
            0,1,2,3,4 -> 1 + qpTier
            else -> qpTier + (qpTier + (qpTier - 5)) //5 = 10, 6 = 13, 7 = 15, etc
        }
        for((index,item) in foodStock.withIndex())
        {
            stock[index].id = item.id
            stock[index].amount = if(item.id == Items.PIZZA_BASE_2283) 1 else maxQty
        }
        return stock
    }

    //Generate default gear stock based on an amount of total QP.
    private fun generateGearStock(points: Int): Array<Item>
    {
        val stock = Array(gearStock.size) { Item(0,0) }
        val qpTier = (points/18)
        for((index, item) in stock.withIndex()) item.id = gearStock[index]

        for(i in 0 until min(qpTier, 10))
        {
            stock[i].amount = 30
            stock[i + 10].amount = 5
        }
        return stock
    }

    //Default gear shop stock
    private val gearStock = arrayOf(
        Items.GLOVES_7453,
        Items.GLOVES_7454,
        Items.GLOVES_7455,
        Items.GLOVES_7456,
        Items.GLOVES_7457,
        Items.GLOVES_7458,
        Items.GLOVES_7459,
        Items.GLOVES_7460,
        Items.GLOVES_7461,
        Items.GLOVES_7462,
        Items.WOODEN_SPOON_7433,
        Items.EGG_WHISK_7435,
        Items.SPORK_7437,
        Items.SPATULA_7439,
        Items.FRYING_PAN_7441,
        Items.SKEWER_7443,
        Items.ROLLING_PIN_7445,
        Items.KITCHEN_KNIFE_7447,
        Items.MEAT_TENDERISER_7449,
        Items.CLEAVER_7451
    )

    //Default food shop stock
    private val foodStock = arrayOf(
        Item(Items.CHOCOLATE_BAR_1973,1),
        Item(Items.CHEESE_1985, 1),
        Item(Items.TOMATO_1982, 1),
        Item(Items.COOKING_APPLE_1955, 1),
        Item(Items.GRAPES_1987, 1),
        Item(Items.POT_OF_FLOUR_1933, 1),
        Item(Items.PIZZA_BASE_2283, 1),
        Item(Items.EGG_1944, 1),
        Item(Items.BUCKET_OF_MILK_1927, 1),
        Item(Items.POT_OF_CREAM_2130, 1),
        Item(Items.PAT_OF_BUTTER_6697, 1),
        Item(Items.SPICE_2007, 1),
        Item(Items.PIE_DISH_2313, 1),
        Item(Items.CAKE_TIN_1887, 1),
        Item(Items.BOWL_1923, 1),
        Item(Items.JUG_1935, 1),
        Item(Items.EMPTY_POT_1931, 1),
        Item(Items.EMPTY_CUP_1980, 1),
        Item(Items.BUCKET_1925, 1)
    )

    //Enable the chest if the player has 18 quest points or more
    override fun login(player: Player) {
        if(player.questRepository.points >= 18){
            player.varpManager.get(678).setVarbit(0, 5).send(player)
            setAttribute(player, "culino-tier", player.questRepository.points / 18) //Set this, so we can check if the player has gained a tier during server runtime

            //Restock pulse for this player (yes, this means the chest will only restock if the player has logged in. Shop system needs work in order to do otherwise.)
            val restockPulse = object : Pulse(100){ //Run once a minute
                override fun pulse(): Boolean {
                    getShop(player, false).restock()
                    getShop(player, true).restock()
                    return false
                }
            }
            GameWorld.Pulser.submit(restockPulse)
            //Stop the pulse if the player logs out (easy way to avoid a player having multiple restock pulses by relogging a bunch)
            //Stopped pulses are cleared from the list on the next tick cycle
            player.logoutListeners["culino-restock"] = {restockPulse.stop()}
        }
    }
}