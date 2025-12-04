package com.github.theredbrain.rpgclassselection.screen;

import com.github.theredbrain.rpgclassselection.component.type.ClassStateComponent;
import com.github.theredbrain.rpgclassselection.data.RPGClass;
import com.github.theredbrain.rpgclassselection.registry.ScreenHandlerTypesRegistry;
import net.minecraft.entity.player.PlayerInventory;

import java.util.List;

public class ThreeUpgradesClassSelectionScreenHandler extends ClassSelectionScreenHandler {

	public ThreeUpgradesClassSelectionScreenHandler(int syncId, PlayerInventory playerInventory, ClassSelectionScreenData data) {
		this(syncId, playerInventory, data.initialClassIndex(), data.activeClassState(), data.classUnlockStateDataList(), data.rpgClassList());
	}

	public ThreeUpgradesClassSelectionScreenHandler(int syncId, PlayerInventory playerInventory, int initialClassIndex, ClassStateComponent.ActiveClassState activeClassState, List<ClassSelectionScreenHandler.ClassSelectionScreenData.ClassUnlockStateData> classUnlockStateDataList, List<RPGClass> rpgClassList) {
		super(ScreenHandlerTypesRegistry.THREE_UPGRADES_CLASS_SELECTION_SCREEN_HANDLER, syncId, playerInventory, initialClassIndex, activeClassState, classUnlockStateDataList, rpgClassList);
	}

}