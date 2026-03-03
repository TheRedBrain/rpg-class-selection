package com.github.theredbrain.rpgclassselection.network.packet;

import com.github.theredbrain.rpgclassselection.RPGClassSelection;
import com.github.theredbrain.rpgclassselection.component.type.ClassStateComponent;
import com.github.theredbrain.rpgclassselection.data.RPGClass;
import com.github.theredbrain.rpgclassselection.data.UpgradeEntryComponent;
import com.github.theredbrain.rpgclassselection.registry.CustomDynamicRegistries;
import com.github.theredbrain.rpgclassselection.screen.ClassSelectionScreenHandler;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.spell_engine.api.spell.SpellDataComponents;
import net.spell_engine.api.spell.container.SpellContainer;
import net.spell_engine.item.SpellEngineItems;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UpdateClassPacketReceiver implements ServerPlayNetworking.PlayPayloadHandler<UpdateClassPacket> {
	@Override
	public void receive(UpdateClassPacket payload, ServerPlayNetworking.Context context) {

		ServerPlayerEntity player = context.player();

		World world = player.getEntityWorld();

		ScreenHandler screenHandler = player.currentScreenHandler;

		if (screenHandler instanceof ClassSelectionScreenHandler classSelectionScreenHandler) {

			ItemStack classItemStack = ItemStack.EMPTY;

			ClassStateComponent.ActiveClassState activeClassState = payload.newActiveClassState();
			String rpgClassIdentifierString = activeClassState.activeClassIdentifier();
			if (!rpgClassIdentifierString.isEmpty()) {

				Optional<RegistryEntry.Reference<RPGClass>> optionalRPGClassReference = world.getRegistryManager().get(CustomDynamicRegistries.RPG_CLASS_REGISTRY_KEY).getEntry(Identifier.of(rpgClassIdentifierString));
				if (optionalRPGClassReference.isPresent()) {
					RPGClass rpgClass = optionalRPGClassReference.get().value();
					classItemStack = SpellEngineItems.SPELL_BOOK.get().getDefaultStack();

					List<String> spellIdentifiers = new ArrayList<>();
					List<AttributeModifiersComponent.Entry> attributeModifiers = new ArrayList<>();

					for (int i = 0; i < rpgClass.upgrade_entry_group_list().size(); i++) {
						RPGClass.UpgradeEntryGroup upgradeEntryGroup = rpgClass.upgrade_entry_group_list().get(i);

						String activeUpgradeIdentifier = activeClassState.activeUpgradeIdentifierList().get(i);

						if (!activeUpgradeIdentifier.isEmpty()) {
							for (int j = 0; j < upgradeEntryGroup.upgrade_entry_list().size(); j++) {
								RPGClass.UpgradeEntryGroup.UpgradeEntry upgradeEntry = upgradeEntryGroup.upgrade_entry_list().get(j);

								if (Objects.equals(upgradeEntry.upgrade_identifier(), activeUpgradeIdentifier)) {

									for (UpgradeEntryComponent component : upgradeEntry.component_list()) {

										if (Objects.equals(component.type(), UpgradeEntryComponent.Type.SPELL.asString())) {
											String spellIdentifierString = component.spell_identifier();
											// TODO check if spellIdentifier is valid?
											if (!spellIdentifierString.isEmpty()) {
												spellIdentifiers.add(spellIdentifierString);
											}
										} else if (Objects.equals(component.type(), UpgradeEntryComponent.Type.ATTRIBUTE_MODIFIER.asString())) {

											Optional<RegistryEntry.Reference<EntityAttribute>> optionalEntityAttributeReference = world.getRegistryManager().get(RegistryKeys.ATTRIBUTE).getEntry(Identifier.of(component.attribute_identifier()));

											if (optionalEntityAttributeReference.isPresent()) {
												EntityAttributeModifier entityAttributeModifier = null;
												try {
													entityAttributeModifier = new EntityAttributeModifier(
															RPGClassSelection.identifier("upgrade_" + i),
															component.attribute_modifier_amount(),
															EntityAttributeModifier.Operation.valueOf(component.attribute_modifier_operation())
													);
												} catch (IllegalArgumentException e) {
													RPGClassSelection.warn(e.getMessage());
												}
												if (entityAttributeModifier != null) {
													attributeModifiers.add(new AttributeModifiersComponent.Entry(
															optionalEntityAttributeReference.get(),
															entityAttributeModifier,
															RPGClassSelection.getClassItemAttributeModifierSlot()
													));
												}
											}
										}
									}
								}
							}
						}
					}
					classItemStack.set(SpellDataComponents.SPELL_CONTAINER, new SpellContainer(
							SpellContainer.ContentType.NONE,
							"",
							"",
							spellIdentifiers.size(),
							spellIdentifiers
					));
					classItemStack.set(DataComponentTypes.ATTRIBUTE_MODIFIERS, new AttributeModifiersComponent(
							attributeModifiers,
							true
					));
					classItemStack.set(RPGClassSelection.CLASS_STATE_COMPONENT_TYPE, new ClassStateComponent(activeClassState));
				}
			}

			RPGClassSelection.setClassItemStack(player, classItemStack);
			player.closeHandledScreen();
		}
	}
}