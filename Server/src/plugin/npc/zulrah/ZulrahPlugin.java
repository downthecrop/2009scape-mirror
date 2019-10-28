package plugin.npc.zulrah;

import org.crandor.cache.def.impl.ObjectDefinition;
import org.crandor.game.content.activity.ActivityManager;
import org.crandor.game.content.dialogue.DialogueAction;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.world.GameWorld;
import org.crandor.plugin.Plugin;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.PluginManager;

/**
 * Handles interactions related to Zulrah.
 * @author Vexia
 *
 */
@InitializablePlugin
public class ZulrahPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ObjectDefinition.forId(10075).getConfigurations().put("option:board", this);
		PluginManager.definePlugin(new ZulrahCutscene(), new ZulrahNPC());
		return this;
	}

	@Override
	public boolean handle(final Player player, Node node, String option) {
		if (node.asObject().getId() == 10075 && GameWorld.getSettings().isDevMode()) {
			player.getDialogueInterpreter().sendOptions("Return to Zulrah's shrine?", "Yes", "No");
			player.getDialogueInterpreter().addAction(new DialogueAction() {
				@Override
				public void handle(Player p, int buttonId) {
					switch(buttonId) {
					case 2:
						ActivityManager.start(p, "zulrah cutscene", false);
						break;
					case 3:
						p.getDialogueInterpreter().close();
						break;
					}
					
				}
				
			});
			return true;
		}
		return true;
	}
	
}
