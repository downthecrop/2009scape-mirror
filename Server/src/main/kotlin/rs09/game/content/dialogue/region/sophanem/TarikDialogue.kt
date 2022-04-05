package rs09.game.content.dialogue.region.sophanem

import api.setAttribute
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs
import rs09.tools.END_DIALOGUE

@Initializable
class TarikDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun open(vararg args: Any?): Boolean {
        npcl(FacialExpression.WORRIED, "Ouch!")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage)
        {
            0 -> playerl(FacialExpression.HALF_WORRIED, "Are you alright?").also { stage++ }
            1 -> npcl(FacialExpression.FRIENDLY, "I'm fine, I'm fine. Just a scratch.").also { stage++ }
            2 -> options("Who are you then?","How did you injure yourself?","Do you know anything about this pyramid?").also { stage++ }
            3 -> when(buttonId) {
                1 -> playerl(FacialExpression.FRIENDLY, "Who are you then?").also { stage = 10 }
                2 -> playerl(FacialExpression.HALF_WORRIED, "How did you injure yourself?").also { stage = 20 }
                3 -> playerl(FacialExpression.HALF_ASKING, "So what's in the pyramid?").also { stage = 30 }
            }

            //Who are you
            10 -> npcl(FacialExpression.FRIENDLY, "Me? I'm Tarik.").also { stage++ }
            11 -> playerl(FacialExpression.HALF_ASKING, "That hat you're wearing doesn't look like it comes from around here?").also { stage++ }
            12 -> npcl(FacialExpression.FRIENDLY, "It was a present, my employer thought it would suit me. I'm not sure what it does, though it does keep the sun off very well.").also { stage++ }
            13 -> playerl(FacialExpression.FRIENDLY, "Who is your employer then?").also { stage++ }
            14 -> npcl(FacialExpression.FRIENDLY, "Simon Templeton, though I haven't seen him since the town gates were closed off.").also { stage++ }
            15 -> playerl(FacialExpression.HALF_THINKING, "Simon Templeton ... that name seems familiar?").also { stage++ }
            16 -> npcl(FacialExpression.FRIENDLY, "He's an archaeologist, I worked as an assistant for him. Though I work for myself now.").also { stage++ }
            17 -> playerl(FacialExpression.FRIENDLY, "So what have you been up to?").also { stage++ }
            18 -> npcl(FacialExpression.FRIENDLY, "Simon suggested that there might be riches to be found in that pyramid over there.").also { stage = 30 }

            //How did you injure yourself
            20 -> npcl(FacialExpression.NEUTRAL, "I've been investigating that pyramid over there.").also { stage = 30 }

            //Do you know anything about this pyramid?
            30 -> npcl(FacialExpression.HALF_THINKING, "Well, I'm not sure. First of all there's something odd about the doors on the place. There's four of them - but three of them lead to an empty tomb. The other one is guarded by something.").also { stage++ }
            31 -> npcl(FacialExpression.THINKING, "If I make it through the door there's a Mummy waiting. But which door the right one is seems to change, it's all quite confusing.").also { stage++ }
            32 -> playerl(FacialExpression.HALF_ASKING, "What about this Mummy?").also { stage++ }
            33 -> npcl(FacialExpression.FRIENDLY, "I don't think he likes people. However he does allow you to enter some of the rooms in the tomb. They are dangerous though.").also { stage++ }
            34 -> playerl(FacialExpression.HAPPY, "Bah, I laugh in the face of danger!").also { stage++ }
            35 -> npcl(FacialExpression.FRIENDLY, "Well, if you go into that pyramid then you'll be laughing a lot then. It's full of poisonous snakes and scarabs, and rather nasty Mummies as well. You could die in there.").also { stage++ }
            36 -> npcl(FacialExpression.FRIENDLY, "I managed to get into one room, but the next room was harder. My lockpicking skills weren't good enough, maybe I should have brought a lockpick.").also { stage++ }
            37 -> api.sendDialogue(player, "The first room in the pyramid requires a thieving level of 21. Each subsequent room requires an extra 10 levels to enter.").also { stage++ }
            38 -> npcl(FacialExpression.NEUTRAL, "There are also lots of poisonous snakes in the urns. They'll bite you if they can. You might be able to charm them if you know how. I'd bring some antipoison anyway if I were you.").also { stage++ }
            39 -> playerl(FacialExpression.FRIENDLY, "It all sounds like fun to me. So is there anything valuable in there?").also { stage++ }
            40 -> npcl(FacialExpression.NEUTRAL, "There are lots of artefacts, you should be able to sell them on the black market, I mean, to a legitimate trader, for some money.").also { stage++ }
            41 -> playerl(FacialExpression.NEUTRAL, "You mean Simon Templeton?").also { stage++ }
            42 -> npcl(FacialExpression.NEUTRAL, "Well, he's a 'legitimate' as you can get, if you can get out of this city and get to him.").also { stage++ }
            43 -> playerl(FacialExpression.FRIENDLY, "I'll find a way. Is there anything else of value in there?").also { stage++ }
            44 -> npcl(FacialExpression.SUSPICIOUS, "I have heard a rumour that there is a valuable magic sceptre in there as well.").also { stage++ }
            45 -> playerl(FacialExpression.HALF_THINKING, "Ah, now you have piqued my interest. What do you know about this sceptre?").also { stage++ }
            46 -> npcl(FacialExpression.SUSPICIOUS, "Not a lot. It is apparently made of gold and covered in jewels, and used to be owned by one of Tumeken's sons.").also { stage++ }
            47 -> playerl(FacialExpression.HALF_ASKING, "Tumeken?").also { setAttribute(player, "/save:tarik-spoken-to", true); stage++ }
            48 -> npcl(FacialExpression.NEUTRAL, "Tumeken, the sun god and head of the gods. His sons were the rulers, chosen by him to rule in his name.").also { stage++ }
            49 -> playerl(FacialExpression.HALF_THINKING, "So these sons were rich and powerful then? This sceptre should be pretty good then.").also { stage++ }
            50 -> npcl(FacialExpression.NEUTRAL, "Hmmm. Well it is supposed to have some magical powers.").also { stage++ }
            51 -> playerl(FacialExpression.HALF_ASKING, "Magical powers? This sounds good. What are they?").also { stage++ }
            52 -> npcl(FacialExpression.LAUGH, "I'll let you know when I find it!").also { stage++ }
            53 -> playerl(FacialExpression.ANNOYED, "Not if I find it first!").also { stage++ }
            54 -> npcl(FacialExpression.FRIENDLY, "Hey! If you find it I deserve a share of the profits! You wouldn't have known it existed without my help.").also { stage++ }
            55 -> playerl(FacialExpression.NEUTRAL, "I'll think about it... Right, I think I'd better investigate this place.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.TARIK_4478)
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return TarikDialogue(player)
    }
}