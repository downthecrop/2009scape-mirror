package core.game.container.access;

import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.net.packet.PacketRepository;
import core.net.packet.context.ContainerContext;
import core.net.packet.out.ContainerPacket;

/**
 * Generates a set of items and options on an interface.
 * @date 5/02/2013
 * @author Stacx
 */
public class InterfaceContainer {

	/**
	 * The client script index for set_options.
	 */
	private static final int CLIENT_SCRIPT_INDEX = 150;

	/**
	 * This index will increase each time a set is generated.
	 */
	private static int index = 600; // 93

	/**
	 * Generates a container/array of items in an interface positioned on the
	 * child index.
	 * @param player , the player we generate this set for
	 * @param itemArray , the container/array of items we want to display
	 * @param options , the right-click options we want for the items
	 * @param interfaceIndex , the interface index
	 * @param childIndex , the child index of the interface where we display the
	 * items at.
	 * @return The container key.
	 */
	private static int generate(Player player, Item[] itemArray, String[] options, int interfaceIndex, int childIndex, int x, int y, int key) {
		Object[] clientScript = new Object[options.length + 7];
		player.getPacketDispatch().sendRunScript(CLIENT_SCRIPT_INDEX, generateScriptArguments(options.length), populateScript(clientScript, options, interfaceIndex << 16 | childIndex, x, y, key));
		BitregisterAssembler.send(player, interfaceIndex, childIndex, 0, itemArray.length, new BitregisterAssembler(options));
		PacketRepository.send(ContainerPacket.class, new ContainerContext(player, -1, -2, key, itemArray, itemArray.length, false));
		return increment();
	}

	public static int generateOptions(Player player, String[] options, int interfaceIndex, int childIndex, int x, int y, int key){
		player.getPacketDispatch().sendRunScript(CLIENT_SCRIPT_INDEX, generateScriptArguments(options.length), populateScript(new Object[options.length + 7],options,interfaceIndex << 16 | childIndex, x, y, key));
		BitregisterAssembler.send(player,interfaceIndex,childIndex,0,28,new BitregisterAssembler(options));
		return increment();
	}

	/**
	 * Generates options for the interface item container.
	 * @param player The player.
	 * @param interfaceId The interface id.
	 * @param childId The child index.
	 * @param itemLength The amount of items.
	 * @param options The options.
	 * @return The container key.
	 */
	public static int generate(Player player, int interfaceId, int childId, int itemLength, String... options) {
		return generate(player, interfaceId, childId, itemLength, 7, 3, options);
	}

	/**
	 * Generates options for the interface item container.
	 * @param player The player.
	 * @param interfaceId The interface id.
	 * @param childId The child index.
	 * @param itemLength The amount of items.
	 * @param x The amount of items in a row.
	 * @param y The amount of item rows.
	 * @param options The options.
	 * @return The container key.
	 */
	public static int generate(Player player, int interfaceId, int childId, int itemLength, int x, int y, String... options) {
		int key = increment();
		Object[] clientScript = new Object[options.length + 7];
		player.getPacketDispatch().sendRunScript(CLIENT_SCRIPT_INDEX, generateScriptArguments(options.length), populateScript(clientScript, options, interfaceId << 16 | childId, x, y, key));
		BitregisterAssembler.send(player, interfaceId, childId, 0, itemLength, new BitregisterAssembler(options));
		return key;
	}

	/**
	 * Increments the current index.
	 * @return The previous index.
	 */
	private static int increment() {
		if (index == 6999) {
			index = 600;
		}
		return index++;
	}

	/**
	 * Populates an object array used as a script for the client
	 * @param script , the array we want to populate
	 * @param options , the right-click options for our items
	 * @param hash , interfaceIndex << 16 | childIndex
	 * @return script, the populated script
	 */
	private static Object[] populateScript(Object[] script, String[] options, int hash, int x, int y, int key) {
		int offset = 0;
		for (String option : options) {
			script[offset++] = option;
		}
		System.arraycopy(new Object[] { -1, 0, x, y, key, hash }, 0, script, offset, 6);
		return script;
	}

	/**
	 * Generates a script argument type string for the client (note: everything
	 * but a "s" is integer for the run script packet)
	 * @param length , the amount of options
	 * @return the generated string.
	 */
	private static String generateScriptArguments(int length) {
		StringBuilder builder = new StringBuilder("IviiiI");
		while (length > 0) {
			builder.append("s");
			length--;
		}
		return builder.toString();
	}

	/**
	 * Default method to generate and send an item array for the client.
	 * @return The container key.
	 * @see {@link InterfaceContainer.generate}
	 */
	public static int generateItems(Player player, Item[] itemArray, String[] options, int interfaceIndex, int childIndex) {
		return generateItems(player, itemArray, options, interfaceIndex, childIndex, 7, 3, increment());
	}

	/**
	 * Default method to generate and send an item array for the client.
	 * @return The container key.
	 * @see {@link InterfaceContainer.generate}
	 */
	public static int generateItems(Player player, Item[] itemArray, String[] options, int interfaceIndex, int childIndex, int key) {
		return generateItems(player, itemArray, options, interfaceIndex, childIndex, 7, 3, key);
	}

	/**
	 * Method to generate the send items for the client with a specified
	 * location for the items.
	 * @param x , the x coordinate
	 * @param y , the y coordinate
	 * @return The container key.
	 */
	public static int generateItems(Player player, Item[] itemArray, String[] options, int interfaceIndex, int childIndex, int x, int y) {
		return generateItems(player, itemArray, options, interfaceIndex, childIndex, x, y, increment());
	}

	/**
	 * Method to generate the send items for the client with a specified
	 * location for the items.
	 * @param x , the x coordinate
	 * @param y , the y coordinate
	 * @return The container key.
	 */
	public static int generateItems(Player player, Item[] itemArray, String[] options, int interfaceIndex, int childIndex, int x, int y, int key) {
		return generate(player, itemArray, options, interfaceIndex, childIndex, x, y, key);
	}

}
