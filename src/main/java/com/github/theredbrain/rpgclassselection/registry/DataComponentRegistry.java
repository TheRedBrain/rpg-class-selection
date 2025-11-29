package com.github.theredbrain.rpgclassselection.registry;

import com.github.theredbrain.rpgclassselection.RPGClassSelection;
import com.github.theredbrain.rpgclassselection.component.type.ClassStateComponent;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class DataComponentRegistry {
	static {
		RPGClassSelection.CLASS_STATE_COMPONENT_TYPE = Registry.register(
				Registries.DATA_COMPONENT_TYPE,
				RPGClassSelection.identifier("class_state"),
				ComponentType.<ClassStateComponent>builder().codec(ClassStateComponent.CODEC).packetCodec(ClassStateComponent.PACKET_CODEC).build()
		);
	}

	public static void init() {
	}
}
