package content.global.skill.summoning.familiar

import core.api.openDialogue
import core.game.dialogue.ChatAnim
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialoguePlugin
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager.TeleportType
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import core.game.world.map.zone.impl.WildernessZone
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs09.consts.NPCs

/**
 * Represents the spirit graahk's dialogue
 * @author Splinter & Bishop
 * @version 2.0
 */
@Initializable
class SpiritGraahkDialogue : DialoguePlugin {
    constructor()
    constructor(player: Player?) : super(player)

    override fun newInstance(player: Player?): DialoguePlugin {
        return SpiritGraahkDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC?
        if (npc !is Familiar) {
            return false
        }
        val fam = npc as Familiar
        if (fam.getOwner() !== player) {
            player.packetDispatch.sendMessage("This is not your familiar.")
            return true
        } else {
            interpreter.sendOptions("Select an Option", "Chat", "Teleport")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (buttonId) {
            1 -> if (player.skills.getStaticLevel(Skills.SUMMONING) >= 67) {
                openDialogue(player, SpiritGraahkDialogueFile(), npc)
            } else {
                player.sendMessage("The Graahk does not feel like talking now.") //This message is likely inauthentic, but I cannot source the correct one so I'm keeping the default here -Bishop
                end()
            }

            2 -> if (!WildernessZone.checkTeleport(player, 20)) {
                player.sendMessage("You cannot teleport with the Graahk above level 20 wilderness.")
                end()
            } else {
                player.teleporter.send(Location(2786, 3002), TeleportType.NORMAL)
                end()
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SPIRIT_GRAAHK_7363, NPCs.SPIRIT_GRAAHK_7364)
    }
}

class SpiritGraahkDialogueFile : DialogueLabeller() {
    override fun addConversation() {
        exec { player, npc ->
            val spiritGraahkRandomConversation = RandomFunction.random(4)
            when (spiritGraahkRandomConversation) {
                0 -> loadLabel(player, "spiky spikes")
                1 -> loadLabel(player, "pet the spikes")
                2 -> loadLabel(player, "therapeutic spikes")
                3 -> loadLabel(player, "inspect the spikes")
            }
        }

        label("spiky spikes")
            player(ChatAnim.NEUTRAL, "Your spikes are looking particularly spiky today.")
            npc(ChatAnim.GHRAAK_SHAKE_VIGOROUS, "Graaaaahk raaaawr?", "(Really, you think so?)")
            player(ChatAnim.HAPPY, "Yes. Most pointy, indeed.")
            npc(ChatAnim.GHRAAK_SHAKE_VIGOROUS, "Raaaawr...", "(That's really kind of you to say. I was going to spike", "you but I won't now...)")
            player(ChatAnim.HAPPY, "Thanks?")
            npc(ChatAnim.GHRAAK_SHAKE_VIGOROUS, "...Grrrrr ark.", "(...I'll do it later instead.)")
            player(ChatAnim.HAPPY, "*Sigh!*")

        label("pet the spikes")
            npc(ChatAnim.GHRAAK_SHAKE_MILD, "Graahk grrrrowl?", "(My spikes hurt, could you pet them for me?)")
            player(ChatAnim.HAPPY, "Aww, of course I can I'll just... Oww! I think you drew", "blood that time.")

        label("therapeutic spikes")
            npc(ChatAnim.GHRAAK_SHAKE_MILD, "Graahk!", "(Hi!)") //?
            player(ChatAnim.SUSPICIOUS, "Hello. Are you going to spike me again?")
            npc(ChatAnim.GHRAAK_SHAKE_VIGOROUS, "Grar rawr howl!", "(No, I got a present to apologise for last time.)")
            player(ChatAnim.HAPPY, "That's really sweet, thank you.")
            npc(ChatAnim.GRAAHK_NOD, "Howl graaahk rawr.", "(Here you go, it's a special cushion to make you", "comfortable.)")
            player(ChatAnim.ANGRY, "It's made of spikes!")
            npc(ChatAnim.GHRAAK_SHAKE_VIGOROUS, "Howl graaahk rawr.", "(Yes, but they're therapeutic spikes.)")
            player(ChatAnim.ANGRY, "...")

        label("inspect the spikes")
            player(ChatAnim.HAPPY, "How's your day going?")
            npc(ChatAnim.GHRAAK_SHAKE_VIGOROUS, "Graaahk. Grak grak!", "(It's great! Actually I've got something to show you!)")
            player(ChatAnim.HALF_ASKING, "Oh? What's that?")
            npc(ChatAnim.GRAAHK_NOD, "Grrrrrk hiss graaaaa!", "(You'll need to get closer!)")
            player(ChatAnim.HALF_ASKING, "I can't see anything...")
            npc(ChatAnim.GHRAAK_SHAKE_VIGOROUS, "Grah grr aaaaahk grahk.", "(It's really small - even closer.)")
            player(ChatAnim.ANGRY, "Oww! I'm going to have your spikes trimmed!")
    }
}