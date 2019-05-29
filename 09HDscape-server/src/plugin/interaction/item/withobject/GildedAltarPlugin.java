package plugin.interaction.item.withobject;

import org.crandor.game.content.global.Bones;
import org.crandor.game.content.skill.Skills;
import org.crandor.game.interaction.NodeUsageEvent;
import org.crandor.game.interaction.UseWithHandler;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.game.world.update.flag.context.Graphics;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Handles the guilded altar.
 * @author Vexia
 *
 */
@InitializablePlugin
public class GildedAltarPlugin extends UseWithHandler {

	/**
	 * The animation for the player.
	 */
	private static final Animation ANIMATION = new Animation(713);
	
	/**
	 * The graphics for the player.
	 */
	private static final Graphics GRAPHICS = new Graphics(624);

	/**
	 * Constructs the {@code GuildedAltarPlugin}
	 */
	public GildedAltarPlugin() {
		super(Bones.getArray());
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		addHandler(24343, OBJECT_TYPE, this);
		return this;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		final Player player = event.getPlayer();
		final Bones bone = Bones.forId(event.getUsedItem().getId());
		if (bone == null) {
			return true;
		}
		if (player.getInventory().remove(event.getUsedItem(), event.getUsedItem().getSlot(), true)) {
			player.lock(ANIMATION.getDelay());
			player.visualize(ANIMATION, GRAPHICS);
			player.getSkills().addExperience(Skills.PRAYER, bone.getExperience() * 2.5);
			player.sendMessage("The gods are very pleased with your offering.");
		}
		return true;
	}

}
