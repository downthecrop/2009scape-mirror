package core.game.consumable;

import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Animation;
import org.rs09.consts.Sounds;

import static core.api.ContentAPIKt.playAudio;

public class Food extends Consumable {

	public Food(final int[] ids, final ConsumableEffect effect, final String... messages) {
		super(ids, effect, messages);
		animation = new Animation(829);
	}

	public Food(int[] ids, ConsumableEffect effect, Animation animation, String... messages) {
		super(ids, effect, animation, messages);
	}

	@Override
	protected void sendDefaultMessages(Player player, Item item) {
		player.getPacketDispatch().sendMessage("You eat the " + getFormattedName(item) + ".");
	}

	@Override
	protected void executeConsumptionActions(Player player) {
		player.animate(animation);
		playEatingSound(player);
	}

	private void playEatingSound(Player player) {
		playAudio(player, Sounds.EAT_2393);
	}
}
