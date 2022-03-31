package core.game.node.entity.npc.familiar;

import core.cache.def.impl.NPCDefinition;
import core.plugin.Initializable;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.skill.summoning.familiar.Familiar;
import core.game.node.entity.skill.summoning.familiar.FamiliarSpecial;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.player.Player;
import core.game.node.entity.state.EntityState;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Plugin;
import rs09.plugin.ClassScanner;

/**
 * Represents the Unicorn Stallion familiar.
 * @author Aero
 */
@Initializable
public class UnicornStallionNPC extends Familiar {

	/**
	 * Constructs a new {@code UnicornStallion} {@code Object}.
	 */
	public UnicornStallionNPC() {
		this(null, 6822);
	}

	/**
	 * Constructs a new {@code UnicornStallion} {@code Object}.
	 * @param owner The owner.
	 * @param id The id.
	 */
	public UnicornStallionNPC(Player owner, int id) {
		super(owner, id, 5400, 12039, 20, WeaponInterface.STYLE_CONTROLLED);
	}

	@Override
	public Familiar construct(Player owner, int id) {
		return new UnicornStallionNPC(owner, id);
	}

	@Override
	protected boolean specialMove(FamiliarSpecial special) {
		Player player = (Player) special.getNode();
		player.getAudioManager().send(4372);
		visualize(Animation.create(8267), Graphics.create(1356));
		player.getSkills().heal((int) (player.getSkills().getMaximumLifepoints() * 0.15));
		return true;
	}

	@Override
	protected void configureFamiliar() {
		ClassScanner.definePlugin(new OptionHandler() {

			@Override
			public Plugin<Object> newInstance(Object arg) throws Throwable {
				NPCDefinition.forId(6822).getHandlers().put("option:cure", this);
				NPCDefinition.forId(6823).getHandlers().put("option:cure", this);
				return this;
			}

			@Override
			public boolean handle(Player player, Node node, String option) {
				Familiar familiar = (Familiar) node;
				if (!player.getFamiliarManager().isOwner(familiar)) {
					return true;
				}
				Player owner = player;
				if (owner.getSkills().getLevel(Skills.MAGIC) < 2) {
					player.sendMessage("You don't have enough summoning points left");
					return true;
				}
				if (!owner.getStateManager().hasState(EntityState.POISONED)) {
					player.sendMessage("You are not poisoned.");
					return true;
				}
				player.getAudioManager().send(4372);
				familiar.visualize(Animation.create(8267), Graphics.create(1356));
				player.getStateManager().remove(EntityState.POISONED);
				player.getSkills().updateLevel(Skills.SUMMONING, -2, 0);
				return true;
			}

		});
	}

	@Override
	public void visualizeSpecialMove() {
		owner.visualize(Animation.create(7660), Graphics.create(1298));
	}

	@Override
	public int[] getIds() {
		return new int[] { 6822, 6823 };
	}

}
