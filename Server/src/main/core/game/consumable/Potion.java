package core.game.consumable;

import content.data.consumables.Consumables;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.audio.Audio;
import core.game.node.item.Item;
import org.rs09.consts.Sounds;

import static core.api.ContentAPIKt.playAudio;

public class Potion extends Drink {

    private static final int VIAL = 229;

    private static final Audio SOUND = new Audio(2401, 1, 1);

    public Potion(final int[] ids, final ConsumableEffect effect, final String... messages) {
        super(ids, effect, messages);
    }

    @Override
    public void consume(Item item, Player player) {
        executeConsumptionActions(player);
        final int nextItemId = getNextItemId(item.getId());
        if (nextItemId != -1) {
            player.getInventory().replace(new Item(nextItemId), item.getSlot());
        }
        else {
            player.getInventory().replace(new Item(VIAL), item.getSlot());
        }

        final int initialLifePoints = player.getSkills().getLifepoints();
        Consumables.getConsumableById(item.getId()).getConsumable().effect.activate(player);
        if (messages.length == 0) {
            sendDefaultMessages(player, item);
        } else {
            sendCustomMessages(player, messages);
        }
        sendHealingMessage(player, initialLifePoints);
    }

    @Override
    protected void executeConsumptionActions(Player player) {
        player.animate(animation);
        playAudio(player, Sounds.LIQUID_2401);
    }

    @Override
    protected void sendDefaultMessages(Player player, Item item) {
        int consumedDoses = 1;
        int i = 0;
        while (ids[i] != item.getId()) {
            consumedDoses++;
            i++;
        }
        final int dosesLeft = ids.length - consumedDoses;
        player.getPacketDispatch().sendMessage("You drink some of your " + getFormattedName(item) + ".");
        if (dosesLeft > 1) {
            player.getPacketDispatch().sendMessage("You have " + dosesLeft + " doses of potion left.");
        } else if (dosesLeft == 1) {
            player.getPacketDispatch().sendMessage("You have 1 dose of potion left.");
        } else {
            player.getPacketDispatch().sendMessage("You have finished your potion.");
        }
    }

    public int getDose(Item potion){
        for (int i = 0; i < ids.length; i++)
            if (ids[i] == potion.getId()) return ids.length - i;
        return Integer.parseInt(potion.getName().replaceAll("[^\\d.]",""));
    }

    public int[] getIds() {
        return ids;
    }

    public ConsumableEffect getEffect() {
        return effect;
    }
}
