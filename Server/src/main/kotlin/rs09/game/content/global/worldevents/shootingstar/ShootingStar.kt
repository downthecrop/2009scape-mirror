package rs09.game.content.global.worldevents.shootingstar

import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import rs09.ServerStore.Companion.getBoolean
import rs09.ServerStore.Companion.getInt
import rs09.ServerStore.Companion.getString

import rs09.game.ai.general.scriptrepository.ShootingStarBot
import rs09.game.world.repository.Repository

/**
 * Represents a shooting star object (Only ever initialized once) (ideally)
 * @author Ceikry
 */
class ShootingStar(var level: ShootingStarType = ShootingStarType.values().random()){
    val crash_locations = mapOf(
            "East of Dark Wizards' Tower" to Location.create(2925, 3339, 0), // East of Dark Wizards' Tower
            "Crafting Guild" to Location.create(2940, 3280, 0), // Crafting Guild Mine
            "Falador East Bank" to Location.create(3030, 3349, 0), // Falador East Bank
            "Rimmington mining site" to Location.create(2974, 3240, 0), // Rimmington Mine 2974, 3240, 0)
            "Karamja northwestern mining site" to Location.create(2737, 3223, 0), // Karamja northwestern mining site/Brimhaven gold rocks
            "Brimhaven mining site" to Location.create(2743, 3143, 0), // Brimhaven mining site
            "South Crandor mining site" to Location.create(2822, 3239, 0), // South Crandor mining site (requires Dragon Slayer)
            "Karamja mining site" to Location.create(2854, 3032, 0), // Karamja mining site
            "Shilo Village mining site" to Location.create(2826, 2997, 0), // Shilo Village mining site/Gem rocks
            "Relleka mining site" to Location.create(2682, 3700, 0), // Rellekka mining site
            "Jatizso mine" to Location.create(2393, 3815, 0), //Jatiszo mining site (requires Fremennik Trials)
            //"Lunar Isle mine" to Location.create(2140, 3939, 0), // Lunar Isle mine (requires Lunar Diplomacy?)
            //"Miscellania coal mine" to Location.create(2529, 3887, 0), // Miscellania coal mine (requires Fremennik Trials)
            //"Neitiznot runite mine" to Location.create(2376, 3835, 0), // Near the Neitiznot runite mine (requires Fremennik Trials) currently inaccessible as bridge does not work
            "Ardougne mining site" to Location.create(2600, 3232, 0), // Ardougne mining site (Monastery)
            "Ardougne eastern mine" to Location.create(2706, 3334, 0), // Ardougne mining site (Legends Guild)
            "Kandarin Coal trucks" to Location.create(2589, 3485, 0), // Kandarin Coal trucks
            "Yanille Bank" to Location.create(2603, 3087, 0), // Yanille Bank (Wizards' Guild)
            "Port Khazard mine" to Location.create(2626, 3140, 0), // Mining spot north-east of Yanille / Port Khazard mine
            "Al Kharid bank" to Location.create(3276, 3176, 0), // Al Kharid bank
            "Al Kharid mining site" to Location.create(3296, 3297, 0), // Al Kharid mine (Scorpion mine)
            "Duel Arena bank chest" to Location.create(3342, 3267, 0), // Duel Arena bank chest
            "Kharidian Desert clay mine" to Location.create(3426, 3159, 0), // Desert clay mine / Ruins of Uzer mine
            "Nardah mining site" to Location.create(3320, 2872, 0), // Kharidian Desert gold mine / Vulture mine
            "Nardah bank" to Location.create(3434, 2888, 0), // Nardah bank
            "Granite and sandstone quarry" to Location.create(3170, 2913, 0), // Granite and sandstone quarry / Quarry on map
            "South-east Varrock mine" to Location.create(3292, 3353, 0), // South-east Varrock mine
            "South-west Varrock mine" to Location.create(3176, 3362, 0), // South-west Varrock mine / Champion's Guild mine
            "Varrock east bank" to Location.create(3259, 3407, 0), // Varrock east bank / Rune shop
            "Lumbridge Swamp south-east mine" to Location.create(3227, 3150, 0), // Lumbridge Swamp south-east mine
            //"Burgh de Rott bank" to Location.create(3500, 3219, 0), // Burgh de Rott bank (requires Quest to enter)
            "Canifis Bank" to Location.create(3504, 3487, 0), // Canifis bank
            "Mos Le'Harmless bank" to Location.create(3687, 2969, 0), // Mos Le'Harmless bank (requires Quest to enter but is currently accessible for Slayer)
            "Gnome stronghold Bank" to Location.create(2460, 3432, 0), // Gnome stronghold bank
            "Lletya bank" to Location.create(2329, 3163, 0), // Lletya bank (requires Roving Elves?)
            "Piscatoris mining site" to Location.create(2336, 3636, 0), // Piscatoris mining site
            "North Edgeville mining site" to Location.create(3101, 3569, 0), // Wilderness Steel mine / Zamorak mage mine
            "Southern wilderness mine" to Location.create(3025, 3591, 0), // Wilderness skeleton mine
            "Wilderness Volcano bank" to Location.create(3188, 3695, 0), // Wilderness Volcano bank
            "Wilderness hobgoblin mine" to Location.create(3020, 3809, 0), // Wilderness hobgoblin mine
            "Pirates' Hideout mine" to Location.create(3059, 3940, 0), // Pirates' Hideout mine
            "Lava Maze mining site" to Location.create(3062, 3885, 0), // Wilderness rune mine
            "Mage Arena bank" to Location.create(3093, 3962, 0) // Mage Arena bank
    )
    val starSprite = NPC(8091)
    var location = "Canifis Bank"
    var maxDust = level.totalStardust
    var dustLeft = level.totalStardust
    var starObject = Scenery(level.objectId, crash_locations.get(location))
    var isDiscovered = false
    var ticks = 0
    var isSpawned = false
    var spriteSpawned = false
    var firstStar = true
    var selfBots = ArrayList<ShootingStarBot>()
    var activePlayers = HashSet<Player>()

    /**
     * Degrades a ShootingStar (or removes the starObject and spawns a Star Sprite if it's the last star)
     */
    fun degrade() {
        if(level.ordinal == 0){
            selfBots.filter { it.isMining() }.forEach { it.sleep() }
            SceneryBuilder.remove(starObject)
            isSpawned = false
            starSprite.location = starObject.location
            starSprite.init()
            spriteSpawned = true
            ShootingStarPlugin.getStoreFile().clear()
            return
        }
        level = getNextType()
        maxDust = level.totalStardust
        dustLeft = level.totalStardust

        ShootingStarPlugin.getStoreFile()["level"] = level.ordinal
        ShootingStarPlugin.getStoreFile()["isDiscovered"] = isDiscovered

        val newStar = Scenery(level.objectId, starObject.location)
        SceneryBuilder.replace(starObject, newStar)
        starObject = newStar
    }

    private fun getNextType(): ShootingStarType{
        return ShootingStarType.values()[level.ordinal - 1]
    }

    /**
     * Fires the shooting star (spawns a new one). Only used when spawning new shooting stars, not for downgrading existing ones.
     */
    fun fire() {
        SceneryBuilder.remove(starObject)
        rebuildVars()
        clearSprite()
        SceneryBuilder.add(starObject)
        if(!isSpawned) {
            (0..2).forEach {
                selfBots.add(ShootingStarBot.new())
            }
        }
        if (level.ordinal + 1 > 5) {
            selfBots.filter { it.isIdle() }.forEach { it.activate(true) }
        }
        isSpawned = true
        Repository.sendNews("A shooting star level ${level.ordinal + 1} just crashed near ${location.toLowerCase()}!")
    }

    /**
     * Rebuilds some of the variables with new information.
     */
    fun rebuildVars(){
        if(firstStar && ShootingStarPlugin.getStoreFile().isNotEmpty()){
            level = ShootingStarType.values()[ShootingStarPlugin.getStoreFile().getInt("level")]
            location = ShootingStarPlugin.getStoreFile().getString("location")
            isDiscovered = ShootingStarPlugin.getStoreFile().getBoolean("isDiscovered")
        } else {
            level = ShootingStarType.values().random()
            location = crash_locations.entries.random().key
            isDiscovered = false
        }

        maxDust = level.totalStardust
        dustLeft = level.totalStardust
        starObject = Scenery(level.objectId, crash_locations.get(location))

        ShootingStarPlugin.getStoreFile()["level"] = level.ordinal
        ShootingStarPlugin.getStoreFile()["location"] = location
        ShootingStarPlugin.getStoreFile()["isDiscovered"] = false

        ticks = 0
        firstStar = false
    }

    fun clearSprite() {
        starSprite.clear()
        spriteSpawned = false
    }

    /**
     * Decrement the amount of dust left in the current star and degrade it if the amount left is 0 or less.
     */
    fun decDust() {
        if(--dustLeft <= 0) degrade()
    }

    /**
     * Proxy method for starting to mine a shooting star
     */
    fun mine(player: Player) {
        player.pulseManager.run(ShootingStarMiningPulse(player, starObject, this))
    }

    /**
     * Prospecting shooting stars.
     */
    fun prospect(player: Player) {
        player.dialogueInterpreter.sendDialogue("This is a size " + (level.ordinal + 1) + " star. A Mining level of at least " + miningLevel + " is", "required to mine this layer. There is $dustLeft stardust remaining", "until the next layer.")
    }

    /**
     * Notifies the star when a player begins mining it
     */
    fun notifyNewPlayer(player: Player) {
        if(activePlayers.size < 3){
            val leavingBot = selfBots.firstOrNull { it.isMining() }
            leavingBot?.sleep()
        }
        activePlayers.add(player)
    }

    /**
     * Notifies the star when a player stops mining it
     */
    fun notifyPlayerLeave(player: Player) {
        activePlayers.remove(player)
        if (activePlayers.size < 3){
            val newBot = selfBots.firstOrNull() { it.isIdle() }
            newBot?.activate(true)
        }
    }

    /**
     * Gets the mining level required based on the star's current level ordinal.
     */
    val miningLevel: Int
        get() = (level.ordinal + 1) * 10
}

/**
 * Various levels of shooting stars
 * @author Ceikry
 */
enum class ShootingStarType(val objectId: Int, val exp: Int, val totalStardust: Int,val rate: Double) {
    LEVEL_1(38668, 14, 1200, 0.05),
    LEVEL_2(38667, 25, 700, 0.1),
    LEVEL_3(38666, 29, 439, 0.3),
    LEVEL_4(38665, 32, 250, 0.4),
    LEVEL_5(38664, 47, 175, 0.5),
    LEVEL_6(38663, 71, 80, 0.70),
    LEVEL_7(38662, 114, 40, 0.80),
    LEVEL_8(38661, 145, 25, 0.85),
    LEVEL_9(38660, 210, 15, 0.95);
}