package core.game.interaction.inter;

import api.ContentAPIKt.*;
import core.game.component.Component;
import core.game.component.ComponentDefinition;
import core.game.component.ComponentPlugin;
import core.game.node.entity.skill.LevelUp;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.RunScript;
import rs09.game.world.GameWorld;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the plugin used to handle the skilling tab.
 * @author Vexia
 * @author Splinter
 * @version 1.1
 */
@Initializable
public final class SkillTabInterface extends ComponentPlugin {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ComponentDefinition.put(320, this);
		return this;
	}

	@Override
	public boolean handle(Player p, Component component, int opcode, int button, int slot, int itemId) {
		final SkillConfig config = SkillConfig.forId(button);
		if (config == null) {
			return true;
		}
		if (!GameWorld.getSettings().isPvp()) {
			if (p.getAttribute("levelup:" + config.getSkillId(), false)) {
				p.removeAttribute("levelup:" + config.getSkillId());
				LevelUp.sendFlashingIcons(p, -1);
				p.getConfigManager().set(1230, ADVANCE_CONFIGS[config.getSkillId()]);
				p.getInterfaceManager().open(new Component(741));
			} else {
				p.getPulseManager().clear();
				p.getInterfaceManager().open(new Component(499));
				p.getConfigManager().set(965, config.getConfig());
				p.getAttributes().put("skillMenu", config.getConfig());
			}
		} else {
			if (config.getSkillId() > 6) {
				p.getPacketDispatch().sendMessage("You cannot set a target level for this skill.");
				return false;
			}
			if (p.canSpawn()) {
				p.sendMessage("You must be inside Edgeville bank to set levels.");
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Holds all the config values of the skill advances.
	 */
	public static final int[] ADVANCE_CONFIGS = {
		9, 40, 17, 49,
		25, 57, 33, 641,
		659, 664, 121, 649,
		89, 114, 107, 72,
		64, 80, 673, 680,
		99, 698, 689, 705, 
	};

	public enum SkillConfig {
		ATTACK(125, 1, Skills.ATTACK), 
		STRENGTH(126, 2, Skills.STRENGTH), 
		DEFENCE(127, 5, Skills.DEFENCE), 
		RANGE(128, 3, Skills.RANGE), 
		PRAYER(129, 7, Skills.PRAYER), 
		MAGIC(130, 4, Skills.MAGIC), 
		RUNECRAFTING(131, 12, Skills.RUNECRAFTING), 
		HITPOINTS(133, 6, Skills.HITPOINTS), 
		AGILITY(134, 8, Skills.AGILITY), 
		HERBLORE(135, 9, Skills.HERBLORE), 
		THIEVING(136, 10, Skills.THIEVING), 
		CRAFTING(137, 11, Skills.CRAFTING), 
		FLETCHING(138, 19, Skills.FLETCHING), 
		SLAYER(139, 20, Skills.SLAYER), 
		MINING(141, 13, Skills.MINING), 
		SMITHING(142, 14, Skills.SMITHING), 
		FISHING(143, 15, Skills.FISHING), 
		COOKING(144, 16, Skills.COOKING), 
		FIREMAKING(145, 17, Skills.FIREMAKING),
		WOODCUTTING(146, 18, Skills.WOODCUTTING),
		FARMING(147, 21, Skills.FARMING), 
		CONSTRUCTION(132, 22, Skills.CONSTRUCTION), 
		HUNTER(140, 23, Skills.HUNTER), 
		SUMMONING(148, 24, Skills.SUMMONING);

		/**
		 * Constructs a new {@code SkillConfig} {@code Object}.
		 * @param button the button.
		 * @param config the config.
		 */
		SkillConfig(int button, int config, int skillId) {
			this.button = button;
			this.config = config;
			this.skillId = skillId;
		}

		/**
		 * Represents the button.
		 */
		private int button;

		/**
		 * Represents the config.
		 */
		private int config;

		/**
		 * The skill id.
		 */
		private int skillId;
		
		/**
		 * Gets the skill config.
		 * @param id the id.
		 * @return the skill config.
		 */
		public static SkillConfig forId(int id) {
			for (SkillConfig config : SkillConfig.values()) {
				if (config.button == id)
					return config;
			}
			return null;
		}

		/**
		 * Gets the button.
		 * @return the button.
		 */
		public int getButton() {
			return button;
		}

		/**
		 * Gets the config.
		 * @return the config.
		 */
		public int getConfig() {
			return config;
		}
		
		/**
		 * Gets the skill id.
		 * @return The skill id.
		 */
		public int getSkillId() {
			return skillId;
		}
	}
}
