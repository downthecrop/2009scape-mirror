package content.region.misthalin.wiztower.handlers;

import static core.api.ContentAPIKt.*;
import core.cache.def.impl.NPCDefinition;
import core.cache.def.impl.SceneryDefinition;
import core.game.component.Component;
import core.game.dialogue.DialoguePlugin;
import core.game.global.Skillcape;
import core.game.global.action.ClimbActionHandler;
import core.game.global.action.DoorActionHandler;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.combat.spell.CombatSpell;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.SpellBookManager.SpellBook;
import core.game.node.entity.player.link.diary.DiaryType;
import core.game.node.entity.player.link.quest.Quest;
import core.game.node.entity.skill.Skills;
import core.game.node.item.GroundItem;
import core.game.node.item.GroundItemManager;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.system.task.Pulse;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.net.packet.PacketRepository;
import core.net.packet.context.ChildPositionContext;
import core.net.packet.out.RepositionChild;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.tools.RandomFunction;
import core.tools.StringUtils;
import kotlin.Unit;
import content.global.travel.EssenceTeleport;
import core.game.world.GameWorld;
import core.plugin.ClassScanner;
import content.data.Quests;

/**
 * Represents the plugins used related to the wizard tower.
 *
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class WizardTowerPlugin extends OptionHandler {

    /**
     * Represents the location of the wizard tower.
     */
    private static final Location BASEMENT = new Location(3104, 9576, 0);

    /**
     * Represents the location of the ground floor.
     */
    private static final Location GROUND_FLOOR = new Location(3105, 3162, 0);

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(12540).getHandlers().put("option:search", this);
		SceneryDefinition.forId(12539).getHandlers().put("option:search", this);
		SceneryDefinition.forId(2147).getHandlers().put("option:climb-down", this);
		SceneryDefinition.forId(32015).getHandlers().put("option:climb-up", this);
		NPCDefinition.forId(300).getHandlers().put("option:teleport", this);
		SceneryDefinition.forId(11993).getHandlers().put("option:open", this);
		ClassScanner.definePlugin(new WizardtowerWizardNPC());
		ClassScanner.definePlugin(new WizardTowerDialogue());
		ClassScanner.definePlugin(new WizardMizgogDialogue());
		ClassScanner.definePlugin(new WizardGrayzagDialogue());
		ClassScanner.definePlugin(new WizardDialogue());
		ClassScanner.definePlugin(new SedridorDialogue());
		ClassScanner.definePlugin(new AuburyDialoguePlugin());
		return this;
	}

    @Override
    public boolean handle(Player player, Node node, String option) {
        switch (option) {
            case "teleport":
                if (!player.getQuestRepository().isComplete(Quests.RUNE_MYSTERIES)) {
                    player.getPacketDispatch().sendMessage("You need to have completed the Rune Mysteries Quest to use this feature.");
                    return true;
                }
                EssenceTeleport.teleport(((NPC) node), player);
                break;
            case "climb-down":
                player.getProperties().setTeleportLocation(BASEMENT);
                break;
            case "climb-up":
                if (node.getLocation().equals(new Location(3017, 10249))) {
                    ClimbActionHandler.climb(player, new Animation(828), new Location(3069, 3857, 0));
                    return true;
                }
                if (node.getLocation().equals(new Location(3069, 10256))) {
                    ClimbActionHandler.climb(player, new Animation(828), new Location(3017, 3850, 0));
                    return true;
                }
                if (player.getLocation().withinDistance(Location.create(3117, 9753, 0))) {
                    ClimbActionHandler.climb(player, new Animation(828), new Location(3092, 3361, 0));
                    return true;
                }
                if (!Location.create(3103, 9576, 0).equals(((Scenery) node).getLocation())) {
                    return SceneryDefinition.getOptionHandler(2147, "climb-up").handle(player, node, option);
                }
                player.getProperties().setTeleportLocation(GROUND_FLOOR);
                break;
            case "search":
                player.getDialogueInterpreter().open(458543948);
                break;
            case "open":
                if (node.getLocation().equals(new Location(3107, 3162, 0))) {
                    DoorActionHandler.handleAutowalkDoor(player, (Scenery) node, player.getLocation().getX() >= 3107 ? Location.create(3106, 3161, 0) : Location.create(3108, 3163, 0));
                } else {
                    DoorActionHandler.handleDoor(player, (Scenery) node);
                }
                break;
        }
        return true;
    }

    @Override
    public Location getDestination(Node node, Node n) {
        if (n instanceof Scenery) {
            final Scenery object = (Scenery) n;
            if (object.getId() == 11993 && object.getLocation().equals(new Location(3107, 3162, 0))) {
                return node.getLocation().getX() >= 3107 ? Location.create(3108, 3163, 0) : Location.create(3106, 3161, 0);
            }
        }
        return null;
    }

    /**
     * Represents a wizard at the wizard tower.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public static final class WizardtowerWizardNPC extends AbstractNPC {

        /**
         * The NPC ids of NPCs using this plugin.
         */
        private static final int[] ID = {13};

        /**
         * Constructs a new {@code WizardtowerWizardNPC} {@code Object}.
         */
        public WizardtowerWizardNPC() {
            super(0, null);
        }

        /**
         * Constructs a new {@code AlKharidWarriorPlugin} {@code Object}.
         *
         * @param id       The NPC id.
         * @param location The location.
         */
        private WizardtowerWizardNPC(int id, Location location) {
            super(id, location);
        }

        @Override
        public AbstractNPC construct(int id, Location location, Object... objects) {
            return new WizardtowerWizardNPC(id, location);
        }

        @Override
        public void init() {
            super.init();
            getProperties().getCombatPulse().setStyle(CombatStyle.MAGIC);
            getProperties().setAutocastSpell((CombatSpell) SpellBook.MODERN.getSpell(8));
        }

        @Override
        public int[] getIds() {
            return ID;
        }

    }

    /**
     * Represents the dialogue used in the wizard tower.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public final class WizardTowerDialogue extends DialoguePlugin {

        /**
         * Represents the book name to use.
         */
        private String bookName = "";

        /**
         * Represents the book names.
         */
        private final String[] books = new String[]{"Living with a Wizard Husband - a Housewife's Story", "Wind Strike for Beginners", "So you think you're a Mage? Volume 28", "101 Ways to Impress your Mates with Magic", "The Life & Times of a Thingummywut by Traiborn the Wizard", "How to become the Ulitmate Wizard of the Universe", "The Dark Arts of Magical Wands"};

        /**
         * Constructs a new {@code WizardTowerDialogue.java} {@code Object}.
         */
        public WizardTowerDialogue() {
        }

        /**
         * Constructs a new {@code WizardTowerDialogue.java} {@code Object}.
         *
         * @param player the player.
         */
        public WizardTowerDialogue(Player player) {
            super(player);
        }

        @Override
        public DialoguePlugin newInstance(Player player) {
            return new WizardTowerDialogue(player);
        }

        @Override
        public boolean open(Object... args) {
            sendDialogue("There's a large selection of books, the majority of which look fairly", "old. Some very strange names... You pick one at random :");
            bookName = books[RandomFunction.random(books.length)];
            stage = 0;
            return true;
        }

        @Override
        public boolean handle(int interfaceId, int buttonId) {
            switch (stage) {
                case 0:
                    sendDialogue("'" + bookName + "'");
                    stage = 1;
                    break;
                case 1:
                    sendDialogue("Interesting...");
                    stage = 2;
                    break;
                case 2:
                    end();
                    break;
            }
            return true;
        }

        @Override
        public int[] getIds() {
            return new int[]{458543948};
        }
    }

    /**
     * Handles the WizardMizgogDialogue dialogue.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public static final class WizardMizgogDialogue extends DialoguePlugin {

        /**
         * Represents the black bead item.
         */
        private static final Item BLACK_BEAD = new Item(1474);

        /**
         * Represents the red bead item.
         */
        private static final Item RED_BEAD = new Item(1470);

        /**
         * Represents the white bead item.
         */
        private static final Item WHITE_BEAD = new Item(1476);

        /**
         * Represents the yellow bead item.
         */
        private static final Item YELLOW_BEAD = new Item(1472);

        /**
         * Represents the animation.
         */
        private static final Animation ANIMATION = new Animation(4285);

        /**
         * Constructs a new {@code WizardMizgogDialogue} {@code Object}.
         */
        public WizardMizgogDialogue() {
            /**
             * empty.
             */
        }

        /**
         * Constructs a new {@code WizardMizgogDialogue} {@code Object}.
         *
         * @param player the player.
         */
        public WizardMizgogDialogue(Player player) {
            super(player);
        }

        @Override
        public DialoguePlugin newInstance(Player player) {
            return new WizardMizgogDialogue(player);
        }

        @Override
        public boolean open(Object... args) {
            npc = (NPC) args[0];
            Quest quest = player.getQuestRepository().getQuest(Quests.IMP_CATCHER);
            switch (quest.getStage(player)) {
                case 0:
                    player("Give me a quest!");
                    stage = 0;
                    break;
                case 10:
                    npc("So how are you doing finding my beads?");
                    stage = 0;
                    break;
                case 100:
                    options("Got any more quests?", "Do you know any interesting spells you could teach me?");
                    stage = 0;
                    break;
            }
            return true;
        }

        @Override
        public boolean handle(int interfaceId, int buttonId) {
            final Quest quest = player.getQuestRepository().getQuest(Quests.IMP_CATCHER);
            switch (quest.getStage(player)) {
                case 0:
                    switch (stage) {
                        case 0:
                            npc("Give me a quest what?");
                            stage = 1;
                            break;
                        case 1:
                            options("Give me a quest please.", "Give me a quest or else!", "Just stop messing around and give me a quest!");
                            stage = 2;
                            break;
                        case 2:
                            switch (buttonId) {
                                case 1:
                                    player("Give me a quest please.");
                                    stage = 10;
                                    break;
                                case 2:
                                    player("Give me a quest or else!");
                                    stage = 20;
                                    break;
                                case 3:
                                    player("Just stop messing around and give me a quest!");
                                    stage = 30;
                                    break;
                            }
                            break;
                        case 10:
                            npc("Well seeing as you asked nicely... I could do with some", "help.");
                            stage = 11;
                            break;
                        case 11:
                            npc("The wizard Grayzag next door decided he didn't like", "me so he enlisted an army of hundreds of imps.");
                            stage = 12;
                            break;
                        case 12:
                            npc("The imps stole all sorts of my things. Most of these", "things I don't really care about, just eggs and balls of", "string and things.");
                            stage = 13;
                            break;
                        case 13:
                            npc("But they stole my four magical beads. There was a red", "one, a yellow one, a black one, and a white one.");
                            stage = 14;
                            break;
                        case 14:
                            npc("These imps have now spread out all over the kingdom.", "Could you get my beads back for me?");
                            stage = 15;
                            break;
                        case 15:
                            options("I'll try", "I've better things to do than chase imps.");
                            stage = 16;
                            break;
                        case 16:
                            switch (buttonId) {
                                case 1:
                                    player("I'll try.");
                                    stage = 17;
                                    break;
                                case 2:
                                    player("I've better things to do than chase imps.");
                                    stage = 18;
                                    break;
                            }
                            break;
                        case 17:
                            npc("That's great, thank you.");
                            stage = 19;
                            break;
                        case 19:
                            quest.start(player);
                            player.getQuestRepository().syncronizeTab(player);
                            end();
                            break;
                        case 18:
                            end();
                            break;
                        case 20:
                            npc("Or else what? You'll attack me?");
                            stage = 21;
                            break;
                        case 21:
                            npc("Hahaha!");
                            stage = 22;
                            break;
                        case 22:
                            end();
                            break;
                        case 30:
                            npc("Ah now you're assuming I have one to give.");
                            stage = 31;
                            break;
                        case 31:
                            end();
                            break;
                    }
                    break;
                case 10:
                    switch (stage) {
                        case 0:
                            if (player.getInventory().containItems(BLACK_BEAD.getId(), RED_BEAD.getId(), YELLOW_BEAD.getId(), WHITE_BEAD.getId())) {
                                player("I've got all four beads. It was hard work I can tell you.");
                                stage = 5;
                                break;
                            }
                            if (!player.getInventory().containsItem(WHITE_BEAD) && !player.getInventory().containsItem(RED_BEAD) && !player.getInventory().containsItem(YELLOW_BEAD) && !player.getInventory().containsItem(BLACK_BEAD)) {
                                player("I've not found any yet.");
                                stage = 1;
                            } else {
                                player("I have found some of your beads.");
                                stage = 3;
                            }
                            break;
                        case 1:
                            npc("Well get on with it. I've lost a white bead, a read bead, a", "black bead, and a yellow bead. Go kill some imps!");
                            stage = 2;
                            break;
                        case 2:
                            end();
                            break;
                        case 3:
                            npc("Come back when you have them all. The colour of the", "four beads that I need are red, yellow, black, and white.", "Go chase some imps!");
                            stage = 4;
                            break;
                        case 4:
                            end();
                            break;
                        case 5:
                            npc("Give them here and I'll check that they really are MY", "beads, before I give you your reward. You'll like it, it's", "an amulet of accuracy.");
                            stage = 6;
                            break;
                        case 6:
                            sendDialogue("You give four coloured beads to Wizard Mizgog.");
                            stage = 7;
                            break;
                        case 7:
                            end();
                            if (player.getInventory().remove(BLACK_BEAD, WHITE_BEAD, RED_BEAD, YELLOW_BEAD)) {
                                player.lock(7);
                                npc.faceLocation(Location.create(3102, 3163, 2));
                                npc.animate(ANIMATION);
						GameWorld.getPulser().submit(new Pulse(3, player) {
                                    @Override
                                    public boolean pulse() {
                                        quest.finish(player);
                                        player.getQuestRepository().syncronizeTab(player);
                                        return true;
                                    }

                                });
                            }
                            break;
                    }
                    break;
                case 100:
                    switch (stage) {
                        case 0:
                            switch (buttonId) {
                                case 1:
                                    player("Got any more quests?");
                                    stage = 10;
                                    break;
                                case 2:
                                    player("Do you know any interesting spells you could teach me?");
                                    stage = 20;
                                    break;
                            }
                            break;
                        case 10:
                            npc("No, everything is good with the world today.");
                            stage = 11;
                            break;
                        case 11:
                            end();
                            break;
                        case 20:
                            npc("I don't think so, the type of magic I study involves", "years of meditation and research.");
                            stage = 11;
                            break;
                    }
            }
            return true;
        }

        @Override
        public int[] getIds() {
            return new int[]{706};
        }
    }

    /**
     * Handles the WizardGrayzagDialogue dialogue.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public final class WizardGrayzagDialogue extends DialoguePlugin {

        /**
         * Constructs a new {@code WizardGrayzagDialogue} {@code Object}.
         */
        public WizardGrayzagDialogue() {
            /**
             * empty.
             */
        }

        /**
         * Constructs a new {@code WizardGrayzagDialogue} {@code Object}.
         *
         * @param player the player.
         */
        public WizardGrayzagDialogue(Player player) {
            super(player);
        }

        @Override
        public DialoguePlugin newInstance(Player player) {
            return new WizardGrayzagDialogue(player);
        }

        @Override
        public boolean open(Object... args) {
            npc = (NPC) args[0];
            npc("Not now, I'm trying to concentrate on a", "very difficult spell!");
            stage = 0;
            return true;
        }

        @Override
        public boolean handle(int interfaceId, int buttonId) {
            switch (stage) {
                case 0:
                    end();
                    break;
            }
            return true;
        }

        @Override
        public int[] getIds() {
            return new int[]{707};
        }
    }

    /**
     * Handles the WizardDialogue dialogue.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public static final class WizardDialogue extends DialoguePlugin {

        /**
         * Represents the bark item used to make split bark equipment.
         */
        private static final Item BARK = new Item(3239);

        /**
         * Constructs a new {@code WizardDialogue} {@code Object}.
         */
        public WizardDialogue() {
            /**
             * empty.
             */
        }

        /**
         * Constructs a new {@code WizardDialogue.java} {@code Object}.
         *
         * @param player the player.
         */
        public WizardDialogue(Player player) {
            super(player);
        }

        @Override
        public DialoguePlugin newInstance(Player player) {
            return new WizardDialogue(player);
        }

        @Override
        public boolean open(Object... args) {
            npc = (NPC) args[0];
            npc("Hello there, can I help you?");
            stage = 0;
            return true;
        }

        @Override
        public boolean handle(int interfaceId, int buttonId) {
            switch (stage) {
                case 0:
                    options("What do you do here?", "What's that you're wearing?", "Can you make me some armour please?", "No thanks.");
                    stage = 1;
                    break;
                case 1:
                    switch (buttonId) {
                        case 1:
                            player("What do you do here?");
                            stage = 10;
                            break;
                        case 2:
                            player("What's that you're wearing?");
                            stage = 20;
                            break;
                        case 3:
                            player("Can you make me some armour please?");
                            stage = 30;
                            break;
                        case 4:
                            player("No thanks.");
                            stage = 40;
                            break;
                    }
                    break;
                case 30:
                    npc("Certainly, what would like to me to make?");
                    stage = 31;
                    break;
                case 31:
                    player.getInterfaceManager().openChatbox(new Component(306));
                    int count = 2;
                    int shift = 75;
                    PacketRepository.send(RepositionChild.class, new ChildPositionContext(player, 306, 26, 10, shift));
                    PacketRepository.send(RepositionChild.class, new ChildPositionContext(player, 306, 22, 88, shift));
                    PacketRepository.send(RepositionChild.class, new ChildPositionContext(player, 306, 18, 288, shift));
                    PacketRepository.send(RepositionChild.class, new ChildPositionContext(player, 306, 14, 188, shift + 5));
                    PacketRepository.send(RepositionChild.class, new ChildPositionContext(player, 306, 10, 380, shift));
                    int indexes[] = new int[]{26, 22, 14, 18, 10};
                    for (SplitBark bark : SplitBark.values()) {
                        player.getPacketDispatch().sendItemZoomOnInterface(bark.itemId, 170, 306, count);
                        player.getPacketDispatch().sendString(StringUtils.formatDisplayName(bark.name()), 306, indexes[bark.ordinal()]);
                        count++;
                    }
                    stage = 32;
                    break;
                case 32:
                    final SplitBark bark = SplitBark.forButton(buttonId);
                    if (bark == null) {
                        end();
                        return true;
                    }

                    if (!player.getInventory().contains(BARK.getId(), bark.getAmt())) {
                        String name = bark == SplitBark.HELM ? "a splitbark helm" : bark == SplitBark.BODY ? "a splitbark body" : bark == SplitBark.BOOTS ? "splitbark boots" : bark == SplitBark.GAUNTLETS ? "splitbark gauntlets" : "splitbark legs";
                        npc("You need " + bark.getAmt() + " pieces of bark for " + name + ".");
                        return true;
                    }
                    final int amount = getAmt(buttonId);
                    if (amount == -1) {// rscript.
                        sendInputDialogue(player, true, "Enter the amount:", (value) -> {
                            make(bark, (int) value);
                            return Unit.INSTANCE;
                        });
                        return true;
                    }
                    make(bark, amount);
                    break;
                case 33:
                    end();
                    break;
                case 20:
                    npc("Split-bark armour is special armour for mages, it's much", "more resistant to physical attacks than normal robes.", "It's actually very easy for me to make, but I've been", "having trouble getting hold of the pieces.");
                    stage = 14;
                    break;
                case 10:
                    npc("I've been studying the practice of making split-bark", "armour.");
                    stage = 11;
                    break;
                case 11:
                    options("Split-bark armour, what's that?", "Can you make me some?");
                    stage = 12;
                    break;
                case 12:
                    switch (buttonId) {
                        case 1:
                            player("Split-bark armour, what's that?");
                            stage = 13;
                            break;
                        case 2:
                            player("Can you make me some?");
                            stage = 9000;
                            break;
                    }
                    break;
                case 13:
                    npc("Split-bark armour is special armour for mages, it's much", "more resistant to physical attacks than normal robes.", "It's actually very easy for me to make, but I've been", "having trouble getting hold of the pieces.");
                    stage = 14;
                    break;
                case 14:
                    options("Well good luck with that.", "Can you make me some?");
                    stage = 15;
                    break;
                case 15:
                    switch (buttonId) {
                        case 1:
                            player("Well good luck with that.");
                            stage = 16;
                            break;
                        case 2:
                            player("Can you make me some?");
                            stage = 9000;
                            break;
                    }
                    break;
                case 16:
                    end();
                    break;
                case 9000:
                    npc("I need bark from a hollow tree, and some fine cloth.", "Unfortunately both these items can be found in", "Morytania, especially the cloth which is found in the", "tombs of shades.");
                    stage = 9001;
                    break;
                case 9001:
                    npc("Of course I'd happily sell you some at a discounted", "price if you bring me those items.");
                    stage = 9002;
                    break;
                case 9002:
                    options("Ok, guess I'll go looking then!", "Ok, how much do I need?");
                    stage = 9003;
                    break;
                case 9003:
                    switch (buttonId) {
                        case 1:
                            player("Ok, guess I'll go looking then!");
                            stage = 9004;
                            break;
                        case 2:
                            player("Ok, how much do I need?");
                            stage = 9005;
                            break;
                    }
                    break;
                case 9004:
                    end();
                    break;
                case 9005:
                    npc("1 need 1 piece of each for either gloves or boots,", "2 pieces of each for a hat,", "3 pieces of each for leggings,", "and 4 pieces of each for a top.");
                    stage = 9006;
                    break;
                case 9006:
                    npc("I'll charge you 1,000 coins for either gloves or boots,", "6,000 coins for a hat", "32,000 coins for leggings,", "and 37,000 for a top.");
                    stage = 9007;
                    break;
                case 9007:
                    player("Ok, guess I'll go looking then!");
                    stage = 9008;
                    break;
                case 9008:
                    end();
                    break;
                case 40:
                    end();
                    break;
            }
            return true;
        }

        @Override
        public int[] getIds() {
            return new int[]{1263};
        }

        /**
         * Method used to make split bark.
         *
         * @param bark   the bark.
         * @param amount the amount.
         */
        public final void make(final SplitBark bark, int amount) {
            final int barkAmt = player.getInventory().getAmount(BARK);
            if (barkAmt < amount * bark.getAmt()) {
                npc("You don't have enough bark to make that many.");
                stage = 33;
                return;
            }
            if (amount < 1) {
                end();
                return;
            }
            if (player.getInventory().freeSlots() < amount) {
                player("Sorry, I don't seem to have enough inventory space.");
                stage = 33;
                return;
            }
            if (!player.getInventory().contains(995, bark.getCost() * amount)) {
                player("Sorry, I don't seem to have enough coins.");
                stage = 33;
                return;
            }
            final Item money = new Item(995, bark.getCost() * amount);
            final Item barkRemove = new Item(BARK.getId(), amount * bark.getAmt());
            if (player.getInventory().remove(money) && player.getInventory().remove(barkRemove)) {
                player.getInventory().add(new Item(bark.getItemId(), amount));
                npc("There you go, enjoy your new armour!");
                return;
            }
            end();
        }

        /**
         * Method used to get the amount to make.
         *
         * @param buttonId the button id.
         * @return the amt.
         */
        public final int getAmt(final int buttonId) {
            int amount = -1;
            switch (buttonId) {
                case 10:
                case 14:
                case 18:
                case 22:
                case 26:
                    amount = 1;
                    break;
                case 9:
                case 13:
                case 17:
                case 21:
                case 25:
                    amount = 5;
                    break;
                case 8:
                case 12:
                case 16:
                case 20:
                case 24:
                    amount = 10;
                    break;
                case 7:
                case 11:
                case 15:
                case 19:
                case 23:
                    amount = -1;
                    break;
            }
            return amount;
        }

        /**
         * Represents the split bark item info.
         *
         * @author 'Vexia
         * @date 21/11/2013
         */
        public enum SplitBark {
            HELM(3385, 6000, 2, 9), BODY(3387, 37000, 4, 13), LEGS(3389, 32000, 3, 17), GAUNTLETS(3391, 1000, 1, 21), BOOTS(3393, 1000, 1, 25);

            /**
             * Constructs a new {@code WizardDialogue.java} {@code Object}.
             *
             * @param itemId   the item id.
             * @param cost     the cost.
             * @param buttonId the button id.
             */
            SplitBark(final int itemId, final int cost, final int amt, final int buttonId) {
                this.itemId = itemId;
                this.cost = cost;
                this.amt = amt;
                this.buttonId = buttonId;
            }

            /**
             * Represents the item to give.
             */
            private final int itemId;

            /**
             * Represents the cost of the item.
             */
            private final int cost;

            /**
             * Represents the needed amt.
             */
            private final int amt;

            /**
             * Represents the button id.
             */
            private final int buttonId;

            /**
             * Gets the itemId.
             *
             * @return The itemId.
             */
            public int getItemId() {
                return itemId;
            }

            /**
             * Gets the cost.
             *
             * @return The cost.
             */
            public int getCost() {
                return cost;
            }

            /**
             * Gets the buttonId.
             *
             * @return The buttonId.
             */
            public int getButtonId() {
                return buttonId;
            }

            /**
             * Method used to get the splitbark armour.
             *
             * @return the split bark.
             */
            public static SplitBark forButton(final int buttonId) {
                SplitBark bark = null;
                if (buttonId >= 7 && buttonId <= 10) {
                    bark = HELM;
                }
                if (buttonId >= 11 && buttonId <= 14) {
                    bark = BODY;
                }
                if (buttonId >= 15 && buttonId <= 18) {
                    bark = LEGS;
                }
                if (buttonId >= 19 && buttonId <= 22) {
                    bark = GAUNTLETS;
                }
                if (buttonId >= 23 && buttonId <= 26) {
                    bark = BOOTS;
                }
                if (buttonId == 26) {
                    bark = HELM;
                }
                return bark;
            }

            /**
             * Gets the amt.
             *
             * @return The amt.
             */
            public int getAmt() {
                return amt;
            }
        }
    }

    /**
     * Handles the SedridorDialogue dialogue.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public final static class SedridorDialogue extends DialoguePlugin {

        /**
         * Represents the talisman item.
         */
        private static final Item TALISMAN = new Item(1438);

        /**
         * Represents the package item.
         */
        private static final Item PACKAGE = new Item(290);

        /**
         * Represents the notes item.
         */
        private static final Item NOTES = new Item(291);

        /**
         * Represents the graphic to use.
         */
        private static final Graphics GRAPHIC = new Graphics(6);

        /**
         * Constructs a new {@code SedridorDialogue} {@code Object}.
         */
        public SedridorDialogue() {
            /**
             * empty.
             */
        }

        /**
         * Constructs a new {@code SedridorDialogue} {@code Object}.
         *
         * @param player the player.
         */
        public SedridorDialogue(Player player) {
            super(player);
        }

        @Override
        public DialoguePlugin newInstance(Player player) {
            return new SedridorDialogue(player);
        }

        @Override
        public boolean open(Object... args) {
            npc = (NPC) args[0];
            npc("Welcome adventurer, to the world renowned", "Wizards' Tower. How may I help you?");
            stage = 0;
            return true;
        }

        @Override
        public boolean handle(int interfaceId, int buttonId) {
            final Quest quest = player.getQuestRepository().getQuest(Quests.RUNE_MYSTERIES);
            switch (stage) {
                case 0:
                    if (quest.getStage(player) == 100) {
                        player("Hello there.");
                        stage = 400;
                        return true;
                    }
                    if (quest.getStage(player) == 10) {
                        options("Nothing thanks, I'm just looking around.", "What are you doing down here?", "I'm looking for the head wizard.");
                        stage = 1000;
                        return true;
                    }
                    if (quest.getStage(player) == 20) {
                        npc("Wow! This is... incredible!");
                        stage = 52;
                        return true;
                    }
                    if (quest.getStage(player) == 30) {
                        npc("Ah, " + player.getUsername() + ". How goes your quest? Have you", "delivered the research notes to my friend Aubury yet?");
                        stage = 800;
                        return true;
                    }
                    if (quest.getStage(player) == 50) {
                        npc("Ah, " + player.getUsername() + ". How goes your quest? Have you", "delivered the research notes to my friend Aubury yet?");
                        stage = 900;
                        return true;
                    }
                    options("Nothing thanks, I'm just looking around.", "What are you doing down here?");
                    stage = 1;
                    break;
                case 400:
                    npc("Hello again " + player.getUsername() + ". What can I do for you?");
                    stage = 401;
                    break;
                case 401:
                    options("Nothing thanks, I'm just looking around.", "Can you teleport me to the Rune Essence?");
                    stage = 402;
                    break;
                case 402:
                    switch (buttonId) {
                        case 1:
                            player("Nothing thanks, I'm just looking around.");
                            stage = 403;
                            break;
                        case 2:
                            player("Can you teleport me to the Rune Essence?");
                            stage = 405;
                            break;
                    }
                    break;
                case 403:
                    npc("Well, take care adventurer. You stand on the", "ruins of the old destroyed Wizards' Tower.", "Strange and powerful magicks lurk here,");
                    stage = 404;
                    break;
                case 404:
                    end();
                    break;
                case 405:
                    end();
                    EssenceTeleport.teleport(npc, player);
                    player.getAchievementDiaryManager().finishTask(player, DiaryType.LUMBRIDGE, 1, 14);
                    break;
                case 900:
                    player("Yes, I have. He gave me some research notes", "to pass on to you.");
                    stage = 901;
                    break;
                case 901:
                    npc("May I have his notes then?");
                    stage = 902;
                    break;
                case 902:
                    if (!player.getInventory().containsItem(NOTES)) {
                        player("Uh... I kind of... lost them...");
                        stage = 903;
                        break;
                    }
                    player("Sure. I have them here.");
                    stage = 905;
                    break;
                case 905:
                    npc("Well, before you hand them over to me, as you", "have been nothing but truthful with me to this point,", "and I admire that in an adventurer, I will let you", "into the secret of our research.");
                    stage = 906;
                    break;
                case 906:
                    npc("Now as you may or may not know, many", "centuries ago, the wizards at this Tower", "learnt the secret of creating Rune Stones, which", "allowed us to cast Magic very easily.");
                    stage = 907;
                    break;
                case 907:
                    npc("When this Tower was burnt down the secret of", "creating runes was lost to us for all time... except it", "wasn't. Some months ago, while searching these ruins", "for information from the old days,");
                    stage = 908;
                    break;
                case 908:
                    npc("I came upon a scroll, almost destroyed, that detailed a", "magical rock deep in the icefields of the North, closed", "off from access by anything other than magical means.");
                    stage = 909;
                    break;
                case 909:
                    npc("This rock was called the 'Rune Essence' by the", "magicians who studied its power. Apparently, by simply", "breaking a chunk from it, a Rune Stone could be", "fashioned very quickly and easily at certain");
                    stage = 910;
                    break;
                case 910:
                    npc("elemental altars that were scattered across the land", "back then. Now, this is an interesting little piece of", "history, but not much use to us as modern wizards", "without access to the Rune Essence,");
                    stage = 911;
                    break;
                case 911:
                    npc("or these elemental altars. This is where you and", "Aubury come into this story. A few weeks back,", "Aubury discovered in a standard delivery of runes", "to his store, a parchment detailing a");
                    stage = 912;
                    break;
                case 912:
                    npc("teleportation spell that he had never come across", "before. To his shock, when cast it took him to a", "strange rock he had never encountered before...", "yet that he felt strangely famliar...");
                    stage = 913;
                    break;
                case 913:
                    npc("As I'm sure you have now guessed, he had discovered a", "portal leading to the mythical Rune Essence. As soon as", "he told me of this spell, I saw the importance of his find,");
                    stage = 914;
                    break;
                case 914:
                    npc("for it we could but find the elemental altars spoken", "of in the ancient texts, we would once more be able", "to create runes as our ancestors had done! It would", "be the saviour of the wizards' art!");
                    stage = 915;
                    break;
                case 915:
                    player("I'm still not sure how I fit into", "this little story of yours...");
                    stage = 916;
                    break;
                case 916:
                    npc("You haven't guessed? This talisman you brought me...", "it is the key to the elemental altar of air! When", "you hold it next, it will direct you towards");
                    stage = 917;
                    break;
                case 917:
                    npc("the entrance to the long forgotten Air Altar! By", "bringing pieces of the Rune Essence to the Air Temple,", "you will be able to fashion your own Air Runes!");
                    stage = 918;
                    break;
                case 918:
                    npc("And this is not all! By finding other talismans similar", "to this one, you will eventually be able to craft every", "rune that is available on this world! Just");
                    stage = 919;
                    break;
                case 919:
                    npc("as our ancestors did! I cannot stress enough what a", "find this is! Now, due to the risks involved of letting", "this mighty power fall into the wrong hands");
                    stage = 920;
                    break;
                case 920:
                    npc("I will keep the teleport skill to the Rune Essence", "a closely guarded secret, shared only by myself", "and those Magic users around the world", "whom I trust enough to keep it.");
                    stage = 921;
                    break;
                case 921:
                    npc("This means that if any evil power should discover", "the talismans required to enter the elemental", "temples, we will be able to prevent their access", "to the Rune Essence and prevent");
                    stage = 922;
                    break;
                case 922:
                    npc("tragedy befalling this world. I know not where the", "temples are located, nor do I know where the talismans", "have been scattered to in this land, but I now");
                    stage = 923;
                    break;
                case 923:
                    npc("return your Air Talisman to you. Find the Air", "Temple, and you will be able to charge your Rune", "Essences to become Air Runes at will. Any time");
                    stage = 924;
                    break;
                case 924:
                    npc("you wish to visit the Rune Essence, speak to me", "or Aubury and we will open a portal to that", "mystical place for you to visit.");
                    stage = 925;
                    break;
                case 925:
                    player("So only you and Aubury know the teleport", "spell to the Rune Essence?");
                    stage = 926;
                    break;
                case 926:
                    npc("No... there are others... whom I will tell of your", "authorisation to visit that place. When you speak", "to them, they will know you, and grant you", "access to that place when asked.");
                    stage = 927;
                    break;
                case 927:
                    npc("Use the Air Talisman to locate the air temple,", "and use any further talismans you find to locate", "the other missing elemental temples.", "Now... my research notes please?");
                    stage = 928;
                    break;
                case 928:
                    sendDialogue("You hand the head wizard the research notes.", "He hands you back the Air Talisman.");
                    stage = 929;
                    break;
                case 929:
                    if (player.getInventory().remove(NOTES)) {
                        if (!player.getInventory().add(TALISMAN)) {
                            GroundItemManager.create(new GroundItem(TALISMAN, player.getLocation(), player));
                        }
                        quest.finish(player);
                        player.getQuestRepository().syncronizeTab(player);
                    }
                    end();
                    break;
                case 903:
                    npc("You did? You are extremely careless aren't you? I", "suggest you go and speak to Aubury once more, with", "luck he will have made copies of his research.");
                    stage = 904;
                    break;
                case 904:
                    end();
                    break;
                case 800:
                    player("Not yet...");
                    stage = 801;
                    break;
                case 801:
                    if (!player.getInventory().containsItem(PACKAGE) && !player.getBank().containsItem(PACKAGE)) {
                        player("...I lost the package you gave me.");
                        stage = 804;
                        return true;
                    }
                    npc("Well, please do so as soon as possible. Remember: to get", "to Varrock, head due North, through Draynor Village,", "around Draynor Manor, and then head East when");
                    stage = 802;
                    break;
                case 802:
                    npc("you get to the Barbarian village. The man you seek", "is named Aubury, and he owns the rune shop there.", "It is vital he receives this package.");
                    stage = 803;
                    break;
                case 803:
                    end();
                    break;
                case 804:
                    npc("You WHAT?");
                    stage = 805;
                    break;
                case 805:
                    npc("Tch, that was really very careless of you. Luckily as", "head wizard I have great powers, and will be able to", "teleport it back here without too much effort.");
                    stage = 806;
                    break;
                case 806:
                    close();
                    npc.graphics(GRAPHIC);
                    player.lock(3);
				GameWorld.getPulser().submit(new Pulse(2) {
                        @Override
                        public boolean pulse() {
                            npc("Ok, I have retrieved it. Luckily it doesn't appear to", "have been damaged. Now please take it to Aubury, ", "and try not to lose it again.");
                            stage = 807;
                            return true;
                        }
                    });
                    break;
                case 807:
                    if (!player.getInventory().add(PACKAGE)) {
                        GroundItemManager.create(new GroundItem(PACKAGE, player.getLocation(), player));
                    }
                    end();
                    break;
                case 1000:
                    switch (buttonId) {
                        case 1:
                            player("Nothing thanks, I'm just looking around.");
                            stage = 10;
                            break;
                        case 2:
                            player("What are you doing down here?");
                            stage = 20;
                            break;
                        case 3:
                            player("I'm looking for the head wizard.");
                            stage = 30;
                            break;
                    }
                    break;
                case 30:
                    npc("Oh, you are, are you?", "And just why would you be doing that?");
                    stage = 31;
                    break;
                case 31:
                    player("The Duke of Lumbridge sent me to find him. I have", "this weird talisman he found. He said the head wizard", "would be very interested in it.");
                    stage = 32;
                    break;
                case 32:// ...except I don't have it with me..."
                    npc("Did he now? HmmmMMMMMmmmmm.", "Well that IS interesting. Hand it over then adventurer,", "let me see what all the hubbub about it is.", "Just come amulet I'll wager.");
                    stage = 33;
                    break;
                case 33:
                    options("Ok, here you are.", "No, I'll only give it to the head wizard.");
                    stage = 34;
                    break;
                case 34:
                    switch (buttonId) {
                        case 1:
                            player("Ok, here you are.");
                            stage = 50;
                            break;
                        case 2:
                            player("No, I'll only give it to the head wizard.");
                            stage = 40;
                            break;
                    }
                    break;
                case 50:
                    if (!player.getInventory().containsItem(TALISMAN)) {
                        player("...except I don't have it with me...");
                        stage = 99;
                        break;
                    }
                    sendDialogue("You hand the Talisman to the wizard.");
                    stage = 51;
                    break;
                case 51:
                    if (player.getInventory().remove(TALISMAN)) {
                        quest.setStage(player, 20);
                        npc("Wow! This is... incredible!");
                        stage = 52;
                    }
                    break;
                case 52:
                    npc("Th-this talisman you brought me...! It is the last piece", "of the puzzle, I think! Finally! The legacy of our", "ancestors... it will return to us once more!");
                    stage = 53;
                    break;
                case 53:
                    npc("I need time to study this, " + player.getUsername() + ". Can you please", "do me this task while I study this talisman you have", "brought me? In the mighty town of Varrock, which");
                    stage = 54;
                    break;
                case 54:
                    npc("is located North East of here, there is a certain shop", "that sells magical runes. I have in this package all of the", "research I have done relating to the Rune Stones, and");
                    stage = 55;
                    break;
                case 55:
                    npc("require somebody to take them to the shopkeeper so that", "he may share my research and offer me his insights.", "Do this thing for me, and bring back what he gives you,");
                    stage = 56;
                    break;
                case 56:
                    npc("and if my suspicions are correct, I will let you into the", "knowledge of one of the greatest secrets this world has", "ever known! A secret so powerful that it destroyed the");
                    stage = 57;
                    break;
                case 57:
                    npc("original Wizards' Tower all of those centuries", "ago! My research, combined with this mysterious", "talisman... I cannot believe the answer", "the mysteries is so close now!");
                    stage = 58;
                    break;
                case 58:
                    npc("Do this thing for me " + player.getUsername() + ". Be rewarded in a", "way you can never imagine.");
                    stage = 59;
                    break;
                case 59:
                    options("Yes, certainly.", "No, I'm busy.");
                    stage = 60;
                    break;
                case 60:
                    switch (buttonId) {
                        case 1:
                            player("Yes, certainly.");
                            stage = 70;
                            break;
                        case 2:
                            player("No, I'm busy.");
                            stage = 61;
                            break;
                    }
                    break;
                case 70:
                    npc("Take this package, and head directly North", "from here, through Draynor village, until you reach", "the Barbarian Village. Then head East from there", "until you reach Varrock.");
                    stage = 71;
                    break;
                case 71:
                    npc("Once in Varrock, take this package to the owner of the", "rune shop. His name is Aubury. You may find it", "helpful to ask one of Varrock's citizens for directions,");
                    stage = 72;
                    break;
                case 72:
                    npc("as Varrock can be a confusing place for the first time", "visitor. He will give you a special item - bring it back to", "me, and I shall show you the mystery of the runes...");
                    stage = 73;
                    break;
                case 73:
                    sendDialogue("The head wizard gives you a package.");
                    stage = 74;
                    break;
                case 74:
                    quest.setStage(player, 30);
                    npc("Best of luck with your quest, " + player.getUsername() + ".");
                    if (!player.getInventory().add(PACKAGE)) {
                        GroundItemManager.create(new GroundItem(PACKAGE, player.getLocation(), player));
                    }
                    stage = 75;
                    break;
                case 75:
                    end();
                    break;
                case 61:
                    npc("As you wish adventurer. I will continue to study this", "talisman you have brought me. Return here if you find", "yourself with some spare time to help me.");
                    stage = 62;
                    break;
                case 62:
                    end();
                    break;
                case 99:
                    end();
                    break;
                case 40:
                    npc("HA HA HA HA HA! I can tell you are new to this", "land, for I AM the head wizard! Hand it over and", "let me have a proper look at it, hmmm?");
                    stage = 41;
                    break;
                case 41:
                    options("Ok, here you are.", "No, I'll only give it to the head wizard.");
                    stage = 34;
                    break;
                case 1:
                    switch (buttonId) {
                        case 1:
                            player("Nothing thanks, I'm just looking around.");
                            stage = 10;
                            break;
                        case 2:
                            player("What are you doing down here?");
                            stage = 20;
                            break;
                    }
                    break;
                case 10:
                    npc("Well, take care adventurer. You stand on the", "ruins of the destroyed Wizards' Tower.", "Strange and powerful magicks lurk here.");
                    stage = 11;
                    break;
                case 11:
                    end();
                    break;
                case 20:
                    npc("That is indeed a good question. Here in the cellar", "of the Wizards' Tower you find the remains of", "the old Wizards' Tower, destroyed by fire");
                    stage = 21;
                    break;
                case 21:
                    npc("many years past by the treachery of the Zamorakians.", "Many mytseries were lost, which we try to find once", "more. By building this tower on the remains of the old,");
                    stage = 22;
                    break;
                case 22:
                    npc("we sought to show the world of our dedication to", "learning the mysteries of Magic. I am here searching", "through these fragments for knowledge from", "the artefacts from our past.");
                    stage = 23;
                    break;
                case 23:
                    player("And have you found anything useful?");
                    stage = 24;
                    break;
                case 24:
                    npc("Aaah... that would be telling adventurer. Anything I", "have found I cannot speak freely of, for fear the", "treachery of the past might be repeated.");
                    stage = 25;
                    break;
                case 25:
                    player("Ok, well I'll leave you to it.");
                    stage = 26;
                    break;
                case 26:
                    npc("Perhaphs I will see you later " + player.getUsername() + ".");
                    stage = 27;
                    break;
                case 27:
                    player("How did you know my name???");
                    stage = 28;
                    break;
                case 28:
                    npc("Well, I AM the head wizard here...");
                    stage = 29;
                    break;
                case 29:
                    end();
                    break;
            }
            return true;
        }

        @Override
        public int[] getIds() {
            return new int[]{300};
        }
    }

    /**
     * Represents the dialogue plugin used for aubury.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public static final class AuburyDialoguePlugin extends DialoguePlugin {

        /**
         * Represents the package item.
         */
        private static final Item PACKAGE = new Item(290);

        /**
         * Represents the package item.
         */
        private static final Item NOTES = new Item(291);

        /**
         * The NPC ids that use this dialogue plugin.
         */
        private static final int[] NPC_IDS = {553};

        /**
         * Constructs a new {@code AuburyDialoguePlugin} {@code Object}.
         */
        public AuburyDialoguePlugin() {
            /*
             * empty.
             */
        }

        public AuburyDialoguePlugin(Player player) {
            super(player);
        }

        @Override
        public DialoguePlugin newInstance(Player player) {
            return new AuburyDialoguePlugin(player);
        }

        @Override
        public boolean open(Object... args) {
            npc = (NPC) args[0];
            final Quest quest = player.getQuestRepository().getQuest(Quests.RUNE_MYSTERIES);
            if (quest.getStage(player) == 40) {
                npc("My gratitude to you adventurer for bringing me these", "research notes. I notice that you brought the head", "wizard a special talisman that was the key to our finally", "unlocking the puzzle.");
                stage = 900;
                return true;
            }
            if (Skillcape.isMaster(player, Skills.RUNECRAFTING)) {
                options("Can I buy a Skillcape of Runecrafting?", "Something else");
                stage = 450;
            } else {
                npc("Do you want to buy some runes?");
            }
            return true;
        }

        @Override
        public boolean handle(int interfaceId, int buttonId) {
            final Quest quest = player.getQuestRepository().getQuest(Quests.RUNE_MYSTERIES);
            switch (stage) {
                case 0:
                    if (quest.getStage(player) == 30) {
                        options("Yes please!", "Oh, it's a rune shop. No thank you, then.", "I have been sent here with a package for you.");
                        stage = 800;
                        return true;
                    }
                    if (quest.getStage(player) == 40) {
                        npc("My gratitude to you adventurer for bringing me these", "research notes. I notice that you brought the head", "wizard a speical talisman that was the key to our finally", "unlocking the puzzle.");
                        stage = 900;
                        return true;
                    }
                    if (quest.getStage(player) == 50) {
                        npc("I suggest you take those research notes of mine back", "to the head wizard at the Wizards' Tower.");
                        stage = 950;
                        return true;
                    }
                    if (!player.getQuestRepository().isComplete(Quests.RUNE_MYSTERIES)) {
                        options("Yes please!", "Oh, it's a rune shop. No thank you, then.");
                        stage = 100;
                    } else {
                        options("Yes, please.", "No thanks.", "Can you teleport me to the rune essence?");
                        stage = 1;
                    }
                    break;
                case 900:// quest
                    npc("Combined with the information I had already collated", "regarding the Rune Essence, I think we have finally", "unlocked the power to");
                    stage = 901;
                    break;
                case 901:
                    npc("...no. I am getting ahead of myself. Please take this", "summary of my research back to the head wizard at", "the Wizards' Tower. I trust his judgement on whether", "to let you in on our little secret or not.");
                    stage = 902;
                    break;
                case 902:
                    quest.setStage(player, 50);
                    if (!player.getInventory().add(NOTES)) {
                        GroundItemManager.create(new GroundItem(NOTES, player.getLocation(), player));
                    }
                    sendDialogue("Aubury gives you his research notes.");
                    stage = 903;
                    break;
                case 903:
                    end();
                    break;
                case 950:
                    if (!player.getBank().containsItem(NOTES) && !player.getInventory().containsItem(NOTES)) {
                        player("I can't... I lost them...");
                        stage = 955;
                        return true;
                    }
                    player("Ok then, I will do that.");
                    stage = 951;
                    break;
                case 951:
                    npc("Unless you were talking to me because you wished to", "buy some runes?");
                    stage = 952;
                    break;
                case 952:
                    options("Yes please!", "Oh, it's a rune shop. No thank you, then.");
                    stage = 100;
                    break;
                case 955:
                    npc("Well, luckily I have duplicates. It's a good thing they", "are written in code, I would not want the wrong kind", "of person to get access to the information contained", "within.");
                    stage = 956;
                    break;
                case 956:
                    if (!player.getInventory().add(NOTES)) {
                        GroundItemManager.create(new GroundItem(NOTES, player.getLocation(), player));
                    }
                    sendDialogue("Aubury gives you his research notes.");
                    stage = 957;
                    break;
                case 957:
                    end();
                    break;
                case 800:// quest
                    switch (buttonId) {
                        case 1:
                            npc.openShop(player);
                            end();
                            break;
                        case 2:
                            player("Oh, it's a rune shop. No thank you, then.");
                            stage = 105;
                            break;
                        case 3:
                            player("I have been sent here with a package for you. It's from ", "the head wizard at the Wizards' Tower.");
                            stage = 801;
                            break;
                    }
                    break;
                case 801:
                    player("I have been sent here with a package for you. It's from ", "the head wizard at the Wizards' Tower.");
                    stage = 802;
                    break;
                case 802:
                    npc("Really? But... surely he can't  have..? Please, let me", "have it, it must be extremely important for him to have", "sent a stranger.");
                    stage = 803;
                    break;
                case 803:
                    if (!player.getInventory().containsItem(PACKAGE)) {
                        player("Uh... yeah... about that... I kind of don't have it with", "me...");
                        stage = 804;
                        return true;
                    }
                    sendDialogue("You hand Aubury the research package.");
                    stage = 807;
                    break;
                case 804:
                    npc("What kind of person tells me theyhave a delivery for", "me, but not with them? Honestly.");
                    stage = 805;
                    break;
                case 805:
                    npc("Come back when you do.");
                    stage = 806;
                    break;
                case 806:
                    end();
                    break;
                case 807:
                    if (player.getInventory().remove(PACKAGE)) {
                        quest.setStage(player, 40);
                        npc("This... this is incredible. Please, give me a few moments", "to quickly look over this, and then talk to me again.");
                        stage = 808;
                    }
                    break;
                case 808:
                    end();
                    break;
                case 1:
                    switch (interfaceId) {
                        case 230:
                            switch (buttonId) {
                                case 1:
                                    npc.openShop(player);
                                    end();
                                    break;
                                case 2:
                                    player("No thanks.");
                                    stage = 10;
                                    break;
                                case 3:
                                    player("Can you teleport me to the rune essence?");
                                    stage = 11;
                                    break;
                            }
                    }
                    break;
                case 100:
                    switch (buttonId) {
                        case 1:
                            npc.openShop(player);
                            end();
                            break;
                        case 2:
                            player("Oh, it's a rune shop. No thank you, then.");
                            stage = 105;
                            break;
                    }
                    break;
                case 105:
                    end();
                    break;
                case 106:
                    end();
                    break;
                case 10:
                    end();
                    break;
                case 11:
                    EssenceTeleport.teleport(npc, player);
                    end();
                    break;
                case 450:
                    switch (buttonId) {
                        case 1:
                            player("Can I buy a Skillcape of Runecrafting?");
                            stage = 2;
                            break;
                        case 2:
                            npc("Do you want to buy some runes?");
                            stage = 0;
                            break;
                    }
                    break;
                case 2:
                    npc("Certainly! Right when you give me 99000 coins.");
                    stage = 3;
                    break;
                case 3:
                    options("Okay, here you go.", "No");
                    stage = 4;
                    break;
                case 4:
                    switch (buttonId) {
                        case 1:
                            player("Okay, here you go.");
                            stage = 5;
                            break;
                        case 2:
                            end();
                            break;
                    }
                    break;
                case 5:
                    if (Skillcape.purchase(player, Skills.RUNECRAFTING)) {
                        npc("There you go! Enjoy.");
                    }
                    stage = 6;
                    break;
                case 6:
                    end();
                    break;
            }
            return true;
        }

        @Override
        public int[] getIds() {
            return NPC_IDS;
        }
    }

}
