package core.game.node.entity.npc.city.sophanem;

import core.plugin.Initializable;
import core.game.content.dialogue.DialoguePlugin;
import core.game.node.entity.player.Player;

/**
 * Stand-in until we get the real dialogue for Tarik
 * @author ceik 
 */

@Initializable
public class TarikDialogue extends DialoguePlugin {
    public TarikDialogue() {
        /**
         * Empty
         */
    }
    public TarikDialogue(Player player){super(player);}

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new TarikDialogue(player);
    }

    @Override
    public boolean open(Object... args){
        npc("I found this crazy looking pyramid here.","I can't seem to figure out what it's for.","Maybe you should head inside?");
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId){
        switch(stage){
            case 0:
                player("Oh, alright...");
                stage = 1;
                break;
            case 1:
                end();
                break;
        }
        return true;
    }
    @Override
    public int[] getIds() {
        return new int[] { 4478 };
    }
}
