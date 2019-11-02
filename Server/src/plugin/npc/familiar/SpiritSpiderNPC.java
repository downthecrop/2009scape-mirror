package plugin.npc.familiar;

import org.crandor.game.content.skill.member.summoning.familiar.Familiar;
import org.crandor.game.content.skill.member.summoning.familiar.FamiliarSpecial;
import org.crandor.game.node.entity.combat.equipment.WeaponInterface;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.GroundItemManager;
import org.crandor.game.node.item.Item;
import org.crandor.game.world.GameWorld;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.map.RegionManager;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.tools.RandomFunction;

/**
 * Represents the Spirit Spider familiar.
 * @author Aero
 * @author Vexia
 */
@InitializablePlugin
public class SpiritSpiderNPC extends Familiar {

	/**
	 * The red spider egg item.
	 */
	private static final Item EGG = new Item(223);

	/**
	 * The delay until the next chance of random eggs.
	 */
	private int eggDelay = GameWorld.getTicks() + 500;

	/**
	 * Constructs a new {@code SpiritSpiderNPC} {@code Object}.
	 */
	public SpiritSpiderNPC() {
		this(null, 6841);
	}

	/**
	 * Constructs a new {@code SpiritSpiderNPC} {@code Object}.
	 * @param owner The owner.
	 * @param id The id.
	 */
	public SpiritSpiderNPC(Player owner, int id) {
		super(owner, id, 1500, 12059, 6, WeaponInterface.STYLE_CONTROLLED);
	}

	@Override
	public void handleFamiliarTick() {
		super.handleFamiliarTick();
		if (eggDelay < GameWorld.getTicks()) {
			if (RandomFunction.random(25) == 5) {
				createEggs();
				sendChat("Clicketyclack");
				eggDelay = GameWorld.getTicks() + 500;
			}
		}
	}

	@Override
	public Familiar construct(Player owner, int id) {
		return new SpiritSpiderNPC(owner, id);
	}

	@Override
	protected boolean specialMove(FamiliarSpecial special) {
		createEggs();
		return true;
	}

	/**
	 * Creates red spider eggs.
	 */
	private void createEggs() {
		final int amount = RandomFunction.random(8);
		Location location = null;
		for (int i = 0; i < amount; i++) {
			location = getEggLocation();
			if (location == null) {
				continue;
			}
			owner.getPacketDispatch().sendPositionedGraphic(1342, 0, 0, location);
			GroundItemManager.create(EGG, location, owner);
		}
		animate(Animation.create(5328));
	}

	/**
	 * Gets the egg location.
	 * @return the location.
	 */
	private Location getEggLocation() {
		Location loc = owner.getLocation().transform(RandomFunction.random(2) == 1 ? -RandomFunction.random(2) : RandomFunction.random(2), RandomFunction.random(2) == 1 ? -RandomFunction.random(2) : RandomFunction.random(2), 0);
		if (loc.equals(owner.getLocation()) || !RegionManager.isTeleportPermitted(loc) || RegionManager.getObject(loc) != null) {
			return null;
		}
		return loc;
	}

	@Override
	public int[] getIds() {
		return new int[] { 6841, 6842 };
	}

}
