package com.github.theredbrain.rpgclassselection.gui.screen.ingame;

import com.github.theredbrain.rpgclassselection.RPGClassSelection;
import com.github.theredbrain.rpgclassselection.component.type.ClassStateComponent;
import com.github.theredbrain.rpgclassselection.data.RPGClass;
import com.github.theredbrain.rpgclassselection.network.packet.UpdateClassPacket;
import com.github.theredbrain.rpgclassselection.screen.ClassSelectionScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public abstract class AbstractClassSelectionScreen extends HandledScreen<ClassSelectionScreenHandler> {
	protected static final Identifier SCROLL_BAR_BACKGROUND_TEXTURE = RPGClassSelection.identifier("scroll_bar/scroll_bar_background");
	protected static final Identifier SCROLLER_TEXTURE = RPGClassSelection.identifier("scroll_bar/scroller_vertical_6_7");
	public static Identifier BACKGROUND_TEXTURE;
	protected static final Text CHOOSE_CLASS_BUTTON_LABEL_TEXT = Text.translatable("class_selection_screen.choose_class_button_label");

	protected ClassStateComponent.ActiveClassState newActiveClassState;

	protected int currentClassIndex;
	protected String activeClassDescription = "";
	protected final List<Integer> currentUpgradeIndexList = new ArrayList<>();

	public AbstractClassSelectionScreen(ClassSelectionScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	@Override
	public void resize(MinecraftClient client, int width, int height) {
		ClassStateComponent.ActiveClassState var = this.newActiveClassState;
		int integer = this.currentClassIndex;
		String string = this.activeClassDescription;
		List<Integer> list = new ArrayList<>(this.currentUpgradeIndexList);
		this.init(client, width, height);
		this.newActiveClassState = var;
		this.currentClassIndex = integer;
		this.activeClassDescription = string;
		this.currentUpgradeIndexList.clear();
		this.currentUpgradeIndexList.addAll(list);
		this.updateWidgets();
	}

	protected void cycleClassIndexBackwards() {
		int index = this.currentClassIndex;
		index = index - 1;
		if (index < 0) {
			index = this.handler.getRpgClassList().size() - 1;
		}
		this.currentClassIndex = index;
	}

	protected void cycleClassIndexForwards() {
		int index = this.currentClassIndex;
		index = index + 1;
		if (index >= this.handler.getRpgClassList().size()) {
			index = 0;
		}
		this.currentClassIndex = index;
	}

	protected void cycleUpgradeIndexBackwards(int upgradeIndex) {
		if (upgradeIndex < this.currentUpgradeIndexList.size()) {
			int index = this.currentUpgradeIndexList.get(upgradeIndex);
			List<ClassSelectionScreenHandler.ClassSelectionScreenData.ClassUnlockStateData.UpgradeUnlockStateData> unlockStateList = this.handler.getClassUnlockStateDataList().get(this.currentClassIndex).upgradeUnlockStateDataList();
			if (upgradeIndex < unlockStateList.size()) {
				int stateListSize = unlockStateList.get(upgradeIndex).upgradeUnlockStatesList().size();
				index = index - 1;
				if (index < 0) {
					index = stateListSize - 1;
				}
				this.currentUpgradeIndexList.set(upgradeIndex, index);
			}
		}
	}

	protected void cycleUpgradeIndexForwards(int upgradeIndex) {
		if (upgradeIndex < this.currentUpgradeIndexList.size()) {
			int index = this.currentUpgradeIndexList.get(upgradeIndex);
			List<ClassSelectionScreenHandler.ClassSelectionScreenData.ClassUnlockStateData.UpgradeUnlockStateData> unlockStateList = this.handler.getClassUnlockStateDataList().get(this.currentClassIndex).upgradeUnlockStateDataList();
			if (upgradeIndex < unlockStateList.size()) {
				int stateListSize = unlockStateList.get(upgradeIndex).upgradeUnlockStatesList().size();
				index = index + 1;
				if (index >= stateListSize) {
					index = 0;
				}
				this.currentUpgradeIndexList.set(upgradeIndex, index);
			}
		}
	}

	protected void cycleClassBackwards() {
		this.cycleClassIndexBackwards();
		this.resetCurrentUpgradeIndexes();
		this.updateActiveClassState();
	}

	protected void cycleClassForwards() {
		this.cycleClassIndexForwards();
		this.resetCurrentUpgradeIndexes();
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

	protected void resetCurrentUpgradeIndexes() {

		this.currentUpgradeIndexList.clear();
		RPGClass rpgClass = this.handler.getRpgClassList().get(this.currentClassIndex);
		if (Objects.equals(rpgClass.class_identifier(), this.handler.getActiveClassState().activeClassIdentifier())) {
			List<String> list = this.handler.getActiveClassState().activeUpgradeIdentifierList();
			for (int i = 0; i < list.size(); i++) {
				String upgradeIdentifier = list.get(i);
				if (!upgradeIdentifier.isEmpty()) {
					if (i < rpgClass.upgrade_entry_group_list().size()) {
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
		List<String> activeUpgradeIdentifierList = this.getActiveUpgradeIdentifierList();
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

	private List<String> getActiveUpgradeIdentifierList() {
		List<String> activeUpgradeIdentifierList = new ArrayList<>();
		RPGClass rpgClass = this.handler.getRpgClassList().get(this.currentClassIndex);
		List<ClassSelectionScreenHandler.ClassSelectionScreenData.ClassUnlockStateData.UpgradeUnlockStateData> upgradeUnlockStateDataList = this.handler.getClassUnlockStateDataList().get(this.currentClassIndex).upgradeUnlockStateDataList();
		List<RPGClass.UpgradeEntryGroup> upgradeEntryGroupList = rpgClass.upgrade_entry_group_list();
		for (int i = 0; i < upgradeEntryGroupList.size(); i++) {
			if (i < this.currentUpgradeIndexList.size() && i < upgradeUnlockStateDataList.size()) {
				int currentUpgradeIndex = this.currentUpgradeIndexList.get(i);
				List<RPGClass.UpgradeEntryGroup.UpgradeEntry> upgradeEntryList = upgradeEntryGroupList.get(i).upgrade_entry_list();
				List<Boolean> upgradeUnlockStatesList = upgradeUnlockStateDataList.get(i).upgradeUnlockStatesList();
				if (currentUpgradeIndex < upgradeEntryList.size() && currentUpgradeIndex < upgradeUnlockStatesList.size()) {
					if (upgradeUnlockStatesList.get(currentUpgradeIndex)) {
						activeUpgradeIdentifierList.add(upgradeEntryList.get(currentUpgradeIndex).upgrade_identifier());
						continue;
					}
				}
				activeUpgradeIdentifierList.add("");
			}
		}
		return activeUpgradeIdentifierList;
	}

	protected void chooseClass() {
		ClientPlayNetworking.send(new UpdateClassPacket(this.newActiveClassState));
	}

	protected void updateWidgets() {

	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);

		this.drawMouseoverTooltip(context, mouseX, mouseY);
	}

	@Override
	protected void drawForeground(DrawContext context, int mouseX, int mouseY) {

		this.drawClassTitleAndDescription(context);

		this.drawUpgradeEntryTitles(context);
	}

	@Override
	protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
		context.drawTexture(BACKGROUND_TEXTURE, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight, this.backgroundWidth, this.backgroundHeight);

		this.drawUpgradeEntryIcons(context);
	}

	protected void drawClassTitleAndDescription(DrawContext context) {
	}

	protected void drawUpgradeEntryIcons(DrawContext context) {
		this.drawUpgradeEntries(context, true);
	}

	protected void drawUpgradeEntryTitles(DrawContext context) {
		this.drawUpgradeEntries(context, false);
	}

	protected void drawUpgradeEntries(DrawContext context, boolean background) {
	}

}
