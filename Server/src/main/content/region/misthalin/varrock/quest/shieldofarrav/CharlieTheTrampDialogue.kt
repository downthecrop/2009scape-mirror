package content.region.misthalin.varrock.quest.shieldofarrav

import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import content.data.Quests

/**
 * @author qmqz
 */

@Initializable
class CharlieTheTrampDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player){
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(core.game.dialogue.FacialExpression.FRIENDLY,"Spare some change guv?").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> options("Who are you?", "Sorry, I haven't got any.", "Go get a job!", "Ok. Here you go.", "Is there anything down this alleyway?").also { stage++ }

            1 -> when (buttonId) {
                1 -> player(core.game.dialogue.FacialExpression.HALF_THINKING, "Who are you?").also { stage = 50 }
                2 -> player(core.game.dialogue.FacialExpression.NEUTRAL, "Sorry, I haven't got any.").also { stage = 100 }
                3 -> player(core.game.dialogue.FacialExpression.ANNOYED, "Go get a job!").also { stage = 150 }
                4 -> player(core.game.dialogue.FacialExpression.ANNOYED, "Ok. Here you go.").also { stage = 200 }
                5 -> player(core.game.dialogue.FacialExpression.ANNOYED, "Is there anything down this alleyway?").also { stage = 250 }
            }

            50 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY, "Charles. Charles E. Trampin' at your service. Now, about that change you were going to give me...").also { stage = 0 }
            100 -> npc(core.game.dialogue.FacialExpression.SAD, "Thanks anyways!").also { stage = 99 }
            150 -> npc(core.game.dialogue.FacialExpression.ANNOYED, "You startin? I hope your nose falls off!").also { stage = 99 }
            200 -> {
                if (player.inventory.contains(Items.COINS_995, 1)) {
                    player.inventory.remove(Item(Items.COINS_995, 1)).also { end() }
                } else {
                    sendDialogue("You need one coin to give away.").also { stage = 99 }
                }
            }
            201 -> npc(core.game.dialogue.FacialExpression.HAPPY, "Hey, thanks a lot!").also { stage++ }
            202 -> options("No problem.", "Don't I get some sort of quest hint or something now?").also { stage++ }
            203 -> when(buttonId) {
                1 -> player(core.game.dialogue.FacialExpression.FRIENDLY, "No problem.").also { stage = 99 }
                2 -> player(core.game.dialogue.FacialExpression.HALF_THINKING, "So... don't I get some sort of quest hint or something now?").also { stage = 220 }
            }
            220 -> npcl(core.game.dialogue.FacialExpression.ANNOYED, "Huh? What do you mean? That wasn't why I asked you for money.").also { stage++ }
            221 -> npcl(core.game.dialogue.FacialExpression.SAD, "I just need to eat...").also { stage = 99 }
            250 -> npcl(core.game.dialogue.FacialExpression.AFRAID,"The ruthless and notorious criminal gang known as the Black Arm Gang have their headquarters down there.").also { stage++ }
            251 -> options("Thank you for the warning!", "Do you think they would let me join?").also { stage++ }
            252 -> when (buttonId) {
                1 -> player(core.game.dialogue.FacialExpression.FRIENDLY,"Thanks for the warning!").also { stage = 270 }
                2 -> player(core.game.dialogue.FacialExpression.ASKING, "Do you think they would let me join?").also { stage = 280 }
            }
            270 -> npc(core.game.dialogue.FacialExpression.HAPPY, "Don't worry about it.").also { stage = 99 }

            280 -> if (!ShieldofArrav.isBlackArm(player) && !ShieldofArrav.isPhoenix(player)) {
                npcl(core.game.dialogue.FacialExpression.SUSPICIOUS, "You never know. You'll find a lady down there called Katrine. Speak to her.").also { stage = 282 }
            } else if (ShieldofArrav.isBlackArm(player)) {
                npcl(core.game.dialogue.FacialExpression.SUSPICIOUS, "I was under the impression you were already a member...").also { stage = 99 }
            } else {
                npcl(core.game.dialogue.FacialExpression.ANNOYED, "No. You're a collaborator with the Phoenix Gang. There's no way they'll let you join now.").also { stage++ }
            }

            282 -> npc(core.game.dialogue.FacialExpression.AFRAID, "But don't upset her, she's pretty dangerous.").also { stage++ }
            283 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY, "I also heard that Reldo the librarian knows more about them, go talk to him.").also { stage++ }
            284 -> {
                if (!player.questRepository.hasStarted(Quests.SHIELD_OF_ARRAV)) {
                    player.questRepository.getQuest(Quests.SHIELD_OF_ARRAV).start(player)
                    player.questRepository.getQuest(Quests.SHIELD_OF_ARRAV).setStage(player,50)
                } else if (!ShieldofArrav.isBlackArm(player) && !ShieldofArrav.isPhoenix(player)) {
                    player.questRepository.getQuest(Quests.SHIELD_OF_ARRAV).setStage(player, 50)
                }
                end()
            }

            291 -> options("How did you know I was in the Phoenix Gang?", "Any ideas how I could get in there then?").also { stage++ }
            292 -> when(buttonId) {
                1 -> player(core.game.dialogue.FacialExpression.SUSPICIOUS, "How did you know I was in the Phoenix Gang?").also { stage = 300 }
                2 -> stage = 290
            }

            300 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL, "In my current profession I spend a lot of time on the streets and you hear these sorta things sometimes.").also { stage++ }
            301 -> player(core.game.dialogue.FacialExpression.ASKING, "Any ideas how I could get in there then?").also { stage++ }
            302 -> npc(core.game.dialogue.FacialExpression.THINKING, "Hmmm. I dunno.").also { stage++ }
            303 -> npcl(core.game.dialogue.FacialExpression.THINKING, "Your best bet would probably be to find someone else... Someone who ISN'T a member of the Phoenix Gang, and get them to infiltrate the ranks of the Black Arm Gang for you.").also { stage++ }
            304 -> npc(core.game.dialogue.FacialExpression.THINKING, "If you find someone like that, tell 'em to come to me first.").also { stage++ }

            305 -> options("Ok. Good plan!", "Like who?").also { stage++ }
            306 -> when (buttonId) {
                1 -> player(core.game.dialogue.FacialExpression.FRIENDLY, "Ok. Good plan!").also { stage = 310 }
                2 -> player(core.game.dialogue.FacialExpression.ASKING, "Like who?").also { stage = 320 }
            }

            310 -> npc(core.game.dialogue.FacialExpression.LAUGH, "I'm not just a pretty face!").also { stage = 99 }
            320 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL, "There's plenty of other adventurers about besides yourself. I'm sure if you asked one of them nicely they would help you.").also { stage = 99 }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return CharlieTheTrampDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.CHARLIE_THE_TRAMP_641)
    }
}
