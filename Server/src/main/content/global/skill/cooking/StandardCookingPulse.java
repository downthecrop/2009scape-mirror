package content.global.skill.cooking;

import content.global.skill.skillcapeperks.SkillcapePerks;
import core.game.event.ResourceProducedEvent;
import core.game.node.entity.impl.Animator;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.audio.Audio;
import core.game.node.entity.skill.Skills;
import core.game.node.item.GroundItemManager;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.system.task.Pulse;
import core.game.world.update.flag.context.Animation;
import core.tools.RandomFunction;
import org.rs09.consts.Items;
import org.rs09.consts.Sounds;

import static core.api.ContentAPIKt.playAudio;

public class StandardCookingPulse extends Pulse {
    //range animation
    private static final Animation RANGE_ANIMATION = new Animation(883, Animator.Priority.HIGH);

    //fire animation
    private static final Animation FIRE_ANIMATION = new Animation(897, Animator.Priority.HIGH);

    //Cooking sound
    public static final Audio SOUND = new Audio(2577, 1, 1);

    private static final int LUMBRIDGE_RANGE = 114;

    private final int initial;
    private final int product;
    private int amount;
    private final Scenery object;
    private final Player player;
    private double experience;
    private boolean burned = false;
    public CookableItems properties;

    public StandardCookingPulse(Player player, Scenery object, int initial, int product, int amount) {
        this.player = player;
        this.object = object;
        this.initial = initial;
        this.product = product;
        this.amount = amount;
    }

    @Override
    public void start() {
        properties = CookableItems.forId(initial);
        if (checkRequirements()) {
            super.start();
            cook(player, object, properties != null && burned, initial, product);
            amount--;
        }
    }

    @Override
    public boolean pulse() {
        if (amount < 1 || !checkRequirements()) {
            return true;
        }
        return reward();
    }

    public void animate() {
        player.animate(getAnimation(object));
    }

    public boolean checkRequirements() {
        this.experience = 0;
        if (properties != null) {
            // Handle Cook's Assistant range
            if (object.getId() == LUMBRIDGE_RANGE && !player.getQuestRepository().isComplete("Cook's Assistant")) {
                player.getPacketDispatch().sendMessage("You need to have completed the Cook's Assistant quest in order to use that range.");
                return false;
            }

            //check level
            if (player.getSkills().getLevel(Skills.COOKING) < properties.level) {
                player.getDialogueInterpreter().sendDialogue("You need a cooking level of " + properties.level + " to cook this.");
                return false;
            }

            this.experience = properties.experience;
            this.burned = isBurned(player, object, initial);
        }
        if (amount < 1) {
            return false;
        }

        return object.isActive();
    }

    public boolean reward() {
        if (getDelay() == 1) {
            int delay = object.getName().toLowerCase().contains("range") ? 5 : 4;
            if (SkillcapePerks.isActive(SkillcapePerks.HASTY_COOKING, player)) {
                delay -= 1;
            }
            setDelay(delay);
            return false;
        }

        if (cook(player, object, burned, initial, product)) {
            amount--;
        } else {
            return true;
        }
        return amount < 1;
    }

    public boolean isBurned(final Player player, final Scenery object, int food) {
        boolean hasGauntlets = player.getEquipment().containsItem(new Item(Items.COOKING_GAUNTLETS_775));
        int effectiveCookingLevel = player.getSkills().getLevel(Skills.COOKING);
        if (SkillcapePerks.isActive(SkillcapePerks.HASTY_COOKING, player)) {
            effectiveCookingLevel -= 5;
        }
        CookableItems item = CookableItems.forId(food);
        int low;
        int high;
        if (hasGauntlets && CookableItems.gauntletValues.containsKey(food)) {
            int[] successValues = CookableItems.gauntletValues.get(food);
            low = successValues[0];
            high = successValues[1];
        } else if (object.getId() == LUMBRIDGE_RANGE) {
            int[] successValues = CookableItems.lumbridgeRangeValues.getOrDefault(food, new int[]{item.lowRange, item.highRange});
            low = successValues[0];
            high = successValues[1];
        } else {
            boolean isFire = object.getName().toLowerCase().contains("fire");
            low = isFire ? item.low : item.lowRange;
            high = isFire ? item.high : item.highRange;
        }
        double host_ratio = RandomFunction.randomDouble(100.0);
        double client_ratio = RandomFunction.getSkillSuccessChance(low, high, effectiveCookingLevel);
        return host_ratio > client_ratio;
    }

    public boolean cook(final Player player, final Scenery object, final boolean burned, final int initial, final int product) {
        Item initialItem = new Item(initial);
        Item productItem = new Item(product);
        animate();

        //handle special cooking results (spits, cake, etc) that don't justify separate plugin
        switch (initial) {
            case Items.SKEWERED_CHOMPY_7230:
            case Items.SKEWERED_RABBIT_7224:
            case Items.SKEWERED_BIRD_MEAT_9984:
            case Items.SKEWERED_BEAST_9992:
            case Items.IRON_SPIT_7225:
                if (RandomFunction.random(15) == 5) {
                    player.getPacketDispatch().sendMessage("Your iron spit seems to have broken in the process.");
                } else {
                    if (!player.getInventory().add(new Item(Items.IRON_SPIT_7225))) {
                        GroundItemManager.create(new Item(Items.IRON_SPIT_7225), player.getLocation(), player);
                    }
                }
                break;
            case Items.UNCOOKED_CAKE_1889: //cake
                if (!player.getInventory().add(new Item(Items.CAKE_TIN_1887))) {
                    GroundItemManager.create(new Item(Items.CAKE_TIN_1887), player);
                }
                break;
        }
        if (player.getInventory().remove(initialItem)) {
            if (!burned) {
                player.getInventory().add(productItem);
                player.dispatch(new ResourceProducedEvent(productItem.getId(), 1, object, initialItem.getId()));
                player.getSkills().addExperience(Skills.COOKING, experience, true);
            } else {
                player.dispatch(new ResourceProducedEvent(CookableItems.getBurnt(initial).getId(), 1, object, initialItem.getId()));
                player.getInventory().add(CookableItems.getBurnt(initial));
            }
            player.getPacketDispatch().sendMessage(getMessage(initialItem, productItem, burned));
            playAudio(player, Sounds.FRY_2577);
            return true;
        }
        return false;
    }

    public String getMessage(Item food, Item product, boolean burned) {
        if (food.getId() == Items.RAW_OOMLIE_2337) {
            return "The meat is far too delicate to cook like this. Perhaps you should wrap something around it to protect it from the heat.";
        }
        if (product.getId() == Items.SODA_ASH_1781) {
            return "You burn the seaweed into soda ash.";
        }
        if (CookableItems.intentionalBurn(food.getId())) {
            return "You deliberately burn the perfectly good piece of meat.";
        }

        if (!burned && food.getName().startsWith("Raw")) {
            return "You manage to cook some " + food.getName().replace("Raw ", "") + ".";
        } else if (burned && food.getName().startsWith("Raw")) {
            return "You accidentally burn some " + food.getName().replace("Raw ", "") + ".";
        }

        if (!burned && food.getName().startsWith(("Uncooked"))) {
            return "You manage to cook some " + food.getName().replace("Uncooked ", "") + ".";
        } else if (burned && food.getName().startsWith(("Uncooked"))) {
            return "You accidentally burn some " + food.getName().replace("Uncooked ", "") + ".";
        }
        return null;
    }

    private Animation getAnimation(final Scenery object) {
        return !object.getName().equalsIgnoreCase("fire") ? RANGE_ANIMATION : FIRE_ANIMATION;
    }
}
