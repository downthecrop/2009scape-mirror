package rs09.game.system.command.oldsys

//import rs09.game.ai.general.scriptrepository.ManThiever
import core.game.container.impl.EquipmentContainer
import core.game.interaction.Interaction
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.appearance.Gender
import core.game.node.entity.skill.Skills
import rs09.game.system.command.CommandPlugin
import core.game.system.command.CommandSet
import core.game.system.task.Pulse
import rs09.game.world.GameWorld
import rs09.game.world.ImmerseWorld
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.map.path.Pathfinder
import rs09.game.world.repository.Repository
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction
import rs09.game.ai.AIPBuilder
import rs09.game.ai.AIPlayer
import rs09.game.ai.general.GeneralBotCreator
import rs09.game.ai.general.scriptrepository.LobsterCatcher
import rs09.game.ai.pvmbots.PvMBotsBuilder
import rs09.game.ai.pvp.PVPAIPActions
import rs09.game.ai.pvp.PVPAIPBuilderUtils
import rs09.game.ai.resource.ResourceAIPActions
import rs09.game.ai.skillingbot.SkillingBotsBuilder
import rs09.game.ai.wilderness.PvPBotsBuilder
import java.util.*

/**
 * Handles the AIPlayer commands.
 * These commands are for bots
 *
 * @author Emperor
 */
@Initializable
class AIPCommandPlugin : CommandPlugin() {
    override fun newInstance(arg: Any?): Plugin<Any?>? {
        link(CommandSet.ADMINISTRATOR)
        return this
    }

    override fun parse(player: Player?, name: String?, args: Array<String?>?): Boolean {
        var name = name
        var legion = player!!.getAttribute<MutableList<AIPlayer>>("aip_legion")
        when (name) {
            "desaip" -> {
                player.removeAttribute("aip_select")
                return true
            }
            "sellegion" -> {
                if (legion != null && !legion.isEmpty()) {
                    player.setAttribute("aip_select", legion[0])
                }
                return true
            }
            "regroup" -> {
                var last = player
                if (legion != null && !legion.isEmpty()) {
                    for (p in legion) {
                        p.follow(last)
                        last = p
                    }
                }
                player.removeAttribute("aip_select")
                return true
            }
            "clearlegion" -> {
                if (legion != null && !legion.isEmpty()) {
                    for (p in legion) {
                        AIPlayer.deregister(p.uid)
                    }
                    legion.clear()
                }
                player.removeAttribute("aip_select")
                player.removeAttribute("aip_legion")
                return true
            }
            "clearaips" -> {
                for (p in Repository.players) {
                    if (p.isArtificial) {
                        p.clear()
                    }
                }
                return true
            }
            "aip" -> {
                name = if (args!!.size < 2) player.name else args[1]
                val p = AIPBuilder.copy(player, player.location.transform(0, 1, 0))
                p.init()
                Interaction.sendOption(player, 7, "Control")
                return true
            }
            "legion" -> {
                val size = if (args!!.size < 2) 10 else args[1]!!.toInt()
                var last = player
                if (legion == null) {
                    player.setAttribute("aip_legion", ArrayList<AIPlayer>().also { legion = it })
                }
                Interaction.sendOption(player, 7, "Control")
                val joinClan = player.communication.clan != null && !player.communication.clan.isDefault
                var i = 0
                while (i < size) {
                    val aip = AIPBuilder.copy(player, last!!.getLocation().transform(0, 1, 0))
                    aip.init()
                    if (legion!!.isEmpty()) {
                        aip.setAttribute("aip_legion", legion)
                    }
                    legion!!.add(aip)
                    val l: Player = last!!
                    if (joinClan) {
                        if (player.communication.clan.enter(aip)) {
                            aip.communication.clan = player.communication.clan
                        }
                        if (player.communication.clan.clanWar != null) {
                            player.communication.clan.clanWar.fireEvent("join", aip)
                        }
                    }
                    GameWorld.Pulser.submit(object : Pulse(1) {
                        override fun pulse(): Boolean {
                            aip.follow(l)
                            return true
                        }
                    })
                    last = aip
                    i++
                }
                return true
            }
            "banktest" -> {
                val bot = AIPlayer(player.location)
                //Location.create(2719, 3431, 0)
                //Location.create(2726, 3477, 0)
                //Location.create(2726, 3491, 0)
                Pathfinder.find(bot,Location.create(2726, 3491, 0)).walk(bot)
                return true
            }
            "pvplegion" -> {
                val size = if (args!!.size < 2) 10 else args[1]!!.toInt()
                var last = player
                if (PVPAIPActions.pvp_players == null) {
                    player.setAttribute("aip_legion", ArrayList<AIPlayer>().also { PVPAIPActions.pvp_players = it })
                }
                var i = 0
                while (i < size) {
                    val aip = AIPBuilder.create(generateLocation(player))
                    aip.controler = player
                    aip.appearance.gender = if (RandomFunction.random(3) == 1) Gender.FEMALE else Gender.MALE
                    aip.init()
                    PVPAIPBuilderUtils.generateClass(aip)
                    if (PVPAIPActions.pvp_players.isEmpty()) {
                        aip.setAttribute("aip_legion", PVPAIPActions.pvp_players)
                    }
                    PVPAIPActions.pvp_players.add(aip)
                    last = aip
                    i++
                }
                return true
            }
            "resourcelegion" -> {
                val size = if (args!!.size < 2) 10 else args[1]!!.toInt()
                var last = player
                if (ResourceAIPActions.resource_players == null) {
                    player.setAttribute("aip_legion", ArrayList<AIPlayer>().also { ResourceAIPActions.resource_players = it })
                }
                var i = 0
                while (i < size) {
                    val aip = AIPBuilder.create(generateLocation(player))
                    aip.controler = player
                    aip.appearance.gender = if (RandomFunction.random(3) == 1) Gender.FEMALE else Gender.MALE
                    aip.init()
                    PVPAIPBuilderUtils.generateClass(aip)
                    if (ResourceAIPActions.resource_players.isEmpty()) {
                        aip.setAttribute("aip_legion", ResourceAIPActions.resource_players)
                    }
                    ResourceAIPActions.resource_players.add(aip)
                    i++
                }
                return true
            }
            "syncresource" -> ResourceAIPActions.syncBotThread(player)
            "pvpfight" -> {
                PVPAIPActions.syncBotThread(player)
                return true
            }
            "bot" -> {
                PvMBotsBuilder.spawnLowest(player.location)
                return true
            }
            "molebot" -> {
                PvMBotsBuilder.spawnGiantMoleBot(player.location)
                return true
            }
            "slayerpoints" -> {
                player.slayer.slayerPoints = 50000
                return true
            }
            "dragonbot" -> {
                PvMBotsBuilder.spawnDragonKiller(player.location)
                return true
            }
            "pure" -> {
                player.skills.setStaticLevel(Skills.HITPOINTS, 60)
                player.skills.setStaticLevel(Skills.RANGE, 95)
                player.skills.setStaticLevel(Skills.MAGIC, 95)
                player.skills.setStaticLevel(Skills.ATTACK, 50)
                player.skills.setStaticLevel(Skills.STRENGTH, 93)
                player.skills.setStaticLevel(Skills.DEFENCE, 1)
                player.skills.setStaticLevel(Skills.PRAYER, 1)
                player.skills.updateCombatLevel()
                return true
            }
            "noobbot" -> {
                PvMBotsBuilder.spawnNoob(player.location)
                return true
            }
            "immerse", "immersiveworld", "immersive" -> {
                player.sendMessage("Started immersive world, 2")
                return true
            }
            "botdataform" -> {
                //Dumps your current character info in the form used by data/botdata
                //name:cblevel:helmet:cape:neck:weapon:chest:shield:unknown:legs:unknown:gloves:boots:
                println(player.username + ":"
                        + player.properties.currentCombatLevel + ":"
                        + player.equipment.getAsId(EquipmentContainer.SLOT_HAT) + ":"
                        + player.equipment.getAsId(EquipmentContainer.SLOT_CAPE) + ":"
                        + player.equipment.getAsId(EquipmentContainer.SLOT_AMULET) + ":"
                        + player.equipment.getAsId(EquipmentContainer.SLOT_WEAPON) + ":"
                        + player.equipment.getAsId(EquipmentContainer.SLOT_CHEST) + ":"
                        + player.equipment.getAsId(EquipmentContainer.SLOT_SHIELD) + ":"
                        + "0" + ":"
                        + player.equipment.getAsId(EquipmentContainer.SLOT_LEGS) + ":"
                        + "0" + ":"
                        + player.equipment.getAsId(EquipmentContainer.SLOT_HANDS) + ":"
                        + player.equipment.getAsId(EquipmentContainer.SLOT_FEET) + ":"
                )
                return true
            }
            "fishtest" -> {
                SkillingBotsBuilder.spawnTroutLumbridge(Location(3241, 3242))
                return true
            }
            "varrockminebots" -> {
                SkillingBotsBuilder.spawnClayBotVarrock(Location(3181, 3368))
                SkillingBotsBuilder.spawnSilverBotVarrock(Location(3181, 3368))
                SkillingBotsBuilder.spawnIronBotVarrock(Location(3181, 3368))
                SkillingBotsBuilder.spawnTinBotVarrock(Location(3181, 3368))
                return true
            }
            "pvpbot" -> {
                PvPBotsBuilder.spawn(player.location)
                return true
            }
            "pvpbots" -> {
                var amountBots = 0
                while (amountBots < 50) {
                    PvPBotsBuilder.spawn(player.location)
                    amountBots++
                }
                return true
            }
            "ranger" -> {
            }
            "removetask" -> return if (!player.slayer.hasTask()) {
                player.sendMessage("You don't have an active task right now.")
                true
            } else {
                player.slayer.clear()
                player.sendMessage("You have canceled your current task.")
                true
            }
            "testpest", "pcbots", "pestcontrolbots", "pest-test", "test-pest", "pesttest" -> {
                player.sendMessage("Spawning some bots I think")
                val arg2: Int
                arg2 = try {
                    args!![1]!!.toInt()
                } catch (e: Exception) {
                    20
                }
                var pestBotsAmount = 0
                while (pestBotsAmount < arg2) {
                    PvMBotsBuilder.createPestControlTestBot(player.location)
                    pestBotsAmount++
                }
                return true
            }
            "bots" -> {
                var arg = 1
                var xpos = 0
                var ypos = 0
                try {
                    arg = args!![1]!!.toInt()
                } catch (e: Exception) {
                    println("Rip " + args!![1])
                }
                var amountBots2 = 0
                while (amountBots2 < arg) {
                    xpos = 2500 + RandomFunction.getRandom(1000)
                    ypos = 3000 + RandomFunction.getRandom(500)
                    PvMBotsBuilder.spawnNoob(Location(xpos, ypos))
                    amountBots2++
                }
                println("$xpos $ypos")
                return true
            }
            //"manthiev" -> GeneralBotCreator(player.location, ManThiever())
            "fish" -> GeneralBotCreator(Location.create(2805, 3435, 0), LobsterCatcher())
        }
        return false
    }

    private fun generateLocation(player: Player?): Location {
        val random_location = player!!.location.transform(RandomFunction.random(-15, 15), RandomFunction.random(-15, 15), 0)
        if (!RegionManager.isTeleportPermitted(random_location)) {
            return generateLocation(player)
        }
        if (!Pathfinder.find(player, random_location, false, Pathfinder.DUMB).isSuccessful) {
            return generateLocation(player)
        }
        return if (RegionManager.getObject(random_location) != null) {
            generateLocation(player)
        } else random_location
    }
}