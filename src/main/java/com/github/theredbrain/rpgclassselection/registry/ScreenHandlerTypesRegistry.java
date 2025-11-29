package com.github.theredbrain.rpgclassselection.registry;

import com.github.theredbrain.rpgclassselection.RPGClassSelection;
import com.github.theredbrain.rpgclassselection.screen.ClassSelectionScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;

public class ScreenHandlerTypesRegistry {
	public static final ScreenHandlerType<ClassSelectionScreenHandler> CLASS_SELECTION_SCREEN_HANDLER = new ExtendedScreenHandlerType<>(ClassSelectionScreenHandler::new, ClassSelectionScreenHandler.ClassSelectionScreenData.PACKET_CODEC);

	public static void registerAll() {
		Registry.register(Registries.SCREEN_HANDLER, RPGClassSelection.identifier("class_selection"), CLASS_SELECTION_SCREEN_HANDLER);
	}
}
