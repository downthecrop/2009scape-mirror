package plugin.skill.magic.lunar;

import org.crandor.game.component.CloseEvent;
import org.crandor.game.component.Component;
import org.crandor.game.content.skill.free.magic.MagicSpell;
import org.crandor.game.content.skill.free.magic.Runes;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.equipment.SpellType;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.SpellBookManager.SpellBook;
import org.crandor.game.node.item.Item;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.game.world.update.flag.context.Graphics;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the monster examine spell.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class MonsterExamineSpell extends MagicSpell {

	/**
	 * Represents the animation of this spell.
	 */
	private final static Animation ANIMATION = new Animation(6293);

	/**
	 * Repesents the graphics of this spell.
	 */
	private static final Graphics GRAPHIC = new Graphics(738, 130);

	/**
	 * Represents the graphics of the eye.
	 */
	private static final Graphics EYE = new Graphics(1059);

	/**
	 * Represents the animation of the spell.
	 */
	private static final Component COMPONENT = new Component(522);

	/**
	 * Constructs a new {@code CurePlantSpell} {@code Object}.
	 */
	public MonsterExamineSpell() {
		super(SpellBook.LUNAR, 66, 61, ANIMATION, null, null, new Item[] { new Item(Runes.COSMIC_RUNE.getId(), 1), new Item(Runes.ASTRAL_RUNE.getId(), 1), new Item(Runes.MIND_RUNE.getId(), 1) });
	}

	@Override
	public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
		SpellBook.LUNAR.register(6, this);
		return this;
	}

	@Override
	public boolean cast(Entity entity, Node target) {
		final Player player = ((Player) entity);
		if (!(target instanceof NPC)) {
			player.getPacketDispatch().sendMessage("You can only cast this spell on monsters.");
			return false;
		}
		if (!super.meetsRequirements(player, true, true)) {
			return false;
		}
		final NPC npc = ((NPC) target);
		player.animate(ANIMATION);
		player.face(npc);
		player.getAudioManager().send(3621);
		;
		COMPONENT.setCloseEvent(new CloseEvent() {
			@Override
			public boolean close(Player player, Component c) {
				player.getInterfaceManager().restoreTabs();
				return true;
			}
		});
		player.graphics(EYE);
		npc.graphics(GRAPHIC);
		npc.getSkills().updateCombatLevel();
		player.getPacketDispatch().sendString("Monster name: " + npc.getName(), 522, 0);
		player.getPacketDispatch().sendString("Combat level: " + npc.getDefinition().getCombatLevel(), 522, 1);
		player.getPacketDispatch().sendString("Life points: " + npc.getSkills().getMaximumLifepoints(), 522, 2);
		player.getPacketDispatch().sendString("Creature's max hit: " + npc.getSwingHandler(false).calculateHit(npc, npc, 1.0), 522, 3);
		player.getPacketDispatch().sendString("", 522, 4);
		player.getInterfaceManager().openSingleTab(COMPONENT);
		return true;
	}
}
