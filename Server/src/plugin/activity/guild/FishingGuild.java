package plugin.activity.guild;

import org.crandor.cache.def.impl.ObjectDefinition;
import org.crandor.game.content.dialogue.DialoguePlugin;
import org.crandor.game.content.global.Skillcape;
import org.crandor.game.content.global.action.DoorActionHandler;
import org.crandor.game.content.skill.Skills;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.object.GameObject;
import org.crandor.game.world.map.Location;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the plugin used for the fishing guild.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class FishingGuild extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ObjectDefinition.forId(2025).getConfigurations().put("option:open", this);
		new MasterFisherDialogue().init();
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		final int id = ((GameObject) node).getId();
		switch (option) {
		case "open":
			switch (id) {
			case 2025:
				if (player.getSkills().getStaticLevel(Skills.FISHING) < 68 && player.getLocation().withinDistance(new Location(2611, 3394, 0))) {
					player.getDialogueInterpreter().sendDialogues(308, null, "Hello, I'm afraid only the top fishers are allowed to use", "our premier fishing facilities.");
					return true;
				}
				DoorActionHandler.handleAutowalkDoor(player, (GameObject) node);
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
