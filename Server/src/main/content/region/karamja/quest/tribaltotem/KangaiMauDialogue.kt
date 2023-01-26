package content.region.karamja.quest.tribaltotem

import core.api.isQuestComplete
import core.api.removeItem
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.Items

@Initializable
class KangaiMauDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player) {

    override fun open(vararg args: Any?): Boolean {
        if(!player.questRepository.hasStarted("Tribal Totem")){
            npcl(core.game.dialogue.FacialExpression.HAPPY,"Hello. I'm Kangai Mau of the Rantuki Tribe.")
            stage = 0
        } else if(isQuestComplete(player, "Tribal Totem")) {
            npcl(core.game.dialogue.FacialExpression.HAPPY, "Many greetings esteemed thief.")
            stage = 40
        }
        else if(player.inventory.containsAtLeastOneItem(Items.TOTEM_1857)){
            npcl(core.game.dialogue.FacialExpression.ASKING,"Have you got our totem back?")
            stage = 35
        }
        else if(player.questRepository.hasStarted("Tribal Totem")){
            npcl(core.game.dialogue.FacialExpression.ASKING,"Have you got our totem back?")
            stage = 30
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> options("And what are you doing here in Brimhaven?","I'm in search of adventure!","Who are the Rantuki tribe?").also { stage++ }
            1 -> when(buttonId){
                1 -> playerl(core.game.dialogue.FacialExpression.ASKING,"And what are you doing here in Brimhaven?").also { stage = 5 }
                2 -> playerl(core.game.dialogue.FacialExpression.HAPPY,"I'm in search of adventure!").also { stage = 15 }
                3 -> playerl(core.game.dialogue.FacialExpression.ASKING,"Who are the Rantuki Tribe?").also { stage = 10 }
            }

            5 -> npcl(core.game.dialogue.FacialExpression.HAPPY,"I'm looking for someone brave to go on important mission. Someone skilled in thievery and sneaking about.").also { stage++ }
            6 -> npcl(core.game.dialogue.FacialExpression.HAPPY,"I am told I can find such people in Brimhaven.").also { stage++ }
            7 -> playerl(core.game.dialogue.FacialExpression.HAPPY,"Yep. I have heard there are many of that type here.").also { stage++ }
            8 -> npcl(core.game.dialogue.FacialExpression.THINKING,"Let's hope I find them.").also { stage = 1000 }

            10 -> npcl(core.game.dialogue.FacialExpression.HAPPY,"A proud and noble tribe of Karamja.").also { stage++ }
            11 -> npcl(core.game.dialogue.FacialExpression.ANGRY,"But now we are few, as men come from across, steal our land, and settle on our hunting grounds").also { stage = 1000 }

            15 -> npcl(core.game.dialogue.FacialExpression.HAPPY,"Adventure is something I may be able to give. I need someone to go on a mission to the city of Ardougne.").also { stage++ }
            16 -> npcl(core.game.dialogue.FacialExpression.HAPPY,"There you will find the house of Lord Handlemort. In his house he has our tribal totem. We need it back.").also { stage++ }
            17 -> playerl(core.game.dialogue.FacialExpression.ASKING,"Why does he have it?").also { stage++ }
            18 -> npcl(core.game.dialogue.FacialExpression.ANGRY,"Lord Handlemort is an Ardougnese explorer which means he think he have the right to come to my tribal home,").also { stage++ }
            19 -> npcl(core.game.dialogue.FacialExpression.ANGRY,"steal our stuff and put in his private museum.").also { stage++ }
            20 -> playerl(core.game.dialogue.FacialExpression.THINKING,"How can I find Handlemoret's house? Ardougne IS a big place...").also { stage++ }
            21 -> npcl(core.game.dialogue.FacialExpression.ANNOYED,"I don't know Ardougne. You tell me.").also { stage++ }
            22 -> playerl(core.game.dialogue.FacialExpression.HAPPY,"Ok, I will get it back.").also {
                player.questRepository.getQuest("Tribal Totem").start(player)
                player.questRepository.getQuest("Tribal Totem").setStage(player, 10)
                stage++
            }
            23 -> npcl(core.game.dialogue.FacialExpression.HAPPY,"Best of luck with that adventurer").also { stage = 1000 }

            30 -> playerl(core.game.dialogue.FacialExpression.SAD,"Not yet, sorry.").also { stage = 1000 }

            35 -> playerl(core.game.dialogue.FacialExpression.HAPPY,"Yes I have.").also { stage++ }
            36 -> npcl(core.game.dialogue.FacialExpression.HAPPY,"You have??? Many thanks brave adventurer! Here, have some freshly cooked Karamjan fish, caught specially by my tribe.").also { stage++ }
            37 -> sendDialogue("You hand over the totem").also {
                if(!isQuestComplete(player, "Tribal Totem") && removeItem(player, Items.TOTEM_1857)) {
                        player.questRepository.getQuest("Tribal Totem").finish(player)
                        stage = 1000
                } else {
                    stage = 1000
                }
            }

            40 -> player(core.game.dialogue.FacialExpression.NEUTRAL, "Hey.").also { stage = 1000 }

            1000 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return KangaiMauDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(846)
    }
}