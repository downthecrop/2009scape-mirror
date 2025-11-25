package core.game.dialogue

import core.api.Event
import core.api.InputType
import core.api.face
import core.api.openDialogue
import core.api.splitLines
import core.game.component.Component
import core.game.component.Component.setUnclosable
import core.game.event.DialogueCloseEvent
import core.game.event.EventHook
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item

/** Alias FacialExpression to ChatAnim for compatibility. */
typealias ChatAnim = FacialExpression
/** Alias InputType to InputType for compatibility. */
typealias InputType = InputType
/** Create container class DialogueOption for [DialogueLabeller.options] */
class DialogueOption(
        val goto: String, // Required: Label to go to if player selects option.
        val option: String, // Required: Printed text on the option list.
        val spokenText: String = option, // Optional: Option selected will be spoken text unless provided here.
        val expression: ChatAnim = ChatAnim.NEUTRAL, // Optional: Player expression to show.
        val skipPlayer: Boolean = false, // Optional: Skips the player spoken text (if wildly different)
        val optionIf: ((player: Player, npc: NPC) -> Boolean)? = null // Optional: Function to show/not show option.
)

/**
 * DialogueLabeller is another way to organize a dialogue file.
 * It shares the same dialogue layout as another project for portability.
 * - Uses [stage] is for the current loop and [super.stage] as the "next" stage
 * - Have [dialogueCounter] assign stages and guards for each option/player/npc dialogue
 * - Assign [exec] and [goto] the same [dialogueCounter] to execute all in one pass
 * - Save [label] to a HashMap to refer to when using [goto]
 * - Loops again when a [goto] is encountered, ends when no [stageHit] during a stage
 *
 * ! WARNING: DO NOT use functions in DialogueFile as it will cause unexpected behavior.
 *
 * ! HELP: There are snippets below that you can copy and modify to use as part of your code.
 */
abstract class DialogueLabeller : DialogueFile() {

    companion object {
        /**
         * Makes NPC stop in its tracks and look at player.
         * An alternative to setting up DialoguePlugin(player) to be used in InteractionListener.
         */
        fun open(player: Player, dialogue: Any, npc: NPC) {
            face(npc, player.location)
            npc.setDialoguePlayer(player) // This prevents random walking in [NPC.java handleTickActions()]
            npc.getWalkingQueue().reset()
            npc.getPulseManager().clear()
            openDialogue(player, dialogue, npc)
        }
    }

    /** Maps labels to stage numbers, to make jumps. */
    val labelStageMap = HashMap<String, Int> ()
    /** Assigns the number to each dialogue line. */
    var dialogueCounter = 0
    /** Set [stage] to start off (1 when there is an initial label). */
    var startingStage: Int? = null
    /** Keeps the current stage for the whole cycle. */
    override var stage = -1
    /** Assigns the number to each stage. */
    var stageHit = false
    /** Jump to next stage number when hitting a goto. */
    var jumpTo: Int? = null
    /** ButtonID on every click */
    var buttonID: Int? = null
    /** ButtonID when user clicks on an option */
    var optButton: Int? = null
    /** Input value after a user enters a value */
    var optInput: Any? = null

    /** Implement this function instead of overriding handle. */
    abstract fun addConversation()

    /** Helper functions to create an individual stage for each of the dialogue stages. */
    private fun assignIndividualStage(callback: () -> Component?, unclosable: Boolean) {
        if (startingStage == null) { startingStage = 0 }
        if (stage == dialogueCounter && jumpTo == null) { // Run this stage when the stage equals to the dialogueCounter of this dialogue
            val component = callback() // CALLBACK FUNCTION
            if (unclosable && component != null) {
                setUnclosable(player, component)
            }
            super.stage++ // Increment the stage to the next stage (only applies after a pass)
            stageHit = true // Flag that the stage was hit, so that it doesn't close the dialogue
        }
        dialogueCounter++ // Increment the dialogueCounter to assign each line of dialogue
    }

    /** Formats a vararg messages or falls back to message to an array for spread function. */
    private fun formatMessages(messages: Array<out String>?, message: String = ""): Array<out String> {
        return if (messages == null || messages.isEmpty()) {
            splitLines(message)
        } else if (messages.size > 1) {
            messages
        } else {
            splitLines(messages[0]) // A single line message is similar to a splitLine returning 1 line.
        }
    }

    /** Does absolutely nothing but to make it look like the other API. */
    fun assignToIds(npcid: Int) { /* super.npc = NPC(npcid) */ }

    /** Marks the start of a series of dialogue that can be jumped to using a [goto]. */
    fun label(label: String, nesting: () -> Unit = {}) {
        if (startingStage == null) { startingStage = 1 }
        dialogueCounter++
        labelStageMap[label] = dialogueCounter
        nesting()
    }

    /** Jumps to a [label] after a series of dialogue. */
    fun goto(label: String) {
        if (stage == dialogueCounter && jumpTo == null) {
            jumpTo = labelStageMap[label]
        }
    }

    /** Jumps to a [label] inside an [exec]. */
    fun loadLabel(player: Player, label: String) {
        goto(label)
    }

    /**
     * Executes the callback between stages and can be used for branching with [loadLabel].
     * You can chain as many exec as you like since they read sequentially.
     * You can also read [options] and [input] values here via [optButton] and [optInput].
     */
    fun exec(callback: (player: Player, npc: NPC) -> Unit) {
        if (startingStage == null) { startingStage = 0 }
        if (stage == dialogueCounter && jumpTo == null) {
            callback(player!!, npc!!)
        }
    }

    /** Manual stage. For custom creation of an individual stage. Must call interpreter in some form. **/
    fun manual(unclosable: Boolean = false, callback: (player: Player, npc: NPC) -> Component?) {
        assignIndividualStage({ return@assignIndividualStage callback(player!!, npc!!) }, unclosable)
    }

    /** Dialogue player/playerl. Shows player chathead with text. **/
    fun player(chatAnim: ChatAnim = ChatAnim.NEUTRAL, vararg messages: String, unclosable: Boolean = false) {
        assignIndividualStage({ return@assignIndividualStage interpreter!!.sendDialogues(player, chatAnim, *formatMessages(messages)) }, unclosable)
    }
    /** Dialogue player/playerl. Shows player chathead with text. **/
    fun player(vararg messages: String) { player(ChatAnim.NEUTRAL, *messages) }
    @Deprecated("Use player() instead.", ReplaceWith("player(chatAnim, *messages)"))
    fun playerl(chatAnim: ChatAnim = ChatAnim.NEUTRAL, vararg messages: String) { throw Exception("Deprecated DialogueLabel: Use player() instead.") }
    @Deprecated("Use player() instead.", ReplaceWith("player(*messages)"))
    fun playerl(vararg messages: String) { throw Exception("Deprecated DialogueLabel: Use player() instead.") }

    /** Dialogue npc/npcl. Shows npc chathead with text. **/
    fun npc(chatAnim: ChatAnim = ChatAnim.NEUTRAL, vararg messages: String, unclosable: Boolean = false) {
        val callback = callback@{ return@callback interpreter!!.sendDialogues(npc, chatAnim, *formatMessages(messages)) }
        assignIndividualStage(callback, unclosable)
    }
    /** Dialogue npc/npcl. Shows npcId chathead with text. **/
    fun npc(chatAnim: ChatAnim = ChatAnim.NEUTRAL, npcId: Int = npc!!.id, vararg messages: String, unclosable: Boolean = false) {
        assignIndividualStage({ return@assignIndividualStage interpreter!!.sendDialogues(NPC(npcId), chatAnim, *formatMessages(messages)) }, unclosable)
    }
    /** Dialogue npc/npcl. Shows npcId chathead with text. **/
    fun npc(npcId: Int = npc!!.id, vararg messages: String, unclosable: Boolean = false) {
        assignIndividualStage({ return@assignIndividualStage interpreter!!.sendDialogues(NPC(npcId), ChatAnim.NEUTRAL, *formatMessages(messages)) }, unclosable)
    }
    /** Dialogue npc/npcl. Shows npc chathead with text. **/
    fun npc(vararg messages: String) { npc(ChatAnim.NEUTRAL, *messages) }
    @Deprecated("Use npc() instead.", ReplaceWith("npc(chatAnim, *messages)"))
    fun npcl(chatAnim: ChatAnim = ChatAnim.NEUTRAL, vararg messages: String) { throw Exception("Deprecated DialogueLabel: Use npc() instead.") }
    @Deprecated("Use npc() instead.", ReplaceWith("npc(*messages)"))
    fun npcl(vararg messages: String) { throw Exception("Deprecated DialogueLabel: Use npc() instead.") }

    /** Dialogue item/iteml. Shows item with text. **/
    fun item(item: Item, vararg messages: String, message: String = "", unclosable: Boolean = false) {
        val callback = callback@{ return@callback interpreter!!.sendItemMessage(item, *formatMessages(messages, message)) }
        assignIndividualStage(callback, unclosable)
    }
    @Deprecated("Use item() instead.", ReplaceWith("item(item, *messages)"))
    fun iteml(item: Item, vararg messages: String) { throw Exception("Deprecated DialogueLabel: Use item() instead.") }

    /** Dialogue overloaded doubleItem/doubleIteml. Shows two items with text. **/
    fun item(item: Item, item2: Item, vararg messages: String, message: String = "", unclosable: Boolean = false) {
        assignIndividualStage({ return@assignIndividualStage interpreter!!.sendDoubleItemMessage(item, item2, *formatMessages(messages, message)) }, unclosable)
    }

    /** Dialogue line/linel. Simply shows text. **/
    fun line(vararg messages: String, unclosable: Boolean = false) {
        val callback = callback@{ return@callback interpreter!!.sendDialogue(*messages) }
        assignIndividualStage(callback, unclosable)
    }
    @Deprecated("Use line() instead.", ReplaceWith("line(*messages)"))
    fun linel(vararg messages: String) { throw Exception("Deprecated DialogueLabel: Use line() instead.") }

    /** Dialogue option. Shows the option dialogue with choices for the user to select. **/
    fun options(vararg options: DialogueOption, title: String = "Select an Option", unclosable: Boolean = false) {
        // Filter out options that aren't shown.
        val filteredOptions = options.filter{ if (it.optionIf != null) { it.optionIf.invoke(player!!, npc!!) } else { true } }
        // Stage Part 1: Options List Dialogue
        val callback = callback@{ return@callback interpreter!!.sendOptions(title, *filteredOptions.map{ it.option }.toTypedArray()) }
        assignIndividualStage(callback, unclosable)
        // Stage Part 2: Show spoken text.
        val opt = if (buttonID != null && buttonID in 1..filteredOptions.size) { filteredOptions[buttonID!! - 1] } else { null }
        assignIndividualStage({
            var component: Component? = null
            if (opt?.skipPlayer == true) {
                jumpTo = stage + 1
            } else {
                component = interpreter!!.sendDialogues(player, opt?.expression ?: ChatAnim.NEUTRAL, *(splitLines(opt?.spokenText ?: " ")))
            }
            optButton = buttonID // transfer the buttonID to a temp memory for the next stage
            return@assignIndividualStage component
        }, unclosable)
        // Stage Part 3: Jump To goto
        if (stage == dialogueCounter && optButton != null && optButton in 1..filteredOptions.size) {
            jumpTo = labelStageMap[filteredOptions[optButton!! - 1].goto]
        }
        dialogueCounter++
    }
    @Deprecated("Use options(DialogueOption()) and not options(string).", ReplaceWith("options(DialogueOption(options))"))
    override fun options(vararg options: String?, title: String) { throw Exception("Deprecated DialogueLabel: Use options(DialogueOption()) and not options(string).") }

    /** Dialogue input. Shows the input dialogue with an input box for the user to type in. Read [optInput] for the value. **/
    fun input(type: InputType, prompt: String = "Enter the amount") {
        assignIndividualStage({
            // These are similar to calling sendInputDialogue
            when (type) {
                InputType.AMOUNT -> interpreter!!.sendInput(true, prompt)
                InputType.NUMERIC -> interpreter!!.sendInput(false, prompt)
                InputType.STRING_SHORT -> interpreter!!.sendInput(true, prompt) // Only 12 letters
                InputType.STRING_LONG -> interpreter!!.sendLongInput(prompt) // Very long text, can overflow.
                InputType.MESSAGE -> interpreter!!.sendMessageInput(prompt)
            }
            if (type == InputType.AMOUNT) {
                player!!.setAttribute("parseamount", true)
            }
            // This runscript is the same runscript as the one in ContentAPI sendInputDialogue
            player!!.setAttribute("runscript") { value: Any ->
                optInput = value
                // The next line is a hack. Because this prompt overlays the actual chat box, we trigger the next dialogue with a call to handle.
                interpreter!!.handle(player!!.interfaceManager.chatbox.id, 2)
            }
            player!!.setAttribute("input-type", type)
            return@assignIndividualStage null
        }, false)
    }
    /** Dialogue input. Shows the input dialogue with an input box for the user to type in. Read [optInput] in an [exec] function for the value. **/
    fun input(numeric: Boolean, prompt: String = "Enter the amount") { input( if (numeric) { InputType.NUMERIC } else { InputType.STRING_SHORT }, prompt) }

    /** Runs arbitrary code when the dialogue closes, once. **/
    fun afterClose(callback: (player: Player) -> Unit) {
        val hook = object : EventHook<DialogueCloseEvent> {
            override fun process(entity: Entity, event: DialogueCloseEvent) {
                val you = entity as Player
                you.unhook(this)
                callback(you)
            }
        }
        player!!.hook(Event.DialogueClosed, hook)
    }

    /** Calls another dialogue file. Always use this to open another dialogue file instead of calling openDialogue() in exec{} due to interfaces clashing. **/
    fun open(player: Player, dialogue: Any, vararg args: Any) {
        val callback = callback@{
            core.api.openDialogue(player, dialogue, *args)
            return@callback null
        }
        assignIndividualStage(callback, false)
    }

    /** WARNING: DIALOGUE LABELLER WILL BREAK IN CERTAIN FUNCTIONS. USE open() instead. */
    fun openDialogue(player: Player, dialogue: Any, vararg args: Any) {
        core.api.openDialogue(player, dialogue, *args)
    }

    /** Hook onto the handle function of DialogueFile. This function gets called every loop with a super.stage. */
    override fun handle(componentID: Int, buttonID: Int) {
        this.buttonID = buttonID
        startingStage = null
        /** This -1 stage is to read labels into a hashmap and to find the starting stage. */
        if (stage == -1) {
            dialogueCounter = 0
            addConversation() // Force all labels to be recorded into hashmap.
            super.stage = startingStage ?: 0
        }
        for (i in 0..10) { // Limit to 10 jumpTo PER DIALOGUE LINE to prevent infinite looping/ping-ponging jumps.
            if (jumpTo != null) { // If jumpTo is set, set the super.stage stage as the new jumpTo stage.
                super.stage = jumpTo as Int
                jumpTo = null
            }
            stageHit = false
            stage = super.stage // [stage] is for the current loop. [super.stage] is treated as the "next" stage.
            dialogueCounter = 0
            addConversation() // MAIN CALLBACK FUNCTION
            // If there is no jumpTo set, or jumpTo is set to the same stage as this(infinite loop), exit.
            if (jumpTo == null || jumpTo == stage) {
                break
            }
        }
        // If a dialogue stage is not hit, end the dialogue.
        if (!stageHit) {
            end()
        }
    }
}

/*
// COPY PASTA SNIPPETS SECTION FOR QUICK BOILERPLATES

// STANDARD: Initializing with DialoguePlugin.
@Initializable
class DonieDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player): DialoguePlugin {
        return DonieDialogue(player)
    }
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, DonieDialogueFile(), npc)
        return false
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DONIE_2238)
    }
}
class DonieDialogueFile : DialogueLabeller() {
    override fun addConversation() {
        assignToIds(NPCs.DONIE_2238)

        npc(ChatAnim.FRIENDLY, "Hello there, can I help you?")
        goto("nowhere")
    }
}

// NEW! (EXPERIMENTAL): Initializing with InteractionListener.
class SomeDudeDialogue : InteractionListener {
    override fun defineListeners() {
        on(NPCs.PRIEST_OF_GUTHIX_8555, IntType.NPC, "talk-to") { player, node ->
            DialogueLabeller.open(player, SomeDudeDialogueLabellerFile(), node as NPC)
            return@on true
        }
    }
}

// Exec with quest stage branches.
exec { player, npc ->
    when(getQuestStage(player, SomeQuest.questName)) {
        100 -> loadLabel(player, "SomeQuestStage100")
        10, 20 -> loadLabel(player, "SomeQuestStage10")
        else -> {
            loadLabel(player, "SomeQuestStage0")
        }
    }
}

// Exec to move quest stage up.
exec { player, npc ->
    if (getQuestStage(player, SomeQuest.questName) == 0) {
        setQuestStage(player, SomeQuest.questName, 10)
    }
}

// Exec to add item, set attribute.
exec { player, npc ->
    if (removeItem(player, Items.ITEM_1)) {
        addItemOrDrop(player, Items.ITEM_2)
    }
    if (getAttribute(player, attributeSomething, null) == null) {
        setAttribute(player, attributeSomething, player.location)
    }
}

// Open to another dialogue file.
npc("I'm going to another file.")
open(player!!, SomeDialogueFile2(), npc!!)
...
class SomeDialogueFile2 : DialogueLabeller() {
    override fun addConversation() {
        npc("This is another file.")
    }
}

// Options with all the different controls.
options(
        DialogueOption("Label1", "Line 1 Say", expression=ChatAnim.THINKING),
        DialogueOption("Label2", "Line 2 Huh", skipPlayer=true),
        DialogueOption("Label3", "Line 3 Blah", spokenText="I say something else", expression=ChatAnim.FRIENDLY),
        DialogueOption("Label4", "Line 4 What") { player, npc ->
            return@DialogueOption false // Don't show
        }
)

*/