package content.region.kandarin.witchhaven.dialogue

import core.api.openDialogue
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

@Initializable
class WitchavenVillagerDialogue(player: Player? = null) : DialoguePlugin(player){
    override fun newInstance(player: Player): DialoguePlugin {
        return WitchavenVillagerDialogue(player)
    }
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        // Fallback to default. Always the start of Sea Slug
        openDialogue(player!!, WitchavenVillagerDialogueFile(), npc)
        return true
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.WITCHAVEN_VILLAGER_4883, NPCs.WITCHAVEN_VILLAGER_4884,
                NPCs.WITCHAVEN_VILLAGER_4885, NPCs.WITCHAVEN_VILLAGER_4886,
                NPCs.WITCHAVEN_VILLAGER_4887, NPCs.WITCHAVEN_VILLAGER_4888)
    }
}

class WitchavenVillagerDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        b.onPredicate { _ -> true }
                .player(FacialExpression.FRIENDLY, "Hello there.")
                .branch { player ->
                    return@branch (0 .. 4).random()
                }.let { branch ->
                    branch.onValue(0)
                            .npcl("What have you got to be so cheerful about?")
                            .playerl("Well, it's another nice day.")
                            .npcl("Ha! Try worrying about how you feed a family with no job. Then tell me how nice the day is!")
                            .playerl("Okay, I guess I've caught you at a bad time. Goodbye.")
                            .end()

                    branch.onValue(1)
                            .npcl("Hmm? Oh, hello there.")
                            .playerl("Are you okay? You seem a bit preoccupied.")
                            .npcl("It's nothing stranger. No need to concern yourself.")
                            .end()

                    branch.onValue(2)
                            .npcl("Spare a coin mister?")
                            .playerl("What do you need it for?")
                            .npcl("For a poor unemployed fisherman what needs to eat.")
                            .playerl("Why don't you just fish for some food?")
                            .npcl("Err... Goodbye mister.")
                            .end()

                    branch.onValue(3)
                            .npcl("Can you believe they did this to us?")
                            .playerl("Wha...")
                            .npcl("I mean, what did they think would happen?")
                            .playerl("Who...")
                            .npcl("Building that whacking great Fishing Platform just off the coast.")
                            .playerl("Fish...")
                            .npcl("Dratted thing stole all of our trade.")
                            .playerl("Excuse...")
                            .npcl("I'm sorry, I'm too angry to speak right now. Goodbye")
                            .end()

                    branch.onValue(4)
                            .npcl("With our nets and gear we're faring,")
                            .npcl("On the wild and wasteful ocean,")
                            .npcl("It's there on the deep that we harvest and reap our bread,")
                            .npcl("As we hunt the bonny shoals of herring.")
                            .playerl("That's a lovely song.")
                            .npcl("Aye lad, and sing it every day we did.")
                            .npcl("'Till the Fishing Platform came and ruined everything.")
                            .playerl("Oh, I'm sorry.")
                            .npcl("No need lad, it not be your fault.")
                            .end()
                }

    }
}
