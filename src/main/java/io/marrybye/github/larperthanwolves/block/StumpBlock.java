package io.marrybye.github.larperthanwolves.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import java.util.function.Supplier;

import io.marrybye.github.larperthanwolves.compat.IJeiDocumentationProvider;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class StumpBlock extends RotatedPillarBlock implements IJeiDocumentationProvider {
    private final Supplier<Block> baseLog;

    public StumpBlock(Supplier<Block> baseLog, Properties properties) {
        super(properties);
        this.baseLog = baseLog;
    }

    public Block getBaseLog() {
        return baseLog.get();
    }

    @Override
    public void registerJeiInfo(IRecipeRegistration registration) {
        registration.addIngredientInfo(new ItemStack(this), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.larperthanwolves.info.stump"));
    }
}
