package core.game.node.entity.npc.other;

import core.game.content.dialogue.DialoguePlugin;
import core.game.content.dialogue.FacialExpression;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;

@Initializable
public class akharanu extends DialoguePlugin {
    public akharanu() {};
    public akharanu(Player player){super(player);}
    @Override
    public DialoguePlugin newInstance(Player player){return new akharanu(player);}
    public boolean open(Object... args){
        npc("Hello, there, friend!");
        stage = 0;
        return true;
    }
    @Override
    public boolean handle(int componentId, int buttonId){
        switch(stage){
            case 0:
                player.getDialogueInterpreter().sendOptions("Select one","Why are you, errr, so stiff?","Do you sell anything?");
                stage++;
                break;
            case 1:
                switch(buttonId){
                    case 1:
                        player("Why are you, errr, so stiff?");
                        stage++;
                        break;
                    case 2:
                        player("Do you sell anything?");
                        stage = 8;
                        break;
                }
                break;
            case 2:
                npc(FacialExpression.HALF_GUILTY,"I have extremely severe arthritis. It really sucks.");
                stage++;
                break;
            case 3:
                player("Oh. Well I'm sorry to hear that.");
                stage++;
                break;
            case 4:
                npc("Yes, thank you for your concern.");
                stage++;
                break;
            case 5:
                end();
                break;
            case 8:
                player("Do you sell anything?");
                stage++;
                break;
            case 9:
                npc("Why, yes I do!");
                stage++;
                break;
            case 10:
                end();
                new NPC(1688).openShop(player);
                break;
        }
        return true;
    }
    public int[] getIds() {return new int[] {1688};}
}
