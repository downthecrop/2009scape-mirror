package core.game.node.entity.npc.familiar;

import core.game.content.dialogue.DialogueInterpreter;
import core.game.content.dialogue.DialoguePlugin;
import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.entity.combat.ImpactHandler.HitsplatType;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.skill.summoning.familiar.Familiar;
import core.game.node.entity.skill.summoning.familiar.FamiliarSpecial;
import core.game.node.entity.skill.summoning.familiar.Forager;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.tools.RandomFunction;
import org.rs09.consts.Items;
import rs09.game.node.entity.skill.farming.CompostBin;
import rs09.game.node.entity.skill.farming.CompostBins;
import rs09.plugin.ClassScanner;

/**
 * Represents the Compost Mound familiar.
 * @author Vexia
 * @author Aero
 */
@Initializable
public class CompostMoundNPC extends Forager {

	/**
	 * The product items.
	 */
	private static final Item[] ITEMS = new Item[] { new Item(6032), new Item(6034), new Item(5318), new Item(5319), new Item(5324), new Item(5322), new Item(5320), new Item(5323), new Item(5321), new Item(5305), new Item(5307), new Item(5308), new Item(5306), new Item(5309), new Item(5310), new Item(5311), new Item(5101), new Item(5102), new Item(5103), new Item(5104), new Item(5105), new Item(5106), new Item(5096), new Item(5097), new Item(5098), new Item(5099), new Item(5100), new Item(5291), new Item(5292), new Item(5293), new Item(5294), new Item(5295), new Item(12176), new Item(5296), new Item(5298), new Item(5299), new Item(5300), new Item(5301), new Item(5302), new Item(5303), new Item(5304) };

	/**
	 * Constructs a new {@code CompostMoundNPC} {@code Object}.
	 */
	public CompostMoundNPC() {
		this(null, 6871);
	}

	/**
	 * Constructs a new {@code CompostMoundNPC} {@code Object}.
	 * @param owner The owner.
	 * @param id The id.
	 */
	public CompostMoundNPC(Player owner, int id) {
		super(owner, id, 2400, 12091, 12, WeaponInterface.STYLE_AGGRESSIVE, ITEMS);
	}

	@Override
	public Familiar construct(Player owner, int id) {
		return new CompostMoundNPC(owner, id);
	}

	@Override
	public void configureFamiliar() {
		ClassScanner.definePlugin(new CompostBucketPlugin());
		if (!DialogueInterpreter.contains(getIds()[1])) {
			ClassScanner.definePlugin(new CompostMoundDialogue());
		}
	}

	@Override
	public boolean isPoisonImmune() {
		return true;
	}

	@Override
	protected boolean specialMove(FamiliarSpecial special) {
		final Scenery object = (Scenery) special.getNode();
		if (!object.getName().equals("Compost Bin")) {
			owner.getPacketDispatch().sendMessage("This scroll can only be used on an empty compost bin.");
			return false;
		}
		CompostBins cbin = CompostBins.forObject(special.getNode().asScenery());
		if(cbin == null){
			return false;
		}
		CompostBin bin = cbin.getBinForPlayer(owner);
		if(bin.isFinished() || bin.isFull() || bin.isClosed()){
			return false;
		}
		final boolean superCompost = RandomFunction.random(10) == 1;
		faceLocation(object.getLocation());
		Item toAdd = new Item(superCompost ? Items.PINEAPPLE_2114 : Items.POTATO_1942);
		toAdd.setAmount(15);
		bin.addItem(toAdd);
		bin.close();
		animate(Animation.create(7775));
		graphics(Graphics.create(1424));
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 6871, 6872 };
	}

	/**
	 * The compost bucket plugin.
	 * @author Vexia
	 * @version 1.0
	 */
	public class CompostBucketPlugin extends UseWithHandler {

		/**
		 * Constructs a new {@code CompostBucketPlugin} {@code Object}.
		 */
		public CompostBucketPlugin() {
			super(1925);
		}

		@Override
		public Plugin<Object> newInstance(Object arg) throws Throwable {
			addHandler(6871, NPC_TYPE, this);
			addHandler(6872, NPC_TYPE, this);
			return this;
		}

		@Override
		public boolean handle(NodeUsageEvent event) {
			final Player player = event.getPlayer();
			final Familiar familiar = (Familiar) event.getUsedWith();
			if (!player.getFamiliarManager().isOwner(familiar)) {
				return true;
			}
			player.animate(Animation.create(895));
			familiar.animate(Animation.create(7775));
			player.getInventory().replace(ITEMS[0], event.getUsedItem().getSlot());
			familiar.getImpactHandler().manualHit(player, 2, HitsplatType.NORMAL);
			return true;
		}

	}

	/**
	 * The compost dialogue.
	 * @author Vexia
	 * @version 1.0
	 */
	public class CompostMoundDialogue extends DialoguePlugin {

		/**
		 * Constructs a new {@code CompostDialogue} {@code Object}.
		 */
		public CompostMoundDialogue() {
			/**
			 * empty.
			 */
		}

		/**
		 * Constructs a new {@code CompostDialogue} {@code Object}.
		 * @param player the player.
		 */
		public CompostMoundDialogue(final Player player) {
			super(player);
		}

		@Override
		public DialoguePlugin newInstance(Player player) {
			return new CompostMoundDialogue(player);
		}

		@Override
		public boolean open(Object... args) {
			options("Chat", "Withdraw", "Farming boost");
			return true;
		}

		@Override
		public boolean handle(int interfaceId, int buttonId) {
			switch (stage) {
			case 0:
				switch (buttonId) {
				case 1:
					npc("Schlorp, splort, splort, splutter shclorp?", "(What we be doin' 'ere, zur?)");
					stage = 10;
					break;
				case 2:
					end();
					Forager forager = (Forager) player.getFamiliarManager().getFamiliar();
					forager.openInterface();
					break;
				case 3:
					player("Can you boost my Farming stat, please?");
					stage = 30;
					break;
				}
				break;
			case 10:
				player("Oh, I have a few things to take care of here, is all.");
				stage++;
				break;
			case 11:
				npc("Schorp, splutter, splutter. Schlup schorp.", "(Aye, right ye are, zur. Oi'll be roight there.)");
				stage++;
				break;
			case 12:
				end();
				break;
			case 30:
				npc("Schlup glorp sputter!", "(Oi do believe oi can!)");
				stage++;
				break;
			case 31:
				if (player.getSkills().getLevel(Skills.FARMING) > player.getSkills().getStaticLevel(Skills.FARMING)) {
					end();
					player.getPacketDispatch().sendMessage("Your stat cannot be boosted this way right now.");
					return true;
				}
				player.getSkills().updateLevel(Skills.FARMING, (int) (1 + (player.getSkills().getStaticLevel(Skills.FARMING) * 0.02)));
				player.getPacketDispatch().sendMessage("The Compost mound has boosted your Farming stat.");
				end();
				break;
			}
			return true;
		}

		@Override
		public int[] getIds() {
			return CompostMoundNPC.this.getIds();
		}

	}
}
