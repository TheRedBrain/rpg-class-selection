package com.github.theredbrain.rpgclassselection.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.StringIdentifiable;

import java.util.Arrays;
import java.util.Optional;

public record UpgradeEntryComponent(
		String type,
		String spell_identifier,
		String attribute_identifier,
		double attribute_modifier_amount,
		String attribute_modifier_operation
) {
	public static final Codec<UpgradeEntryComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.STRING.optionalFieldOf("type", UpgradeEntryComponent.Type.SPELL.asString()).forGetter(x -> x.type),
			Codec.STRING.optionalFieldOf("spell_identifier", "").forGetter(x -> x.spell_identifier),
			Codec.STRING.optionalFieldOf("attribute_identifier", "").forGetter(x -> x.attribute_identifier),
			Codec.DOUBLE.optionalFieldOf("attribute_modifier_amount", 0.0).forGetter(x -> x.attribute_modifier_amount),
			Codec.STRING.optionalFieldOf("attribute_modifier_operation", EntityAttributeModifier.Operation.ADD_VALUE.asString()).forGetter(x -> x.attribute_modifier_operation)
	).apply(instance, UpgradeEntryComponent::new));

	public static final PacketCodec<ByteBuf, UpgradeEntryComponent> PACKET_CODEC = new PacketCodec<>() {
		public UpgradeEntryComponent decode(ByteBuf byteBuf) {
			return new UpgradeEntryComponent(
					PacketCodecs.STRING.decode(byteBuf),
					PacketCodecs.STRING.decode(byteBuf),
					PacketCodecs.STRING.decode(byteBuf),
					PacketCodecs.DOUBLE.decode(byteBuf),
					PacketCodecs.STRING.decode(byteBuf)
			);
		}

		public void encode(ByteBuf byteBuf, UpgradeEntryComponent upgradeEntry) {
			PacketCodecs.STRING.encode(byteBuf, upgradeEntry.type());
			PacketCodecs.STRING.encode(byteBuf, upgradeEntry.spell_identifier());
			PacketCodecs.STRING.encode(byteBuf, upgradeEntry.attribute_identifier());
			PacketCodecs.DOUBLE.encode(byteBuf, upgradeEntry.attribute_modifier_amount());
			PacketCodecs.STRING.encode(byteBuf, upgradeEntry.attribute_modifier_operation());
		}
	};

	public UpgradeEntryComponent(
			String type,
			String spell_identifier,
			String attribute_identifier,
			double attribute_modifier_amount,
			String attribute_modifier_operation
	) {
		this.type = !type.isEmpty() ? type : "";
		this.spell_identifier = !spell_identifier.isEmpty() ? spell_identifier : "";
		this.attribute_identifier = !attribute_identifier.isEmpty() ? attribute_identifier : "";
		this.attribute_modifier_amount = attribute_modifier_amount;
		this.attribute_modifier_operation = !attribute_modifier_operation.isEmpty() ? attribute_modifier_operation : EntityAttributeModifier.Operation.ADD_VALUE.asString();
	}

	public enum Type implements StringIdentifiable {
		SPELL("spell"),
		ATTRIBUTE_MODIFIER("attribute_modifier");

		private final String name;

		Type(String name) {
			this.name = name;
		}

		@Override
		public String asString() {
			return this.name;
		}

		public static Optional<UpgradeEntryComponent.Type> byName(String name) {
			return Arrays.stream(UpgradeEntryComponent.Type.values()).filter(type -> type.asString().equals(name)).findFirst();
		}
	}
}