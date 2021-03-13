package core.game.node.entity.skill.fletching;

import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.fletching.items.gem.GemBoltPulse;
import core.game.node.item.Item;
import core.net.packet.PacketRepository;
import core.net.packet.context.ChildPositionContext;
import core.net.packet.out.RepositionChild;
import core.plugin.Initializable;
import core.plugin.Plugin;
import rs09.game.content.dialogue.SkillDialogueHandler;
import rs09.game.content.dialogue.SkillDialogueHandler.SkillDialogue;
import rs09.game.node.entity.skill.fletching.items.gem.GemBoltCutPulse;
import org.rs09.consts.Items;

/**
 * Represents the gem bolt creating plugin.
 * @author Ceikry
 * @version 2.0
 */
@Initializable
public final class GemBoltPlugin extends UseWithHandler {
	Fletching.GemBolts bolt;

	/**
	 * Constructs a new {@code GemBoltPlugin} {@code Object}.
	 */
	public GemBoltPlugin() {
		super(Items.OPAL_BOLT_TIPS_45,
				Items.PEARL_BOLT_TIPS_46,
				Items.JADE_BOLT_TIPS_9187,
				Items.TOPAZ_BOLT_TIPS_9188,
				Items.SAPPHIRE_BOLT_TIPS_9189,
				Items.EMERALD_BOLT_TIPS_9190,
				Items.RUBY_BOLT_TIPS_9191,
				Items.DIAMOND_BOLT_TIPS_9192,
				Items.DRAGON_BOLT_TIPS_9193,
				Items.ONYX_BOLT_TIPS_9194,
				Items.OYSTER_PEARL_411,
				Items.OYSTER_PEARLS_413,
				Items.OPAL_1609,
				Items.JADE_1611,
				Items.RED_TOPAZ_1613,
				Items.SAPPHIRE_1607,
				Items.EMERALD_1605,
				Items.RUBY_1603,
				Items.DIAMOND_1601,
				Items.DRAGONSTONE_1615,
				Items.ONYX_6573);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		addHandler(Items.CHISEL_1755, ITEM_TYPE, this);
		for(Fletching.GemBolts gem : Fletching.GemBolts.values()){
			addHandler(gem.base, ITEM_TYPE,this);
		}
		return this;
	}

	@Override
	public boolean handle(final NodeUsageEvent event) {
		final Player player = event.getPlayer();
		bolt = Fletching.tipMap.get(event.getUsedItem().getId());
		if(bolt == null){
			bolt = Fletching.tipMap.get(event.getUsedWith().getId());
		}
		if(bolt != null){
			SkillDialogueHandler handler = new SkillDialogueHandler(player, SkillDialogue.ONE_OPTION, new Item(bolt.product)) {
				@Override
				public void create(final int amount, int index) {
					player.getPulseManager().run(new GemBoltPulse(player, event.getUsedItem(), bolt, amount));
				}
				@Override
				public int getAll(int index) {
					return player.getInventory().getAmount(event.getUsedItem());
				}
			};
			handler.open();
			PacketRepository.send(RepositionChild.class, new ChildPositionContext(player, 309, 2, 210, 10));
		} else {
			final Fletching.GemBolts gem = Fletching.gemMap.get(event.getUsedItem().getId() == Items.CHISEL_1755 ? event.getBaseItem().getId() : event.getUsedItem().getId());
			if(gem == null){
				return false;
			}
			new SkillDialogueHandler(player, SkillDialogue.ONE_OPTION, new Item(gem.gem)) {
				@Override
				public void create(final int amount, final int index) {
					player.getPulseManager().run(new GemBoltCutPulse(player, event.getUsedItem(), gem, amount));
				}

				@Override
				public int getAll(int index) {
					return player.getInventory().getAmount(gem.gem);
				}

			}.open();
		}
		return true;
	}
}
