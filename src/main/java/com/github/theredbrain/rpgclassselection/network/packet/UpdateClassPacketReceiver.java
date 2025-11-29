package com.github.theredbrain.rpgclassselection.network.packet;

import com.github.theredbrain.rpgclassselection.component.type.ClassStateComponent;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class UpdateClassPacketReceiver implements ServerPlayNetworking.PlayPayloadHandler<UpdateClassPacket> {
	@Override
	public void receive(UpdateClassPacket payload, ServerPlayNetworking.Context context) {

		ServerPlayerEntity player = context.player();

		ClassStateComponent.ActiveClassState newActiveClassState = payload.newActiveClassState();

		player.sendMessage(Text.literal("New class:"));
		player.sendMessage(Text.literal(newActiveClassState.toString()));

	}
}