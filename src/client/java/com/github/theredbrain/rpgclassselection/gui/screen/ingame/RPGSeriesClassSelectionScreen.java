package com.github.theredbrain.rpgclassselection.gui.screen.ingame;

import com.github.theredbrain.rpgclassselection.RPGClassSelection;
import com.github.theredbrain.rpgclassselection.component.type.ClassStateComponent;
import com.github.theredbrain.rpgclassselection.data.RPGClass;
import com.github.theredbrain.rpgclassselection.screen.ClassSelectionScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class RPGSeriesClassSelectionScreen extends AbstractClassSelectionScreen {
	public static final Identifier BACKGROUND_TEXTURE = RPGClassSelection.identifier("textures/gui/container/rpg_series_class_selection_background.png");

	private ButtonWidget cycleClassesBackwardsButton;
	private ButtonWidget cycleClassesForwardsButton;
	private ButtonWidget cycleUpgrade1BackwardsButton;
	private ButtonWidget cycleUpgrade1ForwardsButton;
	private ButtonWidget cycleUpgrade2BackwardsButton;
	private ButtonWidget cycleUpgrade2ForwardsButton;
	private ButtonWidget cycleUpgrade3BackwardsButton;
	private ButtonWidget cycleUpgrade3ForwardsButton;

	private ButtonWidget chooseClassButton;

	public RPGSeriesClassSelectionScreen(ClassSelectionScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
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

	@Override
	protected void updateWidgets() {

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
		List<RPGClass.UpgradeEntryGroup> upgradeEntryGroupList = rpgClass.upgrade_entry_group_list();
		if (upgradeEntryGroupList.size() >= 1) {
			this.cycleUpgrade1BackwardsButton.visible = true;
			this.cycleUpgrade1ForwardsButton.visible = true;
			if (upgradeEntryGroupList.get(0).upgrade_entry_list().size() > 1) {
				this.cycleUpgrade1BackwardsButton.active = true;
				this.cycleUpgrade1ForwardsButton.active = true;
			}
		}
		if (upgradeEntryGroupList.size() >= 2) {
			this.cycleUpgrade2BackwardsButton.visible = true;
			this.cycleUpgrade2ForwardsButton.visible = true;
			if (upgradeEntryGroupList.get(1).upgrade_entry_list().size() > 1) {
				this.cycleUpgrade2BackwardsButton.active = true;
				this.cycleUpgrade2ForwardsButton.active = true;
			}
		}
		if (upgradeEntryGroupList.size() >= 3) {
			this.cycleUpgrade3BackwardsButton.visible = true;
			this.cycleUpgrade3ForwardsButton.visible = true;
			if (upgradeEntryGroupList.get(2).upgrade_entry_list().size() > 1) {
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
		for (int i = 0; i < Math.min(3, rpgClass.upgrade_entry_group_list().size()); i++) {
			RPGClass.UpgradeEntryGroup upgradeEntryGroup = rpgClass.upgrade_entry_group_list().get(i);

			if (i < this.currentUpgradeIndexList.size()) {
				int index = this.currentUpgradeIndexList.get(i);

				if (index < upgradeEntryGroup.upgrade_entry_list().size()) {
					RPGClass.UpgradeEntryGroup.UpgradeEntry upgradeEntry = upgradeEntryGroup.upgrade_entry_list().get(index);
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
