package core.game.interaction.item.withobject;

import core.game.content.global.Bones;
import core.game.node.entity.skill.Skills;
import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.entity.player.Player;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Handles the guilded altar.
 * @author Vexia
 *
 */
@Initializable
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
