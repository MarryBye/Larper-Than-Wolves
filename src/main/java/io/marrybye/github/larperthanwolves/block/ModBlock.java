package io.marrybye.github.larperthanwolves.block;

import io.marrybye.github.larperthanwolves.compat.IJeiDocumentationProvider;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

/**
 * Base class for mod blocks supporting declarative JEI documentation tabs.
 */
public class ModBlock extends Block implements IJeiDocumentationProvider {
    private final String infoTranslationKey;

    public ModBlock(Properties properties, String infoTranslationKey) {
        super(properties);
        this.infoTranslationKey = infoTranslationKey;
    }

    public ModBlock(Properties properties) {
        this(properties, null);
    }

    @Override
    public void registerJeiInfo(IRecipeRegistration registration) {
        if (infoTranslationKey != null && !infoTranslationKey.isEmpty()) {
            registration.addIngredientInfo(new ItemStack(this), VanillaTypes.ITEM_STACK,
                    Component.translatable(infoTranslationKey));
        }
    }
}
