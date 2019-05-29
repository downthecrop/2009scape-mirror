package plugin.dialogue;

import org.crandor.game.content.dialogue.DialoguePlugin;
import org.crandor.game.content.skill.member.herblore.HerbTarPulse;
import org.crandor.game.content.skill.member.herblore.Tars;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.RunScript;
import org.crandor.net.packet.PacketRepository;
import org.crandor.net.packet.context.ChildPositionContext;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.net.packet.out.RepositionChild;

/**
 * Represents the dialogue used to determine the amount of a herb tar to make.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class HerbTarDialogue extends DialoguePlugin {

	/**
	 * Represents this dialogues id.
	 */
	public static final int ID = 2827673;

	/**
	 * Represents the tar to make.
	 */
	private Tars tar;

	/**
	 * Constructs a new {@code HerbloreDialogue} {@code Object}.
	 */
	public HerbTarDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code HerbloreDialogue} {@code Object}.
	 * @param player the player.
	 */
	public HerbTarDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new HerbTarDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		tar = (Tars) args[0];
		player.getInterfaceManager().openChatbox(309);
		PacketRepository.send(RepositionChild.class, new ChildPositionContext(player, 309, 3, 60, 79));
		PacketRepository.send(RepositionChild.class, new ChildPositionContext(player, 309, 2, 210, 10));
		player.getPacketDispatch().sendItemZoomOnInterface(tar.getTar().getId(), 200, 309, 2);
		player.getPacketDispatch().sendString(tar.getTar().getName(), 309, 3);
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			int amount = getAmount(buttonId);
			if (amount == -1) {
				player.setAttribute("runscript", new RunScript() {
					@Override
					public boolean handle() {
						int amount = (int) value;
						player.getPulseManager().run(new HerbTarPulse(player, null, tar, amount));
						end();
						return false;
					}
				});
				player.getDialogueInterpreter().sendInput(false, "Enter amount:");
				return true;
			}
			player.getPulseManager().run(new HerbTarPulse(player, null, tar, amount));
			end();
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { ID };
	}

	/**
	 * Method used to get the amount to make based off the button id.
	 * @param buttonId the button id.
	 * @return the amount to make.
	 */
	private final int getAmount(final int buttonId) {
		switch (buttonId) {
		case 6:
			return 1;
		case 5:
			return 5;
		case 4:
			return -1;
		}
		return -1;
	}
}
