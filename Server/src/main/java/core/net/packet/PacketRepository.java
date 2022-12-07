package core.net.packet;

import core.net.packet.out.GrandExchangePacket;
import core.net.packet.out.*;
import rs09.game.system.SystemLogger;
import rs09.net.packet.PacketWriteQueue;

import java.util.HashMap;
import java.util.Map;

/**
 * The packet repository.
 * @author Emperor
 */
public final class PacketRepository {

	/**
	 * The outgoing packets mapping.
	 */
	public final static Map<Class<?>, OutgoingPacket<? extends Context>> OUTGOING_PACKETS = new HashMap<>();

	/**
	 * Populate the mappings.
	 */
	static {
		OUTGOING_PACKETS.put(LoginPacket.class, new LoginPacket());				 			//
		OUTGOING_PACKETS.put(UpdateSceneGraph.class, new UpdateSceneGraph());	 			//
		OUTGOING_PACKETS.put(WindowsPane.class, new WindowsPane());				 			//
		OUTGOING_PACKETS.put(Interface.class, new Interface());					 			//
		OUTGOING_PACKETS.put(SkillLevel.class, new SkillLevel());				 			//
		OUTGOING_PACKETS.put(Config.class, new Config());						 			//
		OUTGOING_PACKETS.put(AccessMask.class, new AccessMask());				 			//
		OUTGOING_PACKETS.put(GameMessage.class, new GameMessage());				 			//
		OUTGOING_PACKETS.put(RunScriptPacket.class, new RunScriptPacket());		 			//
		OUTGOING_PACKETS.put(RunEnergy.class, new RunEnergy());					 			//
		OUTGOING_PACKETS.put(ContainerPacket.class, new ContainerPacket());		 			//
		OUTGOING_PACKETS.put(StringPacket.class, new StringPacket());			 			//
		OUTGOING_PACKETS.put(Logout.class, new Logout());						 			//
		OUTGOING_PACKETS.put(CloseInterface.class, new CloseInterface());		 			//
		OUTGOING_PACKETS.put(AnimateInterface.class, new AnimateInterface());	 			//
		OUTGOING_PACKETS.put(DisplayModel.class, new DisplayModel());			 			//
		OUTGOING_PACKETS.put(InterfaceConfig.class, new InterfaceConfig());		 			//
		OUTGOING_PACKETS.put(PingPacket.class, new PingPacket());				 			//
		OUTGOING_PACKETS.put(UpdateAreaPosition.class, new UpdateAreaPosition());			//
		OUTGOING_PACKETS.put(ConstructScenery.class, new ConstructScenery());		 			//
		OUTGOING_PACKETS.put(ClearScenery.class, new ClearScenery());				 			//
		OUTGOING_PACKETS.put(HintIcon.class, new HintIcon());					 			//
		OUTGOING_PACKETS.put(ClearMinimapFlag.class, new ClearMinimapFlag());	 			//
		OUTGOING_PACKETS.put(InteractionOption.class, new InteractionOption());  			//
		OUTGOING_PACKETS.put(SetWalkOption.class, new SetWalkOption());			 			//
		OUTGOING_PACKETS.put(MinimapState.class, new MinimapState());			 			//
		OUTGOING_PACKETS.put(ConstructGroundItem.class, new ConstructGroundItem());			//
		OUTGOING_PACKETS.put(ClearGroundItem.class, new ClearGroundItem());		 			//
		OUTGOING_PACKETS.put(RepositionChild.class, new RepositionChild());		 			//
		OUTGOING_PACKETS.put(PositionedGraphic.class, new PositionedGraphic());	 			//
		OUTGOING_PACKETS.put(SystemUpdatePacket.class, new SystemUpdatePacket());			//
		OUTGOING_PACKETS.put(CameraViewPacket.class, new CameraViewPacket());	 			//
		OUTGOING_PACKETS.put(MusicPacket.class, new MusicPacket());				 			//
		OUTGOING_PACKETS.put(AudioPacket.class, new AudioPacket());				 			//
		OUTGOING_PACKETS.put(GrandExchangePacket.class, new GrandExchangePacket());			//
		OUTGOING_PACKETS.put(BuildDynamicScene.class, new BuildDynamicScene());				//
		OUTGOING_PACKETS.put(AnimateObjectPacket.class, new AnimateObjectPacket());			//
		OUTGOING_PACKETS.put(ClearRegionChunk.class, new ClearRegionChunk());				//
		OUTGOING_PACKETS.put(ContactPackets.class, new ContactPackets());					//
		OUTGOING_PACKETS.put(CommunicationMessage.class, new CommunicationMessage());		//
		OUTGOING_PACKETS.put(UpdateClanChat.class, new UpdateClanChat());					//
		OUTGOING_PACKETS.put(UpdateGroundItemAmount.class, new UpdateGroundItemAmount());   //
		OUTGOING_PACKETS.put(WeightUpdate.class, new WeightUpdate());						//
		OUTGOING_PACKETS.put(UpdateRandomFile.class, new UpdateRandomFile());				//
		OUTGOING_PACKETS.put(InstancedLocationUpdate.class, new InstancedLocationUpdate());	//
		OUTGOING_PACKETS.put(CSConfigPacket.class, new CSConfigPacket());					//
		OUTGOING_PACKETS.put(Varbit.class, new Varbit());
        OUTGOING_PACKETS.put(VarcUpdate.class, new VarcUpdate());
	}

	/**
	 * Sends a new packet.
	 * @param clazz The class of the outgoing packet to send.
	 * @param context The context.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void send(Class<? extends OutgoingPacket> clazz, Context context) {
		if(context.getPlayer().getSession() == null) return;
		OutgoingPacket p = OUTGOING_PACKETS.get(clazz);
		if (p == null) {
			SystemLogger.logErr(PacketRepository.class, "Invalid outgoing packet [handler=" + clazz + ", context=" + context + "].");
			return;
		}
		if(!context.getPlayer().isArtificial()) {
			PacketWriteQueue.handle(p, context);
		}
	}

}
