package rs09.game.content.quest.members.thelosttribe

import core.game.content.activity.ActivityPlugin
import core.game.content.activity.CutscenePlugin
import core.game.content.dialogue.FacialExpression
import core.game.content.global.action.DoorActionHandler
import core.game.interaction.MovementPulse
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.emote.Emotes
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.map.build.DynamicRegion
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import rs09.game.camerautils.PlayerCamera
import rs09.game.world.World.Pulser

private const val DUKE = 2088
private const val MISTAG = 2089
private const val SIGMUND = 2090
private const val URTAG = 5858
private const val BOW_ANIM = 6000
@Initializable
/**
 * Final cutscene for the lost tribe
 * @author Ceikry
 */
class LostTribeCutscene(val pl: Player? = null) : CutscenePlugin("Lost Tribe Cutscene",true) {
    var camera: PlayerCamera = PlayerCamera(null)

    init {
        this.player = pl
    }

    override fun configure() {
        region = DynamicRegion.create(12850)
        setRegionBase()
        registerRegion(region.id)
    }

    override fun getStartLocation(): Location {
        return base.transform(7,17,0)
    }

    override fun newInstance(p: Player?): ActivityPlugin {
        return LostTribeCutscene(player)
    }

    override fun getSpawnLocation(): Location {
        return Location.create(3208, 3213, 0)
    }

    override fun open() {
        player.setAttribute("cutscene:original-loc",player.location)
        player.setAttribute("real-end",Location.create(3207, 3221, 0))
        player.interfaceManager.hideTabs(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14)
        if(player.settings.isRunToggled){
            player.settings.toggleRun()
        }

        camera = PlayerCamera(player)
        var loc = coords(8,26)
        camera.panTo(loc.x,loc.y,300,100)
        loc = coords(5,18)
        camera.rotateTo(loc.x,loc.y,300,100)

        player.faceLocation(coords(7,18))

        val mistag = npcs.get(1)
        val urtag = npcs.get(2)
        val duke = npcs.get(0)
        val sigmund = npcs.get(3)

        Pulser.submit(object: Pulse(){
            var counter = 0
            var stage = 0
            override fun pulse(): Boolean {
                if(!player.isPlaying || !player.isActive) return true
                when(counter++){
                    5 -> DoorActionHandler.handleDoor(player, RegionManager.getObject(coords(7,17)))
                    7 -> {
                        player.pulseManager.run(object: MovementPulse(player,coords(7,22),false){
                            override fun pulse(): Boolean {
                                return true
                            }
                        }, "movement")
                        mistag.pulseManager.run(object : MovementPulse(mistag,coords(7,19)){//coords(7,20)){
                            override fun pulse(): Boolean {
                                return true
                            }
                        }, "movement")
                        urtag.pulseManager.run(object : MovementPulse(urtag,coords(7,18)){
                            override fun pulse(): Boolean {
                                return true
                            }
                        })
                    }
                    12 -> {
                        mistag.pulseManager.run(object : MovementPulse(mistag,coords(7,20)){//coords(7,20)){
                        override fun pulse(): Boolean {
                            return true
                        }
                        }, "movement")
                        urtag.pulseManager.run(object : MovementPulse(urtag,coords(6,21)){
                            override fun pulse(): Boolean {
                                return true
                            }
                        })
                    }
                    15 -> {
                        player.faceLocation(duke.location)
                        player.dialogueInterpreter.sendDialogues(player,FacialExpression.FRIENDLY,"Your grace, I present Ur-tag, headman of the","Dorgeshuun.")
                        Emotes.BOW.play(player)
                        player.dialogueInterpreter.addAction { player, buttonId ->
                            Pulser.submit(object: MovementPulse(player,coords(7,24)){
                                override fun pulse(): Boolean {
                                    return true
                                }
                            })
                            Pulser.submit(object : Pulse(5){
                                var counter = 0
                                override fun pulse(): Boolean {
                                    duke.animator.animate(Emotes.BOW.animation)
                                    urtag.animator.animate(Animation(BOW_ANIM))
                                    player.dialogueInterpreter.sendDialogues(duke,FacialExpression.FRIENDLY,"Welcome, Ur-tag. I am sorry that your race came","under suspicion.")
                                    player.dialogueInterpreter.addAction { player, buttonId ->
                                        player.dialogueInterpreter.sendDialogues(duke,FacialExpression.ANGRY,"I assure you that the warmongering element has been","dealt with.")
                                        loc = coords(9,22)
                                        camera.panTo(loc.x,loc.y,300,2)
                                        loc = coords(6,22)
                                        camera.rotateTo(loc.x,loc.y,300,2)
                                        player.dialogueInterpreter.addAction { player, buttonId ->
                                            player.dialogueInterpreter.sendDialogues(urtag,FacialExpression.OLD_NORMAL,"I apologize for the damage to your cellar. I will send","workers to repair the hole.")
                                            player.dialogueInterpreter.addAction { player, buttonId ->
                                                player.dialogueInterpreter.sendDialogues(duke,FacialExpression.FRIENDLY,"No, let it stay. It can be a route of commerce between","our lands.")
                                                player.dialogueInterpreter.addAction { player, buttonId ->
                                                    duke.faceLocation(player.location)
                                                    player.faceLocation(duke.location)
                                                    camera.rotateTo(duke.location.x,duke.location.y,350,4)
                                                    loc = coords(11,22)
                                                    camera.panTo(loc.x,loc.y,325,3)
                                                    loc = coords(6,22)
                                                    camera.rotateTo(loc.x,loc.y,300,3)
                                                    player.dialogueInterpreter.sendDialogues(duke,FacialExpression.FRIENDLY,"${player.name}, Lumbridge is in your debt. Please accept","this ring as a token of my thanks.")
                                                    player.dialogueInterpreter.addAction { player, buttonId ->
                                                        player.dialogueInterpreter.sendDialogues(duke,FacialExpression.FRIENDLY,"It is enchanted to save you in your hour of need.")
                                                        player.dialogueInterpreter.addAction { player, buttonId ->
                                                            urtag.pulseManager.run(object: MovementPulse(urtag,coords(7,23)){
                                                                override fun pulse(): Boolean {
                                                                    return true
                                                                }
                                                            })
                                                            urtag.faceLocation(player.location)
                                                            player.dialogueInterpreter.sendDialogues(urtag,FacialExpression.OLD_NORMAL,"I too thank you. Accept the freedom of the Dorgeshuun","mines.")
                                                            player.dialogueInterpreter.addAction { player, buttonId ->
                                                                player.dialogueInterpreter.sendDialogues(urtag,FacialExpression.OLD_NORMAL,"These are strange times. I never dreamed that I would","see the surface, still less that I would be on friendly","terms with its people.")
                                                                loc = coords(16,21)
                                                                camera.panTo(loc.x,loc.y,300,3)
                                                                loc = coords(6,22)
                                                                camera.rotateTo(loc.x,loc.y,300,2)
                                                                player.dialogueInterpreter.addAction { player, buttonId ->
                                                                    Pulser.submit(object : Pulse(5) {
                                                                        override fun pulse(): Boolean {
                                                                            player.dialogueInterpreter.sendDialogues(sigmund,FacialExpression.ANGRY,"Prattle on, goblin.")
                                                                            player.dialogueInterpreter.addAction { player, buttonId ->
                                                                                player.dialogueInterpreter.sendDialogues(sigmund,FacialExpression.EVIL_LAUGH,"Soon you will be destroyed!")
                                                                                sigmund.animator.animate(Emotes.LAUGH.animation)
                                                                                player.dialogueInterpreter.addAction { player, buttonId ->
                                                                                    loc = coords(16,17)
                                                                                    sigmund.pulseManager.run(object: MovementPulse(sigmund,loc){
                                                                                        override fun pulse(): Boolean {
                                                                                            return true
                                                                                        }
                                                                                    })
                                                                                    Pulser.submit(object : Pulse(4){
                                                                                        override fun pulse(): Boolean {
                                                                                            Pulser.submit(endPulse)
                                                                                            Pulser.submit(object : Pulse(7){
                                                                                                override fun pulse(): Boolean {
                                                                                                    player.questRepository.getQuest("Lost Tribe").setStage(player,100)
                                                                                                    player.questRepository.getQuest("Lost Tribe").finish(player)
                                                                                                    return true
                                                                                                }
                                                                                            })
                                                                                            return true
                                                                                        }
                                                                                    })
                                                                                }
                                                                            }
                                                                            return true
                                                                        }
                                                                    })
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    return true
                                }
                            })
                        }
                        loc = coords(7,25)
                        camera.panTo(loc.x,loc.y,300,3)
                        return true
                    }
                }
                return false
            }
        })
    }

    override fun start(player: Player?, login: Boolean, vararg args: Any?): Boolean {
        val duke = NPC(DUKE,coords(6,23))
        val mistag = NPC(MISTAG,coords(8,17))
        val urtag = NPC(URTAG,coords(8,15))
        val sigmund = NPC(SIGMUND,coords(13,22))
        duke.init()
        duke.faceLocation(coords(6,22))
        mistag.init()
        urtag.init()
        sigmund.init()
        sigmund.faceLocation(coords(11,18))
        npcs.add(duke)
        npcs.add(mistag)
        npcs.add(urtag)
        npcs.add(sigmund)
        return super.start(player, login, *args)
    }

    fun coords(x: Int, y: Int): Location {
        return base.transform(x,y,0)
    }

}