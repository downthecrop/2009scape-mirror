package core.net.registry;

import core.cache.misc.buffer.ByteBufferUtils;
import core.game.system.task.Pulse;
import core.net.Constants;
import core.net.IoSession;
import rs09.ServerConstants;
import rs09.auth.UserAccountInfo;
import rs09.game.system.SystemLogger;
import rs09.game.world.GameWorld;
import rs09.net.event.LoginReadEvent;
import rs09.net.packet.in.Login;

import java.nio.ByteBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles the registry of new accounts.
 * @author Vexia
 *
 */
public class AccountRegister {
	/**
	 * The pattern compiler.
	 */
	private static final Pattern PATTERN = Pattern.compile("[a-z0-9_]{1,12}");

	/**
	 * Reads the incoming opcode of an account register.
	 * @param session the session.
	 * @param opcode the opcode.
	 * @param buffer the buffer.
	 */
	public static void read(final IoSession session, int opcode, ByteBuffer buffer) {
		int day,month,year,country;
		UserAccountInfo info = UserAccountInfo.createDefault();
		switch (opcode) {
			case 147://details
				day = buffer.get();
				month = buffer.get();
				year = buffer.getShort();
				country = buffer.getShort();
				response(session, RegistryResponse.SUCCESS);
				break;
			case 186://username
				final String username = ByteBufferUtils.getString(buffer).replace(" ", "_").toLowerCase().replace("|", "");
				info.setUsername(username);
				if (username.length() <= 0 || username.length() > 12) {
					response(session, RegistryResponse.INVALID_USERNAME);
					break;
				}
				if (invalidUsername(username)) {
					System.out.println("AHAHHA " + username);
					response(session,RegistryResponse.INVALID_USERNAME);
					break;
				}
				System.out.println(username);
				if (!GameWorld.getAuthenticator().canCreateAccountWith(info)) {
					response(session, RegistryResponse.NOT_AVAILBLE_USER);
					return;
				}
				response(session, RegistryResponse.SUCCESS);
				break;
			case 36://Register details
				SystemLogger.logInfo(AccountRegister.class, "Made it to final stage");
				buffer.get(); //Useless size being written that is already written in the RSA block
				buffer = Login.decryptRSABuffer(buffer, ServerConstants.EXPONENT, ServerConstants.MODULUS);
				if(buffer.get() != 10){ //RSA header (aka did this decrypt properly)
					SystemLogger.logInfo(AccountRegister.class, "Decryption failed during registration :(");
					response(session, RegistryResponse.CANNOT_CREATE);
					break;
				}
				buffer.getShort(); // random data
				int revision = buffer.getShort();//revision?
				if (revision != Constants.REVISION) {
					response(session, RegistryResponse.CANNOT_CREATE);
					break;
				}
				final String name = ByteBufferUtils.getString(buffer).replace(" ", "_").toLowerCase().replace("|", "");
				buffer.getInt();
				String password = ByteBufferUtils.getString(buffer);
				info.setUsername(name);
				info.setPassword(password);
				if (password.length() < 5 || password.length() > 20) {
					response(session, RegistryResponse.INVALID_PASS_LENGTH);
					break;
				}
				if (password.equals(name)) {
					response(session, RegistryResponse.PASS_SIMILAR_TO_USER);
					break;
				}
				if (invalidUsername(name)) {
					response(session, RegistryResponse.INVALID_USERNAME);
					break;
				}
				buffer.getInt();
				buffer.getShort();
				day = buffer.get();
				month = buffer.get();
				buffer.getInt();
				year = buffer.getShort();
				country = buffer.getShort();
				buffer.getInt();
				if (!GameWorld.getAuthenticator().canCreateAccountWith(info)) {
					response(session, RegistryResponse.CANNOT_CREATE);
					return;
				}
				GameWorld.getAuthenticator().createAccountWith(info);
				GameWorld.getPulser().submit(new Pulse() {
					@Override
					public boolean pulse() {
						response(session, RegistryResponse.SUCCESS);
						return true;
					}
				});
				break;
			default:
				SystemLogger.logErr(AccountRegister.class, "Unhandled account registry opcode = " + opcode);
				break;
		}
	}

	/**
	 * Sends a registry response code.
	 * @param response the response.
	 */
	private static void response(IoSession session, RegistryResponse response) {
		ByteBuffer buf = ByteBuffer.allocate(100);
		buf.put((byte) response.getId());
		session.queue(buf.flip());
	}

	/**
	 * Checks if a username is valid.
	 * @return {@code True} if so.
	 */
	public static boolean invalidUsername(final String username) {
		Matcher matcher = PATTERN.matcher(username);
		return !matcher.matches();
	}
}
