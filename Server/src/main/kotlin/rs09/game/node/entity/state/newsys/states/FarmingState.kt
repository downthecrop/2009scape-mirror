package rs09.game.node.entity.state.newsys.states

import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import rs09.game.node.entity.skill.farming.*
import rs09.game.node.entity.state.newsys.PlayerState
import rs09.game.node.entity.state.newsys.State
import rs09.game.system.SystemLogger
import java.util.concurrent.TimeUnit

@PlayerState("farming")
class FarmingState(player: Player? = null) : State(player) {
    private val patchMap = HashMap<FarmingPatch, Patch>()
    private val binMap = HashMap<CompostBins, CompostBin>()


    fun getPatch(patch: FarmingPatch): Patch {
        return patchMap[patch] ?: (Patch(player!!,patch).also { patchMap[patch] = it })
    }

    fun getBin(bin: CompostBins) : CompostBin{
        return binMap[bin] ?: (CompostBin(player!!,bin).also { binMap[bin] = it })
    }

    fun getPatches(): MutableCollection<Patch>{
        return patchMap.values
    }

    fun getBins(): MutableCollection<CompostBin>{
        return binMap.values
    }

    override fun save(root: JSONObject) {
        val patches = JSONArray()
        for((key,patch) in patchMap){
            val p = JSONObject()
            p.put("patch-ordinal",key.ordinal)
            p.put("patch-plantable-ordinal",patch.plantable?.ordinal ?: -1)
            p.put("patch-watered",patch.isWatered)
            p.put("patch-diseased",patch.isDiseased)
            p.put("patch-dead",patch.isDead)
            p.put("patch-stage",patch.currentGrowthStage)
            p.put("patch-state",patch.getCurrentState())
            p.put("patch-nextGrowth",patch.nextGrowth)
            p.put("patch-harvestAmt",patch.harvestAmt)
            p.put("patch-checkHealth",patch.isCheckHealth)
            p.put("patch-compost",patch.compost.ordinal)
            p.put("patch-paidprot",patch.protectionPaid)
            p.put("patch-croplives", patch.cropLives)
            patches.add(p)
        }
        val bins = JSONArray()
        for((key,bin) in binMap){
            val b = JSONObject()
            b.put("bin-ordinal",key.ordinal)
            bin.save(b)
            bins.add(b)
        }
        root.put("farming-patches",patches)
        root.put("farming-bins",bins)
    }

    override fun parse(_data: JSONObject) {
        player ?: return
        if(_data.containsKey("farming-bins")){
            (_data["farming-bins"] as JSONArray).forEach {
                val bin = it as JSONObject
                val binOrdinal = bin["bin-ordinal"].toString().toInt()
                val cBin = CompostBins.values()[binOrdinal]
                val b = cBin.getBinForPlayer(player)
                b.parse(bin["binData"] as JSONObject)
                SystemLogger.logErr("bin size " + binMap.size)
            }
        }
        if(_data.containsKey("farming-patches")){
            val data = _data["farming-patches"] as JSONArray
            for(d in data){
                val p = d as JSONObject
                val patchOrdinal = p["patch-ordinal"].toString().toInt()
                val patchPlantableOrdinal = p["patch-plantable-ordinal"].toString().toInt()
                val patchWatered = p["patch-watered"] as Boolean
                val patchDiseased = p["patch-diseased"] as Boolean
                val patchDead = p["patch-dead"] as Boolean
                val patchStage = p["patch-stage"].toString().toInt()
                val nextGrowth = p["patch-nextGrowth"].toString().toLong()
                val harvestAmt = (p["patch-harvestAmt"] ?: 0).toString().toInt()
                val checkHealth = p["patch-checkHealth"] as Boolean
                val savedState = p["patch-state"].toString().toInt()
                val compostOrdinal = p["patch-compost"].toString().toInt()
                val protectionPaid = p["patch-paidprot"] as Boolean
                val cropLives = if(p["patch-croplives"] != null) p["patch-croplives"].toString().toInt() else 3
                val fPatch = FarmingPatch.values()[patchOrdinal]
                val plantable = if(patchPlantableOrdinal != -1) Plantable.values()[patchPlantableOrdinal] else null
                val patch = Patch(player,fPatch,plantable,patchStage,patchDiseased,patchDead,patchWatered,nextGrowth,harvestAmt,checkHealth)

                patch.cropLives = cropLives
                patch.compost = CompostType.values()[compostOrdinal]
                patch.protectionPaid = protectionPaid
                patch.setCurrentState(savedState)
                if((patch.currentGrowthStage < patch.plantable?.stages ?: 0) && !patch.isWeedy()){
                    val startTime = (patch.nextGrowth - TimeUnit.MINUTES.toMillis(patch.patch.type.stageGrowthTime * (patch.currentGrowthStage + 1).toLong()))
                    var expectedStage = Math.floor((System.currentTimeMillis() - startTime.toDouble()) / TimeUnit.MINUTES.toMillis(patch.patch.type.stageGrowthTime.toLong())).toInt()
                    SystemLogger.logErr("$expectedStage $startTime ${System.currentTimeMillis()}")

                    if(!patchDiseased && !patchDead) {
                        if (expectedStage > patch.plantable?.stages ?: 0) {
                            expectedStage = patch.plantable?.stages ?: 0
                        }
                        for(i in 0 until (expectedStage - patch.currentGrowthStage)){
                            patch.update()
                        }
                    }
                }

                if((savedState - (patch?.plantable?.value ?: 0)) > patch.currentGrowthStage){
                    patch.setCurrentState(savedState)
                } else {
                    patch.setCurrentState((patch.plantable?.value ?: 0) + patch.currentGrowthStage)
                }

                if(patchMap[fPatch] == null) {
                    patchMap[fPatch] = patch
                }
            }
        }
    }

    override fun newInstance(player: Player?): State {
        return FarmingState(player)
    }

    override fun createPulse() {
        pulse = object : Pulse(3){
            override fun pulse(): Boolean {

                GlobalScope.launch {
                    var removeList = ArrayList<FarmingPatch>()
                    for((_,patch) in patchMap){

                        if(patch.getCurrentState() in 1..3 && patch.nextGrowth == 0L){
                            patch.nextGrowth = System.currentTimeMillis() + 60000
                            continue
                        }

                        if(patch.nextGrowth < System.currentTimeMillis() && !patch.isDead){
                            patch.update()
                            var minutes = patch.patch.type.stageGrowthTime.toLong()
                            if(patch.patch.type == PatchType.FRUIT_TREE && patch.isGrown()) {
                                // Fruit trees take 160 minutes per stage to grow, but
                                // restocking their fruit should take 40 minutes per fruit
                                minutes = 40
                            }
                            patch.nextGrowth = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(minutes)
                        }

                    }

                    for((_,bin) in binMap){
                        if(bin.isReady() && !bin.isFinished){
                            bin.finish()
                        }
                    }
                }

                return false
            }
        }
    }
}
