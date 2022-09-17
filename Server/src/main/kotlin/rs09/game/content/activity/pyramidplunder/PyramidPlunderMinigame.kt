package rs09.game.content.activity.pyramidplunder

import api.*
import core.game.content.activity.pyramidplunder.PyramidPlunderMummyNPC
import core.game.content.activity.pyramidplunder.PyramidPlunderSwarmNPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.entity.state.EntityState
import core.game.system.task.Pulse
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery
import rs09.game.content.dialogue.DialogueFile
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.IntType


/**
 * The "controller" class for pyramid plunder. Handles per-tick updates, logout hooks, and defines interaction listeners for the minigame.
 * @author Ceikry
 */
class PyramidPlunderMinigame : InteractionListener, TickListener, LogoutListener {
    override fun tick() {
        val playersToExpel = PlunderUtils.decrementTimeRemaining()
        playersToExpel.forEach {player -> PlunderUtils.expel(player, false) }
        PlunderUtils.checkEntranceSwitch()
    }

    override fun logout(player: Player) {
        if(PlunderUtils.hasPlayer(player))
        {
            player.location = Location.create(3288, 2802, 0)
            PlunderUtils.unregisterPlayer(player)
        }
    }

    override fun defineListeners() {
        val CHECK_ANIM = 3572
        val URN_CHECK = 4340
        val URN_BIT = 4341
        val URN_SUCCESS = 4342
        val PUSH_LID_START = 4343
        val PUSH_LID_LOOP = 4344
        val PUSH_LID_FINISH = 4345
        val CHARM_ANIM = 1877
        val OPEN_CHEST_ANIM = 536

        val SPEAR_OBJ_ANIM = 459
        val SNAKE_URN_ANIM = 4335

        val SEALED_URNS = intArrayOf(Scenery.URN_16501, Scenery.URN_16502, Scenery.URN_16503)
        val OPEN_URNS = intArrayOf(Scenery.URN_16505, Scenery.URN_16506, Scenery.URN_16507)
        val SNAKE_URNS = intArrayOf(Scenery.URN_16509, Scenery.URN_16510, Scenery.URN_16511)
        val CHARMED_SNAKES = intArrayOf(Scenery.URN_16513, Scenery.URN_16514, Scenery.URN_16515)
        val ENTRANCES = intArrayOf(16484, 16487, 16490, 16493) //All Scenery.AN_ANONYMOUS_LOOKING_DOOR_16484/etc
        val SARCOPHAGUS = Scenery.SARCOPHAGUS_16495

        on(Scenery.SPEARTRAP_16517, IntType.SCENERY, "pass"){player, node ->
            val anim = Animation(CHECK_ANIM)
            val duration = animationDuration(anim)

            if(player.skills.getStaticLevel(Skills.THIEVING) < PlunderUtils.getRoomLevel(player))
            {
                sendDialogue(player, "You need a Thieving level of ${PlunderUtils.getRoomLevel(player)} to do that.")
                return@on true
            }

            val spearDir = PlunderUtils.getRoom(player)!!.spearDirection

            val pastSpears = (spearDir == Direction.NORTH && player.location.y > node.location.y)
                          || (spearDir == Direction.SOUTH && player.location.y < node.location.y)
                          || (spearDir == Direction.EAST && player.location.x > node.location.x)
                          || (spearDir == Direction.WEST && player.location.x < node.location.x)

            if(pastSpears)
            {
                sendDialogue(player, "I have no reason to do that.")
                return@on true
            }

            animate(player, anim)
            sendMessage(player, "You carefully try to temporarily disable the trap.")
            player.pulseManager.run(object : Pulse(duration) {
                override fun pulse(): Boolean {
                    if(RandomFunction.roll(5))
                    {
                        val dest = PlunderUtils.getSpearDestination(player)
                        player.walkingQueue.reset()
                        player.walkingQueue.addPath(dest.x,dest.y)
                        rewardXP(player, Skills.THIEVING, 10.0)
                        return true
                    }
                    if(RandomFunction.roll(20))
                    {
                        animateScenery(node.asScenery(), SPEAR_OBJ_ANIM)
                        impact(player, RandomFunction.random(1,5))
                        sendChat(player, "Ouch!")
                        sendMessage(player, "You fail to disable the trap.")
                        return true
                    }
                    animate(player, anim)
                    return false
                }
            })
            return@on true
        }

        on(intArrayOf(*SEALED_URNS, *SNAKE_URNS), IntType.SCENERY, "search") { player, node ->
            animate(player, URN_CHECK)
            player.faceLocation(node.location)
            lock(player, 1)
            runTask(player, 1) {
                if(!PlunderUtils.rollUrnSuccess(player))
                {
                    animate(player, URN_BIT)
                    sendMessage(player, "You've been bitten by something moving around in the urn.")
                    impact(player, RandomFunction.random(1,5))
                    player.stateManager.register(EntityState.POISONED, true, 20, player)
                }
                else {
                    animate(player, URN_SUCCESS)
                    sendMessage(player, "You successfully loot the urn.")
                    rewardXP(player, Skills.THIEVING, PlunderUtils.getUrnXp(player, false))
                    addItemOrDrop(player, PlunderUtils.rollArtifact(player, 1))
                    player.varpManager.setVarbit(node.asScenery().definition.varbitID, 1)
                }
            }
            return@on true
        }

        on(SEALED_URNS, IntType.SCENERY, "check for snakes") { player, node ->
            val urn = node.asScenery()
            animate(player, URN_CHECK)
            player.faceLocation(node.location)
            lock(player, 1)
            runTask(player, 1){
                animate(player, URN_BIT)
                player.varpManager.setVarbit(urn.definition.varbitID, 2)
                animateScenery(urn, SNAKE_URN_ANIM)
            }
            return@on true
        }

        on(SNAKE_URNS, IntType.SCENERY, "charm snake") { player, node ->
            if(!inInventory(player, Items.SNAKE_CHARM_4605))
            {
                sendMessage(player, "You need a snake charm to charm a snake.")
                return@on true
            }
            lock(player, 2)
            animate(player, CHARM_ANIM)
            runTask(player, 1)
            {
                player.varpManager.setVarbit(node.asScenery().definition.varbitID, 3)
                sendMessage(player, "You charm the snake with your music.")
            }
            return@on true
        }

        on(CHARMED_SNAKES, IntType.SCENERY, "search") { player, node ->
            animate(player, URN_CHECK)
            player.faceLocation(node.location)
            lock(player, 1)
            runTask(player, 1) {
                if(!PlunderUtils.rollUrnSuccess(player, true))
                {
                    animate(player, URN_BIT)
                    sendMessage(player, "You've been bitten by something moving around in the urn.")
                    impact(player, RandomFunction.random(1,5))
                    player.stateManager.register(EntityState.POISONED, true, 20, player)
                }
                else {
                    animate(player, URN_SUCCESS)
                    sendMessage(player, "You successfully loot the urn.")
                    addItemOrDrop(player, PlunderUtils.rollArtifact(player, 1))
                    rewardXP(player, Skills.THIEVING, PlunderUtils.getUrnXp(player, false) * 0.66)
                    player.varpManager.setVarbit(node.asScenery().definition.varbitID, 1)
                }
            }
            return@on true
        }

        on(OPEN_URNS, IntType.SCENERY, "search") { player, _ ->
            sendMessage(player, "You've already looted this urn.")
            return@on true
        }

        on(SARCOPHAGUS, IntType.SCENERY, "open") { player, node ->
            sendMessage(player, "You attempt to push open the massive lid.")
            val strength = getDynLevel(player, Skills.STRENGTH)
            animate(player, PUSH_LID_START)
            player.pulseManager.run(object : Pulse(2)
            {
                override fun pulse(): Boolean {
                    animate(player, PUSH_LID_LOOP)
                    if(RandomFunction.random(125) > strength)
                        return false
                    animate(player, PUSH_LID_FINISH)
                    runTask(player, 3){
                        player.varpManager.setVarbit(node.asScenery().definition.varbitID, 1)
                        rewardXP(player, Skills.STRENGTH, PlunderUtils.getSarcophagusXp(player))
                        if(RandomFunction.roll(25))
                        {
                            val mummy = PyramidPlunderMummyNPC(player.location, player)
                            mummy.isRespawn = false
                            mummy.init()
                            mummy.attack(player)
                        }
                        else if(!PlunderUtils.rollSceptre(player))
                        {
                            addItemOrDrop(player, PlunderUtils.rollArtifact(player, 2))
                        }
                    }
                    return true
                }
            })
            return@on true
        }

        on(Scenery.GRAND_GOLD_CHEST_16473, IntType.SCENERY, "search") { player, node ->
            animate(player, OPEN_CHEST_ANIM)
            runTask(player){
                if(RandomFunction.roll(25))
                {
                    val swarm = PyramidPlunderSwarmNPC(player.location, player)
                    swarm.isRespawn = false
                    swarm.init()
                    swarm.attack(player)
                    impact(player, RandomFunction.random(1,5))
                }
                else
                {
                    rewardXP(player, Skills.THIEVING, PlunderUtils.getChestXp(player))
                    if(!PlunderUtils.rollSceptre(player))
                        addItemOrDrop(player, PlunderUtils.rollArtifact(player, 3))
                }
                player.varpManager.setVarbit(node.asScenery().definition.varbitID, 1)
            }
            return@on true
        }

        on(Scenery.TOMB_DOOR_16475, IntType.SCENERY, "pick-lock") { player, node ->
            val anim = Animation(CHECK_ANIM)
            val duration = animationDuration(anim)

            if(PlunderUtils.getRoom(player)!!.room == 8)
            {
                sendMessage(player, "This is the final room. I should probably just leave instead.")
                return@on true
            }

            animate(player, anim)
            lock(player, duration)
            val rate =
                if(inInventory(player, Items.LOCKPICK_1523))
                {
                    sendMessage(player, "You attempt to open the door.")
                    2
                }
                else
                {
                    sendMessage(player, "You attempt to open the door. Lockpicks would make it easier...")
                    3
                }
            runTask(player, duration){
                if(RandomFunction.roll(rate))
                {
                    val varbitId = node.asScenery().definition.varbitID
                    rewardXP(player, Skills.THIEVING, PlunderUtils.getDoorXp(player, rate == 3))
                    if(PlunderUtils.getDoor(player) == varbitId) {
                        PlunderUtils.loadNextRoom(player)
                        PlunderUtils.resetObjectVarbits(player)
                    } else {
                        sendMessage(player, "The door leads to a dead end.")
                        player.varpManager.setVarbit(varbitId, 1)
                    }
                }
                else
                {
                    sendMessage(player, "Your attempt fails.")
                }
            }
            return@on true
        }

        on(Scenery.TOMB_DOOR_16458, IntType.SCENERY, "leave tomb") { player, _ ->
            openDialogue(player, object : DialogueFile()
            {
                override fun handle(componentID: Int, buttonID: Int) {
                    when(stage)
                    {
                        0 -> options("Yes, I'm sure I want to leave.", "No, never mind.").also { stage++ }
                        1 -> when(buttonID)
                        {
                            1 -> {
                                end()
                                teleport(player, Location.create(3288, 2802, 0))
                                PlunderUtils.unregisterPlayer(player)
                                PlunderUtils.resetOverlay(player)
                                PlunderUtils.resetObjectVarbits(player)
                            }
                            2 -> end()
                        }
                    }
                }
            })
            return@on true
        }

        on(ENTRANCES, IntType.SCENERY, "search") { player, door ->
            if(!getAttribute(player, "tarik-spoken-to", false))
            {
                sendDialogue(player, "I should probably try to find out more about this place before I try to break in.")
                return@on true
            }
            val anim = Animation(CHECK_ANIM)
            val duration = animationDuration(anim)

            animate(player, anim)
            sendMessage(player, "You use your thieving skills to search the stone panel.")
            player.pulseManager.run(object : Pulse(duration){
                override fun pulse(): Boolean {
                    if(RandomFunction.roll(3))
                    {
                        setAttribute(player, "pyramid-entrance", player.location.transform(0,0,0))
                        sendMessage(player, "You find a door! You open it.")
                        if(PlunderUtils.checkEntrance(door)) {
                            teleport(player, GUARDIAN_ROOM)
                            rewardXP(player, Skills.THIEVING, 20.0)
                        }
                        else
                            teleport(player, EMPTY_ROOM)

                        return true
                    }
                    animate(player, anim)
                    return false
                }
            })
            return@on true
        }

        on(Scenery.TOMB_DOOR_16459, IntType.SCENERY, "leave tomb") { player, _ ->
            teleport(player, getAttribute(player, "pyramid-entrance", Location.create(3288, 2802, 0)))
            return@on true
        }

        on(NPCs.GUARDIAN_MUMMY_4476, IntType.NPC, "start-minigame") { player, _ ->
            if(!getAttribute(player, "pp:mummy-spoken-to", false))
            {
                openDialogue(player, NPCs.GUARDIAN_MUMMY_4476)
                return@on true
            }

            join(player)
            return@on true
        }
    }

    companion object {
        @JvmStatic val GUARDIAN_ROOM = Location.create(1968, 4420, 2)
        @JvmStatic val EMPTY_ROOM = Location.create(1934, 4450, 2)

        @JvmStatic fun join(player: Player)
        {
            if(PlunderUtils.hasPlayer(player))
            {
                sendMessage(player, "[PLUNDER] You should never see this message. Please report this.")
                return
            }
            PlunderUtils.registerPlayer(player)
            PlunderUtils.loadNextRoom(player)
            PlunderUtils.openOverlay(player)
        }
    }
}