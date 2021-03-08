package core.game.node.entity.npc.city.falador;

import core.game.content.dialogue.DialoguePlugin;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;

/**
 * Handles the garden supplier NPC
 * @author ceik
 */
@Initializable
public class gardensupplier extends DialoguePlugin {
    public gardensupplier(){
        /**
         * Empty
         */
    }
    public gardensupplier(Player player){super(player);}

    @Override
    public DialoguePlugin newInstance(Player player){return new gardensupplier(player);}

    @Override
    public boolean open(Object... args){
        npc("Hello, I sell many plants. Would you like","to see what I have?");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int componentId, int buttonId){
        switch(stage){
            case 0:
                player.getDialogueInterpreter().sendOptions("Select one","Yes, please!","No, thanks.");
                stage++;
                break;
            case 1:
                switch(buttonId){
                    case 1:
                        player("Yes, please!");
                        stage++;
                        break;
                    case 2:
                        player("No, thanks.");
                        stage = 10;
                        break;
                }
                break;
            case 2:
                end();
                new NPC(4251).openShop(player);
                break;
            case 10:
                end();
                break;
        }
        return true;
    }
    public int[] getIds() { return new int[] {4251};}
}
