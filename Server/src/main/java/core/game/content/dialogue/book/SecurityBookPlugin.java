package core.game.content.dialogue.book;

import core.game.component.Component;
import core.game.content.dialogue.DialoguePlugin;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import rs09.game.world.GameWorld;

/**
 * Represents the strong hold of security book.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class SecurityBookPlugin extends Book {

	/**
	 * Represents the strong hold of security book.
	 */
	public static int ID = 49610759;

	/**
	 * Represents the component interface.
	 */
	private static final Component INTERFACE = new Component(26);

	/**
	 * Represents the array of pages for this book.
	 */
	private static final PageSet[] PAGES = new PageSet[] { new PageSet(new Page(new BookLine("Chapters", 102), new BookLine("<h1>" + BLUE + "Password Tips</col></h1>", 74), new BookLine("<h1>" + BLUE + "Recovery Questions</col></h1>", 75), new BookLine("<h1>" + BLUE + "Password Tips</col></h1>", 74), new BookLine("<h1>" + BLUE + "Recovery Questions</col></h1>", 75), new BookLine("<h1>" + BLUE + "Other Security Tips</col></h1>", 76), new BookLine("<h1>" + BLUE + "Stringhold of Security</col></h1>", 77)), new Page(new BookLine("<h1>" + BLUE + "Password Tips</col></h1>", 87), new BookLine("A good password should be", 88), new BookLine("easily remembered by", 89), new BookLine("yourself but not easily", 90), new BookLine("guessed by anyone else.", 91), new BookLine("Choose a password that has", 93), new BookLine("both letters and numbers in", 94), new BookLine("it for the best security but", 95), new BookLine("don't make it so hard that", 96), new BookLine("you'll forget it!", 97), new BookLine("Never write your password", 99), new BookLine("down or leave it in a text file", 100), new BookLine("on your computer, someone", 101))), new PageSet(new Page(new BookLine("could find it easily!", 102), new BookLine("Never tell anyone your", 74), new BookLine("password in " + GameWorld.getSettings().getName() + ", not", 75), new BookLine("even a Moderator of any", 76), new BookLine("kind.", 77)), new Page(new BookLine("<h1>" + BLUE + "Recovery Questions</col></h1>", 87), new BookLine("Ideally your recovery", 88), new BookLine("questions should be easily", 89), new BookLine("remembered by you but not", 90), new BookLine("guessable by anyone who", 91), new BookLine("may know you or given", 92), new BookLine("away in conversation. Choose", 93), new BookLine("things that do not change,", 94), new BookLine("like dates or names but don't", 95), new BookLine("choose obvious ones like your", 96), new BookLine("birthday and your sister or", 97), new BookLine("bother's name because lots", 98), new BookLine("of people will know that.", 99))), new PageSet(new Page(new BookLine("<h1>" + BLUE + "Recovery Questions</col></h1>", 102), new BookLine("Bear in mind that recovery", 73), new BookLine("questions will take 14 days to", 74), new BookLine("become active after you have", 75), new BookLine("applied for them to be", 76), new BookLine("changed. This is to protect", 77), new BookLine("your account from hijackers", 78), new BookLine("who may change them.", 79), new BookLine("Never give your password to", 81), new BookLine("ANYONE. This includes", 82), new BookLine("your friends, family, and", 83), new BookLine("moderators in game.", 84), new BookLine("Never leave your account", 86)), new Page(new BookLine("logged on if you are away", 87), new BookLine("from the computer, it only", 88), new BookLine("takes 5 seconds to steal your", 89), new BookLine("account!", 90))), new PageSet(new Page(new BookLine("<h1>" + BLUE + "Stronghold of Security</col></h1>", 102), new BookLine("Location: The Stronghold of", 73), new BookLine("Security, as we call it, is", 74), new BookLine("located under the village filled", 75), new BookLine("with Barbarians. It was", 76), new BookLine("found after they moved their", 77), new BookLine("mining operations and a", 78), new BookLine("miner fell through. The", 79), new BookLine("Stronghold contains many", 80), new BookLine("challenges. Both for those", 81), new BookLine("who enjoy combat and those", 82), new BookLine("who enjoy challenges of the", 83), new BookLine("mind. This book will be very", 84), new BookLine("useful to you in your travels", 85), new BookLine("there.", 86)), new Page(new BookLine("You can find the Stronghold", 87), new BookLine("of Security by looking for a", 88), new BookLine("hole in Barbarian Village.", 89), new BookLine("Be sure to take your combat", 90), new BookLine("equipment though!", 91))) };

	/**
	 * Constructs a new {@code ShieldofArravBook} {@code Object}.
	 */
	public SecurityBookPlugin(final Player player) {
		super(player, "" + GameWorld.getSettings().getName() + " Account Security", 9003, PAGES);
	}

	/**
	 * Constructs a new {@code ShieldofArravBook} {@code Object}.
	 */
	public SecurityBookPlugin() {
		/**
		 * empty.
		 */
	}

	/**
	 * Method used to display a set of pages.
	 * @param set the set.
	 */
	public void display(Page[] set) {
		/*player.lock(2);
		player.getInterfaceManager().open(getInterface());
		player.getPacketDispatch().sendInterfaceConfig(getInterface().getId(), 65, index < 1 ? true : false);
		boolean lastPage = index == 3;
		player.getPacketDispatch().sendInterfaceConfig(getInterface().getId(), 67, lastPage ? true : false);
		if (lastPage) {
			finish();
		}
		for (int i : getHiddenChilds()) {
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
		player.unlock();*/
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
		return new SecurityBookPlugin(player);
	}

	/**
	 * Represents all the array of all configs to hide.
	 */
	private static final int[] ALL = new int[] { 102, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101 };

	/**
	 * Represents the first page hidden configs.
	 */
	private static final int[] FIRST = new int[] { 73, 78, 79, 80, 81, 82, 83, 84, 85, 86, 92, 98 };

	/**
	 * Represents the second stage hidden configs.
	 */
	private static final int[] SECOND = new int[] { 73, 78, 79, 80, 81, 82, 83, 84, 85, 86, 100, 101 };

	/**
	 * Represents the third page hidden configs.
	 */
	private static final int[] THIRD = new int[] { 85, 80, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101 };

	/**
	 * Represents the fourth page hidden configs.
	 */
	private static final int[] FOURTH = new int[] { 92, 93, 94, 95, 96, 97, 98, 99, 100, 101 };

	/**
	 * Gets the hidden childs.
	 * @return the childs.
	 */
	@SuppressWarnings("unused")
	private int[] getHiddenChilds() {
		if (index == 0) {
			return FIRST;
		} else if (index == 1) {
			return SECOND;
		} else if (index == 3) {
			return THIRD;
		} else if (index == 4) {
			return FOURTH;
		}
		return ALL;
	}

	@Override
	public int[] getIds() {
		return new int[] { ID };
	}
}
