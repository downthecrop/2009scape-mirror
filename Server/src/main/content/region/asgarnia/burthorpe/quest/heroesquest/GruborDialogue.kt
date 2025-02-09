package content.region.asgarnia.burthorpe.quest.heroesquest

import content.data.Quests
import core.api.*
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

@Initializable
class GruborDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player): DialoguePlugin {
        return GruborDialogue(player)
    }
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, GruborDialogueFile(), npc)
        return false
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GRUBOR_789)
    }
}

class GruborDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {

        b.onPredicate { player ->
            getQuestStage(player, Quests.HEROES_QUEST) >= 2 &&
                getAttribute(player, HeroesQuest.attributeGruborLetsYouIn, false) &&
                HeroesQuest.isBlackArm(player)
        }
                .playerl("Hi.")
                .npcl("Hi, I'm a little busy right now.")
                .end()

        b.onPredicate { player ->
            getQuestStage(player, Quests.HEROES_QUEST) >= 2 &&
                !getAttribute(player, HeroesQuest.attributeGruborLetsYouIn, false) &&
                HeroesQuest.isBlackArm(player)
        }
                .npcl(FacialExpression.THINKING, "Yes? What do you want?")
                .options()
                .let { optionBuilder ->

                    optionBuilder.option_playerl("Rabbit's foot.")
                            .npcl("Eh? What are you on about? Go away!")
                            .end()

                    optionBuilder.option_playerl("Four leaved clover.")
                            .npcl("Oh you're one of the gang are you? Ok, hold up a second, I'll just let you in through here.")
                            .linel("You hear the door being unbarred from inside.")
                            .endWith { _, player ->
                                setAttribute(player, HeroesQuest.attributeGruborLetsYouIn, true)
                            }

                    optionBuilder.option_playerl("Lucky horseshoe.")
                            .npcl("Eh? What are you on about? Go away!")
                            .end()

                    optionBuilder.option_playerl("Black cat.")
                            .npcl("Eh? What are you on about? Go away!")
                            .end()
                }


        b.onPredicate { _ -> true }
                .npcl(FacialExpression.THINKING, "Yes? What do you want?")
                .options()
                .let { optionBuilder ->

                    optionBuilder.option_playerl("Would you like your hedges trimming?")
                            .npcl("Eh? Don't be daft! We don't even HAVE any hedges!")
                            .end()

                    optionBuilder.option_playerl("I want to come in.")
                            .npcl("No, go away.")
                            .end()

                    optionBuilder.option_playerl("Do you want to trade?")
                            .npcl("No, I'm busy.")
                            .end()
                }
    }
}