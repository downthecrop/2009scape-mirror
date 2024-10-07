package content.region.misthalin.varrock.quest.whatliesbelow;

import core.plugin.Initializable;
import org.rs09.consts.Items;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.quest.Quest;
import core.game.node.item.Item;
import core.plugin.ClassScanner;

import static core.api.ContentAPIKt.*;

/**
 * The what lies below quest.
 * @author Vexa
 * 
 */
@Initializable
public class WhatLiesBelow extends Quest {

    /**
     * The name of the quest.
     */
    public static final String NAME = "What Lies Below";

    /**
     * The bowl item.
     */
    public static final Item BOWL = new Item(Items.BOWL_1923);

    /**
     * The sin keth diary.
     */
    public static final Item SIN_KETH_DIARY = new Item(Items.SINKETHS_DIARY_11002);

	/**
	 * The empty folder item.
	 */
	public static final Item EMPTY_FOLDER = new Item(Items.AN_EMPTY_FOLDER_11003);

    /**
     * The used folder.
     */
    public static final Item USED_FOLDER = new Item(Items.USED_FOLDER_11006);

    /**
     * The full folder.
     */
    public static final Item FULL_FOLDER = new Item(Items.FULL_FOLDER_11007);

	/**
	 * The rats paper item.
	 */
	public static final Item RATS_PAPER = new Item(Items.RATS_PAPER_11008);

	/**
	 * The rats letter.
	 */
	public static final Item RATS_LETTER = new Item(Items.RATS_LETTER_11009);

    /**
     * The suroks letter.
     */
    public static final Item SUROKS_LETTER = new Item(Items.SUROKS_LETTER_11010);

	/**
	 * The wand item.
	 */
	public static final Item WAND = new Item(Items.WAND_11012);

	/**
	 * The infused item.
	 */
	public static final Item INFUSED_WAND = new Item(Items.INFUSED_WAND_11013);

	/**
	 * The bacon ring.
	 */
	public static final Item BEACON_RING = new Item(Items.BEACON_RING_11014);

	/**
	 * The requirements.
	 */
	private final boolean[] requirements = new boolean[4];

	/**
	 * Constructs a new {@Code WhatLiesBelow} {@Code Object}
     */
	public WhatLiesBelow() {
		super(NAME, 136, 135, 1);
	}

	@Override
	public Quest newInstance(Object object) {
		ClassScanner.definePlugin(new WLBelowPlugin());
		return this;
	}
	
	@Override
	public void drawJournal(Player player, int stage) {
		super.drawJournal(player, stage);
		var line = 12;

		if(stage == 0){
			line(player, "I can start this quest by speaking to !!Rat Burgiss?? on the", line++);
			line(player, "road south of !!Varrock??.", line++);
			line(player, "Before I begin I will need to:", line++);
			line(player, "Have level 35 !!Runecrafting??.", line++, getStatLevel(player, Skills.RUNECRAFTING) >= 35);
			line(player, "Be able to defeat a !!level 47 enemy??.", line++);
			line(player, "I need to have completed the !!Rune Mysteries?? quest.", line++, isQuestComplete(player, "Rune Mysteries"));
			line(player, "Have a !!Mining?? level of 42 to use the !!Chaos Tunnel??.", line++, getStatLevel(player, Skills.MINING) >= 42);
		} else {
			// These are somehow at the top with different stage when crossed out.
			if (stage >= 10) {
				line(player, "!!Rat??, a trader in Varrock, has asked me to help him with a", line++, stage >= 30);
				line(player, "task.", line++, stage >= 30);
			}
			if (stage >= 30) {
				line(player, "!!Surok??, a Wizard in Varrock, has asked me to complete a", line++, stage >= 50);
				line(player, "task for him.", line++, stage >= 50);
			}
			// End

			if (stage >= 10) {
				line(player, "I need to kill !!outlaws?? west of Varrock so that I can collect 5", line++, stage >= 20);
				line(player, "of Rat's !!papers??.", line++, stage >= 20);
				if (inInventory(player, Items.FULL_FOLDER_11007, 1)) {
					line(player, "I should take the !!full folder?? back to Rat.", line++);
				}
			}
			if (stage >= 20) {
				line(player, "I have delivered Rat's folder to him. Perhaps I should", line++, stage >= 30);
				line(player, "should speak to him again.", line++, stage >= 30);
				// Should be separated stages
				line(player, "I need to deliver !!Rat's?? letter to !!Surok Magis?? in !!Varrock??.", line++, stage >= 30);
				// Should be separated stages
				line(player, "I need to talk to !!Surok?? about the secret he has for me.", line++, stage >= 30);
			}
			if (stage >= 30) {
				line(player, "I need to infuse the !!metal wand?? with !!chaos runes?? at the", line++, stage >= 50);
				line(player, "!!Chaos Altar??. I also need to find or buy an empty !!bowl??.", line++, stage >= 50);
			}
			if (stage >= 50) {
				line(player, "I need to take the !!glowing wand?? I have created back to", line++, true);
				line(player, "!!Surok?? in Varrock along with an empty !!bowl??.", line++, true);
				// Should be separated stages
				line(player, "I need to deliver !!Surok's letter?? to !!Rat?? who is waiting for me", line++, true);
				line(player, "south of Varrock.", line++, true);
				// Should be separated stages
				line(player, "I should speak to !!Rat?? again; he is waiting for me south of", line++, stage >= 60);
				line(player, "Varrock.", line++, stage >= 60);
			}
			if (stage >= 60) {
				line(player, "I need to speak to !!Zaff?? of !!Zaff's Staffs?? in Varrock.", line++, stage >= 70);
			}
			if (stage >= 70) {
				line(player, "I need to tell !!Surok?? in Varrock that he is under arrest.", line++, stage >= 80);
			}
			if (stage >= 80) {
				line(player, "I need to defeat !!King Roald?? in Varrock so that !!Zaff?? can", line++, true);
				line(player, "remove the mind-control spell.", line++, true);
				// Should be separated stages
				line(player, "I need to tell !!Rat?? what has happened; he is waiting for me", line++, stage >= 100);
				line(player, "south of Varrock.", line++, stage >= 100);
			}
			if (stage >= 100) {
				line++;
				line++;
				line(player,"<col=FF0000>QUEST COMPLETE!</col>", line++);
				line++;
				line(player, "I have been given information about the !!Chaos Tunnel??.", line++);
				line(player, "Zaff has given me the !!Beacon Ring??.", line++);
				line++;
				line(player, "I have also been given !!8,000 Runecrafting XP, 2000??.", line++);
				line(player, "!!Defence XP?? and !!1 Quest Point??.", line++);
			}
		}
	}

	@Override
	public void start(Player player) {
		super.start(player);
		player.getInventory().add(EMPTY_FOLDER, player);
	}

	@Override
	public void finish(Player player) {
		super.finish(player);
		player.getPacketDispatch().sendString("8,000 Runecrafting XP", 277, 8+ 2);
		player.getPacketDispatch().sendString("2,000 Defence XP", 277, 9+ 2);
		player.getPacketDispatch().sendString("Beacon Ring", 277, 10+ 2);
		player.getPacketDispatch().sendString("Knowledge of Chaos Tunnel", 277, 11+ 2);
		player.getPacketDispatch().sendItemZoomOnInterface(BEACON_RING.getId(), 235, 277, 3+ 2);
		player.getSkills().addExperience(Skills.RUNECRAFTING, 8000);
		player.getSkills().addExperience(Skills.DEFENCE, 2000);
		player.getQuestRepository().syncronizeTab(player);
	}


	@Override
	public boolean hasRequirements(Player player) {
		requirements[0] = player.getSkills().getStaticLevel(Skills.RUNECRAFTING) >= 35;
		requirements[1] = false;
		requirements[3] = player.getSkills().getStaticLevel(Skills.MINING) >= 42;
		requirements[2] = player.getQuestRepository().isComplete("Rune Mysteries");
		return requirements[0] && requirements[2] && requirements[3];
	}

	@Override
	public int[] getConfig(Player player, int stage) {
		int id = 992;
		if (stage >= 40 && stage != 100) {
			return new int[] { id, (1 << 8) + 1 };
		}
		if (stage == 0) {
			return new int[] { id, 0 };
		} else if (stage > 0 && stage < 100) {
			return new int[] { id, 1 };
		}
                setVarp(player, 1181, (1 << 8) + (1 << 9), true);
		return new int[] { id, 502 };
	}
	
}
