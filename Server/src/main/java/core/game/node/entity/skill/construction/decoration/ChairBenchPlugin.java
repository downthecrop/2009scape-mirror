package core.game.node.entity.skill.construction.decoration;

import core.cache.def.impl.SceneryDefinition;
import core.game.node.entity.skill.construction.Decoration;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.impl.ForceMovement;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.game.system.task.Pulse;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Handles chair and bench options.
 * @author Emperor
 *
 */
@Initializable
public final class ChairBenchPlugin extends OptionHandler {

	/**
	 * The decorations that can be seated on.
	 */
	private static final Object[][] CHAIRS = {
			{Decoration.CRUDE_CHAIR, 4073, 4103},
			{Decoration.WOODEN_CHAIR, 4075, 4103},
			{Decoration.ROCKING_CHAIR, 4079, 4103},
			{Decoration.OAK_CHAIR, 4081, 4103},
			{Decoration.OAK_ARMCHAIR, 4083, 4103},
			{Decoration.TEAK_ARMCHAIR, 4085, 4103},
			{Decoration.MAHOGANY_ARMCHAIR, 4087, 4103},
			{Decoration.BENCH_WOODEN, 4089, 4104},
			{Decoration.BENCH_OAK, 4091, 4104},
			{Decoration.BENCH_CARVED_OAK, 4093, 4104},
			{Decoration.BENCH_TEAK, 4095, 4104},
			{Decoration.BENCH_CARVED_TEAK, 4097, 4104},
			{Decoration.BENCH_MAHOGANY, 4099, 4104},
			{Decoration.BENCH_GILDED, 4101, 4104},
			{Decoration.CARVED_TEAK_BENCH, 4097, 4104},
			{Decoration.MAHOGANY_BENCH, 4099, 4104},
			{Decoration.GILDED_BENCH, 4101, 4104},
			{Decoration.OAK_THRONE, 4111, 4103},
			{Decoration.TEAK_THRONE, 4112, 4103},
			{Decoration.MAHOGANY_THRONE, 4113, 4103},
			{Decoration.GILDED_THRONE, 4114, 4103},
			{Decoration.SKELETON_THRONE, 4115, 4103},
			{Decoration.CRYSTAL_THRONE, 4116, 4103},
			{Decoration.DEMONIC_THRONE, 4117, 4103},
	};
	
	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		for (Object[] data : CHAIRS) {
			SceneryDefinition.forId(((Decoration) data[0]).getObjectId()).getHandlers().put("option:sit-on", this);
		}
		return this;
	}

	@Override
	public boolean handle(final Player player, Node node, String option) {
		Scenery object = (Scenery) node;
		int animId = -1;
		int sitAnimId = -1;
		for (Object[] data : CHAIRS) {
			if (((Decoration) data[0]).getObjectId() == object.getId()) {
				animId = (Integer) data[1];
				sitAnimId = (Integer) data[2];
				break;
			}
		}
		if (object.getType() == 11) {
			animId++;
		}
		final int animation = animId;
		final int sitAnimation = sitAnimId;
		ForceMovement.run(player, player.getLocation(), node.getLocation(), ForceMovement.WALK_ANIMATION, Animation.create(sitAnimation), object.getDirection().getOpposite(), ForceMovement.WALKING_SPEED, ForceMovement.WALKING_SPEED);
		player.getLocks().lockInteractions(600000);
		player.getPulseManager().run(new Pulse(2) {
			@Override
			public boolean pulse() {
				player.animate(Animation.create(animation));
				return false;
			}
			
			@Override
			public void stop() {
				super.stop();
				player.getLocks().unlockInteraction();
				player.animate(Animation.create(sitAnimation + 2));
			}
		});
		return true;
	}

}