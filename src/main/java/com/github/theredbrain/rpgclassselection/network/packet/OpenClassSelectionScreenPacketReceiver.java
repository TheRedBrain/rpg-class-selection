package com.github.theredbrain.rpgclassselection.network.packet;

import com.github.theredbrain.rpgclassselection.RPGClassSelection;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class OpenClassSelectionScreenPacketReceiver implements ServerPlayNetworking.PlayPayloadHandler<OpenClassSelectionScreenPacket> {

	@Override
	public void receive(OpenClassSelectionScreenPacket payload, ServerPlayNetworking.Context context) {

		RPGClassSelection.openRPGClassSelectionScreen(
				context.player(),
				payload.initial_class_identifier_string(),
				payload.allow_changing_class(),
				payload.allow_changing_upgrades()
		);
	}
}
