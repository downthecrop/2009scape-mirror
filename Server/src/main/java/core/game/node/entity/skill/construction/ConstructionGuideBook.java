package core.game.node.entity.skill.construction;


import core.game.content.dialogue.DialogueInterpreter;
import core.game.content.dialogue.DialoguePlugin;
import core.game.content.dialogue.book.Book;
import core.game.content.dialogue.book.Page;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import rs09.game.world.GameWorld;
import core.plugin.Initializable;

/**
 * Handles the construction guide book.
 * @author Emperor
 *
 */
@Initializable
public class ConstructionGuideBook extends Book {

	/**
	 * The resources used in beta.
	 */
	private static final Item[] RESOURCES = {
		new Item(8794),
		new Item(2347),
		new Item(4819, 500000),
		new Item(4820, 500000),
		new Item(1539, 500000),
		new Item(4821, 500000),
		new Item(4822, 500000),
		new Item(4823, 500000),
		new Item(4824, 500000),
		new Item(961, 500000),
		new Item(8779, 500000),
		new Item(8781, 500000),
		new Item(8783, 500000),
		new Item(8791, 500000),
		new Item(8785, 500000),
		new Item(8787, 500000),
		new Item(8789, 500000), //16
		new Item(8418, 500000),
		new Item(8420, 500000),
		new Item(8422, 500000),
		new Item(8424, 500000),
		new Item(8426, 500000),
		new Item(8428, 500000),
		new Item(8430, 500000),
		new Item(8432, 500000),
		new Item(8434, 500000),
		new Item(8436, 500000),
	};
	
	/**
	 * Constructs a new {@code ConstructionGuideBook} {@code Object}.
	 */
	public ConstructionGuideBook() {
		/*
		 * empty.
		 */
	}
	
	/**
	 * Constructs a new {@code ConstructionGuideBook} {@code Object}.
	 * @param player
	 */
	public ConstructionGuideBook(Player player) {
		super(player, "Construction guide book", DialogueInterpreter.getDialogueKey("book:conguide"));
	}
	
	@Override
	public boolean open(final Player player) {
    	if (GameWorld.getSettings().isDevMode() && GameWorld.getSettings().isBeta()) {
	    	for (Item item : RESOURCES) {
	    		if (!player.getInventory().contains(item.getId(), item.getAmount())) {
	    			player.getInventory().add(item, player);
	    		}
	    	}
	  	}
    	interpreter.sendPlainMessage(false, "Upon reading the book you discover you're supposed to", "use these resources to test out construction.", "Report all bugs on the forums.");
    	return true;
	}
	
	@Override
	public void finish() {
		
	}

    @Override
    public void display(Page[] set) {
    	
    }
	
	@Override
	public DialoguePlugin newInstance(Player player) {
		return new ConstructionGuideBook(player);
	}

	@Override
	public int[] getIds() {
		return new int[] { DialogueInterpreter.getDialogueKey("book:conguide") };
	}

}