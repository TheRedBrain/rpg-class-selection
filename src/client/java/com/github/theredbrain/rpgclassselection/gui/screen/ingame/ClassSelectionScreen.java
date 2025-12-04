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
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.registry.SpellRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class ClassSelectionScreen extends HandledScreen<ClassSelectionScreenHandler> {
	public static final Identifier BACKGROUND_TEXTURE = RPGClassSelection.identifier("textures/gui/container/class_selection_background.png");

	private ButtonWidget cycleClassesBackwardsButton;
	private ButtonWidget cycleClassesForwardsButton;
	private ButtonWidget cycleUpgrade1BackwardsButton;
	private ButtonWidget cycleUpgrade1ForwardsButton;
	private ButtonWidget cycleUpgrade2BackwardsButton;
	private ButtonWidget cycleUpgrade2ForwardsButton;
	private ButtonWidget cycleUpgrade3BackwardsButton;
	private ButtonWidget cycleUpgrade3ForwardsButton;

	private ButtonWidget chooseClassButton;

	private ClassStateComponent.ActiveClassState newActiveClassState;

	private int currentClassIndex;
	private String activeClassDescription = "";
	private final List<Integer> currentUpgradeIndexList = new ArrayList<>();

	public ClassSelectionScreen(ClassSelectionScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	public void cycleClassIndexBackwards() {
		int index = this.currentClassIndex;
		List<RPGClass> dataList = this.handler.getRpgClassList();
		List<ClassSelectionScreenHandler.ClassSelectionScreenData.ClassUnlockStateData> stateList = this.handler.getClassUnlockStateDataList();
		int listSize = dataList.size();
		while (true) {
			index = index - 1;
			if (index < 0) {
				index = listSize - 1;
			}
			if (index == 0 || stateList.get(index).classUnlockState() || dataList.get(index).visibleWhenLocked()) {
				this.currentClassIndex = index;
				break;
			}
		}
	}

	public void cycleClassIndexForwards() {
		int index = this.currentClassIndex;
		List<RPGClass> dataList = this.handler.getRpgClassList();
		List<ClassSelectionScreenHandler.ClassSelectionScreenData.ClassUnlockStateData> stateList = this.handler.getClassUnlockStateDataList();
		int listSize = dataList.size();
		while (true) {
			index = index + 1;
			if (index >= listSize) {
				index = 0;
			}
			if (index == 0 || stateList.get(index).classUnlockState() || dataList.get(index).visibleWhenLocked()) {
				this.currentClassIndex = index;
				break;
			}
		}
	}

	public void cycleUpgradeIndexBackwards(int upgradeIndex) {
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

	public void cycleUpgradeIndexForwards(int upgradeIndex) {
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

	private void cycleClassBackwards() {
		this.cycleClassIndexBackwards();
		this.updateCurrentUpgradeIndexes();
		this.updateActiveClassState();
	}

	private void cycleClassForwards() {
		this.cycleClassIndexForwards();
		this.updateCurrentUpgradeIndexes();
		this.updateActiveClassState();
	}

	private void cycleUpgradeBackwards(int index) {
		this.cycleUpgradeIndexBackwards(index);
		this.updateActiveClassState();
	}

	private void cycleUpgradeForwards(int index) {
		this.cycleUpgradeIndexForwards(index);
		this.updateActiveClassState();
	}

	private void updateCurrentUpgradeIndexes() {

		this.currentUpgradeIndexList.clear();
		RPGClass rpgClass = this.handler.getRpgClassList().get(this.currentClassIndex);
		if (Objects.equals(rpgClass.class_identifier(), this.handler.getActiveClassState().activeClassIdentifier())) {
			List<String> list = this.handler.getActiveClassState().activeUpgradeIdentifierList();
			for (int i = 0; i < list.size(); i++) {
				String upgradeIdentifier = list.get(i);
				if (!upgradeIdentifier.isEmpty()) {
					List<RPGClass.UpgradeEntryGroup.UpgradeEntry> upgradeEntryList = rpgClass.upgradeEntryGroupList().get(i).upgradeEntryList();
					for (int j = 0; j < upgradeEntryList.size(); j++) {

						RPGClass.UpgradeEntryGroup.UpgradeEntry upgradeEntry = upgradeEntryList.get(j);
						if (upgradeIdentifier.equals(upgradeEntry.upgradeIdentifier())) {
							this.currentUpgradeIndexList.add(j);
							break;
						}
					}
					continue;
				}
				this.currentUpgradeIndexList.add(0);
			}
		} else {
			for (int i = 0; i < rpgClass.upgradeEntryGroupList().size(); i++) {
				this.currentUpgradeIndexList.add(0);
			}
		}
	}

	private void updateActiveClassState() {
		RPGClass rpgClass = this.handler.getRpgClassList().get(this.currentClassIndex);
		List<String> activeUpgradeIdentifierList = new ArrayList<>();
		for (int i = 0; i < this.currentUpgradeIndexList.size(); i++) {
			if (i < rpgClass.upgradeEntryGroupList().size()) {
				activeUpgradeIdentifierList.add(rpgClass.upgradeEntryGroupList().get(i).upgradeEntryList().get(this.currentUpgradeIndexList.get(i)).upgradeIdentifier());
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

	private void chooseClass() {
		ClientPlayNetworking.send(new UpdateClassPacket(this.newActiveClassState));
	}

	@Override
	protected void init() {
		this.backgroundWidth = 218;
		this.backgroundHeight = 215;

		super.init();

		this.currentClassIndex = this.handler.getInitialClassIndex();

		this.updateCurrentUpgradeIndexes();

		this.cycleClassesBackwardsButton = this.addDrawableChild(ButtonWidget.builder(Text.literal("<"), button -> this.cycleClassBackwards()).dimensions(this.x + 7, this.y + 7, 20, 20).build());
		this.cycleClassesForwardsButton = this.addDrawableChild(ButtonWidget.builder(Text.literal(">"), button -> this.cycleClassForwards()).dimensions(this.x + this.backgroundWidth - 27, this.y + 7, 20, 20).build());
		this.cycleUpgrade1BackwardsButton = this.addDrawableChild(ButtonWidget.builder(Text.literal("<"), button -> this.cycleUpgradeBackwards(0)).dimensions(this.x + 7, this.y + 116, 20, 20).build());
		this.cycleUpgrade1ForwardsButton = this.addDrawableChild(ButtonWidget.builder(Text.literal(">"), button -> this.cycleUpgradeForwards(0)).dimensions(this.x + this.backgroundWidth - 27, this.y + 116, 20, 20).build());
		this.cycleUpgrade2BackwardsButton = this.addDrawableChild(ButtonWidget.builder(Text.literal("<"), button -> this.cycleUpgradeBackwards(1)).dimensions(this.x + 7, this.y + 140, 20, 20).build());
		this.cycleUpgrade2ForwardsButton = this.addDrawableChild(ButtonWidget.builder(Text.literal(">"), button -> this.cycleUpgradeForwards(1)).dimensions(this.x + this.backgroundWidth - 27, this.y + 140, 20, 20).build());
		this.cycleUpgrade3BackwardsButton = this.addDrawableChild(ButtonWidget.builder(Text.literal("<"), button -> this.cycleUpgradeBackwards(2)).dimensions(this.x + 7, this.y + 164, 20, 20).build());
		this.cycleUpgrade3ForwardsButton = this.addDrawableChild(ButtonWidget.builder(Text.literal(">"), button -> this.cycleUpgradeForwards(2)).dimensions(this.x + this.backgroundWidth - 27, this.y + 164, 20, 20).build());
		this.chooseClassButton = this.addDrawableChild(ButtonWidget.builder(Text.translatable("Choose Class (WIP)"), button -> this.chooseClass()).dimensions(this.x + 7, this.y + this.backgroundHeight - 27, this.backgroundWidth - 14, 20).build());

		this.updateActiveClassState();
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

	private void updateWidgets() {

		this.cycleClassesBackwardsButton.active = false;
		this.cycleClassesForwardsButton.active = false;
		this.cycleUpgrade1BackwardsButton.active = false;
		this.cycleUpgrade1ForwardsButton.active = false;
		this.cycleUpgrade2BackwardsButton.active = false;
		this.cycleUpgrade2ForwardsButton.active = false;
		this.cycleUpgrade3BackwardsButton.active = false;
		this.cycleUpgrade3ForwardsButton.active = false;

		this.cycleUpgrade1BackwardsButton.visible = false;
		this.cycleUpgrade1ForwardsButton.visible = false;
		this.cycleUpgrade2BackwardsButton.visible = false;
		this.cycleUpgrade2ForwardsButton.visible = false;
		this.cycleUpgrade3BackwardsButton.visible = false;
		this.cycleUpgrade3ForwardsButton.visible = false;

		if (this.handler.getRpgClassList().size() > 1) {

			this.cycleClassesBackwardsButton.active = true;
			this.cycleClassesForwardsButton.active = true;

		}
		RPGClass rpgClass = this.handler.getRpgClassList().get(this.currentClassIndex);
		List<RPGClass.UpgradeEntryGroup> upgradeEntryGroupList = rpgClass.upgradeEntryGroupList();
		if (upgradeEntryGroupList.size() >= 1) {
			this.cycleUpgrade1BackwardsButton.visible = true;
			this.cycleUpgrade1ForwardsButton.visible = true;
			if (upgradeEntryGroupList.get(0).upgradeEntryList().size() > 1) {
				this.cycleUpgrade1BackwardsButton.active = true;
				this.cycleUpgrade1ForwardsButton.active = true;
			}
		}
		if (upgradeEntryGroupList.size() >= 2) {
			this.cycleUpgrade2BackwardsButton.visible = true;
			this.cycleUpgrade2ForwardsButton.visible = true;
			if (upgradeEntryGroupList.get(1).upgradeEntryList().size() > 1) {
				this.cycleUpgrade2BackwardsButton.active = true;
				this.cycleUpgrade2ForwardsButton.active = true;
			}
		}
		if (upgradeEntryGroupList.size() >= 3) {
			this.cycleUpgrade3BackwardsButton.visible = true;
			this.cycleUpgrade3ForwardsButton.visible = true;
			if (upgradeEntryGroupList.get(2).upgradeEntryList().size() > 1) {
				this.cycleUpgrade3BackwardsButton.active = true;
				this.cycleUpgrade3ForwardsButton.active = true;
			}
		}

	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);

		this.drawMouseoverTooltip(context, mouseX, mouseY);
	}

	@Override
	protected void drawForeground(DrawContext context, int mouseX, int mouseY) {

		Text className = Text.translatable("class_selection_screen." + this.newActiveClassState.activeClassIdentifier().replace(":", ".") + ".title");

		context.drawText(this.textRenderer, className, (this.backgroundWidth - this.textRenderer.getWidth(className)) / 2, 13, 0/*4210752*/, false);

		if (!this.activeClassDescription.isEmpty()) {
			context.drawTextWrapped(this.textRenderer, Text.translatable(this.activeClassDescription), 11, 35, 196, 0/*Colors.BLACK*/);
		}
		RPGClass rpgClass = this.handler.getRpgClassList().get(this.currentClassIndex);
		for (int i = 0; i < Math.min(3, rpgClass.upgradeEntryGroupList().size()); i++) {
			RPGClass.UpgradeEntryGroup upgradeEntryGroup = rpgClass.upgradeEntryGroupList().get(i);

			if (i < this.currentUpgradeIndexList.size()) {
				int index = this.currentUpgradeIndexList.get(i);

				if (index < upgradeEntryGroup.upgradeEntryList().size()) {
					RPGClass.UpgradeEntryGroup.UpgradeEntry upgradeEntry = upgradeEntryGroup.upgradeEntryList().get(index);
					int x_offset = 0;

					if (!upgradeEntry.icon_path().isEmpty()) {
						x_offset = 20;
						context.drawTexture(Identifier.of(upgradeEntry.icon_path()), 31, 118 + i * 24, 0, 0, 16, 16, 16, 16);
					}
					context.drawText(this.textRenderer, Text.translatable(upgradeEntry.title()), 31 + x_offset, 122 + i * 24, 0/*4210752*/, false);
				}
			}
		}
	}

	@Override
	protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
		context.drawTexture(BACKGROUND_TEXTURE, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight, this.backgroundWidth, this.backgroundHeight);
	}

}
