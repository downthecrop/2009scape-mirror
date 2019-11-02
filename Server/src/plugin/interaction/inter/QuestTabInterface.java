package plugin.interaction.inter;

import org.crandor.game.component.Component;
import org.crandor.game.component.ComponentDefinition;
import org.crandor.game.component.ComponentPlugin;
import org.crandor.game.content.global.tutorial.TutorialSession;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.diary.AchievementDiary;
import org.crandor.game.node.entity.player.link.diary.DiaryType;
import org.crandor.game.node.entity.player.link.quest.Quest;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Handles the quest tab reward buttons.
 * @author Emperor
 * @author Vexia
 */
@InitializablePlugin
public class QuestTabInterface extends ComponentPlugin {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ComponentDefinition.put(274, this); // Quests
		ComponentDefinition.put(259, this); // Achievement diary
		return this;
	}

	@Override
	public boolean handle(Player p, Component component, int opcode, int button, int slot, int itemId) {
		if (TutorialSession.getExtension(p).getStage() < TutorialSession.MAX_STAGE) {
			return true;
		}
		p.getPulseManager().clear();
		switch (component.getId()) {
			case 274:
//				if (!GameWorld.isEconomyWorld()) {
//					p.getSavedData().getSpawnData().handleButton(p, button);
//				}
				switch (button) {
					case 3:
						p.getAchievementDiaryManager().openTab();
						return true;
					case 10:
						break;
					default:
//						if (GameWorld.isEconomyWorld()) {
							Quest quest = p.getQuestRepository().forButtonId(button);
							if (quest != null) {
								p.getInterfaceManager().open(new Component(275));
								quest.drawJournal(p, quest.getStage(p));
								return true;
							}
//						}
						return false;
				}
				break;
			case 259:
				switch (button) {
					case 8:
						p.getInterfaceManager().openTab(2, new Component(274));
						return true;
					default:
						AchievementDiary diary = p.getAchievementDiaryManager().getDiary(DiaryType.forChild(button));
						if (diary != null) {
							diary.open(p);
						}
						return true;
				}
		}
		return true;
	}

}