package com.github.theredbrain.rpgclassselection.block.entity;

import com.github.theredbrain.rpgclassselection.registry.EntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;

public class RPGClassSelectionBlockEntity extends BlockEntity {
	private String initialClassIdentifierString = "";
	private boolean allowChangingClass = true;
	private boolean allowChangingUpgrades = true;

	public RPGClassSelectionBlockEntity(BlockPos pos, BlockState state) {
		super(EntityRegistry.RPG_CLASS_SELECTION_BLOCK_ENTITY, pos, state);
	}

	@Override
	protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {

		if (!this.initialClassIdentifierString.isEmpty()) {
			nbt.putString("initial_class_identifier_string", this.initialClassIdentifierString);
		}

		nbt.putBoolean("allow_changing_class", this.allowChangingClass);

		nbt.putBoolean("allow_changing_upgrades", this.allowChangingUpgrades);

		super.writeNbt(nbt, registryLookup);
	}

	@Override
	protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {

		if (nbt.contains("initial_class_identifier_string")) {
			this.initialClassIdentifierString = nbt.getString("initial_class_identifier_string");
		}

		this.allowChangingClass = nbt.getBoolean("allow_changing_class");

		this.allowChangingUpgrades = nbt.getBoolean("allow_changing_upgrades");

		super.readNbt(nbt, registryLookup);
	}

	public BlockEntityUpdateS2CPacket toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}

	@Override
	public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
		return this.createComponentlessNbt(registryLookup);
	}

	public String getInitialClassIdentifierString() {
		return this.initialClassIdentifierString;
	}

	public void setInitialClassIdentifierString(String initial_class_identifier_string) {
		this.initialClassIdentifierString = initial_class_identifier_string;
	}

	public boolean allowChangingClass() {
		return allowChangingClass;
	}

	public void setAllowChangingClass(boolean allowChangingClass) {
		this.allowChangingClass = allowChangingClass;
	}

	public boolean allowChangingUpgrades() {
		return allowChangingUpgrades;
	}

	public void setAllowChangingUpgrades(boolean allowChangingUpgrades) {
		this.allowChangingUpgrades = allowChangingUpgrades;
	}
}
