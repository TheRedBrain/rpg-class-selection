package com.github.theredbrain.rpgclassselection.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

import java.util.ArrayList;
import java.util.List;

public record RPGClass(
		String class_identifier,
		String unlock_advancement_identifier,
		boolean visible_when_locked,
		String description,
		String locked_description,
		List<UpgradeEntryGroup> upgrade_entry_group_list
) {

	public static final RPGClass DEFAULT = new RPGClass("rpgclassselection:empty_class", "", true, "class_selection_screen.rpgclassselection.empty_class.description", "", List.of());

	public static final Codec<RPGClass> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.STRING.optionalFieldOf("class_identifier", "").forGetter(x -> x.class_identifier),
			Codec.STRING.optionalFieldOf("unlock_advancement_identifier", "").forGetter(x -> x.unlock_advancement_identifier),
			Codec.BOOL.optionalFieldOf("visible_when_locked", true).forGetter(x -> x.visible_when_locked),
			Codec.STRING.optionalFieldOf("description", "").forGetter(x -> x.description),
			Codec.STRING.optionalFieldOf("locked_description", "").forGetter(x -> x.locked_description),
			UpgradeEntryGroup.CODEC.listOf().optionalFieldOf("upgrade_entry_group_list", List.of()).forGetter(x -> x.upgrade_entry_group_list)
	).apply(instance, RPGClass::new));

	public static final PacketCodec<ByteBuf, RPGClass> PACKET_CODEC = new PacketCodec<>() {
		public RPGClass decode(ByteBuf byteBuf) {
			String class_identifier = PacketCodecs.STRING.decode(byteBuf);
			String unlock_advancement_identifier = PacketCodecs.STRING.decode(byteBuf);
			boolean visible_when_locked = PacketCodecs.BOOL.decode(byteBuf);
			String description = PacketCodecs.STRING.decode(byteBuf);
			String locked_description = PacketCodecs.STRING.decode(byteBuf);
			int upgradeUnlockStatesListSize = PacketCodecs.INTEGER.decode(byteBuf);
			List<UpgradeEntryGroup> upgrade_entry_group_list = new ArrayList<>();
			for (int i = 0; i < upgradeUnlockStatesListSize; i++) {
				upgrade_entry_group_list.add(UpgradeEntryGroup.PACKET_CODEC.decode(byteBuf));
			}
			return new RPGClass(
					class_identifier,
					unlock_advancement_identifier,
					visible_when_locked,
					description,
					locked_description,
					upgrade_entry_group_list
			);
		}

		public void encode(ByteBuf byteBuf, RPGClass classUnlockStateData) {
			PacketCodecs.STRING.encode(byteBuf, classUnlockStateData.class_identifier());
			PacketCodecs.STRING.encode(byteBuf, classUnlockStateData.unlock_advancement_identifier());
			PacketCodecs.BOOL.encode(byteBuf, classUnlockStateData.visible_when_locked());
			PacketCodecs.STRING.encode(byteBuf, classUnlockStateData.description());
			PacketCodecs.STRING.encode(byteBuf, classUnlockStateData.locked_description());
			int upgradeEntryGroupListSize = classUnlockStateData.upgrade_entry_group_list().size();
			PacketCodecs.INTEGER.encode(byteBuf, upgradeEntryGroupListSize);
			for (int i = 0; i < upgradeEntryGroupListSize; i++) {
				UpgradeEntryGroup.PACKET_CODEC.encode(byteBuf, classUnlockStateData.upgrade_entry_group_list().get(i));
			}
		}
	};

	public RPGClass(
			String class_identifier,
			String unlock_advancement_identifier,
			boolean visible_when_locked,
			String description,
			String locked_description,
			List<UpgradeEntryGroup> upgrade_entry_group_list
	) {
		this.class_identifier = class_identifier != null ? class_identifier : "";
		this.unlock_advancement_identifier = unlock_advancement_identifier != null ? unlock_advancement_identifier : "";
		this.visible_when_locked = visible_when_locked;
		this.description = description != null ? description : "";
		this.locked_description = locked_description != null ? locked_description : "";
		this.upgrade_entry_group_list = upgrade_entry_group_list != null ? upgrade_entry_group_list : List.of();
	}


	public record UpgradeEntryGroup(
			List<UpgradeEntry> upgrade_entry_list
	) {

		public static final Codec<UpgradeEntryGroup> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				UpgradeEntry.CODEC.listOf().optionalFieldOf("upgrade_entry_list", List.of()).forGetter(x -> x.upgrade_entry_list)
		).apply(instance, UpgradeEntryGroup::new));

		public static final PacketCodec<ByteBuf, UpgradeEntryGroup> PACKET_CODEC = new PacketCodec<>() {
			public UpgradeEntryGroup decode(ByteBuf byteBuf) {
				int listSize = PacketCodecs.INTEGER.decode(byteBuf);
				List<UpgradeEntry> upgrade_entry_list = new ArrayList<>();
				for (int i = 0; i < listSize; i++) {
					upgrade_entry_list.add(UpgradeEntry.PACKET_CODEC.decode(byteBuf));
				}
				return new UpgradeEntryGroup(upgrade_entry_list);
			}

			public void encode(ByteBuf byteBuf, UpgradeEntryGroup updateEntryGroup) {
				int listSize = updateEntryGroup.upgrade_entry_list().size();
				PacketCodecs.INTEGER.encode(byteBuf, listSize);
				for (int i = 0; i < listSize; i++) {
					UpgradeEntry.PACKET_CODEC.encode(byteBuf, updateEntryGroup.upgrade_entry_list().get(i));
				}
			}
		};

		public UpgradeEntryGroup(
				List<UpgradeEntry> upgrade_entry_list
		) {
			this.upgrade_entry_list = upgrade_entry_list != null ? upgrade_entry_list : List.of();
		}

		public record UpgradeEntry(
				String upgrade_identifier,
				String unlock_advancement_identifier,
				boolean visible_when_locked,
				String title,
				String icon_path,
				List<UpgradeEntryComponent> component_list
		) {
			public static final UpgradeEntry DEFAULT = new UpgradeEntry("", "", true, "class_selection_screen.empty_upgrade.description", "", new ArrayList<>());

			public static final Codec<UpgradeEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					Codec.STRING.optionalFieldOf("upgrade_identifier", "").forGetter(x -> x.upgrade_identifier),
					Codec.STRING.optionalFieldOf("unlock_advancement_identifier", "").forGetter(x -> x.unlock_advancement_identifier),
					Codec.BOOL.optionalFieldOf("visible_when_locked", true).forGetter(x -> x.visible_when_locked),
					Codec.STRING.optionalFieldOf("title", "").forGetter(x -> x.title),
					Codec.STRING.optionalFieldOf("icon_path", "").forGetter(x -> x.icon_path),
					UpgradeEntryComponent.CODEC.listOf().optionalFieldOf("component_list", List.of()).forGetter(x -> x.component_list)
			).apply(instance, UpgradeEntry::new));

			public static final PacketCodec<ByteBuf, UpgradeEntry> PACKET_CODEC = new PacketCodec<>() {
				public UpgradeEntry decode(ByteBuf byteBuf) {
					String upgrade_identifier = PacketCodecs.STRING.decode(byteBuf);
					String unlock_advancement_identifier = PacketCodecs.STRING.decode(byteBuf);
					boolean visible_when_locked = PacketCodecs.BOOL.decode(byteBuf);
					String title = PacketCodecs.STRING.decode(byteBuf);
					String icon_path = PacketCodecs.STRING.decode(byteBuf);
					int listSize = PacketCodecs.INTEGER.decode(byteBuf);
					List<UpgradeEntryComponent> component_list = new ArrayList<>();
					for (int i = 0; i < listSize; i++) {
						component_list.add(UpgradeEntryComponent.PACKET_CODEC.decode(byteBuf));
					}
					return new UpgradeEntry(
							upgrade_identifier,
							unlock_advancement_identifier,
							visible_when_locked,
							title,
							icon_path,
							component_list
					);
				}

				public void encode(ByteBuf byteBuf, UpgradeEntry upgradeEntry) {
					PacketCodecs.STRING.encode(byteBuf, upgradeEntry.upgrade_identifier());
					PacketCodecs.STRING.encode(byteBuf, upgradeEntry.unlock_advancement_identifier());
					PacketCodecs.BOOL.encode(byteBuf, upgradeEntry.visible_when_locked());
					PacketCodecs.STRING.encode(byteBuf, upgradeEntry.title());
					PacketCodecs.STRING.encode(byteBuf, upgradeEntry.icon_path());
					int componentListSize = upgradeEntry.component_list().size();
					PacketCodecs.INTEGER.encode(byteBuf, componentListSize);
					for (int i = 0; i < componentListSize; i++) {
						UpgradeEntryComponent.PACKET_CODEC.encode(byteBuf, upgradeEntry.component_list().get(i));
					}
				}
			};

			public UpgradeEntry(
					String upgrade_identifier,
					String unlock_advancement_identifier,
					boolean visible_when_locked,
					String title,
					String icon_path,
					List<UpgradeEntryComponent> component_list
			) {
				this.upgrade_identifier = upgrade_identifier != null ? upgrade_identifier : "";
				this.unlock_advancement_identifier = unlock_advancement_identifier != null ? unlock_advancement_identifier : "";
				this.visible_when_locked = visible_when_locked;
				this.title = title != null ? title : "";
				this.icon_path = icon_path != null ? icon_path : "";
				this.component_list = component_list != null ? component_list : List.of();
			}
		}
	}
}
