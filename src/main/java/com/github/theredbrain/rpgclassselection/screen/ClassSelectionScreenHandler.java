package com.github.theredbrain.rpgclassselection.screen;

import com.github.theredbrain.rpgclassselection.component.type.ClassStateComponent;
import com.github.theredbrain.rpgclassselection.data.DisplayedRPGClass;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class ClassSelectionScreenHandler extends ScreenHandler {

	private final int initialClassIndex;
	private final ClassStateComponent.ActiveClassState activeClassState;
	private final List<ClassSelectionScreenData.ClassUnlockStateData> classUnlockStateDataList = new ArrayList<>();
	private final List<DisplayedRPGClass> displayedRpgClassList = new ArrayList<>();
	private final PlayerEntity player;
	private final World world;

	public ClassSelectionScreenHandler(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, int initialClassIndex, ClassStateComponent.ActiveClassState activeClassState, List<ClassSelectionScreenHandler.ClassSelectionScreenData.ClassUnlockStateData> classUnlockStateDataList, List<DisplayedRPGClass> displayedRpgClassList) {
		super(type, syncId);
		this.player = playerInventory.player;
		this.world = playerInventory.player.getEntityWorld();
		this.initialClassIndex = initialClassIndex;
		this.activeClassState = activeClassState;
		this.classUnlockStateDataList.addAll(classUnlockStateDataList);
		this.displayedRpgClassList.addAll(displayedRpgClassList);
	}

	@Override
	public ItemStack quickMove(PlayerEntity player, int slot) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return true;
	}

	public int getInitialClassIndex() {
		return this.initialClassIndex;
	}

	public ClassStateComponent.ActiveClassState getActiveClassState() {
		return this.activeClassState;
	}

	public List<ClassSelectionScreenData.ClassUnlockStateData> getClassUnlockStateDataList() {
		return this.classUnlockStateDataList;
	}

	public List<DisplayedRPGClass> getDisplayedRpgClassList() {
		return this.displayedRpgClassList;
	}

	public PlayerEntity getPlayer() {
		return this.player;
	}

	public World getWorld() {
		return this.world;
	}

	public enum ClassSelectionScreenType implements StringIdentifiable {
		RPG_SERIES("rpg_series"),
		THREE_UPGRADES("three_upgrades");

		private final String name;

		ClassSelectionScreenType(String name) {
			this.name = name;
		}

		@Override
		public String asString() {
			return this.name;
		}

	}

	public enum EmptyUpgradeMode implements StringIdentifiable {
		ALWAYS("always"),
		EMPTY_GROUPS("empty_groups"),
		NON_EMPTY_GROUPS("non_empty_groups"),
		NEVER("never");

		private final String name;

		EmptyUpgradeMode(String name) {
			this.name = name;
		}

		@Override
		public String asString() {
			return this.name;
		}

	}

	public record ClassSelectionScreenData(
			int initialClassIndex,
			// potential customization for the screen
			ClassStateComponent.ActiveClassState activeClassState,
			List<ClassUnlockStateData> classUnlockStateDataList,
			List<DisplayedRPGClass> displayedRPGClassList
	) {

		public static final PacketCodec<ByteBuf, ClassSelectionScreenData> PACKET_CODEC = new PacketCodec<>() {
			public ClassSelectionScreenData decode(ByteBuf byteBuf) {
				int initialClassIndex = PacketCodecs.INTEGER.decode(byteBuf);
				ClassStateComponent.ActiveClassState activeClassState = ClassStateComponent.ActiveClassState.PACKET_CODEC.decode(byteBuf);
				int listSize = PacketCodecs.INTEGER.decode(byteBuf);
				List<ClassUnlockStateData> classUnlockStateDataList = new ArrayList<>();
				for (int i = 0; i < listSize; i++) {
					classUnlockStateDataList.add(ClassUnlockStateData.PACKET_CODEC.decode(byteBuf));
				}
				listSize = PacketCodecs.INTEGER.decode(byteBuf);
				List<DisplayedRPGClass> rpgClassList = new ArrayList<>();
				for (int i = 0; i < listSize; i++) {
					rpgClassList.add(DisplayedRPGClass.PACKET_CODEC.decode(byteBuf));
				}
				return new ClassSelectionScreenData(initialClassIndex, activeClassState, classUnlockStateDataList, rpgClassList);
			}

			public void encode(ByteBuf byteBuf, ClassSelectionScreenData classSelectionScreenData) {
				PacketCodecs.INTEGER.encode(byteBuf, classSelectionScreenData.initialClassIndex());
				ClassStateComponent.ActiveClassState.PACKET_CODEC.encode(byteBuf, classSelectionScreenData.activeClassState());
				int listSize = classSelectionScreenData.classUnlockStateDataList().size();
				PacketCodecs.INTEGER.encode(byteBuf, listSize);
				for (int i = 0; i < listSize; i++) {
					ClassUnlockStateData.PACKET_CODEC.encode(byteBuf, classSelectionScreenData.classUnlockStateDataList().get(i));
				}
				listSize = classSelectionScreenData.displayedRPGClassList().size();
				PacketCodecs.INTEGER.encode(byteBuf, listSize);
				for (int i = 0; i < listSize; i++) {
					DisplayedRPGClass.PACKET_CODEC.encode(byteBuf, classSelectionScreenData.displayedRPGClassList().get(i));
				}
			}
		};

		public record ClassUnlockStateData(
				String unlockStateString,
				List<UpgradeUnlockStateData> upgradeUnlockStateDataList
		) {

			public static final PacketCodec<ByteBuf, ClassUnlockStateData> PACKET_CODEC = new PacketCodec<>() {
				public ClassUnlockStateData decode(ByteBuf byteBuf) {
					String unlockStateString = PacketCodecs.STRING.decode(byteBuf);
					int listSize = PacketCodecs.INTEGER.decode(byteBuf);
					List<UpgradeUnlockStateData> upgradeUnlockStateDataList = new ArrayList<>();
					for (int i = 0; i < listSize; i++) {
						upgradeUnlockStateDataList.add(UpgradeUnlockStateData.PACKET_CODEC.decode(byteBuf));
					}
					return new ClassUnlockStateData(unlockStateString, upgradeUnlockStateDataList);
				}

				public void encode(ByteBuf byteBuf, ClassUnlockStateData classUnlockStateData) {
					PacketCodecs.STRING.encode(byteBuf, classUnlockStateData.unlockStateString());
					int listSize = classUnlockStateData.upgradeUnlockStateDataList().size();
					PacketCodecs.INTEGER.encode(byteBuf, listSize);
					for (int i = 0; i < listSize; i++) {
						UpgradeUnlockStateData.PACKET_CODEC.encode(byteBuf, classUnlockStateData.upgradeUnlockStateDataList().get(i));
					}
				}
			};

			public record UpgradeUnlockStateData(
					List<Boolean> upgradeUnlockStatesList
			) {

				public static final PacketCodec<ByteBuf, UpgradeUnlockStateData> PACKET_CODEC = new PacketCodec<>() {
					public UpgradeUnlockStateData decode(ByteBuf byteBuf) {
						int listSize = PacketCodecs.INTEGER.decode(byteBuf);
						List<Boolean> upgradeUnlockStatesList = new ArrayList<>();
						for (int i = 0; i < listSize; i++) {
							upgradeUnlockStatesList.add(PacketCodecs.BOOL.decode(byteBuf));
						}
						return new UpgradeUnlockStateData(upgradeUnlockStatesList);
					}

					public void encode(ByteBuf byteBuf, UpgradeUnlockStateData classUnlockStateData) {
						int listSize = classUnlockStateData.upgradeUnlockStatesList().size();
						PacketCodecs.INTEGER.encode(byteBuf, listSize);
						for (int i = 0; i < listSize; i++) {
							PacketCodecs.BOOL.encode(byteBuf, classUnlockStateData.upgradeUnlockStatesList().get(i));
						}
					}
				};

			}
		}

		public static enum UnlockState implements StringIdentifiable {
			UNLOCKED("unlocked"),
			DISPLAY("display"),
			LOCKED("locked");

			private final String name;

			private UnlockState(String name) {
				this.name = name;
			}

			@Override
			public String asString() {
				return this.name;
			}

			public static Optional<UnlockState> byName(String name) {
				return Arrays.stream(UnlockState.values()).filter(unlockState -> unlockState.asString().equals(name)).findFirst();
			}

		}
	}
}