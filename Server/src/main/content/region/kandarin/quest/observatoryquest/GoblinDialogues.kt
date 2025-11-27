package content.region.kandarin.quest.observatoryquest

import core.api.*
import core.game.dialogue.*
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs09.consts.NPCs

class GoblinDialogues : InteractionListener {
    override fun defineListeners() {
        on(NPCs.GREASYCHEEKS_6127, NPC, "talk-to") { player, node ->
            openDialogue(player, GreasycheeksDialogueFile(), node as NPC)
            return@on true
        }
        on(NPCs.SMELLYTOES_6128, NPC, "talk-to") { player, node ->
            openDialogue(player, SmellytoesDialogueFile(), node as NPC)
            return@on true
        }
        on(NPCs.CREAKYKNEES_6129, NPC, "talk-to") { player, node ->
            openDialogue(player, CreakykneesDialogueFile(), node as NPC)
            return@on true
        }
    }
}

class GreasycheeksDialogueFile : DialogueLabeller() {
    override fun addConversation() {
        assignToIds(NPCs.GREASYCHEEKS_6127)
        player("Hello.")
        npc(ChatAnim.OLD_NORMAL, "Shush! I'm concentrating.")
        player("Oh, sorry.")
    }
}

class SmellytoesDialogueFile : DialogueLabeller() {
    override fun addConversation() {
        assignToIds(NPCs.SMELLYTOES_6128)
        player("Hi there.")
        npc(ChatAnim.OLD_NORMAL, "Hey, ids me matesh!")
        player("Sorry, have we met?")
        npc(ChatAnim.OLD_NORMAL, "Yeah! you wazsh wiv me in dat pub overy by hill!")
        player("I have no idea what you're going on about.")
        npc(ChatAnim.OLD_NORMAL, "Glad yeeash remembers.")
    }
}

class CreakykneesDialogueFile : DialogueLabeller() {
    override fun addConversation() {
        assignToIds(NPCs.CREAKYKNEES_6129)
        player("Where did you get that lens?")
        npc(ChatAnim.OLD_NORMAL, "From that strange metal thing up on the hill.")
        player(ChatAnim.ANGRY, "You should give that back!")
        npc(ChatAnim.OLD_NORMAL, "Even if it's cracked?")
        player("Ah, well, I suppose it's of no use. But, still.")
    }
}

@Initializable
class GreasycheeksNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {
    override fun construct(id: Int, location: Location?, vararg objects: Any?): AbstractNPC {
        return GreasycheeksNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GREASYCHEEKS_6127)
    }

    override fun handleTickActions() {
        super.handleTickActions()
        if (RandomFunction.roll(12)) {
            sendChat(this, arrayOf(
                "Cook, cook, cook!",
                "I'm so hungry!",
                "This is gonna taste sooo good!",
            ).random())
        }
    }
}

@Initializable
class SmellytoesNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {
    override fun construct(id: Int, location: Location?, vararg objects: Any?): AbstractNPC {
        return SmellytoesNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SMELLYTOES_6128)
    }

    override fun handleTickActions() {
        super.handleTickActions()
        if (RandomFunction.roll(12)) {
            sendChat(this, arrayOf(
                "Doh ray meeee laa doh faaa!",
                "La la la! Do di dum dii!",
            ).random())
        }
    }
}

@Initializable
class CreakykneesNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {
    override fun construct(id: Int, location: Location?, vararg objects: Any?): AbstractNPC {
        return CreakykneesNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.CREAKYKNEES_6129)
    }

    override fun handleTickActions() {
        super.handleTickActions()
        if (RandomFunction.roll(12)) {
            sendChat(this, arrayOf(
                "Was that a spark?",
                "Come on! Please light!",
            ).random())
        }
    }
}
@Initializable
class LostGoblinNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {
    override fun construct(id: Int, location: Location?, vararg objects: Any?): AbstractNPC {
        return LostGoblinNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GOBLIN_6125)
    }

    override fun handleTickActions() {
        super.handleTickActions()
        if (RandomFunction.roll(12)) {
            sendChat(this, arrayOf(
                "Which way should I go?",
                "These dungeons are such a maze.",
                "Where's the exit?!?",
                "This is the fifth time this week. I'm lost!",
                "I've been wandering around down here for hours.",
                "How do you get back to the village?",
                "I hate being so lost!",
                "How could I be so disoriented?",
                "Where am I? I'm so lost.",
                "I know the exit's around here, somewhere."
            ).random())
        }
    }
}