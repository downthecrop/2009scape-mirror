package core.game.node.entity.skill.construction.decoration.bedroom;


import core.cache.def.impl.SceneryDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.plugin.Initializable;
import core.plugin.Plugin;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * ClockPlugin.java
 * @author Clayton Williams (hope)
 * @date Oct 26, 2015
 */
@Initializable
public class ClockPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(13169).getHandlers().put("option:read", this);
		SceneryDefinition.forId(13170).getHandlers().put("option:read", this);
		SceneryDefinition.forId(13171).getHandlers().put("option:read", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		Scenery object = node.asScenery();
		SimpleDateFormat format = new SimpleDateFormat("mm");
		int minuteDisplay = Integer.parseInt(format.format(Calendar.getInstance().getTime()));
		StringBuilder sb = new StringBuilder("It's ");
		if (minuteDisplay == 0) {
			sb.append("Rune o'clock.");
		} else if (minuteDisplay == 15) {
			sb.append("a quarter past Rune.");	
		} else if (minuteDisplay > 0 && minuteDisplay < 30) {
			sb.append(minuteDisplay + " past Rune.");
		} else if (minuteDisplay == 45) {
			sb.append("a quarter till Rune.");
		} else {
			sb.append((60 - minuteDisplay) + " till Rune.");
		}
		player.getDialogueInterpreter().sendItemMessage(object.getId() - 5117, sb.toString());
		return true;
	}

}
