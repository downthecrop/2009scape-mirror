package content.global.skill.summoning.familiar;

import core.game.node.entity.skill.Skills;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Plugin;
import core.plugin.Initializable;
import core.plugin.ClassScanner;

/**
 * Handles the loading of a cockatrice familiar.
 * @author Vexia
 */
@Initializable
public final class CockatriceFamiliarNPC implements Plugin<Object> {

	/**
	 * The cockatrice egg.
	 */
	private static final Item COCKATRICE_EGG = new Item(12109);

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ClassScanner.definePlugin(new SpiritCockatrice());
		ClassScanner.definePlugin(new SpiritGuthatrice());
		ClassScanner.definePlugin(new SpiritZamatrice());
		ClassScanner.definePlugin(new SpiritPengatrice());
		ClassScanner.definePlugin(new SpiritCoraxatrice());
		ClassScanner.definePlugin(new SpiritVulatrice());
		ClassScanner.definePlugin(new SpiritSaratrice());
		return this;
	}

	@Override
	public Object fireEvent(String identifier, Object... args) {
		return null;
	}

	/**
	 * Handles a petrified gaze.
	 * @param familiar the familiar.
	 * @param special the special.
	 * @return {@code True} if so.
	 */
	public boolean petrifyingGaze(final Familiar familiar, final FamiliarSpecial special, final int skill) {
		final Entity target = special.getTarget();
		if (!familiar.canCombatSpecial(target)) {
			return false;
		}
		familiar.faceTemporary(target, 2);
		familiar.visualize(Animation.create(7762), Graphics.create(1467));
		GameWorld.getPulser().submit(new Pulse(1, familiar.getOwner(), familiar, target) {
			@Override
			public boolean pulse() {
				if(skill == 5) {
					target.skills.decrementPrayerPoints(3);
				}else {
					target.getSkills().updateLevel(skill, -3, 0);
				}
				Projectile.magic(familiar, target, 1468, 40, 36, 71, 10).send();
				familiar.sendFamiliarHit(target, 10, Graphics.create(1469));
				return true;
			}
		});
		return true;
	}

	/**
	 * Handles the spirit cockatrice.
	 * @author Vexia
	 */
	public final class SpiritCockatrice extends Forager {

		/**
		 * Constructs a new {@code SpiritCockatrice} {@code Object}.
		 */
		public SpiritCockatrice() {
			this(null, 6875);
		}

		/**
		 * Constructs a new {@code SpiritCockatrice} {@code Object}.
		 * @param owner the owner.
		 * @param id the id.
		 */
		public SpiritCockatrice(Player owner, int id) {
			super(owner, id, 3600, 12095, 3, WeaponInterface.STYLE_CAST, COCKATRICE_EGG);
		}

		@Override
		public Familiar construct(Player owner, int id) {
			return new SpiritCockatrice(owner, id);
		}

		@Override
		protected boolean specialMove(FamiliarSpecial special) {
			return petrifyingGaze(this, special, Skills.DEFENCE);
		}

		@Override
		public int[] getIds() {
			return new int[] { 6875, 6876 };
		}

	}

	/**
	 * Handles the spirit guthatrice.
	 * @author Vexia
	 */
	public class SpiritGuthatrice extends Forager {

		/**
		 * Constructs a new {@code SpiritGuthatrice} {@code Object}.
		 */
		public SpiritGuthatrice() {
			this(null, 6877);
		}

		/**
		 * Constructs a new {@code SpiritGuthatrice} {@code Object}.
		 * @param owner the owner.
		 * @param id the id.
		 */
		public SpiritGuthatrice(Player owner, int id) {
			super(owner, id, 3600, 12097, 3, WeaponInterface.STYLE_CAST, COCKATRICE_EGG);
		}

		@Override
		public Familiar construct(Player owner, int id) {
			return new SpiritGuthatrice(owner, id);
		}

		@Override
		protected boolean specialMove(FamiliarSpecial special) {
			return petrifyingGaze(this, special, Skills.ATTACK);
		}

		@Override
		public int[] getIds() {
			return new int[] { 6877, 6878 };
		}
	}

	/**
	 * Handles the spirit zamatrice.
	 * @author Vexia
	 */
	public class SpiritZamatrice extends Forager {

		/**
		 * Constructs a new {@code SpiritZamatrice} {@code Object}.
		 */
		public SpiritZamatrice() {
			this(null, 6881);
		}

		/**
		 * Constructs a new {@code SpiritZamatrice} {@code Object}.
		 * @param owner the owner.
		 * @param id the id.
		 */
		public SpiritZamatrice(Player owner, int id) {
			super(owner, id, 3600, 12101, 3, WeaponInterface.STYLE_CAST, COCKATRICE_EGG);
		}

		@Override
		public Familiar construct(Player owner, int id) {
			return new SpiritZamatrice(owner, id);
		}

		@Override
		protected boolean specialMove(FamiliarSpecial special) {
			return petrifyingGaze(this, special, Skills.STRENGTH);
		}

		@Override
		public int[] getIds() {
			return new int[] { 6881, 6882 };
		}
	}

	/**
	 * Handles the spirit pengatrice.
	 * @author Vexia
	 */
	public class SpiritPengatrice extends Forager {

		/**
		 * Constructs a new {@code SpiritPengatrice} {@code Object}.
		 */
		public SpiritPengatrice() {
			this(null, 6883);
		}

		/**
		 * Constructs a new {@code SpiritPengatrice} {@code Object}.
		 * @param owner the owner.
		 * @param id the id.
		 */
		public SpiritPengatrice(Player owner, int id) {
			super(owner, id, 3600, 12103, 3, WeaponInterface.STYLE_CAST, COCKATRICE_EGG);
		}

		@Override
		public Familiar construct(Player owner, int id) {
			return new SpiritPengatrice(owner, id);
		}

		@Override
		protected boolean specialMove(FamiliarSpecial special) {
			return petrifyingGaze(this, special, Skills.MAGIC);
		}

		@Override
		public int[] getIds() {
			return new int[] { 6883, 6884 };
		}
	}

	/**
	 * Represents the Spirit Coraxatrice familiar.
	 * @author Aero
	 */
	public class SpiritCoraxatrice extends Forager {

		/**
		 * Constructs a new {@code SpiritCoraxatriceNPC} {@code Object}.
		 */
		public SpiritCoraxatrice() {
			this(null, 6885);
		}

		/**
		 * Constructs a new {@code SpiritCoraxatriceNPC} {@code Object}.
		 * @param owner The owner.
		 * @param id The id.
		 */
		public SpiritCoraxatrice(Player owner, int id) {
			super(owner, id, 3600, 12105, 3, WeaponInterface.STYLE_CAST, COCKATRICE_EGG);
		}

		@Override
		public Familiar construct(Player owner, int id) {
			return new SpiritCoraxatrice(owner, id);
		}

		@Override
		protected boolean specialMove(FamiliarSpecial special) {
			return petrifyingGaze(this, special, Skills.SUMMONING);
		}

		@Override
		public int[] getIds() {
			return new int[] { 6885, 6886 };
		}

	}

	/**
	 * Represents the Spirit Vulatrice familiar.
	 * @author Aero
	 */
	public class SpiritVulatrice extends Forager {

		/**
		 * Constructs a new {@code SpiritVulatriceNPC} {@code Object}.
		 */
		public SpiritVulatrice() {
			this(null, 6887);
		}

		/**
		 * Constructs a new {@code SpiritVulatriceNPC} {@code Object}.
		 * @param owner The owner.
		 * @param id The id.
		 */
		public SpiritVulatrice(Player owner, int id) {
			super(owner, id, 3600, 12107, 3, WeaponInterface.STYLE_CAST, COCKATRICE_EGG);
		}

		@Override
		public Familiar construct(Player owner, int id) {
			return new SpiritVulatrice(owner, id);
		}

		@Override
		protected boolean specialMove(FamiliarSpecial special) {
			return petrifyingGaze(this, special, Skills.RANGE);
		}

		@Override
		public int[] getIds() {
			return new int[] { 6887, 6888 };
		}

	}

	/**
	 * Represents the Spirit Saratrice familiar.
	 * @author Aero - DeadlyGenga
	 */
	public class SpiritSaratrice extends Forager {

		/**
		 * Constructs a new {@code SpiritSaratriceNPC} {@code Object}.
		 */
		public SpiritSaratrice() {
			this(null, 6879);
		}

		/**
		 * Constructs a new {@code SpiritSaratriceNPC} {@code Object}.
		 * @param owner The owner.
		 * @param id The id.
		 */
		public SpiritSaratrice(Player owner, int id) {
			super(owner, id, 3600, 12099, 3, WeaponInterface.STYLE_CAST, COCKATRICE_EGG);
		}

		@Override
		public Familiar construct(Player owner, int id) {
			return new SpiritSaratrice(owner, id);
		}

		@Override
		protected boolean specialMove(FamiliarSpecial special) {
			return petrifyingGaze(this, special, Skills.PRAYER);
		}

		@Override
		public int[] getIds() {
			return new int[] { 6879, 6880 };
		}

	}
}
