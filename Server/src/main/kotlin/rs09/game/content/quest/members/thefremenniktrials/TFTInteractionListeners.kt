package rs09.game.content.quest.members.thefremenniktrials

import core.game.content.dialogue.FacialExpression
import core.game.node.`object`.Scenery
import core.game.node.entity.impl.Animator
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.gather.SkillingTool
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.primextends.getNext
import org.rs09.primextends.isLast
import rs09.game.interaction.InteractionListener
import rs09.game.world.GameWorld
import rs09.game.world.GameWorld.Pulser

class TFTInteractionListeners : InteractionListener(){

    val BEER = Items.BEER_1917
    val WORKER = NPCs.COUNCIL_WORKMAN_1287
    val FISH_ALTAR = 4141
    val LYRE = Items.LYRE_3689
    val FISH = intArrayOf(Items.RAW_SHARK_383,Items.RAW_SEA_TURTLE_395,Items.RAW_MANTA_RAY_389)
    val LOW_ALC_KEG = Items.LOW_ALCOHOL_KEG_3712
    val KEG = Items.KEG_OF_BEER_3711
    val TINDERBOX = Items.TINDERBOX_590
    val CHERRY_BOMB = Items.STRANGE_OBJECT_3713
    val LIT_BOMB = Items.LIT_STRANGE_OBJECT_3714
    val PIPE = 4162
    val PORTALIDs = intArrayOf(2273,2274,2506,2507,2505,2503,2504,5138)
    val SWENSEN_LADDER = 4158
    val SWAYING_TREE = 4142
    val KNIFE = Items.KNIFE_946
    val TREE_BRANCH = Items.BRANCH_3692
    val LYRE_IDs = intArrayOf(14591,14590,6127,6126,6125,3691,3690)


    override fun defineListeners() {
        onUseWith(NPC,BEER,WORKER){ player, _, _ ->
            player.dialogueInterpreter.open(CouncilWorkerFTDialogue(0,true), NPC(WORKER))
            return@onUseWith true
        }

        onUseWith(SCENERY,FISH_ALTAR,*FISH){ player, _, fish ->
            if(player.inventory.contains(LYRE,1)) {
                Pulser.submit(spiritPulse(player, fish.id))
            } else {
                player.sendMessage("I should probably have my lyre with me.")
            }
            return@onUseWith true
        }

        onUseWith(ITEM,LOW_ALC_KEG,KEG){player,lowKeg,_ ->
            if(!player.getAttribute("fremtrials:keg-mixed",false)!!){
                if(player.getAttribute("fremtrials:cherrybomb",false)) {
                    player.inventory?.remove(lowKeg.asItem())
                    player.setAttribute("/save:fremtrials:keg-mixed", true)
                    player.sendMessage("The cherry bomb in the pipe goes off.")
                    RegionManager.getLocalEntitys(player).stream().forEach { e -> e.sendChat("What was THAT??") }
                    player.sendMessage("You mix the kegs together.")
                    return@onUseWith true
                } else {
                    player.dialogueInterpreter?.sendDialogue("I can't do this right now. I should create","a distraction.")
                    return@onUseWith true
                }
            } else return@onUseWith false
        }

        onUseWith(ITEM,TINDERBOX,CHERRY_BOMB){player,_,bomb ->
            player.inventory.remove(bomb.asItem())
            player.inventory.add(Item(LIT_BOMB))
            player.sendMessage("You light the strange object.")
            return@onUseWith true
        }

        onUseWith(ITEM,KNIFE,TREE_BRANCH){player,knife,_ ->
            if (player.inventory.containsItem(knife.asItem())) {
                GameWorld.submit(BranchFletchingPulse(player))
            } else {
                player.sendMessage("You need a knife to do this.")
            }
            return@onUseWith true
        }

        on(LYRE_IDs,ITEM,"play"){player,lyre ->
            if(player.questRepository?.getStage("Fremennik Trials")!! < 20){
                player.sendMessage("You lack the knowledge to play this.")
                return@on true
            }
            if(LYRE_IDs.isLast(lyre.id)){
                player.sendMessage("Your lyre is out of charges!")
            } else {
                player.inventory?.remove(lyre.asItem())
                player.inventory?.add(Item(LYRE_IDs.getNext(lyre.id)))
                player.lock()
                Pulser.submit(telePulse(player))
            }
            return@on true
        }

        on(PIPE,SCENERY,"put-inside"){ player, _ ->
            val bombItem = Item(LIT_BOMB)
            if(player.inventory.containsItem(bombItem)){
                player.sendMessage("You stuff the lit object into the pipe.")
                player.setAttribute("/save:fremtrials:cherrybomb",true)
                player.inventory.remove(bombItem)
            } else {
                player.sendMessage("What am I supposed to put in there? A shoe?")
            }
            return@on true
        }

        on(PORTALIDs,SCENERY,"use"){ player, portal ->
            val toLocation =
            when(portal.id){
                2273 -> destRoom(2639, 10012, 2645, 10018).getCenter()
                2274 -> destRoom(2650, 10034, 2656, 10040).getCenter()
                2506 -> destRoom(2662, 10023, 2669, 10029).getCenter()
                2507 -> destRoom(2626, 10023, 2633, 10029).getCenter()
                2505 -> destRoom(2650, 10001, 2656, 10007).getCenter()
                2503 -> destRoom(2662, 10012, 2668, 10018).getCenter()
                2504 -> {player.setAttribute("/save:fremtrials:maze-complete",true); destRoom(2662, 10034, 2668, 10039).getCenter()}
                else -> getRandomLocation(player)
            }
            player.properties?.teleportLocation = toLocation
            return@on true;
        }

        on(SWENSEN_LADDER,SCENERY,"climb"){ player, _ ->
            if(player.getAttribute("fremtrials:swensen-accepted",false) == false){
                player.dialogueInterpreter?.sendDialogues(1283,FacialExpression.ANGRY,"Where do you think you're going?")
                return@on true
            }
            return@on true
        }

        on(SWAYING_TREE,SCENERY,"chop down"){ player, _ ->
            SkillingTool.getHatchet(player)?.let { Pulser.submit(ChoppingPulse(player)).also { return@on true } }

            player.sendMessage("You need an axe which you have the woodcutting level to use to do this.")
            return@on true
        }
    }

    class destRoom(val swx: Int, val swy: Int, val nex: Int, val ney: Int)

    fun destRoom.getCenter(): Location {
        return Location((swx + nex) / 2, (swy + ney) / 2).transform(1,0,0)
    }

    fun getRandomLocation(player: Player?): Location{
        var obj: Scenery? = null

        while(obj?.id != 5138) {
            val objects = player?.viewport?.chunks?.random()?.random()?.objects
            obj = objects?.random()?.random()
            if(obj == null || obj.location?.equals(Location(0,0,0))!!){
                continue
            }
        }
        return obj.location
    }

    class spiritPulse(val player: Player?, val fish: Int) : Pulse(){
        var counter = 0
        val npc = NPC(1273,player?.location)
        override fun pulse(): Boolean {
            when(counter++){
                0 -> npc.init().also { player?.lock() }.also { player?.inventory?.remove(Item(fish)) }
                1 -> npc.moveStep()
                2 -> npc.face(player).also { player?.face(npc) }
                3 -> player?.dialogueInterpreter?.sendDialogues(npc,
                    FacialExpression.HAPPY,"I will kindly accept this offering, and","bestow upon you a gift in return.")
                4 -> player?.inventory?.remove(Item(3689))
                5 -> when(fish){
                    383 -> player?.inventory?.add(Item(6125))
                    389 -> player?.inventory?.add(Item(6126))
                    395 -> player?.inventory?.add(Item(6127))
                }
                6 -> player?.unlock()
                10 -> npc.clear().also { return true }
            }
            return false
        }
    }

    class ChoppingPulse(val player: Player) : Pulse() {
        var counter = 0
        override fun pulse(): Boolean {
            when(counter++){
                0 -> player.animator.animate(SkillingTool.getHatchet(player).animation)
                4 -> player.animator.reset().also { player.inventory.add(Item(3692));return true}
            }
            return false
        }
    }

    class BranchFletchingPulse(val player: Player?) : Pulse(){
        var counter = 0
        override fun pulse(): Boolean {
            when(counter++){
                0 -> player?.animator?.animate(Animation(1248)).also { player!!.lock() }
                3 -> player?.inventory?.remove(Item(Items.BRANCH_3692)).also { player?.inventory?.add(Item(Items.UNSTRUNG_LYRE_3688)); player!!.unlock(); return true}
            }
            return false
        }
    }

    class telePulse(val player: Player) : Pulse(){
        var counter = 0
        override fun pulse(): Boolean {
            when(counter++){
                0 -> player.animator.animate(Animation(9600, Animator.Priority.VERY_HIGH), Graphics(1682))
                6 -> player.properties.teleportLocation = Location.create(2661, 3646, 0)
                7 -> player.unlock().also { return true }
            }
            return false
        }
    }
}