package content.region.kandarin.quest.grandtree

import core.ServerConstants
import core.api.*
import core.game.component.Component
import core.game.dialogue.DialogueFile
import core.game.interaction.IntType
import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import core.game.interaction.InteractionListener
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.RegionManager
import core.tools.END_DIALOGUE
import org.rs09.consts.Components

@Initializable
class ForemanNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id,location), InteractionListener {

    override fun construct(id: Int, location: Location?, vararg objects: Any?): AbstractNPC {
        return ForemanNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FOREMAN_674)
    }

    override fun defineListeners() {
        on(this.ids, IntType.NPC, "talk-to"){ player, npc ->
            openDialogue(player, ForemanDialogue(), npc)
            return@on true
        }
    }

    override fun finalizeDeath(killer: Entity?) {
        if(getQuestStage(killer as Player, TheGrandTree.questName) == 55) {
            sendMessage(killer,"The foreman drops a piece of paper as he dies.")
            produceGroundItem(killer, Items.LUMBER_ORDER_787, 1, this.location)
        }
        super.finalizeDeath(killer)
    }
}

class ForemanDialogue: DialogueFile(){
    private fun attackPlayer(){
        val foreman = RegionManager.getNpc(player!!.location, NPCs.FOREMAN_674, 6)
        foreman!!.attack(player!!)
    }
    override fun handle(componentID: Int, buttonID: Int) {
        when(stage){
            0 -> playerl("Hello, are you in charge?").also { stage++ }
            1 -> npcl("That's right, and you are...?").also { stage++ }
            2 -> playerl("Glough sent me to check on how you are doing.").also { stage++ }
            3 -> npcl("Right. Glough sent a human?").also { stage++ }
            4 -> playerl("His gnomes are busy.").also { stage++ }
            5 -> npcl("Hmm... in that case we'd better go to my office. Follow me.").also {
                val foremanOffice = Location.create(2954, 3024, 0)
                GameWorld.Pulser.submit(object : Pulse(0) {
                    var count = 0
                    override fun pulse(): Boolean {
                        when (count) {
                            0 -> {
                                player!!.interfaceManager.closeOverlay()
                                player!!.interfaceManager.openOverlay(Component(Components.FADE_TO_BLACK_120))
                            }
                            2 -> {
                                teleport(player!!,foremanOffice)
                                player!!.interfaceManager.closeOverlay()
                                player!!.interfaceManager.openOverlay(Component(Components.FADE_FROM_BLACK_170))
                                return true
                            }
                        }
                        count++
                        return false
                    }
                })
                stage++
            }
            6 -> npcl("Tell me again why you're here.").also { stage++ }
            7 -> playerl("Er... Glough sent me?").also { stage++ }
            8 -> npcl("By the way how is Glough? Still with his wife?").also { stage++ }
            9 -> options("Yes, they're getting on great.","Always arguing as usual!","Sadly his wife is no longer with us!").also { stage++ }
            10 -> when(buttonID){
                1 -> playerl("Yes, they're getting on great.").also { stage++ }
                2 -> playerl("Always arguing as usual!").also { stage++ }
                3 -> playerl("Sadly his wife is no longer with us!").also { stage = 20 }
            }
            11 -> npcl("Really? That's odd, considering she died last year. Die, imposter!").also {
                attackPlayer()
                stage = END_DIALOGUE
            }
            20 -> npcl("Right answer. I have to watch for imposters. What's Glough's favourite dish?").also { stage++ }
            21 -> options("He loves tangled toads legs.","He loves worm holes.","He loves choc bombs.").also { stage++ }
            22 -> when(buttonID){
                1 -> playerl("He loves tangled toads legs.").also { stage++ }
                2 -> playerl("He loves worm holes.").also { stage = 30 }
                3 -> playerl("He loves choc bombs.").also { stage++ }
            }
            23 -> npcl("Our survey said.... Bzzzzzz! Wrong answer!").also {
                attackPlayer()
                stage = END_DIALOGUE
            }
            30 -> npcl("OK. Just one more. What's the name of his new girlfriend?").also { stage++ }
            31 -> options("Anita.","Alia.","Elena.").also { stage++ }
            32 -> when(buttonID){
                1 -> playerl("Anita.").also { stage = 35 }
                2 -> playerl("Alia.").also { stage++ }
                3 -> playerl("Elena.").also { stage++ }
            }
            33 -> npcl("You almost had me fooled! Die, imposter!").also {
                attackPlayer()
                stage = END_DIALOGUE
            }
            35 -> npcl("Well, well, you do know Glough. Sorry for the interrogation but I'm sure you understand.").also { stage++ }
            36 -> playerl("Of course, security is paramount.").also { stage++ }
            37 -> npcl("As you can see things are going well.").also { stage++ }
            38 -> playerl("Indeed.").also { stage++ }
            39 -> npcl("When I was asked to build a fleet large enough to invade Port Sarim and carry 300 gnome troops I said: 'If anyone can, I can.'").also { stage++ }
            40 -> playerl("That's a lot of troops!").also { stage++ }
            41 -> npcl("True, but if the gnomes are really going to take over " + ServerConstants.SERVER_NAME + " they'll need at least that.").also { stage++ }
            42 -> playerl("Take over?").also { stage++ }
            43 -> npcl("Of course, why else would Glough want 30 battleships? Between you and me I don't think he stands a chance.").also { stage++ }
            44 -> playerl("No?").also { stage++ }
            45 -> npcl("I mean, for the kind of battleships Glough's ordered I'll need tons and tons of lumber! Still, if he says he can supply the wood I'm sure he can! Anyway, here's the order for the lumber.").also { stage++ }
            46 -> sendDialogue(player!!, "The Foreman gives you the Lumber order.").also {
                addItemOrDrop(player!!,Items.LUMBER_ORDER_787)
                stage++
            }
            47 -> playerl("OK. I'll head off and give this order to Glough.").also {
                stage = END_DIALOGUE
            }
        }
    }
}