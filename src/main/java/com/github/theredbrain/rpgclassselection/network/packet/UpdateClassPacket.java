package com.github.theredbrain.rpgclassselection.network.packet;

import com.github.theredbrain.rpgclassselection.RPGClassSelection;
import com.github.theredbrain.rpgclassselection.component.type.ClassStateComponent;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record UpdateClassPacket(
		ClassStateComponent.ActiveClassState newActiveClassState
) implements CustomPayload {
	public static final Id<UpdateClassPacket> PACKET_ID = new Id<>(RPGClassSelection.identifier("update_class"));

	public static final PacketCodec<ByteBuf, UpdateClassPacket> PACKET_CODEC = new PacketCodec<>() {
		public UpdateClassPacket decode(ByteBuf byteBuf) {
			return new UpdateClassPacket(
					ClassStateComponent.ActiveClassState.PACKET_CODEC.decode(byteBuf)
			);
		}

		public void encode(ByteBuf byteBuf, UpdateClassPacket updateClassPacket) {
			ClassStateComponent.ActiveClassState.PACKET_CODEC.encode(byteBuf, updateClassPacket.newActiveClassState());
		}
	};

	@Override
	public Id<? extends CustomPayload> getId() {
		return PACKET_ID;
	}
}
