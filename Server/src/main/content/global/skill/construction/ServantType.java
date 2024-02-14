package content.global.skill.construction;


import core.game.node.item.Item;
import org.rs09.consts.Items;

/**
 * Represents a type of servant
 * @author Splinter 
 * @version 1.1
 */
public enum ServantType {
	NONE(-1, -1, -1, -1, -1),
	RICK(4235, 500,  6, 20, 60),
	MAID(4237, 1000, 10, 25, 50, new Item(Items.STEW_2003)),
	COOK(4239, 3000, 16, 30, 17, new Item(Items.PINEAPPLE_PIZZA_2301), new Item(Items.CUP_OF_TEA_712)),
	BUTLER(4241, 5000, 20, 40, 12, new Item(Items.CHOCOLATE_CAKE_1897), new Item(Items.CUP_OF_TEA_712)),
	DEMON_BUTLER(4243, 10000, 26, 50, 7, new Item(Items.CURRY_2011))
	;

	/**
	 * The ID of the npc.
	 */
	private int npcId;

	/**
	 * How much this servant costs.
	 */
	private int cost;

	/**
	 * How much this servant is able to bring back.
	 */
	private int capacity;

	/**
	 * The level the player must have before recruiting this servant.
	 */
	private int level;

	/**
	 * How long it takes this servant to return from their duties.
	 */
	private int timer;

	/**
	 * The food items the servant can cook.
	 */
	private Item[] food;

	/**
	 * Constructor
	 */
	private ServantType(int npcId, int cost, int capacity, int level, int timer, Item... food) {
		this.npcId = npcId;
		this.cost = cost;
		this.capacity = capacity;
		this.level = level;
		this.timer = timer;
		this.food = food;
	}
	
	/**
	 * Gets a servant for the NPC id
	 * @param id The NPC id.
	 * @return The servant type.
	 */
	public static ServantType forId(int id){
		for (ServantType s : ServantType.values()){
			if (s.getId() == id){
				return s;
			}
		}
		return null;
	}

	/**
	 * Gets the NPC's id
	 * @return
	 */
	public int getId(){
		return npcId;
	}

	/**
	 * Gets the initial cost
	 * @return
	 */
	public int getCost(){
		return cost;
	}

	/**
	 * Gets the NPC's capacity
	 * @return
	 */
	public int getCapacity(){
		return capacity;
	}

	/**
	 * Gets the level
	 * @return
	 */
	public int getLevel(){
		return level;
	}

	/**
	 * Gets the timer
	 * @return
	 */
	public int getTimer(){
		return timer;
	}

	/**
	 * Gets the food the servant can cook
	 * @return
	 */
	public Item[] getFood(){
		return food;
	}
}