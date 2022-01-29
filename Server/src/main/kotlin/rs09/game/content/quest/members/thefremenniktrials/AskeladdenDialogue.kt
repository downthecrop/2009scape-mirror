package rs09.game.content.quest.members.thefremenniktrials

import core.game.node.entity.player.Player
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs09.consts.Items

@Initializable
//Disabled because the quest isn't done yet.
class AskeladdenDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun open(vararg args: Any?): Boolean {
        player?.let {
            if(player.inventory.contains(3709,1)){
                playerl(FacialExpression.ASKING,"I thought you really liked the long hall?")
                stage = 27
                return true
            }
            else if(player?.getAttribute("sigmund-steps", 0) == 13){
                playerl(FacialExpression.ASKING,"I don't suppose you have any idea where I could find a written promise from Askeladden to stay out of the Longhall?")
                stage = 15
                return true
            }
            else if(player?.getAttribute("sigmundreturning",false) == true){
                playerl(FacialExpression.ASKING,"I've lost one of the items I was supposed to be trading.")
                stage = 35
                return true
            }
            else if(player.questRepository.isComplete("Fremennik Trials")){
                playerl(FacialExpression.HAPPY,"Hello again Askeladden.")
                stage = 40
                return true
            }
            else if (it.questRepository.getStage("Fremennik Trials") > 0) {
                player("Hello there.")
                stage = 0
                return true
            }
            else if(it.getAttribute("fremtrials:lalli-talkedto",false)!!){
                player("Hello there. I understand you managed to get some", "golden wool from Lalli?")
                stage = 0
                return true
            }
            else{
                playerl(FacialExpression.HAPPY,"Hello there.")
                stage = 55
                return true
            }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return AskeladdenDialogue(player)
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npc(FacialExpression.CHILD_LOUDLY_LAUGHING, "HAHAHA! Yeah, that Lalli... what a maroon!").also { stage++ }
            1 -> player("So how did you manage to get the wool?").also { stage++ }
            2 -> npc(FacialExpression.CHILD_FRIENDLY, "Well, as you know, I am doing the same trials that you", "are as part of my test of manhood, and that troll is the", "only one who can get that wool.").also { stage++ }
            3 -> npc(FacialExpression.CHILD_NORMAL, "You might have noticed he's kind of... messed in the", "head buddy! He's real paranoid about people stealing his", "golden apples, isn't he?").also { stage++ }
            4 -> player(FacialExpression.HALF_ASKING,"Indeed he is. So how did you manage to get some", "golden wool from him?").also { stage++ }
            5 -> npc(FacialExpression.CHILD_NORMAL, "It was easy buddy! I persuaded him he needed a pet to", "help him guard his apples. A pet that would never sleep!", "A pet that would never need food or exercise!").also { stage++ }
            6 -> npc(FacialExpression.CHILD_NORMAL, "A pet that would never need him to clean up its... well,", "you know, buddy. A pet that would always be loyal to", "him! A faithful companion for life!").also { stage++ }
            7 -> player(FacialExpression.HALF_ASKING, "What pet is this then?").also { stage++ }
            8 -> npc(FacialExpression.CHILD_LOUDLY_LAUGHING, "A pet ROCK!").also { stage++ }
            9 -> npc(FacialExpression.CHILD_NORMAL, "Man, can you believe that stupid troll traded me some", "of his golden wool for a worthless ROCK?").also { stage++ }
            10 -> npc(FacialExpression.CHILD_FRIENDLY, "Buddy, I hafta say: if brains were explosives, that troll", "wouldn't have enough to blow his nose!").also { stage++ }
            11 -> player(FacialExpression.HALF_ASKING, "Do you have any spare rocks then?").also { stage++ }
            12 -> npc(FacialExpression.CHILD_NEUTRAL, "Sure thing buddy, although I have to say, I doubt even", "that troll is stupid enough to fall for the SAME trick", "TWICE in a row! You can try anyways though!").also {
                player.inventory.add(Item(Items.PET_ROCK_3695))
                player.setAttribute("/save:fremtrials:askeladden-talkedto",true);
                stage = 1000}

            15 -> npcl(FacialExpression.CHILD_LOUDLY_LAUGHING,"What? I can't believe she asked you to get a written promise from me to stay out!").also { stage++ }
            16 -> playerl(FacialExpression.NEUTRAL,"Yup, she really did.").also { stage++ }
            17 -> npcl(FacialExpression.CHILD_FRIENDLY,"Awwwwwww.... but the longhall is just SO MUCH FUN! I'd live there if I could! I suppose you really need that promise to help become a Fremennik, huh?").also { stage++ }
            18 -> playerl(FacialExpression.NEUTRAL,"Yeah, I really do...").also { stage++ }
            19 -> npcl(FacialExpression.CHILD_FRIENDLY,"Well I'll tell you what buddy; As it's you, I'll give you that written promise. All I ask in return for it is a measly 5000 gold. What do you say?").also { stage++ }
            20 -> options("Yes","No").also { stage++ }
            21 -> when(buttonId){
                1 -> if(player?.inventory?.contains(995,5000)!! && !player.inventory.isFull){
                    playerl(FacialExpression.HAPPY,"That's all you want in return? Sure thing. Here you go.")
                    stage = 22
                }else stage = 25
                2 -> stage = 25
            }
            22 -> npcl(FacialExpression.CHILD_FRIENDLY,"Done, and done. Let me know if you got any more cash burning a hole in your pocket I can relieve you of, buddy.").also {
                player?.inventory?.remove(Item(Items.COINS_995,5000))
                player?.inventory?.add(Item(Items.PROMISSORY_NOTE_3709))
                player?.incrementAttribute("sigmund-steps",1)
                stage = 1000
            }
            25 -> playerl(FacialExpression.SAD,"I don't think so... That's really quite a lot of money...").also { stage++ }
            26 -> npcl(FacialExpression.CHILD_LOUDLY_LAUGHING,"Hey, suit yourself buddy. You change your mind, the bank of Askeladden is open for deposits 24 hours a day! Eh-heh- heh-heh-heh.").also { stage = 1000 }

            27 -> npcl(FacialExpression.CHILD_FRIENDLY,"I do!").also { stage++ }
            28 -> playerl(FacialExpression.ASKING,"Then why did you sign this guarantee that you will never enter it again?").also { stage++ }
            29 -> npcl(FacialExpression.CHILD_FRIENDLY,"Aha! It is because I am cunning! That guarantee says that Askeladden will never enter the longhall again!").also { stage++ }
            30 -> npcl(FacialExpression.CHILD_FRIENDLY,"But when I have completed my Fremennik trials, and passed my trial of manhood,").also { stage++ }
            31 -> npcl(FacialExpression.CHILD_LOUDLY_LAUGHING,"I will be given a new name, as is our custom, and will therefore not be Askeladden anymore!").also { stage++ }
            32 -> npcl(FacialExpression.CHILD_LOUDLY_LAUGHING,"That guarantee isn't worth the paper it's written on!! You didn't think I would give up going to the longhall for only 5000 did you?").also { stage++ }
            33 -> playerl(FacialExpression.ANNOYED,"Knowing you, I guess I didn't.").also { stage = 1000 }

            35 -> npcl(FacialExpression.CHILD_NORMAL,"That's a shame. You should speak to the merchant.").also { stage = 1000 }

            40 -> npcl(FacialExpression.CHILD_FRIENDLY,"Hey buddy! That ain't my name no more! My new Fremennik name is Larravak! What's yours?").also { stage++ }
            41 -> playerl(FacialExpression.HAPPY,"My Fremennik name is ${player.getAttribute("fremennikname","lebron james")}.").also { stage++ }
            42 -> npcl(FacialExpression.CHILD_LOUDLY_LAUGHING,"Ha! Ain't as good as my name buddy! So what can I do ya for?").also { stage++ }
            43 -> options("Ask about things to do","Ask for a new pet rock").also { stage++ }
            44 -> when(buttonId){
                1 -> playerl(FacialExpression.ASKING,"So, Askeladd- sorry, Larravak; What is there to do around here now that we are both Fremenniks?").also { stage++ }
                2 -> playerl(FacialExpression.ASKING,"Can I have another pet rock? I lost mine...").also { stage = 50 }
            }
            45 -> npcl(FacialExpression.CHILD_THINKING,"I guess you could do a bit of shopping. We got fresh fish at the docks, and some armour and weapons at Skulgrimen's place.").also { stage++ }
            46 -> playerl(FacialExpression.HAPPY,"Okay, thanks.").also { stage = 1000 }

            50 -> npcl(FacialExpression.CHILD_FRIENDLY,"Sure thing buddy! I'd say take better care of this one, but it's just a rock! I have hundreds of them! Go wild!").also {
                player.inventory.add(Item(Items.PET_ROCK_3695))
                stage = 1000
            }

            55 -> npcl(FacialExpression.CHILD_FRIENDLY,"Hey there, buddy. You're an outerlander, huh? I'm not really supposed to talk to you.").also { stage++ }
            56 -> playerl(FacialExpression.ASKING,"Why not?").also { stage++ }
            57 -> npcl(FacialExpression.CHILD_THINKING,"I dunno buddy. Some stupid tradition or other. We're always told not to talk to outerlanders unless the Chieftain has allowed it.").also { stage++ }
            58 -> playerl(FacialExpression.ASKING,"The Chieftain? Who is that?").also { stage++ }
            59 -> npcl(FacialExpression.CHILD_FRIENDLY,"His names Brundt, buddy. He's a real boring guy. You can find him in the long hall there.").also { stage++ }
            60 -> playerl(FacialExpression.ASKING,"Can you take me to see him?").also { stage++ }
            61 -> npcl(FacialExpression.CHILD_FRIENDLY,"Sorry buddy, I'm not allowed into the long hall. Don't worry, he's easy to spot. He's the one in the fancy helmet. Oh, and he only has one eye.").also { stage++ }
            62 -> playerl(FacialExpression.ASKING,"He's a cyclops?").also { stage++ }
            63 -> npcl(FacialExpression.CHILD_NORMAL,"Nah, nah, nah... He just wears an eyepatch! Well, see ya around buddy!").also { stage = 1000 }


            1000 -> end()
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(2439)
    }

}