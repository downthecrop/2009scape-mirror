package rs09.game.node.entity.skill.hunter.tracking

import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.scenery.Scenery
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import org.rs09.consts.Items
import rs09.game.system.SystemLogger
import rs09.game.world.GameWorld
import java.util.*

/**
 * Main class for hunter tracking. All the other subclasses just define values that the methods in this class use.
 * @author Ceikry
 */
abstract class HunterTracking : OptionHandler(){
    var KEBBIT_ANIM = Animation(0)
    val MISS_ANIM = Animation(5255)
    var trailLimit = 0
    var attribute = ""
    var indexAttribute = ""
    var rewards = Array<Item>(0){Item(0)}
    var tunnelEntrances = Array<Location>(0){ Location(0,0,0) }
    var initialMap = HashMap<Int,ArrayList<TrailDefinition>>()
    var linkingTrails = ArrayList<TrailDefinition>()
    var experience = 0.0
    var varp = 0
    var requiredLevel = 1

    /**
     * Gets the initial trail based on the ID of the hole object the player clicked on
     * @author Ceikry
     */
    fun getInitialTrail(obj: Scenery): TrailDefinition? {
        return initialMap[obj.id]?.random()
    }

    /**
     * Generates the entire trail all at once.
     * @author Ceikry
     */
    fun generateTrail(startobj: Scenery, player: Player) {
        val trail = player.getAttribute(attribute, ArrayList<TrailDefinition>())
        val initialTrail = getInitialTrail(startobj)
        if(initialTrail == null) {
            SystemLogger.logWarn("UNHANDLED STARTING OBJECT FOR HUNTER TRACKING $startobj")
            return
        }
        trail.add(initialTrail)
        player.setAttribute(attribute,trail)

        var numSpots = RandomFunction.random(2,trailLimit)
        var triesRemaining = numSpots * 3

        while(numSpots > 0){
            if(triesRemaining-- <= 0) {
                clearTrail(player)
                return
            }
            val nextTrail = getLinkingTrail(player)
            nextTrail ?: continue
            var offsetUsed = false
            for(i in trail){
                if(i.offset == nextTrail.offset){ offsetUsed = true; break }
            }
            if(offsetUsed) continue
            if(nextTrail.type == TrailType.TUNNEL){
                trail.add(nextTrail)
                continue
            }
            trail.add(nextTrail)
            player.setAttribute(attribute,trail)
            numSpots--
        }
    }

    /**
     * Gets a linking trail point, used to build entire trails
     * @author Ceikry
     */
    fun getLinkingTrail(player: Player): TrailDefinition? {
        val trail = player.getAttribute(attribute,ArrayList<TrailDefinition>())
        val previousTrail = trail.get(trail.lastIndex)
        if(previousTrail.type == TrailType.TUNNEL){
            val possibleTrails = ArrayList<TrailDefinition>()
            for(trail in linkingTrails){
                val invTrail = getTrailInverse(trail,false)
                if(invTrail.type == TrailType.TUNNEL && previousTrail.endLocation.withinDistance(invTrail.startLocation,5) && !previousTrail.endLocation.equals(invTrail.startLocation) && previousTrail.offset != trail.offset){
                    possibleTrails.add(trail)
                }
            }
            return possibleTrails.random()
        }
        val possibleTrails = ArrayList<TrailDefinition>()
        for(trail in linkingTrails){
            if(trail.startLocation.equals(previousTrail.endLocation) && previousTrail.offset != trail.offset){
                possibleTrails.add(trail)
            }
        }
        return possibleTrails.random()
    }


    /**
     * Inverts a trail so trails only have to be defined manually in one direction
     * @author Ceikry
     */
    fun getTrailInverse(trail: TrailDefinition, swapLocations: Boolean): TrailDefinition{
        if(swapLocations)
            return TrailDefinition(trail.offset,if(tunnelEntrances.contains(trail.startLocation)) TrailType.TUNNEL else TrailType.LINKING,!trail.inverted,trail.endLocation,trail.startLocation, trail.triggerObjectLocation)
        return TrailDefinition(trail.offset, if(tunnelEntrances.contains(trail.startLocation)) TrailType.TUNNEL else TrailType.LINKING, !trail.inverted, trail.startLocation, trail.endLocation)
    }

    /**
     * Populates the linked trail list with inverses of the manually defined trails
     * @author Ceikry
     */
    fun addExtraTrails(){
        linkingTrails.toTypedArray().forEach { trail ->
            linkingTrails.add(getTrailInverse(trail,true))
        }
        if(this is PolarKebbitHunting){
            initialMap.values.forEach {
                linkingTrails.addAll(it)
                it.forEach { trail ->
                    linkingTrails.add(getTrailInverse(trail, true))
                }
            }
        }
    }

    /**
     * Resets a player's trail
     * @author Ceikry
     */
    fun clearTrail(player: Player){
        player.removeAttribute(attribute)
        player.removeAttribute(indexAttribute)
        player.varpManager.get(varp).clear()
        player.varpManager.get(varp).send(player)
    }

    /**
     * Check if a player has an active trail
     * @author Ceikry
     */
    fun hasTrail(player: Player): Boolean{
        return player.getAttribute(attribute,null) != null
    }

    /**
     * Rewards the player with items from the rewards array and awards experience.
     * Pass false to success to just play the kebbit animation without rewarding anything.
     * @author Ceikry
     */
    fun reward(player: Player, success: Boolean) {
        player.lock()
        player.animator.animate(if(success) KEBBIT_ANIM else MISS_ANIM)
        GameWorld.Pulser.submit(object : Pulse(KEBBIT_ANIM.duration){
            override fun pulse(): Boolean {
                if(hasTrail(player) && success){
                    for(item in rewards){
                        if(!player.inventory.add(item))
                            GroundItemManager.create(item,player)
                    }
                    player.skills.addExperience(Skills.HUNTER,experience)
                    clearTrail(player)
                }
                player.unlock()
                return true
            }
        })
    }

    /**
     * Updates the trail varp based on the current trail index
     * @author Ceikry
     */
    fun updateTrail(player: Player){
        player ?: return
        val trail = player.getAttribute(attribute,ArrayList<TrailDefinition>())
        val trailIndex = player.getAttribute(indexAttribute,0)
        for(index in 0..trailIndex){
            val trl = trail[index]
            player.varpManager.get(varp).setVarbit(trl.offset + 2,1)
            player.varpManager.get(varp).setVarbit(trl.offset,if(trl.inverted) 1 else 0)
        }
        player.varpManager.get(varp).send(player)
    }

    /**
     * Handles all trail interactions
     * @author Ceikry
     */
    override fun handle(player: Player?, node: Node?, option: String?): Boolean {
        node ?: return true
        player ?: return true
        val trail = player.getAttribute(attribute,ArrayList<TrailDefinition>())
        val currentIndex = player.getAttribute(indexAttribute,0)
        if(!hasTrail(player) && !initialMap.containsKey(node.id)){
            player.dialogueInterpreter.sendDialogue("You search but find nothing.")
            return true
        }
        val currentTrail = if(hasTrail(player)) {
            if (currentIndex < trail.lastIndex) {
                trail.get(currentIndex + 1)
            } else {
                trail.get(currentIndex)
            }
        } else {
            TrailDefinition(0,TrailType.LINKING,false,Location(0,0,0),Location(0,0,0),Location(0,0,0))
        }
        when(option){

            "attack" -> {
                if(!hasNooseWand(player)){
                    player.dialogueInterpreter.sendDialogue("You need a noose wand to catch the kebbit.")
                    return true
                }
                if(currentIndex == trail.lastIndex && currentTrail.endLocation.equals(node.location)){
                    reward(player,true)
                } else {
                    reward(player,false)
                }
            }

            "inspect","search" -> {
                if(!hasTrail(player)){
                    if(player.skills.getLevel(Skills.HUNTER) < requiredLevel){
                        player.dialogueInterpreter.sendDialogue("You need a hunter level of $requiredLevel to track these.")
                        return true
                    }
                    generateTrail(node.asScenery(),player)
                    updateTrail(player)
                } else {
                    if(currentTrail.triggerObjectLocation.equals(node.location) || (currentIndex == trail.lastIndex && currentTrail.endLocation.equals(node.location))){
                        if(currentIndex == trail.lastIndex){
                            player.dialogueInterpreter.sendDialogue("It looks like something is moving around in there.")
                        } else {
                            player.dialogueInterpreter.sendDialogue("You discover some tracks nearby.")
                            player.incrementAttribute(indexAttribute)
                            updateTrail(player)
                        }
                    } else {
                        player.dialogueInterpreter.sendDialogue("You search but find nothing of interest.")
                    }
                }
            }


        }
        return true
    }

    fun hasNooseWand(player: Player) : Boolean{
        return player.equipment.contains(Items.NOOSE_WAND_10150,1) || player.inventory.contains(Items.NOOSE_WAND_10150,1)
    }
}