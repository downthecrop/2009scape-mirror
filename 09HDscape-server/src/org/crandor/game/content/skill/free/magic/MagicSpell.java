package org.crandor.game.content.skill.free.magic;

import org.crandor.game.component.Component;
import org.crandor.game.content.skill.Skills;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.CombatSpell;
import org.crandor.game.node.entity.combat.CombatSwingHandler;
import org.crandor.game.node.entity.combat.equipment.SpellType;
import org.crandor.game.node.entity.combat.equipment.WeaponInterface;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.SpellBookManager.SpellBook;
import org.crandor.game.node.entity.player.link.audio.Audio;
import org.crandor.game.node.item.Item;
import org.crandor.game.world.GameWorld;
import org.crandor.game.world.map.MapDistance;
import org.crandor.game.world.map.RegionManager;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.game.world.update.flag.context.Graphics;
import org.crandor.plugin.Plugin;
import org.crandor.tools.RandomFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a magic spell.
 * @author Emperor
 * @param <T> The node type to cast this spell on.
 */
public abstract class MagicSpell implements Plugin<SpellType> {

	/**
	 * The spell book.
	 */
	protected final SpellBook book;

	/**
	 * The level requirement.
	 */
	protected final int level;

	/**
	 * The animation.
	 */
	protected final Animation animation;

	/**
	 * The casting graphics.
	 */
	protected final Graphics graphic;

	/**
	 * The casting Audio.
	 */
	protected final Audio audio;

	/**
	 * The item-array containing the runes required.
	 */
	protected final Item[] runes;

	/**
	 * The spell id.
	 */
	protected int spellId;

	/**
	 * The experience gained.
	 */
	private final double experience;

	/**
	 * Constructs a new {@code MagicSpell} {@code Object}.
	 */
	public MagicSpell() {
		this(SpellBook.MODERN, 0, 0, null, null, null, new Item[0]);
	}

	/**
	 * Constructs a new {@code MagicSpell} {@code Object}.
	 * @param book The spell book this spell is from.
	 * @param level The level requirement.
	 * @param animation the cast animation.
	 * @param graphic The cast graphic.
	 * @param Audio The casting Audio.
	 * @param runes The runes required to cast the spell.
	 */
	public MagicSpell(SpellBook book, int level, final double experience, Animation animation, Graphics graphic, Audio Audio, Item[] runes) {
		this.book = book;
		this.level = level;
		this.experience = experience;
		this.animation = animation;
		this.graphic = graphic;
		this.audio = Audio;
		this.runes = runes;
	}

	/**
	 * Casts a spell.
	 * @param p The player casting the spell.
	 * @param spell The spell.
	 * @param target The target.
	 */
	public static boolean castSpell(final Player p, SpellBook book, int spellId, Node target) {
		if (p.getAttribute("magic-delay", 0) > GameWorld.getTicks()) {
			return false;
		}
		MagicSpell spell = book.getSpell(spellId);
		if (spell == null) {
			return false;
		}
		if (spell.book != book || p.getSpellBookManager().getSpellBook() != book.getInterfaceId()) {
			return false;
		}
		if (target.getLocation() != null && target != p) {
			if (!target.getLocation().withinDistance(p.getLocation(), 15)) {
				return false;
			}
			p.faceLocation(target.getLocation());
		}
		boolean combatSpell = spell instanceof CombatSpell;
		if (!combatSpell && target instanceof Entity) {
			p.faceTemporary((Entity) target, 1);
		}
		if (spell.cast(p, target)) {
			if (book != SpellBook.LUNAR && p.getAttribute("spell:swap", 0) != 0) {
				p.removeAttribute("spell:swap");
				p.getSpellBookManager().setSpellBook(SpellBook.LUNAR);
				p.getInterfaceManager().openTab(new Component(SpellBook.LUNAR.getInterfaceId()));
			}
			if (!combatSpell) {
				p.getSkills().addExperience(Skills.MAGIC, spell.getExperience(p), true);
			}
			if (p.getAttribute("magic-delay", 0) <= GameWorld.getTicks()) {
				p.setAttribute("magic-delay", GameWorld.getTicks() + spell.getDelay());
			}
			return true;
		}
		return false;
	}

	/**
	 * Gets the delay.
	 * @return the delay.
	 */
	public int getDelay() {
		return 3;
	}

	/**
	 * Starts the effect of this spell (if any).
	 * @param entity The entity.
	 * @param victim The victim.
	 * @param state The battle state.
	 */
	public abstract boolean cast(Entity entity, Node target);

	/**
	 * Visualizes the spell.
	 * @param entity The entity.
	 * @param target The target.
	 */
	public void visualize(Entity entity, Node target) {
		entity.graphics(graphic);
		entity.animate(animation);
		sendAudio(entity);
	}

	/**
	 * Checks if the player is holding the staff that will be used instead of
	 * the rune.
	 * @param p The player.
	 * @param rune The rune item id.
	 * @return {@code True} if the player is wearing the correct staff for the
	 * rune.
	 */
	public boolean usingStaff(Player p, int rune) {
		Item weapon = p.getEquipment().get(3);
		if (weapon == null) {
			return false;
		}
		MagicStaff staff = MagicStaff.forId(rune);
		if (staff == null) {
			return false;
		}
		int[] staves = staff.getStaves();
		for (int id : staves) {
			if (weapon.getId() == id) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the casting entity meets the requirements to cast this spell.
	 * @param caster The caster.
	 * @param message If a message should be send to the caster if it doesn't
	 * meet the requirements.
	 * @param remove If we should remove the runes from the player's inventory.
	 * @return {@code True} if so.
	 */
	public boolean meetsRequirements(Entity caster, boolean message, boolean remove) {
		if (!checkLevelRequirement(caster, message)) {
			return false;
		}
		if (caster instanceof Player) {
			CombatSpell spell = ((Player) caster).getProperties().getAutocastSpell();
			if (spell != null) {
				boolean slayer = ((Player) caster).getEquipment().get(3).getName().contains("layer's staff");
				boolean voidKnight = ((Player) caster).getEquipment().get(3).getName().contains("knight mace");
				if ((spell.getSpellId() == 31 && !slayer) || (spell.getSpellId() == 42 && !voidKnight)) {
					((Player) caster).getPacketDispatch().sendMessage("You need the proper staff to autocast this spell.");
					return false;
				}
			}
		}
		if((spellId == 12 || spellId == 30 || spellId == 56) && caster instanceof Player){
			if (caster.getAttribute("entangleDelay", 0) > GameWorld.getTicks()) {
				caster.asPlayer().sendMessage("You have recently cast a binding spell.");
				return false;
			}
		}
		if (caster instanceof Player) {
			Player p = (Player) caster;
			if(p.getEquipment().get(3) != null && p.getEquipment().get(3).getId() == 14726){
				if(RandomFunction.getRandom(100) < 13){
					p.sendMessage("Your staff negates the rune requirement of the spell.");
					return true;
				}
			}
			if (runes == null) {
				return true;
			}
			List<Item> toRemove = new ArrayList<>();
			for (Item item : runes) {
				if (!hasRune(p, item, toRemove, message)) {
					return false;
				}
			}
			if (remove) {
				p.getInventory().remove(toRemove.toArray(new Item[0]));
			}
			return true;
		}
		return true;
	}
	
	/**
	 * Checks the level requirement.
	 * @param caster The caster.
	 * @param message If we display the message.
	 * @return {@code True} if passed.
	 */
	public boolean checkLevelRequirement(Entity caster, boolean message) {
		if (caster instanceof Player && caster.getSkills().getLevel(Skills.MAGIC, this instanceof CombatSpell ? true : false) < levelRequirement()) {
			if (message && caster instanceof Player) {
				((Player) caster).getPacketDispatch().sendMessage("You need a Magic level of " + levelRequirement() + " to cast this spell.");
			}
			return false;
		}
		return true;
	}

	/**
	 * Checks if the player has a rune to remove.
	 * @param p the player.
	 * @param item the item.
	 * @param toRemove the list of items to remove.
	 * @param message the message.
	 * @return {@code True} if so.
	 */
	public boolean hasRune(Player p, Item item, List<Item> toRemove, boolean message) {
		if (!usingStaff(p, item.getId())) {
			if (!p.getInventory().contains(item.getId(), item.getAmount())) {
				if (message) {
					p.getPacketDispatch().sendMessage("You don't have enough " + item.getName() + "s to cast this spell.");
				}
				return false;
			}
			toRemove.add(item);
			return true;
		}
		return true;
	}

	/**
	 * Adds the experience for casting this spell.
	 * @param entity The entity to reward with experience.
	 * @param hit The hit.
	 */
	public void addExperience(Entity entity, int hit) {
		if (!(entity instanceof Player) || hit < 1) {
			return;
		}
		entity.getSkills().addExperience(Skills.HITPOINTS, hit * 1.33, true);
		if (entity.getProperties().getAttackStyle().getStyle() == WeaponInterface.STYLE_DEFENSIVE_CAST) {
			entity.getSkills().addExperience(Skills.DEFENCE, hit, true);
			entity.getSkills().addExperience(Skills.MAGIC, 1.33 * hit, true);
			return;
		}
		entity.getSkills().addExperience(Skills.MAGIC, hit * (CombatSwingHandler.EXPERIENCE_MOD / 2), true);
	}

	/**
	 * Gets the level requirement to cast this spell.
	 * @return The level requirement.
	 */
	public int levelRequirement() {
		return level;
	}

	/**
	 * Sends the Audio packet for all players to hear (in a distance specified
	 * by {@link MapDistance#Audio#getDistance()}).
	 * @param entity The entity from where this Audio comes from.
	 */
	public void sendAudio(Entity entity) {
		sendAudio(entity, audio);
	}

	/**
	 * Sends the Audio packet for all players to hear (in a distance specified
	 * by {@link MapDistance#Audio#getDistance()}).
	 * @param entity The entity from where this Audio comes from.
	 * @param audio The Audio to send.
	 */
	public void sendAudio(Entity entity, Audio audio) {
		if (audio == null || audio.getId() < 0) {
			return;
		}
		for (Player p : RegionManager.getLocalPlayers(entity, MapDistance.SOUND.getDistance())) {
			p.getAudioManager().send(audio);
		}
	}

	@Override
	public Object fireEvent(String identifier, Object... args) {
		return null;
	}

	/**
	 * Gets the audio.
	 * @return the audio.
	 */
	public Audio getAudio() {
		return audio;
	}

	/**
	 * Gets the spell book for this spell.
	 * @return The spell book.
	 */
	public SpellBook getBook() {
		return book;
	}

	/**
	 * Gets the item array of runes required to cast this spell.
	 * @return The item array of runes, or {@code null} if the spell doesn't need
	 * any runes.
	 */
	public Item[] getCastRunes() {
		return runes;
	}

	/**
	 * Gets the spellId.
	 * @return The spellId.
	 */
	public int getSpellId() {
		return spellId;
	}

	/**
	 * Sets the spellId.
	 * @param spellId The spellId to set.
	 */
	public void setSpellId(int spellId) {
		this.spellId = spellId;
	}

	/**
	 * Gets the experience.
	 * @return The experience.
	 */
	public double getExperience() {
		return experience;
	}

	/**
	 * Gets the experience.
	 * @param player the player.
	 * @return the experience.
	 */
	public double getExperience(Player player) {
		return experience;
	}

	/**
	 * Gets the level requirement.
	 * @return The level requirement.
	 */
	public int getLevel() {
		return level;
	}
}