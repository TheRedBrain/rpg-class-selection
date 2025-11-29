package com.github.theredbrain.rpgclassselection.network.packet;

import com.github.theredbrain.rpgclassselection.RPGClassSelection;
import com.github.theredbrain.rpgclassselection.component.type.ClassStateComponent;
import com.github.theredbrain.rpgclassselection.data.RPGClass;
import com.github.theredbrain.rpgclassselection.screen.ClassSelectionScreenHandler;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class OpenClassSelectionScreenPacketReceiver implements ServerPlayNetworking.PlayPayloadHandler<OpenClassSelectionScreenPacket> {

	@Override
	public void receive(OpenClassSelectionScreenPacket payload, ServerPlayNetworking.Context context) {

		String initialClassIdentifierString = payload.initialClassIdentifierString(); // when not empty, defines the selected class

		ItemStack currentClassItemStack = RPGClassSelection.getClassItemStack(context.player());

		ClassStateComponent.ActiveClassState activeClassState = currentClassItemStack.getOrDefault(RPGClassSelection.CLASS_STATE_COMPONENT_TYPE, ClassStateComponent.DEFAULT).activeClassState();

		List<ClassSelectionScreenHandler.ClassSelectionScreenData.ClassUnlockStateData> classUnlockStateDataList = new ArrayList<>();

		List<RPGClass> rpgClassList = new ArrayList<>();

		// determine status of all classes and calculate index of initial class and current class
		// classes can define if they are invisible when locked, but they are still in the list

		context.player().openHandledScreen(new ExtendedScreenHandlerFactory<>() {
			@Override
			public ClassSelectionScreenHandler.ClassSelectionScreenData getScreenOpeningData(ServerPlayerEntity player) {
				return new ClassSelectionScreenHandler.ClassSelectionScreenData(
						initialClassIdentifierString,
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
						initialClassIdentifierString,
						activeClassState,
						classUnlockStateDataList,
						rpgClassList
				);
			}
		});
	}
}
