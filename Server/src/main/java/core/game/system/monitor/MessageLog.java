package core.game.system.monitor;

import core.cache.misc.buffer.ByteBufferUtils;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Handles message monitoring.
 * @author Emperor
 */
public class MessageLog {

	/**
	 * The date format used.
	 */
	private final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	/**
	 * The messages.
	 */
	private List<String> messages;

	/**
	 * The logging capacity.
	 */
	private final int capacity;

	/**
	 * If all messages logged should be unique.
	 */
	private boolean uniqueLogging;

	/**
	 * Constructs a new {@code MessageLog} {@code Object} without capacity and
	 * uniqueLogging mode disabled.
	 */
	public MessageLog() {
		this(-1, false);
	}

	/**
	 * Constructs a new {@code MessageLog} {@code Object} with uniqueLogging
	 * mode disabled.
	 * @param capacity The capacity (-1 for no capacity).
	 */
	public MessageLog(int capacity) {
		this(capacity, false);
	}

	/**
	 * Constructs a new {@code MessageLog} {@code Object}.
	 * @param capacity The capacity (-1 for no capacity).
	 * @param uniqueLogging If all messages logged should be unique.
	 */
	public MessageLog(int capacity, boolean uniqueLogging) {
		this.capacity = capacity;
		this.messages = new ArrayList<>(20);
		this.uniqueLogging = uniqueLogging;
	}

	/**
	 * Adds a message to the cache.
	 * @param message The message.
	 * @param timeStamp If the message should be time stamped.
	 */
	public void log(String message, boolean timeStamp) {
		if (messages.size() == capacity) {
			messages.remove(0);
		}
		if (uniqueLogging && messages.contains(message)) {
			return;
		}
		if (timeStamp) {
			StringBuilder sb = new StringBuilder(dateFormat.format(new Date()));
			message = sb.append(": ").append(message).toString();
		}
		messages.add(message);
	}

	/**
	 * Writes the logged data to the file.
	 * @param fileName The name of the file to write on.
	 */
	public void write(String fileName) {
		if (messages.isEmpty()) {
			return;
		}
		int size = messages.size();
		ByteBuffer buffer = ByteBuffer.allocate(size * 16055);
		buffer.putShort((short) size);
		for (String message : messages) {
			ByteBufferUtils.putString(message, buffer);
			buffer.put((byte) '\n');
		}
		buffer.putShort((short) -1);
		buffer.flip();
		try (RandomAccessFile raf = new RandomAccessFile(fileName, "rw")) {
			FileChannel channel = raf.getChannel();
			long pos = channel.size() - 2l;
			if (pos < 1) {
				pos = 0;
			}
			channel.write(buffer, pos);
			raf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		messages.clear();
	}

	/**
	 * Gets the messages.
	 * @return The messages.
	 */
	public List<String> getMessages() {
		return messages;
	}

	/**
	 * Sets the messages.
	 * @param messages The messages to set.
	 */
	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	/**
	 * Gets the capacity.
	 * @return The capacity.
	 */
	public int getCapacity() {
		return capacity;
	}

}