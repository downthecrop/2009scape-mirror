package core.game.content.dialogue.book;

import core.game.component.Component;
import core.game.content.dialogue.DialoguePlugin;
import core.plugin.Initializable;
import core.game.node.entity.player.Player;

/**
 * Represents the strong hold of security book.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class KeldagrimRuleBook extends Book {

	/**
	 * Represents the id of the book.
	 */
	public static final int ID = 496107759;

	/**
	 * Represents the component interface.
	 */
	private static final Component INTERFACE = new Component(26);

	/**
	 * Represents the array of pages for this book.
	 */
	private static final PageSet[] PAGES = new PageSet[] { new PageSet(new Page(new BookLine("Rules", 102), new BookLine("1 - Offensive Language", 74), new BookLine("2 - Item Scamming", 75), new BookLine("3 - Password Scamming", 76), new BookLine("4 - Cheating/Bug Abuse", 77), new BookLine("5 - Staff Impersonation", 78), new BookLine("6 - Account Sharing/Trading", 79), new BookLine("7 - Using Third Party", 80), new BookLine("Software", 81), new BookLine("8 - Multi Logging-in", 82), new BookLine("9 - Encouraging Others to", 83), new BookLine("Break Rules", 84), new BookLine("10 - False Representation", 85), new BookLine("11 - Website Advertising", 86)), new Page(new BookLine("12 - Read World Item", 87), new BookLine("Trading", 88), new BookLine("13 - Asking For Personal", 89), new BookLine("Details", 90), new BookLine("14 - Misuse of Official", 91), new BookLine("Forums", 92), new BookLine("15 - Advert Blocking", 93))), new PageSet(new Page(new BookLine(BLUE + "1 - Offensive Language", 102), new BookLine("You must not use any", 74), new BookLine("language which is offensive,", 75), new BookLine("racist or obscene. Remember,", 76), new BookLine("it's nice to be nice!", 77)), new Page(new BookLine(BLUE + "2 - Item Scamming", 87), new BookLine("You must not scam or", 89), new BookLine("deceive other players. This", 90), new BookLine("includes claiming that items", 91), new BookLine("are rare when they are not,", 92), new BookLine("team scamming or telling", 93), new BookLine("players that you can", 94), new BookLine("upgrade' their armour in", 95), new BookLine("any way!", 96))), new PageSet(new Page(new BookLine(BLUE + "3 - Password Scamming", 102), new BookLine("Asking for - or trying to", 74), new BookLine("obtain - another player's", 75), new BookLine("password in any way will not", 76), new BookLine("be tolerated, even in jest!", 77)), new Page(new BookLine(BLUE + "4 - Cheating/Bug Abuse", 87), new BookLine("A bug is a technical glitch", 89), new BookLine("found in the game. You", 90), new BookLine("must not use or attempt to", 91), new BookLine("use any cheats or errors that", 92), new BookLine("you find in our software.", 93), new BookLine("Any bugs found must be", 94), new BookLine("reported to Keldagrim", 95), new BookLine("immediately, by clicking the", 96), new BookLine("'Report a bug/fault' link", 97), new BookLine("found on the main page.", 98))), new PageSet(new Page(new BookLine(BLUE + "5 - Staff Impersonation", 102), new BookLine("You should not attempt to", 74), new BookLine("impersonate Keldagrim staff in", 75), new BookLine("any way.", 76)), new Page(new BookLine(BLUE + "6 - Account Sharing/Trading", 87), new BookLine("Each account should only be", 89), new BookLine("used by ONE person and", 90), new BookLine("ONE person alone.", 91), new BookLine("Remember that trying to", 92), new BookLine("buy, sell, borrow or give", 93), new BookLine("away an account is against", 94), new BookLine("the rules and will land you in", 95), new BookLine("trouble when caught!", 96))), new PageSet(new Page(new BookLine(BLUE + "7 - Using Third Party <br>" + BLUE + "Software<br><br>You must not use other<br>programs to gain an unfair<br>advantage in the game.", 102)), new Page(new BookLine(BLUE + "8 - Multi Logging-in</col><br><br>If you create more than one<br>Keldagrim account, they<br>must not interact. This<br>includes giving items to a<br>friend to transfer to another<br>account you own.", 87))), new PageSet(new Page(new BookLine(BLUE + "9 - Encouraging Others to<br>" + BLUE + "Break Rules</col><br><br>You must not encourage<br>others to break any of the<br>Keldagrim rules.", 102), new BookLine(BLUE + "10 - False Representation</col><br><br>You must not misuse<br>Keldagrim Customer<br>Support.Trying to get<br>another player into trouble<br>by reporting them for no<br>reason or framing them is an<br>abuse of the Customer<br>Support Team's service.<br> want to help as many<br>players as possible and so the<br>Customer Support Team's<br>service must be used<br>appropriately and treated with", 87))), new PageSet(new Page(new BookLine("respect at all times.", 102), new BookLine(BLUE + "11 - Website Advertising</col><br><br>You are not allowed to<br>actively advertise any<br>websites, fan-sites, IRC<br>channels or product<br>anywhere in Keldagrim,<br>including the Keldagrim<br>forums.", 87)), new Page()), new PageSet(new Page(new BookLine(BLUE + "12 - Real World Item<br>" + BLUE + "Trading<br><br></col>Keldagrim items must only<br>be exchanged for other items<br>or service within the game.<br>Buying or selling items<br>outside of the Keldagrim<br>enviironment is not in the<br>spirit of the game and is easy<br>for Keldagrim to trace!", 102), new BookLine(BLUE + "13 - Asking for Personal<br>" + BLUE + "Details<br><br></col>To protect player's safety<br>and privacy, you must not<br>ask for personal details. This<br>includes full names, phone<br>numbers, MSN, AIM, email<br>and home/school addresses!", 87))), new PageSet(new Page(new BookLine(BLUE + "14 - Misuse of Official<br>" + BLUE + "Forums</col><br><br>Forums must be used in<br>accordance with the Forum<br>Code of Conduct and treated<br>with respect at all times.", 102), new BookLine(BLUE + "15 - Advert Blocking<br><br>Blocking the adverts in the<br>free to play version of<br>Keldagrim is against the<br>rules.", 87))) };

	/**
	 * Constructs a new {@code ShieldofArravBook} {@code Object}.
	 */
	public KeldagrimRuleBook(final Player player) {
		super(player, "Keldagrim Rules", -1, PAGES);
	}

	/**
	 * Constructs a new {@code ShieldofArravBook} {@code Object}.
	 */
	public KeldagrimRuleBook() {
		/**
		 * empty.
		 */
	}

	/**
	 * Method used to display a set of pages.
	 * @param set the set.
	 */
	@Override
	public void display(Page[] set) {
		player.lock(2);
		player.getInterfaceManager().open(getInterface());
		player.getPacketDispatch().sendInterfaceConfig(getInterface().getId(), 65, index < 1 ? true : false);
		boolean lastPage = index == 8;
		player.getPacketDispatch().sendInterfaceConfig(getInterface().getId(), 67, lastPage ? true : false);
		if (lastPage) {
			finish();
		}
		for (int i : getHiddenChilds()) {
			player.getPacketDispatch().sendString("", 26, i);
		}
		int page1 = index == 0 ? 1 : index == 1 ? 3 : index == 2 ? 5 : index == 3 ? 7 : index == 4 ? 9 : index == 5 ? 11 : index == 6 ? 13 : index == 7 ? 15 : index == 8 ? 17 : index == 9 ? 19 : 21;
		player.getPacketDispatch().sendString(getName(), 26, 3);
		player.getPacketDispatch().sendString("" + page1, 26, 68);
		player.getPacketDispatch().sendString("" + (page1 + 1), 26, 69);
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
		return new KeldagrimRuleBook(player);
	}

	/**
	 * Represents all the array of all configs to hide.
	 */
	private static final int[] ALL = new int[] { 102, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101 };

	/**
	 * Gets the hidden childs.
	 * @return the childs.
	 */
	private int[] getHiddenChilds() {
		return ALL;
	}

	@Override
	public int[] getIds() {
		return new int[] { ID };
	}
}
