package org.rs09

import org.rs09.client.config.GameConfig
import org.rs09.client.rendering.RenderingUtils
import org.rs09.client.rendering.Toolkit
import org.runite.client.*
import java.util.concurrent.TimeUnit

object SlayerTracker {
    const val posX = 5
    const val posY = 30
    const val textX = 7
    const val textY = 50
    const val spriteY = 30
    val boxColor = "635a38".toInt(16)

    @JvmField
    var lastUpdate = 0L

    var curSprite: AbstractSprite? = null


    @JvmStatic
    fun draw(){
        val timeSinceUpdate = System.currentTimeMillis() - lastUpdate
        if(!GameConfig.slayerCountEnabled) return
        if(GameConfig.slayerTaskAmount == 0) return
        if(timeSinceUpdate >= TimeUnit.MINUTES.toMillis(5L)) return
        val tk = Toolkit.getActiveToolkit()
        var rectWidth = 60
        var amountPos = textX + 40
        if(rectWidth < 90) rectWidth = 90
        if(amountPos < textX + 60) amountPos = textX + 60
        tk.fillRect(posX,posY,rectWidth,30, boxColor,180)

        curSprite?.drawAt(textX, spriteY)
        RenderingUtils.drawText(GameConfig.slayerTaskAmount.toString(),amountPos, textY, -1, 2, false)
    }

    @JvmStatic
    fun reset(){
        GameConfig.slayerTaskAmount = 0
        GameConfig.slayerTaskID = 0
        GameConfig.slayerCountEnabled = false;
        lastUpdate = 0
    }

    @JvmStatic
    fun setSprite(){
        SystemLogger.logInfo("Getting slayer sprite...")
        val itemId: Int =  when(GameConfig.slayerTaskID){
            0 -> 4144
            1 -> 4149
            2 -> 560
            3 -> 10176
            4 -> 4135
            5 -> 4139
            6 -> 14072
            7 -> 948
            8 -> 12189
            9 -> 3098
            10 -> 1747
            11 -> 4141
            12 -> 1751
            13 -> 11047
            14 -> 2349
            15 -> 9008
            16 -> 4521
            17 -> 4134
            18 -> 8900
            19 -> 4520
            20 -> 4137
            21 -> 1739
            22 -> 7982
            23 -> 10149
            24 -> 532
            25 -> 8141
            26 -> 6637
            27 -> 6695
            28 -> 8132
            29 -> 4145
            30 -> 7500
            31 -> 1422
            32 -> 1387
            33 -> 9011
            34 -> 4147
            35 -> 552
            36 -> 6722
            37 -> 10998
            38 -> 9016
            39 -> 2402
            40 -> 1753
            41 -> 7050
            42 -> 8137
            43 -> 12570
            44 -> 8133
            45 -> 4671
            46 -> 4671
            47 -> 1159
            48 -> 4140
            49 -> 2351
            50 -> 4142
            51 -> 7778
            52 -> 8139
            53 -> 4146
            54 -> 2402
            55 -> 2359
            56 -> 12079
            57 -> 12201
            58 -> 12570
            59 -> 4148
            60 -> 4818
            61 -> 6107
            62 -> 4138
            63 -> 14074
            64 -> 4136
            65 -> 6297
            66 -> 10634
            67 -> 553
            68 -> 8135
            69 -> 11732
            70 -> 10284
            71 -> 13923
            72 -> 2353
            73 -> 8136
            74 -> 4143
            75 -> 6528
            76 -> 10109
            77 -> 1403
            78 -> 2952
            79 -> 958
            80 -> 7594
            89 -> 6811
            else -> -1
        }

        var sprite = AbstractSprite.constructItemSprite(0, HDToolKit.highDetail, itemId, false, 1, 1, false)
        if(HDToolKit.highDetail) {
            if (sprite is Class3_Sub28_Sub16_Sub2_Sub1) {
                sprite = Class3_Sub28_Sub16_Sub1_Sub1(sprite as SoftwareSprite)
            } else {
                sprite = HDSprite(sprite as SoftwareSprite)
            }
        }

        curSprite = sprite
    }
}