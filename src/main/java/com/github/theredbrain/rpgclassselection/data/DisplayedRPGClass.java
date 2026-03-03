package com.github.theredbrain.rpgclassselection.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

import java.util.ArrayList;
import java.util.List;

public record DisplayedRPGClass(
		String class_identifier,
		String description,
//		String locked_description,
		List<DisplayedUpgradeEntryGroup> displayed_upgrade_entry_group_list
) {

	public static final DisplayedRPGClass DEFAULT = new DisplayedRPGClass("rpgclassselection:empty_class", "class_selection_screen.rpgclassselection.empty_class.description", List.of());

	public static final Codec<DisplayedRPGClass> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.STRING.optionalFieldOf("class_identifier", "").forGetter(x -> x.class_identifier),
			Codec.STRING.optionalFieldOf("description", "").forGetter(x -> x.description),
//			Codec.STRING.optionalFieldOf("locked_description", "").forGetter(x -> x.locked_description),
			DisplayedUpgradeEntryGroup.CODEC.listOf().optionalFieldOf("displayed_upgrade_entry_group_list", List.of()).forGetter(x -> x.displayed_upgrade_entry_group_list)
	).apply(instance, DisplayedRPGClass::new));

	public static final PacketCodec<ByteBuf, DisplayedRPGClass> PACKET_CODEC = new PacketCodec<>() {
		public DisplayedRPGClass decode(ByteBuf byteBuf) {
			String class_identifier = PacketCodecs.STRING.decode(byteBuf);
			String description = PacketCodecs.STRING.decode(byteBuf);
			int upgradeUnlockStatesListSize = PacketCodecs.INTEGER.decode(byteBuf);
			List<DisplayedUpgradeEntryGroup> displayed_upgrade_entry_group_list = new ArrayList<>();
			for (int i = 0; i < upgradeUnlockStatesListSize; i++) {
				displayed_upgrade_entry_group_list.add(DisplayedUpgradeEntryGroup.PACKET_CODEC.decode(byteBuf));
			}
			return new DisplayedRPGClass(
					class_identifier,
					description,
					displayed_upgrade_entry_group_list
			);
		}

		public void encode(ByteBuf byteBuf, DisplayedRPGClass classUnlockStateData) {
			PacketCodecs.STRING.encode(byteBuf, classUnlockStateData.class_identifier());
			PacketCodecs.STRING.encode(byteBuf, classUnlockStateData.description());
//			PacketCodecs.STRING.encode(byteBuf, classUnlockStateData.locked_description());
			int upgradeEntryGroupListSize = classUnlockStateData.displayed_upgrade_entry_group_list().size();
			PacketCodecs.INTEGER.encode(byteBuf, upgradeEntryGroupListSize);
			for (int i = 0; i < upgradeEntryGroupListSize; i++) {
				DisplayedUpgradeEntryGroup.PACKET_CODEC.encode(byteBuf, classUnlockStateData.displayed_upgrade_entry_group_list().get(i));
			}
		}
	};

	public DisplayedRPGClass(
			String class_identifier,
			String description,
//			String locked_description,
			List<DisplayedUpgradeEntryGroup> displayed_upgrade_entry_group_list
	) {
		this.class_identifier = class_identifier != null ? class_identifier : "";
		this.description = description != null ? description : "";
//		this.locked_description = locked_description != null ? locked_description : "";
		this.displayed_upgrade_entry_group_list = displayed_upgrade_entry_group_list != null ? displayed_upgrade_entry_group_list : List.of();
	}


	public record DisplayedUpgradeEntryGroup(
			List<DisplayedUpgradeEntry> displayed_upgrade_entry_list
	) {

		public static final Codec<DisplayedUpgradeEntryGroup> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				DisplayedUpgradeEntry.CODEC.listOf().optionalFieldOf("displayed_upgrade_entry_list", List.of()).forGetter(x -> x.displayed_upgrade_entry_list)
		).apply(instance, DisplayedUpgradeEntryGroup::new));

		public static final PacketCodec<ByteBuf, DisplayedUpgradeEntryGroup> PACKET_CODEC = new PacketCodec<>() {
			public DisplayedUpgradeEntryGroup decode(ByteBuf byteBuf) {
				int listSize = PacketCodecs.INTEGER.decode(byteBuf);
				List<DisplayedUpgradeEntry> displayed_upgrade_entry_list = new ArrayList<>();
				for (int i = 0; i < listSize; i++) {
					displayed_upgrade_entry_list.add(DisplayedUpgradeEntry.PACKET_CODEC.decode(byteBuf));
				}
				return new DisplayedUpgradeEntryGroup(displayed_upgrade_entry_list);
			}

			public void encode(ByteBuf byteBuf, DisplayedUpgradeEntryGroup updateEntryGroup) {
				int listSize = updateEntryGroup.displayed_upgrade_entry_list().size();
				PacketCodecs.INTEGER.encode(byteBuf, listSize);
				for (int i = 0; i < listSize; i++) {
					DisplayedUpgradeEntry.PACKET_CODEC.encode(byteBuf, updateEntryGroup.displayed_upgrade_entry_list().get(i));
				}
			}
		};

		public DisplayedUpgradeEntryGroup(
				List<DisplayedUpgradeEntry> displayed_upgrade_entry_list
		) {
			this.displayed_upgrade_entry_list = displayed_upgrade_entry_list != null ? displayed_upgrade_entry_list : List.of();
		}

		public record DisplayedUpgradeEntry(
				String upgrade_identifier,
				String title,
				String icon_path,
				List<UpgradeEntryComponent> component_list
		) {
			public static final DisplayedUpgradeEntry DEFAULT = new DisplayedUpgradeEntry("", "class_selection_screen.empty_upgrade.description", "", new ArrayList<>());

			public static final Codec<DisplayedUpgradeEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					Codec.STRING.optionalFieldOf("upgrade_identifier", "").forGetter(x -> x.upgrade_identifier),
					Codec.STRING.optionalFieldOf("title", "").forGetter(x -> x.title),
					Codec.STRING.optionalFieldOf("icon_path", "").forGetter(x -> x.icon_path),
					UpgradeEntryComponent.CODEC.listOf().optionalFieldOf("component_list", List.of()).forGetter(x -> x.component_list)
			).apply(instance, DisplayedUpgradeEntry::new));

			public static final PacketCodec<ByteBuf, DisplayedUpgradeEntry> PACKET_CODEC = new PacketCodec<>() {
				public DisplayedUpgradeEntry decode(ByteBuf byteBuf) {
					String upgrade_identifier = PacketCodecs.STRING.decode(byteBuf);
					String title = PacketCodecs.STRING.decode(byteBuf);
					String icon_path = PacketCodecs.STRING.decode(byteBuf);
					int listSize = PacketCodecs.INTEGER.decode(byteBuf);
					List<UpgradeEntryComponent> component_list = new ArrayList<>();
					for (int i = 0; i < listSize; i++) {
						component_list.add(UpgradeEntryComponent.PACKET_CODEC.decode(byteBuf));
					}
					return new DisplayedUpgradeEntry(
							upgrade_identifier,
							title,
							icon_path,
							component_list
					);
				}

				public void encode(ByteBuf byteBuf, DisplayedUpgradeEntry upgradeEntry) {
					PacketCodecs.STRING.encode(byteBuf, upgradeEntry.upgrade_identifier());
					PacketCodecs.STRING.encode(byteBuf, upgradeEntry.title());
					PacketCodecs.STRING.encode(byteBuf, upgradeEntry.icon_path());
					int componentListSize = upgradeEntry.component_list().size();
					PacketCodecs.INTEGER.encode(byteBuf, componentListSize);
					for (int i = 0; i < componentListSize; i++) {
						UpgradeEntryComponent.PACKET_CODEC.encode(byteBuf, upgradeEntry.component_list().get(i));
					}
				}
			};

			public DisplayedUpgradeEntry(
					String upgrade_identifier,
					String title,
					String icon_path,
					List<UpgradeEntryComponent> component_list
			) {
				this.upgrade_identifier = upgrade_identifier != null ? upgrade_identifier : "";
				this.title = title != null ? title : "";
				this.icon_path = icon_path != null ? icon_path : "";
				this.component_list = component_list != null ? component_list : List.of();
			}
		}
	}
}
