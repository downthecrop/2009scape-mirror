package core.game.interaction.object.wildyditch;

import core.cache.def.impl.SceneryDefinition;
import core.game.component.Component;
import core.game.interaction.MovementPulse;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.impl.PulseType;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.plugin.Initializable;
import core.plugin.Plugin;
import rs09.plugin.ClassScanner;

/**
 * Represents the plugin to handle the crossing.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class WildernessDitchPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(23271).getHandlers().put("option:cross", this);
		ClassScanner.definePlugin(new WildernessInterfacePlugin());
		return this;
	}

	@Override
	public boolean handle(final Player player, final Node node, String option) {
		if (player.getLocation().getDistance(node.getLocation()) < 3) {
			handleDitch(player, node);
		} else {
			player.getPulseManager().run(new MovementPulse(player, node) {
				@Override
				public boolean pulse() {
					handleDitch(player, node);
					return true;
				}
			}, PulseType.STANDARD);
		}
		return true;
	}

	/**
	 * Handles the wilderness ditch jumping.
	 * @param player The player.
	 * @param node The ditch object.
	 */
	public void handleDitch(final Player player, Node node) {
		player.faceLocation(node.getLocation());
		Scenery ditch = (Scenery) node;
		player.setAttribute("wildy_ditch", ditch);
		if(!player.isArtificial()) {
			if (ditch.getRotation() % 2 == 0) {
				if (player.getLocation().getY() <= node.getLocation().getY()) {
					player.getInterfaceManager().open(new Component(382));
					return;
				}
			} else {
				if (player.getLocation().getX() > node.getLocation().getX()) {
					player.getInterfaceManager().open(new Component(382));
					return;
				}
			}
		}
		WildernessInterfacePlugin.handleDitch(player);
	}

	@Override
	public boolean isWalk() {
		return true;
	}
}
