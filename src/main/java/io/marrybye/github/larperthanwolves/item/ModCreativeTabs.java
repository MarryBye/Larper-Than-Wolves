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
                        output.accept(ModItems.COPPER_NUGGET.get());

                        output.accept(ModItems.IRON_DUST.get());
                        output.accept(ModItems.COPPER_DUST.get());
                        output.accept(ModItems.GOLD_DUST.get());

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

                        output.accept(ModItems.BRICK_FURNACE.get());
                        output.accept(ModItems.UNFIRED_BRICK.get());
                    }).build());
}
