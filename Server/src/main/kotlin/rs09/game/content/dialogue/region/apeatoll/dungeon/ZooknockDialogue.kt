package rs09.game.content.dialogue.region.apeatoll.dungeon

import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

/**
 * @author qmqz
 * using temporary variables until quest is actually added
 */

@Initializable
class ZooknockDialogue(player: Player? = null) : DialoguePlugin(player){


    var fr = FacialExpression.FRIENDLY
    var neu = FacialExpression.NEUTRAL
    var ask = FacialExpression.ASKING
    var nor = FacialExpression.OLD_NORMAL
    var ntalk1 = FacialExpression.OLD_CALM_TALK1
    var ntalk2 = FacialExpression.OLD_CALM_TALK2

    var hasSpokenToZook = false

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (!hasSpokenToZook) {
            player(ask, "Hello?").also { stage = 1 }
        } else {
            options("What do we need for the monkey amulet?", "What do we need for the monkey talisman?").also { stage = 57 }
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            1 -> npc(ntalk1, "A human ... here? What business have you on Ape Atoll?").also { stage++ }
            2 -> playerl(neu, "I am on a mission for King Narode Shareen of the Gnomes.").also { stage++ }
            3 -> playerl(neu, "I have been sent to investigate the whereabouts of the 10th squad of his Royal Guard, which went missing during a recent mission to Karamja.").also { stage++ }
            4 -> npc(ntalk1, "Well you've found us - what's left of us, that is.").also { stage++ }
            5 -> npcl(ntalk2, "I am Zooknock, the 10th squad mage. These are Bunkwicker and Waymottin, two of our finest sappers. I assme you will want to know how we got here?").also { stage++ }
            6 -> player(neu, "Of course.").also { stage++ }
            7 -> npcl(ntalk1, "Your story first, human. What possessed you to travel to this forsaken island?").also { stage++ }
            8 -> playerl(neu, "I am currently in the service of your King. I was the human who exposed Glough's warfaring plans and defeated his demon.").also { stage++ }
            9 -> playerl(neu, "As far as I understand, the 10th squad were sent to oversee the decomission of Glough's shipyard in east Karamja.").also { stage++ }
            10 -> playerl(neu, "Rumour has it you were blown off course. The King worried as to your fate, and sent me to investigate.").also { stage++ }
            11 -> npc(ntalk1, "You were sent alone?").also { stage++ }
            12 -> playerl(neu, "No, I have been accompanied by Flight Commander Waydar. We flew south on a special type of glider and landed on a small island to our east.").also { stage++ }
            13 -> npcl(ntalk2, "The so called Crash Island. We left there one of our number, Lumdo, to guard our gliders until our return.").also { stage++ }
            14 -> player(neu, "Yes, we have met. He ferried me across to the atoll.").also { stage++ }
            15 -> npcl(FacialExpression.OLD_ANGRY1, "He did!? He was explicitly ordered to guard the gliders! How did this happen? Who is guarding the gliders now?").also { stage++ }
            16 -> playerl(ntalk1, "Waydar ordered him to leave his post. He is guarding the gliders himself.").also { stage++ }
            17 -> npcl(ntalk2, "Flight Commander Waydar you said? For some reason that name is familiar...").also { stage++ }
            18 -> player(ask, "So why are you here?").also { stage++ }
            19 -> npcl(ntalk2, "The rumours are correct. We were indeed blown off course. Fortunately, we managed to find a small island to steer our gliders towards, else we surely would have drowned.").also { stage++ }
            20 -> playerl(neu, "Then you gathered enough wood to fashion two boats. Your Sergeant and the rest of the 10th squad took the larger boat to this island, leaving Lumdo to guard the gliders and the other boat...").also { stage++ }
            21 -> npc(ntalk1, "Correct. I assume Lumdo told you this?").also { stage++ }
            22 -> player(ask, "Yes. What happened when you landed here?").also { stage++ }
            23 -> npcl(ntalk2, "We split up into several small groups to search the island for potential gnome glider launch sites. Whilst we knew the island to be inhabited, we did not expect its occupants to be quite so ... militant.").also { stage++ }
            24 -> player(FacialExpression.THINKING, "...").also { stage++ }
            25 -> npcl(ntalk1, "Monkeys. Lots of monkeys. They are unlike any other type of monkey we've come across. A far cry from the usual wild variety, these were armed with high quality weaponry and uncanny tactical ability.").also { stage++ }
            26 -> npcl(ntalk2, "We were overwhelmed in numbers. Some of us managed to escape, but the rest were taken captive.").also { stage++ }
            27 -> player(ask, "Who survived?").also { stage++ }
            28 -> npcl(ntalk1, "Myself, the Sergeant, Bunwicker and Waymottin here. Karam, our assassin, probably managed to escape - he usually does.").also { stage++ }
            29 -> player(ask, "And of the rest?").also { stage++ }
            30 -> npcl(ntalk1, "Lumo, Bunkdo and Carado were captured, as I said. We believe they are being held in the jail. We are working on a way to release them. I have sensed there lies a cavern to the north.").also { stage++ }
            31 -> npcl(ntalk2, "We are attempting to tunnel to this northern cavern and then move upwards from there.").also { stage++ }
            32 -> player(ask, "Why don't you just go overground?").also { stage++ }
            33 -> npcl(ntalk2, "We have considered this, but every entrance seems to be excessively guarded.").also { stage++ }
            34 -> player(ask, "I see.").also { stage++ }
            35 -> playerl(ask, "I have spoken to your Sergeant. He believes that the best way to rescue the rest of your squad with the minimum of casualties is to have an insider - a monkey working for us.").also { stage++ }
            36 -> npc(ntalk1, "Aha. He wants me to turn you into a monkey.").also { stage++ }
            37 -> player(ask, "Actually, it was more along the lines of a disguise...").also { stage++ }
            38 -> npcl(ntalk1, "I think you misunderstand, human. Do you know why you were sent here?").also { stage++ }
            39 -> player(ask, "King Narode Shareen asked me to...").also { stage++ }
            40 -> npcl(ntalk1, "Indeed. However, King Narode Shareen is still in contact with Garkor! You were sent here because Garkot specifically asked Narode for you!").also { stage++ }
            41 -> player(ask, "Why wasn't I told?").also { stage++ }
            42 -> npcl(ntalk2, "Before you arrived on this island, that information would have endangered both yourself and the mission.").also { stage++ }
            43 -> player(ask, "But why a human? Why me?").also { stage++ }
            44 -> npcl(ntalk1, "Garkor had long decided that we need a monkey insider. I have the necessary magic to perform the shapeshifting spell, but we needed a human to transform.").also { stage++ }
            45 -> player(ask, "Why don't you just transform a gnome?").also { stage++ }
            46 -> npcl(ntalk2, "It has been tried in the past, but the results were far from ... satisfactory. Although we, like you, are related to the monkeys, the link is too weak for a successful transformation.").also { stage++ }
            47 -> npc(ntalk1, "That is why we need you.").also { stage++ }
            48 -> player(ask, "Right. What do I have to do?").also { stage++ }
            49 -> npcl(ntalk1, "There will be two aspects to your transformation. We must first arrange it so that you are able to understand and communicate with the monkeys.").also { stage++ }
            50 -> npcl(ntalk2, "We must also transform your body so that you may pass amongst them unnoticed.").also { stage++ }
            51 -> npcl(ntalk1, "So that the effects of my spells are not permanent, I will invest their power into magical items which you must find. You can then use them at your will.").also { stage++ }
            52 -> player(ask, "What kind of items?").also { stage++ }
            53 -> npcl(ntalk2, "For the spells to take full effect, they will have to be in some way related to the monkeys.").also { stage++ }
            54 -> npcl(ntalk1, "I suggest that I invest the ability to communicate with the monkeys in an authentic monkey amulet.").also { stage++ }
            55 -> {
                hasSpokenToZook = true
                npcl(ntalk1, "Similarily, the transformation spell should be stored in a monkey talisman of some kind.").also { stage++ }
            }
            56 -> options("What do we need for the monkey amulet?", "What do we need for the monkey talisman?").also { stage++ }
            57 -> when (buttonId) {
                1 -> player(ask, "What do we need for the monkey amulet?").also { stage = 60 }
                2 -> player(ask, "What do we need for the monkey talisman?").also { stage = 100 }
            }

            60 -> npcl(ntalk1, "We need a gold bar, a monkey amulet mould and something to do monkey speech.").also { stage++ }
            61 -> options("Where do I find the gold bar?", "Where do I find a monkey amulet mould?", "Where do I find something to do with monkey speech?", "I'll be back later.").also { stage++ }
            62 -> when (buttonId) {
                1 -> player(ask, "Where do I find the gold bar?").also { stage = 70 }
                2 -> player(ask, "Where do I find a monkey amulet mould?").also { stage = 80 }
                3 -> player(ask, "Where do I find something to do with monkey speech?").also { stage = 90 }
                4 -> player(neu, "I'll be back later.").also { stage = 99 }
            }

            70 -> npcl(ntalk1, "I am not sure. You look wealthy enough to know the answer to that question anyway.").also { stage = 61 }
            80 -> npcl(ntalk1, "They ought to be for sale in the village, but be careful: you probably will not be able to walk straight in and buy one.").also { stage = 61 }
            90 -> npc(ntalk2, "I don't know. I'm sure you'll think of something.").also { stage = 61 }

            100 -> npcl(nor, "We need some kind of monkey remains as well as an authentic magical monkey talisman.").also { stage++ }
            101 -> options("Where do I find monkey remains?", "Where do I find a magical monkey talisman?", "I'll be back later.").also { stage++ }
            102 -> when (buttonId) {
                1 -> player(ask, "Where do I find monkey remains?").also { stage = 110 }
                2 -> player(ask, "Where do I find a magical monkey talisman?").also { stage = 120 }
                3 -> player(neu, "I'll be back later.").also { stage = 99 }
            }

            110 -> npcl(nor, "I'll leave that to your better judgement... However, bear in mind the type of remain might affect the type of monkey you become...").also { stage++ }
            111 -> player(ask, "What if I need to be another type of monkey?").also { stage++ }
            112 -> npcl(ntalk1, "Then bring me different monkey remains and another talisman.").also { stage = 101}

            120 -> npcl(ntalk2, "There ought to be something in the village. I cannot be sure, as I have not spent much time there.").also { stage =  101 }


            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return ZooknockDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ZOOKNOCK_1425)
    }
}
