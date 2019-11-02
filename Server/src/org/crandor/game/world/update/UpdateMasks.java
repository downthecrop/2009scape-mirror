package org.crandor.game.world.update;

import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.ImpactHandler;
import org.crandor.game.node.entity.combat.ImpactHandler.Impact;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.world.update.flag.UpdateFlag;
import org.crandor.game.world.update.flag.context.HitMark;
import org.crandor.game.world.update.flag.npc.NPCHitFlag;
import org.crandor.game.world.update.flag.npc.NPCHitFlag1;
import org.crandor.game.world.update.flag.player.AppearanceFlag;
import org.crandor.game.world.update.flag.player.HitUpdateFlag;
import org.crandor.game.world.update.flag.player.HitUpdateFlag1;
import org.crandor.net.packet.IoBuffer;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Holds an entity's update masks.
 * @author Emperor
 */
public final class UpdateMasks {

	/**
	 * The amount of update masks.
	 */
	public static final int SIZE = 11;

	/**
	 * The mask data.
	 */
	private int maskData = 0;

	/**
	 * The update masks array.
	 */
	private UpdateFlag<?>[] masks = new UpdateFlag[SIZE];

	/**
	 * The appearance time stamp.
	 */
	private long appearanceStamp;

	/**
	 * The synced mask data.
	 */
	private int syncedMask = 0;

	/**
	 * The update masks array.
	 */
	private UpdateFlag<?>[] syncedMasks = new UpdateFlag[SIZE];

	/**
	 * If the update masks are being updated.
	 */
	private AtomicBoolean updating = new AtomicBoolean();

	/**
	 * Registers an update flag.
	 * @param flag The update flag.
	 * @return {@code True} if successful.
	 */
	public boolean register(UpdateFlag<?> flag) {
		return register(flag, false);
	}

	/**
	 * Registers an update flag.
	 * @param flag The update flag.
	 * @return {@code True} if successful.
	 */
	public boolean register(UpdateFlag<?> flag, boolean synced) {
		if (updating.get()) {
			return false;
		}
		if (flag instanceof AppearanceFlag) {
			appearanceStamp = System.currentTimeMillis();
			synced = true;
		}
		if (synced) {
			syncedMasks[flag.ordinal()] = flag;
			syncedMask |= flag.data();
		}
		maskData |= flag.data();
		masks[flag.ordinal()] = flag;
		return true;
	}

	/**
	 * Unregisters a synced update mask.
	 * @param maskData The mask data.
	 * @return {@code True} if the mask got removed.
	 */
	public boolean unregisterSynced(int ordinal) {
		if (syncedMasks[ordinal] != null) {
			syncedMask &= ~syncedMasks[ordinal].data();
			syncedMasks[ordinal] = null;
			return true;
		}
		return false;
	}

	/**
	 * Writes the flags.
	 * @param p The player.
	 * @param e The entity to update.
	 * @param buffer The buffer to write on.
	 */
	public void write(Player p, Entity e, IoBuffer buffer) {
		int maskData = this.maskData;
		if (maskData >= 0x100) {
			maskData |= e instanceof Player ? 0x10 : 0x8;
			buffer.put(maskData).put(maskData >> 8);
		} else {
			buffer.put(maskData);
		}
		for (int i = 0; i < masks.length; i++) {
			UpdateFlag<?> flag = masks[i];
			if (flag != null) {
				flag.writeDynamic(buffer, p);
			}
		}
	}

	/**
	 * Writes the update masks on synchronization.
	 * @param p The player.
	 * @param e The entity to update.
	 * @param buffer The buffer to write on.
	 * @param appearance If the appearance mask should be written.
	 */
	public void writeSynced(Player p, Entity e, IoBuffer buffer, boolean appearance) {
		int maskData = this.maskData;
		int synced = this.syncedMask;
		if (!appearance && (synced & AppearanceFlag.getData()) != 0) {
			synced &= ~AppearanceFlag.getData();
		}
		maskData |= synced;
		if (maskData >= 0x100) {
			maskData |= e instanceof Player ? 0x10 : 0x8;
			buffer.put(maskData).put(maskData >> 8);
		} else {
			buffer.put(maskData);
		}
		for (int i = 0; i < masks.length; i++) {
			UpdateFlag<?> flag = masks[i];
			if (flag == null) {
				flag = syncedMasks[i];
				if (!appearance && flag instanceof AppearanceFlag) {
					continue;
				}
			}
			if (flag != null) {
				flag.writeDynamic(buffer, p);
			}
		}
	}

	/**
	 * Adds the dynamic update flags.
	 * @param entity The entity.
	 */
	public void prepare(Entity entity) {
		ImpactHandler handler = entity.getImpactHandler();
		for (int i = 0; i < 2; i++) {
			if (handler.getImpactQueue().peek() == null) {
				break;
			}
			Impact impact = handler.getImpactQueue().poll();
			registerHitUpdate(entity, impact, i == 1);
		}
		updating.set(true);
	}

	/**
	 * Registers the hit update for the given {@link Impact}.
	 * @param e The entity.
	 * @param impact The impact to update.
	 * @param secondary If the hit update is secondary.
	 */
	private HitMark registerHitUpdate(Entity e, Impact impact, boolean secondary) {
		boolean player = e instanceof Player;
		HitMark mark = new HitMark(impact.getAmount(), impact.getType().ordinal(), e);
		if (player) {
			register(secondary ? new HitUpdateFlag1(mark) : new HitUpdateFlag(mark));
		} else {
			register(secondary ? new NPCHitFlag1(mark) : new NPCHitFlag(mark));
		}
		return mark;
	}

	/**
	 * Resets the update masks.
	 */
	public void reset() {
		for (int i = 0; i < masks.length; i++) {
			masks[i] = null;
		}
		maskData = 0;
		updating.set(false);
	}

	/**
	 * Gets the appearanceStamp.
	 * @return The appearanceStamp.
	 */
	public long getAppearanceStamp() {
		return appearanceStamp;
	}

	/**
	 * Sets the appearanceStamp.
	 * @param appearanceStamp The appearanceStamp to set.
	 */
	public void setAppearanceStamp(long appearanceStamp) {
		this.appearanceStamp = appearanceStamp;
	}

	/**
	 * Checks if an update is required.
	 * @return {@code True} if so.
	 */
	public boolean isUpdateRequired() {
		return maskData != 0;
	}

	/**
	 * Checks if synced update masks have been registered.
	 * @return {@code True} if so.
	 */
	public boolean hasSynced() {
		return syncedMask != 0;
	}
}