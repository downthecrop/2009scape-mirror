package plugin.interaction.item.withitem;

import org.crandor.game.component.Component;
import org.crandor.game.content.skill.Skills;
import org.crandor.game.content.skill.free.crafting.GlassProduct;
import org.crandor.game.interaction.NodeUsageEvent;
import org.crandor.game.interaction.UseWithHandler;
import org.crandor.game.node.entity.player.Player;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the plugin used to make glass.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public class MakeGlassInterfacePlugin extends UseWithHandler {

	/**
	 * Constructs a new {@code MakeGlassInterfacePlugin} {@code Object}.
	 */
	public MakeGlassInterfacePlugin() {
		super(1775);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		addHandler(1785, ITEM_TYPE, this);
		return this;
	}

	@Override
	public boolean handle(final NodeUsageEvent event) {
		event.getPlayer().getInterfaceManager().open(new Component(542));
		final Player player = event.getPlayer();
		switch (GlassProduct.BEER) {
		case BEER:
			break;
		case CANDLE:
			if (player.getSkills().getLevel(Skills.CRAFTING) < 4)
				player.getPacketDispatch().sendString("<col=FF8000>Candle lantern</col>", 542, 25);
			break;
		case FISHBOWL:
			if (player.getSkills().getLevel(Skills.CRAFTING) < 42)
				player.getPacketDispatch().sendString("<col=FF8000>Fish Bowl</col>", 542, 28);
			break;
		case LANTERN_LENS:
			if (player.getSkills().getLevel(Skills.CRAFTING) < 49)
				player.getPacketDispatch().sendString("<col=FF8000>Lantern lens</col>", 542, 27);
			break;
		case OIL_LAMP:
			if (player.getSkills().getLevel(Skills.CRAFTING) < 12)
				player.getPacketDispatch().sendString("<col=FF8000>Oil lamp</col>", 542, 26);
			break;
		case ORB:
			if (player.getSkills().getLevel(Skills.CRAFTING) < 46)
				player.getPacketDispatch().sendString("<col=FF8000>Unpowered orb</col>", 542, 23);
			break;
		case VIAL:
			if (player.getSkills().getLevel(Skills.CRAFTING) < 33)
				player.getPacketDispatch().sendString("<col=FF8000>Vial</col>", 542, 22);
			break;
		default:
			break;

		}
		return true;
	}

}
