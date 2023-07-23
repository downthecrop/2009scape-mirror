package content.region.desert.nardah.dialogue;

import core.game.node.item.Item;
import core.cache.def.impl.NPCDefinition;
import core.plugin.Initializable;
import core.game.dialogue.DialoguePlugin;
import core.game.dialogue.FacialExpression;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.plugin.Plugin;
import core.plugin.ClassScanner;
import java.util.List;
import kotlin.Pair;

import static core.api.ContentAPIKt.decantContainer;

/**
 * Handles the zahur dialogue.
 * @author Empathy
 */
@Initializable
public class ZahurDialoguePlugin extends DialoguePlugin {
    /**
     * Constructs a new {@code ZahurDialoguePlugin} {@code Object}.
     */
    public ZahurDialoguePlugin() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code ZahurDialoguePlugin} {@code Object}.
     * @param player the player.
     */
    public ZahurDialoguePlugin(Player player) {
        super(player);
    }
    
    @Override
    public DialoguePlugin newInstance(Player player) {
        return new ZahurDialoguePlugin(player);
    }

    @Override
    public boolean open(Object... args) {
        npc("I can combine your potion vials to try and make", "the potions fit into fewer vials. This service is free.", "Would you like to do this?");
        stage = 1;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
        case 1:
            options("Yes", "No");
            stage = 2;
            break;
        case 2:
            if (buttonId == 1) {
                Pair<List<Item>,List<Item>> decantResult = decantContainer(player.getInventory());
                List<Item> toRemove = decantResult.getFirst();
                List<Item> toAdd = decantResult.getSecond();

                for (Item item : toRemove) {
                    player.getInventory().remove(item);
                }

                for (Item item : toAdd) {
                    player.getInventory().add(item);
                }

                npc("There, all done.");
            }
            stage = 3;
            break;
        case 3:
            end();
            break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[] { 3037 };
    }
    
}
