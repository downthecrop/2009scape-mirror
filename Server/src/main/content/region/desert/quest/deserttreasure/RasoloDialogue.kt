package content.region.desert.quest.deserttreasure

import core.api.*
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery

@Initializable
class RasoloDialogue(player: Player? = null) : DialoguePlugin(player){
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player!!, RasoloDialogueFile(), npc)
        return false
    }
    override fun newInstance(player: Player?): DialoguePlugin {
        return RasoloDialogue(player)
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.RASOLO_1972)
    }
}

class RasoloDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {

        b.onQuestStages(DesertTreasure.questName, 0,1,2,3,4,5,6,7,8)
                .npc("Greetings friend.", "I am Rasolo, the famous merchant.", "Would you care to see my wares?")
                .options()
                .let { optionBuilder ->
                    optionBuilder.option("Yes")
                            .endWith { _, player ->
                                openNpcShop(player, npc!!.id)
                            }
                    optionBuilder.option("No")
                            .playerl("No, not really.")
                            .npcl("As you wish. I will travel wherever the business takes me.")
                            .end()
                }

        b.onPredicate { player ->
            DesertTreasure.getSubStage(player, DesertTreasure.attributeShadowStage) == 0 &&
                    getQuestStage(player, DesertTreasure.questName) >= 9
        }
                .npc("Greetings friend.", "I am Rasolo, the famous merchant.", "Would you care to see my wares?")
                .options()
                .let { optionBuilder ->
                    optionBuilder.option("Yes")
                            .endWith { _, player ->
                                openNpcShop(player, npc!!.id)
                            }
                    optionBuilder.option("No")
                            .playerl("No, not really.")
                            .npcl("As you wish. I will travel wherever the business takes me.")
                            .end()

                    optionBuilder.option("Ask about the Diamonds of Azzanadra")
                            .playerl("No, actually I was looking for something specific...")
                            .npcl("Hmmmm? And what would that be?")
                            .playerl("I am looking for one of the Diamonds of Azzanadra. I have reason to believe it may be somewhere around here...")
                            .npcl("Ahhh... The Shadow Diamond...")
                            .npcl("I know the object of which you speak. It is guarded by a fearsome warrior known as Damis, they say, who lives in the shadows, invisible to prying eyes...")
                            .playerl("How can I find this 'Damis' then?")
                            .npcl("Well now... perhaps we can help each other out here.")
                            .npcl("I have in my possession a small trinket, a ring, that allows its wearer to see the unseen...")
                            .playerl("How much do you want for it?")
                            .npcl("Ah, but it is not for sale... As such...")
                            .npcl("I am offering to trade it for an item that was rightfully mine, but that was stolen by a bandit named Laheeb.")
                            .npcl("The item is question is a gilded cross, that has some sentimental value to myself. I wish for you to recover this item for me, and I will happily let you have my ring of visibility.")
                            .playerl("Where can I find this Laheeb?")
                            .npcl("Well, as a travelling merchant I have roamed these lands for many years...")
                            .npcl("To the far east of here there is an area that is dry and barren like the desert... it is called...")
                            .playerl("Al Kharid?")
                            .npcl("...Yes, Al Kharid. Now to the south of Al Kharid there is a passageway, it is called the...")
                            .playerl("Shantay Pass.")
                            .npcl("...Yes. I didn't realise you had travelled there yourself.")
                            .npcl("Anyway, when you have gone through the Shantay Pass, you will find yourself in a hostile desert... You will need to bring water with you to keep your life. Now, to the south-west of this pass, you will find a small")
                            .npcl("village...")
                            .playerl("The Bedabin camp.")
                            .npcl(FacialExpression.ANNOYED, "If you know where Laheeb lives, why did you ask me?")
                            .playerl("I don't. Sorry, please continue.")
                            .npcl("Well, okay then. Anyway, south of this encampment there is an area where few have ever been... It is a village of murderous bandits, and treacherous")
                            .npcl("thieves... This is where Laheeb makes his home.")
                            .npcl("He will have hidden his stolen treasure somewhere in that village, I am sure of it. When you find his loot, you will find my gilded cross. Return it to me, and I will reward you with my ring")
                            .npcl("of visibility, so that you may find Damis. Does this seem fair to you?")
                            .options()
                            .let { optionBuilder2 ->
                                optionBuilder2.option("Yes")
                                        .playerl("Not a problem. I'll be back with your cross before you know it.")
                                        .endWith { _, player ->
                                            if (DesertTreasure.getSubStage(player, DesertTreasure.attributeShadowStage) == 0) {
                                                DesertTreasure.setSubStage(player, DesertTreasure.attributeShadowStage, 1)
                                            }
                                        }
                                optionBuilder2.option("No")
                                        .playerl("Sounds like too much effort to me. I'll find this Damis by myself.")
                                        .npcl("As you wish.")
                                        .end()
                            }
                }

        b.onPredicate { player ->
            DesertTreasure.getSubStage(player, DesertTreasure.attributeShadowStage) in 1 .. 2 &&
                    getQuestStage(player, DesertTreasure.questName) >= 9
        }
                .npcl("Have you retrieved my gilded cross for me yet?")
                .branch { player ->
                    if (DesertTreasure.getSubStage(player, DesertTreasure.attributeShadowStage) == 2 && inInventory(player, Items.GILDED_CROSS_4674)) { 1 } else { 0 }
                }.let { branch ->
                    branch.onValue(0)
                            .playerl("No, not yet...")
                            .npcl("Well what seems to be the problem?")
                            .options()
                            .let { optionBuilder ->
                                optionBuilder.option("Where can I find Laheeb?")
                                        .playerl("Where can I find this Laheeb?")
                                        .npcl("Well, as a travelling merchant I have roamed these lands for many years...")
                                        .npcl("To the far east of here there is an area that is dry and barren like the desert... it is called...")
                                        .playerl("Al Kharid?")
                                        .npcl("...Yes, Al Kharid. Now to the south of Al Kharid there is a passageway, it is called the...")
                                        .playerl("Shantay Pass.")
                                        .npcl("...Yes. I didn't realise you had travelled there yourself.")
                                        .npcl("Anyway, when you have gone through the Shantay Pass, you will find yourself in a hostile desert... You will need to bring water with you to keep your life. Now, to the south-west of this pass, you will find a small")
                                        .npcl("village...")
                                        .playerl("The Bedabin camp.")
                                        .npcl("If you know where Laheeb lives, why did you ask me?")
                                        .playerl("I don't. Sorry, please continue.")
                                        .npcl("Well, okay then. Anyway, south of this encampment there is an area where few have ever been... It is a village of murderous bandits, and treacherous")
                                        .npcl("thieves... This is where Laheeb makes his home.")
                                        .end()
                                optionBuilder.option("Can't I just buy your ring?")
                                        .playerl("Can't I just buy your ring?")
                                        .npcl("No, it is not for sale. Some things are more important than money, and the return of my gilded cross is one of them.")
                                        .end()
                                optionBuilder.option("Is Damis near here, then?")
                                        .playerl("Is Damis near here, then?")
                                        .npcl("You would be surprised to know just how close he is...")
                                        .end()
                            }
                    branch.onValue(1)
                            .playerl("Yes I have!")
                            .npcl("Excellent, excellent. Here, take this ring. While you wear it, you will be able to see the things that live in shadows...")
                            .npcl("And you will be able to find the entrance to Damis' lair.")
                            .endWith { _, player ->
                                if (removeItem(player, Items.GILDED_CROSS_4674)) {
                                    if (DesertTreasure.getSubStage(player, DesertTreasure.attributeShadowStage) == 2) {
                                        addItemOrDrop(player, Items.RING_OF_VISIBILITY_4657)
                                        DesertTreasure.setSubStage(player, DesertTreasure.attributeShadowStage, 3)
                                    }
                                }
                            }
                }

        b.onPredicate { player ->
            DesertTreasure.getSubStage(player, DesertTreasure.attributeShadowStage) == 3 &&
                    getQuestStage(player, DesertTreasure.questName) >= 9
        }
                .npcl("So how goes your quest? Did you managed to find the Diamond you were looking for yet?")
            .branch { player ->
                if (inInventory(player, Items.RING_OF_VISIBILITY_4657)) { 1 } else { 0 }
            }.let { branch ->
                branch.onValue(1)
                    .playerl("Not yet...")
                    .npcl("Well, his lair is very close to here. I suggest you look around for it.")
                    .end()

                branch.onValue(0)
                    .playerl("I lost that ring you gave me...")
                    .npcl("Then by all means, take another. Only a foolish merchant would give away his only stock!")
                    .endWith { _, player ->
                        addItem(player, Items.RING_OF_VISIBILITY_4657)
                    }
            }


        b.onPredicate { player ->
            DesertTreasure.getSubStage(player, DesertTreasure.attributeShadowStage) > 3 &&
                    getQuestStage(player, DesertTreasure.questName) >= 9 || getQuestStage(player, DesertTreasure.questName) >= 10
        }
                .npcl("Many thanks for returning my heirloom to me, adventurer. Would you like to buy something?")
                .options()
                .let { optionBuilder ->
                    optionBuilder.option("Yes")
                            .endWith { _, player ->
                                openNpcShop(player, npc!!.id)
                            }
                    optionBuilder.option("No")
                            .playerl("No, not really.")
                            .npcl("As you wish. I will travel wherever the business takes me.")
                            .end()

                    optionBuilder.optionIf("I lost that ring you gave me...")  { player ->
                        return@optionIf !inInventory(player, Items.RING_OF_VISIBILITY_4657)
                    }
                            .playerl("I lost that ring you gave me...")
                            .npcl("Then by all means, take another. Only a foolish merchant would give away his only stock!")
                            .endWith { _, player ->
                                addItem(player, Items.RING_OF_VISIBILITY_4657)
                            }
                }

    }
}

class RasoloTradeListeners : InteractionListener {
    override fun defineListeners() {
        on(NPCs.RASOLO_1972, NPC, "trade") { player, node ->
            openNpcShop(player, node.id)
            return@on true
        }
    }
}