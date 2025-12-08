package com.github.theredbrain.rpgclassselection.network.packet;

import com.github.theredbrain.rpgclassselection.RPGClassSelection;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record OpenClassSelectionScreenPacket(
		String initial_class_identifier_string,
		boolean restrict_class_list,
		boolean allow_changing_class,
		boolean allow_changing_upgrades
) implements CustomPayload {
	public static final Id<OpenClassSelectionScreenPacket> PACKET_ID = new Id<>(RPGClassSelection.identifier("open_class_selection_screen"));
	public static final PacketCodec<RegistryByteBuf, OpenClassSelectionScreenPacket> PACKET_CODEC = PacketCodec.of(OpenClassSelectionScreenPacket::write, OpenClassSelectionScreenPacket::new);

	public OpenClassSelectionScreenPacket(PacketByteBuf buf) {
		this(
				buf.readString(),
				buf.readBoolean(),
				buf.readBoolean(),
				buf.readBoolean()
		);
	}

	private void write(RegistryByteBuf registryByteBuf) {
		registryByteBuf.writeString(this.initial_class_identifier_string);
		registryByteBuf.writeBoolean(this.restrict_class_list);
		registryByteBuf.writeBoolean(this.allow_changing_class);
		registryByteBuf.writeBoolean(this.allow_changing_upgrades);
	}

	@Override
	public Id<? extends CustomPayload> getId() {
		return PACKET_ID;
	}
}