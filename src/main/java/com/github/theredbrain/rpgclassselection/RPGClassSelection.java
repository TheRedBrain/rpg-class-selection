package com.github.theredbrain.rpgclassselection;

import com.github.theredbrain.rpgclassselection.compat.RPGInventoryCompat;
import com.github.theredbrain.rpgclassselection.component.type.ClassStateComponent;
import com.github.theredbrain.rpgclassselection.config.ServerConfig;
import com.github.theredbrain.rpgclassselection.registry.CustomDynamicRegistries;
import com.github.theredbrain.rpgclassselection.registry.DataComponentRegistry;
import com.github.theredbrain.rpgclassselection.registry.ScreenHandlerTypesRegistry;
import com.github.theredbrain.rpgclassselection.registry.ServerPacketRegistry;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import net.fabricmc.api.ModInitializer;
import net.minecraft.component.ComponentType;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RPGClassSelection implements ModInitializer {
	public static final String MOD_ID = "rpgclassselection";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static ServerConfig SERVER_CONFIG;

	public static ComponentType<ClassStateComponent> CLASS_STATE_COMPONENT_TYPE;

	public static AttributeModifierSlot getClassItemAttributeModifierSlot() {
		AttributeModifierSlot attributeModifierSlot = AttributeModifierSlot.ANY;
		attributeModifierSlot = RPGInventoryCompat.getClassItemAttributeModifierSlot();
		return attributeModifierSlot;
	}

	public static ItemStack getClassItemStack(PlayerEntity player) {
		ItemStack classItemStack = ItemStack.EMPTY;
		classItemStack = RPGInventoryCompat.getClassItemStack(player);
		return classItemStack;
	}

	public static void setClassItemStack(PlayerEntity player, ItemStack classItemStack) {
		RPGInventoryCompat.setClassItemStack(player, classItemStack);
	}

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing class selection!");
		SERVER_CONFIG = ConfigApiJava.registerAndLoadConfig(ServerConfig::new);

		CustomDynamicRegistries.init();
		DataComponentRegistry.init();
		ScreenHandlerTypesRegistry.registerAll();
		ServerPacketRegistry.init();
	}

	public static Identifier identifier(String path) {
		return Identifier.of(MOD_ID, path);
	}

	public static void info(String message) {
		LOGGER.info("[" + MOD_ID + "] [info]: " + message);
	}

	public static void warn(String message) {
		LOGGER.warn("[" + MOD_ID + "] [warn]: " + message);
	}

}