package rs09.game.content.zone.phasmatys.bonegrinder

import api.Container
import api.*
import core.game.content.global.Bones
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Items
import rs09.game.interaction.InteractionListener
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

class BoneGrinderListener : InteractionListener() {

    private val boneIDs = Bones.values().map { it.itemId }.toIntArray()

    override fun defineListeners() {

        /**
         * Handle the bone loader/hopper fill option
         */
        on(LOADER,SCENERY,"fill"){ player, _ ->
            handleFill(player)
        }

        /**
         * Handle the wheel's wind option
         */
        on(BONE_GRINDER,SCENERY,"wind"){ player, _ ->
            handleWind(player)
        }

        /**
         * Handle the wheel's status option
         */
        on(BONE_GRINDER,SCENERY,"status"){ player, _ ->
            handleStatus(player)
        }

        /**
         * Handle the bin's empty option
         */
        on(BIN,SCENERY,"empty"){ player, _ ->
            handleEmpty(player)
        }

        /**
         * Handle Bone -> Hopper
         */
        onUseWith(SCENERY,LOADER,*boneIDs){ player, _, _ ->
            handleFill(player)
            return@onUseWith true
        }

    }

    fun handleFill(player: Player): Boolean{
        val bone = getBone(player)
        if(bone == null){
            sendMessage(player,"You have no bones to grind.")
            return true
        }
        if(getAttribute(player,BONE_HOPPER_KEY,false)){
            sendMessage(player,"You already have some bones in the hopper.")
            return true
        }
        if(getAttribute(player,BONE_BIN_KEY,false)){
            sendMessage(player,"You already have some bonemeal that needs to be collected.")
            return true
        }

        val fillPulse = object: Pulse(){
            var stage = 0
            override fun pulse(): Boolean {
                when(stage++){
                    0 -> {
                        lock(player, FILL_ANIM.duration)
                        animate(player,FILL_ANIM)
                    }
                    FILL_ANIM.duration -> {
                        sendMessage(player,"You fill the hopper with bones.")
                        removeItem(player,Item(bone.itemId),Container.INVENTORY)
                        setAttribute(player,LOADED_BONE_KEY,bone.ordinal)
                        setAttribute(player,BONE_HOPPER_KEY,true)
                        return true
                    }
                }
                return false
            }
        }

        if(inInventory(player,bone.itemId)){
            player.pulseManager.run(object : Pulse(){
                var stage = 0
                override fun pulse(): Boolean {
                    when(stage++){
                        0 -> Pulser.submit(fillPulse).also { delay = FILL_ANIM.duration + 1}
                        1 -> {
                            stopWalk(player)
                            forceWalk(player,Location(3659,3524),"smart")
                            delay = 2
                        }
                        2 -> {
                            handleWind(player)
                            delay = WIND_ANIM.duration + 1
                        }
                        3 -> {
                            stopWalk(player)
                            forceWalk(player,Location(3658,3524),"smart")
                            delay = 2
                        }
                        4 -> {
                            if(!inInventory(player,Items.EMPTY_POT_1931,1)){
                                return handleEmpty(player)
                            } else {
                                handleEmpty(player)
                                delay = SCOOP_ANIM.duration + 1
                            }
                        }
                        5 -> {
                            stopWalk(player)
                            forceWalk(player,Location(3660,3524),"smart")
                            delay = 4
                        }
                        6 -> {
                            face(player,Location(3660,3526))
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
        if(!getAttribute(player,BONE_HOPPER_KEY,false)){
            sendMessage(player,"You have no bones loaded to grind.")
            return true
        }

        if(getAttribute(player,BONE_BIN_KEY,false)){
            sendMessage(player,"You already have some bonemeal which you need to collect.")
            return true
        }

        Pulser.submit(object : Pulse(){
            var stage = 0
            override fun pulse(): Boolean {
                when(stage++){
                    0 -> {
                        face(player,Location(3659, 3526, 1))
                        lock(player,WIND_ANIM.duration)
                        animate(player,WIND_ANIM)
                        sendMessage(player,"You wind the handle.")
                    }
                    WIND_ANIM.duration -> {
                        sendMessage(player,"The bonemeal falls into the bin.")
                        setAttribute(player,BONE_HOPPER_KEY,false)
                        setAttribute(player,BONE_BIN_KEY,true)
                        return true
                    }
                }
                return false
            }
        })
        return true
    }

    private fun handleStatus(player: Player): Boolean{
        val bonesLoaded = getAttribute(player,BONE_HOPPER_KEY,false)
        val boneMealReady = getAttribute(player,BONE_BIN_KEY,false)

        if(bonesLoaded) sendMessage(player,"There are bones waiting in the hopper.")
        if(boneMealReady) sendMessage(player,"There is bonemeal waiting in the bin to be collected.")

        if(!bonesLoaded && !boneMealReady){
            sendMessage(player,"There is nothing loaded into the machine.")
        }

        return true
    }

    fun handleEmpty(player: Player): Boolean{
        if(!getAttribute(player,BONE_BIN_KEY,false)){
            sendMessage(player,"You have no bonemeal to collect.")
            return true
        }

        if(getAttribute(player,BONE_HOPPER_KEY,false) && !getAttribute(player,BONE_BIN_KEY,false)){
            sendMessage(player,"You need to wind the wheel to grind the bones.")
            return true
        }

        if(!inInventory(player,Items.EMPTY_POT_1931,1)){
            sendMessage(player,"You don't have any pots to take the bonemeal with.")
            return true
        }

        val bone = Bones.values()[getAttribute(player,LOADED_BONE_KEY,-1)]


        Pulser.submit(object : Pulse(){
            var stage = 0
            override fun pulse(): Boolean {
                when(stage++){
                    0 -> {
                        face(player,Location(3658, 3525, 1))
                        lock(player, SCOOP_ANIM.duration)
                        animate(player,SCOOP_ANIM)
                    }
                    SCOOP_ANIM.duration -> {
                        if(removeItem(player,Item(Items.EMPTY_POT_1931),Container.INVENTORY)){
                            addItem(player,bone.boneMeal.id)
                            setAttribute(player,BONE_BIN_KEY,false)
                            setAttribute(player,BONE_HOPPER_KEY,false)
                            setAttribute(player,LOADED_BONE_KEY,-1)
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
            if(inInventory(player,bone.itemId)) return bone
        }
        return null
    }
}