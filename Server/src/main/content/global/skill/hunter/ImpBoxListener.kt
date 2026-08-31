package content.global.skill.hunter

import content.data.Lamps
import content.data.LightSource
import core.api.*
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialogueOption
import core.game.dialogue.FacialExpression
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.LogType
import core.game.node.entity.player.info.PlayerMonitor.log
import core.game.node.entity.player.link.IronmanMode
import core.game.node.item.Item
import core.game.system.config.ItemConfigParser
import core.tools.RandomFunction
import org.rs09.consts.Items
import org.rs09.consts.NPCs

/**
 * The Imp-in-a-box that can bank items.
 */

class ImpBoxListener : InteractionListener, InterfaceListener {

    // Great source: https://www.youtube.com/watch?v=k0LhRhcQ0g8
    // wiki: https://runescape.wiki/w/Imp-in-a-box, https://runescape.wiki/w/Magic_box?oldid=1743149
    // uses 701.cs2

    companion object {
        const val RELEASE_IFACE = 478
        private const val TEXT_CHILD = 13
        private const val CLICK_ITEM_BUTTON = 14
        private const val DEPOSIT_OPCODE = 155
        private const val EXAMINE_OPCODE = 196
        private val BOXES = intArrayOf(Items.IMP_IN_A_BOX2_10027, Items.IMP_IN_A_BOX1_10028)

        // sets the bottom text
        fun setText(player: Player, box: Int, selfBank : Boolean = false) {
            val lock = "The imp won't lock himself away in your bank account."
            val select = "Select an item or stack of items to deposit."

            val twoItem = "You can deposit up to 2 items or stacks."
            val oneItem = "You can deposit 1 more item."

            val line1 = if (selfBank) lock else select
            val line2 = if (box == Items.IMP_IN_A_BOX2_10027) twoItem else oneItem
            setInterfaceText(player, "$line1<br>$line2", RELEASE_IFACE, TEXT_CHILD)
        }
    }

    override fun defineListeners() {

        // use an item with the imp box
        onUseAnyWith(IntType.ITEM,*BOXES) { player, used, _ ->
            if (used.id == Items.IMP_IN_A_BOX2_10027 || used.id == Items.IMP_IN_A_BOX1_10028) {
                sendMessage(player, "The imp won't lock himself away in your bank account.")
                return@onUseAnyWith true
            } else {
                bankItem(player, used.asItem())
            }
            return@onUseAnyWith true
        }

        // open the banking interface
        on(BOXES, IntType.ITEM, "bank") {player, node ->
            openInterface(player, RELEASE_IFACE)
            setText(player, node.id)
            return@on true
        }

        // talk to the imp
        on(BOXES, IntType.ITEM, "talk-to") {player, node ->
            openDialogue(player, ImpBoxDialogueFile(node.id), NPC(NPCs.IMP_1531))
            return@on true
        }
    }

    override fun defineInterfaceListeners() {

        // send the interface settings that display the deposit and examine options that get set by the cs2 file
        onOpen(RELEASE_IFACE) { player, _ ->
            val settings = IfaceSettingsBuilder()
                .enableOptions(0, 1)  // option 0 = "Deposit" (CS2 slot 1), option 1 = "Examine" (CS2 slot 2)
                .build()
            player.packetDispatch.sendIfaceSettings(settings, 14, RELEASE_IFACE, 0, 28)
            return@onOpen true
        }

        on(RELEASE_IFACE) { player, _, opcode, buttonID, slot, _ ->
            val item = player.inventory[slot]
            when(buttonID) {
                CLICK_ITEM_BUTTON -> {
                    when(opcode) {
                        DEPOSIT_OPCODE -> bankItem(player, item)
                        EXAMINE_OPCODE -> sendMessage(player, "${item.definition.examine}")
                    }
                }
            }
            return@on true
        }
    }

    // banks the item
    private fun bankItem(player: Player, item: Item) {
        if (item == null) return

        // check for special unbankable items
        val refusal = ImpBoxRefusal.getRefusal(player, item)
        if (refusal != null) {
            sendNPCDialogue(player, NPCs.IMP_1531, refusal.message, FacialExpression.OLD_DEFAULT)
            return
        }

        // check for regular unbankable items
        if (!item.definition.getConfiguration(ItemConfigParser.BANKABLE, true)) {
            sendMessage(player, "The imp seems uneasy about teleporting that.")
            return
        }

        // check if you're UIM (inauthentic mechanic for 2009 era)
        if (player.ironmanManager.mode == IronmanMode.ULTIMATE) {
            sendMessage(player, "A magical force prevents you from banking this item.")
            return
        }

        // check if you're banking the imp (that's very mean of you)
        if (item.id in BOXES) {
            setText(player, item.id, true)
            return
        }

        // can't do in combat
        if (player.inCombat()) {
            sendMessage(player, "You can't use this while in combat.")
            return
        }

        // check for imp random refusal chance (I do not know the authentic chance), or if in wildy
        if (RandomFunction.roll(10)
            || player.skullManager.level > 20
        ) {
            // not sure on authentic dialogue. I took it to be this message because per wiki, it was written as if they use the same refusal dialogue both in wildy, and at random
            // "However, they can randomly refuse and will always refuse to do so past level 20 Wilderness."
            sendNPCDialogue(player, NPCs.IMP_1531, "Sorry, guv'nor, I seem to 'ave caught meself; I can't do nuffink right now.", FacialExpression.OLD_DEFAULT)
            return
        }

        // try to add the item to bank
        if (addItem(player, unnote(item).id, item.amount, Container.BANK)) {
            // if successful, remove the player's item
            if (removeItem(player, item)) {
                // banking is successful, decrement the imp box
                decrementBox(player)
            } else {
                // add was successful, but removal was not. THIS IS ITEM DUPING AND SHOULD NEVER HAPPEN.
                if (removeItem(player, item, Container.BANK)) {
                    // removed the item from bank
                    log(player, LogType.DUPE_ALERT, "Successfully recovered from potential dupe attempt involving the imp-in-a-box.")
                } else {
                    // item is duped :(
                    log(player, LogType.DUPE_ALERT, "Failed to recover from potential dupe attempt involving the imp-in-a-box.")
                }
            }
        } else {
            // adding to bank failed
            sendMessage(player, "There is not enough space left in your bank.")
        }
    }

    // decrements the box
    private fun decrementBox(player: Player) : Boolean {
        if (amountInInventory(player, Items.IMP_IN_A_BOX1_10028) > 0) {
            // replace a 1-charge box with a 0-charge
            if (removeItem(player, Items.IMP_IN_A_BOX1_10028)) {
                addItem(player, Items.MAGIC_BOX_10025)
                sendMessage(player, "The imp teleports away, taking the item to your bank account.")
                closeInterface(player)
                return true
            }
        } else if (amountInInventory(player, Items.IMP_IN_A_BOX2_10027) > 0) {
            // replace a 2-charge box with a 1-charge
            if (removeItem(player, Items.IMP_IN_A_BOX2_10027)) {
                addItem(player, Items.IMP_IN_A_BOX1_10028)
                setText(player, Items.IMP_IN_A_BOX1_10028)
                sendMessage(player, "The imp teleports the item to your bank account.")
                return true
            }
        } else {
            sendMessage(player, "ERROR: No valid imp boxes found. Please report this.")
            return false
        }
        return true
    }

}

// dialogue for the imp in a box
class ImpBoxDialogueFile(val box: Int) : DialogueLabeller() {
    override fun addConversation() {
        exec { player, _ ->
            if (box == Items.IMP_IN_A_BOX2_10027) {
                loadLabel(player, "two start")
            } else {
                loadLabel(player, "one start")
            }
        }

        label("one start")
        player("Hey imp, are you still there?")
        npc(FacialExpression.OLD_DEFAULT, "An who's fault is that, eh? EH? I got betta fings to do dan sit ere waitin' for ya. 'Ave ya made up ya mind what else I'm luggin about?")
        options(
            DialogueOption("ok1", "Yes I have."),
            DialogueOption("no escape", "No, I'm afraid you're going to have to wait a bit longer.")
        )

        label("no escape")
        npc(FacialExpression.OLD_DEFAULT, "Dat's alright guv'nor. I'll get back to me luvverly dream about beads. Big shiny beads. Love it!")
        goto("end")

        label("two start")
        player("Hey imp, can you hear me?")
        npc(FacialExpression.OLD_DEFAULT, "Of course I can hear ya, ya great big ape. You know,", "'veres not even enuf space to swing Bob about in 'ere.", "How about a breather? You know, stretch me pins for a", "bit?")
        options(
            DialogueOption("no", "No, I'm going to keep you in there.", "No, I'm going to keep you in there. I might keep you as a pet."),
            DialogueOption("not bad", "It's not that bad.", "It's not that bad. You've got four big windows, charming company...er..."),
            DialogueOption("wish", "Don't I get three wishes?")
        )

        label("no")
        npc(FacialExpression.OLD_DEFAULT, "Pet!! Nah mate. I fink you'd find dat you'd be my pet!! We is not makin good pets.")
        player("Really? Why not?")
        npc(FacialExpression.OLD_DEFAULT, "Coz...errr...")
        npc(FacialExpression.OLD_DEFAULT, "We bite! Yeah we is biting and...and...er...")
        npc(FacialExpression.OLD_DEFAULT, "We is fire risk! Yeah dat's it! We be burning down your housey and stealin' all ya shiny gems. Oh, and da beads!! Mmmm, beads.")
        player("Fire risk? How does that work?")
        npc(FacialExpression.OLD_DEFAULT, "Is those wizzies. Dey don't like de imps so dey make us go BOOOM!!")
        goto("end")

        label("not bad")
        npc(FacialExpression.OLD_DEFAULT, "Yeah, we's love tiny, crampt space. It be magical. But I is a busy imp, innit? Dragons needin' ticklin', shiny relics needin' stealin', you know how it goes.")
        npc(FacialExpression.OLD_DEFAULT, "So, if you's know whas good for ya, you'd be lettin' me go, right?")
        options(
            DialogueOption("no", "No, I'm going to keep you in there.", "No, I'm going to keep you in there. I might keep you as a pet."),
            DialogueOption("wish", "Don't I get three wishes?")
        )

        label("wish")
        npc(FacialExpression.OLD_DEFAULT, "Nah, mate. Dunno what you're chirpin' about.")
        player("Well, you're a magical creature aren't you? Surely I", "get some wishes for capturing you, or releasing you, or", "something?")
        npc(FacialExpression.OLD_DEFAULT, "I'm finking dat you be a bit confoosed. I is an imp, not", "some namby-pamby genie or some kinda fairy. Ye can", "tell by the horns.")
        npc(FacialExpression.OLD_DEFAULT, "Sayin' dat, I don't fancy being cooped up like one of", "me uncle's pigeons. Tell you what, is there anything", "you need deliverin' to the bank? I may not be no", "cunjerer or sommink like dat, but I can get about nice")
        npc(FacialExpression.OLD_DEFAULT, "an quick like. If you let me scarper, I'll take a couple of", "fings to the bank for ya. You game?")
        options(
            DialogueOption("ok2", "Okay, that sounds fair."),
            DialogueOption("three", "Surely it should be three items?", "Surely it should be three items? Then it's one item per wish."),
            DialogueOption("nothing", "I've got nothing I need banking right now.")
        )

        label("ok2")
        exec { player, _ ->
            openInterface(player, ImpBoxListener.RELEASE_IFACE)
            ImpBoxListener.setText(player, Items.IMP_IN_A_BOX2_10027)
            end()
        }

        label("ok1")
        exec { player, _ ->
            openInterface(player, ImpBoxListener.RELEASE_IFACE)
            ImpBoxListener.setText(player, Items.IMP_IN_A_BOX1_10028)
            end()
        }

        label("three")
        npc(FacialExpression.OLD_DEFAULT, "I've already told ya, I ain't no bloomin' fairy. Besides, you know wot dey say, three's a crowd innit? I don't fink I can hop about carryin' more dan 2 fings.")
        options(
            DialogueOption("ok2", "Okay, that sounds fair."),
            DialogueOption("nothing", "I've got nothing I need banking right now.")
        )

        label("nothing")
        npc(FacialExpression.OLD_DEFAULT, "Great, just blinkin great, dat is. I'll just sit about", "countin' zombie sheep then. One...two...two and a", "bit...three and a bit more... I don't fink sheep 'ave dat", "many legs...")
        goto("end")
    }
}

// messages that the imp responds with for unbankable conditions
// source: https://runescape.wiki/w/Transcript:Imp-in-a-box#Attempting_to_bank_unbankable_items
enum class ImpBoxRefusal(val message: String, val matches: (Player, Item) -> Boolean) {

    // if the player is teleblocked
    TELEBLOCKED(
        "Sorry, guv'nor, I seem to 'ave caught meself; I can't do nuffink right now.",
        { player, _ -> player.isTeleBlocked }
    ),

    // can't bank the POH versions of some items
    POH_ITEM(
        "What use would that be in a bank? You'd be better off leaving it in yer house.",
        { _, item -> item.id in Items.KETTLE_7688..Items.CHEFS_DELIGHT_7755 }
    ),

    // can't bank light sources
    LIGHT_SOURCE(
        "Ow! That's hot! Put it in yer bank yerself!",
        { _, item -> item.id in LightSource.values().map { it.product.id }.toIntArray() }
    ),

    // can't bank bloated toad from Big Chompy Bird Hunting
    BLOATED_TOAD(
        "Nope, between me and you, that toad doesn't look stable.",
        { _, item -> item.id == Items.BLOATED_TOAD_2875 }
    ),

    // can't bank book of knowledge from random events
    BOOK_OF_KNOWLEDGE(
        "What good is banking that book? Just read it or throw it away!",
        { _, item -> item.id == Items.BOOK_OF_KNOWLEDGE_11640 }
    ),

    // can't bank glowing fungus from Haunted Mine
    GLOWING_FUNGUS(
        "I'm no expert, but when a mushroom glows that much, I'm guessing it's dangerous. Keep it away from me.",
        { _, item -> item.id == Items.GLOWING_FUNGUS_4075 }
    ),

    // can't bank gnomeball from the minigame of the same name
    GNOMEBALL(
        "I'm not touching a gnome ball; that's a vicious game! I might die!",
        { _, item -> item.id == Items.GNOMEBALL_751 }
    ),

    // can't bank karamjan rum from Pirate's Treasure
    KARAMJAN_RUM(
        "Don't you know? You can get into trouble for giving alcohol to minors, and I'm pretty small, it has to be said.",
        { _, item -> item.id == Items.KARAMJAN_RUM_431 }
    ),

    // can't bank skeletal monkey bones from Monkey Madness dungeon
    MONKEY_BONES(
        "There's something weird about those bones; I ain't touching them.",
        { _, item -> item.id == Items.BONES_3187 }
    ),

    // can't bank stick from the Werewolf Agility course
    STICK(
        "It's a flipping stick; why bother banking that? You humans are crazy sometimes.",
        { _, item -> item.id == Items.STICK_4179 }
    ),

    // can't bank skill lamps
    LAMP(
        "I know what lamps like that contain; I'm not touching it. I might get hurt.",
        { _, item ->
            Lamps.forItem(item) != null
        }
    ),

    // can't bank unholy symbol from Icthlarin's Little Helper
    UNHOLY_SYMBOL(
        "Sorry, I'm not going to risk upsetting someone by bundling random holy symbols I don't recognise into bank vaults.",
        { _, item -> item.id == Items.UNHOLY_SYMBOL_4683 }
    ),

    // can't bank Ana in a barrel from Tourist Trap
    ANA_BARREL(
        "Hey, if she wants to use the bank, why can't she use her own two legs? Besides, she's too big.",
        { _, item -> item.id == Items.ANA_IN_A_BARREL_1842 }
    );

    companion object {
        fun getRefusal(player: Player, item: Item): ImpBoxRefusal? {
            return values().firstOrNull { it.matches(player, item) }
        }
    }
}