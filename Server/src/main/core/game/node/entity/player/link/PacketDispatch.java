package core.game.node.entity.player.link;

import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.tools.Log;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.game.world.update.flag.chunk.AnimateObjectUpdateFlag;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.game.world.update.flag.EntityFlag;
import core.net.packet.PacketRepository;
import core.net.packet.context.*;
import core.net.packet.context.DisplayModelContext.ModelType;
import core.net.packet.out.*;

import static core.api.ContentAPIKt.log;
import static core.api.ContentAPIKt.*;


/**
 * Represents the class used to dispatching packets.
 * @author Emperor
 * @author Vexia
 */
public final class PacketDispatch {

	/**
	 * The instance of the {@code Player}.
	 */
	private final Player player;

	/**
	 * The player context.
	 */
	private final PlayerContext context;

	/**
	 * Constructs a new {@code PacketDispatch} {@code Object}.
	 * @param player the player.
	 */
	public PacketDispatch(Player player) {
		this.player = player;
		this.context = new PlayerContext(player);
	}

	public void sendVarp (int index, int value) {
		PacketRepository.send(Config.class, new ConfigContext(player, index, value));
	}

    public void sendVarcUpdate(short index, int value) {
        PacketRepository.send(VarcUpdate.class, new VarcUpdateContext(player, index, value));
    }

	/**
	 * Send a game message.
	 * @param message The game message.
	 */
	public void sendMessage(String message) {
		if (message == null) {
			return;
		}
		if (player.getAttribute("chat_filter") != null && !message.contains("<col=CC6600>") && !message.contains("<col=FFFF00>")) {
			return;
		}
		if (message.length() > 255) {
			log(this.getClass(), Log.ERR,  "Message length out of bounds (" + message + ")!");
			message = message.substring(0, 255);
		}
		PacketRepository.send(GameMessage.class, new GameMessageContext(player, message));
	}

	/**
	 * Sends game messages.
	 * @param messages the messages.
	 */
	public void sendMessages(final String... messages) {
		for (String message : messages) {
			sendMessage(message);
		}
	}

	/**
	 * Method used to send a game message on a tick.
	 * @param message the message.
	 * @param ticks the ticks.
	 */
	public void sendMessage(final String message, int ticks) {
		GameWorld.getPulser().submit(new Pulse(ticks, player) {
			@Override
			public boolean pulse() {
				sendMessage(message);
				return true;
			}
		});
	}

	/**
	 * Send a access mask.
	 * @param settingsHash The access mask settingsHash.
	 * @param childId The access mask child id.
	 * @param interfaceId The access mask interface Id.
	 * @param offset The access mask off set.
	 * @param length The access mask length.
	 */
	public void sendIfaceSettings(int settingsHash, int childId, int interfaceId, int offset, int length) {
		PacketRepository.send(AccessMask.class, new AccessMaskContext(player, settingsHash, childId, interfaceId, offset, length));
	}

	/**
	 * Send a windowns pane.
	 * @param windowId The windows pane id.
	 * @param type The windowns pane type.
	 */
	public void sendWindowsPane(int windowId, int type) {
		PacketRepository.send(WindowsPane.class, new WindowsPaneContext(player, windowId, type));
	}

	/**
	 * sends the system update packet.
	 * @param time the amount of time.
	 */
	public void sendSystemUpdate(int time) {
		PacketRepository.send(SystemUpdatePacket.class, new SystemUpdateContext(player, time));
	}

	/**
	 * Sends music packet.
	 * @param musicId The music id.
	 */
	public void sendMusic(int musicId) {
		PacketRepository.send(MusicPacket.class, new MusicContext(player, musicId));
	}

	/**
	 * Sends the temporary music packet.
	 * @param musicId The music id.
	 */
	public void sendTempMusic(int musicId) {
		PacketRepository.send(MusicPacket.class, new MusicContext(player, musicId, true));
	}
	
	/**
	 * Sends a client script config to the player.
	 * @param id The id to set.
	 * @param value The value of the config.
	 */
	public void sendScriptConfig(int id, int value, String types, Object... parameters) {
		PacketRepository.send(CSConfigPacket.class, new CSConfigContext(player, id, value, types, parameters));
	}

	/**
	 * Send a run script.
	 * @param id The run script id.
	 * @param string The run script string.
	 * @param objects The run scripts objects.
	 */
	public void sendRunScript(int id, String string, Object... objects) {
		PacketRepository.send(RunScriptPacket.class, new RunScriptContext(player, id, string, objects));
	}

	/**
	 * Send a StringPacket.
	 * @param string The string.
	 * @param interfaceId The interface id.
	 * @param lineId The line id.
	 */
	public void sendString(String string, int interfaceId, int lineId) {
		PacketRepository.send(StringPacket.class, new StringContext(player, string, interfaceId, lineId));
	}

	/**
	 * Send a update packet for the amount of run energy.
	 */
	public void sendRunEnergy() {
		PacketRepository.send(RunEnergy.class, getContext());
	}

	/**
	 * Send the logout packet.
	 */
	public void sendLogout() {
		PacketRepository.send(Logout.class, getContext());
	}

	/**
	 * Send the interface animation packet.
	 * @param animationId The animation id.
	 * @param interfaceId The interface id.
	 * @param childId The child id.
	 */
	public void sendAnimationInterface(int animationId, int interfaceId, int childId) {
		PacketRepository.send(AnimateInterface.class, new AnimateInterfaceContext(player, animationId, interfaceId, childId));
	}

	/**
	 * Send the player on interface packet.
	 * @param interfaceId The interface id.
	 * @param childId The child id.
	 */
	public void sendPlayerOnInterface(int interfaceId, int childId) {
		// fixme right now for iface 68-71 the player is massive
		// The zoom for the other windows is 2150
		// for these 4 individuals it should be 796 but dmc.setZoom doesn't work
		DisplayModelContext dmc = new DisplayModelContext(player, interfaceId, childId);
		dmc.setZoom(796); // this appears to do nothing
		PacketRepository.send(DisplayModel.class, dmc);
	}

	/**
	 * Send the non-player character on interface packet.
	 * @param npcId The non-player character's id.
	 * @param interfaceId The interface id.
	 * @param childId The child id.
	 */
	public void sendNpcOnInterface(int npcId, int interfaceId, int childId) {
		PacketRepository.send(DisplayModel.class, new DisplayModelContext(player, npcId, interfaceId, childId));
	}

	public void sendModelOnInterface(int modelID, int interfaceId, int childId, int zoom){
		PacketRepository.send(DisplayModel.class, new DisplayModelContext(player, ModelType.MODEL, modelID,zoom,interfaceId,childId,new Object()));
	}

	public void sendRepositionOnInterface(int interfaceId, int childId, int positionX, int positionY){
		PacketRepository.send(RepositionChild.class, new ChildPositionContext(player, interfaceId, childId, positionX, positionY));
	}

	public void sendAngleOnInterface(int interfaceId, int childId, int zoom, int pitch, int yaw){
		PacketRepository.send(InterfaceSetAngle.class, new DefaultContext(player, pitch, zoom, yaw, interfaceId, childId));
	}

	/**
	 * Send the item on interface packet.
	 * @param itemId The item id.
	 * @param amount The item amount.
	 * @param interfaceId The interface id.
	 * @param childId The child id.
	 */
	public void sendItemOnInterface(int itemId, int amount, int interfaceId, int childId) {
		PacketRepository.send(DisplayModel.class, new DisplayModelContext(player, ModelType.ITEM, itemId, amount, interfaceId, childId));
	}

	/**
	 * Send the item on interface packet.
	 * @param itemId The item id.
	 * @param zoom the zoom.
	 * @param interfaceId The interface id.
	 * @param childId The child id.
	 */
	public void sendItemZoomOnInterface(int itemId, int zoom, int interfaceId, int childId) {
		PacketRepository.send(DisplayModel.class, new DisplayModelContext(player, ModelType.ITEM, itemId, zoom, interfaceId, childId, zoom));
	}

	public void sendInterSetItemsOptionsScript(int interfaceId, int componentId, int key, int width, int height, String... options) {
		sendInterSetItemsOptionsScript(interfaceId, componentId, key, false, width, height, options);
	}

	public void sendInterSetItemsOptionsScript(int interfaceId, int componentId, int key, boolean negativeKey, int width, int height, String... options) {
		Object[] parameters = new Object[6 + options.length];
		int index = 0;
		for (int count = options.length - 1; count >= 0; count--)
			parameters[index++] = options[count];
		parameters[index++] = -1; // dunno but always this
		parameters[index++] = 0;// dunno but always this, maybe startslot?
		parameters[index++] = height;
		parameters[index++] = width;
		parameters[index++] = key;
		parameters[index++] = interfaceId << 16 | componentId;
		sendRunScript(negativeKey ? 695 : 150, parameters);
		// name says*/
	}
	public void sendRunScript(int scriptId, Object... params) {
		String parameterTypes = "";
		if (params != null) {
			for (int count = params.length - 1; count >= 0; count--) {
				if (params[count] instanceof String)
					parameterTypes += "s"; // string
				else
					parameterTypes += "i"; // integer
			}
		}
		sendRunScript(scriptId, parameterTypes, params);
	}


	/**
	 * Send the item on interface packet.
	 * @param itemId The item id.
	 * @param amount The amount.
	 * @param zoom the zoom.
	 * @param interfaceId The interface id.
	 * @param childId The child id.
	 */
	public void sendItemZoomOnInterface(int itemId, int amount, int zoom, int interfaceId, int childId) {
		PacketRepository.send(DisplayModel.class, new DisplayModelContext(player, ModelType.ITEM, itemId, amount, interfaceId, childId, zoom));
	}

	/**
	 * Send the interface config packet.
	 * @param interfaceId The interface id.
	 * @param childId The child id.
	 * @param hide If the component should be hidden.
	 */
	public void sendInterfaceConfig(int interfaceId, int childId, boolean hide) {
		PacketRepository.send(InterfaceConfig.class, new InterfaceConfigContext(player, interfaceId, childId, hide));
	}

	/**
	 * Send a animation update flag mask.
	 * @param id The animation id.
	 */
	public void sendAnimation(int id) {
                player.getUpdateMasks().register(EntityFlag.Animate, new Animation(id));
	}

	/**
	 * Send a animation update flag mask.
	 * @param id The animation id.
	 * @param delay The animation delay.
	 */
	public void sendAnimation(int id, int delay) {
                player.getUpdateMasks().register(EntityFlag.Animate, new Animation(id, delay));
	}

	/**
	 * Send a graphic update flag mask.
	 * @param id The graphic id.
	 */
	public void sendGraphic(int id) {
                player.getUpdateMasks().register(EntityFlag.SpotAnim, new Graphics(id));
	}

	/**
	 * Sends the positioned graphic.
	 * @param id the id.
	 * @param height the height.
	 * @param delay the delay.
	 * @param location the location.
	 */
	public void sendPositionedGraphic(int id, int height, int delay, Location location) {
		PacketRepository.send(PositionedGraphic.class, new PositionedGraphicContext(player, new Graphics(id, height, delay), location, 0, 0));
	}

	/**
	 * Sends a global graphic.
	 * @param id the id.
	 * @param location the location.
	 */
	public void sendGlobalPositionGraphic(int id, Location location) {
		for (Player player : RegionManager.getLocalPlayers(location)) {
			player.getPacketDispatch().sendPositionedGraphic(id, 0, 0, location);
		}
	}

	/**
	 * Sends the positioned graphic.
	 * @param graphics the graphics.
	 * @param location the location.
	 */
	public void sendPositionedGraphics(Graphics graphics, Location location) {
		PacketRepository.send(PositionedGraphic.class, new PositionedGraphicContext(player, graphics, location, 0, 0));
	}

	/**
	 * Method used to send an object animation.
	 * @param object the object.
	 * @param animation the animation.
	 */
	public void sendSceneryAnimation(Scenery object, Animation animation) {
		animation = new Animation(animation.getId(), animation.getDelay(), animation.getPriority());
		animation.setObject(object);
		RegionManager.getRegionChunk(object.getLocation()).flag(new AnimateObjectUpdateFlag(animation));
	}

	/**
	 * Method used to send an object animation.
	 * @param object the object.
	 * @param animation the animation.
	 * @param global if the animation is global or not.
	 */
	public void sendSceneryAnimation(Scenery object, Animation animation, boolean global) {
		if (global) {
			sendSceneryAnimation(object, animation);
			return;
		}
		animation.setObject(object);
		PacketRepository.send(AnimateObjectPacket.class, new AnimateObjectContext(player, animation));
	}

	/**
	 * Send a graphic update flag mask.
	 * @param id The graphic id.
	 * @param height The graphic height.
	 */
	public void sendGraphic(int id, int height) {
                player.getUpdateMasks().register(EntityFlag.SpotAnim, new Graphics(id, height));
	}
	public void sendVarClient(int id, int value, boolean cs2) {
		PacketRepository.send(Config.class, new ConfigContext(player, id, value, cs2));
	}

	public void sendLeftShiftedVarbit(int varpIndex, int offset, int value){
		setVarp(player, varpIndex, (value << offset));
	}

	public void sendRightShiftedVarbit(int varpIndex, int offset, int value){
		setVarp(player, varpIndex, (value >> offset));
	}


	/**
	 * Gets the player.
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets the context.
	 * @return The context.
	 */
	public PlayerContext getContext() {
		return context;
	}

	public void sendScriptConfigs(int id, int value, String type, Object... params) {
		PacketRepository.send(CSConfigPacket.class, new CSConfigContext(player, id, value, type, params));
	}

	public void resetInterface(int id) {
		PacketRepository.send(ResetInterface.class, new InterfaceContext(player, 0, 0, id, false));
	}

	public void sendLastLoginInfo() {
		PacketRepository.send(LastLoginInfo.class, new PlayerContext(player));
	}
}
