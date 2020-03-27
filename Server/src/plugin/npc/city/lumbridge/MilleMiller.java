package plugin.npc.city.lumbridge;

import org.crandor.game.content.dialogue.DialoguePlugin;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.plugin.InitializablePlugin;

/**
 * @author ceik
 */
@InitializablePlugin
public class MilleMiller extends DialoguePlugin {
    public MilleMiller(){
        /**
         * Empty
         */
    }
    public MilleMiller(Player player){ super(player);}
    @Override
    public DialoguePlugin newInstance(Player player){return new MilleMiller(player);}

    @Override
    public boolean open(Object... args){
        player("Hello, there. Can you teach me how","to make wheat?");
        return true;
    }

    @Override
    public boolean handle(int componentId, int buttonId){
        switch(stage){
            case 0:
                npc("Certainly! You first need to gather wheat from","the field nearby, then you go on up","to the top, put it in the hopper, and","flip those levers!");
                stage++;
                break;
            case 1:
                npc("Afterwords, you need to make sure you have","a pot, and come down and grab your wheat!");
                stage++;
                break;
            case 2:
                if(!player.getInventory().containsItem(new Item(1931))){
                    player("Could you give me one of those?");
                    stage = 4;
                } else {
                    player("Thanks!");
                    stage++;
                }
                break;
            case 3:
                end();
                break;
            case 4:
                npc("Sure thing!");
                stage++;
                break;
            case 5:
                end();
                player.getInventory().add(new Item(1931));
                break;
        }
        return true;
    }
    @Override
    public int[] getIds() {return new int[] {3806};}
}
