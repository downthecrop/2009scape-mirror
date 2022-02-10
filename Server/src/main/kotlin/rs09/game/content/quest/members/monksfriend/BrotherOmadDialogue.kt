package rs09.game.content.quest.members.monksfriend


import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.plugin.Initializable
import rs09.game.world.GameWorld.Pulser


@Initializable
/**
* Handles BrotherOmadDialogue Dialogue
* @author Plex ?????
*/
class BrotherOmadDialogue(player: Player? = null): DialoguePlugin(player) {
    override fun newInstance(player: Player?): DialoguePlugin {
        return BrotherOmadDialogue(player)
    }

    //Item declaration
    private val BLANKET = 90

    override fun open(vararg args: Any?): Boolean {
        npc = (args[0] as NPC).getShownNPC(player)
        val qstage = player?.questRepository?.getStage("Monk's Friend") ?: -1

        when(qstage) {
            0 -> playerl(FacialExpression.HAPPY,"Hello there. What's wrong?").also { stage++ }                         // First dialogue
            10 -> playerl(FacialExpression.HAPPY,"Hello.").also { stage = 20 }                                      // Finding blanket
            20 -> playerl(FacialExpression.HAPPY,"Hello, how are you?").also { stage = 30 }                         // Talk to him again
            30 -> playerl(FacialExpression.HAPPY,"Hello brother Omad.").also{stage = 50}                            // Haven't found Cedric
            40 -> npcl(FacialExpression.HAPPY,"Hello adventurer, have you found Brother Cedric?").also{stage = 60}  // Haven't given Cedric water
            41 -> playerl(FacialExpression.HAPPY,"Hello again brother Omad.").also{stage = 70}                      // Haven't completed dialogue
            42 -> playerl(FacialExpression.HAPPY,"Hello again brother Omad.").also{stage = 70}                      // Haven't given Cedric logs
            50 -> playerl(FacialExpression.HAPPY,"Hi Omad, Brother Cedric is on his way!").also{stage = 80}         // Helped Cedric
            100 -> commenceParty()                                                                     // After quest complete
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            1 -> npcl(FacialExpression.SAD, "*yawn*...oh, hello...*yawn* I'm sorry! I'm just so tired! I haven't slept in a week!").also{stage++}
            2 -> options(
                    "Why can't you sleep, what's wrong?",
                    "Sorry! I'm too busy to hear your problems!").also {stage++}
            3 -> when(buttonId){
                1 -> playerl(FacialExpression.HALF_ASKING,"Why can't you sleep, what's wrong?").also { stage++}
                2 -> playerl(FacialExpression.FRIENDLY,"Sorry! I'm too busy to hear your problems!").also { stage = 1000}
            }
            4 -> npcl(FacialExpression.SAD, "It's brother Androe's son! With his constant: Waaaaaah! Waaaaaaaaah! Androe said it's natural, but it's so annoying!").also {stage++}

            5 -> playerl(FacialExpression.NEUTRAL,"I suppose that's what kids do.").also { stage++}
            6 -> npcl(FacialExpression.SAD,"He was fine, up until last week! Thieves broke in! They stole his favourite sleeping blanket!").also {stage++}
            7 -> npcl(FacialExpression.SAD,"Now he won't rest until it's returned... ...and that, means neither can I!").also { stage++}
            8 -> options(
                    "Can I help at all?",
                    "I'm sorry to hear that! I hope you find his blanket.").also { stage++}
            9 -> when(buttonId){
                1 -> playerl(FacialExpression.HALF_ASKING,"Can I help at all?").also { stage++}
                2 -> playerl(FacialExpression.FRIENDLY,"I'm sorry to hear that! I hope you find his blanket.").also { stage = 1000}
            }
            10 -> npcl(FacialExpression.HALF_WORRIED,"Please do. We won't be able to help you as we are peaceful men but we would be grateful for your help!").also { stage++}
            11 -> playerl(FacialExpression.HALF_ASKING,"Where are they?").also { stage++ }
            12 -> npcl(FacialExpression.SAD, "They hide in a secret cave in the forest. It's hidden under a ring of stones. Please, bring back the blanket!").also { stage = 1000 }.also { player.questRepository.getQuest("Monk's Friend").start(player) }.also { player.questRepository.syncronizeTab(player) }

            20 -> npcl(FacialExpression.SAD,"*yawn*...oh, hello again...*yawn*").also { stage++ }
            21 -> npcl(FacialExpression.SAD,"Please tell me you have the blanket.").also{ stage++}
            22 -> if(player.inventory.containItems(BLANKET)){
                playerl(FacialExpression.HAPPY,"Yes! I've recovered it from the clutches of the evil thieves!").also{stage++}
            } else {
                playerl(FacialExpression.SAD,"I'm afraid not.").also{stage = 999}
            }
            23 -> sendDialogue("You hand the monk the childs blanket.").also { stage++ }.also{player.inventory.remove(Item(BLANKET))}.also{player.questRepository.getQuest("Monk's Friend").setStage(player, 20)}
            24 -> npcl(FacialExpression.HAPPY,"Really, that's excellent, well done! Maybe now I will be able to get some rest.").also{ stage++}
            25 -> npcl(FacialExpression.SAD,"*yawn*..I'm off to bed! Farewell brave traveller!").also{ stage=1000}

            30 -> npcl(FacialExpression.HAPPY,"Much better now I'm sleeping well! Now I can organise the party.").also { stage++}
            31 -> playerl(FacialExpression.HAPPY,"Ooh! What party?").also { stage++}
            32 -> npcl(FacialExpression.HAPPY,"The son of Brother Androe's birthday party. He's going to be one year old!").also { stage++ }
            33 -> playerl(FacialExpression.HAPPY,"That's sweet!").also { stage++ }
            34 -> npcl(FacialExpression.HAPPY,"It's also a great excuse for a drink!").also { stage++}
            35 -> npcl(FacialExpression.NEUTRAL,"We just need Brother Cedric to return with the wine.").also { stage++}
            36 -> options(
                    "Who's Brother Cedric?",
                    "Enjoy it! I'll see you soon!").also { stage++}
            37 -> when(buttonId){
                1 -> playerl(FacialExpression.HALF_ASKING,"Who's Brother Cedric?").also { stage++}
                2 -> playerl(FacialExpression.FRIENDLY,"Enjoy it! I'll see you soon!").also { stage = 998}
            }
            38 -> npcl(FacialExpression.FRIENDLY,"Cedric is a member of the order too. We sent him out three days ago to collect wine. But he didn't return!").also { stage++}
            39 -> npcl(FacialExpression.NEUTRAL,"He most probably got drunk and lost in the forest!").also { stage++}
            40 -> options(
                    "I've no time for that, sorry.",
                    "Where should I look?",
                    "Can I come to the party?").also { stage++}
            41 -> when(buttonId){
                1 -> playerl(FacialExpression.NEUTRAL,"I've no time for that, sorry.").also { stage = 996}
                2 -> playerl(FacialExpression.HALF_ASKING,"Where should I look?").also { stage++}
                3 -> playerl(FacialExpression.HALF_ASKING,"Can I come to the party?").also { stage = 997}
            }
            42 -> npcl(FacialExpression.FRIENDLY,"Oh, he won't be far. Probably out in the forest.").also { stage++}
            43 -> playerl(FacialExpression.FRIENDLY,"Ok, I'll go and find him.").also { stage = 1000}.also{player.questRepository.getQuest("Monk's Friend").setStage(player, 30)}

            50 -> npcl(FacialExpression.NEUTRAL,"Hello adventurer, have you found Brother Cedric?").also{stage++}
            51 -> playerl(FacialExpression.SAD,"Not yet.").also{stage++}
            52 -> npcl(FacialExpression.FRIENDLY,"Well, keep looking, we need that wine!").also{stage = 1000}

            60 -> playerl(FacialExpression.NEUTRAL,"Yes I've seen him, he's a bit drunk!").also{stage++}
            61 -> npcl(FacialExpression.FRIENDLY,"Well, try your best to get him back here!").also{stage = 1000}

            70 -> npcl(FacialExpression.NEUTRAL,"Hello adventurer, where's Brother Cedric?").also{stage++}
            71 -> npcl(FacialExpression.NEUTRAL,"He's having a bit of trouble with his cart.").also{stage++}
            72 -> npcl(FacialExpression.NEUTRAL,"Hmmm! Maybe you could help?").also{stage = 1000}

            80 -> npcl(FacialExpression.HAPPY,"Good! Good! Now we can party!").also{stage++}
            81 -> npcl(FacialExpression.HAPPY,"I have little to repay you with but please! Take these Rune Stones.").also{stage++}
            82 -> sendDialogue("Brother Omad gives you 8 Law Runes").also{stage++}
            83 -> playerl(FacialExpression.HAPPY,"Thanks Brother Omad!").also{stage = 1000
                player.questRepository.getQuest("Monk's Friend").finish(player)
                player.questRepository.getQuest("Monk's Friend").setStage(player, 100)
            }

            996 -> npcl(FacialExpression.NEUTRAL,"Okay traveller, take care.").also{stage = 1000}
            997 -> npcl(FacialExpression.NEUTRAL,"Of course, but we need the wine first.").also{stage = 1000}
            998 -> npcl(FacialExpression.NEUTRAL,"Take care traveller.").also{stage = 1000}
            999 -> npcl(FacialExpression.SAD,"I need some sleep!").also{stage++}
            1000 -> end()

        }
        return true
    }

    private fun commenceParty() {

        // Spawn in some balloons???

        /*
        for (i in 0..10) {
            val balloons = Object.create(balloonID, Location.create(balloonLocation))
            balloons.init()
        }*/

        Pulser.submit(object : Pulse(1) {
            var count = 0
            override fun pulse(): Boolean {
                when (count) {

                    // Monks currently don't party

                    1 -> npc.sendChat("Party!")
                    3 -> player.sendChat("Woop!")
                    // 5 - Yeah!
                    7 -> npc.sendChat("Let's boogie!")
                    9 -> player.sendChat("Oh, baby!")
                    // 11 - GO!
                    13 -> npc.sendChat("Get down!")
                    15 -> player.sendChat("Feel the rhythm!")
                    // 17 - Dance!
                    19 -> npc.sendChat("Oh my!")
                    21 -> player.sendChat("Watch me go!")
                    // 23 - You go!

                    // Clear balloons

                    /*24 -> {
                            balloons.clear()
                            i++
                    }*/
                }
                count++
                return false
            }
        })
    }


    override fun getIds(): IntArray {
        return intArrayOf(279)
    }

}
