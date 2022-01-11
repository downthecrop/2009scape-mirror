package core.game.content.activity.magearena;

import core.game.content.dialogue.DialoguePlugin;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import rs09.game.world.GameWorld;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;

/**
 * Handles the kolodion dialogue.
 * @author Vexia
 */
public final class KolodionDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code KolodionDialogue} {@code Object}.
	 */
	public KolodionDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code KolodionDialogue} {@code Object}.
	 * @param player the player.
	 */
	public KolodionDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new KolodionDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		if (player.getSavedData().getActivityData().hasStartedKolodion()) {
			player("Hi.");
			return true;
		}
		if (player.getSavedData().getActivityData().hasKilledKolodion()) {
			player("Hello, Kolodion.");
			return true;
		}
		player("Hello there. What is this place?");
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		if (player.getSavedData().getActivityData().hasStartedKolodion()) {
			switch (stage) {
			case 0:
				npc("You return, young conjurer. You obviously have a", "taste for the dark side of magic.");
				stage++;
				break;
			case 1:
				startFight(player);
				end();
				break;
			}
			return true;
		}
		if (player.getSavedData().getActivityData().hasRecievedKolodionReward()) {
			switch (stage) {
			case 0:
				npc("Hey there, how are you? Are you enjoying the", "bloodshed?");
				stage++;
				break;
			case 1:
				player("I think I've had enough for now.");
				stage++;
				break;
			case 2:
				npc("A shame. You're a good battle mage. I hope", "to see you soon.");
				stage++;
				break;
			case 3:
				end();
				break;
			}
			return true;
		} else if (player.getSavedData().getActivityData().hasKilledKolodion()) {
			switch (stage) {
			case 0:
				npc("Hello, you mage. You're a tough one.");
				stage++;
				break;
			case 1:
				player("What now?");
				stage++;
				break;
			case 2:
				npc("Step into the magic pool. It will take you to a chamber.", "There, you must decide which god you will represent in", "the arena.");
				stage++;
				break;
			case 3:
				player("Thanks, Kolodion.");
				stage++;
				break;
			case 4:
				npc("That's what I'm here for.");
				stage++;
				break;
			case 5:
				end();
				break;
			}
			return true;
		}
		switch (stage) {
		case 0:
			if (player.getSkills().getStaticLevel(Skills.MAGIC) < 60) {
				npc("Do not waste my time with trivial questions. I am the", "Great Kolodion, master of battle magic. I have an arena", "to run.");
				stage++;
			} else {
				npc("I am the great Kolodion, master of battle magic, and", "this is my battle arena. Top wizards travel from all over", GameWorld.getSettings().getName() + " to fight here.");
				stage = 4;
			}
			break;
		case 1:
			player("Can I enter?");
			stage++;
			break;
		case 2:
			player("Hah! A wizard of your level? Don't be absurd.");
			stage++;
			break;
		case 3:
			end();
			break;
		case 4:
			options("Can I fight here?", "What's the point of that?", "That's barbaric!");
			stage++;
			break;
		case 5:
			switch (buttonId) {
			case 1:
				player("Can I fight here?");
				stage = 10;
				break;
			case 2:
				player("What's the point of that?");
				stage = 20;
				break;
			case 3:
				player("That's barbaric!");
				stage = 30;
				break;
			}
			break;
		case 10:
			npc("My arena is open to any high level wizard, but this is", "no game. Many wizards fall in this arena, never to rise", "again. The strongest mages have been destroyed.");
			stage++;
			break;
		case 11:
			npc("If you're sure you want in?");
			stage++;
			break;
		case 12:
			options("Yes indeedy.", "No I don't.");
			stage++;
			break;
		case 13:
			switch (buttonId) {
			case 1:
				player("Yes indeedy.");
				stage++;
				break;
			case 2:
				end();
				break;
			}
			break;
		case 14:
			npc("Good, good. You have a healthy sense of competition.");
			stage++;
			break;
		case 15:
			npc("Remember, traveller - in my arena, hand-to-hand", "combat is useless. Your strength will diminish as you", "enter the arena, but the spells you can learn are", "amongst the most powerful in all of " + GameWorld.getSettings().getName() + ".");
			stage++;
			break;
		case 16:
			npc("Before I can accept you in, we must duel.");
			stage++;
			break;
		case 17:
			options("Okay, let's fight.", "No thanks.");
			stage++;
			break;
		case 18:
			switch (buttonId) {
			case 1:
				player("Okay, let's fight.");
				stage++;
				break;
			case 2:
				end();
				break;
			}
			break;
		case 19:
			npc("I must first check that you are up to scratch.");
			stage = 40;
			break;
		case 40:
			player("You don't need to worry about that.");
			stage++;
			break;
		case 41:
			npc("Not just any magician can enter - only the most", "powerful and most feared. Before you can use the", "power of this arena, you must prove yourself against", "me.");
			stage++;
			break;
		case 42:
			startFight(player);
			end();
			break;
		case 20:
			npc("They want to crown themselves the best", "mage in all of " + GameWorld.getSettings().getName() + "!");
			stage = 30;
			break;
		case 30:
			end();
			break;
		}
		return true;
	}

	/**
	 * Starts the fight.
	 * @param player the player.
	 */
	private void startFight(final Player player) {
		player.getSavedData().getActivityData().setKolodionStage(1);
		player.lock();
		npc.animate(Animation.create(811));
		player.teleport(Location.create(3105, 3934, 0), 3);
		player.visualize(Animation.create(1816), Graphics.create(301, 100));
		KolodionSession.create(player).start();
	}

	@Override
	public int[] getIds() {
		return new int[] { 905 };
	}

}
