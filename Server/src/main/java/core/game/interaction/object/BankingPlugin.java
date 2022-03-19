package core.game.interaction.object;

import static api.ContentAPIKt.*;

import api.events.InteractionEvent;
import core.cache.def.impl.NPCDefinition;
import core.cache.def.impl.SceneryDefinition;
import core.game.component.CloseEvent;
import core.game.component.Component;
import core.game.component.ComponentDefinition;
import core.game.component.ComponentPlugin;
import core.game.container.access.InterfaceContainer;
import core.game.content.dialogue.DialogueAction;
import core.game.content.dialogue.DialoguePlugin;
import core.game.content.dialogue.FacialExpression;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.IronmanMode;
import core.game.node.entity.player.link.diary.DiaryType;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.system.task.Pulse;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.plugin.Plugin;
import kotlin.Unit;
import rs09.game.content.dialogue.DepositAllDialogue;
import rs09.game.ge.GrandExchangeRecords;
import rs09.game.ge.GrandExchangeOffer;
import rs09.game.world.GameWorld;

import java.text.NumberFormat;

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
        SceneryDefinition.setOptionHandler("use-quickly", this);
        SceneryDefinition.setOptionHandler("use", this);
        SceneryDefinition.setOptionHandler("bank", this);
        SceneryDefinition.setOptionHandler("collect", this);
        SceneryDefinition.setOptionHandler("deposit", this);
        new BankingInterface().newInstance(arg);
        new BankDepositInterface().newInstance(arg);
        new BankNPCPlugin().newInstance(arg);
        new BankNPC().newInstance(arg);
        new BankerDialogue().init();
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        final Scenery object = (Scenery) node;
        if (player.getIronmanManager().checkRestriction(IronmanMode.ULTIMATE)) {
            return true;
        }
        if (object.getName().contains("Bank") || object.getName().contains("Deposit")) {
            //TODO: REPLACE THIS ALL WITH A LISTENER. I DONT FEEL LIKE IT RIGHT NOW.
            player.dispatch(new InteractionEvent(object, option));
            switch (option) {
                case "use":
//				final Location l = object.getLocation();
//				final Location p = player.getLocation();
//				final NPC npc = Repository.findNPC(l.transform(l.getX() - p.getX(), l.getY() - p.getY(), 0));
//				if (node.getId() == 4483) {
//					player.getBank().open();
//					return true;
//				}
//				if (npc != null && DialogueInterpreter.contains(npc.getId())) {
//					npc.faceLocation(node.getLocation());
//					player.getDialogueInterpreter().open(npc.getId(), npc.getId());
//				} else {
//					player.getDialogueInterpreter().open(494);
//				}
//				return true;
                case "use-quickly":
                case "bank":
                    player.getBankPinManager().openType(1);
                    checkAchievements(player);
                    return true;
                case "collect":
                    player.getExchangeRecords().openCollectionBox();
                    return true;
                case "deposit":
                    openDepositBox(player);
                    return true;
            }
        }
        return true;
    }

    /**
     * Method used to open the deposit box.
     *
     * @param player the player.
     */
    private void openDepositBox(final Player player) {
        player.getInterfaceManager().open(new Component(11)).setCloseEvent(new CloseEvent() {
            @Override
            public boolean close(Player player, Component c) {
                player.getInterfaceManager().openDefaultTabs();
                return true;
            }
        });
        //player.getInterfaceManager().closeDefaultTabs();
        player.getInterfaceManager().removeTabs(0, 1, 2, 3, 4, 5, 6);
        InterfaceContainer.generateItems(player, player.getInventory().toArray(), new String[]{"Examine", "Deposit-X", "Deposit-All", "Deposit-10", "Deposit-5", "Deposit-1",}, 11, 15, 5, 7);
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
        private static final int[] NPC_IDS = {44, 45, 166, 494, 495, 496, 497, 498, 499, 902, 1036, 1360, 1702, 2163, 2164, 2354, 2355, 2568, 2569, 2570, 2619, 3046, 3198, 3199, 3824, 4296, 5257, 5258, 5259, 5260, 5383, 5488, 5776, 5777, 5898, 5912, 5913, 6200, 6362, 6532, 6533, 6534, 6535, 6538, 7049, 7050, 7445, 7446, 7605};

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
            for (GrandExchangeRecords.OfferRecord r : player.getExchangeRecords().getOfferRecords()) {
                GrandExchangeOffer o = player.getExchangeRecords().getOffer(r);
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
                    player(FacialExpression.LAUGH,"Well of course I can, a man of my","status could afford a measly bank fee");
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
                                    checkAchievements(player);
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
                                    checkAchievements(player);
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

    /**
     * Represents the component plugin used to handle banking interfaces.
     *
     * @author Emperor
     * @author 'Vexia
     * @version 1.0
     */
    public static final class BankingInterface extends ComponentPlugin {

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            ComponentDefinition.put(763, this);
            ComponentDefinition.put(762, this);
            ComponentDefinition.put(767, this);
            return this;
        }

        @Override
        public void open(Player player, Component component) {
            super.open(player, component);
            player.getBank().sendBankSpace();
        }

        @Override
        public boolean handle(final Player p, Component component, int opcode, int button, final int slot, int itemId) {
            final Item item = component.getId() == 762 ? p.getBank().get(slot) : p.getInventory().get(slot);
            switch (component.getId()) {
                case 767:
                    switch (button) {
                        case 10:
                            p.getBank().open();
                            break;
                    }
                    break;
                case 762:
                    switch (button) {
                        case 18:
                            p.getDialogueInterpreter().open(new DepositAllDialogue().getID());
                            return true;
                        case 23:
                            p.getDialogueInterpreter().sendOptions("Select an Option", "Check bank value", "Banking assistance", "Close");
                            p.getDialogueInterpreter().addAction(new DialogueAction() {

                                @Override
                                public void handle(Player player, int buttonId) {
                                    switch (buttonId) {
                                        case 2:
                                            p.getDialogueInterpreter().sendItemMessage(new Item(995, 50000), "<br>Your bank is worth approximately <col=a52929>" + NumberFormat.getInstance().format(p.getBank().getWealth()) + "</col> coins.");
                                            break;
                                        case 3:
                                            p.getBank().close();
                                            p.getInterfaceManager().open(new Component(767));
                                            break;
                                        case 4:
                                            p.getDialogueInterpreter().close();
                                            break;
                                    }
                                }

                            });
                            break;
                        case 14:
                            p.getBank().setInsertItems(!p.getBank().isInsertItems());
                            return true;
                        case 16:
                            p.getBank().setNoteItems(!p.getBank().isNoteItems());
                            return true;
                        case 73:
                            int amount = 0;
                            switch (opcode) {
                                case 155:
                                    amount = 1;
                                    break;
                                case 196:
                                    amount = 5;
                                    break;
                                case 124:
                                    amount = 10;
                                    break;
                                case 199:
                                    amount = p.getBank().getLastAmountX();
                                    break;
                                case 234:
                                    sendInputDialogue(p, false, "Enter the amount:", (value) -> {
                                        String s = value.toString();
                                        s = s.replace("k","000");
                                        s = s.replace("K","000");
                                        s = s.replace("m","000000");
                                        s = s.replace("M","000000");
                                        int val = Integer.parseInt(s);
                                        p.getBank().takeItem(slot, val);
                                        p.getBank().updateLastAmountX(val);
                                        return Unit.INSTANCE;
                                    });
                                    break;
                                case 9:
                                    p.sendMessage(p.getBank().get(slot).getDefinition().getExamine());
                                    break;
                                case 168:
                                    amount = p.getBank().getAmount(item);
                                    break;
                                case 166:
                                    amount = p.getBank().getAmount(item) - 1;
                                    break;
                                default:
                                    return false;
                            }
                            if (amount > 0) {
                                final int withdraw = amount;
						GameWorld.getPulser().submit(new Pulse(1, p) {
                                    @Override
                                    public boolean pulse() {
                                        if (item == null) {
                                            return true;
                                        }
                                        p.getBank().takeItem(slot, withdraw);
                                        return true;
                                    }
                                });
                            }
                            return true;
                        case 20://search
                            p.setAttribute("search", true);
                            break;
                        case 41:
                        case 39:
                        case 37:
                        case 35:
                        case 33:
                        case 31:
                        case 29:
                        case 27:
                        case 25:
                            if (p.getAttribute("search", false)) {
                                p.getBank().reopen();
                            }
                            int tabIndex = -((button - 41) / 2);
                            switch (opcode) {
                                case 155:
                                    p.getBank().setTabIndex(tabIndex);
                                    return true;
                                case 196:
                                    p.getBank().collapseTab(tabIndex);
                                    return true;
                            }
                            return false;
                    }
                    break;
                case 763:// overlay.
                    switch (opcode) {
                        case 155:
                            p.getBank().addItem(slot, 1);
                            break;
                        case 196:
                            p.getBank().addItem(slot, 5);
                            break;
                        case 124:
                            p.getBank().addItem(slot, 10);
                            break;
                        case 199:
                            p.getBank().addItem(slot, p.getBank().getLastAmountX());
                            break;
                        case 234:
                            sendInputDialogue(p, false, "Enter the amount:", (value) -> {
                                String s = value.toString();
                                s = s.replace("k","000");
                                s = s.replace("K","000");
                                int val = Integer.parseInt(s);
                                p.getBank().addItem(slot, val);
                                p.getBank().updateLastAmountX(val);
                                return Unit.INSTANCE;
                            });
                            break;
                        case 168:
                            p.getBank().addItem(slot, p.getInventory().getAmount(item));
                            break;
                    }
                    break;
            }
            return true;
        }

    }

    /**
     * Represents the bank deposit interface handler.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public static final class BankDepositInterface extends ComponentPlugin {

        /**
         * Represents the deposit animation.
         */
        private static final Animation DEPOSIT_ANIMATION = new Animation(834);

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            ComponentDefinition.put(11, this);
            return this;
        }

        @Override
        public boolean handle(final Player p, Component component, int opcode, int button, final int slot, int itemId) {
            final Item item = p.getInventory().get(slot);
            if (item == null && button != 15 && button != 13) {
                return true;
            }
            switch (component.getId()) {
                case 11:
                    p.animate(DEPOSIT_ANIMATION);
                    switch (opcode) {
                        case 155: // Deposit items
                            p.getBank().addItem(slot, 1);
                            break;
                        case 196:
                            p.getBank().addItem(slot, 5);
                            break;
                        case 124:
                            p.getBank().addItem(slot, 10);
                            break;
                        case 199:
                            p.getPulseManager().run(new Pulse(1, p) {
                                @Override
                                public boolean pulse() {
                                    p.getBank().addItem(slot, p.getInventory().getAmount(item));
                                    InterfaceContainer.generateItems(p, p.getInventory().toArray(), new String[]{"Examine", "Deposit-X", "Deposit-All", "Deposit-10", "Deposit-5", "Deposit-1",}, 11, 15, 5, 7);
                                    return true;
                                }
                            });
                            return true;
                        case 234:
                            sendInputDialogue(p, false, "Enter the amount:", (value) -> {
                                String s = value.toString();
                                s = s.replace("k","000");
                                s = s.replace("K","000");
                                int val = Integer.parseInt(s);
                                p.getBank().addItem(slot, val);
                                InterfaceContainer.generateItems(p, p.getInventory().toArray(), new String[]{"Examine", "Deposit-X", "Deposit-All", "Deposit-10", "Deposit-5", "Deposit-1",}, 11, 15, 5, 7);
                                return Unit.INSTANCE;
                            });
                            break;
                        case 168:
                            p.sendMessage(item.getDefinition().getExamine());
                            break;
                    }
                    switch (button) {
                        case 13:
                            p.getFamiliarManager().dumpBob();
                            break;
                    }
                    break;
            }
            InterfaceContainer.generateItems(p, p.getInventory().toArray(), new String[]{"Examine", "Deposit-X", "Deposit-All", "Deposit-10", "Deposit-5", "Deposit-1",}, 11, 15, 5, 7);
            return true;
        }

    }

    /**
     * Represents the plugin used to handle the banker npc.
     *
     * @author 'Vexia
     * @author Emperor
     * @version 1.01
     */
    public static final class BankNPCPlugin extends OptionHandler {

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            NPCDefinition.setOptionHandler("bank", this);
            NPCDefinition.setOptionHandler("collect", this);
            return this;
        }

        @Override
        public boolean handle(Player player, Node node, String option) {
            final NPC npc = ((NPC) node);
            npc.faceLocation(node.getLocation());
            if (option.equals("bank")) {
                player.getBank().open();
                checkAchievements(player);
            } else {
                player.getExchangeRecords().openCollectionBox();
            }
            return true;
        }

        @Override
        public Location getDestination(Node n, Node node) {
            NPC npc = (NPC) node;
            if (npc.getAttribute("facing_booth", false)) {
                Direction dir = npc.getDirection();
                return npc.getLocation().transform(dir.getStepX() << 1, dir.getStepY() << 1, 0);
            }
            if (npc.getId() == 6533) {
                return Location.create(3167, 3489, 0);// ge bankers.
            } else if (npc.getId() == 6534) {
                return Location.create(3167, 3490, 0);// ge bankers.
            } else if (npc.getId() == 6535) {
                return Location.create(3162, 3489, 0);
            } else if (npc.getId() == 6532) {
                return Location.create(3162, 3489, 0);
            } else if (npc.getId() == 4907) {
                return npc.getLocation().transform(0, -2, 0);
            }
            return super.getDestination(npc, node);
        }
    }

    /**
     * Represents the abstract npc of a banker.
     *
     * @author 'Vexia
     * @author Emperor
     * @version 1.2
     */
    public static final class BankNPC extends AbstractNPC {

        /**
         * Represents the ids of this class.
         */
        private final int[] IDS = new int[]{4907, 44, 45, 166, 494, 495, 496, 497, 498, 499, 902, 1036, 1360, 1702, 2163, 2164, 2354, 2355, 2568, 2569, 2570, 2619, 3046, 3198, 3199, 4296, 4519, 5257, 5258, 5259, 5260, 5383, 5488, 5776, 5777, 5898, 5912, 5913, 6200, 6362, 6532, 6533, 6534, 6535, 6538, 7049, 7050, 7445, 7446, 7605};

        /**
         * Constructs a new {@code BankNPC} {@code Object}.
         */
        public BankNPC() {
            super(0, null);
        }

        @Override
        public void init() {
            super.init();
            for (int i = 0; i < 4; i++) {
                Direction dir = Direction.get(i);
                Location loc = getLocation().transform(dir.getStepX(), dir.getStepY(), 0);
                Scenery bank = RegionManager.getObject(loc);
                if (bank != null && bank.getName().equals("Bank booth")) {
                    setDirection(dir);
                    setAttribute("facing_booth", true);
                    super.setWalks(false);
                    break;
                }
            }
        }

        /**
         * Constructs a new {@code BankNPC} {@code Object}.
         *
         * @param id       The NPC id.
         * @param location The location.
         */
        private BankNPC(int id, Location location) {
            super(id, location);
        }

        @Override
        public AbstractNPC construct(int id, Location location, Object... objects) {
            return new BankNPC(id, location);
        }

        @Override
        public int[] getIds() {
            return IDS;
        }
    }

    private static void checkAchievements(Player player) {
		// Access the bank in Draynor Village
		if (player.getLocation().withinDistance(Location.create(3092, 3243, 0))) {
			player.getAchievementDiaryManager().finishTask(player, DiaryType.LUMBRIDGE, 1, 15);
		}
	}
}
