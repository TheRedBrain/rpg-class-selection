package com.github.theredbrain.rpgclassselection.screen;

import com.github.theredbrain.rpgclassselection.component.type.ClassStateComponent;
import com.github.theredbrain.rpgclassselection.data.RPGClass;
import com.github.theredbrain.rpgclassselection.registry.ScreenHandlerTypesRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.screen.ScreenHandler;

import java.util.ArrayList;
import java.util.List;

public class ClassSelectionScreenHandler extends ScreenHandler {

	private final String initialClassIdentifierString;
	private final ClassStateComponent.ActiveClassState activeClassState;
	private final List<ClassSelectionScreenData.ClassUnlockStateData> classUnlockStateDataList = new ArrayList<>(List.of());
	private final List<RPGClass> rpgClassList = new ArrayList<>();

	public ClassSelectionScreenHandler(int syncId, PlayerInventory playerInventory, ClassSelectionScreenData data) {
		this(syncId, playerInventory, data.initialClassIdentifierString, data.activeClassState, data.classUnlockStateDataList, data.rpgClassList);
	}

	public ClassSelectionScreenHandler(int syncId, PlayerInventory playerInventory, String initialClassIdentifierString, ClassStateComponent.ActiveClassState activeClassState, List<ClassSelectionScreenHandler.ClassSelectionScreenData.ClassUnlockStateData> classUnlockStateDataList, List<RPGClass> rpgClassList) {
		super(ScreenHandlerTypesRegistry.CLASS_SELECTION_SCREEN_HANDLER, syncId);
		this.initialClassIdentifierString = initialClassIdentifierString;
		this.activeClassState = activeClassState;
		this.classUnlockStateDataList.addAll(classUnlockStateDataList);
		this.rpgClassList.addAll(rpgClassList);
	}

	@Override
	public ItemStack quickMove(PlayerEntity player, int slot) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return true;
	}

	@Override
	public boolean onButtonClick(PlayerEntity player, int id) {
//		this.selectedClass.set(id);
		return true;
	}

	public String getInitialClassIdentifierString() {
		return this.initialClassIdentifierString;
	}

	public ClassStateComponent.ActiveClassState getActiveClassState() {
		return this.activeClassState;
	}

	public List<ClassSelectionScreenData.ClassUnlockStateData> getClassUnlockStateDataList() {
		return this.classUnlockStateDataList;
	}

	public List<RPGClass> getRpgClassList() {
		return this.rpgClassList;
	}

	public record ClassSelectionScreenData(
			String initialClassIdentifierString,
			// potential customization for the screen
			ClassStateComponent.ActiveClassState activeClassState,
			List<ClassUnlockStateData> classUnlockStateDataList,
			List<RPGClass> rpgClassList
	) {

		public static final PacketCodec<ByteBuf, ClassSelectionScreenData> PACKET_CODEC = new PacketCodec<>() {
			public ClassSelectionScreenData decode(ByteBuf byteBuf) {
				String initialClassIdentifierString = PacketCodecs.STRING.decode(byteBuf);
				ClassStateComponent.ActiveClassState activeClassState = ClassStateComponent.ActiveClassState.PACKET_CODEC.decode(byteBuf);
				int listSize = PacketCodecs.INTEGER.decode(byteBuf);
				List<ClassUnlockStateData> classUnlockStateDataList = new ArrayList<>();
				for (int i = 0; i < listSize; i++) {
					classUnlockStateDataList.add(ClassUnlockStateData.PACKET_CODEC.decode(byteBuf));
				}
				listSize = PacketCodecs.INTEGER.decode(byteBuf);
				List<RPGClass> rpgClassList = new ArrayList<>();
				for (int i = 0; i < listSize; i++) {
					rpgClassList.add(RPGClass.PACKET_CODEC.decode(byteBuf));
				}
				return new ClassSelectionScreenData(initialClassIdentifierString, activeClassState, classUnlockStateDataList, rpgClassList);
			}

			public void encode(ByteBuf byteBuf, ClassSelectionScreenData classSelectionScreenData) {
				PacketCodecs.STRING.encode(byteBuf, classSelectionScreenData.initialClassIdentifierString());
				ClassStateComponent.ActiveClassState.PACKET_CODEC.encode(byteBuf, classSelectionScreenData.activeClassState());
				int listSize = classSelectionScreenData.classUnlockStateDataList().size();
				PacketCodecs.INTEGER.encode(byteBuf, listSize);
				for (int i = 0; i < listSize; i++) {
					ClassUnlockStateData.PACKET_CODEC.encode(byteBuf, classSelectionScreenData.classUnlockStateDataList().get(i));
				}
				listSize = classSelectionScreenData.rpgClassList().size();
				PacketCodecs.INTEGER.encode(byteBuf, listSize);
				for (int i = 0; i < listSize; i++) {
					RPGClass.PACKET_CODEC.encode(byteBuf, classSelectionScreenData.rpgClassList().get(i));
				}
			}
		};

		public record ClassUnlockStateData(
				boolean classUnlockState,
				List<UpgradeUnlockStateData> upgradeUnlockStateDataList
		) {

			public static final PacketCodec<ByteBuf, ClassUnlockStateData> PACKET_CODEC = new PacketCodec<>() {
				public ClassUnlockStateData decode(ByteBuf byteBuf) {
					boolean classUnlockState = PacketCodecs.BOOL.decode(byteBuf);
					int listSize = PacketCodecs.INTEGER.decode(byteBuf);
					List<UpgradeUnlockStateData> upgradeUnlockStateDataList = new ArrayList<>();
					for (int i = 0; i < listSize; i++) {
						upgradeUnlockStateDataList.add(UpgradeUnlockStateData.PACKET_CODEC.decode(byteBuf));
					}
					return new ClassUnlockStateData(classUnlockState, upgradeUnlockStateDataList);
				}

				public void encode(ByteBuf byteBuf, ClassUnlockStateData classUnlockStateData) {
					PacketCodecs.BOOL.encode(byteBuf, classUnlockStateData.classUnlockState());
					int listSize = classUnlockStateData.upgradeUnlockStateDataList().size();
					PacketCodecs.INTEGER.encode(byteBuf, listSize);
					for (int i = 0; i < listSize; i++) {
						UpgradeUnlockStateData.PACKET_CODEC.encode(byteBuf, classUnlockStateData.upgradeUnlockStateDataList().get(i));
					}
				}
			};

			public record UpgradeUnlockStateData(
					boolean classUnlockState,
					List<Boolean> upgradeUnlockStatesList
			) {

				public static final PacketCodec<ByteBuf, UpgradeUnlockStateData> PACKET_CODEC = new PacketCodec<>() {
					public UpgradeUnlockStateData decode(ByteBuf byteBuf) {
						boolean classUnlockState = PacketCodecs.BOOL.decode(byteBuf);
						int listSize = PacketCodecs.INTEGER.decode(byteBuf);
						List<Boolean> upgradeUnlockStatesList = new ArrayList<>();
						for (int i = 0; i < listSize; i++) {
							upgradeUnlockStatesList.add(PacketCodecs.BOOL.decode(byteBuf));
						}
						return new UpgradeUnlockStateData(classUnlockState, upgradeUnlockStatesList);
					}

					public void encode(ByteBuf byteBuf, UpgradeUnlockStateData classUnlockStateData) {
						PacketCodecs.BOOL.encode(byteBuf, classUnlockStateData.classUnlockState());
						int listSize = classUnlockStateData.upgradeUnlockStatesList().size();
						PacketCodecs.INTEGER.encode(byteBuf, listSize);
						for (int i = 0; i < listSize; i++) {
							PacketCodecs.BOOL.encode(byteBuf, classUnlockStateData.upgradeUnlockStatesList().get(i));
						}
					}
				};

			}
		}
	}
}