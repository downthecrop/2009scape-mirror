package rs09.game.content.dialogue.region.craftingguild

import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.world.map.Location
import rs09.game.content.activity.guild.crafting.CraftingGuildListeners
import rs09.game.content.dialogue.DialogueFile
import rs09.tools.END_DIALOGUE

/**
 * @author bushtail
 */

class TheDoorDialogues(val it: Int) : DialogueFile() {
    var i = CraftingGuildListeners()
    var n = NPC(i.master)
    var l = Location.create(2933,3289,0)

    override fun handle(interfaceId: Int, buttonId: Int) {
        npc = n
        when(it) {
            0 -> when(stage) {
                0 -> npcl(FacialExpression.DISAGREE, "Sorry, only experienced crafters are allowed in here. You must be level 40 or above to enter.").also{ stage = END_DIALOGUE}
            }

            1 -> when(stage) {
                0 -> npcl(FacialExpression.DISAGREE, "Where's your brown apron? You can't come in here unless you're wearing one.").also{ stage = END_DIALOGUE }
            }

            2 -> when(stage) {
                0 -> if(player!!.location == l) {
                    npcl(FacialExpression.FRIENDLY, "Welcome to the Guild of Master Craftsmen.").also{ stage = END_DIALOGUE }
                }
            }
        }
    }
}