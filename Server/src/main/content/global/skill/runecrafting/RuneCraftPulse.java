package content.global.skill.runecrafting;

import content.global.handlers.item.equipment.fistofguthixgloves.FOGGlovesManager;
import core.ServerConstants;
import core.api.Container;
import core.game.container.impl.EquipmentContainer;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.diary.DiaryType;
import core.game.node.entity.skill.SkillPulse;
import core.game.node.entity.skill.Skills;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.tools.RandomFunction;

import static core.api.ContentAPIKt.*;
import static core.game.system.command.sets.StatAttributeKeysKt.STATS_BASE;
import static core.game.system.command.sets.StatAttributeKeysKt.STATS_RC;

import core.game.world.GameWorld;
import org.rs09.consts.Items;
import org.rs09.consts.Sounds;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A class used to craft runes.
 *
 * @author Vexia
 * @author Player Name
 */
public final class RuneCraftPulse extends SkillPulse<Item> {

    /**
     * Represent the rune essence item.
     */
    private static final Item RUNE_ESSENCE = new Item(1436);

    /**
     * Represents the pure essence item.
     */
    private static final Item PURE_ESSENCE = new Item(7936);

    /**
     * Represents the binding necklace item.
     */
    private static final Item BINDING_NECKLACE = new Item(5521);

    /**
     * Represents the animation used for this pulse.
     */
    private static final Animation ANIMATION = new Animation(791, Priority.HIGH);

    /**
     * Represents the graphics used for this pulse.
     */
    private static final Graphics GRAPHICS = new Graphics(186, 100);

    /**
     * Represents the altar.
     */
    private final Altar altar;

    /**
     * Represents the rune we're crafting.
     */
    private final Rune rune;

    /**
     * Represents the combination rune(if any)
     */
    private final CombinationRune combo;

    /**
     * Represents if it's a combination pulse.
     */
    private final boolean combination;

    /**
     * Represents the talisman to remove.
     */
    private Talisman talisman;

    /**
     * Constructs a new {@code RuneCraftPulse} {@code Object}.
     *
     * @param player the player.
     * @param node   the node.
     * @param altar  the altar.
     */
    public RuneCraftPulse(Player player, Item node, final Altar altar, final boolean combination, final CombinationRune combo) {
        super(player, node);
        this.altar = altar;
        this.rune = altar.getRune();
        this.combination = combination;
        this.combo = combo;
        this.resetAnimation = false;
    }

    @Override
    public boolean checkRequirements() {
        if (altar == Altar.ASTRAL) {
            if (!hasRequirement(player, "Lunar Diplomacy"))
                return false;
        }
        if (altar == Altar.DEATH) {
            if (!hasRequirement(player, "Mourning's End Part II"))
                return false;
        }
        if (altar == Altar.BLOOD) {
            if (!hasRequirement(player, "Legacy of Seergaze"))
                return false;
        }
        if (!altar.isOurania() && getDynLevel(player, Skills.RUNECRAFTING) < rune.getLevel()) {
            sendMessage(player, "You need a Runecrafting level of at least " + rune.getLevel() + " to craft this rune.");
            return false;
        }
        if (combination && amountInInventory(player, PURE_ESSENCE.getId()) == 0) {
            sendMessage(player, "You need pure essence to craft this rune.");
            return false;
        }
        if (!altar.isOurania() && !rune.isNormal() && amountInInventory(player, PURE_ESSENCE.getId()) == 0) {
            sendMessage(player, "You need pure essence to craft this rune.");
            return false;
        }
        if (!altar.isOurania() && rune.isNormal() && amountInInventory(player, PURE_ESSENCE.getId()) == 0 && amountInInventory(player, RUNE_ESSENCE.getId()) == 0) {
            sendMessage(player, "You need rune essence or pure essence in order to craft this rune.");
            return false;
        }
        if (altar.isOurania() && amountInInventory(player, PURE_ESSENCE.getId()) == 0) {
            sendMessage(player, "You need pure essence to craft this rune.");
            return false;
        }
        if (combination && getDynLevel(player, Skills.RUNECRAFTING) < combo.getLevel()) {
            sendMessage(player, "You need a Runecrafting level of at least " + combo.getLevel() + " to combine this rune.");
            return false;
        }
        if (node != null) {
            if (node.getName().contains("rune") && !hasSpellImbue()) {
                final Rune r = Rune.forItem(node);
                final Talisman t = Talisman.forName(r.name());
                if (amountInInventory(player, t.getTalisman().getId()) == 0) {
                    sendMessage(player, "You don't have the correct talisman to combine this rune.");
                    return false;
                }
                talisman = t;
            }
        }
        player.lock(4);
        return true;
    }

    @Override
    public void animate() {
        player.animate(ANIMATION);
        player.graphics(GRAPHICS);
        playAudio(player, Sounds.BIND_RUNES_2710);
    }

    @Override
    public boolean reward() {
        if (!combination) {
            craft();
        } else {
            combine();
        }
        return true;
    }

    private static final int[][] OuraniaTable = { //https://x.com/JagexAsh/status/1312893446395506688/photo/1
            /*level up to  9*/ {  2,   7,  15,  30,  60, 105, 165, 250, 400, 700,1300,2500,5000,10000},
            /*level up to 19*/ {  3,   9,  21,  45,  85, 145, 225, 400,1000,2200,4600,6700,8500,10000},
            /*level up to 29*/ {  8,  23,  55, 110, 220, 430, 850,1650,3250,4750,6150,7500,8800,10000},
            /*level up to 39*/ { 20,  60, 120, 250, 500,1000,2000,4000,5300,6500,7600,8500,9300,10000},
            /*level up to 49*/ { 40, 120, 240, 500,1000,2000,4000,5500,6500,7300,8050,8750,9400,10000},
            /*level up to 59*/ { 80, 250, 600,1300,2650,4150,5250,6250,7000,7700,8350,8950,9500,10000},
            /*level up to 69*/ {100, 300, 700,1500,3050,4450,5500,6450,7200,7900,8500,9050,9550,10000},
            /*level up to 79*/ {200, 700,1700,3500,5000,6200,7100,7800,8300,8700,9100,9400,9700,10000},
            /*level up to 89*/ {400,1000,2450,3900,5250,6300,7100,7800,8400,8900,9300,9600,9800,10000},
            /*level up to 98*/ {650,1650,3300,4750,6100,7100,7800,8400,8900,9300,9600,9800,9900,10000},
            /*level up to 99*/ {900,2200,3750,5200,6550,7500,8100,8600,9000,9300,9600,9800,9900,10000}
    };

    /**
     * Method used to craft runes.
     */
    private void craft() {
        final Item item = getEssenceItem();
        int amount = player.getInventory().getAmount(item);
        if (altar.isOurania()) {
            if (removeItem(player, item, Container.INVENTORY)) {
                sendMessage(player, "You bind the temple's power into runes.");
                player.incrementAttribute("/save:" + STATS_BASE + ":" + STATS_RC, amount);

                int[] OuraniaValues;
                if (getDynLevel(player, Skills.RUNECRAFTING) == 99) {
                    OuraniaValues = OuraniaTable[10];
                } else {
                    int index = getDynLevel(player, Skills.RUNECRAFTING) / 10;
                    OuraniaValues = OuraniaTable[index];
                }
                for (int i = 0; i < amount; i++) {
                    int roll = RandomFunction.random(10000);
                    Rune rune = null;
                    for (int j = 0; j < 14; j++) {
                        if (roll < OuraniaValues[j]) {
                            rune = Rune.values()[13 - j];
                            break;
                        }
                    }
                    rewardXP(player, Skills.RUNECRAFTING, rune.getExperience() * 2);
                    addItemOrDrop(player, rune.getRune().getId(), 1);
                }
            }
        } else {
            int total = 0;
            for(int j = 0; j < amount; j++) {
                // since getMultiplier is stochastic, roll `amount` independent copies
                total += getMultiplier();
            }

            if (removeItem(player, item, Container.INVENTORY)) {
                sendMessage(player, "You bind the temple's power into " + (combination ? combo.getRune().getName().toLowerCase() : rune.getRune().getName().toLowerCase()) + "s.");
                addItemOrDrop(player, rune.getRune().getId(), total);
                player.incrementAttribute("/save:" + STATS_BASE + ":" + STATS_RC, amount);

                // Fist of guthix gloves
                double xp = rune.getExperience() * amount;
                if ((altar == Altar.AIR && inEquipment(player, Items.AIR_RUNECRAFTING_GLOVES_12863, 1))
                        || (altar == Altar.WATER && inEquipment(player, Items.WATER_RUNECRAFTING_GLOVES_12864, 1))
                        || (altar == Altar.EARTH && inEquipment(player, Items.EARTH_RUNECRAFTING_GLOVES_12865, 1))) {
                    xp += xp * FOGGlovesManager.updateCharges(player, amount) / amount;
                }
                rewardXP(player, Skills.RUNECRAFTING, xp);

                // Achievement Diary handling
                // Craft some nature runes
                if (altar == Altar.NATURE) {
                    player.getAchievementDiaryManager().finishTask(player, DiaryType.KARAMJA, 2, 3);
                }
                // Craft 196 or more air runes simultaneously
                if (altar == Altar.AIR && total >= 196) {
                    player.getAchievementDiaryManager().finishTask(player, DiaryType.FALADOR, 2, 2);
                }
                // Craft a water rune at the Water Altar
                if (altar == Altar.WATER && rune == Rune.WATER) {
                    player.getAchievementDiaryManager().finishTask(player, DiaryType.LUMBRIDGE, 1, 11);
                }

            }
        }
    }

    /**
     * Method used to combine runes.
     */
    private void combine() {
        final Item remove = node.getName().contains("talisman") ? node : talisman != null ? talisman.getTalisman() : Talisman.forName(Rune.forItem(node).name()).getTalisman();
        boolean imbued = hasSpellImbue();
        if (!imbued ? removeItem(player, remove, Container.INVENTORY) : imbued) {
            int amount = 0;
            int essenceAmt = player.getInventory().getAmount(PURE_ESSENCE);
            final Item rune = node.getName().contains("rune") ? Rune.forItem(node).getRune() : Rune.forName(Talisman.forItem(node).name()).getRune();
            int runeAmt = player.getInventory().getAmount(rune);
            amount = Math.min(essenceAmt, runeAmt);
            if (removeItem(player, new Item(PURE_ESSENCE.getId(), amount), Container.INVENTORY) && removeItem(player, new Item(rune.getId(), amount), Container.INVENTORY)) {
                for (int i = 0; i < amount; i++) {
                    if (RandomFunction.random(1, 3) == 1 || hasBindingNecklace()) {
                        addItemOrDrop(player, combo.getRune().getId(), 1);
                        rewardXP(player, Skills.RUNECRAFTING, combo.getExperience());
                    }
                }
                if (hasBindingNecklace()) {
                    player.getEquipment().get(EquipmentContainer.SLOT_AMULET).setCharge(player.getEquipment().get(EquipmentContainer.SLOT_AMULET).getCharge() - 1);
                    if (1000 - player.getEquipment().get(EquipmentContainer.SLOT_AMULET).getCharge() > 14) {
                        if (player.getEquipment().remove(BINDING_NECKLACE, true)) {
                            sendMessage(player, "Your binding necklace crumbles into dust.");
                        }
                    }
                }
            }
        }
    }

    /**
     * Checks if the player has the spell imbue.
     *
     * @return {@code True} if so.
     */
    private boolean hasSpellImbue() {
        return player.getAttribute("spell:imbue", 0) > GameWorld.getTicks();
    }

    /**
     * Gets the rune essence item.
     *
     * @return the rune essence item.
     */
    private Item getEssenceItem() {
        if (altar.isOurania() && amountInInventory(player, PURE_ESSENCE.getId()) > 0) {
            return new Item(PURE_ESSENCE.getId(), amountInInventory(player, PURE_ESSENCE.getId()));
        }
        if (!rune.isNormal() && amountInInventory(player, PURE_ESSENCE.getId()) > 0) {
            return new Item(PURE_ESSENCE.getId(), amountInInventory(player, PURE_ESSENCE.getId()));
        }
        if (rune.isNormal() && amountInInventory(player, RUNE_ESSENCE.getId()) > 0) {
            return new Item(RUNE_ESSENCE.getId(), amountInInventory(player, RUNE_ESSENCE.getId()));
        }
        return new Item(PURE_ESSENCE.getId(), amountInInventory(player, PURE_ESSENCE.getId()));
    }

    /**
     * Gets the multiplied amount of runes to make.
     *
     * @return the amount.
     */
    public int getMultiplier() {
        if (altar.isOurania()) {
            return 1;
        }
        int rcLevel = getDynLevel(player, Skills.RUNECRAFTING);
        int runecraftingFormulaRevision = ServerConstants.RUNECRAFTING_FORMULA_REVISION;
        boolean lumbridgeDiary = player.getAchievementDiaryManager().getDiary(DiaryType.LUMBRIDGE).isComplete(1);
        return RuneCraftPulse.getMultiplier(rcLevel, rune, runecraftingFormulaRevision, lumbridgeDiary);
    }

    public static int getMultiplier(int rcLevel, Rune rune, int runecraftingFormulaRevision, boolean lumbridgeDiary) {
        int[] multipleLevels = rune.getMultiple();
        int i = 0;
        for (int level : multipleLevels) {
            if (rcLevel >= level) {
                i++;
            }
        }

        if (multipleLevels.length > i && runecraftingFormulaRevision >= 573) {
            int a = Math.max(multipleLevels[i-1], rune.getLevel());
            int b = multipleLevels[i];
            if(b <= 99 || runecraftingFormulaRevision >= 581) {
                double chance = ((double)rcLevel - (double)a) / ((double)b - (double)a);
                if(RandomFunction.random(0.0, 1.0) < chance) {
                    i += 1;
                }
            }
        }

        if (lumbridgeDiary
                && new ArrayList<>(Arrays.asList(Rune.AIR, Rune.WATER, Rune.FIRE, Rune.EARTH)).contains(rune)
                && RandomFunction.getRandom(10) == 0) { //approximately 10% chance
            i += 1;
        }

        return i != 0 ? i : 1;
    }

    /**
     * Method used to check if the player has a binding necklace.
     *
     * @return <code>True</code> if so.
     */
    public boolean hasBindingNecklace() {
        return player.getEquipment().containsItem(BINDING_NECKLACE);
    }

    /**
     * Gets the altar.
     *
     * @return The altar.
     */
    public Altar getAltar() {
        return altar;
    }
}
