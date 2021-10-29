package rs09.game.node.entity.skill.farming

import core.game.node.entity.player.Player
import core.tools.RandomFunction
import org.rs09.consts.Items
import rs09.game.system.SystemLogger
import java.util.concurrent.TimeUnit
import kotlin.math.ceil
import kotlin.math.min

class Patch(val player: Player, val patch: FarmingPatch, var plantable: Plantable?, var currentGrowthStage: Int, var isDiseased: Boolean, var isDead: Boolean, var isWatered: Boolean, var nextGrowth: Long, var harvestAmt: Int, var isCheckHealth: Boolean) {
    constructor(player: Player, patch: FarmingPatch) : this(player,patch,null,0,false,false,false,0L,0,false)

    var diseaseMod = 0
    var compost = CompostType.NONE
    var protectionPaid = false
    var cropLives = 3

    fun setNewHarvestAmount() {
        val compostMod = when(compost) {
            CompostType.NONE -> 0
            CompostType.NORMAL -> 1
            CompostType.SUPER -> 2
        }
        harvestAmt = when (plantable) {
            Plantable.LIMPWURT_SEED, Plantable.WOAD_SEED -> 3
            Plantable.MUSHROOM_SPORE -> 6
            Plantable.WILLOW_SAPLING -> 0
            else -> 1
        }
        if(plantable != null && plantable?.applicablePatch != PatchType.FLOWER) {
            harvestAmt += compostMod
        }
        cropLives = 3 + compostMod
    }

    fun rollLivesDecrement(farmingLevel: Int, magicSecateurs: Boolean){
        if(patch.type == PatchType.HERB){
            //authentic formula thanks to released data.
            var herbSaveLow = when(plantable){
                Plantable.GUAM_SEED         -> min(24 + farmingLevel, 80)
                Plantable.MARRENTILL_SEED   -> min(28 + farmingLevel, 80)
                Plantable.TARROMIN_SEED     -> min(31 + farmingLevel, 80)
                Plantable.HARRALANDER_SEED  -> min(36 + farmingLevel, 80)
                Plantable.GOUT_TUBER        -> min(39 + farmingLevel, 80)
                Plantable.RANARR_SEED       -> min(39 + farmingLevel, 80)
                Plantable.SPIRIT_WEED_SEED  -> min(43 + farmingLevel, 80)
                Plantable.TOADFLAX_SEED     -> min(43 + farmingLevel, 80)
                Plantable.IRIT_SEED         -> min(46 + farmingLevel, 80)
                Plantable.AVANTOE_SEED      -> min(50 + farmingLevel, 80)
                Plantable.KWUARM_SEED       -> min(54 + farmingLevel, 80)
                Plantable.SNAPDRAGON_SEED   -> min(57 + farmingLevel, 80)
                Plantable.CADANTINE_SEED    -> min(60 + farmingLevel, 80)
                Plantable.LANTADYME_SEED    -> min(64 + farmingLevel, 80)
                Plantable.DWARF_WEED_SEED   -> min(67 + farmingLevel, 80)
                Plantable.TORSTOL_SEED      -> min(71 + farmingLevel, 80)
                else -> -1
            }

            if(magicSecateurs) herbSaveLow = ceil(1.10 * herbSaveLow).toInt()

            val rand = RandomFunction.random(256)

            if(rand > herbSaveLow){
                cropLives -= 1
            }
        } else {
            //inauthentic formulae based on reported averages due to lack of formula
            var chance = when(patch.type){
                PatchType.ALLOTMENT -> 8 //average of 8 per life times 3 lives = average 24
                PatchType.HOPS -> 6 //average of 6 per life times 3 lives = 18
                PatchType.BELLADONNA -> 2 //average of 2 per life times 3 lives = 6
                PatchType.EVIL_TURNIP -> 2 //average 2 per, same as BELLADONNA
                PatchType.CACTUS -> 3 //average of 3 per life times 3 lives = 9
                else -> 0 // nothing should go here, but if it does, do not give extra crops amd decrement cropLives
            }

            if(magicSecateurs) chance += ceil(1.10 * chance).toInt() //will increase average yield by roughly 3.

            if(RandomFunction.roll(chance)) cropLives -= 1
        }

        if(cropLives <= 0) clear()
    }

    fun isWeedy(): Boolean {
        return getCurrentState() in 0..3
    }

    fun getCurrentState(): Int{
        return player.varpManager.get(patch.varpIndex).getVarbitValue(patch.varpOffset)
    }

    fun setCurrentState(state: Int){
        player.varpManager.get(patch.varpIndex).setVarbit(patch.varpOffset,state)
        updateBit()
    }

    fun isGrown(): Boolean{
        return currentGrowthStage == (plantable?.stages ?: 0)
    }

    private fun updateBit(){
        if(isCheckHealth){
            when(patch.type){
                PatchType.FRUIT_TREE -> player.varpManager.get(patch.varpIndex).setVarbit(patch.varpOffset,plantable!!.value + plantable!!.stages + 20)
                PatchType.BUSH -> player.varpManager.get(patch.varpIndex).setVarbit(patch.varpOffset,250 + (plantable!!.ordinal - Plantable.REDBERRY_SEED.ordinal))
                PatchType.CACTUS -> player.varpManager.get(patch.varpIndex).setVarbit(patch.varpOffset, 31)
                else -> SystemLogger.logWarn("Invalid setting of isCheckHealth for patch type: " + patch.type.name)
            }
        } else {
            when(patch.type){
                PatchType.ALLOTMENT,PatchType.FLOWER,PatchType.HOPS -> {
                    player.varpManager.get(patch.varpIndex).setVarbit(patch.varpOffset + 6, if (isWatered || isDead) 1 else 0)
                    player.varpManager.get(patch.varpIndex).setVarbit(patch.varpOffset + 7, if (isDiseased) 1 else 0)
                }
                PatchType.BUSH -> {
                    if(isDead) player.varpManager.get(patch.varpIndex).setVarbit(patch.varpOffset,getBushDeathValue())
                    else if(isDiseased && !isDead) player.varpManager.get(patch.varpIndex).setVarbit(patch.varpOffset,getBushDiseaseValue())
                    //else player.varpManager.get(patch.varpIndex).setVarbit(patch.varpOffset, (plantable?.value ?: 0) + currentGrowthStage)
                }
                PatchType.TREE -> {
                    if(isDead) player.varpManager.get(patch.varpIndex).setVarbit(patch.varpOffset + 7,1)
                    else if(isDiseased && !isDead) player.varpManager.get(patch.varpIndex).setVarbit(patch.varpOffset + 6, 1)
                }
                PatchType.FRUIT_TREE -> {
                    if(isDead) player.varpManager.get(patch.varpIndex).setVarbit(patch.varpOffset,getFruitTreeDeathValue())
                    else if(isDiseased && !isDead) player.varpManager.get(patch.varpIndex).setVarbit(patch.varpOffset, getFruitTreeDiseaseValue())
                }
                PatchType.BELLADONNA -> {
                    if(isDead) player.varpManager.get(patch.varpIndex).setVarbit(patch.varpOffset, getBelladonnaDeathValue())
                    else if(isDiseased && !isDead) player.varpManager.get(patch.varpIndex).setVarbit(patch.varpOffset, getBelladonnaDiseaseValue())
                    else player.varpManager.get(patch.varpIndex).setVarbit(patch.varpOffset, (plantable?.value ?: 0) + currentGrowthStage)
                }
                PatchType.CACTUS -> {
                    if(isDead) player.varpManager.get(patch.varpIndex).setVarbit(patch.varpOffset, getCactusDeathValue())
                    else if(isDiseased && !isDead) player.varpManager.get(patch.varpIndex).setVarbit(patch.varpOffset, getCactusDiseaseValue())
                }
                PatchType.HERB -> {
                    if(isDead) player.varpManager.get(patch.varpIndex).setVarbit(patch.varpOffset, getHerbDeathValue())
                    else if(isDiseased && !isDead) player.varpManager.get(patch.varpIndex).setVarbit(patch.varpOffset, getHerbDiseaseValue())
                    else player.varpManager.get(patch.varpIndex).setVarbit(patch.varpOffset, (plantable?.value ?: 0) + currentGrowthStage)
                }
                else -> {}
            }
        }
        player.varpManager.get(patch.varpIndex).send(player)
    }

    fun cureDisease() {
        isDiseased = false
        when(patch.type){
            PatchType.BUSH,PatchType.FRUIT_TREE -> player.varpManager.get(patch.varpIndex).setVarbit(patch.varpOffset,(plantable?.value ?: 0) + currentGrowthStage)
            PatchType.TREE -> player.varpManager.get(patch.varpIndex).clearBitRange(patch.varpOffset + 6, patch.varpOffset + 7)
            PatchType.CACTUS -> player.varpManager.get(patch.varpIndex).setVarbit(patch.varpOffset, (plantable?.value ?: 0) + currentGrowthStage)
            else -> {}
        }
        updateBit()
    }

    fun water() {
        isWatered = true
        updateBit()
    }

    private fun getBushDiseaseValue(): Int{
        if(plantable == Plantable.POISON_IVY_SEED){
            return (plantable?.value ?: 0) + currentGrowthStage + 12
        } else {
            return (plantable?.value ?: 0) + currentGrowthStage + 64
        }
    }

    private fun getBushDeathValue(): Int{
        if(plantable == Plantable.POISON_IVY_SEED){
            return (plantable?.value ?: 0) + currentGrowthStage + 22
        } else {
            return (plantable?.value ?: 0) + currentGrowthStage + 126
        }
    }

    private fun getFruitTreeDiseaseValue(): Int {
        return (plantable?.value ?: 0) + currentGrowthStage + 12
    }

    private fun getFruitTreeDeathValue(): Int {
        return (plantable?.value ?: 0) + currentGrowthStage + 18
    }

    private fun getBelladonnaDiseaseValue(): Int {
        return (plantable?.value ?: 0) + currentGrowthStage + 4
    }

    private fun getBelladonnaDeathValue(): Int {
        return (plantable?.value ?: 0) + currentGrowthStage + 7
    }

    private fun getCactusDiseaseValue(): Int {
        return (plantable?.value ?: 0) + currentGrowthStage + 10
    }

    private fun getCactusDeathValue(): Int {
        return (plantable?.value ?: 0) + currentGrowthStage + 16
    }

    private fun getHerbDiseaseValue(): Int {
        return if (plantable?.value ?: -1 <= 103) {
            128 + (((plantable?.ordinal ?: 0) - Plantable.GUAM_SEED.ordinal) * 3) + currentGrowthStage - 1
        } else if (plantable == Plantable.SPIRIT_WEED_SEED) {
            211 + currentGrowthStage - 1
        } else {
            198 + currentGrowthStage -1
        }
    }

    private fun getHerbDeathValue(): Int {
        return if(plantable == Plantable.GOUT_TUBER){
            201 + currentGrowthStage - 1
        } else 170 + currentGrowthStage - 1
    }

    private fun grow(){
        if(isWeedy() && getCurrentState() > 0) {
            nextGrowth = System.currentTimeMillis() + 60000
            setCurrentState(getCurrentState() - 1)
            currentGrowthStage--
            return
        }

        if(isDiseased){
            isDead = true
            return
        }

        diseaseMod = when(compost){
            CompostType.NONE -> 0
            CompostType.NORMAL -> 8
            CompostType.SUPER -> 13
        }

        if(patch != FarmingPatch.TROLL_STRONGHOLD_HERB && RandomFunction.random(128) <= (17 - diseaseMod) && !isWatered && !isGrown() && !protectionPaid && !isFlowerProtected() && patch.type != PatchType.EVIL_TURNIP ){
            //bush, tree, fruit tree, herb and cactus can not disease on stage 1(0) of growth.
            if(!((patch.type == PatchType.BUSH || patch.type == PatchType.TREE || patch.type == PatchType.FRUIT_TREE || patch.type == PatchType.CACTUS || patch.type == PatchType.HERB) && currentGrowthStage == 0)) {
                isDiseased = true
                return
            }
        }

        if((patch.type == PatchType.FRUIT_TREE || patch.type == PatchType.TREE || patch.type == PatchType.BUSH || patch.type == PatchType.CACTUS) && plantable != null && plantable?.stages == currentGrowthStage + 1){
            isCheckHealth = true
        }

        if((patch.type == PatchType.FRUIT_TREE || patch.type == PatchType.BUSH || patch.type == PatchType.CACTUS) && plantable?.stages == currentGrowthStage){
            if((patch.type == PatchType.BUSH && getFruitOrBerryCount() < 4) || (patch.type == PatchType.FRUIT_TREE && getFruitOrBerryCount() < 6) || (patch.type == PatchType.CACTUS && getFruitOrBerryCount() < 3)){
                setCurrentState(getCurrentState() + 1)
            }
        }
        if(patch.type == PatchType.TREE) {
            // Willow branches
            if(harvestAmt < 6) {
                harvestAmt++;
            }
        }

        if(plantable?.stages ?: 0 > currentGrowthStage && !isGrown()){
            currentGrowthStage++
            setCurrentState(getCurrentState() + 1)
            isWatered = false
        }

        regrowIfTreeStump()
    }

    fun regrowIfTreeStump() {
        if(patch.type == PatchType.TREE && plantable != null) {
            // plantable.value + plantable.stages is the check-health stage, so +1 is the choppable tree, and +2 is the stump
            if(getCurrentState() == plantable!!.value + plantable!!.stages + 2) {
                setCurrentState(getCurrentState() - 1)
                isWatered = false
            }
        }
    }

    fun update(){
        grow()
        updateBit()
    }

    fun plant(plantable: Plantable){
        nextGrowth = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(plantable.applicablePatch.stageGrowthTime.toLong())
        this.plantable = plantable
        isDead = false
        isDiseased = false
        currentGrowthStage = 0
        setCurrentState(plantable.value)
    }

    fun clear(){
        isCheckHealth = false
        isDiseased = false
        isDead = false
        plantable = null
        player.varpManager.get(patch.varpIndex).clearBitRange(patch.varpOffset,patch.varpOffset + 7)
        nextGrowth = 0L
        currentGrowthStage = 3
        setCurrentState(3)
        compost = CompostType.NONE
        protectionPaid = false
    }

    fun getFruitOrBerryCount() : Int {
        plantable ?: return 0
        return getCurrentState() - plantable!!.value - plantable!!.stages
    }

    fun isFlowerProtected(): Boolean{
        if(patch.type != PatchType.ALLOTMENT) return false

        val fpatch = when(patch){
            FarmingPatch.S_FALADOR_ALLOTMENT_SE,FarmingPatch.S_FALADOR_ALLOTMENT_NW -> FarmingPatch.S_FALADOR_FLOWER_C
            FarmingPatch.ARDOUGNE_ALLOTMENT_S,FarmingPatch.ARDOUGNE_ALLOTMENT_N -> FarmingPatch.ARDOUGNE_FLOWER_C
            FarmingPatch.CATHERBY_ALLOTMENT_S,FarmingPatch.CATHERBY_ALLOTMENT_N -> FarmingPatch.CATHERBY_FLOWER_C
            FarmingPatch.PORT_PHAS_ALLOTMENT_SE,FarmingPatch.PORT_PHAS_ALLOTMENT_NW -> FarmingPatch.PORT_PHAS_FLOWER_C
            else -> return false
        }.getPatchFor(player)

        return (fpatch.plantable != null &&
            (fpatch.plantable == plantable?.protectionFlower || fpatch.plantable == Plantable.forItemID(Items.WHITE_LILY_SEED_14589))
            && fpatch.isGrown())
    }
}
