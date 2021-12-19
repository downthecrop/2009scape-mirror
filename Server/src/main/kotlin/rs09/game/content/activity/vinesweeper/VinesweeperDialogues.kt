import api.*
import core.game.component.Component
import core.game.node.entity.player.Player
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import org.rs09.consts.Items
import rs09.game.content.dialogue.DialogueFile
import rs09.tools.END_DIALOGUE

val BLINKIN_FLAG_LINES = arrayOf(
    "Let me check for ya.",
    "Flags? It appears ya don't have enough room for 'em. Make some space and talk to me again.",
    "Ah! First things first. One of the farm lads dropped off some flags for ya. Ya can have them back. Here ya go.",
    "Righty-ho! Ya can have a total of 10 flags. To get yerself a full set of flags'll cost ya %d gold pieces. Would ya like to buy these flags?",
    "Here ya go, then.",
    "Flags? It appears ya don't have enough room for 'em. Make some space and talk to me again.",
    "Ya don't have the coins fer these, I'm afraid! Come back when yer a little bit richer p'raps?",
    "Right y'are then! See ya.",
    "It looks like ya got all the flags ya need right now. Ya don't need to buy any more."
    )

val WINKIN_FLAG_LINES = arrayOf(
    "Let me check for you.",
    "I'm sorry dear, you don't appear to have enough room. Make some space and talk to me again.",
    "Ah! First things first. One of the farmers dropped off some flags for you. You can have them back. Here you go.",
    "Alright. You can have a total of 10 flags. To obtain a full set of flags will cost you %d coins. Would you like to buy these flags?",
    "Here you are then, dear.",
    "I'm sorry dear, you don't appear to have enough room. Make some space and talk to me again.",
    "I'm afraid it looks like you don't have enough money, dear. Come back and see me again when you have a bit more.",
    "Ok, dear. Goodbye.",
    "It looks like you have all the flags you need. You don't need to buy any more."
    )

abstract class FarmerDialogue : DialogueFile() {
    fun handleFlags(componentID: Int, buttonID: Int, lines: Array<String>) {
        when(stage) {
            20 -> npcl(lines[0]).also { stage++ }
            21 -> {
                val flags = player!!.getAttribute("vinesweeper:stored-flags", 10)
                if(flags > 0) {
                    if(!player!!.inventory.add(Item(Items.FLAG_12625, flags))) {
                        npcl(lines[1])
                        stage = END_DIALOGUE
                    } else {
                        player!!.setAttribute("/save:vinesweeper:stored-flags", 0)
                        npcl(lines[2])
                        stage++
                    }
                } else {
                    stage++
                    handle(componentID, buttonID)
                }
            }
            22 -> {
                val flags = player!!.inventory.getAmount(Items.FLAG_12625)
                if(flags < 10) {
                    val price = 500 * (10 - flags)
                    npcl(String.format(lines[3], price))
                    stage = 220
                } else {
                    stage = 23
                    handle(componentID, buttonID)
                }
            }
            220 -> options("Yes, please.", "No, thanks").also { stage++ }
            221 -> when(buttonID) {
                1 -> playerl("Yes, please.").also { stage = 222 }
                2 -> playerl("No, thanks.").also { stage = 223 }
            }
            222 -> {
                val flags = player!!.inventory.getAmount(Items.FLAG_12625)
                val price = Item(Items.COINS_995, 500 * (10 - flags))
                if(player!!.inventory.containsItem(price) && player!!.inventory.remove(price)) {
                    if(player!!.inventory.add(Item(Items.FLAG_12625, 10 - flags))) {
                        npcl(lines[4])
                        stage = 23
                    } else {
                        npcl(lines[5])
                        // Refund the coins, can't fail because we just removed them
                        player!!.inventory.add(price)
                        stage = END_DIALOGUE
                    }
                } else {
                    npcl(lines[6])
                    stage = END_DIALOGUE
                }
            }
            223 -> npcl(lines[7]).also { stage = END_DIALOGUE }
            23 -> npcl(lines[8]).also { stage = END_DIALOGUE }
        }
    }
}
class BlinkinDialogue : FarmerDialogue() {
    override fun handle(componentID: Int, buttonID: Int) {
        when(stage) {
            0 -> npcl("'Ello there! Welcome to Winkin's Farm. What can I do for ya?").also { stage++ }
            1 -> options("What is this place?", "Do you have any flags?", "Where is Mr. Winkin?").also { stage++ }
            2 -> when(buttonID) {
                1 -> playerl("What is this place?").also { stage = 10 }
                2 -> playerl("Do you have any flags?").also { stage = 20 }
                3 -> playerl("Where is Mr. Winkin?").also { stage = 30 }
            }
            10 -> npcl("Ha! I told ya, it's Winkin's Farm. This is where we grow the magical ogleroots for the rest of the world.").also { stage++ }
            11 -> playerl("So, what can I do here?").also { stage++ }
            12 -> npcl("Ya can improve yer Farming skill by getting some flags from me or Mrs. Winkin, inside. Then, head out to the fields and flag where ya think plants are, er, planted.").also { stage++ }
            13 -> playerl("Is that it?").also { stage++ }
            14 -> npcl("Not at all! There's more. When ya've placed yar flags, the farmers will collect them up and give out points. Ya can trade these points for seeds or experience at the shop inside.").also { stage++ }
            15 -> playerl("Okay, that sounds great! I'll get planting, then.").also { stage++ }
            16 -> npcl("Aye, but be careful where ya plant the flags. If there's no plant under yar flag, the farmer will keep it and they cost a pretty penny to buy more.").also { stage++ }
            17 -> playerl("I see. Thanks for the help.").also { stage++ }
            18 -> npcl("Bye, for now. If I have to say 'flag' one more time, I tell ya...").also { stage = END_DIALOGUE }
            20, 21, 22, 220, 221, 222, 223, 23 -> handleFlags(componentID, buttonID, BLINKIN_FLAG_LINES)
            30 -> npcl("Farmer Winkin? Well, last I 'eard he was heading into market with a fresh load of Ogleroots.").also { stage++ }
            31 -> playerl("Ogleroots?").also { stage++ }
            32 -> npcl("Aye! We get them growing a lot here in the farmyard. We dig 'em up and sell 'em to yar lot.").also { stage++ }
            33 -> playerl("My lot?").also { stage++ }
            34 -> npcl("Aye! Humans.").also { stage++ }
            35 -> playerl("Oh, I see. Thanks!").also { stage++ }
            36 -> npcl("Bye now!").also { stage = END_DIALOGUE }
            // Not accessible in normal conversation, requires "buy-roots" right click
            40 -> playerl("Do you have any Ogleroots to feed the rabbits?").also { stage++ }
            41 -> npcl("I sure do. They'll cost ya 10 gold each. Any ya leave with will be returned to us, but ya'll get yer money back for 'em. How many do ya want?").also { stage++ }
            42 -> {
				player!!.setAttribute("runscript") { amount: Int ->
                    val price = Item(Items.COINS_995, 10 * amount)
                    if(price.amount <= 0){
                        return@setAttribute
                    }
                    if(player!!.inventory.containsItem(price) && player!!.inventory.remove(price)) {
                        if(player!!.inventory.add(Item(Items.OGLEROOT_12624, amount))) {
                            npcl("There ya go! Good luck!")
                            stage = END_DIALOGUE
                        } else {
                            npcl("TODO (crash): dialogue for no space for ogleroots")
                            // Refund the coins, can't fail because we just removed them
                            player!!.inventory.add(price)
                            stage = END_DIALOGUE
                        }
                    } else {
                        npcl("Sorry, ya can't afford that many. Come back when yer feeling a bit richer if ya like!")
                        stage = END_DIALOGUE
                    }
                    //openDialogue(player!!, this, npc as Any)
                }
				player!!.dialogueInterpreter.sendInput(false, "Enter the amount:")
            }
        }
    }
}

class WinkinDialogue : FarmerDialogue() {
    override fun handle(componentID: Int, buttonID: Int) {
        when(stage) {
            0 -> npcl("Oh, hello there, dear. How can I help you?").also { stage++ }
            1 -> options("Where are we?", "Have you got any flags?", "Do you have a spare spade?", "Do you have anything for trade?", "Nothing. I'm fine, thanks.").also { stage++ }
            2 -> when(buttonID) {
                1 -> playerl("Where are we?").also { stage = 10 }
                2 -> playerl("Have you got any flags?").also { stage = 20 }
                3 -> playerl("Do you have a spare spade?").also { stage = 30 }
                4 -> playerl("Do you have anything for trade?").also { stage = 40 }
                5 -> playerl("Nothing. I'm fine, thanks.").also { stage = 50 }
            }
            10 -> npcl("This is Winkin's Farm, dear.").also { stage++ }
            11 -> playerl("Oh, I see. So where is Mr. Winkin?").also { stage++ }
            12 -> npcl("Oh, he headed off to the market a while back. We look after the farm while he's gone.").also { stage++ }
            13 -> npcl("Now, was there anything else you wanted?").also { stage = 1 }
            20, 21, 22, 220, 221, 222, 223, 23 -> handleFlags(componentID, buttonID, WINKIN_FLAG_LINES)
            30 -> npcl("Why, of course. I can sell you one for 5 gold pieces.").also { stage++ }
            31 -> options("Okay, thanks.", "Actually, I've changed my mind.").also { stage++ }
            32 -> when(buttonID) {
                1 -> playerl("Okay, thanks.").also { stage = 320 }
                2 -> playerl("Actually, I've changed my mind.").also { stage = 330 }
            }
            320 -> {
                val price = Item(Items.COINS_995, 5)
                if(player!!.inventory.containsItem(price) && player!!.inventory.remove(price)) {
                    npcl("Here you are, then.")
                    val spade = Item(Items.SPADE_952)
                    if(!player!!.inventory.add(spade)) {
                        GroundItemManager.create(spade, player)
                    }
                    stage = END_DIALOGUE
                } else {
                    npcl("I'm afraid it looks like you don't have enough money, dear. Come back and see me again when you have a bit more.")
                    stage = END_DIALOGUE
                }
            }
            330 -> npcl("Okay then.").also { stage = END_DIALOGUE }
            40 -> npcl("Of course.").also { stage++ }
            41 -> {
                end()
                player!!.interfaceManager.open(Component(686))
            }
            50 -> playerl("Nothing. I'm fine, thanks.").also { stage++ }
            51 -> npcl("Okay, dear.").also { stage = END_DIALOGUE }
        }
    }
}
