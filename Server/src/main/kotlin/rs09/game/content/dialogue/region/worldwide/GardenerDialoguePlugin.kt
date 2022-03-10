package rs09.game.content.dialogue.region.worldwide

import core.game.content.dialogue.DialoguePlugin
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs09.consts.Items
import rs09.game.node.entity.skill.farming.FarmerPayOptionDialogue
import rs09.game.node.entity.skill.farming.Farmers
import rs09.game.node.entity.skill.farming.FarmingPatch
import rs09.tools.END_DIALOGUE

@Initializable
class GardenerDialoguePlugin(player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player?): DialoguePlugin {
        return GardenerDialoguePlugin(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        options("Would you look after my crops for me?","Can you sell me something?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> when(buttonId){
                1 -> player("Would you look after my crops for me?").also { stage = 10 }
                2 -> player("Can you sell me something?").also { stage = 30 }
            }

            10 -> npc("I might. Which one were you thinking of?").also { stage++ }
            11 -> when(npc.id){
                Farmers.ELSTAN.id, Farmers.LYRA.id -> options("The north-western allotment.","The south-eastern allotment.").also { stage = 15 }
                Farmers.DANTAERA.id, Farmers.KRAGEN.id -> options("The north allotment.","The south allotment.").also { stage = 15 }
                else -> player("Uh, that one.").also { stage++ }
            }

            12 -> npc("Oh, right. My bad.").also { stage++ }
            13 -> checkPatch(player,Farmers.forId(npc.id)!!.patches[0])

            15 -> when(buttonId){
                1 -> checkPatch(player,Farmers.forId(npc.id)!!.patches[0])
                2 -> checkPatch(player,Farmers.forId(npc.id)!!.patches[1])
            }

            30 -> npc("That depends on whether I have it to sell.","What is it that you're looking for?").also { stage++ }
            31 -> options("Some plant cure.","A bucket of compost.","A rake.","(See more items)").also { stage = 32 }
            32 -> when(buttonId){
                1 -> player("Some plant cure.").also { stage = 100 }
                2 -> player("A bucket of compost.").also { stage = 200 }
                3 -> player("A rake.").also { stage = 300 }
                4 -> options("A watering can.","A gardening trowel.","A seed dibber.","(See previous items)").also { stage++ }
            }
            33 -> when(buttonId){
                1 -> player("A watering can.").also { stage = 400 }
                2 -> player("A gardening trowel.").also { stage = 500 }
                3 -> player("A seed dibber.").also { stage = 600 }
                4 -> options("Some plant cure.","A bucket of compost.","A rake.","(See more items)").also { stage = 32 }
            }

            100 -> npc("Plant cure, eh? I might have some put aside for myself.","Tell you what, I'll sell you some plant cure","for 25 gp if you like.").also { stage++ }
            101 -> options("Yes, that sounds like a fair price.","No thanks, I can get that much cheaper.").also { stage++ }
            102 -> when(buttonId){
                1 -> {
                    player("Yes, that sounds like a fair price.").also { stage = END_DIALOGUE }
                    if(player.inventory.remove(Item(995,25))){
                        player.inventory.add(Item(Items.PLANT_CURE_6036))
                    } else {
                        player.sendMessage("You need 25 gp to pay for that.")
                    }
                }
                2 -> end()
            }

            200 -> npc("A bucket of compost, eh? I might have one spare...","tell you what, I'll sell it to you for 35 gp if you like.").also { stage++ }
            201 -> options("Yes, that sounds fair.","No thanks, I can get that cheaper.").also { stage++ }
            202 -> when(buttonId){
                1 -> {
                    player("Yes, that sounds like a fair price.").also { stage = END_DIALOGUE }
                    if(player.inventory.remove(Item(995,35))){
                        player.inventory.add(Item(Items.COMPOST_6032))
                    } else {
                        player.sendMessage("You need 35 gp to pay for that.")
                    }
                }
                2 -> end()
            }

            300 -> npc("A rake, eh? I might have one spare...","tell you what, I'll sell it to you for 15 gp if you like.").also { stage++ }
            301 -> options("Yes, that sounds fair.","No thanks, I can get that cheaper.").also { stage++ }
            302 -> when(buttonId){
                1 -> {
                    player("Yes, that sounds like a fair price.").also { stage = END_DIALOGUE }
                    if(player.inventory.remove(Item(995,15))){
                        player.inventory.add(Item(Items.RAKE_5341))
                    } else {
                        player.sendMessage("You need 15 gp to pay for that.")
                    }
                }
                2 -> end()
            }

            400 -> npc("A watering can, eh? I might have one spare...","tell you what, I'll sell it to you for 25 gp if you like.").also { stage++ }
            401 -> options("Yes, that sounds fair.","No thanks, I can get that cheaper.").also { stage++ }
            402 -> when(buttonId){
                1 -> {
                    player("Yes, that sounds like a fair price.").also { stage = END_DIALOGUE }
                    if(player.inventory.remove(Item(995,25))){
                        player.inventory.add(Item(Items.WATERING_CAN8_5340))
                    } else {
                        player.sendMessage("You need 25 gp to pay for that.")
                    }
                }
                2 -> end()
            }

            500 -> npc("A gardening trowel, eh? I might have one spare...","tell you what, I'll sell it to you for 15 gp if you like.").also { stage++ }
            501 -> options("Yes, that sounds fair.","No thanks, I can get that cheaper.").also { stage++ }
            502 -> when(buttonId){
                1 -> {
                    player("Yes, that sounds like a fair price.").also { stage = END_DIALOGUE }
                    if(player.inventory.remove(Item(995,15))){
                        player.inventory.add(Item(Items.GARDENING_TROWEL_5325))
                    } else {
                        player.sendMessage("You need 15 gp to pay for that.")
                    }
                }
                2 -> end()
            }

            600 -> npc("A seed dibber, eh? I might have one spare...","tell you what, I'll sell it to you for 15 gp if you like.").also { stage++ }
            601 -> options("Yes, that sounds fair.","No thanks, I can get that cheaper.").also { stage++ }
            602 -> when(buttonId){
                1 -> {
                    player("Yes, that sounds like a fair price.").also { stage = END_DIALOGUE }
                    if(player.inventory.remove(Item(995,15))){
                        player.inventory.add(Item(Items.SEED_DIBBER_5343))
                    } else {
                        player.sendMessage("You need 15 gp to pay for that.")
                    }
                }
                2 -> end()
            }
        }
        return true
    }

    fun checkPatch(player: Player,fPatch: FarmingPatch){
        if(fPatch.getPatchFor(player).isWeedy()){
            npc("You don't have anything planted in that patch.","Plant something and I might agree to look after it for you.").also { stage = END_DIALOGUE }
        } else if(fPatch.getPatchFor(player).isGrown()){
            npc("That patch is already fully grown!","I don't know what you want me to do with it!").also { stage = END_DIALOGUE }
        } else if(fPatch.getPatchFor(player).protectionPaid) {
            npc("Are you alright? You've already", "paid me for that.").also { stage = END_DIALOGUE }
        } else {
            end()
            player.dialogueInterpreter.open(FarmerPayOptionDialogue(fPatch.getPatchFor(player)),npc)
        }
    }

    override fun getIds(): IntArray {
        return Farmers.values().map(Farmers::id).toIntArray()
    }

}