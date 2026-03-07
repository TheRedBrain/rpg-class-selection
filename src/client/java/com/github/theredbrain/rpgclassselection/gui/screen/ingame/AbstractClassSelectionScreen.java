package com.github.theredbrain.rpgclassselection.gui.screen.ingame;

import com.github.theredbrain.rpgclassselection.RPGClassSelection;
import com.github.theredbrain.rpgclassselection.RPGClassSelectionClient;
import com.github.theredbrain.rpgclassselection.component.type.ClassStateComponent;
import com.github.theredbrain.rpgclassselection.config.ClientConfig;
import com.github.theredbrain.rpgclassselection.data.DisplayedRPGClass;
import com.github.theredbrain.rpgclassselection.data.UpgradeEntryComponent;
import com.github.theredbrain.rpgclassselection.network.packet.UpdateClassPacket;
import com.github.theredbrain.rpgclassselection.screen.ClassSelectionScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.registry.SpellRegistry;
import net.spell_engine.client.gui.SpellTooltip;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public abstract class AbstractClassSelectionScreen extends HandledScreen<ClassSelectionScreenHandler> {
	protected static final Identifier SCROLL_BAR_BACKGROUND_TEXTURE = RPGClassSelection.identifier("scroll_bar/scroll_bar_background");
	protected static final Identifier SCROLLER_TEXTURE = RPGClassSelection.identifier("scroll_bar/scroller_vertical_6_7");
	protected static final Identifier DESCRIPTION_FIELD_SCROLLER_TEXTURE = RPGClassSelection.identifier("scroll_bar/description_field_scroller");
	public static Identifier BACKGROUND_TEXTURE;
	protected static final Text UNLOCKED_CHOOSE_CLASS_BUTTON_LABEL_TEXT = Text.translatable("class_selection_screen.unlocked_choose_class_button_label");
	protected static final Text DISPLAYED_CHOOSE_CLASS_BUTTON_LABEL_TEXT = Text.translatable("class_selection_screen.displayed_choose_class_button_label");
	protected static final Text LOCKED_CHOOSE_CLASS_BUTTON_LABEL_TEXT = Text.translatable("class_selection_screen.locked_choose_class_button_label");

	protected ClassStateComponent.ActiveClassState newActiveClassState;

	protected int currentClassIndex;
	protected String activeClassDescription = "";
	protected final List<Integer> currentUpgradeIndexList = new ArrayList<>();

	protected int classDescriptionScrollPosition = 0;
	protected float classDescriptionScrollAmount = 0.0f;
	protected boolean classDescriptionMouseClicked = false;

	public AbstractClassSelectionScreen(ClassSelectionScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	@Override
	public void resize(MinecraftClient client, int width, int height) {
		int number = this.classDescriptionScrollPosition;
		float number1 = this.classDescriptionScrollAmount;
		boolean bool = this.classDescriptionMouseClicked;
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
		this.classDescriptionScrollPosition = number;
		this.classDescriptionScrollAmount = number1;
		this.classDescriptionMouseClicked = bool;
		this.updateWidgets();
	}

	protected void cycleClassIndexBackwards() {
		int index = this.currentClassIndex;
		index = index - 1;
		if (index < 0) {
			index = this.handler.getDisplayedRpgClassList().size() - 1;
		}
		this.currentClassIndex = index;

		this.classDescriptionScrollPosition = 0;
		this.classDescriptionScrollAmount = 0.0F;
	}

	protected void cycleClassIndexForwards() {
		int index = this.currentClassIndex;
		index = index + 1;
		if (index >= this.handler.getDisplayedRpgClassList().size()) {
			index = 0;
		}
		this.currentClassIndex = index;

		this.classDescriptionScrollPosition = 0;
		this.classDescriptionScrollAmount = 0.0F;
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
		DisplayedRPGClass displayedRPGClass = this.handler.getDisplayedRpgClassList().get(this.currentClassIndex);
		if (Objects.equals(displayedRPGClass.class_identifier(), this.handler.getActiveClassState().activeClassIdentifier())) {
			List<String> list = this.handler.getActiveClassState().activeUpgradeIdentifierList();
			for (int i = 0; i < list.size(); i++) {
				String upgradeIdentifier = list.get(i);
				if (!upgradeIdentifier.isEmpty()) {
					if (i < displayedRPGClass.displayed_upgrade_entry_group_list().size()) {
						List<DisplayedRPGClass.DisplayedUpgradeEntryGroup.DisplayedUpgradeEntry> displayedUpgradeEntryList = displayedRPGClass.displayed_upgrade_entry_group_list().get(i).displayed_upgrade_entry_list();
						for (int j = 0; j < displayedUpgradeEntryList.size(); j++) {

							DisplayedRPGClass.DisplayedUpgradeEntryGroup.DisplayedUpgradeEntry displayedUpgradeEntry = displayedUpgradeEntryList.get(j);
							if (upgradeIdentifier.equals(displayedUpgradeEntry.upgrade_identifier())) {
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
			for (int i = 0; i < displayedRPGClass.displayed_upgrade_entry_group_list().size(); i++) {
				this.currentUpgradeIndexList.add(0);
			}
		}
	}

	protected void updateActiveClassState() {
		DisplayedRPGClass displayedRPGClass = this.handler.getDisplayedRpgClassList().get(this.currentClassIndex);
		List<String> activeUpgradeIdentifierList = this.getActiveUpgradeIdentifierList();
		this.newActiveClassState = new ClassStateComponent.ActiveClassState(
				displayedRPGClass.class_identifier(),
				activeUpgradeIdentifierList
		);

		this.activeClassDescription = displayedRPGClass.description();

		this.updateWidgets();
	}

	private List<String> getActiveUpgradeIdentifierList() {
		List<String> activeUpgradeIdentifierList = new ArrayList<>();
		DisplayedRPGClass displayedRPGClass = this.handler.getDisplayedRpgClassList().get(this.currentClassIndex);
		List<ClassSelectionScreenHandler.ClassSelectionScreenData.ClassUnlockStateData.UpgradeUnlockStateData> upgradeUnlockStateDataList = this.handler.getClassUnlockStateDataList().get(this.currentClassIndex).upgradeUnlockStateDataList();
		List<DisplayedRPGClass.DisplayedUpgradeEntryGroup> displayedUpgradeEntryGroupList = displayedRPGClass.displayed_upgrade_entry_group_list();
		for (int i = 0; i < displayedUpgradeEntryGroupList.size(); i++) {
			if (i < this.currentUpgradeIndexList.size() && i < upgradeUnlockStateDataList.size()) {
				int currentUpgradeIndex = this.currentUpgradeIndexList.get(i);
				List<DisplayedRPGClass.DisplayedUpgradeEntryGroup.DisplayedUpgradeEntry> upgradeEntryList = displayedUpgradeEntryGroupList.get(i).displayed_upgrade_entry_list();
				List<Boolean> upgradeUnlockStatesList = upgradeUnlockStateDataList.get(i).upgradeUnlockStatesList();
				if (currentUpgradeIndex < upgradeEntryList.size() && currentUpgradeIndex < upgradeUnlockStatesList.size()) {
					if (upgradeUnlockStatesList.get(currentUpgradeIndex)) {
						activeUpgradeIdentifierList.add(upgradeEntryList.get(currentUpgradeIndex).upgrade_identifier());
						continue;
					}
				}
			}
			activeUpgradeIdentifierList.add("");
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

	protected List<OrderedText> getActiveClassDescriptionLines() {
		return this.textRenderer.wrapLines(Text.translatable(this.activeClassDescription), this.getClassDescriptionFieldWidth() - 8);
	}

	protected abstract int getClassDescriptionFieldX();

	protected abstract int getClassDescriptionFieldY();

	protected abstract int getClassDescriptionFieldWidth();

	protected abstract int getClassDescriptionFieldHeight();

	protected abstract int getClassDescriptionFieldMaxLineAmount();

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		List<OrderedText> classDescriptionLines = getActiveClassDescriptionLines();
		this.classDescriptionMouseClicked = false;
		if (classDescriptionLines.size() > this.getClassDescriptionFieldMaxLineAmount()) {
			if (mouseX >= this.x + this.getClassDescriptionFieldX() + this.getClassDescriptionFieldWidth() - 6
					&& mouseX < this.x + this.getClassDescriptionFieldX() + this.getClassDescriptionFieldWidth()
					&& mouseY >= (double) this.y + this.getClassDescriptionFieldY()
					&& mouseY < (double) this.y + this.getClassDescriptionFieldY() + this.getClassDescriptionFieldHeight()
			) {
				this.classDescriptionMouseClicked = true;
			}
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		List<OrderedText> classDescriptionLines = getActiveClassDescriptionLines();
		if (this.classDescriptionMouseClicked) {
			int i = classDescriptionLines.size() - this.getClassDescriptionFieldMaxLineAmount();
			float f = (float) deltaY / (float) i;
			this.classDescriptionScrollAmount = MathHelper.clamp(this.classDescriptionScrollAmount + f, 0.0f, 1.0f);
			this.classDescriptionScrollPosition = (int) ((double) (this.classDescriptionScrollAmount * (float) i));
		}
		return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
		List<OrderedText> classDescriptionLines = getActiveClassDescriptionLines();
		if (classDescriptionLines.size() > this.getClassDescriptionFieldMaxLineAmount()
				&& mouseX >= this.x + this.getClassDescriptionFieldX()
				&& mouseX < this.x + this.getClassDescriptionFieldX() + this.getClassDescriptionFieldWidth()
				&& mouseY >= (double) this.y + this.getClassDescriptionFieldY()
				&& mouseY < (double) this.y + this.getClassDescriptionFieldY() + this.getClassDescriptionFieldHeight()
		) {
			int i = classDescriptionLines.size() - this.getClassDescriptionFieldMaxLineAmount();
			float f = (float) verticalAmount / (float) i;
			this.classDescriptionScrollAmount = MathHelper.clamp(this.classDescriptionScrollAmount - f, 0.0f, 1.0f);
			this.classDescriptionScrollPosition = (int) ((double) (this.classDescriptionScrollAmount * (float) i));
		}
		return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
	}

	@Override
	protected void drawForeground(DrawContext context, int mouseX, int mouseY) {

		this.drawClassTitleAndDescription(context);

		this.drawUpgradeEntryTitles(context);
	}

	@Override
	protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
		context.drawTexture(BACKGROUND_TEXTURE, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight, this.backgroundWidth, this.backgroundHeight);

		if (getActiveClassDescriptionLines().size() > this.getClassDescriptionFieldMaxLineAmount()) {
			int k = (int) ((this.getClassDescriptionFieldHeight() - 7) * this.classDescriptionScrollAmount);
			context.drawGuiTexture(DESCRIPTION_FIELD_SCROLLER_TEXTURE, this.x + this.getClassDescriptionFieldX() + this.getClassDescriptionFieldWidth() - 6, this.y + this.getClassDescriptionFieldY() + k, 6, 7);
		}

		this.drawUpgradeEntryIcons(context);
	}

	protected List<Text> getUpgradeEntryTooltipList(DisplayedRPGClass.DisplayedUpgradeEntryGroup.DisplayedUpgradeEntry upgradeEntry) {
		List<Text> list = new ArrayList<>();

		for (UpgradeEntryComponent upgradeEntryComponent : upgradeEntry.component_list()) {

			if (Objects.equals(upgradeEntryComponent.type(), UpgradeEntryComponent.Type.SPELL.asString())) {
				Optional<RegistryEntry.Reference<Spell>> optionalSpellReference = this.handler.getWorld().getRegistryManager().get(SpellRegistry.KEY).getEntry(Identifier.of(upgradeEntryComponent.spell_identifier()));

				if (optionalSpellReference.isPresent() && this.handler.getPlayer() != null) {
					list.addAll(SpellTooltip.spellEntry(optionalSpellReference.get(), this.handler.getPlayer(), ItemStack.EMPTY, true, 0));
				}
			} else if (Objects.equals(upgradeEntryComponent.type(), UpgradeEntryComponent.Type.ATTRIBUTE_MODIFIER.asString())) {

				Optional<RegistryEntry.Reference<EntityAttribute>> optionalEntityAttributeReference = this.handler.getWorld().getRegistryManager().get(RegistryKeys.ATTRIBUTE).getEntry(Identifier.of(upgradeEntryComponent.attribute_identifier()));

				if (optionalEntityAttributeReference.isPresent()) {
					RegistryEntry<EntityAttribute> entityAttributeRegistryEntry = optionalEntityAttributeReference.get();
					double d = upgradeEntryComponent.attribute_modifier_amount();

					EntityAttributeModifier.Operation entityAttributeModifierOperation = null;
					try {
						entityAttributeModifierOperation = EntityAttributeModifier.Operation.valueOf(upgradeEntryComponent.attribute_modifier_operation());
					} catch (IllegalArgumentException e) {
						RPGClassSelection.warn(e.getMessage());
					}
					if (entityAttributeModifierOperation != null) {
						double displayAmount;
						if (entityAttributeModifierOperation == EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
								|| entityAttributeModifierOperation == EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
							displayAmount = d * 100.0;
						} else if (entityAttributeRegistryEntry.matches(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE)) {
							displayAmount = d * 10.0;
						} else {
							displayAmount = d;
						}
						if (d > 0.0) {
							list.add(
									Text.translatable(
													"attribute.modifier.plus." + entityAttributeModifierOperation.getId(),
													AttributeModifiersComponent.DECIMAL_FORMAT.format(displayAmount),
													Text.translatable(entityAttributeRegistryEntry.value().getTranslationKey())
											)
											.formatted(entityAttributeRegistryEntry.value().getFormatting(true))
							);
						} else if (d < 0.0) {
							list.add(
									Text.translatable(
													"attribute.modifier.take." + entityAttributeModifierOperation.getId(),
													AttributeModifiersComponent.DECIMAL_FORMAT.format(-displayAmount),
													Text.translatable(entityAttributeRegistryEntry.value().getTranslationKey())
											)
											.formatted(entityAttributeRegistryEntry.value().getFormatting(false))
							);
						}
					}
				}
			}
		}
		return list;
	}

	protected void drawClassTitleAndDescription(DrawContext context) {

		ClientConfig clientConfig = RPGClassSelectionClient.CLIENT_CONFIG;

		Text className = Text.translatable("class_selection_screen." + this.newActiveClassState.activeClassIdentifier().replace(":", ".") + ".title");

		context.drawText(this.textRenderer, className, (this.backgroundWidth - this.textRenderer.getWidth(className)) / 2, 13, clientConfig.class_name_text_colour.toInt(), false);

		List<OrderedText> classDescriptionLines = getActiveClassDescriptionLines();
		for (int i = this.classDescriptionScrollPosition; i < Math.min(this.getClassDescriptionFieldMaxLineAmount() + this.classDescriptionScrollPosition, classDescriptionLines.size()); i++) {

			context.drawText(this.textRenderer, classDescriptionLines.get(i), 11, 35 + (i - this.classDescriptionScrollPosition) * 9, clientConfig.class_description_text_colour.toInt(), false);
		}
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
