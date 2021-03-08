package core.game.component;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the component definitions.
 * @author Emperor
 *
 */
public final class ComponentDefinition {

	/**
	 * The component definitions mapping.
	 */
	private static final Map<Integer, ComponentDefinition> DEFINITIONS = new HashMap<Integer, ComponentDefinition>();

	/**
	 * The interface type.
	 */
	private InterfaceType type = InterfaceType.DEFAULT;

	/**
	 * The interface context.
	 */
	private boolean walkable;

	/**
	 * The tab index.
	 */
	private int tabIndex = -1;

	/**
	 * Represents the plugin handler.
	 */
	private ComponentPlugin plugin;

	/**
	 * Constructs a new {@code ComponentDefinition} {@code Object}.
	 */
	public ComponentDefinition() {
		/*
		 * empty.
		 */
	}
	
	/**
	 * Parses the definition values from a result set.
	 * @throws SQLException The exception if thrown.
	 */
	public ComponentDefinition parse(String type, String walkable, String tabIndex){
		setType(InterfaceType.values()[Integer.parseInt(type)]);
		setWalkable(Boolean.parseBoolean(walkable));
		setTabIndex(Integer.parseInt(tabIndex));
		return this;
	}
	
	/**
	 * Gets the component definitions for the component id.
	 * @param componentId The component id.
	 * @return The component definitions.
	 */
	public static ComponentDefinition forId(int componentId) {
		ComponentDefinition def = DEFINITIONS.get(componentId);
		if (def == null) {
			DEFINITIONS.put(componentId, def = new ComponentDefinition());
		}
		return def;
	}

	/**
	 * Add a plugin to a definition.
	 * @param id the id.
	 * @param plugin the plugin.
	 */
	public static void put(int id, ComponentPlugin plugin) {
		ComponentDefinition.forId(id).setPlugin(plugin);
	}

	/**
	 * Gets the definitions mapping.
	 * @return The definitions mapping.
	 */
	public static Map<Integer, ComponentDefinition> getDefinitions() {
		return DEFINITIONS;
	}

	/**
	 * Gets the plugin.
	 * @return The plugin.
	 */
	public ComponentPlugin getPlugin() {
		return plugin;
	}

	/**
	 * Sets the plugin.
	 * @param plugin The plugin to set.
	 */
	public void setPlugin(ComponentPlugin plugin) {
		this.plugin = plugin;
	}

	/**
	 * Gets the window pane id.
	 * @param resizable If the player is using resizable mode.
	 * @return The window pane id.
	 */
	public int getWindowPaneId(boolean resizable) {
		return resizable ? type.getResizablePaneId() : type.getFixedPaneId();
	}

	/**
	 * Gets the child id.
	 * @param resizable If the player is using resizable mode.
	 * @return The child id.
	 */
	public int getChildId(boolean resizable) {
		return resizable ? type.getResizableChildId() : type.getFixedChildId();
	}

	/**
	 * Gets the type.
	 * @return the type
	 */
	public InterfaceType getType() {
		return type;
	}

	/**
	 * Sets the batype.
	 * @param type the type to set.
	 */
	public void setType(InterfaceType type) {
		this.type = type;
	}

	/**
	 * Gets the walkable.
	 * @return the walkable
	 */
	public boolean isWalkable() {
		return walkable;
	}

	/**
	 * Sets the bawalkable.
	 * @param walkable the walkable to set.
	 */
	public void setWalkable(boolean walkable) {
		this.walkable = walkable;
	}

	/**
	 * Gets the tabIndex.
	 * @return the tabIndex
	 */
	public int getTabIndex() {
		return tabIndex;
	}

	/**
	 * Sets the batabIndex.
	 * @param tabIndex the tabIndex to set.
	 */
	public void setTabIndex(int tabIndex) {
		this.tabIndex = tabIndex;
	}

	@Override
	public String toString() {
		return "ComponentDefinition [type=" + type + ", walkable=" + walkable + ", tabIndex=" + tabIndex + ", plugin=" + plugin + "]";
	}

}