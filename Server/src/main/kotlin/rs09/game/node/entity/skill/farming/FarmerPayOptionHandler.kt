package rs09.game.node.entity.skill.farming

import core.cache.def.impl.NPCDefinition
import core.game.content.dialogue.DialoguePlugin
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin

@Initializable
class FarmerPayOptionHandler : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        NPCDefinition.setOptionHandler("pay",this)
        NPCDefinition.setOptionHandler("pay (north)",this)
        NPCDefinition.setOptionHandler("pay (south)",this)
        NPCDefinition.setOptionHandler("pay (north-west)",this)
        NPCDefinition.setOptionHandler("pay (south-east)",this)
        return this
    }

    override fun handle(player: Player?, node: Node?, option: String?): Boolean {
        player ?: return false
        node ?: return false
        val farmer = Farmers.forId(node.id)

        if(farmer == null){
            player.sendMessage("This shouldn't be happening. Report this.")
            return true
        }

        val fPatch = when(option){
            "pay" -> farmer.patches[0]
            "pay (north)","pay (north-west)" -> farmer.patches[0]
            "pay (south)","pay (south-east)" -> farmer.patches[1]
            else -> farmer.patches[0]
        }

        val patch = fPatch.getPatchFor(player)
        if(patch.plantable == null){
            player.dialogueInterpreter.sendDialogue("I have nothing to protect in that patch.")
            return true
        }

        if(patch.protectionPaid){
            player.dialogueInterpreter.sendDialogue("I have already paid to protect that patch.")
            return true
        }

        player.dialogueInterpreter.open(FarmerPayDialogue.KEY,node.asNpc(),patch)
        return true
    }

    @Initializable
    class FarmerPayDialogue(player: Player? = null) : DialoguePlugin(player){
        var patch: Patch? = null
        var item: Item? = null

        companion object {
            @JvmField
            val KEY = 1256743
        }

        override fun newInstance(player: Player?): DialoguePlugin {
            return FarmerPayDialogue(player)
        }

        override fun open(vararg args: Any?): Boolean {
            npc = (args[0] as NPC).getShownNPC(player)
            patch = (args[1] as Patch)
            if(patch == null){
                npc("Hello.")
                stage = 1000
                return true
            } else {
                item = patch?.plantable?.protectionItem
                if(item == null) npc("Sorry, I won't protect that.").also { stage = 1000 }
                else{
                    val name = item?.name?.toLowerCase()
                    npc("I would like ${item?.amount} $name","to protect that patch.")
                    stage = 0
                }
            }
            return true
        }

        override fun handle(interfaceId: Int, buttonId: Int): Boolean {
            when(stage){
                0 -> options("Sure!","No, thanks.").also { stage++ }
                1 -> when(buttonId){
                    1 -> player("Sure!").also { stage++ }
                    2 -> player("No, thanks.").also { stage = 1000 }
                }
                2 -> {
                    if(player.inventory.containsItem(item)){
                        player("Here you go.").also { stage = 10 }
                        player.inventory.remove()
                    } else {
                        item = Item(item!!.noteChange,item!!.amount)
                        if(player.inventory.containsItem(item)){
                            player("Here you go.").also { stage = 10 }
                        } else {
                            player("I don't have that to give.").also { stage = 20 }
                        }
                    }
                }

                10 -> {
                    if(player.inventory.remove(item)){
                        npc("Thank you! I'll keep an eye on this patch.").also { stage = 1000 }
                        patch?.protectionPaid = true
                    } else {
                        npc("That stuff just... vanished....").also { stage = 1000 }
                    }
                }

                20 -> {
                    npc("Come back when you do.")
                    stage = 1000
                }

                1000 -> end()
            }
            return true
        }

        override fun getIds(): IntArray {
            return intArrayOf(KEY)
        }

    }

}