package com.github.theredbrain.rpgclassselection.component.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

import java.util.ArrayList;
import java.util.List;

public record ClassStateComponent(
		ActiveClassState activeClassState
) {
	public static final ClassStateComponent DEFAULT = new ClassStateComponent(new ActiveClassState("rpgclassslection:empty_class", List.of()));
	public static final Codec<ClassStateComponent> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
							ActiveClassState.CODEC.fieldOf("activeClassState").forGetter(component -> component.activeClassState)
					)
					.apply(instance, ClassStateComponent::new)
	);

	public static final PacketCodec<RegistryByteBuf, ClassStateComponent> PACKET_CODEC = PacketCodec.tuple(
			ClassStateComponent.ActiveClassState.PACKET_CODEC,
			component -> component.activeClassState,
			ClassStateComponent::new
	);

	public ClassStateComponent(
			String activeClassIdentifier,
			List<String> activeUpgradeIdentifierList
	) {
		this(
				new ActiveClassState(activeClassIdentifier, activeUpgradeIdentifierList)
		);
	}

	public record ActiveClassState(
			String activeClassIdentifier,
			List<String> activeUpgradeIdentifierList
	) {

		public static final Codec<ActiveClassState> CODEC = RecordCodecBuilder.create(
				instance -> instance.group(
								Codec.STRING.optionalFieldOf("activeClassIdentifier", "").forGetter(component -> component.activeClassIdentifier),
								Codec.STRING.listOf().optionalFieldOf("activeUpgradeIdentifierList", List.of()).forGetter(component -> component.activeUpgradeIdentifierList)
						)
						.apply(instance, ActiveClassState::new)
		);

		public static final PacketCodec<ByteBuf, ActiveClassState> PACKET_CODEC = new PacketCodec<>() {
			public ActiveClassState decode(ByteBuf byteBuf) {
				String activeClassIdentifier = PacketCodecs.STRING.decode(byteBuf);
				int listSize = PacketCodecs.INTEGER.decode(byteBuf);
				List<String> activeUpgradeIdentifierList = new ArrayList<>();
				for (int i = 0; i < listSize; i++) {
					activeUpgradeIdentifierList.add(PacketCodecs.STRING.decode(byteBuf));
				}
				return new ActiveClassState(activeClassIdentifier, activeUpgradeIdentifierList);
			}

			public void encode(ByteBuf byteBuf, ActiveClassState classUnlockStateData) {
				PacketCodecs.STRING.encode(byteBuf, classUnlockStateData.activeClassIdentifier());
				int listSize = classUnlockStateData.activeUpgradeIdentifierList().size();
				PacketCodecs.INTEGER.encode(byteBuf, listSize);
				for (int i = 0; i < listSize; i++) {
					PacketCodecs.STRING.encode(byteBuf, classUnlockStateData.activeUpgradeIdentifierList().get(i));
				}
			}
		};
	}
}
