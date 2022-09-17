package core.game.content.quest.free.shieldofarrav;

import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import rs09.game.content.dialogue.DialogueFile;

public final class CuratorHaigHalenSOADialogue extends DialogueFile {
    @Override
    public void handle(int componentID, int buttonID) {
        Player player = getPlayer();
		if (player.getInventory().containsItem(ShieldofArrav.PHOENIX_SHIELD) || player.getInventory().containsItem(ShieldofArrav.BLACKARM_SHIELD)) {
			switch (getStage()) {
			case 0:
				player("I have half the shield of Arrav here. Can I get a", "reward?");
				setStage(1);
				break;
			case 1:
				npc("The Shield of Arrav! Goodness, the Museum has been", "searching for that for years! The late King Roald II", "offered a reward for it years ago!");
				setStage(2);
				break;
			case 2:
				player("Well, I'm here to claim it.");
				setStage(3);
				break;
			case 3:
				npc("Let me have a look at it first.");
				setStage(4);
				break;
			case 4:
				getInterpreter().sendItemMessage(ShieldofArrav.getShield(player), "The curator peers at the shield.");
				setStage(5);
				break;
			case 5:
				npc("This is incredible!");
				setStage(6);
				break;
			case 6:
				npc("That shield has been missing for over twenty-five years!");
				setStage(7);
				break;
			case 7:
				npc("Leave the shield here with me and I'll write you out a", "certificate saying that you have returned the shield, so", "that you can claim your reward from the King.");
				setStage(8);
				break;
			case 8:
				player("Can I have two certificates please?");
				setStage(9);
				break;
			case 9:
				npc("Yes, certainly. Please hand over the shield.");
				setStage(10);
				break;
			case 10:
				getInterpreter().sendItemMessage(ShieldofArrav.getShield(player), "You hand over the shield half.");
				setStage(11);
				break;
			case 11:
				final Item shield = ShieldofArrav.getShield(player);
				final Item certificate = shield == ShieldofArrav.BLACKARM_SHIELD ? ShieldofArrav.BLACKARM_CERTIFICATE : ShieldofArrav.PHOENIX_CERTIFICATE;
				if (player.getInventory().remove(shield)) {
					player.getInventory().add(certificate);
					getInterpreter().sendItemMessage(certificate, "The curator writes out two half-certificates.");
					setStage(12);
				}
				break;
			}
			return;
		}
        switch (getStage()) {
        case 0:
        case 12:
            npc("Of course you won't actually be able to claim the", "reward with only half the reward certificate...");
            setStage(13);
            break;
        case 13:
            player("What? I went through a lot of trouble to get that shield", "piece and now you tell me it was for nothing? That's", "not very fair!");
            setStage(14);
            break;
        case 14:
            npc("Well, if you were to get me the other half of the shield,", "I could give you the other half of the reward certificate.", "It's rumoured to be in the possession of the infamous", "Blackarm Gang, beyond that I can't help you.");
            setStage(15);
            break;
        case 15:
            player("Okay, I'll see what I can do.");
            setStage(16);
            break;
        case 16:
            end();
            break;
        }
    }
}

