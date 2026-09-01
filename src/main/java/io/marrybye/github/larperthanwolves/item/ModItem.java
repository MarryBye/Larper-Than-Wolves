package io.marrybye.github.larperthanwolves.item;

import io.marrybye.github.larperthanwolves.compat.IJeiDocumentationProvider;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Base class for mod items supporting declarative JEI documentation tabs.
 */
public class ModItem extends Item implements IJeiDocumentationProvider {
    private final String infoTranslationKey;

    public ModItem(Properties properties, String infoTranslationKey) {
        super(properties);
        this.infoTranslationKey = infoTranslationKey;
    }

    public ModItem(Properties properties) {
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
