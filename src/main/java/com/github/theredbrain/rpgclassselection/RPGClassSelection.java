package com.github.theredbrain.rpgclassselection;

import com.github.theredbrain.rpgclassselection.compat.RPGInventoryCompat;
import com.github.theredbrain.rpgclassselection.component.type.ClassStateComponent;
import com.github.theredbrain.rpgclassselection.config.ServerConfig;
import com.github.theredbrain.rpgclassselection.data.RPGClass;
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
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.component.ComponentType;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerAdvancementLoader;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
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

	public static ComponentType<ClassStateComponent> CLASS_STATE_COMPONENT_TYPE;

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

			PlayerAdvancementTracker playerAdvancementTracker = player.getAdvancementTracker();
			ServerAdvancementLoader serverAdvancementLoader = server.getAdvancementLoader();

			if (serverAdvancementLoader != null && playerAdvancementTracker != null) {

				ItemStack currentClassItemStack = RPGClassSelection.getClassItemStack(player);

				ClassStateComponent.ActiveClassState activeClassState = currentClassItemStack.getOrDefault(RPGClassSelection.CLASS_STATE_COMPONENT_TYPE, ClassStateComponent.DEFAULT).activeClassState();
				String currentClassIdentifierString = activeClassState.activeClassIdentifier();

				if (initial_class_identifier_string.isEmpty() && allow_changing_class) {
					initial_class_identifier_string = currentClassIdentifierString;
				}

				List<ClassSelectionScreenHandler.ClassSelectionScreenData.ClassUnlockStateData> classUnlockStateDataList = new ArrayList<>();

				List<RPGClass> rpgClassList = new ArrayList<>();
				int classIndex = 0;

				// add empty class
				if ((allow_changing_class && !restrict_class_list) || currentClassIdentifierString.isEmpty()) {
					classUnlockStateDataList.add(new ClassSelectionScreenHandler.ClassSelectionScreenData.ClassUnlockStateData(
							true,
							new ArrayList<>()
					));
					rpgClassList.add(RPGClass.DEFAULT);
					classIndex = classIndex + 1;
				}

				int initialClassIndex = 0;
				ClassSelectionScreenHandler.EmptyUpgradeMode emptyUpgradeMode = RPGClassSelection.SERVER_CONFIG.empty_upgrade_mode.get();

				for (Map.Entry<RegistryKey<RPGClass>, RPGClass> entry : player.getWorld().getRegistryManager().get(CustomDynamicRegistries.RPG_CLASS_REGISTRY_KEY).getEntrySet()) {
					RPGClass rpgClass = entry.getValue();

					boolean isClassUnlocked = true;
					List<ClassSelectionScreenHandler.ClassSelectionScreenData.ClassUnlockStateData.UpgradeUnlockStateData> upgradeUnlockStateDataList = new ArrayList<>();

					String unlockAdvancementIdentifierString = rpgClass.unlock_advancement_identifier();
					if (!unlockAdvancementIdentifierString.isEmpty()) {

						AdvancementEntry unlockAdvancementEntry = serverAdvancementLoader.get(Identifier.of(unlockAdvancementIdentifierString));

						if (unlockAdvancementEntry != null) {
							isClassUnlocked = playerAdvancementTracker.getProgress(unlockAdvancementEntry).isDone();
						}
					}

					List<RPGClass.UpgradeEntryGroup> upgradeEntryGroupList = new ArrayList<>();
					if (isClassUnlocked) {

						int groupIndex = 0;
						for (RPGClass.UpgradeEntryGroup upgradeEntryGroup : rpgClass.upgrade_entry_group_list()) {
							List<RPGClass.UpgradeEntryGroup.UpgradeEntry> upgradeEntryList = new ArrayList<>();
							List<Boolean> upgradeUnlockStatesList = new ArrayList<>();

							String currentUpgradeIdentifierString = "";
							if (groupIndex < activeClassState.activeUpgradeIdentifierList().size()) {
								currentUpgradeIdentifierString = activeClassState.activeUpgradeIdentifierList().get(groupIndex);
							}

							boolean groupEntryListIsEmpty = upgradeEntryGroup.upgrade_entry_list().isEmpty();
							if (
									(!groupEntryListIsEmpty && allow_changing_upgrades && emptyUpgradeMode == ClassSelectionScreenHandler.EmptyUpgradeMode.NON_EMPTY_GROUPS) ||
											(groupEntryListIsEmpty && emptyUpgradeMode == ClassSelectionScreenHandler.EmptyUpgradeMode.EMPTY_GROUPS) ||
											((allow_changing_upgrades || groupEntryListIsEmpty) && emptyUpgradeMode == ClassSelectionScreenHandler.EmptyUpgradeMode.ALWAYS)
							) {
								upgradeEntryList.add(RPGClass.UpgradeEntryGroup.UpgradeEntry.DEFAULT);
								upgradeUnlockStatesList.add(true);
							}

							for (RPGClass.UpgradeEntryGroup.UpgradeEntry upgradeEntry : upgradeEntryGroup.upgrade_entry_list()) {

								boolean isUpgradeUnlocked = true;
								unlockAdvancementIdentifierString = upgradeEntry.unlock_advancement_identifier();
								if (!unlockAdvancementIdentifierString.isEmpty()) {

									AdvancementEntry unlockAdvancementEntry = serverAdvancementLoader.get(Identifier.of(unlockAdvancementIdentifierString));

									if (unlockAdvancementEntry != null) {
										isUpgradeUnlocked = playerAdvancementTracker.getProgress(unlockAdvancementEntry).isDone();
									}

								}
								if ((isUpgradeUnlocked || upgradeEntry.visible_when_locked()) && (allow_changing_upgrades || Objects.equals(upgradeEntry.upgrade_identifier(), currentUpgradeIdentifierString))) {
									upgradeEntryList.add(upgradeEntry);
									upgradeUnlockStatesList.add(isUpgradeUnlocked);
								}
							}

							upgradeEntryGroupList.add(
									new RPGClass.UpgradeEntryGroup(upgradeEntryList)
							);
							upgradeUnlockStateDataList.add(
									new ClassSelectionScreenHandler.ClassSelectionScreenData.ClassUnlockStateData.UpgradeUnlockStateData(upgradeUnlockStatesList)
							);

							groupIndex = groupIndex + 1;
						}

					}

					boolean isCurrentClass = entry.getKey().getValue().toString().equals(currentClassIdentifierString);
					boolean isInitialClass = entry.getKey().getValue().toString().equals(initial_class_identifier_string);

					if ((isClassUnlocked || rpgClass.visible_when_locked() || isCurrentClass)
							&& (isCurrentClass || allow_changing_class)
							&& (!restrict_class_list || isInitialClass || isCurrentClass || initial_class_identifier_string.isEmpty())
					) {

						classUnlockStateDataList.add(new ClassSelectionScreenHandler.ClassSelectionScreenData.ClassUnlockStateData(
								isClassUnlocked,
								upgradeUnlockStateDataList
						));
						rpgClassList.add(new RPGClass(
								rpgClass.class_identifier(),
								rpgClass.unlock_advancement_identifier(),
								rpgClass.class_item_identifier(),
								rpgClass.visible_when_locked(),
								rpgClass.description(),
								rpgClass.locked_description(),
								upgradeEntryGroupList
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
								rpgClassList
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
									rpgClassList
							);
						} else {
							return new ThreeUpgradesClassSelectionScreenHandler(
									syncId,
									playerInventory,
									finalInitialClassIndex,
									activeClassState,
									classUnlockStateDataList,
									rpgClassList
							);
						}
					}
				});
			}
		}
	}

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

		Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer(MOD_ID);
		if (modContainer.isPresent()) {
			ResourceManagerHelper.registerBuiltinResourcePack(identifier("more_rpg_series_classes"), modContainer.get(), Text.translatable("resourcepack.rpgclassselection.more_rpg_series_classes.name"), ResourcePackActivationType.DEFAULT_ENABLED);
			ResourceManagerHelper.registerBuiltinResourcePack(identifier("rpg_series_classes"), modContainer.get(), Text.translatable("resourcepack.rpgclassselection.rpg_series_classes.name"), ResourcePackActivationType.DEFAULT_ENABLED);
			ResourceManagerHelper.registerBuiltinResourcePack(identifier("compat_pack_resources"), modContainer.get(), Text.translatable("resourcepack.rpgclassselection.compat_pack_resources.name"), ResourcePackActivationType.DEFAULT_ENABLED);
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