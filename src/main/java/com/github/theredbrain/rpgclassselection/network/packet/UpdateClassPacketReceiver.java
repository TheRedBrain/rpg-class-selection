package com.github.theredbrain.rpgclassselection.network.packet;

import com.github.theredbrain.rpgclassselection.RPGClassSelection;
import com.github.theredbrain.rpgclassselection.component.type.ClassStateComponent;
import com.github.theredbrain.rpgclassselection.data.RPGClass;
import com.github.theredbrain.rpgclassselection.registry.CustomDynamicRegistries;
import com.github.theredbrain.rpgclassselection.screen.ClassSelectionScreenHandler;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.spell_engine.api.spell.SpellDataComponents;
import net.spell_engine.api.spell.container.SpellContainer;

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

				RPGClassSelection.info("!rpgClassIdentifierString.isEmpty()");

				Optional<RegistryEntry.Reference<RPGClass>> optionalRPGClassReference = world.getRegistryManager().get(CustomDynamicRegistries.RPG_CLASS_REGISTRY_KEY).getEntry(Identifier.of(rpgClassIdentifierString));
				if (optionalRPGClassReference.isPresent()) {
					RPGClass rpgClass = optionalRPGClassReference.get().value();
					Optional<RegistryEntry.Reference<Item>> optionalItemReference = world.getRegistryManager().get(RegistryKeys.ITEM).getEntry(Identifier.of(rpgClass.classItemIdentifierString()));
					if (optionalItemReference.isPresent()) {
						classItemStack = optionalItemReference.get().value().getDefaultStack();

						List<String> spellIdentifiers = new ArrayList<>();
						List<AttributeModifiersComponent.Entry> attributeModifiers = new ArrayList<>();

						for (int i = 0; i < rpgClass.upgradeEntryGroupList().size(); i++) {
							RPGClass.UpgradeEntryGroup upgradeEntryGroup = rpgClass.upgradeEntryGroupList().get(i);

							for (int j = 0; j < upgradeEntryGroup.upgradeEntryList().size(); j++) {
								RPGClass.UpgradeEntryGroup.UpgradeEntry upgradeEntry = upgradeEntryGroup.upgradeEntryList().get(j);
								if (Objects.equals(upgradeEntry.upgradeIdentifier(), activeClassState.activeUpgradeIdentifierList().get(i))) {

									if (Objects.equals(upgradeEntry.entryType(), RPGClass.UpgradeEntryGroup.UpgradeEntry.Type.SPELL.asString())) {
										String spellIdentifierString = upgradeEntry.spellIdentifierString();
										// TODO check if spellIdentifier is valid?
										if (!spellIdentifierString.isEmpty()) {
											spellIdentifiers.add(spellIdentifierString);
										}
									} else if (Objects.equals(upgradeEntry.entryType(), RPGClass.UpgradeEntryGroup.UpgradeEntry.Type.ATTRIBUTE_MODIFIER.asString())) {

										Optional<RegistryEntry.Reference<EntityAttribute>> optionalEntityAttributeReference = world.getRegistryManager().get(RegistryKeys.ATTRIBUTE).getEntry(Identifier.of(upgradeEntry.attributeIdentifier()));

										if (optionalEntityAttributeReference.isPresent()) {
											EntityAttributeModifier entityAttributeModifier = null;
											try {
												entityAttributeModifier = new EntityAttributeModifier(
														Identifier.of(upgradeEntry.attributeModifierIdentifier()),
														upgradeEntry.attributeModifierAmount(),
														EntityAttributeModifier.Operation.valueOf(upgradeEntry.attributeModifierOperation())
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
						classItemStack.set(SpellDataComponents.SPELL_CONTAINER, new SpellContainer(
								SpellContainer.ContentType.ANY,
								false,
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
			}

			RPGClassSelection.info("classItemStack: " + classItemStack.toString());

			RPGClassSelection.setClassItemStack(player, classItemStack);
			player.closeHandledScreen();
		}
	}
}