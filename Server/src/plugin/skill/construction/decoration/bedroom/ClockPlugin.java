package plugin.skill.construction.decoration.bedroom;


import org.crandor.cache.def.impl.ObjectDefinition;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.object.GameObject;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;
import org.joda.time.DateTime;

/**
 * ClockPlugin.java
 * @author Clayton Williams (hope)
 * @date Oct 26, 2015
 */
@InitializablePlugin
public class ClockPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ObjectDefinition.forId(13169).getConfigurations().put("option:read", this);
		ObjectDefinition.forId(13170).getConfigurations().put("option:read", this);
		ObjectDefinition.forId(13171).getConfigurations().put("option:read", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		GameObject object = node.asObject();
		DateTime now = new DateTime();
		int minuteDisplay = ((int) (now.getMinuteOfHour() / 5)) * 5;
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
