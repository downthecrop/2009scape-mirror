package content.region.asgarnia.burthorpe.quest.heroesquest

import content.data.Quests
import core.api.getQuestStage
import core.api.openDialogue
import core.api.setQuestStage
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

@Initializable
class CharlieTheCookDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player): DialoguePlugin {
        return CharlieTheCookDialogue(player)
    }
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, CharlieTheCookDialogueFile(), npc)
        return false
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.CHARLIE_THE_COOK_794)
    }
}
class CharlieTheCookDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        b.onPredicate { _ -> true }
                .npc(FacialExpression.ANGRY, "Hey! What are you doing back here?")
                .options()
                .let { optionBuilder ->
                    val continuePath = b.placeholder()

                    optionBuilder.optionIf("I'm looking for a gherkin...") { player -> return@optionIf getQuestStage(player,  Quests.HEROES_QUEST) >= 3 && HeroesQuest.isPhoenix(player) }
                            .playerl("I'm looking for a gherkin...")
                            .goto(continuePath)

                    optionBuilder.optionIf("I'm a fellow member of the Phoenix Gang.") { player -> return@optionIf getQuestStage(player, Quests.HEROES_QUEST) >= 3 && HeroesQuest.isPhoenix(player) }
                            .playerl("I'm a fellow member of the Phoenix Gang.")
                            .goto(continuePath)

                    optionBuilder.option_playerl("Just exploring.")
                            .npcl(FacialExpression.ANGRY, "Well, get out! This kitchen isn't for exploring. It's a private establishment! It's out of bounds to customers!")
                            .end()

                    return@let continuePath.builder()
                }
                .npcl("Ah, a fellow Phoenix! So, tell me compadre... What brings you to sunny Brimhaven?")
                .options()
                .let { optionBuilder ->
                    optionBuilder.option_playerl("Sun, sand, and the fresh sea air!")
                            .playerl("Sun, sand, and the fresh sea air!")
                            .npcl("Well, can't say I blame you, compadre. I used to be a city boy myself, but have to admit it's a lot nicer living here nowadays. Brimhaven's certainly good for it.")
                            .playerl("I also want to steal Scarface Pete's candlesticks.")
                            .npcl("Ah yes, of course. The candlesticks. Well, I have to be honest with you, compadre, we haven't made much progress in that task ourselves so far.")
                            .npcl("We can however offer a little assistance. Setting up this restaurant was the start of things; we have a secret door out the back of here that leads through the back of Cap'n Arnav's garden.")
                            .npcl("Now, at the other side of Cap'n Arnav's garden, is an old side entrance to Scarface Pete's mansion. It seems to have been blocked off from the rest of the mansion some years ago and we can't seem to find a way through.")
                            .npcl("We're positive this is the key to entering the house undetected, however, and I promise to let you know if we find anything there.")
                            .playerl("Mind if I check it out for myself?")
                            .npcl("Not at all! The more minds we have working on the problem, the quicker we get that loot!")
                            .endWith { _, player ->
                                if (getQuestStage(player, Quests.HEROES_QUEST) == 3) {
                                    setQuestStage(player, Quests.HEROES_QUEST, 4)
                                }
                            }

                    optionBuilder.option_playerl("I want to steal Scarface Pete's candlesticks.")
                            .npcl("Ah yes, of course. The candlesticks. Well, I have to be honest with you, compadre, we haven't made much progress in that task ourselves so far.")
                            .npcl("We can however offer a little assistance. Setting up this restaurant was the start of things; we have a secret door out the back of here that leads through the back of Cap'n Arnav's garden.")
                            .npcl("Now, at the other side of Cap'n Arnav's garden, is an old side entrance to Scarface Pete's mansion. It seems to have been blocked off from the rest of the mansion some years ago and we can't seem to find a way through.")
                            .npcl("We're positive this is the key to entering the house undetected, however, and I promise to let you know if we find anything there.")
                            .playerl("Mind if I check it out for myself?")
                            .npcl("Not at all! The more minds we have working on the problem, the quicker we get that loot!")
                            .endWith { _, player ->
                                if (getQuestStage(player, Quests.HEROES_QUEST) == 3) {
                                    setQuestStage(player, Quests.HEROES_QUEST, 4)
                                }
                            }
                }

        }
}