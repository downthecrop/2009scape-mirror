package core.game.interaction.item.brawling_gloves;

import api.LoginListener;
import api.PersistPlayer;
import core.cache.def.impl.ItemDefinition;
import core.game.node.entity.player.Player;

import core.game.node.item.Item;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Objects;

/**
 * Manages brawling gloves for a player
 * @author ceik
 */
public class BrawlingGlovesManager implements LoginListener, PersistPlayer {
    final Player player;
    public HashMap<Integer, Integer> GloveCharges = new HashMap<Integer,Integer>();

    public BrawlingGlovesManager(Player player){this.player = player;}
    public BrawlingGlovesManager(){this.player = null;}

    @Override
    public void login(@NotNull Player player) {
        BrawlingGlovesManager instance = new BrawlingGlovesManager(player);
        player.setAttribute("bg-manager", instance);
    }

    @Override
    public void parsePlayer(@NotNull Player player, @NotNull JSONObject data) {
        BrawlingGlovesManager instance = getInstance(player);
        if(data.containsKey("brawlingGloves"))
        {
            JSONArray bgData = (JSONArray) data.get("brawlingGloves");
            for (Object bg : bgData) {
                JSONObject glove = (JSONObject) bg;
                instance.registerGlove(BrawlingGloves.forIndicator(Integer.parseInt(glove.get("gloveId").toString())).getId(), Integer.parseInt(glove.get("charges").toString()));
            }
        }
    }

    @Override
    public void savePlayer(@NotNull Player player, @NotNull JSONObject save) {
        if(getInstance(player).GloveCharges.size() > 0){
            JSONArray brawlingGloves = new JSONArray();
            getInstance(player).GloveCharges.entrySet().forEach(glove -> {
                JSONObject bGlove = new JSONObject();
                bGlove.put("gloveId", Integer.toString(BrawlingGloves.forId(glove.getKey()).getIndicator()));
                bGlove.put("charges", Integer.toString(glove.getValue()));
                brawlingGloves.add(bGlove);
            });
            save.put("brawlingGloves", brawlingGloves);
        }
    }

    public void registerGlove(int id) {
        try {
            registerGlove(id, Objects.requireNonNull(BrawlingGloves.forId(id)).getCharges());
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void registerGlove(int id, int charges) {GloveCharges.putIfAbsent(id,charges);}


    public void updateCharges(int glove, int charges){
        if(GloveCharges.get(glove) != null){
            if(GloveCharges.get(glove) - charges <= 0) {
                GloveCharges.remove(glove);
                player.getEquipment().remove(new Item(glove));
                player.getPacketDispatch().sendMessage("<col=ff0000>You use the last charge of your " + ItemDefinition.forId(glove).getName() + " and they vanish.</col>");
                return;
            }
            int currentCharges = GloveCharges.get(glove);
            GloveCharges.replace(glove,currentCharges - charges);
            player.debug("Glove charges: " + (currentCharges - 1));
            if((currentCharges - 1) % 50 == 0) {
                player.getPacketDispatch().sendMessage("<col=1fbd0d>Your " + ItemDefinition.forId(glove).getName() + " have " + GloveCharges.get(glove) + " charges left.</col>");
            }
        }
    }

    public double getExperienceBonus(){
        double bonus;
        int level = player.getSkullManager().getLevel();
        if(level > 0){
            bonus = 3.0;
        }else{
            bonus = 0.5;
        }
        return bonus;
    }

    public static BrawlingGlovesManager getInstance(Player player)
    {
        return player.getAttribute("bg-manager", new BrawlingGlovesManager());
    }
}
