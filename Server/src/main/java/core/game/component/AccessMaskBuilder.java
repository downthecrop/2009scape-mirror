package core.game.component;

/**
 * Used to generate component option settings.
 * @author Mangis
 */
public final class AccessMaskBuilder {

	/**
	 * Contains the value which should be sent in access mask packet.
	 */
	private int value;

	/**
	 * Sets default option setting.
	 * @param allowed If the packet for the default option should be sent to the
	 * server.
	 */
	public void allowDefaultOption(boolean allowed) {
		value &= ~(0x1);
		if (allowed) {
			value |= 0x1;
		}
	}

	/**
	 * Sets right click option settings. If specified option is not allowed, it
	 * will not appear in right click menu and the packet will not be send to
	 * server when clicked.
	 * @param optionId The option index.
	 * @param show If the option is allowed.
	 */
	public void showMenuOption(int optionId, boolean show) {
		if (optionId < 0 || optionId > 9) {
			throw new IllegalArgumentException("Option index must be 0-9.");
		}
		value &= ~(0x1 << (optionId + 1));
		if (show) {
			value |= (0x1 << (optionId + 1));
		}
	}

	/**
	 * Sets use on option settings. If nothing is allowed then 'use' option will
	 * not appear in right click menu.
	 */
	public void setUseOnSettings(boolean groundItems, boolean npcs, boolean objects, boolean otherPlayer, boolean selfPlayer, boolean component) {
		int useFlag = 0;
		if (groundItems) {
			useFlag |= 0x1;
		}
		if (npcs) {
			useFlag |= 0x2;
		}
		if (objects) {
			useFlag |= 0x4;
		}
		if (otherPlayer) {
			useFlag |= 0x8;
		}
		if (selfPlayer) {
			useFlag |= 0x10;
		}
		if (component) {
			useFlag |= 0x20;
		}
		value &= ~(127 << 7); // disable
		value |= useFlag << 7;
	}

	/**
	 * Sets interface events depth. For example, we have inventory interface
	 * which is opened on gameframe interface (548) If depth is 1, then the
	 * clicks in inventory will also invoke click event handler scripts on
	 * gameframe interface.
	 * @param depth The depth value.
	 */
	public void setInterfaceEventsDepth(int depth) {
		if (depth < 0 || depth > 7) {
			throw new IllegalArgumentException("depth must be 0-7.");
		}
		value &= ~(0x7 << 18);
		value |= (depth << 18);
	}

	/**
	 * Flags other component options being allowed to be used on this component.
	 * @param allow If an option can be used on this component.
	 */
	public void allowUsage(boolean allow) {
		value &= ~(1 << 22);
		if (allow) {
			value |= (1 << 22);
		}
	}

	/**
	 * Gets the current value.
	 * @return The value.
	 */
	public int getValue() {
		return value;
	}
}