package core.game.node.entity.skill.cooking;

import api.events.ResourceProducedEvent;
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
import rs09.game.node.entity.skill.skillcapeperks.SkillcapePerks;

public class StandardCookingPulse extends Pulse {
    //range animation
    private static final Animation RANGE_ANIMATION = new Animation(883, Animator.Priority.HIGH);

    //fire animation
    private static final Animation FIRE_ANIMATION = new Animation(897, Animator.Priority.HIGH);

    //Cooking sound
    public static final Audio SOUND = new Audio(2577, 1, 1);

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
            cook(player, object, CookableItems.cookingMap.get(initial) != null && isBurned(player, object, initial), initial, product);
            amount--;
        }
    }

    @Override
    public boolean pulse() {
        if (!checkRequirements()) {
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
            if (object.getId() == 114 && !player.getQuestRepository().isComplete("Cook's Assistant")) {
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
            int delay = object.getName().toLowerCase().equals("range") ? 5 : 4;
            if(SkillcapePerks.isActive(SkillcapePerks.HASTY_COOKING, player)) {
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
        double burn_stop = (double) CookableItems.getBurnLevel(food);
        int effectiveCookingLevel = player.getSkills().getLevel(Skills.COOKING);
        if(SkillcapePerks.isActive(SkillcapePerks.HASTY_COOKING, player)) {
            effectiveCookingLevel -= 5;
        }
        int gauntlets_boost = 0;
        CookableItems item = CookableItems.forId(food);
        if (hasGauntlets && (food == Items.RAW_SWORDFISH_371 || food == Items.RAW_LOBSTER_377 || food == Items.RAW_MONKFISH_7944 || food == Items.RAW_SHARK_383)) {
            burn_stop -= 6;
            gauntlets_boost += 6;
        }
        if (effectiveCookingLevel >= burn_stop) {
            return false;
        }
        int cook_level = effectiveCookingLevel + gauntlets_boost;
        double host_ratio = RandomFunction.randomDouble(100.0);
        double low = item.low + (object.getName().contains("fire") ? 0 : (0.1 * item.low));
        double high = item.high + (object.getName().contains("fire") ? 0 : (0.1 * item.high));
        double client_ratio = RandomFunction.getSkillSuccessChance(low,high,cook_level);
        return host_ratio > client_ratio;
    }

    public boolean cook(final Player player, final Scenery object, final boolean burned, final int initial, final int product) {
        Item initialItem = new Item(initial);
        Item productItem = new Item(product);
        animate();

        //handle special cooking results (spits, cake, etc) that don't justify separate plugin
        switch (initial) {
            case Items.RAW_BEAST_MEAT_9986:
            case Items.RAW_CHOMPY_2876:
            case Items.RAW_JUBBLY_7566:
            case Items.SKEWERED_BIRD_MEAT_9984:
            case Items.RAW_RABBIT_3226: // Iron spits
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
            player.getAudioManager().send(SOUND);
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
        if (!burned) {
            return "You manage to cook some " + food.getName().replace("Raw ", "");
        } else {
            return "You accidentally burn some " + food.getName().replace("Raw ", "");
        }
    }

    private Animation getAnimation(final Scenery object) {
        return !object.getName().equalsIgnoreCase("fire") ? RANGE_ANIMATION : FIRE_ANIMATION;
    }
}
