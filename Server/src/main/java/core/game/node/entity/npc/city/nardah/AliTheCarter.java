package core.game.node.entity.npc.city.nardah;

import core.plugin.Initializable;
import core.game.content.dialogue.DialoguePlugin;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.item.GroundItemManager;
import core.game.node.item.Item;

/**
 * Handles Ali the Carter, TODO: Add more dialogue after Spirits of the Elid is added
 * @author ceik
 */

@Initializable
public class AliTheCarter extends DialoguePlugin {
    public AliTheCarter(){
        /**
         * Empty
         */
    }
    public AliTheCarter(Player player){super(player);}
    @Override
    public DialoguePlugin newInstance(Player player){return new AliTheCarter(player);}
    public boolean open(Object... args){
        npc = (NPC)args[0];
        player("Hello");
        return true;
    }

    public boolean handle(int interfaceId, int buttonId){
        switch(stage){
            case 0:
                npc("Hello, friend! Welcome to Nardah.","Do you happen to be in need of water?");
                stage++;
                break;
            case 1:
                interpreter.sendOptions("Select one","Yes I am!","No thank you I'm good.");
                stage++;
                break;
            case 2:
                switch(buttonId){
                    case 1:
                        player("Yes I am!");
                        stage = 3;
                        break;
                    case 2:
                        player("No thank you.");
                        stage = 10;
                        break;
                }
                break;
            case 3:
                npc("It'll be 1000 coins for a full waterskin.");
                stage++;
                break;
            case 4:
                interpreter.sendOptions("Select one","Oh, wow. Um, sure.","Oh my! No thank you.");
                stage++;
                break;
            case 5:
                switch(buttonId){
                    case 1:
                        player("Oh, wow. Um, sure.");
                        stage++;
                        break;
                    case 2:
                        player("Oh my! No thank you.");
                        stage = 10;
                        break;
                }
                break;
            case 6:
                if(player.getInventory().contains(995,1000)){
                    player.getInventory().remove(new Item(995,1000));
                    if(player.getInventory().freeSlots() > 0) {
                        player.getInventory().add(new Item(1823));
                    } else {
                        GroundItemManager.create(new Item(1823),player.getLocation());
                    }
                    end();
                } else {
                    player("Err, I seem to not have enough gold.");
                    stage++;
                }
                break;
            case 7:
                npc("Too bad, friend.");
                stage = 10;
                break;
            case 10:
                end();
                break;
        }
        return true;
    }
    @Override
    public int[] getIds() {return new int[] {3030};}
}
