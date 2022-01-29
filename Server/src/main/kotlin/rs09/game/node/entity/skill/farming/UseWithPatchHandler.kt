package rs09.game.node.entity.skill.farming

import api.*
import core.game.interaction.NodeUsageEvent
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Items
import rs09.game.node.entity.skill.farming.CropHarvester

object UseWithPatchHandler{
    val RAKE = Items.RAKE_5341
    val SEED_DIBBER = Items.SEED_DIBBER_5343
    val SPADE = Items.SPADE_952
    val SECATEURS = Items.SECATEURS_5329
    val TROWEL = Items.GARDENING_TROWEL_5325
    val pourBucketAnim = Animation(2283)
    val wateringCanAnim = Animation(2293)
    val plantCureAnim = Animation(2288)
    val secateursAnim = Animation(7227)

    @JvmField
    val allowedNodes = ArrayList<Int>()

    init {
        loadNodes()
    }

    @JvmStatic
    fun loadNodes(){
        for(p in Plantable.values()){
            allowedNodes.add(p.itemID)
        }
        allowedNodes.addAll(arrayListOf(RAKE,SEED_DIBBER,SPADE,SECATEURS,TROWEL,Items.SUPERCOMPOST_6034,Items.COMPOST_6032,Items.PLANT_CURE_6036,Items.WATERING_CAN1_5333,Items.WATERING_CAN2_5334,Items.WATERING_CAN3_5335,Items.WATERING_CAN4_5336,Items.WATERING_CAN5_5337,Items.WATERING_CAN6_5338,Items.WATERING_CAN7_5339,Items.WATERING_CAN8_5340))
    }

    @JvmStatic
    fun handle(event: NodeUsageEvent?): Boolean {
        event ?: return false
        val player = event.player
        val usedItem = event.usedItem
        val patch = FarmingPatch.forObject(event.usedWith.asScenery())
        patch ?: return false

        player.faceLocation(event.usedWith.location)
        when(usedItem.id){
            RAKE -> PatchRaker.rake(player,patch)
            SEED_DIBBER -> player.sendMessage("I should plant a seed, not the seed dibber.")
            SPADE -> player.dialogueInterpreter.open(67984003,patch.getPatchFor(player)) //DigUpPatchDialogue.kt
            SECATEURS -> {
                val p = patch.getPatchFor(player)
                if(patch.type == PatchType.TREE) {
                    if(p.isDiseased && !p.isDead) {
                        player.pulseManager.run(object: Pulse(){
                            override fun pulse(): Boolean {
                                player.animator.animate(secateursAnim)
                                p.cureDisease()
                                return true
                            }
                        })
                    } else if(p.plantable == Plantable.WILLOW_SAPLING && p.harvestAmt > 0) {
                        val pulse = CropHarvester.harvestPulse(player, event.usedWith, Items.WILLOW_BRANCH_5933) ?: return false
                        player.pulseManager.run(pulse)
                    }
                }
            }
            TROWEL -> {
                val p = patch.getPatchFor(player)
                if(!p.isWeedy()){
                    player.sendMessage("This patch has something growing in it.")
                    return true
                } else if (p.currentGrowthStage != 3){
                    player.sendMessage("I should clear this of weeds before trying to take some dirt.")
                    return true
                }

                val potAmount = player.inventory.getAmount(Items.PLANT_POT_5356)

                if(potAmount == 0){
                    player.sendMessage("You have no plant pots to fill.")
                    return true
                }

                val anim = Animation(2272)

                player.pulseManager.run(object : Pulse(anim.duration){
                    override fun pulse(): Boolean {
                        if(player.inventory.remove(Item(Items.PLANT_POT_5356))){
                            player.animator.animate(anim)
                            player.inventory.add(Item(Items.PLANT_POT_5354))
                        } else return true
                        return false
                    }
                })
            }

            Items.PLANT_CURE_6036 -> {
                val p = patch.getPatchFor(player)
                if(p.isDiseased && !p.isDead){
                    player.pulseManager.run(object: Pulse(){
                        override fun pulse(): Boolean {
                            player.animator.animate(plantCureAnim)
                            player.audioManager.send(2438)
                            if(player.inventory.remove(event.usedItem)){
                                player.inventory.add(Item(Items.VIAL_229))
                                p.cureDisease()
                            }
                            return true
                        }
                    })
                } else {
                    player.sendMessage("I have no reason to do this right now.")
                }
            }

            Items.WATERING_CAN1_5333,Items.WATERING_CAN2_5334,Items.WATERING_CAN3_5335,Items.WATERING_CAN4_5336,Items.WATERING_CAN5_5337,Items.WATERING_CAN6_5338,Items.WATERING_CAN7_5339,Items.WATERING_CAN8_5340 -> {
                val p = patch.getPatchFor(player)
                val t = p.patch.type
                if(!p.isWatered && (t == PatchType.ALLOTMENT || t == PatchType.FLOWER || t == PatchType.HOPS) && !p.isGrown()){
                    player.pulseManager.run(object : Pulse(){
                        override fun pulse(): Boolean {
                            if(p.isWeedy()){
                                player.sendMessage("You should grow something first.")
                                return true
                            }
                            player.animator.animate(wateringCanAnim)
                            player.audioManager.send(2446)                            
                            if(player.inventory.remove(event.usedItem)){
                                player.inventory.add(Item(usedItem.id.getNext()))
                                p.water()
                            }
                            return true
                        }
                    })
                }
            }

            Items.SUPERCOMPOST_6034, Items.COMPOST_6032 -> {
                val p = patch.getPatchFor(player)
                if(p.compost == CompostType.NONE) {
                    player.animator.animate(pourBucketAnim)
                    player.audioManager.send(2427)
                    player.pulseManager.run(object : Pulse(){
                        override fun pulse(): Boolean {
                            if(player.inventory.remove(event.usedItem,false)){
                                p.compost = if(usedItem.id == Items.SUPERCOMPOST_6034) CompostType.SUPER else CompostType.NORMAL
                                if(p.compost == CompostType.SUPER) rewardXP(player, Skills.FARMING, 26.0) else rewardXP(player, Skills.FARMING, 18.5)
                                if(p.plantable != null && p.plantable?.applicablePatch != PatchType.FLOWER) {
                                    p.harvestAmt += if(p.compost == CompostType.NORMAL) 1 else if(p.compost == CompostType.SUPER) 2 else 0
                                }
                                p.cropLives += if(p.compost == CompostType.SUPER) 2 else 1
                                player.inventory.add(Item(Items.BUCKET_1925))
                            }
                            return true
                        }
                    })
                } else {
                    player.sendMessage("This patch has already been treated with compost.")
                }
            }

            else -> {
                val plantable = Plantable.forItemID(usedItem.id)
                if(plantable == null) return false

                if(plantable.applicablePatch != patch.type){
                    player.sendMessage("You can't plant that seed in this patch.")
                    return true
                }

                if(plantable.requiredLevel > player.skills.getLevel(Skills.FARMING)){
                    player.sendMessage("You need a Farming level of ${plantable.requiredLevel} to plant this.")
                    return true
                }

                val p = patch.getPatchFor(player)
                if(p.getCurrentState() < 3 && p.isWeedy()){
                    player.sendMessage("You must weed your patch before you can plant a seed in it.")
                    return true
                } else if(p.getCurrentState() > 3){
                    player.sendMessage("There is already something growing in this patch.")
                    return true
                }

                val plantItem =
                    if(patch.type == PatchType.ALLOTMENT) Item(plantable.itemID,3) else if(patch.type == PatchType.HOPS){
                    if(plantable == Plantable.JUTE_SEED) Item(plantable.itemID,3) else Item(plantable.itemID,4)
                } else {
                    Item(plantable.itemID,1)
                }

                if(patch.type == PatchType.ALLOTMENT){
                    if(!player.inventory.containsItem(plantItem)){
                        player.sendMessage("You need 3 seeds to plant an allotment patch.")
                        return true
                    }
                }
                if(patch.type != PatchType.FRUIT_TREE && patch.type != PatchType.TREE){
                    if(!player.inventory.contains(Items.SEED_DIBBER_5343,1)){
                        player.sendMessage("You need a seed dibber to plant that.")
                        return true
                    }
                } else {
                    if(!player.inventory.contains(Items.SPADE_952,1)){
                        player.sendMessage("You need a spade to plant that.")
                        return true
                    }
                }
                player.lock()
                if(player.inventory.remove(plantItem)) {
                    player.animator.animate(Animation(2291))
                    player.audioManager.send(2432)                    
                    player.pulseManager.run(object : Pulse(3) {
                        override fun pulse(): Boolean {
                            if(plantable == Plantable.JUTE_SEED && patch == FarmingPatch.MCGRUBOR_HOPS && !player.achievementDiaryManager.hasCompletedTask(DiaryType.SEERS_VILLAGE,0,7)){
                                player.achievementDiaryManager.finishTask(player,DiaryType.SEERS_VILLAGE,0,7)
                            }
                            p.plant(plantable)
                            player.skills.addExperience(Skills.FARMING, plantable.plantingXP)
                            p.setNewHarvestAmount()
                            if(p.patch.type == PatchType.TREE || p.patch.type == PatchType.FRUIT_TREE){
                                player.inventory.add(Item(Items.PLANT_POT_5356))
                            }
                            player.unlock()
                            return true
                        }
                    })
                }
            }
        }

        return true
    }

    private fun Int.getNext(): Int {
        if(this == Items.WATERING_CAN1_5333) return Items.WATERING_CAN_5331
        else return this - 1
    }
}
