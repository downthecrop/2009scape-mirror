package org.crandor.game.node.entity.player.ai.resource;

import org.crandor.game.content.skill.free.gather.GatheringSkillPulse;
import org.crandor.game.content.skill.free.gather.SkillingResource;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.ai.AIPlayer;
import org.crandor.game.node.object.GameObject;
import org.crandor.game.system.task.Pulse;
import org.crandor.game.world.GameWorld;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.map.RegionManager;
import org.crandor.game.world.map.path.Pathfinder;

import java.util.List;

public class ResourceAIPActions {
	

	public static List<AIPlayer> resource_players = null;

	protected SkillingResource resource;
	
	public static void syncBotThread(final Player player) {
		if (resource_players == null || resource_players.size() == 0) {
			return;
		}
		for (int aip_index = 0; aip_index < resource_players.size(); aip_index++) {
			final AIPlayer bot = resource_players.get(aip_index);
			GameWorld.submit(new Pulse(1, bot) {
				int ticks;
				@Override
				public boolean pulse() {
					/*if (bot.getInventory().freeSlots() < 1) {
						//flee(bot);
						return true;
					}*/
					//Inactivity logout timer.
					if (ticks++ == 5000/* && bot.getWalkingQueue().getQueue().isEmpty()*/) {
						AIPlayer.deregister(bot.getUid());
						resource_players.remove(bot);
						return true;
					}
					chopTree(bot, RegionManager.getObject(Location.create(3234,3231,0)));
					return false;
				}
			});
		}
	}
	
	private static void chopTree(final AIPlayer bot, final GameObject node) {
		bot.sendChat("WE CHOPPIN");
//		Pathfinder.find(bot, node);
//		Pulse pulse = new GatheringSkillPulse(bot, node);
        bot.getPulseManager().run(new GatheringSkillPulse(bot, node));
//		pulse.start();
	}	
}
