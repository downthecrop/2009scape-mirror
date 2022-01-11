package core.game.content.dialogue;

import core.game.content.global.Skillcape;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import rs09.game.world.GameWorld;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.tools.RandomFunction;

/**
 * Represents the dialogue used for ignatius vulcan.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class IgnatiusVulcanDialogue extends DialoguePlugin {

	/**
	 * Represents the animation to use.
	 */
	private static final Animation ANIMATION = new Animation(733, Priority.HIGH);

	/**
	 * Constructs a new {@code IgnatiusVulcanDialogue} {@code Object}.
	 */
	public IgnatiusVulcanDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code IgnatiusVulcanDialogue} {@code Object}.
	 * @param player the player.
	 */
	public IgnatiusVulcanDialogue(final Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new IgnatiusVulcanDialogue(player);
	}

	@Override
	public void init() {
		super.init();
		try {
			new IgnatiusVulcanNPC().newInstance(null);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		npc("Can I help you at all?");
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			if (Skillcape.isMaster(player, Skills.FIREMAKING)) {
				options("Who are you?", "Could I buy a Skillcape of Firemaking?", "No, thanks.");
				stage = 100;
			} else {
				options("Who are you?", "What is that cape you're wearing?", "No, thanks.");
				stage = 1;
			}
			break;
		case 100:
			switch (buttonId) {
			case 1:
				player("Who are you?");
				stage = 10;
				break;
			case 2:
				player("Could I buy a Skillcape of Firemaking?");
				stage = 101;
				break;
			case 3:
				player("No, thanks.");
				stage = 30;
				break;
			}
			break;
		case 101:
			npc("Certainly! Right when you give me 99000 coins.");
			stage = 102;
			break;
		case 102:
			options("Okay, here you go.", "No, thanks.");
			stage = 103;
			break;
		case 103:
			switch (buttonId) {
			case 1:
				player("Okay, here you go.");
				stage = 104;
				break;
			case 2:
				end();
				break;
			}
			break;
		case 104:
			if (Skillcape.purchase(player, Skills.FIREMAKING)) {
				npc("There you go! Enjoy.");
			}
			stage = 105;
			break;
		case 105:
			end();
			break;
		case 1:
			switch (buttonId) {
			case 1:
				player("Who are you?");
				stage = 10;
				break;
			case 2:
				player("What is that cape you're wearing?");
				stage = 20;
				break;
			case 3:
				player("No, thanks.");
				stage = 30;
				break;
			}
			break;
		case 10:
			npc("My name is Ignatius Vulcan. Once I was - like you -", "an adventurer, but that was before I realised the", "beauty and power of flame! Just look at this...");
			stage = 11;
			break;
		case 11:
			createFire(npc, player.getLocation());
			player.moveStep();
			player.sendChat("Yeeouch!");
			npc("Stare into the flame and witness the purity and power", "of fire! As my attraction to flame grew, so did my skills", "at firelighting. I began to neglect my combat skills, my", "Mining skills and my questing. Who needs such");
			stage = 12;
			break;
		case 12:
			npc("mundane skills when one can harness the power of fire?", "After years of practice I am now the acknowledged", "master of Flame! Everything must be purified by fire!");
			stage = 13;
			break;
		case 13:
			player("Okaaay! err, I'll be going now. Umm, get better soon.");
			stage = 14;
			break;
		case 14:
			end();
			break;
		case 20:
			npc("This is a Skillcape of Firemaking. I was given it in", "recognition of my skill as the greatest firemaker in the", "lands! I AM the Master of Flame!");
			stage = 21;
			break;
		case 21:
			player("Hmm, I'll be going now. Keep a sharp look out for", "those men with their white jackets!");
			stage = 22;
			break;
		case 22:
			end();
			break;
		case 30:
			end();
			break;
		}
		return true;
	}

	/**
	 * Method used to create an ignatius fire.
	 */
	public static void createFire(final NPC npc, final Location location) {
		npc.getWalkingQueue().reset();
		npc.getAnimator().forceAnimation(ANIMATION);
		if (RegionManager.getObject(location) == null) {
			final Scenery fire = new Scenery(2732, location);
			SceneryBuilder.add(fire, RandomFunction.random(100, 130));
			npc.faceLocation(fire.getLocation());
		}
	}

	@Override
	public int[] getIds() {
		return new int[] { 4946 };
	}

	/**
	 * Represents the ignatius vulcan npc as a representation.
	 * @author 'Vexia
	 * @version 1.0
	 */
	public final class IgnatiusVulcanNPC extends AbstractNPC {

		/**
		 * Represents the time till the last fire.
		 */
		private int lastFire;

		/**
		 * Constructs a new {@code IgnatiusVulcanNPC} {@code Object}.
		 * @param id the id.
		 * @param location the location.
		 */
		public IgnatiusVulcanNPC(int id, Location location) {
			super(id, location, true);
		}

		/**
		 * Constructs a new {@code IgnatiusVulcanNPC} {@code Object}.
		 */
		public IgnatiusVulcanNPC() {
			super(0, null);
		}

		@Override
		public AbstractNPC construct(int id, Location location, Object... objects) {
			return new IgnatiusVulcanNPC(id, location);
		}

		@Override
		public void tick() {
			if (lastFire < GameWorld.getTicks()) {
				createFire(this, getLocation());
				lastFire = GameWorld.getTicks() + RandomFunction.random(50, 200);
			}
			super.tick();
		}

		@Override
		public int[] getIds() {
			return new int[] { 4946 };
		}

	}
}
