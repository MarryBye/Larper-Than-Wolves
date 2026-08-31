package io.marrybye.github.larperthanwolves.mixin;

import io.marrybye.github.larperthanwolves.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(StructureTemplate.class)
public class StructureTemplateMixin {

    @Inject(
            method = "processBlockInfos",
            at = @At("RETURN"),
            cancellable = true
    )
    private static void larperthanwolves$modifyStructureBlocks(
            ServerLevelAccessor level,
            BlockPos offset,
            BlockPos pos,
            StructurePlaceSettings settings,
            List<StructureTemplate.StructureBlockInfo> blockInfos,
            CallbackInfoReturnable<List<StructureTemplate.StructureBlockInfo>> cir
    ) {
        List<StructureTemplate.StructureBlockInfo> list = cir.getReturnValue();
        if (list == null || list.isEmpty()) {
            return;
        }

        List<StructureTemplate.StructureBlockInfo> modified = new ArrayList<>(list.size());

        for (StructureTemplate.StructureBlockInfo info : list) {
            BlockState state = info.state();
            CompoundTag nbt = info.nbt();

            // 1. Convert Furnaces & Blast Furnaces -> Brick Furnace (preserve horizontal facing)
            if (state.is(Blocks.FURNACE) || state.is(Blocks.BLAST_FURNACE)) {
                BlockState newState = ModBlocks.BRICK_FURNACE.get().defaultBlockState();
                if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING) && newState.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                    newState = newState.setValue(BlockStateProperties.HORIZONTAL_FACING, state.getValue(BlockStateProperties.HORIZONTAL_FACING));
                }
                modified.add(new StructureTemplate.StructureBlockInfo(info.pos(), newState, null));
                continue;
            }

            // 2. Convert Smokers -> Oven (preserve horizontal facing)
            if (state.is(Blocks.SMOKER)) {
                BlockState newState = ModBlocks.OVEN.get().defaultBlockState();
                if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING) && newState.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                    newState = newState.setValue(BlockStateProperties.HORIZONTAL_FACING, state.getValue(BlockStateProperties.HORIZONTAL_FACING));
                }
                modified.add(new StructureTemplate.StructureBlockInfo(info.pos(), newState, null));
                continue;
            }

            // 3. Remove Crafting Tables (replace with Oak Tree Stump so players must carve with a chisel)
            if (state.is(Blocks.CRAFTING_TABLE)) {
                modified.add(new StructureTemplate.StructureBlockInfo(info.pos(), ModBlocks.OAK_STUMP.get().defaultBlockState(), null));
                continue;
            }

            // 4. Worldgen Chests / Trapped Chests / Barrels (Non-Bastion) -> Strip LootTable & Items
            if (state.is(Blocks.CHEST) || state.is(Blocks.TRAPPED_CHEST) || state.is(Blocks.BARREL)) {
                boolean isBastion = false;
                if (nbt != null && nbt.contains("LootTable", Tag.TAG_STRING)) {
                    String lootTableStr = nbt.getString("LootTable");
                    if (lootTableStr.contains("bastion")) {
                        isBastion = true;
                    }
                }
                if (!isBastion) {
                    CompoundTag cleanNbt = nbt != null ? nbt.copy() : null;
                    if (cleanNbt != null) {
                        cleanNbt.remove("LootTable");
                        cleanNbt.remove("LootTableSeed");
                        cleanNbt.remove("Items");
                    }
                    modified.add(new StructureTemplate.StructureBlockInfo(info.pos(), state, cleanNbt));
                    continue;
                }
            }

            modified.add(info);
        }

        cir.setReturnValue(modified);
    }
}
