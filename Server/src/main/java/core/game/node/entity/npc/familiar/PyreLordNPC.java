package core.game.node.entity.npc.familiar;

import core.game.node.entity.player.link.diary.DiaryType;
import core.plugin.Initializable;
import core.game.node.entity.skill.SkillBonus;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.skill.crafting.jewellery.JewelleryCrafting;
import core.game.node.entity.skill.firemaking.FireMakingPulse;
import core.game.node.entity.skill.firemaking.Log;
import core.game.node.entity.skill.summoning.familiar.Familiar;
import core.game.node.entity.skill.summoning.familiar.FamiliarSpecial;
import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.player.Player;
import core.game.node.item.GroundItem;
import core.game.node.item.GroundItemManager;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.system.task.Pulse;
import rs09.game.world.GameWorld;
import core.game.world.map.RegionManager;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.game.world.update.flag.player.FaceLocationFlag;
import core.plugin.Plugin;
import rs09.plugin.ClassScanner;

/**
 * Represents the Pyrelord familiar.
 * @author Aero
 * @author Vexia
 */
@Initializable
public class PyreLordNPC extends Familiar {

	/**
	 * The animation of the pyre lord.
	 */
	private static final Animation FIREMAKE_ANIMATION = Animation.create(8085);

	/**
	 * Constructs a new {@code PyreLordNPC} {@code Object}.
	 */
	public PyreLordNPC() {
		this(null, 7377);
	}

	/**
	 * Constructs a new {@code PyreLordNPC} {@code Object}.
	 * @param owner The owner.
	 * @param id The id.
	 */
	public PyreLordNPC(Player owner, int id) {
		super(owner, id, 3200, 12816, 6, WeaponInterface.STYLE_AGGRESSIVE);
		boosts.add(new SkillBonus(Skills.FIREMAKING, 3));		
	}

	@Override
	public Familiar construct(Player owner, int id) {
		return new PyreLordNPC(owner, id);
	}

	@Override
	public void configureFamiliar() {
		ClassScanner.definePlugin(new PyreLordFiremake());
	}

	@Override
	protected boolean specialMove(FamiliarSpecial special) {
		final Item item = (Item) special.getNode();
		if (item.getId() != 2357) {
			owner.getPacketDispatch().sendMessage("You can only use this special on gold bars.");
			return false;
		}
		owner.lock(1);
		animate(Animation.create(8081));
		owner.graphics(Graphics.create(1463));
		JewelleryCrafting.open(owner);
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 7377, 7378 };
	}

	/**
	 * Handles the use with event of a log on a pyrelord.
	 * @author Vexia
	 */
	public final class PyreLordFiremake extends UseWithHandler {

		/**
		 * Constructs a new {@code PyreLordFiremake} {@code Object}.
		 */
		public PyreLordFiremake() {
			super(1511, 2862, 1521, 1519, 6333, 10810, 1517, 6332, 12581, 1515, 1513, 13567, 10329, 10328, 7406, 7405, 7404);
		}

		@Override
		public Plugin<Object> newInstance(Object arg) throws Throwable {
			for (int id : getIds()) {
				addHandler(id, NPC_TYPE, this);
			}
			return this;
		}

		@Override
		public boolean handle(NodeUsageEvent event) {
			final Player player = event.getPlayer();
			final Log log = Log.forId(event.getUsedItem().getId());
			final Familiar familiar = (Familiar) event.getUsedWith();
			final int ticks = FIREMAKE_ANIMATION.getDefinition().getDurationTicks();
			if (!player.getFamiliarManager().isOwner(familiar)) {
				return true;
			}
			if (RegionManager.getObject(familiar.getLocation()) != null || familiar.getZoneMonitor().isInZone("bank")) {
				player.getPacketDispatch().sendMessage("You can't light a fire here.");
				return false;
			}
			familiar.lock(ticks);
			familiar.animate(FIREMAKE_ANIMATION);
			if (player.getInventory().remove(event.getUsedItem())) {
				final GroundItem ground = GroundItemManager.create(event.getUsedItem(), familiar.getLocation(), player);
				GameWorld.getPulser().submit(new Pulse(ticks, player, familiar) {
					@Override
					public boolean pulse() {
						if (!ground.isActive()) {
							return true;
						}
						final Scenery object = new Scenery(log.getFireId(), familiar.getLocation());
						familiar.moveStep();
						GroundItemManager.destroy(ground);
						player.getSkills().addExperience(Skills.FIREMAKING, log.getXp() + 10);
						familiar.faceLocation(FaceLocationFlag.getFaceLocation(familiar, object));
						SceneryBuilder.add(object, log.getLife(), FireMakingPulse.getAsh(player, log, object));
						if (player.getViewport().getRegion().getId() == 10806) {
							player.getAchievementDiaryManager().finishTask(player, DiaryType.SEERS_VILLAGE, 1, 9);
						}
						return true;
					}
				});
			}
			return true;
		}

	}
}
