package content.region.kandarin.feldip.quest.zogreflesheaters

import core.api.*
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import core.tools.RandomFunction
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery

/** This NPC is a scenery. */
//@Initializable
//class SithikIntsDialogue (player: Player? = null) : DialoguePlugin(player) {
//    override fun newInstance(player: Player): DialoguePlugin {
//        return SithikIntsDialogue(player)
//    }
//    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
//        openDialogue(player, SithikIntsDialogueFile(), npc)
//        return false
//    }
//    override fun getIds(): IntArray {
//        return intArrayOf(NPCs.SITHIK_INTS_2061, NPCs.SITHIK_INTS_2062)
//    }
//}
class SithikIntsDialogue : InteractionListener {
    override fun defineListeners() {
        on(Scenery.SITHIK_INTS_6888, SCENERY, "talk-to") { player, node ->
            openDialogue(player, SithikIntsDialogueFile(), NPC(NPCs.SITHIK_INTS_2061))
            return@on true
        }
        on(Scenery.SITHIK_INTS_6889, SCENERY, "talk-to") { player, node ->
            openDialogue(player, SithikIntsOgreFormDialogueFile(), NPC(NPCs.SITHIK_INTS_2062))
            return@on true
        }

        on(Scenery.DRAWERS_6875, SCENERY, "search") { player, node ->
            if (getQuestStage(player, ZogreFleshEaters.questName) <= 2) {
                openDialogue(player, object : DialogueFile() {
                    override fun handle(componentID: Int, buttonID: Int) {
                        when (stage) {
                            0 -> sendNPCDialogue(player, NPCs.SITHIK_INTS_2061, "Hey! What do you think you're doing?", FacialExpression.ANNOYED).also { stage++ }
                            1 -> sendPlayerDialogue(player, "Erk! I'd better not start rifling through peoples things without permission.").also { stage = END_DIALOGUE }
                        }
                    }
                })
            } else if (getQuestStage(player, ZogreFleshEaters.questName) in 3..4 &&
                    (!inInventory(player, Items.BOOK_OF_PORTRAITURE_4817) ||
                            !inInventory(player, Items.PAPYRUS_970) ||
                            !inInventory(player, Items.CHARCOAL_973)
                            )) {
                if (hasSpaceFor(player, Item(Items.BOOK_OF_PORTRAITURE_4817, 3))) {
                    openDialogue(player, object : DialogueFile() {
                        override fun handle(componentID: Int, buttonID: Int) {
                            when (stage) {
                                0 -> {
                                    if (!inInventory(player, Items.PAPYRUS_970) && !inInventory(player, Items.CHARCOAL_973)) {
                                        sendDoubleItemDialogue(player, Items.CHARCOAL_973, Items.PAPYRUS_970, "You find some charcoal and papyrus.")
                                        addItemOrDrop(player, Items.PAPYRUS_970)
                                        addItemOrDrop(player, Items.CHARCOAL_973)
                                        stage++
                                    } else if (!inInventory(player, Items.PAPYRUS_970)) {
                                        sendItemDialogue(player, Items.PAPYRUS_970, "You find some papyrus.")
                                        addItemOrDrop(player, Items.PAPYRUS_970)
                                        stage++
                                    } else if (!inInventory(player, Items.CHARCOAL_973)) {
                                        sendItemDialogue(player, Items.CHARCOAL_973, "You find some charcoal.")
                                        addItemOrDrop(player, Items.CHARCOAL_973)
                                        stage++
                                    } else {
                                        sendItemDialogue(player, Items.BOOK_OF_PORTRAITURE_4817, "You also find a book on portraiture.")
                                        addItemOrDrop(player, Items.BOOK_OF_PORTRAITURE_4817)
                                        stage = END_DIALOGUE
                                    }
                                }
                                1 -> {
                                    sendItemDialogue(player, Items.BOOK_OF_PORTRAITURE_4817, "You also find a book on portraiture.")
                                    addItemOrDrop(player, Items.BOOK_OF_PORTRAITURE_4817)
                                    stage = END_DIALOGUE
                                }
                            }
                        }
                    })
                } else {
                    sendDialogue(player, "You see some items in the drawer, but you need 3 free inventory spaces to take them.")
                }
            } else {
                sendMessage(player, "You search but find nothing of significance.")
            }
            return@on true
        }

        on(Scenery.CUPBOARD_6876, SCENERY, "search") { player, node ->
            if (getQuestStage(player, ZogreFleshEaters.questName) <= 2) {
                openDialogue(player, object : DialogueFile() {
                    override fun handle(componentID: Int, buttonID: Int) {
                        when (stage) {
                            0 -> sendNPCDialogue(player, NPCs.SITHIK_INTS_2061, "Hey! What do you think you're doing?", FacialExpression.ANNOYED).also { stage++ }
                            1 -> sendPlayerDialogue(player, "Erk! I'd better not start rifling through peoples things without permission.").also { stage = END_DIALOGUE }
                        }
                    }
                })
            } else if (getQuestStage(player, ZogreFleshEaters.questName) in 3..4 && !inInventory(player, Items.NECROMANCY_BOOK_4837)) {
                if (hasSpaceFor(player, Item(Items.NECROMANCY_BOOK_4837))) {
                    addItemOrDrop(player, Items.NECROMANCY_BOOK_4837)
                    sendItemDialogue(player, Items.NECROMANCY_BOOK_4837, "You find a book on Necromancy.")
                    setAttribute(player, ZogreFleshEaters.attributeFoundNecromanticBook, true)
                } else {
                    sendDialogue(player, "You see an item in the cupboard, but you don't have space to put it in your inventory.")
                }
            } else {
                sendMessage(player, "You search but find nothing of significance.")
            }
            return@on true
        }

        onUseWith(IntType.SCENERY, Items.NECROMANCY_BOOK_4837, Scenery.SITHIK_INTS_6888) { player, used, with ->
            openDialogue(player, object : DialogueFile() {
                override fun handle(componentID: Int, buttonID: Int) {
                    when (stage) {
                        0 -> sendPlayerDialogue(player,"Aha! A necromantic book! What's this doing here then?").also { stage++ }
                        1 -> sendItemDialogue(player, Items.NECROMANCY_BOOK_4837, "You show the Necromantic book to Sithik.").also { stage++ }
                        2 -> sendNPCDialogue(player, NPCs.SITHIK_INTS_2061, "Oh..I'm not quite sure actually...where did you find that then?").also { stage++ }
                        3 -> sendPlayerDialogue(player,"I found it in this cupboard! What do you have to say for yourself?").also { stage++ }
                        4 -> sendNPCDialogue(player, NPCs.SITHIK_INTS_2061, "Oh yes, that's right...I remember now. It's for my research, there's nothing really dangerous about it, unless it falls into the wrong hands. I'm sure it's pretty safe with me.").also { stage++ }
                        5 -> sendPlayerDialogue(player,"Hmmm, likely story!").also { stage = END_DIALOGUE }
                    }
                }
            })
            return@onUseWith true
        }

        on(Scenery.WARDROBE_6877, SCENERY, "search") { player, node ->
            if (getQuestStage(player, ZogreFleshEaters.questName) <= 2) {
                openDialogue(player, object : DialogueFile() {
                    override fun handle(componentID: Int, buttonID: Int) {
                        when (stage) {
                            0 -> sendNPCDialogue(player, NPCs.SITHIK_INTS_2061, "Hey! What do you think you're doing?", FacialExpression.ANNOYED).also { stage++ }
                            1 -> sendPlayerDialogue(player, "Erk! I'd better not start rifling through peoples things without permission.").also { stage = END_DIALOGUE }
                        }
                    }
                })
            } else if (getQuestStage(player, ZogreFleshEaters.questName) in 3..4 && !inInventory(player, Items.BOOK_OF_HAM_4829)) {
                if (hasSpaceFor(player, Item(Items.BOOK_OF_HAM_4829))) {
                    addItemOrDrop(player, Items.BOOK_OF_HAM_4829)
                    sendItemDialogue(player, Items.BOOK_OF_HAM_4829, "You find a book on Philosophy written by the 'Human's Against Monsters' leader, Johanhus Albrect.")
                    setAttribute(player, ZogreFleshEaters.attributeFoundHamBook, true)
                } else {
                    sendDialogue(player, "You see an item in the wardrobe, but you don't have space to put it in your inventory.")
                }
            } else {
                sendMessage(player, "You search but find nothing of significance.")
            }
            return@on true
        }

        onUseWith(IntType.SCENERY, Items.BOOK_OF_HAM_4829, Scenery.SITHIK_INTS_6888) { player, used, with ->
            openDialogue(player, object : DialogueFile() {
                override fun handle(componentID: Int, buttonID: Int) {
                    when (stage) {
                        0 -> sendPlayerDialogue(player,"What's this then?").also { stage++ }
                        1 -> sendItemDialogue(player, Items.BOOK_OF_HAM_4829, "You show the HAM book to Sithik.").also { stage++ }
                        2 -> sendNPCDialogue(player, NPCs.SITHIK_INTS_2061, "What do you mean? It's a book by the respected HAM leader Johanhus Ulsbrecht, that man speaks for a lot of people who are unhappy with the current state of affairs.").also { stage++ }
                        3 -> sendNPCDialogue(player, NPCs.SITHIK_INTS_2061, "Can you honestly tell me that you've not had to fight for your life against the odd monster or two?").also { stage++ }
                        4 -> sendPlayerDialogue(player,"Hmm, that may be true, but I don't universally hate all monsters, whereas I have a sneaking suspicion that you do...and ogres in particular!").also { stage++ }
                        5 -> sendNPCDialogue(player, NPCs.SITHIK_INTS_2061, "Hmm, that's an interesting theory, care to back it up with any facts?").also { stage = END_DIALOGUE }
                    }
                }
            })
            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, Items.PAPYRUS_970, Scenery.SITHIK_INTS_6888) { player, used, with ->
            if(inInventory(player, Items.CHARCOAL_973)) {
                if (removeItem(player, used)) {
                    openDialogue(player, object : DialogueFile() {
                        override fun handle(componentID: Int, buttonID: Int) {
                            when (stage) {
                                0 -> sendNPCDialogue(player, NPCs.SITHIK_INTS_2061, "Oh lovely! You're making my portrait! Let me see it afterwards!", FacialExpression.FRIENDLY).also { stage++ }
                                1 -> sendDialogue(player, "You begin sketching the irritable Sithik.").also { animate(player, 909); stage++ }
                                2 -> {
                                    val skill = Skills.CRAFTING
                                    val level: Int = getDynLevel(player, skill) + getFamiliarBoost(player, skill)
                                    val ratio = RandomFunction.getSkillSuccessChance(0.0, 80.0, level)

                                    if (ratio > 0.5) {
                                        // Passed
                                        sendItemDialogue(player, Items.SITHIK_PORTRAIT_4814, "You get a portrait of Sithik.")
                                        addItemOrDrop(player, Items.SITHIK_PORTRAIT_4814)
                                    } else {
                                        // Failed
                                        sendItemDialogue(player, Items.SITHIK_PORTRAIT_4815, "You get a portrait of Sithik.")
                                        addItemOrDrop(player, Items.SITHIK_PORTRAIT_4815)
                                    }
                                    setAttribute(player, ZogreFleshEaters.attributeMadePortrait, true)
                                    stage = END_DIALOGUE
                                }
                            }
                        }
                    })
                }
            } else {
                sendDialogue(player, "You have no charcoal with which to sketch this subject.")
            }
            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, Items.BOOK_OF_PORTRAITURE_4817, Scenery.SITHIK_INTS_6888) { player, used, with ->
            openDialogue(player, object : DialogueFile() {
                override fun handle(componentID: Int, buttonID: Int) {
                    when (stage) {
                        0 -> sendPlayerDialogue(player,"Oh, so explain this then?").also { stage++ }
                        1 -> sendItemDialogue(player, Items.BOOK_OF_PORTRAITURE_4817, "You show the book on portraiture to Sithik.").also { stage++ }
                        2 -> sendNPCDialogue(player, NPCs.SITHIK_INTS_2061, "It's my hobby...I'm interested in portraiture, but all art in general. It's fun, you should try it.").also { stage++ }
                        3 -> sendPlayerDialogue(player,"How do I do it...").also { stage++ }
                        4 -> sendNPCDialogue(player, NPCs.SITHIK_INTS_2061, "Well...you could start by reading the book!").also { stage = END_DIALOGUE }
                    }
                }
            })
            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, Items.SITHIK_PORTRAIT_4814, Scenery.SITHIK_INTS_6888) { player, used, with ->
            openDialogue(player, object : DialogueFile() {
                override fun handle(componentID: Int, buttonID: Int) {
                    when (stage) {
                        0 -> sendPlayerDialogue(player, "Here you go, what do you think?").also { stage++ }
                        1 -> sendItemDialogue(player, Items.SITHIK_PORTRAIT_4814, "You show the sketch...").also { stage++ }
                        2 -> sendNPCDialogue(player, NPCs.SITHIK_INTS_2061, "Hmmm, well it's not the most flattering of portraits, but I like the 'honesty' of the work...well done.").also { stage = END_DIALOGUE }
                    }
                }
            })
            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, Items.SITHIK_PORTRAIT_4815, Scenery.SITHIK_INTS_6888) { player, used, with ->
            openDialogue(player, object : DialogueFile() {
                override fun handle(componentID: Int, buttonID: Int) {
                    when (stage) {
                        0 -> sendPlayerDialogue(player, "Here you go, what do you think?").also { stage++ }
                        1 -> sendItemDialogue(player, Items.SITHIK_PORTRAIT_4815, "You show the sketch...").also { stage++ }
                        2 -> sendNPCDialogue(player, NPCs.SITHIK_INTS_2061, "Hmmm, well it's an interesting interpretation, but not really classic realist representation is it? It's not my favourite, but I like the 'truth' of the work...well done.").also { stage = END_DIALOGUE }
                    }
                }
            })
            return@onUseWith true
        }

        on(Items.CUP_OF_TEA_4838, ITEM, "take") { player, node ->
            sendNPCDialogue(player, NPCs.SITHIK_INTS_2061, "Hey! What do you think you're doing? Leave my tea alone!", FacialExpression.ANNOYED)
            return@on true
        }

        onUseWith(IntType.SCENERY, Items.STRANGE_POTION_4836, Scenery.SITHIK_INTS_6888) { player, used, with ->
            openDialogue(player, object : DialogueFile() {
                override fun handle(componentID: Int, buttonID: Int) {
                    when (stage) {
                        0 -> sendPlayerDialogue(player,"Here, try some of this potion, it'll make you feel better!").also { stage++ }
                        1 -> sendNPCDialogue(player, NPCs.SITHIK_INTS_2061, "Err, yuck...no way am I taking any potions or medication off you...I don't trust you!").also { stage = END_DIALOGUE }
                    }
                }
            })
            return@onUseWith true
        }

        onUseWith(IntType.GROUNDITEM, Items.STRANGE_POTION_4836, Items.CUP_OF_TEA_4838) { player, _, with ->
            if(getQuestStage(player, ZogreFleshEaters.questName) == 5) {
                if (removeItem(player, Items.STRANGE_POTION_4836)) {
                    sendItemDialogue(player, Items.STRANGE_POTION_4836, "You pour some of the potion into the cup. Zavistic said it may take some time to have an effect.")
                    addItemOrDrop(player, Items.SAMPLE_BOTTLE_3377)
                }
                setQuestStage(player, ZogreFleshEaters.questName, 6)
            }
            return@onUseWith true
        }
    }
}

class SithikIntsDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        b.onQuestStages(ZogreFleshEaters.questName, 0, 1, 2)
                .npcl(FacialExpression.ANNOYED, "Hey... who gave you permission to come in here! Get out, get out I say.")
                .playerl("Alright, alright... keep your night cap on.")
                .end()

        b.onQuestStages(ZogreFleshEaters.questName, 3, 4, 5, 6)
                .npcl(FacialExpression.ANNOYED, "Hey... who gave you permission to come in here! Get out, get out I say.")
                .playerl("Zavistic Rarve said that I could come and talk to you and ask you a few questions.")
                .betweenStage { df, player, _, _ ->
                    if (getQuestStage(player, ZogreFleshEaters.questName) == 3) {
                        setQuestStage(player, ZogreFleshEaters.questName, 4)
                    }
                }
                .npcl(FacialExpression.ANNOYED, "Oh, Zavistic...why...why would he send you to me?")

                .let { builder ->
                    val returnJoin = b.placeholder()
                    returnJoin.builder()
                            .options()
                            .let { optionBuilder ->
                                optionBuilder.option_playerl("Do you know anything about the undead ogres at Jiggig?")
                                        .npcl("Er...undead ogres...no, sorry, no idea what you're talking about there.")
                                        .playerl("Hmm, is that right...")
                                        .npcl("Well, yes, yes it is. If I knew something, I'd tell you.")
                                        .npcl("Anyway, dead ogres you say? How strange? That must be a strange sight?")
                                        .playerl("Very well, if you don't know anything about it, you won't mind if I look around then?")
                                        .npcl("Well,err....well, actually yes I do mind...it's my place and I don't want strangers going through my things.")
                                        .goto(returnJoin)

                                optionBuilder.option_playerl("What do you do?")
                                        .npcl("I'm a scholarly student of the magical arts. When I was younger I used to be an adventurer, probably just like yourself. But I lost interest in the constant fighting, looting and gaining abilities.")
                                        .npcl("Instead I decided to focus my attention and time to study the purer form of the lost arts.")
                                        .playerl("The lost arts? What are they?")
                                        .npcl("Ignorant people call them the 'dark arts'. I'm talking about Necromancy, the power to bring the dead back to life - the power of the gods! Surely the most awesome power known to man.")
                                        .playerl("Hmm, well I guess I must be an ignorant person then, because bringing the dead back to life sounds very unnatural.")
                                        .goto(returnJoin)

                                optionBuilder.option_playerl("Do you mind if I look around?")
                                        .npcl("Well,err....well, actually yes I do mind...it's my place and I don't want strangers going through my things.")
                                        .playerl("Well, I'm going to have a look around anyway, if you're not involved in this whole thing, you won't have anything to hide.")
                                        .npcl("Why, if I was a few years younger I'd give you a good hiding!")
                                        .playerl("I'm sure!")
                                        .goto(returnJoin)

                                optionBuilder.option_playerl("Ok, thanks.")
                                        .end()
                            }
                    builder.goto(returnJoin)
                }
    }
}

class SithikIntsOgreFormDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {

        b.onQuestStages(ZogreFleshEaters.questName, 7,8,9,100)
            /*
            There are some after first time dialogue, but who reads this...
            .npcl("Arghhhh..what do you want now...you've turned me into a beast!")
            .playerl("I've got some questions for you...and you'd better answer them well or else!")
            .npcl("Ok, ok, I'll tell you anything, just turn me back into a human again!")
             */
            .npcl(FacialExpression.OLD_DEFAULT, "Arghhhh..what's happened to me...you beast!")
            .playerl("It's your own fault, you shouldn't have lied about your involvement with the undead Ogres at Jiggig. The potion will wear off once you've told the truth!")
            .npcl(FacialExpression.OLD_DEFAULT, "Ok, ok, I admit it, I got Brentle Vahn to cast the spell to put an end to those awful Ogres...they're just disgusting creatures...")
            .playerl("Ok, that's a start...now I want some answers.")
                .let { builder ->
                    val returnJoin = b.placeholder()
                    returnJoin.builder()
                            .options()
                            .let { optionBuilder ->
                                optionBuilder.option("How do I remove the effects of the spell from the area?")
                                        .playerl("How do I remove the effects of the spell from the area? The ogres want to get their ceremonial dance area back and can't do that with undead walking all over it.")
                                        .npcl(FacialExpression.OLD_DEFAULT, "Unfortunately you can't. The spell is permanent, it will last forever, the only option you have is to move the ceremonial area.")
                                        .playerl("You're an evil man and I'm going to make you pay for this...you can stay like that forever as far as I'm concerned.")
                                        .npcl(FacialExpression.OLD_DEFAULT, "No...no, let me try to make amends...please I can help you. Just don't leave me like this.")
                                        .goto(returnJoin)

                                optionBuilder.option_playerl("How do I get rid of the undead ogres?")
                                        .npcl(FacialExpression.OLD_DEFAULT, "Ok, similar spells have been cast before and the only way to deal with the resulting creatures is to cordon off the area and not go in there again.")
                                        .npcl(FacialExpression.OLD_DEFAULT, "The undead creatures usually manifest some sort of disease so it's best to attack them from a distance with a ranged weapon.")
                                        .npcl(FacialExpression.OLD_DEFAULT, "Normal missiles like arrows and darts do very little damage to them because they're designed to destroy internal organs. This is a waste of time with undead creatures like undead ogres.")
                                        .playerl("Yeah, clearly so what should we use?")
                                        .npcl(FacialExpression.OLD_DEFAULT, "From my research it looks like a flat ended arrow was designed called a 'Brutal arrow'. This does large amounts of crushing damage to the creature. You can make them by using larger arrows.")
                                        .npcl(FacialExpression.OLD_DEFAULT, "I think some Ogre hunters make them. But instead of adding an arrow tip, you hammer a large nail into the end of the shaft.")
                                        .goto(returnJoin)

                                optionBuilder.option_playerl("How do I get rid of the disease?")
                                        .npcl(FacialExpression.OLD_DEFAULT, "My research shows that two jungle based herbs can be used, one is found near river tributaries and looks like a vine, the other is found in caves and grows on the wall.")
                                        .npcl(FacialExpression.OLD_DEFAULT, "It's quite well camouflaged so it's unlikely that you'll find it.")
                                        .playerl("We'll see about that!")
                                        .goto(returnJoin)

                                optionBuilder.option_playerl("Sorry, I have to go.")
                                        .npcl(FacialExpression.OLD_DEFAULT, "But...you can't just leave me here like this!")
                                        .end()
                            }
                    builder.goto(returnJoin)
                }
    }
}