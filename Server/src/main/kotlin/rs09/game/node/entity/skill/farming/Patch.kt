package rs09.game.node.entity.skill.farming

import core.game.node.entity.player.Player
import core.tools.RandomFunction
import rs09.game.system.SystemLogger
import java.util.concurrent.TimeUnit

class Patch(val player: Player, val patch: FarmingPatch, var plantable: Plantable?, var currentGrowthStage: Int, var isDiseased: Boolean, var isDead: Boolean, var isWatered: Boolean, var nextGrowth: Long, var harvestAmt: Int, var isCheckHealth: Boolean) {
    constructor(player: Player, patch: FarmingPatch) : this(player,patch,null,0,false,false,false,0L,0,false)

    var diseaseMod = 0
    var compost = CompostType.NONE
    var protectionPaid = false

    fun setNewHarvestAmount() {
        if(patch.type == PatchType.ALLOTMENT){
            harvestAmt = RandomFunction.random(4,17)
        } else if(patch.type == PatchType.FLOWER) {
            harvestAmt = when (plantable) {
                Plantable.LIMPWURT_SEED, Plantable.WOAD_SEED -> 3
                else -> 1
            }
        } else if(patch.type == PatchType.HOPS){
            harvestAmt = RandomFunction.random(3,35)
        } else {
            harvestAmt = RandomFunction.random(3,5)
        }
        if(compost == CompostType.NORMAL) harvestAmt += 1
        if(compost == CompostType.SUPER) harvestAmt += 2
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
                    else player.varpManager.get(patch.varpIndex).setVarbit(patch.varpOffset, plantable?.value ?: 0 + currentGrowthStage)
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
            return (plantable?.value ?: 0) + currentGrowthStage + 13
        } else {
            return (plantable?.value ?: 0) + currentGrowthStage + 65
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

        if(RandomFunction.random(128) <= (17 - diseaseMod) && !isWatered && !isGrown() && !protectionPaid && !isFlowerProtected()){
            //bush, tree, fruit tree can not disease on stage 1(0) of growth.
            if(!((patch.type == PatchType.BUSH || patch.type == PatchType.TREE || patch.type == PatchType.FRUIT_TREE) && currentGrowthStage == 0)) {
                isDiseased = true
                return
            }
        }

        if((patch.type == PatchType.FRUIT_TREE || patch.type == PatchType.TREE || patch.type == PatchType.BUSH) && plantable != null && plantable?.stages == currentGrowthStage + 1){
            isCheckHealth = true
        }

        if((patch.type == PatchType.FRUIT_TREE || patch.type == PatchType.BUSH) && plantable?.stages == currentGrowthStage){
            if((patch.type == PatchType.BUSH && getFruitOrBerryCount() < 4) || (patch.type == PatchType.FRUIT_TREE && getFruitOrBerryCount() < 6)){
                setCurrentState(getCurrentState() + 1)
            }
        }

        if(plantable?.stages ?: 0 > currentGrowthStage && !isGrown()){
            currentGrowthStage++
            setCurrentState(getCurrentState() + 1)
            isWatered = false
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

        return (fpatch.plantable != null && fpatch.plantable == plantable?.protectionFlower && fpatch.isGrown())
    }
}