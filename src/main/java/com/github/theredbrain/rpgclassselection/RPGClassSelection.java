package com.github.theredbrain.rpgclassselection;

import com.github.theredbrain.rpgclassselection.compat.RPGInventoryCompat;
import com.github.theredbrain.rpgclassselection.component.type.ClassStateComponent;
import com.github.theredbrain.rpgclassselection.config.ServerConfig;
import com.github.theredbrain.rpgclassselection.data.DisplayedRPGClass;
import com.github.theredbrain.rpgclassselection.data.RPGClass;
import com.github.theredbrain.rpgclassselection.data.UpgradeEntryComponent;
import com.github.theredbrain.rpgclassselection.registry.BlockRegistry;
import com.github.theredbrain.rpgclassselection.registry.CustomDynamicRegistries;
import com.github.theredbrain.rpgclassselection.registry.DataComponentRegistry;
import com.github.theredbrain.rpgclassselection.registry.EntityRegistry;
import com.github.theredbrain.rpgclassselection.registry.ScreenHandlerTypesRegistry;
import com.github.theredbrain.rpgclassselection.registry.ServerEventRegistry;
import com.github.theredbrain.rpgclassselection.registry.ServerPacketRegistry;
import com.github.theredbrain.rpgclassselection.screen.ClassSelectionScreenHandler;
import com.github.theredbrain.rpgclassselection.screen.RPGSeriesClassSelectionScreenHandler;
import com.github.theredbrain.rpgclassselection.screen.ThreeUpgradesClassSelectionScreenHandler;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.component.ComponentType;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.registry.SpellRegistry;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class RPGClassSelection implements ModInitializer {
	public static final String MOD_ID = "rpgclassselection";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static ServerConfig SERVER_CONFIG;

	public static final boolean isArchersLoaded = FabricLoader.getInstance().isModLoaded("archers");
	public static final boolean isLneArchersLoaded = FabricLoader.getInstance().isModLoaded("lne_archers");
	public static final boolean isArchersExpansionLoaded = FabricLoader.getInstance().isModLoaded("archers_expansion");
	public static final boolean isBardsLoaded = FabricLoader.getInstance().isModLoaded("bards_rpg");
	public static final boolean isBerserkerLoaded = FabricLoader.getInstance().isModLoaded("berserker_rpg");
	public static final boolean isDruidsLoaded = FabricLoader.getInstance().isModLoaded("druids");
	public static final boolean isElementalWizardsLoaded = FabricLoader.getInstance().isModLoaded("elemental_wizards_rpg");
	public static final boolean isForcemasterLoaded = FabricLoader.getInstance().isModLoaded("forcemaster_rpg");
	public static final boolean isRoguesLoaded = FabricLoader.getInstance().isModLoaded("rogues");
	public static final boolean isLneRoguesLoaded = FabricLoader.getInstance().isModLoaded("lne_rogues");
	public static final boolean isPaladinsLoaded = FabricLoader.getInstance().isModLoaded("paladins");
	public static final boolean isLnePaladinsLoaded = FabricLoader.getInstance().isModLoaded("lne_paladins");
	public static final boolean isWizardsLoaded = FabricLoader.getInstance().isModLoaded("wizards");
	public static final boolean isLneWizardsLoaded = FabricLoader.getInstance().isModLoaded("lne_wizards");
	public static final boolean isSkillTreeRPGSeriesLoaded = FabricLoader.getInstance().isModLoaded("skill_tree_rpgs");
	public static final boolean isSpellEngineExtensionLoaded = FabricLoader.getInstance().isModLoaded("spellengineextension");

	public static ComponentType<ClassStateComponent> CLASS_STATE_COMPONENT_TYPE;

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing class selection!");
		SERVER_CONFIG = ConfigApiJava.registerAndLoadConfig(ServerConfig::new);

		BlockRegistry.init();
		CustomDynamicRegistries.init();
		DataComponentRegistry.init();
		EntityRegistry.init();
		ScreenHandlerTypesRegistry.registerAll();
		ServerEventRegistry.initializeServerEvents();
		ServerPacketRegistry.init();
		loadBuiltInResourcePacks();
	}

	private static void loadBuiltInResourcePacks() {

		Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer(MOD_ID);
		if (modContainer.isPresent()) {
			if (isArchersLoaded) {
				if (isLneArchersLoaded) {
					ResourceManagerHelper.registerBuiltinResourcePack(identifier("lne_archers_class"), modContainer.get(), Text.translatable("resourcepack.rpgclassselection.archers_class.name"), ResourcePackActivationType.DEFAULT_ENABLED);
				} else {
					ResourceManagerHelper.registerBuiltinResourcePack(identifier("archers_class"), modContainer.get(), Text.translatable("resourcepack.rpgclassselection.archers_class.name"), ResourcePackActivationType.DEFAULT_ENABLED);
				}
			}
			if (isArchersExpansionLoaded) {
				ResourceManagerHelper.registerBuiltinResourcePack(identifier("archers_expansion_classes"), modContainer.get(), Text.translatable("resourcepack.rpgclassselection.archers_expansion_classes.name"), ResourcePackActivationType.DEFAULT_ENABLED);
			}
			if (isBardsLoaded) {
				ResourceManagerHelper.registerBuiltinResourcePack(identifier("bard_class"), modContainer.get(), Text.translatable("resourcepack.rpgclassselection.bard_class.name"), ResourcePackActivationType.DEFAULT_ENABLED);
			}
			if (isBerserkerLoaded) {
				ResourceManagerHelper.registerBuiltinResourcePack(identifier("berserker_class"), modContainer.get(), Text.translatable("resourcepack.rpgclassselection.berserker_class.name"), ResourcePackActivationType.DEFAULT_ENABLED);
			}
			ResourceManagerHelper.registerBuiltinResourcePack(identifier("compat_pack_resources"), modContainer.get(), Text.translatable("resourcepack.rpgclassselection.compat_pack_resources.name"), ResourcePackActivationType.DEFAULT_ENABLED);
			if (isDruidsLoaded) {
				ResourceManagerHelper.registerBuiltinResourcePack(identifier("druids_class"), modContainer.get(), Text.translatable("resourcepack.rpgclassselection.druids_class.name"), ResourcePackActivationType.DEFAULT_ENABLED);
			}
			if (isElementalWizardsLoaded) {
				ResourceManagerHelper.registerBuiltinResourcePack(identifier("elemental_wizards_classes"), modContainer.get(), Text.translatable("resourcepack.rpgclassselection.elemental_wizards_classes.name"), ResourcePackActivationType.DEFAULT_ENABLED);
			}
			if (isForcemasterLoaded) {
				ResourceManagerHelper.registerBuiltinResourcePack(identifier("forcemaster_class"), modContainer.get(), Text.translatable("resourcepack.rpgclassselection.forcemaster_class.name"), ResourcePackActivationType.DEFAULT_ENABLED);
			}
			if (isPaladinsLoaded) {
				if (isLnePaladinsLoaded) {
					ResourceManagerHelper.registerBuiltinResourcePack(identifier("lne_paladins_and_priests_classes"), modContainer.get(), Text.translatable("resourcepack.rpgclassselection.paladins_and_priests_classes.name"), ResourcePackActivationType.DEFAULT_ENABLED);
				} else {
					ResourceManagerHelper.registerBuiltinResourcePack(identifier("paladins_and_priests_classes"), modContainer.get(), Text.translatable("resourcepack.rpgclassselection.paladins_and_priests_classes.name"), ResourcePackActivationType.DEFAULT_ENABLED);
				}
			}
			if (isRoguesLoaded) {
				if (isLneRoguesLoaded) {
					ResourceManagerHelper.registerBuiltinResourcePack(identifier("lne_rogues_and_warriors_classes"), modContainer.get(), Text.translatable("resourcepack.rpgclassselection.rogues_and_warriors_classes.name"), ResourcePackActivationType.DEFAULT_ENABLED);
				} else {
					ResourceManagerHelper.registerBuiltinResourcePack(identifier("rogues_and_warriors_classes"), modContainer.get(), Text.translatable("resourcepack.rpgclassselection.rogues_and_warriors_classes.name"), ResourcePackActivationType.DEFAULT_ENABLED);
				}
			}
			if (isSpellEngineExtensionLoaded && isSkillTreeRPGSeriesLoaded) {
				ResourceManagerHelper.registerBuiltinResourcePack(identifier("weapon_skill_upgrade_enchantments"), modContainer.get(), Text.translatable("resourcepack.rpgclassselection.weapon_skill_upgrade_enchantments.name"), ResourcePackActivationType.DEFAULT_ENABLED);
			}
			if (isWizardsLoaded) {
				if (isLneWizardsLoaded) {
					ResourceManagerHelper.registerBuiltinResourcePack(identifier("lne_wizards_classes"), modContainer.get(), Text.translatable("resourcepack.rpgclassselection.wizards_classes.name"), ResourcePackActivationType.DEFAULT_ENABLED);
				} else {
					ResourceManagerHelper.registerBuiltinResourcePack(identifier("wizards_classes"), modContainer.get(), Text.translatable("resourcepack.rpgclassselection.wizards_classes.name"), ResourcePackActivationType.DEFAULT_ENABLED);
				}
			}
		}
	}

	public static AttributeModifierSlot getClassItemAttributeModifierSlot() {
		AttributeModifierSlot attributeModifierSlot = AttributeModifierSlot.ANY;
		attributeModifierSlot = RPGInventoryCompat.getClassItemAttributeModifierSlot();
		return attributeModifierSlot;
	}

	public static ItemStack getClassItemStack(PlayerEntity player) {
		ItemStack classItemStack = ItemStack.EMPTY;
		classItemStack = RPGInventoryCompat.getClassItemStack(player);
		return classItemStack;
	}

	public static void setClassItemStack(PlayerEntity player, ItemStack classItemStack) {
		RPGInventoryCompat.setClassItemStack(player, classItemStack);
	}

	public static void openRPGClassSelectionScreen(ServerPlayerEntity player, String initial_class_identifier_string, boolean restrict_class_list, boolean allow_changing_class, boolean allow_changing_upgrades) {

		MinecraftServer server = player.server;

		if (server != null) {

			ItemStack currentClassItemStack = RPGClassSelection.getClassItemStack(player);

			ClassStateComponent.ActiveClassState activeClassState = currentClassItemStack.getOrDefault(RPGClassSelection.CLASS_STATE_COMPONENT_TYPE, ClassStateComponent.DEFAULT).activeClassState();
			String currentClassIdentifierString = activeClassState.activeClassIdentifier();

			if (initial_class_identifier_string.isEmpty() && allow_changing_class) {
				initial_class_identifier_string = currentClassIdentifierString;
			}

			List<ClassSelectionScreenHandler.ClassSelectionScreenData.ClassUnlockStateData> classUnlockStateDataList = new ArrayList<>();

			List<DisplayedRPGClass> displayedRPGClassList = new ArrayList<>();
			int classIndex = 0;

			// add empty class
			if ((allow_changing_class && !restrict_class_list) || currentClassIdentifierString.isEmpty()) {
				classUnlockStateDataList.add(new ClassSelectionScreenHandler.ClassSelectionScreenData.ClassUnlockStateData(
						ClassSelectionScreenHandler.ClassSelectionScreenData.UnlockState.UNLOCKED.asString(),
						new ArrayList<>()
				));
				displayedRPGClassList.add(DisplayedRPGClass.DEFAULT);
				classIndex = classIndex + 1;
			}

			int initialClassIndex = 0;
			ClassSelectionScreenHandler.EmptyUpgradeMode emptyUpgradeMode = RPGClassSelection.SERVER_CONFIG.empty_upgrade_mode.get();

			Optional<LootContextPredicate> optionalEntityPredicate;

			for (Map.Entry<RegistryKey<RPGClass>, RPGClass> entry : player.getWorld().getRegistryManager().get(CustomDynamicRegistries.RPG_CLASS_REGISTRY_KEY).getEntrySet()) {
				RPGClass rpgClass = entry.getValue();

				boolean isClassUnlocked = true;
				List<ClassSelectionScreenHandler.ClassSelectionScreenData.ClassUnlockStateData.UpgradeUnlockStateData> upgradeUnlockStateDataList = new ArrayList<>();

				optionalEntityPredicate = rpgClass.unlock_predicate();

				if (optionalEntityPredicate.isPresent()) {
					isClassUnlocked = optionalEntityPredicate.get().test(EntityPredicate.createAdvancementEntityLootContext(player, player));
				}

				boolean isCurrentClass = entry.getKey().getValue().toString().equals(currentClassIdentifierString);

				List<DisplayedRPGClass.DisplayedUpgradeEntryGroup> displayedUpgradeEntryGroupList = new ArrayList<>();
				if (isClassUnlocked || isCurrentClass) {

					int groupIndex = 0;
					for (RPGClass.UpgradeEntryGroup upgradeEntryGroup : rpgClass.upgrade_entry_group_list()) {
						List<DisplayedRPGClass.DisplayedUpgradeEntryGroup.DisplayedUpgradeEntry> displayedUpgradeEntryList = new ArrayList<>();
						List<Boolean> upgradeUnlockStatesList = new ArrayList<>();

						String currentUpgradeIdentifierString = "";
						if (groupIndex < activeClassState.activeUpgradeIdentifierList().size()) {
							currentUpgradeIdentifierString = activeClassState.activeUpgradeIdentifierList().get(groupIndex);
						}

						for (RPGClass.UpgradeEntryGroup.UpgradeEntry upgradeEntry : upgradeEntryGroup.upgrade_entry_list()) {

							// check if all upgrade entry components are valid
							boolean upgradeEntryIsValid = true;

							for (UpgradeEntryComponent upgradeEntryComponent : upgradeEntry.component_list()) {

								if (Objects.equals(upgradeEntryComponent.type(), UpgradeEntryComponent.Type.SPELL.asString())) {
									Optional<RegistryEntry.Reference<Spell>> optionalSpellReference = player.getServerWorld().getRegistryManager().get(SpellRegistry.KEY).getEntry(Identifier.of(upgradeEntryComponent.spell_identifier()));

									if (optionalSpellReference.isEmpty()) {
										upgradeEntryIsValid = false;
									}
								} else if (Objects.equals(upgradeEntryComponent.type(), UpgradeEntryComponent.Type.ATTRIBUTE_MODIFIER.asString())) {

									Optional<RegistryEntry.Reference<EntityAttribute>> optionalEntityAttributeReference = player.getServerWorld().getRegistryManager().get(RegistryKeys.ATTRIBUTE).getEntry(Identifier.of(upgradeEntryComponent.attribute_identifier()));

									if (optionalEntityAttributeReference.isEmpty()) {
										upgradeEntryIsValid = false;
									}
								}
							}

							if (!upgradeEntryIsValid) {
								if (RPGClassSelection.SERVER_CONFIG.show_invalid_upgrade_entries.get()) {
									displayedUpgradeEntryList.add(DisplayedRPGClass.DisplayedUpgradeEntryGroup.DisplayedUpgradeEntry.INVALID_UPGRADE);
									upgradeUnlockStatesList.add(false);
								}
								continue;
							}

							boolean isUpgradeUnlocked = true;
							optionalEntityPredicate = upgradeEntry.unlock_predicate();

							if (optionalEntityPredicate.isPresent()) {
								isUpgradeUnlocked = optionalEntityPredicate.get().test(EntityPredicate.createAdvancementEntityLootContext(player, player));
							}

							boolean isCurrentUpgrade = Objects.equals(upgradeEntry.upgrade_identifier(), currentUpgradeIdentifierString);

							if ((isClassUnlocked || isCurrentUpgrade) && (isUpgradeUnlocked || upgradeEntry.visible_when_locked()) && (allow_changing_upgrades || isCurrentUpgrade)) {
								displayedUpgradeEntryList.add(new DisplayedRPGClass.DisplayedUpgradeEntryGroup.DisplayedUpgradeEntry(
										upgradeEntry.upgrade_identifier(),
										isUpgradeUnlocked ? upgradeEntry.title() : upgradeEntry.locked_title(),
										upgradeEntry.icon_path(),
										upgradeEntry.component_list()
								));
								upgradeUnlockStatesList.add(isUpgradeUnlocked);
							} else {
								displayedUpgradeEntryList.add(DisplayedRPGClass.DisplayedUpgradeEntryGroup.DisplayedUpgradeEntry.LOCKED_UPGRADE);
								upgradeUnlockStatesList.add(false);
							}
						}

						boolean groupEntryListIsEmpty = displayedUpgradeEntryList.isEmpty();
						if (
								((!groupEntryListIsEmpty && allow_changing_upgrades && emptyUpgradeMode == ClassSelectionScreenHandler.EmptyUpgradeMode.NON_EMPTY_GROUPS) ||
										(groupEntryListIsEmpty && emptyUpgradeMode == ClassSelectionScreenHandler.EmptyUpgradeMode.EMPTY_GROUPS) ||
										((allow_changing_upgrades || groupEntryListIsEmpty) && emptyUpgradeMode == ClassSelectionScreenHandler.EmptyUpgradeMode.ALWAYS)
												&& isClassUnlocked)
						) {
							displayedUpgradeEntryList.addFirst(DisplayedRPGClass.DisplayedUpgradeEntryGroup.DisplayedUpgradeEntry.EMPTY_UPGRADE);
							upgradeUnlockStatesList.addFirst(true);
						}

						displayedUpgradeEntryGroupList.add(
								new DisplayedRPGClass.DisplayedUpgradeEntryGroup(displayedUpgradeEntryList)
						);
						upgradeUnlockStateDataList.add(
								new ClassSelectionScreenHandler.ClassSelectionScreenData.ClassUnlockStateData.UpgradeUnlockStateData(upgradeUnlockStatesList)
						);

						groupIndex = groupIndex + 1;
					}

				}

				boolean isInitialClass = entry.getKey().getValue().toString().equals(initial_class_identifier_string);

				if ((isClassUnlocked || rpgClass.visible_when_locked() || isCurrentClass)
						&& (isCurrentClass || allow_changing_class)
						&& (!restrict_class_list || isInitialClass || isCurrentClass || initial_class_identifier_string.isEmpty())
				) {

					classUnlockStateDataList.add(new ClassSelectionScreenHandler.ClassSelectionScreenData.ClassUnlockStateData(
							isClassUnlocked ? ClassSelectionScreenHandler.ClassSelectionScreenData.UnlockState.UNLOCKED.asString() : isCurrentClass ? ClassSelectionScreenHandler.ClassSelectionScreenData.UnlockState.DISPLAY.asString() : ClassSelectionScreenHandler.ClassSelectionScreenData.UnlockState.LOCKED.asString(),
							upgradeUnlockStateDataList
					));
					displayedRPGClassList.add(new DisplayedRPGClass(
							rpgClass.class_identifier(),
							isClassUnlocked || isCurrentClass ? rpgClass.description() : rpgClass.locked_description(),
							displayedUpgradeEntryGroupList
					));

					if (isInitialClass) {
						initialClassIndex = classIndex;
					}

					classIndex = classIndex + 1;

				}
			}

			int finalInitialClassIndex = initialClassIndex;
			player.openHandledScreen(new ExtendedScreenHandlerFactory<>() {
				@Override
				public ClassSelectionScreenHandler.ClassSelectionScreenData getScreenOpeningData(ServerPlayerEntity player) {
					return new ClassSelectionScreenHandler.ClassSelectionScreenData(
							finalInitialClassIndex,
							activeClassState,
							classUnlockStateDataList,
							displayedRPGClassList
					);
				}

				@Override
				public Text getDisplayName() {
					return Text.translatable("Class Selection");
				}

				@Nullable
				@Override
				public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
					if (RPGClassSelection.SERVER_CONFIG.class_selection_screen_type.get() == ClassSelectionScreenHandler.ClassSelectionScreenType.RPG_SERIES) {
						return new RPGSeriesClassSelectionScreenHandler(
								syncId,
								playerInventory,
								finalInitialClassIndex,
								activeClassState,
								classUnlockStateDataList,
								displayedRPGClassList
						);
					} else {
						return new ThreeUpgradesClassSelectionScreenHandler(
								syncId,
								playerInventory,
								finalInitialClassIndex,
								activeClassState,
								classUnlockStateDataList,
								displayedRPGClassList
						);
					}
				}
			});
		}
	}

	public static Identifier identifier(String path) {
		return Identifier.of(MOD_ID, path);
	}

	public static void info(String message) {
		LOGGER.info("[" + MOD_ID + "] [info]: " + message);
	}

	public static void warn(String message) {
		LOGGER.warn("[" + MOD_ID + "] [warn]: " + message);
	}

}