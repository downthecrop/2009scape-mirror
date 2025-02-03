package content.global.skill.construction;


import core.game.node.entity.npc.NPC;
import core.game.node.item.Item;
import org.json.simple.JSONObject;

/**
 * Represents a player's servant.
 * @author Emperor
 *
 */
public final class Servant extends NPC {

	/**
	 * The servant type.
	 */
	private final ServantType type;
	
	/**
	 * The item the servant is carrying.
	 */
	private Item item;
	
	/**
	 * The amount this servant has been used.
	 */
	private int uses;
	
	/**
	 * If the servant is greeting players entering the house.
	 */
	private boolean greet;
	
	/**
	 * Constructs a new {@code Servant} {@code Object}.
	 * @param type The servant type.
	 */
	public Servant(ServantType type) {
		super(type.getId());
		this.type = type;
	}

	/**
	 * Parses the servant from the save file.
	 * @return The servant.
	 */
	public static Servant parse(JSONObject data){
		int type = Integer.parseInt( data.get("type").toString());
		Servant servant = new Servant(ServantType.values()[type]);
		servant.uses = Integer.parseInt( data.get("uses").toString());
		Object itemRaw = data.get("item");
		if(itemRaw != null){
			JSONObject item = (JSONObject) itemRaw;
			servant.item = new Item(Integer.parseInt(item.get("id").toString()),Integer.parseInt(item.get("amount").toString()));
		}
		servant.greet = (boolean) data.get("greet");
		return servant;
	}

	/**
	 * Gets the item value.
	 * @return The item.
	 */
	public Item getItem() {
		return item;
	}

	/**
	 * Sets the item value.
	 * @param item The item to set.
	 */
	public void setItem(Item item) {
		this.item = item;
	}

	/**
	 * Gets the uses value.
	 * @return The uses.
	 */
	public int getUses() {
		return uses;
	}

	/**
	 * Sets the uses value.
	 * @param uses The uses to set.
	 */
	public void setUses(int uses) {
		this.uses = uses;
	}

	/**
	 * Gets the greet value.
	 * @return The greet.
	 */
	public boolean isGreet() {
		return greet;
	}

	/**
	 * Sets the greet value.
	 * @param greet The greet to set.
	 */
	public void setGreet(boolean greet) {
		this.greet = greet;
	}

	/**
	 * Gets the type value.
	 * @return The type.
	 */
	public ServantType getType() {
		return type;
	}
}