package core.game.content.activity.wguild.dummy;

import core.cache.def.impl.SceneryDefinition;
import core.game.component.Component;
import core.plugin.Initializable;
import core.game.node.entity.skill.Skills;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.system.task.Pulse;
import rs09.game.world.GameWorld;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.game.world.update.flag.chunk.AnimateObjectUpdateFlag;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Plugin;
import core.tools.RandomFunction;

/**
 * Handles the dummy room.
 * @author Emperor
 */
@Initializable
public final class DummyRoom extends OptionHandler {

	/**
	 * Represents a dummy.
	 * @author Emperor
	 */
	private static enum Dummy {
		STAB(new Scenery(15629, 2857, 3549, 0, 10, 2), -1, WeaponInterface.BONUS_STAB), SLASH(new Scenery(15625, 2858, 3554, 0), -1, WeaponInterface.BONUS_SLASH), CRUSH(new Scenery(15628, 2859, 3549, 0, 10, 2), -1, WeaponInterface.BONUS_CRUSH), CONTROLLED(new Scenery(15627, 2855, 3552, 0, 10, 3), WeaponInterface.STYLE_CONTROLLED, -1), DEFENCE(new Scenery(15630, 2855, 3550, 0, 10, 3), WeaponInterface.STYLE_DEFENSIVE, -1), AGGRESSIVE(new Scenery(15626, 2860, 3553, 0, 10, 1), WeaponInterface.STYLE_AGGRESSIVE, -1), ACCURATE(new Scenery(15624, 2856, 3554, 0), WeaponInterface.STYLE_ACCURATE, -1), ;

		/**
		 * The object.
		 */
		private final Scenery object;

		/**
		 * The attack style to be used on this dummy.
		 */
		private final int attackStyle;

		/**
		 * The bonus type to be used on this dummy.
		 */
		private final int bonusType;

		/**
		 * Constructs a new {@code Dummy} {@code Object}.
		 * @param object The object.
		 * @param attackStyle The attack style.
		 */
		private Dummy(Scenery object, int attackStyle, int bonusType) {
			this.object = object;
			this.attackStyle = attackStyle;
			this.bonusType = bonusType;
		}

		/**
		 * Gets the object.
		 * @return The object.
		 */
		public Scenery getObject() {
			return object;
		}

		/**
		 * Gets the attackStyle.
		 * @return The attackStyle.
		 */
		public int getAttackStyle() {
			return attackStyle;
		}

		/**
		 * Gets the bonusType.
		 * @return The bonusType.
		 */
		public int getBonusType() {
			return bonusType;
		}
	}

	/**
	 * The current dummy.
	 */
	private static Dummy dummy;

	/**
	 * The time stamp.
	 */
	private static int timeStamp;

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(15656).getHandlers().put("option:view", this);
		for (Dummy dummy : Dummy.values()) {
			SceneryDefinition.forId(dummy.getObject().getId()).getHandlers().put("option:hit", this);
		}
		GameWorld.getPulser().submit(new Pulse(10) {
			boolean activeDummy;
			Scenery controlled;

			@Override
			public boolean pulse() {
				if (!activeDummy) {
					setDelay(10);
					timeStamp = GameWorld.getTicks();
					dummy = RandomFunction.getRandomElement(Dummy.values());
					SceneryBuilder.replace(RegionManager.getObject(dummy.getObject().getLocation()), dummy.getObject(), 11);
					activeDummy = true;
					if (dummy == Dummy.CONTROLLED && controlled == null) {
						Location l = Location.create(2860, 3551, 0);
						controlled = new Scenery(dummy.getObject().getId(), l, 10, 1);
						SceneryBuilder.replace(RegionManager.getObject(l), controlled, 11);
					}
					return false;
				}
				setDelay(4);
				Animation animation = Animation.create(4164);
				animation.setObject(dummy.getObject());
				RegionManager.getRegionChunk(dummy.getObject().getLocation()).flag(new AnimateObjectUpdateFlag(animation));
				activeDummy = false;
				if (controlled != null) {
					animation = Animation.create(4164);
					animation.setObject(controlled);
					RegionManager.getRegionChunk(controlled.getLocation()).flag(new AnimateObjectUpdateFlag(animation));
					controlled = null;
				}
				return false;
			}
		});
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		Scenery object = (Scenery) node;
		if (object.getId() == 15656) {
			player.getInterfaceManager().open(new Component(412));
			return true;
		}
		if (object.getId() == dummy.getObject().getId()) {
			if (timeStamp == player.getAttribute("dummy_ts", -1)) {
				player.getPacketDispatch().sendMessage("You have already hit a dummy this turn.");
				return true;
			}
			if (player.getProperties().getAttackStyle().getStyle() != dummy.getAttackStyle() && player.getProperties().getAttackStyle().getBonusType() != dummy.getBonusType()) {
				player.lock(5);
				player.visualize(player.getProperties().getAttackAnimation(), new Graphics(80, 96));
				player.getPacketDispatch().sendMessage("You whack the dummy with the wrong attack style.");
			} else {
				player.getSkills().addExperience(Skills.ATTACK, 15.0, true);
				player.animate(player.getProperties().getAttackAnimation());
				player.getPacketDispatch().sendMessage("You whack the dummy successfully!");
				player.setAttribute("dummy_ts", timeStamp);
				player.getSavedData().getActivityData().updateWarriorTokens(2);
			}
			return true;
		}
		return false;
	}

}
