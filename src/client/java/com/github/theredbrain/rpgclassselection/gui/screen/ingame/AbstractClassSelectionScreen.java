package com.github.theredbrain.rpgclassselection.gui.screen.ingame;

import com.github.theredbrain.rpgclassselection.component.type.ClassStateComponent;
import com.github.theredbrain.rpgclassselection.data.RPGClass;
import com.github.theredbrain.rpgclassselection.network.packet.UpdateClassPacket;
import com.github.theredbrain.rpgclassselection.screen.ClassSelectionScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public abstract class AbstractClassSelectionScreen extends HandledScreen<ClassSelectionScreenHandler> {

	protected ClassStateComponent.ActiveClassState newActiveClassState;

	protected int currentClassIndex;
	protected String activeClassDescription = "";
	protected final List<Integer> currentUpgradeIndexList = new ArrayList<>();

	public AbstractClassSelectionScreen(ClassSelectionScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	protected void cycleClassIndexBackwards() {
		int index = this.currentClassIndex;
		List<RPGClass> dataList = this.handler.getRpgClassList();
		List<ClassSelectionScreenHandler.ClassSelectionScreenData.ClassUnlockStateData> stateList = this.handler.getClassUnlockStateDataList();
		int listSize = dataList.size();
		while (true) {
			index = index - 1;
			if (index < 0) {
				index = listSize - 1;
			}
			if (index == 0 || stateList.get(index).classUnlockState() || dataList.get(index).visible_when_locked()) {
				this.currentClassIndex = index;
				break;
			}
		}
	}

	protected void cycleClassIndexForwards() {
		int index = this.currentClassIndex;
		List<RPGClass> dataList = this.handler.getRpgClassList();
		List<ClassSelectionScreenHandler.ClassSelectionScreenData.ClassUnlockStateData> stateList = this.handler.getClassUnlockStateDataList();
		int listSize = dataList.size();
		while (true) {
			index = index + 1;
			if (index >= listSize) {
				index = 0;
			}
			if (index == 0 || stateList.get(index).classUnlockState() || dataList.get(index).visible_when_locked()) {
				this.currentClassIndex = index;
				break;
			}
		}
	}

	protected void cycleUpgradeIndexBackwards(int upgradeIndex) {
		if (upgradeIndex < this.currentUpgradeIndexList.size()) {
			int index = this.currentUpgradeIndexList.get(upgradeIndex);
			List<ClassSelectionScreenHandler.ClassSelectionScreenData.ClassUnlockStateData.UpgradeUnlockStateData> unlockStateList = this.handler.getClassUnlockStateDataList().get(this.currentClassIndex).upgradeUnlockStateDataList();
			if (upgradeIndex < unlockStateList.size()) {
				List<Boolean> stateList = this.handler.getClassUnlockStateDataList().get(this.currentClassIndex).upgradeUnlockStateDataList().get(upgradeIndex).upgradeUnlockStatesList();
				int stateListSize = stateList.size();
				while (true) {
					index = index - 1;
					if (index < 0) {
						index = stateListSize - 1;
					}
					if (index == 0 || stateList.get(index)) {
						this.currentUpgradeIndexList.set(upgradeIndex, index);
						break;
					}
				}
			}
		}
	}

	protected void cycleUpgradeIndexForwards(int upgradeIndex) {
		if (upgradeIndex < this.currentUpgradeIndexList.size()) {
			int index = this.currentUpgradeIndexList.get(upgradeIndex);
			List<ClassSelectionScreenHandler.ClassSelectionScreenData.ClassUnlockStateData.UpgradeUnlockStateData> unlockStateList = this.handler.getClassUnlockStateDataList().get(this.currentClassIndex).upgradeUnlockStateDataList();
			if (upgradeIndex < unlockStateList.size()) {
				List<Boolean> stateList = unlockStateList.get(upgradeIndex).upgradeUnlockStatesList();
				int stateListSize = stateList.size();
				while (true) {
					index = index + 1;
					if (index >= stateListSize) {
						index = 0;
					}
					if (index == 0 || stateList.get(index)) {
						this.currentUpgradeIndexList.set(upgradeIndex, index);
						break;
					}
				}
			}
		}
	}

	protected void cycleClassBackwards() {
		this.cycleClassIndexBackwards();
		this.updateCurrentUpgradeIndexes();
		this.updateActiveClassState();
	}

	protected void cycleClassForwards() {
		this.cycleClassIndexForwards();
		this.updateCurrentUpgradeIndexes();
		this.updateActiveClassState();
	}

	protected void cycleUpgradeBackwards(int index) {
		this.cycleUpgradeIndexBackwards(index);
		this.updateActiveClassState();
	}

	protected void cycleUpgradeForwards(int index) {
		this.cycleUpgradeIndexForwards(index);
		this.updateActiveClassState();
	}

	protected void updateCurrentUpgradeIndexes() {

		this.currentUpgradeIndexList.clear();
		RPGClass rpgClass = this.handler.getRpgClassList().get(this.currentClassIndex);
		if (Objects.equals(rpgClass.class_identifier(), this.handler.getActiveClassState().activeClassIdentifier())) {
			List<String> list = this.handler.getActiveClassState().activeUpgradeIdentifierList();
			for (int i = 0; i < list.size(); i++) {
				String upgradeIdentifier = list.get(i);
				if (!upgradeIdentifier.isEmpty()) {
					List<RPGClass.UpgradeEntryGroup.UpgradeEntry> upgradeEntryList = rpgClass.upgrade_entry_group_list().get(i).upgrade_entry_list();
					for (int j = 0; j < upgradeEntryList.size(); j++) {

						RPGClass.UpgradeEntryGroup.UpgradeEntry upgradeEntry = upgradeEntryList.get(j);
						if (upgradeIdentifier.equals(upgradeEntry.upgrade_identifier())) {
							this.currentUpgradeIndexList.add(j);
							break;
						}
					}
					continue;
				}
				this.currentUpgradeIndexList.add(0);
			}
		} else {
			for (int i = 0; i < rpgClass.upgrade_entry_group_list().size(); i++) {
				this.currentUpgradeIndexList.add(0);
			}
		}
	}

	protected void updateActiveClassState() {
		RPGClass rpgClass = this.handler.getRpgClassList().get(this.currentClassIndex);
		List<String> activeUpgradeIdentifierList = new ArrayList<>();
		for (int i = 0; i < this.currentUpgradeIndexList.size(); i++) {
			if (i < rpgClass.upgrade_entry_group_list().size()) {
				activeUpgradeIdentifierList.add(rpgClass.upgrade_entry_group_list().get(i).upgrade_entry_list().get(this.currentUpgradeIndexList.get(i)).upgrade_identifier());
			}
		}
		this.newActiveClassState = new ClassStateComponent.ActiveClassState(
				rpgClass.class_identifier(),
				activeUpgradeIdentifierList
		);

		if (this.handler.getClassUnlockStateDataList().get(this.currentClassIndex).classUnlockState()) {
			this.activeClassDescription = rpgClass.description();
		} else {
			this.activeClassDescription = rpgClass.locked_description();
		}

		this.updateWidgets();
	}

	protected void chooseClass() {
		ClientPlayNetworking.send(new UpdateClassPacket(this.newActiveClassState));
	}

	protected void updateWidgets() {

	}

}
