package core.game.interaction.city;

import static api.ContentAPIKt.*;
import core.cache.def.impl.SceneryDefinition;
import core.game.component.Component;
import core.game.content.global.action.ClimbActionHandler;
import core.game.content.global.action.DoorActionHandler;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the plugin used to handle Edgeville related interactions.
 *
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class EdgevilleNodePlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        SceneryDefinition.forId(9262).getHandlers().put("option:take-seed", this);
        SceneryDefinition.forId(9261).getHandlers().put("option:take-seed", this);
        SceneryDefinition.forId(30806).getHandlers().put("option:take-seed", this);
        SceneryDefinition.forId(12265).getHandlers().put("option:climb", this);

        //Dungeon Wilderness gates
        SceneryDefinition.forId(29319).getHandlers().put("option:open", this);
        SceneryDefinition.forId(29320).getHandlers().put("option:open", this);


		SceneryDefinition.forId(12266).getHandlers().put("option:open", this);
        SceneryDefinition.forId(26933).getHandlers().put("option:open", this);
		SceneryDefinition.forId(26934).getHandlers().put("option:close", this);
		SceneryDefinition.forId(26934).getHandlers().put("option:climb-down", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        switch (node.getId()) {
            case 9262:
            case 9261:
            case 30806:
			    sendMessage(player, "There doesn't seem to be any seeds on this rosebush.");
                break;
            case 12265:
                ClimbActionHandler.climb(player, null, Location.create(3078, 3493, 0));
                break;
			case 12266:
				if (option.equalsIgnoreCase("open")) {
					player.getConfigManager().set(680, 1<<22);
				} else if (option.equalsIgnoreCase("close")) {
					player.getConfigManager().set(680, 0);
				}
				break;
            case 26933: // Edgeville Dungeon trapdoor (when closed)
                if (option.equalsIgnoreCase("open")) {
					player.animate(Animation.create(536));
					sendMessage(player, "The trapdoor opens...");
					SceneryBuilder.replace(node.asScenery(), node.asScenery().transform(26934), 500);
                }
                break;
            case 26934: // Edgeville Dungeon trapdoor (when open)
                if (option.equalsIgnoreCase("close")) {
					player.animate(Animation.create(535));
					sendMessage(player, "You close the trapdoor.");
                    SceneryBuilder.replace(node.asScenery(), node.asScenery().transform(26933));
                } else if (option.equalsIgnoreCase("climb-down")) {
					sendMessage(player, "You climb down through the trapdoor...");
					ClimbActionHandler.climbLadder(player, (Scenery) node, option);
                }
                break;
            case 29319:
            case 29320: // Edgeville Dungeon wilderness entrance
                if (option.equalsIgnoreCase("open") && player.getLocation().getY() < 9918) {
                    player.getInterfaceManager().openComponent(382);
                    player.setAttribute("wildy_gate", node);
                }
                else{ // Leaving the wilderness
                    DoorActionHandler.handleAutowalkDoor(player, (Scenery) node);
                }
        }
        return true;
    }

}
