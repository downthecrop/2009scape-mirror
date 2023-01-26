package content.region.fremennik.rellekka.quest.thefremenniktrials

import core.api.addItem
import core.api.removeItem
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

@Initializable
class ThorvaldDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player){
    override fun open(vararg args: Any?): Boolean {
        if(player?.inventory?.contains(3706,1) == true){
            playerl(core.game.dialogue.FacialExpression.HAPPY,"I would like your contract to offer your services as a bodyguard.")
            stage = 215
            return true
        }
        else if(player?.inventory?.contains(3710,1) == true){
            playerl(core.game.dialogue.FacialExpression.ASKING,"You didn't take much persuading to 'lower' yourself to a bodyguard.")
            stage = 220
            return true
        }
        else if(player?.getAttribute("sigmundreturning",false) == true){
            playerl(core.game.dialogue.FacialExpression.ASKING,"Is this item for you?")
            stage = 214
            return true
        }
        else if(player?.getAttribute("sigmund-steps", 0) == 11){
            playerl(core.game.dialogue.FacialExpression.ASKING,"I don't suppose you have any idea where I could find the token to allow your seat at the champions table?")
            stage = 211
            return true
        }
        else if(player?.getAttribute("sigmund-steps", 0) == 10){
            playerl(core.game.dialogue.FacialExpression.ASKING,"I don't suppose you have any idea where I could find a brave and powerful warrior to act as a bodyguard?")
            stage = 200
            return true
        }
        else if (args.size > 1) {
            npcl(core.game.dialogue.FacialExpression.FRIENDLY, "Hahaha! Well fought outerlander! Now come down from there, you have passed my trial with flying colours!")
            stage = 150
            return true
        }
        else if (player?.questRepository?.isComplete("Fremennik Trials")!!) {
            playerl(core.game.dialogue.FacialExpression.FRIENDLY, "Howdy Thorvald!")
            stage = 0
            return true
        }
        else if(player!!.getAttribute("fremtrials:thorvald-vote", false)!!){
            playerl(core.game.dialogue.FacialExpression.FRIENDLY, "So can I count on your vote at the council of elders now Thorvald?")
            stage = 160
            return true
        }
        else if(player.questRepository.isComplete("Fremennik Trials")){
            playerl(core.game.dialogue.FacialExpression.HAPPY,"Howdy Thorvald!")
            stage = 250
            return true
        }
        else if(!player.questRepository.hasStarted("Fremennik Trials")){
            npcl(core.game.dialogue.FacialExpression.ANNOYED, "Leave me be, outerlander. I have nothing to say to the likes of you.")
            stage = 1000
            return true
        }
        else if (!player!!.getAttribute("fremtrials:thorvald-vote", false)!!) {
            if (player!!.getAttribute("fremtrials:warrior-accepted", false)!!) {
                options("What do I have to do again?", "Who is my opponent?", "Can't I do something else?")
                stage = 100
                return true
            } else {
                playerl(core.game.dialogue.FacialExpression.FRIENDLY, "Hello!")
                stage = 60
                return true
            }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            //After Fremennik Trials
            0 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY, "And greetings to you too. It is good to see new blood entering the Fremennik; we gain our strength by bringing new warriors into the tribe.").also { stage = 1000 }

            //Warrior Trial
            60 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL, "Hello yourself, outerlander. What brings you to dare speak to a mighty Fremennik warrior such as myself?").also { stage++ }
            61 -> playerl(core.game.dialogue.FacialExpression.HALF_ASKING, "Erm... are you a member of the council?").also { stage++ }
            62 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL, "The Fremennik council of elders? Why, of course I am. I am recognised as one the clans mightiest warriors.").also { stage++ }
            63 -> npcl(core.game.dialogue.FacialExpression.HALF_ASKING, "What is it to you outerlander?").also { stage++ }
            64 -> playerl(core.game.dialogue.FacialExpression.NEUTRAL, "Well... I was wondering if you could vote for me to become a Fremennik.").also { stage++ }
            65 -> npcl(core.game.dialogue.FacialExpression.LAUGH, "An outerlander wishes to become a Fremennik!?!? Ha! That is priceless!").also { stage++ }
            66 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL, "Well, let us say that I am not totally against this concept. As a warrior, I appreciate the value of brave and powerful warriors to our clan, and even though you may be an outerlander,").also { stage++ }
            67 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL, "I will not hold this against you if you can prove yourself to be fierce of heart in a combat situation to me.").also { stage++ }
            68 -> playerl(core.game.dialogue.FacialExpression.NEUTRAL, "So how can I prove that? You want to fight me? Come on then, bring it on! Right here, right now, buddy!").also { stage++ }
            69 -> npcl(core.game.dialogue.FacialExpression.LOUDLY_LAUGHING, "Hahahahaha! You certainly show some spirit for an outerlander!").also { stage++ }
            70 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL, "But spirit does not always make a good warrior. It takes both skill and spirit to be so. I have a test that I give all Fremenniks on their path to be a member of the clan.").also { stage++ }
            71 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL, "My test will challenge both your combat prowess and your bravery equally. Should you pass it you will earn my vote at the council, and more importantly my respect for you as a warrior.").also { stage++ }
            72 -> npcl(core.game.dialogue.FacialExpression.HALF_ASKING, "So what say you, outerlander? Are you prepared for the battle?").also { stage++ }
            73 -> options("Yes", "No").also { stage++ }
            74 -> when(buttonId){
                1 -> playerl(core.game.dialogue.FacialExpression.EVIL_LAUGH, "Am I prepared? I'll show you what combat's all about, you big sissy barbarian type guy!").also { player.setAttribute("/save:fremtrials:warrior-accepted",true); stage = 80 }
                2 -> playerl(core.game.dialogue.FacialExpression.NEUTRAL, "No thanks, I'm pretty sure that I can find someone else to vote for me.").also { stage = 90 }
            }
                //Yes
                80 -> npcl(core.game.dialogue.FacialExpression.LAUGH, "Hahahahaha! I'm beginning to like you already, outerlander!").also { stage++ }
                81 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL, "Then allow me to present you with my challenge; This ladder here will take you to a place of combat. I have placed a special warrior down there to challenge you.").also { stage++ }
                82 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL, "Battle him to the death, and you will pass my challenge. If at any point you wish to leave combat, simply climb back up the ladder, to leave that place. If you leave you will of course fail the test.").also { stage++ }
                83 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL, "You may retry my test in the future if you fail, but you must stay down there until the death if you wish for my vote at the council. You must defeat him three times to prove that you are worthy.").also { stage++ }
                84 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL, "The fourth time that you fight him will be to the death, so do not show cowardice.").also { stage++ }
                85 -> playerl(core.game.dialogue.FacialExpression.EVIL_LAUGH, "Is that all? It will be easy!").also { stage++ }
                86 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL, "No, there is one more important rule; You may not enter the battleground with any armour or weaponry of any kind.").also { stage++ }
                87 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL, "If you need to place your equipment into your bank account, I recommend that you speak to the seer, who knows a spell that will do that for you.").also { stage = 1000 }

                //No thanks
                90 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL, "Hmm, not so brave after all, outerlander? Perhaps it is for the best. I doubt you have what it takes to pass my challenge.").also { stage = 1000 }

            //Warrior Trial Accepted Not Completed
            100 -> when(buttonId){
                1 -> playerl(core.game.dialogue.FacialExpression.HALF_ASKING, "So what do I have to do to earn your vote at the council again?").also { stage = 110 }
                2 -> playerl(core.game.dialogue.FacialExpression.HALF_ASKING, "So, who is my opponent?").also { stage = 120 }
                3 -> playerl(core.game.dialogue.FacialExpression.HALF_ASKING, "I don't really like fighting that much... Isn't there something else I can do to earn your vote at the council of elders?").also { stage = 130 }
            }
                //What do I have to do again?
                110 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL, "I will not offer my vote to anybody whose bravery in combat I do not trust completely. You must go down that ladder and fight your foe to the death.").also { stage = 1000 }

                //Who is my opponent?
                120 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL, "Ah, a wise question before entering combat. His name is Koschei the deathless. He is something of a mystery, even to us.").also { stage++ }
                121 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL, "On one of our regular raiding parties, our longship discovered a man in the frozen waters far north-east of here. We took him aboard our ship, thinking he must be dead.").also { stage++ }
                122 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL, "To our amazement he was perfectly healthy, even though he must have been in those deadly icey waters for many weeks.").also { stage++ }
                123 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL, "He has no memory of who he is, or how he came to be there, except for his own name: Koschei. We named him 'The Deathless' because he is seemingly unkillable!").also { stage++ }
                124 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL, "Any combat technique used against him, he learns instantly! He also apparently can heal himself from any wound at will!").also { stage++ }
                125 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL, "When he attacks, his weapon moves like a whirlwind! He can hide his combat level from his opponents at will as well!").also { stage++ }
                126 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL, "He is truly a horribly fierce opponent to face! I am only glad that he has chosen to stay here with us!").also { stage++ }
                127 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL, "The daylight makes him feel weak, so we have built him his own battleground beneath this building, where he can train his fiercesome skills without being disturbed.").also { stage++ }
                128 -> playerl(core.game.dialogue.FacialExpression.LAUGH, "But he can't REALLY be unkillable...").also { stage++ }
                129 -> playerl(core.game.dialogue.FacialExpression.SUSPICIOUS, "...can he?").also { stage++ }
                130 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL, "Some say he cannot die, for he has hidden his heart outside of his body to be kept forever safe in a duck egg.").also { stage++ }
                131 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL, "Others say he has been cursed by the gods to wander this land forever, never knowing any peace in his life, but only combat.").also { stage++ }
                132 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL, "Some claim that the sword he carries is the source of all his power, and if he should lose it, then exactly one minute later, he will turn back into his true form; A weakened, lame, old man.").also { stage++ }
                133 -> playerl(core.game.dialogue.FacialExpression.HALF_ASKING, "And what do you believe?").also { stage++ }
                134 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL, "I believe you shouldn't look a gift horse in the mouth. He is a fearfully powerful warrior, but more importantly; He is on OUR side, not against us.").also { stage++ }
                135 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL, "He is content testing the battle skills of anyone taking their Fremennik trials of manhood, and I am content knowing that should an enemy ever invade").also { stage++ }
                136 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL, "our town, while our warriors are out on a raiding party, Koschei will be able to hold off ANY invader long enough for us to make our return.").also { stage = 1000}

                //Can't I do something else?
                140 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY, "Yes of course outerlander! If you bring me five raw sardines then I will vote for you instead!").also { stage++ }
                141 -> playerl(core.game.dialogue.FacialExpression.EXTREMELY_SHOCKED, "REALLY?!?!?").also { stage++ }
                142 -> npcl(core.game.dialogue.FacialExpression.LOUDLY_LAUGHING, "HAHAHAHAHAHA! No, of course not! You are stupid, even by outerlander standards!").also { stage++ }
                143 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL, "If you want my vote, you must pass my trial. It is as simple as that.").also { stage = 1000 }

            //Warrior Trial Completed - after the fight
            150 -> playerl(core.game.dialogue.FacialExpression.NEUTRAL, "But... I don't understand... I did not manage to beat Koschei...").also { stage++ }
            151 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL, "I did not say you had to, outerlander! All I asked was that you fought to the death! And you did! The death was your own!").also { stage++ }
            152 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL, "I was not interested in how strong you are! I was interested in how BRAVE you are!").also { stage++ }
            153 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL, "You fought a superior enemy to your very last breath - THAT is bravery.").also { stage++ }
            154 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL, "I would be honoured to represent you to the council as worthy of being a Fremennik after watching that superb battle!").also { stage = 1000 }

            //Warrior Trial Completed
            160 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL, "Absolutely! I watched the entire battle, and was extremely impressed with your bravery in combat!").also { stage = 1000 }

            //Sigmund stuff
            200 -> npcl(core.game.dialogue.FacialExpression.ANNOYED,"Know you not who I am outerlander? There are none more brave or powerful than me amongst all the Fremennik!").also { stage++ }
            201 -> npcl(core.game.dialogue.FacialExpression.ANGRY,"The role of bodyguard is below me, as a noble warrior. You might as well ask me to babysit the children!").also { stage++ }
            202 -> playerl(core.game.dialogue.FacialExpression.THINKING,"Is there no way you would do this for me?").also { stage++ }
            203 -> npcl(core.game.dialogue.FacialExpression.ANNOYED,"There is but one way outerlander. Since I was steeled in battle, I have dreamt of earning my place at the Champions Table in the Long Hall.").also { stage++ }
            204 -> npcl(core.game.dialogue.FacialExpression.ANNOYED,"It is a tradition amongst us that the bravest and strongest are honoured with a table of champions to drink and feast all that they can in our Long Hall.").also { stage++ }
            205 -> npcl(core.game.dialogue.FacialExpression.ANNOYED,"Unfortunately, there are only a fixed number of places available at the table, and these places were all filled many moons ago by others.").also { stage++ }
            206 -> npcl(core.game.dialogue.FacialExpression.ANNOYED,"Although my worthiness is undeniable, the only way I may take my place is if one of those already there die, or give up their place to me voluntarily.").also { stage++ }
            207 -> playerl(core.game.dialogue.FacialExpression.THINKING,"So you want me to go kill one of them off for you? Make it look like an accident?").also { stage++ }
            208 -> npcl(core.game.dialogue.FacialExpression.EXTREMELY_SHOCKED,"WHAT? No, no, not at all! I am shocked you would suggest such a thing!").also { stage++ }
            209 -> npcl(core.game.dialogue.FacialExpression.THINKING,"If you can persuade one of the Revellers to give up their Champions' Token to you so that I might take their place, you may have my contract as a bodyguard.").also { stage++ }
            210 -> playerl(core.game.dialogue.FacialExpression.HAPPY,"Okay, I'll see what I can do.").also {
                player?.incrementAttribute("sigmund-steps",1)
                stage = 1000
            }
            211 -> npcl(core.game.dialogue.FacialExpression.HAPPY,"You will need to persuade one of the revellers in the Long Hall to give up their token, and their place, in deference to my own worthiness somehow.").also { stage = 1000 }

            214 -> npcl(core.game.dialogue.FacialExpression.ANNOYED,"I'm afraid not, outerlander.").also { stage = 1000 }

            215 -> npcl(core.game.dialogue.FacialExpression.ANGRY,"Oh you would, would you outerlander? I have already told you, I will not demean myself with such a baby sitting job until I can sit in the Longhall with pride.").also { stage++ }
            216 -> playerl(core.game.dialogue.FacialExpression.HAPPY,"It's a good thing I have the Champions' Token right here then, isn't it?").also { stage++ }
            217 -> npcl(core.game.dialogue.FacialExpression.HAPPY,"Ah... well this is a different matter. With that token I can claim my rightful place as a champion in the Long hall!").also { stage++ }
            218 -> npcl(core.game.dialogue.FacialExpression.HAPPY,"Here outerlander, I can suffer the indignity of playing babysitter if it means that I can then revel with my warrior equals in the Long Hall afterwards!").also {
                removeItem(player,3706)
                addItem(player,3710)
                stage++
            }
            219 -> npcl(core.game.dialogue.FacialExpression.HAPPY,"Here outerlander, take this contract; I will fulfill it to my utmost.").also { stage = 1000 }

            220 -> npcl(core.game.dialogue.FacialExpression.HAPPY,"You misunderstand, outerlander. Normally I will only battle for a noble cause, but have never been recognised as a true champion here.").also { stage++ }
            221 -> npcl(core.game.dialogue.FacialExpression.HAPPY,"With this Champion's token, I can stand alongside my warrior brethren in the Long Hall, and revel in the glories of past victories together!").also { stage = 1000 }

            250 -> npcl(core.game.dialogue.FacialExpression.HAPPY,"And greetings to you too. It is good to see new blood entering the Fremennik; we gain our strength by bringing new warriors into the tribe.").also { stage = 1000 }


            1000 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return ThorvaldDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.THORVALD_THE_WARRIOR_1289)
    }

}