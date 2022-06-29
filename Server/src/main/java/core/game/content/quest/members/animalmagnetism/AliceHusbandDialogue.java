package core.game.content.quest.members.animalmagnetism;

import core.game.content.activity.ActivityManager;
import core.game.content.activity.ActivityPlugin;
import core.game.content.activity.CutscenePlugin;
import core.game.content.dialogue.DialoguePlugin;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.ImpactHandler.HitsplatType;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.quest.Quest;
import core.game.node.item.Item;
import core.game.system.task.Pulse;
import rs09.game.content.quest.members.anma.AnmaCutscene;
import rs09.game.world.GameWorld;
import core.game.world.map.Location;
import core.game.world.map.build.DynamicRegion;
import core.game.world.map.path.Pathfinder;
import core.game.world.update.flag.context.Animation;
import core.net.packet.PacketRepository;
import core.net.packet.context.CameraContext;
import core.net.packet.context.CameraContext.CameraType;
import core.net.packet.out.CameraViewPacket;
import rs09.plugin.ClassScanner;

/**
 * Handles the husband of alice's npc dialogue.
 * @author Vexia
 */
public final class AliceHusbandDialogue extends DialoguePlugin {

	/**
	 * The quest.
	 */
	private Quest quest;

	/**
	 * Constructs a new {@code AliceHusbandDialogue} {@code Object}.
	 */
	public AliceHusbandDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code AliceHusbandDialogue} {@code Object}.
	 * @param player the player.
	 */
	public AliceHusbandDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new AliceHusbandDialogue(player);
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	public boolean open(Object... args) {
		quest = player.getQuestRepository().getQuest(AnimalMagnetism.NAME);
		switch (quest.getStage(player)) {
		case 0:
			npc("Hi, I don't feel like talking.");
			break;
		case 10:
			player("Your animals don't look too healthy.");
			break;
		case 11:
			npc("'Ave you talked to the wife for me?");
			break;
		case 12:
			player("Your wife says she needs the family cash and wants to", "know what you did with it.");
			stage++;
			break;
		case 13:
			npc("Any luck wiv me wife?");
			break;
		case 14:
		case 15:
			npc("'Ave you talked to 'er?");
			break;
		case 16:
		case 17:
		case 18:
			player("I talked to your wife and thought that if you had a", "special amulet, you could speak to her and sort out the", "bank situation without me being involved.");
			break;
		case 19:
			npc("Ahh, many thanks. Now what was it you were wanting", "again?");
			break;
		default:
			npc("Hello, how can I help you? I'm sellin' if you have ecto-", "tokens.");
			break;
		}
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (quest.getStage(player)) {
		default:
			switch (stage) {
			case 0:
				options("Could I buy those chickens now, then?", "Your animals don't look too healthy.", "I'm okay, thank you.", "Where can I get these ecto-tokens?");
				stage++;
				break;
			case 1:
				switch (buttonId) {
				case 1:
					player("Could I buy those chickens now, then?");
					stage = 10;
					break;
				case 2:
					player("Your animals don't look too healthy.");
					stage = 20;
					break;
				case 3:
					player("I'm okay, thank you.");
					stage = 30;
					break;
				case 4:
					player("Where can I get these ecto-tokens?");
					stage = 40;
					break;
				}
				break;
			case 10:
				npc("I can hand over a chicken if you give me 10 of them", "ecto-token thingies per bird.");
				stage++;
				break;
			case 11:
				options("Could I buy 1 chicken now?", "Could I buy 2 chickens now?", "Your animals don't look too healthy; I'll buy elsewhere.");
				stage++;
				break;
			case 12:
				switch (buttonId) {
				case 1:
					player("Could I buy 1 chicken now?");
					stage = 14;
					break;
				case 2:
					player("Could I buy 2 chickens now?");
					stage = 15;
					break;
				case 3:
					player("Your animals don't look too healthy; I'll buy elsewhere.");
					stage++;
					break;
				}
				break;
			case 13:
				end();
				break;
			case 14:
			case 15:
				buy(stage == 14 ? 1 : 2);
				stage = 16;
				break;
			case 16:
				end();
				break;
			case 20:
				npc("It's that fountain thingy in the temple to the east. It's", "turned them all into zombies.");
				stage++;
				break;
			case 21:
				player("What use are zombie animals?");
				stage++;
				break;
			case 22:
				npc("None at all, mate, except that those worshippers at that", "temple keep comin' and killin' em all for their bones.", "Don't ask me why.");
				stage++;
				break;
			case 23:
				player("But you're a ghost - surely you know", "something about it.");
				stage++;
				break;
			case 24:
				npc("I don't know nuthin' about nuthin'. Oim a simple ghost", "with simple needs. All I know is, years ago, that temple", "started glowing green and, a few months later, I woke", "up dead. That's all there is to it.");
				stage++;
				break;
			case 25:
				end();
				break;
			case 30:
				end();
				break;
			case 40:
				npc("The ghosts I talk to say that the tokens have something", "to do with the tower just east of here. If you need to", "collect some I'd try there.");
				stage++;
				break;
			case 41:
				end();
				break;
			}
			break;
		case 0:
			switch (stage) {
			default:
				end();
				break;
			}
			break;
		case 10:
			switch (stage) {
			case 0:
				npc("It's that fountain thingy in the temple to the east. It's", "turned them all into zombies.");
				stage++;
				break;
			case 1:
				player("What use are zombie animals?");
				stage++;
				break;
			case 2:
				npc("None at all, mate, except that those worshippers at that", "temple keep comin' and killin' em all for their bones.", "Don't ask me why.");
				stage++;
				break;
			case 3:
				player("But you're a ghost - surely you know", "something about it.");
				stage++;
				break;
			case 4:
				npc("I don't know nuthin' about nuthin'. Oim a simple ghost", "with simple needs. All I know is, years ago, that temple", "started glowing green and, a few months later, I woke", "up dead. That's all there is to it.");
				stage++;
				break;
			case 5:
				npc("I do miss the wife though; tell 'er I still loves her.");
				stage++;
				break;
			case 6:
				player("Would I be able to buy some of your chickens?");
				stage++;
				break;
			case 7:
				npc("Talk to my wife and I'll think about it.");
				stage++;
				break;
			case 8:
				if (quest.getStage(player) == 10) {
					quest.setStage(player, 11);
				}
				end();
				break;
			}
			break;
		case 11:
			switch (stage) {
			case 0:
				player("Not yet; I've been distracted by the thought of undead", "cow milk.");
				stage++;
				break;
			case 1:
				end();
				break;
			}
			break;
		case 12:
			switch (stage) {
			case 1:
				npc("Tell 'er I spent it on cheap spirits, har har.");
				stage++;
				break;
			case 2:
				player("Your sense of humour died too, it seems...");
				stage++;
				break;
			case 3:
				npc("Hah, just trying to lift your spirits.");
				stage++;
				break;
			case 4:
				player("I rest my case.");
				stage++;
				break;
			case 5:
				npc("Suit yerself, stick-in-the-mud. Anyway, Oim not one o'", "them yokels. Tell 'er I putted the cash in the bank like", "she always told me to.");
				stage++;
				break;
			case 6:
				npc("A warning to ya, too; annoy her and I'll haunt ya till", "yer hair turns white.");
				stage++;
				break;
			case 7:
				quest.setStage(player, 13);
				end();
				break;
			}
			break;
		case 13:
			switch (stage) {
			case 0:
				player("Nothing new, no.");
				stage++;
				break;
			case 1:
				end();
				break;
			}
			break;
		case 14:
			switch (stage) {
			case 0:
				player("You may not believe me, but she wants me to find", "your bank pass now.");
				stage++;
				break;
			case 1:
				npc("Maybe she said that, maybe she didn't. I think you're", "just after me savings. Tell 'er that no one but a fool", "gives away their pass numbers.");
				stage++;
				break;
			case 2:
				npc("Go tell 'er now, if you're not a double-dealing' scammer,", "that is.");
				stage++;
				break;
			case 3:
				quest.setStage(player, 15);
				end();
				break;
			}
			break;
		case 15:
			switch (stage) {
			case 0:
				player("Not since we last spoke.");
				stage++;
				break;
			case 1:
				end();
				break;
			}
			break;
		case 16:
		case 17:
		case 18:
			switch (stage) {
			case 0:
				npc("Arr, that makes far more sense than I was expecting", "from a muscle-head like you. My wife's a clever one.");
				stage++;
				break;
			case 1:
				if (player.getInventory().containsItem(AnimalMagnetism.CRONE_AMULET)) {
					player("Well... oh, never mind. I'm desperate enough for those", "chickens to let that pass.");
					stage += 2;
					break;
				}
				player("Well...oh, never mind. I'm working on getting the", "amulet anyway.");
				stage++;
				break;
			case 2:
				end();
				break;
			case 3:
				npc("Give me that amulet, then, and we'll be seeing about", "your unnatural desire for chickens.");
				stage++;
				break;
			case 4:
				player("Okay, you need it more than I do, I suppose.");
				stage++;
				break;
			case 5:
				npc("Ta, mate.");
				stage++;
				break;
			case 6:
				player("Lucky we had such a brilliant idea.");
				stage++;
				break;
			case 7:
				if (player.getInventory().remove(AnimalMagnetism.CRONE_AMULET)) {
					quest.setStage(player, 19);
					end();
				}
				break;
			}
			break;
		case 19:
			switch (stage) {
			case 0:
				player("I need a couple of your chickens.");
				stage++;
				break;
			case 1:
				npc("Chickens is tricksy, 'specially dead 'uns. I'll have to catch", "'em for ye.");
				stage++;
				break;
			case 2:
				player("They look preety pathetic, how hard can it be?");
				stage++;
				break;
			case 3:
				npc("Stand back while I catches 'em, ya city slicker.");
				stage++;
				break;
			case 4:
				end();
				new AnmaCutscene(player).start();
				break;
			}
			break;
		}
		return true;
	}

	/**
	 * Buys an undead chicken(s).
	 * @param amount the amount.
	 */
	private void buy(int amount) {
		final Item tokens = new Item(4278, amount * 10);
		if (!player.getInventory().containsItem(tokens)) {
			npc("I'm not a charity here, ya know. Bad enough all you", "cow-killing folks are a'slaughterin' me beasts. Come back", "when ya have enough tokens.");
			return;
		}
		if (player.getInventory().freeSlots() < amount) {
			player("Sorry, I don't have enough inventory space.");
			return;
		}
		if (player.getInventory().remove(tokens)) {
			player.getInventory().add(new Item(10487, amount), player);
			npc("Great! I'm laying away me tokens for some killer cows.", "That'll learn them bones rustlers.");
		}
	}

	@Override
	public int[] getIds() {
		return new int[] { 5202 };
	}

}
