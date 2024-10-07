package content.region.kandarin.ardougne.quest.sheepherder;

import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.quest.Quest;
import core.game.node.item.Item;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.plugin.Initializable;
import org.rs09.consts.Items;

import java.util.HashMap;

@Initializable
public class SheepHerder extends Quest {
    public static Item CATTLE_PROD = new Item(Items.CATTLEPROD_278);
    public static Item POISON = new Item(279);
    public static Item PLAGUE_TOP = new Item(284);
    public static Item PLAGUE_BOTTOM = new Item(285);
    public static Item RED_SHEEP_BONES = new Item(Items.SHEEP_BONES_1_280);
    public static Item GREEN_SHEEP_BONES = new Item(Items.SHEEP_BONES_2_281);
    public static Item BLUE_SHEEP_BONES = new Item(Items.SHEEP_BONES_3_282);
    public static Item YELLOW_SHEEP_BONES = new Item(Items.SHEEP_BONES_4_283);
    public static int RED_SHEEP = 2345;
    public static int GREEN_SHEEP = 2346;
    public static int BLUE_SHEEP = 2347;
    public static int YELLOW_SHEEP = 2348;
    public static int FARMER_BRUMTY = 291;

    public static HashMap<Integer,Item> boneMap = new HashMap<>();
    static{
        boneMap.put(RED_SHEEP,RED_SHEEP_BONES);
        boneMap.put(GREEN_SHEEP,GREEN_SHEEP_BONES);
        boneMap.put(YELLOW_SHEEP,YELLOW_SHEEP_BONES);
        boneMap.put(BLUE_SHEEP,BLUE_SHEEP_BONES);
    }

    public SheepHerder(){super("Sheep Herder",113,112,4,60,0,1,3);}

    @Override
    public void drawJournal(Player player, int stage) {
        boolean hasGear = (player.getInventory().containsItem(PLAGUE_BOTTOM) && player.getInventory().containsItem(PLAGUE_TOP) || (player.getEquipment().containsItem(PLAGUE_BOTTOM) && player.getEquipment().containsItem(PLAGUE_TOP))) || stage >= 20;
        int line = 12;
        boolean sheepDead = player.getAttribute("sheep_herder:all_dead",false);
        super.drawJournal(player, stage);
        if(stage == 0){
            line(player,"I can start this quest by speaking to !!Councillor Halgrive??", line++);
            line(player, "near to the !!Zoo?? in !!East Ardougne??.", line++);
        } else {
            line(player,"Councillor Halgrive asked me to dispose of four plague", line++, true);
            line(player,"bearing sheep just north of Ardougne and I accepted.", line++, true);
            line(player,"He gave me some poisoned sheep feed to do this.", line++, true);
            if(hasGear) {
                line(player, "I bought some protective clothing from Dr. Orbon in the", line++, true);
                line(player, "chapel north of Ardougne Zoo. I could now kill the sheep.", line++, true);
            } else {
                line(player, "!!Councillor Halgrive?? said I should speak to !!Doctor Orbon??", line++);
                line(player, "about getting some protective gear.", line++);
            }
            if(stage == 10) {
                // This is not authentic.
//                line(player, "I need to !!locate the diseased sheep?? and corral them !!into the pen??", line++,sheepDead);
//                line(player, "After which, I need to !!poison them?? and !!incinerate their bones.??", line++,sheepDead);
                line++;
                if (sheepDead) {
                    line(player, "I equipped a prod and then I used it to to herd the diseased", line++, true);
                    line(player, "sheep to a pen where I could safely kill them and", line++, true);
                    line(player, "incinerate their bones.", line++, true);
                    line(player,"I should return to !!Councillor Halgrive?? to collect the reward", line++);
                    line(player,"he has promised me for my hard work.", line++);
                } else {
                    if (player.getAttribute("sheep_herder:red_dead", false)) {
                        line(player, "I have killed the first sheep and incinerated its bones.", line++, true);
                    } else {
                        line(player, "I must find the first sheep and herd it to the special pen.", line++);
                    }
                    if (player.getAttribute("sheep_herder:green_dead", false)) {
                        line(player, "I have killed the second sheep and incinerated its bones.", line++, true);
                    } else {
                        line(player, "I must find the second sheep and herd it to the special", line++);
                        line(player, "pen.", line++);
                    }
                    if (player.getAttribute("sheep_herder:blue_dead", false)) {
                        line(player, "I have killed the third sheep and incinerated its bones.", line++, true);
                    } else {
                        line(player, "I must find the third sheep and herd it to the special pen.", line++);
                    }
                    if (player.getAttribute("sheep_herder:yellow_dead", false)) {
                        line(player, "I have killed the fourth sheep and incinerated its bones.", line++, true);
                    } else {
                        line(player, "I must find the fourth sheep and herd it to the special pen.", line++);
                    }
                }
            }

            if(stage >= 100) {
                line(player, "I equipped a prod to herd the diseased sheep and then I", line++, true);
                line(player, "used it to incinerate all four plagued sheep.", line++, true);
                line(player, "I returned to let Councillor Halgrive know that the plagued", line++, true);
                line(player, "sheep were no more and claimed my reward.", line++, true);
                line++;
                line(player, "%%QUEST COMPLETE!&&", line++, false);
            }
        }
    }

    @Override
    public void finish(Player player) {
        int ln = 10;
        super.finish(player);
        player.getPacketDispatch().sendItemZoomOnInterface(995, 230, 277, 5);
        drawReward(player,"4 Quest Points",ln++);
        drawReward(player,"3100 coins",ln++);
        player.removeAttribute("sheep_herder:red_dead");
        player.removeAttribute("sheep_herder:blue_dead");
        player.removeAttribute("sheep_herder:green_dead");
        player.removeAttribute("sheep_herder:yellow_dead");
        player.removeAttribute("sheep_herder:all_dead");
        player.getInventory().add(new Item(995,3100));
    }

    @Override
    public Quest newInstance(Object object) {
        new HerderSheepNPC(RED_SHEEP, Location.create(2609, 3343, 0)).init();
        new HerderSheepNPC(RED_SHEEP,Location.create(2610, 3344, 0)).init();
        new HerderSheepNPC(RED_SHEEP,Location.create(2609, 3345, 0)).init();
        new HerderSheepNPC(RED_SHEEP,Location.create(2615, 3343, 0)).init();
        new HerderSheepNPC(GREEN_SHEEP,Location.create(2622, 3366, 0)).init();
        new HerderSheepNPC(GREEN_SHEEP,Location.create(2622, 3366, 0)).init();
        new HerderSheepNPC(GREEN_SHEEP,Location.create(2619, 3371, 0)).init();
        new HerderSheepNPC(GREEN_SHEEP,Location.create(2617, 3365, 0)).init();
        new HerderSheepNPC(YELLOW_SHEEP,Location.create(2610, 3390, 0)).init();
        new HerderSheepNPC(YELLOW_SHEEP,Location.create(2613, 3389, 0)).init();
        new HerderSheepNPC(YELLOW_SHEEP,Location.create(2606, 3391, 0)).init();
        new HerderSheepNPC(YELLOW_SHEEP,Location.create(2608, 3387, 0)).init();
        new HerderSheepNPC(BLUE_SHEEP,Location.create(2559, 3388, 0)).init();
        new HerderSheepNPC(BLUE_SHEEP,Location.create(2562, 3388, 0)).init();
        new HerderSheepNPC(BLUE_SHEEP,Location.create(2563, 3383, 0)).init();
        new HerderSheepNPC(BLUE_SHEEP,Location.create(2570, 3381, 0)).init();
        NPC Brumty = new NPC(FARMER_BRUMTY,Location.create(2594,3357,0));
        Brumty.setWalks(false);
        Brumty.setRespawn(true);
        Brumty.init();
        Brumty.setDirection(Direction.NORTH);
        return this;
    }
}
