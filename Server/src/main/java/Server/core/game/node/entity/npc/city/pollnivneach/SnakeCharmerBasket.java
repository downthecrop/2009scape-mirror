package core.game.node.entity.npc.city.pollnivneach;

import core.game.content.dialogue.DialoguePlugin;
import core.game.content.dialogue.FacialExpression;
import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.entity.player.Player;
import core.game.node.item.GroundItemManager;
import core.game.node.item.Item;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Handles Ali the Snake Charmer
 * @author ceik
 */
@Initializable
public class SnakeCharmerBasket extends UseWithHandler {
    public SnakeCharmerBasket() {
        super(995);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        new AliTheSnakeCharmer().init();
        addHandler(6230, OBJECT_TYPE, this);
        return this;
    }

    @Override
    public boolean handle(NodeUsageEvent event) {
        final Player player = event.getPlayer();
        if (player.getInventory().containsItem(new Item(995, 3))) {
            player.getInventory().remove(new Item(995, 3));
            player.getDialogueInterpreter().open(1872, true);
        }
        return true;
    }

    public class AliTheSnakeCharmer extends DialoguePlugin {
        public AliTheSnakeCharmer() {
            /**
             * empty.
             */
        }

        public AliTheSnakeCharmer(Player player) {
            super(player);
        }

        @Override
        public DialoguePlugin newInstance(Player player) {
            return new AliTheSnakeCharmer(player);
        }

        @Override
        public boolean open(Object... args) {
            if (args.length > 0) {
                player("Wow a snake charmer. Can I have a go? Please?");
                stage = 5;
                return true;
            }
            player("Wow a snake charmer. Can I have a go? Please?");
            stage = 0;
            return true;
        }

        @Override
        public boolean handle(int interfaceId, int buttonId) {
            switch (stage) {
                case 0:
                    player.getDialogueInterpreter().sendDialogue("The snake charmer fails to acknowledge you,","he seems too deep into the music to notice you.");
                    stage = 4;
                    break;
                case 4:
                    end();
                    break;
                case 5:
                    player.getDialogueInterpreter().sendDialogue("The snake charmer snaps out of his trance","and directs his full attention to you.");
                    stage = 6;
                    break;
                case 6:
                    player(FacialExpression.JOLLY, "Wow a snake charmer. Can I have a go? Please?");
                    stage = 7;
                    break;
                case 7:
                    if(!player.getInventory().containsItems(new Item(4605),new Item(4606)) && !player.getBank().containItems(4605,4606)) {
                        if (player.getInventory().freeSlots() >= 2) {
                            player.getInventory().add(new Item(4605));
                            player.getInventory().add(new Item(4606));
                        } else {
                            GroundItemManager.create(new Item(4605), player.getLocation());
                            GroundItemManager.create(new Item(4606), player.getLocation());
                        }
                        npc(FacialExpression.ANNOYED, "If it means that you'll leave me alone, I would give you"," my snake charming super starter kit complete","with flute and basket.");
                    } else {
                        npc(FacialExpression.ANGRY,"I already gave you one!");
                    }
                    stage = 4;
                    break;
            }
            return true;
        }

        @Override
        public int[] getIds() {
            return new int[]{1872};
        }
    }
}
