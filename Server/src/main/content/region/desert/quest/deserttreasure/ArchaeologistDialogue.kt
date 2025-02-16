package content.region.desert.quest.deserttreasure

import content.global.handlers.iface.BookInterface
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
/** Known in RS3 as Asgarnia Smith */
class ArchaeologistDialogue(player: Player? = null) : DialoguePlugin(player){
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player!!, ArchaeologistDialogueFile(), npc)
        return false
    }
    override fun newInstance(player: Player?): DialoguePlugin {
        return ArchaeologistDialogue(player)
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ARCHAEOLOGIST_1918)
    }
}

class ArchaeologistDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {

        b.onQuestStages(DesertTreasure.questName, 0)
                .player(FacialExpression.FRIENDLY,"Hello there.")
                .branch { player ->
                    return@branch if (DesertTreasure.hasRequirements(player)) { 1 } else { 0 }
                }.let{ branch ->
                    // Failure branch
                    branch.onValue(0)
                            .line("He seems to be lost in his own thoughts...")
                            .end()
                    return@let branch // Return DialogueBranchBuilder instead of DialogueBuilder to forward the success branch.
                }.onValue(1) // Success branch
                .npc(FacialExpression.FRIENDLY, "Howdy stranger. What brings you out to these parts?")
                .let { path ->
                    val originalPath = b.placeholder()
                    path.goto(originalPath)
                    originalPath.builder().options().let { optionBuilder ->
                        optionBuilder.option("What are you doing here?")
                                .player("Nothing much - What are you doing here?", "It doesn't seem like there's much to do out here in the", "desert!")
                                .npc("Well, that's where you'd be wrong.", "I work for the Archaeological Society of Varrock, and", "have been excavating around here recently.")
                                .npcl("Was there something you specifically wanted?")
                                .goto(originalPath)
                        optionBuilder.option("Do you have any quests?")
                                .player(FacialExpression.FRIENDLY, "Do you have any quests?", "Call it a hunch, but you look like the type of man who", "might...")
                                .npc(FacialExpression.HALF_THINKING, "Well, it's funny you should say that.", "I'm not sure if I would really call it a quest as such,", "but I found this ancient stone tablet in one of my", "excavations, and it would really help me out if you")
                                .npc(FacialExpression.FRIENDLY, "could go and take it back to the digsite for me and get", "it examined.")
                                .npc(FacialExpression.NEUTRAL, "It's very old, and I don't recognise and of the", "inscriptions on it.")
                                .options().let { optionBuilder2 ->
                                    optionBuilder2.option("Yes, I'll help you.")
                                            .betweenStage { df, player, _, _ ->
                                                addItemOrDrop(player, Items.ETCHINGS_4654)
                                            }
                                            .player("Sure, I was heading that way anyway.", "Any particular person at the digsite you want me to", "talk to?")
                                            .npc(FacialExpression.NEUTRAL, "His name's Terry Balando. Give it to nobody but him.", "I'm sorry, I can't entrust you with the actual tablet I", "found, but it is far too valuable to give away, but I took", "these etchings - they should be enough for him to make")
                                            .npc(FacialExpression.NEUTRAL, "a preliminary translation on.", "Come back and let me know what he says, I would hate", "to waste my time excavating anything that isn't worth", "my time as a world famous archaeologist!")
                                            .endWith { _, player ->
                                                if(getQuestStage(player, DesertTreasure.questName) == 0) {
                                                    setQuestStage(player, DesertTreasure.questName, 1)
                                                }
                                            }
                                    optionBuilder2.option("No thanks, I don't want to help.")
                                            .playerl("No thanks. Playing delivery boy doesn't really sound like much fun to me. ")
                                            .npcl(" Well, okay. Can't say as I blame you for thinking that. I'll go take it there myself later I guess.")
                                            .end()
                                }
                        optionBuilder.option("Who are you?")
                                .playerl(FacialExpression.THINKING, "Who are you, anyway?")
                                .npc(FacialExpression.EXTREMELY_SHOCKED, "You don't recognize me???", "I am the world famous Asgarnia Smith, Archaeologist", "extraordinaire!")
                                .npc(FacialExpression.HALF_THINKING, "I am the one who discovered the long forgotten", "Temple of Ikov!", "The one who unearthed the strange trap filled arena", "in Brimhaven!")
                                .npc(FacialExpression.HALF_THINKING, "I'm the leading archaeological expert of our time!", "I was voted archaeologist of the year four years", "running by the Varrock Herald! Are you REALLY", "sure you've never heard of me?")
                                .playerl(FacialExpression.THINKING, "No, I really haven't.")
                                .npc(FacialExpression.EXTREMELY_SHOCKED, "Well then, I'm confused.", "Why did you come over and speak to me if you don't", "know who I am? What do you want?")
                                .goto(originalPath)
                        optionBuilder.option("Nothing really.")
                                .playerl("Nothing really. I'm just wandering around for no good reason.")
                                .npcl("Uh-huh. Well, if you'll excuse me, I have a lot more work to do before I can bed down for the night.")
                                .end()
                    }
                }


        b.onQuestStages(DesertTreasure.questName, 1,2)
                .npcl("So what did Terry Balando say about those etchings? Did he give you a translation for me?")
                .playerl("Um...yeah...about that... I kind of didn't go and speak to him yet...")
                .npcl("Well what are you waiting for? A written invitation? All I want you to do is take those etchings up to the digsite for me!")
                .playerl("Okay, I'll do that then.")
                .end()

        b.onQuestStages(DesertTreasure.questName, 3)
                .playerl("Hello there.")
                .npcl("So what did Terry Balando say about those etchings? Did he give you a translation for me?")
                .branch { player ->
                    return@branch if (inInventory(player, Items.TRANSLATION_4655)) { 1 } else { 0 }
                }.let{ branch ->
                    // Failure branch
                    branch.onValue(0)
                        .playerl("Yeah, he did. But I don't have it with me.")
                        .npcl("...")
                        .npcl("I see.")
                        .npcl("Well could you go and get me that translation? It could be vital!")
                        .end()
                    return@let branch // Return DialogueBranchBuilder instead of DialogueBuilder to forward the success branch.
                }.onValue(1) // Success branch
                .playerl("Yeah, he did. I have it here in this book.")
                .npcl("Did you take a read of this? It will do you good to understand how this profession works. Here, have a read.")
                .options().let { optionBuilder ->
                    optionBuilder.option("Read book")
                            .endWith { _, player ->
                                BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, TranslationBook.Companion::display)
                            }
                    return@let optionBuilder
                }
                .option("Don't read book")
                .playerl("Yeah, I did. Kind of boring really.")
                .npcl("Excellent. Just give me a moment to read this, and talk to me again in a second.")
                .endWith { _, player ->
                    if(getQuestStage(player, DesertTreasure.questName) == 3) {
                        removeItem(player, Items.TRANSLATION_4655) // remove if exists
                        setQuestStage(player, DesertTreasure.questName, 4)
                    }
                }

        b.onQuestStages(DesertTreasure.questName, 4)
                .playerl("Hello there.")
                .npcl("Hmmm. Interesting. It seems to me like there's some kind of treasure hidden out in the desert.")
                .npcl("So what do you say? Fancy being a treasure hunter like me?")
                .playerl("Uh... don't you mean archaeologist?")
                .npcl("Yeeees... An archaeologist...")
                .npcl("It's all this hot sun getting to me I think.")
                .npcl("Well anyway, let's just assume there is a treasure hidden in the desert somewhere, and let's just say for the sake of argument that maybe if I found a big stash of gold and treasure I wouldn't necessarily just hand it")
                .npcl("all over to the Museum of Varrock.")
                .npcl("Let's also say, purely hypothetically, that if there were such a big stash of treasure and someone were to help me find it, then that hypothetical personal might be entitled to, oh, let's say a purely for the sake of")
                .npcl("argument thirty percent split...")
                .playerl("Fifty percent.")
                .npcl("That's right!")
                .npcl("A purely for the sake of argument fifty-fifty split on this hypothetical treasure, should it exist...")
                .npcl("Do you see where I'm going with this?")
                .playerl("You want me to help you find some treasure for a fifty percent share, as long as I don't tell anybody, and your reputation as an esteemed archaeologist with the Museum of Varrock remains intact, and nobody")
                .playerl("discovers you're actually just a treasure hunter?")
                .npcl("Uh... yes, but the way you said it makes it sound like I'm doing something wrong...")
                .npcl("So what do you say? Partners?")
                .options()
                .let { optionBuilder ->
                    optionBuilder.option("Help him")
                            .playerl("Well... I guess nobody is really going to lose out on anything, and we don't even know if there is any treasure...")
                            .playerl("Aw, go on then. Count me in.")
                            .npcl("Good @g[lad,lass]! Well, if we split up we'll be able to find treasure quicker.")
                            .npcl("I'll continue searching around here in this Bedabin Camp, you head due South to the Bandit Village. If either of us find anything, we'll come find each other and say what, okay?")
                            .npcl("You head due South, see what you can find out about this tablet.")
                            .endWith { _, player ->
                                if(getQuestStage(player, DesertTreasure.questName) == 4) {
                                    setQuestStage(player, DesertTreasure.questName, 5)
                                }
                            }
                    optionBuilder.option("Don't help him")
                            .playerl("This all sounds very immoral, and I don't want any part of your seedy little schemes.")
                            .npcl("Aw, c'mon, cut me a break here man! Look, nobody is really getting hurt, right? This treasure is either going to lie around in some underground ruin, or lie around in some museum for")
                            .npcl("people to look at!")
                            .npcl("It makes sense for the people to go to all the risk to get a little payback for it, right? Besides, anything of real historical value we can hand over to the museum - we'll just keep the cash for")
                            .npcl("ourselves!")
                            .npcl("C'mon... what do you say? Partners?")
                                .options()
                                .let { optionBuilder ->
                                    optionBuilder.option("Help him")
                                            .playerl("Well... I guess nobody is really going to lose out on anything, and we don't even know if there is any treasure...")
                                            .playerl("Aw, go on then. Count me in.")
                                            .npcl("Good @g[lad,lass]! Well, if we split up we'll be able to find treasure quicker.")
                                            .npcl("I'll continue searching around here in this Bedabin Camp, you head due South to the Bandit Village. If either of us find anything, we'll come find each other and say what, okay?")
                                            .npcl("You head due South, see what you can find out about this tablet.")
                                            .endWith { _, player ->
                                                if(getQuestStage(player, DesertTreasure.questName) == 4) {
                                                    setQuestStage(player, DesertTreasure.questName, 5)
                                                }
                                            }
                                    optionBuilder.option("Don't help him")
                                            .playerl("I don't trust anything about your little scheme, including you! I want nothing to do with this!")
                                            .npcl("Eh, you'll come running back when you realize just how much money we could be talking here.")
                                            .npcl("I'll see you again when you come crawling back, begging to be cut in on the deal.")
                                            .end()
                                }
                }

        b.onQuestStages(DesertTreasure.questName, 5)
                .playerl("Hello there.")
                .npcl("Hello again. You find any signs of that treasure yet?")
                .playerl("Not yet...")
                .npcl("Well, head south to that bandit camp. I bet if there's anything out here, they'll know about it. Don't let on it might be valuable though! Those thieves will steal it as soon as look at you!")

        b.onQuestStages(DesertTreasure.questName, 6,7,8)
                .playerl("Hello there.")
                .npcl("Hello again. You find any signs of that treasure yet?")
                .playerl("Not as such... Although I think I'm onto something of a promising lead...")
                .playerl("Did you ever hear of something called the Diamonds of Azzanadra?")
                .npcl("Diamonds of Azzanadra? That sounds very promising indeed!")
                .playerl("So you have heard of them?")
                .npcl("Nope, never heard of them before, but you said diamonds! Diamonds are always promising!")


    }

}