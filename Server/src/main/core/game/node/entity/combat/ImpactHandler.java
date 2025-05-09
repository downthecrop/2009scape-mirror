package core.game.node.entity.combat;

import content.data.EnchantedJewellery;
import core.game.container.impl.EquipmentContainer;
import core.game.node.entity.skill.Skills;
import content.global.skill.summoning.familiar.Familiar;
import content.global.skill.summoning.pet.Pet;
import core.game.node.entity.Entity;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.prayer.PrayerType;
import core.game.node.item.Item;
import core.game.system.task.Pulse;
import core.game.bots.AIPlayer;
import core.game.world.GameWorld;
import core.game.world.map.zone.ZoneType;
import core.game.world.repository.Repository;
import org.rs09.consts.Items;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * Class used for handling combat impacts.
 * @author Emperor
 */
public final class ImpactHandler {

	/**
	 * The entity.
	 */
	private final Entity entity;

	/**
	 * The amount of ticks impacts have been disabled for.
	 */
	private int disabledTicks;

	/**
	 * The NPC impact log.
	 */
	private final Map<Entity, Integer> npcImpactLog = new HashMap<>();

	/**
	 * The player impact log. This is by player uid to cope with players relogging.
	 */
	private final Map<Integer, Integer> playerImpactLog = new HashMap<>();

	/**
	 * Gets the current hitsplats to show.
	 */
	private final Queue<Impact> impactQueue = new LinkedList<Impact>();

	/**
	 * Constructs a new {@code ImpactHandler} {@code Object}.
	 * @param entity The entity.
	 */
	public ImpactHandler(Entity entity) {
		this.entity = entity;
	}

	/**
	 * Manually hits the entity.
	 * @param source The entity dealing the hit.
	 * @param hit The amount to hit.
	 * @param type The hitsplat type.
	 * @return The impact object.
	 */
	public Impact manualHit(Entity source, int hit, HitsplatType type) {
		if (hit > entity.getSkills().getLifepoints()) {
			hit = entity.getSkills().getLifepoints();
		}
		return handleImpact(source, hit, null, null, type);
	}

	/**
	 * Manually hits the entity.
	 * @param source The entity dealing the hit.
	 * @param hit The amount to hit.
	 * @param type The hitsplat type.
	 * @param ticks The delay before handling the hit.
	 * @return The impact object (or null if a pulse got submitted).
	 */
	public Impact manualHit(final Entity source, int hit, final HitsplatType type, int ticks) {
		if (ticks > 0) {
			final int damage = hit;
			GameWorld.getPulser().submit(new Pulse(ticks, entity) {
				@Override
				public boolean pulse() {
					manualHit(source, damage, type);
					return true;
				}
			});
			return null;
		}
		return manualHit(source, hit, type);
	}

	/**
	 * Handles an impact.
	 * @param source The impact-dealing entity.
	 * @param hit The hit amount.
	 * @param style The combat style used to deal the impact.
	 * @return The impact object created.
	 */
	public Impact handleImpact(Entity source, int hit, CombatStyle style) {
		return handleImpact(source, hit, style, null, null, false);
	}

	/**
	 * Handles an impact.
	 * @param source The impact-dealing entity.
	 * @param hit The hit amount.
	 * @param style The combat style used to deal the impact.
	 * @param state The battle state.
	 * @return The impact object created.
	 */
	public Impact handleImpact(Entity source, int hit, CombatStyle style, BattleState state) {
		return handleImpact(source, hit, style, state, null, false);
	}

	/**
	 * Handles an impact.
	 * @param source The impact-dealing entity.
	 * @param hit The hit amount.
	 * @param style The combat style used to deal the impact.
	 * @param state The battle state.
	 * @param type The hitsplat type.
	 * @return The impact object created.
	 */
	public Impact handleImpact(Entity source, int hit, CombatStyle style, BattleState state, HitsplatType type) {
		return handleImpact(source, hit, style, state, type, false);
	}

	/**
	 * Handles an impact.
	 * @param source The impact-dealing entity.
	 * @param hit The hit amount.
	 * @param style The combat style used to deal the impact.
	 * @param state The battle state.
	 * @param type The hitsplat type.
	 * @return The impact object created.
	 */
	public Impact handleImpact(Entity source, int hit, final CombatStyle style, final BattleState state, HitsplatType type, boolean secondary) {
		if (DeathTask.isDead(this.entity)) {
			this.entity.getProperties().getCombatPulse().setVictim(null);
			this.entity.getProperties().getCombatPulse().stop();
			return null;
		}
		boolean fam = source instanceof Familiar;
		if (fam) {
			source = ((Familiar) source).getOwner();
		}
		if (disabledTicks > GameWorld.getTicks()) {
			return null;
		}
		if (entity instanceof Player && !(entity instanceof AIPlayer) && !(entity.getAttribute("tutorial:complete",false))) {
			Impact impact = new Impact(source, 0, style, HitsplatType.MISS);
			impactQueue.add(impact);
			return impact;
		}
		hit -= entity.getSkills().hit(hit);
		if (type == null || type == HitsplatType.NORMAL) {
			if (hit == 0) {
				type = HitsplatType.MISS;
			} else {
				type = HitsplatType.NORMAL;
			}
		} else if (hit == 0 && type == HitsplatType.POISON) {
			return null;
		}
		if (hit > 0) {
			if (source instanceof Player) {
				int uid = source.asPlayer().getDetails().getUid();
				Integer value = playerImpactLog.get(uid);
				playerImpactLog.put(uid, value == null ? hit : hit + value);
			} else {
				Integer value = npcImpactLog.get(source);
				npcImpactLog.put(source, value == null ? hit : hit + value);
			}
		}
		if (style != null && style.getSwingHandler() != null && source instanceof Player) {
			Player player = source.asPlayer();
			if (fam && player.getFamiliarManager().hasFamiliar() && !(player.getFamiliarManager().getFamiliar() instanceof Pet)) {
				source.setAttribute("fam-exp", true);
			}
			source.removeAttribute("fam-exp");
		}
		boolean dead = false;
		if (entity.getSkills().getLifepoints() < 1) {
			entity.getProperties().getCombatPulse().stop();
			entity.face(source);
			entity.startDeath(getMostDamageEntity(source));
			dead = true;
		} else if (entity.getSkills().getLifepoints() < (entity.getSkills().getMaximumLifepoints() * 0.1)) {
			if (entity instanceof Player && ((Player) entity).getPrayer().get(PrayerType.REDEMPTION)) {
				((Player) entity).getPrayer().startRedemption();
			}
		}
		if (entity instanceof Player) {
			Player p = entity.asPlayer();
			if (p.getAttribute("godMode", false)) {
				p.getSkills().heal(10000);
			}
		}
		Impact impact = new Impact(source, hit, style, type);
		impactQueue.add(impact);
		if (entity instanceof Player && !dead) {
			final Player p = entity.asPlayer();
			if (p.getZoneMonitor().getType() != ZoneType.SAFE.getId() && (p.getEquipment().contains(Items.RING_OF_LIFE_2570, 1))) {
				int percentage = (int) (entity.getSkills().getStaticLevel(Skills.HITPOINTS) * 0.10);
				if (p.getSkills().getLifepoints() <= percentage) {
					Item rolItem = new Item(Items.RING_OF_LIFE_2570);
					if (EnchantedJewellery.RING_OF_LIFE.attemptTeleport(p, rolItem, 0, true)) {
						p.sendMessage("Your ring of life saves you and in the process is destroyed.");
					}
				}
			}
		}
		return impact;
	}

	/**
	 * Handles the recoil effect.
	 * @param attacker The attacker.
	 * @param hit The hit to handle.
	 */
	public void handleRecoilEffect(Entity attacker, int hit) {
		int damage = (int) Math.ceil(hit * 0.1);
		if (entity instanceof Player) {
			Player player = (Player) entity;
			int current = player.getSavedData().getGlobalData().getRecoilDamage();
			if (damage >= current) {
				damage = current;
				player.getPacketDispatch().sendMessage("Your Ring of Recoil has shattered.");
				player.getEquipment().replace(null, EquipmentContainer.SLOT_RING);
				player.getSavedData().getGlobalData().setRecoilDamage(40);
			} else {
				player.getSavedData().getGlobalData().setRecoilDamage(current - damage);
			}
		}
		if (damage > 0) {
			attacker.getImpactHandler().manualHit(entity, damage, HitsplatType.NORMAL);
		}
	}

	/**
	 * Gets the player who's dealt the most damage.
	 * @param killer The killer.
	 * @return The player.
	 */
	public Entity getMostDamageEntity(Entity killer) {
		Entity entity = this.entity;
		if (entity instanceof Player) {
			return killer;
		}
		if (killer instanceof AIPlayer) return killer;

		int damage = -1;
		if (playerImpactLog.isEmpty()) {
			for (Entity e : npcImpactLog.keySet()) {
				if (e == this.entity) {
					continue;
				}
				int amount = npcImpactLog.get(e);
				if (amount > damage) {
					damage = amount;
					entity = e;
				}
			}
			return entity;
		}

		int player = 0; //needs to be fake-initialized because java is dumb
		for (int uid : playerImpactLog.keySet()) {
			int amount = playerImpactLog.get(uid);
			if (amount > damage) {
				damage = amount;
				player = uid;
			}
		}
		return Repository.getPlayerByUid(player);
	}

	/**
	 * Gets the npc impact log.
	 * @return The npc impact log.
	 */
	public Map<Entity, Integer> getNpcImpactLog() {
		return npcImpactLog;
	}

	/**
	 * Gets the player impact log.
	 * @return The player impact log.
	 */
	public Map<Integer, Integer> getPlayerImpactLog() {
		return playerImpactLog;
	}

	/**
	 * Checks if the entity needs a hit update.
	 * @return {@code True} if so.
	 */
	public boolean isHitUpdate() {
		return impactQueue.peek() != null;
	}

	/**
	 * Gets the hitsplats.
	 * @return The hitsplats.
	 */
	public Queue<Impact> getImpactQueue() {
		return impactQueue;
	}

	/**
	 * Gets the disabledTicks.
	 * @return The disabledTicks.
	 */
	public int getDisabledTicks() {
		return disabledTicks;
	}

	/**
	 * Sets the disabledTicks.
	 */
	public void setDisabledTicks(int ticks) {
		this.disabledTicks = GameWorld.getTicks() + ticks;
	}

	/**
	 * Represents an impact.
	 * @author Emperor
	 */
	public static class Impact {

		/**
		 * The impact-dealing entity.
		 */
		private final Entity source;

		/**
		 * The hit amount.
		 */
		private final int amount;

		/**
		 * The combat style used to deal the hit.
		 */
		private final CombatStyle style;

		/**
		 * The hitsplat type.
		 */
		private final HitsplatType type;

		/**
		 * Constructs a new {@code ImpactHandler} {@code Object}.
		 * @param source The impact-dealing entity.
		 * @param amount The hit amount.
		 * @param style The combat style used to deal the hit.
		 * @param type The hitsplat type.
		 */
		public Impact(Entity source, int amount, CombatStyle style, HitsplatType type) {
			this.source = source;
			this.amount = amount;
			this.style = style;
			this.type = type;
		}

		/**
		 * Gets the source.
		 * @return The source.
		 */
		public Entity getSource() {
			return source;
		}

		/**
		 * Gets the amount.
		 * @return The amount.
		 */
		public int getAmount() {
			return amount;
		}

		/**
		 * Gets the style.
		 * @return The style.
		 */
		public CombatStyle getStyle() {
			return style;
		}

		/**
		 * Gets the type.
		 * @return The type.
		 */
		public HitsplatType getType() {
			return type;
		}
	}

	/**
	 * Represents the hitsplat types.
	 * @author Emperor
	 */
	public static enum HitsplatType {
		MISS, NORMAL, POISON, DISEASE, NORMAL_1, VENOM;
	}
}
