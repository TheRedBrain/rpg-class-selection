package com.github.theredbrain.rpgclassselection.gui.screen.ingame;

import com.github.theredbrain.rpgclassselection.RPGClassSelection;
import com.github.theredbrain.rpgclassselection.component.type.ClassStateComponent;
import com.github.theredbrain.rpgclassselection.network.packet.UpdateClassPacket;
import com.github.theredbrain.rpgclassselection.screen.ClassSelectionScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

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

	private String activeClassDescription = "";

	public ClassSelectionScreen(ClassSelectionScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	private void cycleClassBackwards() {
	}

	private void cycleClassForwards() {
	}

	private void cycleUpgradeBackwards(int index) {
	}

	private void cycleUpgradeForwards(int index) {
	}

	private void chooseClass() {
		ClientPlayNetworking.send(new UpdateClassPacket(this.newActiveClassState));
		this.close();
	}

	@Override
	protected void init() {
		this.backgroundWidth = 218;
		this.backgroundHeight = 215;

		super.init();

		this.newActiveClassState = this.handler.getActiveClassState();

		this.cycleClassesBackwardsButton = this.addDrawableChild(ButtonWidget.builder(Text.literal("<"), button -> this.cycleClassBackwards()).dimensions(this.x + 7, this.y + 7, 20, 20).build());
		this.cycleClassesForwardsButton = this.addDrawableChild(ButtonWidget.builder(Text.literal(">"), button -> this.cycleClassForwards()).dimensions(this.x + this.backgroundWidth - 27, this.y + 7, 20, 20).build());
		this.cycleUpgrade1BackwardsButton = this.addDrawableChild(ButtonWidget.builder(Text.literal("<"), button -> this.cycleUpgradeBackwards(1)).dimensions(this.x + 7, this.y + 116, 20, 20).build());
		this.cycleUpgrade1ForwardsButton = this.addDrawableChild(ButtonWidget.builder(Text.literal(">"), button -> this.cycleUpgradeForwards(1)).dimensions(this.x + this.backgroundWidth - 27, this.y + 116, 20, 20).build());
		this.cycleUpgrade2BackwardsButton = this.addDrawableChild(ButtonWidget.builder(Text.literal("<"), button -> this.cycleUpgradeBackwards(2)).dimensions(this.x + 7, this.y + 140, 20, 20).build());
		this.cycleUpgrade2ForwardsButton = this.addDrawableChild(ButtonWidget.builder(Text.literal(">"), button -> this.cycleUpgradeForwards(2)).dimensions(this.x + this.backgroundWidth - 27, this.y + 140, 20, 20).build());
		this.cycleUpgrade3BackwardsButton = this.addDrawableChild(ButtonWidget.builder(Text.literal("<"), button -> this.cycleUpgradeBackwards(3)).dimensions(this.x + 7, this.y + 164, 20, 20).build());
		this.cycleUpgrade3ForwardsButton = this.addDrawableChild(ButtonWidget.builder(Text.literal(">"), button -> this.cycleUpgradeForwards(3)).dimensions(this.x + this.backgroundWidth - 27, this.y + 164, 20, 20).build());
		this.chooseClassButton = this.addDrawableChild(ButtonWidget.builder(Text.translatable("Choose Class (WIP)"), button -> this.chooseClass()).dimensions(this.x + 7, this.y + this.backgroundHeight - 27, this.backgroundWidth - 14, 20).build());

		this.updateWidgets();
	}

	private void updateWidgets() {

	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);

		this.drawMouseoverTooltip(context, mouseX, mouseY);
	}

	@Override
	protected void drawForeground(DrawContext context, int mouseX, int mouseY) {

		String className = "Test Class Name";//this.newActiveClassState.activeClassIdentifier();

		context.drawText(this.textRenderer, className, (this.backgroundWidth - this.textRenderer.getWidth(className)) / 2, 13, 4210752, false);

		context.drawText(this.textRenderer, className, (this.backgroundWidth - this.textRenderer.getWidth(className)) / 2, 13, 4210752, false);

		Text classDescription = Text.translatable("This is a veeeeery long class description. It's main purpose is testing out the automatic text wreapping. The text color is also tested with this text. Last but not least, the correct text position is tested."/*this.activeClassDescription*/);

		context.drawTextWrapped(this.textRenderer, classDescription, 11, 35, 196, Colors.BLACK);
	}

	@Override
	protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
		context.drawTexture(BACKGROUND_TEXTURE, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight, this.backgroundWidth, this.backgroundHeight);
		RPGClassSelection.LOGGER.info("backgroundWidth: " + this.backgroundWidth);
		RPGClassSelection.LOGGER.info("backgroundHeight: " + this.backgroundHeight);
	}

}
