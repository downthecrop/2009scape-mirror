package core.game.world.map.zone.impl;

import core.game.node.entity.Entity;
import core.game.node.entity.player.Player;
import core.game.world.map.zone.MapZone;
import core.game.world.map.zone.ZoneBorders;

/**
 * Handles the stone circle zone south of Ardougne.
 * @author Kya
 */
public final class StoneCircleZone extends MapZone {

	/**
	 * The instance.
	 */
	private static final StoneCircleZone INSTANCE = new StoneCircleZone();

	/**
	 * Constructs a new {@code StoneCircleZone} {@code Object}.
	 */
	public StoneCircleZone() {
		super("Stonecircle", true);
	}

	@Override
	public void configure() {
		register(new ZoneBorders(2558, 3219, 2559, 3225));
		register(new ZoneBorders(2560, 3219, 2564, 3225));
	}

	@Override
	public boolean enter(Entity e) {
		if (e instanceof Player) {
			Player p = (Player) e;
			p.varpManager.get(1184).setVarbit(28,1).send(p);
		}
		return super.enter(e);
	}
	
	@Override
	public boolean leave(Entity e, boolean logout) {
		if (!logout && e instanceof Player) {
			Player p = (Player) e;
			p.varpManager.get(1184).setVarbit(28,0).send(p);
		}
		return super.leave(e, logout);
	}

	/**
	 * Gets the instance.
	 * @return The instance.
	 */
	public static StoneCircleZone getInstance() {
		return INSTANCE;
	}

}
