package plugin.quest;

import org.crandor.game.content.skill.Skills;
import org.crandor.game.node.entity.player.Player;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.game.node.entity.player.link.quest.Quest;

/**
 * Represents the cooks assistants quest
 * @author Vexia
 * 
 */
@InitializablePlugin
public final class CooksAssistant extends Quest {

	/**
	 * Constructs a new {@Code CooksAssistant} {@Code Object}
	 */
	public CooksAssistant() {
		super("Cook's Assistant", 15, 14, 1, 29, 0, 1, 2);
	}

	@Override
	public void drawJournal(Player player, int stage) {
		super.drawJournal(player, stage);
		player.getPacketDispatch().sendString("<col=08088A>I can start this quest by speaking to the <col=8A0808>Cook</col> <col=08088A>in the", 275, 4+ 7);
		player.getPacketDispatch().sendString("<col=8A0808>Kitchen</col> <col=08088A>on the ground for of <col=8A0808>Lumbridge Castle.<col=8A0808>", 275, 5+ 7);
		if (stage == 10) {
			player.getPacketDispatch().sendString("<col=08088A>It's the <col=8A0808>Duke of Lumbridge's</col> <col=08088A>birthday and I have to help", 275, 4+ 7);
			player.getPacketDispatch().sendString("<col=08088A>his</col> <col=8A0808>Cook</col> <col=08088A>make him a <col=8A0808>birthday cake.</col><col=08088A> To do this I need to", 275, 5+ 7);
			player.getPacketDispatch().sendString("<col=08088A>bring him the following ingredients:", 275, 6+ 7);
			if (player.getInventory().contains(1927, 1)) {
				player.getPacketDispatch().sendString("<col=08088A>I have found a <col=8A0808>bucket of milk</col><col=08088A> to give to the cook.", 275, 7+ 7);
			} else {
				player.getPacketDispatch().sendString("<col=08088A>I need to find a <col=8A0808>bucket of milk</col><col=08088A>.", 275, 7+ 7);
			}
			if (player.getInventory().contains(1933, 1)) {
				player.getPacketDispatch().sendString("<col=08088A>I have found a <col=8A0808>pot of flour</col><col=08088A> to give to the cook.", 275, 8+ 7);
			} else {
				player.getPacketDispatch().sendString("<col=08088A>I need to find a <col=8A0808>pot of flour</col><col=08088A>.", 275, 8+ 7);
			}
			if (player.getInventory().contains(1944, 1)) {
				player.getPacketDispatch().sendString("<col=08088A>I have found an <col=8A0808>egg</col><col=08088A> to give to the cook.", 275, 9+ 7);
			} else {
				player.getPacketDispatch().sendString("<col=08088A>I need to find <col=8A0808>an egg</col><col=08088A>.", 275, 9+ 7);
			}
			if (player.getSavedData().getQuestData().getCookAssist("milk")) {
				player.getPacketDispatch().sendString("<str>I have given the cook a bucket of milk.</str>", 275, 7+ 7);
			}
			if (player.getSavedData().getQuestData().getCookAssist("flour")) {
				player.getPacketDispatch().sendString("<str>I have given the cook a pot of flour.</str>", 275, 8+ 7);
			}
			if (player.getSavedData().getQuestData().getCookAssist("egg")) {
				player.getPacketDispatch().sendString("<str>I have given the cook an egg.</str>", 275, 9+ 7);// I
			}

		}
		if (stage == 100) {
			player.getPacketDispatch().sendString("<str>It was the Duke of Lumbridge's birthday,  but his cook had", 275, 4+ 7);
			player.getPacketDispatch().sendString("<str>forgotten to buy the ingredients he needed to make him a", 275, 5+ 7);
			player.getPacketDispatch().sendString("<str>cake. I brought the cook an egg, some flour and some milk", 275, 6+ 7);
			player.getPacketDispatch().sendString("<str>and then cook made a delicious looking cake with them.", 275, 7+ 7);
			player.getPacketDispatch().sendString("<str>As a reward he now lets me use his high quality range", 275, 9+ 7);
			player.getPacketDispatch().sendString("<str>which lets me burn things less whenever I wish to cook", 275, 10+ 7);
			player.getPacketDispatch().sendString("<str>there.", 275, 11+ 7);
			player.getPacketDispatch().sendString("<col=FF0000>QUEST COMPLETE!", 275, 13+ 7);
		}
	}

	@Override
	public void finish(Player player) {
		super.finish(player);
		player.getPacketDispatch().sendString("1 Quest Point", 277, 8 + 2);
		player.getPacketDispatch().sendString("300 Cooking XP", 277, 9 + 2);
		player.getSkills().addExperience(Skills.COOKING, 300);
		player.getInterfaceManager().closeChatbox();
		player.getPacketDispatch().sendItemZoomOnInterface(1891, 240, 277, 5);
	}

	@Override
	public Quest newInstance(Object object) {
		return this;
	}

}
