package core.game.content.activity.stronghold.playersafety;

import core.game.component.Component;
import core.game.component.ComponentPlugin;
import core.game.content.dialogue.DialogueInterpreter;
import core.game.content.dialogue.DialoguePlugin;
import core.game.node.entity.player.Player;
import core.plugin.Plugin;

/**
 * Handles the player safety test.
 * @author Tyler Telis
 * @author Vexia
 */
public class PlayerSafetyTest extends DialoguePlugin {

	/**
	 * The check box button ids.
	 */
	public static int ANSWER_1 = 3, ANSWER_2 = 4, ANSWER_3 = 12;

	/**
	 * Constructs a new {@Code PlayerSafetyTest} {@Code Object}
	 * @param player the player.
	 */
	public PlayerSafetyTest(Player player) {
		super(player);
	}

	/**
	 * Constructs a new {@code PlayerSafetyTest} instance.
	 */
	public PlayerSafetyTest() {
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new PlayerSafetyTest(player);
	}

	@Override
	public boolean open(Object... args) {
		stage = player.getAttribute("s-stage", 0);
		if (args.length >= 2) {
			handleQuiz(Integer.parseInt(String.valueOf(args[1])));
		} else {
			stage = 0;
			sendTest();
		}
		return true;
	}

	/**
	 * Sends the test.
	 */
	public void sendTest() {
		Component component = getComponentForStage(stage);
		component.setPlugin(new ComponentPlugin() {

			@Override
			public Plugin<Object> newInstance(Object arg) throws Throwable {
				return this;
			}

			@Override
			public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
				if (player == null) {
					return false;
				}
				if (player.getDialogueInterpreter().getDialogue() == null) {
					player.getDialogueInterpreter().open(DialogueInterpreter.getDialogueKey("player_safety"), "reopen", button);
				}
				if (player.getDialogueInterpreter().getDialogue() == null) {
					return false;
				}
				return player.getDialogueInterpreter().getDialogue().handle(component.getId(), button);
			}

		});
		player.getInterfaceManager().open(component);
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		return handleQuiz(buttonId);
	}

	private boolean handleQuiz(int buttonId) {
		if (buttonId == 48 || buttonId == 39 || buttonId == 40) {
			if (stage >= TestQuestions.values().length) {
				player.getInterfaceManager().close();
				player.getSavedData().getGlobalData().setTestStage(2);
				player.sendMessage("Well done! You completed the exams.");
				player.getDialogueInterpreter().sendDialogue("Congratulations! The test has been completed. Hand the paper in to", "Professor Henry for marking.");
				return false;
			} else {
				sendTest();
			}
			return true;
		}
		if (stage == TestQuestions.values().length) {// Last question. 
			player.getDialogueInterpreter().getDialogue().end();
			return true;
		}
		if (forStage(stage).answer(player, buttonId)) {
			return true;
		}
		return true;
	}

	@Override
	public boolean close() {
		player.setAttribute("s-stage", stage);
		return super.close();
	}

	/**
	 * A test question.
	 * @author Tyler Tellis
	 */
	public enum TestQuestions {
		QUESTION_1(697, ANSWER_2) {
			@Override
			public void showAnswer(Player player, boolean correct, int interfaceId) {
				player.getPacketDispatch().sendInterfaceConfig(interfaceId, 26, false);
				player.getPacketDispatch().sendInterfaceConfig(interfaceId, correct ? 37 : 43, false);
			}
		},
		QUESTION_2(699, ANSWER_2) {
			@Override
			public void showAnswer(Player player, boolean correct, int interfaceId) {
				player.getPacketDispatch().sendInterfaceConfig(interfaceId, 20, false);
				player.getPacketDispatch().sendInterfaceConfig(interfaceId, correct ? 31 : 34, false);
			}
		},
		QUESTION_3(707, ANSWER_1) {
			@Override
			public void showAnswer(Player player, boolean correct, int interfaceId) {
				player.getPacketDispatch().sendInterfaceConfig(interfaceId, 20, false);
				player.getPacketDispatch().sendInterfaceConfig(interfaceId, correct ? 31 : 35, false);
			}
		},
		QUESTION_4(710, 9) {
			@Override
			public void showAnswer(Player player, boolean correct, int interfaceId) {
				player.getPacketDispatch().sendInterfaceConfig(interfaceId, 20, false);
				player.getPacketDispatch().sendInterfaceConfig(interfaceId, correct ? 31 : 34, false);
			}
		},
		QUESTION_5(704, 10) {
			@Override
			public void showAnswer(Player player, boolean correct, int interfaceId) {
				player.getPacketDispatch().sendInterfaceConfig(interfaceId, 26, false);
				player.getPacketDispatch().sendInterfaceConfig(interfaceId, correct ? 37 : 40, false);
			}
		},
		QUESTION_6(694, 10) {
			@Override
			public void showAnswer(Player player, boolean correct, int interfaceId) {
				player.getPacketDispatch().sendInterfaceConfig(interfaceId, 20, false);
				player.getPacketDispatch().sendInterfaceConfig(interfaceId, correct ? 31 : 34, false);
			}
		},
		QUESTION_8(696, 4) {
			@Override
			public void showAnswer(Player player, boolean correct, int interfaceId) {
				player.getPacketDispatch().sendInterfaceConfig(interfaceId, 20, false);
				player.getPacketDispatch().sendInterfaceConfig(interfaceId, correct ? 31 : 34, false);
			}
		};

		/**
		 * The interface id.
		 */
		private int interfaceId;

		/**
		 * The correct button id to click.
		 */
		private int correctButton;

		/**
		 * Constructs a new {@Code TestQuestions} {@Code Object}
		 * @param interfaceId the interface.
		 * @param correctButton the button.
		 */
		private TestQuestions(int interfaceId, int correctButton) {
			this.interfaceId = interfaceId;
			this.correctButton = correctButton;
		}

		/**
		 * Answers a question.
		 * @param player the player.
		 * @param buttonId the button id.
		 * @return {@code True} if so.
		 */
		public boolean answer(Player player, int buttonId) {
			boolean correct = buttonId == correctButton;
			if (interfaceId == 710 && buttonId == 9) {
				correct = true;
			} else if (interfaceId == 704 && buttonId == 10) {
				correct = true;
			} else if (interfaceId == 696 && buttonId == 4) {
				correct = true;
			}
			showAnswer(player, correct, interfaceId);
			if (correct) {
				player.getDialogueInterpreter().getDialogue().increment();
			}
			return player.getInterfaceManager().getComponent(interfaceId) != null;
		}

		/**
		 * Shows the answer.
		 * @param player the player.
		 * @param correct the correct.
		 * @param interfaceId the id.
		 */
		public abstract void showAnswer(Player player, boolean correct, int interfaceId);

		/**
		 * Gets the value.
		 * @return the value.
		 */
		public int value() {
			return ordinal();
		}

		/**
		 * Gets the inter id.
		 * @return the id.
		 */
		public int getInterfaceId() {
			return interfaceId;
		}

		/**
		 * Gets the correct button.
		 * @return the button.
		 */
		public int getCorrectButton() {
			return correctButton;
		}
	}

	@Override
	public int[] getIds() {
		return new int[] { DialogueInterpreter.getDialogueKey("player_safety") };
	}

	/**
	 * Gets a new component for the stage.
	 * @param stage The stage;
	 * @return The {@code Component} instance.
	 */
	public static Component getComponentForStage(int stage) {
		return new Component(forStage(stage).getInterfaceId());
	}

	/**
	 * Gets it for the stage.
	 * @param stage the stage.
	 * @return the question.
	 */
	public static TestQuestions forStage(int stage) {
		return TestQuestions.values()[stage];
	}
}
