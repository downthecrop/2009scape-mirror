package content.region.asgarnia.burthorpe.quest.heroesquest

import core.api.*
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs

@Initializable
class AchiettiesDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, AchiettiesDialogueFile(), npc)
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return AchiettiesDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ACHIETTIES_796)
    }
}

class AchiettiesDialogueFile : DialogueBuilderFile() {

    override fun create(b: DialogueBuilder) {

        b.onQuestStages(HeroesQuest.questName, 0,1)
                .branch { player ->
                    return@branch getQuestStage(player, HeroesQuest.questName)
                }.let{ branch ->
                    branch.onValue(0)
                            .npcl(FacialExpression.FRIENDLY, "Greetings. Welcome to the Heroes' Guild.")
                            .npcl("Only the greatest heroes of this land may gain entrance to this guild.")
                            // - If the player's skill levels are lower than the quest requirements. (I think this is after 2009)
                            // linel("Before starting this quest, be aware that one or more of your skill levels are lower than what is required to fully complete it.")
                            .options()
                            .let { optionBuilder ->
                                optionBuilder.option("I'm a hero, may I apply to join?")
                                        .playerl("I'm a hero. May I apply to join?")
                                        .branch { player ->
                                            return@branch if (HeroesQuest.hasRequirements(player)) {
                                                1
                                            } else {
                                                0
                                            }
                                        }.let { branch ->
                                            branch.onValue(0)
                                                    .npcl("You're a hero? I've never heard of YOU. You are required to possess at least 55 quest points to file an application.")
                                                    .npcl("Additionally you must have completed the Shield of Arrav, Lost City, Merlin's Crystal and Dragon Slayer quests.")
                                                    .end()
                                            return@let branch
                                        }.onValue(1)
                                        .betweenStage { df, player, _, _ ->
                                            if(getQuestStage(player, HeroesQuest.questName) == 0) {
                                                setQuestStage(player, HeroesQuest.questName, 1)
                                            }
                                        }
                                        .npcl("Well you seem to meet our initial requirements, so you may now begin the tasks to earn membership in the Heroes' Guild.")
                                        .npcl("The three items required for entrance are: An Entranan Firebird feather, a Master Thieves' armband, and a cooked Lava Eel.")
                                        .options()
                                        .let { optionBuilder2 ->
                                            optionBuilder2.option_playerl("Any hints on getting the thieves armband?")
                                                    .npcl("I'm sure you have the relevant contacts to find out about that.")
                                                    .end()
                                            optionBuilder2.option_playerl("Any hints on getting the feather?")
                                                    .npcl("Not really - other than Entranan firebirds tend to live on Entrana.")
                                                    .end()
                                            optionBuilder2.option_playerl("Any hints on getting the eel?")
                                                    .npcl("Maybe go and find someone who knows a lot about fishing?")
                                                    .end()
                                            optionBuilder2.option_playerl("I'll start looking for all those things then.")
                                                    .npcl("Good luck with that.")
                                                    .end()
                                        }

                                optionBuilder.option_playerl("Good for the foremost heroes of the land.")
                                        .npcl("Yes. Yes it is.")
                                        .end()
                            }
                    branch.onValue(1)
                            .npcl("Greetings. Welcome to the Heroes' Guild.")
                            .npcl("How goes thy quest adventurer?")
                            .playerl("It's tough. I've not done it yet.")
                            .npcl("Remember, the items you need to enter are:")
                            .npcl("An Entranan Firebirds' feather, A Master Thieves armband, and a cooked Lava Eel.")
                            .options()
                            .let { optionBuilder2 ->
                                optionBuilder2.option_playerl("Any hints on getting the thieves armband?")
                                        .npcl("I'm sure you have the relevant contacts to find out about that.")
                                        .end()
                                optionBuilder2.option_playerl("Any hints on getting the feather?")
                                        .npcl("Not really - other than Entranan firebirds tend to live on Entrana.")
                                        .end()
                                optionBuilder2.option_playerl("Any hints on getting the eel?")
                                        .npcl("Maybe go and find someone who knows a lot about fishing?")
                                        .end()
                                optionBuilder2.option_playerl("I'll start looking for all those things then.")
                                        .npcl("Good luck with that.")
                                        .end()
                            }
                }

        b.onQuestStages(HeroesQuest.questName, 2,3,4)
                .npcl("Greetings. Welcome to the Heroes' Guild.")
                .npcl("How goes thy quest adventurer?")
                .playerl("It's tough. I've not done it yet.")
                .npcl("Remember, the items you need to enter are:")
                .npcl("An Entranan Firebirds' feather, A Master Thieves armband, and a cooked Lava Eel.")
                .options()
                .let { optionBuilder2 ->
                    optionBuilder2.option_playerl("Any hints on getting the thieves armband?")
                            .npcl("I'm sure you have the relevant contacts to find out about that.")
                            .end()
                    optionBuilder2.option_playerl("Any hints on getting the feather?")
                            .npcl("Not really - other than Entranan firebirds tend to live on Entrana.")
                            .end()
                    optionBuilder2.option_playerl("Any hints on getting the eel?")
                            .npcl("Maybe go and find someone who knows a lot about fishing?")
                            .end()
                    optionBuilder2.option_playerl("I'll start looking for all those things then.")
                            .npcl("Good luck with that.")
                            .end()
                }

        b.onQuestStages(HeroesQuest.questName, 6)
                .npcl("Greetings. Welcome to the Heroes' Guild.")
                .npcl("How goes thy quest adventurer?")
                .branch { player ->
                    return@branch if (HeroesQuest.allItemsInInventory(player)) { 1 } else { 0 }
                }.let { branch ->
                    branch.onValue(0)
                            .playerl("It's tough. I've not done it yet.")
                            .npcl("Remember, the items you need to enter are:")
                            .npcl("An Entranan Firebirds' feather, A Master Thieves armband, and a cooked Lava Eel.")
                            .options()
                            .let { optionBuilder2 ->
                                optionBuilder2.option_playerl("Any hints on getting the thieves armband?")
                                        .npcl("I'm sure you have the relevant contacts to find out about that.")
                                        .end()
                                optionBuilder2.option_playerl("Any hints on getting the feather?")
                                        .npcl("Not really - other than Entranan firebirds tend to live on Entrana.")
                                        .end()
                                optionBuilder2.option_playerl("Any hints on getting the eel?")
                                        .npcl("Maybe go and find someone who knows a lot about fishing?")
                                        .end()
                                optionBuilder2.option_playerl("I'll start looking for all those things then.")
                                        .npcl("Good luck with that.")
                                        .end()
                            }

                    branch.onValue(1)
                            .playerl("I have all the required items.")
                            .npcl("I see that you have. Well done. Now, to complete the quest, and gain entry to the Heroes' Guild in your final task all that you have to do is...")
                            .playerl("W-what? What do you mean? There's MORE?")
                            .npcl("I'm sorry, I was just having a little fun with you. Just a little Heroes' Guild humour there. What I really meant was")
                            .npcl("Congratulations! You have completed the Heroes' Guild entry requirements! You will find the door now open for you! Enter, Hero! And take this reward!")
                            .endWith { _, player ->
                                if (HeroesQuest.allItemsInInventory(player)) {
                                    removeItem(player, Items.FIRE_FEATHER_1583)
                                    removeItem(player, Items.LAVA_EEL_2149)
                                    removeItem(player, Items.THIEVES_ARMBAND_1579)
                                    if (getQuestStage(player, HeroesQuest.questName) == 6) {
                                        finishQuest(player, HeroesQuest.questName)
                                    }
                                }
                            }
                }

        b.onQuestStages(HeroesQuest.questName, 100)
                .npcl("Greetings. Welcome to the Heroes' Guild.")
    }
}