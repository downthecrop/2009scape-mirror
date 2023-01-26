package core.game.world.update.flag.player;

import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.appearance.Appearance;
import core.game.node.entity.player.link.appearance.BodyPart;
import core.game.world.update.flag.UpdateFlag;
import core.net.packet.IoBuffer;
import core.tools.StringUtils;

/**
 * Handles the appearance update flag.
 * @author Emperor
 *
 */
public final class AppearanceFlag extends UpdateFlag<Player> {

	/**
	 * Constructs a new {@code AppearanceFlag} {@code Object}.
	 * @param player The player.
	 */
	public AppearanceFlag(Player player) {
		super(player);
	}

	@Override
	public void write(IoBuffer buffer) {
		Appearance appearance = context.getAppearance();
		appearance.prepareBodyData(context);
		IoBuffer block = new IoBuffer();
		int settings = appearance.getGender().toByte();
		if (context.size() > 1) {
			settings |= (context.size() - 1) << 3;
		}
		block.put(settings); // settings hash.
		block.put(appearance.getSkullIcon()); // Skull icon
		block.put(appearance.getHeadIcon()); // Head icon
		int npcId = appearance.getNpcId();
		if (npcId == -1) {
			int[] parts = appearance.getBodyParts();
			for (int i = 0; i < 12; i++) {
				int value = parts[i];
				if (value == 0) {
					block.put(0);
				} else {
					block.putShort(value);
				}
			}
		} else {
			block.putShort(-1);
			block.putShort(npcId);
			block.put(255);
		}
		final BodyPart[] colors = new BodyPart[] { appearance.getHair(), appearance.getTorso(), appearance.getLegs(), appearance.getFeet(), appearance.getSkin() };
		for (int i = 0; i < colors.length; i++) {// colours
			block.put(colors[i].getColor());
		}
		block.putShort(appearance.getRenderAnimation());
		block.putLong(StringUtils.stringToLong(context.getUsername()));
		block.put(context.getProperties().getCurrentCombatLevel());
		block.putShort(0);
		block.put(0);
		buffer.putA(block.toByteBuffer().position());
		buffer.put(block);
	}

	@Override
	public int data() {
		return getData();
	}

	@Override
	public int ordinal() {
		return getOrdinal();
	}

	/**
	 * Gets the ordinal for this flag.
	 * @return The flag ordinal.
	 */
	public static int getOrdinal() {
		return 3;
	}

	/**
	 * Gets the mask data.
	 * @return The mask data.
	 */
	public static int getData() {
		return 0x4;
	}
}