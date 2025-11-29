package com.github.theredbrain.rpgclassselection;

import com.github.theredbrain.rpgclassselection.gui.screen.ingame.ClassSelectionScreen;
import com.github.theredbrain.rpgclassselection.registry.KeyBindingsRegistry;
import com.github.theredbrain.rpgclassselection.registry.ScreenHandlerTypesRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class RPGClassSelectionClient implements ClientModInitializer {
//	public static ClientConfig CLIENT_CONFIG;

	@Override
	public void onInitializeClient() {
//		CLIENT_CONFIG = ConfigApiJava.registerAndLoadConfig(ClientConfig::new, RegisterType.CLIENT);

		KeyBindingsRegistry.registerKeyBindings();
		registerScreens();
	}


	private void registerScreens() {
		HandledScreens.register(ScreenHandlerTypesRegistry.CLASS_SELECTION_SCREEN_HANDLER, ClassSelectionScreen::new);
	}
}