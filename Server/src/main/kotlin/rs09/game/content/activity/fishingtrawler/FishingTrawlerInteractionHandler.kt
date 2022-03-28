package rs09.game.content.activity.fishingtrawler

import core.game.content.activity.ActivityManager
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import org.rs09.consts.Items
import rs09.game.interaction.InteractionListener
import rs09.game.node.entity.player.info.stats.FISHING_TRAWLER_LEAKS_PATCHED
import rs09.game.node.entity.player.info.stats.STATS_BASE
import rs09.tools.stringtools.colorize
import kotlin.math.ceil

/**
 * Option handler for fishing trawler
 * @author Ceikry
 */
class FishingTrawlerInteractionHandler : InteractionListener() {
    val ENTRANCE_PLANK = 2178
    val EXIT_PLANK = 2179
    val HOLE = 2167
    val NETIDs = intArrayOf(2164,2165)
    val REWARD_NET = 2166
    val BARREL_IDS = intArrayOf(2159,2160)
    val BAILING_BUCKET = 583
    val FULL_BAIL_BUCKET = 585

    override fun defineListeners() {

        on(ENTRANCE_PLANK,SCENERY,"cross"){ player, _ ->
            if(player.skills.getLevel(Skills.FISHING) < 15){
                player.dialogueInterpreter.sendDialogue("You need to be at least level 15 fishing to play.")
                return@on true
            }
            player.properties.teleportLocation = Location.create(2672, 3170, 1)
            (ActivityManager.getActivity("fishing trawler") as FishingTrawlerActivity).addPlayer(player)
            return@on true
        }

        on(EXIT_PLANK,SCENERY,"cross"){ player, _ ->
            player.properties.teleportLocation = Location.create(2676, 3170, 0)
            (ActivityManager.getActivity("fishing trawler") as FishingTrawlerActivity).removePlayer(player)
            val session: FishingTrawlerSession? = player.getAttribute("ft-session",null)
            session?.players?.remove(player)
            return@on true
        }

        on(HOLE,SCENERY,"fill"){ player, node ->
            val session: FishingTrawlerSession? = player.getAttribute("ft-session",null)
            session ?: return@on false
            player.lock()
            player.pulseManager.run(object : Pulse(){
                var counter = 0
                override fun pulse(): Boolean {
                    when(counter++){
                        0 -> player.animator.animate(Animation(827)).also { player.lock() }
                        1 -> session.repairHole(player,node.asScenery()).also { player.incrementAttribute("/save:$STATS_BASE:$FISHING_TRAWLER_LEAKS_PATCHED"); player.unlock() }
                        2 -> return true
                    }
                    return false
                }
            })
            return@on true
        }

        on(NETIDs,SCENERY,"inspect"){ player, _ ->
            player.dialogueInterpreter.open(18237583)
            return@on true
        }

        on(REWARD_NET,SCENERY,"inspect"){ player, _ ->
            val session: FishingTrawlerSession? = player.getAttribute("ft-session",null)
            if(session == null || session.boatSank){
                player.dialogueInterpreter.sendDialogues(player, FacialExpression.GUILTY,"I'd better not go stealing other people's fish.")
                return@on true
            }
            player.dialogueInterpreter.open(18237582)
            return@on true
        }

        on(BARREL_IDS,SCENERY,"climb-on"){ player, _ ->
            player.properties.teleportLocation = Location.create(2672, 3222, 0)
            player.dialogueInterpreter.sendDialogue("You climb onto the floating barrel and begin to kick your way to the","shore.","You make it to the shore tired and weary.")
            player.appearance.setDefaultAnimations()
            player.appearance.sync()
            return@on true
        }

        on(FULL_BAIL_BUCKET,ITEM,"empty"){player,node ->
            player.lock()
            player.pulseManager.run(
                object : Pulse(){
                    var counter = 0
                    override fun pulse(): Boolean {
                        when(counter++){
                            0 -> player.animator.animate(Animation(2450))
                            1 -> {
                                if(player.inventory.remove(node.asItem()))
                                    player.inventory.add(Item(Items.BAILING_BUCKET_583))
                                player.unlock()
                                return true
                            }
                        }
                        return false
                    }
                }
            )
            return@on true
        }

        on(BAILING_BUCKET,ITEM,"bail-with") { player, node ->
            val session: FishingTrawlerSession? = player.getAttribute("ft-session",null)
            session ?: return@on false
            if(!session.isActive){
                return@on false
            }
            if(player.location.z > 0){
                player.sendMessage("You can't scoop water out up here.")
                return@on true
            }
            player.lock()
            player.pulseManager.run(
                object : Pulse(){
                    var counter = 0
                    override fun pulse(): Boolean {
                        when(counter++){
                            0 -> player.animator.animate(Animation(4471))
                            1 -> if(player.inventory.remove(node.asItem())) {
                                if (session.waterAmount > 0) {
                                    session.waterAmount -= 20
                                    if (session.waterAmount < 0) session.waterAmount = 0
                                    player.inventory.add(Item(Items.BAILING_BUCKET_585))
                                } else {
                                    player.sendMessage("There's no water to remove.")
                                    player.inventory.add(node.asItem())
                                }
                            }
                            2 -> player.unlock().also { return true }
                        }
                        return false
                    }
                }
            )
            return@on true
        }
    }
}

@Initializable
class NetLootDialogue(player: Player? = null): DialoguePlugin(player){
    var session: FishingTrawlerSession? = null
    var rolls = 0
    override fun newInstance(player: Player?): DialoguePlugin {
        return NetLootDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        session = player.getAttribute("ft-session",null)
        if(session == null) return false
        rolls = ceil(session!!.fishAmount / session!!.players.size.toDouble()).toInt()
        player.dialogueInterpreter.sendOptions("Skip Junk Items?","Yes","No")
        stage = 0
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        val level = player.skills.getLevel(Skills.FISHING)
        when(buttonId){
            1 -> TrawlerLoot.getLoot(level, rolls, true).forEach {
                if(!player.bank.add(it)) GroundItemManager.create(it,player)
            }
            2 -> TrawlerLoot.getLoot(level, rolls, false).forEach {
                if(!player.bank.add(it)) GroundItemManager.create(it,player)
            }
        }
        player.sendMessage(colorize("%RYour reward has been sent to your bank."))
        player.skills.addExperience(Skills.FISHING,(((0.015 * player.skills.getLevel(Skills.FISHING))) * player.skills.getLevel(Skills.FISHING)) * rolls)
        player.removeAttribute("ft-session")
        end()
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(18237582)
    }

}

@Initializable
class NetRepairDialogue(player: Player? = null) : DialoguePlugin(player){
    var session: FishingTrawlerSession? = null
    override fun newInstance(player: Player?): DialoguePlugin {
        return NetRepairDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        session  = player.getAttribute("ft-session",null)
        if(session!!.netRipped){
            player.dialogueInterpreter.sendDialogue("The net is ripped and needs repair.")
            stage = 10
        } else {
            player.dialogueInterpreter.sendDialogue("The net is in perfect condition")
            stage = 0
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        session ?: return false
        when(stage++){
            0 -> end()
            10 -> player.dialogueInterpreter.sendOptions("Repair the net?","Yes","No")
            11 -> when(buttonId){
                1 -> {
                    end()
                    session!!.repairNet(player)
                }
                else -> {}
            }
            12 -> end()
        }

        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(18237583)
    }

}
