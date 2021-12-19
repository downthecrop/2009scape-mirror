package core.game.content.dialogue;

import core.game.component.Component;
import core.game.node.entity.Entity;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Plugin;
import core.plugin.PluginManifest;
import core.plugin.PluginType;
import rs09.game.content.dialogue.DialogueFile;

import java.util.ArrayList;

import static api.DialUtilsKt.splitLines;
import static rs09.tools.DialogueConstKt.DIALOGUE_INITIAL_OPTIONS_HANDLE;
import static rs09.tools.DialogueConstKt.START_DIALOGUE;

/**
 * Represents a dialogue plugin.
 * @author Emperor
 */
@PluginManifest(type = PluginType.DIALOGUE)
public abstract class DialoguePlugin implements Plugin<Player> {

	/**
	 * Represents the red string.
	 */
	protected static final String RED = "<col=8A0808>";

	/**
	 * Represents the blue string.
	 */
	protected static final String BLUE = "<col=08088A>";

	/**
	 * The player.
	 */
	protected Player player;

	/**
	 * The dialogue interpreter.
	 */
	protected DialogueInterpreter interpreter;

	public DialogueFile file;
	
	protected ArrayList<String> optionNames = new ArrayList<String>(10);
	protected ArrayList<DialogueFile> optionFiles = new ArrayList<DialogueFile>(10);

	/**
	 * Two options interface.
	 */
	protected final int TWO_OPTIONS = 228;

	/**
	 * Three options interface.
	 */
	protected final int THREE_OPTIONS = 230;

	/**
	 * Four options interface.
	 */
	protected final int FOUR_OPTIONS = 232;

	/**
	 * Five options interface.
	 */
	protected final int FIVE_OPTIONS = 234;

	/**
	 * The NPC the player is talking with.
	 */
	protected NPC npc;

	/**
	 * The current dialogue stage.
	 */
	public int stage;

	/**
	 * If the dialogue is finished.
	 */
	protected boolean finished;

	/**
	 * Constructs a new {@code DialoguePlugin} {@code Object}.
	 */
	public DialoguePlugin() {
		/*
		 * empty.
		 */
	}
	
	public String pirateGender() {
		return (player.isMale() ? "lad" : "lass");
		
	}

	/**
	 * Constructs a new {@code DialoguePlugin} {@code Object}.
	 * @param player The player.
	 */
	public DialoguePlugin(Player player) {
		this.player = player;
		if (player != null) {
			this.interpreter = player.getDialogueInterpreter();
		}
	}

	/**
	 * Initializes this plugin.
	 */
	public void init() {
		for (int id : getIds()) {
			DialogueInterpreter.add(id, this);
		}
	}

	/**
	 * Closes <b>(but does not end)</b> the dialogue.
	 * @return {@code True} if the dialogue succesfully closed.
	 */
	public boolean close() {
		player.getInterfaceManager().closeChatbox();
		player.getInterfaceManager().openChatbox(137);
		if(file != null) file.end();
		finished = true;
		return true;
	}

	public void sendNormalDialogue(Entity entity, FacialExpression expression, String... messages) {
		interpreter.sendDialogues(entity, expression, messages);
	}

	/**
	 * Increments the stage variable.
	 */
	public void increment() {
		stage++;
	}


	/**
	 * Increments the stage variable.
	 * @return The stage variable.
	 */
	public int getAndIncrement() {
		return stage++;
	}

	/**
	 * Ends the dialogue.
	 */
	public void end() {
		if (interpreter != null) {
			interpreter.close();
		}
	}

	public void finish() {
		setStage(-1);
	}

	@Override
	public Object fireEvent(String identifier, Object... args) {
		return null;
	}

	@Override
	public abstract DialoguePlugin newInstance(Player player);

	/**
	 * Opens the dialogue.
	 * @param args The arguments.
	 * @return {@code True} if the dialogue plugin succesfully opened.
	 */
	public abstract boolean open(Object... args);

	/**
	 * Handles the progress of this dialogue..
	 * @return {@code True} if the dialogue has started.
	 */
	public abstract boolean handle(int interfaceId, int buttonId);

	/**
	 * Gets the ids of the NPCs using this dialogue plugin.
	 * @return The array of NPC ids.
	 */
	public abstract int[] getIds();
	
	/**
	 * Method wrapper to send an npc dial.
	 * @return the component.
	 */
	public Component npc(final String... messages) {
		if (npc == null) {
			return interpreter.sendDialogues(getIds()[0], getIds()[0] > 8591 ? FacialExpression.OLD_NORMAL : FacialExpression.FRIENDLY, messages);
		}
		return interpreter.sendDialogues(npc, npc.getId() > 8591 ? FacialExpression.OLD_NORMAL : FacialExpression.FRIENDLY, messages);
	}

	/**
	 * Method wrapper to send an npc dial.
	 * @param id the id.
	 * @return the component.
	 */
	public Component npc(int id, final String... messages) {
		return interpreter.sendDialogues(id, FacialExpression.FRIENDLY, messages);
	}

	public Component sendDialogue(String... messages) {
		return interpreter.sendDialogue(messages);
	}
	
	/**
	 * Method wrapper to send an npc dial.
	 * @return the component.
	 */
	public Component npc(FacialExpression expression, final String... messages) {
		if (npc == null) {
			return interpreter.sendDialogues(getIds()[0], expression, messages);
		}
		return interpreter.sendDialogues(npc, expression, messages);
	}

	/**
	 * Method wrapper to send a player dial.
	 * @return the component.
	 */
	public Component player(final String... messages) {
		return interpreter.sendDialogues(player, null, messages);
	}
	/**
	 * Method wrapper to send a player dial.
	 * @return the component.
	 */
	public Component player(FacialExpression expression, final String... messages) {
		return interpreter.sendDialogues(player, expression, messages);
	}

	/**
	 * Method used to send options.
	 * @param options the options.
	 */
	public void options(final String... options) {
		interpreter.sendOptions("Select an Option", options);
	}

	/**
	 * Checks if the dialogue plugin is finished.
	 * @return {@code True} if so.
	 */
	public boolean isFinished() {
		return finished;
	}

	/**
	 * Gets the player.
	 * @return The player.
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Sets the stage.
	 * @param i the stage.
	 */
	public void setStage(int i) {
		this.stage = i;
	}

	public void next() {
		this.stage += 1;
	}

	/**
	 * Loads a DialogueFile and sets its stage to START_DIALOGUE, and diverts all further handling for the conversation to the file.
	 * @param file the DialogueFile to load.
	 */
	public void loadFile(DialogueFile file){
		if(file == null) return;
		this.file = file.load(player,npc,interpreter);
		this.file.setDialoguePlugin(this);
		stage = START_DIALOGUE;
	}

	/**
	 * Add an option to the list of possible choices a player can pick from. Helps build the options interface for sendChoices()
	 * @param name the name of the quest/activity to talk about. Turns into "Talk about $name" on the option interface.
	 * @param file the DialogueFile that the option loads when selected.
	 */
	public void addOption(String name, DialogueFile file){
		optionNames.add("Talk about " + name);
		optionFiles.add(file);
	}

	/**
	 * Send the player a list of conversation options if there's more than one choice. I.E. multiple quest lines.
	 * @return true if an options interface was sent, false if not.
	 */
	public boolean sendChoices(){
		if(optionNames.size() == 1){
			loadFile(optionFiles.get(0));
			return false;
		} else if(optionNames.isEmpty()) {
			stage = START_DIALOGUE;
			return false;
		} else {
			options(optionNames.toArray(new String[0]));
			stage = DIALOGUE_INITIAL_OPTIONS_HANDLE;
			return true;
		}
	}

	/**
	 * Use the automatic linesplitting feature in DialUtils to produce npc dialogues
	 * @param expr the FacialExpression to use, located in the FacialExpression enum.
	 * @param msg the message for the NPC to say
	 */
	public Component npcl(FacialExpression expr, String msg){
		return npc(expr, splitLines(msg));
	}

	/**
	 * Use the automatic linesplitting feature in DialUtils to produce player dialogues
	 * @param expr the FacialExpression to use, located in the FacialExpression enum.
	 * @param msg the message for the player to say
	 */
	public Component playerl(FacialExpression expr, String msg){
		return player(expr, splitLines(msg));
	}

}