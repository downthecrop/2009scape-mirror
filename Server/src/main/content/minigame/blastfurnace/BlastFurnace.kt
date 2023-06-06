package content.minigame.blastfurnace

import content.global.skill.smithing.smelting.Bar
import core.api.*
import core.game.container.impl.EquipmentContainer
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.tools.RandomFunction
import org.rs09.consts.Items
import org.rs09.consts.Items.GOLDSMITH_GAUNTLETS_776

/**le Blast Furnace has arrived:
 * Handles most of the Blast Furnace's operating logic, there is some crosstalk between
 * this file and BlastFurnaceListeners.kt as well as the BlastFurnaceZone.kt.
 * This also handles a lot of the varbit fuckery for the furnace. The other
 * varbit fuckery is handled in BlastFurnaceListeners.
 * Varbits are still slightly busted but not in a way that breaks the operation or usage of
 * Blast Furnace, just visual and I've had enough of the funny binary numbers to say fuck it
 * and will just come back to it at a later date.
 * @author phil lips*/

object BlastFurnace {

    val belt1 = getScenery(1943, 4967, 0)
    val belt2 = getScenery(1943, 4966, 0)
    val belt3 = getScenery(1943, 4965, 0)
    val disLoc = getScenery(1941, 4963, 0)
    var pipe1 = getScenery(1943, 4961, 0)
    var pipe2 = getScenery(1947, 4961, 0)
    var cogs1 = getScenery(1945, 4965, 0)
    var cogs2 = getScenery(1945, 4967, 0)
    var beltLoc1 = getScenery(1944, 4965, 0)
    var beltLoc2 = getScenery(1944, 4967, 0)
    var gearsLoc = getScenery(1945, 4966, 0)
    var stoveLoc = getScenery(1948, 4963, 0)

    val mPot = 9098

    var gaugeViewList = ArrayList<Player>()
    var blastFurnacePlayerList = ArrayList<Player>()
    var furnaceTemp = 0
    var pumpPipeBroken = false
    var potPipeBroken = false
    var stoveCoke = 0
    var stoveTemp = 0
    var beltRunning = false
    var makeBars = false
    var beltBroken = false
    var cogBroken = false
    var pumping = false
    var pedaling = false
    var barsHot = false
    var giveSmithXp = 0

    /**This pulse object handles checks between*/
    var blastPulse = object : Pulse() {
        override fun pulse(): Boolean {
            gaugeViewList.forEach {
                var anim = furnaceTemp + 2452
                animateInterface(it, 30, 4, anim)
            }
            blastFurnacePlayerList.forEach {
                if(it.getAttribute("BlastTimer",0) > 0 && it.getSkills().getLevel(Skills.SMITHING) < 60){
                    it.incrementAttribute("BlastTimer",-1)
                } else if(it.getAttribute("BlastTimer",0) <= 0 && it.getSkills().getLevel(Skills.SMITHING) < 60){
                    it.removeAttribute("BlastTimer")
                    sendDialogue(it,"Your time in the Blast Furnace has run out!")
                    it.properties.teleportLocation = Location.create(2931, 10197, 0)
                } else if(it.getAttribute("BlastTimer",false) && it.getSkills().getLevel(Skills.SMITHING) >= 60){
                    it.removeAttribute("BlastTimer")
                }
                if(giveSmithXp > 0){
                    rewardXP(it,Skills.SMITHING,1.0)
                    giveSmithXp--
                }
            }
            //interfaceManager()
            runConveyor()
            stoveCokeTemperature()
            furnaceTemperature()
            operateFurnace()
            breakStuff()
            fixStuff()
            return false
        }
    }

    /**This handles the coke stove temperature, if there is coke in the
     * coke oven the temperature will raise, otherwise the temperature
     * will lower, 0-33 is cold, 34-66 is warm, 67-100 is hot*/
    fun stoveCokeTemperature() {
        if (getWorldTicks() % 10 == 0 && stoveCoke > 0) {
            stoveCoke--
        }
        if (stoveCoke > 0) {
            if (stoveTemp < 100) {
                stoveTemp += 1
            }
        } else if (stoveTemp > 0) {
            stoveTemp--
        }
        if (stoveTemp > 66 && stoveLoc!!.id != 9087) {
            stoveLoc = getScenery(1948, 4963, 0)
            replaceScenery(stoveLoc!!, 9087, -1)
        } else if (stoveTemp in 34..66 && stoveLoc!!.id != 9086) {
            stoveLoc = getScenery(1948, 4963, 0)
            replaceScenery(stoveLoc!!, 9086, -1)
        } else if (stoveTemp < 32 && stoveLoc!!.id != 9085) {
            stoveLoc = getScenery(1948, 4963, 0)
            replaceScenery(stoveLoc!!, 9085, -1)
        }
    }

    /**This handles the raising and lowering of the furnace temperature
     * depending on whether or not the coke stove is hot and if the
     * furnace pump is being operated. A hot coke stove will raise the
     * temperature the fastest, a warm coke stove will raise it slowly,
     * and a cold stove will raise it the slowest*/
    fun furnaceTemperature() {
        if (stoveTemp >= 67 && pumping && furnaceTemp < 100 && (!pumpPipeBroken || !pumpPipeBroken)) {
            furnaceTemp += 3
        } else if (stoveTemp >= 34 && pumping && furnaceTemp < 100 && (!pumpPipeBroken || !pumpPipeBroken)) {
            furnaceTemp += 2
        } else if (stoveTemp > 0 && pumping && furnaceTemp < 100 && (!pumpPipeBroken || !pumpPipeBroken)) {
            furnaceTemp += 1
        } else if (furnaceTemp > 0) {
            furnaceTemp -= 1
        }
    }

    /**Tells the furnace whether or not to smelt bars depending on if
     * the furnace temperature is in the right range. Also contains the
     * logic for smelting the players ore given the fact that they actually
     * have ore in their ore container and/or have coal in their coal container*/
    fun operateFurnace() {
        makeBars = furnaceTemp in 51..66
        blastFurnacePlayerList.forEach { player ->
            var playerCoal = player.blastCoal.getAmount(Items.COAL_453)
            var playerOre = player.blastOre.toArray().filterNotNull()
            var barsAmountFree = player.blastBars.freeSlots()
            var barsAmount = player.blastBars.itemCount()
            var oreAmount = player.blastOre.itemCount()
            var totalAmount = barsAmount + oreAmount
            if (barsHot) {
                setVarbit(player, 936, 2)
            }else if (makeBars && playerOre.isNotEmpty() && barsAmountFree > 0 && totalAmount < 56 && player.getAttribute("OreInPot",false) == true) {
                playerOre.forEach { oreID ->
                    playerCoal = player.blastCoal.getAmount(Items.COAL_453)
                    var bars = arrayOf(Bar.forOre(oreID.id)!!)
                    if(oreID.id == Items.IRON_ORE_440) {
                        bars = arrayOf(Bar.STEEL, Bar.IRON)
                    }
                    inner@ for(bar in bars) {
                        var hasRequirements = true
                        for(required in bar.ores) {
                            if(required.id == Items.COAL_453) {
                                if(!player.blastCoal.contains(Items.COAL_453, required.amount / 2)) {
                                    hasRequirements = false
                                }
                            } else if(!player.blastOre.containsItem(required)) {
                                hasRequirements = false
                            }
                        }
                        if(hasRequirements) {
                            var removed = true
                            for(required in bar.ores) {
                                if(required.id == Items.COAL_453) {
                                    if(!player.blastCoal.remove(Item(Items.COAL_453, required.amount / 2))) {
                                        removed = false
                                    }
                                } else {
                                    if(!player.blastOre.remove(required)) {
                                        removed = false
                                    }
                                }
                            }
                            if(removed) {
                                setVarbit(player, 936, 1)
                                barsHot = true
                                player.blastBars.add(bar.product)
                                var experience = bar.experience
                                if(bar.product.id == Items.GOLD_BAR_2357 &&
                                    player.equipment[EquipmentContainer.SLOT_HANDS] != null &&
                                    player.equipment[EquipmentContainer.SLOT_HANDS].id == GOLDSMITH_GAUNTLETS_776) {
                                    experience *= 2.5;
                                }
                                rewardXP(player, Skills.SMITHING, experience)
                                totalAmount = barsAmount + oreAmount
                                giveSmithXp++
                                break@inner
                            }
                        }
                    }
                }
            }
            if(!barsHot && playerOre.isNotEmpty()) {
                var coalToAdd = 0
                playerOre.forEach { oreID ->
                    when(oreID.id){
                        Items.RUNITE_ORE_451 -> coalToAdd = (player.blastOre.getAmount(451) * 4) - playerCoal
                        Items.ADAMANTITE_ORE_449 -> coalToAdd = (player.blastOre.getAmount(449) * 3) - playerCoal
                        Items.MITHRIL_ORE_447 -> coalToAdd = (player.blastOre.getAmount(447) * 2) - playerCoal
                        Items.IRON_ORE_440 -> coalToAdd = player.blastOre.getAmount(440) - playerCoal
                    }
                }
                if (coalToAdd < 0){
                    setVarbit(player, 940, 0)
                } else setVarbit(player, 940, coalToAdd)
            }
            else if(playerOre.isEmpty()) {
                setVarbit(player, 940, 0)
            }
            if(playerOre.isEmpty() && player.getAttribute("OreInPot",false)){
                player.removeAttribute("OreInPot")
            }
        }
    }

    /**This handles the conveyor belt, if the pedals are running and the belt
     * isn't broken then the conveyor belt will run*/
    fun runConveyor() {
        if (pedaling && (!beltBroken && !cogBroken)) {
            animateScenery(belt1!!, 2435)
            animateScenery(belt2!!, 2435)
            animateScenery(belt3!!, 2435)
            animateScenery(beltLoc1!!, 2436)
            animateScenery(beltLoc2!!, 2436)
            animateScenery(cogs1!!, 2436)
            animateScenery(cogs2!!, 2436)
            animateScenery(gearsLoc!!, 2436)
            beltRunning = true
        } else {
            animateScenery(belt1!!, -1)
            animateScenery(belt2!!, -1)
            animateScenery(belt3!!, -1)
            animateScenery(beltLoc1!!, -1)
            animateScenery(beltLoc2!!, -1)
            animateScenery(cogs1!!, -1)
            animateScenery(cogs2!!, -1)
            animateScenery(gearsLoc!!, -1)
            beltRunning = false
        }

    }

    /**Its just one of those days*/
    fun breakStuff() {
        if (pumping && furnaceTemp > 76) {
            if (RandomFunction.random(1, 100) > 20 && (!potPipeBroken || !pumpPipeBroken)) {
                if (RandomFunction.random(1, 100) > 50 && !potPipeBroken) {
                    pipe1 = getScenery(1943, 4961, 0)
                    potPipeBroken = true
                }
                if (RandomFunction.random(1, 100) <= 50 && !pumpPipeBroken) {
                    pipe2 = getScenery(1947, 4961, 0)
                    pumpPipeBroken = true
                }
            }
        }
        if (beltRunning && (!beltBroken || !cogBroken)) {
            if (RandomFunction.random(1, 100) <= 2) {
                beltLoc2 = getScenery(1944, 4967, 0)
                beltBroken = true
            } else if (RandomFunction.random(1, 100) <= 2) {
                cogs2 = getScenery(1945, 4967, 0)
                cogBroken = true
            }
        }
        if (beltBroken && beltLoc2!!.id != 9103) {
            beltLoc2 = getScenery(1944, 4967, 0)
            replaceScenery(beltLoc2!!, 9103, -1)
        }
        if (cogBroken && cogs2!!.id != 9105) {
            cogs2 = getScenery(1945, 4967, 0)
            replaceScenery(cogs2!!, 9105, -1)
        }
        if (potPipeBroken && pipe1!!.id != 9117) {
            pipe1 = getScenery(1943, 4961, 0)
            replaceScenery(pipe1!!, 9117, -1)
        }
        if (pumpPipeBroken && pipe2!!.id != 9121) {
            pipe2 = getScenery(1947, 4961, 0)
            replaceScenery(pipe2!!, 9121, -1)
        }
    }

    /**This replaces the breakable objects IDs back to their unbroken state*/
    fun fixStuff() {
        if (!potPipeBroken && pipe1!!.id != 9116) {
            pipe1 = getScenery(1943, 4961, 0)
            replaceScenery(pipe1!!, 9116, -1)
        }
        if (!pumpPipeBroken && pipe2!!.id != 9120) {
            pipe2 = getScenery(1947, 4961, 0)
            replaceScenery(pipe2!!, 9120, -1)
        }
        if (!beltBroken && beltLoc2!!.id != 9102) {
            beltLoc2 = getScenery(1944, 4967, 0)
            replaceScenery(beltLoc2!!, 9102, -1)
        }
        if (!cogBroken && cogs2!!.id != 9104) {
            cogs2 = getScenery(1945, 4967, 0)
            replaceScenery(cogs2!!, 9104, -1)
        }
    }
}
