package core.game.content.dialogue;

import core.plugin.Initializable;
import core.game.node.entity.skill.summoning.familiar.Familiar;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.tools.RandomFunction;

/**
 * Represents the dialogue plugin used for familiars.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class FamiliarDialoguePlugin extends DialoguePlugin {

	/**
	 * Constructs a new {@code FamiliarDialoguePlugin} {@code Object}.
	 */
	public FamiliarDialoguePlugin() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code FamiliarDialoguePlugin} {@code Object}.
	 * @param player the player.
	 */
	public FamiliarDialoguePlugin(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new FamiliarDialoguePlugin(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		if (!(npc instanceof Familiar)) {
			return false;
		}
		final Familiar f = (Familiar) npc;
		if (f.getOwner() != player) {
			player.getPacketDispatch().sendMessage("This is not your follower.");
			return true;
		}
		// if (f.getDetails().getType() == FamiliarType.PET) {TODO:Pet
		// if (player.getSkills().getLevel(Skills.SUMMONING) < ((PetDetails)
		// f.getDetails()).getPet().getSummoningLevel() + 10) {
		// interpreter.sendDialogues(npc, null, "Grumble...!");
		// int lvl = ((PetDetails) f.getDetails()).getPet().getSummoningLevel()
		// + 10;
		// if (lvl > 99) {
		// player.getPacketDispatch().sendMessage("It is impossible to talk to this pet.");//dragon,
		// chamerelon, monket..etc
		// return true;
		// }
		// player.getPacketDispatch().sendMessage("You need a summoning level of "
		// + lvl + " in order to communicate with your pet.");
		// stage = 99;
		// return true;
		// }
		// }
		stage = RandomFunction.random(1, 3);
		switch (stage) {
		case 1:
			interpreter.sendDialogues(npc, null, "Are we going to be walking all day?");
			stage = 1;
			break;
		case 2:
			interpreter.sendDialogues(npc, null, "Yer lookin' tired. You should have a nap.");
			stage = 20;
			break;
		case 3:
			interpreter.sendDialogues(npc, null, "I could do wi' a cup of tea, mind.");
			stage = 30;
			break;
		}
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 1:// random first.
			interpreter.sendDialogues(player, null, "Well, I could carry you if you like.");
			stage = 10;
			break;
		case 10:
			interpreter.sendDialogues(npc, null, "That would be great!");
			stage = 11;
			break;
		case 11:
			interpreter.sendDialogues(player, null, "You know, you won't grow up to be big and strong", "if you keep getting carried everywhere.");
			stage = 12;
			break;
		case 12:
			interpreter.sendDialogues(npc, null, "I know, I know.");
			stage = 99;
			break;
		case 2:// random second dial.
			interpreter.sendDialogues(npc, null, "Yer lookin' tired. You should have a nap.");
			stage = 20;
			break;
		case 20:
			interpreter.sendDialogues(player, null, "Is it me who needs a rest or you?");
			stage = 21;
			break;
		case 21:
			interpreter.sendDialogues(npc, null, "Well, if you took one, I might too...");
			stage = 22;
			break;
		case 22:
			interpreter.sendDialogues(player, null, "Yeah, I thought as much.");
			stage = 23;
			break;
		case 23:
			interpreter.sendDialogues(npc, null, "Grumble...");
			stage = 99;
			break;
		case 3:// random third dial.
			interpreter.sendDialogues(npc, null, "I could do wi' a cup of tea, mind.");
			stage = 30;
			break;
		case 30:
			interpreter.sendDialogues(player, null, "I don't know if a cup of tea would be good for you.");
			stage = 31;
			break;
		case 31:
			interpreter.sendDialogues(npc, null, "Fair 'nuff, then.");
			stage = 99;
			break;
		case 99:
			end();
			player.getFamiliarManager().getFamiliar().startFollowing();
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 761, 762, 763, 764, 765, 766, 3505, 3598, 6969, 7259, 7260, 6964, 7249, 7251, 6960, 7241, 7243, 6962, 7245, 7247, 6966, 7253, 7255, 6958, 7237, 7239, 6915, 7277, 7278, 7279, 7280, 7018, 7019, 7020, 6908, 7313, 7316, 6947, 7293, 7295, 7297, 7299, 6911, 7261, 7263, 7265, 7267, 7269, 6919, 7301, 7303, 7305, 7307, 6949, 6952, 6955, 6913, 7271, 7273, 6945, 7319, 7321, 7323, 7325, 7327, 6922, 6942, 7210, 7212, 7214, 7216, 7218, 7220, 7222, 7224, 7226, 6900, 6902, 6904, 6906, 768, 769, 770, 771, 772, 773, 3504, 8214, 6968, 7257, 7258, 6965, 7250, 7252, 6961, 7242, 7244, 6963, 7246, 7248, 6967, 7254, 7256, 6859, 7238, 7240, 6916, 7281, 7282, 7283, 7284, 7015, 7016, 7017, 6909, 7314, 7317, 11413, 6948, 7294, 7296, 7298, 7300, 6912, 7262, 7264, 7266, 7268, 7270, 6920, 7302, 7304, 7306, 7308, 6950, 6953, 6956, 6914, 7272, 7274, 13090, 6946, 7320, 7322, 7324, 7326, 7328, 6923, 6943, 7211, 7213, 7215, 7217, 7219, 7221, 7223, 7225, 7227, 6901, 6903, 6905, 6907, 774, 775, 776, 777, 778, 779, 3503, 8216, 6951, 6954, 6957 };
	}
}
