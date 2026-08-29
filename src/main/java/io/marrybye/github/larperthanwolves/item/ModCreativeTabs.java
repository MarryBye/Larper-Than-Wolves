package io.marrybye.github.larperthanwolves.item;

import io.marrybye.github.larperthanwolves.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "larperthanwolves");

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> LARPERTHANWOLVES_TAB =
            CREATIVE_MODE_TABS.register("larperthanwolves_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.larperthanwolves"))
                    .icon(() -> new ItemStack(ModItems.SILICON_PICKAXE.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.SILICON_SHARD.get());
                        output.accept(ModItems.DRY_GRASS.get());
                        output.accept(ModItems.ROPE.get());
                        output.accept(ModItems.LIGHTER.get());

                        output.accept(ModItems.STONE_NUGGET.get());
                        output.accept(ModItems.DIORITE_NUGGET.get());
                        output.accept(ModItems.GRANITE_NUGGET.get());
                        output.accept(ModItems.ANDESITE_NUGGET.get());
                        output.accept(ModItems.TUFF_NUGGET.get());
                        output.accept(ModItems.CALCITE_NUGGET.get());
                        output.accept(ModItems.COPPER_NUGGET.get());

                        output.accept(ModItems.IRON_DUST.get());
                        output.accept(ModItems.COPPER_DUST.get());
                        output.accept(ModItems.TIN_DUST.get());
                        output.accept(ModItems.GOLD_DUST.get());
                        output.accept(ModItems.DIAMOND_DUST.get());
                        output.accept(ModItems.BRONZE_DUST.get());

                        output.accept(ModItems.RAW_TIN.get());
                        output.accept(ModItems.TIN_NUGGET.get());
                        output.accept(ModItems.TIN_INGOT.get());
                        output.accept(ModItems.BRONZE_NUGGET.get());
                        output.accept(ModItems.BRONZE_INGOT.get());

                        output.accept(ModItems.SILICON_SHEARS.get());
                        output.accept(ModItems.SILICON_SPEAR.get());
                        output.accept(ModItems.SILICON_AXE.get());
                        output.accept(ModItems.SILICON_PICKAXE.get());
                        output.accept(ModItems.SILICON_SHOVEL.get());

                        output.accept(ModItems.COPPER_SWORD.get());
                        output.accept(ModItems.COPPER_PICKAXE.get());
                        output.accept(ModItems.COPPER_AXE.get());
                        output.accept(ModItems.COPPER_SHOVEL.get());
                        output.accept(ModItems.COPPER_HOE.get());

                        output.accept(ModItems.COPPER_HELMET.get());
                        output.accept(ModItems.COPPER_CHESTPLATE.get());
                        output.accept(ModItems.COPPER_LEGGINGS.get());
                        output.accept(ModItems.COPPER_BOOTS.get());

                        output.accept(ModItems.BRONZE_SWORD.get());
                        output.accept(ModItems.BRONZE_PICKAXE.get());
                        output.accept(ModItems.BRONZE_AXE.get());
                        output.accept(ModItems.BRONZE_SHOVEL.get());
                        output.accept(ModItems.BRONZE_HOE.get());

                        output.accept(ModItems.BRONZE_HELMET.get());
                        output.accept(ModItems.BRONZE_CHESTPLATE.get());
                        output.accept(ModItems.BRONZE_LEGGINGS.get());
                        output.accept(ModItems.BRONZE_BOOTS.get());

                        output.accept(ModItems.TIN_ORE.get());
                        output.accept(ModItems.DEEPSLATE_TIN_ORE.get());
                        output.accept(ModItems.RAW_TIN_BLOCK.get());
                        output.accept(ModItems.TIN_BLOCK.get());
                        output.accept(ModItems.BRONZE_BLOCK.get());

                        output.accept(ModItems.BRICK_FURNACE.get());
                        output.accept(ModItems.UNFIRED_BRICK.get());
                        output.accept(ModItems.ALLOY_MIXER.get());
                        output.accept(ModItems.SIEVE.get());

                        output.accept(ModItems.MESH.get());
                        output.accept(ModItems.CHISEL.get());
                        output.accept(ModItems.DIAMOND_INGOT.get());

                        output.accept(ModItems.REINFORCED_IRON_SWORD.get());
                        output.accept(ModItems.REINFORCED_IRON_PICKAXE.get());
                        output.accept(ModItems.REINFORCED_IRON_AXE.get());
                        output.accept(ModItems.REINFORCED_IRON_SHOVEL.get());
                        output.accept(ModItems.REINFORCED_IRON_HOE.get());

                        output.accept(ModItems.REINFORCED_IRON_HELMET.get());
                        output.accept(ModItems.REINFORCED_IRON_CHESTPLATE.get());
                        output.accept(ModItems.REINFORCED_IRON_LEGGINGS.get());
                        output.accept(ModItems.REINFORCED_IRON_BOOTS.get());

                        output.accept(ModItems.OAK_STUMP.get());
                        output.accept(ModItems.BIRCH_STUMP.get());
                        output.accept(ModItems.SPRUCE_STUMP.get());
                        output.accept(ModItems.JUNGLE_STUMP.get());
                        output.accept(ModItems.ACACIA_STUMP.get());
                        output.accept(ModItems.DARK_OAK_STUMP.get());
                        output.accept(ModItems.MANGROVE_STUMP.get());
                        output.accept(ModItems.CHERRY_STUMP.get());
                        output.accept(ModItems.CRIMSON_STUMP.get());
                        output.accept(ModItems.WARPED_STUMP.get());
                    }).build());
}
