package com.github.theredbrain.rpgclassselection.screen;

import com.github.theredbrain.rpgclassselection.component.type.ClassStateComponent;
import com.github.theredbrain.rpgclassselection.data.DisplayedRPGClass;
import com.github.theredbrain.rpgclassselection.registry.ScreenHandlerTypesRegistry;
import net.minecraft.entity.player.PlayerInventory;

import java.util.List;

public class RPGSeriesClassSelectionScreenHandler extends ClassSelectionScreenHandler {

	public RPGSeriesClassSelectionScreenHandler(int syncId, PlayerInventory playerInventory, ClassSelectionScreenData data) {
		this(syncId, playerInventory, data.initialClassIndex(), data.activeClassState(), data.classUnlockStateDataList(), data.displayedRPGClassList());
	}

	public RPGSeriesClassSelectionScreenHandler(int syncId, PlayerInventory playerInventory, int initialClassIndex, ClassStateComponent.ActiveClassState activeClassState, List<ClassSelectionScreenData.ClassUnlockStateData> classUnlockStateDataList, List<DisplayedRPGClass> displayedRPGClassList) {
		super(ScreenHandlerTypesRegistry.RPG_SERIES_CLASS_SELECTION_SCREEN_HANDLER, syncId, playerInventory, initialClassIndex, activeClassState, classUnlockStateDataList, displayedRPGClassList);
	}

}