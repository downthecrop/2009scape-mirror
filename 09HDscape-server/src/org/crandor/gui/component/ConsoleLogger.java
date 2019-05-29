package org.crandor.gui.component;

import java.io.PrintStream;

import org.crandor.gui.KeldagrimFrame;

/**
 * Logs output to the gui console.
 * @author Vexia
 */
public class ConsoleLogger extends PrintStream {

	/**
	 * Constructs a new {@code WorkLogger} {@code Object}
	 * @param stream the stream.
	 */
	public ConsoleLogger(PrintStream stream) {
		super(stream);
	}

	@Override
	public void println(String message) {
		log(message);
	}

	@Override
	public PrintStream printf(String message, Object... objects) {
		return null;
	}

	@Override
	public void println(boolean message) {
		log(String.valueOf(message));
	}

	@Override
	public void println(int message) {
		log(String.valueOf(message));
	}

	@Override
	public void println(double message) {
		log(String.valueOf(message));
	}

	@Override
	public void println(char message) {
		log(Character.toString(message));
	}

	@Override
	public void println(long message) {
		log(Long.toString(message));
	}

	@Override
	public void println(Object message) {
		log(String.valueOf(message));
	}

	/**
	 * Method used to log the message.
	 * @param message the message.
	 */
	public void log(final String message) {
		KeldagrimFrame.getInstance().getStatisticsTab().log(message);
	}

}
