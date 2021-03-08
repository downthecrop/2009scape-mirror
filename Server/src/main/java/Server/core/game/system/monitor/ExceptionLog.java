package core.game.system.monitor;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Handles exception logging.
 * @author Emperor
 */
public final class ExceptionLog extends PrintStream {

	/**
	 * The logger.
	 */
	private final PrintStream logger;

	/**
	 * Constructs a new {@code ExceptionLog} {@code Object}
	 * @param file The output file directory.
	 * @throws IOException When an I/O exception occurs.
	 */
	public ExceptionLog(String file) throws IOException {
		super(new File(file));
		this.logger = System.err;
	}

	@Override
	public void println(String message) {
		logger.println(message);
	}

	@Override
	public PrintStream printf(String message, Object... objects) {
		logger.printf(message, objects);
		return super.printf(message, objects);
	}

	@Override
	public void println(boolean message) {
		logger.println(message);
	}

	@Override
	public void println(int message) {
		logger.println(message);
	}

	@Override
	public void println(double message) {
		logger.println(message);
	}

	@Override
	public void println(char message) {
		logger.println(message);
	}

	@Override
	public void println(long message) {
		logger.println(message);
	}

	@Override
	public void println(Object message) {
		logger.println(message);
	}

	@Override
	public void print(boolean message) {
		logger.print(message);
	}

	@Override
	public void print(int message) {
		logger.print(message);
	}

	@Override
	public void print(double message) {
		logger.print(message);
	}

	@Override
	public void print(char message) {
		logger.print(message);
	}

	@Override
	public void print(long message) {
		logger.print(message);
	}

	@Override
	public void print(Object message) {
		logger.print(message);
	}
}