package core.game.node.entity.npc.city.nardah;

import core.plugin.Initializable;
import core.game.content.dialogue.DialoguePlugin;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;

/**
 * Added some dialogue for Seddu
 * @author ceik
 */

@Initializable
public class Seddu extends DialoguePlugin {
    public Seddu(){
        /**
         * Empty
         */
    }
    public Seddu(Player player){
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player){return new Seddu(player);}

    @Override
    public boolean open(Object... args){
        npc("I buy and sell adventurer's equipment, do you want to trade?");
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId){
        switch(stage){
            case 0:
                interpreter.sendOptions("Select one","Yes, please","No, thanks");
                stage++;
                break;
            case 1:
                switch(buttonId){
                    case 1:
                        end();
                        NPC seddu = new NPC(3038);
                        seddu.openShop(player);
                        break;
                    case 2:
                        player("No, thanks.");
                        stage++;
                        break;
                    case 3:
                        end();
                        break;
                }
                break;
        }
        return true;
    }
    public int[] getIds(){return new int[] {3038};}
}
