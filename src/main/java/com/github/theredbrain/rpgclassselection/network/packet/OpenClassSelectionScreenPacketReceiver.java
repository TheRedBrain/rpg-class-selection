package com.github.theredbrain.rpgclassselection.network.packet;

import com.github.theredbrain.rpgclassselection.RPGClassSelection;
import com.github.theredbrain.rpgclassselection.component.type.ClassStateComponent;
import com.github.theredbrain.rpgclassselection.data.RPGClass;
import com.github.theredbrain.rpgclassselection.registry.CustomDynamicRegistries;
import com.github.theredbrain.rpgclassselection.screen.ClassSelectionScreenHandler;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.PlayerAdvancementTracker;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OpenClassSelectionScreenPacketReceiver implements ServerPlayNetworking.PlayPayloadHandler<OpenClassSelectionScreenPacket> {

	@Override
	public void receive(OpenClassSelectionScreenPacket payload, ServerPlayNetworking.Context context) {

		ServerPlayerEntity player = context.player();
		MinecraftServer server = player.server;

		if (server != null) {

			PlayerAdvancementTracker playerAdvancementTracker = player.getAdvancementTracker();
			ServerAdvancementLoader serverAdvancementLoader = server.getAdvancementLoader();

			if (serverAdvancementLoader != null && playerAdvancementTracker != null) {
				String initialClassIdentifierString = payload.initialClassIdentifierString(); // when not empty, defines the selected class

				ItemStack currentClassItemStack = RPGClassSelection.getClassItemStack(context.player());

				ClassStateComponent.ActiveClassState activeClassState = currentClassItemStack.getOrDefault(RPGClassSelection.CLASS_STATE_COMPONENT_TYPE, ClassStateComponent.DEFAULT).activeClassState();

				if (initialClassIdentifierString.isEmpty()) {
					initialClassIdentifierString = activeClassState.activeClassIdentifier();
				}

				List<ClassSelectionScreenHandler.ClassSelectionScreenData.ClassUnlockStateData> classUnlockStateDataList = new ArrayList<>();

				List<RPGClass> rpgClassList = new ArrayList<>();

				// add empty class
				classUnlockStateDataList.add(new ClassSelectionScreenHandler.ClassSelectionScreenData.ClassUnlockStateData(
						true,
						new ArrayList<>()
				));
				rpgClassList.add(RPGClass.DEFAULT);

				int initialClassIndex = 0;
				int index = 0;

				for (Map.Entry<RegistryKey<RPGClass>, RPGClass> entry : context.player().getWorld().getRegistryManager().get(CustomDynamicRegistries.RPG_CLASS_REGISTRY_KEY).getEntrySet()) {
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

						for (RPGClass.UpgradeEntryGroup upgradeEntryGroup : rpgClass.upgrade_entry_group_list()) {
							List<RPGClass.UpgradeEntryGroup.UpgradeEntry> upgradeEntryList = new ArrayList<>();
							List<Boolean> upgradeUnlockStatesList = new ArrayList<>();

							upgradeEntryList.add(RPGClass.UpgradeEntryGroup.UpgradeEntry.DEFAULT);
							upgradeUnlockStatesList.add(true);

							for (RPGClass.UpgradeEntryGroup.UpgradeEntry upgradeEntry : upgradeEntryGroup.upgrade_entry_list()) {

								boolean isUpgradeUnlocked = true;
								unlockAdvancementIdentifierString = upgradeEntry.unlock_advancement_identifier();
								if (!unlockAdvancementIdentifierString.isEmpty()) {

									AdvancementEntry unlockAdvancementEntry = serverAdvancementLoader.get(Identifier.of(unlockAdvancementIdentifierString));

									if (unlockAdvancementEntry != null) {
										isUpgradeUnlocked = playerAdvancementTracker.getProgress(unlockAdvancementEntry).isDone();
									}

								}
								upgradeEntryList.add(upgradeEntry);
								upgradeUnlockStatesList.add(isUpgradeUnlocked);
							}
							upgradeEntryGroupList.add(
									new RPGClass.UpgradeEntryGroup(upgradeEntryList)
							);
							upgradeUnlockStateDataList.add(
									new ClassSelectionScreenHandler.ClassSelectionScreenData.ClassUnlockStateData.UpgradeUnlockStateData(upgradeUnlockStatesList)
							);
						}

					}

					if (entry.getKey().getValue().toString().equals(initialClassIdentifierString)) {
						initialClassIndex = index + 1;
					}

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

					index++;

				}

				int finalInitialClassIndex = initialClassIndex;
				context.player().openHandledScreen(new ExtendedScreenHandlerFactory<>() {
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
						return new ClassSelectionScreenHandler(
								syncId,
								playerInventory,
								finalInitialClassIndex,
								activeClassState,
								classUnlockStateDataList,
								rpgClassList
						);
					}
				});
			}
		}
	}
}
