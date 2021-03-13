package rs09.game.ai.general.scriptrepository

import core.game.interaction.DestinationFlag
import core.game.interaction.MovementPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.tools.RandomFunction
import org.rs09.consts.Items
import rs09.game.ai.AIRepository

/**
 * A bot that does various random slayer tasks throughout the game and sells the loot on the GE.
 * @author Ceikry
 */
class GenericSlayerBot : Script() {
    var state = State.GETTING_TASK
    var task = Task.CAVE_CRAWLER //default value to ensure non-nullability
    var assignment = Assignment("",0) //same as above
    var teleportFlag = false //have we teleported?
    val FOOD_ID = Items.LOBSTER_379
    val varrockBankBorders = ZoneBorders(3184, 3435,3187, 3441)



    /**
     * Per-tick actions (they don't run if the bot already has a pulse running, such as moving or combat.)
     */
    override fun tick() {
        scriptAPI.eat(FOOD_ID) //try to eat every tick (does nothing if health isn't low enough)

        when(state){

            /**
             * State that handles getting a new task
             */
            State.GETTING_TASK -> {
                populateTaskAndAssignment()
                state = State.GOING_TO_HUB //switch states
            }

            /**
             * State that handles going to the "hub" for the task. Such as fremmy slayer cave or slayer tower.
             */
            State.GOING_TO_HUB -> {
                when(task.hub){

                    TaskHub.FREMENNIK_CAVE -> {
                        if(!teleportFlag) {
                            scriptAPI.teleport(Location.create(2781, 3615, 0)) //tele -> fremennik fairy ring
                            teleportFlag = !teleportFlag //teleportFlag -> true
                        }
                        else {
                            val entrance = scriptAPI.getNearestNode(4499,true) //returns the object if within render distance, null if not.
                            if(entrance == null || !entrance.location.withinDistance(bot.location,2)) {
                                scriptAPI.walkTo(Location.create(2796, 3615, 0))
                            } else {
                                entrance.interaction.handle(bot,entrance.interaction[0]) //bot -> use interaction [0] of cave object
                                teleportFlag = !teleportFlag //teleportFlag -> false
                                state = State.KILLING_ENEMY //switch states
                            }
                        }
                    }

                }
            }


            /**
             * State used for combat. Handles moving the bot back into the zone for the monster and looting as well.
             */
            State.KILLING_ENEMY -> {
                val items = AIRepository.groundItems.get(bot) //retrieve drops that belong to this bot
                if(items != null && items.isNotEmpty()){ //if we have loot...
                    if(bot.inventory.freeSlots() == 0){
                        if(bot.inventory.contains(FOOD_ID,1)){
                            scriptAPI.forceEat(FOOD_ID)
                            return
                        }
                    }
                    scriptAPI.takeNearestGroundItem(items.get(0).id)
                    if(bot.inventory.getAmount(Item(FOOD_ID)) == 0){
                        state = State.GOING_TO_BANK
                    }
                    return
                }

                if(assignment.amount > 0) {
                    if (task.borders.insideBorder(bot)) { //make sure bot is within the borders of the task's zone.
                        scriptAPI.attackNpcInRadius(bot, task.npc_name, 20) //attack nearest NPC with given task name within radius.
                        assignment.amount--
                    } else {
                        scriptAPI.walkTo(task.borders.randomLoc)
                    }
                } else {
                    state = State.GOING_TO_BANK //switch states and bank.
                }
            }

            /**
             * State used to handle traveling to a bank. Due to the dynamic nature of where these bots might be
             * in this state, they simply teleport to Varrock and then walk to the west bank to bank.
             */
            State.GOING_TO_BANK -> {
                if(!teleportFlag){
                    scriptAPI.teleport(Location.create(3213, 3426, 0))
                    teleportFlag = !teleportFlag
                } else {
                    if(!varrockBankBorders.insideBorder(bot)) {
                        scriptAPI.walkTo(varrockBankBorders.randomLoc)
                    } else {
                        val bank = scriptAPI.getNearestNode("Bank Booth",true)
                        bank ?: return
                        /**
                         * Run a pulse that moves the bot to the bank booth and then faces it, and then swaps to the actual banking state
                         */
                        bot.pulseManager.run(object: MovementPulse(bot,bank, DestinationFlag.OBJECT){
                            override fun pulse(): Boolean {
                                bot.faceLocation(bank.location)
                                state = State.BANKING
                                teleportFlag = !teleportFlag
                                return true
                            }
                        })
                    }
                }
            }

            /**
             * State used to handle banking itself. Runs a pulse with a delay of 10 (to make it look like the bot is idling at the bank booth a bit.)
             * the pulse adds every item in the bot's inventory to its bank and then clears the bot's inventory. Re-adds lobsters to the inventory afterwords.
             */
            State.BANKING -> {
                bot.pulseManager.run(object: Pulse(10){
                    override fun pulse(): Boolean {
                        for(item in bot.inventory.toArray()){
                            item ?: continue
                            if(item.checkIgnored()) continue
                            bot.bank.add(item)
                        }
                        bot.inventory.clear()
                        for(item in inventory)
                            bot.inventory.add(item)
                        scriptAPI.withdraw(org.rs09.consts.Items.LOBSTER_379,10)
                        bot.fullRestore()

                        if(assignment.amount <= 0){
                            state = State.GOING_TO_GE
                        } else {
                            state = State.GOING_TO_HUB
                        }

                        return true
                    }
                })
            }


            /**
             * State used to handle travelling to the GE.
             */
            State.GOING_TO_GE -> {
                if(bot.location != Location.create(3165, 3487, 0)) {
                    scriptAPI.walkTo(Location.create(3165, 3487, 0))
                } else {
                    state = State.SELLING
                }
            }

            /**
             * State used to handle selling items on the GE
             */
            State.SELLING -> {
                scriptAPI.sellAllOnGe()
                state = State.GETTING_TASK
            }

        }
    }


    /**
     * Called when a bot needs to be regenerated, like when a bot dies.
     */
    override fun newInstance(): Script {
        TODO("Not yet implemented")
    }

    /**
     * Populate task and assignment vars
     */
    fun populateTaskAndAssignment() {
        task = Task.values().random() //get a new random task from the enum
        assignment = Assignment(task.npc_name, RandomFunction.random(task.minAmt, task.maxAmt + 1)) //produce a new Assignment class with values from our Task.
    }


    /**
     * Item extension function to check for ignored items when banking.
     */
    fun Item.checkIgnored(): Boolean {
        if(name.toLowerCase().contains("charm")) return true
        if(name.toLowerCase().contains("lobster")) return true
        if(name.toLowerCase().contains("clue")) return true
        if(!definition.isTradeable) return true
        return when(id){
            Items.BONES_2530 -> true
            995 -> true
            else -> false
        }
    }

    /**
     * The state enums
     */
    enum class State {
        GETTING_TASK,
        GOING_TO_HUB,
        KILLING_ENEMY,
        LOOTING,
        GOING_TO_BANK,
        BANKING,
        GOING_TO_GE,
        SELLING
    }

    /**
     * Assign default skills and initial inventory
     */
    init {
        skills[Skills.SLAYER] = 99
        inventory.add(Item(Items.LOBSTER_379,10))

    }

    /**
     * List of tasks that this bot can receive
     */
    enum class Task(val npc_name: String, val minAmt: Int, val maxAmt: Int, val hub: TaskHub, val borders: ZoneBorders) {
        CAVE_CRAWLER("Cave crawler",20,100,TaskHub.FREMENNIK_CAVE, ZoneBorders(2778, 9988,2798, 10002))
    }

    /**
     * The task hubs
     */
    enum class TaskHub {
        FREMENNIK_CAVE,
        SLAYER_TOWER,
        TAVERLY_DUNGEON
    }

    class Assignment(val npc_name: String, var amount: Int) //class used to keep track of current assignment and how many are left.
}