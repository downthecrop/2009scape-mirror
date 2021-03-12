package core.game.content.ame.events.evilchicken;

import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.combat.CombatSwingHandler;
import core.game.node.entity.combat.handlers.MagicSwingHandler;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.player.Player;
import core.game.node.item.GroundItem;
import core.game.node.item.GroundItemManager;
import core.game.node.item.Item;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Graphics;
import core.tools.RandomFunction;

/**
 * Handles the evil chicken NPC.
 * @author Emperor
 */
public final class EvilChickenNPC extends AbstractNPC {

	/**
	 * The messages the evil chicken can use.
	 */
	private static final String[] MESSAGES = { "Bwuk", "Bwuk bwuk bwuk", "Bwaaaaaauk bwuk bwuk", "How dare you invade my lair?", "MUAHAHAHAHAAA!", "Flee from me, @name@!", "Begone, @name@!" };

	/**
	 * The player.
	 */
	protected Player player;

	/**
	 * The random event instance.
	 */
	public EvilChickenRandomEvent event;

	/**
	 * The combat swing handler.
	 */
	private static final CombatSwingHandler HANDLER = new MagicSwingHandler() {

		@Override
		public int swing(Entity entity, Entity victim, BattleState state) {
			entity.sendChat(RandomFunction.getRandomElement(MESSAGES).replace("@name@", victim.getUsername()));
			return super.swing(entity, victim, state);
		}
	};

	/**
	 * Constructs a new {@code EvilChickenNPC} {@code Object}.
	 */
	public EvilChickenNPC() {
		this(2463, null);
	}

	@Override
	public void finalizeDeath(Entity killer) {
		super.finalizeDeath(killer);
		if (killer instanceof Player) {
			GroundItemManager.create(new GroundItem(new Item(526), getLocation(), killer.asPlayer()));
			GroundItemManager.create(new GroundItem(new Item(2138), getLocation(), killer.asPlayer()));
			GroundItemManager.create(new GroundItem(new Item(314, RandomFunction.random(32, 750)), getLocation(), killer.asPlayer()));
		}
	}

	/**
	 * Constructs a new {@code EvilChickenNPC} {@code Object}.
	 * @param id The NPC id.
	 * @param location The location.
	 */
	public EvilChickenNPC(int id, Location location) {
		super(id, location);
	}

	@Override
	public void init() {
		super.init();
		setRespawn(false);
		graphics(Graphics.create(86));
		getProperties().getCombatPulse().attack(player);
	}

	@Override
	public void handleTickActions() {
		if (player == null) {
			return;
		}
		if (!getProperties().getCombatPulse().isAttacking()) {
			getProperties().getCombatPulse().attack(player);
		}
		if (!player.isActive() || !getLocation().withinDistance(player.getLocation(), 10)) {
			clear();
		}
	}

	@Override
	public CombatSwingHandler getSwingHandler(boolean swing) {
		return HANDLER;
	}

	@Override
	public boolean isAttackable(Entity entity, CombatStyle style) {
		if (entity instanceof Player && entity != player) {
			((Player) entity).getPacketDispatch().sendMessage("It's not after you.");
			return false;
		}
		return super.isAttackable(entity, style);
	}

	@Override
	public boolean isIgnoreMultiBoundaries(Entity victim) {
		return victim == player;
	}

	@Override
	public void onRegionInactivity() {
		super.onRegionInactivity();
		clear();
	}

	@Override
	public void clear() {
		super.clear();
		if (event != null) {
			event.terminate();
		}
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new EvilChickenNPC(id, location);
	}

	@Override
	public int[] getIds() {
		return new int[] { 2463, 2464, 2465, 2466, 2467, 2468 };
	}

}