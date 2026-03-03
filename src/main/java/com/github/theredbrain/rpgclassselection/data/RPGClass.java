package com.github.theredbrain.rpgclassselection.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.predicate.entity.EntityPredicate;

import java.util.List;
import java.util.Optional;

public record RPGClass(
		String class_identifier,
		Optional<EntityPredicate> unlock_predicate,
		boolean visible_when_locked,
		String description,
		String locked_description,
		List<UpgradeEntryGroup> upgrade_entry_group_list
) {

	public static final Codec<RPGClass> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.STRING.optionalFieldOf("class_identifier", "").forGetter(x -> x.class_identifier),
			EntityPredicate.CODEC.optionalFieldOf("unlock_predicate").forGetter(x -> x.unlock_predicate),
			Codec.BOOL.optionalFieldOf("visible_when_locked", true).forGetter(x -> x.visible_when_locked),
			Codec.STRING.optionalFieldOf("description", "").forGetter(x -> x.description),
			Codec.STRING.optionalFieldOf("locked_description", "").forGetter(x -> x.locked_description),
			UpgradeEntryGroup.CODEC.listOf().optionalFieldOf("upgrade_entry_group_list", List.of()).forGetter(x -> x.upgrade_entry_group_list)
	).apply(instance, RPGClass::new));

	public RPGClass(
			String class_identifier,
			Optional<EntityPredicate> unlock_predicate,
			boolean visible_when_locked,
			String description,
			String locked_description,
			List<UpgradeEntryGroup> upgrade_entry_group_list
	) {
		this.class_identifier = class_identifier != null ? class_identifier : "";
		this.unlock_predicate = unlock_predicate;
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

		public UpgradeEntryGroup(
				List<UpgradeEntry> upgrade_entry_list
		) {
			this.upgrade_entry_list = upgrade_entry_list != null ? upgrade_entry_list : List.of();
		}

		public record UpgradeEntry(
				String upgrade_identifier,
				Optional<EntityPredicate> unlock_predicate,
				boolean visible_when_locked,
				String title,
				String icon_path,
				List<UpgradeEntryComponent> component_list
		) {

			public static final Codec<UpgradeEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					Codec.STRING.optionalFieldOf("upgrade_identifier", "").forGetter(x -> x.upgrade_identifier),
					EntityPredicate.CODEC.optionalFieldOf("unlock_predicate").forGetter(x -> x.unlock_predicate),
					Codec.BOOL.optionalFieldOf("visible_when_locked", true).forGetter(x -> x.visible_when_locked),
					Codec.STRING.optionalFieldOf("title", "").forGetter(x -> x.title),
					Codec.STRING.optionalFieldOf("icon_path", "").forGetter(x -> x.icon_path),
					UpgradeEntryComponent.CODEC.listOf().optionalFieldOf("component_list", List.of()).forGetter(x -> x.component_list)
			).apply(instance, UpgradeEntry::new));

			public UpgradeEntry(
					String upgrade_identifier,
					Optional<EntityPredicate> unlock_predicate,
					boolean visible_when_locked,
					String title,
					String icon_path,
					List<UpgradeEntryComponent> component_list
			) {
				this.upgrade_identifier = upgrade_identifier != null ? upgrade_identifier : "";
				this.unlock_predicate = unlock_predicate;
				this.visible_when_locked = visible_when_locked;
				this.title = title != null ? title : "";
				this.icon_path = icon_path != null ? icon_path : "";
				this.component_list = component_list != null ? component_list : List.of();
			}
		}
	}
}
