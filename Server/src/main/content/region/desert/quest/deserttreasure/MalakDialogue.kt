package content.region.desert.quest.deserttreasure

import core.api.*
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.DialoguePlugin
import core.game.node.entity.combat.ImpactHandler.HitsplatType
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs

@Initializable
class MalakDialogue(player: Player? = null) : DialoguePlugin(player){
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player!!, MalakDialogueFile(), npc)
        return false
    }
    override fun newInstance(player: Player?): DialoguePlugin {
        return MalakDialogue(player)
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MALAK_1920)
    }
}

class MalakDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        b.onQuestStages(DesertTreasure.questName, 0,1,2,3,4,5,6,7,8)
                .npc("Away from me, dog.", "I have business to discuss with the barkeeper.")
                .end()


        b.onQuestStages(DesertTreasure.questName, 9)
                .branch { player ->
                    if (DesertTreasure.getSubStage(player, DesertTreasure.attributeBloodStage) == 1) {
                        return@branch 0 // Same branch as 0
                    }
                    return@branch DesertTreasure.getSubStage(player, DesertTreasure.attributeBloodStage)
                }.let { branch ->
                    branch.onValue(0)
                            .npcl("A human, eh? Give me one good reason why I should not just take you to Lord Drakan now.")
                            // There's a ring of charos dialogue here, but its one line and doesn't change anything...
                            .npcl("You had better make it a good one too, or you will not survive the night, I'll wager.")
                            .let { builder ->
                                val returnJoin = b.placeholder()
                                builder.goto(returnJoin)
                                return@let returnJoin.builder()
                                        .options()
                                        .let { optionBuilder ->
                                            val continuePath = b.placeholder()
                                            optionBuilder.option("I am here to worship Zamorak")
                                                    .playerl("I am here to worship the almighty Zamorak! Yay! Go Zamorak!")
                                                    .npcl("I see. You are a moron. You have probably settled right in with the rest of the idiots in this pathetic excuse for a village.")
                                                    .npcl("Unfortunately for you, that is not a good enough reason to explain your presence here.")
                                                    .npcl("Now tell me the reason for your coming here, or I will ensure you suffer a horrible fate indeed.")
                                                    .goto(returnJoin)

                                            optionBuilder.option("I am here to praise Lord Drakan")
                                                    .playerl("I am here only to serve the mighty Drakan. Yup, Drakan, he's the man.")
                                                    .npcl("I see. I would perhaps be more inclined to believe you if I could not smell the death blood of his brother Draynor upon you.")
                                                    .npcl("What are you real intentions here?")
                                                    // "If the player has not completed Vampyre Slayer" but I'm lazy again
                                                    // .npcl("Really? That is interesting, that you would want to give your life by tresspassing in this land for such an unbelievable reason.")
                                                    // .npcl("Now speak, what is your purpose in coming here?")
                                                    .goto(returnJoin)

                                            optionBuilder.option("I am here to worship you, oh mighty Malak")
                                                    .playerl("I am here only to serve the mighty Drakan.")
                                                    .playerl("I came here looking for you, oh mighty Malak, so that I might serve your glory.")
                                                    .npcl("Please. Do not think that I am so vain and foolish as to allow you to avoid my question with such obvious sycophancy.")
                                                    .npcl("Now tell me the reason behind your being here, or I will ensure that you suffer.")
                                                    .goto(returnJoin)

                                            optionBuilder.option("I am here to kill Lord Drakan")
                                                    .playerl("I am here to kill Lord Drakan, and every stinking one of his vampyre brood!")
                                                    .npcl("Hah! Most entertaining, human!")
                                                    .npcl("Now tell me the reason you are here, or we shall soon see who will be killing whom.")
                                                    .goto(returnJoin)

                                            optionBuilder.option("I am looking for a special Diamond...")
                                                    .playerl("I am here looking for a special diamond... I have reason to believe it is somewhere in this vicinity, and it is probably in the possession of a warrior of Zamorak.")
                                                    .playerl("I'm fairly sure it will have some kind of magical aura or something too. I don't suppose you've seen it, or know where it might be?")
                                                    .npcl("Interesting... Well perhaps we can come to a little... arrangement, human.")
                                                    .npcl("I may have information that may assist you, but you in turn will have to do something for me. What do you say? Do you think we could come to some form of")
                                                    .npcl("agreement?")
                                                    .playerl("Well, what kind of something? No offence, but you're not exactly the trustworthy type...")
                                                    .npcl("Ah, you have a healthy sense of paranoia, I see. It is not a particularly unfair request on my part...")
                                                    .npcl("All I ask is that you ensure that the current owner of the diamond is killed. For my part, I will let you know his whereabouts, and how exactly to kill him.")
                                                    .npcl("When he is dead, you may take the diamond from his corpse and do with it what you will. I have no interest in such baubles.")
                                                    .npcl("So what say you? A life for a diamond. As a mark of good faith, I will give you some information free:")
                                                    .npcl("The current owner of this diamond is named Dessous.")
                                                    .betweenStage { df, player, _, _ ->
                                                        if (DesertTreasure.getSubStage(player, DesertTreasure.attributeBloodStage) == 0) {
                                                            DesertTreasure.setSubStage(player, DesertTreasure.attributeBloodStage, 1)
                                                        }
                                                    }
                                                    .options("Agree to this arrangement?")
                                                    .let { optionBuilder2 ->
                                                        optionBuilder2.option("Yes")
                                                                .betweenStage { df, player, _, _ ->
                                                                    if (DesertTreasure.getSubStage(player, DesertTreasure.attributeBloodStage) == 1) {
                                                                        DesertTreasure.setSubStage(player, DesertTreasure.attributeBloodStage, 2)
                                                                    }
                                                                }
                                                                .playerl("Well... I can't see any drawback. Okay, I accept your offer. Now tell me what you know.")
                                                                .goto(continuePath)

                                                        optionBuilder2.option("No")
                                                                .playerl("I don't trust you, or anything you say. I'm afraid I have to decline.")
                                                                .npcl("As you wish human. I doubt anybody near here knows of the diamond you seek, however.")
                                                                .npcl("If you wish to claim it as your own, you have little choice but to accept my bargain. I will wait here until such time as you change your mind.")
                                                                .end()
                                                    }
                                            return@let continuePath.builder()
                                        }
                            }
                            .npcl("What I know? Hah! After you have been alive for as long as I, the things I know are enough to fill a library.")
                            .npcl("I'm afraid you will need to be a little more specific.")
                            .let { builder ->
                                val returnJoin = b.placeholder()
                                builder.goto(returnJoin)
                                returnJoin.builder()
                                        .options()
                                        .let { optionBuilder ->
                                            optionBuilder.option("Why do you want Dessous dead?")
                                                    .playerl("I don't see exactly how this bargain benefits you... Why exactly do you want Dessous killed anyway?")
                                                    .npcl("That is an impertinent question to demand from myself.")
                                                    .npcl("However, if it will help seal our deal, I will let you know some of the details.")
                                                    .npcl("As you may or may not know, myself and my blood kin are the rulers of this land, all serving under Lord Drakan. However, we tend not to die of old age or similar")
                                                    .npcl("natural causes, which means that to gain another Lords tithe or land, there often need to be...")
                                                    .npcl("Unnatural causes of death involved. Let us just say, that Dessous is in control of some land that I myself would like some say in, and that it is not in my interest to be seen to be responsible for the")
                                                    .npcl("death of a fellow Lord.")
                                                    .npcl("It would however be extremely advantageous to myself should some random human adventurer take it upon themself to remove this rival for me... Do you understand?")
                                                    .playerl("Yes... I think so...")
                                                    .npcl("Good, we understand each other then. We will both benefit from the death of Dessous.")
                                                    .goto(returnJoin)

                                            optionBuilder.option("Where can I find Dessous?")
                                                    .playerl("Where can I find this Dessous?")
                                                    .npcl("He currently resides in a graveyard to the South-East of here. You will not be able to move the gravestone which he lies beneath however, you will need to find some way to")
                                                    .npcl("lure him out from his tomb.")
                                                    .playerl("And how exactly would I go about doing that?")
                                                    .npcl("Well, he is a vampyre, so fresh blood would almost certainly entice him out.")
                                                    .npcl("However, even though he is a frail and decrepit example of our species, he will be able to kill a weakling human such as yourself extremely easily. Having him in a bloodlust as he does so, will not make")
                                                    .npcl("your job any easier.")
                                                    .goto(returnJoin)

                                            optionBuilder.option("How can I kill Dessous?")
                                                    .playerl("So what advice can you give me on killing Dessous?")
                                                    .npcl("As ancient and weak as Dessous is, he is still more than a match for the likes of you.")
                                                    .npcl("That is, assuming you were to fight him fairly.")
                                                    .npcl("My proposal would be for you to even the odds up a little bit...")
                                                    .playerl("How would I go about doing that?")
                                                    .npcl("Well, my plan would be as follows. First, take a silver bar to the man living in the sewers in Draynor. He was an assistant to Count Draynor in some of his...")
                                                    .npcl("more interesting experiments many years past. Tell him you need a sacrificial offering pot. He will know what you speak of, it is a unique type of container used in various ancient vampyric ceremonies.")
                                                    .npcl("Then take the pot to Entrana and get it blessed by the Head Monk. This will lend the pot some holy power.")
                                                    .npcl("If you then bring that silver pot back to me, I will provide you with some fresh blood, to put into it. To that pot of blood, you will add some crushed garlic, and some spice to disguise the smell.")
                                                    .npcl("Use that pot of blood upon Dessous' tomb, and he will be unable to resist rising and drinking from it.")
                                                    .npcl("The combination of garlic, silver, and blessings from Saradomin will act upon him as a poison, and allow you to kill him.")
                                                    .npcl("This is just my suggestion of course, you may ignore it if you wish, although I offer no guarantees of your ability to defeat him otherwise.")
                                                    .npcl("Was there anything else you wanted?")
                                                    .goto(returnJoin)

                                            optionBuilder.option("Actually, I don't need to know anything.")
                                                    .playerl("Never mind, I will figure out all I need to know by myself.")
                                                    .npcl("As you wish. Come and see me when you have managed to kill Dessous.")
                                                    .end()
                                        }
                            }

                    branch.onValue(2)
                            .branch { player ->
                                return@branch if(inInventory(player, Items.BLESSED_POT_4659) || inInventory(player, Items.SILVER_POT_4658)) { 1 } else { 0}
                            }.let { branch2 ->
                                branch2.onValue(1)
                                        .player("I found Ruantun in Draynors sewers.", "He made me this pot, now where can I get some fresh", "blood to fill it with?")
                                        .betweenStage { df, player, _, _ ->
                                            sendMessage(player, "Malak cuts you and pours some of your blood into the pot.")
                                            if (removeItem(player, Items.SILVER_POT_4658)) {
                                                addItemOrDrop(player, Items.SILVER_POT_4660)
                                            } else if (removeItem(player, Items.BLESSED_POT_4659)) {
                                                addItemOrDrop(player, Items.BLESSED_POT_4661)
                                            }
                                            animate(npc!!, 1264)
                                            player.impactHandler.manualHit(player, 5, HitsplatType.NORMAL)
                                        }
                                        .linel("Malak cuts you and pours some of your blood into the pot.") // Supposed to be a sendMessage not a dialogue, but why...
                                        .playerl("Ow!")
                                        .npcl("There you go. As fresh as it gets.")
                                        .playerl("Thanks for nothing.")
                                        .npcl("Come and speak to me again when you have managed to kill Dessous.")
                                        .end()

                                branch2.onValue(0)
                                        .npcl("Why are you still here? I notice Dessous still lives.")
                                        .options()
                                        .let { optionBuilder ->
                                            optionBuilder.option("Where can I find Dessous?")
                                                    .playerl("Where can I find this Dessous?")
                                                    .npcl("He currently resides in a graveyard to the South-East of here. You will not be able to move the gravestone which he lies beneath however, you will need to find some way to")
                                                    .npcl("lure him out from his tomb.")
                                                    .playerl("And how exactly would I go about doing that?")
                                                    .npcl("Come and see my when you have prepared a silver ritual pot in the manner I have told you.")
                                                    .npcl("I will ensure that you get some fresh blood that you may taint with garlic and spices, to lure our Dessous.")
                                                    .end()

                                            optionBuilder.option("How do I kill Dessous again?")
                                                    .playerl("How am I supposed to kill Dessous again?")
                                                    .npcl("Take a silver bar to the man in the Draynor sewers. He will fashion a ritualistic pot for you, which you should then take to Entrana and get blessed.")
                                                    .npcl("When you have done that, come back here and speak to me, I will provide you with some fresh blood which you will then crush some garlic into, and then add some spices to hide the garlic.")
                                                    .npcl("In this way, you will be able to lure him from his tomb and he should be sufficiently weakened to be vulnerable to your attacks.")
                                                    .end()

                                            optionBuilder.option("Actually, I don't need to know anything.")
                                                    .playerl("Never mind, I will figure out all I need to know by myself.")
                                                    .npcl("As you wish. Come and see me when you have managed to kill Dessous.")
                                                    .end()
                                        }
                            }

                    branch.onValue(3)
                            .npcl("Ah, the wandering hero returns! I take it you have dispatched poor old Dessous for me?")
                            .playerl("Quit playing games with me, Malak. I want that diamond, and I want it now!")
                            .betweenStage { df, player, _, _ ->
                                if (DesertTreasure.getSubStage(player, DesertTreasure.attributeBloodStage) == 3) {
                                    addItemOrDrop(player, Items.BLOOD_DIAMOND_4670)
                                    DesertTreasure.setSubStage(player, DesertTreasure.attributeBloodStage, 100)
                                }
                            }
                            .npcl("Do not take that tone of voice with me, meat. You should be thankful I have allowed you your life.")
                            .npcl("Here, take your precious little bauble.")
                            .npcl("I will take that silver pot from you as well, humans are not meant to possess such artefacts. Now get out of my sight, our deal is complete, and if I see you here again I will not hesistate to take you to")
                            .npcl("Lord Drakan.")
                            .npcl("He will be extremely pleased to meet the murderer of poor Dessous, I suspect.")
                            .end()

                    branch.onValue(100)
                            .branch { player ->
                                return@branch if(!inInventory(player, Items.BLOOD_DIAMOND_4670)) { 1 } else { 0 }
                            }.let { branch2 ->
                                branch2.onValue(1)
                                        .playerl("Where is the Diamond of Blood? I know you have it!")
                                        .betweenStage { df, player, _, _ ->
                                            addItemOrDrop(player, Items.BLOOD_DIAMOND_4670)
                                        }
                                        .npcl("Do not take that tone of voice with me, meat. Here, take your bauble. I have no use for it.")
                                        .end()

                                branch2.onValue(0)
                                        .npcl("Be lucky I have let you live, meat. Our deal is done, I wish no further dealing with you.")
                                        .end()

                            }
                }

        b.onQuestStages(DesertTreasure.questName, 10,11,12,13,100)
                .npcl("Be lucky I have let you live, meat. Our deal is done, I wish no further dealing with you.")
                .end()
    }

}