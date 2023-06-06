package content.minigame.blastfurnace

import content.global.skill.smithing.smelting.Bar
import core.api.*
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Location
import org.rs09.consts.Items

/**"Most" of the listeners for blast furnace live in this file, handles
 * listeners for most things from interacting with the temp gauge to putting ore on the
 * conveyor belt. The only thing that's not in here as far as listeners goes is Ordan's unnoting.
 * That lives in OrdanDialogue.kt
 * @author phil, bushtail*/

class BlastFurnaceListeners : InteractionListener {

    val disLoc = getScenery(1941, 4963, 0)
    val brokenPotPipe = 9117
    val brokenPumpPipe = 9121
    val pump = 9090
    val stove = intArrayOf(9085, 9086, 9087)
    val coke = 9088
    val pedals = 9097
    val conveyorLoad = 9100
    val dispenser = intArrayOf(9093, 9094, 9095, 9096)
    val brokenBelt = 9103
    val brokenCog = 9105
    val tGauge = 9089
    val sink = 9143
    val playerOre = intArrayOf(
        Items.IRON_ORE_440,
        Items.COPPER_ORE_436,
        Items.TIN_ORE_438,
        Items.COAL_453,
        Items.MITHRIL_ORE_447,
        Items.ADAMANTITE_ORE_449,
        Items.SILVER_ORE_442,
        Items.GOLD_ORE_444,
        Items.RUNITE_ORE_451
    )
    val notedOre = intArrayOf(
        Items.IRON_ORE_441,
        Items.COPPER_ORE_437,
        Items.TIN_ORE_439,
        Items.COAL_454,
        Items.MITHRIL_ORE_448,
        Items.ADAMANTITE_ORE_450,
        Items.SILVER_ORE_443,
        Items.GOLD_ORE_445,
        Items.RUNITE_ORE_452
    )

    override fun defineListeners() {

        /**Handles operating the pump*/

        on(pump, SCENERY, "operate") { player, node ->
            if(player.getSkills().getLevel(Skills.STRENGTH) >= 30) {
                val pumpL = Location(1950, 4961, 0)
                val pumpF = Location(1949, 4961, 0)
                player.properties.teleportLocation = pumpL
                submitIndividualPulse(player, object : Pulse() {
                    override fun pulse(): Boolean {
                        face(player, pumpF,)
                        animate(player, 2432)
                        BlastFurnace.pumping = true
                        if (!BlastFurnace.pumpPipeBroken || !BlastFurnace.potPipeBroken) rewardXP(
                            player,
                            Skills.STRENGTH,
                            4.0
                        )
                        return false
                    }

                    override fun stop() {
                        BlastFurnace.pumping = false
                        super.stop()
                    }
                })
            }else{
                sendDialogue(player,"I need 30 Strength to do this!")
            }
            return@on true
        }

        /**Logic for the pedals that run the conveyor, rewards Agility XP every tick that they're being
         * pedaled but will stop once the conveyor breaks.*/

        on(pedals, SCENERY, "pedal") { player, node ->
            if(player.getSkills().getLevel(Skills.AGILITY) >= 30) {
                val pedalL = Location(1947, 4966, 0)
                val pedalF = Location(1946, 4966, 0)
                player.properties.teleportLocation = pedalL
                submitIndividualPulse(player, object : Pulse() {
                    override fun pulse(): Boolean {
                        face(player, pedalF,)
                        animate(player, 2433)
                        BlastFurnace.pedaling = true
                        if (BlastFurnace.beltRunning) rewardXP(player, Skills.AGILITY, 2.0)
                        return false
                    }
                    override fun stop() {
                        BlastFurnace.pedaling = false
                        super.stop()
                    }
                })
            }else{
                sendDialogue(player,"I need 30 Agility to do this!")
            }
            return@on true
        }

        /**Lets players use a spade to take coke from the pile of coke*/

        on(coke, SCENERY, "collect") { player, _ ->
            if (inInventory(player, Items.SPADE_952, 1)) {
                if(removeItem(player, Items.SPADE_952, Container.INVENTORY) && addItem(player, Items.SPADEFUL_OF_COKE_6448, 1)) {
                    lockInteractions(player,1)
                    animate(player, 2441)
                }
            } else {
                sendMessage(player, "You need a spade to do this!")
            }
        return@on true
        }

        /**This code is for handling the coke stove, lets players interact with it with the spadeful of coke
         * and rewards firemaking XP when shoving coke in there, it will not allow players to shovel more
         * coke in if it is already full.*/

        on(stove, SCENERY,"refuel") { player, _ ->
            if (inInventory(player, Items.SPADEFUL_OF_COKE_6448, 1) && BlastFurnace.stoveCoke < 30 && player.getSkills().getLevel(Skills.FIREMAKING) >= 30)  {
                animate(player, 2442)
                lockInteractions(player,2)
                submitIndividualPulse(player, object : Pulse() {
                    override fun pulse(): Boolean {
                        return if(removeItem(player, Items.SPADEFUL_OF_COKE_6448, Container.INVENTORY) && addItem(player, Items.SPADE_952, 1)) {
                            animate(player, 2443)
                            true
                        } else {
                            false
                        }
                    }
                })
                rewardXP(player, Skills.FIREMAKING, 5.0)
                BlastFurnace.stoveCoke++
            }else if(inInventory(player, Items.SPADEFUL_OF_COKE_6448,1) && BlastFurnace.stoveCoke >= 30 && player.getSkills().getLevel(Skills.FIREMAKING) >= 30){
                sendDialogue(player,"The coke stove is already full!")
            } else if(player.getSkills().getLevel(Skills.FIREMAKING) < 30){
                sendDialogue(player,"I need 30 Firemaking to do this")
            } else {
                sendMessage(player,"You need some coke to do that!")
            }
        return@on true
        }

        /**This block of code handles the logic for putting coal and ores
         * on the conveyor belt. It won't let you put anything on there if there's no room
         * in your blast ore or blast coal player containers, the way it works is that it
         * puts your coal and ore into the blast furnace containers however, the blast furnace will NOT
         * smelt your ores because it's waiting for a player attribute to be set by the ore NPCs that this spawns*/

        on(conveyorLoad, SCENERY, "put-ore-on") { player, node ->
            val rocksInInventory = playerOre.filter {inInventory(player, it)}
            var oreToActuallyAdd = 0
            var coalToActuallyAdd = 0
            if(player.blastCoal.freeSlots() > 0 || player.blastOre.freeSlots() > 0) {
                player.dialogueInterpreter.sendOptions("Add all your ore to the furnace?", "Yes", "No")
                player.dialogueInterpreter.addAction { player, button ->
                    if (button == 2 && rocksInInventory.isNotEmpty()) {
                        rocksInInventory.forEach { oreID ->
                            val oreAmount = amountInInventory(player, oreID)
                            val copperInPot = player.blastOre.getAmount(436)
                            val tinInPot = player.blastOre.getAmount(438)
                            if(oreID == Items.COAL_453 && oreAmount <= player.blastCoal.freeSlots()){
                                coalToActuallyAdd = oreAmount
                            }else if(oreID == Items.COAL_453 && oreAmount > player.blastCoal.freeSlots() && player.blastCoal.freeSlots() > 0){
                                coalToActuallyAdd = player.blastCoal.freeSlots()
                            }
                            if(oreID != Items.COAL_453 && oreAmount <= player.blastOre.freeSlots()){
                                oreToActuallyAdd = oreAmount
                            }else if(oreID != Items.COAL_453 && oreAmount > player.blastOre.freeSlots() && player.blastOre.freeSlots() > 0){
                                oreToActuallyAdd = player.blastOre.freeSlots()
                            }
                            if(oreID == Items.COPPER_ORE_436 && (oreToActuallyAdd + copperInPot) > 14){
                                while(oreToActuallyAdd + copperInPot > 14){
                                    oreToActuallyAdd--
                                }
                            }
                            if(oreID == Items.TIN_ORE_438 && (oreToActuallyAdd + tinInPot) > 14){
                                while(oreToActuallyAdd + tinInPot > 14){
                                    oreToActuallyAdd--
                                }
                            }
                            val bar = Bar.forOre(oreID)
                            if(oreID == Items.COAL_453) {
                                if (coalToActuallyAdd > 0 && getStatLevel(player, Skills.SMITHING) >= 30) {
                                    if(removeItem(player, Item(oreID, coalToActuallyAdd), Container.INVENTORY) && player.blastCoal.add(Item(Items.COAL_453,coalToActuallyAdd))) {
                                        BlastFurnaceOre(player, BFOreVariant.values()[playerOre.indexOf(oreID)],coalToActuallyAdd).init()
                                    }
                                }
                                else if(getStatLevel(player, Skills.SMITHING) < 30){
                                    sendDialogue(player, "My Smithing level is not high enough to use Coal!")
                                }
                                else sendDialogue(player, "It looks like the melting pot is already full of coal!")
                            } else if (bar != null) {
                                if (oreToActuallyAdd > 0 && getStatLevel(player, Skills.SMITHING) >= bar.level) {
                                    if(removeItem(player, Item(oreID, oreToActuallyAdd), Container.INVENTORY) && player.blastOre.add(Item(oreID,oreToActuallyAdd))) {
                                        BlastFurnaceOre(
                                            player,
                                            BFOreVariant.values()[playerOre.indexOf(oreID)],
                                            oreToActuallyAdd
                                        ).init()
                                    }
                                }
                                else if(getStatLevel(player, Skills.SMITHING) < bar.level){
                                    sendDialogue(player, "My Smithing level is not high enough to smelt ${bar.name.lowercase()}!")
                                }
                                else sendDialogue(player, "It looks like the melting pot is already full of ore!")
                            }
                        }
                    } else if (button == 2 && rocksInInventory.isEmpty()){
                        sendDialogue(player,"I should make sure that I have some ore before doing this.")
                    }
                }
            }
        return@on true
        }

        /**Added this because clicking on ore then the belt and getting "Nothing interesting happens" is annoying
         * it's essentially the same thing as above except without the dialogue because if you're clicking on ore
         * and trying to use it on the conveyor belt then you know what you're trying to do.*/
        onUseWith(SCENERY,playerOre,conveyorLoad){ player, oreType, _ ->
            val amountInInventory = player.inventory.getAmount(oreType.id)
            val spaceForCoal = player.blastCoal.freeSlots()
            val spaceForOre = player.blastOre.freeSlots()
            val copperInPot = player.blastOre.getAmount(436)
            val tinInPot = player.blastOre.getAmount(438)
            var amountToAdd = 0
            if(oreType.id == Items.COAL_453 && amountInInventory <= spaceForCoal){
                amountToAdd = amountInInventory
            }else if(oreType.id == Items.COAL_453 && amountInInventory > spaceForCoal){
                amountToAdd = spaceForOre
            }
            if(oreType.id != Items.COAL_453 && amountInInventory <= spaceForOre){
                amountToAdd = amountInInventory
            }else if(oreType.id != Items.COAL_453 && amountInInventory > spaceForOre){
                amountToAdd = spaceForOre
            }
            if(oreType.id == Items.COPPER_ORE_436 && (amountToAdd + copperInPot) > 14){
                while(amountToAdd + copperInPot > 14){
                    amountToAdd--
                }
            }
            if(oreType.id == Items.TIN_ORE_438 && (amountToAdd + tinInPot) > 14){
                while(amountToAdd + tinInPot > 14){
                    amountToAdd--
                }
            }
            val bar = Bar.forOre(oreType.id)
            if(oreType.id == Items.COAL_453) {
                if (amountToAdd > 0 && getStatLevel(player, Skills.SMITHING) >= 30) {
                    if(removeItem(player, Item(oreType.id, amountToAdd), Container.INVENTORY) && player.blastCoal.add(Item(oreType.id,amountToAdd))) {
                        BlastFurnaceOre(
                            player,
                            BFOreVariant.values()[playerOre.indexOf(oreType.id)],
                            amountToAdd
                        ).init()
                    }
                }else if(getStatLevel(player,Skills.SMITHING) < 30){
                    sendDialogue(player,"My Smithing level is not high enough to use Coal!")
                }
                else sendDialogue(player, "It looks like the melting pot is already full of ore!")
            } else if(bar != null) {
                if (amountToAdd > 0 && getStatLevel(player, Skills.SMITHING) >= bar.level) {
                    player.blastOre.add(Item(oreType.id,amountToAdd))
                    removeItem(player, Item(oreType.id, amountToAdd), Container.INVENTORY)
                    BlastFurnaceOre(player, BFOreVariant.values()[playerOre.indexOf(oreType.id)],amountToAdd).init()
                }else if(getStatLevel(player,Skills.SMITHING) < bar.level){
                    sendDialogue(player,"My Smithing level is not high enough to smelt Iron!")
                }
                else sendDialogue(player, "It looks like the melting pot is already full of ore!")
            }
            return@onUseWith true
        }

        /**This handles interacting with the temperature gauge on the furnace*/

        on(tGauge, SCENERY, "read") { player, _ ->
            player.interfaceManager.openComponent(30)
            return@on true
        }

        /**This handles taking bars from the dispenser*/

        on(dispenser,SCENERY,"search", "take"){ player, _ ->
            if (getVarbit(player, 936) == 0 || getVarbit(player, 936) == 3) {
                player.interfaceManager.openComponent(28)
            }
            return@on true
        }

        /**Handles using a bucket of water on the bar dispenser*/

        onUseWith(SCENERY,Items.BUCKET_OF_WATER_1929,*dispenser){ player, _, _ ->
            when (getVarbit(player, 936)) {
                2 -> {
                    removeItem(player,Items.BUCKET_OF_WATER_1929,Container.INVENTORY)
                    addItem(player,Items.BUCKET_1925)
                    BlastFurnace.barsHot = false
                    setVarbit(player, 936, 3)
                }
                0 -> {
                    sendDialogue(player,"There's nothing to cool off!")
                }
                1 -> {
                    sendDialogue(player,"I should wait until the machine is finished")
                }
                3 -> {
                    sendDialogue(player,"These bars have already cooled off!")
                }
            }
            return@onUseWith true
        }

        /**Handles pipe repairing*/

        on(brokenPotPipe,SCENERY,"repair"){ player, _ ->
            if(player.getSkills().getLevel(Skills.CRAFTING) >= 30){
                if(inInventory(player,Items.HAMMER_2347,1)) {
                    rewardXP(player, Skills.CRAFTING, 50.0)
                    BlastFurnace.potPipeBroken = false
                }
                else {
                    sendMessage(player, "I need a hammer to do this!")
                }
            }else{
                sendDialogue(player,"I need 30 Crafting in order to do this")
            }
            return@on true
        }

        on(brokenPumpPipe,SCENERY, "repair"){ player, _ ->
            if(player.getSkills().getLevel(Skills.CRAFTING) >= 30){
                if(inInventory(player,Items.HAMMER_2347,1)) {
                    rewardXP(player, Skills.CRAFTING, 50.0)
                    BlastFurnace.pumpPipeBroken = false
                }
                else {
                    sendMessage(player, "I need a hammer to do this!")
                }
            }else{
                sendDialogue(player,"I need 30 Crafting in order to do this")
            }
            return@on true
        }

        on(brokenBelt,SCENERY,"repair"){ player, _ ->
            if(player.getSkills().getLevel(Skills.CRAFTING) >= 30){
                if(inInventory(player,Items.HAMMER_2347,1)) {
                    rewardXP(player, Skills.CRAFTING, 50.0)
                    BlastFurnace.beltBroken = false
                }
                else {
                    sendMessage(player, "I need a hammer to do this!")
                }
            }else{
                sendDialogue(player,"I need 30 Crafting in order to do this")
            }
            return@on true
        }

        on(brokenCog,SCENERY,"repair"){ player, _ ->
            if(player.getSkills().getLevel(Skills.CRAFTING) >= 30){
                if(inInventory(player,Items.HAMMER_2347,1)) {
                    rewardXP(player, Skills.CRAFTING, 50.0)
                    BlastFurnace.cogBroken = false
                }
                else {
                    sendMessage(player, "I need a hammer to do this!")
                }
            }else{
                sendDialogue(player,"I need 30 Craft in order to do this")
            }
            return@on true
        }

        /**Handles filling buckets from the sink*/

        on(sink, SCENERY,"fill-bucket"){ player, _ ->
            player.pulseManager.run(object : Pulse(1){
                override fun pulse(): Boolean {
                    if(removeItem(player, Items.BUCKET_1925))
                    {
                        animate(player, 832)
                        sendMessage(player, "You fill the bucket from the sink.")
                        addItemOrDrop(player, Items.BUCKET_OF_WATER_1929)
                    }
                    return true
                }
            })
            return@on true
        }
    }
}
