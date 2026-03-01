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
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class RPGSeriesClassSelectionScreen extends AbstractClassSelectionScreen {

	private ButtonWidget cycleClassesBackwardsButton;
	private ButtonWidget cycleClassesForwardsButton;
	private ButtonWidget cycleUpgrade1BackwardsButton;
	private ButtonWidget cycleUpgrade1ForwardsButton;
	private ButtonWidget cycleUpgrade2BackwardsButton;
	private ButtonWidget cycleUpgrade2ForwardsButton;
	private ButtonWidget cycleUpgrade3BackwardsButton;
	private ButtonWidget cycleUpgrade3ForwardsButton;
	private ButtonWidget cycleUpgrade4BackwardsButton;
	private ButtonWidget cycleUpgrade4ForwardsButton;
	private ButtonWidget cycleUpgrade5BackwardsButton;
	private ButtonWidget cycleUpgrade5ForwardsButton;
	private ButtonWidget cycleUpgrade6BackwardsButton;
	private ButtonWidget cycleUpgrade6ForwardsButton;
	private ButtonWidget cycleUpgrade7BackwardsButton;
	private ButtonWidget cycleUpgrade7ForwardsButton;
	private ButtonWidget cycleUpgrade8BackwardsButton;
	private ButtonWidget cycleUpgrade8ForwardsButton;
	private ButtonWidget cycleUpgrade9BackwardsButton;
	private ButtonWidget cycleUpgrade9ForwardsButton;
	private ButtonWidget cycleUpgrade10BackwardsButton;
	private ButtonWidget cycleUpgrade10ForwardsButton;

	private ButtonWidget chooseClassButton;

	private int scrollPosition = 0;
	private float scrollAmount = 0.0f;
	private boolean mouseClicked = false;

	public RPGSeriesClassSelectionScreen(ClassSelectionScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	@Override
	protected void init() {
		this.backgroundWidth = 424;
		this.backgroundHeight = 233;

		super.init();

		this.currentClassIndex = this.handler.getInitialClassIndex();

		this.resetCurrentUpgradeIndexes();

		this.cycleClassesBackwardsButton = this.addDrawableChild(ButtonWidget.builder(Text.literal("<"), button -> this.cycleClassBackwards()).dimensions(this.x + 7, this.y + 7, 20, 20).build());
		this.cycleClassesForwardsButton = this.addDrawableChild(ButtonWidget.builder(Text.literal(">"), button -> this.cycleClassForwards()).dimensions(this.x + this.backgroundWidth - 27, this.y + 7, 20, 20).build());

		this.cycleUpgrade1BackwardsButton = this.addDrawableChild(ButtonWidget.builder(Text.literal("<"), button -> this.cycleUpgradeBackwards(0)).dimensions(this.x + 7, this.y + 86, 20, 20).build());
		this.cycleUpgrade1ForwardsButton = this.addDrawableChild(ButtonWidget.builder(Text.literal(">"), button -> this.cycleUpgradeForwards(0)).dimensions(this.x + 190, this.y + 86, 20, 20).build());
		this.cycleUpgrade2BackwardsButton = this.addDrawableChild(ButtonWidget.builder(Text.literal("<"), button -> this.cycleUpgradeBackwards(1)).dimensions(this.x + 7, this.y + 110, 20, 20).build());
		this.cycleUpgrade2ForwardsButton = this.addDrawableChild(ButtonWidget.builder(Text.literal(">"), button -> this.cycleUpgradeForwards(1)).dimensions(this.x + 190, this.y + 110, 20, 20).build());
		this.cycleUpgrade3BackwardsButton = this.addDrawableChild(ButtonWidget.builder(Text.literal("<"), button -> this.cycleUpgradeBackwards(2)).dimensions(this.x + 7, this.y + 134, 20, 20).build());
		this.cycleUpgrade3ForwardsButton = this.addDrawableChild(ButtonWidget.builder(Text.literal(">"), button -> this.cycleUpgradeForwards(2)).dimensions(this.x + 190, this.y + 134, 20, 20).build());
		this.cycleUpgrade4BackwardsButton = this.addDrawableChild(ButtonWidget.builder(Text.literal("<"), button -> this.cycleUpgradeBackwards(3)).dimensions(this.x + 7, this.y + 158, 20, 20).build());
		this.cycleUpgrade4ForwardsButton = this.addDrawableChild(ButtonWidget.builder(Text.literal(">"), button -> this.cycleUpgradeForwards(3)).dimensions(this.x + 190, this.y + 158, 20, 20).build());
		this.cycleUpgrade5BackwardsButton = this.addDrawableChild(ButtonWidget.builder(Text.literal("<"), button -> this.cycleUpgradeBackwards(4)).dimensions(this.x + 7, this.y + 182, 20, 20).build());
		this.cycleUpgrade5ForwardsButton = this.addDrawableChild(ButtonWidget.builder(Text.literal(">"), button -> this.cycleUpgradeForwards(4)).dimensions(this.x + 190, this.y + 182, 20, 20).build());

		this.cycleUpgrade6BackwardsButton = this.addDrawableChild(ButtonWidget.builder(Text.literal("<"), button -> this.cycleUpgradeBackwards(5 + this.scrollPosition)).dimensions(this.x + 214, this.y + 86, 20, 20).build());
		this.cycleUpgrade6ForwardsButton = this.addDrawableChild(ButtonWidget.builder(Text.literal(">"), button -> this.cycleUpgradeForwards(5 + this.scrollPosition)).dimensions(this.x + 397, this.y + 86, 20, 20).build());
		this.cycleUpgrade7BackwardsButton = this.addDrawableChild(ButtonWidget.builder(Text.literal("<"), button -> this.cycleUpgradeBackwards(6 + this.scrollPosition)).dimensions(this.x + 214, this.y + 110, 20, 20).build());
		this.cycleUpgrade7ForwardsButton = this.addDrawableChild(ButtonWidget.builder(Text.literal(">"), button -> this.cycleUpgradeForwards(6 + this.scrollPosition)).dimensions(this.x + 397, this.y + 110, 20, 20).build());
		this.cycleUpgrade8BackwardsButton = this.addDrawableChild(ButtonWidget.builder(Text.literal("<"), button -> this.cycleUpgradeBackwards(7 + this.scrollPosition)).dimensions(this.x + 214, this.y + 134, 20, 20).build());
		this.cycleUpgrade8ForwardsButton = this.addDrawableChild(ButtonWidget.builder(Text.literal(">"), button -> this.cycleUpgradeForwards(7 + this.scrollPosition)).dimensions(this.x + 397, this.y + 134, 20, 20).build());
		this.cycleUpgrade9BackwardsButton = this.addDrawableChild(ButtonWidget.builder(Text.literal("<"), button -> this.cycleUpgradeBackwards(8 + this.scrollPosition)).dimensions(this.x + 214, this.y + 158, 20, 20).build());
		this.cycleUpgrade9ForwardsButton = this.addDrawableChild(ButtonWidget.builder(Text.literal(">"), button -> this.cycleUpgradeForwards(8 + this.scrollPosition)).dimensions(this.x + 397, this.y + 158, 20, 20).build());
		this.cycleUpgrade10BackwardsButton = this.addDrawableChild(ButtonWidget.builder(Text.literal("<"), button -> this.cycleUpgradeBackwards(9 + this.scrollPosition)).dimensions(this.x + 214, this.y + 182, 20, 20).build());
		this.cycleUpgrade10ForwardsButton = this.addDrawableChild(ButtonWidget.builder(Text.literal(">"), button -> this.cycleUpgradeForwards(9 + this.scrollPosition)).dimensions(this.x + 397, this.y + 182, 20, 20).build());

		this.chooseClassButton = this.addDrawableChild(ButtonWidget.builder(CHOOSE_CLASS_BUTTON_LABEL_TEXT, button -> this.chooseClass()).dimensions(this.x + 7, this.y + this.backgroundHeight - 27, this.backgroundWidth - 14, 20).build());

		this.updateActiveClassState();
	}

	@Override
	protected void cycleClassIndexBackwards() {
		super.cycleClassIndexBackwards();

		this.scrollPosition = 0;
		this.scrollAmount = 0.0F;
	}

	@Override
	protected void cycleClassIndexForwards() {
		super.cycleClassIndexForwards();

		this.scrollPosition = 0;
		this.scrollAmount = 0.0F;
	}

	@Override
	public void resize(MinecraftClient client, int width, int height) {
		int number = this.scrollPosition;
		float number1 = this.scrollAmount;
		int number2 = this.classDescriptionScrollPosition;
		float number3 = this.classDescriptionScrollAmount;
		boolean bool = this.mouseClicked;
		boolean bool1 = this.classDescriptionMouseClicked;
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
		this.scrollPosition = number;
		this.scrollAmount = number1;
		this.classDescriptionScrollPosition = number2;
		this.classDescriptionScrollAmount = number3;
		this.mouseClicked = bool;
		this.classDescriptionMouseClicked = bool1;
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
		this.cycleUpgrade4BackwardsButton.active = false;
		this.cycleUpgrade4ForwardsButton.active = false;
		this.cycleUpgrade5BackwardsButton.active = false;
		this.cycleUpgrade5ForwardsButton.active = false;

		this.cycleUpgrade6BackwardsButton.active = false;
		this.cycleUpgrade6ForwardsButton.active = false;
		this.cycleUpgrade7BackwardsButton.active = false;
		this.cycleUpgrade7ForwardsButton.active = false;
		this.cycleUpgrade8BackwardsButton.active = false;
		this.cycleUpgrade8ForwardsButton.active = false;
		this.cycleUpgrade9BackwardsButton.active = false;
		this.cycleUpgrade9ForwardsButton.active = false;
		this.cycleUpgrade10BackwardsButton.active = false;
		this.cycleUpgrade10ForwardsButton.active = false;


		this.cycleUpgrade1BackwardsButton.visible = false;
		this.cycleUpgrade1ForwardsButton.visible = false;
		this.cycleUpgrade2BackwardsButton.visible = false;
		this.cycleUpgrade2ForwardsButton.visible = false;
		this.cycleUpgrade3BackwardsButton.visible = false;
		this.cycleUpgrade3ForwardsButton.visible = false;
		this.cycleUpgrade4BackwardsButton.visible = false;
		this.cycleUpgrade4ForwardsButton.visible = false;
		this.cycleUpgrade5BackwardsButton.visible = false;
		this.cycleUpgrade5ForwardsButton.visible = false;

		this.cycleUpgrade6BackwardsButton.visible = false;
		this.cycleUpgrade6ForwardsButton.visible = false;
		this.cycleUpgrade7BackwardsButton.visible = false;
		this.cycleUpgrade7ForwardsButton.visible = false;
		this.cycleUpgrade8BackwardsButton.visible = false;
		this.cycleUpgrade8ForwardsButton.visible = false;
		this.cycleUpgrade9BackwardsButton.visible = false;
		this.cycleUpgrade9ForwardsButton.visible = false;
		this.cycleUpgrade10BackwardsButton.visible = false;
		this.cycleUpgrade10ForwardsButton.visible = false;

		if (this.handler.getRpgClassList().size() > 1) {

			this.cycleClassesBackwardsButton.active = true;
			this.cycleClassesForwardsButton.active = true;

		}
		List<ClassSelectionScreenHandler.ClassSelectionScreenData.ClassUnlockStateData.UpgradeUnlockStateData> upgradeUnlockStateDataList = this.handler.getClassUnlockStateDataList().get(this.currentClassIndex).upgradeUnlockStateDataList();
		if (upgradeUnlockStateDataList.size() >= 1 && !upgradeUnlockStateDataList.get(0).upgradeUnlockStatesList().isEmpty()) {
			this.cycleUpgrade1BackwardsButton.visible = true;
			this.cycleUpgrade1ForwardsButton.visible = true;
			if (upgradeUnlockStateDataList.get(0).upgradeUnlockStatesList().size() > 1) {
				this.cycleUpgrade1BackwardsButton.active = true;
				this.cycleUpgrade1ForwardsButton.active = true;
			}
		}
		if (upgradeUnlockStateDataList.size() >= 2 && !upgradeUnlockStateDataList.get(1).upgradeUnlockStatesList().isEmpty()) {
			this.cycleUpgrade2BackwardsButton.visible = true;
			this.cycleUpgrade2ForwardsButton.visible = true;
			if (upgradeUnlockStateDataList.get(1).upgradeUnlockStatesList().size() > 1) {
				this.cycleUpgrade2BackwardsButton.active = true;
				this.cycleUpgrade2ForwardsButton.active = true;
			}
		}
		if (upgradeUnlockStateDataList.size() >= 3 && !upgradeUnlockStateDataList.get(2).upgradeUnlockStatesList().isEmpty()) {
			this.cycleUpgrade3BackwardsButton.visible = true;
			this.cycleUpgrade3ForwardsButton.visible = true;
			if (upgradeUnlockStateDataList.get(2).upgradeUnlockStatesList().size() > 1) {
				this.cycleUpgrade3BackwardsButton.active = true;
				this.cycleUpgrade3ForwardsButton.active = true;
			}
		}
		if (upgradeUnlockStateDataList.size() >= 4 && !upgradeUnlockStateDataList.get(3).upgradeUnlockStatesList().isEmpty()) {
			this.cycleUpgrade4BackwardsButton.visible = true;
			this.cycleUpgrade4ForwardsButton.visible = true;
			if (upgradeUnlockStateDataList.get(3).upgradeUnlockStatesList().size() > 1) {
				this.cycleUpgrade4BackwardsButton.active = true;
				this.cycleUpgrade4ForwardsButton.active = true;
			}
		}
		if (upgradeUnlockStateDataList.size() >= 5 && !upgradeUnlockStateDataList.get(4).upgradeUnlockStatesList().isEmpty()) {
			this.cycleUpgrade5BackwardsButton.visible = true;
			this.cycleUpgrade5ForwardsButton.visible = true;
			if (upgradeUnlockStateDataList.get(4).upgradeUnlockStatesList().size() > 1) {
				this.cycleUpgrade5BackwardsButton.active = true;
				this.cycleUpgrade5ForwardsButton.active = true;
			}
		}
		if (upgradeUnlockStateDataList.size() >= 6 + this.scrollPosition && !upgradeUnlockStateDataList.get(5 + this.scrollPosition).upgradeUnlockStatesList().isEmpty()) {
			this.cycleUpgrade6BackwardsButton.visible = true;
			this.cycleUpgrade6ForwardsButton.visible = true;
			if (upgradeUnlockStateDataList.get(5 + this.scrollPosition).upgradeUnlockStatesList().size() > 1) {
				this.cycleUpgrade6BackwardsButton.active = true;
				this.cycleUpgrade6ForwardsButton.active = true;
			}
		}
		if (upgradeUnlockStateDataList.size() >= 7 + this.scrollPosition && !upgradeUnlockStateDataList.get(6 + this.scrollPosition).upgradeUnlockStatesList().isEmpty()) {
			this.cycleUpgrade7BackwardsButton.visible = true;
			this.cycleUpgrade7ForwardsButton.visible = true;
			if (upgradeUnlockStateDataList.get(6 + this.scrollPosition).upgradeUnlockStatesList().size() > 1) {
				this.cycleUpgrade7BackwardsButton.active = true;
				this.cycleUpgrade7ForwardsButton.active = true;
			}
		}
		if (upgradeUnlockStateDataList.size() >= 8 + this.scrollPosition && !upgradeUnlockStateDataList.get(7 + this.scrollPosition).upgradeUnlockStatesList().isEmpty()) {
			this.cycleUpgrade8BackwardsButton.visible = true;
			this.cycleUpgrade8ForwardsButton.visible = true;
			if (upgradeUnlockStateDataList.get(7 + this.scrollPosition).upgradeUnlockStatesList().size() > 1) {
				this.cycleUpgrade8BackwardsButton.active = true;
				this.cycleUpgrade8ForwardsButton.active = true;
			}
		}
		if (upgradeUnlockStateDataList.size() >= 9 + this.scrollPosition && !upgradeUnlockStateDataList.get(8 + this.scrollPosition).upgradeUnlockStatesList().isEmpty()) {
			this.cycleUpgrade9BackwardsButton.visible = true;
			this.cycleUpgrade9ForwardsButton.visible = true;
			if (upgradeUnlockStateDataList.get(8 + this.scrollPosition).upgradeUnlockStatesList().size() > 1) {
				this.cycleUpgrade9BackwardsButton.active = true;
				this.cycleUpgrade9ForwardsButton.active = true;
			}
		}
		if (upgradeUnlockStateDataList.size() >= 10 + this.scrollPosition && !upgradeUnlockStateDataList.get(9 + this.scrollPosition).upgradeUnlockStatesList().isEmpty()) {
			this.cycleUpgrade10BackwardsButton.visible = true;
			this.cycleUpgrade10ForwardsButton.visible = true;
			if (upgradeUnlockStateDataList.get(9 + this.scrollPosition).upgradeUnlockStatesList().size() > 1) {
				this.cycleUpgrade10BackwardsButton.active = true;
				this.cycleUpgrade10ForwardsButton.active = true;
			}
		}

	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);

		RPGClass rpgClass = this.handler.getRpgClassList().get(this.currentClassIndex);

		for (int i = 0; i < Math.min(5, rpgClass.upgrade_entry_group_list().size()); i++) {
			RPGClass.UpgradeEntryGroup upgradeEntryGroup = rpgClass.upgrade_entry_group_list().get(i);

			if (i < this.currentUpgradeIndexList.size()) {
				int index = this.currentUpgradeIndexList.get(i);

				if (index < upgradeEntryGroup.upgrade_entry_list().size()) {
					RPGClass.UpgradeEntryGroup.UpgradeEntry upgradeEntry = upgradeEntryGroup.upgrade_entry_list().get(index);

					if (this.isPointWithinBounds(31, 88 + i * 24, 155, 20, mouseX, mouseY)) {
						List<Text> list = this.getUpgradeEntryTooltipList(upgradeEntry);

						context.drawTooltip(this.textRenderer, list, mouseX, mouseY);
					}
				}
			}
		}

		for (int i = 5 + this.scrollPosition; i < Math.min(10 + this.scrollPosition, rpgClass.upgrade_entry_group_list().size()); i++) {
			RPGClass.UpgradeEntryGroup upgradeEntryGroup = rpgClass.upgrade_entry_group_list().get(i);

			if (i < this.currentUpgradeIndexList.size()) {
				int index = this.currentUpgradeIndexList.get(i);

				if (index < upgradeEntryGroup.upgrade_entry_list().size()) {
					RPGClass.UpgradeEntryGroup.UpgradeEntry upgradeEntry = upgradeEntryGroup.upgrade_entry_list().get(index);

					if (this.isPointWithinBounds(238, 88 + (i - 5 - this.scrollPosition) * 24, 155, 20, mouseX, mouseY)) {
						List<Text> list = this.getUpgradeEntryTooltipList(upgradeEntry);

						context.drawTooltip(this.textRenderer, list, mouseX, mouseY);
					}
				}
			}
		}

	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		RPGClass rpgClass = this.handler.getRpgClassList().get(this.currentClassIndex);
		this.mouseClicked = false;
		if (rpgClass.upgrade_entry_group_list().size() > 10) {
			if (mouseX >= this.x + 388 && mouseX < this.x + 394 && mouseY >= (double) this.y + 86 && mouseY < (double) this.y + 202) {
				this.mouseClicked = true;
			}
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		RPGClass rpgClass = this.handler.getRpgClassList().get(this.currentClassIndex);
		if (rpgClass.upgrade_entry_group_list().size() > 10
				&& this.mouseClicked) {
			int i = rpgClass.upgrade_entry_group_list().size() - 10;
			float f = (float) deltaY / (float) i;
			this.scrollAmount = MathHelper.clamp(this.scrollAmount + f, 0.0f, 1.0f);
			this.scrollPosition = (int) ((double) (this.scrollAmount * (float) i));
		}
		this.updateWidgets();
		return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
		RPGClass rpgClass = this.handler.getRpgClassList().get(this.currentClassIndex);
		if (rpgClass.upgrade_entry_group_list().size() > 10
				&& mouseX >= this.x + 234 && mouseX <= this.x + 395
				&& mouseY >= this.y + 86 && mouseY <= this.y + 202) {
			int i = rpgClass.upgrade_entry_group_list().size() - 10;
			float f = (float) verticalAmount / (float) i;
			this.scrollAmount = MathHelper.clamp(this.scrollAmount - f, 0.0f, 1.0f);
			this.scrollPosition = (int) ((double) (this.scrollAmount * (float) i));
		}
		this.updateWidgets();
		return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
	}

	@Override
	protected int getClassDescriptionFieldX() {
		return 7;
	}

	@Override
	protected int getClassDescriptionFieldY() {
		return 31;
	}

	@Override
	protected int getClassDescriptionFieldWidth() {
		return 410;
	}

	@Override
	protected int getClassDescriptionFieldHeight() {
		return 42;
	}

	@Override
	protected int getClassDescriptionFieldMaxLineAmount() {
		return 4;
	}

	@Override
	protected void drawUpgradeEntries(DrawContext context, boolean background) {
		RPGClass rpgClass = this.handler.getRpgClassList().get(this.currentClassIndex);

		if (!background) {
			if (rpgClass.upgrade_entry_group_list().size() > 0) {
				Text spellsLabel = Text.translatable("class_selection_screen.spells_label");
				context.drawText(this.textRenderer, spellsLabel, ((this.backgroundWidth / 2) - this.textRenderer.getWidth(spellsLabel)) / 2, 76, 0/*4210752*/, false);
			}
			if (rpgClass.upgrade_entry_group_list().size() > 5) {
				Text modifiersLabel = Text.translatable("class_selection_screen.modifiers_label");
				context.drawText(this.textRenderer, modifiersLabel, (this.backgroundWidth / 2) + ((this.backgroundWidth / 2) - this.textRenderer.getWidth(modifiersLabel)) / 2, 76, 0/*4210752*/, false);
			}
		}

		for (int i = 0; i < Math.min(5, rpgClass.upgrade_entry_group_list().size()); i++) {
			RPGClass.UpgradeEntryGroup upgradeEntryGroup = rpgClass.upgrade_entry_group_list().get(i);

			if (i < this.currentUpgradeIndexList.size()) {
				int index = this.currentUpgradeIndexList.get(i);

				if (index < upgradeEntryGroup.upgrade_entry_list().size()) {
					RPGClass.UpgradeEntryGroup.UpgradeEntry upgradeEntry = upgradeEntryGroup.upgrade_entry_list().get(index);

					if (background) {
						if (!upgradeEntry.icon_path().isEmpty()) {
							context.drawTexture(Identifier.of(upgradeEntry.icon_path()), this.x + 31, this.y + 88 + i * 24, 0, 0, 16, 16, 16, 16);
						}
					} else {
						context.drawText(this.textRenderer, Text.translatable(upgradeEntry.title()), 31 + (upgradeEntry.icon_path().isEmpty() ? 0 : 20), 92 + i * 24, 0/*4210752*/, false);
					}
				}
			}
		}

		if (background && rpgClass.upgrade_entry_group_list().size() > 10) {
			context.drawGuiTexture(SCROLL_BAR_BACKGROUND_TEXTURE, this.x + 387, this.y + 86, 8, 116);
			int k = (int) ((116 - 7 - 2) * this.scrollAmount);
			context.drawGuiTexture(SCROLLER_TEXTURE, this.x + 387 + 1, this.y + 86 + 1 + k, 6, 7);
		}

		for (int i = 5 + this.scrollPosition; i < Math.min(10 + this.scrollPosition, rpgClass.upgrade_entry_group_list().size()); i++) {
			RPGClass.UpgradeEntryGroup upgradeEntryGroup = rpgClass.upgrade_entry_group_list().get(i);

			if (i < this.currentUpgradeIndexList.size()) {
				int index = this.currentUpgradeIndexList.get(i);

				if (index < upgradeEntryGroup.upgrade_entry_list().size()) {
					RPGClass.UpgradeEntryGroup.UpgradeEntry upgradeEntry = upgradeEntryGroup.upgrade_entry_list().get(index);

					if (background) {
						if (!upgradeEntry.icon_path().isEmpty()) {
							context.drawTexture(Identifier.of(upgradeEntry.icon_path()), this.x + 238, this.y + 88 + (i - 5 - this.scrollPosition) * 24, 0, 0, 16, 16, 16, 16);
						}
					} else {
						context.drawText(this.textRenderer, Text.translatable(upgradeEntry.title()), 238 + (upgradeEntry.icon_path().isEmpty() ? 0 : 20), 92 + (i - 5 - this.scrollPosition) * 24, 0/*4210752*/, false);
					}
				}
			}
		}
	}

	static {
		BACKGROUND_TEXTURE = RPGClassSelection.identifier("textures/gui/container/rpg_series_class_selection_background.png");
	}
}
