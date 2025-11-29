package com.github.theredbrain.rpgclassselection.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.StringIdentifiable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public record RPGClass(
		String unlockAdvancementIdentifierString,
		String classItemIdentifierString,
		boolean visibleWhenLocked,
		List<String> descriptionList,
		List<String> lockedDescriptionList,
		List<UpgradeEntryGroup> upgradeEntryGroupList
) {

	public static final RPGClass DEFAULT = new RPGClass("", "", true, List.of("empty_class.descriptionList"), List.of(), List.of());
	public static final Codec<RPGClass> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.STRING.optionalFieldOf("unlockAdvancementIdentifierString", "").forGetter(x -> x.unlockAdvancementIdentifierString),
			Codec.STRING.optionalFieldOf("classItemIdentifierString", "").forGetter(x -> x.classItemIdentifierString),
			Codec.BOOL.optionalFieldOf("visibleWhenLocked", true).forGetter(x -> x.visibleWhenLocked),
			Codec.STRING.listOf().optionalFieldOf("descriptionList", null).forGetter(x -> x.descriptionList),
			Codec.STRING.listOf().optionalFieldOf("lockedDescriptionList", null).forGetter(x -> x.lockedDescriptionList),
			UpgradeEntryGroup.CODEC.listOf().optionalFieldOf("upgradeEntryGroupList", List.of()).forGetter(x -> x.upgradeEntryGroupList)
	).apply(instance, RPGClass::new));

	public static final PacketCodec<ByteBuf, RPGClass> PACKET_CODEC = new PacketCodec<>() {
		public RPGClass decode(ByteBuf byteBuf) {
			String unlockAdvancementIdentifierString = PacketCodecs.STRING.decode(byteBuf);
			String classItemIdentifierString = PacketCodecs.STRING.decode(byteBuf);
			boolean visibleWhenLocked = PacketCodecs.BOOL.decode(byteBuf);
			int descriptionListSize = PacketCodecs.INTEGER.decode(byteBuf);
			List<String> descriptionList = new ArrayList<>();
			for (int i = 0; i < descriptionListSize; i++) {
				descriptionList.add(PacketCodecs.STRING.decode(byteBuf));
			}
			int lockedDescriptionListSize = PacketCodecs.INTEGER.decode(byteBuf);
			List<String> lockedDescriptionList = new ArrayList<>();
			for (int i = 0; i < lockedDescriptionListSize; i++) {
				lockedDescriptionList.add(PacketCodecs.STRING.decode(byteBuf));
			}
			int upgradeUnlockStatesListSize = PacketCodecs.INTEGER.decode(byteBuf);
			List<UpgradeEntryGroup> upgradeUnlockStatesList = new ArrayList<>();
			for (int i = 0; i < upgradeUnlockStatesListSize; i++) {
				upgradeUnlockStatesList.add(UpgradeEntryGroup.PACKET_CODEC.decode(byteBuf));
			}
			return new RPGClass(
					unlockAdvancementIdentifierString,
					classItemIdentifierString,
					visibleWhenLocked,
					descriptionList,
					lockedDescriptionList,
					upgradeUnlockStatesList
			);
		}

		public void encode(ByteBuf byteBuf, RPGClass classUnlockStateData) {
			PacketCodecs.STRING.encode(byteBuf, classUnlockStateData.unlockAdvancementIdentifierString());
			PacketCodecs.STRING.encode(byteBuf, classUnlockStateData.classItemIdentifierString());
			PacketCodecs.BOOL.encode(byteBuf, classUnlockStateData.visibleWhenLocked());
			int descriptionListSize = classUnlockStateData.upgradeEntryGroupList().size();
			PacketCodecs.INTEGER.encode(byteBuf, descriptionListSize);
			for (int i = 0; i < descriptionListSize; i++) {
				PacketCodecs.STRING.encode(byteBuf, classUnlockStateData.descriptionList().get(i));
			}
			int lockedDescriptionList = classUnlockStateData.upgradeEntryGroupList().size();
			PacketCodecs.INTEGER.encode(byteBuf, lockedDescriptionList);
			for (int i = 0; i < lockedDescriptionList; i++) {
				PacketCodecs.STRING.encode(byteBuf, classUnlockStateData.lockedDescriptionList().get(i));
			}
			int upgradeEntryGroupListSize = classUnlockStateData.upgradeEntryGroupList().size();
			PacketCodecs.INTEGER.encode(byteBuf, upgradeEntryGroupListSize);
			for (int i = 0; i < upgradeEntryGroupListSize; i++) {
				UpgradeEntryGroup.PACKET_CODEC.encode(byteBuf, classUnlockStateData.upgradeEntryGroupList().get(i));
			}
		}
	};

	public RPGClass(
			String unlockAdvancementIdentifierString,
			String classItemIdentifierString,
			boolean visibleWhenLocked,
			List<String> descriptionList,
			List<String> lockedDescriptionList,
			List<UpgradeEntryGroup> upgradeEntryGroupList
	) {
		this.unlockAdvancementIdentifierString = unlockAdvancementIdentifierString != null ? unlockAdvancementIdentifierString : "";
		this.classItemIdentifierString = classItemIdentifierString != null ? classItemIdentifierString : "";
		this.visibleWhenLocked = visibleWhenLocked;
		this.descriptionList = descriptionList != null ? descriptionList : List.of();
		this.lockedDescriptionList = lockedDescriptionList != null ? lockedDescriptionList : List.of();
		this.upgradeEntryGroupList = upgradeEntryGroupList != null ? upgradeEntryGroupList : List.of();
	}


	public record UpgradeEntryGroup(
			List<UpgradeEntry> upgradeEntryList
	) {

		public static final Codec<UpgradeEntryGroup> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				UpgradeEntry.CODEC.listOf().optionalFieldOf("upgradeEntryList", List.of()).forGetter(x -> x.upgradeEntryList)
		).apply(instance, UpgradeEntryGroup::new));

		public static final PacketCodec<ByteBuf, UpgradeEntryGroup> PACKET_CODEC = new PacketCodec<>() {
			public UpgradeEntryGroup decode(ByteBuf byteBuf) {
				int listSize = PacketCodecs.INTEGER.decode(byteBuf);
				List<UpgradeEntry> upgradeEntryList = new ArrayList<>();
				for (int i = 0; i < listSize; i++) {
					upgradeEntryList.add(UpgradeEntry.PACKET_CODEC.decode(byteBuf));
				}
				return new UpgradeEntryGroup(upgradeEntryList);
			}

			public void encode(ByteBuf byteBuf, UpgradeEntryGroup updateEntryGroup) {
				int listSize = updateEntryGroup.upgradeEntryList().size();
				PacketCodecs.INTEGER.encode(byteBuf, listSize);
				for (int i = 0; i < listSize; i++) {
					UpgradeEntry.PACKET_CODEC.encode(byteBuf, updateEntryGroup.upgradeEntryList().get(i));
				}
			}
		};

		public UpgradeEntryGroup(
				List<UpgradeEntry> upgradeEntryList
		) {
			this.upgradeEntryList = upgradeEntryList != null ? upgradeEntryList : List.of();
		}

		public record UpgradeEntry(
				String upgradeIdentifier,
				String entryType,
				String unlockAdvancement,
				String spellIdentifierString,
				String attributeIdentifier,
				String attributeModifierIdentifier,
				double attributeModifierAmount,
				String attributeModifierOperation
		) {

			public static final Codec<UpgradeEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					Codec.STRING.optionalFieldOf("upgradeIdentifier", "").forGetter(x -> x.upgradeIdentifier),
					Codec.STRING.optionalFieldOf("entryType", Type.SPELL.asString()).forGetter(x -> x.entryType),
					Codec.STRING.optionalFieldOf("unlockAdvancement", "").forGetter(x -> x.unlockAdvancement),
					Codec.STRING.optionalFieldOf("spellIdentifierString", "").forGetter(x -> x.spellIdentifierString),
					Codec.STRING.optionalFieldOf("attributeIdentifier", "").forGetter(x -> x.attributeIdentifier),
					Codec.STRING.optionalFieldOf("attributeModifierIdentifier", "").forGetter(x -> x.attributeModifierIdentifier),
					Codec.DOUBLE.optionalFieldOf("attributeModifierAmount", 0.0).forGetter(x -> x.attributeModifierAmount),
					Codec.STRING.optionalFieldOf("attributeModifierOperation", EntityAttributeModifier.Operation.ADD_VALUE.asString()).forGetter(x -> x.attributeModifierOperation)
			).apply(instance, UpgradeEntry::new));

			public static final PacketCodec<ByteBuf, UpgradeEntry> PACKET_CODEC = new PacketCodec<>() {
				public UpgradeEntry decode(ByteBuf byteBuf) {
					return new UpgradeEntry(
							PacketCodecs.STRING.decode(byteBuf),
							PacketCodecs.STRING.decode(byteBuf),
							PacketCodecs.STRING.decode(byteBuf),
							PacketCodecs.STRING.decode(byteBuf),
							PacketCodecs.STRING.decode(byteBuf),
							PacketCodecs.STRING.decode(byteBuf),
							PacketCodecs.DOUBLE.decode(byteBuf),
							PacketCodecs.STRING.decode(byteBuf)
					);
				}

				public void encode(ByteBuf byteBuf, UpgradeEntry upgradeEntry) {
					PacketCodecs.STRING.encode(byteBuf, upgradeEntry.upgradeIdentifier());
					PacketCodecs.STRING.encode(byteBuf, upgradeEntry.entryType());
					PacketCodecs.STRING.encode(byteBuf, upgradeEntry.unlockAdvancement());
					PacketCodecs.STRING.encode(byteBuf, upgradeEntry.spellIdentifierString());
					PacketCodecs.STRING.encode(byteBuf, upgradeEntry.attributeIdentifier());
					PacketCodecs.STRING.encode(byteBuf, upgradeEntry.attributeModifierIdentifier());
					PacketCodecs.DOUBLE.encode(byteBuf, upgradeEntry.attributeModifierAmount());
					PacketCodecs.STRING.encode(byteBuf, upgradeEntry.attributeModifierOperation());
				}
			};

			public UpgradeEntry(
					String upgradeIdentifier,
					String entryType,
					String unlockAdvancement,
					String spellIdentifierString,
					String attributeIdentifier,
					String attributeModifierIdentifier,
					double attributeModifierAmount,
					String attributeModifierOperation
			) {
				this.upgradeIdentifier = upgradeIdentifier != null ? upgradeIdentifier : "";
				this.entryType = entryType != null ? entryType : "";
				this.unlockAdvancement = unlockAdvancement != null ? unlockAdvancement : "";
				this.spellIdentifierString = spellIdentifierString != null ? spellIdentifierString : "";
				this.attributeIdentifier = attributeIdentifier != null ? attributeIdentifier : "";
				this.attributeModifierIdentifier = attributeModifierIdentifier != null ? attributeModifierIdentifier : "";
				this.attributeModifierAmount = attributeModifierAmount;
				this.attributeModifierOperation = attributeModifierOperation != null ? attributeModifierOperation : EntityAttributeModifier.Operation.ADD_VALUE.asString();
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

				public static Optional<Type> byName(String name) {
					return Arrays.stream(Type.values()).filter(type -> type.asString().equals(name)).findFirst();
				}
			}
		}
	}
}
