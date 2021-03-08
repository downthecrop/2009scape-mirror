package core.game.node.entity.combat.equipment;

import core.game.node.entity.skill.Skills;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Graphics;
import core.tools.RandomFunction;

/**
 * Represents the types of armour sets.
 * @author Emperor
 */
public enum ArmourSet {

	/**
	 * Ahrim the blighted barrows set.
	 */
	AHRIM(new Graphics(401, 96), new int[][] { { 4708, 4856, 4857, 4858, 4859 }, { 4710, 4862, 4863, 4864, 4865 }, { 4712, 4868, 4869, 4870, 4871 }, { 4714, 4874, 4875, 4876, 4877 } }) {
		@Override
		public boolean effect(Entity e, Entity victim, BattleState state) {
			if (RandomFunction.random(100) < 20) {
				victim.getSkills().updateLevel(Skills.STRENGTH, -5, 0);
				return true;
			}
			return false;
		}

		@Override
		public boolean isUsing(Entity e) {
			if (e instanceof NPC && ((NPC) e).getId() == 2025) {
				return true;
			}
			return super.isUsing(e);
		}
	},

	/**
	 * Dharok the wretched barrows set.
	 */
	DHAROK(null, new int[][] { { 4716, 4880, 4881, 4882, 4883 }, { 4718, 4886, 4887, 4888, 4889 }, { 4720, 4892, 4893, 4894, 4895 }, { 4722, 4898, 4899, 4900, 4901 } }) {
		@Override
		public boolean isUsing(Entity e) {
			if (e instanceof NPC && ((NPC) e).getId() == 2026) {
				return true;
			}
			return super.isUsing(e);
		}
	},

	/**
	 * Guthan the infested barrows set.
	 */
	GUTHAN(new Graphics(398, 96), new int[][] { { 4724, 4904, 4905, 4906, 4907 }, { 4726, 4910, 4911, 4912, 4913 }, { 4728, 4916, 4917, 4918, 4919 }, { 4730, 4922, 4923, 4924, 4925 } }) {
		@Override
		public boolean effect(Entity e, Entity victim, BattleState state) {
			if (RandomFunction.random(100) < 25) {
				e.getSkills().heal(state.getEstimatedHit());
				return true;
			}
			return false;
		}

		@Override
		public boolean isUsing(Entity e) {
			if (e instanceof NPC && ((NPC) e).getId() == 2027) {
				return true;
			}
			return super.isUsing(e);
		}
	},

	/**
	 * Karil the tainted barrows set.
	 */
	KARIL(new Graphics(400, 96), new int[][] { { 4732, 4928, 4929, 4930, 4931 }, { 4734, 4934, 4935, 4936, 4937 }, { 4736, 4940, 4941, 4942, 4943 }, { 4738, 4946, 4947, 4948, 4949 } }) {
		@Override
		public boolean effect(Entity e, Entity victim, BattleState state) {
			if (state.getEstimatedHit() > 9 && RandomFunction.random(100) < 20) {
				victim.getSkills().updateLevel(Skills.AGILITY, -(state.getEstimatedHit() / 10), 0);
				return true;
			}
			return false;
		}

		@Override
		public boolean isUsing(Entity e) {
			if (e instanceof NPC && ((NPC) e).getId() == 2028) {
				return true;
			}
			return super.isUsing(e);
		}
	},

	/**
	 * Torag the corrupted barrows set.
	 */
	TORAG(new Graphics(399, 96), new int[][] { { 4745, 4952, 4953, 4954, 4955 }, { 4747, 4958, 4959, 4960, 4961 }, { 4749, 4964, 4965, 4966, 4967 }, { 4751, 4970, 4971, 4972, 4973 } }) {
		@Override
		public boolean effect(Entity e, Entity victim, BattleState state) {
			if (state.getEstimatedHit() > 0 && RandomFunction.random(100) < 20) {
				if (victim instanceof Player) {
					((Player) victim).getSettings().updateRunEnergy(20);
				}
				return true;
			}
			return false;
		}

		@Override
		public boolean isUsing(Entity e) {
			if (e instanceof NPC && ((NPC) e).getId() == 2029) {
				return true;
			}
			return super.isUsing(e);
		}
	},

	/**
	 * Verac the defiled barrows set.
	 */
	VERAC(null, new int[][] { { 4753, 4976, 4977, 4978, 4979 }, { 4755, 4982, 4983, 4984, 4985 }, { 4757, 4988, 4989, 4990, 4991 }, { 4759, 4994, 4995, 4996, 4997 } }) {
		@Override
		public boolean isUsing(Entity e) {
			if (e instanceof NPC && ((NPC) e).getId() == 2030) {
				return true;
			}
			return super.isUsing(e);
		}
	};

	/**
	 * The end graphic.
	 */
	private final Graphics endGraphic;

	/**
	 * The items.
	 */
	private final Item[][] sets;

	/**
	 * Constructs a new {@code ArmourSet} {@code Object}.
	 * @param endGraphic the end.
	 * @param set the set.
	 */
	private ArmourSet(Graphics endGraphic, int[][] set) {
		this.endGraphic = endGraphic;
		this.sets = new Item[4][5];
		for (int i = 0; i < set.length; i++) {
			for (int k = 0; k < sets[i].length; k++) {
				sets[i][k] = new Item(set[i][k]);
			}
		}
	}

	/**
	 * Visualizes the effect.
	 * @param e The entity using the effect.
	 * @param victim The victim.
	 */
	public void visualize(Entity e, Entity victim) {
		if (endGraphic != null) {
			victim.graphics(endGraphic);
		}
	}

	/**
	 * Handles the effect of the armour set.
	 * @param e The entity using the effect.
	 * @param victim The victim.
	 * @param state The battle state.
	 * @return {@code True} if an effect occured.
	 */
	public boolean effect(Entity e, Entity victim, BattleState state) {
		return false;
	}

	/**
	 * Checks if the entity is wearing this armour set.
	 * @param e The entity.
	 * @return {@code True} if so.
	 */
	public boolean isUsing(Entity e) {
		if (!(e instanceof Player)) {
			return false;
		}
		Player p = (Player) e;
		int hits = 0;
		for (int i = 0; i < sets.length; i++) {
			for (Item item : sets[i]) {
				if (p.getEquipment().containsAtLeastOneItem(item.getId())) {
					hits++;
					break;
				}
			}
		}
		return hits == 4;
	}

}