package core.game.node.entity.skill.construction.decoration.questhall;


import core.cache.def.impl.SceneryDefinition;
import core.game.content.dialogue.DialogueAction;
import core.game.content.dialogue.DialogueInterpreter;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.audio.Audio;
import core.game.system.task.Pulse;
import rs09.game.world.GameWorld;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Handles the POH Mounted Glory.
 * @author Splinter
 */
@Initializable
public class MountedGloryPlugin extends OptionHandler {

	/**
	 * Represents the teleport animation.
	 */
	private static final Animation ANIMATION = new Animation(714);

	/**
	 * Represents the graphics to use.
	 */
	private static final Graphics GRAPHICS = new Graphics(308, 100, 50);

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(13523).getHandlers().put("option:rub", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		DialogueInterpreter interpreter = player.getDialogueInterpreter();
		interpreter.sendOptions("Select a location.", "Edgeville", "Karamja", "Draynor Village", "Al-Kharid", "Nowhere.");
		interpreter.addAction(new DialogueAction() {

			@Override
			public void handle(Player player, int buttonId) {
				switch (buttonId) {
				case 2:
					teleport(player, Location.create(3087, 3495, 0));
					break;
				case 3:
					teleport(player, Location.create(2919, 3175, 0));
					break;
				case 4:
					teleport(player, Location.create(3081, 3250, 0));
					break;
				case 5:
					teleport(player, Location.create(3304, 3124, 0));
					break;
				}
			}

		});
		return true;
	}

	/**
	 * Method used to teleport to a location.
	 * @param player the player.
	 * @param location the location.
	 */
	private boolean teleport(final Player player, final Location location) {
		if (player.isTeleBlocked()) {
			player.sendMessage("A magical force has stopped you from teleporting.");
			return false;
		}
		player.lock();
		player.visualize(ANIMATION, GRAPHICS);
        player.getAudioManager().send(new Audio(200), true);	
		player.getImpactHandler().setDisabledTicks(4);
		GameWorld.getPulser().submit(new Pulse(4, player) {
			@Override
			public boolean pulse() {
				player.unlock();
				player.getProperties().setTeleportLocation(location);
				player.getAnimator().reset();
				return true;
			}
		});
		return true;
	}

}