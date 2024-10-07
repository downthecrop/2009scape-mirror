package content.region.asgarnia.taverley.quest;

import core.plugin.Initializable;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.quest.Quest;
import core.game.node.item.Item;
import org.rs09.consts.Items;

import static core.api.ContentAPIKt.*;


/**
 * Represents the demon slayer quest.
 * @author 'Vexia
 */
@Initializable
public class WolfWhistle extends Quest {

	/**
	 * The wolf bones item.
	 */
	public static final Item WOLF_BONES = new Item(2859, 2);

	/**
	 * Constructs a new {@code WolfWhistle} {@code Object}.
     */
	public WolfWhistle() {
		super("Wolf Whistle", 146, 145, 1);
	}

	@Override
	public void drawJournal(Player player, int stage) {
		super.drawJournal(player, stage);
		var line = 12;

		if(stage == 0){
			line(player, "I can begin this quest by talking to !!Pikkupstix??, who lives in", line++, false);
			line(player, "!!Taverly??.", line++, false);
		} else {
			if (stage >= 10) {
				line(player, "Having spoken to !!Pikkupstix??, it seems that all I have to do", line++, stage >= 20);
				line(player, "is get rid of the !!little rabbit upstairs in his house??.", line++, stage >= 20);
				line++;
			}
			if (stage >= 20) {
				line(player, "It appears that I have underestimated the rabbit in this", line++, stage >= 30);
				line(player, "case; it is some !!huge rabbit-wolf-monster-bird-thing??. I", line++, stage >= 30);
				line(player, "think I should speak to !!Pikkupstix?? to find out what is going", line++, stage >= 30);
				line(player, "on.", line++, stage >= 30);
				line++;
			}
			// Clicking on the ladder - sendMessage("There is no reason to go up there and face that thing again.")
			if (stage >= 30) {
				line(player, "I have spoken to !!Pikkupstix??, who has promised to teach me ", line++, stage >= 40);
				line(player, "the secrets of !!Summoning?? if I can help dismiss the !!giant??", line++, stage >= 40);
				line(player, "!!wolpertinger??. To do this, I need to bring him !!2 lots of wolf??", line++, stage >= 40);
				line(player, "!!bones??.", line++, stage >= 40);

				if (stage == 30) {
					line(player, "!!I need to get 2 lots of wolf bones.??", line++, inInventory(player, Items.WOLF_BONES_2859, 2));
				} else {
					line(player, "I have given Pikkupstix all of the items he requested.", line++, true);
					line++;
				}
			}
			if (stage >= 40) {
				line(player, "Pikkupstix has given me some !!gold charms??, !!spirit shards??", line++, stage >= 50);
				line(player, "and !!pouches??, with which to make a !!spirit wolf pouch?? and", line++, stage >= 50);
				line(player, "some !!Howl scrolls??. I will then be able to use them to dismiss", line++, stage >= 50);
				line(player, "the !!giant wolpertinger??.", line++, stage >= 50);
			}
			if (stage == 40 && inInventory(player, Items.TRAPDOOR_KEY_12528, 1)) {
				line(player, "I need to open the !!trapdoor?? with the !!trapdoor key?? that I", line++, false);
				line(player, "have been given.", line++, false);
			} else if (stage >= 50 || player.getAttribute("has-key", false)) {
				line(player, "I have unlocked the trapdoor.", line++, true);
			}

			// This part is a shitshow.
			if (stage >= 50 || (stage >= 40 && (inInventory(player, Items.SPIRIT_WOLF_POUCH_12047, 1) || inInventory(player, Items.HOWL_SCROLL_12425, 1)))) {
				line(player, "I need to go into Pikkupstix's !!cellar?? and !!infuse a pouch?? at", line++, stage >= 50);
				line(player, "the obelisk, using the items I have been given.", line++, stage >= 50);
				line++;
				line(player, "I have infused the spirit wolf pouch and made some Howl", line++, stage >= 50);
				line(player, "scrolls. I should speak with !!Pikkupstix?? about how to use", line++, stage >= 50);
				line(player, "them.", line++, stage >= 50);
				line++;
			} else if (stage >= 40 && inInventory(player, Items.SPIRIT_WOLF_POUCH_12047, 2)) {
				line(player, "I have infused the 2 spirit wolf pouches, but I need to", line++, false);
				line(player, "transform one of them into scrolls at the obelisk.", line++, false);
			} else if (stage >= 40 && player.getAttribute("has-key", false)) {
				line(player, "I need to go into Pikkupstix's !!cellar?? and !!infuse a pouch?? at", line++);
				line(player, "the obelisk, using the items I have been given.", line++);
				line(player, "!!I need to bring 2 lots of wolf bones.??", line++, inInventory(player, Items.WOLF_BONES_2859, 2));
				line(player, "!!I need to bring the pouches.??", line++, inInventory(player, Items.POUCH_12155, 2));
				line(player, "!!I need to bring the gold charms.??", line++, inInventory(player, Items.GOLD_CHARM_12158, 2));
				line(player, "!!I need to bring the spirit shards.??", line++, inInventory(player, Items.SPIRIT_SHARDS_12183, 14));
			}

			if (stage >= 50) {
				line(player, "I have been told how to use the spirit wolf pouch and Howl", line++, stage >= 60);
				line(player, "scrolls. I should go back upstairs and confront the !!giant??", line++, stage >= 60);
				line(player, "!!wolpertinger??.", line++, stage >= 60);
				line++;
			}
			if (stage == 50) { // Does not stay.
				if (inInventory(player, Items.SPIRIT_WOLF_POUCH_12047, 1)) {
					line(player, "I have the spirit wolf pouch on me.", line++, false);
				} else {
					line(player, "!!I have lost the spirit wolf pouch.??", line++, false);
				}
				if (inInventory(player, Items.HOWL_SCROLL_12425, 1)) {
					line(player, "I have the Howl scroll on me.", line++, false);
				} else {
					line(player, "!!I have lost the Howl scroll.??", line++, false);
				}
			}

			if (stage >= 60) {
				// Technically, there should be an extra stage speaking to Pikkupstix here, but it is not available.
				line(player, "I have banished the giant !!wolpertinger??. I should speak with", line++, true);
				line(player, "!!Pikkupstix?? to get my reward.", line++, true);
				line++;
				if (player.getSkills().getLevel(Skills.SUMMONING) >= player.getSkills().getStaticLevel(Skills.SUMMONING) || stage >= 100) {
					line(player, "I am feeling drained of Summoning skill points and need to", line++, true);
					line(player, "recharge at the !!obelisk??.", line++, true);
					line++;
					line(player, "I have banished the giant !!wolpertinger?? and refreshed my", line++, stage >= 100);
					line(player, "Summoning skill points. I should speak with !!Pikkupstix?? to", line++, stage >= 100);
					line(player, "get my reward.", line++, stage >= 100);
					line++;
				} else {
					line(player, "I am feeling drained of Summoning skill points and need to", line++);
					line(player, "recharge at the !!obelisk??.", line++);
					line++;
				}
			}

			if (stage >= 100) {
				line(player, "I have been given access to the secrets of Summoning.", line++, true);
				line(player,"<col=FF0000>QUEST COMPLETE!</col>", line++);
				line(player, "!!Reward:??", line++);
				line(player, "1 Quest Point,", line++);
				line(player, "access to the Summoning skill", line++);
				line(player, "275 gold charms", line++);
				line(player, "and 276 Summoning XP", line++);
			}
		}
	}

	@Override
	public void finish(Player player) {
		super.finish(player);
		player.getPacketDispatch().sendString("1 Quest Points", 277, 8+ 2);
		player.getPacketDispatch().sendString("276 Summoning XP", 277, 9+ 2);
		player.getPacketDispatch().sendString("Access to the Summoning", 277, 10+ 2);
		player.getPacketDispatch().sendString("skill.", 277, 11+ 2);
		player.getPacketDispatch().sendString("275 gold charms.", 277, 12+ 2);
		player.getPacketDispatch().sendItemZoomOnInterface(12047, 230, 277, 3+ 2);
		player.getSkills().addExperience(Skills.SUMMONING, 276);
		player.getInventory().add(new Item(12158, 275), player);
		player.removeAttribute("searched-body");
		player.getQuestRepository().syncronizeTab(player);
		player.getInterfaceManager().openInfoBars();
	}

	@Override
	public int[] getConfig(Player player, int stage) {
		int val = getVarp(player, 1178);
		boolean open = val >= 4096;
		boolean closed = val == 2048;
		if (stage == 100) {
			if (val == 5 || val == 0) {
				return new int[] { 1178, 32989 };
			} else if (val == 4101) {
				return new int[] { 1178, 28893 };
			}
			return new int[] { 1178, val };
		}
		return new int[] { 1178, stage > 0 ? 5 + (open ? 4096 : 0) : stage >= 100 ? !closed ? 28893 : 32989 : 0 };
	}

	@Override
	public Quest newInstance(Object object) {
		return this;
	}

}
