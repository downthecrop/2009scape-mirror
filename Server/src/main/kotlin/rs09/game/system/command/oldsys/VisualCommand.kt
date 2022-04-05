package rs09.game.system.command.oldsys

import api.*
import core.cache.Cache
import core.game.container.access.InterfaceContainer
import core.game.content.quest.tutorials.tutorialisland.CharacterDesign
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.node.entity.combat.ImpactHandler.HitsplatType
import core.game.node.entity.impl.Projectile
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.audio.Audio
import core.game.node.item.Item
import core.game.system.command.CommandSet
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.net.packet.PacketRepository
import core.net.packet.context.VarbitContext
import core.net.packet.out.Varbit
import core.plugin.Initializable
import core.plugin.Plugin
import rs09.game.system.command.CommandPlugin
import rs09.game.world.GameWorld
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

/**
 * Represents the the command plugin used for visual commands.
 * @author Vexia
 * @author Emperor
 */
@Initializable
class VisualCommand : CommandPlugin() {
    override fun newInstance(arg: Any?): Plugin<Any?>? {
        link(CommandSet.ADMINISTRATOR)
        return this
    }

    override fun parse(player: Player?, name: String?, args: Array<String?>?): Boolean {
        var location: Location? = null
        var `object`: Scenery? = null
        var o: Player? = null
        when (name) {
            "invisible", "invis", "seti" -> {
                player!!.isInvisible = !player.isInvisible
                player.sendMessage("You are now " + (if (player.isInvisible) "invisible" else "rendering") + " for other players.")
            }
            "maxkc" -> {
                var i = 0
                while (i < 6) {
                    player!!.savedData.activityData.barrowBrothers[i] = true
                    i++
                }
                val names = arrayOf("Ahrim", "Dharok", "Guthan", "Karil", "Torag", "Verac")
                player!!.savedData.activityData.barrowKills = 50
                player.packetDispatch.sendMessage("Flagged all barrow brothers killed and 50 catacomb kills, current entrance: " + names[player.savedData.activityData.barrowTunnelIndex] + ".")
                return true
            }
            "1hko" -> {
                player!!.setAttribute("1hko", !player.getAttribute("1hko", false))
                player.packetDispatch.sendMessage("1-hit KO mode " + if (player.getAttribute("1hko", false)) "on." else "off.")
                return true
            }
            "anim", "emote" -> {
                if (args!!.size < 2) {
                    player!!.debug("syntax error: id (optional) delay")
                    return true
                }
                val animation = Animation(args[1]!!.toInt(), if (args.size > 2) args[2]!!.toInt() else 0)
                player!!.animate(animation)
                return true
            }
            "render", "remote" -> {
                if (args!!.size < 2) {
                    player!!.debug("syntax error: ::render id")
                    return true
                }
                try {
                    player!!.appearance.setAnimations(Animation.create(args[1]!!.toInt()))
                    player.appearance.sync()
                } catch (e: NumberFormatException) {
                    player!!.packetDispatch.sendMessage("Use: ::remote id")
                }
                return true
            }
            "normalwalk" -> {
                player!!.appearance.prepareBodyData(player)
                player.appearance.setDefaultAnimations()
                player.appearance.setAnimations()
                player.appearance.sync()
                return true
            }
            "gfx", "graphic", "graphics" -> {
                if (args!!.size < 2) {
                    player!!.debug("syntax error: id (optional) height delay")
                    return true
                }
                player!!.graphics(Graphics(args[1]!!.toInt(), if (args.size > 2) args[2]!!.toInt() else 0, if (args.size > 3) args[3]!!.toInt() else 0))
                return true
            }
            "sync", "visual" -> {
                if (args!!.size < 3) {
                    player!!.debug("syntax error: anim_id gfx_id (optional) height")
                    return true
                }
                val animId = toInteger(args[1]!!)
                val gfxId = toInteger(args[2]!!)
                val height = if (args.size > 3) toInteger(args[3]!!) else 0
                player!!.visualize(Animation.create(animId), Graphics(gfxId, height))
                return true
            }
            "pos_graphic", "position_gfx", "pos_gfx", "lgfx" -> {
                if (args!!.size < 2) {
                    player!!.debug("syntax error: id x y (optional) z height delay")
                    return true
                }
                location = Location.create(args[2]!!.toInt(), args[3]!!.toInt(), if (args.size > 4) args[4]!!.toInt() else 0)
                player!!.packetDispatch.sendPositionedGraphic(args[1]!!.toInt(), if (args.size > 5) args[5]!!.toInt() else 0, if (args.size > 6) args[6]!!.toInt() else 0, location)
            }
            "npc" -> {
                if (args!!.size < 2) {
                    player!!.debug("syntax error: id (optional) direction")
                    return true
                }
                val npc = NPC.create(toInteger(args[1]!!), player!!.location)
                npc.setAttribute("spawned:npc", true)
                npc.isRespawn = false
                npc.direction = player.direction
                npc.init()
                npc.isWalks = if (args.size > 2) true else false
                val npcString = "{" + npc.location.x + "," + npc.location.y + "," + npc.location.z + "," + (if (npc.isWalks) "1" else "0") + "," + npc.direction.ordinal + "}"
                val clpbrd = Toolkit.getDefaultToolkit().systemClipboard
                clpbrd.setContents(StringSelection(npcString), null)
                println(npcString)
                return true
            }
            "npcsquad" -> {
                if (args!!.size < 2) {
                    player!!.debug("syntax error: id (optional) sizeX sizeY")
                    return true
                }
                var sizeX = 3
                var sizeY = 3
                if (args.size > 2) {
                    sizeX = toInteger(args[2]!!)
                    sizeY = if (args.size > 3) {
                        toInteger(args[3]!!)
                    } else {
                        sizeX
                    }
                }
                val aggressive = args.size > 4
                var x = 0
                while (x < sizeX) {
                    var y = 0
                    while (y < sizeY) {
                        val npc = NPC.create(toInteger(args[1]!!), player!!.location.transform(1 + x, 1 + y, 0))
                        npc.setAttribute("spawned:npc", true)
                        npc.setAggressive(aggressive)
                        npc.init()
                        npc.setRespawn(false)
                        npc.setWalks(aggressive)
                        y++
                    }
                    x++
                }
                return true
            }
            "teleallowed" -> {
                player!!.debug("Is tele allowed here? " + RegionManager.isTeleportPermitted(player!!.location))
                return true
            }
            "oib" -> player!!.interfaceManager.openInfoBars()
            "char" -> CharacterDesign.open(player)
            "savenpc" -> return true
            "objwithanim" -> {
                val go = Scenery(toInteger(args!![1]!!), player!!.location, 0)
                SceneryBuilder.add(go)
                player.packetDispatch.sendSceneryAnimation(go, Animation.create(toInteger(args[2]!!)))
                return true
            }
            "oa", "object_anim", "obj_anim", "objectanim", "objanim" -> {
                if (args!!.size < 2) {
                    player!!.debug("syntax error: x y id")
                    return true
                }
                location = if (args.size > 2) Location.create(args[1]!!.toInt(), args[2]!!.toInt(), player!!.location.z) else player!!.location
                `object` = RegionManager.getObject(location)
                if (`object` == null) {
                    player!!.debug("error: object not found in region cache.")
                    return true
                }
                player!!.packetDispatch.sendSceneryAnimation(`object`, Animation(toInteger(args[args.size - 1]!!)))
                //sendMessage(player, `object`.definition.modelIds.map { it.toString() }.toString())
                sendMessage(player, `object`.definition.animationId.toString())
                return true
            }
            "inter", "component", "interface" -> {
                if (args!!.size < 2) {
                    player!!.debug("syntax error: interface-id")
                    return true
                }
                val componentId = toInteger(args[1]!!)
                if (componentId < 0 || componentId > Cache.getInterfaceDefinitionsSize()) {
                    player!!.debug("Invalid component id [id=" + componentId + ", max=" + Cache.getInterfaceDefinitionsSize() + "].")
                    return true
                }
                player!!.interfaceManager.openComponent(componentId)
                return true
            }
            "ti" -> player!!.packetDispatch.sendInterfaceConfig(90, 87, false)
            "iconfig", "inter_config" -> {
                if (args!!.size < 2) {
                    player!!.debug("syntax error: interface-id child hidden")
                    return true
                }
                val hidden = if (args.size > 3) java.lang.Boolean.parseBoolean(args[3]) else true
                player!!.packetDispatch.sendInterfaceConfig(toInteger(args[1]!!), toInteger(args[2]!!), hidden)
                player.packetDispatch.sendMessage("Interface child (id=" + args[1] + ", child=" + args[2] + ") is " + if (hidden) "hidden." else "visible.")
                return true
            }
            "loop_varposition" -> {
                val value = (args!![1]!!.toString().toInt()) ?: 0
                val cfg_index = (args.getOrNull(2)?.toString()?.toInt() ?: -1)
                if(cfg_index == -1){
                    submitWorldPulse(object : Pulse(3, player){
                        var pos = 32
                        var shift = 0
                        override fun pulse(): Boolean {
                            for(i in 0..1999){
                                player?.configManager?.forceSet(i, pos shl shift, false)
                            }
                            player?.sendMessage("$pos shl $shift")
                            if(pos++ >= 63){
                                shift += 4
                                pos = 0
                            }
                            return shift >= 32
                        }
                    })
                } else {
                    submitWorldPulse(object : Pulse(3, player) {
                        var pos = 0
                        override fun pulse(): Boolean {
                            player?.configManager?.forceSet(cfg_index, value shl pos, false)
                            player?.sendMessage("$pos")
                            return pos++ >= 32
                        }
                    })
                }
            }
            "setbit" -> {
                if (args!!.size < 2) {
                    player!!.debug("syntax error: bit value")
                    return true
                }
                var bit = toInteger(args[0]!!)
                var value = toInteger(args[1]!!)
                var val2 = toInteger(args[2]!!)
                player!!.debug("$value $val2")
                PacketRepository.send(Varbit::class.java, VarbitContext(player, value, val2))
                return true
            }
            "setbits" -> {
                args ?: return false
                val start = toInteger(args[1]!!)
                val end = toInteger(args[2]!!)
                val value = toInteger(args[3]!!)
                for(i in start until end){
                    player?.varpManager?.setVarbit(i, value)
                }
            }
            "loop_anim_on_i" -> {
                var anim = toInteger(args!![1]!!)
                submitWorldPulse(object : Pulse(3){
                    override fun pulse(): Boolean {
                        player!!.packetDispatch.sendAnimationInterface(anim++, 224, 7)
                        sendMessage(player, "${anim - 1}")
                        return false
                    }
                })
            }
            "send_i_anim" -> {
                val iface = args?.getOrNull(0) ?: return true
                val anim = args.getOrNull(1) ?: return true

                player?.packetDispatch?.sendAnimationInterface(toInteger(anim), toInteger(iface),7)
            }
            "loop_inter" -> {
                val st = toInteger(args!![1]!!)
                val en = if (args.size > 2) toInteger(args[2]!!) else 740
                GameWorld.Pulser.submit(object : Pulse(3, player) {
                    var id = st
                    override fun pulse(): Boolean {
//					PacketRepository.send(Interface.class, new InterfaceContext(player, 548, 77, id, false));
                        player!!.interfaceManager.openComponent(id)
                        player.debug("Interface id: $id")
                        return ++id >= en
                    }
                })
                return true
            }
            "loop_iconfig" -> {
                val st = toInteger(args!![1]!!)
                val en = if (args.size > 2) toInteger(args[2]!!) else 740
                GameWorld.Pulser.submit(object : Pulse(3, player) {
                    var id = 0
                    override fun pulse(): Boolean {
//					PacketRepository.send(Interface.class, new InterfaceContext(player, 548, 77, id, false));
                        player!!.packetDispatch.sendInterfaceConfig(st,id,true)
                        player.debug("child id: $id")
                        return ++id >= en
                    }
                })
                return true
            }
            "loop_itemoni" -> {
                val st = toInteger(args!![1]!!)
                val en = if (args.size > 2) toInteger(args[2]!!) else 740
                GameWorld.Pulser.submit(object : Pulse(1, player) {
                    var id = 0
                    override fun pulse(): Boolean {
//					PacketRepository.send(Interface.class, new InterfaceContext(player, 548, 77, id, false));
                       InterfaceContainer.generateItems(player, arrayOf(Item(4151),Item(410)), arrayOf("E"),367,id,5,4)
                        player?.packetDispatch?.sendInterfaceConfig(st,id,true)
                        //player?.packetDispatch?.sendString("ASDASDASD",st,id)
                        player?.debug("child id: $id")
                        return ++id >= en
                    }
                })
                return true
            }
            "loop_config", "config_loop" -> {
                if (args!!.size < 4) {
                    player!!.debug("syntax error: config-id start end value")
                    return true
                }
                val value = toInteger(args[3]!!)
                var i = toInteger(args[1]!!)
                while (i < toInteger(args[2]!!)) {
                    player!!.configManager[i] = value
                    i++
                }
                return true
            }
            "string" -> {
                if (args!!.size < 3) {
                    player!!.debug("syntax error: interface child text")
                    return true
                }
                player!!.packetDispatch.sendString(args[3], toInteger(args[1]!!), toInteger(args[2]!!))
                return true
            }
            "loop_string", "string_loop" -> {
                if (args!!.size < 3) {
                    player!!.debug("syntax error: interface min max")
                    return true
                }
                val interfaceId = toInteger(args[1]!!)
                var i = toInteger(args[2]!!)
                while (i < toInteger(args[3]!!)) {
                    player!!.packetDispatch.sendString("child=$i", interfaceId, i)
                    i++
                }
                return true
            }
            "loop_oa" -> {
                val startId = toInteger(args!![1]!!)
                val endId = if (args.size > 2) toInteger(args[2]!!) else 11000
                GameWorld.Pulser.submit(object : Pulse(3, player) {
                    var id = startId
                    override fun pulse(): Boolean {
                        val `object` = RegionManager.getObject(player!!.location)
                        if (`object` == null) {
                            player.debug("error: object not found in region cache.")
                            return true
                        }
                        player.packetDispatch.sendSceneryAnimation(`object`, Animation(id))
                        player.debug("Animation id: $id")
                        return ++id >= endId
                    }
                })
                return true
            }
            "loop_anim" -> {
                val start = toInteger(args!![1]!!)
                val end = if (args.size > 2) toInteger(args[2]!!) else 11000
                GameWorld.Pulser.submit(object : Pulse(3, player) {
                    var id = start
                    override fun pulse(): Boolean {
                        player!!.animate(Animation.create(id))
                        player.debug("Animation id: $id")
                        return ++id >= end
                    }
                })
                return true
            }
            "loop_gfx" -> {
                val s = toInteger(args!![1]!!)
                val e = if (args.size > 2) toInteger(args[2]!!) else 11000
                GameWorld.Pulser.submit(object : Pulse(3, player) {
                    var id = s
                    override fun pulse(): Boolean {
                        Projectile.create(player!!.location, player.location.transform(0, 3, 0), id, 42, 36, 46, 75, 5, 11).send()
                        player.graphics(Graphics(id, 96))
                        player.debug("Graphic id: $id")
                        return ++id >= e
                    }
                })
                return true
            }
            "removenpc" -> {
                player!!.setAttribute("removenpc", !player.getAttribute("removenpc", false))
                player.debug("You have set remove npc value to " + player.getAttribute("removenpc", false) + ".")
                return true
            }
            "pnpc" -> {
                if (args!!.size < 2) {
                    player!!.debug("syntax error: id")
                    return true
                }
                player!!.appearance.transformNPC(toInteger(args[1]!!))
                return true
            }
            "itemoni" -> {
                val inter = toInteger(args!![1]!!)
                val child = toInteger(args[2]!!)
                val item = if (args.size > 3) toInteger(args[3]!!) else 1038
                player!!.packetDispatch.sendItemZoomOnInterface(item, 270, inter, child)
                return true
            }
            "hit" -> {
                player!!.impactHandler.manualHit(player, toInteger(args!![1]!!), HitsplatType.NORMAL)
                return true
            }
            "sound" -> {
                player!!.audioManager.send(Audio(args!![1]!!.toInt(), 10, 1))
                return true
            }
            "noclip" -> {
                player!!.setAttribute("no_clip", !player.getAttribute("no_clip", false))
                return true
            }
            /*"grow" -> {
                for (wrapper in player!!.farmingManager.patches) {
                    if (wrapper == null || wrapper.patch == null || wrapper.cycle == null || wrapper.cycle.growthHandler == null) {
                        continue
                    }
                    wrapper.cycle.growthHandler.handle()
                }
                return true
            }*/
            "disabledisease" -> {
                player!!.setAttribute("stop-disease", !player.getAttribute("stop-disease", false))
                player.sendMessage("Disable disease=" + player.getAttribute("stop-disease", false))
                return true
            }
            /*
            "rake" -> {
                for (wrapper in player!!.farmingManager.patches) {
                    wrapper.cycle.clear(player)
                }
                return true
            }
            "full" -> {
                for (wrapper in player!!.farmingManager.patches) {
                    var i = 0
                    while (i < 20) {
                        if (wrapper == null || wrapper.patch == null || wrapper.cycle == null || wrapper.cycle.growthHandler == null) {
                            i++
                            continue
                        }
                        wrapper.cycle.growthHandler.handle()
                        i++
                    }
                }
                return true
            }
            "toreg" -> {
                o = Repository.getPlayerByName(args!![1])
                o?.ironmanManager?.mode = IronmanMode.NONE
                player!!.sendMessage("done...")
                o?.sendMessage("<col=FF0000>You are no longer an ironman. Log out to see the ironman icon disappear.</col>")
            }
            "testcut" -> {
                ActivityManager.start(player,"Lost Tribe Cutscene",true)
            }
            "tryinfinity" -> {
                GameWorld.Pulser.submit(object : Pulse(1, player) {
                    var id = 0
                    override fun pulse(): Boolean {
                        player?.configManager?.set(532,id)
                        player?.debug("Child: $id")
                        return ++id == 999
                    }
                })
            }
            "clearpatches" -> {
                if (args!!.size > 1) {
                    o = Repository.getPlayerByName(args[1])
                }
                if (o != null) {
                    for (wrapper in o.farmingManager.patches) {
                        wrapper.cycle.clear(o)
                    }
                    o.sendMessage("Your patches have been cleared.")
                    player!!.sendMessage("You cleared " + o.username + "'s patches.")
                    return true
                }
                for (wrapper in player!!.farmingManager.patches) {
                    wrapper.cycle.clear(player)
                }
                return true
            }*/
        }
        return false;
    }
}