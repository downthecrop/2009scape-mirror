package content.region.desert.quest.deserttreasure

import core.api.*
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs

@Initializable
class BartenderDialogue(player: Player? = null) : DialoguePlugin(player){
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player!!, BartenderDialogueFile(), npc)
        return false
    }
    override fun newInstance(player: Player?): DialoguePlugin {
        return BartenderDialogue(player)
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BARTENDER_1921)
    }
}
class BartenderDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {

        b.onQuestStages(DesertTreasure.questName, 0,1,2,3,4)
                .npcl(FacialExpression.ANNOYED, "Get out of here. I have nothing to say to the likes of you.")
                .end()

        b.onQuestStages(DesertTreasure.questName, 5)
                .branch { player ->
                    return@branch if (getAttribute(player, DesertTreasure.attributeBoughtBeer, false)) { 1 } else { 0 }
                }.let{ branch ->
                    branch.onValue(1)
                            .npcl("You've had your drink, now get out of here. I have nothing to say to the-")
                            .playerl("No, Wait! Look, I'm here on an archaeological expedition for the Museum of Varrock.")
                            .playerl("I am only here looking for artefacts...")
                            .npcl("Oh really? Our inheritance is only sand and death. What do you expect to find out here in this desert forsaken by the gods?")
                            .options()
                            .let { optionBuilder ->
                                optionBuilder.option("I heard about treasure...")
                                        .playerl("As I understand it there's some kind of hidden treasure in these parts...")
                                        .npcl("Look around @g[pal,lady]. Does it looks like there's any treasure near here?")
                                        .npcl("If I were you I'd get lost before someone takes a dislike to your face and removes it for you.")
                                        .end()

                                optionBuilder.option("I heard about four diamonds...")
                                        .playerl("I heard a rumour about four diamonds or crystals...")
                                        .npcl("The four diamonds of Azzanadra??? How came you to know of this?")
                                        .playerl("You've heard of them then?")
                                        .npcl("It's just a fairy tale for children. Maybe one of the village elders might know more, but it's not really something I care about.")
                                        .npcl("Now get out of here, your sort isn't welcome in my bar.")
                                        .endWith { _, player ->
                                            if(getQuestStage(player, DesertTreasure.questName) == 5) {
                                                setQuestStage(player, DesertTreasure.questName, 6)
                                            }
                                        }

                                optionBuilder.option("I heard about a fortress...")
                                        .playerl("Certain things have led me to believe there may be some kind of ruined fortress around here...")
                                        .npcl("Doubt it. What in the world would someone need to guard against in the middle of this desert?")
                                        .npcl("A bad attack of sand? I think you're on the wrong track, mister so-called archaeologist.")
                                        .end()
                            }

                    branch.onValue(0)
                            .npcl("If you're not buying anything, I have nothing to say to you.")
                            .options()
                            .let { optionBuilder ->
                                optionBuilder.option("Ask about Desert Treasure")
                                        .playerl("As I understand it there's some kind of hidden treasure in these parts...")
                                        .npcl("Look around @g[pal,lady]. Does it looks like there's any treasure near here?")
                                        .npcl("If I were you I'd get lost before someone takes a dislike to your face and removes it for you.")
                                        .end()
                                optionBuilder.option("Buy a drink")
                                        .branch { player ->
                                            return@branch if (inInventory(player, Items.COINS_995, 650)) { 1 } else { 0 }
                                        }.let { branch ->
                                            branch.onValue(1)
                                                    .npcl("What's that? You wanna buy a beer? It'll cost ya 650 coins.")
                                                    .options()
                                                    .let { optionBuilder ->
                                                        optionBuilder.option("Buy a beer")
                                                                .betweenStage { _, player, _, _ ->
                                                                    if (removeItem(player, Item(Items.COINS_995, 650))) {
                                                                        addItemOrDrop(player, Items.BANDITS_BREW_4627)
                                                                        setAttribute(player, DesertTreasure.attributeBoughtBeer, true)
                                                                    }
                                                                }
                                                                .npcl("There you go. Now get out, we don't like your sort around here.")
                                                                .end()
                                                        optionBuilder.option("Don't buy anything")
                                                                .npcl("Get out of my bar then! We don't like your sort round here!")
                                                                .end()
                                                    }

                                            branch.onValue(0)
                                                    .npcl("You ain't got the 650 coins it costs to buy it, and I'm glad 'cos I didn't want to serve you anyway.")
                                                    .end()
                                        }

                            }
                }


        b.onQuestStages(DesertTreasure.questName, 6,7,8,9,10,11)
                .branch { player ->
                    return@branch if (getAttribute(player, DesertTreasure.attributeBoughtBeer, false)) { 1 } else { 0 }
                }.let { branch ->
                    branch.onValue(1)
                            .npcl("You've had your drink, now get out of here. I have nothing to say to the-")
                            .playerl("No, Wait! Look, I'm here on an archaeological expedition for the Museum of Varrock.")
                            .playerl("I am only here looking for artefacts...")
                            .npcl("Oh really? Our inheritance is only sand and death. What do you expect to find out here in this desert forsaken by the gods?")
                            .options()
                            .let { optionBuilder ->
                                optionBuilder.option("I heard about treasure...")
                                        .playerl("As I understand it there's some kind of hidden treasure in these parts...")
                                        .npcl("Look around @g[pal,lady]. Does it looks like there's any treasure near here?")
                                        .npcl("If I were you I'd get lost before someone takes a dislike to your face and removes it for you.")
                                        .end()

                                optionBuilder.option("I heard about four diamonds...")
                                        .playerl("I heard a rumour about four diamonds or crystals...")
                                        .npcl("The four diamonds of Azzanadra??? How came you to know of this?")
                                        .playerl("You've heard of them then?")
                                        .npcl("It's just a fairy tale for children. Maybe one of the village elders might know more, but it's not really something I care about.")
                                        .npcl("Now get out of here, your sort isn't welcome in my bar.")
                                        .end()

                                optionBuilder.option("I heard about a fortress...")
                                        .playerl("Certain things have led me to believe there may be some kind of ruined fortress around here...")
                                        .npcl("Doubt it. What in the world would someone need to guard against in the middle of this desert?")
                                        .npcl("A bad attack of sand? I think you're on the wrong track, mister so-called archaeologist.")
                                        .end()

                                optionBuilder.option("I heard of the Diamonds of Azzanadra.")
                                        .playerl("Tell me all you know about the Diamonds of Azzanadra.")
                                        .npcl("Not that I think it's any of your business, but when I was a child I remember hearing the legend.")
                                        .npcl("I don't recall it particularly well, other than they are said to contain an incredible power.")
                                        .npcl("If you really want to hear more about it you'd be best off finding someone who cares about the past, and the history of this area, and stop bothering me.")
                                        .end()
                            }
                }

        b.onQuestStages(DesertTreasure.questName, 100)
                .npcl("So you're the @g[fella,lass] that freed Azzanadra, huh? Fair play to ya. What will you have?")
                .end()

    }
}
