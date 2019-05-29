package org.crandor.game.content.dialogue;

import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.net.packet.PacketRepository;
import org.crandor.net.packet.context.ChildPositionContext;
import org.crandor.net.packet.out.RepositionChild;
import org.crandor.tools.StringUtils;

/**
 * Represents a skill dialogue handler class.
 * @author Vexia
 */
public class SkillDialogueHandler {

	/**
	 * Represents the skill dialogue id.
	 */
	public static final int SKILL_DIALOGUE = 3 << 16;

	/**
	 * Represents the player.
	 */
	private final Player player;

	/**
	 * Represents the skill dialogue type.
	 */
	private final SkillDialogue type;

	/**
	 * Represents the object data passed through.
	 */
	private final Object[] data;

	/**
	 * Constructs a new {@code SkillDialogueHandler} {@code Object}.
	 * @param player the player.
	 * @param type the type.
	 * @param data the data.
	 */
	public SkillDialogueHandler(final Player player, final SkillDialogue type, final Object... data) {
		this.player = player;
		this.type = type;
		this.data = data;
	}

	/**
	 * Method used to open a skill dialogue.
	 */
	public void open() {
		player.getDialogueInterpreter().open(SKILL_DIALOGUE, this);
	}

	/**
	 * Method used to display the content on the dialogue.
	 */
	public void display() {
		if (type == null) {
			player.debug("Error! Type is null.");
			return;
		}
		type.display(player, this);
	}

	/**
	 * Method used to create a product.
	 * @param amount the amount.
	 * @param index the index.
	 */
	public void create(final int amount, int index) {
	}

	/**
	 * Gets the total amount of items to be made.
	 * @param index the index.
	 * @return the amount.
	 */
	public int getAll(int index) {
		return 0;
	}

	/**
	 * Gets the player.
	 * @return The player.
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets the type.
	 * @return The type.
	 */
	public SkillDialogue getType() {
		return type;
	}

	/**
	 * Gets the passed data.
	 * @return the data.
	 */
	public Object[] getData() {
		return data;
	}

	/**
	 * Gets the name.
	 * @param item the item.
	 * @return the name.
	 */
	protected String getName(Item item) {
		return StringUtils.formatDisplayName(item.getName().replace("Unfired", ""));
	}

	/**
	 * Represents a skill dialogue type.
	 * @author 'Vexia
	 */
	public static enum SkillDialogue {
		ONE_OPTION(309, 5, 1) {

			@Override
			public void display(Player player, SkillDialogueHandler handler) {
				final Item item = (Item) handler.getData()[0];
				player.getPacketDispatch().sendString("<br><br><br><br>" + item.getName(), 309, 6);
				player.getPacketDispatch().sendItemZoomOnInterface(item.getId(), 160, 309, 2);
				PacketRepository.send(RepositionChild.class, new ChildPositionContext(player, 309, 6, 60, 20));
				PacketRepository.send(RepositionChild.class, new ChildPositionContext(player, 309, 2, 210, 30));
			}

			@Override
			public int getAmount(SkillDialogueHandler handler, final int buttonId) {
				return buttonId == 5 ? 1 : buttonId == 4 ? 5 : buttonId == 3 ? -1 : handler.getAll(getIndex(handler, buttonId));
			}
		},
		TWO_OPTION(303, 7, 2) {

			@Override
			public void display(Player player, SkillDialogueHandler handler) {
				Item item;
				player.getInterfaceManager().openChatbox(306);
				for (int i = 0; i < handler.getData().length; i++) {
					item = (Item) handler.getData()[i];
					player.getPacketDispatch().sendString("<br><br><br><br>" + handler.getName(item), 303, 7 + i);
					player.getPacketDispatch().sendItemZoomOnInterface(item.getId(), 160, 303, 2 + i);
				}
			}
			
			@Override
			public int getIndex(SkillDialogueHandler handler, final int buttonId) {
				switch(buttonId){
				case 6:
				case 5:
				case 4:
				case 3:
					return 0;
				case 10:
				case 9:
				case 8:
				case 7:
					return 1;
				}
				return 1;
			}
			@Override
			public int getAmount(SkillDialogueHandler handler, final int buttonId) {
				switch(buttonId){
				case 6:
				case 10:
					return 1;
				case 5:
				case 9:
					return 5;
				case 4:
				case 8:
					return 10;
				case 3:
				case 7:
					return -1;
				}
				return 1;
			}
		},
		THREE_OPTION(304, 8, 3) {
			@Override
			public void display(Player player, SkillDialogueHandler handler) {
				Item item = null;
				for (int i = 0; i < 3; i++) {
					item = (Item) handler.getData()[i];
					player.getPacketDispatch().sendItemZoomOnInterface(item.getId(), 135, 304, 2 + i);
					player.getPacketDispatch().sendString("<br><br><br><br>" + item.getName(), 304, (304 - 296) + (i * 4));
				}
			}

			@Override
			public int getIndex(SkillDialogueHandler handler, final int buttonId) {
				switch(buttonId){
				case 7:
				case 6:
				case 5:
				case 4:
					return 0;
				case 11:
				case 10:
				case 9:
				case 8:
					return 1;
				case 15:
				case 14:
				case 13:
				case 12:
					return 2;
				}
				return 1;
			}
			
			@Override
			public int getAmount(SkillDialogueHandler handler, final int buttonId) {
				switch(buttonId){
				case 7:
				case 11:
				case 15:
					return 1;
				case 6:
				case 10:
				case 14:
					return 5;
				case 5:
				case 9:
				case 13:
					return 10;
				case 4:
				case 8:
				case 12:
					return -1;
				}
				return 1;
			}
		},
		FOUR_OPTION(305, 9, 4) {
			@Override
			public void display(Player player, SkillDialogueHandler handler) {
				Item item = null;
				for (int i = 0; i < 4; i++) {
					item = (Item) handler.getData()[i];
					player.getPacketDispatch().sendItemZoomOnInterface(item.getId(), 135, 305, 2 + i);
					player.getPacketDispatch().sendString("<br><br><br><br>" + item.getName(), 305, (305 - 296) + (i * 4));
				}
			}
			
			@Override
			public int getIndex(SkillDialogueHandler handler, final int buttonId) {
				switch(buttonId){
				case 5:
				case 8:
				case 6:
				case 7:
					return 0;
				case 9:
				case 10:
				case 11:
				case 12:
					return 1;
				case 13:
				case 14:
				case 15:
				case 16:
					return 2;
				case 17:
				case 18:
				case 19:
				case 20:
					return 3;
				}
				return 0;
			}
			
			@Override
			public int getAmount(SkillDialogueHandler handler, final int buttonId) {
				switch(buttonId){
				case 8:
				case 12:
				case 16:
				case 20:
					return 1;
				case 7:
				case 11:
				case 15:
				case 19:
					return 5;
				case 6:
				case 10:
				case 14:
				case 18:
					return 5;
				case 5:
				case 9:
				case 13:
				case 17:
					return -1;
				}
				return 1;
			}
			
		},
		FIVE_OPTION(306, 7, 5) {

			/**
			 * Represents the position data.
			 */
			private final int[][] positions = new int[][] { { 10, 30 }, { 117, 10 }, { 217, 20 }, { 317, 15 }, { 408, 15 } };

			@Override
			public void display(Player player, SkillDialogueHandler handler) {
				Item item;
				player.getInterfaceManager().openChatbox(306);
				for (int i = 0; i < handler.getData().length; i++) {
					item = (Item) handler.getData()[i];
					player.getPacketDispatch().sendString("<br><br><br><br>" + handler.getName(item), 306, 10 + (4 * i));
					player.getPacketDispatch().sendItemZoomOnInterface(item.getId(), 160, 306, 2 + i);
					PacketRepository.send(RepositionChild.class, new ChildPositionContext(player, 306, 2 + i, positions[i][0], positions[i][1]));
				}
			}
			@Override
			public int getIndex(SkillDialogueHandler handler, final int buttonId) {
				switch(buttonId){
				case 9:
				case 8:
				case 7:
				case 6:
					return 0;
				case 13:
				case 12:
				case 11:
				case 10:
					return 1;
				case 17:
				case 16:
				case 15:
				case 14:
					return 2;
				case 21:
				case 20:
				case 19:
				case 18:
					return 3;
				case 25:
				case 24:
				case 23:
				case 22:
					return 4;
				}
				return 0;
			}
			
			@Override
			public int getAmount(SkillDialogueHandler handler, final int buttonId) {
				switch(buttonId){
				case 9:
				case 13:
				case 17:
				case 21:
				case 25:
					return 1;
				case 8:
				case 12:
				case 16:
				case 20:
				case 24:
					return 5;
				case 7:
				case 11:
				case 15:
				case 19:
				case 23:
					return 10;
				case 6:
				case 10:
				case 14:
				case 18:
				case 22:
					return -1;
				}
				return 1;
			}
		};

		/**
		 * Represents the interface id.
		 */
		private final int interfaceId;

		/**
		 * Represents the base button.
		 */
		private final int baseButton;

		/**
		 * Represents the length.
		 */
		private final int length;

		/**
		 * Constructs a new {@code SkillDialogue} {@code Object}.
		 * @param interfaceId the interface id.
		 * @param base the base button.
		 * @param length the length.
		 */
		private SkillDialogue(final int interfaceId, final int baseButton, final int length) {
			this.interfaceId = interfaceId;
			this.baseButton = baseButton;
			this.length = length;
		}

		/**
		 * Method used to display the content for this type.
		 * @param player the player.
		 * @param handler the handler.
		 */
		public void display(final Player player, final SkillDialogueHandler handler) {

		}

		/**
		 * Gets the amount.
		 * @param handler the handler.
		 * @param buttonId the buttonId.
		 * @return the amount.
		 */
		public int getAmount(SkillDialogueHandler handler, final int buttonId) {
			for (int k = 0; k < 4; k++) {
				for (int i = 0; i < length; i++) {
					 int val = (baseButton - k) + (4 * i);
					if (val == buttonId) {
						return k == 13 ? 1 : k == 8 ? 5 : k == 7 ? 10 : 6;
					}
				}
			}
			return -1;
		}

		/**
		 * Gets the index selected.
		 * @param handler the handler.
		 * @param buttonId the buttonId.
		 * @return the index selected.
		 */
		public int getIndex(SkillDialogueHandler handler, final int buttonId) {
			int index = 0;
			for (int k = 0; k < 4; k++) {
				for (int i = 1; i < length; i++) {
					int val = (baseButton + k) + (4 * i);
					if (val == buttonId) {
						return index + 1;
					} else if (val <= buttonId) {
						index++;
					}
				}
				index = 0;
			}
			return index;
		}

		/**
		 * Gets the interfaceId.
		 * @return The interfaceId.
		 */
		public int getInterfaceId() {
			return interfaceId;
		}

		/**
		 * Gets the type for the length.
		 * @param length2 the length to compare.
		 * @return the type.
		 */
		public static SkillDialogue forLength(int length2) {
			for (SkillDialogue dial : values()) {
				if (dial.length == length2) {
					return dial;
				}
			}
			return null;
		}

	}

}
