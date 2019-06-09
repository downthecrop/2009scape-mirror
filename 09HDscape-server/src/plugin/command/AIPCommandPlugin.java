package plugin.command;

import org.crandor.game.container.Container;
import org.crandor.game.interaction.Interaction;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.ai.AIPBuilder;
import org.crandor.game.node.entity.player.ai.AIPlayer;
import org.crandor.game.node.entity.player.ai.general.GeneralBotCreator;
import org.crandor.game.node.entity.player.ai.general.scriptrepository.ManThiever;
import org.crandor.game.node.entity.player.ai.pvp.PVPAIPActions;
import org.crandor.game.node.entity.player.ai.pvp.PVPAIPBuilderUtils;
import org.crandor.game.node.entity.player.ai.resource.ResourceAIPActions;
import org.crandor.game.node.entity.player.link.appearance.Gender;
import org.crandor.game.node.item.Item;
import org.crandor.game.system.command.CommandPlugin;
import org.crandor.game.system.command.CommandSet;
import org.crandor.game.system.task.Pulse;
import org.crandor.game.world.GameWorld;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.map.RegionManager;
import org.crandor.game.world.map.path.Pathfinder;
import org.crandor.game.world.repository.Repository;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;
import org.crandor.tools.RandomFunction;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles the AIPlayer commands.
 * These commands are for bots
 * @author Emperor
 */
@InitializablePlugin
public final class AIPCommandPlugin extends CommandPlugin {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		link(CommandSet.ADMINISTRATOR);
		return this;
	}

	@Override
	public boolean parse(final Player player, String name, String[] args) {
		List<AIPlayer> legion = player.getAttribute("aip_legion");
		switch (name) {
		case "desaip":
			player.removeAttribute("aip_select");
			return true;
		case "sellegion":
			if (legion != null && !legion.isEmpty()) {
				player.setAttribute("aip_select", legion.get(0));
			}
			return true;
		case "regroup":
			Player last = player;
			if (legion != null && !legion.isEmpty()) {
				for (AIPlayer p : legion) {
					p.follow(last);
					last = p;
				}
			}
			player.removeAttribute("aip_select");
			return true;
		case "clearlegion":
			if (legion != null && !legion.isEmpty()) {
				for (AIPlayer p : legion) {
					AIPlayer.deregister(p.getUid());
				}
				legion.clear();
			}
			player.removeAttribute("aip_select");
			player.removeAttribute("aip_legion");
			return true;
		case "clearaips":
			for (Player p : Repository.getPlayers()) {
				if (p.isArtificial()) {
					p.clear();
				}
			}
			return true;
		case "aip":
			name = args.length < 2 ? player.getName() : args[1];
			AIPlayer p = AIPBuilder.copy(player, name, player.getLocation().transform(0, 1, 0));
			Repository.getPlayers().add(p);
			p.init();
			
			Interaction.sendOption(player, 7, "Control");
			return true;
		case "legion":
			int size = args.length < 2 ? 10 : Integer.parseInt(args[1]);
			last = player;
			if (legion == null) {
				player.setAttribute("aip_legion", legion = new ArrayList<>());
			}
			Interaction.sendOption(player, 7, "Control");
			boolean joinClan = player.getCommunication().getClan() != null && !player.getCommunication().getClan().isDefault();
			String message = player.getName().equals("ethan") ? "The Dark Army marches again!" : null; // Add
			// your
			// own
			// message
			for (int i = 0; i < size; i++) {
				final AIPlayer aip = AIPBuilder.copy(player, last.getLocation().transform(0, 1, 0));
				Repository.getPlayers().add(aip);
				aip.init();
				if (legion.isEmpty()) {
					aip.setAttribute("aip_legion", legion);
				}
				legion.add(aip);
				final Player l = last;
				if (joinClan) {
					if (player.getCommunication().getClan().enter(aip)) {
						aip.getCommunication().setClan(player.getCommunication().getClan());
					}
					if (player.getCommunication().getClan().getClanWar() != null) {
						player.getCommunication().getClan().getClanWar().fireEvent("join", aip);
					}
				}
				GameWorld.submit(new Pulse(1) {
					@Override
					public boolean pulse() {
						aip.follow(l);
						return true;
					}
				});
				if (message != null) {
					aip.sendChat("The Dark Army marches again!");
				}
				last = aip;
			}
			return true;
		case "pvplegion":
			size = args.length < 2 ? 10 : Integer.parseInt(args[1]);
			last = player;
			if (PVPAIPActions.pvp_players == null) {
				player.setAttribute("aip_legion", PVPAIPActions.pvp_players = new ArrayList<>());
			}
			for (int i = 0; i < size; i++) {
				String aipName = PVPAIPBuilderUtils.names[i];
				final AIPlayer aip = AIPBuilder.create(aipName, generateLocation(player));
				aip.setControler(player);
				aip.getAppearance().setGender(RandomFunction.random(3) == 1 ? Gender.FEMALE : Gender.MALE);
				Repository.getPlayers().add(aip);
				aip.init();
				PVPAIPBuilderUtils.generateClass(aip);
				
				if (PVPAIPActions.pvp_players.isEmpty()) {
					aip.setAttribute("aip_legion", PVPAIPActions.pvp_players);
				}
				PVPAIPActions.pvp_players.add(aip);
				last = aip;
			}
			return true;
			
		case "resourcelegion":
			size = args.length < 2 ? 10 : Integer.parseInt(args[1]);
			last = player;
			if (ResourceAIPActions.resource_players == null) {
				player.setAttribute("aip_legion", ResourceAIPActions.resource_players = new ArrayList<>());
			}
			for (int i = 0; i < size; i++) {
				String aipName = PVPAIPBuilderUtils.names[i];
				final AIPlayer aip = AIPBuilder.create(aipName, generateLocation(player));
				aip.setControler(player);
				aip.getAppearance().setGender(RandomFunction.random(3) == 1 ? Gender.FEMALE : Gender.MALE);
				Repository.getPlayers().add(aip);
				aip.init();
				PVPAIPBuilderUtils.generateClass(aip);
				
				if (ResourceAIPActions.resource_players .isEmpty()) {
					aip.setAttribute("aip_legion", ResourceAIPActions.resource_players );
				}
				ResourceAIPActions.resource_players.add(aip);
				last = aip;
			}
			return true;
		case "syncresource":
			ResourceAIPActions.syncBotThread(player);
			break;
		case "pvpfight":
			PVPAIPActions.syncBotThread(player);
			return true;

		/*
		    Start regular bots
		 */
    	case "manthiev":
    		new GeneralBotCreator("Bot", player.getLocation(), new ManThiever());
    		break;

		}
		return false;
	}
	
	private Location generateLocation(Player player) {
		Location random_location = player.getLocation().transform(RandomFunction.random(-15, 15), RandomFunction.random(-15, 15), 0);
		if (!RegionManager.isTeleportPermitted(random_location)) {
			return generateLocation(player);
		}
		if (!Pathfinder.find(player, random_location, false, Pathfinder.DUMB).isSuccessful()) {
			return generateLocation(player);
		}
		if (RegionManager.getObject(random_location) != null) {
			return generateLocation(player);
		}
		return random_location;
	}

}
