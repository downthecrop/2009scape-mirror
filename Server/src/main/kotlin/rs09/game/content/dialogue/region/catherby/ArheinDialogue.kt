package rs09.game.content.dialogue.region.catherby
import api.*
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.StringUtils
import org.rs09.consts.NPCs
import rs09.ServerStore
import rs09.game.content.dialogue.Topic
import rs09.tools.END_DIALOGUE
import org.rs09.consts.Items
import rs09.game.content.quest.members.merlinsquest.ArheinMCDialogue


/**
 * @author lila
 */

/**
 * Arhein dialogue
 * This class handles all dialogue from the Catherby dock NPC Arhein.
 *
 * Arhein is relevant to:
 * Merlin's Crystal quest
 * crafting/glassmaking - via selling seaweed
 * farming/supercompost - via selling pineapples
 */

@Initializable
class ArheinDialogue(player: Player? = null) : DialoguePlugin(player) {

    val limits = mapOf(
        Items.PINEAPPLE_2114 to 40,
        Items.SEAWEED_401 to 80
    )
    val period = "daily"

    // this function handles Arhein's specialty stock (pineapple and seaweed) transactions.
    fun getGoods(requestedItem: Int, requestedAmount: Int): Int {
        val price = 2
        val afford = player.getInventory().getAmount(Items.COINS_995) / price
        var realamount = minOf(requestedAmount, afford)
        val exactafford = (realamount == afford) && (afford == freeSlots(player) + 1)
        realamount = minOf(realamount,if(exactafford) realamount else freeSlots(player))
        realamount = ServerStore.getNPCItemAmount(NPCs.ARHEIN_563, requestedItem, limits.getOrDefault(requestedItem,0), player, realamount, period)
        if (removeItem(player, Item(Items.COINS_995, realamount * price), Container.INVENTORY)) {
            if (addItem(player, requestedItem, realamount, Container.INVENTORY)) {
                ServerStore.addNPCItemAmount(NPCs.ARHEIN_563, requestedItem, limits.getOrDefault(requestedItem,0), player, realamount, period)
                return realamount
            }
        }
        return 0
    }
    // this function handles selecting which of the specialty stock items (pineapple or seaweed) to use.
    fun selectGoods(requestedItem: Int) {
        this.goods = requestedItem
        this.stock = ServerStore.getNPCItemStock(NPCs.ARHEIN_563,this.goods,limits.getOrDefault(this.goods,0),player,period)
    }

    // some class variables that handle the special stock (pineapples and seaweed)
    private var goods = Items.PINEAPPLE_2114
    private var goodsMessage = ""
    private var goodsName = ""
    private var stock = 0

    /** Arhein Dialogue Opener
     * If the opener is called with nontrivial arguments, then this indicates that the player tried to board the ship without permission.
     * In that case, Arhein tells the player to bugger off.
     * Otherwise, start the normal dialogue.
     * Before all this, we check and initialize some important information for Arhein's specialty stock.
     */
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC

        if (args.size > 1) {
            npcl(FacialExpression.ANGRY, "Hey buddy! Get away from my ship alright?")
            stage = 701
        } else {
            npcl(FacialExpression.HAPPY, "Hello! Would you like to trade? I've a variety of wares for sale!")
            stage = 1
        }
        return true
    }

    /** Arhein Dialogue Handler
     * Handler for the dialogue. the handler determines dialogue content and flow,
     * as well as some specialty operations to handle special things like quest progress and specialty stock.
     */
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage) {
            1 -> showTopics(
                Topic("Let's Trade.", 7),
                Topic("I hear that you sell pineapples.",800),
                Topic("I hear that you sell seaweed.",900),
                Topic("No thank you.", END_DIALOGUE),
                Topic("Is that your ship?", 100)
            )
            7  -> npc.openShop(player).also { stage = END_DIALOGUE }
            100 -> npcl(FacialExpression.NEUTRAL, "Yes, I use it to make deliveries to my customers up and down the coast. These crates here are all ready for my next trip.").also { stage++ }
            101 -> showTopics(
                Topic("Where do you deliver to?",120),
                Topic("Are you rich then?", 110),
                Topic("Do you deliver to the fort just down the coast?",500)
            )
            110 -> npcl(FacialExpression.NEUTRAL, "Business is going reasonably well... I wouldn't say I was the richest of merchants ever, but I'm doing fairly well all things considered.").also { stage = END_DIALOGUE }
            120 -> npcl(FacialExpression.NEUTRAL, "Various places up and down the coast. Mostly Karamja and Port Sarim.").also { stage++ }
            121 -> showTopics(
                Topic("I don't suppose I could get a lift anywhere?",140),
                Topic("Well, good luck with your business.",130)
            )
            130 -> npcl(FacialExpression.HAPPY,"Thanks buddy!").also { stage = END_DIALOGUE }
            140 -> npcl(FacialExpression.GUILTY,"Sorry pal, but I'm afraid I'm not quite ready to sail yet.").also { stage++ }
            141 -> npcl(FacialExpression.NEUTRAL,"I'm waiting on a big delivery of candles which I need to deliver further along the coast.").also { stage = END_DIALOGUE }
            500 -> npcl(FacialExpression.HALF_THINKING, "Yes, I do have orders to deliver there from time to time. I think I may have some bits and pieces for them when I leave here next actually.").also {
                val queststage = player.questRepository.getStage("Merlin's Crystal")
                if(queststage == 30 || queststage == 40) {
                    loadFile(ArheinMCDialogue(queststage))
                } else {
                    stage = END_DIALOGUE
                }
            }
            701 -> playerl(FacialExpression.GUILTY,"Yeah... uh... sorry...").also{ stage = END_DIALOGUE }
            800 -> {
                // Pineapples
                selectGoods(Items.PINEAPPLE_2114)
                // and now the conditional block to handle cases
                if(stock==0) {
                    npcl(FacialExpression.SAD,"Actually, I've run out. Come back tomorrow and I should have some more.")
                    stage = END_DIALOGUE
                } else {
                    // approximately authentic dialogue prompt for buying pineapples from arhein. (closest known dialogues do not make grammatical sense when spliced.)
                    this.goodsMessage = "I certainly do! I've got ${stock} in stock, going for 2 coins each. How many would you like?"
                    npcl(FacialExpression.HAPPY,this.goodsMessage)
                    this.goodsName = if(stock==1) getItemName(this.goods) else StringUtils.plusS(getItemName(this.goods))
                    stage = 1200
                }
            }
            900 -> {
                // Seaweed
                selectGoods(Items.SEAWEED_401)
                // and now the conditional block to handle cases
                if(stock==0) {
                    npcl(FacialExpression.SAD,"Actually, I've run out. Come back tomorrow and I should have some more.")
                    stage = END_DIALOGUE
                } else {
                    // approximately authentic dialogue prompt for buying seaweed from arhein. (closest known dialogues do not make grammatical sense when spliced.)
                    this.goodsMessage = "I certainly do! I've ${stock} at the moment and they cost 2 coins each. How many would you like?"
                    npcl(FacialExpression.HAPPY,this.goodsMessage)
                    this.goodsName = getItemName(this.goods)
                    stage = 1200
                }
            }
            1200 -> {
                // arhein specialty items sales
                val goodsPrompt = "Arhein has ${this.stock} ${this.goodsName}. How many would you like to buy?"
                sendInputDialogue(player,InputType.AMOUNT, goodsPrompt) { value ->
                    val amountReceived = getGoods(this.goods,Integer.parseInt(value.toString()))
                    var exitMsg = ""
                    if(amountReceived==this.stock) {
                        exitMsg = "Here you go! I've run out for now. Come again tomorrow and I should have more."
                    } else if (amountReceived>0) {
                        // inauthentic exit dialogue for arhein's pineapple/seaweed sales. documented dialogue that i found indicates that there is no exit message.
                        // i kept the spirit of arhein's personality, using "buddy!" that is known from another line.
                        exitMsg = "Here you go, buddy!"
                    } else {
                        // same deal here, inauthentic exit dialogue based on arhein's authentic personality.
                        exitMsg = "Take care, buddy!"
                    }
                    npcl(FacialExpression.HAPPY,exitMsg)
                }
                stage = END_DIALOGUE
            }
        }
        return true
    }


    // finale funz - newInstance, getIds
    override fun newInstance(player: Player?): DialoguePlugin {
        return ArheinDialogue(player)
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ARHEIN_563)
    }
}

