package core.game.dialogue;

import core.game.event.DialogueCloseEvent;
import core.game.event.DialogueOpenEvent;
import core.game.event.DialogueOptionSelectionEvent;
import core.cache.def.impl.ItemDefinition;
import core.cache.def.impl.NPCDefinition;
import core.game.component.Component;
import core.game.node.entity.Entity;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.world.GameWorld;
import core.net.packet.PacketRepository;
import core.net.packet.context.ChildPositionContext;
import core.net.packet.context.ContainerContext;
import core.net.packet.out.ContainerPacket;
import core.net.packet.out.RepositionChild;
import core.plugin.PluginManifest;
import core.plugin.PluginType;
import core.game.system.config.ItemConfigParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static core.tools.DialogueConstKt.END_DIALOGUE;


/**
 * Handles the dialogues.
 * @author Emperor
 */
@PluginManifest(type = PluginType.DIALOGUE)
public final class DialogueInterpreter {
    public ArrayList<Topic<?>> activeTopics = new ArrayList<>();

    /**
     * The dialogue plugins.
     */
    private static final Map<Integer, DialoguePlugin> PLUGINS = new HashMap<>();

    /**
     * a List of dialogue actions.
     */
    private final List<DialogueAction> actions = new ArrayList<>(10);

    /**
     * The currently opened dialogue.
     */
    private DialoguePlugin dialogue;

    /**
     * The current dialogue key.
     */
    private int key;

    /**
     * The player.
     */
    private final Player player;

    /**
     * Constructs a new {@code DialogueInterpreter} {@code Object}.
     * @param player The player.
     */
    public DialogueInterpreter(Player player) {
        this.player = player;
    }

    /**
     * @param dialogue the dialogue to set.
     */
    public void setDialogue(DialoguePlugin dialogue) {
        this.dialogue = dialogue;
    }

    /**
     * Opens the dialogue for the given dialogue type.
     * @param dialogueType The dialogue type.
     * @param args the args.
     * @return {@code True} if successful.
     */
    public boolean open(String dialogueType, Object... args) {
        return open(getDialogueKey(dialogueType), args);
    }

    /**
     * Opens the dialogue for the given NPC id.
     * @param dialogueKey The dialogue key (usually NPC id).
     * @param args The arguments.
     * @return {@code True} if successful.
     */
    public boolean open(int dialogueKey, Object... args) {
        key = dialogueKey;
        if (args.length > 0 && args[0] instanceof NPC) {
            NPC npc = (NPC) args[0];
            npc.setDialoguePlayer(player);
            try { //this prevents the dialogue interpreter from dying when you try opening an NPC's dialogue without passing the NPC itself as an argument
                npc.getWalkingQueue().reset();
                npc.getPulseManager().clear();
            } catch(Exception e){
                e.printStackTrace();
            }
        } else if (args.length < 1) {
            args = new Object[] { dialogueKey };
        }
        DialoguePlugin plugin = PLUGINS.get(dialogueKey);
        if (plugin == null) {
            return false;
        }
        if (player.isDebug()) {
            player.sendMessage("Dialogue opening - " + plugin.getClass().getSimpleName() + ", key=" + dialogueKey + "");
        }
        this.dialogue = plugin.newInstance(player);
        if (dialogue == null || !dialogue.open(args)) {
            dialogue = null;
            return false;
        }

        if (dialogue != null) {
            player.dispatch(new DialogueOpenEvent(dialogue));
        }

        return true;
    }

    public void open(DialogueFile file, Object... args){
        this.dialogue = new EmptyPlugin(player,file);
        this.dialogue.open(args);
    }

    /**
     * Handles an dialogue input.
     * @param componentId The id of the chatbox component.
     * @param buttonId The button id.
     */
    public void handle(int componentId, int buttonId) {
        player.setAttribute("chatbox-buttonid",buttonId);
        if((player.getDialogueInterpreter().getDialogue().file != null && player.getDialogueInterpreter().getDialogue().file.getCurrentStage() == END_DIALOGUE) || player.getDialogueInterpreter().getDialogue().stage == END_DIALOGUE){
            player.getInterfaceManager().closeChatbox();
            player.getInterfaceManager().openChatbox(137);
            player.getDialogueInterpreter().dialogue.finished = true;
            close();
            return;
        }

        DialogueFile file = dialogue.file;
        if (!activeTopics.isEmpty() && buttonId >= 2) {
            Topic<?> topic = activeTopics.get(buttonId - 2);

            if (!topic.getSkipPlayer())
                sendDialogues(player, topic.getExpr(), topic.getText());

            if(topic.getToStage() instanceof DialogueFile) {
                DialogueFile topicFile = (DialogueFile) topic.getToStage();
                dialogue.loadFile(topicFile);
            } else if(topic.getToStage() instanceof Integer) {
                int stage = (Integer) topic.getToStage();
                if (file == null) dialogue.stage = stage;
                else file.setStage(stage);
            }
            activeTopics.clear();

            if (topic.getSkipPlayer())
                handle(componentId, buttonId);

            return;
        }

        if(file != null){
            player.dispatch(
                new DialogueOptionSelectionEvent(
                    file,
                    file.getStage(),
                    buttonId - 1
                )
            );

            file.handle(componentId,buttonId - 1);
        } else {
            player.dispatch(
                new DialogueOptionSelectionEvent(
                    dialogue,
                    dialogue.stage,
                    buttonId - 1
                )
            );

            dialogue.handle(componentId, buttonId - 1);//here
        }
    }

    /**
     * Closes the current dialogue.
     * @return {@code True} if successful.
     */
    public boolean close() {
        if (dialogue != null) {
            actions.clear();

            if (player.getInterfaceManager().getChatbox() != null && player.getInterfaceManager().getChatbox().getCloseEvent() != null) {
                return true;
            }
            if (dialogue != null) {
                DialoguePlugin d = dialogue;
                dialogue = null;
                d.close();
                player.dispatch(new DialogueCloseEvent(d));
            }
        }
        activeTopics.clear();
        return dialogue == null;
    }

    /**
     * Puts a dialogue plugin on the mapping.
     * @param id The NPC id (or {@code 1 << 16 | dialogueId} when the dialogue
     * isn't for an NPC).
     * @param plugin The plugin.
     */
    public static void add(int id, DialoguePlugin plugin) {
        if (PLUGINS.containsKey(id)) {
            throw new IllegalArgumentException("Dialogue " + (id & 0xFFFF) + " is already in use - [old=" + PLUGINS.get(id).getClass().getSimpleName() + ", new=" + plugin.getClass().getSimpleName() + "]!");
        }
        PLUGINS.put(id, plugin);
    }

    /**
     * Send plain messages based on the amount of specified messages.
     * @param messages The messages.
     * @return The chatbox component.
     */
    public Component sendDialogue(String... messages) {
        if (messages.length < 1 || messages.length > 4) {
            return null;
        }
        int interfaceId = 209 + messages.length;
        for (int i = 0; i < messages.length; i++) {
            player.getPacketDispatch().sendString(messages[i], interfaceId, i + 1);
        }
        player.getInterfaceManager().openChatbox(interfaceId);
        player.getPacketDispatch().sendInterfaceConfig(player.getInterfaceManager().getChatbox().getId(), 1, false);
        return player.getInterfaceManager().getChatbox();
    }

    /**
     * Sends a plane message and hides the continue button.
     * @param hideContinue if we should hide it or not.
     * @param messages the messages.
     * @return the component.
     */
    public Component sendPlainMessage(final boolean hideContinue, String... messages) {
        sendDialogue(messages);
        player.getPacketDispatch().sendInterfaceConfig(player.getInterfaceManager().getChatbox().getId(), (messages.length + 1), hideContinue);
        return player.getInterfaceManager().getChatbox();
    }

    /**
     * Opens the destroy item chatbox interface.
     * @param id The item id.
     * @param message The message to display.
     * @return The component.
     */
    public Component sendDestroyItem(int id, String message) {
        String text = ItemDefinition.forId(id).getConfiguration(ItemConfigParser.DESTROY_MESSAGE, "Are you sure you want to destroy this object?");
        if (text.length() > 200) {
            String[] words = text.split(" ");
            StringBuilder sb = new StringBuilder(words[0]);
            for (int i = 1; i < words.length; i++) {
                if (i == (words.length / 2)) {
                    sb.append("<br>");
                } else {
                    sb.append(" ");
                }
                sb.append(words[i]);
            }
            text = sb.toString();
        }
        player.getPacketDispatch().sendString(text, 94, 7);
        player.getPacketDispatch().sendString(ItemDefinition.forId(id).getName(), 94, 8);
        player.getPacketDispatch().sendItemOnInterface(id, 1, 94, 9);

        player.getInterfaceManager().openChatbox(94);
        return player.getInterfaceManager().getChatbox();
    }

    /**
     * Send plane messages with a blue title.
     * @param title The title.
     * @param messages The messages.
     * @return The chatbox component.
     */
    public Component sendPlaneMessageWithBlueTitle(String title, String... messages) {
        player.getPacketDispatch().sendString(title, 372, 0);
        for (int i = 0; i < messages.length; i++) {
            player.getPacketDispatch().sendString(messages[i], 372, i + 1);
        }
        player.getInterfaceManager().openChatbox(372);
        return player.getInterfaceManager().getChatbox();
    }

    /**
     * Send plane messages with scroll and a blue title.
     * @param title The title.
     * @param messages The messages.
     * @return The chatbox component.
     */
    public Component sendScrollMessageWithBlueTitle(String title, String... messages) {
        for (int i = 0; i < 11; i++) {
            player.getPacketDispatch().sendString(" ", 421, i + 2);
        }
        player.getPacketDispatch().sendString(title, 421, 1);
        for (int i = 0; i < messages.length; i++) {
            player.getPacketDispatch().sendString(messages[i], 421, i + 2);
        }
        player.getInterfaceManager().openChatbox(421);
        return player.getInterfaceManager().getChatbox();
    }

    /**
     * Send a message with an item next to it.
     * @param itemId The item id.
     */
    public Component sendItemMessage(int itemId, String... messages) {
        // Select interface based on number of messages - 241 (1 line) to 244 (4 lines)
        int interfaceId = 240 + messages.length;
        player.getInterfaceManager().openChatbox(interfaceId);
        player.getPacketDispatch().sendInterfaceConfig(interfaceId, 1, false);
        player.getPacketDispatch().sendInterfaceConfig(interfaceId, 2, false);
        // Hide or empty the title, since double item messages do not have them. (Child 3)
        player.getPacketDispatch().sendString("", interfaceId, 3);
        // Loop and print messages on Child 4,5,6,7 based on messages count.
        for(int i = 0; i < messages.length; i++) {
            player.getPacketDispatch().sendString(messages[i], interfaceId, 4+i);
        }
        // Set the first Item (child 1)
        ItemDefinition itemDef = ItemDefinition.forId(itemId);
        player.getPacketDispatch().sendItemOnInterface(itemId, 1, interfaceId, 2);
        player.getPacketDispatch().sendAngleOnInterface(interfaceId, 2, itemDef.getModelZoom() / 2, itemDef.getModelRotationX(), itemDef.getModelRotationY());
        player.getPacketDispatch().sendRepositionOnInterface(interfaceId, 2, 45, 46);
        // Hide the second item which seems to be used for double items (child 1)
        player.getPacketDispatch().sendInterfaceConfig(interfaceId, 1, true);
        // These are old interfaces which made no sense to use them.
//        player.getPacketDispatch().sendString(message, 131, 1);
//        player.getPacketDispatch().sendItemOnInterface(itemId, 1, 131, 2);
//        player.getPacketDispatch().sendString(message, 173, 4);
//        player.getPacketDispatch().sendString("", 173, 3);
//        player.getPacketDispatch().sendItemOnInterface(itemId, 1, 173, 1);
//        player.getPacketDispatch().sendString(message, 519, 1);
//        player.getPacketDispatch().sendItemOnInterface(itemId, 1, 519, 0);
//        player.getPacketDispatch().sendString(message, 757, 2);
//        player.getPacketDispatch().sendString("", 757, 1);
//        player.getPacketDispatch().sendItemOnInterface(itemId, 1, 757, 0);
//        player.getPacketDispatch().sendString(message, 760, 0);
//        player.getPacketDispatch().sendItemOnInterface(itemId, 1, 760, 1);
        return player.getInterfaceManager().getChatbox();
    }

    /**
     * Send a message with an item next to it.
     */
    public Component sendItemMessage(final Item item, String... messages) {
        return sendItemMessage(item.getId(), messages);
    }

    /**
     * Send a message with an item next to it.
     * @param message The message.
     */
    public Component sendDoubleItemMessage(int first, int second, String... message) {
        return sendDoubleItemMessage(new Item(first), new Item(second), message);
    }

    /**
     * Send a message with two items next to it.
     * Note that interface 241 to 244 contains 2 childs for images, which is used for sendDoubleItemMessage.
     * @param first The first item to display.
     * @param second The second item to display.
     * @param messages The array of messages, one message per line.
     */
    public Component sendDoubleItemMessage(Item first, Item second, String... messages) {
        // Select interface based on number of messages - 241 (1 line) to 244 (4 lines)
        int interfaceId = 240 + messages.length;
        player.getInterfaceManager().openChatbox(interfaceId);
        player.getPacketDispatch().sendInterfaceConfig(interfaceId, 1, false);
        player.getPacketDispatch().sendInterfaceConfig(interfaceId, 2, false);
        // Hide or empty the title, since double item messages do not have them.
        player.getPacketDispatch().sendString("", interfaceId, 3);
        // Loop and print messages on child 4,5,6,7 based on messages count.
        for(int i = 0; i < messages.length; i++) {
            player.getPacketDispatch().sendString(messages[i], interfaceId, 4+i);
        }
        // Set the first item
        ItemDefinition itemDef = ItemDefinition.forId(first.getId());
        player.getPacketDispatch().sendItemOnInterface(first.getId(), first.getAmount(), interfaceId, 1);
        player.getPacketDispatch().sendAngleOnInterface(interfaceId, 1, (int)(itemDef.getModelZoom() / 1.5), itemDef.getModelRotationX(), itemDef.getModelRotationY());
        player.getPacketDispatch().sendRepositionOnInterface(interfaceId, 1, 40, 40);
        // Set the second items
        ItemDefinition itemDef2 = ItemDefinition.forId(second.getId());
        player.getPacketDispatch().sendItemOnInterface(second.getId(), second.getAmount(), interfaceId, 2);
        player.getPacketDispatch().sendAngleOnInterface(interfaceId, 2, (int)(itemDef2.getModelZoom() / 1.5), itemDef2.getModelRotationX(), itemDef2.getModelRotationY());
        player.getPacketDispatch().sendRepositionOnInterface(interfaceId, 2, 60, 65);

        return player.getInterfaceManager().getChatbox();
    }

    /**
     * Send dialogues based on the amount of specified messages.
     * @param entity The entity.
     * @param expression The entity's facial expression.
     * @param messages The messages.
     * @return The chatbox component.
     */
    public Component sendDialogues(Entity entity, FacialExpression expression, String... messages) {
        return sendDialogues(entity, expression == null ? -1 : expression.getAnimationId(), messages);
    }

    /**
     * Send dialogues based on the amount of specified messages.
     * @param entity The entity.
     * @param expression The entity's facial expression.
     * @param messages The messages.
     * @return The chatbox component.
     */
    public Component sendDialogues(Entity entity, int expression, String... messages) {
        return sendDialogues(entity instanceof Player ? -1 : ((NPC) entity).getShownNPC(player).getId(), expression, false, messages);
    }

    /**
     * Send dialogues based on the amount of specified messages.
     * @param npcId The npc id.
     * @param expression The entity's facial expression.
     * @param messages The messages.
     * @param hide should the continue button be hidden?
     * @return The chatbox component.
     */
    public Component sendDialogues(int npcId, FacialExpression expression, boolean hide, String... messages) {
        return sendDialogues(npcId, expression == null ? -1 : expression.getAnimationId(), hide, messages);
    }

    /**
     * Send dialogues based on the amount of specified messages.
     * @param expression The entity's facial expression.
     * @param messages The messages.
     * @param hide the continue.
     * @return The chatbox component.
     */
    public Component sendDialogues(Entity entity, FacialExpression expression, boolean hide, String... messages) {
        return sendDialogues(entity.getId(), expression == null ? -1 : expression.getAnimationId(), hide, messages);
    }

    /**
     * Send dialogues based on the amount of specified messages.
     * @param expression The entity's facial expression.
     * @param messages The messages.
     * @param hide should the continue button be hidden?
     * @return The chatbox component.
     */
    public Component sendDialogues(Entity entity, int expression, boolean hide, String... messages) {
        return sendDialogues(entity.getId(), expression, hide, messages);
    }

    /**
     * Send dialogues based on the amount of specified messages.
     * @param npcId The npc id.
     * @param expression The entity's facial expression.
     * @param messages The messages.
     * @return The chatbox component.
     */
    public Component sendDialogues(int npcId, FacialExpression expression, String... messages) {
        return sendDialogues(npcId, expression == null ? -1 : expression.getAnimationId(),false,  messages);
    }

    static Pattern GENDERED_SUBSTITUTION = Pattern.compile("@g\\[([^,]*),([^\\]]*)\\]");
    public static String doSubstitutions(Player player, String msg) {
        msg = msg.replace("@name", player.getUsername());
        msg = msg.replace("@servername", GameWorld.getSettings().getName());
        StringBuilder sb = new StringBuilder();
        Matcher m = GENDERED_SUBSTITUTION.matcher(msg);
        int index = player.isMale() ? 1 : 2;
        while(m.find()) {
            m.appendReplacement(sb, m.group(index));
        }
        m.appendTail(sb);
        return sb.toString();
    }
    /**
     * Send dialogues based on the amount of specified messages.
     * @param npcId The npc id.
     * @param expression The entity's facial expression.
     * @param messages The messages.
     * @return The chatbox component.
     */
    public Component sendDialogues(int npcId, int expression, String... messages) {
        return sendDialogues(npcId, expression, false, messages);
    }

    /**
     * Send dialogues based on the amount of specified messages.
     * @param npcId The npc id.
     * @param expression The entity's facial expression.
     * @param messages The messages.
     * @param hide should the continue button be hidden?
     * @return The chatbox component.
     */
    public Component sendDialogues(int npcId, int expression, boolean hide, String... messages) {
        if (messages.length < 1 || messages.length > 4) {
            System.err.println("Invalid amount of messages: " + messages.length);
            return null;
        }
        boolean npc = npcId > -1;
        int interfaceId = (npc ? 240 : 63) + messages.length;
        interfaceId += hide ? 4 : 0;
        if (expression == -1) {
            expression = FacialExpression.HALF_GUILTY.getAnimationId();
        }
        player.getInterfaceManager().openChatbox(interfaceId);
        player.getPacketDispatch().sendInterfaceConfig(interfaceId, 1, true);
        player.getPacketDispatch().sendInterfaceConfig(interfaceId, 2, false);
        player.getPacketDispatch().sendAnimationInterface(expression, interfaceId, 2);
        player.getPacketDispatch().sendItemOnInterface(-1, 1, interfaceId, 1);
        if (npc) {
            player.getPacketDispatch().sendItemOnInterface(-1, 1, interfaceId, 1);
            player.getPacketDispatch().sendRepositionOnInterface(interfaceId, 2, 45, 45);
            player.getPacketDispatch().sendNpcOnInterface(npcId, interfaceId, 2);
            player.getPacketDispatch().sendString(NPCDefinition.forId(npcId).getName(), interfaceId, 3);
        } else {
            player.getPacketDispatch().sendRepositionOnInterface(interfaceId, 2, 426, 45); // 423 is 47 * 9
            player.getPacketDispatch().sendPlayerOnInterface(interfaceId, 2);
            player.getPacketDispatch().sendString(player.getUsername(), interfaceId, 3);
        }
        for (int i = 0; i < messages.length; i++) {
            player.getPacketDispatch().sendString(doSubstitutions(player, messages[i]), interfaceId, (i + 4));
        }
        player.getPacketDispatch().sendInterfaceConfig(player.getInterfaceManager().getChatbox().getId(), 3, false);
        return player.getInterfaceManager().getChatbox();
    }

    /**
     * Send options based on the amount of specified options.
     * @param title The title.
     * @param options The options.
     */
    public Component sendOptions(Object title, String... options) {
        int interfaceId = 224 + (2 * options.length);
        if (options.length < 2 || options.length > 5) {
            return null;
        }
        if (title != null) {
            player.getPacketDispatch().sendString(title.toString(), interfaceId, 1);
        }
        for (int i = 0; i < options.length; i++) {
            player.getPacketDispatch().sendString(options[i].toString(), interfaceId, i + 2);
        }
        player.getInterfaceManager().openChatbox(interfaceId);
        return player.getInterfaceManager().getChatbox();
    }

    /**
     * Send a input run script.
     * @param string The strings.
     * @param objects The arguments.
     */
    public void sendInput(boolean string, Object... objects) {
        player.getPacketDispatch().sendRunScript(string ? 109 : 108, "s", objects);
    }

    /**
     * Sends a long input.
     * @param objects the objects.
     */
    public void sendLongInput(Object... objects) {
        player.getPacketDispatch().sendRunScript(110, "s", objects);
    }

    /**
     * Sends the private message input.
     * @param reciever The receiver.
     */
    public void sendMessageInput(String reciever) {
        player.getPacketDispatch().sendRunScript(107, "s", reciever);
    }

    /**
     * Checks if the dialogue for the given id is added.
     * @param id The NPC id/dialogue id.
     * @return {@code True} if so.
     */
    public static boolean contains(int id) {
        return PLUGINS.containsKey(id);
    }

    /**
     * Gets the currently opened dialogue.
     * @return The dialogue plugin.
     */
    public DialoguePlugin getDialogue() {
        return dialogue;
    }

    /**
     * Reserves a key for the name.
     * @param name The name.
     * @return The key.
     */
    public static int getDialogueKey(String name) {
        return 1 << 16 | name.hashCode();
    }

    /**
     * Adds a dialogue reward.
     * @param action the reward.
     */
    public void addAction(DialogueAction action) {
        actions.add(action);
    }

    /**
     * Gets the actions.
     * @return The actions.
     */
    public List<DialogueAction> getActions() {
        return actions;
    }
}
