package com.github.theredbrain.rpgclassselection.registry;

import com.github.theredbrain.rpgclassselection.RPGClassSelection;
import com.github.theredbrain.rpgclassselection.screen.ClassSelectionScreenHandler;
import com.github.theredbrain.rpgclassselection.screen.RPGSeriesClassSelectionScreenHandler;
import com.github.theredbrain.rpgclassselection.screen.ThreeUpgradesClassSelectionScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;

public class ScreenHandlerTypesRegistry {
	public static final ScreenHandlerType<RPGSeriesClassSelectionScreenHandler> RPG_SERIES_CLASS_SELECTION_SCREEN_HANDLER = new ExtendedScreenHandlerType<>(RPGSeriesClassSelectionScreenHandler::new, ClassSelectionScreenHandler.ClassSelectionScreenData.PACKET_CODEC);
	public static final ScreenHandlerType<ThreeUpgradesClassSelectionScreenHandler> THREE_UPGRADES_CLASS_SELECTION_SCREEN_HANDLER = new ExtendedScreenHandlerType<>(ThreeUpgradesClassSelectionScreenHandler::new, ClassSelectionScreenHandler.ClassSelectionScreenData.PACKET_CODEC);

	public static void registerAll() {
		Registry.register(Registries.SCREEN_HANDLER, RPGClassSelection.identifier("rpg_series_class_selection"), RPG_SERIES_CLASS_SELECTION_SCREEN_HANDLER);
		Registry.register(Registries.SCREEN_HANDLER, RPGClassSelection.identifier("three_upgrades_class_selection"), THREE_UPGRADES_CLASS_SELECTION_SCREEN_HANDLER);
	}
}
