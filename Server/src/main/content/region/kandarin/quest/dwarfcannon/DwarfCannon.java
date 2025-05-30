package content.region.kandarin.quest.dwarfcannon;

import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.quest.Quest;
import core.game.node.item.Item;
import core.plugin.Initializable;
import core.plugin.ClassScanner;
import core.game.node.entity.skill.Skills;

import static core.api.ContentAPIKt.*;
import content.data.Quests;

/**
 * Represents the dwarf cannon quest.
 * @author Vexia
 */
@Initializable
public class DwarfCannon extends Quest {
	/**
	 * The dwarf remain item.
	 */
	public static final Item DWARF_REMAINS = new Item(0);

	/**
	 * The tool kit item.
	 */
	public static final Item TOOL_KIT = new Item(1);

	/**
	 * The nulodion notes.
	 */
	public static final Item NULODION_NOTES = new Item(3);

	/**
	 * The mould item.
	 */
	public static final Item MOULD = new Item(4);
	public static int[] railVarbits = new int[] { 2240, 2241, 2242, 2243, 2244, 2245 };

	/**
	 * Constructs a new {@Code DwarfCannon} {@Code Object}
	 */
	public DwarfCannon() {
		super(Quests.DWARF_CANNON, 49, 48, 1);
	}
	
	@Override
	public Quest newInstance(Object object) {
		ClassScanner.definePlugin(new LollkDialogue());
		ClassScanner.definePlugin(new NulodionDialogue());
		ClassScanner.definePlugin(new CaptainLawgofNPC());
		ClassScanner.definePlugin(new CaptainLawgofDialogue());
		ClassScanner.definePlugin(new DwarfCannonPlugin());
		return this;
	}

	@Override
	public void drawJournal(Player player, int stage) {
		super.drawJournal(player, stage);
		switch (stage) {
		case 0:
			line(player, "<blue>I can start this quest by speaking to <red>Lawgof the Dwarven<n><red>Captain of the Black Watch <blue>, he is defending an area<n><red>North-west of the Fishing Guild <blue>against <red>goblin <blue>attack.", 11);
			break;
		case 10:
			line(player, "<str>I have spoken to Captain Lawgof, he recruited me into the<n><str>Black Guard and asked me to help the dwarves.<n><n>" + (allRailsFixed(player) ? "<str>I have repaired all the broken railings,<n><blue>I should report back to <red>Captain Lawgof." : "<blue>My first task is to <red>fix the broken railings<n><blue>in the dwarves defensive perimeter."), 11);
			break;
		case 20:
			line(player, "<str>I have spoken to Captain Lawgof, he recruited me into the<n><str>Black Guard and asked me to help the dwarves.<n><n>" + (player.hasItem(DWARF_REMAINS) ? "<str>I went to the watchtower where I found the remains of<n><str>Gilob.<n><blue>I should take them back to <red>Captain Lawgof." : "<str>I have repaired all the broken railings,<n><blue>Captain Lawgof has asked me to check up on his guards at<n><red>the watchtower <blue>to the South of this camp."), 11);
			break;
		case 30:
			line(player, "<str>I have spoken to Captain Lawgof, he recruited me into the<n><str>Black Guard and asked me to help the dwarves.<n><n><str>I gave the remains to Captain Lawgof.<n><blue>he sent me to find the <red>Goblin base<blue>, South-east of the<n><blue>camp.", 11);
			break;
		case 40:
			line(player, "<str>I have spoken to Captain Lawgof, he recruited me into the<n><str>Black Guard and asked me to help the dwarves.<n><n><str>I have rescued Lollk and sent him back to the Captain.<n><blue>I need to <red>speak to Captain Lawgof <blue>again.", 11);
			break;
		case 50:
			line(player, "<str>I have spoken to Captain Lawgof, he recruited me into the<n><str>Black Guard and asked me to help the dwarves.<n><n><blue>Captain Lawgof has asked me to <red>fix the multicannon.", 11);
			break;
		case 60:
			line(player, "<str>I have spoken to Captain Lawgof, he recruited me into the<n><str>Black Guard and asked me to help the dwarves.<n><n><str>I've fixed the broken multicannon,<n><blue>I need to <red>speak to Captain Lawgof <blue>again.", 11);
			break;
		case 70:
			line(player, "<str>I have spoken to Captain Lawgof, he recruited me into the<n><str>Black Guard and asked me to help the dwarves.<n><n><blue>Captain Lawgof asked me to find <red>Nulodion the Engineer<blue>, he<n><blue>needs to know what <red>ammunition the multicannon <blue>fires.", 11);
			break;
		case 80:
			line(player, "<str>I have spoken to Captain Lawgof, he recruited me into the<n><str>Black Guard and asked me to help the dwarves.<n><n><str>I've spoken to Nulodion,<n><str>He gave me an ammo mould and notes<n><blue>I need to <red>speak to Captain Lawgof <blue>again.", 11);
			break;
		case 100:
			line(player, "<str>I have spoken to Captain Lawgof, he recruited me into the<n><str>Black Guard and asked me to help the dwarves.<n><n><str>I've helped the dwarves keep out the Goblins, and found<n><str>the remains of their friend.<n><str>I fixed the cannon and got a mould for Captain Lawgof.<n><blue>I can now <red>buy a multicannon <blue>from <red>Nulodion <blue>as a reward.<n><n><col=FF0000>QUEST COMPLETE!", 11);
			break;
		}
	}

	@Override
	public void start(Player player) {
		super.start(player);
                setVarp(player, 0, 1);
	}

	@Override
	public void finish(Player player) {
		super.finish(player);
		player.getPacketDispatch().sendString("1 Quest Point", 277, 8 + 2);
		player.getPacketDispatch().sendString("750 Crafting XP", 277, 9 + 2);
		player.getPacketDispatch().sendString("Permission to purchase and", 277, 10 + 2);
		player.getPacketDispatch().sendString("use the Dwarf Multicannon", 277, 11 + 2);
		player.getPacketDispatch().sendItemZoomOnInterface(TOOL_KIT.getId(), 235, 277, 3 + 2);
		player.getSkills().addExperience(Skills.CRAFTING, 750);
		player.getQuestRepository().syncronizeTab(player);
	}

	@Override
	public int[] getConfig(Player player, int stage) {
		int val = 0;
		if (stage >= 100) {
			val = 11;
		} else if (stage > 0 && stage < 100) {
                        val = getVarp(player, 0);
		}
		return new int[] { 0, val };
	}
	
        public static boolean allRailsFixed (Player player) {
            for (int i = 0; i < 6; i++) {
                if (getVarbit(player, railVarbits[i]) != 1)
                    return false;
            }
            return true;
        }
}
