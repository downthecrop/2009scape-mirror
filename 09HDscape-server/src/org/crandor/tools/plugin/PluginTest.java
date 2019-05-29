package org.crandor.tools.plugin;

import org.crandor.cache.Cache;
import org.crandor.game.component.Component;
import org.crandor.game.component.ComponentPlugin;
import org.crandor.game.interaction.Option;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.ai.AIPBuilder;
import org.crandor.game.node.item.Item;
import org.crandor.game.node.object.GameObject;
import org.crandor.game.world.GameWorld;
import org.crandor.game.world.map.Location;

/**
 * Tests most of the plugins.
 * @author Emperor
 */
public final class PluginTest {

	public static void main(String[] args) throws Throwable {
		GameWorld.prompt(false);
		Player player = AIPBuilder.create("testharhar", Location.create(3222, 3217, 0));
		player.init();
		int objectCount = 0;
		int npcCount = 0;
		int itemCount = 0;
		int componentCount = 0;
		for (int id = 0; id < Cache.getObjectDefinitionsSize(); id++) {
			GameObject object = new GameObject(id, Location.create(3222, 3217, 0));
			for (Option o : object.getInteraction().getOptions()) {
				if (o != null && o.getHandler() != null) {
					objectCount++;
					o.getHandler().handle(player, object, o.getName().toLowerCase());
				}
			}
		}
		for (int id = 0; id < Cache.getNPCDefinitionsSize(); id++) {
			NPC npc = NPC.create(id, Location.create(3222, 3217, 0));
			npc.init();
			for (Option o : npc.getInteraction().getOptions()) {
				if (o != null && o.getHandler() != null) {
					npcCount++;
					o.getHandler().handle(player, npc, o.getName().toLowerCase());
				}
			}
		}
		for (int id = 0; id < Cache.getItemDefinitionsSize(); id++) {
			Item item = new Item(id);
			for (Option o : item.getInteraction().getOptions()) {
				if (o != null && o.getHandler() != null) {
					itemCount++;
					o.getHandler().handle(player, item, o.getName().toLowerCase());
				}
			}
		}
		for (int id = 0; id < Cache.getInterfaceDefinitionsSize(); id++) {
			Component comp = new Component(id);
			ComponentPlugin p = comp.getDefinition().getPlugin();
			if (p == null) {
				continue;
			}
			componentCount++;
			for (int component = 0; component < Cache.getInterfaceDefinitionsComponentsSize(id); component++) {
				p.handle(player, comp, 180, component, -1, -1);
			}
		}
		System.out.println("Tested " + objectCount + " object actions...");
		System.out.println("Tested " + npcCount + " NPC actions...");
		System.out.println("Tested " + itemCount + " item actions...");
		System.out.println("Tested " + componentCount + " component actions...");
		int total = objectCount + npcCount + itemCount + componentCount;
		System.out.println("Finished - tested a total of " + total + " actions!");
	}

}