package com.github.theredbrain.rpgclassselection.network.packet;

import com.github.theredbrain.rpgclassselection.RPGClassSelection;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record OpenClassSelectionScreenPacket(
		String initialClassIdentifierString
) implements CustomPayload {
	public static final Id<OpenClassSelectionScreenPacket> PACKET_ID = new Id<>(RPGClassSelection.identifier("open_class_selection_screen"));
	public static final PacketCodec<RegistryByteBuf, OpenClassSelectionScreenPacket> PACKET_CODEC = PacketCodec.of(OpenClassSelectionScreenPacket::write, OpenClassSelectionScreenPacket::new);

	public OpenClassSelectionScreenPacket(PacketByteBuf buf) {
		this(
				buf.readString()
		);
	}

	private void write(RegistryByteBuf registryByteBuf) {
		registryByteBuf.writeString(this.initialClassIdentifierString);
	}

	@Override
	public Id<? extends CustomPayload> getId() {
		return PACKET_ID;
	}
}