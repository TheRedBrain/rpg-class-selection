package com.github.theredbrain.rpgclassselection.gui.screen.ingame;

import com.github.theredbrain.rpgclassselection.RPGClassSelection;
import com.github.theredbrain.rpgclassselection.component.type.ClassStateComponent;
import com.github.theredbrain.rpgclassselection.screen.ClassSelectionScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class ClassSelectionScreen extends HandledScreen<ClassSelectionScreenHandler> {
	public static final Identifier BACKGROUND_TEXTURE = RPGClassSelection.identifier("textures/gui/container/class_selection_background.png");

	private ButtonWidget cycleClassesForwardsButton;
	private ButtonWidget cycleClassesBackwardsButton;

	private ButtonWidget chooseClassButton;

	private ClassStateComponent.ActiveClassState newActiveClassState;

	public ClassSelectionScreen(ClassSelectionScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	@Override
	protected void init() {
		this.backgroundWidth = 218;
		this.backgroundHeight = 215;

		super.init();

		this.newActiveClassState = this.handler.getActiveClassState();

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

		context.drawText(this.textRenderer, this.newActiveClassState.activeClassIdentifier(), 8, this.titleY, 4210752, false);

	}

	@Override
	protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
		context.drawGuiTexture(BACKGROUND_TEXTURE, this.x, this.y, this.backgroundWidth, this.backgroundHeight);
	}

}
