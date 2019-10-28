package org.crandor.game.world.callback;

/**
 * Represents a call back which is used in {@link CallbackHub} to load any
 * neccesary details used on the starting of a {@link World}.
 * @author 'Vexia
 */
public interface CallBack {

	/**
	 * Method used to call anything related to this {@link CallBack}.
	 * @return <code>True</code> if succesfull with calling, and
	 * <code>False</code> if not.
	 */
	public boolean call();
}
