package core.game.node.entity.player.link.appearance;

import core.game.node.entity.player.info.login.SavingModule;
import org.json.simple.JSONObject;

import java.nio.ByteBuffer;

/**
 * Represents a body part of a player encapsulating the type and color.
 * @author 'Vexia
 */
public final class BodyPart implements SavingModule {

	/**
	 * Represents the look value of the part.
	 */
	private int look;

	/**
	 * Represents the color of this part.
	 */
	private int color;

	/**
	 * Constructs a new {@code BodyPart} {@code Object}.
	 */
	public BodyPart() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code BodyPart} {@code Object}.
	 * @param look the look.
	 * @param color the color.
	 */
	public BodyPart(final int look, final int color) {
		this.look = look;
		this.color = color;
	}

	/**
	 * Constructs a new {@code BodyPart} {@code Object}.
	 * @param look the look.
	 */
	public BodyPart(final int look) {
		this(look, 0);
	}

	@Override
	public void save(ByteBuffer buffer) {
		buffer.putInt(look);
		buffer.putInt(color);
	}

	public void parse(JSONObject part){
		changeLook(Integer.parseInt( part.get("look").toString()));
		changeColor(Integer.parseInt( part.get("color").toString()));
	}

	@Override
	public void parse(ByteBuffer buffer) {
		changeLook(buffer.getInt());
		changeColor(buffer.getInt());
	}

	/**
	 * Method used to change the look value.
	 * @param look the look value.
	 */
	public void changeLook(final int look) {
		this.look = look;
	}

	/**
	 * Method used to change the color value.
	 * @param color the color value.
	 */
	public void changeColor(final int color) {
		this.color = color;
	}

	/**
	 * Gets the look.
	 * @return The look.
	 */
	public int getLook() {
		return look;
	}

	/**
	 * Gets the color.
	 * @return The color.
	 */
	public int getColor() {
		return color;
	}

}
