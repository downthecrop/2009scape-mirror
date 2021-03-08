package core.game.node.entity.combat.equipment;

import core.game.container.impl.EquipmentContainer;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.Entity;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.state.EntityState;
import core.game.node.item.Item;

/**
 * Represents the spell types.
 * @author Emperor
 */
public enum SpellType {

	/**
	 * The strike spell type.
	 */
	STRIKE(1.0) {
		@Override
		public int getImpactAmount(Entity e, Entity victim, int base) {
			if (victim instanceof NPC && ((NPC) victim).getId() == 205) {
				return 8 + base;
			}
			return 2 * base;
		}
	},

	/**
	 * The bolt spell type.
	 */
	BOLT(1.1) {
		@Override
		public int getImpactAmount(Entity e, Entity victim, int base) {
			if (e instanceof Player && ((Player) e).getEquipment().getNew(EquipmentContainer.SLOT_HANDS).getId() == 777) {
				return 11 + base;
			}
			return 8 + base;
		}
	},

	/**
	 * Crumble undead.
	 */
	CRUMBLE_UNDEAD(1.2) {
		@Override
		public int getImpactAmount(Entity e, Entity victim, int base) {
			return 15; // Hits as high as Earth blast
		}
	},

	/**
	 * The blast spell type.
	 */
	BLAST(1.2) {
		@Override
		public int getImpactAmount(Entity e, Entity victim, int base) {
			return 12 + base;
		}
	},

	/**
	 * The wave spell type.
	 */
	WAVE(1.3) {
		@Override
		public int getImpactAmount(Entity e, Entity victim, int base) {
			return 16 + base;
		}
	},

	/**
	 * The rush spell type.
	 */
	RUSH(1.1) {
		@Override
		public int getImpactAmount(Entity e, Entity victim, int base) {
			return 14 + base;
		}
	},

	/**
	 * The burst spell type.
	 */
	BURST(1.2) {
		@Override
		public int getImpactAmount(Entity e, Entity victim, int base) {

			return 18 + base;
		}
	},

	/**
	 * The blitz spell type.
	 */
	BLITZ(1.3) {
		@Override
		public int getImpactAmount(Entity e, Entity victim, int base) {
			return 22 + base;
		}
	},

	/**
	 * The barrage spell type.
	 */
	BARRAGE(1.4) {
		@Override
		public int getImpactAmount(Entity e, Entity victim, int base) {
			System.out.println("Attacker - >" + e.getName());
			System.out.println("Victim - >" + victim.getName());
			System.out.println("Accuracy Mod -> " + getAccuracyMod());
			System.out.println("Base Mod -> " + base);
			return 26 + base;
		}
	},

	/**
	 * The confuse spell type.
	 */
	CONFUSE(1.15),

	/**
	 * The weaken spell type.
	 */
	WEAKEN(1.15),

	/**
	 * The curse spell type.
	 */
	CURSE(1.15),

	/**
	 * The vulnerability spell type.
	 */
	VULNERABILITY(1.25),

	/**
	 * The enfeeble spell type.
	 */
	ENFEEBLE(1.25),

	/**
	 * The stun spell type.
	 */
	STUN(1.25),

	/**
	 * The god strike spell type.
	 */
	GOD_STRIKE(1.2) {
		@Override
		public int getImpactAmount(Entity e, Entity victim, int base) {
			if(!(e instanceof Player)) return 20;
			if (e.asPlayer().hasActiveState("godcharge")) {
				Item cape = ((Player) e).getEquipment().getNew(EquipmentContainer.SLOT_CAPE);
				if (cape.getId() == 2412 || cape.getId() == 2413 || cape.getId() == 2414) {
					return 30;
				}
			}
			return 20;
		}
	},

	/**
	 * The bind spell type.
	 */
	BIND(1.1),

	/**
	 * The snare spell type.
	 */
	SNARE(1.2),

	/**
	 * The entangle spell type.
	 */
	ENTANGLE(1.3),

	/**
	 * The magic dart spell type.
	 */
	MAGIC_DART(1.15) {
		@Override
		public int getImpactAmount(Entity e, Entity victim, int base) {
			return 10 + (e.getSkills().getLevel(Skills.MAGIC) / 10);
		}
	},

	/**
	 * The iban blast spell type.
	 */
	IBANS_BLAST(1.4) {
		@Override
		public int getImpactAmount(Entity e, Entity victim, int base) {
			return 25;
		}
	},
	
	/**
	 * The trident of seas built in spell.
	 */
	TRIDENT_OF_SEAS(1.4) {
		@Override
		public int getImpactAmount(Entity e, Entity victim, int base) {
			Player player = e.asPlayer();
			int level = player.getSkills().getLevel(Skills.MAGIC);
			int offset = (level - 75) / 3;
			return offset + base;
		}
	},
	
	/**
	 * The teleporation block spell type.
	 */
	TELEBLOCK(1.3),

	/**
	 * The null spell type.
	 */
	NULL(0.0);

	/**
	 * The accuracy modifier.
	 */
	private final double accuracyMod;

	/**
	 * Constructs a new {@code SpellType} {@Code Object}.
	 * @param accuracyMod The accuracy modifier.
	 */
	private SpellType(double accuracyMod) {
		this.accuracyMod = accuracyMod;
	}

	/**
	 * Gets the base damage for this spell.
	 * @param e The entity casting this spell.
	 * @param victim The entity being hit.
	 * @param base The base damage for the spell element.
	 * @return The base damage for this spell.
	 */
	public int getImpactAmount(Entity e, Entity victim, int base) {
		return 2;
	}

	/**
	 * Gets the accuracy modifier.
	 * @return The accuracy modifier.
	 */
	public double getAccuracyMod() {
		return accuracyMod;
	}

}