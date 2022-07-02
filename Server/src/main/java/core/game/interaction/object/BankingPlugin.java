package core.game.interaction.object;

import core.game.content.dialogue.DialoguePlugin;
import core.game.content.dialogue.FacialExpression;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.IronmanMode;
import core.game.node.entity.player.link.appearance.Gender;
import core.game.node.entity.player.link.diary.DiaryType;
import core.game.node.item.Item;
import core.plugin.Initializable;
import core.plugin.Plugin;
import rs09.game.ge.GrandExchangeOffer;
import rs09.game.ge.GrandExchangeRecords;
import rs09.game.world.GameWorld;

/**
 * Represents the plugin used for anything related to banking.
 *
 * @author Vexia
 * @author Emperor
 */
@Initializable
public final class BankingPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        new BankerDialogue().init();
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        player.sendChat(":crab: :crab: :crab: RETARDED PLUGIN IS GONE :crab: :crab: :crab:");
        return true;
    }

    /**
     * Represents the dialogue plugin used for all bankers.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public static final class BankerDialogue extends DialoguePlugin {

        /**
         * Represents the banker npc ids.
         */
        private static final int[] NPC_IDS = {44, 45, 166, 494, 495, 496, 497, 498, 499, 902, 1036, 1360, 1702, 2163, 2164, 2354, 2355, 2568, 2569, 2570, 2619, 3046, 3198, 3199, 4296, 5257, 5258, 5259, 5260, 5383, 5488, 5776, 5777, 5898, 5912, 5913, 6200, 6362, 6532, 6533, 6534, 6535, 6538, 7049, 7050, 7445, 7446, 7605};

        /**
         * Represents the id to use.
         */
        private int id;

        /**
         * Constructs a new {@code BankerDialogue} {@code Object}.
         */
        public BankerDialogue() {
            /**
             * empty.
             */
        }

        /**
         * Constructs a new {@code BankerDialoguePlugin} {@code Object}.
         *
         * @param player the player.
         */
        public BankerDialogue(Player player) {
            super(player);
        }

        @Override
        public DialoguePlugin newInstance(Player player) {
            return new BankerDialogue(player);
        }

        @Override
        public boolean open(Object... args) {
            if (args[0] instanceof NPC) {
                setId(((NPC) args[0]).getId());
            } else {
                setId((int) args[0]);
            }
            interpreter.sendDialogues(id, FacialExpression.HALF_GUILTY, "Good day, How may I help you?");
            GrandExchangeRecords records = GrandExchangeRecords.getInstance(player);
            for (GrandExchangeRecords.OfferRecord r : records.getOfferRecords()) {
                GrandExchangeOffer o = records.getOffer(r);
                if (o != null && (o.getWithdraw()[0] != null || o.getWithdraw()[1] != null)) {
                    stage = -1;
                    break;
                }
            }
            return true;
        }

        @Override
        public boolean handle(int interfaceId, int buttonId) {
            switch (stage) {
                case -1:
                    interpreter.sendDialogues(id, FacialExpression.HALF_GUILTY, "Before we go any further, I should inform you that you", "have items ready for collection from the Grand Exchange.");
                    stage = 0;
                    break;
                case 0:
                    if(player.getAttribute("UnlockedSecondaryBank",false)){
                        interpreter.sendOptions("What would you like to say?", "I'd like to access my bank account, please.", "I'd like to check my PIN settings.", "I'd like to see my collection box.", "I'd like to switch to my " + (player.useSecondaryBank ? "primary": "secondary") + " bank account.", "What is this place?");
                        stage = 10;
                    }
                    else if(!player.getAttribute("UnlockedSecondaryBank",false)){
                        interpreter.sendOptions("What would you like to say?", "I'd like to access my bank account, please.", "I'd like to check my PIN settings.", "I'd like to see my collection box.", "Can I open a second bank account?", "What is this place?");
                        stage = 20;
                    }
                    break;
                case 1:
				interpreter.sendDialogues(id, FacialExpression.HALF_GUILTY, "This is a branch of the Bank of " + GameWorld.getSettings().getName() + ". We have", "branches in many towns.");
                    stage = 2;
                    break;
                case 2:
                    interpreter.sendOptions("What would you like to say?", "And what do you do?", "Didn't you used to be called the Bank of Varrock?");
                    stage = 3;
                    break;
                case 3:
                    if (buttonId == 1) {
                        player("And what do you do?");
                        stage = 4;
                    } else if (buttonId == 2) {
                        player("Didn't you used to be called the Bank of Varrock?");
                        stage = 5;
                    }
                    break;
                case 4:
                    interpreter.sendDialogues(id, FacialExpression.HALF_GUILTY, "We will look after your items and money for you.", "Leave your valuables with us if you want to keep them", "safe.");
                    stage = 100;
                    break;
                case 5:
                    interpreter.sendDialogues(id, FacialExpression.HALF_GUILTY, "Yes we did, but people kept on coming into our", "signs were wrong. They acted as if we didn't know", "what town we were in or something.");
                    stage = 100;
                    break;
                case 6:
                    player.useSecondaryBank = !player.useSecondaryBank;
                    interpreter.sendDialogues(id, FacialExpression.HALF_GUILTY, "I've switched you over to your " + (player.useSecondaryBank ? "secondary" : "primary") + " bank account");
                    stage = 100;
                    break;
                case 7:
                    interpreter.sendDialogues(id, FacialExpression.WORRIED, "A second account??? What is four hundred and ninety","six slots not enough for you??? What are","you even hoarding?");
                    stage = 8;
                    break;
                case 8:
                    player(FacialExpression.ANNOYED,"Listen, an entrepreneur like me needs","a lot of space for my business ventures");
                    stage = 9;
                    break;
                case 9:
                    interpreter.sendDialogues(id, FacialExpression.ANNOYED,"Oh an entrepreneur eh? Well I guess a rich","entrepreneur like you can afford the fee","for opening a second bank account");
                    stage = 11;
                    break;
                case 11:
                    player(FacialExpression.LAUGH,"Well of course I can, a " + (player.isMale() ? "man" : "woman") + " of my","status could afford a measly bank fee");
                    stage = 12;
                    break;
                case 12:
                    interpreter.sendDialogues(id,FacialExpression.FRIENDLY,"Okay then, that'll be a one time","fee of five million gold coins, we just need","your payment and for you to sign here");
                    stage = 13;
                    break;
                case 13:
                    player(FacialExpression.ANGRY,"FIVE MILLION!?!");
                    stage = 14;
                    break;
                case 14:
                    npcl(FacialExpression.FRIENDLY, "Yes, Five million. These banks are very expensive to upkeep and maintain, but that is the final price. Can't someone with your stature and wealth afford such a trivial fee?");
                    stage = 15;
                    break;
                case 15:
                    interpreter.sendOptions("Put your money where your mouth is?","Yes","No");
                    stage = 16;
                    break;
                case 16:
                    if(buttonId == 1 && player.getInventory().contains(995,5000000)){
                        player(FacialExpression.ANGRY_WITH_SMILE,"Haha yes, just a trivial fee haha.","just a drop in the bucket...");
                        stage = 21;
                    }
                    if(buttonId == 2 || !player.getInventory().contains(995,5000000)){
                        player(FacialExpression.AFRAID,"Well of course I can, let me just get it out of... my bank","uhhhhhh... haha...");
                        stage = 17;
                    }
                    break;
                case 17:
                    interpreter.sendDialogues(id,FacialExpression.HALF_ROLLING_EYES,"You know I can see your bank, right?");
                    stage = 18;
                    break;
                case 18:
                    player("Yeah... I'll uhh, I'll be back later");
                    stage = 100;
                    break;
                case 21:
                    interpreter.sendDialogues(id,FacialExpression.AMAZED,"Wow I've never even SEEN thi- I mean *ahem*","give me one minute while I process this.");
                    stage = 22;
                    break;
                case 22:
                    if(player.getInventory().remove(new Item(995,5000000))) {
                        player.getGameAttributes().setAttribute("/save:UnlockedSecondaryBank",true);
                        interpreter.sendDialogues(id,FacialExpression.FRIENDLY,"You're all set! Whenever you want to switch to","your second bank account just ask","a teller and we'll swap it over for","you");
                        stage = 100;
                    } else {
                        end();
                    }
                    break;
                case 100:
                    end();
                    break;
                case 999:
                    options("Yes.", "No.");
                    stage = 1000;
                    break;
                case 1000:
                    switch (buttonId) {
                        case 1:
                            end();
                            break;
                        case 2:
                            end();
                            break;
                    }
                    break;
                case 10:
                    switch (interfaceId) {
                        case 234:
                            switch (buttonId) {
                                case 1:
                                case 2:
                                case 3:
                                    player.getBankPinManager().openType(buttonId);
                                    end();
                                    break;
                                case 4:
                                    player("I'd like to switch to my " + (player.useSecondaryBank ? "primary": "secondary") + " bank account");
                                    stage = 6;
                                    break;
                                case 5:
                                    player("What is this place?");
                                    stage = 1;
                                    break;
                            }
                            break;
                    }
                    break;
                case 20:
                    switch (interfaceId) {
                        case 234:
                            switch (buttonId) {
                                case 1:
                                case 2:
                                case 3:
                                    player.getBankPinManager().openType(buttonId);
                                    end();
                                    break;
                                case 4:
                                    player("Can I open a second bank account?");
                                    stage = 7;
                                    break;
                                case 5:
                                    player("What is this place?");
                                    stage = 1;
                                    break;
                            }
                    }
            }
            return true;
        }

        @Override
        public int[] getIds() {
            return NPC_IDS;
        }

        /**
         * Gets the id.
         *
         * @return The id.
         */
        public int getId() {
            return id;
        }

        /**
         * Sets the id.
         *
         * @param id The id to set.
         */
        public void setId(int id) {
            this.id = id;
        }

    }
}
