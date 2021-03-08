package core.game.node.entity.npc.city.nardah;

import core.game.content.dialogue.DialoguePlugin;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;

/**
 * Handles dialogue for Artimeus
 * @author ceik
 */

@Initializable
public class ArtimeusDialogue extends DialoguePlugin {
    public ArtimeusDialogue(){
        /**
         * Empty
         */
    }
    public ArtimeusDialogue(Player player){
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player){return new ArtimeusDialogue(player);}

    @Override
    public boolean open(Object... args){
        npc("Greetings, friend; my business here deals with Hunter","related items. Is there anything in which I can interest you?");
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId){
        switch(stage){
            case 0:
                interpreter.sendOptions("Select one","What kind of items do you stock?","I'm not in the market for Hunter equipment right now, thanks.");
                stage++;
                break;
            case 1:
                switch(buttonId){
                    case 1:
                        player("What kind of items do you stock?");
                        stage++;
                        break;
                    case 2:
                        player("I'm not in the market for Hunter equipment","right now, thanks.");
                        stage = 10;
                        break;
                }
                break;
            case 2:
                npc("Have a look for yourself.");
                stage++;
                break;
            case 3:
                end();
                NPC Artimeus = new NPC(5109);
                Artimeus.openShop(player);
                break;
            case 10:
                end();
                break;
        }
        return true;
    }
    public int[] getIds() {return new int[] {5109};}
}
