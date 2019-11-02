package plugin.npc.familiar;

import org.crandor.cache.def.impl.NPCDefinition;
import org.crandor.game.content.skill.Skills;
import org.crandor.game.content.skill.member.summoning.familiar.Familiar;
import org.crandor.game.content.skill.member.summoning.familiar.FamiliarSpecial;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.combat.equipment.WeaponInterface;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.state.EntityState;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.game.world.update.flag.context.Graphics;
import org.crandor.plugin.Plugin;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.PluginManager;

/**
 * Represents the Unicorn Stallion familiar.
 * @author Aero
 */
@InitializablePlugin
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
		player.getSettings().updateRunEnergy(player.getSettings().getRunEnergy() * 0.10);
		player.getSkills().heal((int) (player.getSkills().getMaximumLifepoints() * 0.15));
		return true;
	}

	@Override
	protected void configureFamiliar() {
		PluginManager.definePlugin(new OptionHandler() {

			@Override
			public Plugin<Object> newInstance(Object arg) throws Throwable {
				NPCDefinition.forId(6822).getConfigurations().put("option:cure", this);
				NPCDefinition.forId(6823).getConfigurations().put("option:cure", this);
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
