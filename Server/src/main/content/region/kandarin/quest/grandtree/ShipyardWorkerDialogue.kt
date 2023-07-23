package content.region.kandarin.quest.grandtree

import core.api.getAttribute
import core.api.getQuestStage
import core.api.setAttribute
import core.game.dialogue.DialogueFile
import core.game.global.action.DoorActionHandler
import core.game.node.scenery.Scenery
import core.game.world.map.Location
import core.tools.END_DIALOGUE

class ShipyardWorkerDialogue : DialogueFile(){
    override fun handle(componentID: Int, buttonID: Int) {
        when(stage){
            0 -> npcl("Hey you! What are you up to?").also {
                if(getQuestStage(player!!, TheGrandTree.questName) == 55) {
                    setAttribute(player!!, "/save:grandtree:opt1", false)
                    setAttribute(player!!, "/save:grandtree:opt2", false)
                    setAttribute(player!!, "/save:grandtree:opt3", false)
                }
                stage++
            }
            1 -> playerl("I'm trying to open the gate!").also { stage++ }
            2 -> npcl("I can see that! Why?").also { stage++ }
            3 -> options("I'm from the Ministry of Health and Safety.","Glough sent me.","I'm just looking around.").also { stage++ }
            4 -> when(buttonID){
                1 -> playerl("I'm from the Ministry of Health and Safety.").also { stage = 40}
                2 -> playerl("Glough sent me.").also{ stage = 50}
                3 -> playerl("I'm just looking around.").also { stage = 10 }
            }
            10 -> playerl("I'm just looking around.").also { stage++ }
            11 -> npcl("This ain't a museum! Leave now!").also { stage++ }
            12 -> playerl("I'll leave when I choose!").also { stage++ }
            13 -> npcl("Well you're not on the list so you're not coming in. Go away.").also { stage++ }
            14 -> playerl("Well I'll just stand here then until you let me in.").also { stage++ }
            15 -> npcl("You do that!").also { stage++ }
            16 -> playerl("I will!").also { stage++ }
            17 -> npcl("Yeah?").also { stage++ }
            18 -> playerl("Yeah!").also { stage++ }
            19 -> npcl("...").also { stage++ }
            20 -> playerl("...").also { stage++ }
            21 -> playerl("So are you going to let me in then?").also { stage++ }
            22 -> npcl("No.").also { stage++ }
            23 -> playerl("...").also { stage++ }
            24 -> npcl("...").also { stage++ }
            25 -> playerl("You bored yet?").also { stage++ }
            26 -> npcl("No, I can stand here all day.").also { stage++ }
            27 -> playerl("...").also { stage++ }
            28 -> npcl("...").also { stage++ }
            29 -> playerl("Alright you win. I'll find another way in.").also { stage++ }
            30 -> npcl("No you won't.").also { stage++ }
            31 -> playerl("Yes I will.").also { stage++ }
            32 -> npcl("I'm not starting that again. Maybe if I ignore you you'll go away...").also { stage =
                END_DIALOGUE
            }
            40 -> npcl("Never 'erd of 'em.").also { stage++ }
            41 -> playerl("You will respect my authority!").also { stage++ }
            42 -> npcl("Get out of here before I give you a beating!").also { stage = END_DIALOGUE }
            50 -> npcl("Hmm... really? What for?").also { stage++ }
            51 -> playerl("You're wasting my time! Take me to your superior!").also { stage++ }
            52 -> npcl("OK. Password.").also { stage++ }
            53 -> options("Ka.","Ko.","Ke.").also { stage++ }
            54 -> when(buttonID){
                1 -> playerl("Ka.").also {
                    setAttribute(player!!, "/save:grandtree:opt1", true)
                    stage++
                }
                2 -> playerl("Ko.").also { stage++ }
                3 -> playerl("Ke.").also { stage++ }
            }
            55 -> options("Lo.","Lu.","Le.").also { stage++ }
            56 -> when(buttonID){
                1 -> playerl("Lo.").also { stage++ }
                2 -> playerl("Lu.").also {
                    setAttribute(player!!, "/save:grandtree:opt2", true)
                    stage++
                }
                3 -> playerl("Le.").also { stage++ }
            }
            57 -> options("Mon.","Min.","Men.").also { stage++ }
            58 -> when(buttonID){
                1 -> playerl("Mon.").also { stage++ }
                2 -> playerl("Min.").also {
                    setAttribute(player!!, "/save:grandtree:opt3", true)
                    stage++
                }
                3 -> playerl("Men.").also { stage++ }
            }
            // Correct answer Ka-Lu-Min:
            59 -> {
                if(getAttribute(player!!, "/save:grandtree:opt1", false)
                    && getAttribute(player!!, "/save:grandtree:opt2", false)
                    && getAttribute(player!!, "/save:grandtree:opt3", false)
                ) {
                    DoorActionHandler.autowalkFence(player!!, Scenery(2438, Location(2945, 3041, 0)), 3727, 3728)
                    npcl("Sorry to have kept you.").also { stage = END_DIALOGUE }
                } else {
                    npcl("You have no idea!").also { stage = END_DIALOGUE }
                }
            }
        }
    }
}

class ShipyardWorkerGenericDialogue: DialogueFile(){
    override fun handle(componentID: Int, buttonID: Int) {
        when(stage){
            0 -> playerl("Hello.").also { stage++ }
            1 -> npcl("Hello matey!").also { stage++ }
            2 -> playerl("How are you?").also { stage++ }
            3 -> npcl("Tired!").also { stage++ }
            4 -> playerl("You shouldn't work so hard!").also { stage = END_DIALOGUE }
        }
    }

}

