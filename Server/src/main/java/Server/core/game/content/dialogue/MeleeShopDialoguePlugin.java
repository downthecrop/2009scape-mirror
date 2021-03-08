package core.game.content.dialogue;

import core.game.content.global.shop.Shop;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;

/**
 * Package -> core.game.content.dialogue
 * Created on -> 9/10/2016 @10:33 PM for 530
 *
 * @author Ethan Kyle Millard
 */
@Initializable
public class MeleeShopDialoguePlugin extends DialoguePlugin {

    private Shop shop;


    public MeleeShopDialoguePlugin() {
        /**
         * Empty
         */
    }

    public MeleeShopDialoguePlugin(Player player) {
        super(player);
    }
    @Override
    public DialoguePlugin newInstance(Player player) {
        return new MeleeShopDialoguePlugin(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        options("Melee Shop 1", "Melee Shop 2");
        stage = 0;
        return false;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch(stage) {
            case 0:
                switch (buttonId) {
                    case 1:
                        npc.openShop(player);
                        break;
                    case 2:
                        break;
                }
                break;
        }
        return true;
    }


    @Override
    public int[] getIds() {
        return new int[] { 5503 };
    }
}
