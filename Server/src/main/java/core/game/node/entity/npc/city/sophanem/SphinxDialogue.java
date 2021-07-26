package core.game.node.entity.npc.city.sophanem;

import core.plugin.Initializable;
import core.game.content.dialogue.DialoguePlugin;
import core.game.content.dialogue.FacialExpression;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;

/**
 * Handles Sphinx dialogue
 * @author ceik 
 */
@Initializable
public final class SphinxDialogue extends DialoguePlugin {
    public SphinxDialogue() {
        /**
         * Empty
         */
    }
    public SphinxDialogue(Player player){super(player);}

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new SphinxDialogue(player);
    }

    @Override
    public boolean open(Object... args){
        npc = (NPC) args[0];
        if(player.getFamiliarManager().hasPet() && player.getFamiliarManager().getFamiliar().getId() >= 761 && player.getFamiliarManager().getFamiliar().getId() < 767){
            player("Good day.");
            stage = 50;
        } else {
            player("Good day.");
            stage = 0;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId){
        switch(stage){
            case 0:
                npc("You have the feel of a cat person","about you. Do you look after one?");
                stage = 1;
                break;
            case 1:
                interpreter.sendOptions("Select one.","Yes, but I don't bring it to harsh places like this.","No, you are mistaken.","Yes, but I have left mine in the bank.");
                stage = 2;
                break;
            case 2:
                switch(buttonId){
                    case 1:
                        player("Yes, but I don't bring it to harsh places like this.");
                        stage = 3;
                        break;
                    case 2:
                        player("No, you are mistaken.");
                        stage = 5;
                        break;
                    case 3:
                        player("Yes, but I have left mine in the bank.");
                        stage = 7;
                        break;
                }
                break;
            case 3:
                npc(FacialExpression.SAD,"A pity, they can be of great help in","some adventures. I would like to talk to","your cat. Would you bring it to me?");
                stage = 4;
                break;
            case 4:
                player("I might, but I have a few things to sort out first.");
                stage = 10;
                break;
            case 5:
                npc(FacialExpression.SUSPICIOUS,"Really? I'm generally quite good at knowing","these things.");
                stage = 10;
                break;
            case 7:
                npc(FacialExpression.AFRAID,"What?  That's no place for a cat!");
                stage = 10;
                break;
            case 10:
                end();
                break;
            case 50:
                player.getDialogueInterpreter().sendDialogue("The Sphinx ignores you.");
                stage = 51;
                break;
            case 51:
                npc("Ah, how interesting... a cat. Come here to","me, kitty.");
                stage = 52;
                break;
            case 52:
                player.getFamiliarManager().getFamiliar().sendChat("Meow");
                stage = 53;
                break;
            case 53:
                player.getDialogueInterpreter().sendDialogue("The Sphinx and the cat have a chat.");
                stage = 54;
                break;
            case 54:
                //TODO : Add dialogue for quest-related bits after the quests are added, ends here for now.
                end();
                break;
        }
        return true;
    }
    @Override
    public int[] getIds() {
        return new int[] { 1990 };
    }
}
