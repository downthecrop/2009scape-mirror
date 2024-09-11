package content.data.consumables.effects;

import core.game.node.entity.player.Player;
import core.game.consumable.ConsumableEffect;
import core.game.node.entity.skill.Skills;

public class RestoreEffect extends ConsumableEffect {
    double base,bonus;
    boolean all_skills; // Except for hitpoints
    public RestoreEffect(double base, double bonus){
        this.base = base;
        this.bonus = bonus;
        this.all_skills = false;
    }

    public RestoreEffect(double base, double bonus, boolean all_skills){
        this.base = base;
        this.bonus = bonus;
        this.all_skills = all_skills;
    }
    final int[] SKILLS = new int[] { Skills.DEFENCE, Skills.ATTACK, Skills.STRENGTH, Skills.MAGIC, Skills.RANGE };
    final int[] ALL_SKILLS = new int[]{
            Skills.ATTACK,Skills.DEFENCE, Skills.STRENGTH,Skills.RANGE,Skills.PRAYER,Skills.MAGIC, Skills.COOKING,
            Skills.WOODCUTTING,Skills.FLETCHING,Skills.FISHING,Skills.FIREMAKING,Skills.CRAFTING,Skills.SMITHING,
            Skills.MINING,Skills.HERBLORE,Skills.AGILITY,Skills.THIEVING,Skills.SLAYER,Skills.FARMING,
            Skills.RUNECRAFTING,Skills.HUNTER,Skills.CONSTRUCTION,Skills.SUMMONING };
    @Override
    public void activate(Player p) {
        Skills sk = p.getSkills();
        int[] skills = this.all_skills ? ALL_SKILLS : SKILLS;
        for(int skill : skills){
            int statL = sk.getStaticLevel(skill);
            int curL = sk.getLevel(skill);
            if(curL < statL){
                int boost = (int) (base + (statL * bonus));
                p.getSkills().updateLevel(skill, boost, statL);
            }
        }
    }
}
