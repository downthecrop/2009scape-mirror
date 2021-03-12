package core.game.node.entity.skill.construction;


import core.game.node.entity.npc.NPC;
import core.game.node.item.Item;
import org.json.simple.JSONObject;

import java.nio.ByteBuffer;

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
	 * Saves the servant details.
	 * @param buffer The buffer to write on.
	 */
	public void save(ByteBuffer buffer) {
		buffer.put((byte) type.ordinal());
		buffer.putShort((byte) uses);
		if (item == null) {
			buffer.putShort((short) -1);
		} else {
			buffer.putShort((short) item.getId());
			buffer.putInt(item.getAmount());
		}
		buffer.put((byte) (greet ? 1 : 0));
	}
	
	/**
	 * Parses the servant from the buffer.
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

	public static Servant parse(ByteBuffer buffer) {
		int type = buffer.get();
		if (type == -1) {
			return null;
		}
		Servant servant = new Servant(ServantType.values()[type]);
		servant.uses = buffer.getShort() & 0xFFFF;
		int itemId = buffer.getShort() & 0xFFFF;
		if ((short) itemId != -1) {
			servant.item = new Item(itemId, buffer.getInt());
		}
		servant.greet = buffer.get() == 1;
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