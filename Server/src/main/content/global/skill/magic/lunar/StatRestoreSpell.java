package content.global.skill.magic.lunar;

import content.data.consumables.Consumables;
import core.game.consumable.Potion;
import core.plugin.Initializable;
import core.game.node.entity.combat.spell.MagicSpell;
import core.game.node.entity.combat.spell.Runes;
import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.spell.SpellType;
import core.game.node.entity.player.Player;
import core.game.bots.AIPlayer;
import core.game.node.entity.player.link.SpellBookManager.SpellBook;
import core.game.node.item.Item;
import core.game.world.map.RegionManager;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Plugin;
import org.rs09.consts.Sounds;

import java.util.List;

import static core.api.ContentAPIKt.playGlobalAudio;

@Initializable
public class StatRestoreSpell extends MagicSpell {

	private static final Animation ANIMATION = new Animation(4413);
	private static final Graphics GRAPHICS = new Graphics(733, 130);
        private static final Consumables[] acceptedPotions = new Consumables[] { Consumables.RESTORE, Consumables.SUPER_RESTO, Consumables.PRAYER, Consumables.ENERGY, Consumables.SUPER_ENERGY }; 

	public StatRestoreSpell() {
		super(SpellBook.LUNAR, 81, 84, null, null, null, new Item[] { new Item(Runes.ASTRAL_RUNE.getId(), 2), new Item(Runes.EARTH_RUNE.getId(), 10), new Item(Runes.WATER_RUNE.getId(), 10) });
	}

	@Override
	public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
		SpellBook.LUNAR.register(27, this);
		return this;
	}

	@Override
	public boolean cast(Entity entity, Node target) {
		final Player player = ((Player) entity);
		Item item = ((Item) target);
		player.getInterfaceManager().setViewedTab(6);

                if (Consumables.getConsumableById(item.getId()) == null) {
		    player.getPacketDispatch().sendMessage("You can only cast this spell on a potion.");
                    return false;
                }

		final Potion potion = (Potion) Consumables.getConsumableById(item.getId()).getConsumable();

		if (potion == null) {
		        player.getPacketDispatch().sendMessage("You can only cast this spell on a potion.");
			return false;
		}
		if (!item.getDefinition().isTradeable() || !isRestore(potion)) {
			player.getPacketDispatch().sendMessage("You can't cast this spell on that item.");
			return false;
		}
		List<Player> pl = RegionManager.getLocalPlayers(player, 2);
		int plSize = pl.size() - 1;
		int doses = potion.getDose(item);
		if (pl.size() == 0) {
			return false;
		}
		if (!super.meetsRequirements(player, true, false)) {
			return false;
		}
		int size = 0;
    		for (Player players : pl) {
                        if (size >= doses) break;
			Player o = (Player) players;
			if (!o.isActive() || o.getLocks().isInteractionLocked() || o == player) {
				continue;
			}
			if (!o.getSettings().isAcceptAid() && !(o instanceof AIPlayer)) {
				continue;
			}
			o.graphics(GRAPHICS);
			potion.getEffect().activate(o);
			size++;
		}
		if (size == 0) {
			player.getPacketDispatch().sendMessage("There is nobody around that has accept aid on to share the potion with you.");
			return false;
		}
		super.meetsRequirements(player, true, true);
		potion.getEffect().activate(player);
		playGlobalAudio(player.getLocation(), Sounds.LUNAR_STAT_SHARE_2899);
		player.animate(ANIMATION);
		player.graphics(GRAPHICS);
		player.getInventory().remove(item);
                int newIndex = (potion.getIds().length - doses) + size;
                if (newIndex > potion.getIds().length - 1) {
                    player.getInventory().add(new Item(229));
                    return true;
                }
		player.getInventory().add(new Item(potion.getIds()[newIndex]));
		return true;
	}

	private boolean isRestore(Potion p) {
                for (int i = 0; i < acceptedPotions.length; i++) {
                    if (p == acceptedPotions[i].getConsumable())
                        return true;
                }
		return false;
	}

}
