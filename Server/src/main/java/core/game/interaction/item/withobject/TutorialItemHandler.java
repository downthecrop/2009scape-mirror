package core.game.interaction.item.withobject;

import core.plugin.Initializable;
import core.game.content.quest.tutorials.tutorialisland.TutorialSession;
import core.game.content.quest.tutorials.tutorialisland.TutorialStage;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.skill.smithing.smelting.Bar;
import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.update.flag.context.Animation;
import core.plugin.Plugin;

/**
 * @author 'Vexia
 */
@Initializable
public class TutorialItemHandler extends UseWithHandler {
	public TutorialItemHandler() {
		super(438, 436);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		addHandler(3044, OBJECT_TYPE, this);
		return this;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		final Player player = event.getPlayer();
		if (player.getInventory().containItems(438, 436)) {
			player.animate(new Animation(833));
			GameWorld.getPulser().submit(new Pulse(2) {
				@Override
				public boolean pulse() {
					player.getInventory().remove(new Item(438, 1));
					player.getInventory().remove(new Item(436, 1));
					player.getInventory().add(Bar.BRONZE.getProduct());
					player.getSkills().addExperience(Skills.SMITHING, Bar.BRONZE.getExperience());
					if (TutorialSession.getExtension(player).getStage() == 39) {
						TutorialStage.load(player, 40, false);
					}
					return true;
				}

			});

		}
		return true;
	}

}
