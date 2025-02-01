package content.region.kandarin.witchhaven.quest.seaslug

import core.api.*
import core.game.dialogue.*
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs09.consts.NPCs

@Initializable
class FishermanDialogue(player: Player? = null) : DialoguePlugin(player){
    override fun newInstance(player: Player): DialoguePlugin {
        return FishermanDialogue(player)
    }
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        // Fallback to default. Always the start of Sea Slug
        openDialogue(player!!, FishermanDialogueFile(), npc)
        return true
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FISHERMAN_702, NPCs.FISHERMAN_703, NPCs.FISHERMAN_704)
    }
}

class FishermanDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        b.onPredicate { _ -> true }
                .player(FacialExpression.FRIENDLY, "Hello there.")
                .line("Their eyes are staring vacantly into space.")
                .branch { player ->
                    return@branch (1 .. 1).random()
                }.let { branch ->
                    branch.onValue(0)
                            .npc(FacialExpression.AMAZED, "Ye mariners all, as ye pass by,")
                            .npc(FacialExpression.AMAZED, "Come in and drink if you are dry,")
                            .npc(FacialExpression.AMAZED, "Come spend, me lads, your money brisk,")
                            .npc(FacialExpression.AMAZED, "And pop your nose in a jug of this.")
                            .player("You're not fooling anyone you know.")
                            .npc(FacialExpression.AMAZED, "We fooled you easily enough.")
                            .end()

                    branch.onValue(1)
                            .npcl(FacialExpression.AMAZED,"You are not part of our family...")
                            .playerl("Umm. Not last time I checked.")
                            .npcl(FacialExpression.AMAZED,"Soon you will be... Soon you will...")
                            .end()

                    branch.onValue(2)
                            .npcl(FacialExpression.AMAZED,"Keep away human... Leave or face the deep blue...")
                            .playerl("Pardon?")
                            .npcl(FacialExpression.AMAZED,"You will all end up in the blue... Deep deep under the blue...")
                            .end()

                    branch.onValue(3)
                            .npcl(FacialExpression.AMAZED,"Lost to us.. She is Lost to us...")
                            .playerl("Who is lost?")
                            .npcl(FacialExpression.AMAZED,"Trapped by the light... Lost and trapped...")
                            .playerl("Ermm... So you don't want to tell me then?")
                            .npcl(FacialExpression.AMAZED,"Trapped... In stone and darkness...")
                            .end()

                    branch.onValue(4)
                            .npcl(FacialExpression.AMAZED,"Must find family...")
                            .playerl("What?")
                            .npcl(FacialExpression.AMAZED,"Soon we will all be together...")
                            .playerl("Are you ok?")
                            .npcl(FacialExpression.AMAZED,"Must find family... They are all under the blue... Deep deep under the blue...")
                            .playerl("Ermm... I'll leave you to it then.")
                            .end()

                    branch.onValue(5)
                            .npcl(FacialExpression.AMAZED,"Free of the deep blue we are...")
                            .npcl(FacialExpression.AMAZED,"We must find...")
                            .playerl("Yes?")
                            .npcl(FacialExpression.AMAZED,"a new home...")
                            .npcl(FacialExpression.AMAZED,"We must leave this place...")
                            .playerl("Where will you go?")
                            .npcl(FacialExpression.AMAZED,"Away.. Away to her...")
                            .playerl("Riiight.")
                            .end()

                    branch.onValue(6)
                            .npcl(FacialExpression.AMAZED,"Below the deep, deep blue she waits...")
                            .playerl("Who waits?")
                            .npcl(FacialExpression.AMAZED,"They came to her with fire and faith...")
                            .playerl("Who? Who came to who?")
                            .npcl(FacialExpression.AMAZED,"Too many... Too many...")
                            .playerl("Too many what? Make sense!")
                            .npcl(FacialExpression.AMAZED,"Locked away for all eternity...")
                            .playerl("You'd better start making sense Sonny Jim or I'll...")
                            .npcl(FacialExpression.AMAZED,"Free... Soon to be free...")
                            .end()

                    branch.onValue(7)
                            .npcl(FacialExpression.AMAZED,"Must escape the blue.. Deep deep blue")
                            .playerl("Pardon?")
                            .npcl(FacialExpression.AMAZED,"Family... Under the blue... Must escape the blue...")
                            .end()
                }
    }
}
