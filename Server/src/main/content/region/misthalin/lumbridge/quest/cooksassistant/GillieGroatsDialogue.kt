package content.region.misthalin.lumbridge.quest.cooksassistant

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable

/**
 * Dialogue for Gillie Groats.
 * @author Qweqker
 */

@Initializable
class GillieGroatsDialogue (player: Player? = null) : core.game.dialogue.DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        var milk = false
        if (args.size == 2) milk = true
        if (milk) { //If the player attempts to milk a dairy cow without a bucket
            npc(core.game.dialogue.FacialExpression.LAUGH, "Tee hee! You've never milked a cow before, have you?")
            stage = 100
            return true
        }
        npc(core.game.dialogue.FacialExpression.HAPPY, "Hello, I'm Gillie the Milkmaid. What can I do for you?")
        stage = 0
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {

            //If the player talks to Gillie directly
            0 -> options("Who are you?", "Can you tell me how to milk a cow?", "I'm fine, thanks.").also { stage++ }
            1 -> when(buttonId) {
                1 -> npc(core.game.dialogue.FacialExpression.HAPPY, "My name's Gillie Groats. My father is a farmer and I", "milk the cows for him.").also { stage = 10 }
                2 -> player(core.game.dialogue.FacialExpression.ASKING, "So how do you get milk from a cow then?").also { stage = 20 }
                3 -> player(core.game.dialogue.FacialExpression.NEUTRAL, "I'm fine, thanks.").also { stage = 1000 }
            }

            //Who are you?
            10 -> player(core.game.dialogue.FacialExpression.ASKING, "Do you have any buckets of milk spare?").also { stage++ }
            11 -> npc(core.game.dialogue.FacialExpression.SUSPICIOUS, "I'm afraid not. We need all of our milk to sell to", "market, but you can milk the cow yourself if you need", "milk.").also { stage++ }
            12 -> player(core.game.dialogue.FacialExpression.HAPPY,"Thanks.").also { stage = 1000 }

            //Can you tell me how to milk a cow?
            20 -> npc(core.game.dialogue.FacialExpression.FRIENDLY, "It's very easy. First you need an empty bucket to hold", "the milk.").also { stage++ }
            21 -> npc(core.game.dialogue.FacialExpression.FRIENDLY, "Then find a dairy cow to milk - you can't milk just", "any cow.").also { stage++ }
            22 -> player(core.game.dialogue.FacialExpression.ASKING, "How do I find a dairy cow?").also { stage++ }
            23 -> npc(core.game.dialogue.FacialExpression.FRIENDLY, "They are easy to spot - they are dark brown and", "white, unlike beef cows, which are light brown and white.", "We also tether them to a post to stop them wandering", "around all over the place.").also {stage++ }
            24 -> npc(core.game.dialogue.FacialExpression.FRIENDLY, "There are a couple very near, in this field.").also { stage++ }
            25 -> npc(core.game.dialogue.FacialExpression.HAPPY, "Then just milk the cow and your bucket will fill with", "tasty, nutritious milk.").also { stage = 1000 }

            //Continuation of attempting to milk a dairy cow without a bucket
            100 -> player(core.game.dialogue.FacialExpression.ASKING, "Erm... No. How could you tell?").also { stage++ }
            101 -> npc(core.game.dialogue.FacialExpression.LAUGH, "Because you're spilling milk all over the floor. What a","waste ! You need something to hold the milk.").also { stage++ }
            102 -> player(core.game.dialogue.FacialExpression.NEUTRAL, "Ah yes, I really should have guessed that one, shouldn't", "I?").also { stage++ }
            103 -> npc(core.game.dialogue.FacialExpression.LAUGH, "You're from the city, aren't you... Try it again with an","empty bucket.").also{ stage++ }
            104 -> player(core.game.dialogue.FacialExpression.NEUTRAL,"Right, I'll do that.").also { stage = 1000 }

            //Dialogue Endpoint
            1000 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return GillieGroatsDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(3807)
    }
}