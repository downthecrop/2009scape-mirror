package core.game.content.dialogue.book;

import core.game.component.Component;
import core.plugin.Initializable;
import core.game.content.dialogue.DialoguePlugin;
import core.game.node.entity.player.Player;

/**
 * Represents the strong hold notes book.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class StrongholdNotes extends Book {

	/**
	 * Represents the strong hold of security book.
	 */
	public static int ID = 423943;

	/**
	 * Represents the component interface.
	 */
	private static final Component INTERFACE = new Component(26);

	/**
	 * Represents the array of pages for this book.
	 */
	private static final PageSet[] PAGES = new PageSet[] { new PageSet(new Page(new BookLine("Chapters<br><br>" + BLUE + "Description<br>" + BLUE + "Level 1<br>" + BLUE + "Level 2<br>" + BLUE + "Level 3<br>" + BLUE + "Level 4<br>" + BLUE + "Navigation<br>" + BLUE + "Diary<br><br><br><br><br><br><br><br><br><br>", 102)), new Page(new BookLine(BLUE + "Description<br>This stronghold was<br>unearthed by a miner<br>prospecting for new ores<br>around the Barbarian Village.<br>After gathering some<br>equipment he ventured into<br>the maze of tunnels and was<br>missing for a long time. He<br>finally emerged along with<br>copious notes refarding the<br>new beasts and strange<br>experiences which had befallen<br>him. He also mentioned that<br>there was treasure to be had,", 87))), new PageSet(new Page(new BookLine("but no one has been able to<br>wring a word from him about<br>this, he simply flapped his<br>arms and slapped his head.<br>This book details his notes<br>and my diary of exploration.<br>I am exploring to see if I<br>can find out more...", 102)), new Page(new BookLine(BLUE + "Level 1<br>As well as goblins, creatures<br>like a man but also like a cow<br>infest this place! I have never<br>seen anything like this before.<br>The area itself is reminiscent<br>of frontline castles, with<br>many walls, doors and<br>skeletons of dead enemies.<br>I'm sure I hear voices in my<br>head each time I pass<br>through the gates. I have<br>dubbed this level War as it<br>seems like an eternal<br>battleground. I found only", 87))), new PageSet(new Page(new BookLine("one small peaceful area here.", 102)), new Page(new BookLine(BLUE + "Level 2<br>My supplies are running low<br>and I find myself in barren<br>passages with seemingly<br>endless malnourished beasts<br>attacking me, revenous for<br>food. Nothing appears to be<br>able to grow, many<br>adventurers have died<br>through lack of food and the<br>very air appears to suck<br>vitality from me. I've come to<br>call this place famine.", 87))), new PageSet(new Page(new BookLine(BLUE + "Level 3<br>Just breathing in this place<br>makes me shudder at the<br>thought of what foul disease I<br>may contract. The walls and<br>floor ooze and pulsate like<br>something pox ridden. There<br>is a very strange beast whom<br>I narrowly escaped from. At<br>first I thought it to be a<br>cross between a cow and a<br>sheep, something<br>domesticated... but when it<br>looked up at me I was<br>overcome with weakness and", 102)), new Page(new BookLine("barely got way with my life!<br>Luckily I found a small place<br>where I could heal myself<br>and rest a while. I have<br>named this area pestilence for<br>it reeks with decay.", 87))), new PageSet(new Page(new BookLine(BLUE + "Level 4<br>On my first escapade into<br>this place I was utterly<br>shocked. The adventurers<br>who had come before me<br>must have made up a tiny<br>proportion of the skeletons of<br>the dead. Nothing truely<br>alive exists here, even those<br>beings who do wander the<br>halls are not alive as such,<br>but they do know that I am<br>and I get the distinct<br>impression that were they to<br>have their way, I would not", 102)), new Page(new BookLine("be for long! Death is<br>everywhere and thus I shall<br>name this place. There is one<br>small place of life, which was<br>gladdening to find and very<br>worth my while!", 87))), new PageSet(new Page(new BookLine(BLUE + "Navigation<br>After getting lost several<br>times I finally worked out the<br>key to all the ladders and<br>chains around this death<br>infested place. All ropes and<br>chains will take you to the<br>start of the level that you are<br>on. However most ladders will<br>simply take you to the level<br>above. The one esception is<br>the ladder in the bottom level<br>treasure room, which appears<br>to lead through several<br>extremely twisty passages.", 102)), new Page(new BookLine("and eventually takes you out<br>of the dungeon completely.<br>The portals may be used if<br>you are of sufficient level or<br>have already claimed your<br>reward from the treasure<br>room.", 102))), new PageSet(new Page(new BookLine(BLUE + "Diary<br>Day 1<br>Today I set out to find out<br>more about this place. From<br>my research I knew about<br>the sentient doors, imbued by<br>some unkown force to talk<br>to you and ask questions<br>before they will let you pass.<br>I have so far passed these<br>doors without incident, giving<br>the correct answer seems to<br>work a treat.<br><br>Day 2", 102)), new Page(new BookLine("I have fought my way<br>through the fearsome beasts<br>on the first level and am<br>preparing myself to journey<br>deeper. I hope that things are<br>not too difficult further on as<br>I am already sick of bread<br>and cheese for dinner.<br><br>Day 3<br>I ventured down into the<br>famine level today... I was<br>wounded and have returned<br>to the relative safety of the<br>level above. I am going to", 87))), new PageSet(new Page(new BookLine("try to make my way out<br>through the goblins and<br>mancow things... I hope I<br>make it.....", 102)), new Page(new BookLine("", 87))) };

	/**
	 * Represents all the array of all configs to hide.
	 */
	private static final int[] ALL = new int[] { 102, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101 };

	/**
	 * Constructs a new {@code ShieldofArravBook} {@code Object}.
	 */
	public StrongholdNotes(final Player player) {
		super(player, "Stronghold of Security - Notes", 9004, PAGES);
	}

	/**
	 * Constructs a new {@code ShieldofArravBook} {@code Object}.
	 */
	public StrongholdNotes() {
		/**
		 * empty.
		 */
	}

	/**
	 * Method used to display a set of pages.
	 * @param set the set.
	 */
	public void display(Page[] set) {
		player.lock(2);
		player.getInterfaceManager().open(getInterface());
		player.getPacketDispatch().sendInterfaceConfig(getInterface().getId(), 65, index < 1 ? true : false);
		boolean lastPage = index == 7;
		player.getPacketDispatch().sendInterfaceConfig(getInterface().getId(), 67, lastPage ? true : false);
		if (lastPage) {
			finish();
		}
		for (int i : ALL) {
			player.getPacketDispatch().sendString("", 26, i);
		}
		player.getPacketDispatch().sendString(getName(), 26, 3);
		player.getPacketDispatch().sendString("" + (index == 0 ? (index + 1) : (index + 2)), 26, 68);
		player.getPacketDispatch().sendString("" + (index == 0 ? (index + 2) : (index + 3)), 26, 69);
		for (Page page : set) {
			for (BookLine line : page.getLines()) {
				player.getPacketDispatch().sendString(line.getMessage(), getInterface().getId(), line.getChild());
			}
		}
		player.unlock();
	}

	/**
	 * Gets the interface.
	 * @return the interface.
	 */
	public Component getInterface() {
		return INTERFACE;
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new StrongholdNotes(player);
	}

	@Override
	public int[] getIds() {
		return new int[] { ID };
	}
}
