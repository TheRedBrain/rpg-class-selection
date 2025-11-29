package com.github.theredbrain.rpgclassselection.registry;

import com.github.theredbrain.rpgclassselection.network.packet.OpenClassSelectionScreenPacket;
import com.github.theredbrain.rpgclassselection.network.packet.OpenClassSelectionScreenPacketReceiver;
import com.github.theredbrain.rpgclassselection.network.packet.UpdateClassPacket;
import com.github.theredbrain.rpgclassselection.network.packet.UpdateClassPacketReceiver;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class ServerPacketRegistry {
	public static void init() {

		PayloadTypeRegistry.playC2S().register(OpenClassSelectionScreenPacket.PACKET_ID, OpenClassSelectionScreenPacket.PACKET_CODEC);
		ServerPlayNetworking.registerGlobalReceiver(OpenClassSelectionScreenPacket.PACKET_ID, new OpenClassSelectionScreenPacketReceiver());

		PayloadTypeRegistry.playC2S().register(UpdateClassPacket.PACKET_ID, UpdateClassPacket.PACKET_CODEC);
		ServerPlayNetworking.registerGlobalReceiver(UpdateClassPacket.PACKET_ID, new UpdateClassPacketReceiver());

	}
}
