package rs09.game.content.zone.phasmatys.bonegrinder

import core.game.content.global.Bones
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Items
import rs09.game.interaction.OptionListener
import rs09.game.world.GameWorld.Pulser

private const val LOADER = 11162
private const val BONE_GRINDER = 11163
private const val BIN = 11164
private const val LOADED_BONE_KEY = "/save:bonegrinder-bones"
private const val BONE_HOPPER_KEY = "/save:bonegrinder-hopper"
private const val BONE_BIN_KEY = "/save:bonegrinder-bin"
private val FILL_ANIM = Animation(1649)
private val WIND_ANIM = Animation(1648)
private val SCOOP_ANIM = Animation(1650)

class BoneGrinderListener : OptionListener() {

    override fun defineListeners() {

        /**
         * Handle the bone loader/hopper fill option
         */
        on(LOADER,OBJECT,"fill"){player, _ ->
            handleFill(player)
        }

        /**
         * Handle the wheel's wind option
         */
        on(BONE_GRINDER,OBJECT,"wind"){player,_ ->
            handleWind(player)
        }

        /**
         * Handle the wheel's status option
         */
        on(BONE_GRINDER,OBJECT,"status"){player,_ ->
            handleStatus(player)
        }

        /**
         * Handle the bin's empty option
         */
        on(BIN,OBJECT,"empty"){player,_ ->
            handleEmpty(player)
        }

    }

    fun handleFill(player: Player): Boolean{
        val bone = getBone(player)
        if(bone == null){
            player.sendMessage("You have no bones to grind.")
            return true
        }
        if(player.getAttribute(BONE_HOPPER_KEY,false) != false){
            player.sendMessage("You already have some bones in the hopper.")
            return true
        }
        if(player.getAttribute(BONE_BIN_KEY,false) != false){
            player.sendMessage("You already have some bonemeal that needs to be collected.")
            return true
        }

        val fillPulse = object: Pulse(){
            var stage = 0
            override fun pulse(): Boolean {
                when(stage++){
                    0 -> {
                        player.lock()
                        player.animator.animate(FILL_ANIM)
                    }
                    FILL_ANIM.duration -> {
                        player.sendMessage("You fill the hopper with bones.")
                        player.unlock()
                        player.inventory.remove(Item(bone.itemId))
                        player.setAttribute(LOADED_BONE_KEY,bone.ordinal)
                        player.setAttribute(BONE_HOPPER_KEY,true)
                        return true
                    }
                }
                return false
            }
        }

        if(player.inventory.getAmount(bone.itemId) > 0){
            player.pulseManager.run(object : Pulse(){
                var stage = 0
                override fun pulse(): Boolean {
                    when(stage++){
                        0 -> Pulser.submit(fillPulse).also { delay = FILL_ANIM.duration + 1}
                        1 -> {
                            player.walkingQueue.reset()
                            player.walkingQueue.addPath(3659,3524,true)
                            delay = 2
                        }
                        2 -> {
                            player.faceLocation(Location.create(3659, 3526, 1))
                            handleWind(player)
                            delay = WIND_ANIM.duration + 1
                        }
                        3 -> {
                            player.walkingQueue.reset()
                            player.walkingQueue.addPath(3658,3524,true)
                            delay = 2
                        }
                        4 -> {
                            player.faceLocation(Location.create(3658, 3525, 1))
                            if(!player.inventory.contains(Items.EMPTY_POT_1931,1)){
                                return handleEmpty(player)
                            } else {
                                handleEmpty(player)
                                delay = SCOOP_ANIM.duration + 1
                            }
                        }
                        5 -> {
                            player.walkingQueue.reset()
                            player.walkingQueue.addPath(3660,3524,true)
                            delay = 4
                        }
                        6 -> {
                            player.faceLocation(Location.create(3660,3526))
                            handleFill(player)
                            return true
                        }
                    }
                    return false
                }
            })
        } else {
            Pulser.submit(fillPulse)
        }
        return true
    }

    fun handleWind(player: Player): Boolean{
        if(!player.getAttribute(BONE_HOPPER_KEY,false)){
            player.sendMessage("You have no bones loaded to grind.")
            return true
        }

        if(player.getAttribute(BONE_BIN_KEY,false)){
            player.sendMessage("You already have some bonemeal which you need to collect.")
            return true
        }

        Pulser.submit(object : Pulse(){
            var stage = 0
            override fun pulse(): Boolean {
                when(stage++){
                    0 -> {
                        player.lock()
                        player.animator.animate(WIND_ANIM)
                        player.sendMessage("You wind the handle.")
                    }
                    WIND_ANIM.duration -> {
                        player.unlock()
                        player.sendMessage("The bonemeal falls into the bin.")
                        player.setAttribute(BONE_HOPPER_KEY,false)
                        player.setAttribute(BONE_BIN_KEY,true)
                        return true
                    }
                }
                return false
            }
        })
        return true
    }

    fun handleStatus(player: Player): Boolean{
        val bonesLoaded = player.getAttribute(BONE_HOPPER_KEY,false)
        val boneMealReady = player.getAttribute(BONE_BIN_KEY,false)

        if(bonesLoaded) player.sendMessage("There are bones waiting in the hopper.")
        if(boneMealReady) player.sendMessage("There is bonemeal waiting in the bin to be collected.")

        if(!bonesLoaded && !boneMealReady){
            player.sendMessage("There is nothing loaded into the machine.")
        }

        return true
    }

    fun handleEmpty(player: Player): Boolean{
        if(!player.getAttribute(BONE_BIN_KEY,false)){
            player.sendMessage("You have no bonemeal to collect.")
            return true
        }

        if(player.getAttribute(BONE_HOPPER_KEY,false) && !player.getAttribute(BONE_BIN_KEY,false)){
            player.sendMessage("You need to wind the wheel to grind the bones.")
            return true
        }

        if(!player.inventory.contains(Items.EMPTY_POT_1931,1)){
            player.sendMessage("You don't have any pots to take the bonemeal with.")
            return true
        }

        val bone = Bones.values()[player.getAttribute(LOADED_BONE_KEY,-1)]

        Pulser.submit(object : Pulse(){
            var stage = 0
            override fun pulse(): Boolean {
                when(stage++){
                    0 -> {
                        player.lock()
                        player.animator.animate(SCOOP_ANIM)
                    }
                    SCOOP_ANIM.duration -> {
                        player.unlock()
                        if(player.inventory.remove(Item(Items.EMPTY_POT_1931))){
                            player.inventory.add(bone.boneMeal)
                            player.setAttribute(BONE_BIN_KEY,false)
                            player.setAttribute(BONE_HOPPER_KEY,false)
                            player.setAttribute(LOADED_BONE_KEY,-1)
                        }
                        return true
                    }
                }
                return false
            }
        })

        return true
    }

    fun getBone(player: Player): Bones? {
        for(bone in Bones.values()){
            if(player.inventory.contains(bone.itemId,1)) return bone
        }
        return null
    }
}