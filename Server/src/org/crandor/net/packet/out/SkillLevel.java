package org.crandor.net.packet.out;

import org.crandor.game.content.skill.Skills;
import org.crandor.net.packet.IoBuffer;
import org.crandor.net.packet.OutgoingPacket;
import org.crandor.net.packet.context.SkillContext;

/**
 * Handles the update skill outgoing packet.
 * @author Emperor
 */
public final class SkillLevel implements OutgoingPacket<SkillContext> {

	@Override
	public void send(SkillContext context) {
		final IoBuffer buffer = new IoBuffer(38);
		Skills skills = context.getPlayer().getSkills();
		if (context.getSkillId() == Skills.PRAYER) {
			buffer.putA((int) Math.ceil(skills.getPrayerPoints()));
		} else if (context.getSkillId() == Skills.HITPOINTS) {
			buffer.putA(skills.getLifepoints());
		} else {
			buffer.putA(skills.getLevel(context.getSkillId(), true));
		}
		buffer.putIntA((int) skills.getExperience(context.getSkillId())).put(context.getSkillId());
		context.getPlayer().getDetails().getSession().write(buffer);
	}

}