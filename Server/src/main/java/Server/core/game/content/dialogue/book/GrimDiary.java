package core.game.content.dialogue.book;

import core.game.component.Component;
import core.game.content.dialogue.DialogueInterpreter;
import core.game.content.dialogue.DialoguePlugin;
import core.plugin.Initializable;
import core.game.node.entity.player.Player;

/**
 * The grim diary book.
 * @author Vexia
 */
@Initializable
public class GrimDiary extends Book {

	/**
	 * Represents the component interface.
	 */
	private static final Component INTERFACE = new Component(26);

	/**
	 * Represents the array of pages for this book.
	 */
	private static final PageSet[] PAGES = new PageSet[] { new PageSet(new Page(new BookLine("My Diary<br><br>    by Grim<br><br>" + RED + "'''''''''''''''''''''''''''''''<br>" + RED + "''''12th Bennath<br>" + RED + "'''''''''''''''''''''''''''''''<br>I had such a busy day<br>dealing out death today<br>It's not as easy being grim.<br>Realised that Alfonse is<br>such a good servant. He<br><br><br><br><br><br>", 102)), new Page(new BookLine("seems to have the house<br>in full working order. I<br>shall have to congratulate<br>him tomorrow. Spent<br>some well-deserved time<br>sharpening my scythe -<br>Alfonse kindly reminded<br>me to put the <col=FF0000>sharpener<br></col>back in the <col=FF0000>cabinet</col>. Such<br>a good chap.", 87))), new PageSet(new Page(new BookLine("<br><br>" + RED + "'''''''''''''''''''''''''''''''<br>" + RED + "'''13th Bennath<br>" + RED + "'''''''''''''''''''''''''''''''<br></col>Went to the Wilderness<br>today. Plenty of foolish<br>people surrendering their<br>lives to me without<br>thought. My back is<br>killing me from all the<br>standing around waiting.", 102)), new Page(new BookLine("Must get that seen to.<br>Got my <col=FF0000>robes</col> stained<br>from one victim. Simply<br>ruined! I decided to<br>throw them in the<br><col=FF0000>fireplace</col> - will light the<br>fire soon. Oh, and I must<br>remember to call mother.", 87))), new PageSet(new Page(new BookLine("<br><br>" + RED + "'''''''''''''''''''''''''''''''<br>" + RED + "'''14th Bennath<br>" + RED + "'''''''''''''''''''''''''''''''<br></col>A tragic day. Alfonse and<br>I were in the garden<br>looking at the state of the<br>spider nest. I patted my<br>trusty servant on the<br>back in thanks for all his<br>hard work. Sadly, my<br>touch of death killed him", 102)), new Page(new BookLine("instantly. Feel quite guilty.", 87))), new PageSet(new Page(new BookLine("<br><br>" + RED + "'''''''''''''''''''''''''''''''<br>" + RED + "'''20th Bennath<br>" + RED + "'''''''''''''''''''''''''''''''<br></col>House is getting into<br>quite a state without<br>Alfonse - things strewn all<br>over the place. Almost<br>trod on the eye of my<br>mentor, eww. I put the<br><col=FF0000>eye</col> back on the <col=FF0000>shelf</col> so<br>he can watch over me", 102)), new Page(new BookLine("and make sure I stay<br>true to his teachings.<br>Went into Varrock to<br>buy some new robes -<br>people kept running away<br>in fear, so it was difficult<br>to find a sale.", 87))), new PageSet(new Page(new BookLine("<br><br>" + RED + "'''''''''''''''''''''''''''''''<br>" + RED + "'''21st Bennath<br>" + RED + "'''''''''''''''''''''''''''''''<br></col>Decided to spend a bit of<br>time tidying today - it<br>really isn't an easy job.<br>In my activities I found<br>my old <col=FF0000> 'Voice of Doom'<br></col>potion on the <col=FF0000>bookcase</col> -<br>perfect for giving people a<br>good scare. Oh, I do love", 102)), new Page(new BookLine("my job.<br><br><br>" + RED + "'''''''''''''''''''''''''''''''<br>" + RED + "'''22nd Bennath<br>" + RED + "'''''''''''''''''''''''''''''''</col><br>Ordered a new servant<br>today from the agency<br>and got a 10% discount<br>for getting past the<br>1000th servant mark.", 87))), new PageSet(new Page(new BookLine("Woo hoo! The agency<br>sent me his <col=FF0000>Last Will and<br>Testement</col>. Shall have to<br><col=FF0000>sit on that</col> for a while.<br><br>" + RED + "'''''''''''''''''''''''''''''''<br>" + RED + "'''23rd Bennath<br>" + RED + "'''''''''''''''''''''''''''''''<br></col>Aquired some bones<br>today. Muncher should<br>appreciate them as a treat", 102)), new Page(new BookLine("the next time he behaves.<br>The problem is there<br>aren't many barriers he<br>can't devour, so I decided<br>to lock the <col=FF0000>bones</col> up in<br>the <col=FF0000>chest</col>.<br><br>" + RED + "'''''''''''''''''''''''''''''''" + RED + "<br>" + RED + "''''24th Bennath<br>" + RED + "'''''''''''''''''''''''''''''''<br>My plan to make undead", 87))), new PageSet(new Page(new BookLine("fish is going quite well. I<br>managed to obtain a<br>resurrection <col=FF0000>hourglass</col><br>today,so have added that<br>to the <col=FF0000>fishtank </col>to finish<br>off the process. It's so<br>difficult to have pets when<br>everything you touch dies<br>horrifically. I remember<br>having a rabbit once that<br>exploded when I fed it a", 102)), new Page(new BookLine("carrot!<br><br>" + RED + "'''''''''''''''''''''''''''''''<br>" + RED + "'''25th Bennath<br>" + RED + "'''''''''''''''''''''''''''''''<br></col>Got back home today to<br>find Muncher has run<br>around the house<br>creating havok - he even<br>ate the postman! I", 87))), new PageSet(new Page(new BookLine("scolded him. So hopefully<br>he won't do it again<br>anytime soon. All my<br>things are in such a<br>mess. I'm surely going to<br>have to find someone to<br>tidy things up before my<br>new servant arrives.<br>Don't want to seem<br>totally incapable of looking<br>after myself.", 102)), new Page(new BookLine("", 87))), };

	/**
	 * Represents all the array of all configs to hide.
	 */
	private static final int[] ALL = new int[] { 102, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101 };

	/**
	 * Constructs a new {@code GrimDiary} {@code Object}
	 * @param player the player.
	 */
	public GrimDiary(final Player player) {
		super(player, "Diary of Death", 2739823, PAGES);
	}

	/**
	 * Constructs a new {@code GrimDiary} {@code Object}
	 */
	public GrimDiary() {
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
		return new GrimDiary(player);
	}

	@Override
	public int[] getIds() {
		return new int[] { DialogueInterpreter.getDialogueKey("grim-diary") };
	}

}
