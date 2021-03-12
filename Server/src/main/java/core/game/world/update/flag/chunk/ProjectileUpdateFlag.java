package core.game.world.update.flag.chunk;

import core.game.node.entity.Entity;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.player.Player;
import core.game.world.map.Location;
import core.game.world.update.flag.UpdateFlag;
import core.net.packet.IoBuffer;

/**
 * Handles the projectile updating.
 * @author Emperor
 */
public final class ProjectileUpdateFlag extends UpdateFlag<Projectile> {

	/**
	 * Constructs a new {@code ProjectileUpdateFlag} {@code Object}.
	 * @param projectile The projectile.
	 */
	public ProjectileUpdateFlag(Projectile projectile) {
		super(projectile);
	}

	@Override
	public void write(IoBuffer buffer) {
		Projectile p = context;
		Location start = p.getSourceLocation();
		Entity target = p.getVictim();
		Location end = p.isLocationBased() ? p.getEndLocation() : target.getLocation();
		buffer.put((byte) 16) //opcode
		.put((start.getChunkOffsetX() << 4) | (start.getChunkOffsetY() & 0x7))
		.put(end.getX() - start.getX())
		.put(end.getY() - start.getY())
		.putShort(target != null ? (target instanceof Player ? -(target.getIndex() + 1) : (target.getIndex() + 1)) : -1)
		.putShort(p.getProjectileId())
		.put(p.getStartHeight())
		.put(p.getEndHeight())
		.putShort(p.getStartDelay())
		.putShort(p.getSpeed())
		.put(p.getAngle())
		.put(p.getDistance());
	}

	@Override
	public int data() {
		return 0;
	}

	@Override
	public int ordinal() {
		return 2;
	}

}