package core.game.interaction.item.withitem;

import core.game.node.entity.player.link.diary.DiaryType;
import core.plugin.Initializable;
import org.rs09.consts.Items;
import rs09.game.content.dialogue.SkillDialogueHandler;
import rs09.game.content.dialogue.SkillDialogueHandler.SkillDialogue;
import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.system.task.Pulse;
import core.plugin.Plugin;
import java.util.Objects;

/**
 * Represents the plugin used to make soft clay.
 * @author 'Vexia
 * @date 1/14/14
 */
@Initializable
public final class SoftclayPlugin extends UseWithHandler {

    /**
     * Represents the clay item.
     */
    private static final Item CLAY = new Item(Items.CLAY_434);

    /**
     * Represents the soft clay item.
     */
    private static final Item SOFT_CLAY = new Item(Items.SOFT_CLAY_1761);

    /**
     * Represents the bowl of water item.
     */
    private static final Item BOWL_OF_WATER = new Item(Items.BOWL_OF_WATER_1921);

    /**
     * Represents the empty bowl item.
     */
    private static final Item BOWL = new Item(Items.BOWL_1923);

    /**
     * Represents the empty bucket item.
     */
    private static final Item BUCKET = new Item(Items.BUCKET_1925);

	/**
	 * Represents the bucket of water item.
	 */
	private static final Item BUCKET_OF_WATER = new Item(Items.BUCKET_OF_WATER_1929);

    /**
     * Represents the jug item.
     */
    private static final Item JUG = new Item(Items.JUG_1935);

	/**
	 * Represents the jug of water item.
	 */
	private static final Item JUG_OF_WATER = new Item(Items.JUG_OF_WATER_1937);

    /**
	 * Constructs a new {@code SoftclayPlugin} {@code Object}.
	 */
	public SoftclayPlugin() {
		super(434);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
        addHandler(1921, ITEM_TYPE, this);
		addHandler(1929, ITEM_TYPE, this);
		addHandler(1937, ITEM_TYPE, this);
		return this;
	}

	@Override
	public boolean handle(final NodeUsageEvent event) {
		final Player player = event.getPlayer();
		SkillDialogueHandler handler = new SkillDialogueHandler(player, SkillDialogue.ONE_OPTION, SOFT_CLAY) {
			@Override
			public void create(final int amount, int index) {
				player.getPulseManager().run(new Pulse(2, player) {
					int count;

					@Override
					public boolean pulse() {
						if (!SoftclayPlugin.this.create(player, event)) {
							return true;
						}
						return ++count >= amount;
					}
				});
			}

			@Override
			public int getAll(int index) {
				return player.getInventory().getAmount(CLAY);
			}
		};
		if (player.getInventory().getAmount(CLAY) == 1) {
			create(player, event);
		} else {
			handler.open();
		}
		return true;
	}

	/**
	 * Creates a soft clay.
	 * @param player the player.
	 * @param event the event.
	 * @return {@code True} if so.
	 */
	private boolean create(final Player player, NodeUsageEvent event) {
        Item removeItem = null;
	    Item returnItem = null;
        if (event.getUsedItem().getId() == Items.BUCKET_OF_WATER_1929 || event.getBaseItem().getId() == Items.BUCKET_OF_WATER_1929) {
            removeItem = BUCKET_OF_WATER;
            returnItem = BUCKET;
        }
        if (event.getUsedItem().getId() == Items.BOWL_OF_WATER_1921 || event.getBaseItem().getId() == Items.BOWL_OF_WATER_1921) {
            removeItem = BOWL_OF_WATER;
            returnItem = BOWL;
        }
        if (event.getUsedItem().getId() == Items.JUG_OF_WATER_1937 || event.getBaseItem().getId() == Items.JUG_OF_WATER_1937) {
            removeItem = JUG_OF_WATER;
            returnItem = JUG;
        }

		if (player.getInventory().containsItem(CLAY) && player.getInventory().containsItem(Objects.requireNonNull(removeItem))) {
            player.getInventory().remove(removeItem);
            player.getInventory().remove(CLAY);
            player.getPacketDispatch().sendMessage("You mix the clay and water. You now have some soft, workable clay.");
            player.getInventory().add(SOFT_CLAY);
            player.getInventory().add(returnItem);
            if (!player.getAchievementDiaryManager().hasCompletedTask(DiaryType.LUMBRIDGE, 0, 6) && player.getViewport().getRegion().getId() == 12341) {
                player.getAchievementDiaryManager().finishTask(player, DiaryType.LUMBRIDGE, 0, 6);
            }
			return true;
		} else {
            return false;
        }
	}
}
