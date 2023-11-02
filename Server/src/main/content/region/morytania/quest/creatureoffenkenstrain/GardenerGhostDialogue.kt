package content.region.morytania.quest.creatureoffenkenstrain

import core.api.*
import core.game.dialogue.*
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import java.util.*

@Initializable
class GardenerGhostDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        val gardenerGhost = npc as GardenerGhostNPC
        if (gardenerGhost.target == player) {
            // This is the special case where the gardener follows you.
            if (gardenerGhost.location.withinDistance(gardenerGhost.graveLocation, 5)) {
                sendChat(gardenerGhost, "Here is the place where I met me' maker.")
            } else {
                sendChat(gardenerGhost, "Go " + gardenerGhost.location.deriveDirection(gardenerGhost.graveLocation).name.lowercase(Locale.getDefault()).replace('_', '-') + ", mate")
                gardenerGhost.continueFollowing(player)
            }
        } else {
            openDialogue(player, GardenerGhostDialogueFile(), npc)
        }
        return true
    }
    override fun newInstance(player: Player): DialoguePlugin {
        return GardenerGhostDialogue(player)
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GARDENER_GHOST_1675)
    }
}

class GardenerGhostDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        b.onPredicate { player -> !player.equipment.containsAtLeastOneItem(Items.GHOSTSPEAK_AMULET_552) }
                .npcl("Wooo wooo wooooo.")
                .end()
        b.onQuestStages(CreatureOfFenkenstrain.questName, *(0 .. 8).toIntArray(), 100)
                .options().let { optionBuilder ->
                    optionBuilder.option("Tell me about Fenkenstrain.")
                            .playerl("Can you tell me anything about Fenkenstrain?")
                            .item(Items.GHOSTSPEAK_AMULET_552, "You feel power emanate from the Amulet of Ghostspeak", "and the air around you vibrates with the ghostly voice", "of the headless gardener.")
                            .npcl("Oi could tell you a few things about old Fenky, yeah.")
                            .playerl("Go on.")
                            .npcl("Once, this castle were full o' good folk - my friends. Fenky was just the castle doctor, you know, to the lord and the castle folk. I don't know what happened to them all, but one by one they all disappeared.")
                            .npcl("When they were gone a while, I went an dug graves for 'em in the forest. After a while there weren't no one left, but the lord, Fenkenstrain and meself.")
                            .npcl("Old Fenky sent me into the forest to dig 'im a pit - never said what for - then would you believe it, someone chops me 'ead off.")
                            .playerl("Did you see who did it...before...?")
                            .npcl("Before oi kicked the bucket, you mean?")
                            .playerl("Umm...")
                            .npcl("Don't worry yerself. I'm not worried about bein' dead. Worse things could happen, I suppose.")
                            .npcl("One thing I do know is, there ain't no lord of the castle anymore, 'cept for old Fenky. Makes ya think a bit, don't it?")
                            .end()
                    optionBuilder.optionIf("Do you know where the key to the shed is?") { player -> return@optionIf getQuestStage(player, CreatureOfFenkenstrain.questName) == 4 }
                            .item(Items.GHOSTSPEAK_AMULET_552, "You feel power emanate from the Amulet of Ghostspeak", "and the air around you vibrates with the ghostly voice", "of the headless gardener.")
                            .npcl("Got it right 'ere in my pocket. Here you go.")
                            .iteml(4186, "The headless gardener hands you a rusty key.")
                            .endWith { _, player ->
                                addItemOrDrop(player, Items.SHED_KEY_4186)
                            }
                    optionBuilder.optionIf("Do you know where I can find a lightning conductor mould is?") { player -> return@optionIf getQuestStage(player, CreatureOfFenkenstrain.questName) == 4 }
                            .item(Items.GHOSTSPEAK_AMULET_552, "You feel power emanate from the Amulet of Ghostspeak", "and the air around you vibrates with the ghostly voice", "of the headless gardener.")
                            .npcl("A conductor mould, you say? Let me see...")
                            .npcl("There used to be a bloke 'ere, sort of an 'andyman 'e was. Did everything 'round the place - fixed what was broke, swept the chimneys and the like. He would 'ave had a mould, I imagine.")
                            .playerl("Where is he now?")
                            .npcl("E's dead, just like everyone else round 'ere... 'cept for me.")
                            .end()
                    optionBuilder.option("What happened to your head?")
                            .playerl("What happened to your head?")
                            .item(Items.GHOSTSPEAK_AMULET_552, "You feel power emanate from the Amulet of Ghostspeak", "and the air around you vibrates with the ghostly voice", "of the headless gardener.")
                            .npcl("Oi was in the old 'aunted Forest to the south, diggin' a pit for moi old master, old Fenkenstrain, when would you believe it, someone chops me head off. Awful bad luck weren't it?")
                            .playerl("Oh yes, dreadful bad luck.")
                            .npcl("So oi thinks to meself, I don't needs any 'ead to be getting on with me gardenin', long as I got me hands and me spade.")
                            .playerl("Would you show me where the place was?")
                            .npcl("Well, oi s'pose oi've got ten minutes to spare.")
                            .endWith { df, player -> (df.npc!! as GardenerGhostNPC).startFollowing(player) }
                    optionBuilder.optionIf("What's your name?") { player -> return@optionIf getQuestStage(player, CreatureOfFenkenstrain.questName) < 4 }
                            .playerl("What's your name?")
                            .item(Items.GHOSTSPEAK_AMULET_552, "You feel power emanate from the Amulet of Ghostspeak", "and the air around you vibrates with the ghostly voice", "of the headless gardener.")
                            .npcl("Me name? It's been a moivellous long while, mate, since I had any use for such a thing as a name.")
                            .playerl("Don't worry, I was just trying to make conversation.")
                            .npcl("No, no, I can't be havin' that. I'll remember in a minute...")
                            .playerl("Please, don't worry.")
                            .npcl("Lestwit, that's it! Ed Lestwit.")
                            .end()
                }

    }
}