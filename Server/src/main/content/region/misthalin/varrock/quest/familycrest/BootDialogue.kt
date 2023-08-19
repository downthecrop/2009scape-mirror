package content.region.misthalin.varrock.quest.familycrest

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable


@Initializable
class BootDialogue (player: Player? = null): DialoguePlugin(player){
    override fun newInstance(player: Player?): DialoguePlugin {
        return BootDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = (args[0] as NPC).getShownNPC(player)
        val qstage = player?.questRepository?.getStage("Family Crest") ?: -1

        if(qstage < 14 || qstage > 14){
            npc(FacialExpression.OLD_NORMAL,"Hello tall person.")
            stage = 1
        }
        else{
            npc(FacialExpression.OLD_NORMAL,"Hello tall person.")
            stage = 2
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){

            1-> options("Hello short person.", "Why are you called Boot?").also { stage = 10 }

            2 -> options("Hello. I'm in search of very high quality gold.", "Hello short person.", "Why are you called Boot?").also { stage= 20}

            10 ->when(buttonId){
                1-> player("Hello short person.").also { stage = 1000 }
                2 -> npc(
                    FacialExpression.OLD_ANGRY1,"I'm called Boot, because when I was very young, ",
                        "I used to sleep, in a large boot.").also{stage++}

            }

            11 -> player("Yeah, great, I didn't want your life story.").also { stage = 1000}

            20 -> when (buttonId){
                1 -> npc(
                    FacialExpression.OLD_DEFAULT,"High quality gold eh? Hmmm... " ,
                        "Well, the very best quality gold that I know of " ,
                        "can be found in an underground ruin near Witchaven.").also{stage++}

                2-> player("Hello short person.").also { stage = 1000 }

                3 -> npc(
                    FacialExpression.OLD_ANGRY1,"I'm called Boot, because when I was very young, ",
                        "I used to sleep, in a large boot.").also{stage = 11}
            }

            21 -> npc("I don't believe it's exactly easy to get to though...").also {
                stage = 1000
                player.questRepository.getQuest("Family Crest").setStage(player, 15)
            }
            1000 -> end()
        }
        return true
    }

    override fun getIds(): IntArray {
         return intArrayOf(665)
    }

}
