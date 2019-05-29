package plugin.skill.construction.npc;


import org.crandor.game.interaction.NodeUsageEvent;
import org.crandor.game.interaction.UseWithHandler;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;
import org.crandor.plugin.PluginManager;

/**
 * Handles interaction with the house servant.
 * @author Splinter
 * @date 9/24/2015
 * @version 0.1
 * 
 * @see {@link HouseServantDialogue} 
 * For the other associated classes that deal with house servants.
 *
 */
@InitializablePlugin
public class HouseServantPlugin extends UseWithHandler {
	
	/**
	 * The item IDS to use.
	 */
	final static int[] IDS = { 1511, 1521, 6333, 6332 };

	/**
	 * Constructs a new {@code HouseServantPlugin} {@code Object}.
	 */
	public HouseServantPlugin() {
		super(IDS);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		addHandler(4235, NPC_TYPE, this);
		addHandler(4237, NPC_TYPE, this);
		addHandler(4239, NPC_TYPE, this);
		addHandler(4241, NPC_TYPE, this);
		addHandler(4243, NPC_TYPE, this);
		PluginManager.definePlugin(new HouseServantDialogue());
		return this;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		if (event.getUsedItem() == null || event.getUsedWith() == null) {
			return true;
		}
		event.getPlayer().getDialogueInterpreter().open(event.getUsedWith().asNpc().getId(), event.getUsedWith().asNpc(), true, event.getUsedItem());
		return true;
	}

}