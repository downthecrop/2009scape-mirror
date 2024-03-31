package content.region.kandarin.quest.grandtree

import content.region.kandarin.quest.grandtree.TheGrandTree.Companion.questName
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.interaction.MovementPulse
import core.game.node.entity.impl.PulseType
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.map.path.Pathfinder
import core.game.world.update.flag.context.Animation
import core.tools.END_DIALOGUE
import org.rs09.consts.Items
import org.rs09.consts.NPCs

class KingNarnodeDialogue : DialogueFile() {

    val trapdoorLocation = Location(2464,3497,0)
    val closedTrapDoor = Scenery(2446, Location(2463,3497,0), 22,  0)
    val openedTrapDoor = Scenery(2445, Location(2463,3497,0), 22,  0)
    val tunnels = Location(2464, 9897, 0)
    val ladderClimbAnimation = Animation(828)

    fun leadDownLadder(){
        // Animate Narnode to walk to the trapdoor and climb the ladder
        GameWorld.Pulser.submit(object : Pulse(0) {
            var count = 0
            override fun pulse(): Boolean {
                when (count) {
                    0 -> {
                        forceWalk(player!!, trapdoorLocation, "SMART")
                        lock(player!!,10)
                        npc!!.pulseManager.run(object : MovementPulse(npc, player, Pathfinder.SMART) {
                            override fun pulse(): Boolean {
                                return false
                            }
                        }, PulseType.STANDARD)
                        npc!!.sendChat("Down here!")
                        SceneryBuilder.replace(closedTrapDoor,openedTrapDoor,10)
                    }
                    4 -> {
                        player!!.faceLocation(trapdoorLocation.transform(Direction.WEST, 1))
                        player!!.animator.animate(ladderClimbAnimation)
                    }
                    5 -> {
                        teleport(player!!, tunnels)
                        npc!!.pulseManager.clear()
                    }
                    7 -> {
                        // We are now underground
                        val undergroundNarnode = RegionManager.getNpc(player!!.location, NPCs.KING_NARNODE_SHAREEN_670, 16)
                        forceWalk(undergroundNarnode!!,player!!.location.transform(Direction.EAST,1), "SMART")
                        unlock(player!!)
                        openDialogue(player!!, KingNarnodeUnderGroundDialogue(), undergroundNarnode)
                    }
                    10 -> return true
                }
                count++
                return false
            }
        })
    }
    override fun handle(componentID: Int, buttonID: Int) {
        when (getQuestStage(player!!, questName)) {
            0 -> {
                when (stage) {
                    0 -> npcl("Welcome Traveller. I am King Narnode. It's nice to see an outsider.").also { stage++ }
                    1 -> playerl("Hi! It seems to be a very busy settlement.").also{ stage++ }
                    2 -> npcl("For now.").also { stage++ }
                    3 -> options("You seem worried, what's up?","I'll be off now.").also { stage++ }
                    4 -> when (buttonID) {
                        1 -> playerl("You seem worried, what's up?").also { stage = 5 }
                        2 -> playerl("I'll be off now.").also { stage = 25 }
                    }
                    5 -> npcl("Traveller, can I speak to you in strictest confidence?").also { stage++ }
                    6 -> playerl("Of course sire.").also { stage++ }
                    7 -> npcl("Not here, follow me.").also {
                        stage = END_DIALOGUE
                        // Animate Narnode to walk to the trapdoor and climb the ladder
                        leadDownLadder()
                    }
                    25 -> npcl("Enjoy your stay with us. There are many things to see in my kingdom.").also { stage = END_DIALOGUE }
                }
            }
            10 -> {
                when (stage) {
                    0 -> {
                        // Did the player destroy/drop the quest items
                        if(!player!!.hasItem(Item(Items.BARK_SAMPLE_783)) || !player!!.hasItem(Item(Items.TRANSLATION_BOOK_784))) {
                            npcl("You dropped something I gave you earlier. Please take better care of my thing!")
                            if(!player!!.hasItem(Item(Items.BARK_SAMPLE_783))) {
                                addItemOrDrop(player!!,Items.BARK_SAMPLE_783)
                            }
                            if(!player!!.hasItem(Item(Items.TRANSLATION_BOOK_784))) {
                                addItemOrDrop(player!!,Items.TRANSLATION_BOOK_784)
                            }
                            stage = END_DIALOGUE
                        } else {
                            npcl("Traveller, any word from Hazelmere?")
                            stage++
                        }
                    }
                    1 -> playerl("Not yet.").also { stage = END_DIALOGUE }
                }
            }
            20 -> {
                when (stage) {
                    0 -> npcl("Welcome Traveller. I am King Narnode. It's nice to see an outsider.").also {
                        // Reset correct answered options
                        setAttribute(player!!, "/save:grandtree:opt1", false)
                        setAttribute(player!!, "/save:grandtree:opt2", false)
                        setAttribute(player!!, "/save:grandtree:opt3", false)
                        stage++
                    }
                    1 -> playerl("Hello again, Your Highness.").also{ stage++ }
                    2 -> npcl("Hello Traveller, did you speak to Hazelmere?").also { stage++ }
                    3 -> playerl("Yes! I managed to find him.").also{ stage++ }
                    4 -> npcl("Do you understand what he said?").also { stage++ }
                    5 -> options("I think so!","No, I need to go back.").also { stage++ }
                    6 -> when (buttonID) {
                        1 -> playerl("I think so!").also { stage = 10 }
                        2 -> playerl("No, I need to go back.").also { stage = END_DIALOGUE }
                    }
                    10 -> npcl("So what did he say?").also { stage++ }
                    11 -> options("King Narnode must be stopped, he is a madman!","Praise be to the great Zamorak!", "Do you have any bread? I do like bread.", "The time has come to attack!","None of the above.").also { stage++ }
                    12 -> when (buttonID) {
                        1 -> playerl("King Narnode must be stopped, he is a madman!").also { stage++ }
                        2 -> playerl("Praise be to the great Zamorak!").also { stage++ }
                        3 -> playerl("Do you have any bread? I do like bread.").also { stage++ }
                        4 -> playerl("The time has come to attack!").also { stage++ }
                        5 -> options("The tree is fine, you have nothing to fear.","You must come and see me!", "The tree needs watering as there has been drought.","Grave danger lies ahead, only the bravest will linger.","None of the above.").also { stage = 20 }
                    }
                    13 -> options("The tree is fine, you have nothing to fear.","You must come and see me!", "The tree needs watering as there has been drought.","Grave danger lies ahead, only the bravest will linger.","None of the above.").also { stage = 20 }
                    20 -> when (buttonID) {
                        1 -> playerl("The tree is fine, you have nothing to fear.").also { stage++ }
                        2 -> playerl("You must come and see me!").also { stage++ }
                        3 -> playerl("The tree needs watering as there has been drought.").also { stage++ }
                        4 -> playerl("Grave danger lies ahead, only the bravest will linger.").also { stage++ }
                        5 -> options("Time is of the essence! We must move quickly.","There is no need for haste, just send a runner.", "A man came to me with the King's seal.","You must act now, or we will all die!","None of the above.").also { stage = 30 }
                    }
                    21 -> options("Time is of the essence! We must move quickly.","There is no need for haste, just send a runner.", "Time passes us by.","You must act now, or we will all die!","None of the above.").also { stage = 31 }
                    // Right
                    30 -> when (buttonID) {
                        1 -> playerl("Time is of the essence! We must move quickly.").also { stage = 33 }
                        2 -> playerl("There is no need for haste, just send a runner.").also { stage = 33 }
                        3 -> playerl("A man came to me with the King's seal.").also {
                            setAttribute(player!!, "/save:grandtree:opt1", true)
                            stage = 32
                        }
                        4 -> playerl("You must act now, or we will all die!").also { stage = 33 }
                        5 -> options("Only Adamantite is any use.","You must use force!", "Use a bucket of milk from a scared cow.","Take this banana to him, he will understand.","None of the above.").also { stage = 41 }
                    }
                    // Wrong
                    31 -> when (buttonID) {
                        1 -> playerl("Time is of the essence! We must move quickly.").also { stage = 33 }
                        2 -> playerl("There is no need for haste, just send a runner.").also { stage = 33 }
                        3 -> playerl("Time passes us by.").also { stage = 33 }
                        4 -> playerl("You must act now, or we will all die!").also { stage = 33 }
                        5 -> options("Only Adamantite is any use.","You must use force!", "Use a bucket of milk from a scared cow.","Take this banana to him, he will understand.","None of the above.").also { stage = 41 }
                    }
                    32 -> options("I gave the man Daconia rocks.","You must use force!", "Use a bucket of milk from a scared cow.","Take this banana to him, he will understand.","None of the above.").also { stage = 40 }
                    33 -> options("Only Adamantite is any use.","You must use force!", "Use a bucket of milk from a scared cow.","Take this banana to him, he will understand.","None of the above.").also { stage = 41 }
                    // Right
                    40 -> when (buttonID) {
                        1 -> playerl("I gave the man Daconia rocks.").also {
                            setAttribute(player!!, "/save:grandtree:opt2", true)
                            stage = 42
                        }
                        2 -> playerl("You must use force!").also { stage = 43 }
                        3 -> playerl("Use a bucket of milk from a scared cow.").also { stage = 43 }
                        4 -> playerl("Take this banana to him, he will understand.").also { stage = 43 }
                        5 -> options("All with be fine on the third night.","You must wait till the second night.", "Nothing will help us now!","The tree will die in five days!").also { stage = 51 }
                    }
                    // Wrong
                    41 -> when (buttonID) {
                        1 -> playerl("Only Adamantite is any use.").also { stage = 43 }
                        2 -> playerl("You must use force!").also { stage = 43 }
                        3 -> playerl("Use a bucket of milk from a scared cow.").also { stage = 43 }
                        4 -> playerl("Take this banana to him, he will understand.").also { stage = 43 }
                        5 -> options("All with be fine on the third night.","You must wait till the second night.", "Nothing will help us now!","The tree will die in five days!").also { stage = 51 }
                    }
                    42 -> options("All with be fine on the third night.","You must wait till the second night.", "Nothing will help us now!","And Daconia rocks will kill the tree!").also { stage = 50 }
                    43 -> options("All with be fine on the third night.","You must wait till the second night.", "Nothing will help us now!","The tree will die in five days!").also { stage = 51 }
                    // Right
                    50 -> when (buttonID) {
                        1 -> playerl("All with be fine on the third night.").also { stage = 52 }
                        2 -> playerl("You must wait till the second night.").also { stage = 52 }
                        3 -> playerl("Nothing will help us now!").also { stage = 52 }
                        4 -> playerl("And Daconia rocks will kill the tree!").also {
                            setAttribute(player!!, "/save:grandtree:opt3", true)
                            stage = 52
                        }
                    }
                    // Wrong
                    51 -> when (buttonID) {
                        1 -> playerl("All with be fine on the third night.").also { stage++ }
                        2 -> playerl("You must wait till the second night.").also { stage++ }
                        3 -> playerl("Nothing will help us now!").also { stage++ }
                        4 -> playerl("The tree will die in five days!").also { stage++ }
                    }
                    52 -> {
                        if(getAttribute(player!!, "/save:grandtree:opt1", false)
                            && getAttribute(player!!, "/save:grandtree:opt2", false)
                            && getAttribute(player!!, "/save:grandtree:opt3", false)){
                        npcl("Of course! I should've known! Someone must've forged my royal seal. Hazelmere thought I sent him for the Daconia stones!").also { stage = 60 }
                        } else {
                            npcl("Wait a minute! That doesn't sound like Hazelmere! Are you sure you translated correctly?").also { stage++ }
                        }
                    }
                    53 -> playerl("Erm...I think so.").also { stage++ }
                    54 -> npcl(" I'm sorry Traveller but this is no good. The translation must be perfect or the information's no use. Please come back when you know exactly what Hazelmere said.").also { stage = END_DIALOGUE }
                    60 -> playerl("What are Daconia stones?").also { stage++ }
                    61 -> npcl("Hazelmere created the Daconia stones. They are a safety measure, in case the tree grew out of control. They're the only thing that can kill the tree.").also { stage++ }
                    62 -> npcl("This is terrible! The stones must be recovered!").also { stage++ }
                    63 -> playerl("Can I help?").also { stage++ }
                    64 -> npcl("First, I must warn the tree guardians. Please, could you tell the chief tree guardian, Glough. He lives in a tree house just in front of the Grand Tree.").also { stage++ }
                    65 -> npcl("If he's not there he will be at his girlfriend Anita's place. Meet me back here once you've told him.").also { stage++ }
                    66 -> playerl("Ok! I'll be back soon.").also {
                        setQuestStage(player!!, questName, 40)
                        stage = END_DIALOGUE
                    }
                }
            }
            40 -> {
                when(stage) {
                    0 -> npcl("Hello Traveller, did you speak to Glough?").also { stage++ }
                    1 -> playerl("Not yet.").also { stage++ }
                    2 -> npcl("OK. He lives just in front of the Grand Tree. Let me know when you've talked to him.").also{ stage = END_DIALOGUE }
                }
            }
            45 -> {
                when(stage) {
                    0 -> playerl("Hello, Your Highness. Have you any news on the Daconia stones?").also { stage++ }
                    1 -> npcl("It's OK Traveller, thanks to Glough! He found a human sneaking around! He had three Daconia rocks on him!").also { stage++ }
                    2 -> playerl("Wow! That was quick!").also { stage++ }
                    3 -> npcl("Yes Glough really knows what he's doing. The human has been detained until we know who else is involved. Maybe Glough was right, maybe humans are invading!").also { stage++ }
                    4 -> playerl("I doubt it, can I speak to the prisoner?").also { stage++ }
                    5 -> npcl("Certainly. He's on the top level of the tree. Be careful, it's a long way down!").also {
                        setQuestStage(player!!, questName, 46)
                        stage = END_DIALOGUE
                    }
                }
            }
            46 -> {
                when(stage){
                    0 -> npcl("Hello Traveller. If you wish to talk to the prisoner go to the top of the tree, you'll find him there.").also { stage++ }
                    1 -> playerl("Thanks.").also { stage = END_DIALOGUE }
                }
            }
            55 -> {
                when(stage){
                    0 -> playerl("King Narnode, I need to talk!").also { stage++ }
                    1 -> npcl("Traveller, what are you doing here? The stronghold has been put on full alert! It's not safe for you here!").also { stage++ }
                    2 -> playerl("Your Highness, I believe Glough is killing the trees in order to make a mass fleet of warships!").also { stage++ }
                    3 -> npcl("That's an absurd accusation!").also { stage++ }
                    4 -> playerl("His hatred for humanity is stronger than you know!").also { stage++ }
                    5 -> npcl("That's enough Traveller, you sound as paranoid as him! Traveller please leave! It's bad enough having one human locked up.").also { stage = END_DIALOGUE }
                }
            }
            60 -> {
                if(player!!.hasItem(Item(Items.INVASION_PLANS_794))){
                    when(stage){
                        0 -> playerl("Hi, Your Highness, did you think about what I said?").also { stage++ }
                        1 -> npcl("Look, if you're right about Glough I would have him arrested but there's no reason for me to think he's lying.").also { stage++ }
                        2 -> playerl("Look, I found this at Glough's home!").also { stage++ }
                        3 -> sendDialogue(player!!, "You give the King the invasion plans.").also { stage++ }
                        4 -> npcl("If these are to be believed then this is terrible! But it's not proof, anyone could have made these. Traveller, I understand your concern.").also { stage++ }
                        5 -> npcl("I had guards search Glough's house but they found nothing suspicious, just these odd twigs.").also { stage++ }
                        6 -> sendDialogue(player!!, "The King has given you some twigs lashed together.").also{
                            addItemOrDrop(player!!, Items.TWIGS_789)
                            addItemOrDrop(player!!, Items.TWIGS_790)
                            addItemOrDrop(player!!, Items.TWIGS_791)
                            addItemOrDrop(player!!, Items.TWIGS_792)
                            stage++
                        }
                        7 -> npcl("On the other hand, if Glough's right about the humans we will need an army of gnomes to protect ourselves. ").also { stage++ }
                        8 -> npcl("So I've decided to allow Glough to raise a mighty gnome army. The Grand Tree's still slowly dying. If it is human sabotage we must respond!").also{
                            setQuestStage(player!!, questName, 70)
                            removeItem(player!!, Item(Items.INVASION_PLANS_794), Container.INVENTORY)
                            stage = END_DIALOGUE
                        }
                    }
                } else {
                    when(stage){
                        0 -> playerl("Hello, Your Highness.").also { stage++ }
                        1 -> npcl("Please Traveller, if the gnomes see me talking to you they'll revolt against me.").also { stage++ }
                        2 -> playerl("That's crazy!").also { stage++ }
                        3 -> npcl("Glough's scared the whole town, he expects the humans to attack any day. He's even begun to recruit hundreds of gnome soldiers.").also { stage++ }
                        4 -> playerl("Don't you understand he's creating his own army?!").also { stage++ }
                        5 -> npcl("Please Traveller, leave before it's too late!").also { stage = END_DIALOGUE }
                    }
                }
            }
            70 -> {
                when(stage){
                    0 -> npcl("Please Traveller, take my advice and leave!").also {
                        if(!player!!.hasItem(Item(Items.TWIGS_789)) || !player!!.hasItem(Item(Items.TWIGS_790)) || !player!!.hasItem(Item(Items.TWIGS_791)) || !player!!.hasItem(Item(Items.TWIGS_792))) {
                            stage++
                        } else {
                            stage = END_DIALOGUE
                        }
                    }
                    1 -> playerl("I've lost those twigs you gave me.").also{ stage++ }
                    2 -> {
                        if(player!!.inventory.freeSlots() < 4) {
                            npcl("Hmm. Looks like you can't carry anymore. Put 4 things down and come back to me.").also { stage = END_DIALOGUE }
                        } else {
                            npcl("King Narnode gives you 4 twigs.").also {
                                addItemOrDrop(player!!, Items.TWIGS_789)
                                addItemOrDrop(player!!, Items.TWIGS_790)
                                addItemOrDrop(player!!, Items.TWIGS_791)
                                addItemOrDrop(player!!, Items.TWIGS_792)
                                stage = END_DIALOGUE
                            }
                        }
                    }
                }
            }
            98,99 -> {
                leadDownLadder()
            }
            100 -> {
                when (stage) {
                    0 -> npcl("Thanks again for your help!").also { stage = END_DIALOGUE }
                }
            }
        }
    }
}

class KingNarnodeUpstairsDialogue : DialogueFile(){
    override fun handle(componentID: Int, buttonID: Int) {
        when(stage){
            0 -> playerl("I don't think you can trust Glough, Your Highness. He seems to have an unnatural hatred for humans.").also { stage++ }
            1 -> npcl("I know he can be a bit extreme at times. But he's the best tree guardian I have, he has made the gnomes paranoid about humans though.").also { stage++ }
            2 -> npcl("I'm afraid Glough has placed guards on the front gate to stop you escaping! Let my glider pilot fly you away until things calm down around here.").also { stage++ }
            3 -> playerl("Well, OK.").also { stage++ }
            4 -> npcl("I'm sorry again Traveller!").also { stage = END_DIALOGUE }
        }
    }

}

class KingNarnodeUnderGroundDialogue : DialogueFile() {
    private fun leadUpLadder(){
        val ladderLoc = Location.create(2464, 9897, 0)
        val ladderClimbAnimation = Animation(828)
        val ladderExit = Location.create(2464, 3497, 0)
        GameWorld.Pulser.submit(object : Pulse(0) {
            var count = 0
            override fun pulse(): Boolean {
                when (count) {
                    0 -> {
                        forceWalk(player!!, ladderLoc, "SMART")
                        lock(player!!,10)
                        npc!!.pulseManager.run(object : MovementPulse(npc, player, Pathfinder.SMART) {
                            override fun pulse(): Boolean {
                                return false
                            }
                        }, PulseType.STANDARD)
                        npc!!.sendChat("Up here!")
                    }
                    4 -> {
                        player!!.faceLocation(ladderLoc.transform(Direction.WEST, 1))
                        player!!.animator.animate(ladderClimbAnimation)
                    }
                    5 -> {
                        teleport(player!!, ladderExit)
                        npc!!.pulseManager.clear()
                        unlock(player!!)
                    }
                    10 -> return true
                }
                count++
                return false
            }
        })
    }
    override fun handle(componentID: Int, buttonID: Int) {
        when(getQuestStage(player!!, questName)) {
            98 -> when (stage) {
                0 -> npcl("Traveller, you're wounded! What happened?").also { stage++ }
                1 -> playerl("It's Glough! He set a demon on me!").also { stage++ }
                2 -> npcl("What?! Glough?! With a demon?!").also { stage++ }
                3 -> playerl("Glough has a store of Daconia rocks further up the passage! He's been accessing the roots from a secret passage at his home.").also { stage++ }
                4 -> npcl("Never! Not Glough! He's a good gnome at heart! Guard!").also { stage++ }
                5 -> sendNPCDialogue(player!!, NPCs.GNOME_GUARD_163,"Sire!").also{ stage++ }
                6 -> npcl("Go and check out that passage!").also { stage++ }
                7 -> sendNPCDialogue(player!!, NPCs.GNOME_GUARD_163,"We found Glough hiding under a horde of Daconia rocks!").also{ stage++ }
                8 -> playerl("That's what I've been trying to tell you! Glough's been fooling you!").also { stage++ }
                9 -> npcl("I... I don't know what to say! How could I have been so blind?! Guard! Call off the military training! The humans are not attacking!").also { stage++ }
                10 -> sendNPCDialogue(player!!, NPCs.GNOME_GUARD_163,"Yes sir!").also{ stage++ }
                11 -> npcl("You have my full apologies Traveller! And my gratitude! A reward will have to wait though, the tree is still dying!").also {stage++}
                12 -> npcl("The guards are clearing Glough's rock supply now but there must be more Daconia hidden somewhere in the roots! Help us search, we have little time!").also {
                    setQuestStage(player!!, questName, 99)
                    // position of the daconia rock
                    if(getAttribute(player!!,"treegnome:rock",0) == 0){
                        val answer = (1..5).random()
                        setAttribute(player!!,"/save:treegnome:rock",answer)
                    }
                    stage = END_DIALOGUE
                }
            }

            99 -> {
                if(player!!.hasItem(Item(Items.DACONIA_ROCK_793))){
                    when(stage){
                        0 -> npcl("Traveller, have you managed to find the Daconia?").also { stage++ }
                        1 -> playerl("Is this it?").also { stage++ }
                        2 -> npcl("Yes! Excellent, well done!").also { stage++ }
                        3 -> sendDialogue(player!!,"You give the King the Daconia rock.").also { stage++ }
                        4 -> npcl("It's incredible, the tree's health is improving already! I don't know what to say, we owe you so much! To think Glough had me fooled all along!").also { stage++ }
                        5 -> playerl("All that matters now is that humans and gnomes can live together in peace!").also { stage++ }
                        6 -> npcl("I'll drink to that! From now on I vow to make this stronghold a welcoming place for all! I'll grant you access to all our facilities.").also { stage++ }
                        7 -> playerl("Thanks! I think!").also { stage++ }
                        8 -> npcl("It should make your stay here easier. You can use the spirit tree to transport yourself, as well as the gnome glider. I also give you access to our mine.").also { stage++ }
                        9 -> playerl("Mine?").also { stage++ }
                        10 -> npcl("Very few know of the secret mine under the Grand Tree. If you push on the roots just to my north they will separate and let you pass.").also { stage++ }
                        11 -> playerl("Strange!").also { stage++ }
                        12 -> npcl("That's magic trees for you! All the best Traveller and thanks again!").also { stage++ }
                        13 -> playerl("You too, Your Highness!").also {
                            finishQuest(player!!, questName)
                            removeItem(player!!, Items.DACONIA_ROCK_793)
                            stage = END_DIALOGUE
                        }
                    }
                } else {
                    when (stage) {
                        0 -> npcl("Traveller, have you managed to find the Daconia?").also { stage++ }
                        1 -> playerl("No sign of it so far.").also { stage++ }
                        2 -> npcl("The tree will still die if we don't find it! It could be anywhere!").also { stage++ }
                        3 -> playerl("Don't worry, Your Highness! We'll find it!").also { stage = END_DIALOGUE }
                    }
                }
            }
            100 -> {
                when (stage) {
                    0 -> npcl("Thanks again for your help!").also { stage = END_DIALOGUE }
                }
            }

            else -> when (stage) {
                0 -> npcl("Down here.").also { stage++ }
                1 -> playerl("So what is this place?").also { stage++ }
                2 -> npcl("These, my friend, are the foundations of the stronghold.").also { stage++ }
                3 -> playerl("They look like roots to me.").also { stage++ }
                4 -> npcl("Not just any roots Traveller! These were created by gnome mages eons ago, since then they have grown to become a mighty stronghold!").also { stage++ }
                5 -> playerl("Impressive. What exactly is the problem?").also { stage++ }
                6 -> npcl("In the last two months our tree guardians have reported continuing deterioration of the Grand Tree's health. I've never seen this before! It could be the end for us all!").also { stage++ }
                7 -> playerl("You mean the tree is ill?").also { stage++ }
                8 -> npcl(" In effect yes. Would you be willing to help us discover what is happening to the tree?").also { stage++ }
                9 -> options("I'm sorry, I don't want to get involved.", "I'd be happy to help!").also { stage++ }
                10 -> when (buttonID) {
                    1 -> playerl("I'm sorry, I don't want to get involved.").also { stage = 15 }
                    2 -> playerl("I'd be happy to help!").also { stage = 20 }
                }

                15 -> npcl("I understand Traveller. Please keep this to yourself.").also { stage++ }
                16 -> playerl("Of course.").also { stage++ }
                17 -> npcl("I'll show you the way back up.").also { stage++ }
                18 -> {
                    npcl("Up here.")
                    leadUpLadder()
                    stage = END_DIALOGUE
                }

                20 -> npcl("Thank Guthix for your arrival! The first task is to find out what's killing the tree.").also { stage++ }
                21 -> playerl("Do you have an idea?").also { stage++ }
                22 -> npcl("My top tree guardian, Glough, believes it's human sabotage. I'm not so sure! The only way to know for sure is to talk to Hazelmere.").also { stage++ }
                23 -> playerl("Who's Hazelmere?").also { stage++ }
                24 -> npcl("Hazelmere is one of the mages that created the Grand Tree. He is the only one that has survived from that time. Take this bark sample to him, he will be able to help!").also { stage++ }
                25 -> sendItemDialogue(
                    player!!,
                    Items.BARK_SAMPLE_783,
                    "The king shows you a sample of bark."
                ).also { stage++ }

                26 -> npcl("The mage only talks in the old tongue, you'll need this.").also { stage++ }
                27 -> sendItemDialogue(
                    player!!,
                    Items.TRANSLATION_BOOK_784,
                    "The king shows you a translation book."
                ).also { stage++ }

                28 -> playerl("What is it?").also { stage++ }
                29 -> npcl("It's a translation book, you'll need it to translate what Hazelmere says. Do that carefully! His words are our only hope!").also { stage++ }
                30 -> npcl("You'll find his dwellings high upon a towering hill, on an island east of Yanille. I'll show you the way back up.").also { stage++ }
                31 -> if (player!!.inventory.freeSlots() >= 2) {
                    npcl("Up here.")
                    stage = END_DIALOGUE
                    setQuestStage(player!!, questName, 10)
                    addItemOrDrop(player!!, Items.BARK_SAMPLE_783)
                    addItemOrDrop(player!!, Items.TRANSLATION_BOOK_784)
                    leadUpLadder()
                } else {
                    npcl("You don't have inventory space for my book or the bark! Clear some space and speak to me again")
                }.also { stage = END_DIALOGUE }
            }
        }
    }
}
