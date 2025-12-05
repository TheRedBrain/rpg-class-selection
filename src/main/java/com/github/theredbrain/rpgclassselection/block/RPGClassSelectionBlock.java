package com.github.theredbrain.rpgclassselection.block;

import com.github.theredbrain.rpgclassselection.RPGClassSelection;
import com.github.theredbrain.rpgclassselection.block.entity.RPGClassSelectionBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class RPGClassSelectionBlock extends BlockWithEntity {
	public static final MapCodec<RPGClassSelectionBlock> CODEC = createCodec(RPGClassSelectionBlock::new);

	public RPGClassSelectionBlock(AbstractBlock.Settings settings) {
		super(settings);
	}

	public MapCodec<RPGClassSelectionBlock> getCodec() {
		return CODEC;
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new RPGClassSelectionBlockEntity(pos, state);
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof RPGClassSelectionBlockEntity rpgClassSelectionBlockEntity) {
			if (player instanceof ServerPlayerEntity serverPlayerEntity) {
				RPGClassSelection.openRPGClassSelectionScreen(
						serverPlayerEntity,
						rpgClassSelectionBlockEntity.getInitialClassIdentifierString(),
						rpgClassSelectionBlockEntity.allowChangingClass(),
						rpgClassSelectionBlockEntity.allowChangingUpgrades()
				);
				return ActionResult.CONSUME;
			}
		}
		return ActionResult.PASS;
	}
}
