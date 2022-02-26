package core.game.node.entity.skill;

import core.game.container.impl.EquipmentContainer;
import core.game.node.entity.Entity;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.prayer.PrayerType;
import core.game.node.item.Item;
import org.rs09.consts.Items;
import rs09.game.node.entity.skill.skillcapeperks.SkillcapePerks;
import rs09.game.world.GameWorld;

/**
 * Handles the skill restoration data.
 * @author Emperor
 */
public final class SkillRestoration {

	/**
	 * The skill index.
	 */
	private final int skillId;

	/**
	 * Constructs a new {@code SkillRestoration} {@code Object}.
	 * @param skillId The skill id.
	 */
	public SkillRestoration(int skillId) {
		this.skillId = skillId;
	}

	/**
	 * Restores the skill.
	 * @param entity The entity.
	 */
	public void restore(Entity entity) {
		Skills skills = entity.getSkills();
		int max = skills.getStaticLevel(skillId);
        int tickDivisor = 1;
		if(skillId == Skills.HITPOINTS){
			max = skills.getMaximumLifepoints();
            if(entity instanceof Player) {
                Player player = entity.asPlayer();
                if(player.getPrayer().getActive().contains(PrayerType.RAPID_HEAL)) {
                    tickDivisor *= 2;
                }
                Item gloves = player.getEquipment().get(EquipmentContainer.SLOT_HANDS);
                if(gloves != null && gloves.getId() == Items.REGEN_BRACELET_11133) {
                    tickDivisor *= 2;
                }
            }
		}
        if(skillId == Skills.PRAYER) {
            return;
        }
        if(skillId != Skills.HITPOINTS && skillId != Skills.SUMMONING) {
            if(entity instanceof Player) {
                if(entity.asPlayer().getPrayer().getActive().contains(PrayerType.RAPID_RESTORE)) {
                    tickDivisor *= 2;
                }
            }
        }
        
        int ticks = 100 / tickDivisor;
		if(GameWorld.getTicks() % ticks == 0){
            if(skillId == Skills.HITPOINTS){
                if(skills.getLifepoints() >= max){
                    return;
                }
                skills.heal(1);
            } else {
                int current = skills.getLevel(skillId);
                skills.updateLevel(skillId,current < max ? 1 : -1,max);
			}
		}
	}

	/**
	 * Gets the skillId.
	 * @return The skillId.
	 */
	public int getSkillId() {
		return skillId;
	}
}
