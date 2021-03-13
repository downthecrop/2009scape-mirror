package core.game.content.activity.barrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import core.game.component.Component;
import core.game.component.ComponentDefinition;
import core.game.component.ComponentPlugin;
import core.game.node.entity.player.Player;
import core.net.packet.PacketRepository;
import core.net.packet.context.DisplayModelContext;
import core.net.packet.context.DisplayModelContext.ModelType;
import core.net.packet.out.DisplayModel;
import core.plugin.Plugin;
import core.tools.RandomFunction;

/**
 * Handles the barrows puzzle.
 * @author Emperor
 */
public final class BarrowsPuzzle extends ComponentPlugin {

	/**
	 * The shapes puzzle.
	 */
	public static final BarrowsPuzzle SHAPES = new BarrowsPuzzle(new int[] { 6734, 6735, 6736 }, getAnswerModel(6731, true), getAnswerModel(6732, false), getAnswerModel(6733, false));

	/**
	 * The lines puzzle.
	 */
	public static final BarrowsPuzzle LINES = new BarrowsPuzzle(new int[] { 6728, 6729, 6730 }, getAnswerModel(6725, true), getAnswerModel(6726, false), getAnswerModel(6727, false));

	/**
	 * The squares puzzle.
	 */
	public static final BarrowsPuzzle SQUARES = new BarrowsPuzzle(new int[] { 6722, 6723, 6724 }, getAnswerModel(6719, true), getAnswerModel(6720, false), getAnswerModel(6721, false));

	/**
	 * The triangle-on-circles puzzle.
	 */
	public static final BarrowsPuzzle TRIANGLE_CIRCLES = new BarrowsPuzzle(new int[] { 6716, 6717, 6718 }, getAnswerModel(6713, true), getAnswerModel(6714, false), getAnswerModel(6715, false));

	/**
	 * The puzzle component.
	 */
	private static final Component COMPONENT = new Component(25);

	/**
	 * The question models.
	 */
	private final int[] questionModels;

	/**
	 * The answer models.
	 */
	private final int[] answerModels;

	/**
	 * Constructs a new {@code BarrowsPuzzle} {@code Object}.
	 * @param questionModels The question models.
	 * @param answerModels The answer models.
	 */
	private BarrowsPuzzle(int[] questionModels, int... answerModels) {
		this.questionModels = questionModels;
		this.answerModels = answerModels;
	}

	/**
	 * Creates a new barrows puzzle instance of this puzzle.
	 * @return The new barrows puzzle instance.
	 */
	public BarrowsPuzzle create() {
		int[] answers = Arrays.copyOf(answerModels, answerModels.length);
		List<Integer> list = new ArrayList<>(20);
		for (int answer : answers) {
			list.add(answer);
		}
		Collections.shuffle(list, new Random());
		for (int i = 0; i < list.size(); i++) {
			answers[i] = list.get(i);
		}
		return new BarrowsPuzzle(questionModels, answers);
	}

	/**
	 * Opens a random barrows puzzle.
	 * @param player The player.
	 */
	public static void open(Player player) {
		int index = RandomFunction.random(4);
		if (index == player.getAttribute("puzzle:index", -1)) {
			index = (index + 1) % 4;
		}
		open(player, index);
	}

	/**
	 * Opens the barrows puzzle for the given index.
	 * @param player The player.
	 * @param index The index (0 = shapes, 1 = lines, 2 = squares, 3 =
	 * triangle-on-circle).
	 */
	public static void open(Player player, int index) {
		BarrowsPuzzle puzzle = SHAPES;
		switch (index) {
		case 1:
			puzzle = LINES;
			break;
		case 2:
			puzzle = SQUARES;
			break;
		case 3:
			puzzle = TRIANGLE_CIRCLES;
			break;
		}
		puzzle = puzzle.create();
		player.setAttribute("puzzle:index", index);
		player.setAttribute("puzzle:answers", puzzle.answerModels);
		player.getInterfaceManager().open(COMPONENT);
		for (int i = 0; i < puzzle.questionModels.length; i++) {
			PacketRepository.send(DisplayModel.class, new DisplayModelContext(player, ModelType.MODEL, puzzle.questionModels[i], 0, 25, 6 + i));
		}
		for (int i = 0; i < puzzle.answerModels.length; i++) {
			PacketRepository.send(DisplayModel.class, new DisplayModelContext(player, ModelType.MODEL, puzzle.answerModels[i] & 0xFFFF, 0, 25, 2 + i));
		}
		PacketRepository.send(DisplayModel.class, new DisplayModelContext(player, ModelType.MODEL, puzzle.answerModels[2] & 0xFFFF, 0, 25, 5));
	}

	/**
	 * Gets the answer model id.
	 * @param modelId The model id.
	 * @param correct If the answer is correct.
	 * @return The model id hash.
	 */
	private static int getAnswerModel(int modelId, boolean correct) {
		return modelId | (correct ? 1 : 0) << 16;
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ComponentDefinition.put(25, this);
		return this;
	}

	@Override
	public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
		switch (button) {
		case 2:
		case 3:
		case 5:
			player.getInterfaceManager().close();
			boolean correct = ((player.getAttribute("puzzle:answers", new int[3])[button == 5 ? 2 : button - 2] >> 16) & 0xFF) == 1;
			if (!correct) {
				player.getPacketDispatch().sendMessage("You got the puzzle wrong! You can hear the catacombs moving around you.");
				BarrowsActivityPlugin.shuffleCatacombs(player);
				break;
			}
			player.setAttribute("/save:barrow:solvedpuzzle", true);
			player.getPacketDispatch().sendMessage("You hear the doors' locking mechanism grind open.");
			break;
		default:
			return false;
		}
		return true;
	}
}