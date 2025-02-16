package content.region.desert.quest.deserttreasure

import core.api.*
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs

@Initializable
class EblisDialogue(player: Player? = null) : DialoguePlugin(player){
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player!!, EblisDialogueFile(), npc)
        return false
    }
    override fun newInstance(player: Player?): DialoguePlugin {
        return EblisDialogue(player)
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.EBLIS_1923)
    }

}
class EblisDialogueFile : DialogueBuilderFile() {

    companion object {
        fun checkAllGiven(player: Player): Boolean {
            return (getAttribute(player, DesertTreasure.attributeCountMagicLogs, 0) >= 12 &&
                    getAttribute(player, DesertTreasure.attributeCountSteelBars, 0) >= 6 &&
                    getAttribute(player, DesertTreasure.attributeCountMoltenGlass, 0) >= 6 &&
                    getAttribute(player, DesertTreasure.attributeCountBones, 0) >= 1 &&
                    getAttribute(player, DesertTreasure.attributeCountAshes, 0) >= 1 &&
                    getAttribute(player, DesertTreasure.attributeCountCharcoal, 0) >= 1 &&
                    getAttribute(player, DesertTreasure.attributeCountBloodRune, 0) >= 1)
        }
    }

    override fun create(b: DialogueBuilder) {

        b.onQuestStages(DesertTreasure.questName, 0,1,2,3,4)
                .npcl("Leave us to our fate. We care nothing for the world that betrayed us, or those that come from it.")
                .end()

        b.onQuestStages(DesertTreasure.questName, 5,6)
                .playerl("Hello. I represent the Museum of Varrock, and I have reason to believe there may be some kinds of artefacts of historical significance in the nearby area...")
                .npcl("Ah yes. The only time people care about our existence is when they think they have something to gain from us.")
                .npcl("I have nothing to say to you. You and your kind are not welcome here.")
                .playerl("Please, if I can just have a few minutes of your time to ask some questions...?")
                .npcl("(sigh) I suppose I can spare you that. What do you wish to know about?")
                .options()
                .let { optionBuilder ->
                    optionBuilder.option("Why is this village so hostile?")
                            .playerl("Why are all of the people here so hostile? You would think I was asking you for money instead of just for answers to a few questions...")
                            .npcl("It is a long story, and I doubt you have much interest in hearing it. Your sort never are, you just take what you can of ours, and then abandon us once more to the desert.")
                            .options()
                            .let { optionBuilder2 ->
                                optionBuilder2.option("No, I want to hear this story.")
                                        .playerl("Actually, I'd be quite interested to hear what it is you have to say to excuse the attitude everybody in this village seems to have.")
                                        .npcl("Ah, it all begun many generations ago, when our ancestors were the proud rulers of these lands...")
                                        .npcl("My ancestors lived far to the North of here, and our lands stretched from the sea in the East to the river Lum, and the mountain of ice. From coast to coast, North to South, our domain was absolute.")
                                        .npcl("Our god was kind to us, and blessed us with prosperity and happiness, and in return we were merciless to his enemies wherever we found them.")
                                        .npcl("Then came the betrayal.")
                                        .npcl("Our god was banished, leaving us helpless to our fates.")
                                        .npcl("Without his protection, we were forced to fend for ourselves once more, against the enemies that sought to destroy us through their petty jealousies.")
                                        .npcl("But we did not succumb without fighting! The spiteful Saradomin and pathetic Zamorak warred with each other, but the hatred they had for each other was as nothing to the hatred they held towards us!")
                                        .npcl("With each battle they waged, we lost more and more land, unable to fight on all fronts, and were pushed further and further South into this gods-forsaken desert.")
                                        .npcl("Our greatest hero, Azzanadra, was finally trapped in a strange stone structure to the South of here, and bound within by terrible powers...")
                                        .npcl("And with that our lands, our homes, our very lives were stolen from us! Too weak to reclaim what was rightfully ours, we made our homes here, knowing that someday Azzanadra will")
                                        .npcl("return with his magnificent power, and bring us back to our former glory...")
                                        .playerl("So you're upset because of something that happened hundreds of years ago?")
                                        .playerl("Seems to me like maybe you should find some closure, and let the past go...")
                                        .npcl("The insults heaped upon my race will never be forgotten, will never be forgiven and will never again be overlooked.")
                                        .npcl("Someday, a harsh wind will blow upon this land, uncovering the wrongs of the past, and we will get back what is rightfully ours. Until such a day we will bide our time here, and will")
                                        .npcl("always be ready with our blades for our righteous vengeance.")
                                        .end()

                                optionBuilder2.option("I don't care about your story.")
                                        .playerl("I don't really care what your story is to be honest, there is no excuse for such rudeness or hostility.")
                                        .playerl("I have done nothing wrong to you, but everybody here treats me like I have committed some great crime against the village.")
                                        .npcl("That is because, from our point of view, you have.")
                                        .playerl("What? Just because I entered your village?")
                                        .npcl("You have no right to be here! You have no right to the life you have, for it was taken at our expense!")
                                        .playerl("Whatever... No wonder all you loonies live out here in the desert by yourselves.")
                                        .end()
                            }

                    optionBuilder.option("Do you know anything about treasure near here?")
                            .playerl("I was wondering if you knew anything about some treasure somewhere around here?")
                            .playerl("I have some evidence that there might be some kind of treasure hidden very close to this village...")
                            .npcl("If I knew of any treasure I would not choose to spend my life in this gods-forsaken desert.")
                            .end()

                    optionBuilder.option("Do you know anything about a fortress near here?")
                            .playerl("Do you know anything about some kind of fortress nearby? I have reason to believe there is, or at least used to be, some kind of fortress very close to here...")
                            .npcl("Nobody would build anything in this wasteland unless they were forced to, to survive.")
                            .npcl("I know of no fortress, I know of no reason why anyone would ever bother doing anything out here in the desert.")
                            .end()


                    // This will only show up after you've talked to the bartender.
                    optionBuilder.optionIf("Tell me of the four diamonds of Azzanadra.")  { player ->
                        return@optionIf getQuestStage(player, DesertTreasure.questName) == 6
                    }
                            .playerl("So tell me... Did you ever hear of something called the Diamonds of Azzanadra?")
                            .npcl("This is the treasure which you seek???")
                            .npcl("Please accept my apologies noble @g[sir,madam]! I thought you were but some opportunistic thief, looking to steal what heritage we have left! Now I see that you are in fact a brave adventurer,")
                            .npcl("looking to restore our glories back upon us!")
                            .playerl("Uh... yeah... So anyway, you have heard of them?")
                            .npcl("Heard of them? Of course I have heard of them! They are the legacy of the great Mahjarrat hero, Azzanadra!")
                            .playerl("So... do you have any idea where they might be? I have a feeling they will be very valuable.")
                            .playerl("Uh, valuable as historical artefacts I mean, obviously.")
                            .npcl("They were stolen by warriors of the false god Zamorak generations ago. When you find the warriors, you will find the diamonds.")
                            .npcl("I suspect they will not willingly part with such objects of power however.")
                            .npc("Beware too, for these warriors are very powerful;", "they have taken the powers of the diamonds into themselves!")
                            .playerl("How do you mean?")
                            .npcl("Each diamond has an elemental quality...")
                            .npcl("There is the Diamond of Blood, the Diamond of Ice, the Diamond of Smoke and the Diamond of Shadow.")
                            .npcl("You should expect the warriors to have taken some aspect of these diamonds as their own...")
                            .playerl("Do you have any idea how I could track down these warriors somehow, then?")
                            .npcl("There is an ancient spell I know of that may spy upon such power... But it will require a few ingredients for it to work.")
                            .npcl("Should you be willing to get these ingredients for me, I will be able to locate the rough area where each of these warriors has taken refuge. The spell is imprecise, but it should help you get on the")
                            .npcl("right track in your search.")
                            .npcl("Is your desire for our freedom strong enough? Will you gather the ingredients for this spell for me?")
                            .options()
                            .let { optionBuilder2 ->

                                optionBuilder2.option("Yes")
                                        // The quest should jump to the next stage here, but I didn't want to write some weird if else here.
                                        .playerl("Sure, what do you need?")
                                        .npcl("For this spell, I will need to make some scrying glasses. I will need enough so that we can view the realm in its entirety.")
                                        .npcl("When enchanted, the scrying glass will be able to let us view any area that has been influenced by the presence of the Diamonds of Azzanadra.")
                                        .playerl("Okay, but what exactly do you need for this spell?")
                                        .npcl("Well, six scrying glasses should be sufficient. For each scrying glass, I will need two magic logs, a steel bar and some molten glass. This makes a total of 12 magic logs, 6 pieces of molten")
                                        .npcl("glass, and 6 steel bars.")
                                        .npcl("In addition, for the actual spell to enchant the glasses, I will require one set of normal bones, some ash, some charcoal and a single blood rune.")
                                        .npcl("Do you understand me, adventurer?")
                                        .playerl("Quick question; what kind of bones do you need?")
                                        .npcl("Standard bones. Other types of bones are of no use to me in this spell.")
                                        .options()
                                        .let { optionBuilder3 ->
                                            optionBuilder3.option("Yes, I will go get those for you.")
                                                    .playerl("It's a slightly odd collection of ingredients, but I shouldn't have too much trouble getting those for you.")
                                                    .endWith { _, player ->
                                                        if(getQuestStage(player, DesertTreasure.questName) == 6) {
                                                            setQuestStage(player, DesertTreasure.questName, 7)
                                                        }
                                                    }

                                            optionBuilder3.option("No, please repeat those ingredients.")
                                                    .npcl("Before I can complete the spell I will still need the following items;")
                                                    .npc("12 magic logs", "6 steel bars", "6 molten glass")
                                                    .npc("1 bones,", "1 ashes,", "1 charcoal", "and 1 blood rune.") // This is sic authentic trash dialogue.
                                                    .endWith { _, player ->
                                                        if(getQuestStage(player, DesertTreasure.questName) == 6) {
                                                            setQuestStage(player, DesertTreasure.questName, 7)
                                                        }
                                                    }

                                        }

                                optionBuilder2.option("No")
                                        .playerl("Actually I don't feel like going on a shopping trip for you right now.")
                                        .npcl("As you wish. I should have known not to get my hopes up that our long cursed life may soon be at an end...")
                                        .end()
                            }

                    optionBuilder.option("Nothing thanks.")
                            .playerl("Actually, there was nothing I really wanted to ask you about.")
                            .npcl("Yes, it is exactly like your sort to waste my time in such a way.")
                            .end()
                }

        b.onQuestStages(DesertTreasure.questName, 7)
                // Branch to check

                .branch { player ->
                    return@branch if (checkAllGiven(player)) { 1 } else { 0 }
                }.let { branch ->
                    branch.onValue(1)
                            .npcl("Excellent! Those are all the ingredients I need to create the scrying glasses.")
                            .npcl("I will find a suitable spot in the desert to the East of here, and set them up. When you are ready to begin your search, please come and find me there, I will show you how to utilise the")
                            .npcl("mirrors to find the diamonds.")
                            .endWith { _, player ->
                                if(getQuestStage(player, DesertTreasure.questName) == 7) {
                                    setQuestStage(player, DesertTreasure.questName, 8)
                                }
                            }

                    branch.onValue(0)
                            .npcl("Before I can complete the spell I will still need the following items;")
                            .manualStage { df, player, _, _ ->
                                df.interpreter!!.sendDialogues(npc!!.id, FacialExpression.NEUTRAL,
                                        "" + (12 - getAttribute(player, DesertTreasure.attributeCountMagicLogs, 0)) + " magic logs",
                                        "" + (6 - getAttribute(player, DesertTreasure.attributeCountSteelBars, 0)) + " steel bars",
                                        "" + (6 - getAttribute(player, DesertTreasure.attributeCountMoltenGlass, 0)) + " molten glass")
                            }
                            .manualStage { df, player, _, _ ->
                                df.interpreter!!.sendDialogues(npc!!.id, FacialExpression.NEUTRAL,
                                        "" + (1 - getAttribute(player, DesertTreasure.attributeCountBones, 0)) + " bones,",
                                        "" + (1 - getAttribute(player, DesertTreasure.attributeCountAshes, 0)) + " ashes,",
                                        "" + (1 - getAttribute(player, DesertTreasure.attributeCountCharcoal, 0)) + " charcoal",
                                        "and " + (1 - getAttribute(player, DesertTreasure.attributeCountBloodRune, 0)) + " blood rune.") // This is sic authentic trash dialogue.
                            }
                            .end()
                }

        b.onQuestStages(DesertTreasure.questName, 8,9,10)
                .npcl("Meet me again in the desert East of here, I will use these ingredients to create a scrying glass for you.")
                .end()

        b.onQuestStages(DesertTreasure.questName, 100)
                .npcl("Meet me again in the desert East of here.")
                .end()

    }
}

class EblisCollectionsListeners : InteractionListener {
    override fun defineListeners() {

        onUseWith(IntType.NPC, intArrayOf(Items.MAGIC_LOGS_1513, Items.MAGIC_LOGS_1514), NPCs.EBLIS_1923) { player, used, with ->
            for(i in 0..11) {
                if (inInventory(player, used.id)) {
                    if (getAttribute(player, DesertTreasure.attributeCountMagicLogs, 0) < 12) {
                        if (removeItem(player, used.id)) {
                            setAttribute(player, DesertTreasure.attributeCountMagicLogs,
                                    getAttribute(player, DesertTreasure.attributeCountMagicLogs, 0) + 1)
                            sendMessage(player, "You hand over a magic log.")
                        }
                    } else {
                        break
                    }
                } else {
                    break
                }
            }
            return@onUseWith true
        }

        onUseWith(IntType.NPC, intArrayOf(Items.STEEL_BAR_2353, Items.STEEL_BAR_2354), NPCs.EBLIS_1923) { player, used, with ->
            for(i in 0..5) {
                if (inInventory(player, used.id)) {
                    if (getAttribute(player, DesertTreasure.attributeCountSteelBars, 0) < 6) {
                        if (removeItem(player, used.id)) {
                            setAttribute(player, DesertTreasure.attributeCountSteelBars,
                                    getAttribute(player, DesertTreasure.attributeCountSteelBars, 0) + 1)
                            sendMessage(player, "You hand over a steel bar.")
                        }
                    } else {
                        break
                    }
                } else {
                    break
                }
            }
            return@onUseWith true
        }

        onUseWith(IntType.NPC, intArrayOf(Items.MOLTEN_GLASS_1775, Items.MOLTEN_GLASS_1776), NPCs.EBLIS_1923) { player, used, with ->
            for(i in 0..5) {
                if (inInventory(player, used.id)) {
                    if (getAttribute(player, DesertTreasure.attributeCountMoltenGlass, 0) < 6) {
                        if (removeItem(player, used.id)) {
                            setAttribute(player, DesertTreasure.attributeCountMoltenGlass,
                                    getAttribute(player, DesertTreasure.attributeCountMoltenGlass, 0) + 1)
                            sendMessage(player, "You hand over some molten glass.")
                        }
                    } else {
                        break
                    }
                } else {
                    break
                }
            }
            return@onUseWith true
        }

        onUseWith(IntType.NPC, intArrayOf(Items.BONES_526, Items.BONES_527), NPCs.EBLIS_1923) { player, used, with ->
            if (getAttribute(player, DesertTreasure.attributeCountBones, 0) < 1) {
                if (removeItem(player, used.id)) {
                    setAttribute(player, DesertTreasure.attributeCountBones,
                            getAttribute(player, DesertTreasure.attributeCountBones, 0) + 1)
                    sendNPCDialogue(player, NPCs.EBLIS_1923, "Thank you, those are enough bones for the spell.")
                }
            }
            return@onUseWith true
        }

        onUseWith(IntType.NPC, intArrayOf(Items.ASHES_592, Items.ASHES_593), NPCs.EBLIS_1923) { player, used, with ->
            if (getAttribute(player, DesertTreasure.attributeCountAshes, 0) < 1) {
                if (removeItem(player, used.id)) {
                    setAttribute(player, DesertTreasure.attributeCountAshes,
                            getAttribute(player, DesertTreasure.attributeCountAshes, 0) + 1)
                    sendNPCDialogue(player, NPCs.EBLIS_1923, "Thank you, that is enough ash for the spell.")
                }
            }
            return@onUseWith true
        }

        onUseWith(IntType.NPC, intArrayOf(Items.CHARCOAL_973, Items.CHARCOAL_974), NPCs.EBLIS_1923) { player, used, with ->
            if (getAttribute(player, DesertTreasure.attributeCountCharcoal, 0) < 1) {
                if (removeItem(player, used.id)) {
                    setAttribute(player, DesertTreasure.attributeCountCharcoal,
                            getAttribute(player, DesertTreasure.attributeCountCharcoal, 0) + 1)
                    sendNPCDialogue(player, NPCs.EBLIS_1923, "Thank you, that is enough charcoal for the spell.")
                }
            }
            return@onUseWith true
        }

        onUseWith(IntType.NPC, intArrayOf(Items.BLOOD_RUNE_565), NPCs.EBLIS_1923) { player, used, with ->
            if (getAttribute(player, DesertTreasure.attributeCountBloodRune, 0) < 1) {
                if (removeItem(player, used.id)) {
                    setAttribute(player, DesertTreasure.attributeCountBloodRune,
                            getAttribute(player, DesertTreasure.attributeCountBloodRune, 0) + 1)
                    sendNPCDialogue(player, NPCs.EBLIS_1923, "Thank you, that blood rune should be sufficient for the spell.")
                }
            }
            return@onUseWith true
        }

    }
}