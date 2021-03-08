package org.rs09

import org.rs09.client.config.GameConfig
import org.rs09.client.rendering.RenderingUtils
import org.rs09.client.rendering.Toolkit
import java.util.concurrent.TimeUnit

object SlayerTracker {
    const val posX = 5
    const val posY = 30
    const val textX = 7
    const val textY = 50

    @JvmField
    var lastUpdate = 0L

    @JvmStatic
    fun draw(){
        val timeSinceUpdate = System.currentTimeMillis() - lastUpdate
        if(!GameConfig.slayerCountEnabled) return
        if(GameConfig.slayerTaskAmount == 0) return
        if(timeSinceUpdate >= TimeUnit.MINUTES.toMillis(5L)) return
        val tk = Toolkit.getActiveToolkit()
        var rectWidth = 9 * getTask().length
        var amountPos = textX + (6 * getTask().length)
        if(rectWidth < 90) rectWidth = 90
        if(amountPos < textX + 60) amountPos = textX + 60
        tk.fillRect(posX,posY,rectWidth,30,0,64)
        RenderingUtils.drawText(getTask() + ":", textX, textY,-1,2,false)
        RenderingUtils.drawText(GameConfig.slayerTaskAmount.toString(),amountPos, textY, -1, 2, false)
    }

    @JvmStatic
    fun reset(){
        GameConfig.slayerTaskAmount = 0
        GameConfig.slayerTaskID = 0
        GameConfig.slayerCountEnabled = false;
        lastUpdate = 0
    }

    fun getTask(): String {
        return when(GameConfig.slayerTaskID){
            0 -> "Abby Spectres"
            1 -> "Abyssal Demons"
            2 -> "Ankou"
            3 -> "Aviansies"
            4 -> "Banshee"
            5 -> "Basilisks"
            6 -> "Bats"
            7 -> "Bears"
            8 -> "Birds"
            9 -> "Black Demons"
            10 -> "Black Dragons"
            11 -> "Bloodvelds"
            12 -> "Blue Dragons"
            13 -> "Brine Rats"
            14 -> "Bronze Dragons"
            15 -> "Catablepons"
            16 -> "Cave Bugs"
            17 -> "Cave Crawlers"
            18 -> "Cave Horrors"
            19 -> "Cave Slimes"
            20 -> "Cockatrices"
            21 -> "Cows"
            22 -> "Crawling Hands"
            23 -> "Crocodiles"
            24 -> "Cyclopes"
            25 -> "Dagannoths"
            26 -> "Dark Beasts"
            27 -> "Desert Lizards"
            28 -> "Dogs"
            29 -> "Dust Devils"
            30 -> "Dwarves"
            31 -> "Earth Warriors"
            32 -> "Fire Giants"
            33 -> "Flesh Crawlers"
            34 -> "Gargoyles"
            35 -> "Ghosts"
            36 -> "Ghouls"
            37 -> "Goblins"
            38 -> "Goraks"
            39 -> "Greater Demons"
            40 -> "Green Dragons"
            41 -> "Harpie Swarms"
            42 -> "Hellhounds"
            43 -> "Hill Giants"
            44 -> "Hobgoblins"
            45 -> "Ice Fiends"
            46 -> "Ice Giants"
            47 -> "Ice Warriors"
            48 -> "Infernal Mages"
            49 -> "Iron Dragons"
            50 -> "Jellies"
            51 -> "Jungle Horrors"
            52 -> "Kalphites"
            53 -> "Kurasks"
            54 -> "Lesser Demons"
            55 -> "Mithril Dragons"
            56 -> "Minotaurs"
            57 -> "Monkeys"
            58 -> "Moss Giants"
            59 -> "Nechryaels"
            60 -> "Ogres"
            61 -> "Otherworldly Beings"
            62 -> "Pyrefiends"
            63 -> "Rats"
            64 -> "Rock Slugs"
            65 -> "Scorpions"
            66 -> "Shades"
            67 -> "Skeletons"
            68 -> "Spiders"
            69 -> "Spiritual Mages"
            70 -> "Spiritual Rangers"
            71 -> "Spiritual Warriors"
            72 -> "Steel Dragons"
            73 -> "Trolls"
            74 -> "Turoths"
            75 -> "Tzhaar"
            76 -> "Vampires"
            77 -> "Waterfiends"
            78 -> "Werewolves"
            79 -> "Wolves"
            80 -> "Zombies"
            89 -> "Skeletal Wyverns"
            else -> "Uhh Report Me?"
        }
    }
}