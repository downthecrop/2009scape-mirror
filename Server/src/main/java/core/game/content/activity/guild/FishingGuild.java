package core.game.content.activity.guild;

import core.cache.def.impl.SceneryDefinition;
import core.plugin.Initializable;
import core.game.content.dialogue.DialoguePlugin;
import core.game.content.global.Skillcape;
import core.game.content.global.action.DoorActionHandler;
import core.game.node.entity.skill.Skills;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.game.world.map.Location;
import core.plugin.Plugin;

/**
 * Represents the plugin used for the fishing guild.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class FishingGuild extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(2025).getHandlers().put("option:open", this);
		new MasterFisherDialogue().init();
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		switch (option) {
		case "open":
			switch (node.getId()) {
			case 2025:
				if (player.getSkills().getDynamicLevels()[Skills.FISHING] < 68 && player.getLocation().withinDistance(new Location(2611, 3394, 0))) {
					player.getDialogueInterpreter().sendDialogues(308, null, "Hello, I'm afraid only the top fishers are allowed to use", "our premier fishing facilities.");
					return true;
				}
				DoorActionHandler.handleAutowalkDoor(player, (Scenery) node);
				break;
			}
			break;
		}
		return true;
	}

	/**
	 * Represents the master fisher dialogue.
	 * @author 'Vexia
	 * @version 1.0
	 */
	public final class MasterFisherDialogue extends DialoguePlugin {

		/**
		 * Constructs a new {@code MasterFisherDialogue} {@code Object}.
		 */
		public MasterFisherDialogue() {
			/**
			 * empty.
			 */
		}

		/**
		 * Constructs a new {@code MasterFisherDialogue} {@code Object}.
		 * @param player the player.
		 */
		public MasterFisherDialogue(final Player player) {
			super(player);
		}

		@Override
		public DialoguePlugin newInstance(Player player) {
			return new MasterFisherDialogue(player);
		}

		@Override
		public boolean open(Object... args) {
			if (!Skillcape.isMaster(player, Skills.FISHING)) {
				npc("Hello, I'm afraid only the top fishers are allowed to use", "our premier fishing facilities.");
			} else {
				npc("Hello, only the top fishers are allowed to use", "our premier fishing facilities and you seem", "to meet the criteria. Enjoy!");
			}
			stage = 0;
			return true;
		}

		@Override
		public boolean handle(int interfaceId, int buttonId) {
			switch (stage) {
			case 0:
				if (Skillcape.isMaster(player, Skills.FISHING)) {
					player("Can I buy a Skillcape of Fishing?");
					stage = 4;
				} else {
					player("Can you tell me about that skillcape you're wearing?");
					stage = 1;
				}
				break;
			case 1:
				npc("I'm happy to, my friend. This beautiful cape was", "presented to me in recognition of my skills and", "experience as a fisherman and I was asked to be the", "head of this guild at the same time. As the best");
				stage = 2;
				break;
			case 2:
				npc("fisherman in the guild it is my duty to control who has", "access to the guild and to say who can buy similar", "skillcapes.");
				stage = 3;
				break;
			case 3:
				end();
				break;
			case 4:
				npc("Certainly! Right when you pay me 99000 coins.");
				stage = 5;
				break;
			case 5:
				options("Okay, here you go.", "No, thanks.");
				stage = 6;
				break;
			case 6:
				switch (buttonId) {
				case 1:
					player("Okay, here you go.");
					stage = 7;
					break;
				case 2:
					end();
					break;
				}
				break;
			case 7:
				if (Skillcape.purchase(player, Skills.FISHING)) {
					npc("There you go! Enjoy.");
				}
				stage = 8;

				break;
			case 8:
				end();
				break;
			}
			return true;
		}

		@Override
		public int[] getIds() {
			return new int[] { 308 };
		}

	}
}
