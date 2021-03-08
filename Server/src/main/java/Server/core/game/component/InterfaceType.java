package core.game.component;

import static core.tools.Interfaces.*;

/**
 * Represents an interface type.
 * @author Emperor
 *
 */
public enum InterfaceType {

	/**
	 * Default interface.
	 */
	DEFAULT(TOPLEVEL_548, TOPLEVEL_FULLSCREEN_746, 11, 6),

	/**
	 * Walkable interface.
	 */
	OVERLAY(TOPLEVEL_548, TOPLEVEL_FULLSCREEN_746, 4, 5),

	/**
	 * A tab interface.
	 */
	TAB(TOPLEVEL_548, TOPLEVEL_FULLSCREEN_746, 83, 93),

	/**
	 * The only tab to be shown (when this type is opened).
	 */
	SINGLE_TAB(TOPLEVEL_548, TOPLEVEL_FULLSCREEN_746, 80, 76),

	/**
	 * Chatbox dialogue interface.
	 */
	DIALOGUE(CHATTOP_752, CHATTOP_752, 12, 12),

	/**
	 * A window pane.
	 */
	WINDOW_PANE(TOPLEVEL_548, TOPLEVEL_FULLSCREEN_746, 0, 0),

	/**
	 * Client script chatbox interface.
	 */
	CS_CHATBOX(CHATTOP_752, CHATTOP_752, 6, 6),

	/**
	 * Chatbox interface.
	 */
	CHATBOX(CHATTOP_752, CHATTOP_752, 8, 8),

    /**
     * Wilderness overlay
     */
    WILDERNESS_OVERLAY(TOPLEVEL_548, TOPLEVEL_FULLSCREEN_746, 11, 3);

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