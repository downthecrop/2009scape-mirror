package content.region.asgarnia.burthorpe.quest.heroesquest

import content.data.Quests
import core.api.getQuestStage
import core.api.openDialogue
import core.api.openNpcShop
import core.api.setQuestStage
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.DialoguePlugin
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

@Initializable
class AlfonseTheWaiterDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player): DialoguePlugin {
        return AlfonseTheWaiterDialogue(player)
    }
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, AlfonseTheWaiterDialogueFile(), npc)
        return false
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ALFONSE_THE_WAITER_793)
    }
}
class AlfonseTheWaiterDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {

        b.onPredicate { _ -> true }
                .npc("Welcome to the Shrimp and Parrot.", "Would you like to order, sir?")
                .options()
                .let { optionBuilder ->
                    optionBuilder.option_playerl("Yes please.")
                            .endWith { _, player ->
                                openNpcShop(player, npc!!.id)
                            }

                    optionBuilder.option_playerl("No thank you.")
                            .end()

                    optionBuilder.optionIf("Do you sell Gherkins?"){ player -> return@optionIf getQuestStage(
                        player,
                        Quests.HEROES_QUEST
                    ) >= 2 && HeroesQuest.isPhoenix(player) }
                            .playerl("Do you sell Gherkins?")
                            .npc("Hmmmm Gherkins eh? Ask Charlie the cook, round the", "back. He may have some 'gherkins' for you!")
                            .linel("Alfonse winks at you.")
                            .endWith { _, player ->
                                if(getQuestStage(player, Quests.HEROES_QUEST) == 2) {
                                    setQuestStage(player, Quests.HEROES_QUEST, 3)
                                }
                            }

                    optionBuilder.option("Where do you get your Karambwan from?")
                            .npc("We buy directly off Lubufu, a local fisherman. He", "seems to have a monopoly over Karambwan sales.")
                            .end()

                }
     }
}