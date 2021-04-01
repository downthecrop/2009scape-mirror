package org.rs09

import org.rs09.client.config.GameConfig
import org.rs09.client.rendering.RenderingUtils
import org.rs09.client.rendering.Toolkit
import org.runite.client.*
import java.util.*
import kotlin.math.ceil

/**
 * Handles tracking and drawing of xp drops
 * @author Ceikry
 */
object XPGainDraw {
    internal class XPGain(var skillId: Int, var xpGain: Int, var currentPos: Int)
    @JvmStatic
    var lastXp = Array<Int>(24){0}

    @JvmStatic
    private var thisLoopGains = ArrayList<XPGain>()

    var posX = RenderingUtils.width / 2
    var posY = RenderingUtils.height / 4
    var skillSprite: AbstractSprite? = null
    var lastSkillId = 0

    var gainsAddedThisLoop = 0
    var totalXP = 0
    var lastUpdate = 0L
    var lastGain = 0L

    const val line = 110
    const val clearHeight = 60

    @JvmStatic
    fun addGain(xpGain: Int,skillId: Int){
        if(GameConfig.xpDropMode == 1){
            totalXP += xpGain
        }
        if(xpGain <= 0) return
        if(gainsAddedThisLoop > 6) return
        if(lastXp[skillId] == 0){
            if(GameConfig.xpDropMode == 0) {
                totalXP += xpGain
            }
            return
        }
        var drawAt = line
        if(thisLoopGains.isNotEmpty()) {
            if (thisLoopGains[thisLoopGains.size - 1].currentPos + 25 > line) {
                drawAt = thisLoopGains[thisLoopGains.size - 1].currentPos + 25
            }
        }
        thisLoopGains.add(XPGain(skillId,xpGain,drawAt))
        gainsAddedThisLoop++
        if(System.currentTimeMillis() - lastUpdate > 10000) lastUpdate = System.currentTimeMillis()
        lastGain = System.currentTimeMillis()
    }

    @JvmStatic
    fun drawGains() {
        if(!GameConfig.xpDropsEnabled){
            return
        }
        if(System.currentTimeMillis() - lastGain > 10000) return
        posX = RenderingUtils.width / 2
        posY = RenderingUtils.height / 4
        if(posX == 382) { //Client is in fixed mode
            posX += 60
        }
        drawTotal()
        if(GameConfig.xpTrackMode == 1) {
            skillSprite?.drawAt(posX - 65, 10)
            RenderingUtils.drawText(addCommas(lastXp[lastSkillId].toString()), posX - 40, 28, -1, 2, false)
        }
        val timeDelta = getTimeDelta()
        val removeList = ArrayList<XPGain>()
        for(gain in thisLoopGains) {
            gain.currentPos -= ceil(timeDelta / 20.0).toInt()
            if(gain.currentPos <= clearHeight){
                if(GameConfig.xpDropMode == 0) {
                    totalXP += gain.xpGain
                }
                removeList.add(gain)
                continue;
            }

            val sprite = getSprite(getSpriteArchive(gain.skillId))
            sprite?.drawAt((posX) - 25, gain.currentPos - 20)
            RenderingUtils.drawText("${gain.xpGain}",posX, gain.currentPos,-1,2,false)
            skillSprite = sprite
            lastSkillId = gain.skillId
        }
        lastUpdate = System.currentTimeMillis()
        thisLoopGains.removeAll(removeList)
        gainsAddedThisLoop = 0
        if(Discord.initialized) {
            Discord.updatePresence("In-Game", "Training", "skill_$lastSkillId")
        }
    }

    fun drawTotal() {
        val tk = Toolkit.getActiveToolkit()
        RenderingUtils.setClipping(0,0,RenderingUtils.width,500)
        val horizontal = getSprite(822)
        val horizontalTop = getSprite(820)
        val tl_corner = getSprite(824)
        val bl_corner = getSprite(826)
        val tr_corner = getSprite(825)
        val br_corner = getSprite(827)
        val bg = getSprite(657)
        bg?.drawAt(posX - 77,9)
        tk.fillRect(posX - 75,5,140,30,0,64)
        bl_corner?.drawAt(posX - 77,10)
        tl_corner?.drawAt(posX - 77,5)
        tr_corner?.drawAt(posX + 41,5)
        br_corner?.drawAt(posX + 41,10)
        horizontalTop?.drawAt(posX - 45,-8)
        horizontal?.drawAt(posX - 45,22)
        horizontalTop?.drawAt(posX - 15,-8)
        horizontal?.drawAt(posX - 15,22)
        horizontalTop?.drawAt(posX + 9,-8)
        horizontal?.drawAt(posX + 9,22)
        if(GameConfig.xpTrackMode == 0){
            RenderingUtils.drawText("Total XP: " + addCommas(totalXP.toString()),posX - 65,28,-1,2,false)
        }
    }

    fun getSpriteArchive(skillId: Int): Int{
        return when(skillId) {
            0 -> 197
            1 -> 199
            2 -> 198
            3 -> 203
            4 -> 200
            5 -> 201
            6 -> 202
            7 -> 212
            8 -> 214
            9 -> 208
            10 -> 211
            11 -> 213
            12 -> 207
            13 -> 210
            14 -> 209
            15 -> 205
            16 -> 204
            17 -> 206
            18 -> 216
            19 -> 217
            20 -> 215
            21 -> 220
            22 -> 221
            23 -> 222
            else -> 222
        }
    }

    fun getSprite(archiveIndex: Int): AbstractSprite? {
        var rawSprite: AbstractSprite? = null
        if(CacheIndex.spritesIndex.retrieveSpriteFile(archiveIndex)){
            rawSprite = Unsorted.method562(CacheIndex.spritesIndex, archiveIndex)
            if(HDToolKit.highDetail){
                if(rawSprite is Class3_Sub28_Sub16_Sub2_Sub1){
                    rawSprite = Class3_Sub28_Sub16_Sub1_Sub1(rawSprite as SoftwareSprite)
                } else {
                    rawSprite = HDSprite(rawSprite as SoftwareSprite)
                }
            }
        }
        return rawSprite
    }

    fun addCommas(num: String): String{
        var newString = ""
        if(num.length > 9){
            return "Lots!"
        }
        var counter = 1
        num.reversed().forEach {
            if(counter % 3 == 0 && counter != num.length){
                newString += "$it,"
            } else {
                newString += it
            }
            counter++
        }
        return newString.reversed()
    }

    @JvmStatic
    fun reset(){
        totalXP = 0
        lastXp = Array(24){0}
        lastUpdate = 0
        thisLoopGains.clear()
        lastSkillId = 0
        skillSprite = null
    }

    fun getTimeDelta(): Long{
        return System.currentTimeMillis() - lastUpdate
    }
}