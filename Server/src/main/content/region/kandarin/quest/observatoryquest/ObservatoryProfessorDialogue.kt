package content.region.kandarin.quest.observatoryquest

import content.data.Quests
import core.api.*
import core.game.dialogue.*
import content.region.kandarin.quest.observatoryquest.ObservatoryQuest.Companion.attributeReceivedWine
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs09.consts.Components
import org.rs09.consts.Items
import org.rs09.consts.NPCs

@Initializable
class ObservatoryProfessorDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player): DialoguePlugin {
        return ObservatoryProfessorDialogue(player)
    }
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, ObservatoryProfessorDialogueFile(), npc)
        return false
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.OBSERVATORY_ASSISTANT_6119, NPCs.OBSERVATORY_ASSISTANT_6120)
    }
}

class ObservatoryProfessorDialogueFile : DialogueLabeller() {
    override fun addConversation() {
        assignToIds(NPCs.OBSERVATORY_PROFESSOR_488)

        npc("What would you like to talk about?")
        options(
            DialogueOption("observatoryquest", "Talk about the Observatory Quest.", skipPlayer = true),
            DialogueOption("treasuretrails", "Talk about the Treasure Trails.", skipPlayer = true),
        )

        label("observatoryquest")
            exec { player, npc ->
                loadLabel(player, "observatoryqueststage" + getQuestStage(player, Quests.OBSERVATORY_QUEST))
            }

        label("observatoryqueststage0")
            player("Hi, I was...")
            npc(FacialExpression.FRIENDLY, "Welcome to the magnificent wonder of the Observatory, where wonder is all around you, where the stars can be clutched from the heavens!")
            player("Wow, nice intro.")
            npc(FacialExpression.FRIENDLY, "Why, thanks! How might I help you?")
            options(
                    DialogueOption("totallylost", "I'm totally lost."),
                    DialogueOption("whatwhat", "An Observatory?", expression = FacialExpression.THINKING),
                    DialogueOption("nah", "I'm just passing through."),
            )

        label("totallylost")
            npc(FacialExpression.THINKING, "Lost? It must have been those pesky goblins that led you astray.")
            npc("Head north-east to find the city of Ardougne.")
            player("I'm sure I'll find the way.")
            player("Thanks for all your help.")
            npc(FacialExpression.FRIENDLY, "No problem at all. Come and visit again!")
            line("The professor carries on with his studies.")

        label("nah")
            npc("Fair enough. Not everyone is interested in this place, I suppose.")

        label("whatwhat")
            npc(FacialExpression.FRIENDLY, "Of course. We have a superb telescope up in the Observatory, on the hill.")
            npc(FacialExpression.FRIENDLY, "A truly marvellous invention, the likes of which you'll never behold again.")
            npc(FacialExpression.NEUTRAL, NPCs.OBSERVATORY_ASSISTANT_6118, "Well, it would be if it worked.")
            npc(FacialExpression.ANGRY, "Don't interrupt!")
            player(FacialExpression.THINKING, "What? It doesn't work?")
            npc("Oh, no, no, no. Don't listen to him, he's joking. Aren't you, my FAITHFUL assistant?")
            npc(FacialExpression.NEUTRAL, NPCs.OBSERVATORY_ASSISTANT_6118, "Nope, dead serious. Hasn't been working for a long time.")
            npc(FacialExpression.ANGRY, "Arghhh! Get back to work and stop sticking your nose in!")
            player("So, it's broken. How come?")
            npc(FacialExpression.SAD, "Oh, I suppose there's no use keeping it secret. Did you see those houses outside?")
            player("Up on the hill? Yes, I've seen them.")
            npc("It's a horde of goblins.")
            npc(FacialExpression.SAD, "Since they moved here they have caused nothing but trouble.")
            npc("Last week, my telescope was tampered with.")
            npc(FacialExpression.ANGRY, "Now, parts need replacing before it can be used again. They've even been messing around in the dungeons under this area. Something needs to be done.")
            npc(FacialExpression.NEUTRAL, NPCs.OBSERVATORY_ASSISTANT_6118, "Strikes me that this visitor could help us.")
            npc(FacialExpression.ANGRY, "Stop being so rude.")
            npc("...")
            npc("Although, he has a point. What do you say?")
            player(FacialExpression.THINKING, "What, me?")
            options(
                DialogueOption("yestoquest", "Sounds interesting. How can I help?", "Sounds interesting, what can I do for you?", FacialExpression.FRIENDLY),
                DialogueOption("notoquest", "Oh, sorry, I don't have time for that."),
            )

        label("notoquest")
            npc("Oh dear. I really do need some help.")
            npc("If you see anyone who can help then please send them my way.")

        label("yestoquest")
            exec { player, _ ->
                if(getQuestStage(player, Quests.OBSERVATORY_QUEST) == 0) {
                    setQuestStage(player, Quests.OBSERVATORY_QUEST, 1)
                }
            }
            npc(FacialExpression.FRIENDLY, "Oh, thanks so much.")
            npc(FacialExpression.FRIENDLY, "I shall need some materials for the telescope, so it can be used again.")
            npc("Let's start with three planks of wood for the telescope base. My assistant will help with obtaining these, won't you?")
            npc(FacialExpression.NEUTRAL, NPCs.OBSERVATORY_ASSISTANT_6118, "As if I don't have enough work to do. Seems I don't have a choice.")
            npc(FacialExpression.FRIENDLY, "Go talk to him if you need some advice.")
            player(FacialExpression.FRIENDLY, "Okay, I'll be right back.")

        label("observatoryqueststage1")
            player(FacialExpression.FRIENDLY, "Hi again!")
            npc(FacialExpression.FRIENDLY, "It's my helping hand, back again.")
            npc(FacialExpression.THINKING, "Do you have the planks yet?")
            exec { player, _ ->
                if(inInventory(player, Items.PLANK_960, 3)) {
                    loadLabel(player, "enoughplanks")
                } else {
                    loadLabel(player, "notenoughplanks")
                }
            }

        label("notenoughplanks")
            player("Sorry, not yet. Three planks was it?")
            npc("It was indeed.")

        label("enoughplanks")
            player("Yes, I've got them. Here they are.")
            exec { player, _ ->
                if(removeItem(player, Item(Items.PLANK_960, 3))) {
                    if (getQuestStage(player, Quests.OBSERVATORY_QUEST) == 1) {
                        setQuestStage(player, Quests.OBSERVATORY_QUEST, 2)
                    }
                }
            }
            npc(FacialExpression.FRIENDLY, "Well done. This will make a big difference.")
            npc(FacialExpression.FRIENDLY, "Now, the bronze for the tube. Oh, assistant!")
            npc(FacialExpression.NEUTRAL, NPCs.OBSERVATORY_ASSISTANT_6118, "Okay, okay, ask me if you need any help, @name.")

        label("observatoryqueststage2")
            player("Hi.")
            npc("The traveller returns!")
            player("Still working hard?")
            npc(FacialExpression.NEUTRAL, NPCs.OBSERVATORY_ASSISTANT_6118, "Some of us are.")
            npc(FacialExpression.ANGRY, "What did I tell you about speaking when spoken to?")
            npc(FacialExpression.THINKING, "So, @name, you have the bronze bar?")
            exec { player, _ ->
                if(inInventory(player, Items.BRONZE_BAR_2349)) {
                    loadLabel(player, "hasbronzebar")
                } else {
                    loadLabel(player, "nohasbronzebar")
                }
            }

        label("nohasbronzebar")
            player("Not yet.")
            npc("Please bring me one, then.")

        label("hasbronzebar")
            player(FacialExpression.FRIENDLY, "I certainly do. Here you go.")
            exec { player, _ ->
                if(removeItem(player, Item(Items.BRONZE_BAR_2349))) {
                    if (getQuestStage(player, Quests.OBSERVATORY_QUEST) == 2) {
                        setQuestStage(player, Quests.OBSERVATORY_QUEST, 3)
                    }
                }
            }
            npc(FacialExpression.FRIENDLY, "Great. Now all I need is the lens made.")
            npc(FacialExpression.FRIENDLY, "Please get me some molten glass.")
            npc("Oi! Lazy bones!")
            player(FacialExpression.AMAZED, "What? I'm not lazy.")
            npc("Not you! I'm talking to my assistant.")
            npc(FacialExpression.NEUTRAL, NPCs.OBSERVATORY_ASSISTANT_6118, "Calm down old man, I heard. @name, I'm here if you need any help.")
            npc("Thank you. Wait a minute, who are you calling 'old'?")

        label("observatoryqueststage3")
            npc("How are you getting on finding me some molten glass?")
            exec { player, _ ->
                if(inInventory(player, Items.MOLTEN_GLASS_1775)) {
                    loadLabel(player, "hasmoltenglass")
                } else {
                    loadLabel(player, "nohasmoltenglass")
                }
            }

        label("nohasmoltenglass")
            player("Still working on it.")
            npc("I really need it. Please hurry.")

        label("hasmoltenglass")
            player(FacialExpression.FRIENDLY, "Here it is.")
            exec { player, _ ->
                if(removeItem(player, Item(Items.MOLTEN_GLASS_1775))) {
                    if (getQuestStage(player, Quests.OBSERVATORY_QUEST) == 3) {
                        setQuestStage(player, Quests.OBSERVATORY_QUEST, 4)
                    }
                }
            }
            npc(FacialExpression.FRIENDLY, "Excellent work, let's make the lens.")
            npc(FacialExpression.NEUTRAL, NPCs.OBSERVATORY_ASSISTANT_6118, "It'll need to be made to an exact shape and size.")
            npc("Well, obviously, hence why we have a lens mould.")
            npc(FacialExpression.NEUTRAL, NPCs.OBSERVATORY_ASSISTANT_6118, "Not any more. One of those goblins took it.")
            npc(FacialExpression.SAD, "Great, just what I need. @name, I don't suppose you could find it?")
            player(FacialExpression.FRIENDLY, "I'll have a look - where should I start?")
            npc(FacialExpression.FRIENDLY, "No idea. You could ask my USELESS assistant if you want.")
            npc(FacialExpression.NEUTRAL, NPCs.OBSERVATORY_ASSISTANT_6118, "What have I done to deserve this?")

        label("observatoryqueststage4")
            npc(FacialExpression.THINKING, "Did you bring me the mould?")
            exec { player, _ ->
                if(inInventory(player, Items.LENS_MOULD_602)) {
                    loadLabel(player, "haslensmould")
                } else {
                    loadLabel(player, "nohaslensmould")
                }
            }

        label("nohaslensmould")
            player("Still looking for it.")
            npc("Please try and find it; my assistant may be able to help.")

        label("haslensmould")
            player(FacialExpression.HAPPY, "I certainly have. You'll never guess what they were doing with it.")
            npc("Well, from the smell I'd guess cooking some vile concoction.")
            player(FacialExpression.HAPPY, "Wow, good guess. Well, here you go.")
            npc(FacialExpression.NEUTRAL, NPCs.OBSERVATORY_ASSISTANT_6118, "Please don't give that to him. Last time he tried any Crafting, I had to spend a week cleaning up after the explosion.")
            player("Explosion?")
            npc(FacialExpression.SUSPICIOUS, "Erm, yes. I think in this instance you had probably better do it.")
            player("I suppose it's better I don't ask.")
            npc(FacialExpression.HAPPY,"You can use the mould with molten glass to make a new lens.")
            exec { player, _ ->
                if(inInventory(player, Items.LENS_MOULD_602)) {
                    addItemOrDrop(player, Items.MOLTEN_GLASS_1775)
                    if (getQuestStage(player, Quests.OBSERVATORY_QUEST) == 4) {
                        setQuestStage(player, Quests.OBSERVATORY_QUEST, 5)
                    }
                }
            }
            item(Item(Items.MOLTEN_GLASS_1775), "The professor gives you back the molten glass.")


        label("observatoryqueststage5")
            npc("Is the lens finished?")
            exec { player, _ ->
                if(inInventory(player, Items.OBSERVATORY_LENS_603)) {
                    loadLabel(player, "haslens")
                } else {
                    loadLabel(player, "nohaslens")
                }
            }

        label("nohaslens")
            player("How do I make it again?")
            npc("Use the molten glass with the mould.")
            player("Huh. Simple.")

        label("haslens")
        player("Yes, here it is. You may as well take this mould too.") //No source if no mould present
        line("The player hands the observatory professor the observatory lens.")
        npc("Wonderful, at last I can fix the telescope.")
        npc("Would you accompany me to the Observatory? You simply must see the telescope in operation.")
        player("Sounds interesting. Count me in.")
        npc("Superb. You'll have to go via the dungeon under the goblin settlement, seeing as the bridge is broken. You'll find stairs up to the Observatory from there.")
        player("Okay. See you there.")
        exec { player, _ ->
            setVarbit(player, ObservatoryQuest.telescopeVarbit, 1, true)
            queueScript(player, 0, QueueStrength.SOFT) { stage: Int ->
                when (stage) {
                    0 -> {
                        openOverlay(player, Components.FADE_TO_BLACK_120)
                        return@queueScript delayScript(player, 4)
                    }
                    1 -> {
                        if(removeItem(player, Item(Items.OBSERVATORY_LENS_603))) {
                            removeItem(player, Item(Items.LENS_MOULD_602))
                            if (getQuestStage(player, Quests.OBSERVATORY_QUEST) == 5) {
                                setQuestStage(player, Quests.OBSERVATORY_QUEST, 6)
                            }
                        }
                        return@queueScript delayScript(player, 1)
                    }
                    2 -> {
                        openOverlay(player, Components.FADE_FROM_BLACK_170)
                        return@queueScript delayScript(player, 2)
                    }
                    3 -> {
                        sendDialogueLines(player, "The professor has gone ahead to the Observatory dome. Best you", "follow him to see the finished telescope.")
                        return@queueScript stopExecuting(player)
                    }
                    else -> return@queueScript stopExecuting(player)
                }
            }


        }

        label("observatoryqueststage6")
        npc("Hello, friend.")
        exec { player, _ ->
            if (getAttribute<Int?>(player, ObservatoryQuest.attributeTelescopeStar, null) != null) {
                loadLabel(player, "hasviewtelescope")
            } else {
                loadLabel(player, "nohasviewtelescope")
            }
        }
        label("nohasviewtelescope")
            // http://youtu.be/cK5suqcw3qU
            player("Hi, this really is impressive.")
            npc("Certainly is. Please, take a look through the telescope and tell me what you see.")

        label("hasviewtelescope")
            player("I've had a look through the telescope.")
            npc("What did you see? If you're not sure, you can find out by looking at the star charts dotted around the walls downstairs.")
            player("It was...")
            goto("page1")


        label("page1")
            options(
                DialogueOption("Aquarius", "Aquarius", skipPlayer = true),
                DialogueOption("Capricorn", "Capricorn", skipPlayer = true),
                DialogueOption("Sagittarius", "Sagittarius", skipPlayer = true),
                DialogueOption("Scorpio", "Scorpio", skipPlayer = true),
                DialogueOption("page2", "~ next ~", skipPlayer = true),
            )

        label("page2")
            options(
                DialogueOption("page1", "~ previous ~", skipPlayer = true),
                DialogueOption("Libra", "Libra", skipPlayer = true),
                DialogueOption("Virgo", "Virgo", skipPlayer = true),
                DialogueOption("Leo", "Leo", skipPlayer = true),
                DialogueOption("page3", "~ next ~", skipPlayer = true),
            )

        label("page3")
            options(
                DialogueOption("page2", "~ previous ~", skipPlayer = true),
                DialogueOption("Cancer", "Cancer", skipPlayer = true),
                DialogueOption("Gemini", "Gemini", skipPlayer = true),
                DialogueOption("Taurus", "Taurus", skipPlayer = true),
                DialogueOption("page4", "~ next ~", skipPlayer = true),
            )

        label("page4")
            options(
                DialogueOption("page3", "~ previous ~", skipPlayer = true),
                DialogueOption("Aries", "Aries", skipPlayer = true),
                DialogueOption("Pisces", "Pisces", skipPlayer = true),
            )

        label("Aquarius")
            player("Aquarius!")
            exec { player, _ ->
                if (getAttribute<Int?>(player, ObservatoryQuest.attributeTelescopeStar, null) == 19) {
                    loadLabel(player, "Aquariuscorrect")
                } else {
                    loadLabel(player, "wrongstar")
                }
            }

        label("Aquariuscorrect")
            npc("That's exactly it!")
            player("Yes! Woo hoo!")
            npc("That's Aquarius, the water-bearer.")
            npc("It seems suitable, then, to award you with water runes!")
            // Usually it is given here, but I don't want loopholes.
            npc("By Saradomin's earlobes! You must be a friend of the gods indeed.")
            npc("Look in your backpack for your reward, in payment for your work.")
            exec { player, _ ->
                addItemOrDrop(player, Items.WATER_RUNE_555, 25)
                addItemOrDrop(player, Items.UNCUT_SAPPHIRE_1623)
                finishQuest(player, Quests.OBSERVATORY_QUEST)
            }

        label("Aries")
        player("Aries!")
        exec { player, _ ->
            if (getAttribute<Int?>(player, ObservatoryQuest.attributeTelescopeStar, null) == 20) {
                loadLabel(player, "Ariescorrect")
            } else {
                loadLabel(player, "wrongstar")
            }
        }

        label("Ariescorrect")
        npc("That's exactly it!")
        player("Yes! Woo hoo!")
        npc("That's Aries, the ram.")
        npc("A fierce fighter. I'm sure he'll look down on you and improve your Attack for such insight.")
        // Usually it is given here, but I don't want loopholes.
        npc("By Saradomin's earlobes! You must be a friend of the gods indeed.")
        npc("Look in your backpack for your reward, in payment for your work.")
        exec { player, _ ->
            rewardXP(player, Skills.ATTACK, 875.0)
            addItemOrDrop(player, Items.UNCUT_SAPPHIRE_1623)
            finishQuest(player, Quests.OBSERVATORY_QUEST)
        }

        label("Cancer")
        player("Cancer!")
        exec { player, _ ->
            if (getAttribute<Int?>(player, ObservatoryQuest.attributeTelescopeStar, null) == 21) {
                loadLabel(player, "Cancercorrect")
            } else {
                loadLabel(player, "wrongstar")
            }
        }

        label("Cancercorrect")
        npc("That's exactly it!")
        player("Yes! Woo hoo!")
        npc("That's Cancer, the crab.")
        npc("An armoured creature - I think I shall reward you with an amulet of protection.")
        // Usually it is given here, but I don't want loopholes.
        npc("By Saradomin's earlobes! You must be a friend of the gods indeed.")
        npc("Look in your backpack for your reward, in payment for your work.")
        exec { player, _ ->
            addItemOrDrop(player, Items.AMULET_OF_DEFENCE_1729)
            addItemOrDrop(player, Items.UNCUT_SAPPHIRE_1623)
            finishQuest(player, Quests.OBSERVATORY_QUEST)
        }

        label("Capricorn")
        player("Capricorn!")
        exec { player, _ ->
            if (getAttribute<Int?>(player, ObservatoryQuest.attributeTelescopeStar, null) == 22) {
                loadLabel(player, "Capricorncorrect")
            } else {
                loadLabel(player, "wrongstar")
            }
        }

        label("Capricorncorrect")
        npc("That's exactly it!")
        player("Yes! Woo hoo!")
        npc("That's Capricorn, the goat.")
        npc("Capricorn will surely reward your insight with an increase to your Strength.")
        // Usually it is given here, but I don't want loopholes.
        npc("By Saradomin's earlobes! You must be a friend of the gods indeed.")
        npc("Look in your backpack for your reward, in payment for your work.")
        exec { player, _ ->
            rewardXP(player, Skills.STRENGTH, 875.0)
            addItemOrDrop(player, Items.UNCUT_SAPPHIRE_1623)
            finishQuest(player, Quests.OBSERVATORY_QUEST)
        }

        label("Gemini")
        player("Gemini!")
        exec { player, _ ->
            if (getAttribute<Int?>(player, ObservatoryQuest.attributeTelescopeStar, null) == 23) {
                loadLabel(player, "Geminicorrect")
            } else {
                loadLabel(player, "wrongstar")
            }
        }

        label("Geminicorrect")
        npc("That's exactly it!")
        player("Yes! Woo hoo!")
        npc("That's Gemini, the twins.")
        npc("With the double nature of Gemini, I can't offer you anything more suitable than a two-handed weapon.")
        // Usually it is given here, but I don't want loopholes.
        npc("By Saradomin's earlobes! You must be a friend of the gods indeed.")
        npc("Look in your backpack for your reward, in payment for your work.")
        exec { player, _ ->
            addItemOrDrop(player, Items.BLACK_2H_SWORD_1313)
            addItemOrDrop(player, Items.UNCUT_SAPPHIRE_1623)
            finishQuest(player, Quests.OBSERVATORY_QUEST)
        }

        label("Leo")
        player("Leo!")
        exec { player, _ ->
            if (getAttribute<Int?>(player, ObservatoryQuest.attributeTelescopeStar, null) == 24) {
                loadLabel(player, "Leocorrect")
            } else {
                loadLabel(player, "wrongstar")
            }
        }

        label("Leocorrect")
        npc("That's exactly it!")
        player("Yes! Woo hoo!")
        npc("That's Leo, the lion.")
        npc("I think the majestic power of the lion will raise your Hitpoints.")
        // Usually it is given here, but I don't want loopholes.
        npc("By Saradomin's earlobes! You must be a friend of the gods indeed.")
        npc("Look in your backpack for your reward, in payment for your work.")
        exec { player, _ ->
            rewardXP(player, Skills.HITPOINTS, 875.0)
            addItemOrDrop(player, Items.UNCUT_SAPPHIRE_1623)
            finishQuest(player, Quests.OBSERVATORY_QUEST)
        }

        label("Libra")
        player("Libra!")
        exec { player, _ ->
            if (getAttribute<Int?>(player, ObservatoryQuest.attributeTelescopeStar, null) == 25) {
                loadLabel(player, "Libracorrect")
            } else {
                loadLabel(player, "wrongstar")
            }
        }

        label("Libracorrect")
        npc("That's exactly it!")
        player("Yes! Woo hoo!")
        npc("That's Libra, the scales.")
        npc("Hmmm, balance, law, order - I shall award you with law runes!")
        // Usually it is given here, but I don't want loopholes.
        npc("By Saradomin's earlobes! You must be a friend of the gods indeed.")
        npc("Look in your backpack for your reward, in payment for your work.")
        exec { player, _ ->
            addItemOrDrop(player, Items.LAW_RUNE_563, 25)
            addItemOrDrop(player, Items.UNCUT_SAPPHIRE_1623)
            finishQuest(player, Quests.OBSERVATORY_QUEST)
        }

        label("Pisces")
        player("Pisces!")
        exec { player, _ ->
            if (getAttribute<Int?>(player, ObservatoryQuest.attributeTelescopeStar, null) == 26) {
                loadLabel(player, "Piscescorrect")
            } else {
                loadLabel(player, "wrongstar")
            }
        }

        label("Piscescorrect")
        npc("That's exactly it!")
        player("Yes! Woo hoo!")
        npc("That's Pisces, the fish.")
        npc("What's more suitable as a reward than some tuna?")
        // Usually it is given here, but I don't want loopholes.
        npc("By Saradomin's earlobes! You must be a friend of the gods indeed.")
        npc("Look in your backpack for your reward, in payment for your work.")
        exec { player, _ ->
            addItemOrDrop(player, Items.TUNA_361, 3)
            addItemOrDrop(player, Items.UNCUT_SAPPHIRE_1623)
            finishQuest(player, Quests.OBSERVATORY_QUEST)
        }

        label("Sagittarius")
        player("Sagittarius!")
        exec { player, _ ->
            if (getAttribute<Int?>(player, ObservatoryQuest.attributeTelescopeStar, null) == 27) {
                loadLabel(player, "Sagittariuscorrect")
            } else {
                loadLabel(player, "wrongstar")
            }
        }

        label("Sagittariuscorrect")
        npc("That's exactly it!")
        player("Yes! Woo hoo!")
        npc("That's Sagittarius, the centaur.")
        npc("As you've spotted the archer, I shall reward you with a maple longbow.")
        // Usually it is given here, but I don't want loopholes.
        npc("By Saradomin's earlobes! You must be a friend of the gods indeed.")
        npc("Look in your backpack for your reward, in payment for your work.")
        exec { player, _ ->
            addItemOrDrop(player, Items.MAPLE_LONGBOW_851)
            addItemOrDrop(player, Items.UNCUT_SAPPHIRE_1623)
            finishQuest(player, Quests.OBSERVATORY_QUEST)
        }

        label("Scorpio")
        player("Scorpio!")
        exec { player, _ ->
            if (getAttribute<Int?>(player, ObservatoryQuest.attributeTelescopeStar, null) == 28) {
                loadLabel(player, "Scorpiocorrect")
            } else {
                loadLabel(player, "wrongstar")
            }
        }

        label("Scorpiocorrect")
        npc("That's exactly it!")
        player("Yes! Woo hoo!")
        npc("That's Scorpio, the scorpion.")
        npc("I think weapon poison would make a suitable reward.")
        // Usually it is given here, but I don't want loopholes.
        npc("By Saradomin's earlobes! You must be a friend of the gods indeed.")
        npc("Look in your backpack for your reward, in payment for your work.")
        exec { player, _ ->
            addItemOrDrop(player, Items.WEAPON_POISON_187)
            addItemOrDrop(player, Items.UNCUT_SAPPHIRE_1623)
            finishQuest(player, Quests.OBSERVATORY_QUEST)
        }

        label("Taurus")
        player("Taurus!")
        exec { player, _ ->
            if (getAttribute<Int?>(player, ObservatoryQuest.attributeTelescopeStar, null) == 29) {
                loadLabel(player, "Tauruscorrect")
            } else {
                loadLabel(player, "wrongstar")
            }
        }

        label("Tauruscorrect")
        npc("That's exactly it!")
        player("Yes! Woo hoo!")
        npc("That's Taurus, the bull.")
        npc("This Strength potion should be a suitable reward.")
        // Usually it is given here, but I don't want loopholes.
        npc("By Saradomin's earlobes! You must be a friend of the gods indeed.")
        npc("Look in your backpack for your reward, in payment for your work.")
        exec { player, _ ->
            addItemOrDrop(player, Items.SUPER_STRENGTH1_161)
            addItemOrDrop(player, Items.UNCUT_SAPPHIRE_1623)
            finishQuest(player, Quests.OBSERVATORY_QUEST)
        }

        label("Virgo")
        player("Virgo!")
        exec { player, _ ->
            if (getAttribute<Int?>(player, ObservatoryQuest.attributeTelescopeStar, null) == 30) {
                loadLabel(player, "Virgocorrect")
            } else {
                loadLabel(player, "wrongstar")
            }
        }

        label("Virgocorrect")
        npc("That's exactly it!")
        player("Yes! Woo hoo!")
        npc("That's Virgo, the virtuous.")
        npc("Virgo will surely provide you with an increase to Defence.")
        // Usually it is given here, but I don't want loopholes.
        npc("By Saradomin's earlobes! You must be a friend of the gods indeed.")
        npc("Look in your backpack for your reward, in payment for your work.")
        exec { player, _ ->
            rewardXP(player, Skills.DEFENCE, 875.0)
            addItemOrDrop(player, Items.UNCUT_SAPPHIRE_1623)
            finishQuest(player, Quests.OBSERVATORY_QUEST)
        }

        label("wrongstar")
            npc("I'm afraid not. Have another look. Remember, you can check the star charts on the walls for reference.")

        // http://youtu.be/Z5RRnZl2vTg
        label("observatoryqueststage100")
            npc("Thanks for all your help with the telescope. What can I do for you?")
            options(
                DialogueOption("needmorehelp", "Do you need any more help with the telescope?", expression = FacialExpression.ASKING),
                DialogueOption("mambodunaroona", "Is it true your name is Mambo-duna-roona?", expression = FacialExpression.ASKING) { player, _ ->
                    return@DialogueOption getAttribute(player, attributeReceivedWine, false)
                },
                DialogueOption("nevermind", "Nothing, thanks."),
            )

        label("needmorehelp")
            npc("Not right now,")
            npc("but the stars may hold a secret for you.")

        label("nevermind")
            npc("Okay, no problem. See you again.")

        label("treasuretrails")
            npc("Welcome back! How can I help you today?")
            player("Can you teach me to solve treasure trail clues?")
            npc("Ah, I get asked about treasure trails all the time! Listen carefully and I shall tell you what I know...")
            npc("Lots of clues have <col=8A0808>degrees</col> and <col=8A0808>minutes</col> written on them. These are the coordinates of the place where the treasure is buried.")
            npc("You have to walk to the correct spot, so that your coordinates are exactly the same as the values written on the clue scroll.")
            npc("To do this, you must use a <col=8A0808>sextant</col>, a <col=8A0808>watch</col> and a <col=8A0808>chart</col> to find your own coordinates.")
            npc("Once you know the coordinates of the place where you are, you know which way you have to walk to get to the place where the treasure is!")
            player("Riiight. So where do I get those items from?")
            npc("I think Murphy, the owner of the fishing trawler moored south of Ardougne, might be able to spare you a sextant. Then the nearest Clock Tower is south of Ardougne - you could probably get a watch there. I've")
            npc("got plenty of charts myself, here have one.")

        label("mambodunaroona")
            npc(FacialExpression.AMAZED, "How do you know tha-")
            npc(FacialExpression.SUSPICIOUS, "I mean, of course not, what a silly idea.")
    }
}