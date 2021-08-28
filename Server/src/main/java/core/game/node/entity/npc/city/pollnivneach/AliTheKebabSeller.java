package core.game.node.entity.npc.city.pollnivneach;

import core.game.content.dialogue.DialoguePlugin;
import core.game.content.dialogue.FacialExpression;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.plugin.Initializable;

@Initializable
public class AliTheKebabSeller extends DialoguePlugin {
    public AliTheKebabSeller(){
        /**
         * Empty
         */
    }
    public AliTheKebabSeller(Player player){ super(player);}

    @Override
    public DialoguePlugin newInstance(Player player){return new AliTheKebabSeller(player);}
    @Override
    public boolean open(Object... args){
        npc = (NPC)args[0];
        player("Hello");
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId){
        boolean hasSauced = false;
        switch(stage){
            case 0:
                npc("Hello. What can I do for you?");
                stage = 1;
                break;
            case 1:
                player("I don't know, what can you do for me?");
                stage = 2;
                break;
            case 2:
                npc("Well, that depends.");
                stage = 3;
                break;
            case 3:
                player("Depends on what?");
                stage = 4;
                break;
            case 4:
                npc("It depends on whether you like kebabs or not.");
                stage = 5;
                break;
            case 5:
                player("Why is that?");
                stage = 6;
                break;
            case 6:
                npc("Seeing as I'm in the kebab construction industry.","I mainly help people who have want of a kebab.");
                stage = 7;
                break;
            case 7:
                player(FacialExpression.THINKING,"Well then, what kind of kebabs do you, erm, construct?");
                stage = 8;
                break;
            case 8:
                npc("I offer two different types of kebabs: the standard","run-of-the-mill kebab seen throughout 2009scape and","enjoyed by many a intoxicated dwarf, and my specialty,","the extra-hot kebab. So which shall it be?");
                stage = 9;
                break;
            case 9:
                npc("Or are my services even required?");
                stage = 10;
                break;
            case 10:
                interpreter.sendOptions("Select one","Yes, thanks.","No thanks, I'm good");
                stage = 11;
                break;
            case 11:
                switch(buttonId){
                    case 1:
                        player("Yes, thanks.");
                        stage = 12;
                        break;
                    case 2:
                        player("No thanks, I'm good.");
                        stage = 100;
                        break;
                }
                break;
            case 12:
                interpreter.sendOptions("Select one.","I want a kebab, please.", "Could I have one of those crazy hot kebabs of yours?","Would you sell me that bottle of special kebab sauce?", "What is the difference between the standard and extra hot?","I need some information.");
                stage = 13;
                break;
            case 13:
                switch(buttonId){
                    case 1:
                        player("I want a kebab, please.");
                        stage = 20;
                        break;
                    case 2:
                        player("Could I have one of those crazy hot kebabs of yours?");
                        stage = 30;
                        break;
                    case 3:
                        player("Would you sell me that bottle of special kebab sauce?");
                        stage = 40;
                        break;
                    case 4:
                        player("What is the difference between the standard and extra hot?");
                        stage = 50;
                        break;
                    case 5:
                        player("I need some information.");
                        stage = 60;
                        break;
                }
                break;
            case 20:
                npc("That will be 3 coins please.");
                if(player.getInventory().contains(995,3)) {
                    stage = 21;
                } else {
                    stage = 25;
                }
                break;
            case 21:
                npc("Here you go.");
                player.getInventory().remove(new Item(995,3));
                player.getInventory().add(new Item(1971));
                stage = 22;
                break;
            case 22:
                npc("Is there anything else I can do for you?");
                stage = 10;
                break;
            case 25:
                player("I seem to have not brought enough money with me. Sorry.");
                stage = 22;
                break;
            case 30:
                npc("A super kebab it is so. Be careful, they really are as hot as","they are made out to be.");
                stage = 31;
                break;
            case 31:
                player("Sure, sure.");
                stage = 32;
                break;
            case 32:
                npc("Here you go.");
                player.getInventory().add(new Item(4608));
                stage = 33;
                break;
            case 33:
                player("Thanks.");
                stage = 22;
                break;
            case 40:
                if(!player.getInventory().containsItem(new Item(4610))) {
                    npc("Well, that depends on what you have in mind.");
                    stage = 41;
                    break;
                } else {
                    npc("I already gave it to you!");
                    stage = 22;
                    break;
                }
            case 41:
                player("Set yourself at east, I have no intention of setting up a","rival kebab shop.");
                stage = 42;
                break;
            case 42:
                npc("Well, then, what do you want it for?");
                stage = 43;
                break;
            case 43:
                player("I don't know, but I think I could have a little fun with it.");
                stage = 44;
                break;
            case 44:
                npc("You're not going to put it in drunken Ali's drink"," now, are you? That's what happened last time","I gave someone the sauce.");
                stage = 45;
                break;
            case 45:
                player("No, that would be a bit cliche, I think I'll come up with","something more original.");
                stage = 46;
                break;
            case 46:
                npc("Just be careful with it, it's potent enough to give a camel","the runs.");
                player.getInventory().add(new Item(4610));
                stage = 47;
                break;
            case 47:
                player("Thank you very much.");
                stage = 22;
                break;
            case 50:
                npc("If I told you that I would have to kill you. Kebab sales is","a cut-throat industry. The extra-hot kebab gives","me the competitive edge on the rest and if I were to","divulge my secrets to every passing adventurer, well it");
                stage = 51;
                break;
            case 51:
                npc("would soon disappear.");
                stage = 52;
                break;
            case 52:
                player("So there's no difference except the sauce you use then!");
                stage = 53;
                break;
            case 53:
                npc("Shh! Keep your voice down. You should have told me you","were from the union.");
                stage = 22;
                break;
            case 60:
                npc("If information is what you need, there are many better"," people to ask than myself who could help you. One of the","town's street urchins or perhaps the local drunk.","I may be many things to many people, but a");
                stage = 61;
                break;
            case 61:
                npc("gossip I am not.");
                stage = 22;
                break;
            case 100:
                end();
                break;
        }
        return true;
    }
    @Override
    public int[] getIds() {return new int[] {1865};}
}
