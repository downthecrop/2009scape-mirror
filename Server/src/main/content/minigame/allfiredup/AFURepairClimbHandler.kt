package content.minigame.allfiredup

import core.game.node.entity.impl.ForceMovement
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import content.global.skill.construction.NailType
import core.game.node.item.Item
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Items
import core.game.interaction.InteractionListener
import core.game.interaction.IntType
import core.api.*
import java.util.*

/**
 * Handles repairing and climbing of the 3 beacon shortcuts needed to access them
 * @author Ceikry
 */
class AFURepairClimbHandler : InteractionListener {

    val repairIDs = intArrayOf(38480,38470,38494)
    val climbIDs = intArrayOf(38469,38471,38486,38481)

    override fun defineListeners() {

        on(repairIDs, IntType.SCENERY, "repair"){ player, _ ->
            if (hasRequirement(player, "All Fired Up")){
                val rco: RepairClimbObject? = getClimbingObject(player)
                repair(player,rco!!)
                return@on true
            }
            return@on false
        }

        on(climbIDs, IntType.SCENERY, "climb"){ player, node ->
            val rco: RepairClimbObject? = getClimbingObject(player)
            climb(player,rco!!,node.location)
            return@on true
        }

    }

    private fun getClimbingObject(player: Player): RepairClimbObject?{
        for(ent in RepairClimbObject.values())
            if(ent.destinationDown?.withinDistance(player.location,2) == true ||
                ent.destinationUp?.withinDistance(player.location,2) == true){
                return ent
            }
        return null
    }

    private fun repair(player: Player,rco: RepairClimbObject){
        if (rco == RepairClimbObject.TEMPLE){
            // You can do this 2 different ways
            val hasSmithingLevel = getDynLevel(player, Skills.SMITHING) >= 70
            val hasConstructionLevel = getDynLevel(player, Skills.CONSTRUCTION) >= 59

            if (!hasConstructionLevel && !hasSmithingLevel){
                sendDialogue(player, "You need level 70 smithing or 59 construction for this.")
                return
            }

            val hasHammer = inInventory(player, Items.HAMMER_2347)
            val hasSmithingItems = hasHammer && inInventory(player, Items.IRON_BAR_2351, 2)
            val hasConstructionItems = hasHammer && inInventory(player, Items.PLANK_960, 2)

            if (hasSmithingLevel && hasSmithingItems){
                if (removeItem(player,Item(Items.IRON_BAR_2351, 2))) {
                    setVarbit(player, rco.varbit, 1, true)
                    return
                }
            }
            // Only check this if the smithing repair didn't work
            if (hasConstructionLevel && hasConstructionItems){
                val nails = NailType.get(player, 4)
                if (nails != null){
                    if (removeItem(player, Item(Items.PLANK_960, 2)) && removeItem(player, Item(nails.itemId, 4))) {
                        setVarbit(player, rco.varbit, 1, true)
                        return
                    }
                }
            }

            var msg = "You need "
            msg += if (hasSmithingLevel) "a hammer and 2 iron bars" else ""
            msg += if (hasSmithingLevel && hasConstructionLevel) " or " else ""
            msg += if (hasConstructionLevel) "a hammer, 2 planks and 4 nails for this." else " for this."
            sendDialogue(player, msg)
            return
        }
        val skill = rco.levelRequirement?.first ?: 0
        val level = rco.levelRequirement?.second ?: 0
        if(player.skills.getLevel(skill) < level){
            player.dialogueInterpreter.sendDialogue("You need level $level ${Skills.SKILL_NAME[skill]} for this.")
        }

        var requiresNeedle = false

        val requiredItems = when(rco){
            RepairClimbObject.DEATH_PLATEAU -> {
                arrayOf(Item(Items.PLANK_960,2))
            }

            RepairClimbObject.BURTHORPE -> {
                arrayOf(Item(Items.IRON_BAR_2351,2))
            }

            RepairClimbObject.GWD -> {
                requiresNeedle = true
                arrayOf(Item(Items.JUTE_FIBRE_5931,3))
            }
            else -> return
        }

        if(requiresNeedle){
            if(player.inventory.containsItem(Item(Items.NEEDLE_1733)) && player.inventory.containItems(*requiredItems.map { it.id }.toIntArray())) {
                player.inventory.remove(*requiredItems)
                if (Random().nextBoolean()) player.inventory.remove(Item(Items.NEEDLE_1733))
            } else {
                player.dialogueInterpreter.sendDialogue("You need a needle and ${requiredItems.map { "${it.amount} ${it.name.lowercase()}s" }.toString().replace("[","").replace("]","")} for this.")
                return
            }
        } else {
            if(player.inventory.containsItem(Item(Items.HAMMER_2347)) && player.inventory.containItems(*requiredItems.map { it.id }.toIntArray())) {
                val nails = NailType.get(player,4)
                if(nails == null && rco == RepairClimbObject.DEATH_PLATEAU){
                    player.dialogueInterpreter.sendDialogue("You need 4 nails for this.")
                    return
                } else if (rco == RepairClimbObject.DEATH_PLATEAU){
                    player.inventory.remove(Item(nails.itemId,4))
                }
                player.inventory.remove(*requiredItems)
            } else {
                player.dialogueInterpreter.sendDialogue("You need a hammer and ${requiredItems.map { "${it.amount} ${it.name.lowercase()}s" }.toString().replace("[","").replace("]","")} for this.")
                return
            }
        }
        setVarbit(player, rco.varbit, 1, true)
    }

    private fun climb(player: Player, rco: RepairClimbObject, location: Location){
        ForceMovement.run(player,location,rco.getOtherLocation(player),rco.getAnimation(player),rco.getAnimation(player),rco.getDirection(player),20).endAnimation = Animation(-1)
    }

    private enum class RepairClimbObject(val varbit: Int, val destinationUp: Location?, val destinationDown: Location?, val levelRequirement: Pair<Int,Int>?){
        DEATH_PLATEAU(5161,Location.create(2949, 3623, 0),Location.create(2954, 3623, 0), Pair(Skills.CONSTRUCTION,42)),
        BURTHORPE(5160,Location.create(2941, 3563, 0),Location.create(2934, 3563, 0),Pair(Skills.SMITHING,56)),
        GWD(5163,null,null,Pair(Skills.CRAFTING,60)),
        TEMPLE(5164,Location.create(2949, 3835, 0),Location.create(2956, 3835, 0),Pair(0,0)); // This needs to be handled specially so don't have levels here

        fun getOtherLocation(player: Player): Location?{
            if(player.location == destinationDown) return destinationUp
            else return destinationDown
        }

        fun getAnimation(player: Player): Animation {
            if(getOtherLocation(player) == destinationDown) return Animation(1148)
            else return Animation(740)
        }

        fun getDirection(player: Player): Direction {
            if(this == BURTHORPE){
                return Direction.EAST
            } else return Direction.WEST
        }

        fun isRepaired(player: Player): Boolean{
            return getVarbit(player, varbit) == 1
        }
    }

    companion object {
        fun isRepaired(player: Player, beacon: AFUBeacon): Boolean{
            if(beacon == AFUBeacon.DEATH_PLATEAU) return RepairClimbObject.DEATH_PLATEAU.isRepaired(player)
            if(beacon == AFUBeacon.BURTHORPE) return RepairClimbObject.BURTHORPE.isRepaired(player)
            if(beacon == AFUBeacon.GWD) return RepairClimbObject.GWD.isRepaired(player)
            if(beacon == AFUBeacon.TEMPLE) return RepairClimbObject.TEMPLE.isRepaired(player)
            else return true
        }
    }

}
