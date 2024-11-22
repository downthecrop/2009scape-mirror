package core.game.dialogue

import core.api.InputType
import core.api.face
import core.api.splitLines
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item

/** Alias FacialExpression to ChatAnim for compatibility. */
typealias ChatAnim = FacialExpression
/** Alias InputType to InputType for compatibility. */
typealias InputType = InputType
/** Create container class DialogueOption for [DialogueLabeller.options] */
class DialogueOption(
        val goto: String,
        val option: String,
        val spokenText: String = option,
        val expression: ChatAnim = ChatAnim.NEUTRAL,
        val skipPlayer: Boolean = false,
        val callback: ((player: Player, npc: NPC) -> Boolean)? = null
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
 * Example: [content.region.misthalin.lumbridge.dialogue.RangedTutorDialogue]
 */
abstract class DialogueLabeller : DialogueFile() {

    companion object {
        /**
         * Makes NPC stop in its tracks and look at player.
         * An alternative to setting up DialoguePlugin(player) to be used in InteractionListener.
         */
        fun captureNPC(player: Player, npc: NPC) {
            face(npc, player.location)
            npc.setDialoguePlayer(player) // This prevents random walking in [NPC.java handleTickActions()]
            npc.getWalkingQueue().reset()
            npc.getPulseManager().clear()
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

    /** Helper function to create an individual stage for each of the dialogue stages. */
    private fun assignIndividualStage(callback: () -> Unit) {
        if (startingStage == null) { startingStage = 0 }
        if (stage == dialogueCounter) { // Run this stage when the stage equals to the dialogueCounter of this dialogue
            callback() // CALLBACK FUNCTION
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
    fun label(label: String) {
        if (startingStage == null) { startingStage = 1 }
        dialogueCounter++
        labelStageMap[label] = dialogueCounter
    }

    /** Jumps to a [label] after a series of dialogue. */
    fun goto(label: String) {
        if (stage == dialogueCounter) {
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
        if (stage == dialogueCounter) {
            callback(player!!, npc!!)
        }
    }

    /** Dialogue player/playerl. Shows player chathead with text. **/
    fun player(chatAnim: ChatAnim = ChatAnim.NEUTRAL, vararg messages: String) {
        assignIndividualStage { interpreter!!.sendDialogues(player, chatAnim, *formatMessages(messages)) }
    }
    /** Dialogue player/playerl. Shows player chathead with text. **/
    fun player(vararg messages: String) { player(ChatAnim.NEUTRAL, *messages) }
    @Deprecated("Use player() instead.", ReplaceWith("player(chatAnim, *messages)"))
    fun playerl(chatAnim: ChatAnim = ChatAnim.NEUTRAL, vararg messages: String) { throw Exception("Deprecated DialogueLabel: Use player() instead.") }
    @Deprecated("Use player() instead.", ReplaceWith("player(*messages)"))
    fun playerl(vararg messages: String) { throw Exception("Deprecated DialogueLabel: Use player() instead.") }

    /** Dialogue npc/npcl. Shows npc chathead with text. **/
    fun npc(chatAnim: ChatAnim = ChatAnim.NEUTRAL, vararg messages: String) {
        assignIndividualStage { interpreter!!.sendDialogues(npc, chatAnim, *formatMessages(messages)) }
    }
    /** Dialogue npc/npcl. Shows npcId chathead with text. **/
    fun npc(chatAnim: ChatAnim = ChatAnim.NEUTRAL, npcId: Int, vararg messages: String) {
        assignIndividualStage { interpreter!!.sendDialogues(NPC(npcId), chatAnim, *formatMessages(messages)) }
    }
    /** Dialogue npc/npcl. Shows npc chathead with text. **/
    fun npc(vararg messages: String) { npc(ChatAnim.NEUTRAL, *messages) }
    @Deprecated("Use npc() instead.", ReplaceWith("npc(chatAnim, *messages)"))
    fun npcl(chatAnim: ChatAnim = ChatAnim.NEUTRAL, vararg messages: String) { throw Exception("Deprecated DialogueLabel: Use npc() instead.") }
    @Deprecated("Use npc() instead.", ReplaceWith("npc(*messages)"))
    fun npcl(vararg messages: String) { throw Exception("Deprecated DialogueLabel: Use npc() instead.") }

    /** Dialogue item/iteml. Shows item with text. **/
    fun item(item: Item, vararg messages: String, message: String = "") {
        assignIndividualStage { interpreter!!.sendItemMessage(item, *formatMessages(messages, message)) }
    }
    @Deprecated("Use item() instead.", ReplaceWith("item(item, *messages)"))
    fun iteml(item: Item, vararg messages: String) { throw Exception("Deprecated DialogueLabel: Use item() instead.") }

    /** Dialogue overloaded doubleItem/doubleIteml. Shows two items with text. **/
    fun item(item: Item, item2: Item, vararg messages: String, message: String = "") {
        assignIndividualStage { interpreter!!.sendDoubleItemMessage(item, item2, formatMessages(messages, message).joinToString(" ")) }
    }

    /** Dialogue line/linel. Simply shows text. **/
    fun line(vararg messages: String) {
        assignIndividualStage { interpreter!!.sendDialogue(*messages) }
    }
    @Deprecated("Use line() instead.", ReplaceWith("line(*messages)"))
    fun linel(vararg messages: String) { throw Exception("Deprecated DialogueLabel: Use line() instead.") }

    /** Dialogue option. Shows the option dialogue with choices for the user to select. **/
    fun options(vararg options: DialogueOption, title: String = "Select an Option") {
        // Filter out options that aren't shown.
        val filteredOptions = options.filter{ if (it.callback != null) { it.callback.invoke(player!!, npc!!) } else { true } }
        // Stage Part 1: Options List Dialogue
        assignIndividualStage { interpreter!!.sendOptions(title, *filteredOptions.map{ it.option }.toTypedArray()) }
        // Stage Part 2: Show spoken text.
        var opt = if (buttonID != null && buttonID in 1..filteredOptions.size) { filteredOptions[buttonID!! - 1] } else { null }
        assignIndividualStage {
            if (opt?.skipPlayer == true) {
                jumpTo = stage + 1
            } else {
                interpreter!!.sendDialogues(player, opt?.expression ?: ChatAnim.NEUTRAL, *(splitLines(opt?.spokenText ?: " ")))
            }
            optButton = buttonID // transfer the buttonID to a temp memory for the next stage
        }
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
        assignIndividualStage {
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
            player!!.setAttribute("runscript") { value: Any ->
                optInput = value
                // The next line is a hack. Because this prompt is overlays the actual chatbox, we trigger the next dialogue with a call to handle.
                interpreter!!.handle(player!!.interfaceManager.chatbox.id, 2)
            }
            player!!.setAttribute("input-type", type)
        }
    }
    /** Dialogue input. Shows the input dialogue with an input box for the user to type in. Read [optInput] in an [exec] function for the value. **/
    fun input(numeric: Boolean, prompt: String = "Enter the amount") { input( if (numeric) { InputType.NUMERIC } else { InputType.STRING_SHORT }, prompt) }

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
        if (!stageHit) { end() } // If a dialogue stage is not hit, end the dialogues.
    }
}