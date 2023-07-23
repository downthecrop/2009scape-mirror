package content.region.misthalin.lumbridge.quest.lostcity

import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs
import core.game.dialogue.Topic
import core.tools.END_DIALOGUE
import core.api.getQuestStage
import core.api.startQuest

/**
 * WarriorDialogue, to handle the dialogue for the Warrior in the Lost City quest
 * @author lila
 * @author Vexia
 */
@Initializable
class WarriorDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player) {

    override fun open(vararg args: Any?): Boolean {
        when(getQuestStage(player,"Lost City")) {
            10 -> playerl(core.game.dialogue.FacialExpression.THINKING,"So let me get this straight: I need to search the trees around here for a leprechaun; and then when I find him, he will tell me where this 'Zanaris' is?").also { stage = 1000 }
            20, 21 -> playerl(core.game.dialogue.FacialExpression.HAPPY,"Have you found anything yet?").also { stage = 2000 }
            100 -> playerl(core.game.dialogue.FacialExpression.HAPPY,"Hey, thanks for all the information. It REALLY helped me out in finding the lost city of Zanaris and all.").also { stage = 3000 }
            else -> npcl(core.game.dialogue.FacialExpression.NEUTRAL,"Hello there traveller.").also { stage = 1 }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage) {
            1 -> showTopics(
                Topic(core.game.dialogue.FacialExpression.THINKING,"What are you camped out here for?",100),
                Topic(core.game.dialogue.FacialExpression.HALF_THINKING,"Do you know any good adventures I can go on?",101)
            )
            2 -> showTopics(
                Topic(core.game.dialogue.FacialExpression.ASKING,"Please tell me.",200),
                Topic(core.game.dialogue.FacialExpression.ANGRY,"I don't think you've found a good adventure at all!",250)
            )
            3 -> showTopics(
                Topic(core.game.dialogue.FacialExpression.ASKING,"Who's Zanaris?",301),
                Topic(core.game.dialogue.FacialExpression.ASKING,"What's Zanaris?",302),
                Topic(core.game.dialogue.FacialExpression.ASKING,"What makes you think it's out here?",300)
            )
            4 -> showTopics(
                Topic(core.game.dialogue.FacialExpression.ASKING,"If it's hidden how are you planning to find it?",400),
                Topic(core.game.dialogue.FacialExpression.LAUGH,"There's no such thing!",450)
            )
            5 -> options("Please tell me.", "Looks like you don't know either.").also { stage = 500 }
            6 -> playerl(core.game.dialogue.FacialExpression.HAPPY,"So a leprechaun knows where Zanaris is eh?").also { stage = 600 }
            7 -> playerl(core.game.dialogue.FacialExpression.HAPPY,"Thanks for the help!").also { stage = 700 }
            8 -> end().also {
                startQuest(player,"Lost City")
            }
            100 -> npcl(core.game.dialogue.FacialExpression.HAPPY,"We're looking for Zanaris...GAH! I mean we're not here for any particular reason at all.").also { stage = 3 }
            101 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL,"Well we're on an adventure right now. Mind you, this is OUR adventure and we don't want to share it - find your own!").also { stage = 2 }
            200 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL,"No.").also { stage++ }
            201 -> playerl(core.game.dialogue.FacialExpression.SAD,"Please?").also { stage++ }
            202 -> npcl(core.game.dialogue.FacialExpression.ANNOYED,"No!").also { stage++ }
            203 -> playerl(core.game.dialogue.FacialExpression.SAD,"PLEEEEEEEEEEEEEEEEEEEEEEEEASE???").also { stage++ }
            204 -> npcl(core.game.dialogue.FacialExpression.ANGRY,"NO!").also { stage = END_DIALOGUE }
            250 -> npcl(core.game.dialogue.FacialExpression.ANGRY,"Hah! Adventurers of our calibre don't just hang around in forests for fun, whelp!").also { stage++ }
            251 -> playerl(core.game.dialogue.FacialExpression.THINKING,"Oh Really?").also { stage++ }
            252 -> playerl(core.game.dialogue.FacialExpression.THINKING,"What are you camped out here for?").also{ stage = 100 }
            300 -> npcl(core.game.dialogue.FacialExpression.HAPPY,"Don't you know of the legends that tell of the magical city, hidden in the swamp... Uh, no, you're right, we're wasting our time here.").also { stage = 4 }
            301 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL,"Ahahahaha! Zanaris isn't a person! It's a magical hidden city filled with treasure and rich... uh, nothing. It's nothing.").also { stage = 4 }
            302 -> npcl(core.game.dialogue.FacialExpression.HALF_THINKING,"I don't think we want other people competing with us to find it. Forget I said anything.").also { stage = 5 }
            400 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL,"Well, we don't want to tell anyone else about that, because we don't want anyone else sharing in all that glory and treasure.").also { stage = 5 }
            450 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL,"When we've found Zanaris you'll... GAH! I mean, we're not here for any particular reason at all.").also { stage = 3 }
            500 -> when(buttonId) {
                1 -> playerl(core.game.dialogue.FacialExpression.ASKING,"Please tell me.").also{ stage = 200 }
                2 -> playerl(core.game.dialogue.FacialExpression.THINKING,"Well, it looks to ME like YOU don't know EITHER, seeing as you're all just sat around here.").also { stage++ }
            }
            501 -> npcl(core.game.dialogue.FacialExpression.ANGRY,"Of course we know! We just haven't found which tree the stupid leprechaun's hiding in yet!").also { stage++ }
            502 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL,"GAH! I didn't mean to tell you that! Look, just forget I said anything okay?").also { stage = 6 }
            600 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL,"Ye.. uh, no. No, not at all. And even if he did - which he doesn't - he DEFINITELY ISN'T hiding in some tree around here. Nope, definitely not. Honestly.").also { stage = 7 }
            700 -> npcl(core.game.dialogue.FacialExpression.WORRIED,"Help? What help? I didn't help! Please don't say I did, I'll get in trouble!").also { stage = 8 }
            1000 -> npcl(core.game.dialogue.FacialExpression.WORRIED,"What? How did you know that? Uh... I mean, no, no you're very wrong. Very wrong, and not right at all, and I definitely didn't tell you about that at all.").also { stage = END_DIALOGUE }
            2000 -> npcl(core.game.dialogue.FacialExpression.SAD,"We're still searching for Zanaris...GAH! I mean we're not doing anything here at all.").also { stage++ }
            2001 -> playerl(core.game.dialogue.FacialExpression.SAD,"I haven't found it yet either.").also { stage = END_DIALOGUE }
            3000 -> npcl(core.game.dialogue.FacialExpression.SAD,"Oh please don't say that anymore! If the rest of my party knew I'd helped you they'd probably throw me out and make me walk home by myself!").also { stage++ }
            3001 -> npcl(core.game.dialogue.FacialExpression.ASKING,"So anyway, what have you found out? Where is the fabled Zanaris? Is it all the legends say it is?").also { stage++ }
            3002 -> playerl(core.game.dialogue.FacialExpression.HAPPY,"You know.... I think I'll keep that to myself.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player): core.game.dialogue.DialoguePlugin {
        return WarriorDialogue(player)
    }
    override fun getIds(): IntArray = intArrayOf(NPCs.WARRIOR_650)
}

/*
Some evidence from early/mid 2009 to help determine facial expressions, and contains some lines not previously included in this warrior's dialogue
this also functions as evidence for most of the warrior's dialogue
https://www.youtube.com/watch?v=nFDifUB8dxQ
https://www.youtube.com/watch?v=4BKRG4yw16o
 */
