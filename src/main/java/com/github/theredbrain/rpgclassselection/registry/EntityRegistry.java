package com.github.theredbrain.rpgclassselection.registry;

import com.github.theredbrain.rpgclassselection.RPGClassSelection;
import com.github.theredbrain.rpgclassselection.block.entity.RPGClassSelectionBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class EntityRegistry {

	public static final BlockEntityType<RPGClassSelectionBlockEntity> RPG_CLASS_SELECTION_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
			RPGClassSelection.identifier("rpg_class_selection_block"),
			FabricBlockEntityTypeBuilder.create(RPGClassSelectionBlockEntity::new, BlockRegistry.RPG_CLASS_SELECTION_BLOCK).build());

	public static void init() {
	}

}
