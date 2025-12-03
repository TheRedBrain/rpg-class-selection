package com.github.theredbrain.rpgclassselection.compat;

import com.github.theredbrain.rpginventory.component.type.ExtendedAttributeModifierSlot;
import com.github.theredbrain.rpginventory.entity.ExtendedEquipmentSlot;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class RPGInventoryCompat {

	public static AttributeModifierSlot getClassItemAttributeModifierSlot() {
		return ExtendedAttributeModifierSlot.RELIC;
	}

	public static ItemStack getClassItemStack(PlayerEntity player) {
		return player.getEquippedStack(ExtendedEquipmentSlot.RELIC);
	}

	public static void setClassItemStack(PlayerEntity player, ItemStack classItemStack) {
		player.equipStack(ExtendedEquipmentSlot.RELIC, classItemStack);
	}

}
