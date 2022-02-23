package core.game.content.ttrail;

import core.game.node.entity.Entity;
import core.game.node.entity.combat.CombatSpell;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.combat.equipment.SwitchAttack;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.SpellBookManager.SpellBook;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.plugin.Plugin;
import rs09.game.node.entity.combat.CombatSwingHandler;
import rs09.game.node.entity.combat.handlers.MultiSwingHandler;

/**
 * Handles saradomin npc.
 * @author Vexia
 */
public final class SaradominWizardNPC extends AbstractNPC {

	/**
	 * The combat handler to use.
	 */
	private static final MultiSwingHandler COMBAT_HANDLER = new MultiSwingHandler(new SwitchAttack(CombatStyle.MELEE).setUseHandler(true), new SwitchAttack(CombatStyle.MAGIC).setUseHandler(true));

	/**
	 * The npc ids.
	 */
	private static final int[] IDS = new int[] { 1264 };

	/**
	 * The clue scroll.
	 */
	private ClueScrollPlugin clueScroll;

	/**
	 * The player.
	 */
	private Player player;

	/**
	 * Constructs a new {@code SaradominWizardNPC} {@code Object}
	 */
	public SaradominWizardNPC() {
		super(0, null);
	}

	/**
	 * Constructs a new {@Code SaradominWizardNPC} {@Code Object}
	 * @param id the id.
	 * @param location the location.
	 */
	public SaradominWizardNPC(int id, Location location) {
		super(id, location, false);
		this.setRespawn(false);
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new SaradominWizardNPC(id, location);
	}

	@Override
	public void init() {
		Player player = getAttribute("player", null);
		ClueScrollPlugin clueScroll = getAttribute("clue", null);
		if (player != null) {
			this.player = player;
		}
		if (clueScroll != null) {
			this.clueScroll = clueScroll;
		}
		if (player != null) {
			location = RegionManager.getSpawnLocation(player, this);
			if (location == null) {
				location = player.getLocation();
			}
		}
		super.init();
		getProperties().setSpell((CombatSpell) SpellBook.MODERN.getSpell(41));
		getProperties().setAutocastSpell((CombatSpell) SpellBook.MODERN.getSpell(41));
	}

	@Override
	public void finalizeDeath(Entity killer) {
		if (killer instanceof Player) {
			Player p = killer.asPlayer();
			if (p == player) {
				p.setAttribute("killed-wizard", clueScroll.getClueId());
			}
		}
		super.finalizeDeath(killer);
	}

	@Override
	public void handleTickActions() {
		super.handleTickActions();
		if (player != null) {
			if (player.getLocation().getDistance(getLocation()) > 10 || !player.isActive()) {
				clear();
			}
			if (!getProperties().getCombatPulse().isAttacking()) {
				getProperties().getCombatPulse().attack(player);
			}
		}
	}

	@Override
	public CombatSwingHandler getSwingHandler(boolean swing) {
		return COMBAT_HANDLER;
	}

	@Override
	public boolean canAttack(Entity entity) {
		if (!(entity instanceof Player)) {
			return false;
		}
		if (player != null) {
			Player p = entity.asPlayer();
			return p == player;
		}
		return super.canAttack(entity);
	}

	@Override
	public boolean isAttackable(Entity entity, CombatStyle style, boolean message) {
		if (!(entity instanceof Player)) {
			return false;
		}
		if (player != null) {
			Player p = entity.asPlayer();
			return p == player;
		}
		return super.isAttackable(entity, style, message);
	}

	@Override
	public boolean canSelectTarget(Entity target) {
		return target == player;

	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		return super.newInstance(arg);
	}

	@Override
	public int[] getIds() {
		return IDS;
	}

	/**
	 * Gets the bplayer.
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Sets the baplayer.
	 * @param player the player to set.
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * Gets the bclueScroll.
	 * @return the clueScroll
	 */
	public ClueScrollPlugin getClueScroll() {
		return clueScroll;
	}

}
