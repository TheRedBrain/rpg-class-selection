package com.github.theredbrain.rpgclassselection.registry;

import com.github.theredbrain.rpgclassselection.data.RPGClass;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class CustomDynamicRegistries {

	public static final RegistryKey<Registry<RPGClass>> RPG_CLASS_REGISTRY_KEY = RegistryKey.ofRegistry(Identifier.ofVanilla("rpg_classes"));

	public static void init() {
		DynamicRegistries.registerSynced(RPG_CLASS_REGISTRY_KEY, RPGClass.CODEC);
	}
}
