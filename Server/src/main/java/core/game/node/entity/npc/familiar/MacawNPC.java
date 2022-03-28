package core.game.node.entity.npc.familiar;

import core.game.content.dialogue.DialoguePlugin;
import core.game.node.entity.skill.summoning.familiar.BurdenBeast;
import core.game.node.entity.skill.summoning.familiar.Familiar;
import core.game.node.entity.skill.summoning.familiar.FamiliarSpecial;
import core.game.node.entity.skill.summoning.familiar.Forager;
import core.game.node.entity.skill.summoning.familiar.RemoteViewer;
import core.game.node.entity.player.Player;
import core.game.node.item.GroundItemManager;
import core.game.node.item.Item;
import core.game.system.task.Pulse;
import rs09.game.world.GameWorld;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Plugin;
import rs09.plugin.ClassScanner;
import core.plugin.Initializable;
import core.tools.RandomFunction;

/**
 * Represents the Macaw familiar.
 * @author Aero
 * @author Vexia
 */
@Initializable
public class MacawNPC extends Forager {

	/**
	 * The herb items.
	 */
	private static final Item[] HERBS = new Item[] { new Item(249), new Item(251), new Item(253), new Item(255), new Item(257), new Item(2998), new Item(12172), new Item(259), new Item(261), new Item(263), new Item(3000), new Item(265), new Item(2481), new Item(267), new Item(269) };

	/**
	 * The delay until the next special.
	 */
	private int specialDelay;

	/**
	 * Constructs a new {@code MacawNPC} {@code Object}.
	 */
	public MacawNPC() {
		this(null, 6851);
	}

	/**
	 * Constructs a new {@code MacawNPC} {@code Object}.
	 * @param owner The owner.
	 * @param id The id.
	 */
	public MacawNPC(Player owner, int id) {
		super(owner, id, 3100, 12071, 12, HERBS);
	}

	@Override
	public Familiar construct(Player owner, int id) {
		return new MacawNPC(owner, id);
	}

	@Override
	public Plugin<Object> newInstance(Object object) throws Throwable {
		ClassScanner.definePlugin(new MacawDialogue());
		return super.newInstance(object);
	}

	@Override
	protected boolean specialMove(FamiliarSpecial special) {
		if (specialDelay > GameWorld.getTicks()) {
			owner.getPacketDispatch().sendMessage("You must wait one minute until using the macaws special again.");
			return false;
		}
		final Item herb = HERBS[RandomFunction.random(HERBS.length)];
		animate(Animation.create(8013));
		graphics(Graphics.create(1321), 2);
		GameWorld.getPulser().submit(new Pulse(5, owner) {
			@Override
			public boolean pulse() {
				GroundItemManager.create(herb, getLocation(), owner);
				return true;
			}
		});
		specialDelay = GameWorld.getTicks() + 100;
		return true;
	}

	/**
	 * Handles the macaw dialogue.
	 * @author Vexia
	 */
	public final class MacawDialogue extends DialoguePlugin {

		/**
		 * The familiar.
		 */
		private Familiar familiar;

		/**
		 * Constructs a new {@code MacawDialogue} {@code Object}.
		 */
		public MacawDialogue() {
			/**
			 * empty.
			 */
		}

		/**
		 * Constructs a new {@code MacawDialogue} {@code Object}.
		 * @param player the player.
		 */
		public MacawDialogue(final Player player) {
			super(player);
		}

		@Override
		public DialoguePlugin newInstance(Player player) {
			return new MacawDialogue(player);
		}

		@Override
		public boolean open(Object... args) {
			familiar = (Familiar) args[0];
			options("Chat", "Remote view", "Withdraw");
			return true;
		}

		@Override
		public boolean handle(int interfaceId, int buttonId) {
			switch (stage) {
			case 0:
				switch (buttonId) {
				case 1:
					npc("Awh! Gimme the rum! Gimme the rum!");
					stage++;
					break;
				case 2:
					end();
					RemoteViewer.openDialogue(player, familiar);
					break;
				case 3:
					end();
					((BurdenBeast) familiar).openInterface();
					break;
				}
				break;
			case 1:
				player(" I don't think you'll like the stuff. Besides, I think there", "is a law about feeding birds alcohol.");
				stage++;
				break;
			case 2:
				end();
				break;
			}
			return true;
		}

		@Override
		public int[] getIds() {
			return MacawNPC.this.getIds();
		}

	}

	@Override
	public Animation getViewAnimation() {
		return Animation.create(8013);
	}

	@Override
	public int getRandom() {
		return 40;
	}

	@Override
	public int[] getIds() {
		return new int[] { 6851, 6852 };
	}

}
