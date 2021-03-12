package core.game.content.global.worldevents.shootingstar

import core.game.node.`object`.GameObject
import core.game.node.`object`.ObjectBuilder
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.repository.Repository

/**
 * Represents a shooting star object (Only ever initialized once) (ideally)
 * @author Ceikry
 */
class ShootingStar(var level: ShootingStarType = ShootingStarType.values().random()){
    val crash_locations = hashMapOf(
            "Canifis Bank" to Location.create(3504, 3487, 0),
            "Crafting Guild" to Location.create(2940, 3280, 0),
            "Falador East Bank" to Location.create(3030, 3349, 0),
            "Rimmington mining site" to Location.create(2975, 3237, 0),
            "Rimmington mine" to Location.create(2975, 3240, 0),
            "Karamja mining site" to Location.create(2737, 3223, 0),
            "Brimhaven mining site" to Location.create(2743, 3143, 0),
            "South Crandor mining site" to Location.create(2822, 3239, 0),
            "Karamja mining site" to Location.create(2854, 3032, 0),
            "Shilo Village mining site" to Location.create(2826, 2997, 0),
            "Relleka mining site" to Location.create(2682, 3700, 0),
            "Ardougne mining site" to Location.create(2600, 3232, 0),
            "Yanille Bank" to Location.create(2603, 3087, 0),
            "Al Kharid bank" to Location.create(3276, 3176, 0),
            "Al Kharid mining site" to Location.create(3296, 3297, 0),
            "Duel Arena bank chest" to Location.create(3342, 3267, 0),
            "Nardah mining site" to Location.create(3320, 2872, 0),
            "Nardah bank" to Location.create(3434, 2888, 0),
            "South-east Varrock mine" to Location.create(3292, 3353, 0),
            "South-west Varrock mine" to Location.create(3176, 3362, 0),
            "Varrock east bank" to Location.create(3259, 3407, 0),
            "Lumbridge Swamp mine" to Location.create(3227, 3150, 0),
            "Gnome stronghold Bank" to Location.create(2460, 3432, 0),
            "North Edgeville mining site" to Location.create(3101, 3569, 0),
            "Southern wilderness mine" to Location.create(3025, 3591, 0),
            "Pirates' Hideout mine" to Location.create(3059, 3940, 0),
            "Lava Maze mining site" to Location.create(3062, 3885, 0),
            "Mage Arena bank" to Location.create(3093, 3962, 0)
    )
    val starSprite = NPC(8091)
    var location = "Canifis Bank"
    var maxDust = level.totalStardust
    var dustLeft = level.totalStardust
    var starObject = GameObject(level.objectId, crash_locations.get(location))
    var isDiscovered = false
    var ticks = 0
    var isSpawned = false
    var spriteSpawned = false

    /**
     * Degrades a ShootingStar (or removes the starObject and spawns a Star Sprite if it's the last star)
     */
    fun degrade() {
        if(level.ordinal == 0){
            ObjectBuilder.remove(starObject)
            isSpawned = false
            starSprite.location = starObject.location
            starSprite.init()
            spriteSpawned = true
            return
        }
        level = getNextType()
        maxDust = level.totalStardust
        dustLeft = level.totalStardust

        val newStar = GameObject(level.objectId, starObject.location)
        ObjectBuilder.replace(starObject, newStar)
        starObject = newStar
    }

    private fun getNextType(): ShootingStarType{
        return ShootingStarType.values()[level.ordinal - 1]
    }

    /**
     * Fires the shooting star (spawns a new one). Only used when spawning new shooting stars, not for downgrading existing ones.
     */
    fun fire() {
        ObjectBuilder.remove(starObject)
        rebuildVars()
        clearSprite()
        ObjectBuilder.add(starObject)
        isSpawned = true
        Repository.sendNews("A shooting star level ${level.ordinal + 1} just crashed near ${location.toLowerCase()}!")
    }

    /**
     * Rebuilds some of the variables with new information.
     */
    fun rebuildVars(){
        level = ShootingStarType.values().random()
        location = crash_locations.entries.random().key
        maxDust = level.totalStardust
        dustLeft = level.totalStardust
        starObject = GameObject(level.objectId, crash_locations.get(location))
        isDiscovered = false
        ticks = 0
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