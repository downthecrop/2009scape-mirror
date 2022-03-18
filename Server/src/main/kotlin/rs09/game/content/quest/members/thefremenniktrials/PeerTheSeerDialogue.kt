package rs09.game.content.quest.members.thefremenniktrials

import api.*
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.interaction.`object`.BankingPlugin
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.plugin.Initializable
import core.tools.RandomFunction
import rs09.tools.END_DIALOGUE
import kotlin.random.Random

@Initializable
class PeerTheSeerDialogue(player: Player? = null) : DialoguePlugin(player) {
    val predictionOne = arrayOf("one","two","three","four","five","six","seven","eight","ten")
    val predictionTwo = arrayOf("black","blue","brown","cyan","green","pink","purple","red","yellow")
    val predictionThree = arrayOf("fire giant","ghosts","giant","goblin","green dragon","hobgoblin","lesser demon","moss giant","ogre","zombie")
    val predictionFour = arrayOf("Al Kharid","Ardougne","Burthorpe","Canifis","Catherby","Falador","Karamja","Varrock","The Wilderness","Yanille")
    val predictionFive = arrayOf("battleaxe","crossbow","dagger","javelin","long sword","mace","scimitar","spear","warhammer")
    val predictionSix = arrayOf("Agility","Cooking","Crafting","Fishing","Fletching","Herblore","Mining","Runecrafting","Thieving")
    val PREDICTIONS = arrayOf(
        "You will find luck today with the number ${predictionOne[RandomFunction.getRandom(8)]}.",
        "The colour ${predictionTwo[RandomFunction.getRandom(8)]} will bring you luck this day.",
        "The enemy called ${predictionThree[RandomFunction.getRandom(9)]} is your lucky totem this day.",
        "The place called ${predictionFour[RandomFunction.getRandom(9)]} will be worth your time to visit.",
        "The stars tell me that you should use a ${predictionFive[RandomFunction.getRandom(8)]} in combat today.",
        "You would be wise to train the skill ${predictionSix[RandomFunction.getRandom(8)]}")
    var prediction = RandomFunction.getRandom(5)

    override fun open(vararg args: Any?): Boolean {
        if(player.inventory.contains(3710,1)){
            playerl(FacialExpression.HAPPY,"Can I have a weather forecast now please?")
            stage = 15
            return true
        }
        else if(player.inventory.contains(3705,1)){
            playerl(FacialExpression.ASKING,"So, about this forecast...")
            stage = 20
            return true
        }
        else if(player.getAttribute("sigmundreturning",false) == true){
            playerl(FacialExpression.ASKING,"I've got an item to trade but I don't know if it's for you.")
            stage = 26
            return true
        }
        else if(player.getAttribute("sigmund-steps", 0) == 10){
            playerl(FacialExpression.ASKING,"I don't suppose you have any idea where I could find a brave and powerful warrior to act as a bodyguard?")
            stage = 8
            return true
        }
        else if(player.getAttribute("sigmund-steps", 0) == 9){
            playerl(FacialExpression.ASKING,"I don't suppose you have any idea where I could find a weather forecast from the Fremennik Seer do you?")
            stage = 1
            return true
        }
        else if(player.getAttribute("PeerStarted",false) && !player.inventory.isEmpty || !player.equipment.isEmpty){
            npcl(FacialExpression.SAD,"Uuuh... What was that dark presence I felt?")
            stage = 100
            return true
        }
        else if(player.getAttribute("PeerStarted",false) && player.inventory.isEmpty && player.equipment.isEmpty){
            npcl(FacialExpression.SAD,"Uuuh... What was that dark presence I felt?")
            stage = 110
            return true
        }
        else if(player.getAttribute("fremtrials:peer-vote",false)){
            npcl(FacialExpression.SAD,"Uuuh... What was that dark presence I felt?")
            stage = 120
            return true
        }
        else if(player.questRepository.isComplete("Fremennik Trials")){
            npcl(FacialExpression.SAD,"Uuuh... What was that dark presence I felt?")
            stage = 150
            return true
        }
        else if(player.questRepository.hasStarted("Fremennik Trials")){
            npcl(FacialExpression.SAD,"Uuuh... What was that dark presence I felt?")
            stage = 50
            return true
        }
        if (questStage(player, "Fremennik Trials") == 0) {
            npc(FacialExpression.SAD,"Uuuh... What was that dark presence I felt?").also { stage = 300 }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            1 -> npcl(FacialExpression.ANNOYED,"Er.... Yes, because I AM the Fremennik Seer.").also { stage++ }
            2 -> playerl(FacialExpression.ASKING,"Can I have a weather forecast then please?").also { stage++ }
            3 -> npcl(FacialExpression.THINKING,"You require a divination of the weather? This is a simple matter for me, but I will require something in return from you for this small service.").also { stage++ }
            4 -> playerl(FacialExpression.ASKING,"I knew you were going to say that...").also { stage++ }
            5 -> npcl(FacialExpression.HAPPY,"Do not fret, outerlander; it is a fairly simple matter. I require a bodyguard for protection. Find someone willing to offer me this service.").also { stage++ }
            6 -> playerl(FacialExpression.ASKING,"That's all?").also { stage++ }
            7 -> npcl(FacialExpression.HAPPY,"That is all.").also {
                player?.incrementAttribute("sigmund-steps",1)
                stage = 1000
            }
            10 -> npcl(FacialExpression.ANNOYED,"If I did, then I would simply have asked them myself now, wouldn't I, outerlander?").also { stage = 1000 }

            15 -> npcl(FacialExpression.ANNOYED,"I have already told you outerlander; You may have a reading from me when I have a signed contract from a warrior guaranteeing my protection.").also { stage++ }
            16 -> playerl(FacialExpression.HAPPY,"Yeah, I know; I have one right here from Thorvald.").also {
                removeItem(player,3710)
                addItem(player,3705)
                stage++
            }
            17 -> npcl(FacialExpression.AMAZED,"You have not only persuaded one of the Fremennik to act as a servant to me, but you have enlisted the aid of mighty Thorvald himself???").also { stage++ }
            18 -> npcl(FacialExpression.HAPPY,"You may take this forecast with my blessing outerlander. You have offered me the greatest security I can imagine.").also { stage = 1000 }

            20 -> npcl(FacialExpression.THINKING,"Yes, outerlander?").also { stage++ }
            21 -> playerl(FacialExpression.ASKING,"I still don't know why you didn't just let me have one anyway in the first place. Surely it means nothing to you?").also { stage++ }
            22 -> npcl(FacialExpression.THINKING,"That is not true, outerlander. Although I see glimpses of the future all of the time, using my powers brings the attention of the gods to me.").also { stage++ }
            23 -> npcl(FacialExpression.THINKING,"Some of the gods are spiteful and cruel, and I fear if I use my powers too much then I will meet with unpredictable accidents.").also { stage++ }
            24 -> npcl(FacialExpression.HAPPY,"This is why I needed protection.").also { stage++ }
            25 -> playerl(FacialExpression.THINKING,"Okay... I... think I understand...").also { stage = 1000 }

            26 -> npcl(FacialExpression.ANNOYED,"Not me, I'm afraid.").also { stage++ }

            //The Seer's Trial
            50 -> npcl(FacialExpression.AMAZED,"!").also { stage++ }
            51 -> npcl(FacialExpression.HAPPY,"Ahem, sorry about that. Hello outerlander. What do you want?").also { stage++ }
            52 -> playerl(FacialExpression.ASKING,"Hello. I'm looking for members of the council of elders to vote for me to become a Fremennik.").also { stage++ }
            53 -> npcl(FacialExpression.THINKING,"Are you now? Well that is interesting. Usually outerlanders do not concern themselves with our ways like that.").also { stage++ }
            54 -> npcl(FacialExpression.HAPPY,"I am one of the members of the council of elders, and should you be able to prove to me that you have something to offer my clan I will vote in your favour at the next meeting.").also { stage++ }
            55 -> playerl(FacialExpression.ASKING,"How can I prove that to you?").also { stage++ }
            56 -> npcl(FacialExpression.HAPPY,"Well, I have but a simple test. This building behind me is my house. Inside I have constructed a puzzle.").also { stage++ }
            57 -> npcl(FacialExpression.HAPPY,"As a Seer to the clan, I value intelligence very highly, so you may think of it as an intelligence test of sorts.").also { stage++ }
            58 -> playerl(FacialExpression.THINKING,"An intelligence test? I thought barbarians were stupid!").also { stage++ }
            59 -> npcl(FacialExpression.ANNOYED,"That is the opinion that outerlanders usually hold of my people, it is true. But that is because people often confuse knowledge with wisdom.").also { stage++ }
            60 -> npcl(FacialExpression.ANNOYED,"My puzzle tests not what you know, but what you can work out. All members of our clan have been tested when they took their trials.").also { stage++ }
            61 -> playerl(FacialExpression.ASKING,"So what exactly does this puzzle consist of, then?").also { stage++ }
            62 -> npcl(FacialExpression.HAPPY,"Well, firstly you must enter my house with no items, weapons or armour. Then it is a simple matter of entering through one door and leaving by the other.").also { stage++ }
            63 -> playerl(FacialExpression.ASKING,"I can't take anything in there with me?").also { stage++ }
            64 -> npcl(FacialExpression.HAPPY,"That is correct outerlander. Everything you need to complete the puzzle you will find inside the building. Nothing more.").also { stage++ }
            65 -> npcl(FacialExpression.HAPPY,"So what say you outerlander? You think you have the wit to earn yourself my vote?").also { stage++ }
            66 -> options("Yes","No").also { stage++ }
            67 -> when(buttonId){
                1 ->{ playerl(FacialExpression.HAPPY,"Yes, I accept your challenge, I have one small question, however...")
                    player?.setAttribute("/save:PeerStarted",true)
                    player?.setAttribute("/save:PeerRiddle", Random.nextInt(0,3))
                    stage = 70
                }
                2 ->{ playerl(FacialExpression.HAPPY,"No, thinking about stuff isn't really my 'thing'. I'd rather go kill something. I'll find someone else to vote for me")
                    stage++
                }
            }
            //No to challenge
            68 -> npcl(FacialExpression.HAPPY,"As you wish, outerlander.").also { stage = 1000 }

            //Yes to challenge
            70 -> npcl(FacialExpression.ASKING,"Yes outerlander?").also { stage++ }
            71 -> playerl(FacialExpression.THINKING,"Well... you say I can bring nothing with me when I enter your house...").also { stage++ }
            72 -> npcl(FacialExpression.ANNOYED,"Yes outerlander??").also { stage++ }
            73 -> playerl(FacialExpression.THINKING,"Well...").also { stage++ }
            74 -> npcl(FacialExpression.ANGRY,"Yes, outerlander???").also { stage++ }
            75 -> playerl(FacialExpression.ASKING,"Where is the nearest bank?").also { stage++ }
            76 -> npcl(FacialExpression.HAPPY,"Ah, I see your problem outerlander. The nearest bank to here is the place known to outerlanders as the Seers Village.").also { stage++ }
            77 -> npcl(FacialExpression.HAPPY,"It is some way South. I do however have an alternative, should you wish to take it.").also { stage++ }
            78 -> playerl(FacialExpression.ASKING,"And what is that?").also { stage++ }
            79 -> npcl(FacialExpression.HAPPY,"I can store all the weapons, armour and items that you have upon you directly into your bank account.").also { stage++ }
            80 -> npcl(FacialExpression.HAPPY,"This will tax what little magic I possess however, so you will have to travel to the bank to withdraw them again.").also { stage++ }
            81 -> npcl(FacialExpression.HAPPY,"What say you outerlander? Do you wish me to do this for you?").also { stage++ }
            82 -> options("Yes","No").also { stage++ }
            83 -> when(buttonId){
                1 -> {
                    val slotAmount = player.inventory.itemCount() + player.equipment.itemCount()
                    if (slotAmount < player.bank.freeSlots()){
                        npcl(FacialExpression.HAPPY,"The task is done. I wish you luck with your test, outerlander.")
                        dumpContainer(player,player.inventory)
                        dumpContainer(player,player.equipment)
                        stage = 1000
                    }
                    else {
                        npcl(FacialExpression.SAD,"I am sorry outerlander, the spell is not working. I believe you may have some objects that you cannot bank with you")
                        stage = 1000
                    }
                }
                2 -> playerl(FacialExpression.HAPPY,"No thanks. Nobody touches my stuff but me!").also { stage++ }
            }

            //No to banking
            84 -> npcl(FacialExpression.HAPPY,"As you wish, outerlander. You may attempt my little task when you have deposited your equipment in the bank").also {
                stage = 1000
            }

            //Yes to banking but cannot bank
            90 -> npcl(FacialExpression.SAD,"I am sorry outerlander, the spell is not working. I believe you may have some objects that you cannot bank with you").also {
                stage = 1000
            }

            //Returning after accepting with items.
            100 -> npcl(FacialExpression.AMAZED,"!").also { stage++ }
            101 -> npcl(FacialExpression.HAPPY,"Ahem, sorry about that. Hello outerlander. What do you want?").also { stage++ }
            102 -> playerl(FacialExpression.ASKING,"So I can bring nothing with me when I enter your house?").also { stage++ }
            103 -> npcl(FacialExpression.HAPPY,"That is correct outerlander, but as I say, I can use my small skill in magic to send your items directly into your bank account from here.").also { stage++ }
            104 -> npcl(FacialExpression.HAPPY,"You will need to manually go to the bank to withdraw them again however.").also { stage++ }
            105 -> npcl(FacialExpression.HAPPY,"Would you like me to perform this small spell upon you, outerlander?").also { stage = 82 }

            //Returning after accepting without items.
            110 -> npcl(FacialExpression.AMAZED,"!").also { stage++ }
            111 -> npcl(FacialExpression.HAPPY,"Ahem, sorry about that. Hello outerlander. What do you want?").also { stage++ }
            112 -> playerl(FacialExpression.ASKING,"So I just have to enter by one door of your house, and leave by the other?").also { stage++ }
            113 -> npcl(FacialExpression.HAPPY,"That is correct outerlander. Be warned it is not as easy as it may at first sound...").also { stage = 1000 }

            //After completing the Seer's Trial.
            120 -> npcl(FacialExpression.AMAZED,"!").also { stage++ }
            121 -> npcl(FacialExpression.HAPPY,"Ahem, sorry about that.").also { stage++ }
            122 -> playerl(FacialExpression.HAPPY,"So you will vote for me at the council?").also { stage++ }
            123 -> npcl(FacialExpression.HAPPY,"Absolutely, outerlander. Your wisdom in passing my test marks you as worthy in my eyes.").also { stage = 1000 }


            //After Fremennik Trials
            150 -> npcl(FacialExpression.AMAZED,"!").also { stage++ }
            151 -> npcl(FacialExpression.HAPPY,"Ahem, sorry about that.").also {
                stage = if(player.achievementDiaryManager.getDiary(DiaryType.FREMENNIK).isComplete(0)){
                    200
                }else 152
            }
            152 -> playerl(FacialExpression.HAPPY,"Hello Peer.").also { stage++ }
            153 -> npcl(FacialExpression.HAPPY,"Greetings to you, brother ${player.getAttribute("fremennikname","dingle")}! What brings you to see me again?").also { stage++ }
            154 -> options("Can you tell my future?","Nothing really.").also{stage++}
            155 -> when(buttonId){
                    1 -> playerl(FacialExpression.ASKING,"I was wondering if you could give me a reading on my future...?").also { stage++ }
                    2 -> playerl(FacialExpression.HAPPY,"Nothing really, I just stopped by to say hello").also { stage = 160 }
                }
            156 -> npcl(FacialExpression.HAPPY,"Ah, you would like a prediction? I do not see that that would be so difficult... Wait a moment...").also {
                stage++
            }
            157 -> npcl(FacialExpression.HAPPY,"Here is your prediction: ${PREDICTIONS[prediction]}").also { stage = 1000 }

            160 -> npcl(FacialExpression.HAPPY,"Well, hello to you too!").also { stage = 1000 }


            200 -> options("Deposit service","Can you tell my future?","Nothing really.").also{ stage++ }
            201 -> when(buttonId){
                1 -> playerl(FacialExpression.HAPPY,"Could you deposit some things for me, please?").also { stage++ }
                2 -> playerl(FacialExpression.ASKING,"I was wondering if you could give me a reading on my future...?").also { stage = 156 }
                3 -> playerl(FacialExpression.HAPPY,"Nothing really, I just stopped by to say hello").also { stage = 160 }
            }
            202 -> npcl(FacialExpression.HAPPY,"Of course, ${player.getAttribute("fremennikname","dingle")}. I am always happy to aid those who have earned the right to wear Fremennik sea boots.").also {
                BankingPlugin.BankDepositInterface()
                stage = 1000
            }

            300 -> npc(FacialExpression.NEUTRAL,"!").also { stage++ }
            301 -> npcl(FacialExpression.NEUTRAL,"Ahem, sorry about that. I have no interest in talking to you just now outerlander.").also { stage = END_DIALOGUE }

            1000 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return PeerTheSeerDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(1288)
    }
}