package core.game.node.entity.skill.fishing;

import core.game.container.Container;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Animation;
import core.tools.RandomFunction;
import org.rs09.consts.Items;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Represents a fishing option.
 * @author Emperor
 */
public enum FishingOption {

	CRAYFISH_CAGE(new Item(13431), 1, Animation.create(619), null, "cage", Fish.CRAYFISH),
	SMALL_NET(new Item(303), 1, Animation.create(621), null, "net", Fish.SHRIMP, Fish.ANCHOVIE),
	BAIT(new Item(307), 5, Animation.create(622), new Item[]{new Item(313)}, "bait", Fish.SARDINE, Fish.HERRING),
	LURE(new Item(309), 20, new Animation(622), new Item[]{new Item(Items.FEATHER_314), new Item(Items.STRIPY_FEATHER_10087)}, "lure", Fish.TROUT, Fish.SALMON, Fish.RAINBOW_FISH), 
	L_BAIT(new Item(307), 25, Animation.create(622), new Item[]{new Item(313)}, "bait", Fish.PIKE), 
	CAGE(new Item(301), 40, Animation.create(619), null, "cage", Fish.LOBSTER), 
	HARPOON(new Item(311), 35, Animation.create(618), null, "harpoon", Fish.TUNA, Fish.SWORDFISH),
	BARB_HARPOON(new Item(10129), 35, Animation.create(618), null, "harpoon", Fish.TUNA, Fish.SWORDFISH), 
	BIG_NET(new Item(305), 16, Animation.create(620), null, "net", Fish.MACKEREL, Fish.COD, Fish.BASS, Fish.SEAWEED),
	N_HARPOON(new Item(311), 76, Animation.create(618), null, "harpoon", Fish.SHARK), 
	H_NET(new Item(303), 62, Animation.create(621), null, "net", Fish.MONKFISH),
	MORTMYRE_ROD(new Item(307), 5, Animation.create(622), new Item[]{new Item(313)},"bait", Fish.SLIMY_EEL),
	LUMBDSWAMP_ROD(new Item(307), 5, Animation.create(622), new Item[]{new Item(313)}, "bait", Fish.SLIMY_EEL, Fish.FROG_SPAWN),
	KBWANJI_NET(new Item(Items.SMALL_FISHING_NET_303), 5, Animation.create(621), null, "net", Fish.KARAMBWANJI),
	KARAMBWAN_VES(new Item(Items.KARAMBWAN_VESSEL_3157), 65, Animation.create(1193), new Item[]{new Item(Items.RAW_KARAMBWANJI_3150)}, "fish", Fish.KARAMBWAN),
	OILY_FISHING_ROD(new Item(Items.OILY_FISHING_ROD_1585), 53, Animation.create(622), new Item[]{new Item(313)}, "bait", Fish.LAVA_EEL);

	public static final HashMap<String,FishingOption> nameMap = new HashMap<>();
	static{
		for(FishingOption option : FishingOption.values()){
			nameMap.putIfAbsent(option.name,option);
		}
	}

	public static FishingOption forName(String name){
		return nameMap.get(name);
	}
	/**
	 * The tool required.
	 */
	private final Item tool;

	/**
	 * The fishing level required.
	 */
	private final int level;

	/**
	 * The fishing animation.
	 */
	private final Animation animation;

	/**
	 * The bait.
	 */
	private final Item[] bait;

	/**
	 * The option name.
	 */
	private final String name;

	/**
	 * The fish to catch.
	 */
	private final Fish[] fish;

	/**
	 * Constructs a new {@code FishingOption} {@code Object}.
	 * @param tool The tool needed.
	 * @param level The fishing level required.
	 * @param animation The animation.
	 * @param fish The fish to catch.
	 */
	FishingOption(Item tool, int level, Animation animation, Item[] bait, String name, Fish... fish) {
		this.tool = tool;
		this.level = level;
		this.animation = animation;
		this.bait = bait;
		this.name = name;
        Arrays.sort(fish, (x, y) -> y.getLevel() - x.getLevel());
		this.fish = fish;
	}

	/**
	 * Method used to get a random {@link Fish}.
	 * @return the {@link Fish}.
	 */
	public Fish rollFish(final Player player) {
		if (this == BIG_NET) {
			switch (RandomFunction.randomize(100)) {
			case 0:
				return Fish.OYSTER;
			case 50:
				return Fish.CASKET;
			case 90:
				return Fish.SEAWEED;
			}
		}
        int visibleLevel = player.getSkills().getLevel(Skills.FISHING);
        int invisibleLevel = visibleLevel + player.getFamiliarManager().getBoost(Skills.FISHING);
        for(Fish f : fish) {
            if(f.getLevel() > player.getSkills().getLevel(Skills.FISHING)) {
                continue;
            }
            if(this == LURE && (player.getInventory().contains(Items.STRIPY_FEATHER_10087, 1) != (f == Fish.RAINBOW_FISH))) {
                continue;
            }
            double chance = f.getSuccessChance(invisibleLevel);
            //System.out.printf("rollFish: %s %s %s %s %s\n", player.getName(), f.getItem().getName(), f.getLowChance(), f.getHighChance(), chance);
            if(RandomFunction.random(0.0, 1.0) < chance) {
                return f;
            }
        }
        return null;
	}

	/**
	 * Gets the start message.
	 * @return The start message.
	 */
	public String getStartMessage() {
		if (name.equals("net")) {
			return "You cast out your net...";
		}
		return "You attempt to catch a fish.";
	}

	/**
	 * Gets the tool.
	 * @return The tool.
	 */
	public Item getTool() {
		return tool;
	}

	/**
	 * Gets the level.
	 * @return The level.
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Gets the animation.
	 * @return The animation.
	 */
	public Animation getAnimation() {
		return animation;
	}

    public String getBaitName() {
        if(bait != null && bait.length > 0) {
            return bait[0].getName();
        } else {
            return "null";
        }
            
    }

    public boolean hasBait(Container inventory) {
        if(bait == null) {
            return true;
        } else {
            boolean any_bait = false;
            for(Item b : bait) {
                any_bait = any_bait || inventory.containsItem(b);
            }
            return any_bait;
        }
    }

    public boolean removeBait(Container inventory) {
        if(bait == null) {
            return true;
        } else {
            // Remove more specific bait (later in the list) first.
            for(int i = bait.length; i > 0; i--) {
                if(inventory.remove(bait[i-1])) {
                    return true;
                }
            }
            return false;
        }
    }

	/**
	 * Gets the name.
	 * @return The name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the fish.
	 * @return The fish.
	 */
	public Fish[] getFish() {
		return fish;
	}

}
