package core.game.interaction.item.brawling_gloves;

import core.cache.def.impl.ItemDefinition;
import core.game.node.entity.skill.Skills;

import java.util.HashMap;

/**
 * Represents brawling gloves
 * @author ceik
 */
public enum BrawlingGloves {
    MELEE      (13845, "melee",  500, 1,  Skills.ATTACK),
    RANGED     (13846,"ranged",  500, 2,  Skills.RANGE),
    MAGIC      (13847,"magic",   500, 3,  Skills.MAGIC),
    PRAYER     (13848,"prayer",  450, 4,  Skills.PRAYER),
    AGILITY    (13849,"agility", 450, 5,  Skills.AGILITY),
    WOODCUTTING(13850,"wc",      450, 6,  Skills.WOODCUTTING),
    FIREMAKING (13851,"fm",      450, 7,  Skills.FIREMAKING),
    MINING     (13852,"mining",  450, 8,  Skills.MINING),
    HUNTER     (13853,"hunter",  450, 9,  Skills.HUNTER),
    THIEVING   (13854,"thieving",450, 10, Skills.THIEVING),
    SMITHING   (13855,"smithing",450, 11, Skills.SMITHING),
    FISHING    (13856,"fishing", 450, 12, Skills.FISHING),
    COOKING    (13857,"cooking", 450, 13, Skills.COOKING);

    private final int id;
    private final String name;
    private final int charges;
    private final int indicator;
    private int skillSlot;

    BrawlingGloves(int id, String name, int charges, int indicator, int skillSlot){
        this.id = id;
        this.name = ItemDefinition.forId(id).getName();
        this.charges = charges;
        this.indicator = indicator;
        this.skillSlot = skillSlot;
    }

    public static HashMap<Integer,BrawlingGloves> glovesMap = new HashMap<>();
    public static HashMap<Integer,BrawlingGloves> skillMap = new HashMap<>();
    public static HashMap<Integer,BrawlingGloves> indicatorMap = new HashMap<>();

    static {
        for(BrawlingGloves gloves : BrawlingGloves.values()){
            glovesMap.putIfAbsent(gloves.id, gloves);
            skillMap.putIfAbsent(gloves.skillSlot,gloves);
            indicatorMap.putIfAbsent(gloves.indicator,gloves);
        }
    }



    public static BrawlingGloves forId(int id){
        return glovesMap.get(id);
    }

    public static BrawlingGloves forIndicator(int indicator){
        return indicatorMap.get(indicator);
    }

    public static BrawlingGloves forSkill(int skillSlot){
        return skillMap.get(skillSlot);
    }

    public int getId(){return id;}
    public int getCharges(){return charges;}
    public int getIndicator(){return indicator;}
    public String getName(){return name;}
    public int getSkillSlot() {return skillSlot;}
}
