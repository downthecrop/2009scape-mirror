package plugin.skill.magic;

import org.crandor.game.component.Component;
import org.crandor.game.component.ComponentDefinition;
import org.crandor.game.component.ComponentPlugin;
import org.crandor.game.content.skill.free.magic.MagicSpell;
import org.crandor.game.content.skill.free.magic.Runes;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.equipment.SpellType;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.SpellBookManager.SpellBook;
import org.crandor.game.node.entity.player.link.TeleportManager.TeleportType;
import org.crandor.game.node.entity.player.link.audio.Audio;
import org.crandor.game.node.item.Item;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.game.world.update.flag.context.Graphics;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Handles the teleport other spells.
 * @author Emperor
 */
@InitializablePlugin
public final class TeleotherSpells extends MagicSpell {

	/**
	 * The destination.
	 */
	private String destination;

	/**
	 * The destination location.
	 */
	private Location location;

	/**
	 * Constructs a new {@code TeleotherSpells} {@code Object}.
	 */
	public TeleotherSpells() {
		/*
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code TeleotherSpells} {@code Object}.
	 * @param level The level required.
	 * @param destination The destination name.
	 * @param location The location to teleport to.
	 * @param runes The runes required.
	 */
	public TeleotherSpells(int level, double experience, String destination, Location location, Item... runes) {
		super(SpellBook.MODERN, level, experience, Animation.create(1818), Graphics.create(343), new Audio(199, 0, 0), runes);
		this.destination = destination;
		this.location = location;
	}

	@Override
	public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
		ComponentDefinition.put(326, new ComponentPlugin() {
			@Override
			public Plugin<Object> newInstance(Object arg) throws Throwable {
				return this;
			}

			@Override
			public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
				if (button == 5) {
					player.lock(2);
					if (player.getTeleporter().send(player.getAttribute("t-o_location", player.getLocation()), TeleportType.TELE_OTHER)) {
						player.visualize(Animation.create(1816), Graphics.create(342));
					}
				}
				player.getInterfaceManager().close();
				return true;
			}
		});
		SpellBook.MODERN.register(54, new TeleotherSpells(74, 84, "Lumbridge", Location.create(3222, 3217, 0), Runes.SOUL_RUNE.getItem(1), Runes.LAW_RUNE.getItem(1), Runes.EARTH_RUNE.getItem(1)));
		SpellBook.MODERN.register(59, new TeleotherSpells(82, 92, "Falador", Location.create(2965, 3378, 0), Runes.SOUL_RUNE.getItem(1), Runes.LAW_RUNE.getItem(1), Runes.WATER_RUNE.getItem(1)));
		SpellBook.MODERN.register(62, new TeleotherSpells(90, 100, "Camelot", Location.create(2758, 3478, 0), Runes.SOUL_RUNE.getItem(2), Runes.LAW_RUNE.getItem(1)));
		return this;
	}

	@Override
	public boolean cast(Entity entity, Node target) {
		Player p = (Player) entity;
		if (!(target instanceof Player)) {
			p.getPacketDispatch().sendMessage("You can only cast this spell on other players.");
			return false;
		}
		if (!entity.getZoneMonitor().teleport(0, null)) {
			return false;
		}
		Player o = (Player) target;
		if (!o.isActive() || o.getLocks().isTeleportLocked() || o.getInterfaceManager().isOpened()) {
			p.getPacketDispatch().sendMessage("The other player is currently busy.");
			return false;
		}
		if(o.getZoneMonitor().isInZone("Wilderness") && o.getProperties().getCombatPulse().isInCombat()){
			p.sendMessage("The other player has their hands full at the moment!");
			return true;
		}
		if (!o.getSettings().isAcceptAid()) {
			p.getPacketDispatch().sendMessage("The player is not accepting any aid.");
			return false;
		}
		if (!meetsRequirements(entity, true, true)) {
			return false;
		}
		visualize(entity, target);
		p.faceLocation(o.getLocation());
		o.setAttribute("t-o_location", location);
		o.getPacketDispatch().sendString(p.getUsername(), 326,1);
		o.getPacketDispatch().sendString(destination, 326, 3);
		o.getInterfaceManager().open(new Component(326));
		return true;
	}

}
