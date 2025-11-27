package content.global.ame.events.sandwichlady

import core.game.node.entity.npc.NPC
import core.tools.RandomFunction
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import content.global.ame.RandomEventNPC
import core.api.lock
import core.api.utils.WeightBasedTable

class SandwichLadyRENPC(override var loot: WeightBasedTable? = null) : RandomEventNPC(NPCs.SANDWICH_LADY_3117) {
    lateinit var phrases: Array<String>
    var assigned_item = 0
    val items = arrayOf(Items.BAGUETTE_6961, Items.TRIANGLE_SANDWICH_6962, Items.SQUARE_SANDWICH_6965, Items.ROLL_6963, Items.MEAT_PIE_2327, Items.KEBAB_1971, Items.CHOCOLATE_BAR_1973)

    override fun tick() {
        sayLine(this, phrases, true, true)
        if (ticksLeft == 2) {
            lock(player, 2)
        }
        super.tick()
    }

    override fun init() {
        super.init()
        phrases = arrayOf(
            // https://www.youtube.com/watch?v=ek8r3ZS929E
            // She always starts with "Sandwiches, ${player.username}!" but she ALSO picks that at random, hence duplicate it with hasOpeningPhrase = true
            "Sandwiches, ${player.username}!",
            "Sandwiches, ${player.username}!",
            "Come on ${player.username}, I made these specially!!",
            "All types of sandwiches, ${player.username}.",
            "Did you hear me ${player.username}?",
            "You think I made these just for fun?!!?",
            "How could you ignore me like this, ${player.username}?!" //unknown if authentic but it was already here
        )
        assignItem()
    }

    override fun onTimeUp() {
        noteAndTeleport()
        terminate()
    }

    fun assignItem(){
        assigned_item = items.random()
        player.setAttribute("sandwich-lady:item", assigned_item)
    }

    override fun talkTo(npc: NPC) {
        player.dialogueInterpreter.open(SandwichLadyDialogue(false), npc)
    }
}
