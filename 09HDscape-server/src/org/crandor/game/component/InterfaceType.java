package org.crandor.game.component;

/**
 * Represents an interface type.
 * @author Emperor
 *
 */
public enum InterfaceType {

	/**
	 * Default interface.
	 */
	DEFAULT(548, 746, 11, 6),

	/**
	 * Walkable interface.
	 */
	OVERLAY(548, 746, 5, 3),

	/**
	 * A tab interface.
	 */
	TAB(548, 746, 83, 93),

	/**
	 * The only tab to be shown (when this type is opened).
	 */
	SINGLE_TAB(548, 746, 80, 76),

	/**
	 * Chatbox dialogue interface.
	 */
	DIALOGUE(752, 752, 12, 12),

	/**
	 * A window pane.
	 */
	WINDOW_PANE(548, 746, 0, 0), 

	/**
	 * Client script chatbox interface.
	 */
	CS_CHATBOX(752, 752, 6, 6),

	/**
	 * Chatbox interface.
	 */
	CHATBOX(752, 752, 8, 8),;

	/**
	 * The fixed window pane id.
	 */
	private final int fixedPaneId;

	/**
	 * The resizable window pane id.
	 */
	private final int resizablePaneId;

	/**
	 * The fixed child id.
	 */
	private final int fixedChildId;

	/**
	 * The resizable child id.
	 */
	private final int resizableChildId;

	/**
	 * Constructs a new {@Code InterfaceType} {@Code Object}
	 * @param fixedPaneId The fixed window pane id.
	 * @param resizablePaneId The resizable window pane id.
	 * @param fixedChildId The fixed child id.
	 * @param resizableChildId The resizable child id.
	 */
	private InterfaceType(int fixedPaneId, int resizablePaneId, int fixedChildId, int resizableChildId) {
		this.fixedPaneId = fixedPaneId;
		this.resizablePaneId = resizablePaneId;
		this.fixedChildId = fixedChildId;
		this.resizableChildId = resizableChildId;
	}

	/**
	 * Gets the fixedPaneId.
	 * @return the fixedPaneId
	 */
	public int getFixedPaneId() {
		return fixedPaneId;
	}

	/**
	 * Gets the resizablePaneId.
	 * @return the resizablePaneId
	 */
	public int getResizablePaneId() {
		return resizablePaneId;
	}

	/**
	 * Gets the fixedChildId.
	 * @return the fixedChildId
	 */
	public int getFixedChildId() {
		return fixedChildId;
	}

	/**
	 * Gets the resizableChildId.
	 * @return the resizableChildId
	 */
	public int getResizableChildId() {
		return resizableChildId;
	}
}