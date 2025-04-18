package content.global.skill.herblore;

import core.game.node.entity.skill.SkillPulse;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Animation;
import core.tools.StringUtils;
import content.global.skill.skillcapeperks.*;
import content.data.consumables.Consumables;
import core.tools.RandomFunction;
import org.rs09.consts.Sounds;

import static core.api.ContentAPIKt.playAudio;
import content.data.Quests;


/**
 * Represents the skill pulse used to handle the creating of potions.
 * @author 'Vexia
 */
public final class HerblorePulse extends SkillPulse<Item> {

	/**
	 * Represents the vial of water item.
	 */
	public static final Item VIAL_OF_WATER = new Item(227);

	/**
	 * Represents the coonut milk item.
	 */
	public static final Item COCONUT_MILK = new Item(5935);

	/**
	 * Represents the animation to use when making a potion.
	 */
	private static final Animation ANIMATION = new Animation(363);

	/**
	 * Represents the generic potion.
	 */
	private final GenericPotion potion;

	/**
	 * Represents the amount to make.
	 */
	private int amount;

	/**
	 * Represents the initial amount to make.
	 */
	private int initialAmount;

	/**
	 * Represents the cycles.
	 */
	private int cycles;

	/**
	 * Constructs a new {@code HerblorePulse} {@code Object}.
	 * @param player the player.
	 * @param node the node.
	 */
	public HerblorePulse(final Player player, final Item node, final int amount, final GenericPotion potion) {
		super(player, node);
		this.amount = amount;
		this.initialAmount = amount;
		this.potion = potion;
	}

	@Override
	public boolean checkRequirements() {
		if (!player.getQuestRepository().isComplete(Quests.DRUIDIC_RITUAL)) {
			player.getPacketDispatch().sendMessage("You must complete the Druidic Ritual quest before you can use Herblore.");
			return false;
		}
		if (player.getSkills().getLevel(Skills.HERBLORE) < potion.getLevel()) {
			player.getPacketDispatch().sendMessage("You need a Herblore level of at least " + potion.getLevel() + " in order to do this.");
			return false;
		}
		if (!player.getInventory().containsItem(potion.getBase()) || !player.getInventory().containsItem(potion.getIngredient())) {
			return false;
		}
		return true;
	}

	@Override
	public void animate() {
	}

	@Override
	public boolean reward() {
		if (potion.getBase().getId() == VIAL_OF_WATER.getId()) {
			if (initialAmount == 1 && getDelay() == 1) {
				player.animate(ANIMATION);
				setDelay(3);
				return false;
			}
			handleUnfinished();
		} else {
			if (initialAmount == 1 && getDelay() == 1) {
				player.animate(ANIMATION);
				setDelay(3);
				return false;
			}
			if (getDelay() == 1) {
				setDelay(3);
				player.animate(ANIMATION);
				return false;
			}
			handleFinished();
		}
		amount--;
		return amount == 0;
	}

	/**
	 * Method used to handle the potion making of an unf-potion.
	 */
	public void handleUnfinished() {
		if (cycles == 0) {
			player.animate(ANIMATION);
		}
		if ((player.getInventory().containsItem(potion.getBase()) && player.getInventory().containsItem(potion.getIngredient())) && player.getInventory().remove(potion.getBase(), potion.getIngredient())) {
			final Item item = potion.getProduct();
		    player.getInventory().add(item);
			player.getPacketDispatch().sendMessage("You put the" + StringUtils.formatDisplayName(potion.getIngredient().getName().toLowerCase().replace("clean", "")) + " leaf into the vial of water.");
            playAudio(player, Sounds.GRIND_2608);
			if (cycles++ == 3) {
				player.animate(ANIMATION);
				cycles = 0;
			}
		}
	}

	/**
	 * Method used to handle the finished potion making.
	 */
	public void handleFinished() {
		if ((player.getInventory().containsItem(potion.getBase()) && player.getInventory().containsItem(potion.getIngredient())) && player.getInventory().remove(potion.getBase(), potion.getIngredient())) {
			Item item = potion.getProduct();
                        if (SkillcapePerks.isActive (SkillcapePerks.BREWMASTER, player)) {
                            if (RandomFunction.random(100) < 15) {
                                Consumables consum = Consumables.getConsumableById (item.getId());
                                if (consum != null)
                                    item = new Item (consum.getConsumable().getIds()[0], item.getAmount());
                                player.sendMessage ("Due to your expertise, you manage to make an extra dose.");
                            }
                        }
		    player.getInventory().add(item);
			player.getSkills().addExperience(Skills.HERBLORE, potion.getExperience(), true);
			player.getPacketDispatch().sendMessage("You mix the " + potion.getIngredient().getName().toLowerCase() + " into your potion.");
            playAudio(player, Sounds.GRIND_2608);
			player.animate(ANIMATION);
		}
	}
}
