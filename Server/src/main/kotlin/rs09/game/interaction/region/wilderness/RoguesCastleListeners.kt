package rs09.game.interaction.region.wilderness

import api.ContentAPI
import core.game.node.`object`.Scenery
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.tools.RandomFunction
import org.rs09.consts.Items
import rs09.game.content.global.WeightBasedTable
import rs09.game.content.global.WeightedItem
import rs09.game.interaction.InteractionListener

class RoguesCastleListeners : InteractionListener() {

    val CHEST_ANIM = ContentAPI.getAnimation(536)
    val FLOOR_1_CHESTS = intArrayOf(14773, 14774)
    val FLOOR_2_CHESTS = intArrayOf(38834, 38835)

    override fun defineListeners() {
        on(FLOOR_1_CHESTS, SCENERY, "open"){ player, node ->
            val scenery = node.asScenery()
            openChest(player, scenery)
            return@on true
        }

        on(FLOOR_1_CHESTS, SCENERY, "search"){player, node ->
            val scenery = node.asScenery()
            if(ContentAPI.getCharge(scenery) == 0){
                ContentAPI.sendMessage(player, "This chest has already been looted.")
                return@on true
            }

            if(ContentAPI.freeSlots(player) == 0){
                ContentAPI.sendMessage(player, "You don't have enough space to do that.")
                return@on true
            }

            val item = FLOOR_1_LOOT.roll()[0]
            addLoot(player, item)
            ContentAPI.setCharge(scenery, 0)
            return@on true
        }

        on(FLOOR_2_CHESTS, SCENERY, "open"){player, node ->
            ContentAPI.sendMessage(player, "This chest appears to be locked.")
            return@on true
        }

        on(FLOOR_2_CHESTS, SCENERY, "pick-lock"){player, node ->
            val scenery = node.asScenery()
            if(!ContentAPI.inInventory(player, Items.LOCKPICK_1523)){
                ContentAPI.sendMessage(player, "You need a lockpick in order to attempt this.")
                return@on true
            }

            if(!ContentAPI.hasLevelDyn(player, Skills.THIEVING, 13)){
                ContentAPI.sendMessage(player, "You need a Thieving level of 13 to attempt this.")
                return@on true
            }

            ContentAPI.sendMessage(player, "You attempt to pick the lock on the chest...")
            ContentAPI.submitIndividualPulse(player, object : Pulse(2){
                override fun pulse(): Boolean {
                    val success = RandomFunction.roll(10) // 1/10 chance to succeed
                    if(success){
                        ContentAPI.replaceScenery(scenery, scenery.id + 1, 20)
                        ContentAPI.rewardXP(player, Skills.THIEVING, 300.0)
                    } else {
                        val dealsDamage = RandomFunction.roll(10) // 1/10 chance to deal damage on a fail
                        if(dealsDamage) {
                            ContentAPI.impact(player, RandomFunction.random(1, 3), ImpactHandler.HitsplatType.NORMAL)
                            ContentAPI.sendMessage(player, "You activated a trap on the chest!")
                        }
                    }

                    ContentAPI.sendMessage(player, "You ${if(success) "manage" else "fail"} to pick the lock on the chest.")
                    return true
                }
            })

            return@on true
        }

        on(FLOOR_2_CHESTS, SCENERY, "search"){player, node ->
            val scenery = node.asScenery()

            if(ContentAPI.getCharge(scenery) == 0){
                ContentAPI.sendMessage(player, "This chest has already been looted.")
                return@on true
            }

            val loot = FLOOR_2_LOOT.roll()[0]
            if(ContentAPI.freeSlots(player) > 0){
                ContentAPI.addItemOrDrop(player, loot.id, loot.amount)
                ContentAPI.sendMessage(player, "In the chest you find some ${loot.name.toLowerCase() + if(!loot.name.endsWith("s")) "s" else ""}!")
                ContentAPI.setCharge(scenery, 0)
                ContentAPI.rewardXP(player, Skills.THIEVING, 60.0)
            }

            return@on true
        }
    }

    fun openChest(player: Player, scenery: Scenery){
        ContentAPI.animate(player, CHEST_ANIM)
        ContentAPI.submitIndividualPulse(player, object : Pulse(ContentAPI.animationDuration(CHEST_ANIM)){
            override fun pulse(): Boolean {
                return true.also { ContentAPI.replaceScenery(scenery, scenery.id + 1, 20) }
            }
        })
    }

    fun addLoot(player: Player, item: Item){
        ContentAPI.sendMessage(player, "You search the chest...")
        ContentAPI.submitIndividualPulse(player, object : Pulse(){
            override fun pulse(): Boolean {
                ContentAPI.sendMessage(player, "... and find some ${item.name.toLowerCase() + if(!item.name.endsWith("s")) "s" else ""}!")
                ContentAPI.addItemOrDrop(player, item.id, item.amount)
                return true
            }
        })
    }

    val FLOOR_1_LOOT = WeightBasedTable.create(
            WeightedItem(Items.COINS_995, 8, 25, 70.0),
            WeightedItem(Items.NATURE_RUNE_561, 2, 3, 10.0),
            WeightedItem(Items.BLOOD_RUNE_565, 2, 3, 10.0),
            WeightedItem(Items.DEATH_RUNE_560, 3, 5, 10.0)
    )

    val FLOOR_2_LOOT = WeightBasedTable.create(
            WeightedItem(Items.COINS_995, 4, 57, 75.0),
            WeightedItem(Items.COINS_995, 107, 243, 5.0),
            WeightedItem(Items.BLOOD_RUNE_565, 2, 5, 5.0),
            WeightedItem(Items.GOLD_ORE_445, 1, 1, 5.0),
            WeightedItem(Items.STEEL_MED_HELM_1141, 1, 1, 5.0),
            WeightedItem(Items.STEEL_PLATELEGS_1069, 1, 1, 5.0)
    )

}