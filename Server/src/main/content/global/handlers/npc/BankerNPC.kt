package content.global.handlers.npc

import content.global.handlers.scenery.BankBoothListener
import core.ServerConstants
import core.api.*
import core.game.dialogue.ChatAnim
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialogueOption
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.IronmanMode
import core.game.world.map.Direction
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs09.consts.NPCs

/**
 * Provides dialogue tree for all generic banker NPCs as well as
 * handles all the common interactions like 'bank' and 'collect'.
 *
 * @author vddCore
 * @author Player Name
 */
@Initializable
class BankerNPC : AbstractNPC, InteractionListener {
    val NPC_IDS = intArrayOf(
        NPCs.BANKER_44, NPCs.BANKER_45, NPCs.BANKER_494, NPCs.BANKER_495, NPCs.BANKER_496, NPCs.BANKER_497,
        NPCs.BANKER_498, NPCs.BANKER_499, NPCs.BANKER_1036, NPCs.BANKER_1360, NPCs.BANKER_2163, NPCs.BANKER_2164,
        NPCs.BANKER_2354, NPCs.BANKER_2355, NPCs.BANKER_2568, NPCs.BANKER_2569, NPCs.BANKER_2570, NPCs.BANKER_3198,
        NPCs.BANKER_3199, NPCs.BANKER_5258, NPCs.BANKER_5259, NPCs.BANKER_5260, NPCs.BANKER_5261, NPCs.BANKER_5776,
        NPCs.BANKER_5777, NPCs.BANKER_5912, NPCs.BANKER_5913, NPCs.BANKER_6200, NPCs.BANKER_6532, NPCs.BANKER_6533,
        NPCs.BANKER_6534, NPCs.BANKER_6535, NPCs.BANKER_7445, NPCs.BANKER_7446, NPCs.BANKER_7605,
        NPCs.GUNDAI_902,
        NPCs.GHOST_BANKER_1702, NPCs.GNOME_BANKER_166, NPCs.NARDAH_BANKER_3046, NPCs.MAGNUS_GRAM_5488, NPCs.TZHAAR_KET_ZUH_2619,
        NPCs.SIRSAL_BANKER_4519, NPCs.FADLI_958, NPCs.BANK_TUTOR_4907, NPCs.JADE_4296,
        NPCs.OGRESS_BANKER_7049, NPCs.OGRESS_BANKER_7050,
        NPCs.BANKER_6538
    )

    companion object {
        private const val LUNAR_ISLE_BANK_REGION = 8253

        /**
         * This is poorly named, but performs a few checks to see if the player
         * is trying to access the bank on the Lunar Isle and returns a boolean
         * controlling whether or not to pass the quick bank or collection use.
         */
        fun checkLunarIsleRestriction(player: Player, node: Node): Boolean {
            if (node.location.regionId != LUNAR_ISLE_BANK_REGION)
                return false

            if (!hasSealOfPassage(player)) {
                return true
            }

            return false
        }

        fun attemptBank(player: Player, node: Node): Boolean {
            val npc = node as NPC

            if (checkLunarIsleRestriction(player, node)) {
                openDialogue(player, npc.id, npc)
                return true
            }

            npc.faceLocation(null)
            openBankAccount(player)

            return true
        }

        fun attemptCollect(player: Player, node: Node): Boolean {
            val npc = node as NPC

            if (checkLunarIsleRestriction(player, node)) {
                openDialogue(player, npc.id, npc)
                return true
            }

            npc.faceLocation(null)
            openGrandExchangeCollectionBox(player)

            return true
        }
    }

    //Constructor spaghetti because Arios I guess
    constructor() : super(0, null)
    private constructor(id: Int, location: Location) : super(id, location)

    override fun construct(id: Int, location: Location, vararg objects: Any?): AbstractNPC {
        return BankerNPC(id, location)
    }

    private fun findAdjacentBankBoothLocation(): Pair<Direction, Location>? {
        for (side in arrayOf(Direction.SOUTH, Direction.WEST, Direction.NORTH, Direction.EAST)) {
            val boothLocation = location.transform(side)
            val sceneryObject = getScenery(boothLocation)

            if (sceneryObject != null && sceneryObject.id in BankBoothListener.BANK_BOOTHS) {
                return Pair(side, boothLocation.transform(side, 1))
            }
        }

        return null
    }

    private fun provideDestinationOverride(entity: Entity, node: Node): Location {
        val npc = node as NPC
        return when(npc.id) {
            /* Ogress bankers are 2x2 with their spawn being offset to south-western tile. */
            NPCs.OGRESS_BANKER_7049, NPCs.OGRESS_BANKER_7050 -> npc.location.transform(3, 1, 0)
            /* Magnus has no bank booth nearby so we need to handle that edge case here. */
            NPCs.MAGNUS_GRAM_5488 -> npc.location.transform(Direction.NORTH, 2)
            /* Special-cased NPCs, idk why */
            NPCs.BANKER_6532, NPCs.BANKER_6533, NPCs.BANKER_6534, NPCs.BANKER_6535 -> npc.location.transform(npc.direction, 1)
            else -> {
                if (npc is BankerNPC) {
                    npc.findAdjacentBankBoothLocation()?.let {
                        return it.second
                    }
                }
                return npc.location
            }
        }
    }

    override fun defineListeners() {
        on(NPC_IDS, IntType.NPC, "bank", handler = Companion::attemptBank)
        on(NPC_IDS, IntType.NPC, "collect", handler = Companion::attemptCollect)
        on(NPC_IDS, IntType.NPC, "talk-to") { player, node ->
            DialogueLabeller.open(player, BankerDialogueLabellerFile(), node as NPC)
            return@on true
        }
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.NPC, NPC_IDS, "bank", "collect", "talk-to", handler = ::provideDestinationOverride)
    }

    override fun init() {
        super.init()

        findAdjacentBankBoothLocation()?.let {
            val (boothDirection, _) = it

            direction = boothDirection
            isWalks = false

            setAttribute("facing_booth", true)
        }
    }

    class BankerDialogueLabellerFile : DialogueLabeller() {
        val BANKERS_WITH_EXTRA_OPTION = intArrayOf(NPCs.BANK_TUTOR_4907, NPCs.JADE_4296, NPCs.BANKER_6538)

        override fun addConversation() {
            exec { player, npc ->
                if (npc.id == NPCs.SIRSAL_BANKER_4519 && !hasSealOfPassage(player)) {
                    loadLabel(player, "no seal of passage")
                }
                if (hasIronmanRestriction(player, IronmanMode.ULTIMATE)) {
                    if (npc.id == NPCs.JADE_4296) loadLabel(player, "uim for jade")
                    loadLabel(player, "uim")
                }
                if (npc.id == NPCs.JADE_4296) loadLabel(player, "hello for jade")
                loadLabel(player, "hello")
            }
            label("no seal of passage")
                npc(ChatAnim.ANNOYED, "What are you doing here, Fremennik?!")
                player(ChatAnim.WORRIED, "I have a Seal of Pass...")
                npc(ChatAnim.ANGRY, "No you don't! Begone!")
                // todo: kick them out, but only one of the two banker types should do so (see historical wiki)
                goto("nowhere")
            label("uim for jade")
                npc(ChatAnim.ANNOYED, "Greetings, warrior. I wish I could help you, but our services are not available for Ultimate Iron@g[men,women].")
                goto("nowhere")
            label("uim")
                npc(ChatAnim.ANNOYED, "My apologies, dear @g[sir,madam], our services are not available for Ultimate Iron@g[men,women]")
                goto("nowhere")
            label("hello for jade")
                npc("Greetings warrior, how may I help you?")
                exec { player, _ -> loadLabel(player, if (hasAwaitingGrandExchangeCollections(player)) "ge collect" else "main options") }
            label("hello")
                npc("Good day, would you like to access your bank account?")
                exec { player, _ -> loadLabel(player, if (hasAwaitingGrandExchangeCollections(player)) "ge collect" else "main options") }
            label("ge collect")
                npc("Before we go any further, I should inform you that you have items ready for collection from the Grand Exchange.")
                goto("main options")

            label("main options")
            options(
                DialogueOption("how to use", "How do I use the bank?") { _, npc -> return@DialogueOption npc.id == NPCs.BANK_TUTOR_4907 },
                DialogueOption("who is the bounty hunter banker", "Who are you?") { _, npc -> return@DialogueOption npc.id == NPCs.BANKER_6538 },
                DialogueOption("access", "I'd like to access my bank account please.", expression = ChatAnim.ASKING),
                DialogueOption("buy second bank", "I'd like to open a secondary bank account.") { player, _ -> return@DialogueOption ServerConstants.SECOND_BANK && !hasActivatedSecondaryBankAccount(player) },
                DialogueOption("switch second bank", "I'd like to switch to my primary bank account.") { player, _ -> return@DialogueOption hasActivatedSecondaryBankAccount(player) && isUsingSecondaryBankAccount(player) },
                DialogueOption("switch second bank", "I'd like to switch to my secondary bank account.") { player, _ -> return@DialogueOption hasActivatedSecondaryBankAccount(player) && !isUsingSecondaryBankAccount(player) },
                DialogueOption("pin", "I'd like to check my PIN settings."),
                DialogueOption("collect", "I'd like to collect items."),
                DialogueOption("what is this place", "What is this place?") { _, npc -> return@DialogueOption npc.id !in BANKERS_WITH_EXTRA_OPTION },
                DialogueOption("jade's employment history", "How long have you worked here?") { _, npc -> return@DialogueOption npc.id == NPCs.JADE_4296 },
                DialogueOption("is the bounty hunter banker afraid", "Aren't you afraid of working in the Wilderness?") { _, npc -> return@DialogueOption npc.id == NPCs.BANKER_6538 && !ServerConstants.SECOND_BANK }
            )

            label("how to use")
            options(
                DialogueOption("using bank", "Using the bank itself.", "Using the bank itself. I'm not sure how....?"),
                DialogueOption("using deposit", "Using bank deposit boxes.", "Using Bank deposit boxes.... what are they?"),
                DialogueOption("using PIN", "What's this PIN thing that people keep talking about?", "What's this PIN thing that people keep talking about?"),
                DialogueOption("nowhere", "Goodbye.")
            )

            label("using bank")
            npc("Speak to any banker and ask to see your bank", "account. If you have set a PIN you will be asked for", "it, then all the belongings you have placed in the bank", "will appear in the window. To withdraw one item, left-")
            npc("click on it once.")
            npc("To withdraw many, right-click on the item and select", "from the menu. The same for depositing, left-click on", "the item in your inventory to deposit it in the bank.", "Right-click on it to deposit many of the same items.")
            npc("To move things around in your bank: firstly select", "Swap or Insert as your default moving mode, you can", "find these buttons on the bank window itself. Then click", "and drag an item to where you want it to appear.")
            npc("You may withdraw 'notes' or 'certificates' when the", "items you are trying to withdraw do not stack in your", "inventory. This will only work for items which are", "tradeable.")
            npc("For instance, if you wanted to sell 100 logs to another", "player, they would not fit in one inventory and you", "would need to do multiple trades. Instead, click the", "Note button to withdraw the logs as 'certs' or 'notes'")
            npc("then withdraw the items you need.")
            goto("how to use")

            label("using deposit")
            npc("They look like grey pillars, there's one just over there,", "near the desk. Bank deposit boxes save so much time.", "If you're simply wanting to deposit a single item, 'Use'", "it on the deposit box.")
            npc("Otherwise, simply click once on the box and it will give", "you a choice of what to deposit in an interface very", "similar to the bank itself. Very quick for when you're", "simply fishing or mining etc.")
            goto("how to use")

            label("using PIN")
            npc("The PIN - Personal Identification Number - can be", "set on your bank account to protect the items there in", "case someone finds out your account password. It", "consists of four numbers that you remember and tell")
            npc("no one.")
            npc("So if someone did manage to get your password they", "couldn't steal your items if they were in the bank.")
            goto("how to use")

            label("who is the bounty hunter banker")
            npc(ChatAnim.NEUTRAL, "How inconsiderate of me, dear @g[sir,madam]. My name is Maximillian Sackville and I conduct operations here on behalf of The Bank of Gielinor.")
            goto("main options")

            label("access")
            exec { player, _ -> openBankAccount(player) }
            goto("nowhere")

            label("buy second bank")
            npc("Certainly. We offer secondary accounts to all our customers.")
            npc("The secondary account comes with a standard fee of 5,000,000 coins. The fee is non-refundable and account activation is permanent.")
            npc("If your inventory does not contain enough money to cover the costs, we will complement the amount with the money inside your primary bank account.")
            npc("Knowing all this, would you like to proceed with opening your secondary bank account?")
            options(
                DialogueOption("buy second bank yes", "Yes, I am still interested.", expression = ChatAnim.HAPPY),
                DialogueOption("buy second bank no", "Actually, I've changed my mind.", expression = ChatAnim.ANNOYED)
            )

            label("buy second bank yes")
            exec { player, _ -> loadLabel(player, when (activateSecondaryBankAccount(player)) {
                SecondaryBankAccountActivationResult.ALREADY_ACTIVE -> "already"
                SecondaryBankAccountActivationResult.INTERNAL_FAILURE -> "failure"
                SecondaryBankAccountActivationResult.NOT_ENOUGH_MONEY -> "not enough cash"
                SecondaryBankAccountActivationResult.SUCCESS -> "success"
            }) }
            label("already")
                npc(ChatAnim.FRIENDLY, "Your bank account was already activated, there is no need to pay twice.")
                goto("main options")
            label("failure")
                npc(ChatAnim.ANNOYED, "I must apologize, the transaction was not successful. Please check your primary bank account and your inventory - if there's money missing, please screenshot your chatbox and contact the game developers.")
                goto("main options")
            label("not enough cash")
                npc(ChatAnim.ANNOYED, "It appears that you do not have the money necessary to cover the costs associated with opening a secondary bank account. I will be waiting here until you do.")
                goto("main options")
            label("success")
                npc(ChatAnim.FRIENDLY, "Your secondary bank account has been opened and can be accessed through any of the Bank of Gielinor's employees. Thank you for choosing our services.")
                goto("main options")

            label("buy second bank no")
            npc("Very well. Should you decide a secondary bank account is needed, do not hesitate to contact any of the Bank of Gielinor's stationary employees. We will be happy to help.")
            goto("main options")

            label("switch second bank")
            exec { player, _ ->
                toggleBankAccount(player)
                loadLabel(player, if (isUsingSecondaryBankAccount(player)) "now secondary" else "now primary")
            }
            label("now primary")
                npc("Your active bank account has been switched. You can now access your primary account.")
                goto("main options")
            label("now secondary")
                npc("Your active bank account has been switched. You can now access your secondary account.")
                goto("main options")

            label("pin")
            exec { player, _ -> openBankPinSettings(player) }
            goto("nowhere")

            label("collect")
            exec { player, _ -> openGrandExchangeCollectionBox(player) }
            goto("nowhere")

            label("what is this place")
            npc("This is a branch of the Bank of Gielinor. We have branches in many towns.")
            player("And what do you do?")
            npc("We will look after your items and money for you. Leave your valuables with us if you want to keep them safe.")
            goto("main options")

            label("jade's employment history")
            npc(ChatAnim.FRIENDLY, "Oh, ever since the Guild opened. I like it here.")
            player(ChatAnim.ASKING, "Why's that?")
            npc(ChatAnim.FRIENDLY, "Well... what with all these warriors around, there's not much chance of my bank being robbed, is there?")
            goto("nowhere")

            label("is the bounty hunter banker afraid")
            npc(ChatAnim.NEUTRAL, "While the Wilderness is quite a dangerous place, The Bank of Gielinor offers us - roving bankers - extraordinary benefits for our hard work in hazardous environments.")
            npc(ChatAnim.NEUTRAL, "This allows us to provide our services to customers regardless of their current whereabouts. Our desire to serve is stronger than our fear of the Wilderness.")
            goto("nowhere")
        }
    }

    override fun getIds(): IntArray = NPC_IDS
}
