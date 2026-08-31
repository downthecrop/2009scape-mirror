package content.region.misthalin.draynor.quest.anma;

import content.data.Quests;
import core.api.Container;
import core.game.dialogue.DialoguePlugin;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.quest.Quest;
import core.game.node.item.Item;
import core.game.world.GameWorld;
import org.rs09.consts.Items;

import static core.api.ContentAPIKt.*;

/**
 * Handles the ava npc dialogue.
 * @author Vexia
 */
public final class AvaDialogue extends DialoguePlugin {

	/**
	 * The undead chicken items.
	 */
	private static final Item UNDEAD_CHICKENS = new Item(10487, 2);

	/**
	 * The quest.
	 */
	private Quest quest;

	/**
	 * Constructs a new {@code AvaDialogue} {@code Object}.
	 */
	public AvaDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code AvaDialogue} {@code Object}.
	 * @param player the player.
	 */
	public AvaDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new AvaDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		quest = player.getQuestRepository().getQuest(Quests.ANIMAL_MAGNETISM);
		if (!quest.hasRequirements(player)) {
			player.getPacketDispatch().sendMessage("She doesn't seem interested in talking to you.");
			return false;
		}
		switch (quest.getStage(player)) {
		case 0:
			npc("Hello there and welcome to my humble abode. It's sadly", "rather more humble than I'd like, to be honest, although", "perhaps you can help with that?");
			break;
		case 10:
			npc("Do you need a reminder what you are supposed to do?", "I know you must have shiny beads distracting you, but", "it is an important job you are doing for me.");
			break;
		case 20:
			npc("My spiritometric devices show that you have been in", "close contact with ghostly animals. Are we closer to", "success?");
			break;
		case 25:
			npc("Go and talk to the Witch next door. She'd talk the hind", "legs off a donkey but she can select the iron with which", "it is suitable for the chicken to interact.");
			break;
		case 27:
			if (player.getInventory().containsItem(AnimalMagnetism.BAR_MAGNET)) {
				player("I've manufactured the magnet; here it is.");
				stage = 4;
			} else {
				player("I've talked to the Witch and now I've given her some", "iron bars.");
			}
			break;
		case 28:
			npc("So, what terrible hitch have you encountered now?");
			break;
		case 29:
		case 30:
			player("Well, I tried to hack the trees with my axe, but it just", "bounced off the trunk! It did all seem all-too-convenient", "to work on the first try.");
			break;
		case 31:
			if (player.getInventory().containsItem(AnimalMagnetism.UNDEAD_TWIGS)) {
				player("I have that undead wood at last. Well, twigs anyway.");
				stage++;
			} else {
				npc("Come back when you have undead wood...");
			}
			break;
		case 32:
			player("I'd like to look at those research notes now, unless you", "have translated them without me?");
			break;
		case 33:
			if (player.getInventory().containsItem(AnimalMagnetism.TRANSLATED_NOTES)) {
				player("I've translated the notes. See? I'm not just a thuggish", "moron like you seem to think.");
				stage = 10;
				break;
			}
			if (player.hasItem(AnimalMagnetism.RESEARCH_NOTES)) {
				player("I have the notes but haven't translated them yet. Any", "hints?");
			} else {
				player("I seem to have lost the research notes.");
				stage = 4;
			}
			break;
		case 34:
			if (player.getInventory().containsItem(AnimalMagnetism.CONTAINER)) {
				npc("Wow, great, now the arrow manufacturer is ready for", "use...there you are! Talk to me if you need more", "information later.");
				stage = 20;
				break;
			}
			if (player.hasItem(AnimalMagnetism.PATTERN)) {
				player("So what do I do with this pattern again?");
			} else {
				player("My pattern seems to have vanished from my pack...not", "my fault, of course.");
				stage = 10;
			}
			break;
		case 100:
			npc("Hello again; I'm busy with my newest research, so can't", "gossip too much. Are you after information, an upgrade,", "another device or would you like to see my goods for", "sale?");
			break;
		default:
			npc("How's the quest going?");
			break;
		}
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (quest.getStage(player)) {
		case 0:
			switch (stage) {
			case 0:
				player("I would be happy to make your home a better place.");
				stage++;
				break;
			case 1:
				npc("Yay, I didn't even have to talk about a reward; you're", "more gullible than most adventurers, that's for sure.");
				stage++;
				break;
			case 2:
				npc("Don't worry, though; I just need you to help fix this", "vile old bed for me. Then I'll find a suitable reward for", "you.");
				stage++;
				break;
			case 3:
				player("Great, will I be able to take a nap in it?");
				stage++;
				break;
			case 4:
				npc("Don't be silly; everyone knows that true warriors don't", "ever sleep...or perform many other bodily functions, for", "that matter. I'll come up with something, though.");
				stage++;
				break;
			case 5:
				player("I'm not convinced by just a vague something; can you", "be a slight bit more inspiring in your offer?");
				stage++;
				break;
			case 6:
				npc("I'll use one for my bed, then see what I can make", "from the other in the way of a reward. I have some", "ideas involving infinite feathers.");
				stage++;
				break;
			case 7:
				player("Very well then, I shall await my mystery prize with", "bated breath.");
				stage++;
				break;
			case 8:
				quest.start(player);
				end();
				break;
			}
			break;
		case 10:
			switch (stage) {
			case 0:
				player("Yes, please.");
				stage++;
				break;
			case 1:
				npc("...And people say we scientists are absent minded...");
				stage++;
				break;
			case 2:
				npc("You need to fetch 2 undead chickens for me to use in", "my redecoration. It's not rocket science.");
				stage++;
				break;
			case 3:
				player("Rocket?");
				stage++;
				break;
			case 4:
				npc("If they existed, they would be like arrows but much,", "much bigger. Considering the mess you rangers make", "with small bits of sticks, we scientists have decided they", "don't exist. Now get chicken hunting.");
				stage++;
				break;
			case 5:
				end();
				break;
			}
			break;
		case 20:
			switch (stage) {
			case 0:
				if (player.getInventory().containsItem(UNDEAD_CHICKENS)) {
					player("Here they are.");
					stage = 2;
				} else {
					player("I'm still looking for undead chickens...");
					stage = 1;
				}
				break;
			case 1:
				end();
				break;
			case 2:
				npc("Amazing! Success! I can look forward to some good", "nights' sleep after all.");
				stage++;
				break;
			case 3:
				player("Can I ask exactly how an undead chicken will help you", "sleep?");
				stage++;
				break;
			case 4:
				npc("Well, I need the feathers to make my bed more", "comfortable. A comfortable bed will help me sleep.", "Obvious, really.");
				stage++;
				break;
			case 5:
				player("Obvious, yes, but why on " + GameWorld.getSettings().getName() + " would you need", "an undead chicken when there are perfectly good live", "chickens just down the road?");
				stage++;
				break;
			case 6:
				npc("Well, for a start, undead feathers are much cleaner", "than living ones; no dust mites or anything. Secondly, I", "always think of Ernest when I see a chicken, so my", "nerves can't take killing them.");
				stage++;
				break;
			case 7:
				player("Then why do I need a chicken for my reward; we", "already established that I don't use a bed?");
				stage++;
				break;
			case 8:
				npc("Seeing as how you ranger types use so many feathers", "in your arrows, I was thinking I could harness an", "undead chicken to make an unending supply of arrow", "flights for you.");
				stage++;
				break;
			case 9:
				player("Beats chicken slaying or hanging around in fishing", "shops, I suppose. So, what next?");
				stage++;
				break;
			case 10:
				npc("We'll need a magnet next, one with purely natural", "fields and made from a carefully selected iron bar. A", "firm impact when the iron is parallel to " + GameWorld.getSettings().getName() + "'s", "field will stabilise this field in the rod.");
				stage++;
				break;
			case 11:
				npc("Go and talk to the Witch next door.");
				stage++;
				break;
			case 12:
				if (player.getInventory().remove(UNDEAD_CHICKENS)) {
					quest.setStage(player, 25);
					end();
				}
				break;
			}
			break;
		case 25:
			switch (stage) {
			case 0:
				npc("Despite my extensive studies, her years of experience", "make her better at instinctive magico-mystical", "interaction. Oh well, at least I'm cleverer, prettier and", "will have a better bed.");
				stage++;
				break;
			case 1:
				player("Okay, okay, you're great. Yes, I'll go and talk to her", "when you've finished praising yourself.");
				stage++;
				break;
			case 2:
				end();
				break;
			}
			break;
		case 27:
			switch (stage) {
			case 0:
				npc("I'm thrilled for you. What about my magnet?");
				stage++;
				break;
			case 1:
				player("The process is subject to some unexpected delays.");
				stage++;
				break;
			case 2:
				npc("I can see now why Frenekstrain prefers to work with", "dead folk. It can't be more frustrating than working", "with you.");
				stage++;
				break;
			case 3:
				end();
				break;
			case 4:
				npc("Great stuff! with the Witch's influence within the", "magnet, the undead chicken can use this, I'm sure.");
				stage++;
				break;
			case 5:
				npc("The plan is that the chicken will operate the magnet to", "attract bits of iron and steel, maybe even your own", "recently fired arrows. There are plenty of totally lost", "arrowheads lying about in the Fields of " + GameWorld.getSettings().getName() + ", I");
				stage++;
				break;
			case 6:
				npc("bet.");
				stage++;
				break;
			case 7:
				npc("In addition, arrows which you fire should be able to be", "attracted back to your quiver by cunning avian.");
				stage++;
				break;
			case 8:
				player("I begin to understand your plan. We've covered", "feathers and arrowheads now; what next?");
				stage++;
				break;
			case 9:
				npc("We need a source of wood, but one which is spiritually", "active and can regenerate itself. That will save you", "some axework in the future.");
				stage++;
				break;
			case 10:
				npc("Try using a woodcutting axe on the pesky trees in the", "garden here, that ones that attack rather than the really", "dead ones. They are probably just the sort of thing we", "could use.");
				stage++;
				break;
			case 11:
				player("They will try to kill me, though, and I can't fight back!");
				stage++;
				break;
			case 12:
				npc("Now you know how those poor guards feel when you", "hide behind mushrooms and fences and attack them", "from afar! Anyway, I reckon you'll need to try a", "mithril or better axe on the trees. At least the trees axe");
				stage++;
				break;
			case 13:
				npc("pretty close.");
				stage++;
				break;
			case 14:
				if (player.getInventory().remove(AnimalMagnetism.BAR_MAGNET)) {
                                        setVarp(player, 939, 150, true);
					quest.setStage(player, 28);
					end();
				}
				break;
			}
			break;
		case 28:
			switch (stage) {
			case 0:
				player("Nothing really; I just decided to talk to you in case", "you have any great advice for me.");
				stage++;
				break;
			case 1:
				npc("Nope, sorry.");
				stage++;
				break;
			case 2:
				end();
				break;
			}
			break;
		case 29:
		case 30:
			switch (stage) {
			case 0:
				npc("Fortunately for you, I've done some research and it", "seems to suggest that there are two choices open to", "you.");
				stage++;
				break;
			case 1:
				player("Tell me the worst.");
				stage++;
				break;
			case 2:
				npc("The first is more interesting. We cut off your arms,", "have them reanimated as undead, reattach them and", "then you should be able to cut the trees normally.");
				stage++;
				break;
			case 3:
				npc("Of course, you won't be able to pick your nose any", "more, so I suppose you'll want to try the second option.");
				stage++;
				break;
			case 4:
				player("I'm not exactly addicted to picking my nose, but I do", "think I'll pass on that method.");
				stage++;
				break;
			case 5:
				npc("Well, in that case, I think it may have something to do", "with Slayer abilities. After all, I did see Turael poking", "around the trees while I was moving in.");
				stage++;
				break;
			case 6:
				npc("As he's not known for his random touristic activities,", "you should try chatting with this Turael. He's the", "Slayer Master near Burthorpe.");
				stage++;
				break;
			case 7:
				player("Oh dear, I hope he doesn't want me to buy one of his", "ridiculous fashion accessories. Those earmuffs he sells", "make heroic adventurers into a laughing stock.");
				stage++;
				break;
			case 8:
				if (quest.getStage(player) == 29) {
					quest.setStage(player, 30);
				}
				end();
				break;
			}
			break;
		case 31:
			switch (stage) {
			case 0:
				end();
				break;
			case 1:
				npc("You certainly took your time.");
				stage++;
				break;
			case 2:
				player("I'd say they didn't grow on trees, but I guess you'd", "just be sarcastic about my sense of humour.");
				stage++;
				break;
			case 3:
				npc("Quite. Now that we have all the ingredients for infinite", "arrows, we just need a container in which we can keep", "the components in the correct mutual alignment.");
				stage++;
				break;
			case 4:
				npc("I've gathered together some research notes from various", "sources but I can't quite make out what they mean. If", "you want to have a go at making them out, just ask me", "for a copy.");
				stage++;
				break;
			case 5:
				if (player.getInventory().remove(AnimalMagnetism.UNDEAD_TWIGS)) {
					quest.setStage(player, 32);
				}
				end();
				break;
			}
			break;
		case 32:
			switch (stage) {
			case 0:
				npc("They are still stumping me. Here are the notes; I", "really hope your head doesn't explode from reading", "them.");
				stage++;
				break;
			case 1:
				player("I'd find it slightly inconvenient, I'm sure.");
				stage++;
				break;
			case 2:
				npc("It wouldn't be all bad as your body would be useful for", "research after death. What I'd be upset about was if", "bits of you landed in my nice new bed.");
				stage++;
				break;
			case 3:
				player("Your concern is touching.");
				stage++;
				break;
			case 4:
				quest.setStage(player, 33);
				player.getInventory().add(AnimalMagnetism.RESEARCH_NOTES);
				end();
				break;
			}
			break;
		case 33:
			switch (stage) {
			case 0:
				npc("I know you have the notes; I gave them to you, in case", "you forgot! Furthermore, if I had hints, I'd have", "translated those notes myself.");
				stage++;
				break;
			case 1:
				npc("So, take the hint and go off and translate them. If it's", "too hard, you can always go and shoot demons in", "cages.");
				stage++;
				break;
			case 2:
				end();
				break;
			case 4:
				if (!player.getInventory().hasSpaceFor(AnimalMagnetism.RESEARCH_NOTES)) {
					player("Sorry, I don't have enough inventory space.");
					stage++;
					return true;
				}
				player.getInventory().add(AnimalMagnetism.RESEARCH_NOTES);
				npc("Don't tell me, your cat ate them? You won't get out of", "the job that easily; here are some copies I made.");
				stage++;
				break;
			case 5:
				end();
				break;
			case 10:
				npc("For all I know, it was pure luck, so don't jump to any", "conclusions about your mighty intellect.");
				stage++;
				break;
			case 11:
				player("I can see why you don't have any assistants, you're", "not exactly easy to work with.");
				stage++;
				break;
			case 12:
				npc("Let's get back to the work we're doing, then.", "Remember, this is all a favour to you. I could have just", "decided to fob you off with a feather duster.");
				stage++;
				break;
			case 13:
				npc("I've given you a pattern for the container; you'll need", "to combine them with some polished buttons and hard", "leather. Then we're almost done. Good news, eh?");
				stage++;
				break;
			case 14:
				npc("If you are having trouble finding buttons, I've heard", "rumours that the H.A.M society carry this sort of stuff", "more than most.");
				stage++;
				break;
			case 15:
				player("Really? How would you know this strange detail?");
				stage++;
				break;
			case 16:
				npc("I hear they lose their clothes a lot to thieves so they", "have to make do with shoddy goods. Whatever the", "reason, they seem to carry buttons about in their pockets.");
				stage++;
				break;
			case 17:
				if (player.getInventory().remove(AnimalMagnetism.TRANSLATED_NOTES)) {
					player.getInventory().add(AnimalMagnetism.PATTERN);
					quest.setStage(player, 34);
					end();
				}
				break;
			}
			break;
		case 34:
			switch (stage) {
			case 0:
				npc("Your short-term memory loss worries me. Combine the", "pattern with hard leather and some polished buttons,", "then hand the resulting container to me.");
				stage++;
				break;
			case 1:
				end();
				break;
			case 10:
				npc("You're pretty careless, aren't you. I assume you're the", "type who leaves a trail of arrows and knives behind", "behind them when they train?");
				stage++;
				break;
			case 11:
				if (!player.getInventory().hasSpaceFor(AnimalMagnetism.PATTERN)) {
					player("Sorry, I don't have enough room in my backpack.");
					stage++;
					break;
				}
				player.getInventory().add(AnimalMagnetism.PATTERN);
				npc("Here's a replacement; perhaps if I charged for them,", "you'd be more careful.");
				stage++;
				break;
			case 12:
				end();
				break;
			case 20:
				if (player.getInventory().remove(AnimalMagnetism.CONTAINER)) {
					quest.finish(player);
					end();
				}
				break;
			}
			break;
		case 100:
			switch (stage) {
			case 0:
				options("I'd like information, please.", "I seem to need a new device.", "I'd like to upgrade my device, please.", "I'd like to see your stuff for sale, please.", "I'll just head off, I think.");
				stage++;
				break;
			case 1:
				switch (buttonId) {
				case 1:
					player("I'd like information, please.");
					stage = 20;
					break;
				case 2:
					player("I need another arrow-attracting device, please.");
					stage = 10;
					break;
				case 3:
					player("I'd like to upgrade my device, please.");
					stage = 50;
					break;
				case 4:
					player("I'd like to see your stuff for sale, please.");
					stage = 30;
					break;
				case 5:
					player("I'll just head off, I think.");
					stage = 40;
					break;
				}
				break;
			case 10:
				npc("Well, luckily for you, they have a habit of returning to", "me when people lose them, so I have some spares. They", "are homing chickens, it seems.");
				stage++;
				break;
			case 11:
				npc("I'll need 999 gp, however, for the expenses involved in", "restoring the poor creature to health, or unhealth, or", "whatever.");
				stage++;
				break;
			case 12:
				player("Why 999?");
				stage++;
				break;
			case 13:
				npc("Well, it just sounds less expensive than 1000. Do you", "want that replacement or not?");
				stage++;
				break;
			case 14:
				options("Sounds good to me.", "I'd prefer not to, actually.");
				stage++;
				break;
			case 15:
				switch (buttonId) {
					case 1:
						player("Sounds good to me.");
						stage++;
						break;
					case 2:
						player("I'd prefer not to, actually.");
						stage = 54;
						break;
				}
				break;
			case 16:
				if (!inInventory(player, Items.COINS_995, 999)) {
					npc("You seem not to have enough cash; you could always", "sell some of your gear, though.");
					stage = 55;
					break;
				}
				if (!hasSpaceFor(player, AnimalMagnetism.AVAS_ATTRACTOR)) {
					player("Sorry, I don't have enough inventory space.");
					stage = 55;
					break;
				}
				if (removeItem(player, new Item(Items.COINS_995, 999), Container.INVENTORY)) {
					addItem(player, AnimalMagnetism.AVAS_ATTRACTOR.getId(), 1, Container.INVENTORY);
				}
				npc("Here's your device; take good care of your chicken.");
				stage = 55;
				break;
			case 20:
				npc("Just a few bits of information before you run away to", "persecute rock crabs or cows.");
				stage++;
				break;
			case 21:
				npc("First, your new device won't work if you are wearing", "certain metallic chest armours.");
				stage++;
				break;
			case 22:
				npc("The magnetic attraction required for operation would", "cause feedback, so the device does not allow such", "incompatible item equippage.");
				stage++;
				break;
			case 23:
				npc("Secondly, although the device is calibrated to attract", "only arrow heads, there is a chance that other", "magnetically active items will be attracted.");
				stage++;
				break;
			case 24:
				npc("The arrow recovery function of the devices is slightly", "different and relies upon a harmonic bond between", "your arrows and the chicken's magnet. You'll thus only", "recover arrows which you have fired rather than those");
				stage++;
				break;
			case 25:
				npc("fired by nearby folks.");
				stage++;
				break;
			case 26:
				npc("Thirdly, there is an upgraded version available when", "your Ranged level is 50 or greater.");
				stage++;
				break;
			case 27:
				npc("You'll also need to move about if you want to collect", "arrows since the gathering of long-lost arrowheads can't", "work otherwise.");
				stage++;
				break;
			case 28:
				npc("Finally, the device will only work when worn. It", "automatically deactivates in other circumstances.");
				stage++;
				break;
			case 29:
				npc("I was worried about what would happen if you were to", "place it in a bank in its active state, so I've made sure", "it only works when it's on your back.");
				stage = 41;
				break;
			case 30:
				npc("Here's my shop.");
				stage++;
				break;
			case 31:
				end();
				npc.openShop(player);
				break;
			case 40:
				npc("Thanks for the help with my bed; see you again.");
				stage++;
				break;
			case 41:
				end();
				break;
			case 50:
				if (!inEquipmentOrInventory(player, Items.AVAS_ATTRACTOR_10498, 1)) {
					npc("You don't have a device in your bags that I can", "upgrade, I'm afraid.");
					stage = 55;
					break;
				}
				if (!hasLevelStat(player, Skills.RANGE, 50)) {
					sendDialogue("You need a Ranged level of 50 to do this.");
					stage = 55;
					break;
				}
				if (!inInventory(player, Items.STEEL_ARROW_886, 75)) {
					npc("I need 75 steel arrows for the upgrade process,", "I'm afraid.");
					stage = 55;
					break;
				}
				npc("You are ready to upgrade. I'll take 75 steel arrows and", "the old device, if that's all fine with you?");
				stage++;
				break;
			case 51:
				options("Sounds good to me.", "I'd prefer not to, actually.");
				stage++;
				break;
			case 52:
				switch (buttonId) {
					case 1:
						player("Sounds good to me.");
						stage++;
						break;
					case 2:
						player("I'd prefer not to, actually.");
						stage = 54;
						break;
				}
				break;
			case 53:
				if (!removeItem(player, new Item(Items.STEEL_ARROW_886, 75), Container.INVENTORY)) {
					npc("I need 75 steel arrows for the upgrade process,", "I'm afraid.");
					stage = 55;
					break;
				}
				if (removeItem(player, Items.AVAS_ATTRACTOR_10498, Container.INVENTORY) ||
				removeItem(player, Items.AVAS_ATTRACTOR_10498, Container.EQUIPMENT)) {
					addItem(player, Items.AVAS_ACCUMULATOR_10499, 1, Container.INVENTORY);
				}
				npc("Here's your upgraded device; take good care of it.");
				stage = 55;
				break;
			case 54:
				npc("I've better things to do than be irritated by you.");
				stage++;
				break;
			case 55:
				end();
				break;
			}
			break;
		default:
			switch (stage) {
			case 0:
				player("It's going...");
				stage++;
				break;
			case 1:
				end();
				break;
			}
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 5198, 5199 };
	}

}
